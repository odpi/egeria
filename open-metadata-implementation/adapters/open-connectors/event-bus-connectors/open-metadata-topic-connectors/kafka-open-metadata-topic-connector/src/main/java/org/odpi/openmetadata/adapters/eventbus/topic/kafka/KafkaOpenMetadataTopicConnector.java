/* SPDX-License-Identifier: Apache-2.0 */
package org.odpi.openmetadata.adapters.eventbus.topic.kafka;

import org.odpi.openmetadata.frameworks.connectors.ffdc.OCFErrorCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;

import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectorCheckedException;
import org.odpi.openmetadata.frameworks.connectors.properties.AdditionalProperties;
import org.odpi.openmetadata.frameworks.connectors.properties.ConnectionProperties;
import org.odpi.openmetadata.repositoryservices.connectors.openmetadatatopic.OpenMetadataTopicConnector;

import java.util.*;
import java.util.concurrent.ExecutionException;


/**
 * KafkaOMRSTopicConnector provides a concrete implementation of the OMRSTopicConnector that
 * uses native Apache Kafka as the event/messaging infrastructure.
 */
public class KafkaOpenMetadataTopicConnector extends OpenMetadataTopicConnector {
    private static final Logger log = LoggerFactory.getLogger(KafkaOpenMetadataTopicConnector.class);

    private Properties producerProps;
    private Properties consumerProps;
    private Thread consumerThread;
    private static String KAFKA_TOPIC_ID = "kafka.omrs.topic.id";
    private String outTopic;
    private KafkaOpenMetadataEventConsumer consumer;
    private List<String> eventList = Collections.synchronizedList(new ArrayList<String>());

    public KafkaOpenMetadataTopicConnector() {
        super();
        producerProps = new Properties();
        consumerProps = new Properties();


    }

    @Override
    public void initialize(String connectorInstanceId, ConnectionProperties connectionProperties) {

        super.initialize(connectorInstanceId, connectionProperties);

        AdditionalProperties additionalProperties = connectionProperties.getAdditionalProperties();
        initializeKafkaProperties(additionalProperties);

        // Use random consumer group to ensure delivery
        String consumerGroupId = "omrsConsumerGroup-" + UUID.randomUUID().toString();
        consumerProps.put("group.id", consumerGroupId);
        this.connectorInstanceId = connectorInstanceId;
        outTopic = producerProps.getProperty(KAFKA_TOPIC_ID);
        consumer = new KafkaOpenMetadataEventConsumer(consumerProps, this);
        consumerThread = new Thread(consumer);
        consumerThread.start();

    }

    private void  initializeKafkaProperties(AdditionalProperties additionalProperties) {

        if (additionalProperties != null) {
            Map<String, Object> propertiesMap = (Map) additionalProperties.getProperty("producer");
            for (Map.Entry<String, Object> entry : propertiesMap.entrySet()) {
                producerProps.setProperty(entry.getKey(), (String) entry.getValue());
            }

            propertiesMap = (Map) additionalProperties.getProperty("consumer");
            for (Map.Entry<String, Object> entry : propertiesMap.entrySet()) {
                consumerProps.setProperty(entry.getKey(), (String) entry.getValue());
            }
        }

    }

    /**
     * Indicates that the connector is completely configured and can begin processing.
     *
     * @throws ConnectorCheckedException there is a problem within the connector.
     */
    public void start() throws ConnectorCheckedException {
        super.start();
    }


    /**
     * Sends the supplied event to the topic.
     *
     * @param event object containing the event properties.
     * @throws ConnectorCheckedException the connector is not able to communicate with the event bus
     */
    public void sendEvent(String event) throws ConnectorCheckedException {

        Producer<String, String> producer = new KafkaProducer<>(producerProps);

        try {
            log.debug("sending message {0}" + event);
            ProducerRecord<String, String> record = new ProducerRecord<String, String>(outTopic, event);
            producer.send(record).get();
        } catch (InterruptedException | ExecutionException e) {
            log.error("Exception in sendevent",e);
            e.printStackTrace();
        } finally {
            producer.flush();
            producer.close();
        }

    }


    /**
     * Look to see if there is one of more new events to process.
     *
     * @return a list of received events or null
     */
    protected List<String> checkForEvents() {
        List<String> newEvents = null;

        // This method is called periodically from a independent thread managed by OpenMetadataTopic
        // (superclass) so it should not block.

        if (eventList != null & !eventList.isEmpty()) {
            log.debug("checking for events {0}" + eventList);
            newEvents = new ArrayList<String>();
            newEvents.addAll(eventList);

            //empty eventList otherwise same events will be sent in a loop
            eventList.removeAll(newEvents);


        }

        return newEvents;
    }

    /**
     * Distribute events to other listeners.
     *
     * @param event object containing the event properties.
     */
    public void distributeToListeners(String event) {

        log.debug("distribute event to listeners" + event);
        eventList.add(event);
    }

    /**
     * Free up any resources held since the connector is no longer needed.
     *
     * @throws ConnectorCheckedException there is a problem within the connector.
     */
    public void disconnect() throws ConnectorCheckedException {
        super.disconnect();
    }
}
