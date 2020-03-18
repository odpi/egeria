/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.governanceengine.server.admin;

import org.odpi.openmetadata.accessservices.governanceengine.api.ffdc.errorcode.GovernanceEngineAuditCode;
import org.odpi.openmetadata.accessservices.governanceengine.server.listeners.GovernanceEngineOMRSTopicListener;
import org.odpi.openmetadata.adminservices.configuration.properties.AccessServiceConfig;
import org.odpi.openmetadata.adminservices.configuration.registration.AccessServiceAdmin;
import org.odpi.openmetadata.adminservices.configuration.registration.AccessServiceDescription;
import org.odpi.openmetadata.adminservices.ffdc.exception.OMAGConfigurationErrorException;
import org.odpi.openmetadata.repositoryservices.auditlog.OMRSAuditLog;
import org.odpi.openmetadata.repositoryservices.connectors.omrstopic.OMRSTopicConnector;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryConnector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class GovernanceEngineAdmin extends AccessServiceAdmin {

    private OMRSAuditLog auditLog;
    private GovernanceEngineServicesInstance instance;
    private String serverName;

    /**
     * Initialize the access service.
     *
     * @param accessServiceConfig               - specific configuration properties for this access service.
     * @param enterpriseOMRSTopicConnector      - connector for receiving OMRS Events from the cohorts
     * @param enterpriseOMRSRepositoryConnector - connector for querying the cohort repositories
     * @param auditLog                          - audit log component for logging messages.
     * @param serverUserName                    - user id to use on OMRS calls where there is no end user.
     */
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
    public void initialize(AccessServiceConfig accessServiceConfig,
                           OMRSTopicConnector omrsTopicConnector,
                           OMRSRepositoryConnector repositoryConnector,
                           OMRSAuditLog auditLog,
                           String serverUserName) throws OMAGConfigurationErrorException {
        final String actionDescription = "initialize";

        GovernanceEngineAuditCode auditCode = GovernanceEngineAuditCode.SERVICE_INITIALIZING;
        auditLog.logRecord(actionDescription,
                auditCode.getLogMessageId(),
                auditCode.getSeverity(),
                auditCode.getFormattedLogMessage(),
                null,
                auditCode.getSystemAction(),
                auditCode.getUserAction());

        this.auditLog = auditLog;

        try {
            List<String> supportedZones = this.extractSupportedZones(accessServiceConfig.getAccessServiceOptions(),
                    accessServiceConfig.getAccessServiceName(),
                    auditLog);

            this.instance = new GovernanceEngineServicesInstance(repositoryConnector,
                    supportedZones,
                    auditLog,
                    serverUserName, repositoryConnector.getMaxPageSize());
            this.serverName = instance.getServerName();

            /*
             * Only set up the listening and event publishing if requested in the config.
             */
            if (accessServiceConfig.getAccessServiceOutTopic() != null) {
                GovernanceEngineOMRSTopicListener omrsTopicListener =
                        new GovernanceEngineOMRSTopicListener(accessServiceConfig.getAccessServiceOutTopic(),
                                repositoryConnector.getRepositoryHelper(),
                                repositoryConnector.getRepositoryValidator(),
                                accessServiceConfig.getAccessServiceName(),
                                serverName,
                                supportedZones,
                                auditLog);
                super.registerWithEnterpriseTopic(accessServiceConfig.getAccessServiceName(),
                        serverName,
                        omrsTopicConnector,
                        omrsTopicListener,
                        auditLog);
            }

            auditCode = GovernanceEngineAuditCode.SERVICE_INITIALIZED;
            auditLog.logRecord(actionDescription,
                    auditCode.getLogMessageId(),
                    auditCode.getSeverity(),
                    auditCode.getFormattedLogMessage(serverName),
                    accessServiceConfig.toString(),
                    auditCode.getSystemAction(),
                    auditCode.getUserAction());
        } catch (OMAGConfigurationErrorException error) {
            throw error;
        } catch (Exception error) {
            auditCode = GovernanceEngineAuditCode.SERVICE_INSTANCE_FAILURE;
            auditLog.logException(actionDescription,
                    auditCode.getLogMessageId(),
                    auditCode.getSeverity(),
                    auditCode.getFormattedLogMessage(error.getClass().getName(), error.getMessage()),
                    accessServiceConfig.toString(),
                    auditCode.getSystemAction(),
                    auditCode.getUserAction(),
                    error);

            super.throwUnexpectedInitializationException(actionDescription,
                    AccessServiceDescription.ASSET_CONSUMER_OMAS.getAccessServiceFullName(),
                    error);
        }
    }


    /**
     * Shutdown the access service.
     */
    public void shutdown() {
        final String actionDescription = "shutdown";
        GovernanceEngineAuditCode auditCode;

        if (instance != null) {
            this.instance.shutdown();
        }

        auditCode = GovernanceEngineAuditCode.SERVICE_SHUTDOWN;
        auditLog.logRecord(actionDescription,
                auditCode.getLogMessageId(),
                auditCode.getSeverity(),
                auditCode.getFormattedLogMessage(serverName),
                null,
                auditCode.getSystemAction(),
                auditCode.getUserAction());
    }
}