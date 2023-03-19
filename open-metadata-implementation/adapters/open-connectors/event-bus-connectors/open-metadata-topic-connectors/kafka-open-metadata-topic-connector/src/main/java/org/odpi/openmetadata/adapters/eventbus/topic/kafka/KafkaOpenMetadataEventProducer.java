/* SPDX-License-Identifier: Apache-2.0 */
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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * KafkaOpenMetadataEventProducer manages the sending of events on Apache Kafka.  This is done through called to
 * the Kafka Producer interface.
 * <p>
 * Kafka is not always running.  When this occurs, the call to publish events hangs and this is disruptive to the
 * rest of the server.  So the role of this class is to manage the sending of events in a separate thread
 * and manage the logging of errors to alert the operations team that Kafka needs restarting.
 */
public class KafkaOpenMetadataEventProducer implements Runnable {
    private static final Logger log = LoggerFactory.getLogger(KafkaOpenMetadataEventProducer.class);
    private final List<String> sendBuffer = Collections.synchronizedList(new ArrayList<>());
    private final AuditLog auditLog;
    private final String topicName;
    private final String localServerId;
    private final Properties producerProperties;
    private volatile boolean running = true;
    private Producer<String, String> producer = null;
    private long messageSendCount = 0;
    private long kafkaSendAttemptCount = 0;
    private long messagePublishRequestCount = 0;
    private long inmemoryPutMessageCount = 0;
    private long kafkaSendFailCount = 0;
    private long messageFailedSendCount = 0;


    /**
     * Constructor for the event producer.
     *
     * @param topicName          name of the topic to listen on.
     * @param localServerId      identifier to enable receiver to identify that an event came from this server.
     * @param producerProperties properties for the consumer.
     * @param auditLog           audit log for this component.
     */
    KafkaOpenMetadataEventProducer(String topicName, String localServerId, Properties producerProperties,
                                   AuditLog auditLog) {
        this.auditLog = auditLog;
        this.topicName = topicName;
        this.localServerId = localServerId;
        this.producerProperties = producerProperties;

        final String           actionDescription = "new producer";

        if (auditLog != null) {
            auditLog.logMessage(actionDescription,
                                KafkaOpenMetadataTopicConnectorAuditCode.SERVICE_PRODUCER_PROPERTIES.getMessageDefinition(
                                        Integer.toString(producerProperties.size()), topicName),
                                producerProperties.toString());
        }
    }


    /**
     * Sends the supplied event to the topic.  It retries if Kafka is not responding.
     *
     * @param event object containing the event properties.
     * @throws ConnectorCheckedException the connector is not able to communicate with the event bus
     */
    private void publishEvent(String event) throws ConnectorCheckedException {
        final String methodName = "publishEvent";
        final String messageFailedCountString = "Metrics: messageFailedSendCount {}";

        boolean eventSent = false;
        long eventRetryCount = 0;

        messagePublishRequestCount++;
        log.debug("Metrics: messagePublishRequestCount {}", messagePublishRequestCount);

        if (producer == null) {
            try {
                log.debug("Creating new producer for topic {}", topicName);
                producer = new KafkaProducer<>(producerProperties);
            } catch (Exception error) {
                if (auditLog != null) {
                    auditLog.logException(methodName,
                                          KafkaOpenMetadataTopicConnectorAuditCode.ERROR_CONNECTING_KAFKA_PRODUCER.getMessageDefinition(
                                                  topicName), error);
                }

                throw new ConnectorCheckedException(
                        KafkaOpenMetadataTopicConnectorErrorCode.ERROR_CONNECTING_KAFKA_PRODUCER.getMessageDefinition(
                                error.getMessage()), this.getClass().getName(), methodName, error);
            }
        }
        while (!eventSent) {
            try {
                log.debug("Sending message try {} [0 based] : {}", eventRetryCount,event);
                ProducerRecord<String, String> producerRecord = new ProducerRecord<>(topicName, localServerId, event);
                kafkaSendAttemptCount++;
                log.debug("Metrics: kafkaSendAttemptCount {}", kafkaSendAttemptCount);
                producer.send(producerRecord).get();
                eventSent = true;
                messageSendCount++;
                log.debug("Metrics: messageSendCount {}", messageSendCount);
            } catch (ExecutionException error) {
                kafkaSendFailCount++;
                log.debug("Metrics: kafkaSendFailCount {}", kafkaSendFailCount);
                /*
                 * This may be a simple timeout or something else more
                 */
                log.debug("Kafka had trouble sending event: {} : Exception  message is {}", event, error.getMessage());

                if (!isExceptionRetryable(error)) {
                    /* kafka thinks this isn't a retryable problem */
                    /* so let the caller try */

                    log.debug("Exception not retryable, closing producer");
                    producer.close();
                    producer = null;

                    messageFailedSendCount++;
                    log.warn(messageFailedCountString, messageFailedSendCount);

                    throw new ConnectorCheckedException(
                            KafkaOpenMetadataTopicConnectorErrorCode.ERROR_SENDING_EVENT.getMessageDefinition(
                                    error.getClass().getName(), topicName, error.getMessage()),
                            this.getClass().getName(), methodName, error);
                }
                if (eventRetryCount == 10) {
                    /* we've retried now let the caller retry */
                    producer.close();
                    producer = null;
                    messageFailedSendCount++;
                    log.warn(messageFailedCountString, messageFailedSendCount);
                    log.error("Retryable Exception closed producer after {} tries", eventRetryCount);
                    break;
                } else {
                    if (eventRetryCount == 0) {
                        log.debug("Retrying event warning - count is {}", eventRetryCount);
                        if (auditLog != null) {
                            auditLog.logMessage(methodName,
                                                KafkaOpenMetadataTopicConnectorAuditCode.EVENT_SEND_IN_ERROR_LOOP.getMessageDefinition(
                                                        topicName, Long.toString(messageSendCount),
                                                        Long.toString(this.getSendBufferSize()), error.getMessage()));
                        }
                    }

                    eventRetryCount++;
                }
            } catch (WakeupException error) {
                log.warn("Wake up for shut down");
            } catch (Exception error) {
                if (producer!=null) {
                    producer.close();
                    producer = null;
                }
                log.warn("Closed producer due to Exception in sendEvent {}",error.getMessage());

                if (error instanceof InterruptedException) {
                    Thread.currentThread().interrupt();
                }

                messageFailedSendCount++;
                log.warn(messageFailedCountString, messageFailedSendCount);

                throw new ConnectorCheckedException(
                        KafkaOpenMetadataTopicConnectorErrorCode.ERROR_SENDING_EVENT.getMessageDefinition(
                                error.getClass().getName(), topicName, error.getMessage()), this.getClass().getName(),
                        methodName, error);
            }
        }

    }


    /**
     * This is the method that provides the behaviour of the thread.
     */
    @Override
    public void run() {

        // Keep as compact a possible. topis is important, and keep id for disambiguation. Kafka producer already in class which is logged
        String listenerThreadName = topicName + "/" + Thread.currentThread().getName();

        final String actionDescription = listenerThreadName + ":run";

        Thread.currentThread().setName(listenerThreadName);

        if (auditLog != null) {
            auditLog.logMessage(actionDescription,
                                KafkaOpenMetadataTopicConnectorAuditCode.KAFKA_PRODUCER_START.getMessageDefinition(
                                        topicName, String.valueOf(sendBuffer.size())),
                                this.producerProperties.toString());
        }

        log.info("Main loop started for topic {}", topicName);
        int sleepTime = 1000;
        while (isRunning()) {
            try {
                String bufferedEvent = this.getEvent();

                /*
                 * If there are no events waiting then sleep
                 */
                if (bufferedEvent == null) {
                    TimeUnit.MILLISECONDS.sleep(sleepTime);
                } else {
                    log.debug("Processing buffered events");
                    /*
                     * Send all waiting events
                     */
                    while (bufferedEvent != null) {
                        publishEvent(bufferedEvent);
                        bufferedEvent = this.getEvent();
                    }
                }
            } catch (InterruptedException error) {
                log.debug("Woken up from sleep ");
                Thread.currentThread().interrupt();
            } catch (Exception error) {
                log.warn("Bad exception from sending events: {}",error.getMessage());

                if (isExceptionRetryable(error)) {
                    log.debug("Trying to recover");
                    this.recoverAfterError();
                } else {

                    /* This is an unrecoverable error so clean up and shutdown*/
                    break;
                }
            }
        }
        log.info("Exiting main loop for topic {} & cleaning up", topicName);

        /* producer may have already closed by exception handler in publishEvent */
        if (producer != null) {
            log.debug("");
            producer.close();
            producer = null;
        }

        if (auditLog != null) {
            auditLog.logMessage(actionDescription,
                                KafkaOpenMetadataTopicConnectorAuditCode.KAFKA_PRODUCER_SHUTDOWN.getMessageDefinition(
                                        topicName, Integer.toString(getSendBufferSize()),
                                        Long.toString(messageSendCount)), this.producerProperties.toString());
        }
    }


    /**
     * Supports putting events to the in memory OMRS Topic
     *
     * @param newEvent event to publish
     */
    private void putEvent(String newEvent) {
        inmemoryPutMessageCount++;
        log.debug("Metrics: inmemoryPutMessageCount {}", inmemoryPutMessageCount);
        log.debug("Metrics: sendBufferSize {}", sendBuffer.size());
        sendBuffer.add(newEvent);
    }


    /**
     * Returns the size of the send buffer
     *
     * @return int
     */
    private int getSendBufferSize() {
        return sendBuffer.size();
    }


    /**
     * Returns all the events found on the in memory OMRS Topic.
     *
     * @return list of received events.
     */
    private String getEvent() {
        if (sendBuffer.isEmpty()) {
            return null;
        }

        return sendBuffer.remove(0);
    }


    /**
     * Sends the supplied event to the topic.
     *
     * @param event OMRSEvent object containing the event properties.
     */
    public void sendEvent(String event) {
        this.putEvent(event);
    }


    /**
     * Give time for an error to clear.
     */
    protected void recoverAfterError() {
        long recoverySleepTimeSec = 10L;
        log.info("Waiting {} seconds to recover", recoverySleepTimeSec);

        try {
            Thread.sleep(recoverySleepTimeSec * 1000L);
        } catch (InterruptedException e1) {
            log.debug("Interrupted while recovering with exception: {}", e1.getMessage());
            Thread.currentThread().interrupt();
        }
    }


    /**
     * Normal shutdown
     */
    public void safeCloseProducer() {
        stopRunning();
    }


    /**
     * Should the thread keep looping.
     *
     * @return boolean
     */
    private boolean isRunning() {
        return running;
    }


    /**
     * Flip the switch to stop the thread.
     */
    private synchronized void stopRunning() {
        running = false;
    }

    private boolean isExceptionRetryable(Exception error) {

        /*
        This code would probably be more elegant if it used Throwables
        however I don't want to add Throwabales to the search and I didn't want to cast
         */
        Throwable nested = null;
        while ((nested = error.getCause()) != null) {
            if (nested instanceof RetriableException) {
                return true;
            }

            error = new Exception(error.getCause());
        }
        return false;
    }
}
