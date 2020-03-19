/*
 *  SPDX-License-Identifier: Apache-2.0
 *  Copyright Contributors to the ODPi Egeria project.
 */
package org.odpi.openmetadata.accessservices.securityofficer.server.admin.admin;

import org.odpi.openmetadata.accessservices.securityofficer.api.auditlog.SecurityOfficerAuditCode;
import org.odpi.openmetadata.accessservices.securityofficer.api.ffdc.errorcode.SecurityOfficerErrorCode;
import org.odpi.openmetadata.accessservices.securityofficer.server.admin.listener.SecurityOfficerOMRSTopicListener;
import org.odpi.openmetadata.accessservices.securityofficer.server.admin.processor.SecurityOfficerEventProcessor;
import org.odpi.openmetadata.accessservices.securityofficer.server.admin.publisher.SecurityOfficerPublisher;
import org.odpi.openmetadata.accessservices.securityofficer.server.admin.services.SecurityOfficerServicesInstance;
import org.odpi.openmetadata.adminservices.configuration.properties.AccessServiceConfig;
import org.odpi.openmetadata.adminservices.configuration.registration.AccessServiceAdmin;
import org.odpi.openmetadata.frameworks.connectors.Connector;
import org.odpi.openmetadata.frameworks.connectors.ConnectorBroker;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.Connection;
import org.odpi.openmetadata.repositoryservices.auditlog.OMRSAuditLog;
import org.odpi.openmetadata.repositoryservices.connectors.omrstopic.OMRSTopicConnector;
import org.odpi.openmetadata.repositoryservices.connectors.openmetadatatopic.OpenMetadataTopicConnector;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryConnector;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.OMRSConfigErrorException;

public class SecurityOfficerAdmin extends AccessServiceAdmin
{

    private OMRSAuditLog auditLog;
    private String serverName;
    private SecurityOfficerServicesInstance instance;

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

        SecurityOfficerAuditCode auditCode = SecurityOfficerAuditCode.SERVICE_INITIALIZING;
        auditLog.logRecord(actionDescription,
                auditCode.getLogMessageId(),
                auditCode.getSeverity(),
                auditCode.getFormattedLogMessage(),
                null,
                auditCode.getSystemAction(),
                auditCode.getUserAction());

        try {
            this.auditLog = auditLog;
            OpenMetadataTopicConnector securityOfficerOutputTopic = initializeSecurityOfficerTopicConnector(accessServiceConfigurationProperties.getAccessServiceOutTopic());
            SecurityOfficerEventProcessor securityOfficerEventProcessor = new SecurityOfficerEventProcessor(enterpriseOMRSRepositoryConnector);

            SecurityOfficerPublisher securityOfficerPublisher = new SecurityOfficerPublisher(securityOfficerEventProcessor, securityOfficerOutputTopic, auditLog);
            this.instance = new SecurityOfficerServicesInstance(enterpriseOMRSRepositoryConnector, securityOfficerPublisher);
            this.serverName = instance.getServerName();

            if (enterpriseOMRSTopicConnector != null) {
                auditCode = SecurityOfficerAuditCode.SERVICE_REGISTERED_WITH_TOPIC;
                auditLog.logRecord(actionDescription,
                        auditCode.getLogMessageId(),
                        auditCode.getSeverity(),
                        auditCode.getFormattedLogMessage(serverName),
                        null,
                        auditCode.getSystemAction(),
                        auditCode.getUserAction());

                SecurityOfficerOMRSTopicListener omrsTopicListener = new SecurityOfficerOMRSTopicListener(accessServiceConfigurationProperties.getAccessServiceName(),
                        securityOfficerPublisher);
                enterpriseOMRSTopicConnector.registerListener(omrsTopicListener, accessServiceConfigurationProperties.getAccessServiceName());
            }

            auditCode = SecurityOfficerAuditCode.SERVICE_INITIALIZED;
            auditLog.logRecord(actionDescription,
                    auditCode.getLogMessageId(),
                    auditCode.getSeverity(),
                    auditCode.getFormattedLogMessage(serverName),
                    accessServiceConfigurationProperties.toString(),
                    auditCode.getSystemAction(),
                    auditCode.getUserAction());
        } catch (Exception error) {
            auditCode = SecurityOfficerAuditCode.SERVICE_INSTANCE_FAILURE;
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
    private OpenMetadataTopicConnector initializeSecurityOfficerTopicConnector(Connection topicConnection) {
        final String actionDescription = "initialize";
        if (topicConnection != null) {
            try {
                return getTopicConnector(topicConnection);
            } catch (Exception e) {
                SecurityOfficerAuditCode auditCode = SecurityOfficerAuditCode.ERROR_INITIALIZING_TOPIC_CONNECTION;
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
            final String methodName = "getTopicConnector";
            SecurityOfficerErrorCode errorCode =
                    SecurityOfficerErrorCode.NULL_TOPIC_CONNECTOR;
            String errorMessage = errorCode.getErrorMessageId()
                    + errorCode.getFormattedErrorMessage("getTopicConnector");

            throw new OMRSConfigErrorException(errorCode.getHttpErrorCode(),
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
        SecurityOfficerAuditCode auditCode;

        auditCode = SecurityOfficerAuditCode.SERVICE_TERMINATING;
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

        auditCode = SecurityOfficerAuditCode.SERVICE_SHUTDOWN;
        auditLog.logRecord(actionDescription,
                auditCode.getLogMessageId(),
                auditCode.getSeverity(),
                auditCode.getFormattedLogMessage(serverName),
                null,
                auditCode.getSystemAction(),
                auditCode.getUserAction());
    }
}
