/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.openmetadata.properties.validvalues;


import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.frameworks.openmetadata.properties.RelationshipBeanProperties;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * ValidValueMemberProperties provides a flag to indicate if this value is the default value for the set.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class ValidValueMemberProperties extends RelationshipBeanProperties
{
    boolean isDefaultValue = false;

    /**
     * Default constructor
     */
    public ValidValueMemberProperties()
    {
        super();
        super.setTypeName(OpenMetadataType.VALID_VALUE_MEMBER_RELATIONSHIP.typeName);
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public ValidValueMemberProperties(ValidValueMemberProperties template)
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
        return "ValidValueMemberProperties{" +
                "isDefaultValue=" + isDefaultValue +
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
        ValidValueMemberProperties that = (ValidValueMemberProperties) objectToCompare;
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
