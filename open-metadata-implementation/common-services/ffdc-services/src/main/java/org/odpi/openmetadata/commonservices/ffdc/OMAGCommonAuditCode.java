/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.commonservices.ffdc;

import org.odpi.openmetadata.frameworks.auditlog.messagesets.AuditLogMessageDefinition;
import org.odpi.openmetadata.frameworks.auditlog.messagesets.AuditLogMessageSet;
import org.odpi.openmetadata.repositoryservices.auditlog.OMRSAuditLogRecordSeverity;


/**
 * The OMAGCommonAuditCode is used to define the message content for the OMRS Audit Log.
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
public enum OMAGCommonAuditCode implements AuditLogMessageSet
{
    /**
     * OMAG-COMMON-0001 - The Open Metadata Service has generated an unexpected {0} exception during method {1}.  The message was: {2}
     */
    UNEXPECTED_EXCEPTION("OMAG-COMMON-0001",
             OMRSAuditLogRecordSeverity.EXCEPTION,
             "The Open Metadata Service has generated an unexpected {0} exception during method {1}.  The message was: {2}",
             "The request returns a PropertyServerException.",
             "This is probably a logic error. Review the stack trace to identify where the error " +
                                 "occurred and work to resolve the cause."),

    /**
     * OMAG-COMMON-0002 - A client-side exception was received from API call {0} to OMAG Server {1} at {2}.  The error message was {3}
     */
    CLIENT_SIDE_REST_API_ERROR( "OMAG-COMMON-0002",
             OMRSAuditLogRecordSeverity.ERROR,
             "A client-side exception was received from API call {0} to OMAG Server {1} at {2}.  The error message was {3}",
             "The server has issued a call to the open metadata access service REST API in a remote server and has received an exception from the local client libraries.",
             "Look for errors in the local server's console to understand and correct the source of the error.")
    ;

    private final String                     logMessageId;
    private final OMRSAuditLogRecordSeverity severity;
    private final String                     logMessage;
    private final String                     systemAction;
    private final String                     userAction;


    /**
     * The constructor for OMAGCommonAuditCode expects to be passed one of the enumeration rows defined in
     * OMAGCommonAuditCode above.   For example:
     * <br><br>
     *     OMAGCommonAuditCode   auditCode = OMAGCommonAuditCode.SERVER_NOT_AVAILABLE;
     * <br><br>
     * This will expand out to the 4 parameters shown below.
     *
     * @param messageId - unique identifier for the message
     * @param severity - severity of the message
     * @param message - text for the message
     * @param systemAction - description of the action taken by the system when the condition happened
     * @param userAction - instructions for resolving the situation, if any
     */
    OMAGCommonAuditCode(String                     messageId,
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
        return "OMAGCommonAuditCode{" +
                "logMessageId='" + logMessageId + '\'' +
                ", severity=" + severity +
                ", logMessage='" + logMessage + '\'' +
                ", systemAction='" + systemAction + '\'' +
                ", userAction='" + userAction + '\'' +
                ", messageDefinition=" + getMessageDefinition() +
                '}';
    }
}
