/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworks.openmetadata.mermaid;

import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.OpenMetadataRootElement;
import org.odpi.openmetadata.frameworks.openmetadata.properties.ReferenceableProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.filesandfolders.UserAccountProfileProperties;

import java.util.HashMap;
import java.util.Map;

/**
 * Creates a mermaid pie chart rendering of the userAccountStatus values from UserAccountProfile.
 */
public class UserAccountStatusProfileMermaidPieChartBuilder extends PieChartBuilderBase
{
    /**
     * Constructor for the pie chart builder.
     *
     * @param zone element to display
     */
    public UserAccountStatusProfileMermaidPieChartBuilder(OpenMetadataRootElement zone)
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
        String title = "Account Statuses in ";

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
        if ((element.getElementHeader().getUserAccountProfile() != null) &&
                (element.getElementHeader().getUserAccountProfile().getClassificationProperties() instanceof UserAccountProfileProperties profileProperties) &&
                (profileProperties.getUserAccountCount() > 0))
        {
            Map<String, Long> profile = new HashMap<>();

            profile.put("Active",   profileProperties.getActiveAccountCount());
            profile.put("Expired",  profileProperties.getExpiredAccountCount());
            profile.put("Locked",   profileProperties.getLockedAccountCount());
            profile.put("Disabled", profileProperties.getDisabledAccountCount());

            return profile;
        }

        return null;
    }
}
