/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.viewservices.glossarymanager.ffdc;


import org.odpi.openmetadata.frameworks.auditlog.AuditLogRecordSeverityLevel;
import org.odpi.openmetadata.frameworks.auditlog.messagesets.AuditLogMessageDefinition;
import org.odpi.openmetadata.frameworks.auditlog.messagesets.AuditLogMessageSet;

/**
 * The GlossaryManagerAuditCode is used to define the message content for the OMRS Audit Log.
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


public enum GlossaryManagerAuditCode implements AuditLogMessageSet
{
    /**
     * OMVS-GLOSSARY-MANAGER-0001 The Glossary Manager Open Metadata View Service (OMVS) is initializing
     */
    SERVICE_INITIALIZING("OMVS-GLOSSARY-MANAGER-0001",
                         AuditLogRecordSeverityLevel.STARTUP,
                         "The Glossary Manager Open Metadata View Service (OMVS) is initializing",
                         "The local server is initializing the Glossary Manager Open Metadata View Service. If the initialization is successful then audit message OMVS-GLOSSARY-MANAGER-0002 will be issued, if there were errors then they should be shown in the audit log. ",
                         "No action is required. This is part of the normal operation of the Glossary Manager Open Metadata View Service."),

    /**
     * OMVS-GLOSSARY-MANAGER-0002 The Glossary Manager Open Metadata View Service (OMVS) is initialized
     */
    SERVICE_INITIALIZED("OMVS-GLOSSARY-MANAGER-0002",
                        AuditLogRecordSeverityLevel.STARTUP,
                         "The Glossary Manager Open Metadata View Service (OMVS) is initialized",
                         "The Glossary Manager OMVS has completed initialization. Calls will be accepted by this service, if OMRS is also configured and the view server has been started. ",
                         "No action is required.  This is part of the normal operation of the Glossary Manager Open Metadata View Service. Once the OMRS is configured and the server is started, Glossary Manager view service requests can be accepted."),

    /**
     * OMVS-GLOSSARY-MANAGER-0003 The Glossary Manager Open Metadata View Service (OMVS) is shutting down
     */
    SERVICE_SHUTDOWN("OMVS-GLOSSARY-MANAGER-0003",
                     AuditLogRecordSeverityLevel.SHUTDOWN,
                         "The Glossary Manager Open Metadata View Service (OMVS) is shutting down",
                         "The local server has requested shutdown of the Glossary Manager OMVS.",
                         "No action is required. The operator should verify that shutdown was intended. This is part of the normal operation of the Glossary Manager OMVS."),

    /**
     * OMVS-GLOSSARY-MANAGER-0004 The Glossary Manager Open Metadata View Service (OMVS) is unable to initialize a new instance; error message is {0}
     */
    SERVICE_INSTANCE_FAILURE("OMVS-GLOSSARY-MANAGER-0004",
                             AuditLogRecordSeverityLevel.EXCEPTION,
                         "The Glossary Manager Open Metadata View Service (OMVS) is unable to initialize a new instance; error message is {0}",
                         "The view service detected an error during the start up of a specific server instance.  Its services are not available for the server.",
                         "Review the error message and any other reported failures to determine the cause of the problem.  Once this is resolved, restart the server."),

    /**
     * OMVS-GLOSSARY-MANAGER-0005 The Glossary Manager Open Metadata View Service (OMVS) is shutting down server instance {0}
     */
    SERVICE_TERMINATING("OMVS-GLOSSARY-MANAGER-0005",
                        AuditLogRecordSeverityLevel.SHUTDOWN,
                         "The Glossary Manager Open Metadata View Service (OMVS) is shutting down server instance {0}",
                         "The local handler has requested shut down of the Glossary Manager OMVS.",
                         "No action is required. This is part of the normal operation of the service."),

    /**
     * OMVS-GLOSSARY-MANAGER-0006 The Open Metadata Service has generated an unexpected {0} exception during method {1}.  The message was: {2}
     */
    UNEXPECTED_EXCEPTION("OMVS-GLOSSARY-MANAGER-0006",
                         AuditLogRecordSeverityLevel.EXCEPTION,
                         "The Open Metadata Service has generated an unexpected {0} exception during method {1}.  The message was: {2}",
                         "The request returned an Exception.",
                         "This is probably a logic error. Review the stack trace to identify where the error occurred and work to resolve the cause.")
    ;

    private final String                      logMessageId;
    private final AuditLogRecordSeverityLevel severity;
    private final String                      logMessage;
    private final String                      systemAction;
    private final String                      userAction;



    /**
     * The constructor for GlossaryManagerAuditCode expects to be passed one of the enumeration rows defined in
     * GlossaryManagerAuditCode above.   For example:
     * <br><br>
     *     GlossaryManagerAuditCode   auditCode = GlossaryManagerAuditCode.SERVER_NOT_AVAILABLE;
     * <br><br>
     * This will expand out to the 5 parameters shown below.
     *
     * @param messageId - unique identifier for the message
     * @param severity - severity of the message
     * @param message - text for the message
     * @param systemAction - description of the action taken by the system when the condition happened
     * @param userAction - instructions for resolving the situation, if any
     */
   GlossaryManagerAuditCode(String                      messageId,
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
        return "GlossaryManagerAuditCode{" +
                "logMessageId='" + logMessageId + '\'' +
                ", severity=" + severity +
                ", logMessage='" + logMessage + '\'' +
                ", systemAction='" + systemAction + '\'' +
                ", userAction='" + userAction + '\'' +
                ", messageDefinition=" + getMessageDefinition() +
                '}';
    }
}

