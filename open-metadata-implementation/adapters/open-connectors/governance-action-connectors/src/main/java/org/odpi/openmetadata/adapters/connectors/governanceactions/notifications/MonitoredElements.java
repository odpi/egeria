/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adapters.connectors.governanceactions.notifications;

import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.ElementHeader;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.OpenMetadataRootElement;
import org.odpi.openmetadata.frameworks.openmetadata.properties.AnchorsProperties;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Manage the list of monitored elements and support queries.
 */
public class MonitoredElements
{
    private final Map<String, ElementHeader> monitoredElements = new HashMap<>();

    /**
     * Set up the monitored elements.
     *
     * @param initialResources list of elements for the notification type.
     */
    public synchronized void setMonitoredElements(List<OpenMetadataRootElement> initialResources)
    {
        monitoredElements.clear();

        if (initialResources != null)
        {
            for (OpenMetadataRootElement initialResource : initialResources)
            {
                if (initialResource != null)
                {
                    this.addMonitoredElement(initialResource.getElementHeader());
                }
            }
        }
    }


    /**
     * Add a new element to the map.
     *
     * @param monitoredElement new resource
     */
    public synchronized void addMonitoredElement(ElementHeader monitoredElement)
    {
        if (monitoredElement != null)
        {
            monitoredElements.put(monitoredElement.getGUID(), monitoredElement);
        }
    }


    /**
     * Remove a element from the map.
     *
     * @param monitoredElement old resource
     */
    public synchronized void removeMonitoredElement(ElementHeader monitoredElement)
    {
        if (monitoredElement != null)
        {
            monitoredElements.remove(monitoredElement.getGUID());
        }
    }


    /**
     * Is this one of the monitored elements?
     *
     * @param potentialElement resource header
     * @return boolean - true for monitored element
     */
    public synchronized boolean isMonitored(ElementHeader potentialElement)
    {
        if (potentialElement != null)
        {
            if (monitoredElements.containsKey(potentialElement.getGUID()))
            {
                return true;
            }

            if ((potentialElement.getAnchor() != null) &&
                    (potentialElement.getAnchor().getClassificationProperties() instanceof AnchorsProperties anchorsProperties) &&
                    (anchorsProperties.getAnchorGUID() != null))
            {
                return monitoredElements.containsKey(anchorsProperties.getAnchorGUID());
            }
        }

        return false;
    }
}
