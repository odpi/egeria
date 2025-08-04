/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworks.openmetadata.mermaid;

import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.ElementClassification;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.OpenMetadataRootElement;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.RelatedMetadataElementSummary;
import org.odpi.openmetadata.frameworks.openmetadata.properties.ReferenceableProperties;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;


/**
 * Creates a mermaid graph rendering of the Open Metadata Framework's attributed
 * metadata element.
 */
public class OpenMetadataRootMermaidGraphBuilder extends MermaidGraphBuilderBase
{
    /**
     * Construct a mermaid markdown graph.
     *
     * @param openMetadataRootElement content
     */
    public OpenMetadataRootMermaidGraphBuilder(OpenMetadataRootElement openMetadataRootElement)
    {
        /*
         * Add the graph title
         */
        String currentNodeName    = openMetadataRootElement.getElementHeader().getGUID();
        String currentDisplayName = currentNodeName;

        if (openMetadataRootElement.getProperties() instanceof ReferenceableProperties referenceableProperties)
        {
            currentDisplayName = this.getNodeDisplayName(openMetadataRootElement.getElementHeader(),
                                                         referenceableProperties);
        }

        mermaidGraph.append("---\n");
        mermaidGraph.append("title: ");
        mermaidGraph.append(openMetadataRootElement.getElementHeader().getType().getTypeName());
        mermaidGraph.append(" - ");
        if (currentDisplayName != null)
        {
            mermaidGraph.append(currentDisplayName);
            mermaidGraph.append(" [");
        }
        else
        {
            mermaidGraph.append(currentNodeName);
            mermaidGraph.append(" [");
        }

        mermaidGraph.append(openMetadataRootElement.getElementHeader().getGUID());
        mermaidGraph.append("]\n---\nflowchart TD\n%%{init: {\"flowchart\": {\"htmlLabels\": false}} }%%\n\n");

        /*
         * Add the graph content
         */
        this.addElementToMermaidGraph(openMetadataRootElement);
    }


    /**
     * Add the content of an open metadata element to the mermaid graph.
     *
     * @param openMetadataRootElement content
     */
    protected void addElementToMermaidGraph(OpenMetadataRootElement openMetadataRootElement)
    {
        String currentDisplayName = openMetadataRootElement.getElementHeader().getGUID();

        if (openMetadataRootElement.getProperties() instanceof ReferenceableProperties referenceableProperties)
        {
            currentDisplayName = this.getNodeDisplayName(openMetadataRootElement.getElementHeader(),
                                                         referenceableProperties);
        }

        /*
         * Add details of the primary node.
         */
        super.appendNewMermaidNode(openMetadataRootElement.getElementHeader().getGUID(),
                                   currentDisplayName,
                                   openMetadataRootElement.getElementHeader().getType().getTypeName(),
                                   getVisualStyleForClassifications(openMetadataRootElement.getElementHeader(),
                                                                    VisualStyle.ANCHOR_ELEMENT));

        /*
         * Add details of all of the relationships with special meaning
         */
        super.addRelatedElementSummaries(openMetadataRootElement.getSearchKeywords(),
                                         VisualStyle.TAG,
                                         openMetadataRootElement.getElementHeader().getGUID());
        super.addRelatedElementSummaries(openMetadataRootElement.getKeywordElements(),
                                         VisualStyle.LINKED_ELEMENT,
                                         openMetadataRootElement.getElementHeader().getGUID());

        super.addRelatedElementSummaries(openMetadataRootElement.getExternalReferences(),
                                         VisualStyle.EXTERNAL_REFERENCE,
                                         openMetadataRootElement.getElementHeader().getGUID());
        super.addRelatedElementSummaries(openMetadataRootElement.getReferencingElements(),
                                         VisualStyle.EXTERNAL_REFERENCE,
                                         openMetadataRootElement.getElementHeader().getGUID());

        super.addRelatedElementSummaries(openMetadataRootElement.getAlsoKnownAs(),
                                         VisualStyle.LINKED_ELEMENT,
                                         openMetadataRootElement.getElementHeader().getGUID());
        super.addRelatedElementSummaries(openMetadataRootElement.getEquivalentElements(),
                                         VisualStyle.LINKED_ELEMENT,
                                         openMetadataRootElement.getElementHeader().getGUID());

        super.addRelatedElementSummaries(openMetadataRootElement.getRecognizedExternalIdentifiers(),
                                         VisualStyle.LINKED_ELEMENT,
                                         openMetadataRootElement.getElementHeader().getGUID());
        super.addRelatedElementSummaries(openMetadataRootElement.getIdentifierScopedTo(),
                                         VisualStyle.LINKED_ELEMENT,
                                         openMetadataRootElement.getElementHeader().getGUID());

        super.addRelatedElementSummaries(openMetadataRootElement.getResourceList(),
                                         VisualStyle.LINKED_ELEMENT,
                                         openMetadataRootElement.getElementHeader().getGUID());
        super.addRelatedElementSummaries(openMetadataRootElement.getResourceListUsers(),
                                         VisualStyle.LINKED_ELEMENT,
                                         openMetadataRootElement.getElementHeader().getGUID());

        super.addRelatedElementSummaries(openMetadataRootElement.getMemberOfCollections(),
                                         VisualStyle.COLLECTION,
                                         openMetadataRootElement.getElementHeader().getGUID());
        super.addRelatedElementSummaries(openMetadataRootElement.getCollectionMembers(),
                                         VisualStyle.LINKED_ELEMENT,
                                         openMetadataRootElement.getElementHeader().getGUID());

        super.addRelatedElementSummaries(openMetadataRootElement.getPropertyFacets(),
                                         VisualStyle.LINKED_ELEMENT,
                                         openMetadataRootElement.getElementHeader().getGUID());
        super.addRelatedElementSummaries(openMetadataRootElement.getFacetedElements(),
                                         VisualStyle.LINKED_ELEMENT,
                                         openMetadataRootElement.getElementHeader().getGUID());

        super.addRelatedElementSummaries(openMetadataRootElement.getContactDetails(),
                                         VisualStyle.LINKED_ELEMENT,
                                         openMetadataRootElement.getElementHeader().getGUID());
        super.addRelatedElementSummaries(openMetadataRootElement.getContacts(),
                                         VisualStyle.LINKED_ELEMENT,
                                         openMetadataRootElement.getElementHeader().getGUID());

        super.addRelatedElementSummaries(openMetadataRootElement.getRelevantToScope(),
                                         VisualStyle.LINKED_ELEMENT,
                                         openMetadataRootElement.getElementHeader().getGUID());
        super.addRelatedElementSummaries(openMetadataRootElement.getScopedElements(),
                                         VisualStyle.LINKED_ELEMENT,
                                         openMetadataRootElement.getElementHeader().getGUID());

        super.addRelatedElementSummaries(openMetadataRootElement.getAssignmentScope(),
                                         VisualStyle.LINKED_ELEMENT,
                                         openMetadataRootElement.getElementHeader().getGUID());
        super.addRelatedElementSummaries(openMetadataRootElement.getAssignedActors(),
                                         VisualStyle.LINKED_ELEMENT,
                                         openMetadataRootElement.getElementHeader().getGUID());

        super.addRelatedElementSummaries(openMetadataRootElement.getCommissionedElements(),
                                         VisualStyle.LINKED_ELEMENT,
                                         openMetadataRootElement.getElementHeader().getGUID());
        super.addRelatedElementSummaries(openMetadataRootElement.getCommissionedBy(),
                                         VisualStyle.LINKED_ELEMENT,
                                         openMetadataRootElement.getElementHeader().getGUID());

        super.addRelatedElementSummaries(openMetadataRootElement.getLikes(),
                                         VisualStyle.FEEDBACK,
                                         openMetadataRootElement.getElementHeader().getGUID());
        super.addRelatedElementSummary(openMetadataRootElement.getLikedElement(),
                                         VisualStyle.LINKED_ELEMENT,
                                         openMetadataRootElement.getElementHeader().getGUID());

        super.addRelatedElementSummaries(openMetadataRootElement.getInformalTags(),
                                         VisualStyle.TAG,
                                         openMetadataRootElement.getElementHeader().getGUID());
        super.addRelatedElementSummaries(openMetadataRootElement.getTaggedElements(),
                                         VisualStyle.LINKED_ELEMENT,
                                         openMetadataRootElement.getElementHeader().getGUID());

        super.addRelatedElementSummaries(openMetadataRootElement.getReviews(),
                                         VisualStyle.FEEDBACK,
                                         openMetadataRootElement.getElementHeader().getGUID());
        super.addRelatedElementSummary(openMetadataRootElement.getReviewedElement(),
                                         VisualStyle.LINKED_ELEMENT,
                                         openMetadataRootElement.getElementHeader().getGUID());

        super.addRelatedElementSummaries(openMetadataRootElement.getComments(),
                                         VisualStyle.FEEDBACK,
                                         openMetadataRootElement.getElementHeader().getGUID());
        super.addRelatedElementSummary(openMetadataRootElement.getCommentedOnElement(),
                                         VisualStyle.LINKED_ELEMENT,
                                         openMetadataRootElement.getElementHeader().getGUID());

        super.addRelatedElementSummaries(openMetadataRootElement.getAssociatedGlossaries(),
                                         VisualStyle.LINKED_ELEMENT,
                                         openMetadataRootElement.getElementHeader().getGUID());
        super.addRelatedElementSummaries(openMetadataRootElement.getCategories(),
                                         VisualStyle.LINKED_ELEMENT,
                                         openMetadataRootElement.getElementHeader().getGUID());

        super.addRelatedElementSummary(openMetadataRootElement.getParentGlossary(),
                                         VisualStyle.LINKED_ELEMENT,
                                         openMetadataRootElement.getElementHeader().getGUID());
        super.addRelatedElementSummaries(openMetadataRootElement.getTerms(),
                                         VisualStyle.LINKED_ELEMENT,
                                         openMetadataRootElement.getElementHeader().getGUID());

        super.addRelatedElementSummaries(openMetadataRootElement.getRelatedTerms(),
                                         VisualStyle.LINKED_ELEMENT,
                                         openMetadataRootElement.getElementHeader().getGUID());

        super.addRelatedElementSummaries(openMetadataRootElement.getUsedInContexts(),
                                         VisualStyle.LINKED_ELEMENT,
                                         openMetadataRootElement.getElementHeader().getGUID());
        super.addRelatedElementSummaries(openMetadataRootElement.getContextRelevantTerms(),
                                         VisualStyle.LINKED_ELEMENT,
                                         openMetadataRootElement.getElementHeader().getGUID());


        super.addRelatedElementSummaries(openMetadataRootElement.getMeaningForDataElements(),
                                         VisualStyle.LINKED_ELEMENT,
                                         openMetadataRootElement.getElementHeader().getGUID());
        if (openMetadataRootElement.getMeanings() != null)
        {
            for (RelatedMetadataElementSummary assignee : openMetadataRootElement.getMeanings())
            {
                if (assignee != null)
                {
                    if (assignee.getRelatedElementAtEnd1())
                    {
                        super.appendNewMermaidNode(assignee.getRelatedElement(), VisualStyle.LINKED_ELEMENT);

                        super.appendMermaidLongAnimatedLine(assignee.getRelationshipHeader().getGUID(),
                                                            openMetadataRootElement.getElementHeader().getGUID(),
                                                            super.addSpacesToTypeName(assignee.getRelationshipHeader().getType().getTypeName()),
                                                            assignee.getRelatedElement().getElementHeader().getGUID());

                        if (assignee.getRelatedElement().getElementHeader().getAnchor() != null)
                        {
                            ElementClassification classification = assignee.getRelatedElement().getElementHeader().getAnchor();

                            if (classification.getClassificationProperties().get(OpenMetadataProperty.ANCHOR_GUID.name) != null)
                            {
                                String anchorGUID = classification.getClassificationProperties().get(OpenMetadataProperty.ANCHOR_GUID.name).toString();

                                if ((anchorGUID != null) && (!anchorGUID.equals(assignee.getRelatedElement().getElementHeader().getGUID())))
                                {
                                    String anchorTypeName = OpenMetadataType.OPEN_METADATA_ROOT.typeName;

                                    if (classification.getClassificationProperties().get(OpenMetadataProperty.ANCHOR_TYPE_NAME.name) != null)
                                    {
                                        anchorTypeName = classification.getClassificationProperties().get(OpenMetadataProperty.ANCHOR_TYPE_NAME.name).toString();
                                    }
                                    else if (classification.getClassificationProperties().get(OpenMetadataProperty.ANCHOR_DOMAIN_NAME.name) != null)
                                    {
                                        /*
                                         * Try to narrow down the type of the anchor since its actual type is not defined
                                         */
                                        anchorTypeName = classification.getClassificationProperties().get(OpenMetadataProperty.ANCHOR_DOMAIN_NAME.name).toString();
                                    }

                                    super.appendNewMermaidNode(anchorGUID,
                                                               anchorGUID,
                                                               super.addSpacesToTypeName(anchorTypeName),
                                                               VisualStyle.ANCHOR_ELEMENT);

                                    super.appendMermaidDottedLine(assignee.getRelationshipHeader().getGUID() + "_Anchor",
                                                                  assignee.getRelatedElement().getElementHeader().getGUID(),
                                                                  "Anchored to",
                                                                  anchorGUID);
                                }
                            }
                        }
                    }
                    else
                    {
                        addRelatedElementSummary(assignee,
                                                 VisualStyle.GLOSSARY_TERM,
                                                 assignee.getRelatedElement().getElementHeader().getGUID(),
                                                 LineStyle.NORMAL);
                    }
                }
            }
        }

        super.addRelatedElementSummaries(openMetadataRootElement.getSemanticDefinitions(),
                                         VisualStyle.LINKED_ELEMENT,
                                         openMetadataRootElement.getElementHeader().getGUID());
        super.addRelatedElementSummaries(openMetadataRootElement.getSemanticallyAssociatedDefinitions(),
                                         VisualStyle.LINKED_ELEMENT,
                                         openMetadataRootElement.getElementHeader().getGUID());

        super.addRelatedElementSummaries(openMetadataRootElement.getSupplementaryProperties(),
                                         VisualStyle.LINKED_ELEMENT,
                                         openMetadataRootElement.getElementHeader().getGUID());
        super.addRelatedElementSummary(openMetadataRootElement.getSupplementsElement(),
                                       VisualStyle.LINKED_ELEMENT,
                                       openMetadataRootElement.getElementHeader().getGUID());

        super.addRelatedElementSummaries(openMetadataRootElement.getGovernedBy(),
                                         VisualStyle.LINKED_ELEMENT,
                                         openMetadataRootElement.getElementHeader().getGUID());
        super.addRelatedElementSummaries(openMetadataRootElement.getGovernedElements(),
                                         VisualStyle.LINKED_ELEMENT,
                                         openMetadataRootElement.getElementHeader().getGUID());

        super.addRelatedElementSummaries(openMetadataRootElement.getPeerGovernanceDefinitions(),
                                         VisualStyle.LINKED_ELEMENT,
                                         openMetadataRootElement.getElementHeader().getGUID());

        super.addRelatedElementSummaries(openMetadataRootElement.getSupportedGovernanceDefinitions(),
                                         VisualStyle.LINKED_ELEMENT,
                                         openMetadataRootElement.getElementHeader().getGUID());
        super.addRelatedElementSummaries(openMetadataRootElement.getSupportingGovernanceDefinitions(),
                                         VisualStyle.LINKED_ELEMENT,
                                         openMetadataRootElement.getElementHeader().getGUID());

        super.addRelatedElementSummaries(openMetadataRootElement.getMetrics(),
                                         VisualStyle.LINKED_ELEMENT,
                                         openMetadataRootElement.getElementHeader().getGUID());
        super.addRelatedElementSummaries(openMetadataRootElement.getMeasuredDefinitions(),
                                         VisualStyle.LINKED_ELEMENT,
                                         openMetadataRootElement.getElementHeader().getGUID());

        super.addRelatedElementSummaries(openMetadataRootElement.getLicenses(),
                                         VisualStyle.LINKED_ELEMENT,
                                         openMetadataRootElement.getElementHeader().getGUID());
        super.addRelatedElementSummaries(openMetadataRootElement.getLicensedElements(),
                                         VisualStyle.LINKED_ELEMENT,
                                         openMetadataRootElement.getElementHeader().getGUID());

        super.addRelatedElementSummaries(openMetadataRootElement.getCertifications(),
                                         VisualStyle.LINKED_ELEMENT,
                                         openMetadataRootElement.getElementHeader().getGUID());
        super.addRelatedElementSummaries(openMetadataRootElement.getCertifiedElements(),
                                         VisualStyle.LINKED_ELEMENT,
                                         openMetadataRootElement.getElementHeader().getGUID());

        super.addRelatedElementSummaries(openMetadataRootElement.getAgreementItems(),
                                         VisualStyle.LINKED_ELEMENT,
                                         openMetadataRootElement.getElementHeader().getGUID());
        super.addRelatedElementSummaries(openMetadataRootElement.getAgreementContents(),
                                         VisualStyle.LINKED_ELEMENT,
                                         openMetadataRootElement.getElementHeader().getGUID());

        super.addRelatedElementSummaries(openMetadataRootElement.getAgreementActors(),
                                         VisualStyle.LINKED_ELEMENT,
                                         openMetadataRootElement.getElementHeader().getGUID());
        super.addRelatedElementSummaries(openMetadataRootElement.getInvolvedInAgreements(),
                                         VisualStyle.LINKED_ELEMENT,
                                         openMetadataRootElement.getElementHeader().getGUID());

        super.addRelatedElementSummaries(openMetadataRootElement.getContracts(),
                                         VisualStyle.LINKED_ELEMENT,
                                         openMetadataRootElement.getElementHeader().getGUID());
        super.addRelatedElementSummaries(openMetadataRootElement.getAgreementsForContract(),
                                         VisualStyle.LINKED_ELEMENT,
                                         openMetadataRootElement.getElementHeader().getGUID());





        super.addRelatedElementSummaries(openMetadataRootElement.getLineageLinkage(),
                                         VisualStyle.LINKED_ELEMENT,
                                         openMetadataRootElement.getElementHeader().getGUID());



           super.addRelatedElementSummaries(openMetadataRootElement.getDerivedFrom(),
                                         VisualStyle.LINKED_ELEMENT,
                                         openMetadataRootElement.getElementHeader().getGUID());

        super.addRelatedElementSummaries(openMetadataRootElement.getImplementedBy(),
                                         VisualStyle.LINKED_ELEMENT,
                                         openMetadataRootElement.getElementHeader().getGUID());

        super.addRelatedElementSummaries(openMetadataRootElement.getUsedInImplementationOf(),
                                         VisualStyle.LINKED_ELEMENT,
                                         openMetadataRootElement.getElementHeader().getGUID());

        super.addRelatedElementSummaries(openMetadataRootElement.getImplementationResources(),
                                         VisualStyle.LINKED_ELEMENT,
                                         openMetadataRootElement.getElementHeader().getGUID());

        super.addRelatedElementSummaries(openMetadataRootElement.getOtherRelatedElements(),
                                         VisualStyle.LINKED_ELEMENT,
                                         openMetadataRootElement.getElementHeader().getGUID());

        this.addRelatedElementSummaries(openMetadataRootElement.getUsedByDigitalProducts(),
                                        VisualStyle.LINKED_ELEMENT,
                                        openMetadataRootElement.getElementHeader().getGUID());

        this.addRelatedElementSummaries(openMetadataRootElement.getUsesDigitalProducts(),
                                        VisualStyle.LINKED_ELEMENT,
                                        openMetadataRootElement.getElementHeader().getGUID());

        this.addRelatedElementSummaries(openMetadataRootElement.getAgreementItems(),
                                        VisualStyle.LINKED_ELEMENT,
                                        openMetadataRootElement.getElementHeader().getGUID());

        this.addRelatedElementSummaries(openMetadataRootElement.getAgreementActors(),
                                        VisualStyle.LINKED_ELEMENT,
                                        openMetadataRootElement.getElementHeader().getGUID());

        this.addRelatedElementSummaries(openMetadataRootElement.getDigitalSubscribers(),
                                        VisualStyle.LINKED_ELEMENT,
                                        openMetadataRootElement.getElementHeader().getGUID());

        this.addRelatedElementSummaries(openMetadataRootElement.getContracts(),
                                        VisualStyle.LINKED_ELEMENT,
                                        openMetadataRootElement.getElementHeader().getGUID());
    }
}
