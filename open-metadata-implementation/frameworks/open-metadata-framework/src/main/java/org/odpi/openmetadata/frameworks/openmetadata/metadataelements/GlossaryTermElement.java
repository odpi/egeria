/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworks.openmetadata.metadataelements;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.frameworks.openmetadata.properties.MetadataCorrelationHeader;
import org.odpi.openmetadata.frameworks.openmetadata.properties.glossaries.GlossaryTermProperties;

import java.util.List;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * GlossaryTermElement contains the properties and header for a glossary term entity
 * retrieved from the metadata repository.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class GlossaryTermElement extends AttributedMetadataElement implements CorrelatedMetadataElement
{
    private List<MetadataCorrelationHeader>     correlationHeaders     = null;
    private GlossaryTermProperties              glossaryTermProperties = null;
    private RelatedBy                           relatedBy              = null;
    private RelatedMetadataElementSummary       parentGlossary         = null;
    private List<RelatedMetadataElementSummary> categoryMembership     = null;
    private List<RelatedMetadataElementSummary> relatedToTerms         = null;
    private List<RelatedMetadataElementSummary> relatedFromTerms       = null;
    private List<RelatedMetadataElementSummary> relatedDefinitions     = null;
    private List<RelatedMetadataElementSummary> semanticAssignments    = null;
    private List<RelatedMetadataElementSummary> otherRelatedElements   = null;
    private String                              mermaidGraph           = null;


    /**
     * Default constructor
     */
    public GlossaryTermElement()
    {
        super();
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public GlossaryTermElement(GlossaryTermElement template)
    {
        super (template);

        if (template != null)
        {
            correlationHeaders     = template.getCorrelationHeaders();
            glossaryTermProperties = template.getGlossaryTermProperties();
            relatedBy              = template.getRelatedElement();
            parentGlossary         = template.getParentGlossary();
            categoryMembership     = template.getCategoryMembership();
            relatedToTerms         = template.getRelatedToTerms();
            relatedFromTerms       = template.getRelatedFromTerms();
            relatedDefinitions     = template.getRelatedDefinitions();
            semanticAssignments    = template.getSemanticAssignments();
            otherRelatedElements   = template.getOtherRelatedElements();
            mermaidGraph           = template.getMermaidGraph();
        }
    }


    /**
     * Return the details of the external identifier and other correlation properties about the metadata source.
     *
     * @return properties object
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
     * Return the glossary term properties.
     *
     * @return properties bean
     */
    public GlossaryTermProperties getGlossaryTermProperties()
    {
        return glossaryTermProperties;
    }


    /**
     * Set up the glossary term properties.
     *
     * @param glossaryTermProperties properties bean
     */
    public void setGlossaryTermProperties(GlossaryTermProperties glossaryTermProperties)
    {
        this.glossaryTermProperties = glossaryTermProperties;
    }


    /**
     * Return details of the relationship used to retrieve the element.
     *
     * @return relationship properties and starting element
     */
    public RelatedBy getRelatedElement()
    {
        return relatedBy;
    }


    /**
     * Set up details of the relationship used to retrieve the element.
     *
     * @param relatedBy relationship properties and starting element
     */
    public void setRelatedElement(RelatedBy relatedBy)
    {
        this.relatedBy = relatedBy;
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
     * Return the list of categories that the term is linked to.
     *
     * @return list of categories
     */
    public List<RelatedMetadataElementSummary> getCategoryMembership()
    {
        return categoryMembership;
    }


    /**
     * Set up the list of categories that the term is linked to.
     *
     * @param categoryMembership list of categories
     */
    public void setCategoryMembership(List<RelatedMetadataElementSummary> categoryMembership)
    {
        this.categoryMembership = categoryMembership;
    }


    /**
     * Return the related terms.
     *
     * @return list of terms
     */
    public List<RelatedMetadataElementSummary> getRelatedToTerms()
    {
        return relatedToTerms;
    }


    /**
     * Set up  the related terms.
     *
     * @param relatedToTerms list of terms
     */
    public void setRelatedToTerms(List<RelatedMetadataElementSummary> relatedToTerms)
    {
        this.relatedToTerms = relatedToTerms;
    }


    /**
     * Return the related terms.
     *
     * @return list of terms
     */
    public List<RelatedMetadataElementSummary> getRelatedFromTerms()
    {
        return relatedFromTerms;
    }


    /**
     * Set up  the related terms.
     *
     * @param relatedFromTerms list of terms
     */
    public void setRelatedFromTerms(List<RelatedMetadataElementSummary> relatedFromTerms)
    {
        this.relatedFromTerms = relatedFromTerms;
    }


    /**
     * Return the data definitions that are linked to this glossary term via the semantic definition relationship.
     *
     * @return list of data definitions
     */
    public List<RelatedMetadataElementSummary> getRelatedDefinitions()
    {
        return relatedDefinitions;
    }


    /**
     * Set up the data definitions that are linked to this glossary term via the semantic definition relationship.
     *
     * @param relatedDefinitions list of data definitions
     */
    public void setRelatedDefinitions(List<RelatedMetadataElementSummary> relatedDefinitions)
    {
        this.relatedDefinitions = relatedDefinitions;
    }


    /**
     * Return the list of elements assigned to the glossary term via the semantic assignment relationship,
     *
     * @return list of related elements
     */
    public List<RelatedMetadataElementSummary> getSemanticAssignments()
    {
        return semanticAssignments;
    }


    /**
     * Set up the list of elements assigned to the glossary term via the semantic assignment relationship,
     *
     * @param semanticAssignments list of related elements
     */
    public void setSemanticAssignments(List<RelatedMetadataElementSummary> semanticAssignments)
    {
        this.semanticAssignments = semanticAssignments;
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
        return "GlossaryTermElement{" +
                "correlationHeaders=" + correlationHeaders +
                ", glossaryTermProperties=" + glossaryTermProperties +
                ", relatedBy=" + relatedBy +
                ", parentGlossary=" + parentGlossary +
                ", categoryMembership=" + categoryMembership +
                ", relatedToTerms=" + relatedToTerms +
                ", relatedFromTerms=" + relatedToTerms +
                ", relatedDefinitions=" + relatedDefinitions +
                ", semanticAssignments=" + semanticAssignments +
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
        GlossaryTermElement that = (GlossaryTermElement) objectToCompare;
        return Objects.equals(correlationHeaders, that.correlationHeaders) &&
                Objects.equals(glossaryTermProperties, that.glossaryTermProperties) &&
                Objects.equals(relatedBy, that.relatedBy) &&
                Objects.equals(parentGlossary, that.parentGlossary) &&
                Objects.equals(categoryMembership, that.categoryMembership) &&
                Objects.equals(relatedToTerms, that.relatedToTerms) &&
                Objects.equals(relatedFromTerms, that.relatedFromTerms) &&
                Objects.equals(relatedDefinitions, that.relatedDefinitions) &&
                Objects.equals(semanticAssignments, that.semanticAssignments) &&
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
        return Objects.hash(super.hashCode(), mermaidGraph, correlationHeaders, glossaryTermProperties, relatedBy,
                            parentGlossary, categoryMembership, relatedToTerms, relatedFromTerms, relatedDefinitions,
                            semanticAssignments, otherRelatedElements);
    }
}
