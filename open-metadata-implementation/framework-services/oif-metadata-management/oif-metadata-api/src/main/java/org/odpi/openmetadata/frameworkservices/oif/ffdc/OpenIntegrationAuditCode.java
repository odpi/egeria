/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworkservices.oif.ffdc;

import org.odpi.openmetadata.frameworks.auditlog.AuditLogRecordSeverityLevel;
import org.odpi.openmetadata.frameworks.auditlog.messagesets.AuditLogMessageDefinition;
import org.odpi.openmetadata.frameworks.auditlog.messagesets.AuditLogMessageSet;

/**
 * The OpenIntegrationAuditCode is used to define the message content for the OMRS Audit Log.
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
public enum OpenIntegrationAuditCode implements AuditLogMessageSet
{
    SERVICE_INITIALIZING("OPEN-INTEGRATION-SERVICE-0001",
                         AuditLogRecordSeverityLevel.STARTUP,
                         "The Open Integration Service is initializing a new server instance",
                         "The local server has started up a new instance of the Open Integration Service.  " +
                                 "It will support open integration REST requests.",
                         "This is part of the normal start up of the service.  No action is required if this service " +
                                 "startup was intentional."),

    SERVICE_INITIALIZED("OPEN-INTEGRATION-SERVICE-0005",
                        AuditLogRecordSeverityLevel.STARTUP,
                        "The Open Integration Service has initialized a new instance for server {0}",
                        "The Open Integration Service has completed initialization of a new server instance.",
                        "Verify that there are no error messages logged by the service.  If there are none it means that " +
                                "all parts of the service initialized successfully."),

    SERVICE_INSTANCE_FAILURE("OPEN-INTEGRATION-SERVICE-0006",
                             AuditLogRecordSeverityLevel.ERROR,
                             "The Open Integration Service are unable to initialize a new instance; error message is {0}",
                             "The service detected an error during the start up of a specific server instance.  Its services are not available for the server.",
                             "Review the error message and any other reported failures to determine the cause of the problem.  Once this is resolved, restart the server."),

    UNEXPECTED_INITIALIZATION_EXCEPTION("OPEN-INTEGRATION-SERVICE-0008",
                                        AuditLogRecordSeverityLevel.EXCEPTION,
                                        "The Open Integration Service detected an unexpected {0} exception during the " +
                                                "initialization of its services; error message is {1}",
                                        "The service detected an error during the start up of a specific server instance.  Its services are not available " +
                                                "for the server and an error is returned to the caller.",
                                        "Review the error message and any other reported failures to determine the cause of the problem.  In particular consider the" +
                                                " state of the Event Bus.  Once this is resolved, restart the server."),

    SERVICE_TERMINATING("OPEN-INTEGRATION-SERVICE-0009",
                        AuditLogRecordSeverityLevel.SHUTDOWN,
                        "The Open Integration Service are shutting down server instance {0}",
                        "The local handlers has requested shut down of the Open Integration Service.",
                        "No action is required.  This is part of the normal operation of the service."),

    SERVICE_SHUTDOWN("OPEN-INTEGRATION-SERVICE-0012",
                     AuditLogRecordSeverityLevel.SHUTDOWN,
                     "The Open Integration Service are shutting down its instance for server {0}",
                     "The local administrator has requested shut down of an Open Integration Service instance.  " +
                             "The open integration service interfaces are no longer available and no configuration events will " +
                             "be published to the out topic",
                     "This is part of the normal shutdown of the service.  Verify that all resources have been released."),

    ASSET_AUDIT_LOG("OPEN-INTEGRATION-SERVICE-0020",
                    AuditLogRecordSeverityLevel.INFO,
                    "Log message for element {0} from integration connector {1}: {2}",
                    "A governance service has logged a message about an asset.",
                    "Review the message to ensure no action is required."),

    ;


    private final String                      logMessageId;
    private final AuditLogRecordSeverityLevel severity;
    private final String                      logMessage;
    private final String                      systemAction;
    private final String                      userAction;
    
    
    /**
     * The constructor for OpenIntegrationAuditCode expects to be passed one of the enumeration rows defined in
     * OpenIntegrationAuditCode above.   For example:
     * <p>
     * OpenIntegrationAuditCode   auditCode = OpenIntegrationAuditCode.SERVER_NOT_AVAILABLE;
     * <p>
     * This will expand out to the 5 parameters shown below.
     *
     * @param messageId    - unique id for the message
     * @param severity     - severity of the message
     * @param message      - text for the message
     * @param systemAction - description of the action taken by the system when the condition happened
     * @param userAction   - instructions for resolving the situation, if any
     */
    OpenIntegrationAuditCode(String                      messageId,
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
