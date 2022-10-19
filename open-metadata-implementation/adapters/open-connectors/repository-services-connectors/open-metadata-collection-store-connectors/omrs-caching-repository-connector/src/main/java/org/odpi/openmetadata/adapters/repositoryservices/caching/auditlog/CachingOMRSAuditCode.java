/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.caching.auditlog;

import org.odpi.openmetadata.frameworks.auditlog.messagesets.AuditLogMessageDefinition;
import org.odpi.openmetadata.frameworks.auditlog.messagesets.AuditLogMessageSet;
import org.odpi.openmetadata.repositoryservices.auditlog.OMRSAuditLogRecordSeverity;

/**
 * The CachingOMRSAuditCode is used to define the message content for the OMRS Audit Log.
 * <p>
 * The 5 fields in the enum are:
 * <ul>
 *     <li>Log Message Id - to uniquely identify the message</li>
 *     <li>Severity - is this an event, decision, action, error or exception</li>
 *     <li>Log Message Text - includes placeholder to allow additional values to be captured</li>
 *     <li>Additional Information - further parameters and data relating to the audit message (optional)</li>
 *     <li>SystemAction - describes the result of the situation</li>
 *     <li>UserAction - describes how a user should correct the situation</li>
 * </ul>
 */
public enum CachingOMRSAuditCode implements AuditLogMessageSet {

    REPOSITORY_SERVICE_STARTING("OMRS-CACHING-REPOSITORY-0001",
            OMRSAuditLogRecordSeverity.INFO,
            "The caching repository proxy is starting a new server instance",
            "The local server has started up a new instance of the caching repository proxy.",
            "No action is required. This is part of the normal operation of the service."),
    REPOSITORY_SERVICE_STARTED("OMRS-CACHING-REPOSITORY-0002",
            OMRSAuditLogRecordSeverity.INFO,
            "The caching repository proxy has started a new instance for server {0}",
            "The local server has completed startup of a new instance.",
            "No action is required. This is part of the normal operation of the service."),
    REPOSITORY_SERVICE_SHUTDOWN("OMRS-CACHING-REPOSITORY-0003",
            OMRSAuditLogRecordSeverity.INFO,
            "The caching repository proxy has shutdown its instance for server {0}",
            "The local server has requested shut down of a caching repository proxy instance.",
            "No action is required. This is part of the normal operation of the service."),
    ;


    final private String logMessageId;
    final private OMRSAuditLogRecordSeverity severity;
    final private String logMessage;
    final private String systemAction;
    final private String userAction;


    /**
     * The constructor for OMRSAuditCode expects to be passed one of the enumeration rows defined in
     * OMRSAuditCode above.   For example:
     * <p>
     * OMRSAuditCode   auditCode = OMRSAuditCode.SERVER_NOT_AVAILABLE;
     * <p>
     * This will expand out to the 4 parameters shown below.
     *
     * @param messageId    - unique Id for the message
     * @param severity     - the severity of the message
     * @param message      - text for the message
     * @param systemAction - description of the action taken by the system when the condition happened
     * @param userAction   - instructions for resolving the situation, if any
     */
    CachingOMRSAuditCode(String messageId, OMRSAuditLogRecordSeverity severity, String message,
                         String systemAction, String userAction) {
        this.logMessageId = messageId;
        this.severity = severity;
        this.logMessage = message;
        this.systemAction = systemAction;
        this.userAction = userAction;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public AuditLogMessageDefinition getMessageDefinition() {
        return new AuditLogMessageDefinition(logMessageId,
                severity,
                logMessage,
                systemAction,
                userAction);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public AuditLogMessageDefinition getMessageDefinition(String... params) {
        AuditLogMessageDefinition messageDefinition = new AuditLogMessageDefinition(logMessageId,
                severity,
                logMessage,
                systemAction,
                userAction);
        messageDefinition.setMessageParameters(params);
        return messageDefinition;
    }

}
