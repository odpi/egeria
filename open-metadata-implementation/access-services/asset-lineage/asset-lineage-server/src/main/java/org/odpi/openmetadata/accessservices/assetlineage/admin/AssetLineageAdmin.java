/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.assetlineage.admin;

import org.odpi.openmetadata.accessservices.assetlineage.auditlog.AssetLineageAuditCode;
import org.odpi.openmetadata.accessservices.assetlineage.listener.AssetLineageOMRSTopicListener;
import org.odpi.openmetadata.accessservices.assetlineage.server.AssetLineageServicesInstance;
import org.odpi.openmetadata.adminservices.configuration.properties.AccessServiceConfig;
import org.odpi.openmetadata.adminservices.configuration.registration.AccessServiceAdmin;
import org.odpi.openmetadata.adminservices.ffdc.exception.OMAGConfigurationErrorException;
import org.odpi.openmetadata.repositoryservices.auditlog.OMRSAuditLog;
import org.odpi.openmetadata.repositoryservices.connectors.omrstopic.OMRSTopicConnector;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryConnector;

public class AssetLineageAdmin implements AccessServiceAdmin {
    private OMRSRepositoryConnector repositoryConnector;
    private OMRSTopicConnector omrsTopicConnector;
    private AccessServiceConfig accessServiceConfig;
    private OMRSAuditLog auditLog;
    private AssetLineageServicesInstance instance;
    private String serverName;
    private String serverUserName;
    private AssetLineageOMRSTopicListener omrsTopicListener;

    /**
     * Default constructor
     */
    public AssetLineageAdmin() {
    }


    /**
     * Initialize the access service.
     *
     * @param accessServiceConfigurationProperties specific configuration properties for this access service.
     * @param enterpriseOMRSTopicConnector         connector for receiving OMRS Events from the cohorts
     * @param enterpriseOMRSRepositoryConnector    connector for querying the cohort repositories
     * @param auditLog                             audit log component for logging messages.
     * @param serverUserName                       user id to use on OMRS calls where there is no end user.
     * @throws OMAGConfigurationErrorException invalid parameters in the configuration properties.
     */
    public void initialize(AccessServiceConfig accessServiceConfigurationProperties,
                           OMRSTopicConnector enterpriseOMRSTopicConnector,
                           OMRSRepositoryConnector enterpriseOMRSRepositoryConnector,
                           OMRSAuditLog auditLog,
                           String serverUserName) throws OMAGConfigurationErrorException {
        final String actionDescription = "initialize";
        AssetLineageAuditCode auditCode = AssetLineageAuditCode.SERVICE_INITIALIZING;
        auditLog.logRecord(actionDescription,
                auditCode.getLogMessageId(),
                auditCode.getSeverity(),
                auditCode.getFormattedLogMessage(),
                null,
                auditCode.getSystemAction(),
                auditCode.getUserAction());

        try {
            this.repositoryConnector = enterpriseOMRSRepositoryConnector;
            this.instance = new AssetLineageServicesInstance(repositoryConnector);
            this.serverName = instance.getServerName();

            this.accessServiceConfig = accessServiceConfigurationProperties;
            this.omrsTopicConnector = enterpriseOMRSTopicConnector;
            this.serverUserName = serverUserName;

            if (omrsTopicConnector != null) {
                auditCode = AssetLineageAuditCode.SERVICE_REGISTERED_WITH_ENTERPRISE_TOPIC;
                auditLog.logRecord(actionDescription,
                        auditCode.getLogMessageId(),
                        auditCode.getSeverity(),
                        auditCode.getFormattedLogMessage(serverName),
                        null,
                        auditCode.getSystemAction(),
                        auditCode.getUserAction());

                omrsTopicListener = new AssetLineageOMRSTopicListener(accessServiceConfig.getAccessServiceOutTopic(),
                        repositoryConnector,
                        repositoryConnector.getRepositoryHelper(),
                        repositoryConnector.getRepositoryValidator(),
                        accessServiceConfig.getAccessServiceName());

                omrsTopicConnector.registerListener(omrsTopicListener);
            }

            this.auditLog = auditLog;

            auditCode = AssetLineageAuditCode.SERVICE_INITIALIZED;
            auditLog.logRecord(actionDescription,
                    auditCode.getLogMessageId(),
                    auditCode.getSeverity(),
                    auditCode.getFormattedLogMessage(serverName),
                    null,
                    auditCode.getSystemAction(),
                    auditCode.getUserAction());
        } catch (Throwable error) {
            auditCode = AssetLineageAuditCode.SERVICE_INSTANCE_FAILURE;
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
