/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.baudot.ffdc;

import org.odpi.openmetadata.frameworks.auditlog.AuditLogRecordSeverityLevel;
import org.odpi.openmetadata.frameworks.auditlog.messagesets.AuditLogMessageDefinition;
import org.odpi.openmetadata.frameworks.auditlog.messagesets.AuditLogMessageSet;


/**
 * The BaudotAuditCode is used to define the message content for the Audit Log.
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
public enum BaudotAuditCode implements AuditLogMessageSet
{
    /**
     * BAUDOT-SUBSCRIPTION-MANAGER-0001 - The {0} governance service received an unexpected exception {1} during method {2}; the error message was: {3}
     */
    UNEXPECTED_EXCEPTION("BAUDOT-SUBSCRIPTION-MANAGER-0001",
                         AuditLogRecordSeverityLevel.EXCEPTION,
                         "The {0} governance service received an unexpected exception {1} during method {2}; the error message was: {3}",
                         "The connector cannot provide notifications to subscribers.",
                         "Use the details from the error message to determine the cause of the error and retry the request once it is resolved."),

    /**
     * BAUDOT-SUBSCRIPTION-MANAGER-0002 - The {0} governance service is processing notification type {1} ({2}) where notifications are triggered by changes to its monitored resources.  {3} monitored resources are currently registered
     */
    MONITORED_RESOURCE_NOTIFICATION_TYPE( "BAUDOT-SUBSCRIPTION-MANAGER-0002",
                         AuditLogRecordSeverityLevel.INFO,
                         "The {0} governance service is processing notification type {1} ({2}) where notifications are triggered by changes to its monitored resources.   {3} monitored resources are currently registered",
                         "The governance service monitors the events generated when open metadata elements change.  Notifications are sent if one of the monitored resources (or anything anchored from it) changes, unless another notification has been sent out within the minimumNotificationInterval.",
                         "This notification pattern was selected because multipleNotificationsPermitted is set to true and notificationInterval is set to 0. Verify that this is the intended behaviour and that the correct elements are linked to this notification type using the MonitoredResource relationship."),


    /**
     * BAUDOT-SUBSCRIPTION-MANAGER-0003 - The {0} governance service is processing notification type {1} ({2}) where only one notification is sent to each subscriber
     */
    ONE_TIME_NOTIFICATION_TYPE( "BAUDOT-SUBSCRIPTION-MANAGER-0003",
                              AuditLogRecordSeverityLevel.INFO,
                              "The {0} governance service is processing notification type {1} ({2}) where only one notification is sent to each subscriber",
                              "The governance service sends a notification to each subscriber registered when the service starts up and then monitors for new subscribers in order to send them a notification.",
                              "This notification pattern was selected because multipleNotificationsPermitted is set to false. Validate that this is the right pattern."),

    /**
     * BAUDOT-SUBSCRIPTION-MANAGER-0005 - The {0} governance service is processing notification type {1} ({2}) where a notification is sent to each subscriber on a regular schedule every {3} minutes.  The next scheduled notification will be sent at {4}
     */
    PERIODIC_NOTIFICATION_TYPE("BAUDOT-SUBSCRIPTION-MANAGER-0005",
                      AuditLogRecordSeverityLevel.INFO,
                      "The {0} governance service is processing notification type {1} ({2}) where a notification is sent to each subscriber on a regular schedule every {3} minutes.  The next scheduled notification will be sent at {4}",
                      "The governance service sends a notification to each subscriber registered when the service starts up and then monitors for new subscribers in order to send them notifications.  Additional notifications are sent to each active subscriber every notification interval.",
                      "This notification pattern was selected because multipleNotificationsPermitted is set to true and notificationInterval is greater than 0. Validate that this is the intended behaviour and the notification interval is appropriate."),

    /**
     * BAUDOT-SUBSCRIPTION-MANAGER-0009 - The {0} integration connector has stopped its monitoring of open metadata from server {1} on platform {2} and is shutting down
     */
    CONNECTOR_STOPPING("BAUDOT-SUBSCRIPTION-MANAGER-0009",
                       AuditLogRecordSeverityLevel.INFO,
                       "The {0} governance service has stopped its monitoring of open metadata from server {1} on platform {2} and is shutting down",
                       "The connector is disconnecting.",
                       "No action is required unless there are errors that follow indicating that there were problems shutting down."),
    
    /**
     * BAUDOT-SUBSCRIPTION-MANAGER-0011 - The {0} integration connector is starting its harvesting of open metadata from server {1} on platform {2} into digital products
     */
    STARTING_CONNECTOR("BAUDOT-SUBSCRIPTION-MANAGER-0011",
                       AuditLogRecordSeverityLevel.INFO,
                       "The {0} governance service is starting its subscription management function from server {1} on platform {2}",
                       "The connector is initializing subscription management for the Open Metadata Digital Product Catalog.",
                       "Monitor the set up of the catalog and the switch over to monitoring."),

    /**
     * BAUDOT-SUBSCRIPTION-MANAGER-0016 - The {0} governance action service received a {1} exception when it registered its completion status.  
     * The exception's message is: {2}
     */
    UNABLE_TO_SET_COMPLETION_STATUS("BAUDOT-SUBSCRIPTION-MANAGER-0016",
                                    AuditLogRecordSeverityLevel.INFO,
                                    "The {0} governance action service received a {1} exception when it registered its completion status.  The exception's message is: {2}",
                                    "The governance action throws a GovernanceServiceException in the hope that the hosting server is able to clean up.",
                                    "Review the exception messages that are logged about the same time as one of them will point to the root cause of the error."),

    /**
     * BAUDOT-SUBSCRIPTION-MANAGER-0017 - The {0} governance action service received a {1} exception when it registered a listener with the 
     * governance context.  The exception's message is: {2}
     */
    UNABLE_TO_REGISTER_LISTENER("BAUDOT-SUBSCRIPTION-MANAGER-0017",
                                AuditLogRecordSeverityLevel.INFO,
                                "The {0} governance action service received a {1} exception when it registered a listener with the governance context.  The exception's message is: {2}",
                                "The governance action service throws a GovernanceServiceException.",
                                "This is likely to be a configuration error.  Review the description of the exception's message to understand what is not set up correctly and " +
                                        "and follow its instructions."),

    /**
     * BAUDOT-SUBSCRIPTION-MANAGER-0018 - The {0} governance action service has no targets to operate on
     */
    NO_TARGETS("BAUDOT-SUBSCRIPTION-MANAGER-0018",
               AuditLogRecordSeverityLevel.ERROR,
               "The {0} governance action service has no targets to operate on",
               "The governance action service returns an INVALID completion status.",
               "This is an error in the way that the governance action service has been called." +
                       "Identify the way it was called which could be a direct invocation through the initiateGovernanceAction() method," +
                       "or as part of a governance action process.  Then correct this approach so that an action target is set up."),


    ;

    private final String                      logMessageId;
    private final AuditLogRecordSeverityLevel severity;
    private final String                      logMessage;
    private final String                      systemAction;
    private final String                      userAction;


    /**
     * The constructor for DistributeKafkaAuditCode expects to be passed one of the enumeration rows defined in
     * DistributeKafkaAuditCode above.   For example:
     * <br>
     *     DistributeKafkaAuditCode   auditCode = DistributeKafkaAuditCode.SERVER_NOT_AVAILABLE;
     * <br>
     * This will expand out to the 4 parameters shown below.
     *
     * @param messageId - unique id for the message
     * @param severity - severity of the message
     * @param message - text for the message
     * @param systemAction - description of the action taken by the system when the condition happened
     * @param userAction - instructions for resolving the situation, if any
     */
    BaudotAuditCode(String                      messageId,
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
        return "JacquardAuditCode{" +
                "logMessageId='" + logMessageId + '\'' +
                ", severity=" + severity +
                ", logMessage='" + logMessage + '\'' +
                ", systemAction='" + systemAction + '\'' +
                ", userAction='" + userAction + '\'' +
                '}';
    }
}
