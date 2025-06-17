/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworks.openmetadata.properties.governance;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.frameworks.openmetadata.enums.GlossaryTermAssignmentStatus;
import org.odpi.openmetadata.frameworks.openmetadata.properties.RelationshipProperties;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * SemanticAssignmentProperties links an element to a glossary term to indicate that the glossary term describes its meaning.
 */
@JsonAutoDetect(getterVisibility = PUBLIC_ONLY, setterVisibility = PUBLIC_ONLY, fieldVisibility = NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class SemanticAssignmentProperties extends RelationshipProperties
{
    private String                       expression          = null;
    private String                       description         = null;
    private GlossaryTermAssignmentStatus status              = GlossaryTermAssignmentStatus.VALIDATED;
    private int                          confidence          = 0;
    private String                       createdBy           = null;
    private String                       steward             = null;
    private String                       stewardTypeName     = null;
    private String                       stewardPropertyName = null;
    private String                       source              = null;
    private String                       notes               = null;


    /**
     * Default constructor
     */
    public SemanticAssignmentProperties()
    {
        super();
        super.setTypeName(OpenMetadataType.SEMANTIC_ASSIGNMENT_RELATIONSHIP.typeName);
    }


    /**
     * Copy/clone constructor.
     *
     * @param template template object to copy.
     */
    public SemanticAssignmentProperties(SemanticAssignmentProperties template)
    {
        super(template);

        if (template != null)
        {
            expression = template.getExpression();
            description = template.getDescription();
            status = template.getStatus();
            confidence = template.getConfidence();
            createdBy = template.getCreatedBy();
            steward = template.getSteward();
            stewardTypeName = template.getStewardTypeName();
            stewardPropertyName = template.getStewardPropertyName();
            notes = template.getNotes();
            source = template.getSource();
        }
    }


    /**
     * Set up the expression that describes the relationship.
     *
     * @param expression String name
     */
    public void setExpression(String expression)
    {
        this.expression = expression;
    }


    /**
     * Returns the expression that describes the relationship.
     *
     * @return String name
     */
    public String getExpression()
    {
        return expression;
    }


    /**
     * Set up description of the relationship.
     *
     * @param description String
     */
    public void setDescription(String description)
    {
        this.description = description;
    }


    /**
     * Return the description for the relationship.
     *
     * @return String description
     */
    public String getDescription()
    {
        return description;
    }


    /**
     * Set up whether this relationship should be used.
     *
     * @param status status enum
     */
    public void setStatus(GlossaryTermAssignmentStatus status)
    {
        this.status = status;
    }


    /**
     * Returns whether this relationship should be used.
     *
     * @return status enum
     */
    public GlossaryTermAssignmentStatus getStatus()
    {
        return status;
    }


    /**
     * Return the level of confidence that the relationship is correct.  0 means unassigned. Typical assigned values are usually between 1-100
     * as a percentage scale.
     *
     * @return int
     */
    public int getConfidence()
    {
        return confidence;
    }


    /**
     * Set up the level of confidence that the relationship is correct.  0 means unassigned. Typical assigned values are usually between 1-100
     * as a percentage scale.
     *
     * @param confidence int
     */
    public void setConfidence(int confidence)
    {
        this.confidence = confidence;
    }


    /**
     * Return the name/description of the creator of the relationship.  The relationship includes the userId of the creator, so this field is for
     * a more business friendly name.
     *
     * @return string name
     */
    public String getCreatedBy()
    {
        return createdBy;
    }


    /**
     * Set up the name/description of the creator of the relationship.  The relationship includes the userId of the creator, so this field is for
     * a more business friendly name.
     *
     * @param createdBy string name
     */
    public void setCreatedBy(String createdBy)
    {
        this.createdBy = createdBy;
    }


    /**
     * Set up the id of the steward who assigned the relationship (or approved the discovered value).
     *
     * @param steward user id or name of steward
     */
    public void setSteward(String steward)
    {
        this.steward = steward;
    }


    /**
     * Returns the id of the steward who assigned the relationship (or approved the discovered value).
     *
     * @return user id or name of steward
     */
    public String getSteward()
    {
        return steward;
    }


    /**
     * Return the type of element identifying the steward.
     *
     * @return name
     */
    public String getStewardTypeName()
    {
        return stewardTypeName;
    }


    /**
     * Set up the type of element identifying the steward.
     *
     * @param stewardTypeName name
     */
    public void setStewardTypeName(String stewardTypeName)
    {
        this.stewardTypeName = stewardTypeName;
    }


    /**
     * Return the name of the property identifying the steward.
     *
     * @return string
     */
    public String getStewardPropertyName()
    {
        return stewardPropertyName;
    }


    /**
     * Set up the name of the property identifying the steward.
     *
     * @param stewardPropertyName string
     */
    public void setStewardPropertyName(String stewardPropertyName)
    {
        this.stewardPropertyName = stewardPropertyName;
    }


    /**
     * Set up the id of the source of the knowledge of the relationship.
     *
     * @param source String id
     */
    public void setSource(String source)
    {
        this.source = source;
    }


    /**
     * Returns the id of the source of the knowledge of the relationship.
     *
     * @return String id
     */
    public String getSource()
    {
        return source;
    }


    /**
     * Return the steward's notes.
     *
     * @return string
     */
    public String getNotes()
    {
        return notes;
    }


    /**
     * Set up the steward's notes.
     *
     * @param notes string
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
        return "SemanticAssignmentProperties{" +
                "expression='" + expression + '\'' +
                ", description='" + description + '\'' +
                ", status=" + status +
                ", confidence=" + confidence +
                ", createdBy='" + createdBy + '\'' +
                ", steward='" + steward + '\'' +
                ", stewardTypeName='" + stewardTypeName + '\'' +
                ", stewardPropertyName='" + stewardPropertyName + '\'' +
                ", source='" + source + '\'' +
                ", notes='" + notes + '\'' +
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
        if (this == objectToCompare)
        {
            return true;
        }
        if (! (objectToCompare instanceof SemanticAssignmentProperties that))
        {
            return false;
        }
        if (! super.equals(objectToCompare))
        {
            return false;
        }
        return confidence == that.confidence &&
                Objects.equals(expression, that.expression) &&
                Objects.equals(description, that.description) &&
                status == that.status &&
                Objects.equals(createdBy, that.createdBy) &&
                Objects.equals(steward, that.steward) &&
                Objects.equals(stewardTypeName, that.stewardTypeName) &&
                Objects.equals(stewardPropertyName, that.stewardPropertyName) &&
                Objects.equals(notes, that.notes) &&
                Objects.equals(source, that.source);
    }


    /**
     * Return hash code based on properties.
     *
     * @return int
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), expression, description, status, confidence, createdBy, steward, stewardTypeName, stewardPropertyName, notes, source);
    }
}
