/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.digitalarchitecture.ffdc;

import org.odpi.openmetadata.frameworks.auditlog.messagesets.AuditLogMessageDefinition;
import org.odpi.openmetadata.frameworks.auditlog.messagesets.AuditLogMessageSet;
import org.odpi.openmetadata.repositoryservices.auditlog.OMRSAuditLogRecordSeverity;


/**
 * The DigitalArchitectureAuditCode is used to define the message content for the OMRS Audit Log.
 *
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
public enum DigitalArchitectureAuditCode implements AuditLogMessageSet
{
    SERVICE_INITIALIZING("OMAS-DIGITAL-ARCHITECTURE-0001",
             OMRSAuditLogRecordSeverity.STARTUP,
             "The Digital Architecture Open Metadata Access Service (OMAS) is initializing a new server instance",
             "The local server has started up a new instance of the Digital Architecture OMAS.  " +
                                 "This service supports architects setting up the common definitions for a new project or digital service.",
             "No action is needed if this service is required.  This is part of the configured operation of the server."),

    SERVICE_PUBLISHING("OMAS-DIGITAL-ARCHITECTURE-0002",
                       OMRSAuditLogRecordSeverity.STARTUP,
                       "The Digital Architecture Open Metadata Access Service (OMAS) is ready to publish asset notifications to topic {0}",
                       "The local server has started up the event publisher for the Digital Architecture OMAS.  " +
                               "It will begin publishing asset configuration changes to its out topic.",
                       "This is part of the normal start up of the service. Check that there are no errors from the event bus."),


    SERVICE_INITIALIZED("OMAS-DIGITAL-ARCHITECTURE-0003",
             OMRSAuditLogRecordSeverity.STARTUP,
             "The Digital Architecture Open Metadata Access Service (OMAS) has initialized a new instance for server {0}",
             "The access service has completed initialization of a new instance.  It should have both an inTopic and an outTopic operating" +
                                "as well as a successful registration with the Open Metadata Repository Services (OMRS)",
             "Verify that the service has initialized its topics, it has successfully registered with the repository services and there were no " +
                                "errors reported as the service started."),

    SERVICE_SHUTDOWN("OMAS-DIGITAL-ARCHITECTURE-0003",
             OMRSAuditLogRecordSeverity.SHUTDOWN,
             "The Digital Architecture Open Metadata Access Service (OMAS) is shutting down its instance for server {0}",
             "The local administrator has requested shut down of an Digital Architecture OMAS instance.",
             "Verify that all resources have been released."),

    SERVICE_INSTANCE_FAILURE("OMAS-DIGITAL-ARCHITECTURE-0004",
             OMRSAuditLogRecordSeverity.EXCEPTION,
             "The Digital Architecture Open Metadata Access Service (OMAS) is unable to initialize a new instance; error message is {0}",
             "The access service detected an error during the start up of a specific server instance.  Its services are not available for the server.",
                             "Review the error message and any other reported failures to determine the cause of the problem.  " +
                                     "Once this is resolved, restart the server."),

    PUBLISHING_SHUTDOWN("OMAS-DIGITAL-ARCHITECTURE-0010",
                        OMRSAuditLogRecordSeverity.SHUTDOWN,
                        "The Digital Architecture Open Metadata Access Service (OMAS) is no longer publishing events to topic {0}",
                        "The local administrator has requested shut down of an Digital Architecture OMAS instance.  " +
                                "No more configuration events will be published to the named topic.",
                        "This is part of the normal shutdown of the service.   No action is required if this is service" +
                                "shutdown was intentional."),

    PUBLISHING_SHUTDOWN_ERROR("OMAS-DIGITAL-ARCHITECTURE-0011",
                              OMRSAuditLogRecordSeverity.SHUTDOWN,
                              "The Digital Architecture Open Metadata Access Service (OMAS) caught an unexpected {0} exception whilst shutting down the out " +
                                      "topic {1}. The error message was: {2}",
                              "The local administrator has requested shut down of an Digital Architecture OMAS instance.  " +
                                      "No more configuration events will be published to the named topic, although the connection to the event bus may " +
                                      "not be released properly.",
                              "This is part of the normal shutdown of the service. However, an exception is not expected at this point unless it " +
                                      "is the consequence of a previous error. Review the error message and any other reported failures to " +
                                      "determine if this exception needs special attention."),

    OUT_TOPIC_EVENT("OMAS-DIGITAL-ARCHITECTURE-0012",
                    OMRSAuditLogRecordSeverity.EVENT,
                    "The Digital Architecture Open Metadata Access Service (OMAS) has sent event of type: {0}",
                    "The access service sends out asset notifications to ensure connected tools have the most up to-date " +
                            "knowledge about assets.  This audit log message is to create a record of the events that are being published.",
                    "This event indicates that the metadata for an asset has changed.  This my or may not be significant to " +
                            "the receiving tools."),

    PROCESS_EVENT_EXCEPTION("OMAS-DIGITAL-ARCHITECTURE-0014",
                            OMRSAuditLogRecordSeverity.EXCEPTION,
                            "Event {0} could not be published due to {1} exception with message: {2}",
                            "The system is unable to publish the event to the Digital Architecture OMAS's OutTopic.",
                            "Verify the topic configuration and that the event broker is running."),

    OUT_TOPIC_FAILURE("OMAS-DIGITAL-ARCHITECTURE-0018",
                      OMRSAuditLogRecordSeverity.EXCEPTION,
                      "The Digital Architecture Open Metadata Access Service (OMAS) is unable to send an event on its out topic {0}; exception {1} returned " +
                              "error message: {2}",
                      "The access service detected an error during the start up of the out topic.  Its services are not available for the server.",
                      "Review the error message and any other reported failures to determine the cause of the problem.  Check the status of the event " +
                              "bus.  Once this is resolved, restart the server."),

    ;

    private final AuditLogMessageDefinition messageDefinition;


    /**
     * The constructor for DigitalArchitectureAuditCode expects to be passed one of the enumeration rows defined in
     * DigitalArchitectureAuditCode above.   For example:
     *
     *     DigitalArchitectureAuditCode   auditCode = DigitalArchitectureAuditCode.SERVER_NOT_AVAILABLE;
     *
     * This will expand out to the 4 parameters shown below.
     *
     * @param messageId - unique Id for the message
     * @param severity - severity of the message
     * @param message - text for the message
     * @param systemAction - description of the action taken by the system when the condition happened
     * @param userAction - instructions for resolving the situation, if any
     */
    DigitalArchitectureAuditCode(String                     messageId,
                                 OMRSAuditLogRecordSeverity severity,
                                 String                     message,
                                 String                     systemAction,
                                 String                     userAction)
    {
        messageDefinition = new AuditLogMessageDefinition(messageId,
                                                          severity,
                                                          message,
                                                          systemAction,
                                                          userAction);
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
    public AuditLogMessageDefinition getMessageDefinition(String ...params)
    {
        messageDefinition.setMessageParameters(params);
        return messageDefinition;
    }
}
