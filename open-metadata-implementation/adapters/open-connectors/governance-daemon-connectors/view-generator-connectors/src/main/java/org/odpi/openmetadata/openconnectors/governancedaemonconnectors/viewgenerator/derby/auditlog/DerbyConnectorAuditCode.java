/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
/**
 * This is the interface for the generic operations on data virtualization solutions
 */
package org.odpi.openmetadata.openconnectors.governancedaemonconnectors.viewgenerator.derby.auditlog;

import org.odpi.openmetadata.repositoryservices.auditlog.OMRSAuditLogRecordSeverity;

import java.text.MessageFormat;

public enum DerbyConnectorAuditCode {

    CONNECTOR_INITIALIZING("DERBYCONNECTOR-0001",
            OMRSAuditLogRecordSeverity.INFO,
            "The connector is being initialized",
            "The local server has started up a new instance of the derby connector.",
            "No action is required.  This is part of the normal operation of the service."),
    CONNECTOR_INITIALIZED("DERBYCONNECTOR-0002",
            OMRSAuditLogRecordSeverity.INFO,
            "The derby connector has initialized a new instance for server {0}",
            "The local server has completed initialization of a new instance.",
            "No action is required.  This is part of the normal operation of the service."),
    CONNECTOR_SHUTDOWN("DERBYCONNECTOR-0003",
            OMRSAuditLogRecordSeverity.INFO,
            "The derby connector is shutting down its instance for server {0}",
            "The local server has requested shut down of a derby connector.",
            "No action is required.  This is part of the normal operation of the service."),
    CONNECTOR_SERVER_CONFIGURATION_ERROR("DERBYCONNECTOR-0004",
            OMRSAuditLogRecordSeverity.EXCEPTION,
            "The configuration of the virtualization solution is not valid.",
            "The local server is unable to create a connector.",
            "Check the connection configuration"),
    CONNECTOR_SERVER_ADDRESS_ERROR("DERBYCONNECTOR-0005",
            OMRSAuditLogRecordSeverity.EXCEPTION,
            "The address of the virtualization solution is not valid.",
            "The local server is unable to create a connector.",
            "Check if the address of the solution is valid"),
    CONNECTOR_LOGICAL_TABLE_ERROR("DERBYCONNECTOR-0006.",
            OMRSAuditLogRecordSeverity.EXCEPTION,
            "The name of the logical table is not valid ",
            "The local server is unable to create a connector.",
            "Check if the name of the logical is valid"),
    CONNECTOR_SERVER_CONNECTION_ERROR("DERBYCONNECTOR-0007.",
            OMRSAuditLogRecordSeverity.EXCEPTION,
            "The address of the virtualization solution cannot be connected.",
            "The local server is unable to create a connector.",
            "Check if the address of the solution is accessible."),
    CONNECTOR_QUERY_ERROR("DERBYCONNECTOR-0008",
            OMRSAuditLogRecordSeverity.EXCEPTION,
            "The logical table cannot be queried.",
            "No query result will be provided.",
            "Check if the query is valid."),
    CONNECTOR_INBOUND_EVENT_ERROR("DERBYCONNECTOR-0009",
                           OMRSAuditLogRecordSeverity.EXCEPTION,
            "Provided inbound event is invalid.",
                                   "No action on database will be executed.",
                                   "Check if the event is valid.");
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
    DerbyConnectorAuditCode(String messageId, OMRSAuditLogRecordSeverity severity, String message,
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
     *
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
