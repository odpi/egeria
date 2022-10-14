/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.governanceaction.search;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * An EnumTypePropertyValue stores the value for an enum property.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class EnumTypePropertyValue extends PropertyValue
{
    private static final long    serialVersionUID = 1L;

    private String  symbolicName = null;


    /**
     * Default constructor initializes an empty enum value
     */
    public EnumTypePropertyValue()
    {
        super();
    }


    /**
     * Copy/clone constructor initializes the enum with the values from the template.
     *
     * @param template EnumTypePropertyValue to copy
     */
    public EnumTypePropertyValue(EnumTypePropertyValue template)
    {
        super(template);

        if (template != null)
        {
            this.symbolicName = template.getSymbolicName();
        }
    }


    /**
     * Delegate the process of cloning to the subclass.
     *
     * @return subclass of PropertyValue
     */
    public PropertyValue cloneFromSubclass()
    {
        return new EnumTypePropertyValue(this);
    }


    /**
     * Return the string version of the value - used for error logging.
     *
     * @return string value
     */
    public  String valueAsString()
    {
        return symbolicName == null ? "<null>" : symbolicName;
    }


    /**
     * Return the object version of the value - used for comparisons.
     *
     * @return object value
     */
    public  Object valueAsObject()
    {
        return symbolicName;
    }


    /**
     * Return the symbolic name for this enum value.
     *
     * @return String symbolic name
     */
    public String getSymbolicName() { return symbolicName; }


    /**
     * Set up the symbolic name for this enum value.
     *
     * @param symbolicName String symbolic name
     */
    public void setSymbolicName(String symbolicName) { this.symbolicName = symbolicName; }


    /**
     * Standard toString method.
     *
     * @return JSON style description of variables.
     */
    @Override
    public String toString()
    {
        return "EnumTypePropertyValue{" +
                "symbolicName='" + symbolicName + '\'' +
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
        if (!(objectToCompare instanceof EnumTypePropertyValue))
        {
            return false;
        }
        if (!super.equals(objectToCompare))
        {
            return false;
        }
        EnumTypePropertyValue that = (EnumTypePropertyValue) objectToCompare;
        return Objects.equals(getSymbolicName(), that.getSymbolicName());
    }


    /**
     * Return a hash code based on the values of this object.
     *
     * @return in hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), getSymbolicName());
    }
}

