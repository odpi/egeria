/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.integration.apacheatlas.properties;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * AtlasRelatedTermHeader describes the relationship between a glossary and one of its terms.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class AtlasGlossaryAnchorElement
{
    private String glossaryGuid = null;
    private String relationGuid = null;
   private String  displayText = null;


    public AtlasGlossaryAnchorElement()
    {
    }


    public AtlasGlossaryAnchorElement(AtlasGlossaryAnchorElement template)
    {
        if (template != null)
        {
            glossaryGuid = template.getGlossaryGuid();
            relationGuid = template.getRelationGuid();
            displayText = template.getDisplayText();
        }
    }


    public String getGlossaryGuid()
    {
        return glossaryGuid;
    }


    public void setGlossaryGuid(String glossaryGuid)
    {
        this.glossaryGuid = glossaryGuid;
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
        return "AtlasRelatedTermHeader{" +
                       "termGuid='" + glossaryGuid + '\'' +
                       ", relationGuid='" + relationGuid + '\'' +
                       ", displayText='" + displayText + '\'' +
                       '}';
    }
}
