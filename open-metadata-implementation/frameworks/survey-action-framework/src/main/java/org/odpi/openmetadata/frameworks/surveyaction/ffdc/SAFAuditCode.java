/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.surveyaction.ffdc;

import org.odpi.openmetadata.frameworks.auditlog.AuditLogRecordSeverityLevel;
import org.odpi.openmetadata.frameworks.auditlog.messagesets.AuditLogMessageDefinition;
import org.odpi.openmetadata.frameworks.auditlog.messagesets.AuditLogMessageSet;


/**
 * The SAFAuditCode is used to define the message content for the Audit Log.
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
public enum SAFAuditCode implements AuditLogMessageSet
{
    /**
     * SAF-CONNECTOR-0001 - The {0} survey action service has been disconnected - either due to its own actions or a cancel request
     */
    DISCONNECT_DETECTED("SAF-CONNECTOR-0001",
                        AuditLogRecordSeverityLevel.INFO,
                        "The {0} survey action service has been disconnected - either due to its own actions or a cancel request",
                        "The survey action framework will attempt to stop the work of the survey action framework",
                        "Monitor the shutdown of the survey action service."),


    WRONG_TYPE_OF_CONNECTOR("SAF-CONNECTOR-0002",
                            AuditLogRecordSeverityLevel.ERROR,
                            "The {0} Survey Acton Service has been supplied with a resource connector of class {1} rather than class {2} for asset {3}",
                            "The survey is unable to continue since it is unable to work with the supplied connector.",
                            "Use the details from the error message to determine the class of the connector.  " +
                                    "Update the connector type associated with its Connection in the metadata store."),

    /**
     * SAF-CONNECTOR-0003 - The survey action service {0} is creating log file {1}
     */
    CREATING_LOG_FILE("SAF-CONNECTOR-0003",
                      AuditLogRecordSeverityLevel.INFO,
                      "The survey action service {0} is creating log file {1} which is catalogued as CSVFile asset {2}",
                      "This message tells the survey team that a particular survey log file is being created.",
                      "No specific action is required.  The results are added to the log file and the asset for this log file is catalogued as a CSV file."),


    /**
     * SAF-CONNECTOR-0004 - The survey action service {0} is overriding log file {1}
     */
    REUSING_LOG_FILE("SAF-CONNECTOR-0004",
                     AuditLogRecordSeverityLevel.INFO,
                     "The survey action service {0} is overriding log file {1}",
                     "This message warns the survey team that a particular survey log file is being reused.",
                     "No specific action is required.  The new results are appended to the existing results."),


    ;

    private final String                      logMessageId;
    private final AuditLogRecordSeverityLevel severity;
    private final String                      logMessage;
    private final String                      systemAction;
    private final String                      userAction;


    /**
     * The constructor for SAFAuditCode expects to be passed one of the enumeration rows defined above.
     *
     * @param messageId - unique id for the message
     * @param severity - severity of the message
     * @param message - text for the message
     * @param systemAction - description of the action taken by the system when the condition happened
     * @param userAction - instructions for resolving the situation, if any
     */
    SAFAuditCode(String                      messageId,
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
        return "SAFAuditCode{" +
                "logMessageId='" + logMessageId + '\'' +
                ", severity=" + severity +
                ", logMessage='" + logMessage + '\'' +
                ", systemAction='" + systemAction + '\'' +
                ", userAction='" + userAction + '\'' +
                '}';
    }
}
