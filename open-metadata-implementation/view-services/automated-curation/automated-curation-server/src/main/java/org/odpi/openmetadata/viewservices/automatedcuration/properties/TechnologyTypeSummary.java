/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.viewservices.automatedcuration.properties;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;


/**
 * TechnologyTypeSummary is used in return summary information about a technology type.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class TechnologyTypeSummary
{
    private String technologyTypeGUID = null;
    private String qualifiedName      = null;
    private String displayName        = null;
    private String description        = null;
    private String category           = null;


    public TechnologyTypeSummary()
    {
    }


    public TechnologyTypeSummary(TechnologyTypeSummary template)
    {
        if (template != null)
        {
            technologyTypeGUID = template.getTechnologyTypeGUID();
            qualifiedName = template.getQualifiedName();
            displayName   = template.getDisplayName();
            description   = template.getDescription();
            category = template.getCategory();
        }
    }


    public String getTechnologyTypeGUID()
    {
        return technologyTypeGUID;
    }

    public void setTechnologyTypeGUID(String technologyTypeGUID)
    {
        this.technologyTypeGUID = technologyTypeGUID;
    }

    public String getQualifiedName()
    {
        return qualifiedName;
    }

    public void setQualifiedName(String qualifiedName)
    {
        this.qualifiedName = qualifiedName;
    }

    public String getDisplayName()
    {
        return displayName;
    }

    public void setDisplayName(String displayName)
    {
        this.displayName = displayName;
    }

    public String getDescription()
    {
        return description;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    public String getCategory()
    {
        return category;
    }

    public void setCategory(String category)
    {
        this.category = category;
    }


    /**
     * Generate a string containing the properties.
     *
     * @return string value
     */
    @Override
    public String toString()
    {
        return "TechnologyTypeSummary{" +
                "technologyTypeGUID='" + technologyTypeGUID + '\'' +
                ", qualifiedName='" + qualifiedName + '\'' +
                ", displayName='" + displayName + '\'' +
                ", description='" + description + '\'' +
                ", category='" + category + '\'' +
                '}';
    }
}
