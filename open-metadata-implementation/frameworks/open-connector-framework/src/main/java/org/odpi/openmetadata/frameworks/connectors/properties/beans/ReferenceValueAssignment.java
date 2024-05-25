/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworks.connectors.properties.beans;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;


/**
 * ReferenceValueAssignment describes the link to a valid value for a referenceable via a ReferenceValueAssignment relationship.
 * This relationship is used to tag the referenceable with the valid value as a means of either classifying or describing its meaning.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class ReferenceValueAssignment extends ReferenceValue
{
    private ValidValue validValue = null;


    /**
     * Default constructor
     */
    public ReferenceValueAssignment()
    {
        super();
    }


    /**
     * Copy clone constructor
     *
     * @param template object to copy
     */
    public ReferenceValueAssignment(ReferenceValueAssignment template)
    {
        super(template);

        if (template != null)
        {
            validValue          = template.getValidValue();
        }
    }


    /**
     * Returns the properties of the valid value used in the assignment.
     *
     * @return properties of the valid value
     */
    public ValidValue getValidValue()
    {
        return validValue;
    }


    /**
     * Set up the properties of the valid value used in the assignment.
     *
     * @param validValue properties of the asset
     */
    public void setValidValue(ValidValue validValue)
    {
        this.validValue = validValue;
    }


    /**
     * JSON-style toString
     *
     * @return return string containing the property names and values
     */
    @Override
    public String toString()
    {
        return "ReferenceValueAssignment{" +
                "validValue=" + validValue +
                ", confidence=" + getConfidence() +
                ", steward='" + getSteward() + '\'' +
                ", notes='" + getNotes() + '\'' +
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
        ReferenceValueAssignment that = (ReferenceValueAssignment) objectToCompare;
        return Objects.equals(validValue, that.validValue);
    }


    /**
     * Return hash code for this object
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), validValue);
    }
}
