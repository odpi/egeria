/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.pepys;

/**
 * PendingUsageActivity accumulates the activity observed for a single element since its UsageActivity
 * classification was last written.  There is one count for each kind of activity, and the counts are added to the
 * classification's values on the next refresh.
 */
public class PendingUsageActivity
{
    private final long[] counts = new long[ObservedActivity.UsageCount.values().length];


    /**
     * Default constructor
     */
    public PendingUsageActivity()
    {
    }


    /**
     * Increment the count for an activity.
     *
     * @param usageCount count to increment
     */
    public void increment(ObservedActivity.UsageCount usageCount)
    {
        if (usageCount != null)
        {
            counts[usageCount.ordinal()]++;
        }
    }


    /**
     * Add the counts from another set of pending activity.  This is used to put back the counts that could
     * not be written so that they are tried again on the next refresh.
     *
     * @param other counts to add
     */
    public void add(PendingUsageActivity other)
    {
        if (other != null)
        {
            for (int i = 0; i < counts.length; i++)
            {
                counts[i] += other.counts[i];
            }
        }
    }


    /**
     * Return the number of activities of a particular kind that have been observed.
     *
     * @param usageCount kind of activity
     * @return long
     */
    public long getCount(ObservedActivity.UsageCount usageCount)
    {
        return counts[usageCount.ordinal()];
    }


    /**
     * Return the total number of activities observed.
     *
     * @return long
     */
    public long getTotal()
    {
        long total = 0L;

        for (long count : counts)
        {
            total = total + count;
        }

        return total;
    }


    /**
     * JSON-style toString
     *
     * @return string of property names and values
     */
    @Override
    public String toString()
    {
        StringBuilder stringBuilder = new StringBuilder("PendingUsageActivity{");

        for (ObservedActivity.UsageCount usageCount : ObservedActivity.UsageCount.values())
        {
            if (usageCount.ordinal() > 0)
            {
                stringBuilder.append(", ");
            }

            stringBuilder.append(usageCount.name()).append("=").append(counts[usageCount.ordinal()]);
        }

        return stringBuilder.append('}').toString();
    }
}
