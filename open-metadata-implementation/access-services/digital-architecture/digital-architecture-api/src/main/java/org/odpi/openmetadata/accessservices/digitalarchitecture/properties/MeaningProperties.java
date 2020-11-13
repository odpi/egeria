/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.digitalarchitecture.properties;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * Meaning is a cut-down summary of a glossary term to aid in understanding the content
 * of an element.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class MeaningProperties extends ReferenceableProperties
{
    private static final long     serialVersionUID = 1L;

    /*
     * Attributes of a meaning object definition
     */
    private String displayName  = null;
    private String description  = null;
    private String summary      = null;
    private String examples     = null;
    private String abbreviation = null;
    private String usage        = null;


    /**
     * Default constructor
     */
    public MeaningProperties()
    {
        super();
    }


    /**
     * Copy/clone constructor.
     *
     * @param template element to copy
     */
    public MeaningProperties(MeaningProperties template)
    {
        /*
         * Save the parent asset description.
         */
        super(template);

        if (template != null)
        {
            this.displayName = template.getDisplayName();
            this.summary = template.getSummary();
            this.description = template.getDescription();
            this.examples = template.getExamples();
            this.abbreviation = template.getAbbreviation();
            this.usage = template.getUsage();
        }
    }


    /**
     * Return the display name for this term (normally a shortened form of the qualified name).
     *
     * @return string name
     */
    public String getDisplayName()
    {
        return displayName;
    }


    /**
     * Set up the display name for this term (normally a shortened form of the qualified name).
     *
     * @param displayName string name
     */
    public void setDisplayName(String displayName)
    {
        this.displayName = displayName;
    }


    /**
     * Return a short summary of the meaning of the term - used for summary lists of terms.
     *
     * @return string description
     */
    public String getSummary()
    {
        return summary;
    }


    /**
     * Set up a short summary of the meaning of the term - used for summary lists of terms.
     *
     * @param summary string description
     */
    public void setSummary(String summary)
    {
        this.summary = summary;
    }


    /**
     * Return the detailed description of the term's meaning.  This would be used where the term is the primary
     * information being displayed.
     *
     * @return string description
     */
    public String getDescription()
    {
        return description;
    }


    /**
     * Set up the detailed description of the term's meaning.  This would be used where the term is the primary
     * information being displayed.
     *
     * @param description string description
     */
    public void setDescription(String description)
    {
        this.description = description;
    }


    /**
     * Return example sentences that include this term.
     *
     * @return string examples
     */
    public String getExamples()
    {
        return examples;
    }


    /**
     * Set up example sentences that include this term.
     *
     * @param examples string examples
     */
    public void setExamples(String examples)
    {
        this.examples = examples;
    }


    /**
     * Return the abbreviation for this term (or null).
     *
     * @return string abbreviation
     */
    public String getAbbreviation()
    {
        return abbreviation;
    }


    /**
     * Set up the abbreviation for this term (or null).
     *
     * @param abbreviation string abbreviation
     */
    public void setAbbreviation(String abbreviation)
    {
        this.abbreviation = abbreviation;
    }


    /**
     * Return instructions for how and when this term should be used.
     *
     * @return string usage instructions
     */
    public String getUsage()
    {
        return usage;
    }


    /**
     * Set up instructions for how and when this term should be used.
     *
     * @param usage string usage instructions
     */
    public void setUsage(String usage)
    {
        this.usage = usage;
    }


    /**
     * JSON-style toString
     *
     * @return return string containing the property names and values
     */
    @Override
    public String toString()
    {
        return "MeaningProperties{" +
                "displayName='" + displayName + '\'' +
                ", summary='" + summary + '\'' +
                ", description='" + description + '\'' +
                ", examples='" + examples + '\'' +
                ", abbreviation='" + abbreviation + '\'' +
                ", usage='" + usage + '\'' +
                ", qualifiedName='" + getQualifiedName() + '\'' +
                ", additionalProperties=" + getAdditionalProperties() +
                ", meanings=" + getMeanings() +
                ", type=" + getTypeName() +
                ", classifications=" + getClassifications() +
                ", extendedProperties=" + getExtendedProperties() +
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
        MeaningProperties that = (MeaningProperties) objectToCompare;
        return Objects.equals(getDisplayName(), that.getDisplayName()) &&
                Objects.equals(getSummary(), that.getSummary()) &&
                Objects.equals(getDescription(), that.getDescription()) &&
                Objects.equals(getExamples(), that.getExamples()) &&
                Objects.equals(getAbbreviation(), that.getAbbreviation()) &&
                Objects.equals(getUsage(), that.getUsage());
    }


    /**
     * Return hash code for this object
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), getDisplayName(), getSummary(), getDescription(), getExamples(),
                            getAbbreviation(), getUsage());
    }
}