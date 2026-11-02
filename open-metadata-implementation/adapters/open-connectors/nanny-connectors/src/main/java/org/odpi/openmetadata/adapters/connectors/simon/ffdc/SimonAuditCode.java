/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.simon.ffdc;

import org.odpi.openmetadata.frameworks.auditlog.AuditLogRecordSeverityLevel;
import org.odpi.openmetadata.frameworks.auditlog.messagesets.AuditLogMessageDefinition;
import org.odpi.openmetadata.frameworks.auditlog.messagesets.AuditLogMessageSet;


/**
 * The SimonAuditCode is used to define the message content for the Audit Log.
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
public enum SimonAuditCode implements AuditLogMessageSet
{
    /**
     * SIMON-CONTRIBUTION-RECORDER-0001 - The {0} integration connector received an unexpected exception {1} during method {2}; the error message was: {3}
     */
    UNEXPECTED_EXCEPTION("SIMON-CONTRIBUTION-RECORDER-0001",
                         AuditLogRecordSeverityLevel.EXCEPTION,
                         "The {0} integration connector received an unexpected exception {1} during method {2}; the error message was: {3}",
                         "The connector is unable to complete the recording of some of the karma points it has awarded.",
                         "Use the details from the error message to determine the cause of the error and correct it.",
                         "https://egeria-project.org/concepts/karma-point/"),

    /**
     * SIMON-CONTRIBUTION-RECORDER-0002 - The {0} integration connector is starting to award karma points for the changes
     * made through server {1} on platform {2}; users matching {3} are not awarded karma points
     */
    STARTING_CONNECTOR("SIMON-CONTRIBUTION-RECORDER-0002",
                       AuditLogRecordSeverityLevel.INFO,
                       "The {0} integration connector is starting to award karma points for the changes made through server {1} " +
                               "on platform {2}; users matching {3} are not awarded karma points",
                       "The connector listens for the open metadata events and awards a karma point to the user responsible " +
                               "for each change.  On each refresh it adds the karma points awarded since the previous refresh to " +
                               "the ContributionRecord attached to each user's actor profile.  The users whose userIds match the " +
                               "patterns in the ignoredUserIdPatterns configuration property, and the connector's own user, are " +
                               "not awarded karma points.",
                       "No action is required.  This message is for monitoring the start up of the contribution recorder.",
                       "https://egeria-project.org/concepts/karma-point/"),

    /**
     * SIMON-CONTRIBUTION-RECORDER-0003 - The {0} integration connector has added {1} karma points to the contribution
     * records of {2} users
     */
    KARMA_POINTS_RECORDED("SIMON-CONTRIBUTION-RECORDER-0003",
                          AuditLogRecordSeverityLevel.INFO,
                          "The {0} integration connector has added {1} karma points to the contribution records of {2} users",
                          "The ContributionRecord of each of these users has been created or updated with the karma points " +
                                  "awarded since the previous refresh.",
                          "No action is required.  This message is for monitoring the progress of the contribution recorder.",
                          "https://egeria-project.org/concepts/karma-point/"),

    /**
     * SIMON-CONTRIBUTION-RECORDER-0004 - The {0} integration connector is unable to update the contribution record of user
     * {1} because of exception {2} with message {3}; {4} karma points will be retried on the next refresh
     */
    KARMA_POINTS_DEFERRED("SIMON-CONTRIBUTION-RECORDER-0004",
                          AuditLogRecordSeverityLevel.ERROR,
                          "The {0} integration connector is unable to update the contribution record of user {1} because of " +
                                  "exception {2} with message {3}; {4} karma points will be retried on the next refresh",
                          "The karma points are kept in memory and added to the user's ContributionRecord on the next " +
                                  "refresh.  They are lost if the integration daemon is shut down before then.",
                          "Use the details from the error message to determine why the metadata access server could not be " +
                                  "updated, and correct it.",
                          "https://egeria-project.org/concepts/karma-point/"),

    /**
     * SIMON-CONTRIBUTION-RECORDER-0005 - The {0} integration connector has discarded {1} karma points for user {2}
     * because their contribution record can not be updated: exception {3} with message {4}
     */
    KARMA_POINTS_DISCARDED("SIMON-CONTRIBUTION-RECORDER-0005",
                           AuditLogRecordSeverityLevel.INFO,
                           "The {0} integration connector has discarded {1} karma points for user {2} because their contribution " +
                                   "record can not be updated: exception {3} with message {4}",
                           "The user's actor profile or contribution record can not be retrieved or changed by the connector's " +
                                   "user - for example, it is in a governance zone that the connector's user can not update.  " +
                                   "Trying again would fail in the same way, so the karma points are not kept.",
                           "No action is required unless the user's karma points are expected to be recorded.  If the " +
                                   "exception is a UserNotAuthorizedException, grant the connector's user permission to update " +
                                   "the actor profiles and their contribution records.",
                           "https://egeria-project.org/concepts/karma-point/"),

    /**
     * SIMON-CONTRIBUTION-RECORDER-0006 - The {0} integration connector has discarded {1} karma points for users {2}
     * because they do not have an actor profile
     */
    NO_ACTOR_PROFILE("SIMON-CONTRIBUTION-RECORDER-0006",
                     AuditLogRecordSeverityLevel.INFO,
                     "The {0} integration connector has discarded {1} karma points for users {2} because they do not have " +
                             "an actor profile",
                     "Karma points are recorded in the ContributionRecord attached to a user's actor profile.  These users " +
                             "have no UserIdentity linked to an actor profile, so there is nowhere to record their contributions.",
                     "If the users are people whose contributions should be recognized, create a UserIdentity for each one " +
                             "and link it to their actor profile.  If they are not, add a pattern matching their userIds to the " +
                             "ignoredUserIdPatterns configuration property so that their changes are not counted.",
                     "https://egeria-project.org/concepts/karma-point/"),

    /**
     * SIMON-CONTRIBUTION-RECORDER-0007 - The {0} integration connector has skipped ignored userId pattern {1} because it is not
     * a valid regular expression: {2}
     */
    INVALID_USER_ID_PATTERN("SIMON-CONTRIBUTION-RECORDER-0007",
                            AuditLogRecordSeverityLevel.ERROR,
                            "The {0} integration connector has skipped ignored userId pattern {1} because it is not a valid " +
                                    "regular expression: {2}",
                            "The connector continues to run with the other patterns, so the users that this pattern was " +
                                    "meant to match are awarded karma points.",
                            "Correct the pattern in the ignoredUserIdPatterns configuration property and restart the connector.  " +
                                    "The patterns use Java regular expression syntax and are matched against the whole userId - " +
                                    "for example, .*npa matches every userId that ends in npa.",
                            "https://egeria-project.org/concepts/karma-point/"),
    ;

    private final String                      logMessageId;
    private final AuditLogRecordSeverityLevel severity;
    private final String                      logMessage;
    private final String                      systemAction;
    private final String                      userAction;
    private final String                      url;


    /**
     * The constructor for SimonAuditCode expects to be passed one of the enumeration rows defined in
     * SimonAuditCode above.   For example:
     * <br>
     *     SimonAuditCode   auditCode = SimonAuditCode.STARTING_CONNECTOR;
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
    SimonAuditCode(String                      messageId,
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
        return "SimonAuditCode{" +
                "logMessageId='" + logMessageId + '\'' +
                ", severity=" + severity +
                ", logMessage='" + logMessage + '\'' +
                ", systemAction='" + systemAction + '\'' +
                ", userAction='" + userAction + '\'' +
                ", url='" + url + '\'' +
                '}';
    }
}
