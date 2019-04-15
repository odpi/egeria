/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.discoveryengine.admin;

import org.odpi.openmetadata.accessservices.discoveryengine.auditlog.DiscoveryEngineAuditCode;
import org.odpi.openmetadata.accessservices.discoveryengine.ffdc.DiscoveryEngineErrorCode;
import org.odpi.openmetadata.accessservices.discoveryengine.server.DiscoveryEngineServicesInstance;
import org.odpi.openmetadata.adminservices.configuration.properties.AccessServiceConfig;
import org.odpi.openmetadata.adminservices.configuration.registration.AccessServiceAdmin;
import org.odpi.openmetadata.adminservices.ffdc.exception.OMAGConfigurationErrorException;
import org.odpi.openmetadata.repositoryservices.auditlog.OMRSAuditLog;
import org.odpi.openmetadata.repositoryservices.connectors.omrstopic.OMRSTopicConnector;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryConnector;

import java.util.List;
import java.util.Map;

/**
 * DiscoveryEngineAdmin manages the start up and shutdown of the Discovery Engine OMAS.   During start up,
 * it validates the parameters and options it receives and sets up the service as requested.
 */
public class DiscoveryEngineAdmin implements AccessServiceAdmin
{
    private OMRSAuditLog                    auditLog   = null;
    private DiscoveryEngineServicesInstance instance   = null;
    private String                          serverName = null;

    /**
     * Default constructor
     */
    public DiscoveryEngineAdmin()
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
        final String             actionDescription = "initialize";
        DiscoveryEngineAuditCode auditCode;

        auditCode = DiscoveryEngineAuditCode.SERVICE_INITIALIZING;
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

            this.instance = new DiscoveryEngineServicesInstance(repositoryConnector,
                                                                supportedZones,
                                                                auditLog);
            this.serverName = instance.getServerName();


            auditCode = DiscoveryEngineAuditCode.SERVICE_INITIALIZED;
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
            auditCode = DiscoveryEngineAuditCode.SERVICE_INSTANCE_FAILURE;
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
        DiscoveryEngineAuditCode  auditCode;

        if (instance != null)
        {
            this.instance.shutdown();
        }

        auditCode = DiscoveryEngineAuditCode.SERVICE_SHUTDOWN;
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
        final String             methodName = "extractSupportedZones";
        DiscoveryEngineAuditCode auditCode;

        if (accessServiceOptions == null)
        {
            return null;
        }
        else
        {
            Object   zoneListObject = accessServiceOptions.get(supportedZonesPropertyName);

            if (zoneListObject == null)
            {
                auditCode = DiscoveryEngineAuditCode.ALL_ZONES;
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

                    auditCode = DiscoveryEngineAuditCode.SUPPORTED_ZONES;
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
                    auditCode = DiscoveryEngineAuditCode.BAD_CONFIG;
                    auditLog.logRecord(methodName,
                                       auditCode.getLogMessageId(),
                                       auditCode.getSeverity(),
                                       auditCode.getFormattedLogMessage(zoneListObject.toString(), supportedZonesPropertyName),
                                       null,
                                       auditCode.getSystemAction(),
                                       auditCode.getUserAction());

                    DiscoveryEngineErrorCode errorCode = DiscoveryEngineErrorCode.BAD_CONFIG;
                    String                   errorMessage = errorCode.getErrorMessageId() + errorCode.getFormattedErrorMessage(zoneListObject.toString(),
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
