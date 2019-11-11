/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.userinterface.uichassis.springboot.auditlog;

import org.odpi.openmetadata.repositoryservices.auditlog.OMRSAuditLogRecordSeverity;

import java.text.MessageFormat;

/**
 * The AssetConsumerAuditCode is used to define the message content for the OMRS Audit Log.
 * <p>
 * The 5 fields in the enum are:
 * <ul>
 * <li>Log Message Id - to uniquely identify the message</li>
 * <li>Severity - is this an event, decision, action, error or exception</li>
 * <li>Log Message Text - includes placeholder to allow additional values to be captured</li>
 * <li>Additional Information - further parameters and data relating to the audit message (optional)</li>
 * <li>SystemAction - describes the result of the situation</li>
 * <li>UserAction - describes how a user should correct the situation</li>
 * </ul>
 */
public enum ViewServiceAuditCode {
    SERVICE_INITIALIZING("OMVS-VIEW-0001",
            OMRSAuditLogRecordSeverity.INFO,
            "The {1} Open Metadata View Service (OMVS) is initializing for server {0}",
            "The {1} OMVS has started initialization.",
            "No action is required. This is part of the normal operation of the service."),

    SERVICE_INITIALIZED("OMVS-VIEW-0003",
            OMRSAuditLogRecordSeverity.INFO,
            "The {1} Open Metadata View Service (OMVS) has initialized for server {0}",
            "The {1} OMVS has completed initialization.",
            "No action is required.  This is part of the normal operation of the service."),

    SERVICE_SHUTDOWN("OMVS-VIEW-0004",
            OMRSAuditLogRecordSeverity.INFO,
            "Open Metadata View Service (OMVS) is shutting down for server {0}",
            "The local server has requested shut down of a OMVS instance.",
            "No action is required.  This is part of the normal operation of the service."),

    SERVICE_INSTANCE_FAILURE("OMAS-VIEW-0005",
            OMRSAuditLogRecordSeverity.ERROR,
            "The {0} Open Metadata View Service (OMVS) is unable to initialize a new instance; error message is {1}",
            "The view service detected an error during the start up of a specific server instance.  Its services are not available for the server.",
            "Review the error message and any other reported failures to determine the cause of the problem.  Once this is resolved, restart the server."),
    UNKNOWN_SERVICE_INITIALIZING("OMVS-VIEW-0001",
            OMRSAuditLogRecordSeverity.INFO,
            "A Open Metadata View Service (OMVS) is initializing for server {0}",
            "The local server is initializing a new instance a OMVS.",
            "No action is required.  This is part of the normal operation of the service.")
    ;

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
     * @param severity     - severity of the message
     * @param message      - text for the message
     * @param systemAction - description of the action taken by the system when the condition happened
     * @param userAction   - instructions for resolving the situation, if any
     */
    ViewServiceAuditCode(String messageId, OMRSAuditLogRecordSeverity severity, String message,
                         String systemAction, String userAction) {
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
     * Return the severity of the audit log record.
     *
     * @return OMRSAuditLogRecordSeverity enum
     */
    public OMRSAuditLogRecordSeverity getSeverity() {
        return severity;
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
}
