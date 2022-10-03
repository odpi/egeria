/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.eventbus.topic.kafka;

import java.time.Duration;
import java.util.*;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import org.apache.kafka.clients.consumer.*;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.errors.WakeupException;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static java.lang.Thread.sleep;


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

    // Keep track of when an initial rebalance is done
    private boolean initialPartitionAssignment = true;

    
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
    private final long startTime = System.currentTimeMillis();

    // Keep track of some counters
    private long countIgnoredMessages = 0;
    private long countReceivedMessages = 0;
    private long countCommits = 0;
    private long countMessagesToProcess = 0;
    private long countMessagesFailedToProcess = 0;


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

        if (auditLog != null)
        {
            auditLog.logMessage(actionDescription,
                                KafkaOpenMetadataTopicConnectorAuditCode.SERVICE_CONSUMER_PROPERTIES.getMessageDefinition
                                                                                                             (Integer.toString(kafkaConsumerProperties.size()), topicName),
                                kafkaConsumerProperties.toString());
        }
        
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

        // Log templates usually default to end of this text - so keep the id at the end for guaranteed uniqueness
        Thread.currentThread().setName(this.topicToSubscribe + "/" + Thread.currentThread().getName());

        log.info("Main loop started for topic {}", this.topicToSubscribe);

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
            		log.debug("Skipping Kafka polling since unprocessed message queue size {} is greater than {}", nUnprocessedEvents, maxQueueSize);
            		awaitNextPollingTime();
            		continue;
            	
            	}

            	updateNextMaxPollTimestamp();

                final Duration pollDuration = Duration.ofMillis(pollTimeout);
                final ConsumerRecords<String, String> records = consumer.poll(pollDuration);
                
                log.debug("Found records: {}", records.count());
                for (ConsumerRecord<String, String> consumerRecord : records)
                {
                    String json = consumerRecord.value();
                    log.debug("Received message: {}" ,json);
                    countReceivedMessages++;
                    log.debug("Metrics: receivedMessages: {}", countReceivedMessages);
                    final KafkaIncomingEvent event = new KafkaIncomingEvent(json, consumerRecord.offset());
                    final String recordKey=consumerRecord.key();
                    final String recordValue=consumerRecord.value();
                    if (! localServerId.equals(recordKey))
                    {
                        try
                        {
                            addUnprocessedEvent(consumerRecord.partition(), consumerRecord.topic(), event);
                            connector.distributeToListeners(event);
                            countMessagesToProcess++;
                            log.debug("Metrics: messagesToProcess: {}", countMessagesToProcess);
                        }
                        catch (Exception error)
                        {
                            countMessagesFailedToProcess++;
                            log.debug("Metrics: messagesFailedToProcess: {}", countMessagesFailedToProcess);
                            log.warn("Error distributing inbound event: {}", error.getMessage());

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
                        log.debug("Ignoring message with key: {} and value: {}",recordKey, recordValue);
                        countIgnoredMessages++;
                        log.debug("Metrics: ignoredMessages: {}", countIgnoredMessages);
                    }

                    if ( isAutoCommitEnabled) {
                        /*
                         * Acknowledge receipt of message.
                         */
                        
                        //If auto-commit is disabled, the offset for a message is only committed when
                        //the message has been completely processed by all consumers.  That
                        //is handled by the call to checkForFullyProcessedMessagesIfNeeded().
                        final TopicPartition partition = new TopicPartition(consumerRecord.topic(), consumerRecord.partition());
                        currentOffsets.put(partition, new OffsetAndMetadata(consumerRecord.offset() + 1));
                        countCommits++;
                        log.debug("Metrics: messageCommits: {}", countCommits);
                    
                    }
                }
            }
            catch (WakeupException e)
            {
                log.debug("Received wakeup call, proceeding with graceful shutdown");
            }
            catch (Exception error)
            {
                log.warn("Unexpected error: {}", error.getMessage());

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
                    // 2) all the unprocessed event queues are empty
                    // 3) We are waiting for some event to finish processing
                    
                    if (! isAutoCommitEnabled) {
                        final int nUnprocessedMessages = getNumberOfUnprocessedMessages();
                        if (nUnprocessedMessages > 0) {
                            log.warn("Consumer shut down before all message processing completed! unprocessed messages: {}", nUnprocessedMessages);
                        }
                        else {
                            log.info("All messages processed.  Consumer is shutting down.");
                        }
                    }
                    //commit with the current offsets
                    log.info("Committing current offset {} before shutdown.",currentOffsets);
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
        log.info("Exiting main loop for topic {} & cleaning up", this.topicToSubscribe);

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
     */
    private void checkForFullyProcessedMessagesIfNeeded() {
        if (isAutoCommitEnabled) {
            return;
        }
        if (System.currentTimeMillis() >= nextMessageProcessingStatusCheckTime) {
            checkForFullyProcessedMessages();
            nextMessageProcessingStatusCheckTime = System.currentTimeMillis() + messageProcessingStatusCheckIntervalMs;
        }
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
        log.debug("Checking for fully processed messages whose offsets need to be committed");

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
            log.debug("Committing: {}", commitData);
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
            log.debug("Message with offset {} has been fully processed.",lastRemoved.getOffset() );
            countCommits++;
            log.debug("Metrics: commits: {}", countCommits);
        }
        KafkaIncomingEvent firstEvent = queue.peek();
        if (firstEvent != null) {
            //Queue is not empty, so we're waiting for the processing of first message in
            //the queue to finish
            log.debug("Waiting for completing of processing of message with offset {}",firstEvent.getOffset());
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
            log.warn("Processing of message at offset {} timed out.", firstEvent.getOffset());
            return true;
        }
        
        return firstEvent.isFullyProcessed();
    }
    
    /**
     * Gets the total number of messages in the incoming
     * event queues that have not been fully processed.
     * 
     * @return number of messages still to be processed
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
		    sleep(1000);
		}
		catch (InterruptedException e)
		{
		    log.debug("Interrupted whilst sleeping:");
            Thread.currentThread().interrupt();
		}
	}



    private void recoverAfterError()
    {
        log.info("Waiting {} seconds to recover", recoverySleepTimeSec);

        try
        {
            sleep(recoverySleepTimeSec * 1000L);
        }
        catch (InterruptedException e1)
        {
            log.debug("Interrupted while recovering");
            Thread.currentThread().interrupt();
        }
    }


    /**
     * Normal shutdown
     */
    void safeCloseConsumer()
    {
        log.debug("Closing consumer");
        stopRunning();

        /*
         * Wake the thread up so it shuts down quicker.
         */
        if (consumer != null)
        {
            log.debug("Waking up consumer thread");
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
        log.debug("Set running to false");
        running.set(false);
    }


    private class HandleRebalance implements ConsumerRebalanceListener {
        AuditLog auditLog;

        public HandleRebalance(AuditLog auditLog) {
            this.auditLog = auditLog;
        }

        @Override
        public void onPartitionsAssigned(Collection<TopicPartition> partitions) {

            // Check if we need to rewind to handle initial startup case -- but only on first assignment
            try {
                if (initialPartitionAssignment) {
                    log.debug("Received initial PartitionsAssigned event");

                    long partitionCount = partitions.size();

                    if (partitionCount != 1) {
                        log.warn("Received PartitionsAssigned event with {} partitions. This is not supported.",partitionCount);
                    } else {
                        // there is only one partition, so we can just grab the first one - and we'll try this once only
                        initialPartitionAssignment = false;
                        long maxOffsetWanted; // same as 'beginning'

                        TopicPartition partition = partitions.iterator().next();
                        int partitionID=partition.partition();
                        String partitionTopic = partition.topic();

                        // query offset by timestamp (when we started connector) - NULL if there are no messages later than this offset
                        long reqStartTime=KafkaOpenMetadataEventConsumer.this.startTime;
                        log.info("Querying for offset by timestamp: {}",reqStartTime);
                        OffsetAndTimestamp otByStartTime = consumer.offsetsForTimes(Collections.singletonMap(partition,
                                reqStartTime)).get(partition);

                        // If null, then we don't have any earlier messages - ie there is no offset found
                        if (otByStartTime != null) {
                            // where we want to scoll to - the messages sent since we thought we started
                            maxOffsetWanted = otByStartTime.offset();
                            log.info("Earliest offset found for {} is {}",reqStartTime,otByStartTime.timestamp());

                            // get the current offset
                            long currentOffset = consumer.position(partition);

                            // if the current offset is later than the start time we want, rewind to the start time
                            if (currentOffset > maxOffsetWanted) {

                                log.info("Seeking to {} for partition {} and topic {} as current offset {} is too late", maxOffsetWanted, partitionID,
                                        partitionTopic, currentOffset);
                                consumer.seek(partition, maxOffsetWanted);
                            } else
                                log.info("Not Seeking to {} for partition {} and topic {} as current offset {} is older", maxOffsetWanted, partitionID,
                                        partitionTopic, currentOffset);
                        }
                        else
                            log.info("No missed events found for partition {} and topic {}", partitionID, partitionTopic);
                    }
                }
                else
                    log.debug("PartitionsAssigned Event - no action needed");
            } catch (Exception e) {
                // We leave the offset as-is if anything goes wrong. Eventually other messages will cause the effective state to be updated
                log.info("Error correcting seek position, continuing with defaults. Exception: {}", e.getMessage());
            }
        }

        @Override
        public void onPartitionsRevoked(Collection<TopicPartition> partitions)
        {
            final String methodName = "onPartitionsRevoked.commitSync";
            if( !currentOffsets.isEmpty() )
            {
                log.info("Lost partitions in rebalance. Committing current offsets: {}",currentOffsets);
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
                    if (auditLog != null)
                    {
                        auditLog.logMessage( methodName, KafkaOpenMetadataTopicConnectorAuditCode.FAILED_TO_COMMIT_CONSUMED_EVENTS.getMessageDefinition());
                    }
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
            else
                log.debug("PartitionsRevoked Event - no action needed");
        }
    }
}
