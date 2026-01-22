/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.baudot;

import org.odpi.openmetadata.frameworks.auditlog.messagesets.MessageDefinition;
import org.odpi.openmetadata.frameworks.auditlog.messagesets.MessageSet;


/**
 * The BaudotNotificationMessageSet is used to define the message content for the notifications from Baudot.
 * The 5 fields in the enum are:
 * <ul>
 *     <li>Log Message Id - to uniquely identify the message</li>
 *     <li>Severity - is this an event, decision, action, error, or exception</li>
 *     <li>Log Message Text - includes placeholder to allow additional values to be captured</li>
 *     <li>Additional Information - further parameters and data for the audit message (optional)</li>
 *     <li>SystemAction - describes the result of the situation</li>
 *     <li>UserAction - describes how a user should correct the situation</li>
 * </ul>
 */
public enum BaudotNotificationMessageSet implements MessageSet
{
    /**
     * BAUDOT-SUBSCRIPTION-MANAGEMENT-0001 - Welcome to your subscription for subscription type: {0} ({1})
     */
    NEW_SUBSCRIBER("BAUDOT-SUBSCRIPTION-MANAGEMENT-0001",
                   "Welcome to your subscription for product subscription type: {0} ({1})",
                   "The subscription manager has received your subscription request.  New notifications will be sent each time the monitored resources associated with your subscription have changed until you unsubscribe.",
                   "No specific action is required.  This message is to confirm that the subscription is in place.  You can unsubscribe from this subscription type using the subscription type guid supplied in the welcome message."),

    /**
     * BAUDOT-SUBSCRIPTION-MANAGEMENT-0002 - The subscription for the following subscription type has been terminated: {0} ({1})
     */
    CANCELLED_SUBSCRIBER("BAUDOT-SUBSCRIPTION-MANAGEMENT-0002",
              "The subscription for the following subscription type has been terminated: {0} ({1})",
              "The subscription manager has removed you from the subscriber list and no further notifications will be sent.",
              "No specific action is required.  This message is to confirm the subscription has been cancelled. You may subscribe again at any time, using the subscription type guid supplied in the cancellation message."),

    /**
     * BAUDOT-SUBSCRIPTION-MANAGEMENT-0003 - The monitored {0} resource {1} ({2}) has changed for subscription type: {3} ({4})
     */
    MONITORED_RESOURCE_CHANGED("BAUDOT-SUBSCRIPTION-MANAGEMENT-0003",
              "The monitored {0} resource {1} ({2}) has changed for subscription type: {3} ({4})",
              "The subscription manager has detected a change in one of the monitored resources for the subscription type and has sent this notification to inform you.",
              "No specific action is required.  This message is to inform you of the change.  If you no longer which to receive these types of notifications, you can unsubscribe from this subscription type using the subscription type guid supplied in the welcome message."),

    /**
     * BAUDOT-SUBSCRIPTION-MANAGEMENT-0004 - Your subscription to subscription type {0} ({1}) has been triggered
     */
    ONE_TIME_NOTIFICATION("BAUDOT-SUBSCRIPTION-MANAGEMENT-0004",
                          "Your subscription to {0} ({1}) has been triggered",
                          "The subscription manager has been requested to send this notification to you.  It is a one-time notification.",
                          "No specific action is required.  This message is to inform you of the one-time notification."),


    /**
     * BAUDOT-SUBSCRIPTION-MANAGEMENT-0005 - A regular notification for subscription type {0} ({1}) has been triggered.  It will trigger again in {2} milliseconds
     */
    PERIODIC_NOTIFICATION("BAUDOT-SUBSCRIPTION-MANAGEMENT-0005",
                          "A regular notification for subscription type {0} ({1}) has been triggered.  It will trigger again in {2} milliseconds",
                          "The subscription manager has been requested to send this notification to you.  It is a periodic notification.",
                          "No specific action is required.  This message is to inform you of the periodic notification."),

    ;

    private final String                      logMessageId;
    private final String                      logMessage;
    private final String                      systemAction;
    private final String                      userAction;


    /**
     * The constructor expects to be passed one of the enumeration rows defined above.   For example:
     * This will expand out to the 5 parameters shown below.
     *
     * @param messageId - unique id for the message
     * @param message - text for the message
     * @param systemAction - description of the action taken by the system when the condition happened
     * @param userAction - instructions for resolving the situation, if any
     */
    BaudotNotificationMessageSet(String                      messageId,
                                 String                      message,
                                 String                      systemAction,
                                 String                      userAction)
    {
        this.logMessageId = messageId;
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
    public MessageDefinition getMessageDefinition()
    {
        return new MessageDefinition(logMessageId,
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
    public MessageDefinition getMessageDefinition(String ...params)
    {
        MessageDefinition messageDefinition = new MessageDefinition(logMessageId,
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
        return "BaudotNotificationMessageSet{" +
                "logMessageId='" + logMessageId + '\'' +
                ", logMessage='" + logMessage + '\'' +
                ", systemAction='" + systemAction + '\'' +
                ", userAction='" + userAction + '\'' +
                '}';
    }
}
