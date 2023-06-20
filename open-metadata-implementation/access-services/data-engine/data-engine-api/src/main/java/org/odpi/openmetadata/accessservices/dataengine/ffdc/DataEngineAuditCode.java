/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.dataengine.ffdc;

import lombok.Getter;
import lombok.ToString;
import org.odpi.openmetadata.frameworks.auditlog.messagesets.AuditLogMessageDefinition;
import org.odpi.openmetadata.repositoryservices.auditlog.OMRSAuditLogRecordSeverity;

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
@ToString
public enum DataEngineAuditCode {
    /**
     * OMAS-DATA-ENGINE-0001 The Data Engine Open Metadata Access Service (OMAS) is initializing a new server instance
     */
    SERVICE_INITIALIZING("OMAS-DATA-ENGINE-0001",
            OMRSAuditLogRecordSeverity.STARTUP,
            "The Data Engine Open Metadata Access Service (OMAS) is initializing a new server instance",
            "The local server has started up a new instance of the Data Engine OMAS.",
            Constants.NO_ACTION_IS_REQUIRED),

    /**
     * OMAS-DATA-ENGINE-0002 The Data Engine Open Metadata Access Service (OMAS) has initialized a new instance for server
     */
    SERVICE_INITIALIZED("OMAS-DATA-ENGINE-0002",
            OMRSAuditLogRecordSeverity.STARTUP,
            "The Data Engine Open Metadata Access Service (OMAS) has initialized a new instance for server {0}",
            "The Data Engine OMAS has completed initialization of a new instance.",
            Constants.NO_ACTION_IS_REQUIRED),

    /**
     * OMAS-DATA-ENGINE-0003 The Data Engine Open Metadata Access Service (OMAS) is shutting down its instance for server
     */
    SERVICE_SHUTDOWN("OMAS-DATA-ENGINE-0003",
            OMRSAuditLogRecordSeverity.SHUTDOWN,
            "The Data Engine Open Metadata Access Service (OMAS) is shutting down its instance for server {0}",
            "The local server has requested shut down of an Data Engine OMAS instance.",
            Constants.NO_ACTION_IS_REQUIRED),
    /**
     * OMAS-DATA-ENGINE-0004 The Data Engine Open Metadata Access Service (OMAS) is unable to initialize a new instance
     */
    SERVICE_INSTANCE_FAILURE("OMAS-DATA-ENGINE-0004",
            OMRSAuditLogRecordSeverity.EXCEPTION,
            "The Data Engine Open Metadata Access Service (OMAS) is unable to initialize a new instance; error " +
                    "message is {0}",
            "The access service detected an error during the start up of a specific server instance.  " +
                    "Its services are not available for the server.",
            "Review the error message and any other reported failures to determine the cause of the problem. " +
                    " Once this is resolved, restart the server."),
    /**
     * OMAS-DATA-ENGINE-0005 The Data Engine Open Metadata Access Service (OMAS) is unable to initialize a new instance
     */
    ERROR_INITIALIZING_TOPIC_CONNECTION("OMAS-DATA-ENGINE-0005",
            OMRSAuditLogRecordSeverity.EXCEPTION,
            "The Data Engine Open Metadata Access Service (OMAS) is unable to initialize a new instance; error " +
                    "message is {0}",
            "The access service detected an error during the start up of a specific server instance.  " +
                    "Its services are not available for the server.",
            "Review the error message and any other reported failures to determine the cause of the problem. " +
                    " Once this is resolved, restart the server."),
    /**
     * OMAS-DATA-ENGINE-0006 The Data Engine Open Metadata Access Service (OMAS) is unable to process an event on its in topic
     */
    PROCESS_EVENT_EXCEPTION("OMAS- DATA-ENGINE-0006",
            OMRSAuditLogRecordSeverity.EXCEPTION,
            "The Data Engine Open Metadata Access Service (OMAS) is unable to process an event on its in topic {0}; exception {1} returned " +
                    "error message: {2}",
            "The access service detected an error during the start up of the out topic.  Its services are not available for the server.",
            "Review the error message and any other reported failures to determine the cause of the problem.  Check the status of the event " +
                    "bus.  Once this is resolved, restart the server."),
    /**
     * OMAS-DATA-ENGINE-0007 The Data Engine Open Metadata Access Service (OMAS) is unable to parse an event
     */
    PARSE_EVENT_EXCEPTION("OMAS- DATA-ENGINE-0007",
            OMRSAuditLogRecordSeverity.EXCEPTION,
            "The data engine event {0} could not be parsed. Error: {1}",
            "The system is unable to process the event.",
            "Verify the topic configuration or the event schema."),
    /**
     * OMAS-DATA-ENGINE-0008 The Data Engine Open Metadata Access Service (OMAS) client has sent an event
     */
    IN_TOPIC_EVENT_SENT("OMAS-DATA-ENGINE-0008",
            OMRSAuditLogRecordSeverity.EVENT,
            "The Data Engine Open Metadata Access Service (OMAS) client has sent event of type: {0}",
            "The access service client sends out event notification produced by external source like data engine system.",
            "This event contains external metadata changes that need to be processed by the access service.");

    /**
     * A message definition object for logging
     * -- GETTER --
     * Retrieves a message definition object for logging. This method is used when there are no message inserts.
     *
     * @return message definition object
     */
    @Getter
    private final AuditLogMessageDefinition messageDefinition;

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
        messageDefinition = new AuditLogMessageDefinition(messageId, severity, message, systemAction, userAction);
    }

    /**
     * Retrieve a message definition object for logging.  This method is used when there are values to be inserted into the message.
     *
     * @param params array of parameters (all strings).  They are inserted into the message according to the numbering in the message text.
     * @return message definition object.
     */
    public AuditLogMessageDefinition getMessageDefinition(String... params) {
        messageDefinition.setMessageParameters(params);
        return messageDefinition;
    }

    private static class Constants {
        public static final String NO_ACTION_IS_REQUIRED = "No action is required.  This is part of the normal operation of the service.";
    }
}