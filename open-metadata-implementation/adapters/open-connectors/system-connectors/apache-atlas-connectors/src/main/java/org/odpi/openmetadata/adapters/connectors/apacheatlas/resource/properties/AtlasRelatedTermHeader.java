/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.apacheatlas.resource.properties;

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
public class AtlasRelatedTermHeader
{
    private String termGuid = null;
    private String relationGuid	= null;
   private String  displayText = null; // name in properties


    public AtlasRelatedTermHeader()
    {
    }


    public AtlasRelatedTermHeader(AtlasRelatedTermHeader template)
    {
        if (template != null)
        {
            termGuid = template.getTermGuid();
            relationGuid = template.getRelationGuid();
            displayText = template.getDisplayText();
        }
    }


    public String getTermGuid()
    {
        return termGuid;
    }


    public void setTermGuid(String termGuid)
    {
        this.termGuid = termGuid;
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
                       "termGuid='" + termGuid + '\'' +
                       ", relationGuid='" + relationGuid + '\'' +
                       ", displayText='" + displayText + '\'' +
                       '}';
    }
}
