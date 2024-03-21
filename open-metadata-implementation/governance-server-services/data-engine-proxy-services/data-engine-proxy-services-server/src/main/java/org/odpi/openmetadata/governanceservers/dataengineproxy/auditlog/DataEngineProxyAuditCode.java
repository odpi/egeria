/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.governanceservers.dataengineproxy.auditlog;

import org.odpi.openmetadata.frameworks.auditlog.AuditLogRecordSeverityLevel;
import org.odpi.openmetadata.frameworks.auditlog.messagesets.AuditLogMessageDefinition;
import org.odpi.openmetadata.frameworks.auditlog.messagesets.AuditLogMessageSet;

/**
 * The DataEngineProxyAuditCode is used to define the message content for the OMRS Audit Log.
 * The 5 fields in the enum are:
 * <ul>
 *     <li>Log Message id - to uniquely identify the message</li>
 *     <li>Severity - is this an event, decision, action, error or exception</li>
 *     <li>Log Message Text - includes placeholder to allow additional values to be captured</li>
 *     <li>Additional Information - further parameters and data relating to the audit message (optional)</li>
 *     <li>SystemAction - describes the result of the situation</li>
 *     <li>UserAction - describes how a user should correct the situation</li>
 * </ul>
 */
public enum DataEngineProxyAuditCode implements AuditLogMessageSet {

    SERVICE_INITIALIZING("DATA-ENGINE-PROXY-0001",
                         AuditLogRecordSeverityLevel.INFO,
                         "The Data Engine Proxy is initializing a new server instance",
                         "The local server has started up a new instance of the Data Engine Proxy.",
                         "No action is required.  This is part of the normal operation of the service."),
    SERVICE_INITIALIZED("DATA-ENGINE-PROXY-0005",
                        AuditLogRecordSeverityLevel.INFO,
            "The Data Engine Proxy has initialized a new instance for server {0}",
            "The local server has completed initialization of a new instance.",
            "No action is required.  This is part of the normal operation of the service."),
    INIT_POLLING("DATA-ENGINE-PROXY-0006",
                 AuditLogRecordSeverityLevel.INFO,
            "The Data Engine Proxy is initializing polling for changes",
            "The local server has started up a new change poller for the Data Engine Proxy.",
            "No action is required.  This is part of the normal operation of the service."),
    SERVICE_SHUTDOWN("DATA-ENGINE-PROXY-0007",
                     AuditLogRecordSeverityLevel.INFO,
            "The Data Engine Proxy is shutting down its instance for server {0}",
            "The local server has requested shut down of a Data Engine Proxy instance.",
            "No action is required.  This is part of the normal operation of the service."),
    ERROR_SHUTDOWN("DATA-ENGINE-PROXY-0008",
                   AuditLogRecordSeverityLevel.EXCEPTION,
            "The service is not shutdown properly.",
            "The connection could not be shutdown.",
            "Try again. "),
    POLLING("DATA-ENGINE-PROXY-0009",
            AuditLogRecordSeverityLevel.INFO,
            "The Data Engine Proxy is polling for changes between {0} and {1}",
            "The local server is looking for changes since the last poll interval.",
            "No action is required.  This is part of the normal operation of the service."),
    USER_NOT_AUTHORIZED("DATA-ENGINE-PROXY-0011",
                        AuditLogRecordSeverityLevel.ERROR,
            "The user is not authorized for the Data Engine OMAS operation: {0}",
            "The system is unable to process the operation due to the user not being authorized to do so.",
            "Check your OMAS configuration and user authorizations."),
    POLLING_TYPE_START("DATA-ENGINE-PROXY-0012",
                       AuditLogRecordSeverityLevel.INFO,
            "The Data Engine Proxy is polling for changes to {0}",
            "The local server is looking for changes to the specified information since the last poll interval.",
            "No action is required.  This is part of the normal operation of the service."),
    POLLING_TYPE_FINISH("DATA-ENGINE-PROXY-0013",
                        AuditLogRecordSeverityLevel.INFO,
            "The Data Engine Proxy has completed polling for changes to {0}",
            "The local server has completed looking for changes to the specified type since the last poll interval.",
            "No action is required.  This is part of the normal operation of the service."),
    RUNTIME_EXCEPTION("DATA-ENGINE-PROXY-0014",
                      AuditLogRecordSeverityLevel.EXCEPTION,
            "The Data Engine Proxy processing interrupted due to runtime error.",
            "The system was unable to complete processing because the sub-system error. System will attempt to retry the process if possible.",
            "Check diagnostic message from audit log to determine the cause if the problem persists."),

    ;

    private final String                      logMessageId;
    private final AuditLogRecordSeverityLevel severity;
    private final String logMessage;
    private final String systemAction;
    private final String userAction;


    /**
     * The constructor for OMRSAuditCode expects to be passed one of the enumeration rows defined in
     * OMRSAuditCode above.   For example:
     * <p>
     * OMRSAuditCode   auditCode = OMRSAuditCode.SERVER_NOT_AVAILABLE;
     * <p>
     * This will expand out to the 5 parameters shown below.
     *
     * @param messageId    - unique id for the message
     * @param severity     - the severity of the message
     * @param message      - text for the message
     * @param systemAction - description of the action taken by the system when the condition happened
     * @param userAction   - instructions for resolving the situation, if any
     */
    DataEngineProxyAuditCode(String messageId, AuditLogRecordSeverityLevel severity, String message,
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
    public AuditLogMessageDefinition getMessageDefinition(String ...params) {
        AuditLogMessageDefinition messageDefinition = new AuditLogMessageDefinition(logMessageId,
                severity,
                logMessage,
                systemAction,
                userAction);
        messageDefinition.setMessageParameters(params);
        return messageDefinition;
    }

}
