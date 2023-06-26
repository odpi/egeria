/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices;


import org.odpi.openmetadata.frameworks.connectors.ConnectorProvider;

import org.odpi.openmetadata.frameworks.connectors.ffdc.OCFRuntimeException;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.Connection;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.ConnectorType;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.EmbeddedConnection;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.Endpoint;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.VirtualConnection;
import org.odpi.openmetadata.repositoryservices.auditlog.OMRSAuditLogRecordSeverity;
import org.odpi.openmetadata.repositoryservices.connectors.stores.auditlogstore.OMRSAuditLogStoreProviderBase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;



/**
 * ConnectorConfigurationFactory sets up default configuration for the OMRS components.  It is used by the OMAG and UI server
 * while it manages the changes made to the server configuration by the server administrator.  The aim is to
 * build up the RepositoryServicesConfig object that is used to initialize the OMRSOperationalServices.
 */
public class ConnectorConfigurationFactory
{
    /*
     * Default property fillers
     */
    private static final String defaultTopicRootName                 = "openmetadata.repositoryservices.";
    private static final String defaultSingleOMRSTopicLeafName       = ".OMRSTopic";
    private static final String defaultRegistrationOMRSTopicLeafName = ".OMRSTopic.registration";
    private static final String defaultTypesOMRSTopicLeafName        = ".OMRSTopic.types";
    private static final String defaultInstancesOMRSTopicLeafName    = ".OMRSTopic.instances";

    private static final String defaultEnterpriseTopicConnectorRootName = defaultTopicRootName + "enterprise.";
    private static final String defaultCohortTopicConnectorRootName     = defaultTopicRootName + "cohort.";


    private static final String ENCRYPTED_FILE_BASED_SERVER_CONFIG_STORE_PROVIDER          = "org.odpi.openmetadata.adapters.adminservices.configurationstore.encryptedfile.EncryptedFileBasedServerConfigStoreProvider";
    private static final String IN_MEMORY_OPEN_METADATA_TOPIC_PROVIDER                     = "org.odpi.openmetadata.adapters.eventbus.topic.inmemory.InMemoryOpenMetadataTopicProvider";
    private static final String KAFKA_OPEN_METADATA_TOPIC_PROVIDER                         = "org.odpi.openmetadata.adapters.eventbus.topic.kafka.KafkaOpenMetadataTopicProvider";
    private static final String FILE_BASED_OPEN_METADATA_ARCHIVE_STORE_PROVIDER            = "org.odpi.openmetadata.adapters.repositoryservices.archiveconnector.file.FileBasedOpenMetadataArchiveStoreProvider";
    private static final String CONSOLE_AUDIT_LOG_STORE_PROVIDER                           = "org.odpi.openmetadata.adapters.repositoryservices.auditlogstore.console.ConsoleAuditLogStoreProvider";
    private static final String EVENT_TOPIC_AUDIT_LOG_STORE_PROVIDER                       = "org.odpi.openmetadata.adapters.repositoryservices.auditlogstore.eventtopic.EventTopicAuditLogStoreProvider";
    private static final String FILE_BASED_AUDIT_LOG_STORE_PROVIDER                        = "org.odpi.openmetadata.adapters.repositoryservices.auditlogstore.file.FileBasedAuditLogStoreProvider";
    private static final String SLF_4_J_AUDIT_LOG_STORE_PROVIDER                           = "org.odpi.openmetadata.adapters.repositoryservices.auditlogstore.slf4j.SLF4JAuditLogStoreProvider";
    private static final String FILE_BASED_REGISTRY_STORE_PROVIDER                         = "org.odpi.openmetadata.adapters.repositoryservices.cohortregistrystore.file.FileBasedRegistryStoreProvider";
    private static final String GRAPH_OMRS_REPOSITORY_CONNECTOR_PROVIDER                   = "org.odpi.openmetadata.adapters.repositoryservices.graphrepository.repositoryconnector.GraphOMRSRepositoryConnectorProvider";
    private static final String IN_MEMORY_OMRS_REPOSITORY_CONNECTOR_PROVIDER               = "org.odpi.openmetadata.adapters.repositoryservices.inmemory.repositoryconnector.InMemoryOMRSRepositoryConnectorProvider";
    private static final String READ_ONLY_OMRS_REPOSITORY_CONNECTOR_PROVIDER               = "org.odpi.openmetadata.adapters.repositoryservices.readonly.repositoryconnector.ReadOnlyOMRSRepositoryConnectorProvider";
    private static final String OMRSREST_REPOSITORY_CONNECTOR_PROVIDER                     = "org.odpi.openmetadata.adapters.repositoryservices.rest.repositoryconnector.OMRSRESTRepositoryConnectorProvider";
    private static final String OMRS_TOPIC_PROVIDER                                        = "org.odpi.openmetadata.repositoryservices.connectors.omrstopic.OMRSTopicProvider";

    private static final Logger log = LoggerFactory.getLogger(ConnectorConfigurationFactory.class);


    /**
     * Default constructor
     */
    public ConnectorConfigurationFactory()
    {
    }


    /**
     * Returns the connection for the open metadata server configuration file.
     *
     * @param serverName  name of the server
     * @return Connection object
     */
    public Connection getServerConfigConnection(String    serverName)
    {
        Endpoint   endpoint = new Endpoint();
        endpoint.setAddress("data/servers/" + serverName + "/config/" + serverName + ".config");

        Connection connection = new Connection();
        connection.setDisplayName("Encrypted File Config Store Connection");
        connection.setEndpoint(endpoint);
        connection.setConnectorType(getConnectorType(ENCRYPTED_FILE_BASED_SERVER_CONFIG_STORE_PROVIDER));
        connection.setQualifiedName(endpoint.getAddress());

        return connection;
    }

    /**
     * Returns the connection for the open metadata server configuration retrieve all.
     *
     * @return Connection object
     */
   public Connection getServerConfigConnectionForRetrieveAll()
    {
        Connection connection = new Connection();
        connection.setDisplayName("Encrypted File Config Store Connection");
        connection.setConnectorType(getConnectorType(ENCRYPTED_FILE_BASED_SERVER_CONFIG_STORE_PROVIDER));
        return connection;
    }


    /**
     * Return the connection for the default audit log.
     * By default, the Audit Log written to stdout.
     *
     * @return OCF Connection used to create the default audit logger
     */
    public Connection getDefaultAuditLogConnection()
    {
        List<OMRSAuditLogRecordSeverity> supportedSeverityDefinitions = Arrays.asList(OMRSAuditLogRecordSeverity.values());
        List<String>                     supportedSeverities = new ArrayList<>();

        for (OMRSAuditLogRecordSeverity severityDefinition : supportedSeverityDefinitions)
        {
            if ((! OMRSAuditLogRecordSeverity.TRACE.equals(severityDefinition)) &&
                (! OMRSAuditLogRecordSeverity.PERFMON.equals(severityDefinition)))
            {
                supportedSeverities.add(severityDefinition.getName());
            }
        }

        return getConsoleAuditLogConnection("- default", supportedSeverities);
    }


    /**
     * Set up the supportedSeverities property in the audit log destination connection
     *
     * @param supportedSeverities list of supported severities
     * @param auditLogDestination connection object
     */
    private void setSupportedAuditLogSeverities(List<String> supportedSeverities,
                                                Connection   auditLogDestination)
    {
        if (supportedSeverities != null)
        {
            Map<String, Object> configurationProperties = new HashMap<>();

            configurationProperties.put(OMRSAuditLogStoreProviderBase.supportedSeveritiesProperty, supportedSeverities);
            auditLogDestination.setConfigurationProperties(configurationProperties);
        }
    }


    /**
     * Return the connection for the console audit log destination.
     * This audit log destination writes to stdout.
     *
     * @param qualifier unique qualifier for the connection
     * @param supportedSeverities list of severities that should be logged to this destination (empty list means all)
     * @return OCF Connection used to create the stdout console audit logger
     */
   public Connection getConsoleAuditLogConnection(String       qualifier,
                                                  List<String> supportedSeverities)
    {
        final String destinationName = "Console";

        Connection connection = new Connection();

        connection.setQualifiedName(destinationName + qualifier);
        connection.setDisplayName(destinationName);
        connection.setConnectorType(getConnectorType(CONSOLE_AUDIT_LOG_STORE_PROVIDER));

        setSupportedAuditLogSeverities(supportedSeverities, connection);

        return connection;
    }


    /**
     * Return the connection for the file-based audit log.
     * By default, the File-based Audit log is stored in a directory called localServerName.auditlog.
     *
     * @param localServerName   name of the local server
     * @param supportedSeverities list of severities that should be logged to this destination (empty list means all)
     * @return OCF Connection used to create the file based audit logger
     */
    public Connection getFileBasedAuditLogConnection(String       localServerName,
                                                     List<String> supportedSeverities)
    {
        final String destinationName = "Files";

        String endpointAddress = "data/servers/" + localServerName + "/logs/auditlog";

        Endpoint endpoint = new Endpoint();

        endpoint.setAddress(endpointAddress);

        Connection connection = new Connection();

        connection.setQualifiedName(destinationName + " in " + endpointAddress);
        connection.setDisplayName(destinationName + " in " + endpointAddress);
        connection.setEndpoint(endpoint);
        connection.setConnectorType(getConnectorType(FILE_BASED_AUDIT_LOG_STORE_PROVIDER));

        setSupportedAuditLogSeverities(supportedSeverities, connection);

        return connection;
    }


    /**
     * Return the connection for the file-based audit log.
     * By default, the File-based Audit log is stored in a directory called localServerName.auditlog.
     *
     * @param qualifier unique qualifier for the connection
     * @param supportedSeverities list of severities that should be logged to this destination (empty list means all)
     * @return OCF Connection used to create the file based audit logger
     */
    public Connection getSLF4JAuditLogConnection(String       qualifier,
                                                 List<String> supportedSeverities)
    {
        final String destinationName = "SLF4J";

        Connection connection = new Connection();

        connection.setQualifiedName(destinationName + qualifier);
        connection.setDisplayName(destinationName);
        connection.setConnectorType(getConnectorType(SLF_4_J_AUDIT_LOG_STORE_PROVIDER));

        setSupportedAuditLogSeverities(supportedSeverities, connection);

        return connection;
    }


    /**
     * Return the connection for the event topic audit log.
     * By default, the topic name is called openmetadata.repositoryservices.{localServerName}.auditlog.
     *
     * @param localServerName   name of the local server
     * @param supportedSeverities list of severities that should be logged to this destination (empty list means all)
     * @param eventBusConnectorProviderClassName name of connector provider class that controls the type of connector used.
     * @param topicURLRoot  root URL of the topic - this is prepended to the topic name
     * @param serverId identifier of the server - used to pick up the right offset for the inbound messages.
     * @param eventBusConfigurationProperties - additional properties for the event bus connection
     * @return OCF Connection used to create the event topic audit logger
     */
    public Connection getEventTopicAuditLogConnection(String              localServerName,
                                                      List<String>        supportedSeverities,
                                                      String              eventBusConnectorProviderClassName,
                                                      String              topicURLRoot,
                                                      String              serverId,
                                                      Map<String, Object> eventBusConfigurationProperties)
    {
        final String destinationName = "EventTopic";
        String topicName = defaultTopicRootName + localServerName + ".auditlog";

        VirtualConnection connection = new VirtualConnection();

        connection.setQualifiedName(destinationName + " " + topicName);
        connection.setDisplayName(destinationName + " " + topicName);
        connection.setConnectorType(getConnectorType(EVENT_TOPIC_AUDIT_LOG_STORE_PROVIDER));
        connection.setEmbeddedConnections(getEmbeddedEventBusConnection(localServerName + " Audit Log Event Topic Destination",
                                                                        null,
                                                                        eventBusConnectorProviderClassName,
                                                                        topicURLRoot,
                                                                        topicName,
                                                                        serverId,
                                                                        eventBusConfigurationProperties));

        setSupportedAuditLogSeverities(supportedSeverities, connection);

        return connection;
    }


    /**
     * Return the connection for an open metadata archive file.
     *
     * @param fileName name of the archive file
     * @return OCF Connection used to create the file-based open metadata archive
     */
    public Connection getOpenMetadataArchiveFileConnection(String fileName)
    {

        Endpoint endpoint = new Endpoint();

        endpoint.setAddress(fileName);

        Connection connection = new Connection();

        connection.setDisplayName("Open Metadata Archive File " + fileName + " Connection");
        connection.setConnectorType(getConnectorType(FILE_BASED_OPEN_METADATA_ARCHIVE_STORE_PROVIDER));
        connection.setEndpoint(endpoint);

        return connection;
    }


    /**
     * Return the connection to the default registry store called localServerName.cohortName.registrystore.
     *
     * @param localServerName   name of the local server
     * @param cohortName   name of the cohort
     * @return Connection object
     */
    public Connection getDefaultCohortRegistryConnection(String localServerName, String cohortName)
    {
        String endpointAddress = "./data/servers/" + localServerName + "/cohorts/" + cohortName + ".registrystore";

        Endpoint endpoint = new Endpoint();

        endpoint.setAddress(endpointAddress);

        Connection connection = new Connection();

        connection.setDisplayName("Cohort Registry Connection");
        connection.setEndpoint(endpoint);
        connection.setConnectorType(getConnectorType(FILE_BASED_REGISTRY_STORE_PROVIDER));

        return connection;
    }


    /**
     * Return the default local repository's local connection.  This is set to null which means use the remote
     * connection.
     *
     * @return null Connection object
     */
    public Connection getDefaultLocalRepositoryLocalConnection()
    {
        return null;
    }


    /**
     * Return the Connection for this server's OMRS Repository REST API.  If the localServerURL is
     * something like https://localhost:9443/west-domain then the REST API URL would be
     * https://localhost:9443/west-domain/servers/{localServerName}/open-metadata/repository-services/...
     *
     * @param localServerName   name of the local server
     * @param localServerURL   root of the local server's URL
     * @return Connection object
     */
    public  Connection getDefaultLocalRepositoryRemoteConnection(String localServerName,
                                                                 String localServerURL)
    {
        Endpoint endpoint = new Endpoint();

        endpoint.setAddress(localServerURL + "/servers/" + localServerName);

        Connection connection = new Connection();

        connection.setDisplayName("Local Repository Remote Connection");
        connection.setEndpoint(endpoint);
        connection.setConnectorType(getConnectorType(OMRSREST_REPOSITORY_CONNECTOR_PROVIDER));

        return connection;
    }


    /**
     * Return the local graph repository's connection.  This is using the GraphOMRSRepositoryConnector.
     * Note there is no endpoint defined.  This needs to be added when the graph repository connector
     * is implemented.
     *
     * @param storageProperties  properties used to configure Egeria Graph DB
     *
     * @return Connection object
     */
    public Connection getLocalGraphRepositoryLocalConnection(Map<String, Object> storageProperties)
    {
        Connection connection = new Connection();

        connection.setDisplayName("Local Graph Repository");
        connection.setConnectorType(getConnectorType(GRAPH_OMRS_REPOSITORY_CONNECTOR_PROVIDER));
        connection.setConfigurationProperties(storageProperties);

        return connection;
    }


    /**
     * Return the in-memory local repository connection.  This is using the InMemoryOMRSRepositoryConnector.
     *
     * @return Connection object
     */
    public Connection getInMemoryLocalRepositoryLocalConnection()
    {
        Connection connection = new Connection();

        connection.setDisplayName("In Memory Local Repository Connection");
        connection.setConnectorType(getConnectorType(IN_MEMORY_OMRS_REPOSITORY_CONNECTOR_PROVIDER));

        return connection;
    }


    /**
     * Return the read only local repository connection.  This is using the ReadOnlyOMRSRepositoryConnector.
     *
     * @return Connection object
     */
    public Connection getReadOnlyLocalRepositoryLocalConnection()
    {
        Connection connection = new Connection();

        connection.setDisplayName("Read-only Local Repository Connection");
        connection.setConnectorType(getConnectorType(READ_ONLY_OMRS_REPOSITORY_CONNECTOR_PROVIDER));

        return connection;
    }


    /**
     * Returns the connection for an arbitrary repository proxy.
     *
     * @param connectorProviderClassName  class name of the connector provider
     * @param url  location of the repository proxy
     * @param configurationProperties name value pairs for the connection
     * @return Connection object
     * @throws ClassNotFoundException when the provided class cannot be found
     * @throws InstantiationException when the provided class cannot be instantiated
     * @throws IllegalAccessException when there is insufficient access to instantiate the provided class
     * @throws NoSuchMethodException the default constructor of the connector provider is not implemented
     * @throws InvocationTargetException the default constructor of the connector provider can not be called
     */
    public Connection getRepositoryConnection(String              connectorProviderClassName,
                                              String              url,
                                              Map<String, Object> configurationProperties) throws ClassNotFoundException,
                                                                                                  InstantiationException,
                                                                                                  IllegalAccessException,
                                                                                                  NoSuchMethodException,
                                                                                                  InvocationTargetException
    {
        Endpoint endpoint = new Endpoint();

        endpoint.setAddress(url);

        Connection connection = new Connection();

        connection.setDisplayName("Plugin Local Repository Connection");
        connection.setEndpoint(endpoint);
        connection.setConnectorType(getDynamicConnectorType(connectorProviderClassName));
        connection.setConfigurationProperties(configurationProperties);

        return connection;
    }


    /**
     * Return the default local repository event mapper.  This is null since the use of, or need for, the event mapper
     * is determined by the type of local repository.
     *
     * @return null Connection object
     */
    public Connection getDefaultEventMapperConnection()
    {
        return null;
    }


    /**
     * Return the embedded connections for an event based virtual connector
     *
     * @param eventSource display name of the connector
     * @param arguments  override properties for the event bus connection
     * @param eventBusConnectorProviderClassName name of connector provider class that controls the type of connector used.
     * @param topicURLRoot  root URL of the topic - this is prepended to the topic name
     * @param topicName  name of the topic
     * @param serverId identifier of the server - used to pick up the right offset for the inbound messages.
     * @param eventBusConfigurationProperties - additional properties for the event bus connection
     * @return List of EmbeddedConnection object
     */
    private List<EmbeddedConnection> getEmbeddedEventBusConnection(String              eventSource,
                                                                   Map<String, Object> arguments,
                                                                   String              eventBusConnectorProviderClassName,
                                                                   String              topicURLRoot,
                                                                   String              topicName,
                                                                   String              serverId,
                                                                   Map<String, Object> eventBusConfigurationProperties)
    {
        EmbeddedConnection     embeddedConnection = new EmbeddedConnection();
        Connection             connection         = this.getDefaultEventBusConnection(eventBusConnectorProviderClassName,
                                                                                      topicURLRoot,
                                                                                      topicName,
                                                                                      serverId,
                                                                                      eventBusConfigurationProperties);

        embeddedConnection.setDisplayName(eventSource);
        embeddedConnection.setArguments(arguments);
        embeddedConnection.setEmbeddedConnection(connection);

        List<EmbeddedConnection>      embeddedConnections = new ArrayList<>();
        embeddedConnections.add(embeddedConnection);

        return embeddedConnections;
    }


    /**
     * Return the event bus connection substituting default values where they are missing from the event bus
     * configuration.
     *
     * @param connectorProviderClassName name of connector provider class that controls the type of connector used.
     * @param topicURLRoot  root URL of the topic - this is prepended to the topic name
     * @param topicName  name of the topic - if null it means that the connection should be null
     * @param serverId identifier of the server - used to pick up the right offset for the inbound messages.
     * @param configurationProperties  additional configuration properties for the connection
     * @return Connection object
     */
    public Connection getDefaultEventBusConnection(String               connectorProviderClassName,
                                                   String               topicURLRoot,
                                                   String               topicName,
                                                   String               serverId,
                                                   Map<String, Object>  configurationProperties)
    {
        if (topicName != null)
        {
            Endpoint endpoint = new Endpoint();

            if (topicURLRoot == null)
            {
                endpoint.setAddress(topicName);
            }
            else
            {
                endpoint.setAddress(topicURLRoot + "." + topicName);
            }


            String connectorTypeJavaClassName = KAFKA_OPEN_METADATA_TOPIC_PROVIDER;

            if ((connectorProviderClassName != null) && (connectorProviderClassName.length() > 0))
            {
                connectorTypeJavaClassName = connectorProviderClassName;
            }

            if (configurationProperties == null)
            {
                configurationProperties = new HashMap<>();
            }

            // the serverId is used to set the default topic.id (though this could be overridden in the consumer configuration)
            // retrieve from the default if needed
            if (StringUtils.isEmpty((String) configurationProperties.get("local.server.id")))
            {
                configurationProperties.put("local.server.id", serverId);
            }

            Connection connection = new Connection();

            connection.setDisplayName("Kafka Event Bus Connection");
            connection.setEndpoint(endpoint);
            connection.setConnectorType(getConnectorType(connectorTypeJavaClassName));
            connection.setConfigurationProperties(configurationProperties);

            return connection;
        }

        return null;
    }


    /**
     * Return the default connection for the enterprise OMRS topic.  This uses an in-memory event bus connector
     *
     * @param localServerName   name of local server
     * @param serverId identifier of the server - used to pick up the right offset for the inbound messages.
     * @return Connection object
     */
    public Connection getDefaultEnterpriseOMRSTopicConnection(String              localServerName,
                                                              String              serverId)
    {
        String topicName = defaultEnterpriseTopicConnectorRootName + localServerName + defaultSingleOMRSTopicLeafName;

        VirtualConnection connection = new VirtualConnection();

        connection.setDisplayName("Enterprise OMRS Topic Connection");
        connection.setConnectorType(getConnectorType(OMRS_TOPIC_PROVIDER));
        connection.setEmbeddedConnections(getEmbeddedEventBusConnection("Enterprise OMRS Events",
                                                                        null,
                                                                        IN_MEMORY_OPEN_METADATA_TOPIC_PROVIDER,
                                                                        localServerName,
                                                                        topicName,
                                                                        serverId,
                                                                        null));

        return connection;
    }



    /**
     * Return the default connection for the enterprise OMRS topic.  This uses an in-memory event bus connector
     *
     * @param localServerName   name of local server
     * @param configurationProperties name value property pairs for the topic connection
     * @param eventBusConnectorProvider class name of the event bus connector's provider
     * @param topicURLRoot root name for the topic URL
     * @param serverId identifier of the server - used to pick up the right offset for the inbound messages.
     * @param eventBusConfigurationProperties name value property pairs for the event bus connection
     * @return Connection object
     */
    public Connection getDefaultRemoteEnterpriseOMRSTopicConnection(String              localServerName,
                                                                    Map<String, Object> configurationProperties,
                                                                    String              eventBusConnectorProvider,
                                                                    String              topicURLRoot,
                                                                    String              serverId,
                                                                    Map<String, Object> eventBusConfigurationProperties)
    {
        String topicName = defaultEnterpriseTopicConnectorRootName + localServerName + defaultSingleOMRSTopicLeafName;

        VirtualConnection connection = new VirtualConnection();

        connection.setDisplayName("Remote Enterprise OMRS Topic Connection");
        connection.setConnectorType(getConnectorType(OMRS_TOPIC_PROVIDER));
        connection.setConfigurationProperties(configurationProperties);
        connection.setEmbeddedConnections(getEmbeddedEventBusConnection("Remote Enterprise OMRS Events",
                                                                        null,
                                                                        eventBusConnectorProvider,
                                                                        topicURLRoot,
                                                                        topicName,
                                                                        serverId,
                                                                        eventBusConfigurationProperties));

        return connection;
    }


    /**
     * Return the connection for the single OMRS topic for the named cohort.
     *
     * @param cohortName   name of the cohort
     * @param configurationProperties name value property pairs for the topic connection
     * @param eventBusConnectorProvider class name of the event bus connector's provider
     * @param topicURLRoot root name for the topic URL
     * @param serverId identifier of the server - used to pick up the right offset for the inbound messages.
     * @param eventBusConfigurationProperties name value property pairs for the event bus connection
     * @return Connection object
     */
    public Connection getDefaultSingleCohortOMRSTopicConnection(String              cohortName,
                                                                Map<String, Object> configurationProperties,
                                                                String              eventBusConnectorProvider,
                                                                String              topicURLRoot,
                                                                String              serverId,
                                                                Map<String, Object> eventBusConfigurationProperties)
    {
        String topicName = defaultCohortTopicConnectorRootName + cohortName + defaultSingleOMRSTopicLeafName;
        String eventSource = cohortName + " OMRS Topic";

        return this.getDefaultCohortOMRSTopicConnection(topicName,
                                                        configurationProperties,
                                                        eventSource,
                                                        eventBusConnectorProvider,
                                                        topicURLRoot,
                                                        serverId,
                                                        eventBusConfigurationProperties);
    }


    /**
     * Return the connection for the registration OMRS topic for the named cohort.
     *
     * @param cohortName   name of the cohort
     * @param configurationProperties name value property pairs for the topic connection
     * @param eventBusConnectorProvider class name of the event bus connector's provider
     * @param topicURLRoot root name for the topic URL
     * @param eventBusConfigurationProperties name value property pairs for the event bus connection
     * @return Connection object
     */
    public Connection getDefaultRegistrationCohortOMRSTopicConnection(String              cohortName,
                                                                      Map<String, Object> configurationProperties,
                                                                      String              eventBusConnectorProvider,
                                                                      String              topicURLRoot,
                                                                      Map<String, Object> eventBusConfigurationProperties)
    {
        String topicName = defaultCohortTopicConnectorRootName + cohortName + defaultRegistrationOMRSTopicLeafName;
        String eventSource = cohortName + " OMRS Topic for registrations";

        return this.getDefaultCohortOMRSTopicConnection(topicName,
                                                        configurationProperties,
                                                        eventSource,
                                                        eventBusConnectorProvider,
                                                        topicURLRoot,
                                                        UUID.randomUUID().toString(),
                                                        eventBusConfigurationProperties);
    }


    /**
     * Return the connection for the registration OMRS topic for the named cohort.
     *
     * @param cohortName   name of the cohort
     * @param configurationProperties name value property pairs for the topic connection
     * @param eventBusConnectorProvider class name of the event bus connector's provider
     * @param topicURLRoot root name for the topic URL
     * @param eventBusConfigurationProperties name value property pairs for the event bus connection
     * @return Connection object
     */
    public Connection getDefaultTypesCohortOMRSTopicConnection(String              cohortName,
                                                               Map<String, Object> configurationProperties,
                                                               String              eventBusConnectorProvider,
                                                               String              topicURLRoot,
                                                               Map<String, Object> eventBusConfigurationProperties)
    {
        String topicName = defaultCohortTopicConnectorRootName + cohortName + defaultTypesOMRSTopicLeafName;
        String eventSource = cohortName + " OMRS Topic for types";

        return this.getDefaultCohortOMRSTopicConnection(topicName,
                                                        configurationProperties,
                                                        eventSource,
                                                        eventBusConnectorProvider,
                                                        topicURLRoot,
                                                        UUID.randomUUID().toString(),
                                                        eventBusConfigurationProperties);
    }


    /**
     * Return the connection for the single OMRS topic for the named cohort.
     *
     * @param cohortName   name of the cohort
     * @param configurationProperties name value property pairs for the topic connection
     * @param eventBusConnectorProvider class name of the event bus connector's provider
     * @param topicURLRoot root name for the topic URL
     * @param serverId identifier of the server - used to pick up the right offset for the inbound messages.
     * @param eventBusConfigurationProperties name value property pairs for the event bus connection
     * @return Connection object
     */
    public Connection getDefaultInstancesCohortOMRSTopicConnection(String              cohortName,
                                                                   Map<String, Object> configurationProperties,
                                                                   String              eventBusConnectorProvider,
                                                                   String              topicURLRoot,
                                                                   String              serverId,
                                                                   Map<String, Object> eventBusConfigurationProperties)
    {
        String topicName = defaultCohortTopicConnectorRootName + cohortName + defaultInstancesOMRSTopicLeafName;
        String eventSource = cohortName + " OMRS Topic for instances";

        return this.getDefaultCohortOMRSTopicConnection(topicName,
                                                        configurationProperties,
                                                        eventSource,
                                                        eventBusConnectorProvider,
                                                        topicURLRoot,
                                                        serverId,
                                                        eventBusConfigurationProperties);
    }


    /**
     * Return the connection for the registration OMRS topic for the named cohort.
     *
     * @param topicName   name of the topic in the cohort
     * @param configurationProperties name value property pairs for the topic connection
     * @param eventSourceName name of the event source
     * @param eventBusConnectorProvider class name of the event bus connector's provider
     * @param topicURLRoot root name for the topic URL
     * @param consumerGroupId identifier of the consumer group - used to pick up the right offset for the inbound messages.
     * @param eventBusConfigurationProperties name value property pairs for the event bus connection
     * @return Connection object
     */
    public Connection getDefaultCohortOMRSTopicConnection(String              topicName,
                                                          Map<String, Object> configurationProperties,
                                                          String              eventSourceName,
                                                          String              eventBusConnectorProvider,
                                                          String              topicURLRoot,
                                                          String              consumerGroupId,
                                                          Map<String, Object> eventBusConfigurationProperties)
    {
        VirtualConnection connection = new VirtualConnection();

        connection.setDisplayName("Cohort OMRS Topic Connection for " + eventSourceName);
        connection.setConnectorType(getConnectorType(OMRS_TOPIC_PROVIDER));
        connection.setConfigurationProperties(configurationProperties);
        connection.setEmbeddedConnections(getEmbeddedEventBusConnection(eventSourceName,
                                                                        null,
                                                                        eventBusConnectorProvider,
                                                                        topicURLRoot,
                                                                        topicName,
                                                                        consumerGroupId,
                                                                        eventBusConfigurationProperties));

        return connection;
    }


    /**
     * Return a connection for a repository event mapper.  Notice that this event mapper is not the
     * open metadata virtual connector because this event mapper is interfacing with external technology.
     *
     * @param connectorProviderClassName  java class name of the connector provider for the event mapper
     * @param configurationProperties additional properties for event mapper
     * @param eventSource  name of the event source used by the event mapper
     * @return Connection object
     * @throws ClassNotFoundException when the provided class cannot be found
     * @throws InstantiationException when the provided class cannot be instantiated
     * @throws IllegalAccessException when there is insufficient access to instantiate the provided class
     * @throws NoSuchMethodException the default constructor of the connector provider is not implemented
     * @throws InvocationTargetException the default constructor of the connector provider can not be called
     */
    public Connection getRepositoryEventMapperConnection(String              connectorProviderClassName,
                                                         Map<String, Object> configurationProperties,
                                                         String              eventSource) throws ClassNotFoundException,
                                                                                                 InstantiationException,
                                                                                                 IllegalAccessException,
                                                                                                 NoSuchMethodException,
                                                                                                 InvocationTargetException
    {
        Endpoint endpoint = new Endpoint();

        endpoint.setAddress(eventSource);

        Connection connection = new Connection();

        connection.setDisplayName("Repository Event Mapper Connection");
        connection.setEndpoint(endpoint);
        connection.setConnectorType(getDynamicConnectorType(connectorProviderClassName));
        connection.setConfigurationProperties(configurationProperties);

        return connection;
    }


    /**
     * Return the connector type for the requested connector provider.  This is best used for connector providers that
     * can return their own connector type.  Otherwise, it makes one up.  This method should only be used for connector
     * providers that are known at compile-time, not those that are only determined at runtime.
     *
     * @param connectorProviderClassName name of the connector provider class
     * @return ConnectorType bean
     */
    private ConnectorType getConnectorType(String connectorProviderClassName)
    {
        final String methodName = "getConnectorType";

        try
        {
            return getDynamicConnectorType(connectorProviderClassName);
        }
        catch (Exception classException)
        {
            log.error("Bad connectorProviderClassName: " + classException.getMessage());
            throw new OCFRuntimeException(ConnectorConfigurationFactoryErrorCode.INVALID_CONNECTOR_PROVIDER.getMessageDefinition(connectorProviderClassName,
                                                                                                                                 classException.getClass().getName(),
                                                                                                                                 classException.getMessage()),
                                          this.getClass().getName(),
                                          methodName,
                                          classException);
        }
    }


    /**
     * Return the connector type for the requested connector provider.  This is best used for connector providers that
     * can return their own connector type.  Otherwise, it makes one up.  This method is useful for connector providers
     * that are defined at runtime rather than compile-time.
     *
     * @param connectorProviderClassName name of the connector provider class
     * @return ConnectorType bean
     * @throws ClassNotFoundException when the provided class cannot be found
     * @throws InstantiationException when the provided class cannot be instantiated
     * @throws IllegalAccessException when there is insufficient access to instantiate the provided class
     */
    private ConnectorType getDynamicConnectorType(String connectorProviderClassName) throws ClassNotFoundException,
                                                                                            InstantiationException,
                                                                                            IllegalAccessException,
                                                                                            NoSuchMethodException,
                                                                                            InvocationTargetException
    {
        ConnectorType  connectorType = null;

        if (connectorProviderClassName != null)
        {
                Class<?>   connectorProviderClass = Class.forName(connectorProviderClassName);
                Object     potentialConnectorProvider = connectorProviderClass.getDeclaredConstructor().newInstance();

                ConnectorProvider  connectorProvider = (ConnectorProvider)potentialConnectorProvider;

                connectorType = connectorProvider.getConnectorType();

                if (connectorType == null)
                {
                    connectorType = new ConnectorType();

                    connectorType.setConnectorProviderClassName(connectorProviderClassName);
                }
        }

        return connectorType;
    }
}
