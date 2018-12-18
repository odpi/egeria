/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.assetconsumer.admin;

import org.odpi.openmetadata.accessservices.assetconsumer.auditlog.AssetConsumerAuditCode;
import org.odpi.openmetadata.accessservices.assetconsumer.ffdc.AssetConsumerErrorCode;
import org.odpi.openmetadata.accessservices.assetconsumer.listener.AssetConsumerOMRSTopicListener;
import org.odpi.openmetadata.accessservices.assetconsumer.server.AssetConsumerServicesInstance;
import org.odpi.openmetadata.adminservices.configuration.properties.AccessServiceConfig;
import org.odpi.openmetadata.adminservices.configuration.registration.AccessServiceAdmin;
import org.odpi.openmetadata.adminservices.ffdc.exception.OMAGConfigurationErrorException;
import org.odpi.openmetadata.repositoryservices.auditlog.OMRSAuditLog;
import org.odpi.openmetadata.repositoryservices.connectors.omrstopic.OMRSTopicConnector;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryConnector;

import java.util.List;
import java.util.Map;

/**
 * AssetConsumerAdmin manages the start up and shutdown of the Asset Consumer OMAS.   During start up,
 * it validates the parameters and options it receives and sets up the service as requested.
 */
public class AssetConsumerAdmin implements AccessServiceAdmin
{
    private OMRSAuditLog                   auditLog            = null;
    private AssetConsumerServicesInstance  instance            = null;
    private String                         serverName          = null;

    /**
     * Default constructor
     */
    public AssetConsumerAdmin()
    {
    }


    /**
     * Initialize the access service.
     *
     * @param accessServiceConfig  specific configuration properties for this access service.
     * @param omrsTopicConnector  connector for receiving OMRS Events from the cohorts
     * @param repositoryConnector  connector for querying the cohort repositories
     * @param auditLog  audit log component for logging messages.
     * @param serverUserName  user id to use on OMRS calls where there is no end user.
     * @throws OMAGConfigurationErrorException invalid parameters in the configuration properties.
     */
    public void initialize(AccessServiceConfig     accessServiceConfig,
                           OMRSTopicConnector      omrsTopicConnector,
                           OMRSRepositoryConnector repositoryConnector,
                           OMRSAuditLog            auditLog,
                           String                  serverUserName) throws OMAGConfigurationErrorException
    {
        final String           actionDescription = "initialize";
        AssetConsumerAuditCode auditCode;

        auditCode = AssetConsumerAuditCode.SERVICE_INITIALIZING;
        auditLog.logRecord(actionDescription,
                           auditCode.getLogMessageId(),
                           auditCode.getSeverity(),
                           auditCode.getFormattedLogMessage(),
                           null,
                           auditCode.getSystemAction(),
                           auditCode.getUserAction());


        try
        {
            this.auditLog = auditLog;

            List<String>           supportedZones = this.extractSupportedZones(accessServiceConfig.getAccessServiceOptions());

            this.instance = new AssetConsumerServicesInstance(repositoryConnector,
                                                              supportedZones,
                                                              auditLog);
            this.serverName = instance.getServerName();

            /*
             * Only set up the listening and event publishing if requested in the config.
             */
            if ((omrsTopicConnector != null) && (accessServiceConfig.getAccessServiceOutTopic() != null))
            {
                auditCode = AssetConsumerAuditCode.SERVICE_REGISTERED_WITH_ENTERPRISE_TOPIC;
                auditLog.logRecord(actionDescription,
                                   auditCode.getLogMessageId(),
                                   auditCode.getSeverity(),
                                   auditCode.getFormattedLogMessage(serverName),
                                   null,
                                   auditCode.getSystemAction(),
                                   auditCode.getUserAction());

                AssetConsumerOMRSTopicListener omrsTopicListener;

                omrsTopicListener = new AssetConsumerOMRSTopicListener(accessServiceConfig.getAccessServiceOutTopic(),
                                                                       repositoryConnector.getRepositoryHelper(),
                                                                       repositoryConnector.getRepositoryValidator(),
                                                                       accessServiceConfig.getAccessServiceName(),
                                                                       supportedZones,
                                                                       auditLog);

                omrsTopicConnector.registerListener(omrsTopicListener);
            }

            auditCode = AssetConsumerAuditCode.SERVICE_INITIALIZED;
            auditLog.logRecord(actionDescription,
                               auditCode.getLogMessageId(),
                               auditCode.getSeverity(),
                               auditCode.getFormattedLogMessage(serverName),
                               null,
                               auditCode.getSystemAction(),
                               auditCode.getUserAction());
        }
        catch (OMAGConfigurationErrorException error)
        {
            throw error;
        }
        catch (Throwable error)
        {
            auditCode = AssetConsumerAuditCode.SERVICE_INSTANCE_FAILURE;
            auditLog.logRecord(actionDescription,
                               auditCode.getLogMessageId(),
                               auditCode.getSeverity(),
                               auditCode.getFormattedLogMessage(error.getMessage()),
                               null,
                               auditCode.getSystemAction(),
                               auditCode.getUserAction());
        }
    }


    /**
     * Shutdown the access service.
     */
    public void shutdown()
    {
        final String            actionDescription = "shutdown";
        AssetConsumerAuditCode  auditCode;

        if (instance != null)
        {
            this.instance.shutdown();
        }

        auditCode = AssetConsumerAuditCode.SERVICE_SHUTDOWN;
        auditLog.logRecord(actionDescription,
                           auditCode.getLogMessageId(),
                           auditCode.getSeverity(),
                           auditCode.getFormattedLogMessage(serverName),
                           null,
                           auditCode.getSystemAction(),
                           auditCode.getUserAction());
    }


    /**
     * Extract the supported zones property from the access services option.
     *
     * @param accessServiceOptions options passed to the access service.
     * @return null or list of zone names
     * @throws OMAGConfigurationErrorException the supported zones property is not a list of zone names.
     */
    private List<String> extractSupportedZones(Map<String, Object> accessServiceOptions) throws OMAGConfigurationErrorException
    {
        final String           methodName = "extractSupportedZones";
        AssetConsumerAuditCode auditCode;

        if (accessServiceOptions == null)
        {
            return null;
        }
        else
        {
            Object   zoneListObject = accessServiceOptions.get(supportedZonesPropertyName);

            if (zoneListObject == null)
            {
                auditCode = AssetConsumerAuditCode.ALL_ZONES;
                auditLog.logRecord(methodName,
                                   auditCode.getLogMessageId(),
                                   auditCode.getSeverity(),
                                   auditCode.getFormattedLogMessage(),
                                   null,
                                   auditCode.getSystemAction(),
                                   auditCode.getUserAction());
                return null;
            }
            else
            {
                try
                {
                    List<String>  zoneList =  (List<String>)zoneListObject;

                    auditCode = AssetConsumerAuditCode.SUPPORTED_ZONES;
                    auditLog.logRecord(methodName,
                                       auditCode.getLogMessageId(),
                                       auditCode.getSeverity(),
                                       auditCode.getFormattedLogMessage(zoneList.toString()),
                                       null,
                                       auditCode.getSystemAction(),
                                       auditCode.getUserAction());

                    return zoneList;
                }
                catch (Throwable error)
                {
                    auditCode = AssetConsumerAuditCode.BAD_CONFIG;
                    auditLog.logRecord(methodName,
                                       auditCode.getLogMessageId(),
                                       auditCode.getSeverity(),
                                       auditCode.getFormattedLogMessage(zoneListObject.toString(), supportedZonesPropertyName),
                                       null,
                                       auditCode.getSystemAction(),
                                       auditCode.getUserAction());

                    AssetConsumerErrorCode errorCode    = AssetConsumerErrorCode.BAD_CONFIG;
                    String                 errorMessage = errorCode.getErrorMessageId() + errorCode.getFormattedErrorMessage(zoneListObject.toString(),
                                                                                                                             supportedZonesPropertyName,
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
            }
        }
    }
}
