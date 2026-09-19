/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.openmetadata.properties.governance;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.frameworks.openmetadata.properties.LabeledRelationshipProperties;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * ExceptionProperties links an exception type to an element that is in non-compliance with a particular policy as described by the exception type.
 * The non-compliance may not be with the element itself but with something attached to it, so the affected classifications,
 * elements and relationships identify exactly what the exception applies to.  This allows the exception to be attached to the
 * anchor element and still be precise about which of its anchored elements, classifications or relationships are affected.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class ExceptionProperties extends LabeledRelationshipProperties
{
    private Map<String, String> affectedClassifications = null;
    private List<String>        affectedElements        = null;
    private List<String>        affectedRelationships   = null;
    private Date                lastReviewTime          = null;
    private Date                reviewDate              = null;
    private String              conditions              = null;
    private String              steward                 = null;
    private String              stewardTypeName         = null;
    private String              stewardPropertyName     = null;
    private String              notes                   = null;


    /**
     * Default Constructor
     */
    public ExceptionProperties()
    {
        super();
        super.typeName = OpenMetadataType.EXCEPTION_RELATIONSHIP.typeName;
    }


    /**
     * Copy/clone Constructor - the resulting object.
     *
     * @param template object being copied
     */
    public ExceptionProperties(ExceptionProperties template)
    {
        super(template);

        if (template != null)
        {
            this.affectedClassifications = template.getAffectedClassifications();
            this.affectedElements        = template.getAffectedElements();
            this.affectedRelationships   = template.getAffectedRelationships();
            this.lastReviewTime          = template.getLastReviewTime();
            this.reviewDate              = template.getReviewDate();
            this.conditions              = template.getConditions();
            this.steward                 = template.getSteward();
            this.stewardTypeName         = template.getStewardTypeName();
            this.stewardPropertyName     = template.getStewardPropertyName();
            this.notes                   = template.getNotes();
        }
    }


    /**
     * Return the map of element GUIDs to the names of the classifications on them that are affected by this exception.
     *
     * @return map of element GUID to classification name
     */
    public Map<String, String> getAffectedClassifications()
    {
        return affectedClassifications;
    }


    /**
     * Set up the map of element GUIDs to the names of the classifications on them that are affected by this exception.
     *
     * @param affectedClassifications map of element GUID to classification name
     */
    public void setAffectedClassifications(Map<String, String> affectedClassifications)
    {
        this.affectedClassifications = affectedClassifications;
    }


    /**
     * Return the list of additional element GUIDs that are affected by this exception.  This allows the exception to be
     * attached to the anchor element and also list the anchored elements that are affected.
     *
     * @return list of element GUIDs
     */
    public List<String> getAffectedElements()
    {
        return affectedElements;
    }


    /**
     * Set up the list of additional element GUIDs that are affected by this exception.  This allows the exception to be
     * attached to the anchor element and also list the anchored elements that are affected.
     *
     * @param affectedElements list of element GUIDs
     */
    public void setAffectedElements(List<String> affectedElements)
    {
        this.affectedElements = affectedElements;
    }


    /**
     * Return the list of relationship GUIDs that are affected by this exception.
     *
     * @return list of relationship GUIDs
     */
    public List<String> getAffectedRelationships()
    {
        return affectedRelationships;
    }


    /**
     * Set up the list of relationship GUIDs that are affected by this exception.
     *
     * @param affectedRelationships list of relationship GUIDs
     */
    public void setAffectedRelationships(List<String> affectedRelationships)
    {
        this.affectedRelationships = affectedRelationships;
    }


    /**
     * Return the date/time that this exception was last reviewed.
     *
     * @return date/time
     */
    public Date getLastReviewTime()
    {
        return lastReviewTime;
    }


    /**
     * Set up the date/time that this exception was last reviewed.
     *
     * @param lastReviewTime date/time
     */
    public void setLastReviewTime(Date lastReviewTime)
    {
        this.lastReviewTime = lastReviewTime;
    }


    /**
     * Return the date that this exception is next to be reviewed.
     *
     * @return date
     */
    public Date getReviewDate()
    {
        return reviewDate;
    }


    /**
     * Set up the date that this exception is next to be reviewed.
     *
     * @param reviewDate date
     */
    public void setReviewDate(Date reviewDate)
    {
        this.reviewDate = reviewDate;
    }


    /**
     * Return any conditions to this exception - particularly if it represents an exemption.
     *
     * @return string text
     */
    public String getConditions()
    {
        return conditions;
    }


    /**
     * Set up any conditions or endorsements to this exception - particularly if it represents an exemption.
     *
     * @param conditions string text
     */
    public void setConditions(String conditions)
    {
        this.conditions = conditions;
    }


    /**
     * Return the person/team responsible for the validity of the exception, reviews, and potential resolution of the exception.
     *
     * @return string name/id
     */
    public String getSteward()
    {
        return steward;
    }


    /**
     * Set up the person/team responsible for the validity of the exception, reviews, and potential resolution of the exception.
     *
     * @param steward string name/id
     */
    public void setSteward(String steward)
    {
        this.steward = steward;
    }


    /**
     * Return the name of the type of the element supplying the steward property.
     *
     * @return string type name
     */
    public String getStewardTypeName()
    {
        return stewardTypeName;
    }


    /**
     * Set up the name of the type of the element supplying the steward property.
     *
     * @param stewardTypeName string type name
     */
    public void setStewardTypeName(String stewardTypeName)
    {
        this.stewardTypeName = stewardTypeName;
    }


    /**
     * Return the name of the property from the element supplying the steward property.
     *
     * @return string property name
     */
    public String getStewardPropertyName()
    {
        return stewardPropertyName;
    }


    /**
     * Set up the name of the property from the element supplying the steward property.
     *
     * @param stewardPropertyName string property name
     */
    public void setStewardPropertyName(String stewardPropertyName)
    {
        this.stewardPropertyName = stewardPropertyName;
    }


    /**
     * Return any notes associated with the exception.
     *
     * @return string text
     */
    public String getNotes()
    {
        return notes;
    }


    /**
     * Set up any notes associated with the exception.
     *
     * @param notes string text
     */
    public void setNotes(String notes)
    {
        this.notes = notes;
    }


    /**
     * JSON-style toString.
     *
     * @return list of properties and their values.
     */
    @Override
    public String toString()
    {
        return "ExceptionProperties{" +
                "affectedClassifications=" + affectedClassifications +
                ", affectedElements=" + affectedElements +
                ", affectedRelationships=" + affectedRelationships +
                ", lastReviewTime=" + lastReviewTime +
                ", reviewDate=" + reviewDate +
                ", conditions='" + conditions + '\'' +
                ", steward='" + steward + '\'' +
                ", stewardTypeName='" + stewardTypeName + '\'' +
                ", stewardPropertyName='" + stewardPropertyName + '\'' +
                ", notes='" + notes + '\'' +
                "} " + super.toString();
    }


    /**
     * Equals method that returns true if containing properties are the same.
     *
     * @param objectToCompare object to compare
     * @return boolean result of comparison
     */
    @Override
    public boolean equals(Object objectToCompare)
    {
        if (objectToCompare == null || getClass() != objectToCompare.getClass()) return false;
        if (!super.equals(objectToCompare)) return false;
        ExceptionProperties that = (ExceptionProperties) objectToCompare;
        return Objects.equals(affectedClassifications, that.affectedClassifications) &&
                Objects.equals(affectedElements, that.affectedElements) &&
                Objects.equals(affectedRelationships, that.affectedRelationships) &&
                Objects.equals(lastReviewTime, that.lastReviewTime) &&
                Objects.equals(reviewDate, that.reviewDate) &&
                Objects.equals(conditions, that.conditions) &&
                Objects.equals(steward, that.steward) &&
                Objects.equals(stewardTypeName, that.stewardTypeName) &&
                Objects.equals(stewardPropertyName, that.stewardPropertyName) &&
                Objects.equals(notes, that.notes);
    }

    /**
     * Just use the GUID for the hash code as it should be unique.
     *
     * @return int code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), affectedClassifications, affectedElements, affectedRelationships,
                            lastReviewTime, reviewDate, conditions, steward, stewardTypeName, stewardPropertyName, notes);
    }
}
