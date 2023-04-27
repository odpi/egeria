/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.integration.apacheatlas.properties;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.HashMap;
import java.util.Map;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * AtlasGlossaryBaseProperties describes the common properties found on all an Apache Atlas glossary elements.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class AtlasGlossaryBaseProperties
{
    private String              guid                 = null;
    private String              qualifiedName        = null;
    private String              name                 = null;
    private String              shortDescription     = null;
    private String              longDescription      = null;
    private Map<String, Object> additionalAttributes = null;


    /**
     * Standard constructor
     */
    public AtlasGlossaryBaseProperties()
    {
    }


    /**
     * Copy/clone Constructor
     *
     * @param template object to copy
     */
    public AtlasGlossaryBaseProperties(AtlasGlossaryBaseProperties template)
    {
        if (template != null)
        {
            guid = template.getGuid();
            qualifiedName = template.getQualifiedName();
            name = template.getQualifiedName();
            shortDescription = template.getShortDescription();
            longDescription = template.getLongDescription();
            additionalAttributes = template.getAdditionalAttributes();
        }
    }


    /**
     * Return the unique identifier from Atlas.
     *
     * @return string
     */
    public String getGuid()
    {
        return guid;
    }


    /**
     * Set up the unique identifier from Atlas.
     *
     * @param guid string
     */
    public void setGuid(String guid)
    {
        this.guid = guid;
    }


    /**
     * Return the unique name from
     * @return
     */
    public String getQualifiedName()
    {
        return qualifiedName;
    }


    public void setQualifiedName(String qualifiedName)
    {
        this.qualifiedName = qualifiedName;
    }


    public String getName()
    {
        return name;
    }


    public void setName(String name)
    {
        this.name = name;
    }


    public String getShortDescription()
    {
        return shortDescription;
    }


    public void setShortDescription(String shortDescription)
    {
        this.shortDescription = shortDescription;
    }


    public String getLongDescription()
    {
        return longDescription;
    }


    public void setLongDescription(String longDescription)
    {
        this.longDescription = longDescription;
    }


    public Map<String, Object> getAdditionalAttributes()
    {
        return additionalAttributes;
    }


    public void setAdditionalAttributes(Map<String, Object> additionalAttributes)
    {
        this.additionalAttributes = additionalAttributes;
    }


    @Override
    public String toString()
    {
        return "AtlasGlossaryBaseProperties{" +
                       "guid='" + guid + '\'' +
                       ", qualifiedName='" + qualifiedName + '\'' +
                       ", name='" + name + '\'' +
                       ", shortDescription='" + shortDescription + '\'' +
                       ", longDescription='" + longDescription + '\'' +
                       ", additionalAttributes=" + additionalAttributes +
                       '}';
    }
}
