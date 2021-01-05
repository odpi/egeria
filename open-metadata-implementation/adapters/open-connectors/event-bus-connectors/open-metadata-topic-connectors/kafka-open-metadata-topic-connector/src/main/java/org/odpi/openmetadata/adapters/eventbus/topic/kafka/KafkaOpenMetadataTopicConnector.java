/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.eventbus.topic.kafka;

import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.DescribeClusterResult;
import org.apache.kafka.clients.admin.KafkaAdminClient;
import org.apache.kafka.common.Node;
import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectorCheckedException;
import org.odpi.openmetadata.frameworks.connectors.properties.EndpointProperties;
import org.odpi.openmetadata.repositoryservices.connectors.openmetadatatopic.IncomingEvent;
import org.odpi.openmetadata.repositoryservices.connectors.openmetadatatopic.OpenMetadataTopicConnector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * KafkaOMRSTopicConnector provides a concrete implementation of the OMRSTopicConnector that
 * uses native Apache Kafka as the event/messaging infrastructure.
 */
public class KafkaOpenMetadataTopicConnector extends OpenMetadataTopicConnector
{
    static final String ENABLE_AUTO_COMMIT_PROPERTY = "enable.auto.commit";

    private static final Logger       log      = LoggerFactory.getLogger(KafkaOpenMetadataTopicConnector.class);

    
    private final Properties producerProperties = new Properties();
    private final Properties consumerEgeriaProperties = new Properties();
    private final Properties consumerProperties = new Properties();


    private KafkaOpenMetadataEventConsumer consumer = null;
    private KafkaOpenMetadataEventProducer producer = null;

    private String       topicName          = null;
    private String       serverId           = null;

    /* this buffer is for consumed events */
    private final List<IncomingEvent> incomingEventsList = Collections.synchronizedList(new ArrayList<>());

    private KafkaProducerExecutor executor = null;

    final String                   threadHeader = "Kafka-";
    Thread                         consumerThread;
    Thread                         producerThread;


    /* mock up a a SingleThreadProducer with an override for afterExecute */
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
        producerProperties.put("bring.up.retries", "10");
        producerProperties.put("bring.up.minSleepTime", "5000");


        consumerProperties.put("bootstrap.servers", "localhost:9092");
        consumerProperties.put(ENABLE_AUTO_COMMIT_PROPERTY, "true");
        consumerProperties.put("auto.commit.interval.ms", "1000");
        consumerProperties.put("session.timeout.ms", "30000");
        consumerProperties.put("max.partition.fetch.bytes",	10485760);
        consumerProperties.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        consumerProperties.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        consumerProperties.put("bring.up.retries", "10");
        consumerProperties.put("bring.up.minSleepTime", "5000");
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
    @Override
    public void start() throws ConnectorCheckedException
    {

        this.initializeTopic();

        KafkaStatusChecker kafkaStatus = new KafkaStatusChecker();

        /*
        *  compare the two lists of bootstrap server to check we only want one cluster's status checked
        *  reliant on list being specified in the same order
         * This will need changing for single direction connector
         */
        boolean up = false;
        if ( consumerProperties.getProperty("bootstrap.servers").equals(producerProperties.getProperty("bootstrap.servers")) ) {
            up = kafkaStatus.waitForBrokers( producerProperties);
        }
        else {
            up = kafkaStatus.waitForBrokers( producerProperties) &&
                    kafkaStatus.waitForBrokers(consumerProperties);
        }

        if (!up) {
            final String actionDescription = "waitForThisBroker";
            if (auditLog != null)
            {
                auditLog.logMessage(actionDescription, KafkaOpenMetadataTopicConnectorAuditCode.SERVICE_FAILED_INITIALIZING.getMessageDefinition(topicName));
            }
            throw new ConnectorCheckedException(KafkaOpenMetadataTopicConnectorErrorCode.ERROR_ATTEMPTING_KAFKA_INITIALIZATION.getMessageDefinition(kafkaStatus.getLastException().getClass().getName(),
                    topicName,
                    kafkaStatus.getLastException().getMessage()),
                    this.getClass().getName(),
                    "KafkaMonitor.waitForThisBroker",
                    kafkaStatus.getLastException());
        }

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
    @Override
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
            log.debug("Checking for events.  Number of found events: {}", incomingEventsList.size());
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
    @Override
    public void disconnect() throws ConnectorCheckedException
    {
        final String           actionDescription = "disconnect";


        consumer.safeCloseConsumer();
        producer.safeCloseProducer();

        /*
        * Ensure Kafka client threads have stopped
        * before returning.
         */
        try {
            consumerThread.join();
        }
        catch ( InterruptedException e )
        {
            //expected exception and don't care
        }
        catch ( Exception error ) {
            if (auditLog != null)
            {
                final String command = "consumerThread.join";
                auditLog.logException(actionDescription,
                        KafkaOpenMetadataTopicConnectorAuditCode.UNEXPECTED_SHUTDOWN_EXCEPTION.getMessageDefinition(error.getClass().getName(),
                                                                                                                    topicName,
                                                                                                                    command,
                                                                                                                    error.getMessage()),
                                      error);
            }
        }

        try {
            producerThread.join();
        } catch (InterruptedException e) {
            //expected and don't care
        }
        catch ( Exception error ){
            if (auditLog != null)
            {
                final String command = "producerThread.join";

                auditLog.logException(actionDescription,
                        KafkaOpenMetadataTopicConnectorAuditCode.UNEXPECTED_SHUTDOWN_EXCEPTION.getMessageDefinition(error.getClass().getName(),
                                                                                                                    topicName,
                                                                                                                    command,
                                                                                                                    error.getMessage()),
                                      error);
            }

        }

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

    private class KafkaStatusChecker {

        //instantiate empty objects to avoid if null checks
        // the brokers list if for debug/testing purposes only,
        private Exception lastException = new Exception();

        /*
         * connects and requests a list of running kafka brokers
         * swallows all exceptions to allow caller to control error reporting,
         *
         * If this function populates brokers then:
         * Zookeeper is running
         * Kafka has a least one server which is running at least one broker
         *
         * @param connectionProps The properties to enable connection to kafka
         */
        boolean getRunningBrokers(Properties connectionProperties ) {

            boolean found = false;
            try {
                AdminClient adminClient = KafkaAdminClient.create(connectionProperties);
                DescribeClusterResult describeClusterResult = adminClient.describeCluster();
                Collection<Node> brokers = describeClusterResult.nodes().get();
                if (!brokers.isEmpty()) {
                    found = true;
                }
            } catch (Exception e) {
                //gulp down any exceptions, the waiting method will control any audit logging
                //but keep a copy for reference
                lastException = e;
            }

            return found;
        }


        /*
         * waits for a list of running kafaka brokers
         * @param connectionProperties The kafka connection properties
         * Performs a sanity check that the services Egeria requires are available
         *         */
        public boolean waitForBrokers(Properties connectionProperties ) {
            int count = 0;
            boolean found = false;
            try {
                int napCount = Integer.parseInt( connectionProperties.getProperty("bring.up.retries") );
                int minSleepTime = Integer.parseInt( connectionProperties.getProperty("bring.up.minSleepTime")) ;

                while (count < napCount) {

                    auditLog.logMessage("waitForBrokers", KafkaOpenMetadataTopicConnectorAuditCode.KAFKA_CONNECTION_RETRY.getMessageDefinition(String.valueOf(count+1)));
                    Instant start = Instant.now();
                    if (getRunningBrokers(connectionProperties)) {
                        //we were returned a list of running brokers
                        //so end retry loop
                        found = true;
                        break;
                    } else {
                        count++;
                        /* This provides a minimum sleeptime functionality
                        *  to allow the ride through of short term availablity issues
                         */
                        Instant end = Instant.now();
                        Duration timeTaken = Duration.between(start, end);
                        long millis = timeTaken.toMillis();

                        if (millis < minSleepTime)
                        {
                            Thread.sleep(minSleepTime - millis);
                        }
                    }
                }
            } catch (Exception e) {
                lastException = e;
            }
            return found;
        }

        public Exception getLastException() {

            return lastException;
        }
    }
}