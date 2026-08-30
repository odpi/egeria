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
     * BAUDOT-SUBSCRIPTION-MANAGER-0002 - The {0} governance service is refreshing its cache for configured notification type {1} ({2}) where notifications are triggered by changes to its monitored resources.  {3} monitored resources are currently registered
     */
    MONITORED_RESOURCE_NOTIFICATION_TYPE( "BAUDOT-SUBSCRIPTION-MANAGER-0002",
                         AuditLogRecordSeverityLevel.INFO,
                         "The {0} governance service is refreshing its cache for configured notification type {1} ({2}) where notifications are triggered by changes to its monitored resources.   {3} monitored resources are currently registered",
                         "The governance service monitors the events generated when open metadata elements change.  Notifications are sent if one of the monitored resources (or anything anchored from it) changes, unless another notification has been sent out within the minimumNotificationInterval.",
                         "This notification pattern was selected because multipleNotificationsPermitted is set to true and notificationInterval is set to 0. Verify that this is the intended behaviour and that the correct elements are linked to this notification type using the MonitoredResource relationship.",
                         "https://egeria-project.org/concepts/notification-type/"),

    /**
     * BAUDOT-SUBSCRIPTION-MANAGER-0003 - The {0} governance service is refreshing its cache for configured notification type {1} ({2}) where only one notification is sent to each subscriber
     */
    ONE_TIME_NOTIFICATION_TYPE( "BAUDOT-SUBSCRIPTION-MANAGER-0003",
                              AuditLogRecordSeverityLevel.INFO,
                              "The {0} governance service is refreshing its cache for configured notification type {1} ({2}) where only one notification is sent to each subscriber",
                              "The governance service sends a notification to each subscriber registered when the service starts up and then monitors for new subscribers in order to send them a notification.",
                              "This notification pattern was selected because multipleNotificationsPermitted is set to false. Validate that this is the right pattern.",
                              "https://egeria-project.org/concepts/notification-type/"),

    /**
     * BAUDOT-SUBSCRIPTION-MANAGER-0005 - The {0} governance service is refreshing its cache for configured notification type {1} ({2}) where a notification is sent to each subscriber on a regular schedule every {3} minutes.  The next scheduled notification will be sent at {4}
     */
    PERIODIC_NOTIFICATION_TYPE("BAUDOT-SUBSCRIPTION-MANAGER-0005",
                      AuditLogRecordSeverityLevel.INFO,
                      "The {0} governance service is refreshing its cache for configured notification type {1} ({2}) where a notification is sent to each subscriber on a regular schedule every {3} minutes.  The next scheduled notification will be sent at {4}",
                      "The governance service sends a notification to each subscriber registered when the service starts up and then monitors for new subscribers in order to send them notifications.  Additional notifications are sent to each active subscriber every notification interval.",
                      "This notification pattern was selected because multipleNotificationsPermitted is set to true and notificationInterval is greater than 0. Validate that this is the intended behaviour and the notification interval is appropriate.",
                      "https://egeria-project.org/concepts/notification-type/"),
    /**
     * BAUDOT-SUBSCRIPTION-MANAGER-0011 - The {0} governance service is not monitoring notification type {1} ({2}) because it could not be retrieved, or is not a notification type
     */
    UNREADABLE_NOTIFICATION_TYPE("BAUDOT-SUBSCRIPTION-MANAGER-0011",
                                 AuditLogRecordSeverityLevel.ERROR,
                                 "The {0} governance service is not monitoring notification type {1} ({2}) because it could not be retrieved, or is not a notification type",
                                 "The notification type is skipped.  Its subscribers receive nothing.",
                                 "Check that the element named as an action target of this service is a notification type, and that this service's userId can read it.",
                                 "https://egeria-project.org/concepts/notification-type/"),

    /**
     * BAUDOT-SUBSCRIPTION-MANAGER-0012 - The {0} governance service is not yet monitoring notification type {1} because it is planned to start at {2}
     */
    NOTIFICATION_TYPE_NOT_STARTED("BAUDOT-SUBSCRIPTION-MANAGER-0012",
                                  AuditLogRecordSeverityLevel.INFO,
                                  "The {0} governance service is not yet monitoring notification type {1} because it is planned to start at {2}",
                                  "The notification type is skipped until its planned start date has passed.",
                                  "No action is required if the start date is intended.  A notification type whose subscribers are waiting for data has the wrong start date.",
                                  "https://egeria-project.org/concepts/notification-type/"),

    /**
     * BAUDOT-SUBSCRIPTION-MANAGER-0013 - The {0} governance service refreshed its caches: {1} notification type(s) configured, {2} being monitored for the first time by this service
     */
    CACHE_REFRESHED("BAUDOT-SUBSCRIPTION-MANAGER-0013",
                    AuditLogRecordSeverityLevel.INFO,
                    "The {0} governance service refreshed its caches: {1} notification type(s) configured, {2} being monitored for the first time by this service",
                    "The service monitors the notification types it has been given and notifies their subscribers.",
                    "No action is required.  A refresh that reports zero notification types is monitoring nothing, and subscriptions to any product will not be delivered.",
                    "https://egeria-project.org/concepts/notification-type/"),

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
    BaudotAuditCode(String                      messageId,
                    AuditLogRecordSeverityLevel severity,
                    String                      message,
                    String                      systemAction,
                    String                      userAction)
    {
        this(messageId, severity, message, systemAction, userAction, null);
    }


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
     * @param url link to a page that describes the component or concept behind
     *            this message - null if there is no suitable page
     */
    BaudotAuditCode(String                      messageId,
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
        return "JacquardAuditCode{" +
                "logMessageId='" + logMessageId + '\'' +
                ", severity=" + severity +
                ", logMessage='" + logMessage + '\'' +
                ", systemAction='" + systemAction + '\'' +
                ", userAction='" + userAction + '\'' +
                ", url='" + url + '\'' +
                '}';
    }
}
