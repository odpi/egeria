/*
 *  SPDX-License-Identifier: Apache-2.0
 *  Copyright Contributors to the ODPi Egeria project.
 */

package org.odpi.openmetadata.securityofficerservices.registration;

import org.odpi.openmetadata.adminservices.configuration.properties.SecurityOfficerConfig;
import org.odpi.openmetadata.adminservices.ffdc.exception.OMAGConfigurationErrorException;
import org.odpi.openmetadata.frameworks.connectors.ConnectorBroker;
import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectorCheckedException;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.Connection;
import org.odpi.openmetadata.repositoryservices.auditlog.OMRSAuditLog;
import org.odpi.openmetadata.repositoryservices.auditlog.OMRSAuditLogRecordSeverity;
import org.odpi.openmetadata.repositoryservices.connectors.openmetadatatopic.OpenMetadataTopicConnector;
import org.odpi.openmetadata.repositoryservices.connectors.openmetadatatopic.OpenMetadataTopicListener;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.OMRSConfigErrorException;
import org.odpi.openmetadata.securityofficerservices.auditlog.SecurityOfficerAuditCode;
import org.odpi.openmetadata.securityofficerservices.ffdc.SecurityOfficerErrorCode;
import org.odpi.openmetadata.securityofficerservices.listener.SecurityOfficerEventListener;
import org.odpi.openmetadata.securityofficerservices.processor.SecurityOfficerEventProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SecurityOfficerOperationalServices {

    private static final Logger log = LoggerFactory.getLogger(SecurityOfficerConfig.class);
    private OpenMetadataTopicConnector inTopic;
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
    public SecurityOfficerOperationalServices(String localServerName,
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

    public void initialize(SecurityOfficerConfig securityOfficerConfig, OMRSAuditLog auditLog) throws OMAGConfigurationErrorException {
        if (securityOfficerConfig != null) {
            final String actionDescription = "initialize";
            this.auditLog = auditLog;

            logAudit(SecurityOfficerAuditCode.SERVICE_INITIALIZING, actionDescription);

            SecurityOfficerEventProcessor securityOfficerEventProcessor = new SecurityOfficerEventProcessor(securityOfficerConfig, auditLog);

            inTopic = getTopicConnector(securityOfficerConfig.getSecurityOfficerServerInTopic(), auditLog);
            OpenMetadataTopicListener securityOfficerEventListener = new SecurityOfficerEventListener(securityOfficerEventProcessor);

            inTopic.registerListener(securityOfficerEventListener);

            startTopic(inTopic, securityOfficerConfig.getSecurityOfficerServerInTopicName());

            logAudit(SecurityOfficerAuditCode.SERVICE_INITIALIZED, actionDescription);
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
            final String methodName = "getTopicConnector";

            throw new OMRSConfigErrorException(SecurityOfficerErrorCode.NULL_TOPIC_CONNECTOR.getMessageDefinition(methodName),
                                               this.getClass().getName(),
                                               methodName,
                                               error);

        }
    }

    /**
     * Shutdown the Security Officer Services.
     *
     * @return boolean indicated whether the disconnect was successful.
     */
    public boolean disconnect() {

        try {
            inTopic.disconnect();
        } catch (ConnectorCheckedException e) {
            log.error("Error disconnecting in topic connector");
        }

        final String actionDescription = "shutdown";
        logAudit(SecurityOfficerAuditCode.SERVICE_SHUTDOWN, actionDescription);

        return false;
    }

    private void startTopic(OpenMetadataTopicConnector topic, String topicName) throws OMAGConfigurationErrorException {
        try {
            topic.start();
        } catch (ConnectorCheckedException e) {
            String action = "Unable to initialize the topic connection";
            SecurityOfficerAuditCode auditCode = SecurityOfficerAuditCode.ERROR_INITIALIZING_SECURITY_OFFICER_SERVER_TOPIC_CONNECTION;
            auditLog.logRecord(action,
                    auditCode.getLogMessageId(),
                    auditCode.getSeverity(),
                    auditCode.getFormattedLogMessage(topicName, localServerName),
                    null,
                    auditCode.getSystemAction(),
                    auditCode.getUserAction());
            throw new OMAGConfigurationErrorException(400,
                    this.getClass().getSimpleName(),
                    action,
                    auditCode.getFormattedLogMessage(),
                    auditCode.getSystemAction(),
                    auditCode.getUserAction()
            );
        }
    }

    private void logAudit(SecurityOfficerAuditCode auditCode, String actionDescription) {
        auditLog.logRecord(actionDescription,
                auditCode.getLogMessageId(),
                OMRSAuditLogRecordSeverity.INFO,
                auditCode.getFormattedLogMessage("Security Officer Server"),
                null,
                auditCode.getSystemAction(),
                auditCode.getUserAction());
    }
}