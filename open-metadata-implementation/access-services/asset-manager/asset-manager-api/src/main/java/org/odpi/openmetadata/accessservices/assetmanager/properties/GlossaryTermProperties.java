/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.assetmanager.properties;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serial;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;


/**
 * GlossaryTermProperties contains the semantic definition (meaning) of a word or phrase
 * (term - collectively called terminology).
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class GlossaryTermProperties extends ReferenceableProperties
{
    @Serial
    private static final long serialVersionUID = 1L;

    private String displayName              = null;
    private String summary                  = null;
    private String description              = null;
    private String examples                 = null;
    private String abbreviation             = null;
    private String usage                    = null;
    private String publishVersionIdentifier = null;


    /**
     * Default constructor
     */
    public GlossaryTermProperties()
    {
        super();
    }


    /**
     * Copy/clone constructor.
     *
     * @param template object to copy
     */
    public GlossaryTermProperties(GlossaryTermProperties template)
    {
        super(template);

        if (template != null)
        {
            displayName = template.getDisplayName();
            summary = template.getSummary();
            description = template.getDescription();
            examples = template.getExamples();
            abbreviation = template.getAbbreviation();
            usage = template.getUsage();
            usage = template.getPublishVersionIdentifier();
        }
    }


    /**
     * Returns the stored display name property for the term.
     * If no display name is available then null is returned.
     *
     * @return String name
     */
    public String getDisplayName()
    {
        return displayName;
    }


    /**
     * Set up the stored display name property for the term.
     *
     * @param displayName String name
     */
    public void setDisplayName(String displayName)
    {
        this.displayName = displayName;
    }


    /**
     * Return the short (1-2 sentence) description of the term.
     *
     * @return string text
     */
    public String getSummary()
    {
        return summary;
    }


    /**
     * Set up the short (1-2 sentence) description of the term.
     *
     * @param summary string text
     */
    public void setSummary(String summary)
    {
        this.summary = summary;
    }


    /**
     * Returns the stored description property for the term.
     * If no description is provided then null is returned.
     *
     * @return  String text
     */
    public String getDescription()
    {
        return description;
    }


    /**
     * Set up the stored description property for the term.
     *
     * @param description String text
     */
    public void setDescription(String description)
    {
        this.description = description;
    }


    /**
     * Set up the description of one or more examples.
     *
     * @return string text
     */
    public String getExamples()
    {
        return examples;
    }


    /**
     * Return the description of one or more examples.
     *
     * @param examples string text
     */
    public void setExamples(String examples)
    {
        this.examples = examples;
    }


    /**
     * Return the abbreviation used for this term.
     *
     * @return string text
     */
    public String getAbbreviation()
    {
        return abbreviation;
    }


    /**
     * Set up the abbreviation used for this term.
     *
     * @param abbreviation string text
     */
    public void setAbbreviation(String abbreviation)
    {
        this.abbreviation = abbreviation;
    }


    /**
     * Return details of the expected usage of this term.
     *
     * @return string text
     */
    public String getUsage()
    {
        return usage;
    }


    /**
     * Set up details of the expected usage of this term.
     *
     * @param usage string text
     */
    public void setUsage(String usage)
    {
        this.usage = usage;
    }


    /**
     * Return the author-controlled version identifier.
     *
     * @return version identifier
     */
    public String getPublishVersionIdentifier()
    {
        return publishVersionIdentifier;
    }


    /**
     * Set up the author-controlled version identifier.
     *
     * @param publishVersionIdentifier version identifier
     */
    public void setPublishVersionIdentifier(String publishVersionIdentifier)
    {
        this.publishVersionIdentifier = publishVersionIdentifier;
    }


    /**
     * JSON-style toString
     *
     * @return return string containing the property names and values
     */
    @Override
    public String toString()
    {
        return "GlossaryTermProperties{" +
                       "displayName='" + displayName + '\'' +
                       ", summary='" + summary + '\'' +
                       ", description='" + description + '\'' +
                       ", examples='" + examples + '\'' +
                       ", abbreviation='" + abbreviation + '\'' +
                       ", usage='" + usage + '\'' +
                       ", publishVersionNumber='" + publishVersionIdentifier + '\'' +
                       ", qualifiedName='" + getQualifiedName() + '\'' +
                       ", additionalProperties=" + getAdditionalProperties() +
                       ", effectiveFrom=" + getEffectiveFrom() +
                       ", effectiveTo=" + getEffectiveTo() +
                       ", vendorProperties=" + getVendorProperties() +
                       ", typeName='" + getTypeName() + '\'' +
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
        GlossaryTermProperties that = (GlossaryTermProperties) objectToCompare;
        return Objects.equals(getDisplayName(), that.getDisplayName()) &&
                       Objects.equals(getSummary(), that.getSummary()) &&
                       Objects.equals(getDescription(), that.getDescription()) &&
                       Objects.equals(getExamples(), that.getExamples()) &&
                       Objects.equals(getAbbreviation(), that.getAbbreviation()) &&
                       Objects.equals(getUsage(), that.getUsage()) &&
                       Objects.equals(getPublishVersionIdentifier(), that.getPublishVersionIdentifier());
    }


    /**
     * Return hash code for this object
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), getDisplayName(), getSummary(), getDescription(), getExamples(), getAbbreviation(), getUsage(), getPublishVersionIdentifier());
    }
}
