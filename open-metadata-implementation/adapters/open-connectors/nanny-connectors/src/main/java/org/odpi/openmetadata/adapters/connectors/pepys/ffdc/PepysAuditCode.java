/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.pepys.ffdc;

import org.odpi.openmetadata.frameworks.auditlog.AuditLogRecordSeverityLevel;
import org.odpi.openmetadata.frameworks.auditlog.messagesets.AuditLogMessageDefinition;
import org.odpi.openmetadata.frameworks.auditlog.messagesets.AuditLogMessageSet;


/**
 * The PepysAuditCode is used to define the message content for the Audit Log.
 * The 5 fields in the enum are:
 * <ul>
 *     <li>Log Message Identifier - to uniquely identify the message</li>
 *     <li>Severity - is this an event, decision, action, error, or exception</li>
 *     <li>Log Message Text - includes placeholder to allow additional values to be captured</li>
 *     <li>Additional Information - further parameters and data for the audit message (optional)</li>
 *     <li>SystemAction - describes the result of the situation</li>
 *     <li>UserAction - describes how a user should correct the situation</li>
 * </ul>
 */
public enum PepysAuditCode implements AuditLogMessageSet
{
    /**
     * PEPYS-ACTIVITY-OBSERVER-0001 - The {0} integration connector received an unexpected exception {1} during method {2}; the error message was: {3}
     */
    UNEXPECTED_EXCEPTION("PEPYS-ACTIVITY-OBSERVER-0001",
                         AuditLogRecordSeverityLevel.EXCEPTION,
                         "The {0} integration connector received an unexpected exception {1} during method {2}; the error message was: {3}",
                         "The connector is unable to complete the recording of some of the usage activity it has observed.",
                         "Use the details from the error message to determine the cause of the error and correct it.",
                         "https://egeria-project.org/types/7/0795-Usage-Activity/"),

    /**
     * PEPYS-ACTIVITY-OBSERVER-0002 - The {0} integration connector is starting to record the usage activity of the anchor elements
     * in server {1} on platform {2}; the activity of its own user {3} is ignored
     */
    STARTING_CONNECTOR("PEPYS-ACTIVITY-OBSERVER-0002",
                       AuditLogRecordSeverityLevel.INFO,
                       "The {0} integration connector is starting to record the usage activity of the anchor elements in server {1} on " +
                               "platform {2}; the activity of its own user {3} is ignored",
                       "The connector listens for the anchor element activity messages written to the audit log topics that are " +
                               "registered as its catalog targets.  On each refresh it adds the activity it has observed to the " +
                               "counts in the UsageActivity classification of each anchor element.  The connector's own reads and updates " +
                               "are ignored, since otherwise each update of a classification would be counted as more activity.",
                       "No action is required.  This message is for monitoring the start up of the activity observer.",
                       "https://egeria-project.org/types/7/0795-Usage-Activity/"),

    /**
     * PEPYS-ACTIVITY-OBSERVER-0003 - The {0} integration connector is listening for audit log records on topic {1} (connection {2})
     */
    LISTENING_TO_TOPIC("PEPYS-ACTIVITY-OBSERVER-0003",
                       AuditLogRecordSeverityLevel.INFO,
                       "The {0} integration connector is listening for audit log records on topic {1} (connection {2})",
                       "The topic is one of the connector's catalog targets.  Each audit log record received from it that " +
                               "reports activity against an anchor element is counted.",
                       "No action is required.  This message records the topics that the activity observer is listening to.",
                       "https://egeria-project.org/types/7/0795-Usage-Activity/"),

    /**
     * PEPYS-ACTIVITY-OBSERVER-0004 - The {0} integration connector has added {1} observed activities to the usage activity
     * counts of {2} elements
     */
    USAGE_ACTIVITY_RECORDED("PEPYS-ACTIVITY-OBSERVER-0004",
                            AuditLogRecordSeverityLevel.INFO,
                            "The {0} integration connector has added {1} observed activities to the usage activity counts of {2} elements",
                            "The UsageActivity classification of each of these elements has been created or updated with the " +
                                    "activity observed since the previous refresh.",
                            "No action is required.  This message is for monitoring the progress of the activity observer.",
                            "https://egeria-project.org/types/7/0795-Usage-Activity/"),

    /**
     * PEPYS-ACTIVITY-OBSERVER-0005 - The {0} integration connector is unable to update the usage activity counts of element
     * {1} because of exception {2} with message {3}; {4} observed activities will be retried on the next refresh
     */
    USAGE_ACTIVITY_DEFERRED("PEPYS-ACTIVITY-OBSERVER-0005",
                            AuditLogRecordSeverityLevel.ERROR,
                            "The {0} integration connector is unable to update the usage activity counts of element {1} because of " +
                                    "exception {2} with message {3}; {4} observed activities will be retried on the next refresh",
                            "The counts are kept in memory and added to the element's UsageActivity classification on the next " +
                                    "refresh.  They are lost if the integration daemon is shut down before then.",
                            "Use the details from the error message to determine why the metadata access server could not be " +
                                    "updated, and correct it.",
                            "https://egeria-project.org/types/7/0795-Usage-Activity/"),

    /**
     * PEPYS-ACTIVITY-OBSERVER-0006 - The {0} integration connector has discarded {1} observed activities for element {2}
     * because it can not be updated: exception {3} with message {4}
     */
    USAGE_ACTIVITY_DISCARDED("PEPYS-ACTIVITY-OBSERVER-0006",
                             AuditLogRecordSeverityLevel.INFO,
                             "The {0} integration connector has discarded {1} observed activities for element {2} because it can " +
                                     "not be updated: exception {3} with message {4}",
                             "The element is no longer available - typically because it has been deleted since the activity was " +
                                     "observed - or it is not one that a UsageActivity classification can be attached to, or the " +
                                     "connector's user is not permitted to classify it.",
                             "No action is required unless the element is expected to exist.  If the exception is a " +
                                     "UserNotAuthorizedException, grant the connector's user permission to classify elements in the " +
                                     "element's governance zones so that its usage activity can be recorded.",
                             "https://egeria-project.org/types/7/0795-Usage-Activity/"),
    ;

    private final String                      logMessageId;
    private final AuditLogRecordSeverityLevel severity;
    private final String                      logMessage;
    private final String                      systemAction;
    private final String                      userAction;
    private final String                      url;


    /**
     * The constructor for PepysAuditCode expects to be passed one of the enumeration rows defined in
     * PepysAuditCode above.   For example:
     * <br>
     *     PepysAuditCode   auditCode = PepysAuditCode.STARTING_CONNECTOR;
     * <br>
     * This will expand out to the 6 parameters shown below.
     *
     * @param messageId - unique id for the message
     * @param severity - severity of the message
     * @param message - text for the message
     * @param systemAction - description of the action taken by the system when the condition happened
     * @param userAction - instructions for resolving the situation, if any
     * @param url link to a page that describes the component or concept behind
     *            this message - null if there is no suitable page
     */
    PepysAuditCode(String                      messageId,
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
        return "PepysAuditCode{" +
                "logMessageId='" + logMessageId + '\'' +
                ", severity=" + severity +
                ", logMessage='" + logMessage + '\'' +
                ", systemAction='" + systemAction + '\'' +
                ", userAction='" + userAction + '\'' +
                ", url='" + url + '\'' +
                '}';
    }
}
