/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.openmetadata.properties;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;


/**
 * FindPropertyNamesProperties is the request body structure used on OMAG REST API calls that passes a property value
 * and a list of names to match to.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class FindPropertyNameProperties extends FindRequest
{
    private String propertyValue = null;
    private String propertyName  = null;


    /**
     * Default constructor
     */
    public FindPropertyNameProperties()
    {
        super();
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public FindPropertyNameProperties(FindPropertyNameProperties template)
    {
        super(template);

        if (template != null)
        {
            propertyValue = template.getPropertyValue();
            propertyName  = template.getPropertyName();
        }
    }


    /**
     * Return the name for the query request.
     *
     * @return string name
     */
    public String getPropertyValue()
    {
        return propertyValue;
    }


    /**
     * Set up the name for the query request.
     *
     * @param propertyValue string
     */
    public void setPropertyValue(String propertyValue)
    {
        this.propertyValue = propertyValue;
    }

    /**
     * Return the list of property names to match against.
     *
     * @return string
     */
    public String getPropertyName()
    {
        return propertyName;
    }


    /**
     * Set up the list of property names to search for,
     *
     * @param propertyName string
     */
    public void setPropertyName(String propertyName)
    {
        this.propertyName = propertyName;
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "FindPropertyNameProperties{" +
                "propertyValue='" + propertyValue + '\'' +
                ", propertyName=" + propertyName +
                "} " + super.toString();
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
        if (objectToCompare == null || getClass() != objectToCompare.getClass())
        {
            return false;
        }
        if (! super.equals(objectToCompare))
        {
            return false;
        }
        FindPropertyNameProperties that = (FindPropertyNameProperties) objectToCompare;
        return Objects.equals(propertyValue, that.propertyValue) &&
                Objects.equals(propertyName, that.propertyName);
    }


    /**
     * Create a hash code for this element type.
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), propertyValue, propertyName);
    }
}
