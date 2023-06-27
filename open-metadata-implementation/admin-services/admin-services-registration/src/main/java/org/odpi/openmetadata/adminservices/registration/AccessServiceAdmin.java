/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adminservices.registration;

import org.odpi.openmetadata.adminservices.ffdc.OMAGAdminAuditCode;
import org.odpi.openmetadata.adminservices.configuration.properties.AccessServiceConfig;
import org.odpi.openmetadata.adminservices.ffdc.OMAGAdminErrorCode;
import org.odpi.openmetadata.adminservices.ffdc.exception.OMAGConfigurationErrorException;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.connectors.Connector;
import org.odpi.openmetadata.frameworks.connectors.ConnectorBroker;
import org.odpi.openmetadata.frameworks.connectors.ConnectorProvider;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.*;
import org.odpi.openmetadata.repositoryservices.auditlog.OMRSAuditLog;
import org.odpi.openmetadata.repositoryservices.auditlog.OMRSAuditingComponent;
import org.odpi.openmetadata.repositoryservices.connectors.omrstopic.OMRSTopicConnector;
import org.odpi.openmetadata.repositoryservices.connectors.omrstopic.OMRSTopicListener;
import org.odpi.openmetadata.repositoryservices.connectors.openmetadatatopic.OpenMetadataTopicConnector;
import org.odpi.openmetadata.repositoryservices.connectors.openmetadatatopic.OpenMetadataTopicProvider;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryConnector;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * AccessServiceAdmin is the interface that an access service implements to receive its configuration.
 * The java class that implements this interface is created with a default constructor and then
 * the initialize method is called.  It is configured in the AccessServiceDescription enumeration.
 */
public abstract class AccessServiceAdmin
{
    private static final int defaultKarmaPointThreshold = 500;
    private static final int defaultKarmaPointInterval  = 0;


    /*
     * These are standard property names that an access service may support.  They are passed in the
     * AccessServiceConfig as the accessServicesOptions.  Individual access services may support
     * additional properties.
     */
    protected String   supportedZonesPropertyName      = "SupportedZones";      /* Common */
    protected String   defaultZonesPropertyName        = "DefaultZones";        /* Common */
    protected String   publishZonesPropertyName        = "PublishZones";        /* Common */
    protected String   karmaPointPlateauPropertyName   = "KarmaPointPlateau";   /* Community Profile OMAS */
    protected String   karmaPointIncrementPropertyName = "KarmaPointIncrement"; /* Community Profile OMAS */

    private String     fullServiceName = null;


    /**
     * Initialize the access service.
     *
     * @param accessServiceConfigurationProperties  specific configuration properties for this access service.
     * @param enterpriseOMRSTopicConnector  connector for receiving OMRS Events from the cohorts
     * @param enterpriseOMRSRepositoryConnector  connector for querying the cohort repositories
     * @param auditLog  audit log component for logging messages.
     * @param serverUserName  user id to use on OMRS calls where there is no end user.
     * @throws OMAGConfigurationErrorException invalid parameters in the configuration properties.
     */
    @Deprecated
    public void initialize(AccessServiceConfig     accessServiceConfigurationProperties,
                           OMRSTopicConnector      enterpriseOMRSTopicConnector,
                           OMRSRepositoryConnector enterpriseOMRSRepositoryConnector,
                           OMRSAuditLog            auditLog,
                           String                  serverUserName) throws OMAGConfigurationErrorException
    {
        this.initialize(accessServiceConfigurationProperties,
                        enterpriseOMRSTopicConnector,
                        enterpriseOMRSRepositoryConnector,
                        (AuditLog)auditLog,
                        serverUserName);
    }


    /**
     * Initialize the access service.
     *
     * @param accessServiceConfigurationProperties  specific configuration properties for this access service.
     * @param enterpriseOMRSTopicConnector  connector for receiving OMRS Events from the cohorts
     * @param enterpriseOMRSRepositoryConnector  connector for querying the cohort repositories
     * @param auditLog  audit log component for logging messages.
     * @param serverUserName  user id to use on OMRS calls where there is no end user.
     * @throws OMAGConfigurationErrorException invalid parameters in the configuration properties.
     */
    public void initialize(AccessServiceConfig     accessServiceConfigurationProperties,
                           OMRSTopicConnector      enterpriseOMRSTopicConnector,
                           OMRSRepositoryConnector enterpriseOMRSRepositoryConnector,
                           AuditLog                auditLog,
                           String                  serverUserName) throws OMAGConfigurationErrorException
    {
    }


    /**
     * Return the cached service name.
     *
     * @return string name
     */
    public String getFullServiceName()
    {
        return fullServiceName;
    }


    /**
     * Set up the cached service name.
     *
     * @param fullServiceName string name
     */
    public void setFullServiceName(String fullServiceName)
    {
        this.fullServiceName = fullServiceName;
    }


    /**
     * Shutdown the access service.
     */
    public abstract void shutdown();


    /**
     * Extract the supported zones property from the access services option.
     *
     * @param accessServiceOptions options passed to the access service.
     * @param accessServiceFullName name of calling service
     * @param auditLog audit log for error messages
     * @return null or list of zone names
     * @throws OMAGConfigurationErrorException the supported zones property is not a list of zone names.
     */
    protected List<String> extractSupportedZones(Map<String, Object> accessServiceOptions,
                                                 String              accessServiceFullName,
                                                 AuditLog            auditLog) throws OMAGConfigurationErrorException
    {
        final String  methodName = "extractSupportedZones";

        if (accessServiceOptions == null)
        {
            return null;
        }
        else
        {
            Object   zoneListObject = accessServiceOptions.get(supportedZonesPropertyName);

            if (zoneListObject == null)
            {
                auditLog.logMessage(methodName, OMAGAdminAuditCode.ALL_ZONES.getMessageDefinition(accessServiceFullName));
                return null;
            }
            else
            {
                try
                {
                    @SuppressWarnings("unchecked")
                    List<String> zoneList = (List<String>) zoneListObject;

                    auditLog.logMessage(methodName, OMAGAdminAuditCode.SUPPORTED_ZONES.getMessageDefinition(accessServiceFullName, zoneList.toString()));
                    return zoneList;
                }
                catch (Exception error)
                {
                    logBadConfigProperties(accessServiceFullName,
                                           supportedZonesPropertyName,
                                           zoneListObject.toString(),
                                           auditLog,
                                           methodName,
                                           error);

                    /* unreachable */
                    return null;
                }
            }
        }
    }


    /**
     * Extract the publish zones property from the access services option.
     *
     * @param accessServiceOptions options passed to the access service.
     * @param accessServiceFullName name of calling service
     * @param auditLog audit log for error messages
     * @return null or list of zone names
     * @throws OMAGConfigurationErrorException the supported zones property is not a list of zone names.
     */
    protected List<String> extractPublishZones(Map<String, Object> accessServiceOptions,
                                               String              accessServiceFullName,
                                               AuditLog            auditLog) throws OMAGConfigurationErrorException
    {
        final String  methodName = "extractPublishZones";

        if (accessServiceOptions == null)
        {
            return null;
        }
        else
        {
            Object   zoneListObject = accessServiceOptions.get(publishZonesPropertyName);

            if (zoneListObject == null)
            {
                auditLog.logMessage(methodName, OMAGAdminAuditCode.PUBLISH_ZONES.getMessageDefinition(accessServiceFullName, "<null>"));
                return null;
            }
            else
            {
                try
                {
                    @SuppressWarnings("unchecked")
                    List<String>  zoneList =  (List<String>)zoneListObject;

                    auditLog.logMessage(methodName, OMAGAdminAuditCode.PUBLISH_ZONES.getMessageDefinition(accessServiceFullName, zoneList.toString()));
                    return zoneList;
                }
                catch (Exception error)
                {
                    logBadConfigProperties(accessServiceFullName,
                                           publishZonesPropertyName,
                                           zoneListObject.toString(),
                                           auditLog,
                                           methodName,
                                           error);

                    /* unreachable */
                    return null;
                }
            }
        }
    }


    /**
     * Extract the default zones property from the access services option.
     *
     * @param accessServiceOptions options passed to the access service.
     * @param accessServiceFullName name of calling service
     * @param auditLog audit log for error messages
     * @return null or list of zone names
     * @throws OMAGConfigurationErrorException the supported zones property is not a list of zone names.
     */
    protected List<String> extractDefaultZones(Map<String, Object> accessServiceOptions,
                                               String              accessServiceFullName,
                                               AuditLog            auditLog) throws OMAGConfigurationErrorException
    {
        final String  methodName = "extractDefaultZones";

        if (accessServiceOptions == null)
        {
            return null;
        }
        else
        {
            Object   zoneListObject = accessServiceOptions.get(defaultZonesPropertyName);

            if (zoneListObject == null)
            {
                auditLog.logMessage(methodName, OMAGAdminAuditCode.DEFAULT_ZONES.getMessageDefinition(accessServiceFullName, "<null>"));
                return null;
            }
            else
            {
                try
                {
                    @SuppressWarnings("unchecked")
                    List<String>  zoneList =  (List<String>)zoneListObject;

                    auditLog.logMessage(methodName, OMAGAdminAuditCode.DEFAULT_ZONES.getMessageDefinition(accessServiceFullName, zoneList.toString()));
                    return zoneList;
                }
                catch (Exception error)
                {
                    logBadConfigProperties(accessServiceFullName,
                                           defaultZonesPropertyName,
                                           zoneListObject.toString(),
                                           auditLog,
                                           methodName,
                                           error);

                    /* unreachable */
                    return null;
                }
            }
        }
    }


    /**
     * Extract the karma point plateau property from the access services option.
     *
     * @param accessServiceOptions options passed to the access service.
     * @param accessServiceFullName name of calling service
     * @param auditLog audit log for error messages
     * @return null or list of zone names
     * @throws OMAGConfigurationErrorException the supported zones property is not a list of zone names.
     */
    protected int extractKarmaPointIncrement(Map<String, Object> accessServiceOptions,
                                             String              accessServiceFullName,
                                             AuditLog            auditLog) throws OMAGConfigurationErrorException
    {
        final String  methodName = "extractKarmaPointIncrement";

        if (accessServiceOptions == null)
        {
            return this.useDefaultKarmaPointIncrement(accessServiceFullName, auditLog, methodName);
        }
        else
        {
            Object   incrementObject = accessServiceOptions.get(karmaPointIncrementPropertyName);

            if (incrementObject == null)
            {
                return this.useDefaultKarmaPointIncrement(accessServiceFullName, auditLog, methodName);
            }
            else
            {
                try
                {
                    int increment = Integer.parseInt(incrementObject.toString());

                    auditLog.logMessage(methodName, OMAGAdminAuditCode.KARMA_POINT_COLLECTION_INCREMENT.getMessageDefinition(accessServiceFullName,
                                                                                                                             Integer.toString(increment)));

                    return increment;
                }
                catch (Exception error)
                {
                    logBadConfigProperties(accessServiceFullName,
                                           karmaPointIncrementPropertyName,
                                           incrementObject.toString(),
                                           auditLog,
                                           methodName,
                                           error);

                    /* unreachable */
                    return 0;
                }
            }
        }
    }


    /**
     * Set up and log the default karma point increment.
     *
     * @param accessServiceFullName name of this access service
     * @param auditLog audit log to use
     * @param methodName calling method
     * @return default value
     */
    private int useDefaultKarmaPointIncrement(String       accessServiceFullName,
                                              AuditLog     auditLog,
                                              String       methodName)
    {
        auditLog.logMessage(methodName, OMAGAdminAuditCode.NO_KARMA_POINT_COLLECTION.getMessageDefinition(accessServiceFullName));

        return defaultKarmaPointInterval;
    }


    /**
     * Extract the karma point plateau property from the access services option.
     *
     * @param accessServiceOptions options passed to the access service.
     * @param accessServiceFullName name of calling service
     * @param auditLog audit log for error messages
     * @return null or list of zone names
     * @throws OMAGConfigurationErrorException the supported zones property is not a list of zone names.
     */
    protected int extractKarmaPointPlateau(Map<String, Object> accessServiceOptions,
                                           String              accessServiceFullName,
                                           AuditLog            auditLog) throws OMAGConfigurationErrorException
    {
        final String  methodName = "extractKarmaPointPlateau";

        if (accessServiceOptions == null)
        {
            return this.useDefaultPlateauThreshold(accessServiceFullName, auditLog, methodName);
        }
        else
        {
            Object   plateauThresholdObject = accessServiceOptions.get(karmaPointPlateauPropertyName);

            if (plateauThresholdObject == null)
            {
                return this.useDefaultPlateauThreshold(accessServiceFullName, auditLog, methodName);
            }
            else
            {
                try
                {
                    int plateauThreshold =  Integer.parseInt(plateauThresholdObject.toString());

                    auditLog.logMessage(methodName,
                                        OMAGAdminAuditCode.PLATEAU_THRESHOLD.getMessageDefinition(accessServiceFullName,
                                                                                                  Integer.toString(plateauThreshold)));

                    return plateauThreshold;
                }
                catch (Exception error)
                {
                    logBadConfigProperties(accessServiceFullName,
                                           karmaPointPlateauPropertyName,
                                           plateauThresholdObject.toString(),
                                           auditLog,
                                           methodName,
                                           error);

                    /* unreachable */
                    return 0;
                }
            }
        }
    }


    /**
     * Set up and log the default karma point threshold.
     *
     * @param accessServiceFullName name of this access service
     * @param auditLog audit log to use
     * @param methodName calling method
     * @return default value
     */
    private int useDefaultPlateauThreshold(String       accessServiceFullName,
                                           AuditLog     auditLog,
                                           String       methodName)
    {
        auditLog.logMessage(methodName,
                            OMAGAdminAuditCode.DEFAULT_PLATEAU_THRESHOLD.getMessageDefinition(accessServiceFullName,
                                                                                              Integer.toString(defaultKarmaPointThreshold)));

        return defaultKarmaPointThreshold;
    }


    /**
     * Log that a property value is incorrect.
     *
     * @param accessServiceFullName name of the calling access service
     * @param propertyName name of the property in error
     * @param propertyValue value of the property that is in error
     * @param auditLog log to write message to
     * @param methodName calling method
     * @param error resulting exception
     *
     * @throws OMAGConfigurationErrorException exception documenting the error
     */
    private void logBadConfigProperties(String       accessServiceFullName,
                                        String       propertyName,
                                        String       propertyValue,
                                        AuditLog     auditLog,
                                        String       methodName,
                                        Throwable    error) throws OMAGConfigurationErrorException
    {
        auditLog.logMessage(methodName, OMAGAdminAuditCode.BAD_CONFIG_PROPERTY.getMessageDefinition(accessServiceFullName, propertyValue, propertyName));

        throw new OMAGConfigurationErrorException(OMAGAdminErrorCode.BAD_CONFIG_PROPERTIES.getMessageDefinition(accessServiceFullName,
                                                                                                                propertyValue,
                                                                                                                propertyName,
                                                                                                                error.getClass().getName(),
                                                                                                                error.getMessage()),
                                                  this.getClass().getName(),
                                                  methodName,
                                                  error);
    }


    /**
     * Register a listener with the enterprise topic connector.
     *
     * @param accessServiceFullName name of calling access service
     * @param serverName name of OMAG Server instance
     * @param omrsTopicConnector topic connector to register with
     * @param omrsTopicListener listener to register
     * @param auditLog audit log to record messages
     *
     * @throws OMAGConfigurationErrorException problem with topic connection
     */
    protected void registerWithEnterpriseTopic(String              accessServiceFullName,
                                               String              serverName,
                                               OMRSTopicConnector  omrsTopicConnector,
                                               OMRSTopicListener   omrsTopicListener,
                                               AuditLog            auditLog) throws OMAGConfigurationErrorException
    {
        final String            actionDescription = "initialize OMAS";
        final String            methodName = "initialize";

        if (omrsTopicConnector != null)
        {
            auditLog.logMessage(actionDescription,
                                OMAGAdminAuditCode.SERVICE_REGISTERED_WITH_ENTERPRISE_TOPIC.getMessageDefinition(accessServiceFullName, serverName));

            omrsTopicConnector.registerListener(omrsTopicListener, accessServiceFullName);
        }
        else
        {
            auditLog.logMessage(actionDescription, OMAGAdminAuditCode.NO_ENTERPRISE_TOPIC.getMessageDefinition(accessServiceFullName, serverName));

            throw new OMAGConfigurationErrorException(OMAGAdminErrorCode.NO_ENTERPRISE_TOPIC.getMessageDefinition(accessServiceFullName,
                                                                                                                  serverName),
                                                      this.getClass().getName(),
                                                      methodName);
        }
    }


    /**
     * Return the name of the topic extracted from the endpoint of the topic's Connection.
     *
     * @param connection connection object
     * @return topic name
     */
    protected String getTopicName(Connection connection)
    {
        String      topicName = null;

        if (connection != null)
        {
            Endpoint topicEndpoint = connection.getEndpoint();

            if (topicEndpoint != null)
            {
                topicName = topicEndpoint.getAddress();
            }
        }

        return topicName;
    }


    /**
     * Create the connector for this access services' In Topic.
     *
     * @param inTopicConnection connection from the configuration properties
     * @param accessServiceFullName name of the calling access service
     * @param parentAuditLog audit log from the admin component
     * @return connector to access the topic
     * @throws OMAGConfigurationErrorException problem creating connector
     */
    protected OpenMetadataTopicConnector getInTopicEventBusConnector(Connection   inTopicConnection,
                                                                     String       accessServiceFullName,
                                                                     AuditLog     parentAuditLog) throws OMAGConfigurationErrorException
    {
        final String  methodName = "getInTopicConnector";

        return this.getTopicConnector(inTopicConnection,
                                      parentAuditLog.createNewAuditLog(OMRSAuditingComponent.OMAS_IN_TOPIC),
                                      accessServiceFullName,
                                      methodName);
    }


    /**
     * Create the event bus connector for this access services' Out Topic.
     *
     * @param outTopicEventBusConnection connection from the configuration properties - the event bus
     * @param accessServiceName name of the calling access service
     * @param parentAuditLog audit log from the admin component
     * @return connector to access the topic
     * @throws OMAGConfigurationErrorException problem creating connector
     */
    protected OpenMetadataTopicConnector getOutTopicEventBusConnector(Connection   outTopicEventBusConnection,
                                                                      String       accessServiceName,
                                                                      AuditLog     parentAuditLog) throws OMAGConfigurationErrorException
    {
        final String  methodName = "getOutTopicConnector";

        return this.getTopicConnector(outTopicEventBusConnection,
                                      parentAuditLog.createNewAuditLog(OMRSAuditingComponent.OMAS_OUT_TOPIC),
                                      accessServiceName,
                                      methodName);
    }



    /**
     * Create the connector for this access services' Out Topic.
     *
     * @param outTopicEventBusConnection connection from the configuration properties - the event bus
     * @param accessServiceFullName name of the calling access service
     * @param accessServiceConnectorProviderClassName class name of connector provider
     * @param auditLog audit log from the admin component
     * @return connector to access the topic
     * @throws OMAGConfigurationErrorException problem creating connector
     */
    protected Connection getServerSideOutTopicConnection(Connection   outTopicEventBusConnection,
                                                         String       accessServiceFullName,
                                                         String       accessServiceConnectorProviderClassName,
                                                         AuditLog     auditLog) throws OMAGConfigurationErrorException
    {
        final String methodName = "getOutTopicConnection";
        final String connectionDescription = "OMRS default cohort topic connection.";
        final String eventSource = "Server Event Bus";


        String connectionName = "OutTopicConnector." + accessServiceFullName;

        VirtualConnection connection = new VirtualConnection();

        ElementOrigin elementOrigin = new ElementOrigin();
        elementOrigin.setOriginCategory(ElementOriginCategory.CONFIGURATION);
        elementOrigin.setSourceServer(eventSource);

        connection.setOrigin(elementOrigin);
        connection.setType(VirtualConnection.getVirtualConnectionType());
        connection.setGUID(UUID.randomUUID().toString());
        connection.setQualifiedName(connectionName);
        connection.setDisplayName(connectionName);
        connection.setDescription(connectionDescription);
        connection.setConnectorType(getConnectorType(accessServiceFullName,
                                                     accessServiceConnectorProviderClassName,
                                                     auditLog,
                                                     methodName));

        /*
         * Adjust event bus connector for outbound events only.
         */
        Map<String, Object> configurationProperties = outTopicEventBusConnection.getConfigurationProperties();
        if (configurationProperties == null)
        {
            configurationProperties = new HashMap<>();
        }
        configurationProperties.put(OpenMetadataTopicProvider.EVENT_DIRECTION_PROPERTY_NAME, OpenMetadataTopicProvider.EVENT_DIRECTION_OUT_ONLY);
        outTopicEventBusConnection.setConfigurationProperties(configurationProperties);

        /*
         * The event bus for this server is embedded in the out topic connection.
         */
        EmbeddedConnection embeddedConnection = new EmbeddedConnection();

        embeddedConnection.setDisplayName(eventSource);
        embeddedConnection.setArguments(null);
        embeddedConnection.setEmbeddedConnection(outTopicEventBusConnection);

        List<EmbeddedConnection>      embeddedConnections = new ArrayList<>();
        embeddedConnections.add(embeddedConnection);

        connection.setEmbeddedConnections(embeddedConnections);

        return connection;
    }


    /**
     * Return the connector type for the requested connector provider.  This is best used for connector providers that
     * can return their own connector type.  Otherwise it makes one up.
     *
     * @param accessServiceFullName name of the calling access service
     * @param connectorProviderClassName class name of connector provider
     * @param auditLog audit log from the admin component
     * @return ConnectorType bean
     * @throws OMAGConfigurationErrorException problem creating connector
     */
    private ConnectorType   getConnectorType(String       accessServiceFullName,
                                             String       connectorProviderClassName,
                                             AuditLog     auditLog,
                                             String       methodName) throws OMAGConfigurationErrorException
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
                    connectorType.setGUID(UUID.randomUUID().toString());
                    connectorType.setQualifiedName(connectorProviderClassName);
                    connectorType.setDisplayName(connectorProviderClass.getSimpleName());
                    connectorType.setDescription("ConnectorType for " + connectorType.getDisplayName());
                    connectorType.setConnectorProviderClassName(connectorProviderClassName);
                }
            }
            catch (Exception error)
            {
                auditLog.logException(methodName,
                                      OMAGAdminAuditCode.BAD_TOPIC_CONNECTOR_PROVIDER.getMessageDefinition(methodName,
                                                                                                           accessServiceFullName,
                                                                                                           error.getClass().getName(),
                                                                                                           error.getMessage()),
                                      error);

                throw new OMAGConfigurationErrorException(OMAGAdminErrorCode.BAD_TOPIC_CONNECTOR_PROVIDER.getMessageDefinition(methodName,
                                                                                                                               accessServiceFullName,
                                                                                                                               error.getClass().getName(),
                                                                                                                               error.getMessage()),
                                                          this.getClass().getName(),
                                                          methodName);
            }
        }

        return connectorType;
    }


    /**
     * Log that an unexpected exception was received during start up.
     *
     * @param actionDescription calling method
     * @param fullAccessServiceName name of the access service
     * @param error exception that was caught
     * @throws OMAGConfigurationErrorException wrapped exception
     */
    protected void throwUnexpectedInitializationException(String    actionDescription,
                                                          String    fullAccessServiceName,
                                                          Throwable error) throws OMAGConfigurationErrorException
    {
        throw new OMAGConfigurationErrorException(OMAGAdminErrorCode.UNEXPECTED_INITIALIZATION_EXCEPTION.getMessageDefinition(fullAccessServiceName,
                                                                                                                              error.getClass().getName(),
                                                                                                                              error.getMessage()),
                                                  this.getClass().getName(),
                                                  actionDescription,
                                                  error);
    }


    /**
     * Create an instance of an open metadata topic connector using the supplied connection.
     *
     * @param topicConnection connection needed to create the connector
     * @param topicConnectorClass class to cast to
     * @param <T> type name
     * @param accessServiceFullName name of the calling access service
     * @param auditLog audit log for this connector
     * @param methodName calling method
     * @return connector to access the topic
     * @throws OMAGConfigurationErrorException problem creating connector
     */
    @SuppressWarnings("unchecked")
    public <T>T getTopicConnector(Connection   topicConnection,
                                  Class<T>     topicConnectorClass,
                                  AuditLog     auditLog,
                                  String       accessServiceFullName,
                                  String       methodName) throws OMAGConfigurationErrorException
    {
        try
        {
            ConnectorBroker connectorBroker = new ConnectorBroker(auditLog);
            Connector       connector       = connectorBroker.getConnector(topicConnection);

            connector.start();

            return (T) connector;
        }
        catch (Exception   error)
        {
            auditLog.logException(methodName,
                                  OMAGAdminAuditCode.BAD_TOPIC_CONNECTOR.getMessageDefinition(methodName,
                                                                                              accessServiceFullName,
                                                                                              error.getClass().getName(),
                                                                                              error.getMessage()),
                                  error);

            throw new OMAGConfigurationErrorException(OMAGAdminErrorCode.BAD_TOPIC_CONNECTOR.getMessageDefinition(methodName,
                                                                                                                  accessServiceFullName,
                                                                                                                  error.getClass().getName(),
                                                                                                                  error.getMessage()),
                                                      this.getClass().getName(),
                                                      methodName);
        }
    }


    /**
     * Create an instance of an open metadata topic connector using the supplied connection.
     *
     * @param topicConnection connection needed to create the connector
     * @param accessServiceFullName name of the calling access service
     * @param auditLog audit log for this connector
     * @return connector to access the topic
     * @throws OMAGConfigurationErrorException problem creating connector
     */
    private OpenMetadataTopicConnector getTopicConnector(Connection   topicConnection,
                                                         AuditLog     auditLog,
                                                         String       accessServiceFullName,
                                                         String       methodName) throws OMAGConfigurationErrorException
    {
        return this.getTopicConnector(topicConnection, OpenMetadataTopicConnector.class, auditLog, accessServiceFullName, methodName);
    }
}
