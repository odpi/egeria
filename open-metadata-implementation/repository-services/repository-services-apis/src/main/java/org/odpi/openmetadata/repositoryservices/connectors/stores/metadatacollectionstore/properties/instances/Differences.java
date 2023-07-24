/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances;

import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

/**
 * General class for capturing differences.  Captures the following areas:
 * <ul>
 *     <li>OnlyOnLeft - values that only appear on the 'left' object (the one from which differences were checked)</li>
 *     <li>OnlyOnRight - values that only appear on the 'right' object (the one passed as an argument when differences
 *          were calculated).</li>
 *     <li>Names - the names of the things that differ, including those from OnlyOnLeft, OnlyOnRight and where the
 *          thing has a value on both sides but that value is different</li>
 * </ul>
 * Every difference captures both the left-side value and the right-side value (except where it is OnlyOnLeft or
 * OnlyOnRight, in which case the other side's value will always be null).
 */
public abstract class Differences
{

    protected Map<String, ValuePair> differing;
    protected Map<String, Object> matching;
    protected Map<String, Object> onlyOnLeft;
    protected Map<String, Object> onlyOnRight;

    /**
     * Defines the valid values for the differences.
     */
    public enum SidePresent
    {
        /**
         * OnlyOnLeft - values that only appear on the 'left' object (the one from which differences were checked)
         */
        LEFT_ONLY,

        /**
         * OnlyOnRight - values that only appear on the 'right' object (the one passed as an argument when differences
         * were calculated)
         */
        RIGHT_ONLY,

        /**
         * No differences.
         */
        NEITHER,

        /**
         * Differences on both sides.
         */
        BOTH
    }

    /**
     * Construct a new set of differences.
     */
    public Differences()
    {
        this.differing = new TreeMap<>();
        this.matching = new TreeMap<>();
        this.onlyOnLeft = new TreeMap<>();
        this.onlyOnRight = new TreeMap<>();
    }

    /**
     * Returns true if the two have any differences (are not equal), otherwise false.
     *
     * @return boolean
     */
    public boolean hasDifferences()
    {
        return !differing.isEmpty() || !onlyOnLeft.isEmpty() || !onlyOnRight.isEmpty();
    }

    /**
     * Returns the set of names of the things that differ.
     *
     * @return {@code Set<String>}
     */
    public Set<String> getNames()
    {
        Set<String> set = new TreeSet<>();
        set.addAll(differing.keySet());
        set.addAll(onlyOnLeft.keySet());
        set.addAll(onlyOnRight.keySet());
        return set;
    }

    /**
     * Returns true if the value mapped to the provided name differs, otherwise false if it is the same in both.
     *
     * @param name the name of the thing to check
     * @return boolean
     */
    public boolean isDifferent(String name)
    {
        return differing.containsKey(name) || onlyOnLeft.containsKey(name) || onlyOnRight.containsKey(name);
    }

    /**
     * Returns the value of the thing with the provided name from the first object used to create this diff.
     *
     * @param name the name of the thing for which to retrieve the value
     * @return Object
     */
    public Object getLeftValue(String name)
    {
        if (onlyOnLeft.containsKey(name))
        {
            return onlyOnLeft.get(name);
        }
        else if (onlyOnRight.containsKey(name))
        {
            return null;
        }
        else if (differing.containsKey(name))
        {
            return differing.get(name).getLeft();
        }
        else
        {
            return matching.get(name);
        }
    }

    /**
     * Returns the value of the thing with the provided name from the second object used to create this diff.
     *
     * @param name the name of the thing for which to retrieve the value
     * @return Object
     */
    public Object getRightValue(String name)
    {
        if (onlyOnRight.containsKey(name))
        {
            return onlyOnRight.get(name);
        }
        else if (onlyOnLeft.containsKey(name))
        {
            return null;
        }
        else if (differing.containsKey(name))
        {
            return differing.get(name).getRight();
        }
        else
        {
            return matching.get(name);
        }
    }

    /**
     * Returns a mapping of things (by name) that only appear on the first object used to create this diff.
     *
     * @return {@code Map<String, Object>}
     */
    public Map<String, Object> getOnlyOnLeft()
    {
        return onlyOnLeft;
    }

    /**
     * Returns a mapping of things (by name) that only appear on the second object used to create this diff.
     *
     * @return {@code Map<String, Object>}
     */
    public Map<String, Object> getOnlyOnRight()
    {
        return onlyOnRight;
    }

    /**
     * Determine if there is a difference between the provided values, and capture either that difference or the
     * similarity.
     *
     * @param name the property name that differs
     * @param left the value in the first instance
     * @param right the value in the second instance
     */
    public void check(String name, Object left, Object right)
    {
        if (left == null && right == null)
        {
            addMatching(name, null);
        }
        else if (left == null)
        {
            onlyOnRight.put(name, right);
        }
        else if (right == null)
        {
            onlyOnLeft.put(name, left);
        }
        else if (left.equals(right))
        {
            addMatching(name, left);
        }
        else
        {
            // Otherwise, they are both non-null, and they are not equal
            addDiffering(name, new ValuePair(left, right));
        }
    }

    /**
     * Add the provided name as only existing on one side.
     *
     * @param side the side on which the value exists
     * @param name the name of the thing that points to the value
     * @param value the value
     */
    public void addOnlyOnOneSide(SidePresent side, String name, Object value)
    {
        if (side.equals(SidePresent.LEFT_ONLY))
        {
            onlyOnLeft.put(name, value);
        }
        else if (side.equals(SidePresent.RIGHT_ONLY))
        {
            onlyOnRight.put(name, value);
        }
    }

    /**
     * Add the provided name as a matching thing.
     *
     * @param name the name of the matching thing
     * @param value the value that is the same across both
     */
    protected void addMatching(String name, Object value)
    {
        matching.put(name, value);
    }

    /**
     * Add the provided name as a differing thing.
     *
     * @param name the name of the differing thing
     * @param valuePair the value for each
     */
    protected void addDiffering(String name, ValuePair valuePair)
    {
        differing.put(name, valuePair);
    }

    static class ValuePair
    {
        private final Object left;
        private final Object right;

        ValuePair(Object left, Object right)
        {
            this.left = left;
            this.right = right;
        }

        Object getLeft()
        {
            return left;
        }

        Object getRight()
        {
            return right;
        }
    }
}
