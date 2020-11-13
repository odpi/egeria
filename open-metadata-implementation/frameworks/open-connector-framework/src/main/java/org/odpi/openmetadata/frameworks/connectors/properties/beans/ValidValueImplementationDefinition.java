/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworks.connectors.properties.beans;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;


/**
 * ValidValueImplementationAsset contains the properties for a reference data set implementing a requested valid value.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class ValidValueImplementationDefinition extends ValidValueImplementation
{
    private static final long         serialVersionUID = 1L;

    private ValidValue validValue = null;


    /**
     * Default constructor
     */
    public ValidValueImplementationDefinition()
    {
        super();
    }


    /**
     * Copy clone constructor
     *
     * @param template object to copy
     */
    public ValidValueImplementationDefinition(ValidValueImplementationDefinition template)
    {
        super(template);

        if (template != null)
        {
            validValue = template.getValidValue();
        }
    }


    /**
     * Returns the valid value definition for an entry in the asst's implementation.
     *
     * @return properties of the valid value
     */
    public ValidValue getValidValue()
    {
        return validValue;
    }


    /**
     * Set up the valid value definition for an entry in the asst's implementation.
     *
     * @param validValue properties of the valid value
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
        return "ValidValueImplElement{" +
                ", validValue=" + validValue +
                ", symbolicName='" + getSymbolicName() + '\'' +
                ", implementationValue='" + getImplementationValue() + '\'' +
                ", additionalValues=" + getAdditionalValues() +
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
        ValidValueImplementationDefinition that = (ValidValueImplementationDefinition) objectToCompare;
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
