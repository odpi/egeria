/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.stewardshipaction.ffdc;

import org.odpi.openmetadata.frameworks.auditlog.AuditLogRecordSeverityLevel;
import org.odpi.openmetadata.frameworks.auditlog.messagesets.AuditLogMessageDefinition;
import org.odpi.openmetadata.frameworks.auditlog.messagesets.AuditLogMessageSet;

/**
 * The StewardshipActionAuditCode is used to define the message content for the OMRS Audit Log.
 * The 5 fields in the enum are:
 * <ul>
 *     <li>Log Message Id - to uniquely identify the message</li>
 *     <li>Severity - is this an event, decision, action, error or exception</li>
 *     <li>Log Message Text - includes placeholder to allow additional values to be captured</li>
 *     <li>Additional Information - further parameters and data relating to the audit message (optional)</li>
 *     <li>SystemAction - describes the result of the situation</li>
 *     <li>UserAction - describes how a user should correct the situation</li>
 * </ul>
 */
public enum StewardshipActionAuditCode implements AuditLogMessageSet
{
    /**
     * OMAS-STEWARDSHIP-ACTION-0001 - The Stewardship Action Open Metadata Access Service (OMAS) is initializing a new server instance
     */
    SERVICE_INITIALIZING("OMAS-STEWARDSHIP-ACTION-0001",
                         AuditLogRecordSeverityLevel.STARTUP,
                         "The Stewardship Action Open Metadata Access Service (OMAS) is initializing a new server instance",
                         "The local server has started up a new instance of the Stewardship Action OMAS.",
                         "No action is needed if this service is required.  This is part of the configured operation of the server"),

    /**
     * OMAS-STEWARDSHIP-ACTION-0002 - The Stewardship Action Open Metadata Access Service (OMAS) is ready to publish asset notifications to topic {0}
     */
    SERVICE_PUBLISHING("OMAS-STEWARDSHIP-ACTION-0002",
                       AuditLogRecordSeverityLevel.STARTUP,
                       "The Stewardship Action Open Metadata Access Service (OMAS) is ready to publish asset notifications to topic {0}",
                       "The local server has started up the event publisher for the Stewardship Action OMAS.  " +
                               "It will begin publishing asset configuration changes to its out topic.",
                       "This is part of the normal start up of the service. Check that there are no errors from the event bus."),

    /**
     * OMAS-STEWARDSHIP-ACTION-0003 - The Stewardship Action Open Metadata Access Service (OMAS) has initialized a new instance for server {0}
     */
    SERVICE_INITIALIZED("OMAS-STEWARDSHIP-ACTION-0003",
                        AuditLogRecordSeverityLevel.STARTUP,
                        "The Stewardship Action Open Metadata Access Service (OMAS) has initialized a new instance for server {0}",
                        "The access service has completed initialization of a new instance.",
                        "Verify that there were no errors reported as the service started."),

    /**
     * OMAS-STEWARDSHIP-ACTION-0004 - The Stewardship Action Open Metadata Access Service (OMAS) is shutting down its instance for server {0}
     */
    SERVICE_SHUTDOWN("OMAS-STEWARDSHIP-ACTION-0004",
                     AuditLogRecordSeverityLevel.SHUTDOWN,
                     "The Stewardship Action Open Metadata Access Service (OMAS) is shutting down its instance for server {0}",
                     "The local administrator has requested shut down of an Stewardship Action OMAS instance.",
                     "Verify that all resources have been released."),


    /**
     * OMAS-STEWARDSHIP-ACTION-0005 - The Stewardship Action Open Metadata Access Service (OMAS) is unable to initialize a new instance; error message is {0}
     */
    SERVICE_INSTANCE_FAILURE("OMAS-STEWARDSHIP-ACTION-0005",
                             AuditLogRecordSeverityLevel.EXCEPTION,
                             "The Stewardship Action Open Metadata Access Service (OMAS) is unable to initialize a new instance; error message is {0}",
                             "The access service detected an error during the start up of a specific server instance.  Its services are not available for the server.",
                             "Review the error message and any other reported failures to determine the cause of the problem.  Once this is resolved, restart the server."),

    /**
     * OMAS-STEWARDSHIP-ACTION-0010 - The Stewardship Action Open Metadata Access Service (OMAS) is no longer publishing events to topic {0}
     */
    PUBLISHING_SHUTDOWN("OMAS-STEWARDSHIP-ACTION-0010",
                        AuditLogRecordSeverityLevel.SHUTDOWN,
                        "The Stewardship Action Open Metadata Access Service (OMAS) is no longer publishing events to topic {0}",
                        "The local administrator has requested shut down of an Stewardship Action OMAS instance.  " +
                                "No more configuration events will be published to the named topic.",
                        "This is part of the normal shutdown of the service.   No action is required if this is service" +
                                "shutdown was intentional."),

    /**
     * OMAS-STEWARDSHIP-ACTION-0011 - The Stewardship Action Open Metadata Access Service (OMAS) caught an unexpected {0} exception whilst 
     * shutting down the out topic {1}. The error message was: {2}
     */
    PUBLISHING_SHUTDOWN_ERROR("OMAS-STEWARDSHIP-ACTION-0011",
                              AuditLogRecordSeverityLevel.SHUTDOWN,
                              "The Stewardship Action Open Metadata Access Service (OMAS) caught an unexpected {0} exception whilst shutting down the out " +
                                      "topic {1}. The error message was: {2}",
                              "The local administrator has requested shut down of an Stewardship Action OMAS instance.  " +
                                      "No more configuration events will be published to the named topic, although the connection to the event bus may " +
                                      "not be released properly.",
                              "This is part of the normal shutdown of the service. However, an exception is not expected at this point unless it " +
                                      "is the consequence of a previous error. Review the error message and any other reported failures to " +
                                      "determine if this exception needs special attention."),

    /**
     * OMAS-STEWARDSHIP-ACTION-0012 - The Stewardship Action Open Metadata Access Service (OMAS) has sent event of type: {0}
     */
    OUT_TOPIC_EVENT("OMAS-STEWARDSHIP-ACTION-0012",
                    AuditLogRecordSeverityLevel.EVENT,
                    "The Stewardship Action Open Metadata Access Service (OMAS) has sent event of type: {0}",
                    "The access service sends out asset notifications to ensure connected tools have the most up to-date " +
                            "knowledge about assets.  This audit log message is to create a record of the events that are being published.",
                    "This event indicates that the metadata for an asset has changed.  This my or may not be significant to " +
                            "the receiving tools."),

    /**
     * OMAS-STEWARDSHIP-ACTION-0014 - Event {0} could not be published due to {1} exception with message: {2}
     */
    PROCESS_EVENT_EXCEPTION("OMAS-STEWARDSHIP-ACTION-0014",
                            AuditLogRecordSeverityLevel.EXCEPTION,
                            "Event {0} could not be published due to {1} exception with message: {2}",
                            "The system is unable to publish the event to the Stewardship Action OMAS's OutTopic.",
                            "Verify the topic configuration and that the event broker is running."),

    /**
     * OMAS-STEWARDSHIP-ACTION-0018 - The Stewardship Action Open Metadata Access Service (OMAS) is unable to send an event on its out topic {0}; 
     * exception {1} returned error message: {2}
     */
    OUT_TOPIC_FAILURE("OMAS-STEWARDSHIP-ACTION-0018",
                      AuditLogRecordSeverityLevel.EXCEPTION,
                      "The Stewardship Action Open Metadata Access Service (OMAS) is unable to send an event on its out topic {0}; exception {1} returned " +
                              "error message: {2}",
                      "The access service detected an error during the start up of the out topic.  Its services are not available for the server.",
                      "Review the error message and any other reported failures to determine the cause of the problem.  Check the status of the event " +
                              "bus.  Once this is resolved, restart the server."),
    ;


    private final String                      logMessageId;
    private final AuditLogRecordSeverityLevel severity;
    private final String                      logMessage;
    private final String                      systemAction;
    private final String                      userAction;



    /**
     * The constructor for StewardshipActionAuditCode expects to be passed one of the enumeration rows defined in
     * StewardshipActionAuditCode above.   For example:
     *     StewardshipActionAuditCode   auditCode = StewardshipActionAuditCode.SERVER_NOT_AVAILABLE;
     * This will expand out to the 5 parameters shown below.
     *
     * @param messageId - unique id for the message
     * @param severity - severity of the message
     * @param message - text for the message
     * @param systemAction - description of the action taken by the system when the condition happened
     * @param userAction - instructions for resolving the situation, if any
     */
    StewardshipActionAuditCode(String                      messageId,
                               AuditLogRecordSeverityLevel severity,
                               String                      message,
                               String                      systemAction,
                               String                      userAction)
    {
        this.logMessageId = messageId;
        this.severity = severity;
        this.logMessage = message;
        this.systemAction = systemAction;
        this.userAction = userAction;
    }


    /**
     * Retrieve a message definition object for logging.  This method is used when there are no message inserts.
     *
     * @return message definition object.
     */
    @Override
    public AuditLogMessageDefinition getMessageDefinition()
    {
        return new AuditLogMessageDefinition(logMessageId,
                                             severity,
                                             logMessage,
                                             systemAction,
                                             userAction);
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
        AuditLogMessageDefinition messageDefinition = new AuditLogMessageDefinition(logMessageId,
                                                                                    severity,
                                                                                    logMessage,
                                                                                    systemAction,
                                                                                    userAction);
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
        return "StewardshipActionAuditCode{" +
                       "logMessageId='" + logMessageId + '\'' +
                       ", severity=" + severity +
                       ", logMessage='" + logMessage + '\'' +
                       ", systemAction='" + systemAction + '\'' +
                       ", userAction='" + userAction + '\'' +
                       '}';
    }
}
