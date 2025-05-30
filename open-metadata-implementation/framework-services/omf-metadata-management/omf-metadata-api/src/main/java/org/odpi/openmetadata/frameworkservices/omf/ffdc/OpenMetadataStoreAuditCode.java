/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworkservices.omf.ffdc;

import org.odpi.openmetadata.frameworks.auditlog.AuditLogRecordSeverityLevel;
import org.odpi.openmetadata.frameworks.auditlog.messagesets.AuditLogMessageDefinition;
import org.odpi.openmetadata.frameworks.auditlog.messagesets.AuditLogMessageSet;

/**
 * The OpenMetadataStoreAuditCode is used to define the message content for the OMRS Audit Log.
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
public enum OpenMetadataStoreAuditCode implements AuditLogMessageSet
{
    /**
     * OPEN-METADATA-STORE-0001 - The Open Metadata Store Services are initializing a new server instance
     */
    SERVICE_INITIALIZING("OPEN-METADATA-STORE-0001",
                         AuditLogRecordSeverityLevel.STARTUP,
                         "The Open Metadata Store Services are initializing a new server instance",
                         "The local server has started up a new instance of the Open Metadata Store Services.  " +
                                 "It will support open metadata store REST requests.",
                         "This is part of the normal start up of the service.  No action is required if this service " +
                                 "startup was intentional."),

    /**
     * OPEN-METADATA-STORE-0005 - The Open Metadata Store Services has initialized a new instance for server {0}
     */
    SERVICE_INITIALIZED("OPEN-METADATA-STORE-0005",
                        AuditLogRecordSeverityLevel.STARTUP,
                        "The Open Metadata Store Services has initialized a new instance for server {0}",
                        "The Open Metadata Store Services has completed initialization of a new server instance.",
                        "Verify that there are no error messages logged by the service.  If there are none it means that " +
                                "all parts of the service initialized successfully."),

    /**
     * OPEN-METADATA-STORE-0006 - The Open Metadata Store Services are unable to initialize a new instance; error message is {0}
     */
    SERVICE_INSTANCE_FAILURE("OPEN-METADATA-STORE-0006",
                             AuditLogRecordSeverityLevel.ERROR,
                             "The Open Metadata Store Services are unable to initialize a new instance; error message is {0}",
                             "The service detected an error during the start up of a specific server instance.  Its services are not available for the server.",
                             "Review the error message and any other reported failures to determine the cause of the problem.  Once this is resolved, restart the server."),

    /**
     * OPEN-METADATA-STORE-0008 - The Open Metadata Store Services detected an unexpected {0} exception during the initialization of its services;
     * error message is {1}
     */
    UNEXPECTED_INITIALIZATION_EXCEPTION("OPEN-METADATA-STORE-0008",
                                        AuditLogRecordSeverityLevel.EXCEPTION,
                                        "The Open Metadata Store Services detected an unexpected {0} exception during the " +
                                                "initialization of its services; error message is {1}",
                                        "The service detected an error during the start up of a specific server instance.  Its services are not available " +
                                                "for the server and an error is returned to the caller.",
                                        "Review the error message and any other reported failures to determine the cause of the problem.  In particular consider the" +
                                                " state of the Event Bus.  Once this is resolved, restart the server."),

    /**
     * OPEN-METADATA-STORE-0009 - The Open Metadata Store Services are shutting down server instance {0}
     */
    SERVICE_TERMINATING("OPEN-METADATA-STORE-0009",
                        AuditLogRecordSeverityLevel.SHUTDOWN,
                        "The Open Metadata Store Services are shutting down server instance {0}",
                        "The local handlers has requested shut down of the Open Metadata Store Services.",
                        "No action is required.  This is part of the normal operation of the service."),

    /**
     * OPEN-METADATA-STORE-0012 - The Open Metadata Store Services are shutting down its instance for server {0}
     */
    SERVICE_SHUTDOWN("OPEN-METADATA-STORE-0012",
                     AuditLogRecordSeverityLevel.SHUTDOWN,
                     "The Open Metadata Store Services are shutting down its instance for server {0}",
                     "The local administrator has requested shut down of an Open Metadata Store Services instance.  " +
                             "The open metadata store interfaces are no longer available and no configuration events will " +
                             "be published to the out topic",
                     "This is part of the normal shutdown of the service.  Verify that all resources have been released."),

    /**
     * OMES-GOVERNANCE-ACTION-0015 - The Governance Action Framework received an {0} exception from the {1} governance action service while it was processing a watchdog event of type {2}; error message is {3}
     */
    WATCHDOG_EVENT_FAILURE("OMES-GOVERNANCE-ACTION-0015",
                           AuditLogRecordSeverityLevel.ERROR,
                           "The Governance Action Framework received an {0} exception from the {1} governance action service while it was processing a watchdog event of type {2}; error message is {3}",
                           "The engine services detected an error while processing a watchdog event.",
                           "Review the error message and any other reported failures to determine the cause of the problem.  It may also be necessary to initiate the action that did not occur due to the failure to process this event."),
    /**
     * OPEN-METADATA-STORE-0020 - Log message for asset {0} from governance service {1}: {2}
     */
    ASSET_AUDIT_LOG("OPEN-METADATA-STORE-0020",
                    AuditLogRecordSeverityLevel.INFO,
                    "Log message for asset {0} from governance service {1}: {2}",
                    "A governance service has logged a message about an asset.",
                    "Review the message to ensure no action is required."),

    /**
     * OPEN-METADATA-STORE-0021 - Failed to publish watchdog event to Watchdog Governance Action Service for governance engine {0}.
     * The exception was {1} with error message {2}
     */
    WATCHDOG_LISTENER_EXCEPTION("OPEN-METADATA-STORE-0021",
                                AuditLogRecordSeverityLevel.ERROR,
                                "Failed to publish watchdog event to Watchdog Governance Action Service for governance engine {0}.  The exception was {1} with error message {2}",
                                "An open watchdog governance action service has raised an exception while processing an incoming " +
                                        "watchdog event.  The exception explains the reason.",
                                "Review the error messages and resolve the cause of the problem if needed."),


    /**
     * OPEN-METADATA-STORE-0025 - The Design Model OMAS has received an unexpected {0} exception while formatting a response during method {1}.  The message was: {2}
     */
    UNEXPECTED_CONVERTER_EXCEPTION("OPEN-METADATA-STORE-0025",
                                   AuditLogRecordSeverityLevel.EXCEPTION,
                                   "The Open Metadata Store has received an unexpected {0} exception while formatting a response during method {1} for service {2}.  The message was: {3}",
                                   "The request returns all of the information that it was able to receive.",
                                   "Review the stack trace to identify where the error occurred and work to resolve the cause."),

    ;


    private final String                      logMessageId;
    private final AuditLogRecordSeverityLevel severity;
    private final String                      logMessage;
    private final String                      systemAction;
    private final String                      userAction;


    /**
     * The constructor for OpenMetadataStoreAuditCode expects to be passed one of the enumeration rows defined in
     * OpenMetadataStoreAuditCode above.   For example:
     * <p>
     * OpenMetadataStoreAuditCode   auditCode = OpenMetadataStoreAuditCode.SERVER_NOT_AVAILABLE;
     * <p>
     * This will expand out to the 5 parameters shown below.
     *
     * @param messageId    - unique id for the message
     * @param severity     - severity of the message
     * @param message      - text for the message
     * @param systemAction - description of the action taken by the system when the condition happened
     * @param userAction   - instructions for resolving the situation, if any
     */
    OpenMetadataStoreAuditCode(String                      messageId,
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
