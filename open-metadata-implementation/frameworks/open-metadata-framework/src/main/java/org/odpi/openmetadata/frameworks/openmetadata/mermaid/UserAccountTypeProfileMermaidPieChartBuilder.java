/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworks.openmetadata.mermaid;

import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.OpenMetadataRootElement;
import org.odpi.openmetadata.frameworks.openmetadata.properties.ReferenceableProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.filesandfolders.UserAccountProfileProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.security.ZoneMembershipProfileProperties;

import java.util.HashMap;
import java.util.Map;

/**
 * Creates a mermaid pie chart rendering of the userAccountType values from UserAccountProfile.
 */
public class UserAccountTypeProfileMermaidPieChartBuilder extends PieChartBuilderBase
{
    /**
     * Constructor for the pie chart builder.
     *
     * @param zone element to display
     */
    public UserAccountTypeProfileMermaidPieChartBuilder(OpenMetadataRootElement zone)
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
        String title = "Account Types in ";

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

            profile.put("Employees", profileProperties.getEmployeeAccountCount());
            profile.put("Contractors", profileProperties.getContractorAccountCount());
            profile.put("External Users", profileProperties.getExternalAccountCount());
            profile.put("Digital Users", profileProperties.getDigitalAccountCount());

            return profile;
        }

        return null;
    }
}
