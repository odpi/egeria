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
    private RelatedMetadataElementSummary       parentGlossary         = null;
    private List<RelatedMetadataElementSummary> categoryMembership     = null;
    private List<RelatedMetadataElementSummary> relatedTerms           = null;
    private List<RelatedMetadataElementSummary> relatedDefinitions     = null;
    private List<RelatedMetadataElementSummary> semanticAssignments    = null;


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
            parentGlossary         = template.getParentGlossary();
            categoryMembership     = template.getCategoryMembership();
            relatedTerms           = template.getRelatedTerms();
            relatedDefinitions     = template.getRelatedDefinitions();
            semanticAssignments    = template.getSemanticAssignments();
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
    public List<RelatedMetadataElementSummary> getRelatedTerms()
    {
        return relatedTerms;
    }


    /**
     * Set up the related terms.
     *
     * @param relatedToTerms list of terms
     */
    public void setRelatedToTerms(List<RelatedMetadataElementSummary> relatedToTerms)
    {
        this.relatedTerms = relatedToTerms;
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
                ", parentGlossary=" + parentGlossary +
                ", categoryMembership=" + categoryMembership +
                ", relatedTerms=" + relatedTerms +
                ", relatedDefinitions=" + relatedDefinitions +
                ", semanticAssignments=" + semanticAssignments +
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
                Objects.equals(parentGlossary, that.parentGlossary) &&
                Objects.equals(categoryMembership, that.categoryMembership) &&
                Objects.equals(relatedTerms, that.relatedTerms) &&
                Objects.equals(relatedDefinitions, that.relatedDefinitions) &&
                Objects.equals(semanticAssignments, that.semanticAssignments);
    }


    /**
     * Return hash code for this object
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), correlationHeaders, glossaryTermProperties,
                            parentGlossary, categoryMembership, relatedTerms, relatedDefinitions, semanticAssignments);
    }
}
