/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworkservices.omf.ffdc;

import org.odpi.openmetadata.frameworks.auditlog.AuditLogRecordSeverityLevel;
import org.odpi.openmetadata.frameworks.auditlog.messagesets.AuditLogMessageDefinition;
import org.odpi.openmetadata.frameworks.auditlog.messagesets.AuditLogMessageSet;

/**
 * The OMFServicesAuditCode is used to define the message content for the OMRS Audit Log.
 * <p>
 * The 5 fields in the enum are:
 * <ul>
 * <li>Log Message Identifier - to uniquely identify the message</li>
 * <li>Severity - is this an event, decision, action, error or exception</li>
 * <li>Log Message Text - includes placeholder to allow additional values to be captured</li>
 * <li>Additional Information - further parameters and data relating to the audit message (optional)</li>
 * <li>SystemAction - describes the result of the situation</li>
 * <li>UserAction - describes how a user should correct the situation</li>
 * </ul>
 */
public enum OMFServicesAuditCode implements AuditLogMessageSet
{
    /**
     * OMF-SERVICES-0001 - The Open Metadata Store Services are initializing a new server instance
     */
    SERVICE_INITIALIZING("OMF-SERVICES-0001",
                         AuditLogRecordSeverityLevel.STARTUP,
                         "The Open Metadata Store Services are initializing a new server instance",
                         "The local server has started up a new instance of the Open Metadata Store Services.  " +
                                 "It will support open metadata store REST requests.",
                         "This is part of the normal start up of the service.  No action is required if this service " +
                                 "startup was intentional."),

    /**
     * OMF-SERVICES-0002 The Open Metadata Store is ready to publish asset manager notifications to topic {0}
     */
    SERVICE_PUBLISHING("OMF-SERVICES-0002",
                       AuditLogRecordSeverityLevel.STARTUP,
                       "The Open Metadata Store is ready to publish asset manager notifications to topic {0}",
                       "The local server has started up the event publisher for the OMF Services.  " +
                               "It will begin publishing asset manager metadata changes to its out topic.",
                       "This is part of the normal start up of the service. Check that there are no errors from the event bus."),

    /**
     * OMF-SERVICES-0005 - The Open Metadata Store Services has initialized a new instance for server {0}
     */
    SERVICE_INITIALIZED("OMF-SERVICES-0005",
                        AuditLogRecordSeverityLevel.STARTUP,
                        "The Open Metadata Store Services has initialized a new instance for server {0}",
                        "The Open Metadata Store Services has completed initialization of a new server instance.",
                        "Verify that there are no error messages logged by the service.  If there are none it means that " +
                                "all parts of the service initialized successfully."),
    
    /**
     * OMF-SERVICES-0006 - The Open Metadata Store Services are unable to initialize a new instance; error message is {0}
     */
    SERVICE_INSTANCE_FAILURE("OMF-SERVICES-0006",
                             AuditLogRecordSeverityLevel.ERROR,
                             "The Open Metadata Store Services are unable to initialize a new instance; error message is {0}",
                             "The service detected an error during the start up of a specific server instance.  Its services are not available for the server.",
                             "Review the error message and any other reported failures to determine the cause of the problem.  Once this is resolved, restart the server."),

    
    /**
     * OMF-SERVICES-0007 The Open Metadata Store is no longer publishing events to topic {0}
     */
    PUBLISHING_SHUTDOWN("OMF-SERVICES-0007",
                        AuditLogRecordSeverityLevel.SHUTDOWN,
                        "The Open Metadata Store is no longer publishing events to topic {0}",
                        "The local administrator has requested shut down of an OMF Services instance.  " +
                                "No more events will be published to the named topic.",
                        "This is part of the normal shutdown of the service.   No action is required if this is service" +
                                "shutdown was intentional."),

    /**
     * OMF-SERVICES-0008 The Open Metadata Store caught an unexpected {0} exception whilst shutting down the out
     * topic {1}. The error message was: {2}
     */
    PUBLISHING_SHUTDOWN_ERROR("OMF-SERVICES-0008",
                              AuditLogRecordSeverityLevel.SHUTDOWN,
                              "The Open Metadata Store caught an unexpected {0} exception whilst shutting down the out " +
                                      "topic {1}. The error message was: {2}",
                              "The local administrator has requested shut down of an OMF Services instance.  " +
                                      "No more events will be published to the named topic, although the connection to the event bus may " +
                                      "not be released properly.",
                              "This is part of the normal shutdown of the service. However, an exception is not expected at this point unless it " +
                                      "is the consequence of a previous error. Review the error message and any other reported failures to " +
                                      "determine if this exception needs special attention."),

    
    /**
     * OMF-SERVICES-0008 - The Open Metadata Store Services detected an unexpected {0} exception during the initialization of its services;
     * error message is {1}
     */
    UNEXPECTED_INITIALIZATION_EXCEPTION("OMF-SERVICES-0008",
                                        AuditLogRecordSeverityLevel.EXCEPTION,
                                        "The Open Metadata Store Services detected an unexpected {0} exception during the " +
                                                "initialization of its services; error message is {1}",
                                        "The service detected an error during the start up of a specific server instance.  Its services are not available " +
                                                "for the server and an error is returned to the caller.",
                                        "Review the error message and any other reported failures to determine the cause of the problem.  In particular consider the" +
                                                " state of the Event Bus.  Once this is resolved, restart the server."),

    /**
     * OMF-SERVICES-0009 - The Open Metadata Store Services are shutting down server instance {0}
     */
    SERVICE_TERMINATING("OMF-SERVICES-0009",
                        AuditLogRecordSeverityLevel.SHUTDOWN,
                        "The Open Metadata Store Services are shutting down server instance {0}",
                        "The local handlers has requested shut down of the Open Metadata Store Services.",
                        "No action is required.  This is part of the normal operation of the service."),

    /**
     * OMF-SERVICES-0012 - The Open Metadata Store Services are shutting down its instance for server {0}
     */
    SERVICE_SHUTDOWN("OMF-SERVICES-0012",
                     AuditLogRecordSeverityLevel.SHUTDOWN,
                     "The Open Metadata Store Services are shutting down its instance for server {0}",
                     "The local administrator has requested shut down of an Open Metadata Store Services instance.  " +
                             "The open metadata store interfaces are no longer available and no configuration events will " +
                             "be published to the out topic",
                     "This is part of the normal shutdown of the service.  Verify that all resources have been released."),


    /**
     * OMF-SERVICES-0013 The OMF Services has sent event of type: {0}
     */
    OUT_TOPIC_EVENT("OMF-SERVICES-0013",
                    AuditLogRecordSeverityLevel.EVENT,
                    "The OMF Services has sent event of type: {0}",
                    "The service sends out notifications about changes to open metadata.  This message is to create a record of the events that are being published.",
                    "This event indicates that one of the open metadata elements, relationships or classifications has changed."),

    /**
     * OMF-SERVICES-0014 Event {0} could not be published due to {1} exception with message: {2}
     */
    PROCESS_EVENT_EXCEPTION("OMF-SERVICES-0014",
                            AuditLogRecordSeverityLevel.EXCEPTION,
                            "Event {0} could not be published due to {1} exception with message: {2}",
                            "The system is unable to publish the event to the OMF Services' OutTopic.",
                            "Verify the topic configuration and that the event broker is running."),

    /**
     * OMF-SERVICES-0015 Inbound event {0} could not be parsed. IOException included the following message: {1}
     */
    PARSE_EVENT_EXCEPTION("OMF-SERVICES-0015",
                          AuditLogRecordSeverityLevel.EXCEPTION,
                          "Inbound event {0} could not be parsed. IOException included the following message: {1}",
                          "The system is unable to process the event received on the OMF Services in topic.",
                          "Verify the content and structure of the in topic event.  Check that it includes the correct class name key"),
    
    /**
     * OMF-SERVICES-0020 - Log message for asset {0} from governance service {1}: {2}
     */
    ASSET_AUDIT_LOG("OMF-SERVICES-0020",
                    AuditLogRecordSeverityLevel.INFO,
                    "Log message for asset {0} from governance service {1}: {2}",
                    "A governance service has logged a message about an asset.",
                    "Review the message to ensure no action is required."),



    ;


    private final String                      logMessageId;
    private final AuditLogRecordSeverityLevel severity;
    private final String                      logMessage;
    private final String                      systemAction;
    private final String                      userAction;


    /**
     * The constructor for OMFServicesAuditCode expects to be passed one of the enumeration rows defined in
     * OMFServicesAuditCode above.   For example:
     * <p>
     * OMFServicesAuditCode   auditCode = OMFServicesAuditCode.SERVER_NOT_AVAILABLE;
     * <p>
     * This will expand out to the 5 parameters shown below.
     *
     * @param messageId    - unique id for the message
     * @param severity     - severity of the message
     * @param message      - text for the message
     * @param systemAction - description of the action taken by the system when the condition happened
     * @param userAction   - instructions for resolving the situation, if any
     */
    OMFServicesAuditCode(String                      messageId,
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
        return "AuditCode{" +
                       "logMessageId='" + logMessageId + '\'' +
                       ", severity=" + severity +
                       ", logMessage='" + logMessage + '\'' +
                       ", systemAction='" + systemAction + '\'' +
                       ", userAction='" + userAction + '\'' +
                       '}';
    }
}
