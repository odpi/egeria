/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.digitalarchitecture.ffdc;

import org.odpi.openmetadata.frameworks.auditlog.AuditLogRecordSeverityLevel;
import org.odpi.openmetadata.frameworks.auditlog.messagesets.AuditLogMessageDefinition;
import org.odpi.openmetadata.frameworks.auditlog.messagesets.AuditLogMessageSet;


/**
 * The DigitalArchitectureAuditCode is used to define the message content for the OMRS Audit Log.
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
public enum DigitalArchitectureAuditCode implements AuditLogMessageSet
{
    /**
     * OMAS-DIGITAL-ARCHITECTURE-0001 - The Digital Architecture Open Metadata Access Service (OMAS) is initializing a new server instance
     */
    SERVICE_INITIALIZING("OMAS-DIGITAL-ARCHITECTURE-0001",
                         AuditLogRecordSeverityLevel.STARTUP,
                         "The Digital Architecture Open Metadata Access Service (OMAS) is initializing a new server instance",
                         "The local server has started up a new instance of the Digital Architecture OMAS.  " +
                                 "This service supports architects setting up the common definitions for a new project or digital service.",
                         "No action is needed if this service is required.  This is part of the configured operation of the server."),

    /**
     * OMAS-DIGITAL-ARCHITECTURE-0002 - The Digital Architecture Open Metadata Access Service (OMAS) is ready to publish asset notifications to topic {0}
     */
    SERVICE_PUBLISHING("OMAS-DIGITAL-ARCHITECTURE-0002",
                       AuditLogRecordSeverityLevel.STARTUP,
                       "The Digital Architecture Open Metadata Access Service (OMAS) is ready to publish asset notifications to topic {0}",
                       "The local server has started up the event publisher for the Digital Architecture OMAS.  " +
                               "It will begin publishing asset configuration changes to its out topic.",
                       "This is part of the normal start up of the service. Check that there are no errors from the event bus."),


    /**
     * OMAS-DIGITAL-ARCHITECTURE-0003 - The Digital Architecture Open Metadata Access Service (OMAS) has initialized a new instance for server {0}
     */
    SERVICE_INITIALIZED("OMAS-DIGITAL-ARCHITECTURE-0003",
                        AuditLogRecordSeverityLevel.STARTUP,
             "The Digital Architecture Open Metadata Access Service (OMAS) has initialized a new instance for server {0}",
             "The access service has completed initialization of a new instance.  It should have both an inTopic and an outTopic operating" +
                                "as well as a successful registration with the Open Metadata Repository Services (OMRS)",
             "Verify that the service has initialized its topics, it has successfully registered with the repository services and there were no " +
                                "errors reported as the service started."),

    /**
     * OMAS-DIGITAL-ARCHITECTURE-0004 - The Digital Architecture Open Metadata Access Service (OMAS) is shutting down its instance for server {0}
     */
    SERVICE_SHUTDOWN("OMAS-DIGITAL-ARCHITECTURE-0004",
                     AuditLogRecordSeverityLevel.SHUTDOWN,
             "The Digital Architecture Open Metadata Access Service (OMAS) is shutting down its instance for server {0}",
             "The local administrator has requested shut down of an Digital Architecture OMAS instance.",
             "Verify that all resources have been released."),

    /**
     * OMAS-DIGITAL-ARCHITECTURE-0005 - The Digital Architecture Open Metadata Access Service (OMAS) is unable to initialize a new instance; error message is {0}
     */
    SERVICE_INSTANCE_FAILURE("OMAS-DIGITAL-ARCHITECTURE-0005",
                             AuditLogRecordSeverityLevel.EXCEPTION,
             "The Digital Architecture Open Metadata Access Service (OMAS) is unable to initialize a new instance; error message is {0}",
             "The access service detected an error during the start up of a specific server instance.  Its services are not available for the server.",
                             "Review the error message and any other reported failures to determine the cause of the problem.  " +
                                     "Once this is resolved, restart the server."),

    /**
     * OMAS-DIGITAL-ARCHITECTURE-0010 - The Digital Architecture Open Metadata Access Service (OMAS) is no longer publishing events to topic {0}
     */
    PUBLISHING_SHUTDOWN("OMAS-DIGITAL-ARCHITECTURE-0010",
                        AuditLogRecordSeverityLevel.SHUTDOWN,
                        "The Digital Architecture Open Metadata Access Service (OMAS) is no longer publishing events to topic {0}",
                        "The local administrator has requested shut down of an Digital Architecture OMAS instance.  " +
                                "No more configuration events will be published to the named topic.",
                        "This is part of the normal shutdown of the service.   No action is required if this is service" +
                                "shutdown was intentional."),

    /**
     * OMAS-DIGITAL-ARCHITECTURE-0011 - The Digital Architecture Open Metadata Access Service (OMAS) caught an unexpected {0}
     * exception whilst shutting down the out topic {1}. The error message was: {2}
     */
    PUBLISHING_SHUTDOWN_ERROR("OMAS-DIGITAL-ARCHITECTURE-0011",
                              AuditLogRecordSeverityLevel.SHUTDOWN,
                              "The Digital Architecture Open Metadata Access Service (OMAS) caught an unexpected {0} exception whilst shutting down the out " +
                                      "topic {1}. The error message was: {2}",
                              "The local administrator has requested shut down of an Digital Architecture OMAS instance.  " +
                                      "No more configuration events will be published to the named topic, although the connection to the event bus may " +
                                      "not be released properly.",
                              "This is part of the normal shutdown of the service. However, an exception is not expected at this point unless it " +
                                      "is the consequence of a previous error. Review the error message and any other reported failures to " +
                                      "determine if this exception needs special attention."),

    /**
     * OMAS-DIGITAL-ARCHITECTURE-0012 - The Digital Architecture Open Metadata Access Service (OMAS) has sent event of type: {0}
     */
    OUT_TOPIC_EVENT("OMAS-DIGITAL-ARCHITECTURE-0012",
                    AuditLogRecordSeverityLevel.EVENT,
                    "The Digital Architecture Open Metadata Access Service (OMAS) has sent event of type: {0}",
                    "The access service sends out asset notifications to ensure connected tools have the most up to-date " +
                            "knowledge about assets.  This audit log message is to create a record of the events that are being published.",
                    "This event indicates that the metadata for an asset has changed.  This my or may not be significant to " +
                            "the receiving tools."),

    /**
     * OMAS-DIGITAL-ARCHITECTURE-0014 - Event {0} could not be published due to {1} exception with message: {2}
     */
    PROCESS_EVENT_EXCEPTION("OMAS-DIGITAL-ARCHITECTURE-0014",
                            AuditLogRecordSeverityLevel.EXCEPTION,
                            "Event {0} could not be published due to {1} exception with message: {2}",
                            "The system is unable to publish the event to the Digital Architecture OMAS's OutTopic.",
                            "Verify the topic configuration and that the event broker is running."),

    /**
     * OMAS-DIGITAL-ARCHITECTURE-0015 - The Digital Architecture OMAS has received an unexpected {0} exception while formatting a response during method {1}.  The message was: {2}
     */
    UNEXPECTED_CONVERTER_EXCEPTION("OMAS-DIGITAL-ARCHITECTURE-0015",
                         AuditLogRecordSeverityLevel.EXCEPTION,
                         "The Digital Architecture OMAS has received an unexpected {0} exception while formatting a response during method {1}.  The message was: {2}",
                         "The request returns all of the information that it was able to receive.",
                         "Review the stack trace to identify where the error occurred and work to resolve the cause."),
    ;

    private final String                      logMessageId;
    private final AuditLogRecordSeverityLevel severity;
    private final String                      logMessage;
    private final String                      systemAction;
    private final String                      userAction;
    

    /**
     * The constructor for DigitalArchitectureAuditCode expects to be passed one of the enumeration rows defined in
     * DigitalArchitectureAuditCode above.   For example:
     *     DigitalArchitectureAuditCode   auditCode = DigitalArchitectureAuditCode.SERVER_NOT_AVAILABLE;
     * This will expand out to the 5 parameters shown below.
     *
     * @param messageId - unique id for the message
     * @param severity - severity of the message
     * @param message - text for the message
     * @param systemAction - description of the action taken by the system when the condition happened
     * @param userAction - instructions for resolving the situation, if any
     */
    DigitalArchitectureAuditCode(String                      messageId,
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
        return "DigitalArchitectureAuditCode{" +
                       "logMessageId='" + logMessageId + '\'' +
                       ", severity=" + severity +
                       ", logMessage='" + logMessage + '\'' +
                       ", systemAction='" + systemAction + '\'' +
                       ", userAction='" + userAction + '\'' +
                       '}';
    }
}
