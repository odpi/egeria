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
 * StructTypePropertyValue supports the value part of property that is defined as a complex structure.
 * It manages a list of properties that cover the fields in the structure.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class StructTypePropertyValue extends PropertyValue
{
    private static final long    serialVersionUID = 1L;

    private ElementProperties attributes = null;


    /**
     * Default constructor set StructTypePropertyValue to null.
     */
    public StructTypePropertyValue()
    {
        super();
    }


    /**
     * Copy/clone constructor sets up the values based on the template.
     *
     * @param template StructTypePropertyValue to copy.
     */
    public StructTypePropertyValue(StructTypePropertyValue template)
    {
        super(template);

        if (template != null)
        {
            attributes = template.getAttributes();
        }
    }


    /**
     * Delegate the process of cloning to the subclass.
     *
     * @return subclass of PropertyValue
     */
    public PropertyValue cloneFromSubclass()
    {
        return new StructTypePropertyValue(this);
    }


    /**
     * Return the string version of the value - used for error logging.
     *
     * @return string value
     */
    public String valueAsString()
    {
        return mapValuesAsString(attributes.getPropertyValueMap()).toString();
    }


    /**
     * Return the object version of the value - used for comparisons.
     *
     * @return object value
     */
    public Object valueAsObject()
    {
        return mapValuesAsObject(attributes.getPropertyValueMap());
    }


    /**
     * Return the attributes that make up the fields of the struct.
     *
     * @return attributes ElementProperties iterator
     */
    public ElementProperties getAttributes()
    {
        if (attributes == null)
        {
            return null;
        }
        else
        {
            return new ElementProperties(attributes);
        }
    }


    /**
     * Set up the attributes that make up the fields of the struct.
     *
     * @param attributes ElementProperties iterator
     */
    public void setAttributes(ElementProperties attributes) { this.attributes = attributes; }


    /**
     * Standard toString method.
     *
     * @return JSON style description of variables.
     */
    @Override
    public String toString()
    {
        return "StructTypePropertyValue{" +
                "attributes=" + attributes +
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
        StructTypePropertyValue that = (StructTypePropertyValue) objectToCompare;
        return Objects.equals(attributes, that.attributes);
    }


    /**
     * Return a hash code based on the property values
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(attributes);
    }
}
