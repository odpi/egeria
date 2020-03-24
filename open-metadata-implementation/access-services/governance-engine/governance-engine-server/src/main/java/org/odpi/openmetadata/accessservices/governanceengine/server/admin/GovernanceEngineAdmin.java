/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.governanceengine.server.admin;

import org.odpi.openmetadata.accessservices.governanceengine.api.ffdc.errorcode.GovernanceEngineAuditCode;
import org.odpi.openmetadata.accessservices.governanceengine.server.listeners.GovernanceEngineOMRSTopicListener;
import org.odpi.openmetadata.adminservices.configuration.properties.AccessServiceConfig;
import org.odpi.openmetadata.adminservices.configuration.registration.AccessServiceAdmin;
import org.odpi.openmetadata.adminservices.configuration.registration.AccessServiceDescription;
import org.odpi.openmetadata.adminservices.ffdc.exception.OMAGConfigurationErrorException;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.repositoryservices.connectors.omrstopic.OMRSTopicConnector;
import org.odpi.openmetadata.repositoryservices.connectors.openmetadatatopic.OpenMetadataTopicConnector;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryConnector;

import java.util.List;

public class GovernanceEngineAdmin extends AccessServiceAdmin {

    private AuditLog auditLog;
    private GovernanceEngineServicesInstance instance;
    private String serverName;

    /**
     * Initialize the access service.
     *
     * @param accessServiceConfigurationProperties - specific configuration properties for this access service.
     * @param enterpriseOMRSTopicConnector         - connector for receiving OMRS Events from the cohorts
     * @param enterpriseOMRSRepositoryConnector    - connector for querying the cohort repositories
     * @param auditLog                             - audit log component for logging messages.
     * @param serverUserName                       - user id to use on OMRS calls where there is no end user.
     */
    public void initialize(AccessServiceConfig accessServiceConfigurationProperties,
                           OMRSTopicConnector enterpriseOMRSTopicConnector,
                           OMRSRepositoryConnector enterpriseOMRSRepositoryConnector,
                           AuditLog auditLog,
                           String serverUserName) throws OMAGConfigurationErrorException {
        final String actionDescription = "initialize";

        auditLog.logMessage(actionDescription, GovernanceEngineAuditCode.SERVICE_INITIALIZING.getMessageDefinition());

        this.auditLog = auditLog;

        try {
            List<String> supportedZones = this.extractSupportedZones(
                    accessServiceConfigurationProperties.getAccessServiceOptions(),
                    accessServiceConfigurationProperties.getAccessServiceName(),
                    auditLog);

            this.instance = new GovernanceEngineServicesInstance(enterpriseOMRSRepositoryConnector,
                    supportedZones,
                    auditLog,
                    serverUserName,
                    enterpriseOMRSRepositoryConnector.getMaxPageSize());
            this.serverName = instance.getServerName();

            /*
             * Only set up the listening and event publishing if requested in the config.
             */
            OpenMetadataTopicConnector outTopicConnector = null;

            if (accessServiceConfigurationProperties.getAccessServiceOutTopic() != null) {
                outTopicConnector = super.getOutTopicEventBusConnector(accessServiceConfigurationProperties.getAccessServiceOutTopic(),
                        AccessServiceDescription.GOVERNANCE_ENGINE_OMAS.getAccessServiceFullName(),
                        auditLog);
            }


            if (accessServiceConfigurationProperties.getAccessServiceOutTopic() != null) {
                GovernanceEngineOMRSTopicListener omrsTopicListener =
                        new GovernanceEngineOMRSTopicListener(outTopicConnector,
                                enterpriseOMRSRepositoryConnector.getRepositoryHelper(),
                                enterpriseOMRSRepositoryConnector.getRepositoryValidator(),
                                accessServiceConfigurationProperties.getAccessServiceName(),
                                serverName,
                                supportedZones,
                                auditLog);
                super.registerWithEnterpriseTopic(accessServiceConfigurationProperties.getAccessServiceName(),
                        serverName,
                        enterpriseOMRSTopicConnector,
                        omrsTopicListener,
                        auditLog);
            }

            auditLog.logMessage(actionDescription, GovernanceEngineAuditCode.SERVICE_INITIALIZED.getMessageDefinition());
        } catch (OMAGConfigurationErrorException error) {
            throw error;
        } catch (Throwable error) {
            auditLog.logException(actionDescription,
                    GovernanceEngineAuditCode.SERVICE_INSTANCE_FAILURE.getMessageDefinition(error.getMessage()),
                    error);

            super.throwUnexpectedInitializationException(actionDescription,
                    AccessServiceDescription.GOVERNANCE_ENGINE_OMAS.getAccessServiceFullName(),
                    error);
        }
    }

    /**
     * Shutdown the access service.
     */
    public synchronized void shutdown() {
        final String actionDescription = "shutdown";
        auditLog.logMessage(actionDescription, GovernanceEngineAuditCode.SERVICE_TERMINATING.getMessageDefinition());

        if (instance != null) {
            this.instance.shutdown();
        }

        auditLog.logMessage(actionDescription, GovernanceEngineAuditCode.SERVICE_SHUTDOWN.getMessageDefinition());
    }
}