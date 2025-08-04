/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.openmetadata.properties;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.frameworks.openmetadata.search.PropertyValue;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;


/**
 * The EntityElementProperties class provides support for generic open metadata properties to be added
 * to a new metadata element,
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class EntityElementProperties extends EntityProperties
{
    private Map<String, PropertyValue> propertyValueMap = new HashMap<>();


    /**
     * Typical constructor
     */
    public EntityElementProperties()
    {
        super();
    }


    /**
     * Copy/clone constructor.
     *
     * @param template template object to copy.
     */
    public EntityElementProperties(EntityElementProperties template)
    {
        /*
         * An empty properties object is created in the private variable declaration so nothing to do.
         */
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
     * Return the number of properties stored.
     *
     * @return int property count
     */
    public int getPropertyCount()
    {
        return propertyValueMap.size();
    }


    /**
     * Return all the properties as a string map.
     *
     * @return map of property name to property value as string.
     */
    public Map<String, String> getPropertiesAsStrings()
    {
        if ((propertyValueMap != null) && (! propertyValueMap.isEmpty()))
        {
            Map<String, String> stringStringMap = new HashMap<>();

            for (String propertyName : propertyValueMap.keySet())
            {
                stringStringMap.put(propertyName, propertyValueMap.get(propertyName).valueAsString());
            }

            return stringStringMap;
        }

        return null;
    }


    /**
     * Standard toString method.
     *
     * @return JSON style description of variables.
     */
    @Override
    public String toString()
    {
        return "EntityElementProperties{" +
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
        EntityElementProperties that = (EntityElementProperties) objectToCompare;
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

