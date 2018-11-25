/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.eventbus.topic.kafka;

import java.util.*;

import org.apache.kafka.clients.consumer.ConsumerRebalanceListener;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.consumer.OffsetAndMetadata;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.errors.WakeupException;
import org.odpi.openmetadata.repositoryservices.auditlog.OMRSAuditLog;
import org.odpi.openmetadata.repositoryservices.auditlog.OMRSAuditingComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



/**
 * KafkaOpenMetadataEventConsumer is used to process events from kafka topic and is part of native
 * Apache Kafka event/messaging infrastructure.
 */
public class KafkaOpenMetadataEventConsumer implements Runnable
{
    private static final Logger       log      = LoggerFactory.getLogger(KafkaOpenMetadataEventConsumer.class);

    private OMRSAuditLog auditLog;

    private static final long recoverySleepTimeSec = 10L;
    private static final long defaultPollTimeout   = 1000;

    private              KafkaConsumer<String, String>   consumer;
    private              String                          topicToSubscribe;
    private              String                          localServerId;

    private              KafkaOpenMetadataTopicConnector connector;

    private Map<TopicPartition, OffsetAndMetadata> currentOffsets = new HashMap<>();

    private Boolean running = true;

    /**
     * Constructor for the event consumer.
     *
     * @param topicName name of the topic to listen on.
     * @param localServerId identifier to enable receiver to identify that an event came from this server.
     * @param consumerProperties properties for the consumer.
     * @param connector connector holding the inbound listeners.
     * @param auditLog  audit log for this component.
     */
    public KafkaOpenMetadataEventConsumer(String                          topicName,
                                          String                          localServerId,
                                          Properties                      consumerProperties,
                                          KafkaOpenMetadataTopicConnector connector,
                                          OMRSAuditLog                    auditLog)
    {
        this.auditLog = auditLog;
        this.consumer = new KafkaConsumer<>(consumerProperties);
        this.topicToSubscribe = topicName;
        this.consumer.subscribe(Collections.singletonList(topicToSubscribe), new HandleRebalance());
        this.connector = connector;
        this.localServerId = localServerId;

        final String           actionDescription = "initialize";
        KafkaOpenMetadataTopicConnectorAuditCode auditCode;

        auditCode = KafkaOpenMetadataTopicConnectorAuditCode.SERVICE_CONSUMER_PROPERTIES;
        auditLog.logRecord(actionDescription,
                           auditCode.getLogMessageId(),
                           auditCode.getSeverity(),
                           auditCode.getFormattedLogMessage(topicName, consumerProperties.toString()),
                           null,
                           auditCode.getSystemAction(),
                           auditCode.getUserAction());
    }


    /**
     * The server is shutting down.
     */
    public void stop()
    {
        running = false;
        if (consumer != null)
        {
            consumer.wakeup();
        }
    }


    /**
     * This is the method that provides the behaviour of the thread.
     */
    @Override
    public void run()
    {
        final String           actionDescription = "run";
        KafkaOpenMetadataTopicConnectorAuditCode auditCode;

        while (isRunning())
        {
            try
            {
                ConsumerRecords<String, String> records = consumer.poll(defaultPollTimeout);

                log.debug("Found records: " + records.count());
                for (ConsumerRecord<String, String> record : records)
                {
                    String json = record.value();
                    log.debug("Received message: " + json);

                    if (! localServerId.equals(record.key()))
                    {
                        try
                        {
                            connector.distributeToListeners(json);
                        }
                        catch (Exception error)
                        {
                            log.error(String.format("Error distributing inbound event: %s", error.getMessage()), error);

                            if (auditLog != null)
                            {
                                auditCode = KafkaOpenMetadataTopicConnectorAuditCode.EXCEPTION_DISTRIBUTING_EVENT;
                                auditLog.logRecord(actionDescription,
                                                   auditCode.getLogMessageId(),
                                                   auditCode.getSeverity(),
                                                   auditCode.getFormattedLogMessage(topicToSubscribe,
                                                                                    error.getClass().getName(), json,
                                                                                    error.getMessage()),
                                                   null,
                                                   auditCode.getSystemAction(),
                                                   auditCode.getUserAction());
                            }
                        }
                    }
                    else
                    {
                        log.debug("Ignoring message with key: " + record.key() + " and value " + record.value());
                    }

                    /*
                     * Acknowledge receipt of message.
                     */
                    TopicPartition partition = new TopicPartition(record.topic(), record.partition());
                    currentOffsets.put(partition, new OffsetAndMetadata(record.offset() + 1));
                }
            }
            catch (WakeupException e)
            {
                log.debug("Received wakeup call, proceeding with graceful shutdown", e);
            }
            catch (Exception error)
            {
                log.error(String.format("Unexpected error: %s", error.getMessage()), error);

                if (auditLog != null)
                {
                    auditCode = KafkaOpenMetadataTopicConnectorAuditCode.EXCEPTION_RECEIVING_EVENT;
                    auditLog.logRecord(actionDescription,
                                       auditCode.getLogMessageId(),
                                       auditCode.getSeverity(),
                                       auditCode.getFormattedLogMessage(topicToSubscribe, error.getClass().getName(),
                                                                        error.getMessage()),
                                       null,
                                       auditCode.getSystemAction(),
                                       auditCode.getUserAction());
                }
                recoverAfterError();
            }
            finally
            {
                try
                {
                    Thread.sleep(1000);
                }
                catch (InterruptedException e)
                {
                    log.error(String.format("Interruption error: %s", e.getMessage()), e);
                }
            }
        }

        if (consumer != null)
        {
            try
            {
                consumer.commitSync(currentOffsets);
            }
            finally
            {
                consumer.close();
            }
            consumer = null;
        }
    }


    protected void recoverAfterError()
    {
        log.info(String.format("Waiting %s seconds to recover", recoverySleepTimeSec));

        try
        {
            Thread.sleep(recoverySleepTimeSec * 1000L);
        }
        catch (InterruptedException e1)
        {
            log.debug("Interrupted while recovering", e1);
        }
    }


    /**
     * Normal shutdown
     */
    public void safeCloseConsumer()
    {
        stopRunning();

        /*
         * Wake the thread up so it shuts down quicker.
         */
        if (consumer != null)
        {
            consumer.wakeup();
        }
    }


    /**
     * Should the thread keep looping.
     *
     * @return boolean
     */
    private synchronized  boolean isRunning()
    {
        return running;
    }


    /**
     * Flip the switch to stop the thread.
     */
    private synchronized void stopRunning()
    {
        running = false;
    }


    private class HandleRebalance implements ConsumerRebalanceListener
    {
        public void onPartitionsAssigned(Collection<TopicPartition> partitions)
        {
        }

        public void onPartitionsRevoked(Collection<TopicPartition> partitions)
        {
            log.info("Lost partitions in rebalance. Committing current offsets:" + currentOffsets);
            consumer.commitSync(currentOffsets);
        }
    }
}
