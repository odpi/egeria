/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.governanceservers.lineagewarehouse.requests;

/**
 * Describe the graph direction
 */
public enum HierarchyType
{
    /**
     * Downward elements from the graph
     */
    DOWNWARD(0, "Downward", "Downward elements from the graph"),

    /**
     * Upward elements from the graph
     */
    UPWARD(1, "Upward", "Upward elements from the graph"),

    /**
     * All elements
     */
    ALL(2, "All elements from the graph", "All elements");

    private final int ordinal;
    private final String name;
    private final String description;

    /**
     * Constructor
     *
     * @param ordinal identifier for the enum
     * @param name name of the direction
     * @param description description of the direction
     */
    HierarchyType(int ordinal, String name, String description)
    {
        this.ordinal = ordinal;
        this.name = name;
        this.description = description;
    }


    public int getOrdinal()
    {
        return ordinal;
    }

    public String getName()
    {
        return name;
    }

    public String getDescription()
    {
        return description;
    }


    @Override
    public String toString()
    {
        return "HierarchyType{" +
                "ordinal=" + ordinal +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                "} " + super.toString();
    }
}
