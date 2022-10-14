/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.eventbus.topic.kafka;

/**
 * Configurable properties for the KafkaOpenMetadataEventConsumer
 *
 */
public enum KafkaOpenMetadataEventConsumerProperty
{
	/*
	 * Controls the maximum size of the unprocessed kafka event queue. When this
	 * size is reached, we will not poll for additional events unless a poll is
	 * required to prevent Kafka from treating the consumer as dead.
	 */
	MAX_QUEUE_SIZE("event_bus_max_queue_size", "100"),
	
	/*
	 * In order to ensure that Kafka does not treat our consumer as dead, we need to
	 * poll for messages within some configured maximum poll interval. To make sure
	 * this happens, when we are deciding whether or not to poll Kafka we need to
	 * see how close we are to the time when we will exceed that maximum poll
	 * interval. If we are close enough, then we force a poll to avoid having the consumer die. 
	 * This parameter controls how close to the timeout we need to do be in order to force a poll.
	 * 
	 * The value provided as a number of milliseconds.
	 */
	CONSUMER_TIMEOUT_PREVENTION_SAFETY_WINDOW_MS("timeout_prevention_safety_window_ms", "30000"),
	
	/*
	 * This specifies the poll timeout (in ms) that we provide to kafka when we poll for
	 * messages.
	 */
	POLL_TIMEOUT("poll_timeout_ms", "10000"),
	
	/*
	 * The amount of time to sleep after we encounter a Kafka error, in seconds
	 */
	RECOVERY_SLEEP_TIME("recovery_sleep_time_sec", "10"),
	
    /**
     * This property adds an optional upper bound for the consumer event processing
     * before an event is treated as being fully processed and committed to Kafka.  If
     * the value is negative, there is no timeout, and offsets will not be committed
     * into Kafka until the message has been completely processed by all consumers.
     * 
     * This property is only used if auto commit is disabled in the Kafka consumer.
     */
    CONSUMER_EVENT_PROCESSING_TIMEOUT_MINS("consumer_message_processing_timeout_mins", "30"),
    
    /**
     * Controls the interval between checks to see if a message has
     * been processed and its offset committed to Kafka.  This is only
     * used if auto commit is disabled in the Kafka consumer. 
     * 
     */
    COMMIT_CHECK_INTERVAL_MS("commit_check_interval_ms", "5000");

	private final String propertyName;
	private final String defaultValue;

	KafkaOpenMetadataEventConsumerProperty(String name, String defaultValue)
	{
		this.propertyName = name;
		this.defaultValue = defaultValue;
	}
	
	public String getPropertyName() {
		return propertyName;
	}
	
	public String getDefaultValue() {
		return defaultValue;
	}


}
