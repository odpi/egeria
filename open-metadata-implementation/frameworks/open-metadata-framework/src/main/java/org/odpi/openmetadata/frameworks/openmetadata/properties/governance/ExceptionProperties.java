/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.openmetadata.properties.governance;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.frameworks.openmetadata.properties.LabeledRelationshipProperties;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.util.Date;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * ExceptionProperties links an exception type to an element that is in non-compliance with a particular policy as described by the exception type.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class ExceptionProperties extends LabeledRelationshipProperties
{
    private Date   lastReviewTime      = null;
    private Date   reviewDate          = null;
    private String conditions          = null;
    private String steward             = null;
    private String stewardTypeName     = null;
    private String stewardPropertyName = null;
    private String notes               = null;


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
            this.lastReviewTime      = template.getLastReviewTime();
            this.reviewDate          = template.getReviewDate();
            this.conditions          = template.getConditions();
            this.steward             = template.getSteward();
            this.stewardTypeName     = template.getStewardTypeName();
            this.stewardPropertyName = template.getStewardPropertyName();
            this.notes               = template.getNotes();
        }
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
     * Return any notes associated with the certificate.
     *
     * @return string text
     */
    public String getNotes()
    {
        return notes;
    }


    /**
     * Set up any notes associated with the certificate.
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
                "lastReviewTime=" + lastReviewTime +
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
        return Objects.equals(lastReviewTime, that.lastReviewTime) &&
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
        return Objects.hash(super.hashCode(), lastReviewTime, reviewDate, conditions,
                            steward, stewardTypeName, stewardPropertyName, notes);
    }
}
