/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworks.openmetadata.properties.glossaries;

import com.fasterxml.jackson.annotation.*;
import org.odpi.openmetadata.frameworks.openmetadata.enums.TermRelationshipStatus;
import org.odpi.openmetadata.frameworks.openmetadata.properties.RelationshipBeanProperties;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * GlossaryTermRelationshipProperties describes a type of relationship between two glossary terms in a glossary.
 */
@JsonAutoDetect(getterVisibility = PUBLIC_ONLY, setterVisibility = PUBLIC_ONLY, fieldVisibility = NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "class")
@JsonSubTypes({
        @JsonSubTypes.Type(value = RelatedTermProperties.class, name = "RelatedTermProperties"),
        @JsonSubTypes.Type(value = SynonymProperties.class, name = "SynonymProperties"),
        @JsonSubTypes.Type(value = AntonymProperties.class, name = "AntonymProperties"),
        @JsonSubTypes.Type(value = PreferredTermProperties.class, name = "PreferredTermProperties"),
        @JsonSubTypes.Type(value = ReplacementTermProperties.class, name = "ReplacementTermProperties"),
        @JsonSubTypes.Type(value = ISARelationshipProperties.class, name = "ISARelationshipProperties"),
        @JsonSubTypes.Type(value = UsedInContextProperties.class, name = "UsedInContextProperties"),

})
public class GlossaryTermRelationshipProperties extends RelationshipBeanProperties
{
    private String                 expression  = null;
    private Integer                confidence  = null;
    private String                 description            = null;
    private TermRelationshipStatus termRelationshipStatus = null;
    private String                 steward                = null;
    private String                 source      = null;


    /**
     * Default constructor
     */
    public GlossaryTermRelationshipProperties()
    {
        super();
    }


    /**
     * Copy/clone constructor.
     *
     * @param template template object to copy.
     */
    public GlossaryTermRelationshipProperties(GlossaryTermRelationshipProperties template)
    {
        super(template);

        if (template != null)
        {
            expression = template.getExpression();
            description = template.getDescription();
            confidence             = template.getConfidence();
            termRelationshipStatus = template.getTermRelationshipStatus();
            steward                = template.getSteward();
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
     * Return the level of confidence that the relationship is correct.  0 means unassigned. Typical assigned values are usually between 1-100
     * as a percentage scale.
     *
     * @return int
     */
    public Integer getConfidence()
    {
        return confidence;
    }


    /**
     * Set up the level of confidence that the relationship is correct.  0 means unassigned. Typical assigned values are usually between 1-100
     * as a percentage scale.
     *
     * @param confidence int
     */
    public void setConfidence(Integer confidence)
    {
        this.confidence = confidence;
    }


    /**
     * Set up whether this relationship should be used.
     *
     * @param termRelationshipStatus status enum (draft, active, deprecated, obsolete, other)
     */
    public void setTermRelationshipStatus(TermRelationshipStatus termRelationshipStatus)
    {
        this.termRelationshipStatus = termRelationshipStatus;
    }


    /**
     * Returns whether this relationship should be used.
     *
     * @return status enum (draft, active, deprecated, obsolete, other)
     */
    public TermRelationshipStatus getTermRelationshipStatus()
    {
        return termRelationshipStatus;
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
     * Returns the name of the steward who assigned the relationship (or approved the discovered value).
     *
     * @return user id or name of steward
     */
    public String getSteward()
    {
        return steward;
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
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "GlossaryTermRelationshipProperties{" +
                "expression='" + expression + '\'' +
                ", description='" + description + '\'' +
                ", termRelationshipStatus=" + termRelationshipStatus +
                ", confidence=" + confidence +
                ", steward='" + steward + '\'' +
                ", source='" + source + '\'' +
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
        if (objectToCompare == null || getClass() != objectToCompare.getClass())
        {
            return false;
        }
        if (! super.equals(objectToCompare))
        {
            return false;
        }
        GlossaryTermRelationshipProperties that = (GlossaryTermRelationshipProperties) objectToCompare;
        return Objects.equals(confidence, that.confidence) &&
                       Objects.equals(expression, that.expression) &&
                       Objects.equals(description, that.description) && termRelationshipStatus == that.termRelationshipStatus &&
                       Objects.equals(steward, that.steward) && Objects.equals(source, that.source);
    }


    /**
     * Return hash code based on properties.
     *
     * @return int
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), expression, confidence, description, termRelationshipStatus, steward, source);
    }
}
