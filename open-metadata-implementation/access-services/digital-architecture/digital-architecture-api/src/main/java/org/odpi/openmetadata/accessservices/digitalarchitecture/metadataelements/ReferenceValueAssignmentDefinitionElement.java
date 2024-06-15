/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.digitalarchitecture.metadataelements;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.accessservices.digitalarchitecture.properties.ReferenceValueAssignmentProperties;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * ReferenceValueAssignmentDefinitionElement describes a valid value that is being used as a tag/classifier
 * for a referenceable.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class ReferenceValueAssignmentDefinitionElement extends ReferenceValueAssignmentProperties
{
    private ValidValueElement validValueElement = null;

    /**
     * Default constructor
     */
    public ReferenceValueAssignmentDefinitionElement()
    {
        super();
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public ReferenceValueAssignmentDefinitionElement(ReferenceValueAssignmentDefinitionElement template)
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
     * Set up the valid value bean (maybe a definition or a set).
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
        return "ReferenceValueAssignmentDefinitionElement{" +
                "validValueElement=" + validValueElement +
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
        ReferenceValueAssignmentDefinitionElement that = (ReferenceValueAssignmentDefinitionElement) objectToCompare;
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
