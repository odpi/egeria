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
public class PieChartBuilderBase
{
    private StringBuilder mermaidPieChart = new StringBuilder();


    /**
     * Constructor for the pie chart builder.
     *
     * @param element element to display
     */
    public PieChartBuilderBase(OpenMetadataRootElement element,
                               String                  title,
                               Map<String, Long>       profile)
    {
        if ((element != null) && (profile != null))
        {
            mermaidPieChart.append("pie\n");
            mermaidPieChart.append("title ");
            mermaidPieChart.append(title);
            mermaidPieChart.append("\n");

            for (String segmentName : profile.keySet())
            {
                if (segmentName != null)
                {
                    mermaidPieChart.append("\"");
                    mermaidPieChart.append(segmentName);
                    mermaidPieChart.append("\" : ");
                    mermaidPieChart.append(profile.get(segmentName));
                    mermaidPieChart.append("\n");
                }
            }
        }
        else
        {
            mermaidPieChart = null;
        }
    }


    /**
     * Return the mermaid pie chart.
     *
     * @return string
     */
    public String getMermaidPieChart()
    {
        if (mermaidPieChart != null)
        {
            return mermaidPieChart.toString();
        }

        return null;
    }
}
