/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.assetlineage.admin;


import org.odpi.openmetadata.accessservices.assetlineage.auditlog.AssetLineageAuditCode;
import org.odpi.openmetadata.accessservices.assetlineage.listeners.AssetLineageOMRSTopicListener;
import org.odpi.openmetadata.accessservices.assetlineage.server.AssetLineageServicesInstance;
import org.odpi.openmetadata.adminservices.configuration.properties.AccessServiceConfig;
import org.odpi.openmetadata.adminservices.configuration.registration.AccessServiceAdmin;
import org.odpi.openmetadata.adminservices.ffdc.exception.OMAGConfigurationErrorException;
import org.odpi.openmetadata.repositoryservices.auditlog.OMRSAuditLog;
import org.odpi.openmetadata.repositoryservices.connectors.omrstopic.OMRSTopicConnector;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryConnector;

import java.util.List;

/**
 * AssetLineageAdmin is the class that is called by the OMAG Server to initialize and terminate
 * the Asset Lineage OMAS. The initialization call provides this OMAS with resources from the
 * Open Metadata Repository Services.
 */
public class AssetLineageAdmin extends AccessServiceAdmin
{

    private OMRSAuditLog auditLog;
    private AssetLineageServicesInstance instance;
    private String serverName;
    private String serverUserName;


    /**
     * Default constructor
     */
    public AssetLineageAdmin() {
    }

    /**
     * Initialize the access service.
     *
     * @param accessServiceConfig specific configuration properties for this access service.
     * @param omrsTopicConnector  connector for receiving OMRS Events from the cohorts
     * @param repositoryConnector connector for querying the cohort repositories
     * @param auditLog            audit log component for logging messages.
     * @param serverUserName      user id to use on OMRS calls where there is no end user.
     * @throws OMAGConfigurationErrorException invalid parameters in the configuration properties.
     */
    @Override
    public void initialize(AccessServiceConfig accessServiceConfig,
                           OMRSTopicConnector omrsTopicConnector,
                           OMRSRepositoryConnector repositoryConnector,
                           OMRSAuditLog auditLog,
                           String serverUserName) throws OMAGConfigurationErrorException {
        final String actionDescription = "initialize";
        AssetLineageAuditCode auditCode;
        this.serverUserName = serverUserName;

        auditCode = AssetLineageAuditCode.SERVICE_INITIALIZING;
        auditLog.logRecord(actionDescription,
                auditCode.getLogMessageId(),
                auditCode.getSeverity(),
                auditCode.getFormattedLogMessage(),
                null,
                auditCode.getSystemAction(),
                auditCode.getUserAction());

        try {
            this.auditLog = auditLog;

            List<String> supportedZones = this.extractSupportedZones(accessServiceConfig.getAccessServiceOptions(),
                    accessServiceConfig.getAccessServiceName(),
                    auditLog);

            this.instance = new AssetLineageServicesInstance(repositoryConnector,
                    supportedZones,
                    auditLog,
                    serverUserName);
            this.serverName = instance.getServerName();


            /*
             * Only set up the listening and event publishing if requested in the config.
             */
            if (accessServiceConfig.getAccessServiceOutTopic() != null) {
                AssetLineageOMRSTopicListener omrsTopicListener;

                omrsTopicListener = new AssetLineageOMRSTopicListener(
                        accessServiceConfig.getAccessServiceOutTopic(),
                        repositoryConnector.getRepositoryHelper(),
                        auditLog,
                        serverUserName,
                        serverName);
                super.registerWithEnterpriseTopic(accessServiceConfig.getAccessServiceName(),
                        serverName,
                        omrsTopicConnector,
                        omrsTopicListener,
                        auditLog);
            }

            auditCode = AssetLineageAuditCode.SERVICE_INITIALIZED;
            auditLog.logRecord(actionDescription,
                    auditCode.getLogMessageId(),
                    auditCode.getSeverity(),
                    auditCode.getFormattedLogMessage(serverName),
                    accessServiceConfig.toString(),
                    auditCode.getSystemAction(),
                    auditCode.getUserAction());
        } catch (OMAGConfigurationErrorException error) {
            throw error;
        } catch (Throwable error) {
            auditCode = AssetLineageAuditCode.SERVICE_INSTANCE_FAILURE;
            auditLog.logException(actionDescription,
                    auditCode.getLogMessageId(),
                    auditCode.getSeverity(),
                    auditCode.getFormattedLogMessage(error.getMessage()),
                    accessServiceConfig.toString(),
                    auditCode.getSystemAction(),
                    auditCode.getUserAction(),
                    error);
        }
    }

    /**
     * Shutdown the access service.
     */
    public void shutdown() {
        final String actionDescription = "shutdown";
        AssetLineageAuditCode auditCode;

        if (instance != null) {
            this.instance.shutdown();
        }

        auditCode = AssetLineageAuditCode.SERVICE_SHUTDOWN;
        auditLog.logRecord(actionDescription,
                auditCode.getLogMessageId(),
                auditCode.getSeverity(),
                auditCode.getFormattedLogMessage(serverName),
                null,
                auditCode.getSystemAction(),
                auditCode.getUserAction());
    }
}
