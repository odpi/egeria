/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.viewservices.assetcatalog.rest;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * LineageRequestNode describes the type of node to return.
 */
@JsonAutoDetect(getterVisibility = PUBLIC_ONLY, setterVisibility = PUBLIC_ONLY, fieldVisibility = NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class LineageRequestNode
{
    /**
     * The type of node.
     */
    private String type;

    /**
     * The name of the node.
     */
    private String name;


    /**
     * Default constructor.
     */
    public LineageRequestNode()
    {
    }


    /**
     * Return the type of node.
     *
     * @return type name
     */
    public String getType()
    {
        return type;
    }


    /**
     * Set up the type of node.
     *
     * @param type type name
     */
    public void setType(String type)
    {
        this.type = type;
    }


    /**
     * Return the name of the node.
     *
     * @return string
     */
    public String getName()
    {
        return name;
    }


    /**
     * Set up the name of the node.
     *
     * @param name string
     */
    public void setName(String name)
    {
        this.name = name;
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "LineageRequestNode{" +
                       "type='" + type + '\'' +
                       ", name='" + name + '\'' +
                       '}';
    }


    /**
     * Compare the values of the supplied object with those stored in the current object.
     *
     * @param objectToCompare supplied object
     * @return boolean result of comparison
     */
    @Override
    public boolean equals(Object objectToCompare)
    {
        if (this == objectToCompare)
        {
            return true;
        }
        if (! (objectToCompare instanceof LineageRequestNode that))
        {
            return false;
        }
        return Objects.equals(type, that.type) && Objects.equals(name, that.name);
    }


    /**
     * Return hash code based on properties.
     *
     * @return int
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(type, name);
    }
}
