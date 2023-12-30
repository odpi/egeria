/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.viewservices.rex.api.ffdc;


import org.odpi.openmetadata.frameworks.auditlog.AuditLogRecordSeverityLevel;
import org.odpi.openmetadata.frameworks.auditlog.messagesets.AuditLogMessageDefinition;
import org.odpi.openmetadata.frameworks.auditlog.messagesets.AuditLogMessageSet;


/**
 * The RexAuditCode is used to define the message content for the OMRS Audit Log.
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


public enum RexViewAuditCode implements AuditLogMessageSet
{
    /**
     * OMVS-REPOSITORY-EXPLORER-0001 - The Repository Explorer View Service (OMVS) is initializing
     */
    SERVICE_INITIALIZING("OMVS-REPOSITORY-EXPLORER-0001",
                         AuditLogRecordSeverityLevel.STARTUP,
                         "The Repository Explorer View Service (OMVS) is initializing",
                         "The local server is initializing the Repository Explorer OMVS. If the initialization is successful then audit message OMVS-REPOSITORY-EXPLORER-0003 will be issued, if there were errors then they should be shown in the audit log. ",
                         "No action is required. This is part of the normal operation of the Repository Explorer OMVS."),

    /**
     * OMVS-REPOSITORY-EXPLORER-0002 - The Repository Explorer View Service (OMVS) has initialized a new instance for server {0}
     */
    SERVICE_INITIALIZED("OMVS-REPOSITORY-EXPLORER-0002",
            AuditLogRecordSeverityLevel.STARTUP,
            "The Repository Explorer View Service (OMVS) has initialized a new instance for server {0}",
            "The Repository Explorer OMVS has completed initialization. Calls will be accepted by this service, if OMRS is also configured and the view server has been started. ",
            "No action is required.  This is part of the normal operation of the Repository Explorer OMVS. Once the OMRS is configured and the server is started, Repository Explorer view service requests can be accepted."),

    /**
     * OMVS-REPOSITORY-EXPLORER-0003 - The Repository Explorer View Service (OMVS) is shutting down
     */
    SERVICE_SHUTDOWN("OMVS-REPOSITORY-EXPLORER-0003",
            AuditLogRecordSeverityLevel.SHUTDOWN,
            "The Repository Explorer View Service (OMVS) is shutting down",
            "The local server has requested shutdown of the Repository Explorer OMVS.",
            "No action is required. The operator should verify that shutdown was intended. This is part of the normal operation of the Repository Explorer OMVS."),

    /**
     * OMVS-REPOSITORY-EXPLORER-0004 - The Repository Explorer View Service (OMVS) is unable to initialize a new instance; error message is {0}
     */
    SERVICE_INSTANCE_FAILURE("OMVS-REPOSITORY-EXPLORER-0004",
            AuditLogRecordSeverityLevel.EXCEPTION,
            "The Repository Explorer View Service (OMVS) is unable to initialize a new instance; error message is {0}",
            "The view service detected an error during the start up of a specific server instance.  Its services are not available for the server.",
            "Review the error message and any other reported failures to determine the cause of the problem.  Once this is resolved, restart the server."),

    /**
     * OMVS-REPOSITORY-EXPLORER-0005 - The Repository Explorer View Service (OMVS) is shutting down server instance {0}
     */
    SERVICE_TERMINATING("OMVS-REPOSITORY-EXPLORER-0005",
            AuditLogRecordSeverityLevel.SHUTDOWN,
            "The Repository Explorer View Service (OMVS) is shutting down server instance {0}",
            "The local handler has requested shut down of the Repository Explorer OMVS.",
            "No action is required. This is part of the normal operation of the service."),

    /**
     * OMVS-REPOSITORY-EXPLORER-0006 - The Open Metadata Service has generated an unexpected {0} exception during method {1}.  The message was: {2}
     */
    UNEXPECTED_EXCEPTION("OMVS-REPOSITORY-EXPLORER-0006",
                         AuditLogRecordSeverityLevel.EXCEPTION,
                         "The Open Metadata Service has generated an unexpected {0} exception during method {1}.  The message was: {2}",
                         "The request returned an Exception.",
                         "This is probably a logic error. Review the stack trace to identify where the error " +
                                 "occurred and work to resolve the cause.")
    ;

    private final String                     logMessageId;
    private final AuditLogRecordSeverityLevel severity;
    private final String                     logMessage;
    private final String                     systemAction;
    private final String                     userAction;



    /**
     * The constructor for RexViewAuditCode expects to be passed one of the enumeration rows defined in
     * RexViewAuditCode above.   For example:
     *     RexViewAuditCode   auditCode = RexViewAuditCode.SERVER_NOT_AVAILABLE;
     * This will expand out to the 5 parameters shown below.
     *
     * @param messageId - unique identifier for the message
     * @param severity - severity of the message
     * @param message - text for the message
     * @param systemAction - description of the action taken by the system when the condition happened
     * @param userAction - instructions for resolving the situation, if any
     */
   RexViewAuditCode(String                     messageId,
                    AuditLogRecordSeverityLevel severity,
                    String                     message,
                    String                     systemAction,
                    String                     userAction)
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
    public AuditLogMessageDefinition getMessageDefinition(String ...params)
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
        return "RexViewAuditCode{" +
                "logMessageId='" + logMessageId + '\'' +
                ", severity=" + severity +
                ", logMessage='" + logMessage + '\'' +
                ", systemAction='" + systemAction + '\'' +
                ", userAction='" + userAction + '\'' +
                ", messageDefinition=" + getMessageDefinition() +
                '}';
    }
}

