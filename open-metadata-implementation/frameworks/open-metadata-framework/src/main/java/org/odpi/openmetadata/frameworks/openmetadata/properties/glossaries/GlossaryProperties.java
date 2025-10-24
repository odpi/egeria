/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworks.openmetadata.properties.glossaries;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.frameworks.openmetadata.properties.collections.CollectionProperties;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * GlossaryProperties is a class for representing a generic glossary.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class GlossaryProperties extends CollectionProperties
{
    private String language    = null;
    private String usage       = null;

    /**
     * Default constructor
     */
    public GlossaryProperties()
    {
        super();
        super.setTypeName(OpenMetadataType.GLOSSARY.typeName);
    }


    /**
     * Copy/clone constructor.
     *
     * @param template object to copy
     */
    public GlossaryProperties(GlossaryProperties template)
    {
        super(template);

        if (template != null)
        {
            language = template.getLanguage();
            usage = template.getUsage();
        }
    }


    /**
     * Return the language that the glossary contents are written in.
     *
     * @return string name
     */
    public String getLanguage()
    {
        return language;
    }


    /**
     * Set up the language that the glossary contents are written in.
     *
     * @param language string name
     */
    public void setLanguage(String language)
    {
        this.language = language;
    }


    /**
     * Return the expected usage of the glossary content.
     *
     * @return string description
     */
    public String getUsage()
    {
        return usage;
    }


    /**
     * Set up the expected usage of the glossary content.
     *
     * @param usage string description
     */
    public void setUsage(String usage)
    {
        this.usage = usage;
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "GlossaryProperties{" +
                "language='" + language + '\'' +
                ", usage='" + usage + '\'' +
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
        if (!super.equals(objectToCompare))
        {
            return false;
        }
        GlossaryProperties that = (GlossaryProperties) objectToCompare;
        return Objects.equals(language, that.language) &&
                Objects.equals(usage, that.usage);
    }


    /**
     * Return hash code based on properties.
     *
     * @return int
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), language, usage);
    }
}
