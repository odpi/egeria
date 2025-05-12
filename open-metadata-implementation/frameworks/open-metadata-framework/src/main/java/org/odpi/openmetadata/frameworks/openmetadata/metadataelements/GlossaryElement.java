/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworks.openmetadata.metadataelements;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.frameworks.openmetadata.properties.glossaries.GlossaryProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.MetadataCorrelationHeader;

import java.util.List;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * GlossaryElement contains the properties and header for a glossary entity retrieved from the metadata repository.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class GlossaryElement extends AttributedMetadataElement implements CorrelatedMetadataElement
{
    private List<MetadataCorrelationHeader>     correlationHeaders   = null;
    private GlossaryProperties                  glossaryProperties   = null;
    private List<ChildCategoryElement>          categories           = null;
    private List<RelatedMetadataElementSummary> otherRelatedElements = null;
    private String                              mermaidGraph         = null;


    /**
     * Default constructor
     */
    public GlossaryElement()
    {
        super();
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public GlossaryElement(GlossaryElement template)
    {
        super(template);

        if (template != null)
        {
            correlationHeaders   = template.getCorrelationHeaders();
            glossaryProperties   = template.getGlossaryProperties();
            categories           = template.getCategories();
            otherRelatedElements = template.getOtherRelatedElements();
            mermaidGraph         = template.getMermaidGraph();
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
     * Return details of the glossary
     *
     * @return glossary properties
     */
    public GlossaryProperties getGlossaryProperties()
    {
        return glossaryProperties;
    }


    /**
     * Set up glossary properties
     *
     * @param glossaryProperties glossary properties
     */
    public void setGlossaryProperties(GlossaryProperties glossaryProperties)
    {
        this.glossaryProperties = glossaryProperties;
    }


    /**
     * Return the categories for the glossary, organized hierarchically.
     *
     * @return list of category hierarchies
     */
    public List<ChildCategoryElement> getCategories()
    {
        return categories;
    }


    /**
     * Set up the categories for the glossary, organized hierarchically.
     *
     * @param categories  list of category hierarchies
     */
    public void setCategories(List<ChildCategoryElement> categories)
    {
        this.categories = categories;
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
        return "GlossaryElement{" +
                "correlationHeaders=" + correlationHeaders +
                ", glossaryProperties=" + glossaryProperties +
                ", categories=" + categories +
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
        if (this == objectToCompare)
        {
            return true;
        }
        if (objectToCompare == null || getClass() != objectToCompare.getClass())
        {
            return false;
        }
        if (!super.equals(objectToCompare))
        {
            return false;
        }
        GlossaryElement that = (GlossaryElement) objectToCompare;
        return Objects.equals(mermaidGraph, that.mermaidGraph) &&
                Objects.equals(correlationHeaders, that.correlationHeaders) &&
                Objects.equals(glossaryProperties, that.glossaryProperties) &&
                Objects.equals(categories, that.categories) &&
                Objects.equals(otherRelatedElements, that.otherRelatedElements);
    }


    /**
     * Return hash code for this object
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), mermaidGraph, correlationHeaders, glossaryProperties,
                            categories, otherRelatedElements);
    }
}
