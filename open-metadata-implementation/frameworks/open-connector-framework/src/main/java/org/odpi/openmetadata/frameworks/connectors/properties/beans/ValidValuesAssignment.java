/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworks.connectors.properties.beans;


import com.fasterxml.jackson.annotation.*;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;


/**
 * ValidValuesAssignment contains the properties for a valid value's reference data assignment to a referenceable.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "class")
@JsonSubTypes(
        {
                @JsonSubTypes.Type(value = ValidValuesAssignmentConsumer.class, name = "ValidValuesAssignmentConsumer"),
                @JsonSubTypes.Type(value = ValidValuesAssignmentDefinition.class, name = "ValidValuesAssignmentDefinition")
        })
public class ValidValuesAssignment extends PropertyBase
{
    private static final long         serialVersionUID = 1L;

    private boolean strictRequirement  = false;

    /**
     * Default constructor
     */
    public ValidValuesAssignment()
    {
        super();
    }


    /**
     * Copy clone constructor
     *
     * @param template object to copy
     */
    public ValidValuesAssignment(ValidValuesAssignment template)
    {
        super(template);

        if (template != null)
        {
            strictRequirement  = template.getStrictRequirement();
        }
    }


    /**
     * Returns whether the valid values are a suggestion or a requirement.
     *
     * @return boolean flag
     */
    public boolean getStrictRequirement()
    {
        return strictRequirement;
    }


    /**
     * Set up whether the valid values are a suggestion or a requirement.
     *
     * @param strictRequirement boolean flag
     */
    public void setStrictRequirement(boolean strictRequirement)
    {
        this.strictRequirement = strictRequirement;
    }


    /**
     * JSON-style toString
     *
     * @return return string containing the property names and values
     */
    @Override
    public String toString()
    {
        return "ValidValuesAssignment{" +
                "strictRequirement=" + strictRequirement +
                '}';
    }


    /**
     * Return comparison result based on the content of the properties.
     *
     * @param objectToCompare test object
     * @return result of comparison
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
        if (!super.equals(objectToCompare))
        {
            return false;
        }
        ValidValuesAssignment that = (ValidValuesAssignment) objectToCompare;
        return Objects.equals(strictRequirement, that.strictRequirement);
    }


    /**
     * Return hash code for this object
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), strictRequirement);
    }
}
