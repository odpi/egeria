/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.dataengine.server.admin;

import org.odpi.openmetadata.accessservices.dataengine.server.auditlog.DataEngineAuditCode;
import org.odpi.openmetadata.adminservices.configuration.properties.AccessServiceConfig;
import org.odpi.openmetadata.adminservices.configuration.registration.AccessServiceAdmin;
import org.odpi.openmetadata.repositoryservices.auditlog.OMRSAuditLog;
import org.odpi.openmetadata.repositoryservices.connectors.omrstopic.OMRSTopicConnector;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryConnector;

import java.util.List;

/**
 * DataEngineAdmin is the class that is called by the OMAG Server to initialize and terminate
 * the Data Engine OMAS.  The initialization call provides this OMAS with resources from the
 * Open Metadata Repository Services.
 */
public class DataEngineAdmin extends AccessServiceAdmin {

    private OMRSAuditLog auditLog;
    private DataEngineServicesInstance instance;
    private String serverName;

    /**
     * Initialize the access service.
     *
     * @param accessServiceConfig          - specific configuration properties for this access service.
     * @param enterpriseOMRSTopicConnector - connector for receiving OMRS Events from the cohorts
     * @param repositoryConnector          - connector for querying the cohort repositories
     * @param auditLog                     - audit log component for logging messages.
     * @param serverUserName               - user id to use on OMRS calls where there is no end user.
     */
    @Override
    public void initialize(AccessServiceConfig accessServiceConfig, OMRSTopicConnector enterpriseOMRSTopicConnector,
                           OMRSRepositoryConnector repositoryConnector, OMRSAuditLog auditLog, String serverUserName) {
        final String actionDescription = "initialize";

        DataEngineAuditCode auditCode;

        auditCode = DataEngineAuditCode.SERVICE_INITIALIZING;
        auditLog.logRecord(actionDescription, auditCode.getLogMessageId(), auditCode.getSeverity(),
                auditCode.getFormattedLogMessage(), null, auditCode.getSystemAction(),
                auditCode.getUserAction());
        try {

            this.auditLog = auditLog;

            List<String> supportedZones = this.extractSupportedZones(accessServiceConfig.getAccessServiceOptions(),
                    accessServiceConfig.getAccessServiceName(),
                    auditLog);
            List<String> defaultZones = this.extractDefaultZones(accessServiceConfig.getAccessServiceOptions(),
                    accessServiceConfig.getAccessServiceName(),
                    auditLog);

            instance = new DataEngineServicesInstance(repositoryConnector, supportedZones, defaultZones, auditLog,
                    serverUserName, repositoryConnector.getMaxPageSize());
            serverName = instance.getServerName();

            auditCode = DataEngineAuditCode.SERVICE_INITIALIZED;
            auditLog.logRecord(actionDescription, auditCode.getLogMessageId(), auditCode.getSeverity(),
                    auditCode.getFormattedLogMessage(serverName), null, auditCode.getSystemAction(),
                    auditCode.getUserAction());

        } catch (Exception error) {
            auditCode = DataEngineAuditCode.SERVICE_INSTANCE_FAILURE;
            auditLog.logRecord(actionDescription, auditCode.getLogMessageId(), auditCode.getSeverity(),
                    auditCode.getFormattedLogMessage(error.getMessage()), null,
                    auditCode.getSystemAction(), auditCode.getUserAction());
        }
    }

    /**
     * Shutdown the access service.
     */
    @Override
    public void shutdown() {

        if (instance != null) {
            instance.shutdown();
        }

        if (auditLog != null) {
            final String actionDescription = "shutdown";
            DataEngineAuditCode auditCode;

            auditCode = DataEngineAuditCode.SERVICE_SHUTDOWN;
            auditLog.logRecord(actionDescription, auditCode.getLogMessageId(), auditCode.getSeverity(),
                    auditCode.getFormattedLogMessage(serverName), null, auditCode.getSystemAction(),
                    auditCode.getUserAction());
        }
    }
}
