/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworks.openmetadata.metadataelements;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * Description of an open metadata element (entity instance) retrieved from the open metadata repositories
 * that is expected to have external references and other elements attached.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class AttributedMetadataElement implements MetadataElement
{
    private ElementHeader                       elementHeader        = null;
    private List<RelatedMetadataElementSummary> externalReferences   = null;
    private List<RelatedMetadataElementSummary> alsoKnownAs          = null;
    private List<RelatedMetadataElementSummary> memberOfCollections  = null;
    private List<RelatedMetadataElementSummary> semanticAssignments  = null;
    private List<RelatedMetadataElementSummary> attachedLikes        = null;
    private List<RelatedMetadataElementSummary> attachedTags         = null;
    private List<RelatedMetadataElementSummary> attachedKeywords     = null;
    private List<RelatedMetadataElementSummary> attachedComments     = null;
    private List<RelatedMetadataElementSummary> attachedReviews      = null;
    private List<RelatedMetadataElementSummary> otherRelatedElements = null;
    private RelatedBy                           relatedBy            = null;
    private String                              mermaidGraph         = null;

    /**
     * Default constructor used by subclasses
     */
    public AttributedMetadataElement()
    {
    }


    /**
     * Copy/clone constructor
     *
     * @param template template to copy
     */
    public AttributedMetadataElement(AttributedMetadataElement template)
    {
        if (template != null)
        {
            elementHeader        = template.getElementHeader();
            externalReferences   = template.getExternalReferences();
            alsoKnownAs          = template.getAlsoKnownAs();
            memberOfCollections  = template.getMemberOfCollections();
            semanticAssignments  = template.getSemanticAssignments();
            attachedLikes        = template.getAttachedLikes();
            attachedTags         = template.getAttachedTags();
            attachedKeywords     = template.getAttachedKeywords();
            attachedComments     = template.getAttachedComments();
            attachedReviews      = template.getAttachedReviews();
            otherRelatedElements = template.getOtherRelatedElements();
            relatedBy            = template.getRelatedBy();
            mermaidGraph         = template.getMermaidGraph();
        }
    }


    /**
     * Return the element header associated with the properties.
     *
     * @return element header object
     */
    @Override
    public ElementHeader getElementHeader()
    {
        return elementHeader;
    }

    /**
     * Set up the element header associated with the properties.
     *
     * @param elementHeader element header object
     */
    @Override
    public void setElementHeader(ElementHeader elementHeader)
    {
        this.elementHeader = elementHeader;
    }


    /**
     * Set up the list of external references for this element
     *
     * @param externalReferences list
     */
    public void setExternalReferences(List<RelatedMetadataElementSummary> externalReferences)
    {
        this.externalReferences = externalReferences;
    }


    /**
     * Return the list of external references for this element.
     *
     * @return list
     */
    public List<RelatedMetadataElementSummary> getExternalReferences()
    {
        return externalReferences;
    }



    /**
     * Return the list of collections that is definition is a member of.
     *
     * @return list
     */
    public List<RelatedMetadataElementSummary> getMemberOfCollections()
    {
        return memberOfCollections;
    }


    /**
     * Set up the list of collections that is definition is a member of.
     *
     * @param memberOfCollections list
     */
    public void setMemberOfCollections(List<RelatedMetadataElementSummary> memberOfCollections)
    {
        this.memberOfCollections = memberOfCollections;
    }


    /**
     * Return attached external identifiers.
     *
     * @return list
     */
    public List<RelatedMetadataElementSummary> getAlsoKnownAs()
    {
        return alsoKnownAs;
    }


    /**
     * Set up attached external identifiers.
     *
     * @param alsoKnownAs list
     */
    public void setAlsoKnownAs(List<RelatedMetadataElementSummary> alsoKnownAs)
    {
        this.alsoKnownAs = alsoKnownAs;
    }


    /**
     * Return the glossary terms linked by semantic assignment.
     *
     * @return list
     */
    public List<RelatedMetadataElementSummary> getSemanticAssignments()
    {
        return semanticAssignments;
    }


    /**
     * Set up the glossary terms linked by semantic assignment.
     *
     * @param semanticAssignments list
     */
    public void setSemanticAssignments(List<RelatedMetadataElementSummary> semanticAssignments)
    {
        this.semanticAssignments = semanticAssignments;
    }


    /**
     * Return the list of likes for this element.
     *
     * @return list
     */
    public List<RelatedMetadataElementSummary> getAttachedLikes()
    {
        return attachedLikes;
    }


    /**
     * Set up the list of likes for this element.
     *
     * @param attachedLikes list
     */
    public void setAttachedLikes(List<RelatedMetadataElementSummary> attachedLikes)
    {
        this.attachedLikes = attachedLikes;
    }


    /**
     * Return the attached informal tags.
     *
     * @return list
     */
    public List<RelatedMetadataElementSummary> getAttachedTags()
    {
        return attachedTags;
    }


    /**
     * Set up the attached informal tags.
     *
     * @param attachedTags list
     */
    public void setAttachedTags(List<RelatedMetadataElementSummary> attachedTags)
    {
        this.attachedTags = attachedTags;
    }


    /**
     * Return the attached search keywords.
     *
     * @return list
     */
    public List<RelatedMetadataElementSummary> getAttachedKeywords()
    {
        return attachedKeywords;
    }


    /**
     * Set up the attached search keywords.
     *
     * @param attachedKeywords list
     */
    public void setAttachedKeywords(List<RelatedMetadataElementSummary> attachedKeywords)
    {
        this.attachedKeywords = attachedKeywords;
    }


    /**
     * Return the attached comments.
     *
     * @return list
     */
    public List<RelatedMetadataElementSummary> getAttachedComments()
    {
        return attachedComments;
    }


    /**
     * Set up the attached comments.
     *
     * @param attachedComments list
     */
    public void setAttachedComments(List<RelatedMetadataElementSummary> attachedComments)
    {
        this.attachedComments = attachedComments;
    }


    /**
     * Return the attached reviews (ratings).
     *
     * @return list
     */
    public List<RelatedMetadataElementSummary> getAttachedReviews()
    {
        return attachedReviews;
    }


    /**
     * Set up the attached reviews (ratings).
     *
     * @param attachedReviews list
     */
    public void setAttachedReviews(List<RelatedMetadataElementSummary> attachedReviews)
    {
        this.attachedReviews = attachedReviews;
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
     * Return details of the relationship used to retrieve this element.
     * Will be null if the element was retrieved directly rather than via a relationship.
     *
     * @return list of element stubs
     */
    public RelatedBy getRelatedBy()
    {
        return relatedBy;
    }


    /**
     * Set up details of the relationship used to retrieve this element.
     * Will be null if the element was retrieved directly rather than via a relationship.
     *
     * @param relatedBy relationship details
     */
    public void setRelatedBy(RelatedBy relatedBy)
    {
        this.relatedBy = relatedBy;
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
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "AttributedMetadataElement{" +
                "elementHeader=" + elementHeader +
                ", externalReferences=" + externalReferences +
                ", alsoKnownAs=" + alsoKnownAs +
                ", memberOfCollections=" + memberOfCollections +
                ", semanticAssignments=" + semanticAssignments +
                ", attachedLikes=" + attachedLikes +
                ", attachedTags=" + attachedTags +
                ", attachedKeywords=" + attachedKeywords +
                ", attachedComments=" + attachedComments +
                ", attachedReviews=" + attachedReviews +
                ", otherRelatedElements=" + otherRelatedElements +
                ", relatedBy=" + relatedBy +
                ", mermaidGraph='" + mermaidGraph + '\'' +
                '}';
    }


    /**
     * Compare the values of the supplied object with those stored in the current object.
     *
     * @param objectToCompare supplied object
     * @return boolean result of comparison
     */
    @Override
    public boolean equals(Object objectToCompare)
    {
        if (this == objectToCompare) return true;
        if (objectToCompare == null || getClass() != objectToCompare.getClass()) return false;
        AttributedMetadataElement that = (AttributedMetadataElement) objectToCompare;
        return Objects.equals(elementHeader, that.elementHeader) &&
                Objects.equals(externalReferences, that.externalReferences) &&
                Objects.equals(alsoKnownAs, that.alsoKnownAs) &&
                Objects.equals(memberOfCollections, that.memberOfCollections) &&
                Objects.equals(semanticAssignments, that.semanticAssignments) &&
                Objects.equals(attachedLikes, that.attachedLikes) &&
                Objects.equals(attachedTags, that.attachedTags) &&
                Objects.equals(attachedKeywords, that.attachedKeywords) &&
                Objects.equals(attachedComments, that.attachedComments) &&
                Objects.equals(attachedReviews, that.attachedReviews) &&
                Objects.equals(otherRelatedElements, that.otherRelatedElements) &&
                Objects.equals(relatedBy, that.relatedBy) &&
                Objects.equals(mermaidGraph, that.mermaidGraph);
    }


    /**
     * Create a hash code for this element type.
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(elementHeader, externalReferences, alsoKnownAs, memberOfCollections, semanticAssignments,
                            attachedLikes, attachedTags, attachedKeywords, attachedComments, attachedReviews,
                            otherRelatedElements, relatedBy, mermaidGraph);
    }
}
