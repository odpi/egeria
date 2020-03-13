/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.eventbus.topic.kafka;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.*;

import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectorCheckedException;
import org.odpi.openmetadata.frameworks.connectors.properties.EndpointProperties;
import org.odpi.openmetadata.repositoryservices.connectors.openmetadatatopic.IncomingEvent;
import org.odpi.openmetadata.repositoryservices.connectors.openmetadatatopic.OpenMetadataTopicConnector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * KafkaOMRSTopicConnector provides a concrete implementation of the OMRSTopicConnector that
 * uses native Apache Kafka as the event/messaging infrastructure.
 */
public class KafkaOpenMetadataTopicConnector extends OpenMetadataTopicConnector
{
    public static final String ENABLE_AUTO_COMMIT_PROPERTY = "enable.auto.commit";

    private static final Logger       log      = LoggerFactory.getLogger(KafkaOpenMetadataTopicConnector.class);

    
    private Properties producerProperties = new Properties();
    
    private Properties consumerEgeriaProperties = new Properties();
    private Properties consumerProperties = new Properties();

    private KafkaOpenMetadataEventConsumer consumer = null;
    private KafkaOpenMetadataEventProducer producer = null;

    private String       topicName          = null;
    private String       serverId           = null;
    /* this buffer is for consumed events */
    private List<IncomingEvent> incomingEventsList = Collections.synchronizedList(new ArrayList<>());

    private KafkaProducerExecutor executor = null;

    final String                   threadHeader = "Kafka-";
    Thread                         consumerThread;
    Thread                         producerThread;


    /* mock up a a SingleThreadProducer with an overided afterExecute */
    private class KafkaProducerExecutor extends ThreadPoolExecutor {
        KafkaProducerExecutor() {
            super(1, 1, Long.MAX_VALUE, TimeUnit.MICROSECONDS, new LinkedBlockingQueue<>(1));
        }

        @Override
        public void afterExecute(Runnable r, Throwable t) {
            super.afterExecute(r, t);

            /* we don't care why the thread ended , we just restart it */
            /* The thread will log on exit and on restart already, so no need to let anyone know */
            producer = new KafkaOpenMetadataEventProducer(topicName, serverId, producerProperties, KafkaOpenMetadataTopicConnector.this, auditLog);
            producerThread = new Thread(producer, threadHeader + "Producer-" + topicName);
            executor.execute(producerThread);
        }
    }
    /**
     * Constructor sets up the default properties for the producer and consumer.  Any properties passed through
     * the connection's additional properties will override these values.  For most environments,
     * The caller only needs to provide details of the bootstrap servers as the default properties
     * will support the open metadata workloads.
     */
    public KafkaOpenMetadataTopicConnector()
    {
        super();

        producerProperties.put("bootstrap.servers", "localhost:9092");
        producerProperties.put("acks", "all");
        producerProperties.put("retries", 1);
        producerProperties.put("batch.size", 16384);
        producerProperties.put("linger.ms", 0);
        producerProperties.put("buffer.memory", 33554432);
        producerProperties.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        producerProperties.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");

        consumerProperties.put("bootstrap.servers", "localhost:9092");
        consumerProperties.put(ENABLE_AUTO_COMMIT_PROPERTY, "true");
        consumerProperties.put("auto.commit.interval.ms", "1000");
        consumerProperties.put("session.timeout.ms", "30000");
        consumerProperties.put("max.partition.fetch.bytes",	10485760);
        consumerProperties.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        consumerProperties.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
    }


    /**
     * Retrieve information about the topic
     */
    private void initializeTopic()
    {
        final String           actionDescription = "initialize";
        KafkaOpenMetadataTopicConnectorAuditCode auditCode;

        super.initialize(connectorInstanceId, connectionProperties);

        EndpointProperties endpoint = connectionProperties.getEndpoint();
        if (endpoint != null)
        {
            topicName = endpoint.getAddress();

            Map<String, Object> configurationProperties = connectionProperties.getConfigurationProperties();
            if (configurationProperties != null)
            {
                this.initializeKafkaProperties(configurationProperties);

                /*
                 * The consumer group defines which list of events that this connector is processing.  A particular server
                 * wants to keep reading from the same list.  Thus it needs to be passed the group.id it used
                 * the last time it ran.  This is supplied in the connection object as the serverIdProperty.
                 */
                serverId = (String) configurationProperties.get(KafkaOpenMetadataTopicProvider.serverIdPropertyName);
                consumerProperties.put("group.id", serverId);

                if (auditLog != null)
                {
                    auditCode = KafkaOpenMetadataTopicConnectorAuditCode.SERVICE_INITIALIZING;
                    auditLog.logRecord(actionDescription,
                                       auditCode.getLogMessageId(),
                                       auditCode.getSeverity(),
                                       auditCode.getFormattedLogMessage(topicName, serverId),
                                       null,
                                       auditCode.getSystemAction(),
                                       auditCode.getUserAction());
                }
            }
            else
            {
                if (auditLog != null)
                {
                    auditCode = KafkaOpenMetadataTopicConnectorAuditCode.NULL_ADDITIONAL_PROPERTIES;
                    auditLog.logRecord(actionDescription,
                                       auditCode.getLogMessageId(),
                                       auditCode.getSeverity(),
                                       auditCode.getFormattedLogMessage(topicName),
                                       null,
                                       auditCode.getSystemAction(),
                                       auditCode.getUserAction());
                }
            }
        }
        else
        {
            if (auditLog != null)
            {
                auditCode = KafkaOpenMetadataTopicConnectorAuditCode.NO_TOPIC_NAME;
                auditLog.logRecord(actionDescription,
                                   auditCode.getLogMessageId(),
                                   auditCode.getSeverity(),
                                   auditCode.getFormattedLogMessage(),
                                   null,
                                   auditCode.getSystemAction(),
                                   auditCode.getUserAction());
            }
        }
    }


    /**
     * Apache Kafka is configured with two groups of properties.  One for inbound events (the consumer) and
     * one for the outbound events (producer).  The constructor sets up default values for these properties.
     * This method overrides the initial values with properties configured on the event bus admin service.
     * For most environments, the only properties needed are the bootstrap servers.
     *
     * @param configurationProperties additional properties from the connection.
     */
    private void  initializeKafkaProperties(Map<String, Object> configurationProperties)
    {
        final String                             actionDescription = "initializeKafkaProperties";
        KafkaOpenMetadataTopicConnectorAuditCode auditCode;
        Map<String, Object>   propertiesMap;

        try
        {
            Object              propertiesObject;

            propertiesObject = configurationProperties.get(KafkaOpenMetadataTopicProvider.producerPropertyName);
            copyProperties(propertiesObject, producerProperties);

            propertiesObject = configurationProperties.get(KafkaOpenMetadataTopicProvider.consumerPropertyName);
            copyProperties(propertiesObject, consumerProperties);
            
            propertiesObject = configurationProperties.get(KafkaOpenMetadataTopicProvider.egeriaConsumerPropertyName);
            copyProperties(propertiesObject, consumerEgeriaProperties);
        }
        catch (Throwable   error)
        {
            auditCode = KafkaOpenMetadataTopicConnectorAuditCode.UNABLE_TO_PARSE_CONFIG_PROPERTIES;
            auditLog.logRecord(actionDescription,
                               auditCode.getLogMessageId(),
                               auditCode.getSeverity(),
                               auditCode.getFormattedLogMessage(topicName, error.getClass().getName(), error.getMessage()),
                               null,
                               auditCode.getSystemAction(),
                               auditCode.getUserAction());
        }
    }


    @SuppressWarnings("unchecked")
    private void copyProperties(Object propertiesObject, Properties target)
    {
		Map<String, Object> propertiesMap;
		if (propertiesObject != null)
		{
            try
            {
                propertiesMap = (Map<String, Object>) propertiesObject;
                for (Map.Entry<String, Object> entry : propertiesMap.entrySet())
                {
                    target.setProperty(entry.getKey(), (String) entry.getValue());
                }
            }
		    catch (Throwable error)
            {
                // Problem with properties
            }
		}
	}

    /**
     * Indicates that the connector is completely configured and can begin processing.
     * It creates two threads, one for sending (producer) and the other for receiving
     * events (consumer)
     *
     * @throws ConnectorCheckedException there is a problem within the connector.
     */
    public void start() throws ConnectorCheckedException
    {

        this.initializeTopic();
        
        KafkaOpenMetadataEventConsumerConfiguration consumerConfig = new KafkaOpenMetadataEventConsumerConfiguration(consumerEgeriaProperties, auditLog);
        consumer = new KafkaOpenMetadataEventConsumer(topicName, serverId, consumerConfig, consumerProperties, this, auditLog);
        consumerThread = new Thread(consumer, threadHeader + "Consumer-" + topicName);
        consumerThread.start();

        producer = new KafkaOpenMetadataEventProducer(topicName, serverId, producerProperties, this, auditLog);
        producerThread = new Thread(producer, threadHeader + "Producer-" + topicName);
        executor = new KafkaProducerExecutor();
        executor.execute(producerThread);

        super.start();
    }


    /**
     * Sends the supplied event to the topic.
     *
     * @param event object containing the event properties.
     * @throws ConnectorCheckedException the connector is not able to communicate with the event bus
     */
    public void sendEvent(String event) throws ConnectorCheckedException
    {
        if (producer != null)
        {
            producer.sendEvent(event);
        }
    }


    /**
     * Look to see if there is one of more new events to process.
     *
     * @return a list of received events or null
     */
    @Override
    protected List<IncomingEvent> checkForIncomingEvents()
    {
        List<IncomingEvent> newEvents = null;

        // This method is called periodically from a independent thread managed by OpenMetadataTopic
        // (superclass) so it should not block.

        if ((incomingEventsList != null) && (!incomingEventsList.isEmpty()))
        {
            log.debug("Checking for events.  Number of found events: {0}", incomingEventsList.size());
            newEvents = new ArrayList<>(incomingEventsList);

            // empty incomingEventsList otherwise same events will be sent again
            incomingEventsList.removeAll(newEvents);
        }

        return newEvents;
    }

    /**
     * Distribute events to other listeners.
     *
     * @param event object containing the event properties.
     */
    void distributeToListeners(IncomingEvent event)
    {
        log.debug("distribute event to listeners" + event);
        incomingEventsList.add(event);
    }


    /**
     * Free up any resources held since the connector is no longer needed.
     *
     * @throws ConnectorCheckedException there is a problem within the connector.
     */
    public void disconnect() throws ConnectorCheckedException
    {
        final String           actionDescription = "disconnect";
        KafkaOpenMetadataTopicConnectorAuditCode auditCode;

        consumer.safeCloseConsumer();
        producer.safeCloseProducer();

        super.disconnect();

        auditCode = KafkaOpenMetadataTopicConnectorAuditCode.SERVICE_SHUTDOWN;
        auditLog.logRecord(actionDescription,
                           auditCode.getLogMessageId(),
                           auditCode.getSeverity(),
                           auditCode.getFormattedLogMessage(topicName),
                           null,
                           auditCode.getSystemAction(),
                           auditCode.getUserAction());
    }
    
    /**
     * Gets the number of events that have not been processed yet.
     * 
     * @return int
     */
    int getNumberOfUnprocessedEvents() {
    	return incomingEventsList.size();
    }
}
