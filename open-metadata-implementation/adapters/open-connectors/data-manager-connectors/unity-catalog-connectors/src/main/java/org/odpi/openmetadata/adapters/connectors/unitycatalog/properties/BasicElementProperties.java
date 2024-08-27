/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adapters.connectors.unitycatalog.properties;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * Common properties of an element stored in Unity catalog.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class BasicElementProperties
{
    private String              name       = null;
    private String              comment    = null;

    /*
     * This property is being remove temporarily since there are inconsistencies in the API over whether this is a
     * String or a map. Once this has been resolved, it will be reinstated.
     */
    // private Map<String, String> properties = null;


    /**
     * Constructor
     */
    public BasicElementProperties()
    {
    }

    /**
     * Return the unique name of the element within its name space.
     *
     * @return string
     */
    public String getName()
    {
        return name;
    }


    /**
     * Set up the unique name of the element within its name space.
     *
     * @param name string name
     */
    public void setName(String name)
    {
        this.name = name;
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


    /*
     * Return arbitrary name-value property pairs.
     *
     * @return property string map
     */
    //public Map<String, String> getProperties() {return properties;}


    /*
     * Set up arbitrary name-value property pairs.
     *
     * @param properties property string map
     */
    //public void setProperties(Map<String, String> properties) {this.properties = properties;}


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "BasicElementProperties{" +
                "name='" + name + '\'' +
                ", comment='" + comment + '\'' +
              //  ", properties=" + properties +
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
        BasicElementProperties that = (BasicElementProperties) objectToCompare;
        return Objects.equals(name, that.name) && Objects.equals(comment, that.comment);
                //&& Objects.equals(properties, that.properties);
    }


    /**
     * Return hash code based on properties.
     *
     * @return int
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(name, comment ); //, properties);
    }
}
