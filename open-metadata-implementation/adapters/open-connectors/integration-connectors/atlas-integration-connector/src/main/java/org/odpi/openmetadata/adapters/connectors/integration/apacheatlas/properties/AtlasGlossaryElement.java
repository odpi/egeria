/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.integration.apacheatlas.properties;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * AtlasGlossaryElement describes an Apache Atlas glossary and its terms retrieved from Apache Atlas.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class AtlasGlossaryElement extends AtlasGlossaryBaseProperties
{
    private AtlasRelatedTermHeader terms = null;


    public AtlasGlossaryElement()
    {
    }


    public AtlasGlossaryElement(AtlasGlossaryElement template)
    {
        super(template);

        if (template != null)
        {
            terms = template.getTerms();
        }
    }


    public AtlasRelatedTermHeader getTerms()
    {
        return terms;
    }


    public void setTerms(AtlasRelatedTermHeader terms)
    {
        this.terms = terms;
    }


    @Override
    public String toString()
    {
        return "AtlasGlossaryElement{" +
                       "terms=" + terms +
                       ", guid='" + getGuid() + '\'' +
                       ", qualifiedName='" + getQualifiedName() + '\'' +
                       ", name='" + getName() + '\'' +
                       ", shortDescription='" + getShortDescription() + '\'' +
                       ", longDescription='" + getLongDescription() + '\'' +
                       '}';
    }
}
