/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.integration.apacheatlas.properties;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * AtlasGlossaryTermProperties describes an Apache Atlas glossary term used to call Apache Atlas.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class AtlasGlossaryTermProperties extends AtlasGlossaryMemberBaseProperties
{
    private String abbreviation = null;
    private String usage        = null;


    /**
     * Standard constructor
     */
    public AtlasGlossaryTermProperties()
    {
    }


    /**
     * Copy/clone Constructor
     *
     * @param template object to copy
     */
    public AtlasGlossaryTermProperties(AtlasGlossaryTermProperties template)
    {
        super(template);

        if (template != null)
        {
            abbreviation = template.getAbbreviation();
            usage = template.getUsage();
        }
    }


    public String getAbbreviation()
    {
        return abbreviation;
    }


    public void setAbbreviation(String abbreviation)
    {
        this.abbreviation = abbreviation;
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
        return "AtlasGlossaryTermProperties{" +
                       "abbreviation='" + abbreviation + '\'' +
                       ", usage='" + usage + '\'' +
                       ", anchor=" + getAnchor() +
                       ", guid='" + getGuid() + '\'' +
                       ", qualifiedName='" + getQualifiedName() + '\'' +
                       ", name='" + getName() + '\'' +
                       ", shortDescription='" + getShortDescription() + '\'' +
                       ", longDescription='" + getLongDescription() + '\'' +
                       ", additionalAttributes=" + getAdditionalAttributes() +
                       '}';
    }
}
