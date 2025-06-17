/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.openmetadata.properties.glossaries;

import com.fasterxml.jackson.annotation.*;
import org.odpi.openmetadata.frameworks.openmetadata.properties.ReferenceableProperties;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.util.List;
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
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "class")
@JsonSubTypes(
        {
                @JsonSubTypes.Type(value = ControlledGlossaryTermProperties.class, name = "ControlledGlossaryTermProperties"),
        })
public class GlossaryTermProperties extends ReferenceableProperties
{
    private String       displayName              = null;
    private List<String> aliases                  = null;
    private String       summary                  = null;
    private String       description              = null;
    private String       examples                 = null;
    private String       abbreviation             = null;
    private String       usage                    = null;
    private String       publishVersionIdentifier = null;


    /**
     * Default constructor
     */
    public GlossaryTermProperties()
    {
        super();
        super.setTypeName(OpenMetadataType.GLOSSARY_TERM.typeName);
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
            aliases = template.getAliases();
            summary = template.getSummary();
            description = template.getDescription();
            examples = template.getExamples();
            abbreviation = template.getAbbreviation();
            usage = template.getUsage();
            publishVersionIdentifier = template.getPublishVersionIdentifier();
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
     * Return the list of alternative names for the term.
     *
     * @return list
     */
    public List<String> getAliases()
    {
        return aliases;
    }


    /**
     * Set up the list of alternative names for the term.
     *
     * @param aliases list
     */
    public void setAliases(List<String> aliases)
    {
        this.aliases = aliases;
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
                ", aliases=" + aliases +
                ", summary='" + summary + '\'' +
                ", description='" + description + '\'' +
                ", examples='" + examples + '\'' +
                ", abbreviation='" + abbreviation + '\'' +
                ", usage='" + usage + '\'' +
                ", publishVersionIdentifier='" + publishVersionIdentifier + '\'' +
                "} " + super.toString();
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
                Objects.equals(getAliases(), that.getAliases()) &&
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
        return Objects.hash(super.hashCode(), displayName, aliases, summary, description, examples, abbreviation, usage, publishVersionIdentifier);
    }
}
