/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.metadatasecurity.properties;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

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
public class Glossary extends Referenceable
{
    @Serial
    private static final long serialVersionUID = 1L;

    private String displayName = null;
    private String description = null;
    private String language    = null;
    private String usage       = null;

    /**
     * Default constructor
     */
    public Glossary()
    {
        super();
    }


    /**
     * Copy/clone constructor.
     *
     * @param template object to copy
     */
    public Glossary(Glossary template)
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
        return "Glossary{" +
                       "displayName='" + displayName + '\'' +
                       ", description='" + description + '\'' +
                       ", language='" + language + '\'' +
                       ", usage='" + usage + '\'' +
                       ", typeGUID='" + getTypeGUID() + '\'' +
                       ", typeName='" + getTypeName() + '\'' +
                       ", status=" + getStatus() +
                       ", GUID='" + getGUID() + '\'' +
                       ", qualifiedName='" + getQualifiedName() + '\'' +
                       ", additionalProperties=" + getAdditionalProperties() +
                       ", owner='" + getOwner() + '\'' +
                       ", ownerType=" + getOwnerType() +
                       ", extendedProperties=" + getExtendedProperties() +
                       ", securityLabels=" + getSecurityLabels() +
                       ", securityProperties=" + getSecurityProperties() +
                       ", accessGroups=" + getAccessGroups() +
                       ", confidentiality=" + getConfidentiality() +
                       ", confidence=" + getConfidence() +
                       ", criticality=" + getCriticality() +
                       ", impact=" + getImpact() +
                       ", retention=" + getRetention() +
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
        Glossary that = (Glossary) objectToCompare;
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
