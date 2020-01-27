/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Capture the differences between instance properties.  Just as in the Differences class from which this inherits, it
 * captures the differences in instance properties in the following areas:
 * <ul>
 *     <li>OnlyOnLeft - values that only appear on the 'left' object (the one from which differences were checked)</li>
 *     <li>OnlyOnRight - values that only appear on the 'right' object (the one passed as an argument when differences
 *          were calculated</li>
 *     <li>Names - the names of the properties that differ, including those from OnlyOnLeft, OnlyOnRight and where the
 *          property has a value on both sides but that value is different</li>
 * </ul>
 */
public class InstancePropertiesDifferences extends Differences
{

    /**
     * Construct a new set of differences between two sets of instance properties.
     */
    public InstancePropertiesDifferences()
    {
        super();
    }

    /**
     * Determine if there is a difference between the provided instance properties' values, and capture each of the
     * differences and similarities.
     *
     * @param left the instance properties from the first instance
     * @param right the instance properties from the second instance
     */
    public void check(InstanceProperties left, InstanceProperties right)
    {
        if (left == null && right == null)
        {
            // Do nothing, they are equal and nothing to capture
        }
        else if (left == null)
        {
            setOnlyOnOne(onlyOnRight, right.getInstanceProperties());
        }
        else if (right == null)
        {
            setOnlyOnOne(onlyOnLeft, left.getInstanceProperties());
        }
        else
        {
            Map<String, InstancePropertyValue> propertiesLeft = left.getInstanceProperties();
            Map<String, InstancePropertyValue> propertiesRight = right.getInstanceProperties();
            if (propertiesLeft == null && propertiesRight == null)
            {
                // Do nothing, they are equal and nothing to capture
            }
            else if (propertiesLeft == null)
            {
                setOnlyOnOne(onlyOnRight, propertiesRight);
            }
            else if (propertiesRight == null)
            {
                setOnlyOnOne(onlyOnLeft, left.getInstanceProperties());
            }
            else
            {
                if (propertiesLeft.isEmpty() && propertiesRight.isEmpty())
                {
                    // Do nothing...
                }
                else if (propertiesLeft.isEmpty())
                {
                    setOnlyOnOne(onlyOnRight, propertiesRight);
                }
                else if (propertiesRight.isEmpty())
                {
                    setOnlyOnOne(onlyOnLeft, left.getInstanceProperties());
                }
                else
                {
                    calculateDifferences(propertiesLeft, propertiesRight);
                }
            }
        }
    }

    /**
     * Set these differing classifications on one side only.
     *
     * @param map the map into which to place the differences
     * @param properties the properties from the side that is (probably) not empty / null
     */
    private void setOnlyOnOne(Map<String, Object> map, Map<String, InstancePropertyValue> properties)
    {
        // Note that if the provided properties map is also empty, then there are no differences to capture
        // (so if one is null and the other is empty, they will still be considered the same)
        if (!properties.isEmpty())
        {
            for (Map.Entry<String, InstancePropertyValue> entry : properties.entrySet())
            {
                String propertyName = entry.getKey();
                map.put(propertyName, entry.getValue());
            }
        }
    }

    /**
     * Calculate the differences between the two sets of properties, neither of which is null or empty at this point.
     *
     * @param left the properties from one instance
     * @param right the properties from the other instance
     */
    private void calculateDifferences(Map<String, InstancePropertyValue> left, Map<String, InstancePropertyValue> right)
    {
        Set<String> propertiesHandled = new HashSet<>();
        for (Map.Entry<String, InstancePropertyValue> entryLeft : left.entrySet()) {
            String propertyNameLeft = entryLeft.getKey();
            check(propertyNameLeft, entryLeft.getValue(), right.getOrDefault(propertyNameLeft, null));
            propertiesHandled.add(propertyNameLeft);
        }
        for (Map.Entry<String, InstancePropertyValue> entryRight : right.entrySet()) {
            String propertyNameRight = entryRight.getKey();
            // Skip any properties that we have already checked to avoid duplicate differences and unnecessary processing
            if (!propertiesHandled.contains(propertyNameRight))
            {
                check(propertyNameRight, left.getOrDefault(propertyNameRight, null), entryRight.getValue());
            }
        }
    }

}
