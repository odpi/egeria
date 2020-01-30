/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.dataengine.server.auditlog;

import org.odpi.openmetadata.repositoryservices.auditlog.OMRSAuditLogRecordSeverity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.MessageFormat;
import java.util.Arrays;

/**
 * The DataEngineAuditCode is used to define the message content for the OMRS Audit Log.
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
public enum DataEngineAuditCode {
    SERVICE_INITIALIZING("OMAS-DATA-ENGINE-0001",
            OMRSAuditLogRecordSeverity.STARTUP,
            "The Data Engine Open Metadata Access Service (OMAS) is initializing a new server instance",
            "The local server has started up a new instance of the Data Engine OMAS.",
            "No action is required.  This is part of the normal operation of the service."),

    SERVICE_INITIALIZED("OMAS-DATA-ENGINE-0002",
            OMRSAuditLogRecordSeverity.STARTUP,
            "The Data Engine Open Metadata Access Service (OMAS) has initialized a new instance for server {0}",
            "The Data Engine OMAS has completed initialization of a new instance.",
            "No action is required.  This is part of the normal operation of the service."),

    SERVICE_SHUTDOWN("OMAS-DATA-ENGINE-0003",
            OMRSAuditLogRecordSeverity.SHUTDOWN,
            "The Data Engine Open Metadata Access Service (OMAS) is shutting down its instance for server {0}",
            "The local server has requested shut down of an Data Engine OMAS instance.",
            "No action is required.  This is part of the normal operation of the service."),

    SERVICE_INSTANCE_FAILURE("OMAS-DATA-ENGINE-0004",
            OMRSAuditLogRecordSeverity.EXCEPTION,
            "The Data Engine Open Metadata Access Service (OMAS) is unable to initialize a new instance; error " +
                    "message is {0}",
            "The access service detected an error during the start up of a specific server instance.  " +
                    "Its services are not available for the server.",
            "Review the error message and any other reported failures to determine the cause of the problem. " +
                    " Once this is resolved, restart the server."),
    ERROR_INITIALIZING_TOPIC_CONNECTION("OMAS-DATA-ENGINE-0005",
            OMRSAuditLogRecordSeverity.EXCEPTION,
            "The Data Engine Open Metadata Access Service (OMAS) is unable to initialize a new instance; error " +
                    "message is {0}",
            "The access service detected an error during the start up of a specific server instance.  " +
                    "Its services are not available for the server.",
            "Review the error message and any other reported failures to determine the cause of the problem. " +
                    " Once this is resolved, restart the server.");

    private final String logMessageId;
    private final OMRSAuditLogRecordSeverity severity;
    private final String logMessage;
    private final String systemAction;
    private final String userAction;

    private static final Logger log = LoggerFactory.getLogger(DataEngineAuditCode.class);

    /**
     * The constructor for DataEngineAuditCode expects to be passed one of the enumeration rows defined in
     * DataEngineAuditCode above.   For example:
     * <p>
     * DataEngineAuditCode   auditCode = DataEngineAuditCode.SERVICE_INITIALIZING;
     * <p>
     * This will expand out to the 4 parameters shown below.
     *
     * @param messageId    - unique Id for the message
     * @param severity     - severity of the message
     * @param message      - text for the message
     * @param systemAction - description of the action taken by the system when the condition happened
     * @param userAction   - instructions for resolving the situation, if any
     */
    DataEngineAuditCode(String messageId, OMRSAuditLogRecordSeverity severity, String message, String systemAction,
                        String userAction) {
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
     *
     * @return logMessage (formatted with supplied parameters)
     */
    public String getFormattedLogMessage(String... params) {
        if (log.isDebugEnabled()) {
            log.debug("<== DataEngine Audit Code.getMessage({})", Arrays.toString(params));
        }
        MessageFormat mf = new MessageFormat(logMessage);
        String result = mf.format(params);

        if (log.isDebugEnabled()) {
            log.debug("==> DataEngine Audit Code.getMessage({}): {}", Arrays.toString(params), result);
        }
        return result;
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

