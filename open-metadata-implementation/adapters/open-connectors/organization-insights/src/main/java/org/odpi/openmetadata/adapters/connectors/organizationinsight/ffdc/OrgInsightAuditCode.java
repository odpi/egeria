/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.organizationinsight.ffdc;

import org.odpi.openmetadata.frameworks.auditlog.AuditLogRecordSeverityLevel;
import org.odpi.openmetadata.frameworks.auditlog.messagesets.AuditLogMessageDefinition;
import org.odpi.openmetadata.frameworks.auditlog.messagesets.AuditLogMessageSet;


/**
 * The OrgInsightAuditCode is used to define the message content for the Audit Log.
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
public enum OrgInsightAuditCode implements AuditLogMessageSet
{
    /**
     * ORGANIZATION-INSIGHTS-0001 - The survey action service received an unexpected exception {0} during method {1}; the error message was: {2}
     */
    UNEXPECTED_EXCEPTION("ORGANIZATION-INSIGHTS-0001",
                         AuditLogRecordSeverityLevel.EXCEPTION,
                         "The organization insight service {0} received an unexpected exception {1} during method {2}; the error message was: {3}",
                         "The connector is unable to process the current request.",
                         "Use the details from the error message to determine the cause of the error and retry the request once it is resolved."),

    /**
     * ORGANIZATION-INSIGHTS-0016 - The {0} governance action service received a {1} exception when it registered its completion status.
     * The exception's message is: {2}
     */
    UNABLE_TO_SET_COMPLETION_STATUS("ORGANIZATION-INSIGHTS-0016",
                                    AuditLogRecordSeverityLevel.INFO,
                                    "The {0} governance action service received a {1} exception when it registered its completion status.  The exception's message is: {2}",
                                    "The governance action throws a GovernanceServiceException in the hope that the hosting server is able to clean up.",
                                    "Review the exception messages that are logged about the same time as one of them will point to the root cause of the error."),

    /**
     * ORGANIZATION-INSIGHTS-0017 - The {0} governance action service received a {1} exception when it registered a listener with the
     * governance context.  The exception's message is: {2}
     */
    UNABLE_TO_REGISTER_LISTENER("ORGANIZATION-INSIGHTS-0017",
                                AuditLogRecordSeverityLevel.INFO,
                                "The {0} governance action service received a {1} exception when it registered a listener with the governance context.  The exception's message is: {2}",
                                "The governance action service throws a GovernanceServiceException.",
                                "This is likely to be a configuration error.  Review the description of the exception's message to understand what is not set up correctly and " +
                                        "and follow its instructions."),
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
    OrgInsightAuditCode(String                      messageId,
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
