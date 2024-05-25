/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.surveyaction.ffdc;

import org.odpi.openmetadata.frameworks.auditlog.AuditLogRecordSeverityLevel;
import org.odpi.openmetadata.frameworks.auditlog.messagesets.AuditLogMessageDefinition;
import org.odpi.openmetadata.frameworks.auditlog.messagesets.AuditLogMessageSet;


/**
 * The SurveyServiceAuditCode is used to define the message content for the OMRS Audit Log.
 * The 5 fields in the enum are:
 * <ul>
 *     <li>Log Message Identifier - to uniquely identify the message</li>
 *     <li>Severity - is this an event, decision, action, error or exception</li>
 *     <li>Log Message Text - includes placeholder to allow additional values to be captured</li>
 *     <li>Additional Information - further parameters and data relating to the audit message (optional)</li>
 *     <li>SystemAction - describes the result of the situation</li>
 *     <li>UserAction - describes how a user should correct the situation</li>
 * </ul>
 */
public enum SurveyServiceAuditCode implements AuditLogMessageSet
{
    /**
     * SURVEY-ACTION-SERVICE-0001 - The survey action service received an unexpected exception {0} during method {1}; the error message was: {2}
     */
    UNEXPECTED_EXCEPTION("SURVEY-ACTION-SERVICE-0001",
                         AuditLogRecordSeverityLevel.EXCEPTION,
                         "The survey action service {0} received an unexpected exception {1} during method {2}; the error message was: {3}",
                         "The connector is unable to process the current request.",
                         "Use the details from the error message to determine the cause of the error and retry the request once it is resolved."),

    /**
     * SURVEY-ACTION-SERVICE-0002 - The survey action service {0} is creating log file {1}
     */
    CREATING_LOG_FILE("SURVEY-ACTION-SERVICE-0002",
                     AuditLogRecordSeverityLevel.INFO,
                     "The survey action service {0} is creating log file {1} which is catalogued as CSVFile asset {2}",
                     "This message tells the survey team that a particular survey log file is being created.",
                     "No specific action is required.  The results are added to the log file and the asset for this log file is catalogued as a CSV file."),


    /**
     * SURVEY-ACTION-SERVICE-0003 - The survey action service {0} is overriding log file {1}
     */
    REUSING_LOG_FILE("SURVEY-ACTION-SERVICE-0003",
                     AuditLogRecordSeverityLevel.INFO,
                     "The survey action service {0} is overriding log file {1}",
                     "This message warns the survey team that a particular survey log file is being reused.",
                     "No specific action is required.  The new results are appended to the existing results."),


    /**
     * SURVEY-ACTION-SERVICE-0004 - The survey action service {0} is surveying the {1} folder (directory)
     */
    SURVEYING_FOLDER("SURVEY-ACTION-SERVICE-0004",
                     AuditLogRecordSeverityLevel.INFO,
                     "The survey action service {0} is surveying the {1} folder (directory)",
                     "This message shows that the starting folder to survey.",
                     "No specific action is required.  This message is marking the start of the survey process."),

    /**
     * SURVEY-ACTION-SERVICE-0003 - The survey action service {0} is has surveyed {1} files and folders (directories)
     */
    PROGRESS_REPORT("SURVEY-ACTION-SERVICE-0005",
                     AuditLogRecordSeverityLevel.INFO,
                     "The survey action service {0} is has surveyed {1} files and folders (directories)",
                     "This message shows that the progress of the survey.",
                     "No specific action is required.  This message is marking the progress of the survey process."),

    /**
     * SURVEY-ACTION-SERVICE-0001 - The survey action service received an unexpected exception {0} during method {1}; the error message was: {2}
     */
    FILE_IO_ERROR("SURVEY-ACTION-SERVICE-0006",
                         AuditLogRecordSeverityLevel.ERROR,
                         "The survey action service {0} received an unexpected IO exception {1} when it attempted to access the attributes of file {2}; the error message was: {3}",
                         "The file is skipped and will not appear in the totals for this folder.  However a separate request for action annotation with a log file of all of the inaccessible files is created.",
                         "If this file is of interest and you want it to be included in the survey report, use the details from the error message to determine the cause of the access error; retry the survey once it is resolved."),

    ;

    private final String                     logMessageId;
    private final AuditLogRecordSeverityLevel severity;
    private final String                     logMessage;
    private final String                     systemAction;
    private final String                     userAction;


    /**
     * The constructor for SurveyServiceAuditCode expects to be passed one of the enumeration rows defined in
     * SurveyServiceAuditCode above.   For example:
     * <br>
     *     SurveyServiceAuditCode   auditCode = SurveyServiceAuditCode.SERVER_NOT_AVAILABLE;
     * <br>
     * This will expand out to the 5 parameters shown below.
     *
     * @param messageId - unique id for the message
     * @param severity - severity of the message
     * @param message - text for the message
     * @param systemAction - description of the action taken by the system when the condition happened
     * @param userAction - instructions for resolving the situation, if any
     */
    SurveyServiceAuditCode(String                      messageId,
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
        return "SurveyServiceAuditCode{" +
                "logMessageId='" + logMessageId + '\'' +
                ", severity=" + severity +
                ", logMessage='" + logMessage + '\'' +
                ", systemAction='" + systemAction + '\'' +
                ", userAction='" + userAction + '\'' +
                '}';
    }
}
