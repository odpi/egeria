/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.baudot;

import org.odpi.openmetadata.frameworks.auditlog.messagesets.MessageDefinition;
import org.odpi.openmetadata.frameworks.integration.context.IntegrationContext;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.RelatedMetadataElementSummary;


/**
 * Represents one resource that a notification type is monitoring, along with the notifications to send its
 * subscribers when the resource changes.  The same element may be monitored by several notification types,
 * in which case there is one of these for each.
 */
class MonitoredResource
{
    private final String            monitoredResourceRelationshipGUID;
    private final String            notificationTypeGUID;
    private final String            notificationTypeDisplayName;
    private final MessageDefinition firstNotificationDescription;
    private final MessageDefinition nextNotificationDescription;
    private final MessageDefinition lastNotificationDescription;


    /**
     * Constructor using resource information retrieved from the repository on a refresh.
     *
     * @param retrievedResource resource from the repository
     * @param notificationTypeGUID associated notification type
     * @param notificationTypeDisplayName the display name of the notification type
     * @param integrationContext context, used to derive display names
     */
    MonitoredResource(RelatedMetadataElementSummary retrievedResource,
                      String                        notificationTypeGUID,
                      String                        notificationTypeDisplayName,
                      IntegrationContext            integrationContext)
    {
        this(retrievedResource.getRelationshipHeader().getGUID(),
             retrievedResource.getRelatedElement().getElementHeader().getGUID(),
             integrationContext.getGovernanceDefinitionClient().getDisplayName(retrievedResource.getRelatedElement().getProperties()),
             retrievedResource.getRelatedElement().getElementHeader().getType().getTypeName(),
             notificationTypeGUID,
             notificationTypeDisplayName);
    }


    /**
     * Constructor using resource information from a change event.
     *
     * @param monitoredResourceRelationshipGUID relationship GUID for the monitored resource
     * @param monitoredResourceGUID unique identifier of the monitored resource
     * @param monitoredResourceDisplayName display name of the monitored resource
     * @param monitoredResourceTypeName open metadata type name of the monitored resource
     * @param notificationTypeGUID associated notification type
     * @param notificationTypeDisplayName the display name of the notification type
     */
    MonitoredResource(String monitoredResourceRelationshipGUID,
                      String monitoredResourceGUID,
                      String monitoredResourceDisplayName,
                      String monitoredResourceTypeName,
                      String notificationTypeGUID,
                      String notificationTypeDisplayName)
    {
        this.notificationTypeGUID              = notificationTypeGUID;
        this.notificationTypeDisplayName       = notificationTypeDisplayName;
        this.monitoredResourceRelationshipGUID = monitoredResourceRelationshipGUID;

        this.firstNotificationDescription = BaudotNotificationMessageSet.NEW_SUBSCRIBER.getMessageDefinition(notificationTypeDisplayName,
                                                                                                             notificationTypeGUID);

        this.nextNotificationDescription = BaudotNotificationMessageSet.MONITORED_RESOURCE_CHANGED.getMessageDefinition(monitoredResourceTypeName,
                                                                                                                        monitoredResourceDisplayName,
                                                                                                                        monitoredResourceGUID,
                                                                                                                        notificationTypeDisplayName,
                                                                                                                        notificationTypeGUID);

        this.lastNotificationDescription = BaudotNotificationMessageSet.CANCELLED_SUBSCRIBER.getMessageDefinition(notificationTypeDisplayName,
                                                                                                                  notificationTypeGUID);
    }


    /**
     * Return the notification type GUID for this resource.
     *
     * @return string
     */
    String getNotificationTypeGUID()
    {
        return notificationTypeGUID;
    }


    /**
     * Return the notification type's display name for this resource.
     *
     * @return string
     */
    String getNotificationTypeDisplayName()
    {
        return notificationTypeDisplayName;
    }


    /**
     * Return the relationship GUID for this resource.
     *
     * @return string
     */
    String getMonitoredResourceRelationshipGUID()
    {
        return monitoredResourceRelationshipGUID;
    }


    /**
     * Return the description of the first notification sent to a subscriber.
     *
     * @return message definition
     */
    MessageDefinition getFirstNotificationDescription()
    {
        return firstNotificationDescription;
    }


    /**
     * Return the description of the notification sent when the resource changes.
     *
     * @return message definition
     */
    MessageDefinition getNextNotificationDescription()
    {
        return nextNotificationDescription;
    }


    /**
     * Return the description of the last notification sent to a subscriber.
     *
     * @return message definition
     */
    MessageDefinition getLastNotificationDescription()
    {
        return lastNotificationDescription;
    }
}
