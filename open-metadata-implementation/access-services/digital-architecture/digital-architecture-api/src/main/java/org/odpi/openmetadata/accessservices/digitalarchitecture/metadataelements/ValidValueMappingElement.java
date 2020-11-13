/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.digitalarchitecture.metadataelements;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.accessservices.digitalarchitecture.properties.ValidValuesMappingProperties;

import java.io.Serializable;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * ValidValueMappingElement contains the properties and remote end of a valid value mapping as
 * seen from a valid value.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class ValidValueMappingElement extends ValidValuesMappingProperties implements Serializable
{
    private static final long         serialVersionUID = 1L;

    private ValidValueElement validValueElement = null;


    /**
     * Default constructor
     */
    public ValidValueMappingElement()
    {
        super();
    }


    /**
     * Copy clone constructor
     *
     * @param template object to copy
     */
    public ValidValueMappingElement(ValidValueMappingElement template)
    {
        super(template);

        if (template != null)
        {
            validValueElement = template.getValidValueElement();
        }
    }


    /**
     * Return the valid value element that are mapped to the requested valid value.
     *
     * @return valid value object
     */
    public ValidValueElement getValidValueElement()
    {
        return validValueElement;
    }


    /**
     * Set up the valid value elements that are mapped together.
     *
     * @param validValueElement valid value object
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
        return "ValidValueMappingElement{" +
                "validValueElement=" + validValueElement +
                ", associationDescription='" + getAssociationDescription() + '\'' +
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
        ValidValueMappingElement that = (ValidValueMappingElement) objectToCompare;
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
