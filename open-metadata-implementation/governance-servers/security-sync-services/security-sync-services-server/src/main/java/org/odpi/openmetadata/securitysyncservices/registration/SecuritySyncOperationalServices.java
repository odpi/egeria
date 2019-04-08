/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.securitysyncservices.registration;

import org.odpi.openmetadata.adminservices.configuration.properties.SecuritySyncConfig;
import org.odpi.openmetadata.frameworks.connectors.ConnectorBroker;
import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectorCheckedException;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.Connection;
import org.odpi.openmetadata.repositoryservices.auditlog.OMRSAuditLog;
import org.odpi.openmetadata.repositoryservices.auditlog.OMRSAuditLogRecordSeverity;
import org.odpi.openmetadata.repositoryservices.connectors.openmetadatatopic.OpenMetadataTopicConnector;
import org.odpi.openmetadata.repositoryservices.connectors.openmetadatatopic.OpenMetadataTopicListener;
import org.odpi.openmetadata.repositoryservices.ffdc.OMRSErrorCode;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.OMRSConfigErrorException;
import org.odpi.openmetadata.securitysyncservices.auditlog.SecuritySyncAuditCode;
import org.odpi.openmetadata.securitysyncservices.listener.GovernanceEventListener;
import org.odpi.openmetadata.securitysyncservices.processor.GovernanceEventProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SecuritySyncOperationalServices {

    private static final Logger log = LoggerFactory.getLogger(SecuritySyncOperationalServices.class);
    OpenMetadataTopicConnector inTopic;
    private String localServerName;               /* Initialized in constructor */
    private String localServerType;               /* Initialized in constructor */
    private String localMetadataCollectionName;   /* Initialized in constructor */
    private String localOrganizationName;         /* Initialized in constructor */
    private String localServerUserId;             /* Initialized in constructor */
    private String localServerURL;                /* Initialized in constructor */
    private int maxPageSize;                   /* Initialized in constructor */
    private OMRSAuditLog auditLog;

    /**
     * Constructor used at server startup.
     *
     * @param localServerName       name of the local server
     * @param localServerType       type of the local server
     * @param localOrganizationName name of the organization that owns the local server
     * @param localServerUserId     user id for this server to use if processing inbound messages.
     * @param localServerURL        URL root for this server.
     * @param maxPageSize           maximum number of records that can be requested on the pageSize parameter
     */
    public SecuritySyncOperationalServices(String localServerName,
                                           String localServerType,
                                           String localOrganizationName,
                                           String localServerUserId,
                                           String localServerURL,
                                           int maxPageSize) {
        this.localServerName = localServerName;
        this.localServerType = localServerType;
        this.localOrganizationName = localOrganizationName;
        this.localServerUserId = localServerUserId;
        this.localServerURL = localServerURL;
        this.maxPageSize = maxPageSize;
    }

    public void initialize(SecuritySyncConfig securitySyncConfig, OMRSAuditLog auditLog) {
        if (securitySyncConfig != null) {
            final String actionDescription = "initialize";
            this.auditLog = auditLog;

            logAudit(SecuritySyncAuditCode.SERVICE_INITIALIZING, actionDescription);

            GovernanceEventProcessor governanceEventProcessor = new GovernanceEventProcessor(securitySyncConfig, auditLog);

            inTopic = getTopicConnector(securitySyncConfig.getSecuritySyncInTopic(), auditLog);
            OpenMetadataTopicListener governanceEventListener = new GovernanceEventListener(governanceEventProcessor);
            inTopic.registerListener(governanceEventListener);

            startTopic(inTopic);

            governanceEventProcessor.processExistingGovernedAssetsFromRepository();
            logAudit(SecuritySyncAuditCode.SERVICE_INITIALIZED, actionDescription);
        }
    }


    /**
     * Returns the connector created from topic connection properties
     *
     * @param topicConnection properties of the topic connection
     * @return the connector created based on the topic connection properties
     */
    private OpenMetadataTopicConnector getTopicConnector(Connection topicConnection, OMRSAuditLog auditLog) {
        try {
            ConnectorBroker connectorBroker = new ConnectorBroker();

            OpenMetadataTopicConnector topicConnector = (OpenMetadataTopicConnector) connectorBroker.getConnector(topicConnection);
            topicConnector.setAuditLog(auditLog);

            return topicConnector;
        } catch (Exception error) {
            String methodName = "getTopicConnector";
            log.debug("Unable to create topic connector: " + error.toString());

            OMRSErrorCode errorCode = OMRSErrorCode.NULL_TOPIC_CONNECTOR;
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
     * Shutdown the Security Sync Services.
     *
     * @param permanent boolean flag indicating whether this server permanently shutting down or not
     * @return boolean indicated whether the disconnect was successful.
     */
    public boolean disconnect(boolean permanent) {

        try {
            inTopic.disconnect();
        } catch (ConnectorCheckedException e) {
            log.error("Error disconnecting in topic connector");
        }

        final String actionDescription = "shutdown";
        logAudit(SecuritySyncAuditCode.SERVICE_SHUTDOWN, actionDescription);

        return false;
    }

    private void startTopic(OpenMetadataTopicConnector topic) {
        try {
            topic.start();
        } catch (ConnectorCheckedException e) {
            log.error(e.getErrorMessage());
        }
    }

    private void logAudit(SecuritySyncAuditCode auditCode, String actionDescription) {
        auditLog.logRecord(actionDescription,
                auditCode.getLogMessageId(),
                OMRSAuditLogRecordSeverity.INFO,
                auditCode.getFormattedLogMessage("Security Sync"),
                null,
                auditCode.getSystemAction(),
                auditCode.getUserAction());
    }
}
