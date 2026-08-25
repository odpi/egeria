/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.opengovernance.ffdc;

import org.odpi.openmetadata.frameworks.auditlog.AuditLogRecordSeverityLevel;
import org.odpi.openmetadata.frameworks.auditlog.messagesets.AuditLogMessageDefinition;
import org.odpi.openmetadata.frameworks.auditlog.messagesets.AuditLogMessageSet;


/**
 * The OGFAuditCode is used to define the message content for the Audit Log.
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
public enum OGFAuditCode implements AuditLogMessageSet
{
    /**
     * OPEN-GOVERNANCE-ACTION-0001 - The {0} service is issuing a notification to subscriber {1} of type {2} for notification type {3} (notification count = {4})
     */
    ISSUING_NOTIFICATION("OPEN-GOVERNANCE-ACTION-0001",
                             AuditLogRecordSeverityLevel.INFO,
                             "The {0} service is issuing a notification to subscriber {1} of type {2} for notification type {3} (notification count = {4})",
                             "The governance service attempts to notify the subscriber.",
                             "Verify that this subscriber should be linked to this notification type.  If not, remove the subscriber from the notification type.  If this is a valid subscriber then verify that the notification was successful.  Error messages should be logged if there are any known failures.",
                             "https://egeria-project.org/frameworks/ogf/overview/"),

    /**
     * OPEN-GOVERNANCE-ACTION-0002 - Subscriber {0} for notification type {1} is of a type {2}, but the {3} service only supports the following subscriber type(s): {4}
     */
    WRONG_TYPE_OF_SUBSCRIBER("OPEN-GOVERNANCE-ACTION-0002",
                             AuditLogRecordSeverityLevel.ERROR,
                             "Subscriber {0} for notification type {1} is of a type {2}, but the {3} service only supports the following subscriber type(s): {4}",
                             "The governance service ignores this subscriber.",
                             "Remove this subscriber from the notification type and replace it with a subscriber type that is supported.",
                             "https://egeria-project.org/frameworks/ogf/overview/"),

    /**
     * OPEN-GOVERNANCE-ACTION-0003 - The {0} governance service received an unexpected exception {1} during method {2}; the error message was: {3}
     */
    UNEXPECTED_EXCEPTION("OPEN-GOVERNANCE-ACTION-0003",
                         AuditLogRecordSeverityLevel.EXCEPTION,
                         "The {0} governance service received an unexpected exception {1} during method {2}; the error message was: {3}",
                         "The governance service cannot perform the requested governance action on one or more metadata elements in the metadata repository.",
                         "Use the details from the error message to determine the cause of the error and retry the request once it is resolved.",
                         "https://egeria-project.org/frameworks/ogf/overview/"),
    ;


    private final String                      logMessageId;
    private final AuditLogRecordSeverityLevel severity;
    private final String                      logMessage;
    private final String                      systemAction;
    private final String                      userAction;
    private final String                      url;


    /**
     * Constructor for the message definitions that have no page to link to.
     *
     * @param messageId - unique id for the message
     * @param severity - severity of the message
     * @param message - text for the message
     * @param systemAction - description of the action taken by the system when the condition happened
     * @param userAction - instructions for resolving the situation, if any
     */
    OGFAuditCode(String                      messageId,
                 AuditLogRecordSeverityLevel severity,
                 String                      message,
                 String                      systemAction,
                 String                      userAction)
    {
        this(messageId, severity, message, systemAction, userAction, null);
    }


    /**
     * The constructor for OGFAuditCode expects to be passed one of the enumeration rows defined above.
     *
     * @param messageId - unique id for the message
     * @param severity - severity of the message
     * @param message - text for the message
     * @param systemAction - description of the action taken by the system when the condition happened
     * @param userAction - instructions for resolving the situation, if any
     * @param url link to a page that describes the component or concept behind
     *            this message - null if there is no suitable page
     */
    OGFAuditCode(String                      messageId,
                 AuditLogRecordSeverityLevel severity,
                 String                      message,
                 String                      systemAction,
                 String                      userAction,
                 String                      url)
    {
        this.logMessageId = messageId;
        this.severity = severity;
        this.logMessage = message;
        this.systemAction = systemAction;
        this.userAction = userAction;
        this.url        = url;
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
                                             userAction,
                                             url);
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
                                                                                    userAction,
                                                                                    url);
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
        return "OGFAuditCode{" +
                "logMessageId='" + logMessageId + '\'' +
                ", severity=" + severity +
                ", logMessage='" + logMessage + '\'' +
                ", systemAction='" + systemAction + '\'' +
                ", userAction='" + userAction + '\'' +
                ", url='" + url + '\'' +
                '}';
    }
}
