/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.pasteur.ffdc;

import org.odpi.openmetadata.frameworks.auditlog.AuditLogRecordSeverityLevel;
import org.odpi.openmetadata.frameworks.auditlog.messagesets.AuditLogMessageDefinition;
import org.odpi.openmetadata.frameworks.auditlog.messagesets.AuditLogMessageSet;


/**
 * The PasteurAuditCode is used to define the message content for the Audit Log.
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
public enum PasteurAuditCode implements AuditLogMessageSet
{
    /**
     * PASTEUR-ACTION-HISTORY-CLEANUP-0001 - The {0} integration connector received an unexpected exception {1} during method {2}; the error message was: {3}
     */
    UNEXPECTED_EXCEPTION("PASTEUR-ACTION-HISTORY-CLEANUP-0001",
                         AuditLogRecordSeverityLevel.EXCEPTION,
                         "The {0} integration connector received an unexpected exception {1} during method {2}; the error message was: {3}",
                         "The connector stops cleaning up the action history for this refresh.  It tries again on the next refresh.",
                         "Use the details from the error message to determine the cause of the error and correct it.",
                         "https://egeria-project.org/concepts/action/"),

    /**
     * PASTEUR-ACTION-HISTORY-CLEANUP-0002 - The {0} integration connector is starting to clean up the action history
     * through server {1} on platform {2}; the days to clean up are {3}, the completion statuses are {4} and the delete
     * method is {5}
     */
    STARTING_CONNECTOR("PASTEUR-ACTION-HISTORY-CLEANUP-0002",
                       AuditLogRecordSeverityLevel.INFO,
                       "The {0} integration connector is starting to clean up the action history through server {1} on " +
                               "platform {2}; the days to clean up are {3}, the completion statuses are {4} and the delete method is {5}",
                       "On each refresh the connector deletes the actions of each type listed in the days to clean up " +
                               "whose activity status is one of the completion statuses and that completed at least " +
                               "that many days ago, using the delete method.  With the default, LOOK_FOR_LINEAGE, actions " +
                               "with lineage relationships are archived and the others are soft-deleted.",
                       "No action is required.  This message is for monitoring the start up of the action history clean-up.",
                       "https://egeria-project.org/concepts/action/"),

    /**
     * PASTEUR-ACTION-HISTORY-CLEANUP-0003 - The {0} integration connector has deleted {1} finished actions of type
     * {2} that completed more than {3} days ago using delete method {4}
     */
    ACTIONS_DELETED("PASTEUR-ACTION-HISTORY-CLEANUP-0003",
                    AuditLogRecordSeverityLevel.INFO,
                    "The {0} integration connector has deleted {1} finished actions of type {2} that completed more " +
                            "than {3} days ago using delete method {4}",
                    "The actions, and the elements anchored to them, have been removed.  With the default LOOK_FOR_LINEAGE " +
                            "delete method, those with lineage relationships are archived so lineage queries can still find " +
                            "them, and the others are soft-deleted; either way they can be restored if they were removed in " +
                            "error.  ARCHIVE archives them all, SOFT_DELETE soft-deletes them all and PURGE removes them " +
                            "permanently.",
                    "No action is required.  This message is for monitoring the progress of the action history clean-up.",
                    "https://egeria-project.org/concepts/action/"),

    /**
     * PASTEUR-ACTION-HISTORY-CLEANUP-0004 - The {0} integration connector is unable to delete action {1} of type {2}
     * because of exception {3} with message {4}
     */
    DELETE_FAILED("PASTEUR-ACTION-HISTORY-CLEANUP-0004",
                  AuditLogRecordSeverityLevel.ERROR,
                  "The {0} integration connector is unable to delete action {1} of type {2} because of exception {3} " +
                          "with message {4}",
                  "The action is left in place and the connector carries on with the other actions.  It tries to delete " +
                          "this action again on the next refresh.",
                  "Use the details from the error message to determine why the action could not be deleted - for " +
                          "example, the connector's user may not be permitted to delete elements in the action's " +
                          "governance zones - and correct it.",
                  "https://egeria-project.org/concepts/action/"),

    /**
     * PASTEUR-ACTION-HISTORY-CLEANUP-0005 - The {0} integration connector is ignoring the entries {1} in its daysToCleanUp
     * configuration property because they do not map a type name to a number of days that is zero or more
     */
    INVALID_DAYS_TO_CLEAN_UP("PASTEUR-ACTION-HISTORY-CLEANUP-0005",
                             AuditLogRecordSeverityLevel.ERROR,
                             "The {0} integration connector is ignoring the entries {1} in its daysToCleanUp configuration " +
                                     "property because they do not map a type name to a number of days that is zero or more",
                             "The actions of these types are not cleaned up, unless one of their supertypes has a valid entry.",
                             "Correct the daysToCleanUp configuration property in the connector's connection.  Each key " +
                                     "is the name of Action or one of its subtypes and each value is a whole number of days.",
                             "https://egeria-project.org/concepts/action/"),

    /**
     * PASTEUR-ACTION-HISTORY-CLEANUP-0006 - The {0} integration connector is ignoring the values {1} in its
     * completionStatuses configuration property because they are not activity statuses
     */
    INVALID_COMPLETION_STATUSES("PASTEUR-ACTION-HISTORY-CLEANUP-0006",
                                AuditLogRecordSeverityLevel.ERROR,
                                "The {0} integration connector is ignoring the values {1} in its completionStatuses " +
                                        "configuration property because they are not activity statuses",
                                "The actions with these statuses are not cleaned up.",
                                "Correct the completionStatuses configuration property in the connector's connection.  " +
                                        "Each value is the symbolic name of one of the values of the ActivityStatus enum, " +
                                        "such as COMPLETED, INVALID or FAILED.",
                                "https://egeria-project.org/concepts/action/"),

    /**
     * PASTEUR-ACTION-HISTORY-CLEANUP-0007 - The {0} integration connector has nothing to clean up because its
     * configuration properties give no types of action to clean up, or no completion statuses
     */
    NOTHING_TO_CLEAN_UP("PASTEUR-ACTION-HISTORY-CLEANUP-0007",
                        AuditLogRecordSeverityLevel.ERROR,
                        "The {0} integration connector has nothing to clean up because its configuration properties give " +
                                "no types of action to clean up, or no completion statuses",
                        "The connector runs, but it does not delete any actions, so the action history grows without bound.",
                        "Set the daysToCleanUp configuration property in the connector's connection to the types of action " +
                                "to clean up, and check the completionStatuses configuration property.",
                        "https://egeria-project.org/concepts/action/"),

    /**
     * PASTEUR-ACTION-HISTORY-CLEANUP-0008 - The {0} integration connector is ignoring the value {1} of its deleteMethod
     * configuration property because it is not a delete method; it is using {2}
     */
    INVALID_DELETE_METHOD("PASTEUR-ACTION-HISTORY-CLEANUP-0008",
                          AuditLogRecordSeverityLevel.ERROR,
                          "The {0} integration connector is ignoring the value {1} of its deleteMethod configuration " +
                                  "property because it is not a delete method; it is using {2}",
                          "The connector removes the finished actions using the default delete method.",
                          "Correct the deleteMethod configuration property in the connector's connection.  It is the " +
                                  "symbolic name of one of the values of the DeleteMethod enum: ARCHIVE, SOFT_DELETE, " +
                                  "LOOK_FOR_LINEAGE or PURGE.",
                          "https://egeria-project.org/concepts/action/"),

    ;

    private final String                      logMessageId;
    private final AuditLogRecordSeverityLevel severity;
    private final String                      logMessage;
    private final String                      systemAction;
    private final String                      userAction;
    private final String                      url;


    /**
     * The constructor expects to be passed one of the enumeration rows defined above.
     *
     * @param messageId - unique id for the message
     * @param severity - severity of the message
     * @param message - text for the message
     * @param systemAction - description of the action taken by the system when the condition happened
     * @param userAction - instructions for resolving the situation, if any
     * @param url - link to more information
     */
    PasteurAuditCode(String                      messageId,
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
        return "PasteurAuditCode{" +
                "logMessageId='" + logMessageId + '\'' +
                ", severity=" + severity +
                ", logMessage='" + logMessage + '\'' +
                ", systemAction='" + systemAction + '\'' +
                ", userAction='" + userAction + '\'' +
                ", url='" + url + '\'' +
                '}';
    }
}
