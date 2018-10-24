/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.eventbus.topic.kafka;

import org.odpi.openmetadata.frameworks.connectors.properties.EndpointProperties;
import org.odpi.openmetadata.repositoryservices.auditlog.OMRSAuditLog;
import org.odpi.openmetadata.repositoryservices.auditlog.OMRSAuditingComponent;
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
public class KafkaOpenMetadataTopicConnector extends OpenMetadataTopicConnector
{
    private static final Logger       log      = LoggerFactory.getLogger(KafkaOpenMetadataTopicConnector.class);
    private static final OMRSAuditLog auditLog = new OMRSAuditLog(OMRSAuditingComponent.OPEN_METADATA_TOPIC_CONNECTOR);


    private Properties producerProperties = new Properties();
    private Properties consumerProperties = new Properties();

    private Thread                         consumerThread = null;
    private String                         outTopic = null;
    private String                         serverId = null;
    private KafkaOpenMetadataEventConsumer consumer = null;
    private List<String>                   incomingEventsList = Collections.synchronizedList(new ArrayList<>());


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
        consumerProperties.put("group.id", "test");
        consumerProperties.put("enable.auto.commit", "true");
        consumerProperties.put("auto.commit.interval.ms", "1000");
        consumerProperties.put("session.timeout.ms", "30000");
        consumerProperties.put("zookeeper.session.timeout.ms", 400);
        consumerProperties.put("zookeeper.sync.time.ms", 200);
        consumerProperties.put("fetch.message.max.bytes", 10485760);
        consumerProperties.put("max.partition.fetch.bytes",	10485760);
        consumerProperties.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        consumerProperties.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
    }


    /**
     * Call made by the ConnectorProvider to initialize the Connector with the base services.
     *
     * @param connectorInstanceId   unique id for the connector instance   useful for messages etc
     * @param connectionProperties   POJO for the configuration used to create the connector.
     */
    @Override
    public void initialize(String               connectorInstanceId,
                           ConnectionProperties connectionProperties)
    {
        final String           actionDescription = "initialize";
        KafkaOpenMetadataTopicConnectorAuditCode auditCode;

        super.initialize(connectorInstanceId, connectionProperties);

        EndpointProperties endpoint = connectionProperties.getEndpoint();
        if (endpoint != null)
        {
            outTopic = endpoint.getAddress();

            AdditionalProperties additionalProperties = connectionProperties.getAdditionalProperties();
            if (additionalProperties != null)
            {
                this.initializeKafkaProperties(additionalProperties);

                /*
                 * The consumer group defines which list of events that this connector is processing.  A particular server
                 * wants to keep reading from the same list.  Thus it needs to be passed the group.id it used
                 * the last time it ran.  This is supplied in the connection object as the serverIdProperty.
                 */
                serverId = (String) additionalProperties.getProperty(KafkaOpenMetadataTopicProvider.serverIdPropertyName);
                consumerProperties.put("group.id", serverId);

                auditCode = KafkaOpenMetadataTopicConnectorAuditCode.SERVICE_INITIALIZING;
                auditLog.logRecord(actionDescription,
                                   auditCode.getLogMessageId(),
                                   auditCode.getSeverity(),
                                   auditCode.getFormattedLogMessage(outTopic, serverId),
                                   null,
                                   auditCode.getSystemAction(),
                                   auditCode.getUserAction());

                auditCode = KafkaOpenMetadataTopicConnectorAuditCode.SERVICE_PRODUCER_PROPERTIES;
                auditLog.logRecord(actionDescription,
                                   auditCode.getLogMessageId(),
                                   auditCode.getSeverity(),
                                   auditCode.getFormattedLogMessage(producerProperties.toString()),
                                   null,
                                   auditCode.getSystemAction(),
                                   auditCode.getUserAction());

                /*
                 * Inbound events are received in a different thread so that we can still send events on this thread
                 * even if the Kafka consumer is blocked waiting for the next incoming event.
                 */
                consumer = new KafkaOpenMetadataEventConsumer(outTopic,serverId, consumerProperties, this);
                consumerThread = new Thread(consumer);
                consumerThread.start();
            }
            else
            {
                auditCode = KafkaOpenMetadataTopicConnectorAuditCode.NULL_ADDITIONAL_PROPERTIES;
                auditLog.logRecord(actionDescription,
                                   auditCode.getLogMessageId(),
                                   auditCode.getSeverity(),
                                   auditCode.getFormattedLogMessage(outTopic),
                                   null,
                                   auditCode.getSystemAction(),
                                   auditCode.getUserAction());
            }
        }
        else
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


    /**
     * Apache Kafka is configured with two groups of properties.  One for inbound events (the consumer) and
     * one for the outbound events (producer).  The constructor sets up default values for these properties.
     * This method overrides the initial values with properties configured on the event bus admin service.
     * For most environments, the only properties needed are the bootstrap servers.
     *
     * @param additionalProperties additional properties from the connection.
     */
    private void  initializeKafkaProperties(AdditionalProperties additionalProperties)
    {
        final String           actionDescription = "initializeKafkaProperties";
        KafkaOpenMetadataTopicConnectorAuditCode auditCode;

        try
        {
            Object              propertiesObject;
            Map<String, Object> propertiesMap;

            propertiesObject = additionalProperties.getProperty(KafkaOpenMetadataTopicProvider.producerPropertyName);
            if (propertiesObject != null)
            {
                propertiesMap = (Map<String, Object>)propertiesObject;
                for (Map.Entry<String, Object> entry : propertiesMap.entrySet())
                {
                    producerProperties.setProperty(entry.getKey(), (String) entry.getValue());
                }
            }

            propertiesObject = additionalProperties.getProperty(KafkaOpenMetadataTopicProvider.consumerPropertyName);
            if (propertiesObject != null)
            {
                propertiesMap = (Map<String, Object>) propertiesObject;
                for (Map.Entry<String, Object> entry : propertiesMap.entrySet())
                {
                    consumerProperties.setProperty(entry.getKey(), (String) entry.getValue());
                }
            }
        }
        catch (Throwable   error)
        {
            auditCode = KafkaOpenMetadataTopicConnectorAuditCode.UNABLE_TO_PARSE_ADDITIONAL_PROPERTIES;
            auditLog.logRecord(actionDescription,
                               auditCode.getLogMessageId(),
                               auditCode.getSeverity(),
                               auditCode.getFormattedLogMessage(outTopic, error.getClass().getName(), error.getMessage()),
                               null,
                               auditCode.getSystemAction(),
                               auditCode.getUserAction());
        }
    }


    /**
     * Indicates that the connector is completely configured and can begin processing.
     *
     * @throws ConnectorCheckedException there is a problem within the connector.
     */
    public void start() throws ConnectorCheckedException
    {
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
        final String methodName = "sendEvent";

        Producer<String, String> producer = new KafkaProducer<>(producerProperties);

        try
        {
            log.debug("Sending message {0}" + event);
            ProducerRecord<String, String> record = new ProducerRecord<>(outTopic, serverId, event);
            producer.send(record).get();
        }
        catch (InterruptedException | ExecutionException error)
        {
            log.error("Exception in sendEvent ", error);
            KafkaOpenMetadataTopicConnectorErrorCode errorCode = KafkaOpenMetadataTopicConnectorErrorCode.ERROR_SENDING_EVENT;
            String                                   errorMessage = errorCode.getErrorMessageId() +
                                                                    errorCode.getFormattedErrorMessage(error.getClass().getName(),
                                                                                                       outTopic,
                                                                                                       error.getMessage());

            throw new ConnectorCheckedException(errorCode.getHTTPErrorCode(),
                                                this.getClass().getName(),
                                                methodName,
                                                errorMessage,
                                                errorCode.getSystemAction(),
                                                errorCode.getUserAction(),
                                                error);
        }
        finally
        {
            /*
             * Producers have a thread and an in memory buffer
             */
            producer.flush();
            producer.close();
        }

    }


    /**
     * Look to see if there is one of more new events to process.
     *
     * @return a list of received events or null
     */
    protected List<String> checkForEvents()
    {
        List<String> newEvents = null;

        // This method is called periodically from a independent thread managed by OpenMetadataTopic
        // (superclass) so it should not block.

        if ((incomingEventsList != null) && (!incomingEventsList.isEmpty()))
        {
            log.debug("checking for events {0}" + incomingEventsList);
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
    void distributeToListeners(String event)
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

        super.disconnect();

        auditCode = KafkaOpenMetadataTopicConnectorAuditCode.SERVICE_SHUTDOWN;
        auditLog.logRecord(actionDescription,
                           auditCode.getLogMessageId(),
                           auditCode.getSeverity(),
                           auditCode.getFormattedLogMessage(outTopic),
                           null,
                           auditCode.getSystemAction(),
                           auditCode.getUserAction());

        consumer.safeCloseConsumer();
    }
}
