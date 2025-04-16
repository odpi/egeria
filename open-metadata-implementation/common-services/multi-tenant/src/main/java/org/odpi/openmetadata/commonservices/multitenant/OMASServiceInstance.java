/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.commonservices.multitenant;

import org.odpi.openmetadata.adminservices.configuration.registration.AccessServiceRegistrationEntry;
import org.odpi.openmetadata.adminservices.configuration.registration.ServerTypeClassification;
import org.odpi.openmetadata.adminservices.registration.OMAGAccessServiceRegistration;
import org.odpi.openmetadata.commonservices.ffdc.rest.RegisteredOMAGService;
import org.odpi.openmetadata.commonservices.multitenant.ffdc.OMAGServerInstanceAuditCode;
import org.odpi.openmetadata.commonservices.multitenant.ffdc.OMAGServerInstanceErrorCode;
import org.odpi.openmetadata.commonservices.repositoryhandler.RepositoryErrorHandler;
import org.odpi.openmetadata.commonservices.repositoryhandler.RepositoryHandler;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.connectors.ConnectorProvider;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.*;
import org.odpi.openmetadata.frameworks.openmetadata.enums.ElementOriginCategory;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.ElementOrigin;
import org.odpi.openmetadata.repositoryservices.connectors.openmetadatatopic.OpenMetadataTopicProvider;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.OMRSMetadataCollection;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryConnector;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.commonservices.multitenant.ffdc.exceptions.NewInstanceException;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;

import java.util.*;

/**
 * OMASServiceInstance caches references to OMRS objects for a specific server.
 * It is also responsible for registering itself in the instance map.
 */
public class OMASServiceInstance extends AuditableServerServiceInstance
{
    protected OMRSRepositoryConnector repositoryConnector;
    protected OMRSMetadataCollection  metadataCollection;
    protected OMRSRepositoryHelper    repositoryHelper;

    protected RepositoryHandler       repositoryHandler;
    protected RepositoryErrorHandler  errorHandler;

    protected List<String>            supportedZones;
    protected List<String>            defaultZones;
    protected List<String>            publishZones;

    private final Connection inTopicEventBusConnection;
    private final String     inTopicConnectorProviderName;
    private final Connection outTopicEventBusConnection;
    private final String     outTopicConnectorProviderName;

    private static final String  serverIdPropertyName = "local.server.id";


    /**
     * Set up the local repository connector that will service the REST Calls.
     *
     * @param serviceName name of this service
     * @param repositoryConnector link to the repository responsible for servicing the REST calls.
     * @param auditLog logging destination
     * @param localServerUserId userId used for server initiated actions
     * @param maxPageSize maximum page size
     * @throws NewInstanceException a problem occurred during initialization
     */
    public OMASServiceInstance(String                  serviceName,
                               OMRSRepositoryConnector repositoryConnector,
                               AuditLog                auditLog,
                               String                  localServerUserId,
                               int                     maxPageSize) throws NewInstanceException
    {
        this(serviceName, repositoryConnector, null, null, null, auditLog, localServerUserId, maxPageSize);
    }


    /**
     * Set up the local repository connector that will service the REST Calls.
     *
     * @param serviceName name of this service
     * @param repositoryConnector link to the repository responsible for servicing the REST calls.
     * @param supportedZones list of zones that access service is allowed to serve Assets from.
     * @param defaultZones list of zones that access service should set in all new Assets.
     * @param publishZones list of zones that the access service sets up in published Asset instances.
     * @param auditLog logging destination
     * @param localServerUserId userId used for server initiated actions
     * @param maxPageSize maximum page size
     * @throws NewInstanceException a problem occurred during initialization
     */
    public OMASServiceInstance(String                  serviceName,
                               OMRSRepositoryConnector repositoryConnector,
                               List<String>            supportedZones,
                               List<String>            defaultZones,
                               List<String>            publishZones,
                               AuditLog                auditLog,
                               String                  localServerUserId,
                               int                     maxPageSize) throws NewInstanceException
    {
        this(serviceName,
             repositoryConnector,
             supportedZones,
             defaultZones,
             publishZones,
             auditLog,
             localServerUserId,
             maxPageSize,
             null,
             null,
             null,
             null);
    }


    /**
     * Set up the local repository connector that will service the REST Calls.
     *
     * @param serviceName name of this service
     * @param repositoryConnector link to the repository responsible for servicing the REST calls.
     * @param supportedZones list of zones that access service is allowed to serve Assets from.
     * @param defaultZones list of zones that access service should set in all new Assets.
     * @param publishZones list of zones that the access service sets up in published Asset instances.
     * @param auditLog logging destination
     * @param localServerUserId userId used for server initiated actions
     * @param maxPageSize maximum page size
     * @param inTopicConnectorProviderName class name of the client side in topic connector
     * @param inTopicEventBusConnection connection for the event bus configured with the in topic
     * @param outTopicConnectorProviderName class name of the client side out topic connector
     * @param outTopicEventBusConnection connection for the event bus configured with the out topic
     * @throws NewInstanceException a problem occurred during initialization
     */
    public OMASServiceInstance(String                  serviceName,
                               OMRSRepositoryConnector repositoryConnector,
                               List<String>            supportedZones,
                               List<String>            defaultZones,
                               List<String>            publishZones,
                               AuditLog                auditLog,
                               String                  localServerUserId,
                               int                     maxPageSize,
                               String                  inTopicConnectorProviderName,
                               Connection              inTopicEventBusConnection,
                               String                  outTopicConnectorProviderName,
                               Connection              outTopicEventBusConnection) throws NewInstanceException
    {
        super(null, serviceName, auditLog, localServerUserId, maxPageSize);

        final String methodName = "new ServiceInstance";

        if (repositoryConnector != null)
        {
            try
            {
                this.repositoryConnector = repositoryConnector;
                this.setServerName(repositoryConnector.getServerName());
                this.metadataCollection = repositoryConnector.getMetadataCollection();
                this.repositoryHelper = repositoryConnector.getRepositoryHelper();

                this.errorHandler = new RepositoryErrorHandler(repositoryHelper, serviceName, serverName, auditLog);
                this.repositoryHandler = new RepositoryHandler(auditLog, repositoryHelper, errorHandler, metadataCollection, maxPageSize);
                this.supportedZones = supportedZones;
                this.defaultZones = defaultZones;
                this.publishZones = publishZones;
                this.inTopicConnectorProviderName = inTopicConnectorProviderName;
                this.inTopicEventBusConnection = inTopicEventBusConnection;
                this.outTopicConnectorProviderName = outTopicConnectorProviderName;
                this.outTopicEventBusConnection = outTopicEventBusConnection;
            }
            catch (Exception error)
            {
                throw new NewInstanceException(OMAGServerInstanceErrorCode.OMRS_NOT_INITIALIZED.getMessageDefinition(methodName),
                                               this.getClass().getName(),
                                               methodName);

            }
        }
        else
        {
            throw new NewInstanceException(OMAGServerInstanceErrorCode.OMRS_NOT_INITIALIZED.getMessageDefinition(methodName),
                                           this.getClass().getName(),
                                           methodName);

        }
    }


    /**
     * Return the server name. Used during OMAS initialization which is why the exception
     * is different.
     *
     * @return serverName name of the server for this instance
     * @throws NewInstanceException a problem occurred during initialization
     */
    @Override
    public String getServerName() throws NewInstanceException
    {
        final String methodName = "getServerName";

        if (serverName != null)
        {
            return serverName;
        }
        else
        {
            throw new NewInstanceException(OMAGServerInstanceErrorCode.OMRS_NOT_AVAILABLE.getMessageDefinition(methodName),
                                           this.getClass().getName(),
                                           methodName);
        }
    }


    public RegisteredOMAGService getRegisteredOMAGService(int accessServiceCode)
    {
        List<AccessServiceRegistrationEntry> accessServiceRegistrationList = OMAGAccessServiceRegistration.getAccessServiceRegistrationList();

        for (AccessServiceRegistrationEntry registration : accessServiceRegistrationList)
        {
            if (registration.getAccessServiceCode() == accessServiceCode)
            {
                RegisteredOMAGService omagService = new RegisteredOMAGService();

                omagService.setServiceId(registration.getAccessServiceCode());
                omagService.setServiceName(registration.getAccessServiceFullName());
                omagService.setServiceDescription(registration.getAccessServiceDescription());
                omagService.setServiceURLMarker(registration.getAccessServiceURLMarker());
                omagService.setServiceWiki(registration.getAccessServiceWiki());
                omagService.setServiceDevelopmentStatus(registration.getAccessServiceDevelopmentStatus());
                omagService.setServerType(ServerTypeClassification.METADATA_ACCESS_SERVER.getServerTypeName());

                return omagService;
            }
        }

        return null;
    }

    /**
     * Validate that the repository services are ok for this instance.
     *
     * @param methodName calling method
     * @throws PropertyServerException problem with the repository services
     */
    protected void validateActiveRepository(String  methodName) throws PropertyServerException
    {
        if ((repositoryConnector == null) || (metadataCollection == null) || (! repositoryConnector.isActive()))
        {
            throw new PropertyServerException(OMAGServerInstanceErrorCode.OMRS_NOT_AVAILABLE.getMessageDefinition(methodName),
                                              this.getClass().getName(),
                                              methodName);
        }
    }


    /**
     * Return the local metadata collection for this server.
     *
     * @return OMRSMetadataCollection object
     * @throws PropertyServerException the instance has not been initialized successfully
     */
    OMRSMetadataCollection getMetadataCollection() throws PropertyServerException
    {
        final String methodName = "getMetadataCollection";

        validateActiveRepository(methodName);

        return metadataCollection;
    }


    /**
     * Return the repository connector for this server.
     *
     * @return OMRSRepositoryConnector object
     * @throws PropertyServerException the instance has not been initialized successfully
     */
    OMRSRepositoryConnector  getRepositoryConnector() throws PropertyServerException
    {
        final String methodName = "getRepositoryConnector";

        validateActiveRepository(methodName);

        return repositoryConnector;
    }


    /**
     * Return the repository helper for this server.  It provides many helpful
     * functions for managing .
     *
     * @return repository helper
     * @throws PropertyServerException the instance has not been initialized successfully
     */
    OMRSRepositoryHelper getRepositoryHelper() throws PropertyServerException
    {
        final String methodName = "getRepositoryHelper";

        validateActiveRepository(methodName);

        return repositoryHelper;
    }


    /**
     * Return the repository handler providing an advanced API to the repository
     * services.
     *
     * @return repository handler
     * @throws PropertyServerException the instance has not been initialized successfully
     */
    RepositoryHandler getRepositoryHandler() throws PropertyServerException
    {
        final String methodName = "getRepositoryHandler";

        validateActiveRepository(methodName);

        return repositoryHandler;
    }


    /**
     * Return the handler for managing errors from the repository services.
     *
     * @return repository error handler
     * @throws PropertyServerException the instance has not been initialized successfully
     */
    RepositoryErrorHandler getErrorHandler() throws PropertyServerException
    {
        final String methodName = "getErrorHandler";

        validateActiveRepository(methodName);

        return errorHandler;
    }


    /**
     * Return the list of zones that this instance of the OMAS should support.
     *
     * @return list of zone names.
     * @throws PropertyServerException the instance has not been initialized successfully
     */
    List<String> getSupportedZones() throws PropertyServerException
    {
        final String methodName = "getSupportedZones";

        validateActiveRepository(methodName);

        return supportedZones;
    }


    /**
     * Return the list of zones that this instance of the OMAS should set in any new Asset.
     *
     * @return list of zone names.
     * @throws PropertyServerException the instance has not been initialized successfully
     */
    List<String> getDefaultZones() throws PropertyServerException
    {
        final String methodName = "getDefaultZones";

        validateActiveRepository(methodName);

        return defaultZones;
    }


    /**
     * Return the list of zones that this instance of the OMAS should set in any published Asset.
     *
     * @return list of zone names.
     * @throws PropertyServerException the instance has not been initialized successfully
     */
    List<String> getPublishZones() throws PropertyServerException
    {
        final String methodName = "getPublishZones";

        validateActiveRepository(methodName);

        return publishZones;
    }


    /**
     * Return the connection used in the client to create a connector to send events from the in topic.
     *
     * @param callerId unique identifier of the caller
     * @return connection object for client
     * @throws PropertyServerException problem connecting to event bus
     */
    public Connection getInTopicClientConnection(String callerId) throws PropertyServerException
    {
        final String methodName = "getInTopicClientConnection";

        return this.getClientTopicConnection(callerId,
                                             inTopicEventBusConnection,
                                             inTopicConnectorProviderName,
                                             OpenMetadataTopicProvider.EVENT_DIRECTION_OUT_ONLY,
                                             methodName);
    }



    /**
     * Return the connection used in the client to create a connector to access events from the out topic.
     *
     * @param callerId unique identifier of the caller
     * @return connection object for client
     */
    Connection getOutTopicClientConnection(String  callerId) throws PropertyServerException
    {
        final String methodName = "getOutTopicClientConnection";

        return this.getClientTopicConnection(callerId,
                                             outTopicEventBusConnection,
                                             outTopicConnectorProviderName,
                                             OpenMetadataTopicProvider.EVENT_DIRECTION_IN_ONLY,
                                             methodName);
    }



    /**
     * Return the connection used in the client to create a connector to access events from the out topic.
     *
     * @param callerId unique identifier of the caller
     * @param topicEventBusConnection connection for the event bus pre-populated with the topic name
     * @param connectorProviderClassName Java class name of the connector provider for the client-side topic connector
     * @param eventDirection should the event bus be restricted in the direction that events can flow?
     * @param methodName calling method so error messages show whether this was an in or an out topic problem.
     * @return connection object for client
     */
    private Connection getClientTopicConnection(String     callerId,
                                                Connection topicEventBusConnection,
                                                String     connectorProviderClassName,
                                                String     eventDirection,
                                                String     methodName) throws PropertyServerException
    {
        if ((topicEventBusConnection != null) && (connectorProviderClassName != null))
        {
            /*
             * The caller Identifier needs to be inserted into the configuration properties of the event bus connection.  This is used
             * to identify the caller to the event bus so it knows which events the caller has seen already. (It effectively controls
             * the cursor for the caller for the events on the topic.)
             */
            Connection  newEventBusConnection  = new Connection(topicEventBusConnection);

            Map<String, Object> configurationProperties = newEventBusConnection.getConfigurationProperties();

            if (configurationProperties == null)
            {
                configurationProperties = new HashMap<>();
            }

            configurationProperties.put(serverIdPropertyName, callerId);
            configurationProperties.put(OpenMetadataTopicProvider.EVENT_DIRECTION_PROPERTY_NAME, eventDirection);

            newEventBusConnection.setConfigurationProperties(configurationProperties);

            return this.buildClientTopicConnection(newEventBusConnection, connectorProviderClassName, methodName);
        }
        else
        {
            auditLog.logMessage(methodName, OMAGServerInstanceAuditCode.NO_TOPIC_INFORMATION.getMessageDefinition(methodName, serviceName));

            throw new PropertyServerException(OMAGServerInstanceErrorCode.NO_TOPIC_INFORMATION.getMessageDefinition(methodName, serviceName),
                                              this.getClass().getName(),
                                              methodName);
        }
    }


    /**
     * Create the client-side connector for one of this access service's topics.
     *
     * @param topicEventBusConnection connection from the configuration properties - the event bus
     * @param accessServiceConnectorProviderClassName class name of connector provider
     * @param methodName calling method so error messages show whether this was an in or an out topic problem.
     * @return connector to access the topic
     * @throws PropertyServerException problem creating connector
     */
    private   Connection buildClientTopicConnection(Connection   topicEventBusConnection,
                                                    String       accessServiceConnectorProviderClassName,
                                                    String       methodName) throws PropertyServerException
    {
        final String connectionDescription = "Client-side topic connection.";
        final String eventSource = "Topic Event Bus";

        ElementOrigin elementOrigin = new ElementOrigin();

        elementOrigin.setOriginCategory(ElementOriginCategory.CONFIGURATION);

        String connectionName = "OutTopicConnector." + serviceName;

        VirtualConnection connection = new VirtualConnection();

        connection.setOrigin(elementOrigin);
        connection.setType(VirtualConnection.getVirtualConnectionType());
        connection.setQualifiedName(connectionName);
        connection.setDisplayName(connectionName);
        connection.setDescription(connectionDescription);
        connection.setConnectorType(getConnectorType(accessServiceConnectorProviderClassName, methodName));

        /*
         * The event bus for this server is embedded in the out topic connection.
         */
        EmbeddedConnection embeddedConnection = new EmbeddedConnection();

        embeddedConnection.setDisplayName(eventSource);
        embeddedConnection.setArguments(null);
        embeddedConnection.setEmbeddedConnection(topicEventBusConnection);

        List<EmbeddedConnection> embeddedConnections = new ArrayList<>();
        embeddedConnections.add(embeddedConnection);

        connection.setEmbeddedConnections(embeddedConnections);

        return connection;
    }


    /**
     * Return the connector type for the requested connector provider.  This is best used for connector providers that
     * can return their own connector type.  Otherwise it makes one up.
     *
     * @param connectorProviderClassName name of the connector provider class
     * @param methodName calling method so error messages show whether this was an in or an out topic problem.
     * @return ConnectorType bean
     * @throws PropertyServerException problem creating connector
     */
    private ConnectorType   getConnectorType(String  connectorProviderClassName,
                                             String  methodName) throws PropertyServerException
    {
        ConnectorType  connectorType = null;

        if (connectorProviderClassName != null)
        {
            try
            {
                Class<?>   connectorProviderClass = Class.forName(connectorProviderClassName);
                Object     potentialConnectorProvider = connectorProviderClass.getDeclaredConstructor().newInstance();

                ConnectorProvider connectorProvider = (ConnectorProvider)potentialConnectorProvider;

                connectorType = connectorProvider.getConnectorType();

                if (connectorType == null)
                {
                    connectorType = new ConnectorType();

                    connectorType.setType(connectorType.getType());
                    connectorType.setQualifiedName(connectorProviderClassName);
                    connectorType.setDisplayName(connectorProviderClass.getSimpleName());
                    connectorType.setDescription("ConnectorType for " + connectorType.getDisplayName());
                    connectorType.setConnectorProviderClassName(connectorProviderClassName);
                }
            }
            catch (Exception error)
            {
                auditLog.logException(methodName,
                                      OMAGServerInstanceAuditCode.BAD_TOPIC_CONNECTOR_PROVIDER.getMessageDefinition(methodName,
                                                                                                                    serviceName,
                                                                                                                    error.getClass().getName(),
                                                                                                                    error.getMessage()),
                                      error);

                throw new PropertyServerException(OMAGServerInstanceErrorCode.BAD_TOPIC_CONNECTOR_PROVIDER.getMessageDefinition(methodName,
                                                                                                                                serviceName,
                                                                                                                                error.getClass().getName(),
                                                                                                                                error.getMessage()),
                                                  this.getClass().getName(),
                                                  methodName);
            }
        }

        return connectorType;
    }
}
