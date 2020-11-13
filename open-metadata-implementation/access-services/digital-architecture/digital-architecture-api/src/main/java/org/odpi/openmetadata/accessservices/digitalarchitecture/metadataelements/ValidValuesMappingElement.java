/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.digitalarchitecture.metadataelements;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.accessservices.digitalarchitecture.properties.ValidValuesMappingProperties;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * ValidValuesMappingElement contains the properties and ends of a valid value mapping relationship.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class ValidValuesMappingElement extends ValidValuesMappingProperties implements Serializable
{
    private static final long         serialVersionUID = 1L;

    private List<ValidValueElement> validValueElements = null;


    /**
     * Default constructor
     */
    public ValidValuesMappingElement()
    {
        super();
    }


    /**
     * Copy clone constructor
     *
     * @param template object to copy
     */
    public ValidValuesMappingElement(ValidValuesMappingElement template)
    {
        super(template);

        if (template != null)
        {
            validValueElements = template.getValidValueElements();
        }
    }


    /**
     * Return the valid value elements that are mapped together.
     *
     * @return list of valid value objects
     */
    public List<ValidValueElement> getValidValueElements()
    {
        if (validValueElements == null)
        {
            return null;
        }
        else if (validValueElements.isEmpty())
        {
            return null;
        }
        else
        {
            return validValueElements;
        }
    }


    /**
     * Set up the valid value elements that are mapped together.
     *
     * @param validValueElements list of valid value objects
     */
    public void setValidValueElements(List<ValidValueElement> validValueElements)
    {
        this.validValueElements = validValueElements;
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
                "validValueElements=" + validValueElements +
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
        ValidValuesMappingElement that = (ValidValuesMappingElement) objectToCompare;
        return Objects.equals(validValueElements, that.validValueElements);
    }


    /**
     * Return hash code for this object
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), validValueElements);
    }
}
