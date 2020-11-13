/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.digitalarchitecture.metadataelements;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.accessservices.digitalarchitecture.properties.ValidValuesImplProperties;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * ValidValueImplDefinitionElement describes a Valid Value that defines one if the
 * values in a reference data asset.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class ValidValueImplDefinitionElement extends ValidValuesImplProperties
{
    private static final long    serialVersionUID = 1L;

    private ValidValueElement validValueElement = null;

    /**
     * Default constructor
     */
    public ValidValueImplDefinitionElement()
    {
        super();
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public ValidValueImplDefinitionElement(ValidValueImplDefinitionElement template)
    {
        super(template);

        if (template != null)
        {
            validValueElement = template.getValidValueElement();
        }
    }


    /**
     * Return the valid value bean (may be a definition or a set).
     *
     * @return bean
     */
    public ValidValueElement getValidValueElement()
    {
        return validValueElement;
    }


    /**
     * Set up the the valid value bean (may be a definition or a set).
     *
     * @param validValueElement bean
     */
    public void setValidValueElement(ValidValueElement validValueElement)
    {
        this.validValueElement = validValueElement;
    }


    /**
     * JSON-style toString
     *
     * @return return string containing the property names and values
     */
    @Override
    public String toString()
    {
        return "ValidValueImplDefinitionElement{" +
                "validValueElement=" + validValueElement +
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
        ValidValueImplDefinitionElement that = (ValidValueImplDefinitionElement) objectToCompare;
        return Objects.equals(validValueElement, that.validValueElement);
    }


    /**
     * Return hash code for this object
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), validValueElement);
    }
}
