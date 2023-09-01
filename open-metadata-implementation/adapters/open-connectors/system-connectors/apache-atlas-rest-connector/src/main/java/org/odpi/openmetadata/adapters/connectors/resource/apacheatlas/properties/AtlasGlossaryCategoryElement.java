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
 * AtlasGlossaryElement describes an Apache Atlas glossary category and its related terms/categories retrieved from Apache Atlas.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class AtlasGlossaryCategoryElement extends AtlasGlossaryMemberBaseProperties
{
    private AtlasRelatedCategoryHeader       parentCategory     = null;
    private List<AtlasRelatedCategoryHeader> childrenCategories = null;
    private List<AtlasRelatedTermHeader>     terms              = null;


    public AtlasGlossaryCategoryElement()
    {
    }


    public AtlasGlossaryCategoryElement(AtlasGlossaryCategoryElement template)
    {
        super(template);

        if (template != null)
        {
            terms = template.getTerms();
            parentCategory = template.getParentCategory();
            childrenCategories = template.getChildrenCategories();
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


    public AtlasRelatedCategoryHeader getParentCategory()
    {
        return parentCategory;
    }


    public void setParentCategory(AtlasRelatedCategoryHeader parentCategory)
    {
        this.parentCategory = parentCategory;
    }


    public List<AtlasRelatedCategoryHeader> getChildrenCategories()
    {
        return childrenCategories;
    }


    public void setChildrenCategories(List<AtlasRelatedCategoryHeader> childrenCategories)
    {
        this.childrenCategories = childrenCategories;
    }


    @Override
    public String toString()
    {
        return "AtlasGlossaryCategoryElement{" +
                       "parentCategory=" + parentCategory +
                       ", childrenCategories=" + childrenCategories +
                       ", terms=" + terms +
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
