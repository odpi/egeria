/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.dataplatformservices.auditlog;

import org.odpi.openmetadata.repositoryservices.auditlog.OMRSAuditLogRecordSeverity;

import java.text.MessageFormat;

public enum DataPlatformServicesAuditCode {


    SERVICE_INITIALIZING("DATA-PLATFORM-SERVICES-0001",
            OMRSAuditLogRecordSeverity.INFO,
            "The DATA-PLATFORM-SERVICES is initializing a new server instance",
            "The local server has started up a new instance of the Virutalizer.",
            "No action is required.  This is part of the normal operation of the service."),
    SERVICE_INITIALIZED("DATA-PLATFORM-SERVICES-0002",
            OMRSAuditLogRecordSeverity.INFO,
            "The DATA-PLATFORM-SERVICES has initialized a new instance for server {0}",
            "The local server has completed initialization of a new instance.",
            "No action is required.  This is part of the normal operation of the service."),
    SERVICE_SHUTDOWN("DATA-PLATFORM-SERVICES-0003",
            OMRSAuditLogRecordSeverity.INFO,
            "The DATA-PLATFORM-SERVICES is shutting down its instance for server {0}",
            "The local server has requested shut down of a DATA-PLATFORM-SERVICES instance.",
            "No action is required.  This is part of the normal operation of the service."),
    ERROR_INITIALIZING_DATA_PLATFORM_CONNECTION("DATA-PLATFORM-SERVICES-0004",
            OMRSAuditLogRecordSeverity.ERROR,
            "Unable to initialize the Data Platform connection",
            "The connection could not be initialized.",
            "Review the exception and resolve the configuration. "),
    DP_OMAS_IN_TOPIC_CONNECTION_INITIALIZED("DATA-PLATFORM-SERVICES-0005",
            OMRSAuditLogRecordSeverity.INFO,
            "The DATA-PLATFORM-SERVICES has initialized an event bus connector for Data Platform In Topic",
            "The local server has completed initialization of a new event bus connector.",
            "No action is required.  This is part of the normal operation of the service."),
    ERROR_INITIALIZING_DP_OMAS_IN_TOPIC_CONNECTION("DATA-PLATFORM-SERVICES-0006",
            OMRSAuditLogRecordSeverity.ERROR,
            "Unable to initialize the Data Platform OMAS In Topic connection",
            "The connection could not be initialized.",
            "Review the exception and resolve the configuration. "),
    ERROR_SHUTDOWN("DATA-PLATFORM-SERVICES-0007",
            OMRSAuditLogRecordSeverity.ERROR,
            "The service is not shutdown properly.",
            "The connection could not be shutdown.",
            "Try again. "),
    PUBLISH_EVENT_EXCEPTION("DATA-PLATFORM-SERVICES-0008",
            OMRSAuditLogRecordSeverity.EXCEPTION,
            "Event {0} could not be published: {1}",
            "The system is unable to process the request.",
            "Verify the topic configuration.");


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
    DataPlatformServicesAuditCode(String messageId, OMRSAuditLogRecordSeverity severity,
                                  String message, String systemAction, String userAction) {
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
