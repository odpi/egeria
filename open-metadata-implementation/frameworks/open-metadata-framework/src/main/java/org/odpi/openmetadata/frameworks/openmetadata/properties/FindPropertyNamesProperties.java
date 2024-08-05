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
public class FindPropertyNamesProperties extends FindProperties
{
    private String       propertyValue = null;
    private List<String> propertyNames = null;


    /**
     * Default constructor
     */
    public FindPropertyNamesProperties()
    {
        super();
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public FindPropertyNamesProperties(FindPropertyNamesProperties template)
    {
        super(template);

        if (template != null)
        {
            propertyValue = template.getPropertyValue();
            propertyNames = template.getPropertyNames();
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
     * @return list of names
     */
    public List<String> getPropertyNames()
    {
        return propertyNames;
    }


    /**
     * Set up the list of property names to search for,
     *
     * @param propertyNames list of names
     */
    public void setPropertyNames(List<String> propertyNames)
    {
        this.propertyNames = propertyNames;
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "FindPropertyNamesProperties{" +
                "propertyValue='" + propertyValue + '\'' +
                ", propertyNames=" + propertyNames +
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
        FindPropertyNamesProperties that = (FindPropertyNamesProperties) objectToCompare;
        return Objects.equals(propertyValue, that.propertyValue) &&
                Objects.equals(propertyNames, that.propertyNames);
    }


    /**
     * Create a hash code for this element type.
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), propertyValue, propertyNames);
    }
}
