/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.securitysyncservices.admin;

import org.odpi.openmetadata.adminservices.configuration.properties.SecuritySyncConfig;
import org.odpi.openmetadata.adminservices.configuration.registration.SecuritySyncAdmin;
import org.odpi.openmetadata.frameworks.connectors.ConnectorBroker;
import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectorCheckedException;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.Connection;
import org.odpi.openmetadata.repositoryservices.auditlog.OMRSAuditLog;
import org.odpi.openmetadata.repositoryservices.auditlog.OMRSAuditLogRecordSeverity;
import org.odpi.openmetadata.repositoryservices.connectors.omrstopic.OMRSTopicConnector;
import org.odpi.openmetadata.repositoryservices.connectors.openmetadatatopic.OpenMetadataTopicConnector;
import org.odpi.openmetadata.repositoryservices.connectors.openmetadatatopic.OpenMetadataTopicListener;
import org.odpi.openmetadata.repositoryservices.ffdc.OMRSErrorCode;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.OMRSConfigErrorException;
import org.odpi.openmetadata.securitysyncservices.auditlog.SecuritySyncAuditCode;
import org.odpi.openmetadata.securitysyncservices.listener.SecuritySyncListener;
import org.odpi.openmetadata.securitysyncservices.processor.SecuritySyncProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SecuritySync implements SecuritySyncAdmin {

    private static final Logger log = LoggerFactory.getLogger(SecuritySync.class);

    @Override
    public void initialize(SecuritySyncConfig securitySyncConfig,
                           OMRSTopicConnector enterpriseOMRSTopicConnector,
                           OMRSAuditLog auditLog,
                           String serverUserName) {

        SecuritySyncProcessor securitySyncProcessor = new SecuritySyncProcessor(securitySyncConfig, auditLog);

        OpenMetadataTopicConnector inTopic = getTopicConnector(securitySyncConfig.getSecuritySyncInTopic(), auditLog);
        OpenMetadataTopicListener securitySyncListener = new SecuritySyncListener(auditLog, securitySyncProcessor);
        inTopic.registerListener(securitySyncListener);

        SecuritySyncAuditCode auditCode = SecuritySyncAuditCode.SERVICE_INITIALIZED;
        final String          actionDescription = "initialize";

        auditLog.logRecord(actionDescription,
                auditCode.getLogMessageId(),
                OMRSAuditLogRecordSeverity.INFO,
                auditCode.getFormattedLogMessage("Security Sync"),
                null,
                auditCode.getSystemAction(),
                auditCode.getUserAction());

        startTopic(inTopic);
    }

    @Override
    public void shutdown() {

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
        } catch (Throwable error) {
            String methodName = "getTopicConnector";

            if (log.isDebugEnabled()) {
                log.debug("Unable to create topic connector: " + error.toString());
            }

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

    private void startTopic(OpenMetadataTopicConnector topic) {

        try {
            topic.start();
        } catch (ConnectorCheckedException e) {
            log.error(e.getErrorMessage());
        }
    }
}
