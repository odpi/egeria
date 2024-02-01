/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.glossaryview.server.admin;

import org.odpi.openmetadata.frameworks.auditlog.AuditLogRecordSeverityLevel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.MessageFormat;
import java.util.Arrays;

/**
 * Defines the message content for the OMRS Audit Log.
 * <p>
 * The 5 fields in the enum are:
 * <ul>
 * <li>Log Message Id - to uniquely identify the message</li>
 * <li>Severity - is this an event, decision, action, error or exception</li>
 * <li>Log Message Text - includes placeholder to allow additional values to be captured</li>
 * <li>SystemAction - describes the result of the situation</li>
 * <li>UserAction - describes how a user should correct the situation</li>
 * </ul>
 */
public enum GlossaryViewAuditCode {

    SERVICE_INITIALIZING("OMAS-GLOSSARY-VIEW-0001",
                         AuditLogRecordSeverityLevel.STARTUP,
                         "The Glossary View Open Metadata Access Service (OMAS) is initializing a new server instance",
                         "The local server has started up a new instance of the Glossary View OMAS.",
                         Constants.NORMAL_OPERATION_OF_THE_SERVICE),

    SERVICE_INITIALIZED("OMAS-GLOSSARY-VIEW-0002",
            AuditLogRecordSeverityLevel.STARTUP,
            "The Glossary View Open Metadata Access Service (OMAS) has initialized a new instance for server {0}",
            "The Glossary View OMAS has completed initialization of a new instance.",
            Constants.NORMAL_OPERATION_OF_THE_SERVICE),

    SERVICE_SHUTDOWN("OMAS-GLOSSARY-VIEW-0003",
                     AuditLogRecordSeverityLevel.SHUTDOWN,
            "The Glossary View Open Metadata Access Service (OMAS) is shutting down its instance for server {0}",
            "The local server has requested shut down of an Glossary View OMAS instance.",
            Constants.NORMAL_OPERATION_OF_THE_SERVICE),

    SERVICE_INSTANCE_FAILURE("OMAS-GLOSSARY-VIEW-0004",
                             AuditLogRecordSeverityLevel.EXCEPTION,
            "The Glossary View Open Metadata Access Service (OMAS) is unable to initialize a new instance; error message is {0}",
            "The access service detected an error during the start up of a specific server instance.  " +
                    "Its services are not available for the server.",
            "Review the error message and any other reported failures to determine the cause of the problem. " +
                    " Once this is resolved, restart the server.");

    private String logMessageId;
    private AuditLogRecordSeverityLevel severity;
    private String logMessage;
    private String systemAction;
    private String userAction;

    private static final Logger log = LoggerFactory.getLogger(GlossaryViewAuditCode.class);

    /**
     * @param messageId    unique Id of message
     * @param severity     severity of message
     * @param message      text of message
     * @param systemAction description of action taken by the system when the condition happened
     * @param userAction   instructions for resolving the situation, if any
     */
    GlossaryViewAuditCode(String messageId, AuditLogRecordSeverityLevel severity, String message, String systemAction,
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
     * @return severity
     */
    public AuditLogRecordSeverityLevel getSeverity() {
        return severity;
    }

    /**
     * Returns a description of the action taken by the system when the condition that caused this exception was
     * detected.
     *
     * @return systemAction
     */
    public String getSystemAction() {
        return systemAction;
    }

    /**
     * Returns instructions of how to resolve the issue reported in this exception.
     *
     * @return userAction
     */
    public String getUserAction() {
        return userAction;
    }

    /**
     * Returns the log message with the placeholders filled out with the supplied parameters.
     *
     * @param params strings that plug into the placeholders in the logMessage
     *
     * @return logMessage (formatted with supplied parameters)
     */
    public String getFormattedLogMessage(String... params) {
        log.debug("<== GlossaryView Audit Code.getMessage({})", Arrays.toString(params));

        MessageFormat mf = new MessageFormat(logMessage);
        String result = mf.format(params);

        log.debug("==> GlossaryView Audit Code.getMessage({}): {}", Arrays.toString(params), result);

        return result;
    }

    private static class Constants {
        public static final String NORMAL_OPERATION_OF_THE_SERVICE = "No action is required.  This is part of the normal operation of the service.";
    }
}
