/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.eventbus.topic.kafka;

import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.errors.RetriableException;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.auditlog.AuditLoggingBase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.Properties;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

/**
 * KafkaOpenMetadataEventProducer manages the sending of events on Apache Kafka.
 * <p>
 * Callers hand events to {@link #sendEvent(String)}, which only places them on a bounded in-memory queue and
 * so never blocks the calling thread - even when Kafka is down.  A single producer thread drains the queue and
 * hands each event to the Kafka client asynchronously.  The Kafka client batches, compresses and pipelines the
 * events, and reports the outcome of each one through a callback, so throughput is no longer limited to one
 * broker round trip per event.
 * <p>
 * The queue is owned by the topic connector rather than this object, so events waiting to be sent survive a
 * restart of the producer thread.  When the queue is full, new events are discarded (and counted) rather than
 * allowed to exhaust the heap.
 */
public class KafkaOpenMetadataEventProducer extends AuditLoggingBase implements Runnable
{
    private static final Logger log = LoggerFactory.getLogger(KafkaOpenMetadataEventProducer.class);

    /*
     * How often (in events) the repeating error conditions are reported after the first occurrence.
     */
    static final long         REPORTING_INTERVAL   = 10000L;

    private static final long retryBackoffBaseMs   = 500L;
    private static final long retryBackoffMaxMs    = 5000L;
    private static final long pollTimeoutMs        = 1000L;
    private static final long closeTimeoutSeconds  = 30L;

    private final BlockingQueue<String> sendBuffer;
    private final String                topicName;
    private final String                localServerId;
    private final Properties            producerProperties;

    private volatile boolean            running  = true;
    private Producer<String, String>    producer = null;

    /*
     * The event taken from the queue whose hand-off to the Kafka client failed with a retryable error.
     * It is retried before any other event is taken from the queue so that ordering is preserved.
     */
    private String                      pendingEvent = null;

    /*
     * Counters are updated from the caller threads, this thread and the Kafka client's I/O thread.
     */
    private final AtomicLong messagePublishRequestCount = new AtomicLong(0);
    private final AtomicLong messageSendCount           = new AtomicLong(0);
    private final AtomicLong messageFailedSendCount     = new AtomicLong(0);
    private final AtomicLong droppedEventCount          = new AtomicLong(0);

    private final Callback   deliveryCallback           = this::deliveryComplete;


    /**
     * Constructor for the event producer.
     *
     * @param topicName          name of the topic to send on.
     * @param localServerId      identifier to enable receiver to identify that an event came from this server.
     * @param producerProperties properties for the producer.
     * @param sendBuffer         bounded queue of events waiting to be sent - owned by the topic connector.
     * @param auditLog           audit log for this component.
     */
    KafkaOpenMetadataEventProducer(String                topicName,
                                   String                localServerId,
                                   Properties            producerProperties,
                                   BlockingQueue<String> sendBuffer,
                                   AuditLog              auditLog)
    {
        super(auditLog);

        this.topicName          = topicName;
        this.localServerId      = localServerId;
        this.producerProperties = producerProperties;
        this.sendBuffer         = sendBuffer;

        final String           actionDescription = "new producer";

        logRecord(actionDescription,
                  KafkaOpenMetadataTopicConnectorAuditCode.SERVICE_PRODUCER_PROPERTIES.getMessageDefinition(
                          Integer.toString(producerProperties.size()), topicName),
                  producerProperties.toString());
    }


    /**
     * Queue the supplied event for sending.  This method never blocks.  If the queue is full, the event is
     * discarded and counted, because the alternatives - blocking the caller or growing the queue without limit -
     * stall or crash the server.
     *
     * @param event event to publish
     */
    public void sendEvent(String event)
    {
        messagePublishRequestCount.incrementAndGet();

        if (! sendBuffer.offer(event))
        {
            long dropped = droppedEventCount.incrementAndGet();

            if ((dropped == 1) || (dropped % REPORTING_INTERVAL == 0))
            {
                final String actionDescription = "sendEvent";

                log.warn("Send buffer for topic {} is full; {} events discarded so far", topicName, dropped);
                logRecord(actionDescription,
                          KafkaOpenMetadataTopicConnectorAuditCode.EVENT_BUFFER_FULL.getMessageDefinition(
                                  topicName,
                                  Integer.toString(sendBuffer.size()),
                                  Long.toString(dropped),
                                  Long.toString(messageSendCount.get())));
            }
        }
    }


    /**
     * This is the method that provides the behaviour of the thread.
     */
    @Override
    public void run()
    {
        // Keep as compact as possible. Topic is important, and keep id for disambiguation.
        String listenerThreadName = topicName + "/" + Thread.currentThread().getName();

        final String actionDescription = listenerThreadName + ":run";

        Thread.currentThread().setName(listenerThreadName);

        logRecord(actionDescription,
                  KafkaOpenMetadataTopicConnectorAuditCode.KAFKA_PRODUCER_START.getMessageDefinition(
                          topicName, String.valueOf(sendBuffer.size())),
                  this.producerProperties.toString());

        log.info("Main loop started for topic {}", topicName);

        long consecutiveFailures = 0;

        while (isRunning())
        {
            try
            {
                String event = pendingEvent;

                if (event == null)
                {
                    event = sendBuffer.poll(pollTimeoutMs, TimeUnit.MILLISECONDS);
                }

                if (event != null)
                {
                    pendingEvent = event;

                    if (producer == null)
                    {
                        try
                        {
                            log.debug("Creating new producer for topic {}", topicName);
                            producer = createProducer();
                        }
                        catch (Exception error)
                        {
                            /*
                             * Usually a configuration problem.  The event is kept and the creation retried after a
                             * back-off; the bounded queue limits what accumulates meanwhile.
                             */
                            consecutiveFailures++;

                            if (consecutiveFailures == 1)
                            {
                                logExceptionRecord(actionDescription,
                                                   KafkaOpenMetadataTopicConnectorAuditCode.ERROR_CONNECTING_KAFKA_PRODUCER.getMessageDefinition(topicName),
                                                   error);
                            }

                            if (backOff(consecutiveFailures))
                            {
                                continue;
                            }
                            break;
                        }
                    }

                    /*
                     * Asynchronous hand-off.  This only blocks if the Kafka client's own buffer is full or the
                     * topic's metadata is not available - up to max.block.ms.
                     */
                    producer.send(new ProducerRecord<>(topicName, localServerId, event), deliveryCallback);

                    pendingEvent        = null;
                    consecutiveFailures = 0;
                }
            }
            catch (InterruptedException error)
            {
                log.debug("Producer thread for topic {} interrupted", topicName);
                Thread.currentThread().interrupt();
                break;
            }
            catch (Exception error)
            {
                consecutiveFailures++;

                if (consecutiveFailures == 1)
                {
                    logExceptionRecord(actionDescription,
                                       KafkaOpenMetadataTopicConnectorAuditCode.EVENT_SEND_IN_ERROR_LOOP.getMessageDefinition(
                                               topicName,
                                               Long.toString(messageSendCount.get()),
                                               Integer.toString(sendBuffer.size()),
                                               error.getClass().getName(),
                                               error.getMessage()),
                                       error);
                }

                if (! isExceptionRetryable(error))
                {
                    /*
                     * The Kafka client will not recover from this by itself, so discard the event that caused it
                     * and rebuild the client before trying the next one.
                     */
                    log.warn("Discarding event for topic {} after non-retryable error: {}", topicName, error.getMessage());
                    messageFailedSendCount.incrementAndGet();
                    pendingEvent = null;
                    closeProducer(Duration.ZERO);
                }

                if (! backOff(consecutiveFailures))
                {
                    break;
                }
            }
        }

        log.info("Exiting main loop for topic {} & cleaning up", topicName);

        /*
         * Closing the producer sends the events already handed to the Kafka client.  Any event still on the
         * queue stays there for the next producer thread.
         */
        if (pendingEvent != null)
        {
            sendBuffer.offer(pendingEvent);
            pendingEvent = null;
        }

        closeProducer(Duration.ofSeconds(closeTimeoutSeconds));

        logRecord(actionDescription,
                  KafkaOpenMetadataTopicConnectorAuditCode.KAFKA_PRODUCER_SHUTDOWN.getMessageDefinition(
                          topicName,
                          Integer.toString(sendBuffer.size()),
                          Long.toString(messageSendCount.get())),
                  this.producerProperties.toString());
    }


    /**
     * Called by the Kafka client's I/O thread once an event has been acknowledged or has finally failed (after the
     * client's own retries, bounded by delivery.timeout.ms).  It must return quickly.
     *
     * @param metadata details of the stored record, or null if it failed
     * @param exception failure, or null if the event was stored
     */
    private void deliveryComplete(RecordMetadata metadata, Exception exception)
    {
        if (exception == null)
        {
            messageSendCount.incrementAndGet();
        }
        else
        {
            long failed = messageFailedSendCount.incrementAndGet();

            if ((failed == 1) || (failed % REPORTING_INTERVAL == 0))
            {
                final String actionDescription = "deliveryComplete";

                log.warn("Kafka failed to deliver an event on topic {}; {} failures so far: {}", topicName, failed, exception.getMessage());
                logExceptionRecord(actionDescription,
                                   KafkaOpenMetadataTopicConnectorAuditCode.EVENT_DELIVERY_FAILED.getMessageDefinition(
                                           topicName,
                                           Long.toString(failed),
                                           Long.toString(messageSendCount.get()),
                                           exception.getClass().getName(),
                                           exception.getMessage()),
                                   exception);
            }
        }
    }


    /**
     * Wait before retrying so that a broker returning fast failures does not drive this thread into a tight loop.
     *
     * @param consecutiveFailures number of failures in a row
     * @return false if the thread was interrupted and should stop
     */
    private boolean backOff(long consecutiveFailures)
    {
        try
        {
            TimeUnit.MILLISECONDS.sleep(Math.min(retryBackoffBaseMs * consecutiveFailures, retryBackoffMaxMs));
            return true;
        }
        catch (InterruptedException interrupted)
        {
            Thread.currentThread().interrupt();
            return false;
        }
    }


    /**
     * Close the Kafka client, if there is one.
     *
     * @param timeout how long to wait for events already handed to the client to be sent
     */
    private void closeProducer(Duration timeout)
    {
        if (producer != null)
        {
            try
            {
                producer.close(timeout);
            }
            catch (Exception error)
            {
                log.debug("Error closing producer for topic {}: {}", topicName, error.getMessage());
            }

            producer = null;
        }
    }


    /**
     * Create the Kafka client.  Overridden by the unit tests.
     *
     * @return new producer
     */
    Producer<String, String> createProducer()
    {
        return new KafkaProducer<>(producerProperties);
    }


    /**
     * Return the number of events acknowledged by Kafka.
     *
     * @return count
     */
    long getMessageSendCount()
    {
        return messageSendCount.get();
    }


    /**
     * Return the number of events that Kafka failed to store.
     *
     * @return count
     */
    long getMessageFailedSendCount()
    {
        return messageFailedSendCount.get();
    }


    /**
     * Return the number of events discarded because the send buffer was full.
     *
     * @return count
     */
    long getDroppedEventCount()
    {
        return droppedEventCount.get();
    }


    /**
     * Normal shutdown
     */
    public void safeCloseProducer()
    {
        running = false;
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
     * Walk the cause chain looking for a Kafka RetriableException.
     *
     * @param error exception to test
     * @return true if any exception in the chain is retryable
     */
    static boolean isExceptionRetryable(Throwable error)
    {
        /*
         * Step down the cause chain - stopping at the end or at a self-referencing cause.  The previous
         * implementation re-wrapped the same cause on each iteration, so a non-retryable cause spun
         * forever allocating exceptions (high CPU and heap pressure).
         */
        Throwable current = error;
        while (current != null)
        {
            if (current instanceof RetriableException)
            {
                return true;
            }

            Throwable cause = current.getCause();
            if (cause == current)
            {
                break;
            }
            current = cause;
        }
        return false;
    }
}
