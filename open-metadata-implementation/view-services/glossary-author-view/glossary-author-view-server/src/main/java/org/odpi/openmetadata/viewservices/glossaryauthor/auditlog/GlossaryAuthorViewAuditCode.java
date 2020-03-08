/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.viewservices.glossaryauthor.auditlog;


import org.odpi.openmetadata.repositoryservices.auditlog.OMRSAuditLogRecordSeverity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.MessageFormat;
import java.util.Arrays;

/**
 * The GlossaryAuthorAuditCode is used to define the message content for the OMRS Audit Log.
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


//TODO the adding of terms for audit will need to pass the guid as a parameter and maybe the fqname. I do not see how I can add parameters in the below structure.

public enum GlossaryAuthorViewAuditCode {
    SERVICE_INITIALIZING("OMVS-GLOSSARY-AUTHOR-0001",
            OMRSAuditLogRecordSeverity.STARTUP,
            "The Glossary Author View Service (OMVS) is initializing",
            "The local server is initializing the Glossary Author OMVS. If the initialization is successful then audit message OMVS-GLOSSARY-AUTHOR-0003 will be issued, if there were errors then they should be shown in the audit log. ",
            "No action is required. This is part of the normal operation of the Glossary Author OMVS."),

    SERVICE_INITIALIZED("OMVS-GLOSSARY-AUTHOR-0002",
            OMRSAuditLogRecordSeverity.STARTUP,
            "The Glossary Author View Service (OMVS) is initialized",
            "The Glossary Author OMVS has completed initialization. Calls will be accepted by this service, if OMRS is also configured and the view server has been started. ",
            "No action is required.  This is part of the normal operation of the Glossary Author OMVS. Once the OMRS is configured and the server is started, Glossary Author view service requests can be accepted."),

    SERVICE_SHUTDOWN("OMVS-GLOSSARY-AUTHOR-0003",
            OMRSAuditLogRecordSeverity.SHUTDOWN,
            "The Glossary Author View Service (OMVS) is shutting down",
            "The local server has requested shutdown of the Glossary Author OMVS.",
            "No action is required. The operator should verify that shutdown was intended. This is part of the normal operation of the Glossary Author OMVS."),

    SERVICE_INSTANCE_FAILURE("OMVS-GLOSSARY-AUTHOR-0004",
            OMRSAuditLogRecordSeverity.EXCEPTION,
            "The Glossary Author View Service (OMVS) is unable to initialize a new instance; error message is {0}",
            "The view service detected an error during the start up of a specific server instance.  Its services are not available for the server.",
            "Review the error message and any other reported failures to determine the cause of the problem.  Once this is resolved, restart the server."),
    SERVICE_TERMINATING("OMVS-GLOSSARY-AUTHOR-0005",
            OMRSAuditLogRecordSeverity.SHUTDOWN,
            "The Glossary Author View Service (OMVS) is shutting down server instance {0}",
            "The local handlers has requested shut down of the Glossary Author OMVS.",
            "No action is required. This is part of the normal operation of the service."),

    UNEXPECTED_EXCEPTION("OMVS-GLOSSARY-AUTHOR-0006",
                         OMRSAuditLogRecordSeverity.EXCEPTION,
                         "The Open Metadata Service has generated an unexpected {0} exception during method {1}.  The message was: {2}",
                         "The request returns a PropertyServerException.",
                         "This is probably a logic error. Review the stack trace to identify where the error " +
                                 "occurred and work to resolve the cause.")
    ;

    private String                     logMessageId;
    private OMRSAuditLogRecordSeverity severity;
    private String                     logMessage;
    private String                     systemAction;
    private String                     userAction;

    private static final Logger log = LoggerFactory.getLogger(GlossaryAuthorViewAuditCode.class);


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
    GlossaryAuthorViewAuditCode(String                     messageId,
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
     * Returns the unique identifier for the error message.
     *
     * @return logMessageId
     */
    public String getLogMessageId()
    {
        return logMessageId;
    }


    /**
     * Return the severity of the audit log record.
     *
     * @return OMRSAuditLogRecordSeverity enum
     */
    public OMRSAuditLogRecordSeverity getSeverity()
    {
        return severity;
    }

    /**
     * Returns the log message with the placeholders filled out with the supplied parameters.
     *
     * @param params - strings that plug into the placeholders in the logMessage
     * @return logMessage (formatted with supplied parameters)
     */
    public String getFormattedLogMessage(String... params)
    {
        if (log.isDebugEnabled())
        {
            log.debug(String.format("<== OMRS Audit Code.getMessage(%s)", Arrays.toString(params)));
        }

        MessageFormat mf = new MessageFormat(logMessage);
        String result = mf.format(params);

        if (log.isDebugEnabled())
        {
            log.debug(String.format("==> OMRS Audit Code.getMessage(%s): %s", Arrays.toString(params), result));
        }

        return result;
    }



    /**
     * Returns a description of the action taken by the system when the condition that caused this exception was
     * detected.
     *
     * @return systemAction String
     */
    public String getSystemAction()
    {
        return systemAction;
    }


    /**
     * Returns instructions of how to resolve the issue reported in this exception.
     *
     * @return userAction String
     */
    public String getUserAction()
    {
        return userAction;
    }
}

