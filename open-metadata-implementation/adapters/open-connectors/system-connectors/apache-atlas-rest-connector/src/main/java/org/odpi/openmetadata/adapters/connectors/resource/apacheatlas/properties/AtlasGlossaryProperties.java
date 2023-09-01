/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.resource.apacheatlas.properties;


import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * AtlasGlossaryProperties provides the properties of a glossary - used as part of a request body.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class AtlasGlossaryProperties extends AtlasGlossaryBaseProperties
{
    private String language = null;
    private String usage    = null;


    /**
     * Standard constructor
     */
    public AtlasGlossaryProperties()
    {
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public AtlasGlossaryProperties(AtlasGlossaryProperties template)
    {
        super(template);

        if (template != null)
        {
            language = template.getLanguage();
            usage = template.getUsage();
        }
    }


    public String getLanguage()
    {
        return language;
    }


    public void setLanguage(String language)
    {
        this.language = language;
    }


    public String getUsage()
    {
        return usage;
    }


    public void setUsage(String usage)
    {
        this.usage = usage;
    }


    @Override
    public String toString()
    {
        return "AtlasGlossaryProperties{" +
                       "language='" + language + '\'' +
                       ", usage='" + usage + '\'' +
                       ", guid='" + getGuid() + '\'' +
                       ", qualifiedName='" + getQualifiedName() + '\'' +
                       ", name='" + getName() + '\'' +
                       ", shortDescription='" + getShortDescription() + '\'' +
                       ", longDescription='" + getLongDescription() + '\'' +
                       ", additionalAttributes=" + getAdditionalAttributes() +
                       '}';
    }
}
