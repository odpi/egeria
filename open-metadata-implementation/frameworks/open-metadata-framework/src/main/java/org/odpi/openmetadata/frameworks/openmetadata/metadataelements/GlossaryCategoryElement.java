/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworks.openmetadata.metadataelements;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.frameworks.openmetadata.properties.glossaries.GlossaryCategoryProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.MetadataCorrelationHeader;

import java.util.List;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * GlossaryCategoryElement contains the properties and header for a glossary category
 * retrieved from the metadata repository.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class GlossaryCategoryElement extends AttributedMetadataElement implements CorrelatedMetadataElement
{
    private List<MetadataCorrelationHeader>     correlationHeaders         = null;
    private GlossaryCategoryProperties          glossaryCategoryProperties = null;
    private RelatedMetadataElementSummary       parentGlossary             = null;
    private RelatedMetadataElementSummary       parentCategory             = null;
    private List<RelatedMetadataElementSummary> childCategories            = null;
    private List<RelatedMetadataElementSummary> terms                      = null;
    private List<RelatedMetadataElementSummary> otherRelatedElements       = null;
    private String                              mermaidGraph               = null;

    /**
     * Default constructor
     */
    public GlossaryCategoryElement()
    {
        super();
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public GlossaryCategoryElement(GlossaryCategoryElement template)
    {
        super(template);

        if (template != null)
        {
            correlationHeaders = template.getCorrelationHeaders();
            glossaryCategoryProperties = template.getGlossaryCategoryProperties();
            parentGlossary = template.getParentGlossary();
            parentCategory = template.getParentCategory();
            childCategories = template.getChildCategories();
            terms = template.getTerms();
            otherRelatedElements = template.getOtherRelatedElements();
            mermaidGraph = template.getMermaidGraph();
        }
    }


    /**
     * Return the details of the external identifier and other correlation properties about the metadata source.
     * There is one entry in the list for each element in the third party technology that maps to the single open source
     * element.
     *
     * @return list of correlation properties objects
     */
    @Override
    public List<MetadataCorrelationHeader> getCorrelationHeaders()
    {
        return correlationHeaders;
    }


    /**
     * Set up the details of the external identifier and other correlation properties about the metadata source.
     * There is one entry in the list for each element in the third party technology that maps to the single open source
     * element.
     *
     * @param correlationHeaders list of correlation properties objects
     */
    @Override
    public void setCorrelationHeaders(List<MetadataCorrelationHeader> correlationHeaders)
    {
        this.correlationHeaders = correlationHeaders;
    }


    /**
     * Return the properties for the glossary schema.
     *
     * @return schema properties
     */
    public GlossaryCategoryProperties getGlossaryCategoryProperties()
    {
        return glossaryCategoryProperties;
    }


    /**
     * Set up the glossary schema properties.
     *
     * @param glossaryCategoryProperties schema properties
     */
    public void setGlossaryCategoryProperties(GlossaryCategoryProperties glossaryCategoryProperties)
    {
        this.glossaryCategoryProperties = glossaryCategoryProperties;
    }


    /**
     * Return the parent glossary for this element.
     *
     * @return glossary details
     */
    public RelatedMetadataElementSummary getParentGlossary()
    {
        return parentGlossary;
    }


    /**
     * Set up parent glossary for this element.
     *
     * @param parentGlossary glossary details
     */
    public void setParentGlossary(RelatedMetadataElementSummary parentGlossary)
    {
        this.parentGlossary = parentGlossary;
    }


    /**
     * Return the optional parent category.
     *
     * @return category
     */
    public RelatedMetadataElementSummary getParentCategory()
    {
        return parentCategory;
    }


    /**
     * Set up the optional parent category.
     *
     * @param parentCategory category
     */
    public void setParentCategory(RelatedMetadataElementSummary parentCategory)
    {
        this.parentCategory = parentCategory;
    }


    /**
     * Return the optional child categories.
     *
     * @return categories
     */
    public List<RelatedMetadataElementSummary> getChildCategories()
    {
        return childCategories;
    }


    /**
     * Set up the optional child categories.
     *
     * @param childCategories categories
     */
    public void setChildCategories(List<RelatedMetadataElementSummary> childCategories)
    {
        this.childCategories = childCategories;
    }


    /**
     * Set up the terms categorised by this category.
     *
     * @return terms
     */
    public List<RelatedMetadataElementSummary> getTerms()
    {
        return terms;
    }


    /**
     * Return the terms categorised by this category.
     *
     * @param terms terms
     */
    public void setTerms(List<RelatedMetadataElementSummary> terms)
    {
        this.terms = terms;
    }


    /**
     * Return details of other related elements retrieved from the repository.
     *
     * @return list
     */
    public List<RelatedMetadataElementSummary> getOtherRelatedElements()
    {
        return otherRelatedElements;
    }


    /**
     * Set up details of other related elements retrieved from the repository.
     *
     * @param otherRelatedElements list
     */
    public void setOtherRelatedElements(List<RelatedMetadataElementSummary> otherRelatedElements)
    {
        this.otherRelatedElements = otherRelatedElements;
    }


    /**
     * Return the mermaid representation of this data structure.
     *
     * @return string markdown
     */
    public String getMermaidGraph()
    {
        return mermaidGraph;
    }


    /**
     * Set up the mermaid representation of this data structure.
     *
     * @param mermaidGraph markdown string
     */
    public void setMermaidGraph(String mermaidGraph)
    {
        this.mermaidGraph = mermaidGraph;
    }


    /**
     * JSON-style toString
     *
     * @return return string containing the property names and values
     */
    @Override
    public String toString()
    {
        return "GlossaryCategoryElement{" +
                "correlationHeaders=" + correlationHeaders +
                ", glossaryCategoryProperties=" + glossaryCategoryProperties +
                ", parentGlossary=" + parentGlossary +
                ", parentCategory=" + parentCategory +
                ", childCategories=" + childCategories +
                ", terms=" + terms +
                ", otherRelatedElements=" + otherRelatedElements +
                ", mermaidGraph='" + mermaidGraph + '\'' +
                "} " + super.toString();
    }


    /**
     * Return comparison result based on the content of the properties.
     *
     * @param objectToCompare test object
     * @return result of comparison
     */
    @Override
    public boolean equals(Object objectToCompare)
    {
        if (this == objectToCompare) return true;
        if (objectToCompare == null || getClass() != objectToCompare.getClass()) return false;
        if (!super.equals(objectToCompare)) return false;
        GlossaryCategoryElement that = (GlossaryCategoryElement) objectToCompare;
        return Objects.equals(correlationHeaders, that.correlationHeaders) &&
                Objects.equals(glossaryCategoryProperties, that.glossaryCategoryProperties) &&
                Objects.equals(parentGlossary, that.parentGlossary) &&
                Objects.equals(parentCategory, that.parentCategory) &&
                Objects.equals(childCategories, that.childCategories) &&
                Objects.equals(terms, that.terms) &&
                Objects.equals(otherRelatedElements, that.otherRelatedElements) &&
                Objects.equals(mermaidGraph, that.mermaidGraph);
    }


    /**
     * Return hash code for this object
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), correlationHeaders, glossaryCategoryProperties,
                            parentGlossary, parentCategory, childCategories, terms,
                            otherRelatedElements, mermaidGraph);
    }
}
