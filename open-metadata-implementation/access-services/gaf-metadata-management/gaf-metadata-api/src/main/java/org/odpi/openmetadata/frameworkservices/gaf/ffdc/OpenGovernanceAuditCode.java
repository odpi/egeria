/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworkservices.gaf.ffdc;

import org.odpi.openmetadata.frameworks.auditlog.AuditLogRecordSeverityLevel;
import org.odpi.openmetadata.frameworks.auditlog.messagesets.AuditLogMessageDefinition;
import org.odpi.openmetadata.frameworks.auditlog.messagesets.AuditLogMessageSet;

/**
 * The OpenGovernanceAuditCode is used to define the message content for the OMRS Audit Log.
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
public enum OpenGovernanceAuditCode implements AuditLogMessageSet
{
    /**
     * OPEN-GOVERNANCE-0001 - The Open Metadata Store Services are initializing a new server instance
     */
    SERVICE_INITIALIZING("OPEN-GOVERNANCE-0001",
                         AuditLogRecordSeverityLevel.STARTUP,
                         "The Open Metadata Store Services are initializing a new server instance",
                         "The local server has started up a new instance of the Open Metadata Store Services.  " +
                                 "It will support open metadata store REST requests.",
                         "This is part of the normal start up of the service.  No action is required if this service " +
                                 "startup was intentional.",
                                 "https://egeria-project.org/services/gaf-metadata-management/"),

    /**
     * OPEN-GOVERNANCE-0002 - The catalog integrator context manager is being initialized for calls to server {0} on platform {1}
     */
    CONTEXT_INITIALIZING("OPEN-GOVERNANCE-0002",
                         AuditLogRecordSeverityLevel.STARTUP,
                         "The open integration context manager is being initialized for calls to server {0} on platform {1}",
                         "The integration daemon is initializing its context manager.",
                         "Verify that the start up sequence goes on to initialize the context for each connector configured for this service.",
                         "https://egeria-project.org/services/gaf-metadata-management/"),

    /**
     * OPEN-GOVERNANCE-0003 The Open Governance service is ready to publish  notifications to topic {0}
     */
    SERVICE_PUBLISHING("OPEN-GOVERNANCE-0003",
                       AuditLogRecordSeverityLevel.STARTUP,
                       "The Open Governance service is ready to publish notifications to topic {0}",
                       "The local server has started up the event publisher for the GAF Services.  " +
                               "It will begin publishing metadata changes to its out topic.",
                       "This is part of the normal start up of the service. Check that there are no errors from the event bus.",
                       "https://egeria-project.org/services/gaf-metadata-management/"),

    /**
     * OPEN-GOVERNANCE-0005 - The Open Metadata Store Services has initialized a new instance for server {0}
     */
    SERVICE_INITIALIZED("OPEN-GOVERNANCE-0005",
                        AuditLogRecordSeverityLevel.STARTUP,
                        "The Open Metadata Store Services has initialized a new instance for server {0}",
                        "The Open Metadata Store Services has completed initialization of a new server instance.",
                        "Verify that there are no error messages logged by the service.  If there are none it means that " +
                                "all parts of the service initialized successfully.",
                                "https://egeria-project.org/services/gaf-metadata-management/"),

    /**
     * OPEN-GOVERNANCE-0006 - The Open Metadata Store Services are unable to initialize a new instance; error message is {0}
     */
    SERVICE_INSTANCE_FAILURE("OPEN-GOVERNANCE-0006",
                             AuditLogRecordSeverityLevel.ERROR,
                             "The Open Metadata Store Services are unable to initialize a new instance; error message is {0}",
                             "The service detected an error during the start up of a specific server instance.  Its services are not available for the server.",
                             "Review the error message and any other reported failures to determine the cause of the problem.  Once this is resolved, restart the server.",
                             "https://egeria-project.org/services/gaf-metadata-management/"),

    /**
     * OPEN-GOVERNANCE-0011 The Open Governance Service caught an unexpected {0} exception whilst shutting down the out
     * topic listeners. The error message was: {1}
     */
    EVENT_SHUTDOWN_ERROR("OPEN-GOVERNANCE-0011",
                         AuditLogRecordSeverityLevel.SHUTDOWN,
                         "The Open Governance Service caught an unexpected {0} exception whilst shutting down the out " +
                                 "topic listeners. The error message was: {1}",
                         "The local administrator has requested shutdown of the engine host.  " +
                                 "No more events will be received, although, due to this exception, the connection to the event bus may " +
                                 "not be released properly.",
                         "This is part of the normal shutdown of the engine host. However, an exception is not expected at this point unless it " +
                                 "is the consequence of a previous error. Review the error message and any other reported failures to " +
                                 "determine if this exception needs special attention.",
                                 "https://egeria-project.org/services/gaf-metadata-management/"),

    /**
     * OPEN-GOVERNANCE-0012 - The Open Metadata Store Services are shutting down its instance for server {0}
     */
    SERVICE_SHUTDOWN("OPEN-GOVERNANCE-0012",
                     AuditLogRecordSeverityLevel.SHUTDOWN,
                     "The Open Metadata Store Services are shutting down its instance for server {0}",
                     "The local administrator has requested shut down of an Open Metadata Store Services instance.  " +
                             "The open metadata store interfaces are no longer available and no configuration events will " +
                             "be published to the out topic",
                     "This is part of the normal shutdown of the service.  Verify that all resources have been released.",
                     "https://egeria-project.org/services/gaf-metadata-management/"),

    /**
     * OMES-GOVERNANCE-ACTION-0015 - The Open Governance Framework received an {0} exception from the {1} governance action service while it was processing a watchdog event of type {2}; error message is {3}
     */
    WATCHDOG_EVENT_FAILURE("OMES-GOVERNANCE-ACTION-0015",
                           AuditLogRecordSeverityLevel.ERROR,
                           "The Open Governance Framework (OGF) received an {0} exception from the {1} governance action service while it was processing a watchdog event of type {2}; error message is {3}",
                           "The engine services detected an error while processing a watchdog event.",
                           "Review the error message and any other reported failures to determine the cause of the problem.  It may also be necessary to initiate the action that did not occur due to the failure to process this event.",
                           "https://egeria-project.org/services/gaf-metadata-management/"),

    /**
     * OPEN-GOVERNANCE-0021 - Failed to publish watchdog event to Watchdog Governance Action Service for governance engine {0}.
     * The exception was {1} with error message {2}
     */
    WATCHDOG_LISTENER_EXCEPTION("OPEN-GOVERNANCE-0021",
                                AuditLogRecordSeverityLevel.ERROR,
                                "Failed to publish watchdog event to Watchdog Governance Action Service for governance engine {0}.  The exception was {1} with error message {2}",
                                "An open watchdog governance action service has raised an exception while processing an incoming " +
                                        "watchdog event.  The exception explains the reason.",
                                "Review the error messages and resolve the cause of the problem if needed.",
                                "https://egeria-project.org/services/gaf-metadata-management/"),

    ;


    private final String                      logMessageId;
    private final AuditLogRecordSeverityLevel severity;
    private final String                      logMessage;
    private final String                      systemAction;
    private final String                      userAction;
    private final String                      url;


    /**
     * Constructor for the message definitions that have no page to link to.
     *
     * @param messageId    - unique id for the message
     * @param severity     - severity of the message
     * @param message      - text for the message
     * @param systemAction - description of the action taken by the system when the condition happened
     * @param userAction   - instructions for resolving the situation, if any
     */
    OpenGovernanceAuditCode(String                      messageId,
                            AuditLogRecordSeverityLevel severity,
                            String                      message,
                            String                      systemAction,
                            String                      userAction)
    {
        this(messageId, severity, message, systemAction, userAction, null);
    }


    /**
     * The constructor for OpenGovernanceAuditCode expects to be passed one of the enumeration rows defined in
     * OpenGovernanceAuditCode above.   For example:
     * <p>
     * OpenGovernanceAuditCode   auditCode = OpenGovernanceAuditCode.SERVER_NOT_AVAILABLE;
     * <p>
     * This will expand out to the 5 parameters shown below.
     *
     * @param messageId    - unique id for the message
     * @param severity     - severity of the message
     * @param message      - text for the message
     * @param systemAction - description of the action taken by the system when the condition happened
     * @param userAction   - instructions for resolving the situation, if any
     * @param url link to a page that describes the component or concept behind
     *            this message - null if there is no suitable page
     */
    OpenGovernanceAuditCode(String                      messageId,
                            AuditLogRecordSeverityLevel severity,
                            String                      message,
                            String                      systemAction,
                            String                      userAction,
                            String                      url)
    {
        this.logMessageId = messageId;
        this.severity = severity;
        this.logMessage = message;
        this.systemAction = systemAction;
        this.userAction = userAction;
        this.url        = url;
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
                                             userAction,
                                             url);
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
                                                                                    userAction,
                                                                                    url);
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
                       ", url='" + url + '\'' +
                       '}';
    }
}
