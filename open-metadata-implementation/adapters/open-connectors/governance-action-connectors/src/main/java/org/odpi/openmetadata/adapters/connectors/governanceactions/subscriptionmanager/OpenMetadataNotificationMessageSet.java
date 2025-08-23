/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.governanceactions.subscriptionmanager;

import org.odpi.openmetadata.frameworks.auditlog.messagesets.MessageDefinition;
import org.odpi.openmetadata.frameworks.auditlog.messagesets.MessageSet;


/**
 * The OpenMetadataNotificationMessageSet is used to define the message content for the notifications.
 * The 5 fields in the enum are:
 * <ul>
 *     <li>Log Message Id - to uniquely identify the message</li>
 *     <li>Severity - is this an event, decision, action, error or exception</li>
 *     <li>Log Message Text - includes placeholder to allow additional values to be captured</li>
 *     <li>Additional Information - further parameters and data relating to the audit message (optional)</li>
 *     <li>SystemAction - describes the result of the situation</li>
 *     <li>UserAction - describes how a user should correct the situation</li>
 * </ul>
 */
public enum OpenMetadataNotificationMessageSet implements MessageSet
{
    /**
     * OPEN-METADATA-NOTIFICATION-SERVICE-0001 - Welcome to your subscription for notification type: {0} ({1})
     */
    NEW_SUBSCRIBER("OPEN-METADATA-NOTIFICATION-SERVICE-0001",
                   "Welcome to your subscription for notification type: {0} ({1})",
                   "The notification manager has received your notification request.  New notifications will be sent each time the monitored resources associated with your subscription have changed until you unsubscribe.",
                   "No specific action is required.  This message is to confirm that the subscription is in place.  You can unsubscribe from this notification type using the notification type guid supplied in the welcome message."),

    /**
     * OPEN-METADATA-NOTIFICATION-SERVICE-0002 - The subscription for the following notification type has be terminated: {0} ({1})
     */
    CANCELLED_SUBSCRIBER("OPEN-METADATA-NOTIFICATION-SERVICE-0002",
              "The subscription for the following notification type has be terminated: {0} ({1})",
              "The notification manager has removed you from the subscriber list and no further notifications will be sent.",
              "No specific action is required.  This message is to confirm the subscription has been cancelled. You may subscribe again at any time, using the notification type guid supplied in the cancellation message."),

    /**
     * OPEN-METADATA-NOTIFICATION-SERVICE-0003 - The monitored {0} resource {1} ({2}) has changed for notification type: {3} ({4})
     */
    MONITORED_RESOURCE_CHANGED("OPEN-METADATA-NOTIFICATION-SERVICE-0003",
              "The monitored {0} resource {1} ({2}) has changed for notification type: {3} ({4})",
              "The notification managed has detected a change in one of the monitored resources for the notification and has sent this notification to inform you.",
              "No specific action is required.  This message is to inform you of the change.  If you no longer which to receive these types of notifications, you can unsubscribe from this notification type using the notification type guid supplied in the welcome message."),

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
    OpenMetadataNotificationMessageSet(String                      messageId,
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
        return "OpenMetadataNotificationMessageSet{" +
                "logMessageId='" + logMessageId + '\'' +
                ", logMessage='" + logMessage + '\'' +
                ", systemAction='" + systemAction + '\'' +
                ", userAction='" + userAction + '\'' +
                '}';
    }
}
