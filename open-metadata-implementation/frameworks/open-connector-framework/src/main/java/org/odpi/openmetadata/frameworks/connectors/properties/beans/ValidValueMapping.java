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
 * ValidValueMapping contains the properties and ends of a valid value mapping.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class ValidValueMapping extends PropertyBase
{
    private static final long         serialVersionUID = 1L;


    private String     associationDescription = null;
    private int        confidence             = 0;
    private String     steward                = null;
    private String     notes                  = null;
    private ValidValue validValue             = null;


    /**
     * Default constructor
     */
    public ValidValueMapping()
    {
        super();
    }


    /**
     * Copy clone constructor
     *
     * @param template object to copy
     */
    public ValidValueMapping(ValidValueMapping template)
    {
        super(template);

        if (template != null)
        {
            validValue             = template.getValidValue();
            associationDescription = template.getAssociationDescription();
            confidence             = template.getConfidence();
            steward                = template.getSteward();
            notes                  = template.getNotes();
        }
    }


    /**
     * Returns the short description of the type of association.
     *
     * @return String text
     */
    public String getAssociationDescription()
    {
        return associationDescription;
    }


    /**
     * Set up the short description of the type of association.
     *
     * @param associationDescription String text
     */
    public void setAssociationDescription(String associationDescription)
    {
        this.associationDescription = associationDescription;
    }


    /**
     * Return the confidence level (0-100) that the mapping is correct.
     *
     * @return int
     */
    public int getConfidence()
    {
        return confidence;
    }


    /**
     * Set up the confidence level (0-100) that the mapping is correct.
     *
     * @param confidence int
     */
    public void setConfidence(int confidence)
    {
        this.confidence = confidence;
    }


    /**
     * Returns the id of the steward responsible for the mapping.
     *
     * @return String id
     */
    public String getSteward()
    {
        return steward;
    }


    /**
     * Set up the the id of the steward responsible for the mapping.
     *
     * @param steward String id
     */
    public void setSteward(String steward)
    {
        this.steward = steward;
    }



    /**
     * Return the additional values associated with the symbolic name.
     *
     * @return string text
     */
    public String getNotes()
    {
        return notes;
    }


    /**
     * Set up the additional values associated with the symbolic name.
     *
     * @param notes string text
     */
    public void setNotes(String notes)
    {
        this.notes = notes;
    }


    /**
     * Return the valid value elements that are mapped together.
     *
     * @return list of valid value objects
     */
    public ValidValue getValidValue()
    {
        return validValue;
    }


    /**
     * Set up the valid value elements that are mapped together.
     *
     * @param validValue list of valid value objects
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
        return "ValidValueMapping{" +
                "validValue=" + validValue +
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
        ValidValueMapping that = (ValidValueMapping) objectToCompare;
        return Objects.equals(getValidValue(), that.getValidValue());
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
