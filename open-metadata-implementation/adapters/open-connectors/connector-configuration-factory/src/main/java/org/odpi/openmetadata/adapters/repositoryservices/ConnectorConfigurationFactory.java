/* SPDX-License-Identifier: Apache-2.0 */
package org.odpi.openmetadata.adapters.repositoryservices;

import org.odpi.openmetadata.adapters.adminservices.configurationstore.file.FileBasedServerConfigStoreProvider;
import org.odpi.openmetadata.adapters.repositoryservices.archiveconnector.file.FileBasedOpenMetadataArchiveStoreProvider;
import org.odpi.openmetadata.adapters.repositoryservices.auditlogstore.file.FileBasedAuditLogStoreProvider;
import org.odpi.openmetadata.adapters.repositoryservices.cohortregistrystore.file.FileBasedRegistryStoreProvider;
import org.odpi.openmetadata.adapters.repositoryservices.graphrepository.repositoryconnector.GraphOMRSRepositoryConnectorProvider;
import org.odpi.openmetadata.adapters.repositoryservices.inmemory.repositoryconnector.InMemoryOMRSRepositoryConnectorProvider;
import org.odpi.openmetadata.adapters.repositoryservices.omrstopicconnector.inmemory.InMemoryOMRSTopicProvider;
import org.odpi.openmetadata.adapters.repositoryservices.omrstopicconnector.kafka.KafkaOMRSTopicProvider;
import org.odpi.openmetadata.adapters.repositoryservices.rest.repositoryconnector.OMRSRESTRepositoryConnectorProvider;
import org.odpi.openmetadata.frameworks.connectors.properties.ElementOrigin;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.Connection;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.ConnectorType;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.Endpoint;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.ElementType;

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
    private static final String defaultTopicRootName = "openmetadata/repositoryservices/";
    private static final String defaultTopicLeafName = "/OMRSTopic";

    private static final String defaultEnterpriseTopicConnectorRootName = "enterprise/";
    private static final String defaultCohortTopicConnectorRootName     = "cohort/";

    private static final String defaultOpenMetadataArchiveFileName = "OpenMetadataTypes.json";

    /**
     * Default constructor
     */
    public ConnectorConfigurationFactory()
    {
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
     * Return the Connection for this server's OMRS Repository REST API.  If the localServerURL is
     * something like localhost:8080/omag/localServerName and the REST API URL would be
     * localhost:8080/omag/localServerName/repositoryservices/metadatacollectionstore.
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
     * Return the default connection for the enterprise OMRS topic.  This uses a Kafka topic called
     * omag/repositoryservices/enterprise/localServerName/OMRSTopic.
     *
     * @param localServerName   name of local server
     * @return Connection object
     */
    public Connection getDefaultEnterpriseOMRSTopicConnection(String localServerName)
    {
        final String endpointGUID      = "e0d88035-8522-42bc-b57f-06df05f15825";
        final String connectorTypeGUID = "6536cb46-61f0-4f2d-abb4-2dadede30520";
        final String connectionGUID    = "2084ee90-717b-49a1-938e-8f9d49567b8e";

        final String endpointDescription = "OMRS default enterprise topic endpoint.";

        String endpointAddress = defaultTopicRootName + defaultEnterpriseTopicConnectorRootName + localServerName + defaultTopicLeafName;
        String endpointName    = "DefaultEnterpriseTopic.Endpoint." + endpointAddress;

        Endpoint endpoint = new Endpoint();

        endpoint.setType(this.getEndpointType());
        endpoint.setGUID(endpointGUID);
        endpoint.setQualifiedName(endpointName);
        endpoint.setDisplayName(endpointName);
        endpoint.setDescription(endpointDescription);
        endpoint.setAddress(endpointAddress);


        final String connectorTypeDescription   = "OMRS default enterprise connector type.";
        final String connectorTypeJavaClassName = InMemoryOMRSTopicProvider.class.getName();

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
     * Return the connection for the OMRS topic for the named cohort.
     *
     * @param cohortName   name of the cohort
     * @return Connection object
     */
    public Connection getDefaultCohortOMRSTopicConnection(String cohortName)
    {
        final String endpointGUID      = "dca783a1-d5f9-44a8-b838-4de4d016303d";
        final String connectorTypeGUID = "32843dd8-2597-4296-831c-674af0d8b837";
        final String connectionGUID    = "023bb1f3-03dd-47ae-b3bc-dce62e9c11cb";

        final String endpointDescription = "OMRS default cohort topic endpoint.";

        String endpointAddress = defaultTopicRootName + defaultCohortTopicConnectorRootName + cohortName + defaultTopicLeafName;
        String endpointName    = "DefaultCohortTopic.Endpoint." + endpointAddress;

        Endpoint endpoint = new Endpoint();

        endpoint.setType(this.getEndpointType());
        endpoint.setGUID(endpointGUID);
        endpoint.setQualifiedName(endpointName);
        endpoint.setDisplayName(endpointName);
        endpoint.setDescription(endpointDescription);
        endpoint.setAddress(endpointAddress);


        final String connectorTypeDescription   = "OMRS default cohort topic connector type.";
        final String connectorTypeJavaClassName = KafkaOMRSTopicProvider.class.getName();

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
     * Return default values for the default event bus connection.
     *
     * @param connectionName - name to use in the connection object
     * @param topicName - name of the topic
     * @return Connection object
     */
    public Connection getDefaultEventBusConnection(String    connectionName,
                                                   String    topicName)
    {
        String  description = connectionName;

        final String endpointGUID = "f6e296ae-d001-44b2-80c9-b8240a246d61";
        final String connectorTypeGUID = "1db88a02-475f-43f9-b226-3b807f0caba5";
        final String connectionGUID = "bb32263c-a9ce-4262-98b0-b629a9d08614";

        Endpoint endpoint = new Endpoint();

        endpoint.setType(this.getEndpointType());
        endpoint.setGUID(endpointGUID);
        endpoint.setQualifiedName(topicName);
        endpoint.setDisplayName(topicName);
        endpoint.setDescription(description);
        endpoint.setAddress(topicName);

        // todo - wrong class name
        final String connectorTypeJavaClassName = KafkaOMRSTopicProvider.class.getName();

        ConnectorType connectorType = new ConnectorType();

        connectorType.setType(this.getConnectorTypeType());
        connectorType.setGUID(connectorTypeGUID);
        connectorType.setQualifiedName(topicName);
        connectorType.setDisplayName(topicName);
        connectorType.setDescription(description);
        connectorType.setConnectorProviderClassName(connectorTypeJavaClassName);

        Connection connection = new Connection();

        connection.setType(this.getConnectionType());
        connection.setGUID(connectionGUID);
        connection.setQualifiedName(connectionName);
        connection.setDisplayName(connectionName);
        connection.setDescription(description);
        connection.setEndpoint(endpoint);
        connection.setConnectorType(connectorType);

        return connection;
    }


    /**
     * Returns the connection for an arbitrary repository proxy.
     *
     * @param serverName  name of the real repository server
     * @param connectorProvider  class name of the connector provider
     * @param url  location of the repository proxy
     * @return Connection object
     */
    public Connection  getRepositoryProxyConnection(String    serverName,
                                                    String    connectorProvider,
                                                    String    url)
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

        return connection;
    }


    /**
     * Return a connection for a repository event mapper.
     *
     * @param serverName name of the repository proxy server where the event mapper will run
     * @param connectorProvider  java class name of the connector provider for the event mapper
     * @param eventSource  name of the event source used by the event mapper
     * @return Connection object
     */
    public Connection getRepositoryEventMapperConnection(String   serverName,
                                                         String   connectorProvider,
                                                         String   eventSource)
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

        return connection;
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
     * Return the standard type for an endpoint.
     *
     * @return ElementType object
     */
    private ElementType getEndpointType()
    {
        final String        elementTypeId                   = "dbc20663-d705-4ff0-8424-80c262c6b8e7";
        final String        elementTypeName                 = "Endpoint";
        final long          elementTypeVersion              = 1;
        final String        elementTypeDescription          = "Description of the network address and related information needed to call a software service.";
        final String        elementAccessServiceURL         = null;
        final ElementOrigin elementOrigin                   = ElementOrigin.LOCAL_COHORT;
        final String        elementHomeMetadataCollectionId = null;

        ElementType elementType = new ElementType();

        elementType.setElementTypeId(elementTypeId);
        elementType.setElementTypeName(elementTypeName);
        elementType.setElementTypeVersion(elementTypeVersion);
        elementType.setElementTypeDescription(elementTypeDescription);
        elementType.setElementAccessServiceURL(elementAccessServiceURL);
        elementType.setElementOrigin(elementOrigin);
        elementType.setElementHomeMetadataCollectionId(elementHomeMetadataCollectionId);

        return elementType;
    }


    /**
     * Return the standard type for a connector type.
     *
     * @return ElementType object
     */
    private ElementType getConnectorTypeType()
    {
        final String        elementTypeId                   = "954421eb-33a6-462d-a8ca-b5709a1bd0d4";
        final String        elementTypeName                 = "ConnectorType";
        final long          elementTypeVersion              = 1;
        final String        elementTypeDescription          = "A set of properties describing a type of connector.";
        final String        elementAccessServiceURL         = null;
        final ElementOrigin elementOrigin                   = ElementOrigin.LOCAL_COHORT;
        final String        elementHomeMetadataCollectionId = null;

        ElementType elementType = new ElementType();

        elementType.setElementTypeId(elementTypeId);
        elementType.setElementTypeName(elementTypeName);
        elementType.setElementTypeVersion(elementTypeVersion);
        elementType.setElementTypeDescription(elementTypeDescription);
        elementType.setElementAccessServiceURL(elementAccessServiceURL);
        elementType.setElementOrigin(elementOrigin);
        elementType.setElementHomeMetadataCollectionId(elementHomeMetadataCollectionId);

        return elementType;
    }


    /**
     * Return the standard type for a connection type.
     *
     * @return ElementType object
     */
    private ElementType getConnectionType()
    {
        final String        elementTypeId                   = "114e9f8f-5ff3-4c32-bd37-a7eb42712253";
        final String        elementTypeName                 = "Connection";
        final long          elementTypeVersion              = 1;
        final String        elementTypeDescription          = "A set of properties to identify and configure a connector instance.";
        final String        elementAccessServiceURL         = null;
        final ElementOrigin elementOrigin                   = ElementOrigin.LOCAL_COHORT;
        final String        elementHomeMetadataCollectionId = null;

        ElementType elementType = new ElementType();

        elementType.setElementTypeId(elementTypeId);
        elementType.setElementTypeName(elementTypeName);
        elementType.setElementTypeVersion(elementTypeVersion);
        elementType.setElementTypeDescription(elementTypeDescription);
        elementType.setElementAccessServiceURL(elementAccessServiceURL);
        elementType.setElementOrigin(elementOrigin);
        elementType.setElementHomeMetadataCollectionId(elementHomeMetadataCollectionId);

        return elementType;
    }
}
