/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.assetmanager.properties;

import com.fasterxml.jackson.annotation.*;

import java.io.Serial;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * GlossaryProperties is a class for representing a generic glossary.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class GlossaryProperties extends ReferenceableProperties
{
    private String displayName = null;
    private String description = null;
    private String language    = null;
    private String usage       = null;

    /**
     * Default constructor
     */
    public GlossaryProperties()
    {
        super();
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
            displayName = template.getDisplayName();
            description = template.getDescription();
            language = template.getLanguage();
            usage = template.getUsage();
        }
    }


    /**
     * Return a human memorable name for the glossary.
     *
     * @return string  name
     */
    public String getDisplayName()
    {
        return displayName;
    }


    /**
     * Set up a human memorable name for the glossary.
     *
     * @param displayName string name
     */
    public void setDisplayName(String displayName)
    {
        this.displayName = displayName;
    }


    /**
     * Return the description of the glossary.
     *
     * @return string text
     */
    public String getDescription()
    {
        return description;
    }


    /**
     * Set up the description of the glossary.
     *
     * @param description string text
     */
    public void setDescription(String description)
    {
        this.description = description;
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
                       "displayName='" + displayName + '\'' +
                       ", description='" + description + '\'' +
                       ", language='" + language + '\'' +
                       ", usage='" + usage + '\'' +
                       ", qualifiedName='" + getQualifiedName() + '\'' +
                       ", additionalProperties=" + getAdditionalProperties() +
                       ", vendorProperties=" + getVendorProperties() +
                       ", typeName='" + getTypeName() + '\'' +
                       ", extendedProperties=" + getExtendedProperties() +
                       '}';
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
        return Objects.equals(displayName, that.displayName) &&
                Objects.equals(description, that.description) &&
                Objects.equals(language, that.language) &&
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
        return Objects.hash(super.hashCode(), displayName, description, language, usage);
    }
}
