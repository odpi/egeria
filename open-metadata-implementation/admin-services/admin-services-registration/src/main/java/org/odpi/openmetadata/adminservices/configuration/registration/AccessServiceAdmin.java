/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adminservices.configuration.registration;

import org.odpi.openmetadata.adminservices.configuration.auditlog.OMAGAuditCode;
import org.odpi.openmetadata.adminservices.configuration.properties.AccessServiceConfig;
import org.odpi.openmetadata.adminservices.ffdc.OMAGAdminErrorCode;
import org.odpi.openmetadata.adminservices.ffdc.exception.OMAGConfigurationErrorException;
import org.odpi.openmetadata.frameworks.connectors.Connector;
import org.odpi.openmetadata.frameworks.connectors.ConnectorBroker;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.Connection;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.Endpoint;
import org.odpi.openmetadata.repositoryservices.auditlog.OMRSAuditLog;
import org.odpi.openmetadata.repositoryservices.auditlog.OMRSAuditingComponent;
import org.odpi.openmetadata.repositoryservices.connectors.omrstopic.OMRSTopicConnector;
import org.odpi.openmetadata.repositoryservices.connectors.omrstopic.OMRSTopicListener;
import org.odpi.openmetadata.repositoryservices.connectors.openmetadatatopic.OpenMetadataTopicConnector;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryConnector;

import java.util.List;
import java.util.Map;

/**
 * AccessServiceAdmin is the interface that an access service implements to receive its configuration.
 * The java class that implements this interface is created with a default constructor and then
 * the initialize method is called.  It is configured in the AccessServiceDescription enumeration.
 */
public abstract class AccessServiceAdmin
{
    private static  int defaultKarmaPointThreshold = 500;
    private static  int defaultKarmaPointInterval  = 0;


    /*
     * These are standard property names that an access service may support.  They are passed in the
     * AccessServiceConfig as the accessServicesOptions.  Individual access services may support
     * additional properties.
     */
    protected String   supportedZonesPropertyName      = "SupportedZones";      /* Common */
    protected String   defaultZonesPropertyName        = "DefaultZones";        /* Common */
    protected String   karmaPointPlateauPropertyName   = "KarmaPointPlateau";   /* Community Profile OMAS */
    protected String   karmaPointIncrementPropertyName = "KarmaPointIncrement"; /* Community Profile OMAS */


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
    public abstract void initialize(AccessServiceConfig     accessServiceConfigurationProperties,
                                    OMRSTopicConnector      enterpriseOMRSTopicConnector,
                                    OMRSRepositoryConnector enterpriseOMRSRepositoryConnector,
                                    OMRSAuditLog            auditLog,
                                    String                  serverUserName) throws OMAGConfigurationErrorException;


    /**
     * Shutdown the access service.
     */
    public abstract void shutdown();


    /**
     * Extract the supported zones property from the access services option.
     *
     * @param accessServiceOptions options passed to the access service.
     * @param accessServiceName name of calling service
     * @param auditLog audit log for error messages
     * @return null or list of zone names
     * @throws OMAGConfigurationErrorException the supported zones property is not a list of zone names.
     */
    protected List<String> extractSupportedZones(Map<String, Object> accessServiceOptions,
                                                 String              accessServiceName,
                                                 OMRSAuditLog        auditLog) throws OMAGConfigurationErrorException
    {
        final String  methodName = "extractSupportedZones";
        OMAGAuditCode auditCode;

        if (accessServiceOptions == null)
        {
            return null;
        }
        else
        {
            Object   zoneListObject = accessServiceOptions.get(supportedZonesPropertyName);

            if (zoneListObject == null)
            {
                auditCode = OMAGAuditCode.ALL_ZONES;
                auditLog.logRecord(methodName,
                                   auditCode.getLogMessageId(),
                                   auditCode.getSeverity(),
                                   auditCode.getFormattedLogMessage(accessServiceName),
                                   null,
                                   auditCode.getSystemAction(),
                                   auditCode.getUserAction());
                return null;
            }
            else
            {
                try
                {
                    @SuppressWarnings("unchecked")
                    List<String> zoneList = (List<String>) zoneListObject;

                    auditCode = OMAGAuditCode.SUPPORTED_ZONES;
                    auditLog.logRecord(methodName,
                                       auditCode.getLogMessageId(),
                                       auditCode.getSeverity(),
                                       auditCode.getFormattedLogMessage(accessServiceName, zoneList.toString()),
                                       null,
                                       auditCode.getSystemAction(),
                                       auditCode.getUserAction());

                    return zoneList;
                }
                catch (Throwable error)
                {
                    logBadConfigProperties(accessServiceName,
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
     * Extract the default zones property from the access services option.
     *
     * @param accessServiceOptions options passed to the access service.
     * @param accessServiceName name of calling service
     * @param auditLog audit log for error messages
     * @return null or list of zone names
     * @throws OMAGConfigurationErrorException the supported zones property is not a list of zone names.
     */
    protected List<String> extractDefaultZones(Map<String, Object> accessServiceOptions,
                                               String              accessServiceName,
                                               OMRSAuditLog        auditLog) throws OMAGConfigurationErrorException
    {
        final String  methodName = "extractDefaultZones";
        OMAGAuditCode auditCode;

        if (accessServiceOptions == null)
        {
            return null;
        }
        else
        {
            Object   zoneListObject = accessServiceOptions.get(defaultZonesPropertyName);

            if (zoneListObject == null)
            {
                auditCode = OMAGAuditCode.DEFAULT_ZONES;
                auditLog.logRecord(methodName,
                                   auditCode.getLogMessageId(),
                                   auditCode.getSeverity(),
                                   auditCode.getFormattedLogMessage(accessServiceName, "<null>"),
                                   null,
                                   auditCode.getSystemAction(),
                                   auditCode.getUserAction());
                return null;
            }
            else
            {
                try
                {
                    @SuppressWarnings("unchecked")
                    List<String>  zoneList =  (List<String>)zoneListObject;

                    auditCode = OMAGAuditCode.DEFAULT_ZONES;
                    auditLog.logRecord(methodName,
                                       auditCode.getLogMessageId(),
                                       auditCode.getSeverity(),
                                       auditCode.getFormattedLogMessage(accessServiceName, zoneList.toString()),
                                       null,
                                       auditCode.getSystemAction(),
                                       auditCode.getUserAction());

                    return zoneList;
                }
                catch (Throwable error)
                {
                    logBadConfigProperties(accessServiceName,
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
     * @param accessServiceName name of calling service
     * @param auditLog audit log for error messages
     * @return null or list of zone names
     * @throws OMAGConfigurationErrorException the supported zones property is not a list of zone names.
     */
    protected int extractKarmaPointIncrement(Map<String, Object> accessServiceOptions,
                                             String              accessServiceName,
                                             OMRSAuditLog        auditLog) throws OMAGConfigurationErrorException
    {
        final String  methodName = "extractKarmaPointIncrement";
        OMAGAuditCode auditCode;

        if (accessServiceOptions == null)
        {
            return this.useDefaultKarmaPointIncrement(accessServiceName, auditLog, methodName);
        }
        else
        {
            Object   incrementObject = accessServiceOptions.get(karmaPointIncrementPropertyName);

            if (incrementObject == null)
            {
                return this.useDefaultKarmaPointIncrement(accessServiceName, auditLog, methodName);
            }
            else
            {
                try
                {
                    int increment = Integer.parseInt(incrementObject.toString());

                    auditCode = OMAGAuditCode.KARMA_POINT_COLLECTION_INCREMENT;
                    auditLog.logRecord(methodName,
                                       auditCode.getLogMessageId(),
                                       auditCode.getSeverity(),
                                       auditCode.getFormattedLogMessage(accessServiceName, Integer.toString(increment)),
                                       null,
                                       auditCode.getSystemAction(),
                                       auditCode.getUserAction());

                    return increment;
                }
                catch (Throwable error)
                {
                    logBadConfigProperties(accessServiceName,
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
     * @param accessServiceName name of this access service
     * @param auditLog audit log to use
     * @param methodName calling method
     * @return default value
     */
    private int useDefaultKarmaPointIncrement(String       accessServiceName,
                                              OMRSAuditLog auditLog,
                                              String       methodName)
    {
        OMAGAuditCode auditCode = OMAGAuditCode.NO_KARMA_POINT_COLLECTION;
        auditLog.logRecord(methodName,
                           auditCode.getLogMessageId(),
                           auditCode.getSeverity(),
                           auditCode.getFormattedLogMessage(accessServiceName),
                           null,
                           auditCode.getSystemAction(),
                           auditCode.getUserAction());

        return defaultKarmaPointInterval;
    }


    /**
     * Extract the karma point plateau property from the access services option.
     *
     * @param accessServiceOptions options passed to the access service.
     * @param accessServiceName name of calling service
     * @param auditLog audit log for error messages
     * @return null or list of zone names
     * @throws OMAGConfigurationErrorException the supported zones property is not a list of zone names.
     */
    protected int extractKarmaPointPlateau(Map<String, Object> accessServiceOptions,
                                           String              accessServiceName,
                                           OMRSAuditLog        auditLog) throws OMAGConfigurationErrorException
    {
        final String  methodName = "extractKarmaPointPlateau";
        OMAGAuditCode auditCode;

        if (accessServiceOptions == null)
        {
            return this.useDefaultPlateauThreshold(accessServiceName, auditLog, methodName);
        }
        else
        {
            Object   plateauThresholdObject = accessServiceOptions.get(karmaPointPlateauPropertyName);

            if (plateauThresholdObject == null)
            {
                return this.useDefaultPlateauThreshold(accessServiceName, auditLog, methodName);
            }
            else
            {
                try
                {
                    int plateauThreshold =  Integer.parseInt(plateauThresholdObject.toString());

                    auditCode = OMAGAuditCode.PLATEAU_THRESHOLD;
                    auditLog.logRecord(methodName,
                                       auditCode.getLogMessageId(),
                                       auditCode.getSeverity(),
                                       auditCode.getFormattedLogMessage(accessServiceName, Integer.toString(plateauThreshold)),
                                       null,
                                       auditCode.getSystemAction(),
                                       auditCode.getUserAction());

                    return plateauThreshold;
                }
                catch (Throwable error)
                {
                    logBadConfigProperties(accessServiceName,
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
     * @param accessServiceName name of this access service
     * @param auditLog audit log to use
     * @param methodName calling method
     * @return default value
     */
    private int useDefaultPlateauThreshold(String       accessServiceName,
                                           OMRSAuditLog auditLog,
                                           String       methodName)
    {
        OMAGAuditCode auditCode = OMAGAuditCode.DEFAULT_PLATEAU_THRESHOLD;
        auditLog.logRecord(methodName,
                           auditCode.getLogMessageId(),
                           auditCode.getSeverity(),
                           auditCode.getFormattedLogMessage(accessServiceName, Integer.toString(defaultKarmaPointThreshold)),
                           null,
                           auditCode.getSystemAction(),
                           auditCode.getUserAction());

        return defaultKarmaPointThreshold;
    }




    /**
     * Log that a property value is incorrect.
     *
     * @param accessServiceName name of the calling access service
     * @param propertyName name of the property in error
     * @param propertyValue value of the property that is in error
     * @param auditLog log to write message to
     * @param methodName calling method
     * @param error resulting exception
     *
     * @throws OMAGConfigurationErrorException exception documenting the error
     */
    private void logBadConfigProperties(String       accessServiceName,
                                        String       propertyName,
                                        String       propertyValue,
                                        OMRSAuditLog auditLog,
                                        String       methodName,
                                        Throwable    error) throws OMAGConfigurationErrorException
    {
        OMAGAuditCode auditCode = OMAGAuditCode.BAD_CONFIG_PROPERTY;
        auditLog.logRecord(methodName,
                           auditCode.getLogMessageId(),
                           auditCode.getSeverity(),
                           auditCode.getFormattedLogMessage(accessServiceName, propertyValue, propertyName),
                           null,
                           auditCode.getSystemAction(),
                           auditCode.getUserAction());

        OMAGAdminErrorCode errorCode = OMAGAdminErrorCode.BAD_CONFIG_PROPERTIES;
        String        errorMessage = errorCode.getErrorMessageId() + errorCode.getFormattedErrorMessage(accessServiceName,
                                                                                                        propertyValue,
                                                                                                        propertyName,
                                                                                                        error.getClass().getName(),
                                                                                                        error.getMessage());

        throw new OMAGConfigurationErrorException(errorCode.getHTTPErrorCode(),
                                                  this.getClass().getName(),
                                                  methodName,
                                                  errorMessage,
                                                  errorCode.getSystemAction(),
                                                  errorCode.getUserAction(),
                                                  error);
    }


    /**
     * Register a listener with the enterprise topic connector.
     *
     * @param accessServiceName name of calling access service
     * @param serverName name of OMAG Server instance
     * @param omrsTopicConnector topic connector to register with
     * @param omrsTopicListener listener to register
     * @param auditLog audit log to record messages
     *
     * @throws OMAGConfigurationErrorException problem with topic connection
     */
    protected void registerWithEnterpriseTopic(String              accessServiceName,
                                               String              serverName,
                                               OMRSTopicConnector  omrsTopicConnector,
                                               OMRSTopicListener   omrsTopicListener,
                                               OMRSAuditLog        auditLog) throws OMAGConfigurationErrorException
    {
        final String            actionDescription = "initialize OMAS";
        final String            methodName = "initialize";

        OMAGAuditCode auditCode;

        if (omrsTopicConnector != null)
        {
            auditCode = OMAGAuditCode.SERVICE_REGISTERED_WITH_ENTERPRISE_TOPIC;
            auditLog.logRecord(actionDescription,
                               auditCode.getLogMessageId(),
                               auditCode.getSeverity(),
                               auditCode.getFormattedLogMessage(accessServiceName, serverName),
                               null,
                               auditCode.getSystemAction(),
                               auditCode.getUserAction());

            omrsTopicConnector.registerListener(omrsTopicListener, accessServiceName);
        }
        else
        {
            auditCode = OMAGAuditCode.NO_ENTERPRISE_TOPIC;
            auditLog.logRecord(actionDescription,
                               auditCode.getLogMessageId(),
                               auditCode.getSeverity(),
                               auditCode.getFormattedLogMessage(accessServiceName, serverName),
                               null,
                               auditCode.getSystemAction(),
                               auditCode.getUserAction());

            OMAGAdminErrorCode errorCode = OMAGAdminErrorCode.NO_ENTERPRISE_TOPIC;
            String             errorMessage = errorCode.getErrorMessageId() + errorCode.getFormattedErrorMessage(accessServiceName,
                                                                                                                 serverName);

            throw new OMAGConfigurationErrorException(errorCode.getHTTPErrorCode(),
                                                      this.getClass().getName(),
                                                      methodName,
                                                      errorMessage,
                                                      errorCode.getSystemAction(),
                                                      errorCode.getUserAction());
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
     * @param accessServiceName name of the calling access service
     * @param parentAuditLog audit log from the admin component
     * @return connector to access the topic
     * @throws OMAGConfigurationErrorException problem creating connector
     */
    protected OpenMetadataTopicConnector getInTopicConnector(Connection   inTopicConnection,
                                                             String       accessServiceName,
                                                             OMRSAuditLog parentAuditLog) throws OMAGConfigurationErrorException
    {
        final String  methodName = "getInTopicConnector";

        return this.getTopicConnector(inTopicConnection,
                                      parentAuditLog.createNewAuditLog(OMRSAuditingComponent.OMAS_IN_TOPIC),
                                      accessServiceName,
                                      methodName);
    }


    /**
     * Create the connector for this access services' Out Topic.
     *
     * @param outTopicConnection connection from the configuration properties
     * @param accessServiceName name of the calling access service
     * @param parentAuditLog audit log from the admin component
     * @return connector to access the topic
     * @throws OMAGConfigurationErrorException problem creating connector
     */
    protected OpenMetadataTopicConnector getOutTopicConnector(Connection   outTopicConnection,
                                                              String       accessServiceName,
                                                              OMRSAuditLog parentAuditLog) throws OMAGConfigurationErrorException
    {
        final String  methodName = "getOutTopicConnector";

        return this.getTopicConnector(outTopicConnection,
                                      parentAuditLog.createNewAuditLog(OMRSAuditingComponent.OMAS_OUT_TOPIC),
                                      accessServiceName,
                                      methodName);
    }


    /**
     * Create an instance of an open metadata topic connector using the supplied connection.
     *
     * @param topicConnection connection needed to create the connector
     * @param accessServiceName name of the calling access service
     * @param auditLog audit log for this connector
     * @return connector to access the topic
     * @throws OMAGConfigurationErrorException problem creating connector
     */
    private OpenMetadataTopicConnector getTopicConnector(Connection   topicConnection,
                                                         OMRSAuditLog auditLog,
                                                         String       accessServiceName,
                                                         String       methodName) throws OMAGConfigurationErrorException
    {
        try
        {
            ConnectorBroker connectorBroker = new ConnectorBroker();
            Connector       connector       = connectorBroker.getConnector(topicConnection);

            OpenMetadataTopicConnector topicConnector = (OpenMetadataTopicConnector)connector;

            topicConnector.setAuditLog(auditLog);

            return topicConnector;
        }
        catch (Throwable   error)
        {
            OMAGAuditCode auditCode = OMAGAuditCode.BAD_TOPIC_CONNECTOR;
            auditLog.logRecord(methodName,
                               auditCode.getLogMessageId(),
                               auditCode.getSeverity(),
                               auditCode.getFormattedLogMessage(methodName,
                                                                accessServiceName,
                                                                error.getClass().getName(),
                                                                error.getMessage()),
                               null,
                               auditCode.getSystemAction(),
                               auditCode.getUserAction());

            OMAGAdminErrorCode errorCode = OMAGAdminErrorCode.BAD_TOPIC_CONNECTOR;
            String errorMessage = errorCode.getErrorMessageId()
                                + errorCode.getFormattedErrorMessage(methodName,
                                                                     accessServiceName,
                                                                     error.getClass().getName(),
                                                                     error.getMessage());
            throw new OMAGConfigurationErrorException(errorCode.getHTTPErrorCode(),
                                                      this.getClass().getName(),
                                                      methodName,
                                                      errorMessage,
                                                      errorCode.getSystemAction(),
                                                      errorCode.getUserAction());
        }

    }

}
