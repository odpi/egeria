/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.subjectarea.auditlog;


        import org.odpi.openmetadata.repositoryservices.auditlog.OMRSAuditLogRecordSeverity;
        import org.slf4j.Logger;
        import org.slf4j.LoggerFactory;

        import java.text.MessageFormat;
        import java.util.Arrays;

/**
 * The SubjectAreaAuditCode is used to define the message content for the OMRS Audit Log.
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


//TODO the adding of terms for audit will need to pass the guid as a parameter and maybe the fqname. I do not see how I can add parameters in the beloe structure.

public enum SubjectAreaAuditCode {
    SERVICE_INITIALIZING("OMAS-SUBJECT-AREA-0001",
            OMRSAuditLogRecordSeverity.STARTUP,
            "The Subject Area Open Metadata Access Service (OMAS) is initializing",
            "The local server has started up the Subject Area OMAS.",
            "No action is required. This is part of the normal operation of the Subject Area OMAS."),

    SERVICE_REGISTERED_WITH_TOPIC("OMAS-SUBJECT-AREA-0002",
            OMRSAuditLogRecordSeverity.STARTUP,
            "The Subject Area Open Metadata Access Service (OMAS) is registering a listener with the OMRS Topic",
            "The Subject Area OMAS is registering to receive org.odpi.openmetadata.accessservices.subjectarea.common.events from the connected open metadata repositories.",
            "No action is required. This is part of the normal operation of the  Subject Area OMAS."),

    SERVICE_INITIALIZED("OMAS-SUBJECT-AREA-0003",
            OMRSAuditLogRecordSeverity.STARTUP,
            "The Subject Area Open Metadata Access Service (OMAS) is initialized",
            "The Subject Area OMAS has completed initialization.",
            "No action is required. This is part of the normal operation of the  Subject Area OMAS."),

    SERVICE_SHUTDOWN("OMAS-SUBJECT-AREA-0004",
            OMRSAuditLogRecordSeverity.SHUTDOWN,
            "The Subject Area Open Metadata Access Service (OMAS) is shutting down",
            "The local server has requested shut down of the Subject Area OMAS.",
            "No action is required. This is part of the normal operation of the  Subject Area OMAS."),
    GLOSSARYTERM_ADDED("OMAS-SUBJECT-AREA-0005",
            OMRSAuditLogRecordSeverity.INFO,
            "A Subject Area Open Metadata Access Service (OMAS) Glossary Node has been added",
            "The local server has requested shut down of the Subject Area OMAS.",
            "No action is required. This is part of the normal operation of the  Subject Area OMAS."),
    GLOSSARYTERM_UPDATED("OMAS-SUBJECT-AREA-0006",
            OMRSAuditLogRecordSeverity.INFO,
            "A Subject Area Open Metadata Access Service (OMAS) Glossary Node has been updated",
            "The local server has requested shut down of the Subject Area OMAS.",
            "No action is required. This is part of the normal operation of the  Subject Area OMAS."),
    GLOSSARYTERM_DELETED("OMAS-SUBJECT-AREA-0007",
            OMRSAuditLogRecordSeverity.INFO,
            "A Subject Area Open Metadata Access Service (OMAS) Glossary Node has been deleted",
            "The local server has requested shut down of the Subject Area OMAS.",
            "No action is required. This is part of the normal operation of the  Subject Area OMAS."),
    GLOSSARY_ADDED("OMAS-SUBJECT-AREA-0008",
            OMRSAuditLogRecordSeverity.INFO,
            "A Subject Area Open Metadata Access Service (OMAS) Glossary has been added",
            "The local server has requested shut down of the Subject Area OMAS.",
            "No action is required. This is part of the normal operation of the  Subject Area OMAS."),
    GLOSSARY_UPDATED("OMAS-SUBJECT-AREA-0009",
            OMRSAuditLogRecordSeverity.INFO,
            "A Subject Area Open Metadata Access Service (OMAS) Glossary has been updated",
            "The local server has requested shut down of the Subject Area OMAS.",
            "No action is required. This is part of the normal operation of the  Subject Area OMAS."),
    GLOSSARY_DELETED("OMAS-SUBJECT-AREA-0010",
            OMRSAuditLogRecordSeverity.INFO,
            "A Subject Area Open Metadata Access Service (OMAS) Glossary has been deleted",
            "The local server has requested shut down of the Subject Area OMAS.",
            "No action is required. This is part of the normal operation of the  Subject Area OMAS."),
    RELATIONSHIP_ADDED("OMAS-SUBJECT-AREA-0011",
            OMRSAuditLogRecordSeverity.INFO,
            "A Subject Area Open Metadata Access Service (OMAS) relationship has been added",
            "The local server has requested shut down of the Subject Area OMAS.",
            "No action is required. This is part of the normal operation of the  Subject Area OMAS."),
    RELATIONSHIP_UPDATED("OMAS-SUBJECT-AREA-0012",
            OMRSAuditLogRecordSeverity.INFO,
            "A Subject Area Open Metadata Access Service (OMAS) relationship has been updated",
            "The local server has requested shut down of the Subject Area OMAS.",
            "No action is required. This is part of the normal operation of the  Subject Area OMAS."),
    RELATIONSHIP_DELETED("OMAS-SUBJECT-AREA-0013",
            OMRSAuditLogRecordSeverity.INFO,
            "A Subject Area Open Metadata Access Service (OMAS) relationship has been deleted",
            "The local server has requested shut down of the Subject Area OMAS.",
            "No action is required. This is part of the normal operation of the  Subject Area OMAS."),
    GLOSSARYTERM_CLASSIFIED("OMAS-SUBJECT-AREA-0014",
            OMRSAuditLogRecordSeverity.INFO,
            "A Subject Area Open Metadata Access Service (OMAS) Glossary Node has been classified",
            "The local server has requested shut down of the Subject Area OMAS.",
            "No action is required. This is part of the normal operation of the  Subject Area OMAS."),
    GLOSSARYTERM_DECLASSIFIED("OMAS-SUBJECT-AREA-0015",
            OMRSAuditLogRecordSeverity.INFO,
            "A Subject Area Open Metadata Access Service (OMAS) Glossary Node has been declassified",
            "The local server has requested shut down of the Subject Area OMAS.",
            "No action is required. This is part of the normal operation of the  Subject Area OMAS."),
    GLOSSARYTERM_CLASSIFICATION_UPDATE("OMAS-SUBJECT-AREA-0016",
            OMRSAuditLogRecordSeverity.INFO,
            "A Subject Area Open Metadata Access Service (OMAS) Glossary Node classification has been updated",
            "A Subject Area Open Metadata Access Service (OMAS) Glossary Node classification has been updated",
            "No action is required. This is part of the normal operation of the  Subject Area OMAS."),
    GLOSSARYTERM_CLASSIFIED_WITH_GOVERNANCE("OMAS-SUBJECT-AREA-0017",
            OMRSAuditLogRecordSeverity.INFO,
            "A Subject Area Open Metadata Access Service (OMAS) Glossary Node has been classified with a governance classification",
            "The local server has requested shut down of the Subject Area OMAS.",
            "No action is required. This is part of the normal operation of the  Subject Area OMAS."),
    GLOSSARYTERM_DECLASSIFIED_WITH_GOVERNANCE("OMAS-SUBJECT-AREA-0018",
            OMRSAuditLogRecordSeverity.INFO,
            "A Subject Area Open Metadata Access Service (OMAS) Glossary Node governance classification has been removed.",
            "A Subject Area Open Metadata Access Service (OMAS) Glossary Node governance classification has been removed.",
            "No action is required. This is part of the normal operation of the  Subject Area OMAS."),
    GLOSSARYTERM_CLASSIFICATION_UPDATE_WITH_GOVERNANCE("OMAS-SUBJECT-AREA-0019",
            OMRSAuditLogRecordSeverity.INFO,
            "A Subject Area Open Metadata Access Service (OMAS) Glossary Node classification has been updated with a governance classification",
            "A Subject Area Open Metadata Access Service (OMAS) Glossary Node classification has been updated with a governance classification",
            "No action is required. This is part of the normal operation of the  Subject Area OMAS."),
    GLOSSARY_CLASSIFIED("OMAS-SUBJECT-AREA-0020",
            OMRSAuditLogRecordSeverity.INFO,
            "A Subject Area Open Metadata Access Service (OMAS) Glossary has been classified",
            "A Subject Area Open Metadata Access Service (OMAS) Glossary has been classified",
            "No action is required. This is part of the normal operation of the  Subject Area OMAS."),
    GLOSSARY_DECLASSIFIED("OMAS-SUBJECT-AREA-0021",
            OMRSAuditLogRecordSeverity.INFO,
            "A Subject Area Open Metadata Access Service (OMAS) Glossary has been declassified",
            "A Subject Area Open Metadata Access Service (OMAS) Glossary has been declassified",
            "No action is required. This is part of the normal operation of the  Subject Area OMAS."),
    GLOSSARY_CLASSIFICATION_UPDATE("OMAS-SUBJECT-AREA-0022",
            OMRSAuditLogRecordSeverity.INFO,
            "A Subject Area Open Metadata Access Service (OMAS) Glossary classification has been updated",
            "A Subject Area Open Metadata Access Service (OMAS) Glossary classification has been updated",
            "No action is required. This is part of the normal operation of the  Subject Area OMAS."),
    GLOSSARY_CLASSIFIED_WITH_GOVERNANCE("OMAS-SUBJECT-AREA-0023",
            OMRSAuditLogRecordSeverity.INFO,
            "A Subject Area Open Metadata Access Service (OMAS) Glossary governance classification has been added",
            "A Subject Area Open Metadata Access Service (OMAS) Glossary governance classification has been added",
            "No action is required. This is part of the normal operation of the  Subject Area OMAS."),
    GLOSSARY_DECLASSIFIED_WITH_GOVERNANCE("OMAS-SUBJECT-AREA-0024",
            OMRSAuditLogRecordSeverity.INFO,
            "A Subject Area Open Metadata Access Service (OMAS) Glossary governance classification has been removed",
            "A Subject Area Open Metadata Access Service (OMAS) Glossary governance classification has been removed",
            "No action is required. This is part of the normal operation of the  Subject Area OMAS."),
    GLOSSARY_CLASSIFICATION_UPDATE_WITH_GOVERNANCE("OMAS-SUBJECT-AREA-0025",
            OMRSAuditLogRecordSeverity.INFO,
            "A Subject Area Open Metadata Access Service (OMAS) Glossary governance classification has been updated",
            "A Subject Area Open Metadata Access Service (OMAS) Glossary governance classification has been updated",
            "No action is required. This is part of the normal operation of the  Subject Area OMAS."),
    GLOSSARYCATEGORY_CLASSIFIED("OMAS-SUBJECT-AREA-0026",
            OMRSAuditLogRecordSeverity.INFO,
            "A Subject Area Open Metadata Access Service (OMAS) Glossary Category has been classified",
            "A Subject Area Open Metadata Access Service (OMAS) Glossary Category has been classified",
            "No action is required. This is part of the normal operation of the  Subject Area OMAS."),
    GLOSSARYCATEGORY_DECLASSIFIED("OMAS-SUBJECT-AREA-0027",
            OMRSAuditLogRecordSeverity.INFO,
            "A Subject Area Open Metadata Access Service (OMAS) Glossary Category has been declassified",
            "A Subject Area Open Metadata Access Service (OMAS) Glossary Category has been declassified",
            "No action is required. This is part of the normal operation of the  Subject Area OMAS."),
    GLOSSARYCATEGORY_CLASSIFICATION_UPDATE("OMAS-SUBJECT-AREA-0028",
            OMRSAuditLogRecordSeverity.INFO,
            "A Subject Area Open Metadata Access Service (OMAS) Glossary Category classification has been updated",
            "A Subject Area Open Metadata Access Service (OMAS) Glossary Category classification has been updated",
            "No action is required. This is part of the normal operation of the  Subject Area OMAS."),
    GLOSSARYCATEGORY_CLASSIFIED_WITH_GOVERNANCE("OMAS-SUBJECT-AREA-0029",
            OMRSAuditLogRecordSeverity.INFO,
            "A Subject Area Open Metadata Access Service (OMAS) Glossary Category governance classification has been added",
            "A Subject Area Open Metadata Access Service (OMAS) Glossary Category governance classification has been added",
            "No action is required. This is part of the normal operation of the  Subject Area OMAS."),
    GLOSSARYCATEGORY_DECLASSIFIED_WITH_GOVERNANCE("OMAS-SUBJECT-AREA-0030",
            OMRSAuditLogRecordSeverity.INFO,
            "A Subject Area Open Metadata Access Service (OMAS) Glossary Category governance classification has been removed",
            "A Subject Area Open Metadata Access Service (OMAS) Glossary Category governance classification has been removed",
            "No action is required. This is part of the normal operation of the  Subject Area OMAS."),
    GLOSSARYCATEGORY_CLASSIFICATION_UPDATE_WITH_GOVERNANCE("OMAS-SUBJECT-AREA-0031",
            OMRSAuditLogRecordSeverity.INFO,
            "A Subject Area Open Metadata Access Service (OMAS) Glossary Category governance classification has been updated",
            "A Subject Area Open Metadata Access Service (OMAS) Glossary Category governance classification has been updated",
            "No action is required. This is part of the normal operation of the  Subject Area OMAS."),

    SERVICE_INSTANCE_FAILURE("OMAS-SUBJECT-AREA-0032",
            OMRSAuditLogRecordSeverity.EXCEPTION,
            "The Subject Area Open Metadata Access Service (OMAS) is unable to initialize a new instance; error message is {0}",
            "The access service detected an error during the start up of a specific server instance.  Its services are not available for the server.",
            "Review the error message and any other reported failures to determine the cause of the problem.  Once this is resolved, restart the server."),
    SERVICE_TERMINATING("OMAS-SUBJECT-AREA-0033",
            OMRSAuditLogRecordSeverity.SHUTDOWN,
            "The Subject Area Open Metadata Access Service (OMAS) is shutting down server instance {0}",
            "The local handlers has requested shut down of the Governance Engine OMAS.",
            "No action is required.  This is part of the normal operation of the service."),

    ;

    private String                     logMessageId;
    private OMRSAuditLogRecordSeverity severity;
    private String                     logMessage;
    private String                     systemAction;
    private String                     userAction;

    private static final Logger log = LoggerFactory.getLogger(SubjectAreaAuditCode.class);


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
    SubjectAreaAuditCode(String                     messageId,
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

