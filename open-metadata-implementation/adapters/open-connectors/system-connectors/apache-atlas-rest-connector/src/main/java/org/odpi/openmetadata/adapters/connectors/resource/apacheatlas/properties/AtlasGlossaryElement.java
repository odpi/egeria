/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.resource.apacheatlas.properties;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * AtlasGlossaryElement describes an Apache Atlas glossary and its terms/categories retrieved from Apache Atlas.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class AtlasGlossaryElement extends AtlasGlossaryProperties
{
    private List<AtlasRelatedTermHeader>     terms      = null;
    private List<AtlasRelatedCategoryHeader> categories = null;


    public AtlasGlossaryElement()
    {
    }


    public AtlasGlossaryElement(AtlasGlossaryElement template)
    {
        super(template);

        if (template != null)
        {
            terms = template.getTerms();
            categories = template.getCategories();
        }
    }


    public List<AtlasRelatedTermHeader> getTerms()
    {
        return terms;
    }


    public void setTerms(List<AtlasRelatedTermHeader> terms)
    {
        this.terms = terms;
    }


    public List<AtlasRelatedCategoryHeader> getCategories()
    {
        return categories;
    }


    public void setCategories(List<AtlasRelatedCategoryHeader> categories)
    {
        this.categories = categories;
    }


    @Override
    public String toString()
    {
        return "AtlasGlossaryElement{" +
                       "terms=" + terms +
                       ", categories=" + categories +
                       ", language='" + getLanguage() + '\'' +
                       ", usage='" + getUsage() + '\'' +
                       ", guid='" + getGuid() + '\'' +
                       ", qualifiedName='" + getQualifiedName() + '\'' +
                       ", name='" + getName() + '\'' +
                       ", shortDescription='" + getShortDescription() + '\'' +
                       ", longDescription='" + getLongDescription() + '\'' +
                       ", additionalAttributes=" + getAdditionalAttributes() +
                       '}';
    }
}
