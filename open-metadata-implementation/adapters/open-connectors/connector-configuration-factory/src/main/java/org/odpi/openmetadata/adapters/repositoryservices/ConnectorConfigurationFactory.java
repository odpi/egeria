/* SPDX-License-Identifier: Apache-2.0 */
package org.odpi.openmetadata.adapters.repositoryservices;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.odpi.openmetadata.adapters.adminservices.configurationstore.file.FileBasedServerConfigStoreProvider;
import org.odpi.openmetadata.adapters.eventbus.topic.inmemory.InMemoryOpenMetadataTopicProvider;
import org.odpi.openmetadata.adapters.eventbus.topic.kafka.KafkaOpenMetadataTopicProvider;
import org.odpi.openmetadata.adapters.repositoryservices.archiveconnector.file.FileBasedOpenMetadataArchiveStoreProvider;
import org.odpi.openmetadata.adapters.repositoryservices.auditlogstore.file.FileBasedAuditLogStoreProvider;
import org.odpi.openmetadata.adapters.repositoryservices.cohortregistrystore.file.FileBasedRegistryStoreProvider;
import org.odpi.openmetadata.adapters.repositoryservices.graphrepository.eventmapper.GraphOMRSRepositoryEventMapperProvider;
import org.odpi.openmetadata.adapters.repositoryservices.graphrepository.repositoryconnector.GraphOMRSRepositoryConnectorProvider;
import org.odpi.openmetadata.adapters.repositoryservices.inmemory.repositoryconnector.InMemoryOMRSRepositoryConnectorProvider;
import org.odpi.openmetadata.adapters.repositoryservices.rest.repositoryconnector.OMRSRESTRepositoryConnectorProvider;
import org.odpi.openmetadata.frameworks.connectors.ConnectorProvider;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.*;
import org.odpi.openmetadata.repositoryservices.connectors.omrstopic.OMRSTopicProvider;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;


/**
 * ConnectorConfigurationFactory sets up default configuration for the OMRS components.  It is used by the OMAG server
 * while it manages the changes made to the server configuration by the server administrator.  The aim is to
 * build up the RepositoryServicesConfig object that is used to initialize the OMRSOperationalServices.
 */
public class ConnectorConfigurationFactory
{
    /*
     * Default property fillers
     */
    private static final String defaultTopicRootName     = "/open-metadata/repository-services/";
    private static final String defaultOMRSTopicLeafName = "/OMRSTopic";

    private static final String defaultEnterpriseTopicConnectorRootName = defaultTopicRootName + "enterprise/";
    private static final String defaultCohortTopicConnectorRootName     = defaultTopicRootName + "cohort/";

    private static final String defaultEventMapperTopicName = defaultTopicRootName + "local-repository/events";

    private static final String defaultOpenMetadataArchiveFileName = "OpenMetadataTypes.json";


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

        ConnectorType  connectorType = new ConnectorType();
        connectorType.setConnectorProviderClassName(FileBasedServerConfigStoreProvider.class.getName());

        Connection connection = new Connection();
        connection.setEndpoint(endpoint);
        connection.setConnectorType(connectorType);
        connection.setQualifiedName(endpoint.getAddress());

        return connection;
    }


    /**
     * Return the connection for the default audit log.
     * By default, the Audit log is stored in a directory called localServerName.auditlog.
     *
     * @param localServerName   name of the local server
     * @return OCF Connection used to create the file-based audit logger
     */
    public Connection getDefaultAuditLogConnection(String localServerName)
    {
        final String endpointGUID      = "836efeae-ab34-4425-89f0-6adf2faa1f2e";
        final String connectorTypeGUID = "f8a24f09-9183-4d5c-8408-aa1c8852a7d6";
        final String connectionGUID    = "5390bf3e-6b38-4eda-b34a-de55ac4252a7";

        final String endpointDescription = "OMRS default audit log endpoint.";

        String endpointAddress = localServerName + ".auditlog";
        String endpointName    = "DefaultAuditLog.Endpoint." + endpointAddress;

        Endpoint endpoint = new Endpoint();

        endpoint.setType(this.getEndpointType());
        endpoint.setGUID(endpointGUID);
        endpoint.setQualifiedName(endpointName);
        endpoint.setDisplayName(endpointName);
        endpoint.setDescription(endpointDescription);
        endpoint.setAddress(endpointAddress);

        final String connectorTypeDescription   = "OMRS default audit log connector type.";
        final String connectorTypeJavaClassName = FileBasedAuditLogStoreProvider.class.getName();

        String connectorTypeName = "DefaultAuditLog.ConnectorType." + localServerName;

        ConnectorType connectorType = new ConnectorType();

        connectorType.setType(this.getConnectorTypeType());
        connectorType.setGUID(connectorTypeGUID);
        connectorType.setQualifiedName(connectorTypeName);
        connectorType.setDisplayName(connectorTypeName);
        connectorType.setDescription(connectorTypeDescription);
        connectorType.setConnectorProviderClassName(connectorTypeJavaClassName);

        final String connectionDescription = "OMRS default audit log connection.";

        String connectionName = "DefaultAuditLog.Connection." + localServerName;

        Connection connection = new Connection();

        connection.setType(this.getConnectionType());
        connection.setGUID(connectionGUID);
        connection.setQualifiedName(connectionName);
        connection.setDisplayName(connectionName);
        connection.setDescription(connectionDescription);
        connection.setEndpoint(endpoint);
        connection.setConnectorType(connectorType);

        return connection;
    }


    /**
     * Return the connection for the default audit log.
     * By default, the open metadata is stored in a file called localServerName.auditlog.
     *
     * @return OCF Connection used to create the file-based open metadata archive
     */
    public Connection getOpenMetadataTypesConnection()
    {
        final String endpointGUID      = "45877b9c-9192-44ba-a2b7-6817bc753969";
        final String connectorTypeGUID = "86f52a17-5d3c-47fd-9cac-0b5a45d150a9";
        final String connectionGUID    = "447bbb33-84f9-4a56-a712-addeebdcd764";

        final String endpointDescription = "Open metadata types archive endpoint.";

        String endpointAddress = defaultOpenMetadataArchiveFileName;
        String endpointName    = "OpenMetadataTypes.Endpoint" + endpointAddress;

        Endpoint endpoint = new Endpoint();

        endpoint.setType(this.getEndpointType());
        endpoint.setGUID(endpointGUID);
        endpoint.setQualifiedName(endpointName);
        endpoint.setDisplayName(endpointName);
        endpoint.setDescription(endpointDescription);
        endpoint.setAddress(endpointAddress);

        final String connectorTypeDescription   = "Open metadata types archive connector type.";
        final String connectorTypeJavaClassName = FileBasedOpenMetadataArchiveStoreProvider.class.getName();

        String connectorTypeName = "OpenMetadataTypes.ConnectorType";

        ConnectorType connectorType = new ConnectorType();

        connectorType.setType(this.getConnectorTypeType());
        connectorType.setGUID(connectorTypeGUID);
        connectorType.setQualifiedName(connectorTypeName);
        connectorType.setDisplayName(connectorTypeName);
        connectorType.setDescription(connectorTypeDescription);
        connectorType.setConnectorProviderClassName(connectorTypeJavaClassName);

        final String connectionDescription = "Open metadata types archive connection.";

        String connectionName = "OpenMetadataTypes.Connection";

        Connection connection = new Connection();

        connection.setType(this.getConnectionType());
        connection.setGUID(connectionGUID);
        connection.setQualifiedName(connectionName);
        connection.setDisplayName(connectionName);
        connection.setDescription(connectionDescription);
        connection.setEndpoint(endpoint);
        connection.setConnectorType(connectorType);

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
        final String connectorTypeGUID = "2e1556a3-908f-4303-812d-d81b48b19bab";
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


        final String connectorTypeDescription   = "OMRS default cohort registry connector type.";
        final String connectorTypeJavaClassName = FileBasedRegistryStoreProvider.class.getName();

        String connectorTypeName = "DefaultCohortRegistry.ConnectorType." + localServerName + "." + cohortName;

        ConnectorType connectorType = new ConnectorType();

        connectorType.setType(this.getConnectorTypeType());
        connectorType.setGUID(connectorTypeGUID);
        connectorType.setQualifiedName(connectorTypeName);
        connectorType.setDisplayName(connectorTypeName);
        connectorType.setDescription(connectorTypeDescription);
        connectorType.setConnectorProviderClassName(connectorTypeJavaClassName);


        final String connectionDescription = "OMRS default cohort registry connection.";

        String connectionName = "DefaultCohortRegistry.Connection." + localServerName + "." + cohortName;

        Connection connection = new Connection();

        connection.setType(this.getConnectionType());
        connection.setGUID(connectionGUID);
        connection.setQualifiedName(connectionName);
        connection.setDisplayName(connectionName);
        connection.setDescription(connectionDescription);
        connection.setEndpoint(endpoint);
        connection.setConnectorType(connectorType);

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
     * localhost:8080/west-domain/open-metadata/repository-services/...
     *
     * @param localServerName   name of the local server
     * @param localServerURL   root of the local server's URL
     * @return Connection object
     */
    public  Connection getDefaultLocalRepositoryRemoteConnection(String localServerName,
                                                                 String localServerURL)
    {
        final String endpointGUID      = "cee85898-43aa-4af5-9bbd-2bed809d1acb";
        final String connectorTypeGUID = "64e67923-8190-45ea-8f96-39320d638c02";
        final String connectionGUID    = "858be98b-49d2-4ccf-9b23-01085a5f473f";

        final String endpointDescription = "OMRS default repository REST API endpoint.";

        String endpointAddress = localServerURL + "/openmetadata/repositoryservices/";
        String endpointName    = "DefaultRepositoryRESTAPI.Endpoint." + localServerName;

        Endpoint endpoint = new Endpoint();

        endpoint.setType(this.getEndpointType());
        endpoint.setGUID(endpointGUID);
        endpoint.setQualifiedName(endpointName);
        endpoint.setDisplayName(endpointName);
        endpoint.setDescription(endpointDescription);
        endpoint.setAddress(endpointAddress);

        final String connectorTypeDescription   = "OMRS default repository REST API connector type.";
        final String connectorTypeJavaClassName = OMRSRESTRepositoryConnectorProvider.class.getName();

        String connectorTypeName = "DefaultRepositoryRESTAPI.ConnectorType." + localServerName;

        ConnectorType connectorType = new ConnectorType();

        connectorType.setType(this.getConnectorTypeType());
        connectorType.setGUID(connectorTypeGUID);
        connectorType.setQualifiedName(connectorTypeName);
        connectorType.setDisplayName(connectorTypeName);
        connectorType.setDescription(connectorTypeDescription);
        connectorType.setConnectorProviderClassName(connectorTypeJavaClassName);


        final String connectionDescription = "OMRS default repository REST API connection.";

        String connectionName = "DefaultRepositoryRESTAPI.Connection." + localServerName;

        Connection connection = new Connection();

        connection.setType(this.getConnectionType());
        connection.setGUID(connectionGUID);
        connection.setQualifiedName(connectionName);
        connection.setDisplayName(connectionName);
        connection.setDescription(connectionDescription);
        connection.setEndpoint(endpoint);
        connection.setConnectorType(connectorType);

        return connection;
    }


    /**
     * Return the local graph repository's connection.  This is using the GraphOMRSRepositoryConnector.
     *
     * @param localServerName   name of the local server
     * @return Connection object
     */
    public Connection getLocalGraphRepositoryLocalConnection(String localServerName)
    {
        final String connectorTypeGUID = "18530415-44a2-4bd0-95bb-8efd333e53fb";
        final String connectionGUID    = "3f1fd4fc-90f9-436a-8e2c-2120d590f5e4";

        final String connectorTypeDescription   = "OMRS default graph local repository connector type.";
        final String connectorTypeJavaClassName = GraphOMRSRepositoryConnectorProvider.class.getName();

        String connectorTypeName = "DefaultLocalGraphRepository.ConnectorType." + localServerName;

        ConnectorType connectorType = new ConnectorType();

        connectorType.setType(this.getConnectorTypeType());
        connectorType.setGUID(connectorTypeGUID);
        connectorType.setQualifiedName(connectorTypeName);
        connectorType.setDisplayName(connectorTypeName);
        connectorType.setDescription(connectorTypeDescription);
        connectorType.setConnectorProviderClassName(connectorTypeJavaClassName);


        final String connectionDescription = "OMRS default local graph repository connection.";

        String connectionName = "DefaultLocalGraphRepository.Connection." + localServerName;

        Connection connection = new Connection();

        connection.setType(this.getConnectionType());
        connection.setGUID(connectionGUID);
        connection.setQualifiedName(connectionName);
        connection.setDisplayName(connectionName);
        connection.setDescription(connectionDescription);
        connection.setConnectorType(connectorType);

        return connection;
    }


    /**
     * Return the in-memory local repository connection.  This is using the InMemoryOMRSRepositoryConnector.
     *
     * @param localServerName   name of the local server
     * @return Connection object
     */
    public Connection getInMemoryLocalRepositoryLocalConnection(String localServerName)
    {
        final String connectorTypeGUID = "21422eb9-c6c1-4071-b96b-0572c9680260";
        final String connectionGUID    = "6a3c07b0-0e04-42dc-bcc6-392609bf1d02";

        final String connectorTypeDescription   = "OMRS default in memory local repository connector type.";
        final String connectorTypeJavaClassName = InMemoryOMRSRepositoryConnectorProvider.class.getName();

        String connectorTypeName = "DefaultInMemoryRepository.ConnectorType." + localServerName;

        ConnectorType connectorType = new ConnectorType();

        connectorType.setType(this.getConnectorTypeType());
        connectorType.setGUID(connectorTypeGUID);
        connectorType.setQualifiedName(connectorTypeName);
        connectorType.setDisplayName(connectorTypeName);
        connectorType.setDescription(connectorTypeDescription);
        connectorType.setConnectorProviderClassName(connectorTypeJavaClassName);


        final String connectionDescription = "OMRS default in memory local repository connection.";

        String connectionName = "DefaultInMemoryRepository.Connection." + localServerName;

        Connection connection = new Connection();

        connection.setType(this.getConnectionType());
        connection.setGUID(connectionGUID);
        connection.setQualifiedName(connectionName);
        connection.setDisplayName(connectionName);
        connection.setDescription(connectionDescription);
        connection.setConnectorType(connectorType);

        return connection;
    }


    /**
     * Returns the connection for an arbitrary repository proxy.
     *
     * @param serverName  name of the real repository server
     * @param connectorProvider  class name of the connector provider
     * @param url  location of the repository proxy
     * @param additionalProperties name value pairs for the connection
     * @return Connection object
     */
    public Connection  getRepositoryProxyConnection(String              serverName,
                                                    String              connectorProvider,
                                                    String              url,
                                                    Map<String, Object> additionalProperties)
    {
        final String endpointGUID             = UUID.randomUUID().toString();
        final String connectorTypeGUID        = UUID.randomUUID().toString();
        final String connectionGUID           = UUID.randomUUID().toString();
        final String endpointDescription      = "Metadata repository native endpoint.";
        final String connectorTypeDescription = "Metadata repository native connector type.";
        final String connectionDescription    = "Metadata repository native connection.";


        String endpointName    = "MetadataRepositoryNative.Endpoint." + serverName;

        Endpoint endpoint = new Endpoint();

        endpoint.setType(this.getEndpointType());
        endpoint.setGUID(endpointGUID);
        endpoint.setQualifiedName(endpointName);
        endpoint.setDisplayName(endpointName);
        endpoint.setDescription(endpointDescription);
        endpoint.setAddress(url);

        String connectorTypeName = "MetadataRepositoryNative.ConnectorType." + serverName;

        ConnectorType connectorType = new ConnectorType();

        connectorType.setType(this.getConnectorTypeType());
        connectorType.setGUID(connectorTypeGUID);
        connectorType.setQualifiedName(connectorTypeName);
        connectorType.setDisplayName(connectorTypeName);
        connectorType.setDescription(connectorTypeDescription);
        connectorType.setConnectorProviderClassName(connectorProvider);

        String connectionName = "MetadataRepositoryNative.Connection." + serverName;

        Connection connection = new Connection();

        connection.setType(this.getConnectionType());
        connection.setGUID(connectionGUID);
        connection.setQualifiedName(connectionName);
        connection.setDisplayName(connectionName);
        connection.setDescription(connectionDescription);
        connection.setEndpoint(endpoint);
        connection.setConnectorType(connectorType);
        connection.setAdditionalProperties(additionalProperties);

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
     * @param arguments - override properties for the event bus connection
     * @param eventBusConnectorProviderClassName name of connector provider class that controls the type of connector used.
     * @param topicURLRoot - root URL of the topic - this is prepended to the topic name
     * @param topicName - name of the topic
     * @param eventBusAdditionalProperties - additional properties for the event bus connection
     * @return List of EmbeddedConnection object
     */
    private List<EmbeddedConnection> getEmbeddedEventBusConnection(String              eventSource,
                                                                   Map<String, Object> arguments,
                                                                   String              eventBusConnectorProviderClassName,
                                                                   String              topicURLRoot,
                                                                   String              topicName,
                                                                   Map<String, Object> eventBusAdditionalProperties)
    {
        EmbeddedConnection     embeddedConnection = new EmbeddedConnection();
        Connection             connection         = this.getDefaultEventBusConnection(eventSource,
                                                                                      eventBusConnectorProviderClassName,
                                                                                      topicURLRoot,
                                                                                      topicName,
                                                                                      eventBusAdditionalProperties);

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
     * @param connectionName - name to use in the connection object
     * @param connectorProviderClassName name of connector provider class that controls the type of connector used.
     * @param topicURLRoot - root URL of the topic - this is prepended to the topic name
     * @param topicName - name of the topic
     * @param additionalProperties - additional properties for the connection
     * @return Connection object
     */
    public Connection getDefaultEventBusConnection(String               connectionName,
                                                   String               connectorProviderClassName,
                                                   String               topicURLRoot,
                                                   String               topicName,
                                                   Map<String, Object>  additionalProperties)
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
                endpoint.setAddress(topicURLRoot + topicName);
            }
        }

        String connectorTypeJavaClassName = KafkaOpenMetadataTopicProvider.class.getName();

        if (connectorProviderClassName != null)
        {
            connectorTypeJavaClassName = connectorProviderClassName;
        }

        Connection connection = new Connection();

        connection.setType(this.getConnectionType());
        connection.setGUID(UUID.randomUUID().toString());
        connection.setQualifiedName(connectionName);
        connection.setDisplayName(connectionName);
        connection.setDescription(connectionName);
        connection.setEndpoint(endpoint);
        connection.setConnectorType(getConnectorType(connectorTypeJavaClassName));
        connection.setAdditionalProperties(additionalProperties);

        return connection;
    }


    /**
     * Return the local repository event mapper for the graph repository.
     *
     * @param localServerName   name of the local server
     * @param additionalProperties name value property pairs for the topic connection
     * @param eventBusConnectorProvider class name of the event bus connector's provider
     * @param topicURLRoot root name for the topic URL
     * @param eventBusAdditionalProperties name value property pairs for the event bus connection
     * @return Connection object
     */
    public Connection getLocalGraphRepositoryEventMapperConnection(String              localServerName,
                                                                   Map<String, Object> additionalProperties,
                                                                   String              eventBusConnectorProvider,
                                                                   String              topicURLRoot,
                                                                   Map<String, Object> eventBusAdditionalProperties)
    {
        final String connectorTypeGUID = "3653df37-23a5-4dd0-9fff-d1b906301007";
        final String connectionGUID    = "3cb6d03e-b4a7-4884-9fd9-277c77223236";

        final String connectorTypeDescription   = "OMRS default local graph event mapper connector type.";
        final String connectorTypeJavaClassName = GraphOMRSRepositoryEventMapperProvider.class.getName();

        String connectorTypeName = "DefaultLocalGraphEventMapper.ConnectorType." + localServerName;

        ConnectorType connectorType = new ConnectorType();

        connectorType.setType(this.getConnectorTypeType());
        connectorType.setGUID(connectorTypeGUID);
        connectorType.setQualifiedName(connectorTypeName);
        connectorType.setDisplayName(connectorTypeName);
        connectorType.setDescription(connectorTypeDescription);
        connectorType.setConnectorProviderClassName(connectorTypeJavaClassName);


        final String connectionDescription = "OMRS default local graph event mapper connection.";

        String connectionName = "DefaultLocalGraphRepository.Connection." + localServerName;

        VirtualConnection connection = new VirtualConnection();

        connection.setType(this.getVirtualConnectionType());
        connection.setGUID(connectionGUID);
        connection.setQualifiedName(connectionName);
        connection.setDisplayName(connectionName);
        connection.setDescription(connectionDescription);
        connection.setConnectorType(connectorType);
        connection.setEmbeddedConnections(getEmbeddedEventBusConnection("Local Repository Events",
                                                                        additionalProperties,
                                                                        eventBusConnectorProvider,
                                                                        topicURLRoot,
                                                                        defaultEventMapperTopicName,
                                                                        eventBusAdditionalProperties));

        return connection;
    }


    /**
     * Return the default connection for the enterprise OMRS topic.  This uses an in-memory event bus connector
     *
     * @param localServerName   name of local server
     * @return Connection object
     */
    public Connection getDefaultEnterpriseOMRSTopicConnection(String              localServerName)
    {
        final String connectorTypeGUID = "6536cb46-61f0-4f2d-abb4-2dadede30520";
        final String connectionGUID    = "2084ee90-717b-49a1-938e-8f9d49567b8e";

        String topicName = defaultEnterpriseTopicConnectorRootName + localServerName + defaultOMRSTopicLeafName;

        final String connectorTypeDescription   = "OMRS default enterprise connector type.";
        final String connectorTypeJavaClassName = OMRSTopicProvider.class.getName();

        String connectorTypeName = "DefaultEnterpriseTopic.ConnectorType." + localServerName;

        ConnectorType connectorType = new ConnectorType();

        connectorType.setType(this.getConnectorTypeType());
        connectorType.setGUID(connectorTypeGUID);
        connectorType.setQualifiedName(connectorTypeName);
        connectorType.setDisplayName(connectorTypeName);
        connectorType.setDescription(connectorTypeDescription);
        connectorType.setConnectorProviderClassName(connectorTypeJavaClassName);


        final String connectionDescription = "OMRS default enterprise topic connection.";

        String connectionName = "DefaultEnterpriseTopic.Connection." + localServerName;

        VirtualConnection connection = new VirtualConnection();

        connection.setType(this.getVirtualConnectionType());
        connection.setGUID(connectionGUID);
        connection.setQualifiedName(connectionName);
        connection.setDisplayName(connectionName);
        connection.setDescription(connectionDescription);
        connection.setConnectorType(connectorType);
        connection.setEmbeddedConnections(getEmbeddedEventBusConnection("Enterprise OMRS Events",
                                                                        null,
                                                                        InMemoryOpenMetadataTopicProvider.class.getName(),
                                                                        "",
                                                                        topicName,
                                                                        null));

        return connection;
    }


    /**
     * Return the connection for the OMRS topic for the named cohort.
     *
     * @param cohortName   name of the cohort
     * @param additionalProperties name value property pairs for the topic connection
     * @param eventBusConnectorProvider class name of the event bus connector's provider
     * @param topicURLRoot root name for the topic URL
     * @param eventBusAdditionalProperties name value property pairs for the event bus connection
     * @return Connection object
     */
    public Connection getDefaultCohortOMRSTopicConnection(String              cohortName,
                                                          Map<String, Object> additionalProperties,
                                                          String              eventBusConnectorProvider,
                                                          String              topicURLRoot,
                                                          Map<String, Object> eventBusAdditionalProperties)
    {
        final String connectorTypeGUID = "32843dd8-2597-4296-831c-674af0d8b837";
        final String connectionGUID    = "023bb1f3-03dd-47ae-b3bc-dce62e9c11cb";

        final String connectorTypeDescription   = "OMRS default cohort topic connector type.";
        final String connectorTypeJavaClassName = OMRSTopicProvider.class.getName();

        String connectorTypeName = "DefaultCohortTopic.ConnectorType." + cohortName;

        ConnectorType connectorType = new ConnectorType();

        connectorType.setType(this.getConnectorTypeType());
        connectorType.setGUID(connectorTypeGUID);
        connectorType.setQualifiedName(connectorTypeName);
        connectorType.setDisplayName(connectorTypeName);
        connectorType.setDescription(connectorTypeDescription);
        connectorType.setConnectorProviderClassName(connectorTypeJavaClassName);


        final String connectionDescription = "OMRS default cohort topic connection.";

        String connectionName = "DefaultCohortTopic.Connection." + cohortName;

        VirtualConnection connection = new VirtualConnection();

        connection.setType(this.getVirtualConnectionType());
        connection.setGUID(connectionGUID);
        connection.setQualifiedName(connectionName);
        connection.setDisplayName(connectionName);
        connection.setDescription(connectionDescription);
        connection.setConnectorType(connectorType);
        connection.setEmbeddedConnections(getEmbeddedEventBusConnection(cohortName + " OMRS Topic",
                                                                        additionalProperties,
                                                                        eventBusConnectorProvider,
                                                                        topicURLRoot,
                                                                        defaultCohortTopicConnectorRootName + cohortName + defaultOMRSTopicLeafName,
                                                                        eventBusAdditionalProperties));

        return connection;
    }


    /**
     * Return a connection for a repository event mapper.  Notice that this event mapper is not the
     * open metadata virtual connector because this event mapper is interfacing with external technology.
     *
     * @param serverName name of the repository proxy server where the event mapper will run
     * @param connectorProvider  java class name of the connector provider for the event mapper
     * @param eventSource  name of the event source used by the event mapper
     * @return Connection object
     */
    public Connection getRepositoryEventMapperConnection(String              serverName,
                                                         String              connectorProvider,
                                                         Map<String, Object> additionalProperties,
                                                         String              eventSource)
    {
        final String endpointGUID             = UUID.randomUUID().toString();
        final String connectorTypeGUID        = UUID.randomUUID().toString();
        final String connectionGUID           = UUID.randomUUID().toString();
        final String endpointDescription      = "Repository event mapper endpoint.";
        final String connectorTypeDescription = "Repository event mapper connector type.";
        final String connectionDescription    = "Repository event mapper connection.";

        String endpointName    = "EventMapper.Endpoint." + serverName;

        Endpoint endpoint = new Endpoint();

        endpoint.setType(this.getEndpointType());
        endpoint.setGUID(endpointGUID);
        endpoint.setQualifiedName(endpointName);
        endpoint.setDisplayName(endpointName);
        endpoint.setDescription(endpointDescription);
        endpoint.setAddress(eventSource);

        String connectorTypeName = "EventMapper.ConnectorType." + serverName;

        ConnectorType connectorType = new ConnectorType();

        connectorType.setType(this.getConnectorTypeType());
        connectorType.setGUID(connectorTypeGUID);
        connectorType.setQualifiedName(connectorTypeName);
        connectorType.setDisplayName(connectorTypeName);
        connectorType.setDescription(connectorTypeDescription);
        connectorType.setConnectorProviderClassName(connectorProvider);

        String connectionName = "EventMapper.Connection." + serverName;

        Connection connection = new Connection();

        connection.setType(this.getConnectionType());
        connection.setGUID(connectionGUID);
        connection.setQualifiedName(connectionName);
        connection.setDisplayName(connectionName);
        connection.setDescription(connectionDescription);
        connection.setEndpoint(endpoint);
        connection.setConnectorType(connectorType);
        connection.setAdditionalProperties(additionalProperties);

        return connection;
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
}
