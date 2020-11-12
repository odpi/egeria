/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * An EnumPropertyValue stores the value for an enum property.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class EnumPropertyValue extends InstancePropertyValue
{
    private static final long    serialVersionUID = 1L;

    private int     ordinal = 99;
    private String  symbolicName = null;
    private String  description = null;


    /**
     * Default constructor initializes an empty enum value
     */
    public EnumPropertyValue()
    {
        super(InstancePropertyCategory.ENUM);
    }


    /**
     * Copy/clone constructor initializes the enum with the values from the template.
     *
     * @param template EnumPropertyValue to copy
     */
    public EnumPropertyValue(EnumPropertyValue template)
    {
        super(template);

        if (template != null)
        {
            this.ordinal = template.getOrdinal();
            this.symbolicName = template.getSymbolicName();
            this.description = template.getDescription();
        }
    }


    /**
     * Delegate the process of cloning to the subclass.
     *
     * @return subclass of InstancePropertyValue
     */
    public  InstancePropertyValue cloneFromSubclass()
    {
        return new EnumPropertyValue(this);
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
     * Return the integer ordinal for this enum.
     *
     * @return int ordinal
     */
    public int getOrdinal() { return ordinal; }


    /**
     * Set the integer ordinal for this enum.
     *
     * @param ordinal int
     */
    public void setOrdinal(int ordinal) { this.ordinal = ordinal; }


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
     * Return the description for this enum.
     *
     * @return String description
     */
    public String getDescription() { return description; }


    /**
     * Set up the description for this enum.
     *
     * @param description String description
     */
    public void setDescription(String description) { this.description = description; }


    /**
     * Standard toString method.
     *
     * @return JSON style description of variables.
     */
    @Override
    public String toString()
    {
        return "EnumPropertyValue{" +
                "ordinal=" + ordinal +
                ", symbolicName='" + symbolicName + '\'' +
                ", description='" + description + '\'' +
                ", instancePropertyCategory=" + getInstancePropertyCategory() +
                ", typeGUID='" + getTypeGUID() + '\'' +
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
        if (!(objectToCompare instanceof EnumPropertyValue))
        {
            return false;
        }
        if (!super.equals(objectToCompare))
        {
            return false;
        }
        EnumPropertyValue that = (EnumPropertyValue) objectToCompare;
        return getOrdinal() == that.getOrdinal() &&
                Objects.equals(getSymbolicName(), that.getSymbolicName()) &&
                Objects.equals(getDescription(), that.getDescription());
    }


    /**
     * Return a hash code based on the values of this object.
     *
     * @return in hash code
     */
    @Override
    public int hashCode()
    {

        return Objects.hash(super.hashCode(), getOrdinal(), getSymbolicName(), getDescription());
    }
}

