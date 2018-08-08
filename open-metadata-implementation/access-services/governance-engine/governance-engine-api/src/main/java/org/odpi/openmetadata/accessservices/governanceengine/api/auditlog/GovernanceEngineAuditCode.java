/* SPDX-License-Identifier: Apache-2.0 */

package org.odpi.openmetadata.accessservices.governanceengine.api.auditlog;

import org.odpi.openmetadata.repositoryservices.auditlog.OMRSAuditLogRecordSeverity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.MessageFormat;
import java.util.Arrays;

/**
 * The GovernanceConsumerAuditLog is used to define the message content for the OMRS Audit Log.
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
public enum GovernanceEngineAuditCode {
    SERVICE_INITIALIZING("OMAS-GOVERNANCE-ENGINE-0001",
            OMRSAuditLogRecordSeverity.INFO,
            "The Governance Engine Open Metadata Access Service (OMAS) is initializing",
            "The local handlers has started up the Governance Engine OMAS.",
            "No action is required.  This is part of the normal operation of the handlers."),

    SERVICE_REGISTERED_WITH_TOPIC("OMAS-GOVERNANCE-ENGINE-0002",
            OMRSAuditLogRecordSeverity.INFO,
            "The Governance Engine Open Metadata Access Service (OMAS) is registering a listener with the OMRS Topic",
            "The Governance Engine OMAS is registering to receive events from the connected open metadata repositories.",
            "No action is required.  This is part of the normal operation of the handlers."),

    SERVICE_INITIALIZED("OMAS-GOVERNANCE-ENGINE-0003",
            OMRSAuditLogRecordSeverity.INFO,
            "The Governance Engine Open Metadata Access Service (OMAS) is initialized",
            "The Governance Engine OMAS has completed initialization.",
            "No action is required.  This is part of the normal operation of the handlers."),



    SERVICE_TERMINATING("OMAS-GOVERNANCE-ENGINE-0004",
            OMRSAuditLogRecordSeverity.INFO,
            "The Governance Engine Open Metadata Access Service (OMAS) is shutting down",
            "The local handlers has requested shut down of the Governance Engine OMAS.",
            "No action is required.  This is part of the normal operation of the handlers."),
    SERVICE_SHUTDOWN("OMAS-GOVERNANCE-ENGINE-0005",
            OMRSAuditLogRecordSeverity.INFO,
            "The Governance Engine Open Metadata Access Service (OMAS) has completed shutdown",
            "The requested shutdown has now been processed",
            "No action is required.  This is part of the normal operation of the handlers."),

    ;

    private String logMessageId;
    private OMRSAuditLogRecordSeverity severity;
    private String logMessage;
    private String systemAction;
    private String userAction;

    private static final Logger log = LoggerFactory.getLogger(GovernanceEngineAuditCode.class);


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
    GovernanceEngineAuditCode(String messageId,
                              OMRSAuditLogRecordSeverity severity,
                              String message,
                              String systemAction,
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
     * @return logMessage (formatted with supplied parameters)
     */
    public String getFormattedLogMessage(String... params) {
        if (log.isDebugEnabled()) {
            log.debug(String.format("==> OMRS Audit Code.getMessage(%s)", Arrays.toString(params)));
        }

        MessageFormat mf = new MessageFormat(logMessage);
        String result = mf.format(params);

        if (log.isDebugEnabled()) {
            log.debug(String.format("<== OMRS Audit Code.getMessage(%s): %s", Arrays.toString(params), result));
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
