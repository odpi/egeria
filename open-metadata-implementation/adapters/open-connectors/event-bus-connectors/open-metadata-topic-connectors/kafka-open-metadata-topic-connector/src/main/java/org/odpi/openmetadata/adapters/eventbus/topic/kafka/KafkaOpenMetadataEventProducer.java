/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.eventbus.topic.kafka;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.errors.RetriableException;
import org.apache.kafka.common.errors.WakeupException;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectorCheckedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * KafkaOpenMetadataEventProducer manages the sending of events on Apache Kafka.  This is done through called to
 * the Kafka Producer interface.
 *
 * Kafka is not always running.  When this occurs, the call to publish events hangs and this is disruptive to the
 * rest of the server.  So the role of this class is to manage the sending of events in a separate thread
 * and manage the logging of errors to alert the operations team that Kafka needs restarting.
 */
public class KafkaOpenMetadataEventProducer implements Runnable
{
    private final List<String> sendBuffer = Collections.synchronizedList(new ArrayList<>());

    private static final Logger log = LoggerFactory.getLogger(KafkaOpenMetadataEventProducer.class);

    private static final String       defaultThreadName = "KafkaProducer for topic ";

    private volatile boolean running = true;

    private final AuditLog auditLog;
    private final String   listenerThreadName;
    private final String   topicName;
    private final int sleepTime = 1000;
    private static final long recoverySleepTimeSec = 10L;

    private final String                          localServerId;
    private final Properties                      producerProperties;
    private Producer<String, String>        producer = null;

    private KafkaOpenMetadataTopicConnector connector;

    private long    messageSendCount = 0;


    /**
     *
     * Constructor for the event consumer.
     *
     * @param topicName name of the topic to listen on.
     * @param localServerId identifier to enable receiver to identify that an event came from this server.
     * @param producerProperties properties for the consumer.
     * @param connector connector holding the inbound listeners.
     * @param auditLog  audit log for this component.
     */
    KafkaOpenMetadataEventProducer(String                          topicName,
                                   String                          localServerId,
                                   Properties                      producerProperties,
                                   KafkaOpenMetadataTopicConnector connector,
                                   AuditLog                        auditLog)
    {
        this.auditLog = auditLog;
        this.topicName = topicName;
        this.localServerId = localServerId;
        this.connector = connector;
        this.producerProperties = producerProperties;
        this.listenerThreadName = defaultThreadName + topicName;

        final String           actionDescription = "new producer";

        auditLog.logMessage(actionDescription,
                            KafkaOpenMetadataTopicConnectorAuditCode.SERVICE_PRODUCER_PROPERTIES.getMessageDefinition(
                                    Integer.toString(producerProperties.size()), topicName),
                           producerProperties.toString());
    }



    /**
     * Sends the supplied event to the topic.  It retries if Kafka is not responding.
     *
     * @param event object containing the event properties.
     * @throws ConnectorCheckedException the connector is not able to communicate with the event bus
     */
    private void publishEvent(String event) throws ConnectorCheckedException
    {
        final String methodName = "publishEvent";

        boolean                  eventSent = false;
        long                     eventRetryCount = 0;

        if (producer == null)
        {
            log.debug("Creating Producer");
            producer = new KafkaProducer<>(producerProperties);
        }
        while (!eventSent)
        {
            try
            {
                log.debug("Sending message {0}" + event);
                ProducerRecord<String, String> record = new ProducerRecord<>(topicName, localServerId, event);
                producer.send(record).get();
                eventSent = true;
                messageSendCount++;
            }
            catch (ExecutionException error)
            {
                /*
                 * This may be a simple timeout or something else more
                 */
                log.debug("Kafka had trouble sending event: " + event + "exception message is " + error.getMessage());

                if (!isExceptionRetryable(error))
                {
                    /* kafka thinks this isn't a retryable problem */
                    /* so let the caller try */

                    producer.close();
                    producer = null;

                    throw new ConnectorCheckedException(KafkaOpenMetadataTopicConnectorErrorCode.ERROR_SENDING_EVENT.getMessageDefinition(error.getClass().getName(),
                                                                                                                                          topicName,
                                                                                                                                          error.getMessage()),
                                                                                                                                          this.getClass().getName(),
                                                                                                                                          methodName,
                                                                                                                                          error);
                }
                if (eventRetryCount == 10)
                {
                    /* we've retried now let the caller retry */
                    producer.close();
                    producer = null;
                    log.error("Retryable Exception closed producer ");
                    break;
                }
                else
                {
                    if (eventRetryCount == 0)
                    {
                        auditLog.logMessage(methodName,
                                            KafkaOpenMetadataTopicConnectorAuditCode.EVENT_SEND_IN_ERROR_LOOP.getMessageDefinition(topicName,
                                                                                                                                   Long.toString(messageSendCount),
                                                                                                                                   Long.toString(this.getSendBufferSize()),
                                                                                                                                   error.getMessage()));
                    }

                    eventRetryCount++;
                }
            }
            catch (WakeupException error)
            {
                log.error("Wake up for shut down " + error.toString());
            }
            catch (Throwable error)
            {
                producer.close();
                producer = null;
                log.debug("Send Events Throwable catch block closed producer");
                log.error("Exception in sendEvent " + error.toString());

                throw new ConnectorCheckedException(KafkaOpenMetadataTopicConnectorErrorCode.ERROR_SENDING_EVENT.getMessageDefinition(error.getClass().getName(),
                                                                                                                                      topicName,
                                                                                                                                      error.getMessage()),
                                                    this.getClass().getName(),
                                                    methodName,
                                                    error);
            }
        }

    }


    /**
     * This is the method that provides the behaviour of the thread.
     */
    @Override
    public void run()
    {
        final String           actionDescription = listenerThreadName + ":run";

        auditLog.logMessage(actionDescription,
                            KafkaOpenMetadataTopicConnectorAuditCode.KAFKA_PRODUCER_START.getMessageDefinition(topicName,
                                                                                                               String.valueOf(sendBuffer.size())),
                            this.producerProperties.toString());


        while (isRunning())
        {
            try
            {
                String bufferedEvent = this.getEvent();

                /*
                 * If there are no events waiting then sleep
                 */
                if (bufferedEvent == null)
                {
                    TimeUnit.MILLISECONDS.sleep(sleepTime);
                }
                else
                {
                    /*
                     * Send all waiting events
                     */
                    while (bufferedEvent != null)
                    {
                        publishEvent(bufferedEvent);
                        bufferedEvent = this.getEvent();
                    }
                }
            }
            catch (InterruptedException   error)
            {
                log.info("Woken up from sleep " + error.getMessage());
            }
            catch (Throwable   error)
            {
                log.error("Bad exception from sending events " + error.getMessage());

                if( isExceptionRetryable(error) ) {
                    this.recoverAfterError();
                }
                else {

                    /* This is an unrecoverable error so clean up and shutdown*/
                    break;
                }
            }
        }

        /* producer may have already closed by exception handler in publishEvent */
        if(producer != null) {
            log.debug("");
            producer.close();
            producer = null;
        }

        auditLog.logMessage(actionDescription,
                            KafkaOpenMetadataTopicConnectorAuditCode.KAFKA_PRODUCER_SHUTDOWN.getMessageDefinition(topicName,
                                                                                                                  Integer.toString(getSendBufferSize()),
                                                                                                                  Long.toString(messageSendCount)),
                           this.producerProperties.toString());
    }


    /**
     * Supports putting events to the in memory OMRS Topic
     *
     * @param newEvent  event to publish
     */
    private void putEvent(String  newEvent)
    {
        sendBuffer.add(newEvent);
    }


    /**
     * Returns the size of the send buffer
     *
     * @return int
     */
    private int getSendBufferSize()
    {
        return sendBuffer.size();
    }


    /**
     * Returns all of the events found on the in memory OMRS Topic.
     *
     * @return list of received events.
     */
    private String getEvent()
    {
        if (sendBuffer.isEmpty())
        {
            return null;
        }

        return sendBuffer.remove(0);
    }


    /**
     * Sends the supplied event to the topic.
     *
     * @param event  OMRSEvent object containing the event properties.
     */
    public void sendEvent(String event)
    {
        this.putEvent(event);
    }


    /**
     * Give time for an error to clear.
     */
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
    public void safeCloseProducer()
    {
        stopRunning();
    }


    /**
     * Should the thread keep looping.
     *
     * @return boolean
     */
    private boolean isRunning()
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

    private boolean isExceptionRetryable( Throwable throwable)
    {

        Throwable nested = null;
        while ((nested = throwable.getCause()) != null) {
             if( nested instanceof RetriableException) {
                 return true;
             }
           throwable = throwable.getCause();
       }
        return false;
    }
}
