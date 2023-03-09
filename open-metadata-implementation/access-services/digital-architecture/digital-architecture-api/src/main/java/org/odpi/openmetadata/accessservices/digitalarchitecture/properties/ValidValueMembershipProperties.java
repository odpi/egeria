/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.digitalarchitecture.properties;


import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * ValidValueMembershipProperties provides a flag to indicate if this value is the default value for the set.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class ValidValueMembershipProperties extends RelationshipProperties
{
    private static final long    serialVersionUID = 1L;

    boolean isDefaultValue = false;

    /**
     * Default constructor
     */
    public ValidValueMembershipProperties()
    {
        super();
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public ValidValueMembershipProperties(ValidValueMembershipProperties template)
    {
        super(template);

        if (template != null)
        {
            this.isDefaultValue = template.getDefaultValue();
        }
    }


    /**
     * Return whether it is a default value.
     *
     * @return flag
     */
    public boolean getDefaultValue()
    {
        return isDefaultValue;
    }


    /**
     * Set up whether it is a default value.
     *
     * @param defaultValue flag
     */
    public void setDefaultValue(boolean defaultValue)
    {
        this.isDefaultValue = defaultValue;
    }


    /**
     * JSON-style toString
     *
     * @return return string containing the property names and values
     */
    @Override
    public String toString()
    {
        return "ValidValueMembershipProperties{" +
                       "effectiveFrom=" + getEffectiveFrom() +
                       ", effectiveTo=" + getEffectiveTo() +
                       ", extendedProperties=" + getExtendedProperties() +
                       ", isDefaultValue=" + isDefaultValue +
                       ", defaultValue=" + getDefaultValue() +
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
        if (! super.equals(objectToCompare))
        {
            return false;
        }
        ValidValueMembershipProperties that = (ValidValueMembershipProperties) objectToCompare;
        return isDefaultValue == that.isDefaultValue;
    }


    /**
     * Return hash code based on properties.
     *
     * @return int
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), isDefaultValue);
    }
}
