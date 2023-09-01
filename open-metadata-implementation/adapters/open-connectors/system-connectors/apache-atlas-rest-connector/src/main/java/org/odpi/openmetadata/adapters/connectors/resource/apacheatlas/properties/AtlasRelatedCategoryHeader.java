/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.resource.apacheatlas.properties;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * AtlasRelatedTermHeader describes the relationship between a glossary and one of its categories.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class AtlasRelatedCategoryHeader
{
    private String categoryGuid       = null;
    private String parentCategoryGuid = null;
    private String relationGuid       = null;
    private String displayText        = null; // name in properties


    public AtlasRelatedCategoryHeader()
    {
    }


    public AtlasRelatedCategoryHeader(AtlasRelatedCategoryHeader template)
    {
        if (template != null)
        {
            categoryGuid = template.getCategoryGuid();
            parentCategoryGuid = template.getParentCategoryGuid();
            relationGuid = template.getRelationGuid();
            displayText = template.getDisplayText();
        }
    }


    public String getCategoryGuid()
    {
        return categoryGuid;
    }


    public void setCategoryGuid(String categoryGuid)
    {
        this.categoryGuid = categoryGuid;
    }


    public String getParentCategoryGuid()
    {
        return parentCategoryGuid;
    }


    public void setParentCategoryGuid(String parentCategoryGuid)
    {
        this.parentCategoryGuid = parentCategoryGuid;
    }


    public String getRelationGuid()
    {
        return relationGuid;
    }


    public void setRelationGuid(String relationGuid)
    {
        this.relationGuid = relationGuid;
    }


    public String getDisplayText()
    {
        return displayText;
    }


    public void setDisplayText(String displayText)
    {
        this.displayText = displayText;
    }


    @Override
    public String toString()
    {
        return "AtlasRelatedCategoryHeader{" +
                       "categoryGuid='" + categoryGuid + '\'' +
                       ", parentCategoryGuid='" + parentCategoryGuid + '\'' +
                       ", relationGuid='" + relationGuid + '\'' +
                       ", displayText='" + displayText + '\'' +
                       '}';
    }
}
