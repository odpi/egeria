/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.itinfrastructure.ffdc;

import org.odpi.openmetadata.frameworks.auditlog.messagesets.AuditLogMessageDefinition;
import org.odpi.openmetadata.frameworks.auditlog.messagesets.AuditLogMessageSet;
import org.odpi.openmetadata.repositoryservices.auditlog.OMRSAuditLogRecordSeverity;


/**
 * The ITInfrastructureAuditCode is used to define the message content for the OMRS Audit Log.
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
public enum ITInfrastructureAuditCode implements AuditLogMessageSet
{
    SERVICE_INITIALIZING("OMAS-IT-INFRASTRUCTURE-0001",
             OMRSAuditLogRecordSeverity.STARTUP,
             "The IT Infrastructure Open Metadata Access Service (OMAS) is initializing a new server instance",
             "The local server has started up a new instance of the IT Infrastructure OMAS.  This service " +
                                 "supports the documentation of the IT infrastructure.",
             "No action is needed if this service is required.  This is part of the configured operation of the server."),

    SERVICE_PUBLISHING("OMAS-IT-INFRASTRUCTURE-0002",
                       OMRSAuditLogRecordSeverity.STARTUP,
                       "The IT Infrastructure Open Metadata Access Service (OMAS) is ready to publish notifications to topic {0}",
                       "The local server has started up the event publisher for the IT Infrastructure OMAS.  " +
                               "It will begin publishing events about metadata changes to its out topic.",
                       "This is part of the normal start up of the service. Check that there are no errors from the event bus."),
    
    SERVICE_INITIALIZED("OMAS-IT-INFRASTRUCTURE-0003",
             OMRSAuditLogRecordSeverity.STARTUP,
             "The IT Infrastructure Open Metadata Access Service (OMAS) has initialized a new instance for server {0}",
             "The access service has completed initialization of a new instance.",
             "Verify that there were no errors reported as the service started."),

    SERVICE_SHUTDOWN("OMAS-IT-INFRASTRUCTURE-0004",
             OMRSAuditLogRecordSeverity.SHUTDOWN,
             "The IT Infrastructure Open Metadata Access Service (OMAS) is shutting down its instance for server {0}",
             "The local administrator has requested shut down of an IT Infrastructure OMAS instance.",
             "Verify that all resources have been released."),

    SERVICE_INSTANCE_FAILURE("OMAS-IT-INFRASTRUCTURE-0005",
             OMRSAuditLogRecordSeverity.EXCEPTION,
             "The IT Infrastructure Open Metadata Access Service (OMAS) is unable to initialize a new instance; error message is {0}",
             "The access service detected an error during the start up of a specific server instance.  Its services are not available for the server.",
             "Review the error message and any other reported failures to determine the cause of the problem.  Once this is resolved, restart the server."),

    PUBLISHING_SHUTDOWN("OMAS-IT-INFRASTRUCTURE-0007",
                        OMRSAuditLogRecordSeverity.SHUTDOWN,
                        "The IT Infrastructure Open Metadata Access Service (OMAS) is no longer publishing events to topic {0}",
                        "The local administrator has requested shut down of an IT Infrastructure OMAS instance.  " +
                                "No more events will be published to the named topic.",
                        "This is part of the normal shutdown of the service.   No action is required if this is service" +
                                "shutdown was intentional."),

    PUBLISHING_SHUTDOWN_ERROR("OMAS-IT-INFRASTRUCTURE-0008",
                              OMRSAuditLogRecordSeverity.SHUTDOWN,
                              "The IT Infrastructure Open Metadata Access Service (OMAS) caught an unexpected {0} exception whilst shutting down the out " +
                                      "topic {1}. The error message was: {2}",
                              "The local administrator has requested shut down of an IT Infrastructure OMAS instance.  " +
                                      "No more events will be published to the named topic, although the connection to the event bus may " +
                                      "not be released properly.",
                              "This is part of the normal shutdown of the service. However, an exception is not expected at this point unless it " +
                                      "is the consequence of a previous error. Review the error message and any other reported failures to " +
                                      "determine if this exception needs special attention."),

    OUT_TOPIC_EVENT("OMAS-IT-INFRASTRUCTURE-0013",
                    OMRSAuditLogRecordSeverity.EVENT,
                    "The IT Infrastructure Open Metadata Access Service (OMAS) has sent event of type: {0}",
                    "The access service sends out notifications about changes to metadata.  This message is to create a record of the events that are being published.",
                    "This event indicates that relevant metadata has changed."),

    PROCESS_EVENT_EXCEPTION("OMAS-IT-INFRASTRUCTURE-0014",
                            OMRSAuditLogRecordSeverity.EXCEPTION,
                            "Event {0} could not be published due to {1} exception with message: {2}",
                            "The system is unable to publish the event to the IT Infrastructure OMAS's OutTopic.",
                            "Verify the topic configuration and that the event broker is running."),

    PARSE_EVENT_EXCEPTION("OMAS-IT-INFRASTRUCTURE-0015",
                          OMRSAuditLogRecordSeverity.EXCEPTION,
                          "Inbound event {0} could not be parsed. IOException included the following message: {1}",
                          "The system is unable to process the event received on the IT Infrastructure OMAS in topic.",
                          "Verify the content and structure of the in topic event.  Check that it includes the correct class name key"),


    ;

    private final AuditLogMessageDefinition messageDefinition;


    /**
     * The constructor for ITInfrastructureAuditCode expects to be passed one of the enumeration rows defined in
     * ITInfrastructureAuditCode above.   For example:
     *
     *     ITInfrastructureAuditCode   auditCode = ITInfrastructureAuditCode.SERVER_NOT_AVAILABLE;
     *
     * This will expand out to the 4 parameters shown below.
     *
     * @param messageId - unique Id for the message
     * @param severity - severity of the message
     * @param message - text for the message
     * @param systemAction - description of the action taken by the system when the condition happened
     * @param userAction - instructions for resolving the situation, if any
     */
    ITInfrastructureAuditCode(String                     messageId,
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
