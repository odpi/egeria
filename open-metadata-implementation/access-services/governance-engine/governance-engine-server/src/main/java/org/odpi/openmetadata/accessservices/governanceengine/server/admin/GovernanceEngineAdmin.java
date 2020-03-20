/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.governanceengine.server.admin;

import org.odpi.openmetadata.accessservices.governanceengine.api.auditlog.GovernanceEngineAuditCode;
import org.odpi.openmetadata.accessservices.governanceengine.api.ffdc.errorcode.GovernanceEngineErrorCode;
import org.odpi.openmetadata.accessservices.governanceengine.server.GovernanceEngineServicesInstance;
import org.odpi.openmetadata.accessservices.governanceengine.server.listeners.GovernanceEngineOMRSTopicListener;
import org.odpi.openmetadata.accessservices.governanceengine.server.processor.GovernanceEngineEventProcessor;
import org.odpi.openmetadata.accessservices.governanceengine.server.publisher.GovernanceEnginePublisher;
import org.odpi.openmetadata.adminservices.configuration.properties.AccessServiceConfig;
import org.odpi.openmetadata.adminservices.configuration.registration.AccessServiceAdmin;
import org.odpi.openmetadata.frameworks.connectors.Connector;
import org.odpi.openmetadata.frameworks.connectors.ConnectorBroker;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.Connection;
import org.odpi.openmetadata.repositoryservices.auditlog.OMRSAuditLog;
import org.odpi.openmetadata.repositoryservices.connectors.omrstopic.OMRSTopicConnector;
import org.odpi.openmetadata.repositoryservices.connectors.openmetadatatopic.OpenMetadataTopicConnector;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryConnector;
import org.odpi.openmetadata.repositoryservices.ffdc.OMRSErrorCode;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.OMRSConfigErrorException;

public class GovernanceEngineAdmin extends AccessServiceAdmin
{

    private OMRSAuditLog auditLog;
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
    public synchronized void initialize(AccessServiceConfig accessServiceConfigurationProperties,
                                        OMRSTopicConnector enterpriseOMRSTopicConnector,
                                        OMRSRepositoryConnector enterpriseOMRSRepositoryConnector,
                                        OMRSAuditLog auditLog,
                                        String serverUserName) {
        final String actionDescription = "initialize";

        GovernanceEngineAuditCode auditCode = GovernanceEngineAuditCode.SERVICE_INITIALIZING;
        auditLog.logRecord(actionDescription,
                auditCode.getLogMessageId(),
                auditCode.getSeverity(),
                auditCode.getFormattedLogMessage(),
                null,
                auditCode.getSystemAction(),
                auditCode.getUserAction());

        try {
            this.auditLog = auditLog;
            this.instance = new GovernanceEngineServicesInstance(enterpriseOMRSRepositoryConnector);
            this.serverName = instance.getServerName();

            OpenMetadataTopicConnector governanceEngineOutputTopic = initializeGovernanceEngineTopicConnector(accessServiceConfigurationProperties.getAccessServiceOutTopic());
            GovernanceEngineEventProcessor governanceEngineEventProcessor = new GovernanceEngineEventProcessor(enterpriseOMRSRepositoryConnector, governanceEngineOutputTopic);

            GovernanceEnginePublisher governanceEnginePublisher = new GovernanceEnginePublisher(governanceEngineEventProcessor);

            if (enterpriseOMRSTopicConnector != null) {
                auditCode = GovernanceEngineAuditCode.SERVICE_REGISTERED_WITH_TOPIC;
                auditLog.logRecord(actionDescription,
                        auditCode.getLogMessageId(),
                        auditCode.getSeverity(),
                        auditCode.getFormattedLogMessage(serverName),
                        null,
                        auditCode.getSystemAction(),
                        auditCode.getUserAction());

                GovernanceEngineOMRSTopicListener omrsTopicListener = new GovernanceEngineOMRSTopicListener(governanceEnginePublisher);
                enterpriseOMRSTopicConnector.registerListener(omrsTopicListener, accessServiceConfigurationProperties.getAccessServiceName());
            }

            auditCode = GovernanceEngineAuditCode.SERVICE_INITIALIZED;
            auditLog.logRecord(actionDescription,
                    auditCode.getLogMessageId(),
                    auditCode.getSeverity(),
                    auditCode.getFormattedLogMessage(serverName),
                    accessServiceConfigurationProperties.toString(),
                    auditCode.getSystemAction(),
                    auditCode.getUserAction());
        } catch (Exception error) {
            auditCode = GovernanceEngineAuditCode.SERVICE_INSTANCE_FAILURE;
            auditLog.logException(actionDescription,
                    auditCode.getLogMessageId(),
                    auditCode.getSeverity(),
                    auditCode.getFormattedLogMessage(error.getMessage()),
                    accessServiceConfigurationProperties.toString(),
                    auditCode.getSystemAction(),
                    auditCode.getUserAction(),
                    error);
        }
    }

    /**
     * Returns the topic created based on connection properties
     *
     * @param topicConnection properties of the topic
     * @return the topic created based on the connection properties
     */
    private OpenMetadataTopicConnector initializeGovernanceEngineTopicConnector(Connection topicConnection) {
        final String actionDescription = "initialize";
        if (topicConnection != null) {
            try {
                return getTopicConnector(topicConnection);
            } catch (Exception e) {
                GovernanceEngineAuditCode auditCode = GovernanceEngineAuditCode.ERROR_INITIALIZING_TOPIC_CONNECTION;
                auditLog.logRecord(actionDescription,
                        auditCode.getLogMessageId(),
                        auditCode.getSeverity(),
                        auditCode.getFormattedLogMessage(topicConnection.toString(), serverName, e.getMessage()),
                        null,
                        auditCode.getSystemAction(),
                        auditCode.getUserAction());
                throw e;
            }

        }
        return null;
    }

    private OpenMetadataTopicConnector getTopicConnector(Connection topicConnection) {
        try {
            ConnectorBroker connectorBroker = new ConnectorBroker();
            Connector connector = connectorBroker.getConnector(topicConnection);
            OpenMetadataTopicConnector topicConnector = (OpenMetadataTopicConnector) connector;

            topicConnector.setAuditLog(auditLog);
            topicConnector.start();

            return topicConnector;
        } catch (Exception error) {
            String methodName = "getTopicConnector";
            GovernanceEngineErrorCode errorCode = GovernanceEngineErrorCode.NULL_TOPIC_CONNECTOR;
            String errorMessage = errorCode.getErrorMessageId()
                    + errorCode.getFormattedErrorMessage("getTopicConnector");

            throw new OMRSConfigErrorException(errorCode.getHTTPErrorCode(),
                    this.getClass().getName(),
                    methodName,
                    errorMessage,
                    errorCode.getSystemAction(),
                    errorCode.getUserAction(),
                    error);

        }
    }


    /**
     * Shutdown the access service.
     */
    public synchronized void shutdown() {
        final String actionDescription = "shutdown";
        GovernanceEngineAuditCode auditCode;

        auditCode = GovernanceEngineAuditCode.SERVICE_TERMINATING;
        auditLog.logRecord(actionDescription,
                auditCode.getLogMessageId(),
                auditCode.getSeverity(),
                auditCode.getFormattedLogMessage(serverName),
                null,
                auditCode.getSystemAction(),
                auditCode.getUserAction());

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


