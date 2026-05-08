/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworks.openmetadata.mermaid;

import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.OpenMetadataRootElement;

import java.util.Map;

/**
 * Creates a mermaid pie chart based on the supplied profile.
 * Pie charts do not scale well, so the number of segments is restricted to 15.
 * The values for the remaining segments are summed and displayed as "Others".
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
                               String title,
                               Map<String, Long> profile)
    {
        if ((element != null) && (profile != null))
        {
            mermaidPieChart.append("pie\n");
            mermaidPieChart.append("title ");
            mermaidPieChart.append(title);
            mermaidPieChart.append("\n");

            long others = this.addLargestValue(profile, 0, 0);

            if (others > 0)
            {
                mermaidPieChart.append("\"Others (");
                mermaidPieChart.append(others);
                mermaidPieChart.append(")\" : ");
                mermaidPieChart.append(others);
                mermaidPieChart.append("\n");
            }
        }
        else
        {
            mermaidPieChart = null;
        }
    }


    /**
     * Find the next largest value in the profile.
     *
     * @param profile         map of profile values
     * @param previousLargest previous largest value
     * @return next largest value or 0 if none found
     */
    private long getNextLargestValue(Map<String, Long> profile, long previousLargest)
    {
        long largestValue = 0;

        /*
         * Find the largest value in the profile.
         */
        for (String segmentName : profile.keySet())
        {
            if (segmentName != null)
            {
                long value = profile.get(segmentName);

                if ((value > largestValue) && ((previousLargest == 0) || (value < previousLargest)))
                {
                    largestValue = value;
                }
            }
        }

        if (largestValue == previousLargest)
        {
            return 0L;
        }

        return largestValue;
    }


    /**
     * Iteratively add the largest value to the profile up to 14 entries, then sum the remaining others, and return that sum.
     *
     * @param profile         map of profile values
     * @param previousLargest previous largest value found
     * @param numberInPie     current entries in pie chart
     * @return sum of elements that did not fit in the pie chart
     */
    private long addLargestValue(Map<String, Long> profile, long previousLargest, long numberInPie)
    {
        long newNumberInPie   = numberInPie;
        long nextLargestValue = this.getNextLargestValue(profile, previousLargest);

        if (nextLargestValue > 0)
        {
            /*
             * There are more values to add to the pie chart.
             */
            if (numberInPie < 15)
            {
                long skippedValueSum = 0L;

                /*
                 * Still space to add more entries.
                 */
                for (String segmentName : profile.keySet())
                {
                    if (segmentName != null)
                    {
                        long value = profile.get(segmentName);
                        if (value == nextLargestValue)
                        {
                            if (newNumberInPie < 15)
                            {
                                mermaidPieChart.append("\"");
                                mermaidPieChart.append(segmentName);
                                mermaidPieChart.append(" (");
                                mermaidPieChart.append(value);
                                mermaidPieChart.append(")\" : ");
                                mermaidPieChart.append(profile.get(segmentName));
                                mermaidPieChart.append("\n");

                                newNumberInPie++;
                            }
                            else
                            {
                                skippedValueSum += value;
                            }
                        }
                    }
                }

                /*
                 * Recurse to add the next largest value.
                 */
                return skippedValueSum + this.addLargestValue(profile, nextLargestValue, newNumberInPie);
            }
            else
            {
                /*
                 * There are additional values that don't fit in the Pie Chart.
                 */
                long otherValueSum = this.getNextLargestValue(profile, previousLargest);

                for (String segmentName : profile.keySet())
                {
                    if (segmentName != null)
                    {
                        long value = profile.get(segmentName);
                        if (value == nextLargestValue)
                        {
                            mermaidPieChart.append("\"");
                            mermaidPieChart.append(segmentName);
                            mermaidPieChart.append(" (");
                            mermaidPieChart.append(value);
                            mermaidPieChart.append(")\" : ");
                            mermaidPieChart.append(profile.get(segmentName));
                            mermaidPieChart.append("\n");

                            newNumberInPie++;
                        }
                    }
                }

                return otherValueSum;
            }
        }

        return 0L;
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
