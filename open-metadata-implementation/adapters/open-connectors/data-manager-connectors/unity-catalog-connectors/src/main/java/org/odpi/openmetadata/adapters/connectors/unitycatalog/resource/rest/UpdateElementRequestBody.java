/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adapters.connectors.unitycatalog.resource.rest;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Payload for patching an element
 */
public class UpdateElementRequestBody
{
    private String              comment    = null;
    private Map<String, String> properties = new HashMap<>();
    private String              new_name   = null;


    public UpdateElementRequestBody()
    {
    }



    /**
     * Return the unique name of the element within its name space..
     *
     * @return string
     */
    public String getNew_name()
    {
        return new_name;
    }


    /**
     * Set up the unique name of the element within its name space.
     *
     * @param new_name string name
     */
    public void setNew_name(String new_name)
    {
        this.new_name = new_name;
    }


    /**
     * Return a comment describing the element within its name space.
     *
     * @return text
     */
    public String getComment()
    {
        return comment;
    }


    /**
     * Set up a comment describing the element within its name space.
     *
     * @param comment text
     */
    public void setComment(String comment)
    {
        this.comment = comment;
    }


    /**
     * Return arbitrary name-value property pairs.
     *
     * @return property string map
     */
    public Map<String, String> getProperties()
    {
        return properties;
    }


    /**
     * Set up arbitrary name-value property pairs.
     *
     * @param properties property string map
     */
    public void setProperties(Map<String, String> properties)
    {
        this.properties = properties;
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "UpdateElementRequestBody{" +
                "name='" + new_name + '\'' +
                ", comment='" + comment + '\'' +
                ", properties=" + properties +
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
        if (this == objectToCompare) return true;
        if (objectToCompare == null || getClass() != objectToCompare.getClass()) return false;
        UpdateElementRequestBody that = (UpdateElementRequestBody) objectToCompare;
        return Objects.equals(new_name, that.new_name) && Objects.equals(comment, that.comment) && Objects.equals(properties, that.properties);
    }


    /**
     * Return hash code based on properties.
     *
     * @return int
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(new_name, comment, properties);
    }
}
