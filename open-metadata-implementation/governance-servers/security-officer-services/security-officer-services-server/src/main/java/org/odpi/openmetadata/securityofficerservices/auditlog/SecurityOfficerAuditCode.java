/*
 *  SPDX-License-Identifier: Apache-2.0
 *  Copyright Contributors to the ODPi Egeria project.
 */

package org.odpi.openmetadata.securityofficerservices.auditlog;

import org.odpi.openmetadata.repositoryservices.auditlog.OMRSAuditLogRecordSeverity;

import java.text.MessageFormat;

public enum SecurityOfficerAuditCode {

    SERVICE_INITIALIZING("SECURITY-OFFICER-SERVER-0001",
            OMRSAuditLogRecordSeverity.INFO,
            "The Security Officer Server is initializing a new server instance",
            "The local server has started up a new instance of the Security Officer Server.",
            "No action is required.  This is part of the normal operation of the service."),
    SERVICE_INITIALIZED("SECURITY-OFFICER-SERVER-0002",
            OMRSAuditLogRecordSeverity.INFO,
            "The Security Officer Server has initialized a new instance for server {0}",
            "The Security Officer Server has completed initialization of a new instance.",
            "No action is required.  This is part of the normal operation of the service."),
    SERVICE_SHUTDOWN("SECURITY-OFFICER-SERVER-0003",
            OMRSAuditLogRecordSeverity.INFO,
            "The Security Officer Server is shutting down its instance for server {0}",
            "The local server has requested shut down of a Security Officer Server instance.",
            "No action is required.  This is part of the normal operation of the service."),
    ERROR_INITIALIZING_SECURITY_OFFICER_SERVER_TOPIC_CONNECTION("SECURITY-OFFICER-SERVER-0004",
            OMRSAuditLogRecordSeverity.EXCEPTION,
            "The Security Officer Server is unable to initialize the connection to topic {0} in the instance for server {1} ",
            "The connection to topic could not be initialized.",
            "Review the exception and resolve the configuration.");

    private String logMessageId;
    private OMRSAuditLogRecordSeverity severity;
    private String logMessage;
    private String systemAction;
    private String userAction;


    /**
     * The constructor for OMRSAuditCode expects to be passed one of the enumeration rows defined in
     * OMRSAuditCode above.   For example:
     * <p>
     * OMRSAuditCode   auditCode = OMRSAuditCode.SERVER_NOT_AVAILABLE;
     * <p>
     * This will expand out to the 4 parameters shown below.
     *
     * @param messageId    - unique Id for the message
     * @param message      - text for the message
     * @param severity     - the severity of the message
     * @param systemAction - description of the action taken by the system when the condition happened
     * @param userAction   - instructions for resolving the situation, if any
     */
    SecurityOfficerAuditCode(String messageId, OMRSAuditLogRecordSeverity severity, String message, String systemAction, String userAction) {
        this.logMessageId = messageId;
        this.severity = severity;
        this.logMessage = message;
        this.systemAction = systemAction;
        this.userAction = userAction;
    }

    /**
     * Returns the unique identifier for the error message.
     *
     * @return logMessageId
     */
    public String getLogMessageId() {
        return logMessageId;
    }

    /**
     * Returns the log message with the placeholders filled out with the supplied parameters.
     *
     * @param params - strings that plug into the placeholders in the logMessage
     * @return logMessage (formatted with supplied parameters)
     */
    public String getFormattedLogMessage(String... params) {
        MessageFormat mf = new MessageFormat(logMessage);
        return mf.format(params);
    }


    /**
     * Returns a description of the action taken by the system when the condition that caused this exception was
     * detected.
     *
     * @return systemAction String
     */
    public String getSystemAction() {
        return systemAction;
    }


    /**
     * Returns instructions of how to resolve the issue reported in this exception.
     *
     * @return userAction String
     */
    public String getUserAction() {
        return userAction;
    }

    /**
     * Return the severity of the audit log record.
     *
     * @return OMRSAuditLogRecordSeverity enum
     */
    public OMRSAuditLogRecordSeverity getSeverity() {
        return severity;
    }
}