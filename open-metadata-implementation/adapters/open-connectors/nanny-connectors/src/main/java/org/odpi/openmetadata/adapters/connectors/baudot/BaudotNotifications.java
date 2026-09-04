/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.baudot;

import org.odpi.openmetadata.frameworks.auditlog.MessageFormatter;
import org.odpi.openmetadata.frameworks.auditlog.messagesets.MessageDefinition;
import org.odpi.openmetadata.frameworks.openmetadata.enums.ActivityStatus;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.processes.actions.NotificationProperties;

import java.util.Date;


/**
 * Builds the properties of the notifications that the Baudot Subscription Manager sends.  Shared by the
 * catalog target processors (welcome, one-time and periodic notifications) and the connector's event
 * listener (monitored resource changes), so that every notification Baudot sends is shaped the same way.
 */
class BaudotNotifications
{
    private static final MessageFormatter messageFormatter = new MessageFormatter();


    /**
     * Build the properties for a notification.
     *
     * @param notificationDescription what the notification says - null means no notification of this kind is
     *                                sent, and the returned properties carry only a qualified name
     * @param connectorName name of the sending connector, used in the qualified name
     * @param notificationTypeGUID the notification type the notification belongs to
     * @param notificationCount the notification type's running count, used to keep qualified names unique
     * @return properties for the notification
     */
    static NotificationProperties getNotificationProperties(MessageDefinition notificationDescription,
                                                            String            connectorName,
                                                            String            notificationTypeGUID,
                                                            long              notificationCount)
    {
        Date                   notificationTime       = new Date();
        NotificationProperties notificationProperties = new NotificationProperties();

        notificationProperties.setQualifiedName(notificationProperties.getTypeName() + "::" + connectorName + "::" + notificationTypeGUID + "::" + notificationTime.getTime() + "::" + notificationCount);

        if (notificationDescription != null)
        {
            notificationProperties.setRequestedTime(notificationTime);
            notificationProperties.setStartTime(notificationTime);
            notificationProperties.setActivityStatus(ActivityStatus.FOR_INFO);
            notificationProperties.setDisplayName(notificationTime + " " + messageFormatter.getFormattedMessage(notificationDescription));
            notificationProperties.setSituation(notificationDescription.getSystemAction());
            notificationProperties.setExpectedBehaviour(notificationDescription.getUserAction());
        }

        return notificationProperties;
    }
}
