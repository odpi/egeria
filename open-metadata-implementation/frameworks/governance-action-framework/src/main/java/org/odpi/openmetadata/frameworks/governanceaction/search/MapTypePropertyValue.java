/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.governanceaction.search;


import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * MapTypePropertyValue stores the values of a map within an entity, struct or relationship properties.
 * The elements of the map are stored in an ElementProperties map.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class MapTypePropertyValue extends PropertyValue
{
    private ElementProperties mapValues = null;


    /**
     * Default constructor sets the map to empty.
     */
    public MapTypePropertyValue()
    {
        super();
    }


    /**
     * Copy/clone constructor set up the map using the supplied template.
     *
     * @param template ArrayTypePropertyValue
     */
    public MapTypePropertyValue(MapTypePropertyValue template)
    {
        super(template);

        if (template != null)
        {
            mapValues = template.getMapValues();
        }
    }


    /**
     * Delegate the process of cloning to the subclass.
     *
     * @return subclass of PropertyValue
     */
    public PropertyValue cloneFromSubclass()
    {
        return new MapTypePropertyValue(this);
    }


    /**
     * Return the string version of the value - used for error logging.
     *
     * @return string value
     */
    public String valueAsString()
    {
        if (mapValues != null)
        {
            return mapValuesAsString(mapValues.getPropertyValueMap()).toString();
        }

        return null;
    }


    /**
     * Return the object version of the value - used for comparisons.
     *
     * @return object value
     */
    public Object valueAsObject()
    {
        if (mapValues != null)
        {
            return mapValuesAsObject(mapValues.getPropertyValueMap());
        }

        return null;
    }


    /**
     * Return the number of elements in the map.
     *
     * @return int map size
     */
    public int getMapElementCount()
    {
        if (mapValues == null)
        {
            return 0;
        }
        else
        {
            return mapValues.getPropertyCount();
        }
    }


    /**
     * Return a copy of the map elements.
     *
     * @return ElementProperties containing the map elements
     */
    public ElementProperties getMapValues()
    {
        if (mapValues == null)
        {
            return null;
        }
        else
        {
            return new ElementProperties(mapValues);
        }
    }


    /**
     * Add or update an element in the map.
     * If a null is supplied for the property name, an OMRS runtime exception is thrown.
     * If a null is supplied for the property value, the property is removed.
     *
     * @param propertyName String name
     * @param propertyValue PropertyValue value to store
     */
    public void setMapValue(String  propertyName, PropertyValue propertyValue)
    {
        if (mapValues == null)
        {
            mapValues = new ElementProperties();
        }
        mapValues.setProperty(propertyName, propertyValue);
    }


    /**
     * Set up the map elements in one call.
     *
     * @param mapValues ElementProperties containing the array elements
     */
    public void setMapValues(ElementProperties mapValues) { this.mapValues = mapValues; }


    /**
     * Standard toString method.
     *
     * @return JSON style description of variables.
     */
    @Override
    public String toString()
    {
        return "MapTypePropertyValue{" +
                "mapValues=" + mapValues +
                ", mapElementCount=" + getMapElementCount() +
                ", typeName='" + getTypeName() + '\'' +
                '}';
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
        if (this == objectToCompare)
        {
            return true;
        }
        if (objectToCompare == null || getClass() != objectToCompare.getClass())
        {
            return false;
        }
        MapTypePropertyValue that = (MapTypePropertyValue) objectToCompare;
        return Objects.equals(mapValues, that.mapValues);
    }


    /**
     * Return a hash code based on the property values
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {

        return Objects.hash(mapValues);
    }
}
