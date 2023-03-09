/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.digitalarchitecture.properties;


import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * ValidValuesMappingProperties is a java bean used to create a mapping between two valid values from different valid value sets.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class ValidValuesMappingProperties extends RelationshipProperties
{
    private static final long     serialVersionUID = 1L;

    private String associationDescription = null;
    private int    confidence             = 0;
    private String steward                = null;
    private String stewardTypeName        = null;
    private String stewardPropertyName    = null;
    private String notes                  = null;


    /**
     * Default constructor
     */
    public ValidValuesMappingProperties()
    {
    }


    /**
     * Copy/clone constructor.  Note, this is a deep copy
     *
     * @param template object to copy
     */
    public ValidValuesMappingProperties(ValidValuesMappingProperties template)
    {
        if (template != null)
        {
            associationDescription = template.getAssociationDescription();
            confidence             = template.getConfidence();
            steward                = template.getSteward();
            stewardTypeName        = template.getStewardTypeName();
            stewardPropertyName    = template.getStewardPropertyName();
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
     * Set up the id of the steward responsible for the mapping.
     *
     * @param steward String id
     */
    public void setSteward(String steward)
    {
        this.steward = steward;
    }



    /**
     * Return the type of element that describes the steward.
     *
     * @return type name
     */
    public String getStewardTypeName()
    {
        return stewardTypeName;
    }


    /**
     * Set up the type of element that describes the steward.
     *
     * @param stewardTypeName type name
     */
    public void setStewardTypeName(String stewardTypeName)
    {
        this.stewardTypeName = stewardTypeName;
    }


    /**
     * Return the name of the property that holds the steward's identifier.
     *
     * @return property name
     */
    public String getStewardPropertyName()
    {
        return stewardPropertyName;
    }


    /**
     * Set up the name of the property that holds the steward's identifier.
     *
     * @param stewardPropertyName property name
     */
    public void setStewardPropertyName(String stewardPropertyName)
    {
        this.stewardPropertyName = stewardPropertyName;
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
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "ValidValuesMappingProperties{" +
                       "effectiveFrom=" + getEffectiveFrom() +
                       ", effectiveTo=" + getEffectiveTo() +
                       ", extendedProperties=" + getExtendedProperties() +
                       ", associationDescription='" + associationDescription + '\'' +
                       ", confidence=" + confidence +
                       ", steward='" + steward + '\'' +
                       ", stewardTypeName='" + stewardTypeName + '\'' +
                       ", stewardPropertyName='" + stewardPropertyName + '\'' +
                       ", notes='" + notes + '\'' +
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
        ValidValuesMappingProperties that = (ValidValuesMappingProperties) objectToCompare;
        return confidence == that.confidence &&
                Objects.equals(associationDescription, that.associationDescription) &&
                       Objects.equals(steward, that.steward) &&
                       Objects.equals(stewardTypeName, that.stewardTypeName) &&
                       Objects.equals(getStewardPropertyName(), that.stewardPropertyName) &&
                Objects.equals(notes, that.notes);
    }


    /**
     * Return hash code based on properties.
     *
     * @return int
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(associationDescription, confidence, steward, stewardTypeName, stewardPropertyName, notes);
    }
}
