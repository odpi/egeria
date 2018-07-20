/* SPDX-License-Identifier: Apache-2.0 */
package org.odpi.openmetadata.adapters.eventbus.topic.kafka;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.kafka.clients.consumer.ConsumerRebalanceListener;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.consumer.OffsetAndMetadata;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.errors.WakeupException;
import org.odpi.openmetadata.repositoryservices.connectors.openmetadatatopic.OpenMetadataTopicListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;




public class KafkaOpenMetadataEventConsumer implements Runnable {

    private static final Logger LOGGER = LoggerFactory.getLogger(KafkaOpenMetadataEventConsumer.class);

    private KafkaConsumer<String, String> consumer;
    private String topicToSubscribe;
    private static String CONSUMER_TOPIC_ID = "kafka.omrs.topic.id";
    private static long recoverySleepTimeSec = 10L;
    private static final long DEFAULT_POLL_TIMEOUT = 1000;
    private KafkaOpenMetadataTopicConnector connector;

    private Map<TopicPartition, OffsetAndMetadata> currentOffsets = new HashMap<>();

    private Boolean running = true;

    private ArrayList<OpenMetadataTopicListener> topicListeners;

    public KafkaOpenMetadataEventConsumer(Properties consumerProps, ArrayList<OpenMetadataTopicListener> topicListeners) {
        consumer = new KafkaConsumer<>(consumerProps);
        topicToSubscribe = consumerProps.getProperty(CONSUMER_TOPIC_ID);
        consumer.subscribe(Arrays.asList(topicToSubscribe), new HandleRebalance());
        this.topicListeners = topicListeners;
    }

    public KafkaOpenMetadataEventConsumer(Properties consumerProps, KafkaOpenMetadataTopicConnector connector) {
        consumer = new KafkaConsumer<>(consumerProps);
        topicToSubscribe = consumerProps.getProperty(CONSUMER_TOPIC_ID);
        consumer.subscribe(Arrays.asList(topicToSubscribe), new HandleRebalance());
        this.connector = connector;
    }

    public void stop() {
        running = false;
        if (consumer != null) {
            consumer.wakeup();
        }
    }

    @Override
    public void run() {
        while (running) {

            ConsumerRecords<String, String> records = consumer.poll(DEFAULT_POLL_TIMEOUT);
            try {

                LOGGER.debug("Found records "+records.count());
                for (ConsumerRecord<String, String> record : records) {
                    String json = record.value();
                    LOGGER.debug("Received message: ", json);

                    TopicPartition partition = new TopicPartition(record.topic(),
                            record.partition());
                    currentOffsets.put(partition, new OffsetAndMetadata(record.offset() + 1));
                    try {
                        connector.distributeToListeners(json);
                    } catch (Exception e) {
                        LOGGER.error(String.format("Error distributing event: %s", e.getMessage()), e);
                        e.printStackTrace(System.err);
                    }
                }
            } catch (WakeupException e) {
                LOGGER.debug("Received wakeup call, proceeding with graceful shutdown", e);
            } catch (Exception e) {
                LOGGER.error(String.format("Unexpected error: %s", e.getMessage()), e);
                recoverAfterError();
            } finally {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    LOGGER.error(String.format("Interruption error: %s", e.getMessage()), e);
                }
            }
        }
    }

    protected void recoverAfterError() {
        LOGGER.info(String.format("Waiting %s seconds to recover", recoverySleepTimeSec));
        try {
            Thread.sleep(recoverySleepTimeSec * 1000L);
        } catch (InterruptedException e1) {
            LOGGER.debug("Interrupted while recovering", e1);
        }
    }

    public void safeCloseConsumer() {
        if (consumer != null) {
            try {
                consumer.commitSync(currentOffsets);
            } finally {
                consumer.close();
            }
            consumer = null;
        }
    }

    private class HandleRebalance implements ConsumerRebalanceListener {

        public void onPartitionsAssigned(Collection<TopicPartition> partitions) {
        }

        public void onPartitionsRevoked(Collection<TopicPartition> partitions) {
            LOGGER.info(
                    "Lost partitions in rebalance. Committing current offsets:" + currentOffsets);
            consumer.commitSync(currentOffsets);
        }

    }

    public void stopConsumption() {
        synchronized (running) {
            running = false;
        }
    }

}
