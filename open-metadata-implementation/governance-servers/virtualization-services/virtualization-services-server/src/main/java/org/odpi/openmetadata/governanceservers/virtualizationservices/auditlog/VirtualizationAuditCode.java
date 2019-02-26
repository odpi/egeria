/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.governanceservers.virtualizationservices.auditlog;

import org.odpi.openmetadata.repositoryservices.auditlog.OMRSAuditLogRecordSeverity;

import java.text.MessageFormat;

public enum VirtualizationAuditCode {

    SERVICE_INITIALIZING("VIRTUALIZER-0001",
            OMRSAuditLogRecordSeverity.INFO,
            "The Virtualizer is initializing a new server instance",
            "The local server has started up a new instance of the Virutalizer.",
            "No action is required.  This is part of the normal operation of the service."),
    SERVICE_INITIALIZED("VIRTUALIZER-0002",
            OMRSAuditLogRecordSeverity.INFO,
            "The Virtualizer has initialized a new instance for server {0}",
            "The local server has completed initialization of a new instance.",
            "No action is required.  This is part of the normal operation of the service."),
    SERVICE_SHUTDOWN("VIRTUALIZER-0003",
            OMRSAuditLogRecordSeverity.INFO,
            "The Virtualizer is shutting down its instance for server {0}",
            "The local server has requested shut down of a Virtualizer instance.",
            "No action is required.  This is part of the normal operation of the service."),
    IV_OUT_TOPIC_CONNECTOR_INITIALIZED("VIRTUALIZER-0004",
            OMRSAuditLogRecordSeverity.INFO,
            "The Virtualizer has initialized a Kafka connector for Information View Out Topic",
            "The local server has completed initialization of a new kafka connector.",
            "No action is required.  This is part of the normal operation of the service."),
    ERROR_INITIALIZING_IV_OUT_TOPIC_CONNECTION("VIRTUALIZER-0005",
            OMRSAuditLogRecordSeverity.EXCEPTION,
            "Unable to initialize the Information View Out Topic connection",
            "The connection could not be initialized.",
            "Review the exception and resolve the configuration. "),
    IV_IN_TOPIC_CONNECTOR_INITIALIZED("VIRTUALIZER-0006",
            OMRSAuditLogRecordSeverity.INFO,
            "The Virtualizer has initialized a Kafka connector for Information View In Topic",
            "The local server has completed initialization of a new kafka connector.",
            "No action is required.  This is part of the normal operation of the service."),
    ERROR_INITIALIZING_IV_IN_TOPIC_CONNECTION("VIRTUALIZER-0007",
            OMRSAuditLogRecordSeverity.EXCEPTION,
            "Unable to initialize the Information View In Topic connection",
            "The connection could not be initialized.",
            "Review the exception and resolve the configuration. "),
    ERROR_SHUTDOWN("VIRTUALIZER-008",
            OMRSAuditLogRecordSeverity.EXCEPTION,
            "The service is not shutdown properly.",
            "The connection could not be shutdown.",
            "Try again. ")
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
     * @param message      - text for the message
     * @param systemAction - description of the action taken by the system when the condition happened
     * @param userAction   - instructions for resolving the situation, if any
     */
    VirtualizationAuditCode(String messageId, OMRSAuditLogRecordSeverity severity, String message,
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
     * Return the severity object for the log
     * @return severity
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
