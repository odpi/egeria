/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.eventbus.topic.kafka;

import java.nio.file.FileSystemException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectorCheckedException;
import org.odpi.openmetadata.frameworks.connectors.properties.EndpointProperties;
import org.odpi.openmetadata.repositoryservices.connectors.openmetadatatopic.IncomingEvent;
import org.odpi.openmetadata.repositoryservices.connectors.openmetadatatopic.OpenMetadataTopicConnector;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.OMRSLogicErrorException;
import org.odpi.openmetadata.repositoryservices.init.InitializationManager;
import org.odpi.openmetadata.repositoryservices.init.InitializationMethod;
import org.odpi.openmetadata.repositoryservices.init.InitializationResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * KafkaOMRSTopicConnector provides a concrete implementation of the OMRSTopicConnector that
 * uses native Apache Kafka as the event/messaging infrastructure.
 */
public class KafkaOpenMetadataTopicConnector extends OpenMetadataTopicConnector
{
    static final String ENABLE_AUTO_COMMIT_PROPERTY = "enable.auto.commit";

    private static final Logger       log      = LoggerFactory.getLogger(KafkaOpenMetadataTopicConnector.class);

    
    private Properties producerProperties = new Properties();
    
    private Properties consumerEgeriaProperties = new Properties();
    private Properties consumerProperties = new Properties();
    private Properties connectorProperties = new Properties();
    
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
                    auditLog.logMessage(actionDescription,
                                        KafkaOpenMetadataTopicConnectorAuditCode.SERVICE_INITIALIZING.getMessageDefinition(topicName, serverId));
                }
            }
            else
            {
                if (auditLog != null)
                {
                    auditLog.logMessage(actionDescription,
                                        KafkaOpenMetadataTopicConnectorAuditCode.NULL_ADDITIONAL_PROPERTIES.getMessageDefinition(topicName));
                }
            }
        }
        else
        {
            if (auditLog != null)
            {
                auditLog.logMessage(actionDescription,
                                    KafkaOpenMetadataTopicConnectorAuditCode.NO_TOPIC_NAME.getMessageDefinition());
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
            auditLog.logMessage(actionDescription,
                                KafkaOpenMetadataTopicConnectorAuditCode.UNABLE_TO_PARSE_CONFIG_PROPERTIES.getMessageDefinition(topicName,
                                                                                                                                error.getClass().getName(),
                                                                                                                                error.getMessage()));
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
        
        int maxInitAttempts = consumerConfig.getIntProperty(KafkaOpenMetadataEventConsumerProperty.MAX_INIT_RETRIES);
        long initAttemptRetryIntervalMs = consumerConfig.getLongProperty(KafkaOpenMetadataEventConsumerProperty.INIT_RETRY_INTERVAL_MS);
  
        InitializationManager initializer = new InitializationManager(auditLog, maxInitAttempts, initAttemptRetryIntervalMs, new KafkaInitializationMethod());
        initializer.start();
    }
  
    /**
     * {@link InitializationMethod} that initializes 
     * the {@link KafkaOpenMetadataTopicConnector}
     * 
     * @see InitializationManager
     */
    private class KafkaInitializationMethod implements InitializationMethod 
    {
        
        private final AtomicBoolean consumerStarted = new AtomicBoolean(false);
        private final AtomicBoolean producerStarted = new AtomicBoolean(false);
        
        @Override
        public InitializationResult attemptInitialization()
        {
            try 
            {
                if (! consumerStarted.get()) 
                {
                    KafkaOpenMetadataEventConsumerConfiguration consumerConfig = new KafkaOpenMetadataEventConsumerConfiguration(consumerEgeriaProperties, auditLog);
                    consumer = new KafkaOpenMetadataEventConsumer(topicName, serverId, consumerConfig, consumerProperties, KafkaOpenMetadataTopicConnector.this, auditLog);
                    consumerThread = new Thread(consumer, threadHeader + "Consumer-" + topicName);
                    consumerThread.start();
                    consumerStarted.set(true);
                }
                
                if (! producerStarted.get()) 
                {
                    producer = new KafkaOpenMetadataEventProducer(topicName, serverId, producerProperties, KafkaOpenMetadataTopicConnector.this, auditLog);
                    producerThread = new Thread(producer, threadHeader + "Producer-" + topicName);
                    executor = new KafkaProducerExecutor();
                    executor.execute(producerThread);
                    producerStarted.set(true);
                }
            
                //Do not start the parent and advertise that the
                //connector is started until both the producer
                //and consumer have successfully been started
               
                startParent();
                
                return InitializationResult.SUCCESS;
            }
            catch(Throwable th)
            {
                return new InitializationResult(th);
            }
        }

        /**
         * Returns true if the kafka initialization failed in a way that
         * is retryable
         * 
         * @param result
         * @return
         */
        @Override
        public boolean isRetryNeeded(InitializationResult result)
        {

            if (result.isSuccess())
            {
                return false;
            }
            
            for (Class<? extends Throwable> exceptionClass : NON_RETRYABLE_EXCEPTIONS)
            {
                if (result.hasError(exceptionClass))
                {
                    return false;
                }
            }
            return true;
        }

        @Override
        public String getObjectName() {

            return KafkaOpenMetadataTopicConnector.class.getSimpleName() + " for topic " + topicName;
        }
       
    }
    
    // List of exceptions that, when in the cause list, prevent an initialization
    // retry
    private static final List<Class<? extends Throwable>> NON_RETRYABLE_EXCEPTIONS = buildImmutableList(
            // file system exceptions, such as java.nio.file.NoSuchFileException
            // should not trigger a retry. NoSuchFileException is thrown
            // if the Kafka SSL certificate file is not found.
            FileSystemException.class,

            // OMRSLogicErrorException indicates that there is a bug somewhere
            // in the code. We should not retry if that happens.
            OMRSLogicErrorException.class);
    
    private static List<Class<? extends Throwable>> buildImmutableList(Class<? extends Throwable>... items)
    {

        List<Class<? extends Throwable>> result = new ArrayList<>();
        for (Class<? extends Throwable> item : items)
        {
            result.add(item);
        }
        return Collections.unmodifiableList(result);
    }
    
    private void startParent() throws ConnectorCheckedException {
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

        consumer.safeCloseConsumer();
        producer.safeCloseProducer();

        super.disconnect();

        auditLog.logMessage(actionDescription, KafkaOpenMetadataTopicConnectorAuditCode.SERVICE_SHUTDOWN.getMessageDefinition(topicName));
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
