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
     * BAUDOT-SUBSCRIPTION-MANAGER-0001 - The {0} integration connector is starting; it is connected to metadata access server {1} on platform {2}
     */
    STARTING_CONNECTOR("BAUDOT-SUBSCRIPTION-MANAGER-0001",
                       AuditLogRecordSeverityLevel.STARTUP,
                       "The {0} integration connector is starting; it is connected to metadata access server {1} on platform {2}",
                       "The connector registers to receive metadata change events, and then waits for the integration daemon to refresh it.  Each refresh notifies the subscribers of every notification type the connector has been given as a catalog target.",
                       "No action is required.  The notification types this connector looks after are its catalog targets; add one to have its subscribers notified.",
                       "https://egeria-project.org/concepts/notification-type/"),

    /**
     * BAUDOT-SUBSCRIPTION-MANAGER-0002 - The {0} integration connector has refreshed notification type {1} ({2}) where notifications are triggered by changes to its monitored resources.  {3} monitored resources are currently registered
     */
    MONITORED_RESOURCE_NOTIFICATION_TYPE("BAUDOT-SUBSCRIPTION-MANAGER-0002",
                                         AuditLogRecordSeverityLevel.INFO,
                                         "The {0} integration connector has refreshed notification type {1} ({2}) where notifications are triggered by changes to its monitored resources.  {3} monitored resources are currently registered",
                                         "The connector monitors the events generated when open metadata elements change.  Notifications are sent if one of the monitored resources (or anything anchored from it) changes, unless another notification has been sent out within the minimumNotificationInterval.",
                                         "This notification pattern was selected because the notification type has monitored resources. Verify that this is the intended behaviour and that the correct elements are linked to this notification type using the MonitoredResource relationship.",
                                         "https://egeria-project.org/concepts/notification-type/"),

    /**
     * BAUDOT-SUBSCRIPTION-MANAGER-0003 - The {0} integration connector has refreshed notification type {1} ({2}) where only one notification is sent to each subscriber
     */
    ONE_TIME_NOTIFICATION_TYPE("BAUDOT-SUBSCRIPTION-MANAGER-0003",
                               AuditLogRecordSeverityLevel.INFO,
                               "The {0} integration connector has refreshed notification type {1} ({2}) where only one notification is sent to each subscriber",
                               "The connector sends a notification to each subscriber that has not yet received one.  New subscribers are noticed on each refresh.",
                               "This notification pattern was selected because multipleNotificationsPermitted is set to false. Validate that this is the right pattern.",
                               "https://egeria-project.org/concepts/notification-type/"),

    /**
     * BAUDOT-SUBSCRIPTION-MANAGER-0004 - The {0} integration connector is stopping
     */
    CONNECTOR_STOPPING("BAUDOT-SUBSCRIPTION-MANAGER-0004",
                       AuditLogRecordSeverityLevel.SHUTDOWN,
                       "The {0} integration connector is stopping",
                       "The connector stops listening for metadata change events.  No further notifications are sent until it is restarted.",
                       "No action is required if the integration daemon is shutting down.  Otherwise check why the connector was stopped.",
                       "https://egeria-project.org/concepts/notification-type/"),

    /**
     * BAUDOT-SUBSCRIPTION-MANAGER-0005 - The {0} integration connector has refreshed notification type {1} ({2}) where a notification is sent to each subscriber on a regular schedule no more often than every {3} milliseconds.  The connector's next scheduled refresh is at {4}
     */
    PERIODIC_NOTIFICATION_TYPE("BAUDOT-SUBSCRIPTION-MANAGER-0005",
                               AuditLogRecordSeverityLevel.INFO,
                               "The {0} integration connector has refreshed notification type {1} ({2}) where a notification is sent to each subscriber on a regular schedule no more often than every {3} milliseconds.  The connector's next scheduled refresh is at {4}",
                               "The connector sends a notification to each new subscriber, and a further notification to each existing subscriber on every refresh that falls outside the notification type's minimum interval.  The refresh interval is part of the connector's configuration in the integration daemon.",
                               "This notification pattern was selected because multipleNotificationsPermitted is set to true and the notification type has no monitored resources. Validate that this is the intended behaviour and that the connector's refresh interval and the notification type's minimum interval are appropriate together.",
                               "https://egeria-project.org/concepts/notification-type/"),

    /**
     * BAUDOT-SUBSCRIPTION-MANAGER-0006 - The {0} integration connector was unable to refresh notification type {1} ({2}); the {3} exception had message {4}
     */
    NOTIFICATION_TYPE_REFRESH_FAILED("BAUDOT-SUBSCRIPTION-MANAGER-0006",
                                     AuditLogRecordSeverityLevel.EXCEPTION,
                                     "The {0} integration connector was unable to refresh notification type {1} ({2}); the {3} exception had message {4}",
                                     "The notification type is skipped on this refresh and tried again on the next.  The connector's other notification types are unaffected.",
                                     "Use the exception message to determine the cause, and correct it before the next refresh.",
                                     "https://egeria-project.org/concepts/notification-type/"),

    /**
     * BAUDOT-SUBSCRIPTION-MANAGER-0007 - The {0} integration connector received a {1} exception while processing a change event for element {2}; the message was {3}
     */
    EVENT_PROCESSING_FAILED("BAUDOT-SUBSCRIPTION-MANAGER-0007",
                            AuditLogRecordSeverityLevel.EXCEPTION,
                            "The {0} integration connector received a {1} exception while processing a change event for element {2}; the message was {3}",
                            "The event is dropped.  Any notification it should have prompted is not sent.",
                            "Use the exception message to determine the cause.  If subscribers have missed a change, the next change to the same resource will be notified normally.",
                            "https://egeria-project.org/concepts/notification-type/"),

    /**
     * BAUDOT-SUBSCRIPTION-MANAGER-0011 - The {0} integration connector is not monitoring notification type {1} ({2}) because it could not be retrieved, or is not a notification type
     */
    UNREADABLE_NOTIFICATION_TYPE("BAUDOT-SUBSCRIPTION-MANAGER-0011",
                                 AuditLogRecordSeverityLevel.ERROR,
                                 "The {0} integration connector is not monitoring notification type {1} ({2}) because it could not be retrieved, or is not a notification type",
                                 "The notification type is skipped.  Its subscribers receive nothing.",
                                 "Check that the element attached as a catalog target of this connector is a notification type, and that this connector's userId can read it.",
                                 "https://egeria-project.org/concepts/notification-type/"),

    /**
     * BAUDOT-SUBSCRIPTION-MANAGER-0013 - The {0} integration connector has completed a refresh: {1} notification type(s) are its catalog targets, and {2} resource(s) are being monitored for changes
     */
    REFRESH_COMPLETE("BAUDOT-SUBSCRIPTION-MANAGER-0013",
                     AuditLogRecordSeverityLevel.INFO,
                     "The {0} integration connector has completed a refresh: {1} notification type(s) are its catalog targets, and {2} resource(s) are being monitored for changes",
                     "The connector notifies the subscribers of the notification types it has been given as catalog targets.",
                     "No action is required.  A refresh that reports zero notification types is monitoring nothing, and subscriptions to any product will not be delivered: check that the connector that creates the notification types is adding them to this connector as catalog targets.",
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
     * The constructor for BaudotAuditCode expects to be passed one of the enumeration rows defined above.
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
        return "BaudotAuditCode{" +
                "logMessageId='" + logMessageId + '\'' +
                ", severity=" + severity +
                ", logMessage='" + logMessage + '\'' +
                ", systemAction='" + systemAction + '\'' +
                ", userAction='" + userAction + '\'' +
                ", url='" + url + '\'' +
                '}';
    }
}
