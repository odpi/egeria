/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.eventbus.topic.kafka;

import java.time.Duration;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Queue;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import org.apache.kafka.clients.consumer.CommitFailedException;
import org.apache.kafka.clients.consumer.ConsumerRebalanceListener;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.consumer.OffsetAndMetadata;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.errors.WakeupException;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



/**
 * KafkaOpenMetadataEventConsumer is used to process events from kafka topic and is part of native
 * Apache Kafka event/messaging infrastructure.
 */
public class KafkaOpenMetadataEventConsumer implements Runnable
{
    private static final Logger log      = LoggerFactory.getLogger(KafkaOpenMetadataEventConsumer.class);

    private final AuditLog auditLog;

    private final long recoverySleepTimeSec; 
    private final long pollTimeout;
    private final long maxQueueSize;

    private              KafkaConsumer<String, String>   consumer;
    private final              String                    topicToSubscribe;
    private final              String                    localServerId;

    private final        KafkaOpenMetadataTopicConnector connector;

    private long nextMessageProcessingStatusCheckTime = System.currentTimeMillis();
    private long maxNextPollTimestampToAvoidConsumerTimeout = 0;
    private final long maxMsBetweenPolls;
    
    
    //If we get close enough to the consumer timeout timestamp, force a poll so that
    //we do not exceed the timeout.  This parameter controls how close we can get
    //before forcing a poll.
    private final long consumerTimeoutPreventionSafetyWindowMs;
    
    private final long messageProcessingStatusCheckIntervalMs;
    private final long messageProcessingTimeoutMs;

    private final Map<TopicPartition, OffsetAndMetadata> currentOffsets = new ConcurrentHashMap<>();
    private final Map<TopicPartition, BlockingDeque<KafkaIncomingEvent>> unprocessedEventQueues = new ConcurrentHashMap<>();

    private final AtomicBoolean running = new AtomicBoolean(true);
    
    private final boolean isAutoCommitEnabled;

    /**
     * Constructor for the event consumer.
     *
     * @param topicName name of the topic to listen on.
     * @param localServerId identifier to enable receiver to identify that an event came from this server.
     * @param config additional properties
     * @param kafkaConsumerProperties properties for the consumer.
     * @param connector connector holding the inbound listeners.
     * @param auditLog  audit log for this component.
     */
    KafkaOpenMetadataEventConsumer(String                                      topicName,
                                   String                                      localServerId,
                                   KafkaOpenMetadataEventConsumerConfiguration config,
                                   Properties                                  kafkaConsumerProperties,
                                   KafkaOpenMetadataTopicConnector             connector,
                                   AuditLog                                    auditLog)
    {
        this.auditLog = auditLog;
        this.consumer = new KafkaConsumer<>(kafkaConsumerProperties);
        this.topicToSubscribe = topicName;
        this.consumer.subscribe(Collections.singletonList(topicToSubscribe), new HandleRebalance(auditLog));
        this.connector = connector;
        this.localServerId = localServerId;

        final String           actionDescription = "initialize";

        auditLog.logMessage(actionDescription,
                            KafkaOpenMetadataTopicConnectorAuditCode.SERVICE_CONSUMER_PROPERTIES.getMessageDefinition
                                    (Integer.toString(kafkaConsumerProperties.size()), topicName),
                            kafkaConsumerProperties.toString());
        
        this.maxMsBetweenPolls = new KafkaConfigurationWrapper(kafkaConsumerProperties).getMaxPollIntervalMs();
        this.recoverySleepTimeSec = config.getLongProperty(KafkaOpenMetadataEventConsumerProperty.RECOVERY_SLEEP_TIME);
        this.maxQueueSize = config.getIntProperty(KafkaOpenMetadataEventConsumerProperty.MAX_QUEUE_SIZE);
        this.consumerTimeoutPreventionSafetyWindowMs = config.getLongProperty(KafkaOpenMetadataEventConsumerProperty.CONSUMER_TIMEOUT_PREVENTION_SAFETY_WINDOW_MS);
        this.pollTimeout = config.getLongProperty(KafkaOpenMetadataEventConsumerProperty.POLL_TIMEOUT);
        this.isAutoCommitEnabled = getBooleanProperty(kafkaConsumerProperties, KafkaOpenMetadataTopicConnector.ENABLE_AUTO_COMMIT_PROPERTY, false);
        this.messageProcessingStatusCheckIntervalMs = config.getLongProperty(KafkaOpenMetadataEventConsumerProperty.COMMIT_CHECK_INTERVAL_MS);
        long messageTimeoutMins = config.getLongProperty(KafkaOpenMetadataEventConsumerProperty.CONSUMER_EVENT_PROCESSING_TIMEOUT_MINS);
        this.messageProcessingTimeoutMs = messageTimeoutMins < 0 ? messageTimeoutMins : TimeUnit.MILLISECONDS.convert(messageTimeoutMins, TimeUnit.MINUTES);
    }


    private static boolean getBooleanProperty(Properties p, String name, boolean defaultValue) {
        String value = p.getProperty(name);
        if (value == null) {
            return defaultValue;
        }
       return Boolean.parseBoolean(value);
    }


    private void updateNextMaxPollTimestamp() {
    	maxNextPollTimestampToAvoidConsumerTimeout = System.currentTimeMillis() + maxMsBetweenPolls - consumerTimeoutPreventionSafetyWindowMs;	
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
                
                //This needs to be done in the same thread since the Kafka consumer does
                //not allow access by multiple threads
                checkForFullyProcessedMessagesIfNeeded();
                
            	//if we are close to the timeout, force a poll to avoid having the consumer
            	//be marked as dead because we have not polled often enough
            	final boolean pollRequired = System.currentTimeMillis() > maxNextPollTimestampToAvoidConsumerTimeout;
            
            
                	
            	int nUnprocessedEvents = connector.getNumberOfUnprocessedEvents();
            	if (! pollRequired && nUnprocessedEvents > maxQueueSize)
            	{
            		//The connector queue is too big.  Wait until the size goes down until
            		//polling again.  If we let the events just accumulate, we will
            		//eventually run out of memory if the consumer cannot keep up.
            		log.warn("Skipping Kafka polling since unprocessed message queue size {} is greater than {}", nUnprocessedEvents, maxQueueSize);
            		awaitNextPollingTime();
            		continue;
            	
            	}

            	updateNextMaxPollTimestamp();

                final Duration pollDuration = Duration.ofMillis(pollTimeout);
                final ConsumerRecords<String, String> records = consumer.poll(pollDuration);
                
                log.debug("Found records: " + records.count());
                for (ConsumerRecord<String, String> record : records)
                {
                    String json = record.value();
                    log.debug("Received message: " + json);
                    final KafkaIncomingEvent event = new KafkaIncomingEvent(json, record.offset());
                    if (! localServerId.equals(record.key()))
                    {
                        try
                        {
                            addUnprocessedEvent(record.partition(), record.topic(), event);
                            connector.distributeToListeners(event);
                        }
                        catch (Exception error)
                        {
                            log.error(String.format("Error distributing inbound event: %s", error.getMessage()), error);

                            if (auditLog != null)
                            {
                                auditLog.logException(actionDescription,
                                                      KafkaOpenMetadataTopicConnectorAuditCode.EXCEPTION_DISTRIBUTING_EVENT.getMessageDefinition
                                                            (topicToSubscribe,
                                                             error.getClass().getName(), json,
                                                             error.getMessage()),
                                                      error);
                            }
                        }
                    }
                    else
                    {
                        log.debug("Ignoring message with key: " + record.key() + " and value " + record.value());
                    }

                    if ( isAutoCommitEnabled) {
                        /*
                         * Acknowledge receipt of message.
                         */
                        
                        //If auto-commit is disabled, the offset for a message is only committed when
                        //the message has been completely processed by all consumers.  That
                        //is handled by the call to checkForFullyProcessedMessagesIfNeeded().
                        final TopicPartition partition = new TopicPartition(record.topic(), record.partition());
                        currentOffsets.put(partition, new OffsetAndMetadata(record.offset() + 1));
                    
                    }
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
                    auditLog.logException(actionDescription,
                                          KafkaOpenMetadataTopicConnectorAuditCode.EXCEPTION_RECEIVING_EVENT.getMessageDefinition(topicToSubscribe,
                                                                                                                                  error.getClass().getName(),
                                                                                                                                  error.getMessage()),
                                          error);
                }
                recoverAfterError();
            }
            finally
            {
                awaitNextPollingTime();
            }
        }

        if (consumer != null)
        {
            try
            {
                //Check for fully processed messages one last time before
                //shutting down the consumer
                final boolean changesCommitted = checkForFullyProcessedMessages();
                if (! changesCommitted) {
                    //Figure out why no changes were committed.  There are 3 possibilities:
                    // 1) Auto commit is enabled
                    // 2) All of the unprocessed event queues are empty
                    // 3) We are waiting for some event to finish processing
                    
                    if (! isAutoCommitEnabled) {
                        final int nUnprocessedMessages = getNumberOfUnprocessedMessages();
                        if (nUnprocessedMessages > 0) {
                            log.error("Consumer was shut down before all message processing has completed!  There are " + nUnprocessedMessages + " messages whose processing is incomplete.");
                        }
                        else {
                            log.info("All messages have been fully processed.  Consumer is shutting down safely.");
                        }
                    }
                    //commit with the current offsets
                    log.info("Committing current offsets before shutdown: " + currentOffsets);
                    try {
                        consumer.commitSync(currentOffsets);
                    }
                    catch( WakeupException error)
                    {
                        //ignore we are shutting down
                    }
                    catch( Exception error)
                    {
                        if (auditLog != null)
                        {
                            auditLog.logException("consumer.commitSync",
                                    KafkaOpenMetadataTopicConnectorAuditCode.EXCEPTION_COMMITTING_OFFSETS.getMessageDefinition(error.getClass().getName(),
                                                                                                                               topicToSubscribe,
                                                                                                                               error.getMessage()),
                                    error);

                        }

                    }
                }
            }

            finally
            {
                consumer.close();
            }
            consumer = null;
        }
    }

    private void addUnprocessedEvent(int partition, String topic, KafkaIncomingEvent event) {
        if (isAutoCommitEnabled) {
            return;
        }
        final TopicPartition key = new TopicPartition(topic, partition);
        BlockingDeque<KafkaIncomingEvent> queue = unprocessedEventQueues.get(key);
        if (queue == null) {
            queue = new LinkedBlockingDeque<>();
            unprocessedEventQueues.put(key, queue);
        }
        queue.add(event);
    }

    /**
     * Checks the unprocessed message queues to see if there are any
     * messages whose processing has completed, but only if auto commit
     * is disabled and the configured amount of time has passed since
     * the last check
     * 
     * @return whether the current kafka committed message offsets
     *  changed
     */
    private boolean checkForFullyProcessedMessagesIfNeeded() {
        if (isAutoCommitEnabled) {
            return false;
        }
        if (System.currentTimeMillis() >= nextMessageProcessingStatusCheckTime) {
            boolean changesFound =  checkForFullyProcessedMessages();
            nextMessageProcessingStatusCheckTime = System.currentTimeMillis() + messageProcessingStatusCheckIntervalMs;
            return changesFound;
        }
        return false;
    }

    /**
     * Checks the unprocessed messages queues to see if there are 
     * any messages whose processing has completed.  This method
     * is a no-op if auto commit is enabled.
     * 
     * @return whether the current kafka committed message offsets
     *  changed
     */ 
    private boolean checkForFullyProcessedMessages() {
        if (isAutoCommitEnabled) {
            return false;
        }
        log.info("Checking for fully processed messages whose offsets need to be committed");

        //Check all the queues to see they have events initial events
        //that are fully processed
        Map<TopicPartition, OffsetAndMetadata> commitData = new HashMap<>();
        for(Map.Entry<TopicPartition, BlockingDeque<KafkaIncomingEvent>> entry : unprocessedEventQueues.entrySet()) {
            Queue<KafkaIncomingEvent> queue = entry.getValue();
           
            KafkaIncomingEvent mostRecentProcessedEvent = removeFullyProcessedEventsFromBeginningOfQueue(queue);
            if (mostRecentProcessedEvent != null) {
                OffsetAndMetadata omd = new OffsetAndMetadata(mostRecentProcessedEvent.getOffset() + 1);
                commitData.put(entry.getKey(), omd);
            }
        } 
        
        if (! commitData.isEmpty()) {
            currentOffsets.putAll(commitData);
            log.info("Committing: " + commitData);
            try {
                consumer.commitSync(commitData);
                return true;
            }
            catch( WakeupException error )
            {
                //ignore
            }
            catch( Exception error)
            {
                if (auditLog != null)
                {
                    auditLog.logException("checkForFullyProcessedMessages.commitSync",
                            KafkaOpenMetadataTopicConnectorAuditCode.EXCEPTION_COMMITTING_OFFSETS.getMessageDefinition(error.getClass().getName(),
                                                                                                                       topicToSubscribe,
                                                                                                                       error.getMessage()),
                            error);
                }


            }
        }
        return false;
        
    }
    
    /**
     * Iteratively removes fully processed event from the beginning of the queue until
     * either the queue is empty or the first entry in the queue has not been fully processed
     * 
     * @param queue incoming events
     * @return the most recent fully processed event that was removed from the queue
     */
    private KafkaIncomingEvent removeFullyProcessedEventsFromBeginningOfQueue(Queue<KafkaIncomingEvent> queue) {
        KafkaIncomingEvent lastRemoved = null;
        //Keep fully processed messages from the beginning of the queue until we 
        //encounter a message that is not fully processed or empty the queue
        while(isFirstEventFullyProcessed(queue)) {
            //The message at the beginning of the queue has been fully processed.  Remove
            //it from the queue and repeat the check.
            lastRemoved = queue.remove();
            log.info("Message with offset " + lastRemoved.getOffset() + " has been fully processed.");
        }
        KafkaIncomingEvent firstEvent = queue.peek();
        if (firstEvent != null) {
            //Queue is not empty, so we're waiting for the processing of first message in
            //the queue to finish
            log.info("Waiting for completing of processing of message with offset " + firstEvent.getOffset());
        }
        return lastRemoved;
    }

    private boolean isFirstEventFullyProcessed(Queue<KafkaIncomingEvent> queue) {
        
        KafkaIncomingEvent firstEvent = queue.peek();
        if (firstEvent == null) {
            //queue is empty
            return false;
        }
        
        //check whether the message processing timeout has elapsed (if there is one)
        if (messageProcessingTimeoutMs >= 0 && firstEvent.hasTimeElapsedSinceCreation(messageProcessingTimeoutMs)) {
            //max processing timeout has elapsed, treat the event as being fully processed
            log.warn("Processing of message at offset " + firstEvent.getOffset() + " timed out.");
            return true;
        }
        
        return firstEvent.isFullyProcessed();
    }
    
    /**
     * Gets the total number of messages in the incoming
     * event queues that have not been fully processed.
     * 
     * @return
     */
    private int getNumberOfUnprocessedMessages() {
        if (isAutoCommitEnabled) {
            return 0;
        }

        int result = 0;
        for(Queue<KafkaIncomingEvent> queue : unprocessedEventQueues.values()) {
            if (! queue.isEmpty()) {
                result++;
            }
        }
        return result;
    }

    private void awaitNextPollingTime() {
		try
		{
		    Thread.sleep(1000);
		}
		catch (InterruptedException e)
		{
		    log.error(String.format("Interruption error: %s", e.getMessage()), e);
		}
	}



    private void recoverAfterError()
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
    void safeCloseConsumer()
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
    private boolean isRunning()
    {
        return running.get();
    }


    /**
     * Flip the switch to stop the thread.
     */
    private void stopRunning()
    {
        running.set(false);
    }


    private class HandleRebalance implements ConsumerRebalanceListener
    {
        AuditLog auditLog = null;
        public HandleRebalance(AuditLog auditLog) {
            this.auditLog = auditLog;
        }

        public void onPartitionsAssigned(Collection<TopicPartition> partitions)
        {
        }

        public void onPartitionsRevoked(Collection<TopicPartition> partitions)
        {
            final String methodName = "onPartitionsRevoked.commitSync";
            if( !currentOffsets.isEmpty() )
            {
                log.info("Lost partitions in rebalance. Committing current offsets:" + currentOffsets);
                try
                {
                    consumer.commitSync(currentOffsets);
                } catch (WakeupException error)
                {
                    /*
                    This has occurred because a client was woken up to poll for new messages
                    and can safely be ignored.
                     */
                }
                catch (CommitFailedException error )
                {
                    /*
                    This is usually encountered during development because a debug session has prevented the kafka client
                    from honouring the heartbeat configuration.
                     */
                    auditLog.logMessage( methodName, KafkaOpenMetadataTopicConnectorAuditCode.FAILED_TO_COMMIT_CONSUMED_EVENTS.getMessageDefinition());
                }
                catch (Exception error)
                {
                    if (auditLog != null)
                    {
                        auditLog.logException(methodName,
                                KafkaOpenMetadataTopicConnectorAuditCode.EXCEPTION_COMMITTING_OFFSETS.getMessageDefinition(error.getClass().getName(),
                                        topicToSubscribe,
                                        error.getMessage()),
                                error);
                    }

                }
            }
        }
    }
}
