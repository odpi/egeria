/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.viewservices.assetcatalog.rest;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * HierarchyType describes the direction of a lineage graph request.
 */
@JsonAutoDetect(getterVisibility = PUBLIC_ONLY, setterVisibility = PUBLIC_ONLY, fieldVisibility = NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public enum HierarchyType
{
    /**
     * Downward elements from the graph.
     */
    DOWNWARD(0, "Downward", "Downward elements from the graph."),

    /**
     * Upward elements from the graph.
     */
    UPWARD(1, "Upward", "Upward elements from the graph."),


    /**
     * All elements.
     */
    ALL(2, "All elements from the graph", "All elements.");


    private final int ordinal;
    private final String name;
    private final String description;


    /**
     * Constructor.
     *
     * @param ordinal unique identifier of the enum.
     * @param name display name
     * @param description description of its value
     */
    HierarchyType(int ordinal, String name, String description)
    {
        this.ordinal = ordinal;
        this.name = name;
        this.description = description;
    }


    /**
     * Return the unique identifier of the enum.
     *
     * @return int
     */
    public int getOrdinal()
    {
        return ordinal;
    }


    /**
     * Return the display name of the enum.
     *
     * @return string
     */
    public String getName()
    {
        return name;
    }


    /**
     * Return the description of the enum.
     *
     * @return string
     */
    public String getDescription()
    {
        return description;
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "HierarchyType{" +
                       "name='" + name + '\'' +
                       '}';
    }
}
