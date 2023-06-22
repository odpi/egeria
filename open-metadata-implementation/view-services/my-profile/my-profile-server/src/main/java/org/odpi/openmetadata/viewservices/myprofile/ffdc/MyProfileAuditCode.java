/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.viewservices.myprofile.ffdc;


import org.odpi.openmetadata.frameworks.auditlog.messagesets.AuditLogMessageDefinition;
import org.odpi.openmetadata.frameworks.auditlog.messagesets.AuditLogMessageSet;
import org.odpi.openmetadata.repositoryservices.auditlog.OMRSAuditLogRecordSeverity;

/**
 * The MyProfileAuditCode is used to define the message content for the OMRS Audit Log.
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
public enum MyProfileAuditCode implements AuditLogMessageSet
{
    /**
     * OMVS-MY-PROFILE-0001 The My Profile Open Metadata View Service (OMVS) is initializing
     */
    SERVICE_INITIALIZING("OMVS-MY-PROFILE-0001",
                         OMRSAuditLogRecordSeverity.STARTUP,
                         "The My Profile Open Metadata View Service (OMVS) is initializing",
                         "The local server is initializing the My Profile Open Metadata View Service. If the initialization is successful then audit message OMVS-MY-PROFILE-0002 will be issued, if there were errors then they should be shown in the audit log. ",
                         "No action is required. This is part of the normal operation of the My Profile Open Metadata View Service."),

    /**
     * OMVS-MY-PROFILE-0002 The My Profile Open Metadata View Service (OMVS) is initialized
     */
    SERVICE_INITIALIZED("OMVS-MY-PROFILE-0002",
                         OMRSAuditLogRecordSeverity.STARTUP,
                         "The My Profile Open Metadata View Service (OMVS) is initialized",
                         "The My Profile OMVS has completed initialization. Calls will be accepted by this service, if OMRS is also configured and the view server has been started. ",
                         "No action is required.  This is part of the normal operation of the My Profile Open Metadata View Service. Once the OMRS is configured and the server is started, My Profileview service requests can be accepted."),

    /**
     * OMVS-MY-PROFILE-0003 The My Profile Open Metadata View Service (OMVS) is shutting down
     */
    SERVICE_SHUTDOWN("OMVS-MY-PROFILE-0003",
                         OMRSAuditLogRecordSeverity.SHUTDOWN,
                         "The My Profile Open Metadata View Service (OMVS) is shutting down",
                         "The local server has requested shutdown of the My Profile OMVS.",
                         "No action is required. The operator should verify that shutdown was intended. This is part of the normal operation of the My Profile OMVS."),

    /**
     * OMVS-MY-PROFILE-0004 The My Profile Open Metadata View Service (OMVS) is unable to initialize a new instance; error message is {0}
     */
    SERVICE_INSTANCE_FAILURE("OMVS-MY-PROFILE-0004",
                         OMRSAuditLogRecordSeverity.EXCEPTION,
                         "The My Profile Open Metadata View Service (OMVS) is unable to initialize a new instance; error message is {0}",
                         "The view service detected an error during the start up of a specific server instance.  Its services are not available for the server.",
                         "Review the error message and any other reported failures to determine the cause of the problem.  Once this is resolved, restart the server."),

    /**
     * OMVS-MY-PROFILE-0005 The My Profile Open Metadata View Service (OMVS) is shutting down server instance {0}
     */
    SERVICE_TERMINATING("OMVS-MY-PROFILE-0005",
                         OMRSAuditLogRecordSeverity.SHUTDOWN,
                         "The My Profile Open Metadata View Service (OMVS) is shutting down server instance {0}",
                         "The local handler has requested shut down of the My Profile OMVS.",
                         "No action is required. This is part of the normal operation of the service."),

    /**
     * OMVS-MY-PROFILE-0006 The Open Metadata Service has generated an unexpected {0} exception during method {1}.  The message was: {2}
     */
    UNEXPECTED_EXCEPTION("OMVS-MY-PROFILE-0006",
                         OMRSAuditLogRecordSeverity.EXCEPTION,
                         "The Open Metadata Service has generated an unexpected {0} exception during method {1}.  The message was: {2}",
                         "The request returned an Exception.",
                         "This is probably a logic error. Review the stack trace to identify where the error occurred and work to resolve the cause.")
    ;

    private final String                     logMessageId;
    private final OMRSAuditLogRecordSeverity severity;
    private final String                     logMessage;
    private final String                     systemAction;
    private final String                     userAction;


    /**
     * The constructor for OMRSAuditCode expects to be passed one of the enumeration rows defined in
     * OMRSAuditCode above.   For example:
     *
     *     OMRSAuditCode   auditCode = OMRSAuditCode.SERVER_NOT_AVAILABLE;
     *
     * This will expand out to the 4 parameters shown below.
     *
     * @param messageId - unique Id for the message
     * @param severity - severity of the message
     * @param message - text for the message
     * @param systemAction - description of the action taken by the system when the condition happened
     * @param userAction - instructions for resolving the situation, if any
     */
   MyProfileAuditCode(String                    messageId,
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
        return "MyProfileAuditCode{" +
                "logMessageId='" + logMessageId + '\'' +
                ", severity=" + severity +
                ", logMessage='" + logMessage + '\'' +
                ", systemAction='" + systemAction + '\'' +
                ", userAction='" + userAction + '\'' +
                ", messageDefinition=" + getMessageDefinition() +
                '}';
    }
}

