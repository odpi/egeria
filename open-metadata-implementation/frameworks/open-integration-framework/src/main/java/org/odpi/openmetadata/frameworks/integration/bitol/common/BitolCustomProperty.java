/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.integration.bitol.common;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * This class represents a custom (key/value) property that can be attached to most elements of a Bitol Open Data
 * Contract Standard (ODCS) or Open Data Product Standard (ODPS) document. It is used internally in Egeria to
 * pass this information to the integration connectors and view services.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class BitolCustomProperty
{
    private String id = null;
    private String property = null;
    private Object value = null;
    private String description = null;
    private String vendor = null;


    /**
     * Default constructor
     */
    public BitolCustomProperty()
    {
    }


    /**
     * Return the stable identifier of this element (alphanumeric, hyphen and underscore characters only).
     *
     * @return string identifier
     */
    public String getId()
    {
        return id;
    }


    /**
     * Set up the stable identifier of this element (alphanumeric, hyphen and underscore characters only).
     *
     * @param id string identifier
     */
    public void setId(String id)
    {
        this.id = id;
    }


    /**
     * Return the name of the property.  The Bitol standards recommend camelCase names.
     *
     * @return string property name
     */
    public String getProperty()
    {
        return property;
    }


    /**
     * Set up the name of the property.  The Bitol standards recommend camelCase names.
     *
     * @param property string property name
     */
    public void setProperty(String property)
    {
        this.property = property;
    }


    /**
     * Return the value of the property.  This may be a string, number, boolean, list or map.
     *
     * @return property value
     */
    public Object getValue()
    {
        return value;
    }


    /**
     * Set up the value of the property.  This may be a string, number, boolean, list or map.
     *
     * @param value property value
     */
    public void setValue(Object value)
    {
        this.value = value;
    }


    /**
     * Return the description of the custom property.
     *
     * @return string description
     */
    public String getDescription()
    {
        return description;
    }


    /**
     * Set up the description of the custom property.
     *
     * @param description string description
     */
    public void setDescription(String description)
    {
        this.description = description;
    }


    /**
     * Return the vendor, provider or external system associated with this custom property (a stable lowercase identifier such as confluent or soda).
     *
     * @return String
     */
    public String getVendor()
    {
        return vendor;
    }


    /**
     * Set up the vendor, provider or external system associated with this custom property (a stable lowercase identifier such as confluent or soda).
     *
     * @param vendor String
     */
    public void setVendor(String vendor)
    {
        this.vendor = vendor;
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "BitolCustomProperty{" +
                       "id='" + id + '\'' +
                       ", property='" + property + '\'' +
                       ", value=" + value +
                       ", description='" + description + '\'' +
                       ", vendor=" + vendor +
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
        if (objectToCompare == null || getClass() != objectToCompare.getClass())
        {
            return false;
        }
        BitolCustomProperty that = (BitolCustomProperty) objectToCompare;
        return Objects.equals(id, that.id) &&
                       Objects.equals(property, that.property) &&
                       Objects.equals(value, that.value) &&
                       Objects.equals(description, that.description) &&
                       Objects.equals(vendor, that.vendor);
    }


    /**
     * Return hash code based on properties.
     *
     * @return int
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(id, property, value, description, vendor);
    }
}
