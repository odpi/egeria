/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworks.openmetadata.properties.decisions;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.frameworks.openmetadata.properties.LabeledRelationshipProperties;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.util.Date;
import java.util.Map;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * AssessmentProperties describes an assessment of an element that was used as an input to a decision.
 * There can be many assessments between the same element and decision.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class AssessmentProperties extends LabeledRelationshipProperties
{
    private String              assessor             = null;
    private String              assessorTypeName     = null;
    private String              assessorPropertyName = null;
    private Date                assessmentTime       = null;
    private Date                expiryTime           = null;
    private String              notes                = null;
    private Map<String, String> additionalProperties = null;


    /**
     * Default constructor
     */
    public AssessmentProperties()
    {
        super();
        super.typeName = OpenMetadataType.ASSESSMENT_RELATIONSHIP.typeName;
    }


    /**
     * Copy/clone constructor.
     *
     * @param template object to copy
     */
    public AssessmentProperties(AssessmentProperties template)
    {
        super(template);

        if (template != null)
        {
            assessor             = template.getAssessor();
            assessorTypeName     = template.getAssessorTypeName();
            assessorPropertyName = template.getAssessorPropertyName();
            assessmentTime       = template.getAssessmentTime();
            expiryTime           = template.getExpiryTime();
            notes                = template.getNotes();
            additionalProperties = template.getAdditionalProperties();
        }
    }


    /**
     * Return the person or organization that made the assessment.
     *
     * @return string
     */
    public String getAssessor()
    {
        return assessor;
    }


    /**
     * Set up the person or organization that made the assessment.
     *
     * @param assessor string
     */
    public void setAssessor(String assessor)
    {
        this.assessor = assessor;
    }


    /**
     * Return the type of element referenced in the assessor property.
     *
     * @return string
     */
    public String getAssessorTypeName()
    {
        return assessorTypeName;
    }


    /**
     * Set up the type of element referenced in the assessor property.
     *
     * @param assessorTypeName string
     */
    public void setAssessorTypeName(String assessorTypeName)
    {
        this.assessorTypeName = assessorTypeName;
    }


    /**
     * Return the name of the property from the element used to identify the assessor.
     *
     * @return string
     */
    public String getAssessorPropertyName()
    {
        return assessorPropertyName;
    }


    /**
     * Set up the name of the property from the element used to identify the assessor.
     *
     * @param assessorPropertyName string
     */
    public void setAssessorPropertyName(String assessorPropertyName)
    {
        this.assessorPropertyName = assessorPropertyName;
    }


    /**
     * Return the time that the assessment was made.
     *
     * @return date
     */
    public Date getAssessmentTime()
    {
        return assessmentTime;
    }


    /**
     * Set up the time that the assessment was made.
     *
     * @param assessmentTime date
     */
    public void setAssessmentTime(Date assessmentTime)
    {
        this.assessmentTime = assessmentTime;
    }


    /**
     * Return the time when the assessment will be obsolete and should no longer be relied on.
     *
     * @return date
     */
    public Date getExpiryTime()
    {
        return expiryTime;
    }


    /**
     * Set up the time when the assessment will be obsolete and should no longer be relied on.
     *
     * @param expiryTime date
     */
    public void setExpiryTime(Date expiryTime)
    {
        this.expiryTime = expiryTime;
    }


    /**
     * Return the notes on the assessment.
     *
     * @return string
     */
    public String getNotes()
    {
        return notes;
    }


    /**
     * Set up the notes on the assessment.
     *
     * @param notes string
     */
    public void setNotes(String notes)
    {
        this.notes = notes;
    }


    /**
     * Return the additional properties for the assessment that are not directly supported by the relationship.
     *
     * @return map
     */
    public Map<String, String> getAdditionalProperties()
    {
        return additionalProperties;
    }


    /**
     * Set up the additional properties for the assessment that are not directly supported by the relationship.
     *
     * @param additionalProperties map
     */
    public void setAdditionalProperties(Map<String, String> additionalProperties)
    {
        this.additionalProperties = additionalProperties;
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "AssessmentProperties{" +
                "assessor='" + assessor + '\'' +
                ", assessorTypeName='" + assessorTypeName + '\'' +
                ", assessorPropertyName='" + assessorPropertyName + '\'' +
                ", assessmentTime=" + assessmentTime +
                ", expiryTime=" + expiryTime +
                ", notes='" + notes + '\'' +
                ", additionalProperties=" + additionalProperties +
                "} " + super.toString();
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
        if (this == objectToCompare) return true;
        if (objectToCompare == null || getClass() != objectToCompare.getClass()) return false;
        if (!super.equals(objectToCompare)) return false;
        AssessmentProperties that = (AssessmentProperties) objectToCompare;
        return Objects.equals(assessor, that.assessor) &&
                Objects.equals(assessorTypeName, that.assessorTypeName) &&
                Objects.equals(assessorPropertyName, that.assessorPropertyName) &&
                Objects.equals(assessmentTime, that.assessmentTime) &&
                Objects.equals(expiryTime, that.expiryTime) &&
                Objects.equals(notes, that.notes) &&
                Objects.equals(additionalProperties, that.additionalProperties);
    }


    /**
     * Return hash code based on properties.
     *
     * @return int
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), assessor, assessorTypeName, assessorPropertyName, assessmentTime, expiryTime, notes, additionalProperties);
    }
}
