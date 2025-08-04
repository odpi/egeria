/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.openmetadata.properties;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.frameworks.openmetadata.search.PropertyValue;

import java.util.*;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;


/**
 * The ClassificationElementProperties class provides support for properties to be added to a metadata element,
 * as part of the initial classification.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class ClassificationElementProperties extends ClassificationProperties
{
    private Map<String, PropertyValue> propertyValueMap = new HashMap<>();


    /**
     * Typical constructor
     */
    public ClassificationElementProperties()
    {
        super();
    }


    /**
     * Copy/clone constructor.
     *
     * @param template template object to copy.
     */
    public ClassificationElementProperties(ClassificationElementProperties template)
    {
        super(template);

        if (template != null)
        {
            propertyValueMap = template.getPropertyValueMap();
        }
    }


    /**
     * Return the instance properties as a map.
     *
     * @return  instance properties map.
     */
    public Map<String, PropertyValue> getPropertyValueMap()
    {
        if (propertyValueMap == null)
        {
            return null;
        }
        else if (propertyValueMap.isEmpty())
        {
            return null;
        }
        else
        {
            return new HashMap<>(propertyValueMap);
        }
    }


    /**
     * Set up the instance properties map.
     *
     * @param propertyValueMap map of name valued properties
     */
    public void setPropertyValueMap(Map<String, PropertyValue> propertyValueMap)
    {
        if (propertyValueMap == null)
        {
            this.propertyValueMap = new HashMap<>();
        }
        else
        {
            this.propertyValueMap = propertyValueMap;
        }
    }


    /**
     * Standard toString method.
     *
     * @return JSON style description of variables.
     */
    @Override
    public String toString()
    {
        return "ClassificationElementProperties{" +
                ", propertyValueMap=" + propertyValueMap +
                "} " + super.toString();
    }


    /**
     * Validate that an object is equal depending on their stored values.
     *
     * @param objectToCompare object
     * @return boolean result
     */
    @Override
    public boolean equals(Object objectToCompare)
    {
        if (this == objectToCompare) return true;
        if (objectToCompare == null || getClass() != objectToCompare.getClass()) return false;
        if (!super.equals(objectToCompare)) return false;
        ClassificationElementProperties that = (ClassificationElementProperties) objectToCompare;
        return Objects.equals(propertyValueMap, that.propertyValueMap);
    }

    /**
     * Return a hash code based on the values of this object.
     *
     * @return in hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), propertyValueMap);
    }
}

