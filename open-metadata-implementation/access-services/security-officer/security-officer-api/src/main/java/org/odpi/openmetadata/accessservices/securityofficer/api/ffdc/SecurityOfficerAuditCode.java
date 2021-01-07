/*
 *  SPDX-License-Identifier: Apache-2.0
 *  Copyright Contributors to the ODPi Egeria project.
 */
package org.odpi.openmetadata.accessservices.securityofficer.api.ffdc;

import org.odpi.openmetadata.frameworks.auditlog.messagesets.AuditLogMessageDefinition;
import org.odpi.openmetadata.frameworks.auditlog.messagesets.AuditLogMessageSet;
import org.odpi.openmetadata.repositoryservices.auditlog.OMRSAuditLogRecordSeverity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.MessageFormat;

public enum SecurityOfficerAuditCode implements AuditLogMessageSet
{
    SERVICE_INITIALIZING("OMAS-SECURITY-OFFICER-0001",
            OMRSAuditLogRecordSeverity.STARTUP,
            "The Security Officer Open Metadata Access Service (OMAS) is initializing",
            "The local server has started up a new instance of the Security Officer OMAS.",
            "No action is required.  This is part of the normal operation of the service."),

    SERVICE_REGISTERED_WITH_TOPIC("OMAS-SECURITY-OFFICER-0002",
            OMRSAuditLogRecordSeverity.STARTUP,
            "The Security Officer Open Metadata Access Service (OMAS) is registering a listener with the OMRS Topic for server instance {0}",
            "The Security Officer OMAS is registering the server instance to receive events from the connected open metadata repositories.",
            "No action is required.  This is part of the normal operation of the service."),

    SERVICE_INITIALIZED("OMAS-SECURITY-OFFICER-0003",
            OMRSAuditLogRecordSeverity.STARTUP,
            "The Security Officer Open Metadata Access Service (OMAS) has initialized a new instance for server {0}",
            "The Security Officer OMAS has completed initialization of a new instance.",
            "No action is required.  This is part of the normal operation of the service."),

    SERVICE_TERMINATING("OMAS-SECURITY-OFFICER-0004",
            OMRSAuditLogRecordSeverity.SHUTDOWN,
            "The Security Officer Open Metadata Access Service (OMAS) is shutting down server instance {0}",
            "The local handlers has requested shut down of the Security Officer OMAS.",
            "No action is required.  This is part of the normal operation of the service."),

    SERVICE_SHUTDOWN("OMAS-SECURITY-OFFICER-0005",
            OMRSAuditLogRecordSeverity.SHUTDOWN,
            "The Security Officer Open Metadata Access Service (OMAS) has completed shutdown of server instance {0}",
            "The requested shutdown has now been processed",
            "No action is required.  This is part of the normal operation of the service."),

    SERVICE_INSTANCE_FAILURE("OMAS-SECURITY-OFFICER-0006",
            OMRSAuditLogRecordSeverity.EXCEPTION,
            "The Security Officer Open Metadata Access Service (OMAS) is unable to initialize a new instance; error message is {0}",
            "The access service detected an error during the start up of a specific server instance.  Its services are not available for the server.",
            "Review the error message and any other reported failures to determine the cause of the problem.  Once this is resolved, restart the server."),

    ERROR_INITIALIZING_TOPIC_CONNECTION("OMAS-SECURITY-OFFICER-0007",
            OMRSAuditLogRecordSeverity.EXCEPTION,
            "Unable to initialize the Security Officer Open Metadata Access Service (OMAS) topic connection {0} for server instance {1}; error message was: {2}",
            "The connection could not be initialized.",
            "Review the exception and resolve the configuration. "),

    PUBLISH_EVENT_EXCEPTION("OMAS-SECURITY-OFFICER-0008",
            OMRSAuditLogRecordSeverity.EXCEPTION,
            "Event {0} could not be published: {1}",
            "The system is unable to process the event.",
            "Verify the topic configuration."),

    UNEXPECTED_INITIALIZATION_EXCEPTION("OMAS-SECURITY-OFFICER-0009",
            OMRSAuditLogRecordSeverity.EXCEPTION,
            "A {0} exception was caught during start up of service {1} for server {2}. The error message was: {3}",
            "The system detected an unexpected error during start up and is now in an unknown state.",
            "The error message should indicate the cause of the error.  Otherwise look for errors in the " +
                       "remote server's audit log and console to understand and correct the source of the error."),

    EVENT_PROCESSING_ERROR("OMAS-SECURITY-OFFICER-0010",
                           OMRSAuditLogRecordSeverity.EXCEPTION,
                           "The Security Officer Open Metadata Access Service (OMAS) encounter an exception while processing event of type {0}",
                           "The event could not be processed",
                           "Review the exception to determine the source of the error and correct it."),


    OUT_TOPIC_FAILURE("OMAS-OMAS-SECURITY-OFFICER-0014",
                      OMRSAuditLogRecordSeverity.EXCEPTION,
                      "The Security Officer Open Metadata Access Service (OMAS) is unable to send an event on its out topic {0}; exception {1} returned " +
                              "error message: {2}",
                      "The access service detected an error during the start up of the out topic.  Its services are not available for the server.",
                      "Review the error message and any other reported failures to determine the cause of the problem.  Check the status of the event " +
                              "bus.  Once this is resolved, restart the server."),

    OUT_TOPIC_EVENT("OMAS-OMAS-SECURITY-OFFICER-0015",
                    OMRSAuditLogRecordSeverity.EVENT,
                    "The Security Officer Open Metadata Access Service (OMAS) has sent event: {0}",
                    "The access service sends out configuration notifications to ensure connected governance servers have the most up to-date " +
                            "configuration.  This message is to create a record of the events that are being published.",
                    "This event indicates that the configuration for a governance engine, or governance service has changed.  " +
                            "Check that each connected governance server receives this event and updates its configuration if " +
                            "the change affects their operation."),
    ;



    private AuditLogMessageDefinition messageDefinition;

    /**
     * The constructor for SecurityOfficerAuditCode expects to be passed one of the enumeration rows defined in
     * SecurityOfficerAuditCode above.   For example:
     * <p>
     * SecurityOfficerAuditCode   auditCode = SecurityOfficerAuditCode.SERVER_NOT_AVAILABLE;
     * <p>
     * This will expand out to the 5 parameters shown below.
     *
     * @param messageId    - unique Id for the message
     * @param severity     - severity of the message
     * @param message      - text for the message
     * @param systemAction - description of the action taken by the system when the condition happened
     * @param userAction   - instructions for resolving the situation, if any
     */
    SecurityOfficerAuditCode(String                     messageId,
                             OMRSAuditLogRecordSeverity severity,
                             String                     message,
                             String                     systemAction,
                             String                     userAction)
    {
        messageDefinition = new AuditLogMessageDefinition(messageId, severity, message, systemAction, userAction);
    }


    /**
     * Retrieve a message definition object for logging.  This method is used when there are no message inserts.
     *
     * @return message definition object.
     */
    @Override
    public AuditLogMessageDefinition getMessageDefinition()
    {
        return messageDefinition;
    }


    /**
     * Retrieve a message definition object for logging.  This method is used when there are values to be inserted into the message.
     *
     * @param params array of parameters (all strings).  They are inserted into the message according to the numbering in the message text.
     * @return message definition object.
     */
    @Override
    public AuditLogMessageDefinition getMessageDefinition(String... params)
    {
        messageDefinition.setMessageParameters(params);
        return messageDefinition;
    }


    /**
     * JSON-style toString
     *
     * @return string of property names and values for this enum
     */
    @Override
    public String toString()
    {
        return "SecurityOfficerAuditCode{" +
                       "messageDefinition=" + messageDefinition +
                       '}';
    }
}
