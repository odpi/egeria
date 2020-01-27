/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices;

import org.odpi.openmetadata.adapters.adminservices.configurationstore.file.FileBasedUIServerConfigStoreProvider;
import org.odpi.openmetadata.adapters.repositoryservices.auditlogstore.console.ConsoleAuditLogStoreProvider;
import org.odpi.openmetadata.adapters.repositoryservices.auditlogstore.eventtopic.EventTopicAuditLogStoreProvider;
import org.odpi.openmetadata.adapters.repositoryservices.auditlogstore.file.FileBasedAuditLogStoreProvider;
import org.odpi.openmetadata.adapters.repositoryservices.auditlogstore.slf4j.SLF4JAuditLogStoreProvider;
import org.odpi.openmetadata.openconnector.governancedarmonconnectors.securityofficerconnectors.securitytagconnector.SecurityTagConnectorProvider;
import org.odpi.openmetadata.openconnectors.governancedaemonconnectors.securitysync.rangerconnector.RangerSecurityServiceConnectorProvider;
import org.odpi.openmetadata.repositoryservices.connectors.stores.auditlogstore.OMRSAuditLogStoreProviderBase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.odpi.openmetadata.adapters.adminservices.configurationstore.file.FileBasedServerConfigStoreProvider;
import org.odpi.openmetadata.adapters.eventbus.topic.inmemory.InMemoryOpenMetadataTopicProvider;
import org.odpi.openmetadata.adapters.eventbus.topic.kafka.KafkaOpenMetadataTopicProvider;
import org.odpi.openmetadata.adapters.repositoryservices.archiveconnector.file.FileBasedOpenMetadataArchiveStoreProvider;
import org.odpi.openmetadata.adapters.repositoryservices.cohortregistrystore.file.FileBasedRegistryStoreProvider;
import org.odpi.openmetadata.adapters.repositoryservices.graphrepository.repositoryconnector.GraphOMRSRepositoryConnectorProvider;
import org.odpi.openmetadata.adapters.repositoryservices.inmemory.repositoryconnector.InMemoryOMRSRepositoryConnectorProvider;
import org.odpi.openmetadata.adapters.repositoryservices.rest.repositoryconnector.OMRSRESTRepositoryConnectorProvider;
import org.odpi.openmetadata.frameworks.connectors.ConnectorProvider;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.*;
import org.odpi.openmetadata.repositoryservices.connectors.omrstopic.OMRSTopicProvider;
import org.odpi.openmetadata.governanceservers.virtualizationservices.viewgenerator.utils.ConnectorClassName;


import java.util.*;


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
    private static final String defaultTopicRootName     = "openmetadata.repositoryservices.";
    private static final String defaultOMRSTopicLeafName = ".OMRSTopic";

    private static final String defaultEnterpriseTopicConnectorRootName = defaultTopicRootName + "enterprise.";
    private static final String defaultCohortTopicConnectorRootName     = defaultTopicRootName + "cohort.";

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
        endpoint.setAddress("omag.server." + serverName + ".config");

        Connection connection = new Connection();
        connection.setEndpoint(endpoint);
        connection.setConnectorType(getConnectorType(FileBasedServerConfigStoreProvider.class.getName()));
        connection.setQualifiedName(endpoint.getAddress());

        return connection;
    }


    /**
     * Returns the connection for the user interface server configuration file.
     *
     * @param serverName  name of the server
     * @return Connection object
     */
    public Connection getUIServerConfigConnection(String serverName)
    {
        Endpoint   endpoint = new Endpoint();
        endpoint.setAddress("ui.server." + serverName + ".config");

        Connection connection = new Connection();
        connection.setEndpoint(endpoint);
        connection.setConnectorType(getConnectorType(FileBasedUIServerConfigStoreProvider.class.getName()));
        connection.setQualifiedName(endpoint.getAddress());

        return connection;
    }


    /**
     * Return the connection for the default audit log.
     * By default, the Audit log written to stdout.
     *
     * @param localServerName   name of the local server
     * @return OCF Connection used to create the default audit logger
     */
    public Connection getDefaultAuditLogConnection(String localServerName)
    {
        return getConsoleAuditLogConnection(localServerName, null);
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
     * @param localServerName   name of the local server
     * @param supportedSeverities list of severities that should be logged to this destination (empty list means all)
     * @return OCF Connection used to create the stdout console audit logger
     */
    public Connection getConsoleAuditLogConnection(String       localServerName,
                                                   List<String> supportedSeverities)
    {
        final String connectionGUID    = "5390bf3e-6b38-4eda-b34a-de55ac4252a7";

        final String connectionDescription = "OMRS console audit log destination connection.";

        String connectionName = "DefaultAuditLog.Connection." + localServerName;

        Connection connection = new Connection();

        connection.setType(this.getConnectionType());
        connection.setGUID(connectionGUID);
        connection.setQualifiedName(connectionName);
        connection.setDisplayName(connectionName);
        connection.setDescription(connectionDescription);
        connection.setConnectorType(getConnectorType(ConsoleAuditLogStoreProvider.class.getName()));

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
        final String endpointGUID      = "7e80da06-065f-484e-a1da-427619f6bd1a";
        final String connectionGUID    = "3e3eaf36-bd15-4aa3-9c98-a5441a679de9";

        final String endpointDescription = "OMRS file based audit log destination endpoint.";

        String endpointAddress = localServerName + ".auditlog";
        String endpointName    = "AuditLogDestination.File.Endpoint." + endpointAddress;

        Endpoint endpoint = new Endpoint();

        endpoint.setType(this.getEndpointType());
        endpoint.setGUID(endpointGUID);
        endpoint.setQualifiedName(endpointName);
        endpoint.setDisplayName(endpointName);
        endpoint.setDescription(endpointDescription);
        endpoint.setAddress(endpointAddress);

        final String connectionDescription = "OMRS file based audit log destination connection.";

        String connectionName = "AuditLogDestination.File.Connection." + endpointAddress;

        Connection connection = new Connection();

        connection.setType(this.getConnectionType());
        connection.setGUID(connectionGUID);
        connection.setQualifiedName(connectionName);
        connection.setDisplayName(connectionName);
        connection.setDescription(connectionDescription);
        connection.setEndpoint(endpoint);
        connection.setConnectorType(getConnectorType(FileBasedAuditLogStoreProvider.class.getName()));

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
    public Connection getSLF4JAuditLogConnection(String       localServerName,
                                                 List<String> supportedSeverities)
    {
        final String connectionGUID    = "3e3eaf36-bd15-4aa3-9c98-a5441a679de9";
        final String connectionDescription = "OMRS SLF4J based audit log destination connection.";

        String connectionName = "AuditLogDestination.SLF4J.Connection." + localServerName;

        Connection connection = new Connection();

        connection.setType(this.getConnectionType());
        connection.setGUID(connectionGUID);
        connection.setQualifiedName(connectionName);
        connection.setDisplayName(connectionName);
        connection.setDescription(connectionDescription);
        connection.setConnectorType(getConnectorType(SLF4JAuditLogStoreProvider.class.getName()));

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
     * @return List of EmbeddedConnection object
     * @return OCF Connection used to create the event topic audit logger
     */
    public Connection getEventTopicAuditLogConnection(String              localServerName,
                                                      List<String>        supportedSeverities,
                                                      String              eventBusConnectorProviderClassName,
                                                      String              topicURLRoot,
                                                      String              serverId,
                                                      Map<String, Object> eventBusConfigurationProperties)
    {
        final String connectionGUID    = "2ac9b527-7585-4a0f-a0d8-6e684b15be21";

        String topicName = defaultTopicRootName + localServerName + ".auditlog";

        final String connectionDescription = "OMRS event topic audit log destination connection.";

        String connectionName = "AuditLogDestination.EventTopic.Connection." + topicName;

        VirtualConnection connection = new VirtualConnection();

        connection.setType(this.getConnectionType());
        connection.setGUID(connectionGUID);
        connection.setQualifiedName(connectionName);
        connection.setDisplayName(connectionName);
        connection.setDescription(connectionDescription);
        connection.setConnectorType(getConnectorType(EventTopicAuditLogStoreProvider.class.getName()));
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
     * Return the connection for a open metadata archive file.
     *
     * @param fileName name of the archive file
     * @return OCF Connection used to create the file-based open metadata archive
     */
    public Connection getOpenMetadataArchiveFileConnection(String fileName)
    {
        final String endpointGUID      = "45877b9c-9192-44ba-a2b7-6817bc753969";
        final String connectionGUID    = "447bbb33-84f9-4a56-a712-addeebdcd764";

        final String endpointDescription = "Open metadata archive for " + fileName;

        String endpointName    = "OpenMetadataArchiveFile.Endpoint" + fileName;

        Endpoint endpoint = new Endpoint();

        endpoint.setType(this.getEndpointType());
        endpoint.setGUID(endpointGUID);
        endpoint.setQualifiedName(endpointName);
        endpoint.setDisplayName(endpointName);
        endpoint.setDescription(endpointDescription);
        endpoint.setAddress(fileName);

        final String connectionDescription = "Open metadata archive connection.";

        String connectionName = "OpenMetadataArchive.Connection." + fileName;

        Connection connection = new Connection();

        connection.setType(this.getConnectionType());
        connection.setGUID(connectionGUID);
        connection.setQualifiedName(connectionName);
        connection.setDisplayName(connectionName);
        connection.setDescription(connectionDescription);
        connection.setConnectorType(getConnectorType(FileBasedOpenMetadataArchiveStoreProvider.class.getName()));
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
        final String endpointGUID      = "8bf8f5fa-b5d8-40e1-a00e-e4a0c59fd6c0";
        final String connectionGUID    = "b9af734f-f005-4085-9975-bf46c67a099a";

        final String endpointDescription = "OMRS default cohort registry endpoint.";

        String endpointAddress = localServerName + "." + cohortName + ".registrystore";
        String endpointName    = "DefaultCohortRegistry.Endpoint." + endpointAddress;

        Endpoint endpoint = new Endpoint();

        endpoint.setType(this.getEndpointType());
        endpoint.setGUID(endpointGUID);
        endpoint.setQualifiedName(endpointName);
        endpoint.setDisplayName(endpointName);
        endpoint.setDescription(endpointDescription);
        endpoint.setAddress(endpointAddress);

        final String connectionDescription = "OMRS default cohort registry connection.";

        String connectionName = "DefaultCohortRegistry.Connection." + localServerName + "." + cohortName;

        Connection connection = new Connection();

        connection.setType(this.getConnectionType());
        connection.setGUID(connectionGUID);
        connection.setQualifiedName(connectionName);
        connection.setDisplayName(connectionName);
        connection.setDescription(connectionDescription);
        connection.setEndpoint(endpoint);
        connection.setConnectorType(getConnectorType(FileBasedRegistryStoreProvider.class.getName()));

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
     * something like localhost:8080/west-domain then the REST API URL would be
     * localhost:8080/west-domain/servers/{localServerName}/open-metadata/repository-services/...
     *
     * @param repositoryName   name of the local repository
     * @param localServerName   name of the local server
     * @param localServerURL   root of the local server's URL
     * @return Connection object
     */
    public  Connection getDefaultLocalRepositoryRemoteConnection(String repositoryName,
                                                                 String localServerName,
                                                                 String localServerURL)
    {
        final String endpointGUID      = "cee85898-43aa-4af5-9bbd-2bed809d1acb";
        final String connectionGUID    = "858be98b-49d2-4ccf-9b23-01085a5f473f";

        final String endpointDescription = "OMRS default repository REST API endpoint.";

        String endpointName    = "DefaultRepositoryRESTAPI.Endpoint." + localServerName;

        Endpoint endpoint = new Endpoint();

        endpoint.setType(this.getEndpointType());
        endpoint.setGUID(endpointGUID);
        endpoint.setQualifiedName(endpointName);
        endpoint.setDisplayName(localServerName);
        endpoint.setDescription(endpointDescription);
        endpoint.setAddress(localServerURL + "/servers/" + localServerName);

        final String connectionDescription = "OMRS default repository REST API connection.";

        String connectionName = "Remote connection to " + repositoryName + "@" + localServerName;

        Connection connection = new Connection();

        connection.setType(this.getConnectionType());
        connection.setGUID(connectionGUID);
        connection.setQualifiedName(connectionName);
        connection.setDisplayName(repositoryName + "@" + localServerName);
        connection.setDescription(connectionDescription);
        connection.setEndpoint(endpoint);
        connection.setConnectorType(getConnectorType(OMRSRESTRepositoryConnectorProvider.class.getName()));

        return connection;
    }


    /**
     * Return the local graph repository's connection.  This is using the GraphOMRSRepositoryConnector.
     * Note there is no endpoint defined.  This needs to be added when the graph repository connector
     * is implemented.
     *
     * @param repositoryName   name of the repository
     * @param localServerName   name of the local server
     * @return Connection object
     */
    public Connection getLocalGraphRepositoryLocalConnection(String repositoryName,
                                                             String localServerName)
    {
        final String connectionGUID    = "3f1fd4fc-90f9-436a-8e2c-2120d590f5e4";

        final String connectionDescription = "OMRS default local graph repository connection.";

        String connectionName = "Local connection to " + repositoryName + "@" + localServerName;

        Connection connection = new Connection();

        connection.setType(this.getConnectionType());
        connection.setGUID(connectionGUID);
        connection.setQualifiedName(connectionName);
        connection.setDisplayName(repositoryName + "@" + localServerName);
        connection.setDescription(connectionDescription);
        connection.setConnectorType(getConnectorType(GraphOMRSRepositoryConnectorProvider.class.getName()));

        return connection;
    }


    /**
     * Return the in-memory local repository connection.  This is using the InMemoryOMRSRepositoryConnector.
     *
     * @param repositoryName   name of the repository
     * @param localServerName   name of the local server
     * @return Connection object
     */
    public Connection getInMemoryLocalRepositoryLocalConnection(String repositoryName,
                                                                String localServerName)
    {
        final String connectionGUID    = "6a3c07b0-0e04-42dc-bcc6-392609bf1d02";

        final String connectionDescription = "OMRS default in memory local repository connection.";

        String connectionName = "Local connection to " + repositoryName + "@" + localServerName;

        Connection connection = new Connection();

        connection.setType(this.getConnectionType());
        connection.setGUID(connectionGUID);
        connection.setQualifiedName(connectionName);
        connection.setDisplayName(repositoryName + "@" + localServerName);
        connection.setDescription(connectionDescription);
        connection.setConnectorType(getConnectorType(InMemoryOMRSRepositoryConnectorProvider.class.getName()));

        return connection;
    }


    /**
     * Returns the connection for an arbitrary repository proxy.
     *
     * @param serverName  name of the real repository server
     * @param connectorProviderClassName  class name of the connector provider
     * @param url  location of the repository proxy
     * @param configurationProperties name value pairs for the connection
     * @return Connection object
     */
    public Connection  getRepositoryProxyConnection(String              serverName,
                                                    String              connectorProviderClassName,
                                                    String              url,
                                                    Map<String, Object> configurationProperties)
    {
        final String endpointGUID             = UUID.randomUUID().toString();
        final String connectionGUID           = UUID.randomUUID().toString();
        final String endpointDescription      = "Metadata repository native endpoint.";
        final String connectionDescription    = "Metadata repository native connection.";


        String endpointName    = "MetadataRepositoryNative.Endpoint." + serverName;

        Endpoint endpoint = new Endpoint();

        endpoint.setType(this.getEndpointType());
        endpoint.setGUID(endpointGUID);
        endpoint.setQualifiedName(endpointName);
        endpoint.setDisplayName(endpointName);
        endpoint.setDescription(endpointDescription);
        endpoint.setAddress(url);

        String connectionName = "MetadataRepositoryNative.Connection." + serverName;

        Connection connection = new Connection();

        connection.setType(this.getConnectionType());
        connection.setGUID(connectionGUID);
        connection.setQualifiedName(connectionName);
        connection.setDisplayName(connectionName);
        connection.setDescription(connectionDescription);
        connection.setEndpoint(endpoint);
        connection.setConnectorType(getConnectorType(connectorProviderClassName));
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
        Connection             connection         = this.getDefaultEventBusConnection(eventSource,
                                                                                      eventBusConnectorProviderClassName,
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
     * @param connectionName  name to use in the connection object
     * @param connectorProviderClassName name of connector provider class that controls the type of connector used.
     * @param topicURLRoot  root URL of the topic - this is prepended to the topic name
     * @param topicName  name of the topic
     * @param serverId identifier of the server - used to pick up the right offset for the inbound messages.
     * @param configurationProperties  additional configuration properties for the connection
     * @return Connection object
     */
    public Connection getDefaultEventBusConnection(String               connectionName,
                                                   String               connectorProviderClassName,
                                                   String               topicURLRoot,
                                                   String               topicName,
                                                   String               serverId,
                                                   Map<String, Object>  configurationProperties)
    {
        Endpoint endpoint = null;

        if (topicName != null)
        {
            endpoint = new Endpoint();

            endpoint.setType(this.getEndpointType());
            endpoint.setGUID(UUID.randomUUID().toString());
            endpoint.setQualifiedName(topicName);
            endpoint.setDisplayName(topicName);
            endpoint.setDescription(connectionName);

            if (topicURLRoot == null)
            {
                endpoint.setAddress(topicName);
            }
            else
            {
                endpoint.setAddress(topicURLRoot + "." + topicName);
            }
        }

        String connectorTypeJavaClassName = KafkaOpenMetadataTopicProvider.class.getName();

        if (connectorProviderClassName != null)
        {
            connectorTypeJavaClassName = connectorProviderClassName;
        }

        if (configurationProperties == null)
        {
            configurationProperties = new HashMap<>();
        }

        configurationProperties.put(KafkaOpenMetadataTopicProvider.serverIdPropertyName, serverId);

        Connection connection = new Connection();

        connection.setType(this.getConnectionType());
        connection.setGUID(UUID.randomUUID().toString());
        connection.setQualifiedName(connectionName);
        connection.setDisplayName(connectionName);
        connection.setDescription(connectionName);
        connection.setEndpoint(endpoint);
        connection.setConnectorType(getConnectorType(connectorTypeJavaClassName));
        connection.setConfigurationProperties(configurationProperties);

        return connection;
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
        final String connectionGUID    = "2084ee90-717b-49a1-938e-8f9d49567b8e";
        final String connectionDescription = "OMRS default enterprise topic connection.";

        String topicName = defaultEnterpriseTopicConnectorRootName + localServerName + defaultOMRSTopicLeafName;
        String connectionName = "EnterpriseTopicConnector.Server." + localServerName;

        VirtualConnection connection = new VirtualConnection();

        connection.setType(this.getVirtualConnectionType());
        connection.setGUID(connectionGUID);
        connection.setQualifiedName(connectionName);
        connection.setDisplayName(connectionName);
        connection.setDescription(connectionDescription);
        connection.setConnectorType(getConnectorType(OMRSTopicProvider.class.getName()));
        connection.setEmbeddedConnections(getEmbeddedEventBusConnection("Enterprise OMRS Events",
                                                                        null,
                                                                        InMemoryOpenMetadataTopicProvider.class.getName(),
                                                                        localServerName,
                                                                        topicName,
                                                                        serverId,
                                                                        null));

        return connection;
    }


    /**
     * Return the connection for the OMRS topic for the named cohort.
     *
     * @param cohortName   name of the cohort
     * @param configurationProperties name value property pairs for the topic connection
     * @param eventBusConnectorProvider class name of the event bus connector's provider
     * @param topicURLRoot root name for the topic URL
     * @param serverId identifier of the server - used to pick up the right offset for the inbound messages.
     * @param eventBusConfigurationProperties name value property pairs for the event bus connection
     * @return Connection object
     */
    public Connection getDefaultCohortOMRSTopicConnection(String              cohortName,
                                                          Map<String, Object> configurationProperties,
                                                          String              eventBusConnectorProvider,
                                                          String              topicURLRoot,
                                                          String              serverId,
                                                          Map<String, Object> eventBusConfigurationProperties)
    {
        final String connectionGUID    = "023bb1f3-03dd-47ae-b3bc-dce62e9c11cb";
        final String connectionDescription = "OMRS default cohort topic connection.";

        String topicName = defaultCohortTopicConnectorRootName + cohortName + defaultOMRSTopicLeafName;
        String connectionName = "TopicConnector.Cohort." + cohortName;

        VirtualConnection connection = new VirtualConnection();

        connection.setType(this.getVirtualConnectionType());
        connection.setGUID(connectionGUID);
        connection.setQualifiedName(connectionName);
        connection.setDisplayName(connectionName);
        connection.setDescription(connectionDescription);
        connection.setConnectorType(getConnectorType(OMRSTopicProvider.class.getName()));
        connection.setConfigurationProperties(configurationProperties);
        connection.setEmbeddedConnections(getEmbeddedEventBusConnection(cohortName + " OMRS Topic",
                                                                        null,
                                                                        eventBusConnectorProvider,
                                                                        topicURLRoot,
                                                                        topicName,
                                                                        serverId,
                                                                        eventBusConfigurationProperties));

        return connection;
    }


    /**
     * Return a connection for a repository event mapper.  Notice that this event mapper is not the
     * open metadata virtual connector because this event mapper is interfacing with external technology.
     *
     * @param serverName name of the repository proxy server where the event mapper will run
     * @param connectorProviderClassName  java class name of the connector provider for the event mapper
     * @param configurationProperties additional properties for event mapper
     * @param eventSource  name of the event source used by the event mapper
     * @return Connection object
     */
    public Connection getRepositoryEventMapperConnection(String              serverName,
                                                         String              connectorProviderClassName,
                                                         Map<String, Object> configurationProperties,
                                                         String              eventSource)
    {
        final String endpointGUID             = UUID.randomUUID().toString();
        final String connectionGUID           = UUID.randomUUID().toString();
        final String endpointDescription      = "Repository event mapper endpoint.";
        final String connectionDescription    = "Repository event mapper connection.";

        String endpointName    = "EventMapper.Endpoint." + serverName;

        Endpoint endpoint = new Endpoint();

        endpoint.setType(this.getEndpointType());
        endpoint.setGUID(endpointGUID);
        endpoint.setQualifiedName(endpointName);
        endpoint.setDisplayName(endpointName);
        endpoint.setDescription(endpointDescription);
        endpoint.setAddress(eventSource);

        String connectionName = "EventMapper.Connection." + serverName;

        Connection connection = new Connection();

        connection.setType(this.getConnectionType());
        connection.setGUID(connectionGUID);
        connection.setQualifiedName(connectionName);
        connection.setDisplayName(connectionName);
        connection.setDescription(connectionDescription);
        connection.setEndpoint(endpoint);
        connection.setConnectorType(getConnectorType(connectorProviderClassName));
        connection.setConfigurationProperties(configurationProperties);

        return connection;
    }


    /**
     * Return the connection.  This is using the RangerConnector.
     *
     * @param serverName  name of the real repository server
     * @param url  url for the Security Server
     * @param configurationProperties name value pairs for the connection
     * @return Connection object
     */
    public Connection getSecuritySyncServerConnection(String              serverName,
                                                      String              url,
                                                      Map<String, Object> configurationProperties)
    {
        final String endpointGUID          = "edc55b7b-344f-77c8-7871-e42f08a858fd";
        final String endpointDescription   = "OMRS repository endpoint for Security Sync Server.";
        String endpointName                = "SecuritySyncServer.Endpoint." + serverName;

        Endpoint endpoint = getEndpoint(url, endpointName, endpointGUID, endpointDescription);

        String connectionName = "SecuritySyncServer.Connection." + serverName;
        final String connectionGUID        = "caa5be1e-d51f-8306-fe81-d3853dc7e415";
        final String connectionDescription = "OMRS repository connection to Security Sync Server.";

        return getConnection(configurationProperties,
                             endpoint,
                             connectionName,
                             connectionGUID,
                             connectionDescription,
                             RangerSecurityServiceConnectorProvider.class.getName());
    }


    /**
     * Return the connection.
     *
     * @param serverName  name of the real repository server
     * @param url  url for the Security Officer Server
     * @param configurationProperties name value pairs for the connection
     * @return Connection object
     */
    public Connection getSecurityOfficerServerConnection(String              serverName,
                                                         String              url,
                                                         Map<String, Object> configurationProperties)
    {
        final String endpointGUID          = "4e53023d-a3ec-73ae-d4d4-0302147f7834";
        final String endpointDescription   = "OMRS repository endpoint for Security Officer Server.";
        String endpointName                = "SecurityOfficerServer.Endpoint." + serverName;

        Endpoint endpoint = getEndpoint(url, endpointName, endpointGUID, endpointDescription);

        String connectionName = "SecurityOfficerServer.Connection." + serverName;
        final String connectionGUID        = "05cfb02d-3e40-ef76-ce93-02f492ce4c4d";
        final String connectionDescription = "OMRS repository connection to Security Officer Server.";

        return getConnection(configurationProperties,
                             endpoint,
                             connectionName,
                             connectionGUID,
                             connectionDescription,
                             SecurityTagConnectorProvider.class.getName());
    }


    /**
     * Return the connection. This is used by view generator connectors
     *
     * @param serverName name of the real repository server
     * @param connectorProviderClassName the class name of the
     * @param virtualizationSolutionConfig related configuration
     * @return connection object
     */
    public Connection getVirtualizationSolutionConnection (String              serverName,
                                                           String              connectorProviderClassName,
                                                           Map<String, Object> virtualizationSolutionConfig)
    {
        Connection connection = new Connection();
        Map<String, String> additionalProperties = new HashMap<>();
        Endpoint endpoint = new Endpoint();

        final String endpointGUID             = UUID.randomUUID().toString();
        final String connectionGUID           = UUID.randomUUID().toString();
        final String endpointDescription      = "Virtualization solution endpoint.";
        final String connectionDescription    = "Virtualization solution connection.";

        String endpointName    = "Virtualizer.Endpoint." + serverName;

        if (ConnectorClassName.GAIAN_DB_CONNECTOR.equals(connectorProviderClassName)){
            endpoint.setType(Endpoint.getEndpointType());
            endpoint.setGUID(endpointGUID);
            endpoint.setQualifiedName(endpointName);
            endpoint.setDisplayName(endpointName);
            endpoint.setDescription(endpointDescription);
            endpoint.setAddress(virtualizationSolutionConfig.get("serverAddress").toString());

            Map<String, String> endpointProperties = new HashMap<>();
            endpointProperties.put("timeoutInSecond", virtualizationSolutionConfig.get("timeoutInSecond").toString());
            endpointProperties.put("create", virtualizationSolutionConfig.get("create").toString());
            endpointProperties.put("connectorProviderName", connectorProviderClassName);

            endpoint.setAdditionalProperties(endpointProperties);

            String connectionName = "Vitualizer.Connection." + serverName;

            connection.setType(Connection.getConnectionType());
            connection.setGUID(connectionGUID);
            connection.setQualifiedName(connectionName);
            connection.setDisplayName(connectionName);
            connection.setDescription(connectionDescription);
            connection.setUserId(virtualizationSolutionConfig.get("username").toString());
            connection.setClearPassword(virtualizationSolutionConfig.get("password").toString());
            connection.setEndpoint(endpoint);

            additionalProperties.put("gdbNode", virtualizationSolutionConfig.get("gdbNode").toString());
            additionalProperties.put("logicTableName",virtualizationSolutionConfig.get("logicTableName").toString());
            additionalProperties.put("logicTableDefinition", virtualizationSolutionConfig.get("logicTableDefinition").toString());
            additionalProperties.put("getLogicTables", virtualizationSolutionConfig.get("getLogicTables").toString());
            additionalProperties.put("frontendName", virtualizationSolutionConfig.get("frontendName").toString());
            additionalProperties.put("dataSchema", virtualizationSolutionConfig.get("schema").toString());
            additionalProperties.put("databaseName", virtualizationSolutionConfig.get("databaseName").toString());

            connection.setAdditionalProperties(additionalProperties);
        }

        else {
            log.error("Provided connector class is not registered in virtualizer api or implemented.");
        }

        return connection;
    }


    /**
     * Return the connection of data platform
     *
     * @param serverName name of the real repository server
     * @param connectorProviderClassName the class name of the
     * @param dataPlatformConfigurationProperties related configuration
     * @return connection object
     */
    public Connection getDataPlatformConnection (String              serverName,
                                                 String              connectorProviderClassName,
                                                 Map<String, Object> dataPlatformConfigurationProperties)
    {
        final String endpointGUID             = UUID.randomUUID().toString();
        final String connectionGUID           = UUID.randomUUID().toString();
        final String endpointDescription      = "Data Platform Server endpoint.";
        final String connectionDescription    = "Data Platform connection.";

        String endpointName    = "DataPlatform.Endpoint." + serverName;
        Endpoint endpoint = new Endpoint();
        Connection connection = new Connection();

        endpoint.setType(this.getEndpointType());
        endpoint.setGUID(endpointGUID);
        endpoint.setQualifiedName(endpointName);
        endpoint.setDisplayName(endpointName);
        endpoint.setDescription(endpointDescription);
        endpoint.setAddress(dataPlatformConfigurationProperties.get("serverAddress").toString());

        Map<String, String> endpointProperties = new HashMap<>();
        endpointProperties.put("connectorProviderName", connectorProviderClassName);
        endpoint.setAdditionalProperties(endpointProperties);

        String connectionName = connection.getDisplayName();
        connection.setType(this.getConnectionType());
        connection.setGUID(connectionGUID);
        connection.setQualifiedName(connectionName);
        connection.setDisplayName(connectionName);
        connection.setDescription(connectionDescription);
        connection.setEndpoint(endpoint);


        return connection;
    }


    /**
     * Return the connection.  This is used by open lineage graph connectors.
     *
     * @param serverName  name of the real repository server
     * @param connectorProviderClassName name of the connector provider's implementation
     * @param url  url for the Open Lineage Server
     * @param configurationProperties name value pairs for the connection
     * @return Connection object
     */
    public Connection getOpenLineageServerConfiguration(String              serverName,
                                                        String              connectorProviderClassName,
                                                        String              url,
                                                        Map<String, Object> configurationProperties)
    {
        final String endpointGUID          = UUID.randomUUID().toString();
        final String connectionGUID           = UUID.randomUUID().toString();

        final String endpointDescription      = "Open Lineage native endpoint.";
        final String connectionDescription    = "Open Lineage native connection.";

        String endpointName    = "OpenLineage.Endpoint." + serverName;
        String connectionName  = "OpenLineage.Connection." + serverName;

        Endpoint endpoint = getEndpoint(url, endpointName, endpointGUID, endpointDescription);

        return getConnection(configurationProperties,
                             endpoint,
                             connectionName,
                             connectionGUID,
                             connectionDescription,
                             connectorProviderClassName);
    }


    /**
     * Return the connector type for the requested connector provider.  This is best used for connector providers that
     * can return their own connector type.  Otherwise it makes one up.
     *
     * @param connectorProviderClassName name of the connector provider class
     * @return ConnectorType bean
     */
    private ConnectorType   getConnectorType(String   connectorProviderClassName)
    {
        ConnectorType  connectorType = null;

        if (connectorProviderClassName != null)
        {
            try
            {
                Class      connectorProviderClass = Class.forName(connectorProviderClassName);
                Object     potentialConnectorProvider = connectorProviderClass.newInstance();

                ConnectorProvider  connectorProvider = (ConnectorProvider)potentialConnectorProvider;

                connectorType = connectorProvider.getConnectorType();

                if (connectorType == null)
                {
                    connectorType = new ConnectorType();

                    connectorType.setType(this.getConnectorTypeType());
                    connectorType.setGUID(UUID.randomUUID().toString());
                    connectorType.setQualifiedName(connectorProviderClassName);
                    connectorType.setDisplayName(connectorProviderClass.getSimpleName());
                    connectorType.setDescription("ConnectorType for " + connectorType.getDisplayName());
                    connectorType.setConnectorProviderClassName(connectorProviderClassName);
                }
            }
            catch (Throwable classException)
            {
                log.error("Bad connectorProviderClassName: " + classException.getMessage());
            }
        }

        return connectorType;
    }


    /**
     * Return the standard type for an endpoint.
     *
     * @return ElementType object
     */
    private ElementType getEndpointType()
    {
        ElementType elementType = Endpoint.getEndpointType();

        elementType.setElementOrigin(ElementOrigin.CONFIGURATION);

        return elementType;
    }


    /**
     * Return the standard type for a connector type.
     *
     * @return ElementType object
     */
    private ElementType getConnectorTypeType()
    {
        ElementType elementType = ConnectorType.getConnectorTypeType();

        elementType.setElementOrigin(ElementOrigin.CONFIGURATION);

        return elementType;
    }


    /**
     * Return the standard type for a connection type.
     *
     * @return ElementType object
     */
    private ElementType getConnectionType()
    {
        ElementType elementType = Connection.getConnectionType();

        elementType.setElementOrigin(ElementOrigin.CONFIGURATION);

        return elementType;
    }


    /**
     * Return the standard type for a virtual connection type.
     *
     * @return ElementType object
     */
    private ElementType getVirtualConnectionType()
    {
        ElementType elementType = VirtualConnection.getVirtualConnectionType();

        elementType.setElementOrigin(ElementOrigin.CONFIGURATION);

        return elementType;
    }


    /**
     * Return the Endpoint build based on the given parameters.
     * @param url the server url
     * @param endpointName name of the endpoint
     * @param endpointGUID endpoint identifier
     * @param endpointDescription description for the endpoint

     * @return Endpoint object
     */
    private Endpoint getEndpoint(String url,
                                 String endpointName,
                                 String endpointGUID,
                                 String endpointDescription)
    {
        Endpoint endpoint = new Endpoint();

        endpoint.setType(this.getEndpointType());
        endpoint.setGUID(endpointGUID);
        endpoint.setQualifiedName(endpointName);
        endpoint.setDisplayName(endpointName);
        endpoint.setDescription(endpointDescription);
        endpoint.setAddress(url);

        return endpoint;
    }


    /**
     * Return the Connection build based on the given parameters
     *
     * @param configurationProperties properties used to configure the underlying technology
     * @param endpoint that contains the server url
     * @param connectionName name of the connection
     * @param connectionGUID connection identifier
     * @param connectionDescription description for the connection
     * @return Connection object
     */
    private Connection getConnection(Map<String, Object> configurationProperties,
                                     Endpoint            endpoint,
                                     String              connectionName,
                                     String              connectionGUID,
                                     String              connectionDescription,
                                     String              className)
    {
        Connection connection = new Connection();

        connection.setType(this.getConnectionType());
        connection.setEndpoint(endpoint);
        connection.setGUID(connectionGUID);
        connection.setQualifiedName(connectionName);
        connection.setDisplayName(connectionName);
        connection.setDescription(connectionDescription);
        connection.setConnectorType(getConnectorType(className));
        connection.setConfigurationProperties(configurationProperties);

        return connection;
    }

}
