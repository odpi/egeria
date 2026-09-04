/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.baudot;

import org.odpi.openmetadata.frameworks.integration.context.IntegrationContext;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.ElementHeader;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.RelatedMetadataElementSummary;
import org.odpi.openmetadata.frameworks.openmetadata.properties.AnchorsProperties;
import org.odpi.openmetadata.frameworks.openmetadata.search.ElementProperties;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


/**
 * The cache of resources monitored by the notification types that the Baudot Subscription Manager has been
 * given as catalog targets, indexed by the resource's GUID so that an incoming change event can be matched to
 * the notification types that care about it quickly.
 * <br><br>
 * The cache is shared between the catalog target processors, which fill it on each refresh, and the
 * connector's event listener, which reads it for every event and adjusts it as monitored resource
 * relationships come and go.  All access is synchronized on the instance.
 */
class MonitoredResources
{
    /*
     * Map from resource GUID to the notification types monitoring it.
     */
    private final Map<String, List<MonitoredResource>> monitoredResources = new HashMap<>();


    /**
     * Replace the monitored resources recorded for a notification type with the list just read from the
     * repository.  Called on each refresh of the notification type, so that resources unlinked since the
     * last refresh stop being watched and newly linked ones start.
     *
     * @param retrievedResources monitored resources of the notification type, or null if it has none
     * @param notificationTypeGUID unique identifier of the notification type
     * @param notificationTypeDisplayName display name of the notification type
     * @param integrationContext context, used to derive display names
     */
    synchronized void setMonitoredResources(List<RelatedMetadataElementSummary> retrievedResources,
                                            String                              notificationTypeGUID,
                                            String                              notificationTypeDisplayName,
                                            IntegrationContext                  integrationContext)
    {
        this.removeMonitoredNotificationType(notificationTypeGUID);

        if (retrievedResources != null)
        {
            for (RelatedMetadataElementSummary retrievedResource : retrievedResources)
            {
                if ((retrievedResource != null) && (retrievedResource.getRelatedElement() != null))
                {
                    this.addMonitoredElement(retrievedResource.getRelatedElement().getElementHeader().getGUID(),
                                             new MonitoredResource(retrievedResource,
                                                                   notificationTypeGUID,
                                                                   notificationTypeDisplayName,
                                                                   integrationContext));
                }
            }
        }
    }


    /**
     * Add a monitored resource learnt of from a change event.
     *
     * @param monitoredResourceRelationshipGUID the unique identifier for the monitored resource relationship
     * @param monitoredElement the element header of the monitored resource being added
     * @param monitoredElementProperties the properties of the monitored resource element
     * @param notificationTypeGUID the unique identifier for the notification type
     * @param notificationTypeDisplayName the display name of the notification type
     * @param integrationContext context, used to derive display names
     */
    synchronized void addMonitoredElement(String             monitoredResourceRelationshipGUID,
                                          ElementHeader      monitoredElement,
                                          ElementProperties  monitoredElementProperties,
                                          String             notificationTypeGUID,
                                          String             notificationTypeDisplayName,
                                          IntegrationContext integrationContext)
    {
        if (monitoredElement != null)
        {
            this.addMonitoredElement(monitoredElement.getGUID(),
                                     new MonitoredResource(monitoredResourceRelationshipGUID,
                                                           monitoredElement.getGUID(),
                                                           integrationContext.getGovernanceDefinitionClient().getDisplayName(monitoredElementProperties),
                                                           monitoredElement.getType().getTypeName(),
                                                           notificationTypeGUID,
                                                           notificationTypeDisplayName));
        }
    }


    /**
     * Add an entry to the map.
     *
     * @param resourceGUID unique identifier of the monitored resource
     * @param monitoredResource the notification type's interest in it
     */
    private void addMonitoredElement(String            resourceGUID,
                                     MonitoredResource monitoredResource)
    {
        List<MonitoredResource> resourceList = monitoredResources.computeIfAbsent(resourceGUID, guid -> new ArrayList<>());

        resourceList.add(monitoredResource);
    }


    /**
     * Remove a notification type's interest in a resource - the monitored resource relationship has been
     * deleted.
     *
     * @param monitoredElement the resource
     * @param notificationTypeGUID the notification type
     */
    synchronized void removeMonitoredElement(ElementHeader monitoredElement,
                                             String        notificationTypeGUID)
    {
        if (monitoredElement != null)
        {
            List<MonitoredResource> resourceList = monitoredResources.get(monitoredElement.getGUID());

            if (resourceList != null)
            {
                resourceList.removeIf(resource -> notificationTypeGUID.equals(resource.getNotificationTypeGUID()));

                if (resourceList.isEmpty())
                {
                    monitoredResources.remove(monitoredElement.getGUID());
                }
            }
        }
    }


    /**
     * Remove every interest a notification type has - it is no longer a catalog target of this connector,
     * or its monitored resources are about to be reloaded.
     *
     * @param notificationTypeGUID the notification type
     */
    synchronized void removeMonitoredNotificationType(String notificationTypeGUID)
    {
        Iterator<Map.Entry<String, List<MonitoredResource>>> entries = monitoredResources.entrySet().iterator();

        while (entries.hasNext())
        {
            List<MonitoredResource> resourceList = entries.next().getValue();

            resourceList.removeIf(resource -> notificationTypeGUID.equals(resource.getNotificationTypeGUID()));

            if (resourceList.isEmpty())
            {
                entries.remove();
            }
        }
    }


    /**
     * Return the notification types interested in an element - either because the element itself is a
     * monitored resource, or because it is anchored to one.
     *
     * @param potentialElement element header from an event
     * @return list of interests, or null if nobody is monitoring the element
     */
    synchronized List<MonitoredResource> isMonitored(ElementHeader potentialElement)
    {
        if (potentialElement != null)
        {
            List<MonitoredResource> resourceList = monitoredResources.get(potentialElement.getGUID());

            if ((resourceList == null) &&
                    (potentialElement.getAnchor() != null) &&
                    (potentialElement.getAnchor().getClassificationProperties() instanceof AnchorsProperties anchorsProperties) &&
                    (anchorsProperties.getAnchorGUID() != null))
            {
                resourceList = monitoredResources.get(anchorsProperties.getAnchorGUID());
            }

            if ((resourceList != null) && (! resourceList.isEmpty()))
            {
                return new ArrayList<>(resourceList);
            }
        }

        return null;
    }


    /**
     * Return the number of resources being monitored.
     *
     * @return count
     */
    synchronized int size()
    {
        return monitoredResources.size();
    }
}
