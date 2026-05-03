/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworks.openmetadata.mermaid;

import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.OpenMetadataRootElement;
import org.odpi.openmetadata.frameworks.openmetadata.properties.ReferenceableProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.security.ZoneMembershipProfileProperties;

import java.util.Map;

/**
 * Creates a mermaid pie chart rendering of the zone membership profile.
 */
public class ZoneProfileAllMermaidPieChartBuilder extends PieChartBuilderBase
{
    /**
     * Constructor for the pie chart builder.
     *
     * @param zone element to display
     */
    public ZoneProfileAllMermaidPieChartBuilder(OpenMetadataRootElement zone)
    {
        super(zone, getTitle(zone), getProfile(zone));
    }


    /**
     * Return the title for the pie chart.
     *
     * @param element attached element
     * @return title for the pie chart
     */
    private static String getTitle(OpenMetadataRootElement element)
    {
        String title = "All Elements of ";

        if (element.getProperties() instanceof ReferenceableProperties properties)
        {
            return title + properties.getDisplayName();
        }
        else
        {
            return title + element.getElementHeader().getGUID();
        }
    }


    /**
     * Return the profile for the element.
     *
     * @param element element to display
     * @return profile for the element
     */
    private static Map<String, Long> getProfile(OpenMetadataRootElement element)
    {
        if ((element.getElementHeader().getZoneMembershipProfile() != null) &&
                (element.getElementHeader().getZoneMembershipProfile().getClassificationProperties() instanceof ZoneMembershipProfileProperties zoneMembershipProfileProperties) &&
                (zoneMembershipProfileProperties.getAllTypeMembership() != null))
        {
            return zoneMembershipProfileProperties.getAllTypeMembership();
        }

        return null;
    }
}
