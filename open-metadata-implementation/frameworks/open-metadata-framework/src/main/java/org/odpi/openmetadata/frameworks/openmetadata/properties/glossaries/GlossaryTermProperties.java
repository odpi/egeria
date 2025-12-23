/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.openmetadata.properties.glossaries;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.frameworks.openmetadata.properties.AuthoredReferenceableProperties;
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
public class GlossaryTermProperties extends AuthoredReferenceableProperties
{
    private List<String> aliases           = null;
    private String       summary           = null;
    private String       examples          = null;
    private String       abbreviation      = null;
    private String       usage             = null;



    /**
     * Default constructor
     */
    public GlossaryTermProperties()
    {
        super();
        super.typeName = OpenMetadataType.GLOSSARY_TERM.typeName;
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
            aliases           = template.getAliases();
            summary           = template.getSummary();
            examples          = template.getExamples();
            abbreviation      = template.getAbbreviation();
            usage             = template.getUsage();
        }
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
     * JSON-style toString
     *
     * @return return string containing the property names and values
     */
    @Override
    public String toString()
    {
        return "GlossaryTermProperties{" +
                "aliases=" + aliases +
                ", summary='" + summary + '\'' +
                ", examples='" + examples + '\'' +
                ", abbreviation='" + abbreviation + '\'' +
                ", usage='" + usage + '\'' +
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
        if (this == objectToCompare) return true;
        if (objectToCompare == null || getClass() != objectToCompare.getClass()) return false;
        if (!super.equals(objectToCompare)) return false;
        GlossaryTermProperties that = (GlossaryTermProperties) objectToCompare;
        return Objects.equals(aliases, that.aliases) &&
                Objects.equals(summary, that.summary) &&
                Objects.equals(examples, that.examples) &&
                Objects.equals(abbreviation, that.abbreviation) &&
                Objects.equals(usage, that.usage);
    }

    /**
     * Return hash code for this object
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), aliases, summary, examples, abbreviation, usage);
    }
}
