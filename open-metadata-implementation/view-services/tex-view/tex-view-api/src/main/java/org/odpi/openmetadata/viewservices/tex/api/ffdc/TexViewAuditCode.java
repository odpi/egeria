/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.viewservices.tex.api.ffdc;


import org.odpi.openmetadata.frameworks.auditlog.messagesets.AuditLogMessageDefinition;
import org.odpi.openmetadata.frameworks.auditlog.messagesets.AuditLogMessageSet;
import org.odpi.openmetadata.repositoryservices.auditlog.OMRSAuditLogRecordSeverity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The RexAuditCode is used to define the message content for the OMRS Audit Log.
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


public enum TexViewAuditCode implements AuditLogMessageSet
{
    SERVICE_INITIALIZING("OMVS-TYPE-EXPLORER-0001",
                         OMRSAuditLogRecordSeverity.STARTUP,
                         "The Type Explorer View Service (OMVS) is initializing",
                         "The local server is initializing the Type Explorer OMVS. If the initialization is successful then audit message OMVS-TYPE-EXPLORER-0003 will be issued, if there were errors then they should be shown in the audit log. ",
                         "No action is required. This is part of the normal operation of the Type Explorer OMVS."),

    SERVICE_INITIALIZED("OMVS-TYPE-EXPLORER-0002",
                         OMRSAuditLogRecordSeverity.STARTUP,
                         "The Type Explorer View Service (OMVS) has initialized a new instance for server {0}",
                         "The Type Explorer OMVS has completed initialization. Calls will be accepted by this service, if OMRS is also configured and the view server has been started. ",
                         "No action is required.  This is part of the normal operation of the Repository Explorer OMVS. Once the OMRS is configured and the server is started, Type Explorer view service requests can be accepted."),

    SERVICE_SHUTDOWN("OMVS-TYPE-EXPLORER-0003",
                         OMRSAuditLogRecordSeverity.SHUTDOWN,
                         "The Type Explorer View Service (OMVS) is shutting down",
                         "The local server has requested shutdown of the Type Explorer OMVS.",
                         "No action is required. The operator should verify that shutdown was intended. This is part of the normal operation of the Type Explorer OMVS."),

    SERVICE_INSTANCE_FAILURE("OMVS-TYPE-EXPLORER-0004",
                         OMRSAuditLogRecordSeverity.EXCEPTION,
                         "The Type Explorer View Service (OMVS) is unable to initialize a new instance; error message is {0}",
                         "The view service detected an error during the start up of a specific server instance.  Its services are not available for the server.",
                         "Review the error message and any other reported failures to determine the cause of the problem.  Once this is resolved, restart the server."),

    SERVICE_TERMINATING("OMVS-TYPE-EXPLORER-0005",
                         OMRSAuditLogRecordSeverity.SHUTDOWN,
                         "The Type Explorer View Service (OMVS) is shutting down server instance {0}",
                         "The local handler has requested shut down of the Type Explorer OMVS.",
                         "No action is required. This is part of the normal operation of the service."),

    UNEXPECTED_EXCEPTION("OMVS-TYPE-EXPLORER-0006",
                         OMRSAuditLogRecordSeverity.EXCEPTION,
                         "The Open Metadata Service has generated an unexpected {0} exception during method {1}.  The message was: {2}",
                         "The request returned an Exception.",
                         "This is probably a logic error. Review the stack trace to identify where the error occurred and work to resolve the cause.")
    ;

    private String                     logMessageId;
    private OMRSAuditLogRecordSeverity severity;
    private String                     logMessage;
    private String                     systemAction;
    private String                     userAction;

    private static final Logger log = LoggerFactory.getLogger(TexViewAuditCode.class);


    /**
     * The constructor for OMRSAuditCode expects to be passed one of the enumeration rows defined in
     * OMRSAuditCode above.   For example:
     *
     *     OMRSAuditCode   auditCode = OMRSAuditCode.SERVER_NOT_AVAILABLE;
     *
     * This will expand out to the 4 parameters shown below.
     *
     * @param messageId - unique identifier for the message
     * @param severity - severity of the message
     * @param message - text for the message
     * @param systemAction - description of the action taken by the system when the condition happened
     * @param userAction - instructions for resolving the situation, if any
     */
   TexViewAuditCode(String                     messageId,
                    OMRSAuditLogRecordSeverity severity,
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
        return "TexViewAuditCode{" +
                "logMessageId='" + logMessageId + '\'' +
                ", severity=" + severity +
                ", logMessage='" + logMessage + '\'' +
                ", systemAction='" + systemAction + '\'' +
                ", userAction='" + userAction + '\'' +
                ", messageDefinition=" + getMessageDefinition() +
                '}';
    }
}

