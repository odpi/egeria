/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.assetowner.admin;


import org.odpi.openmetadata.accessservices.assetowner.auditlog.AssetOwnerAuditCode;
import org.odpi.openmetadata.accessservices.assetowner.server.AssetOwnerServicesInstance;
import org.odpi.openmetadata.adminservices.configuration.properties.AccessServiceConfig;
import org.odpi.openmetadata.adminservices.configuration.registration.AccessServiceAdmin;
import org.odpi.openmetadata.adminservices.configuration.registration.AccessServiceDescription;
import org.odpi.openmetadata.adminservices.ffdc.exception.OMAGConfigurationErrorException;
import org.odpi.openmetadata.repositoryservices.auditlog.OMRSAuditLog;
import org.odpi.openmetadata.repositoryservices.connectors.omrstopic.OMRSTopicConnector;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryConnector;

import java.util.List;

/**
 * AssetOwnerAdmin manages the start up and shutdown of the Asset Owner OMAS.   During start up,
 * it validates the parameters and options it receives and sets up the service as requested.
 */
public class AssetOwnerAdmin extends AccessServiceAdmin
{
    private OMRSAuditLog               auditLog   = null;
    private AssetOwnerServicesInstance instance   = null;
    private String                     serverName = null;

    /**
     * Default constructor
     */
    public AssetOwnerAdmin()
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

        AssetOwnerAuditCode      auditCode = AssetOwnerAuditCode.SERVICE_INITIALIZING;
        auditLog.logRecord(actionDescription,
                           auditCode.getLogMessageId(),
                           auditCode.getSeverity(),
                           auditCode.getFormattedLogMessage(),
                           null,
                           auditCode.getSystemAction(),
                           auditCode.getUserAction());

        this.auditLog = auditLog;
        try
        {
            List<String>           supportedZones = this.extractSupportedZones(accessServiceConfig.getAccessServiceOptions(),
                                                                               accessServiceConfig.getAccessServiceName(),
                                                                               auditLog);

            List<String>           defaultZones = this.extractDefaultZones(accessServiceConfig.getAccessServiceOptions(),
                                                                           accessServiceConfig.getAccessServiceName(),
                                                                           auditLog);

            this.instance = new AssetOwnerServicesInstance(repositoryConnector,
                                                           supportedZones,
                                                           defaultZones,
                                                           auditLog,
                                                           serverUserName,
                                                           repositoryConnector.getMaxPageSize());
            this.serverName = instance.getServerName();

            auditCode = AssetOwnerAuditCode.SERVICE_INITIALIZED;
            auditLog.logRecord(actionDescription,
                               auditCode.getLogMessageId(),
                               auditCode.getSeverity(),
                               auditCode.getFormattedLogMessage(serverName),
                               accessServiceConfig.toString(),
                               auditCode.getSystemAction(),
                               auditCode.getUserAction());
        }
        catch (OMAGConfigurationErrorException error)
        {
            throw error;
        }
        catch (Throwable error)
        {
            auditCode = AssetOwnerAuditCode.SERVICE_INSTANCE_FAILURE;
            auditLog.logException(actionDescription,
                                  auditCode.getLogMessageId(),
                                  auditCode.getSeverity(),
                                  auditCode.getFormattedLogMessage(error.getMessage()),
                                  accessServiceConfig.toString(),
                                  auditCode.getSystemAction(),
                                  auditCode.getUserAction(),
                                  error);

            super.throwUnexpectedInitializationException(actionDescription,
                                                         AccessServiceDescription.ASSET_OWNER_OMAS.getAccessServiceFullName(),
                                                         error);
        }
    }


    /**
     * Shutdown the access service.
     */
    public void shutdown()
    {
        final String         actionDescription = "shutdown";
        AssetOwnerAuditCode  auditCode;

        if (instance != null)
        {
            this.instance.shutdown();
        }

        auditCode = AssetOwnerAuditCode.SERVICE_SHUTDOWN;
        auditLog.logRecord(actionDescription,
                           auditCode.getLogMessageId(),
                           auditCode.getSeverity(),
                           auditCode.getFormattedLogMessage(serverName),
                           null,
                           auditCode.getSystemAction(),
                           auditCode.getUserAction());
    }
}
