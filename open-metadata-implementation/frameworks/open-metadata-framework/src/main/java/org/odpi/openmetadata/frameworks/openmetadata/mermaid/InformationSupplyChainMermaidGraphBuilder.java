/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworks.openmetadata.mermaid;

import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.*;
import org.odpi.openmetadata.frameworks.openmetadata.properties.LabeledRelationshipProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.RoledRelationshipProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.informationsupplychains.InformationSupplyChainProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.solutions.SolutionLinkingWireProperties;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;


/**
 * Creates a mermaid graph rendering of the Open Metadata Framework's information supply chain graph.
 */
public class InformationSupplyChainMermaidGraphBuilder extends MermaidGraphBuilderBase
{
    /**
     * Construct a mermaid markdown graph.
     *
     * @param informationSupplyChainElement content
     */
    public InformationSupplyChainMermaidGraphBuilder(InformationSupplyChainElement informationSupplyChainElement)
    {
        if (informationSupplyChainElement.getProperties() instanceof InformationSupplyChainProperties informationSupplyChainProperties)
        {
            maxNodeCount = 1000;

            mermaidGraph.append("---\n");
            mermaidGraph.append("title: Information Supply Chain - ");
            mermaidGraph.append(informationSupplyChainProperties.getDisplayName());
            mermaidGraph.append(" [");
            mermaidGraph.append(informationSupplyChainElement.getElementHeader().getGUID());
            mermaidGraph.append("]\n---\nflowchart TD\n%%{init: {\"flowchart\": {\"htmlLabels\": false}} }%%\n\n");

            String iscAreaName            = "Context";
            String designAreaName         = "Design";
            String implementationAreaName = "Implementation";

            super.startSubgraph(iscAreaName, VisualStyle.WHITE_SUBGRAPH);

            appendNewMermaidNode(informationSupplyChainElement.getElementHeader().getGUID(),
                                 super.getNodeDisplayName(informationSupplyChainElement.getElementHeader(), informationSupplyChainElement.getProperties()),
                                 informationSupplyChainElement.getElementHeader().getType().getTypeName(),
                                 informationSupplyChainElement.getProperties(),
                                 super.getVisualStyleForClassifications(informationSupplyChainElement.getElementHeader(), VisualStyle.PRINCIPLE_INFORMATION_SUPPLY_CHAIN));

            /*
             * Add the segments to the graph.
             */
            super.addRelatedElementSummaries(informationSupplyChainElement.getMemberOfCollections(), OpenMetadataType.INFORMATION_SUPPLY_CHAIN.typeName, VisualStyle.INFORMATION_SUPPLY_CHAIN_SEG, informationSupplyChainElement.getElementHeader().getGUID(), LineStyle.DOTTED);
            super.addRelatedElementSummaries(informationSupplyChainElement.getCollectionMembers(), OpenMetadataType.INFORMATION_SUPPLY_CHAIN.typeName, VisualStyle.INFORMATION_SUPPLY_CHAIN_SEG, informationSupplyChainElement.getElementHeader().getGUID(), LineStyle.DOTTED);

            super.addRelatedElementSummaries(informationSupplyChainElement.getSupplyTo(), OpenMetadataType.INFORMATION_SUPPLY_CHAIN.typeName, VisualStyle.INFORMATION_SUPPLY_CHAIN_SEG, informationSupplyChainElement.getElementHeader().getGUID(), LineStyle.NORMAL);
            super.addRelatedElementSummaries(informationSupplyChainElement.getSupplyFrom(), OpenMetadataType.INFORMATION_SUPPLY_CHAIN.typeName, VisualStyle.INFORMATION_SUPPLY_CHAIN_SEG, informationSupplyChainElement.getElementHeader().getGUID(), LineStyle.NORMAL);

            super.endSubgraph(); // ISC

            if (informationSupplyChainElement.getCollectionMembers() != null)
            {
                int solutionComponentCount = 0;
                for (RelatedMetadataElementSummary collectionMember : informationSupplyChainElement.getCollectionMembers())
                {
                    if ((collectionMember != null) &&
                            (propertyHelper.isTypeOf(collectionMember.getRelatedElement().getElementHeader(), OpenMetadataType.SOLUTION_COMPONENT.typeName)))
                    {
                        solutionComponentCount++;
                    }
                }

                if (solutionComponentCount > 0)
                {
                    super.startSubgraph(designAreaName, VisualStyle.SOLUTION_SUBGRAPH);

                    super.addUnlinkedRelatedElementSummaries(informationSupplyChainElement.getCollectionMembers(), OpenMetadataType.SOLUTION_COMPONENT.typeName, VisualStyle.DEFAULT_SOLUTION_COMPONENT, informationSupplyChainElement.getElementHeader().getGUID());

                    for (RelatedMetadataElementSummary collectionMember : informationSupplyChainElement.getCollectionMembers())
                    {
                        if ((collectionMember != null) &&
                                (propertyHelper.isTypeOf(collectionMember.getRelatedElement().getElementHeader(), OpenMetadataType.SOLUTION_COMPONENT.typeName)))
                        {
                            if (collectionMember instanceof RelatedMetadataHierarchySummary hierarchySummary)
                            {
                                if (hierarchySummary.getSideLinks() != null)
                                {
                                    for (RelatedMetadataElementSummary sideLink : hierarchySummary.getSideLinks())
                                    {
                                        if ((sideLink != null) && (sideLink.getRelationshipProperties() instanceof SolutionLinkingWireProperties relationshipProperties))
                                        {
                                            String label = null;

                                            if (sideLink.getRelationshipProperties() instanceof LabeledRelationshipProperties labeledRelationshipProperties)
                                            {
                                                label = labeledRelationshipProperties.getLabel();
                                            }
                                            else if (sideLink.getRelationshipProperties() instanceof RoledRelationshipProperties roledRelationshipProperties)
                                            {
                                                label = roledRelationshipProperties.getRole();
                                            }

                                            if (label != null)
                                            {
                                                label = label + " [" + super.addSpacesToTypeName(sideLink.getRelationshipHeader().getType().getTypeName()) + "]";
                                            }
                                            else
                                            {
                                                label = super.addSpacesToTypeName(sideLink.getRelationshipHeader().getType().getTypeName());
                                            }

                                            if (sideLink.getRelatedElementAtEnd1())
                                            {
                                                appendMermaidDottedLine(sideLink.getRelationshipHeader().getGUID(),
                                                                        sideLink.getRelatedElement().getElementHeader().getGUID(),
                                                                        label,
                                                                        collectionMember.getRelatedElement().getElementHeader().getGUID());
                                            }
                                            else
                                            {
                                                appendMermaidDottedLine(sideLink.getRelationshipHeader().getGUID(),
                                                                        collectionMember.getRelatedElement().getElementHeader().getGUID(),
                                                                        label,
                                                                        sideLink.getRelatedElement().getElementHeader().getGUID());
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }

                    super.endSubgraph(); // design
                }
                else
                {
                    designAreaName = null;
                }
            }
            else
            {
                designAreaName = null;
            }

            if ((informationSupplyChainElement.getImplementation() != null) && (! informationSupplyChainElement.getImplementation().isEmpty()))
            {
                super.startSubgraph(implementationAreaName, VisualStyle.INFORMATION_SUPPLY_CHAIN_SEG);

                for (MetadataRelationshipSummary lineageRelationship : informationSupplyChainElement.getImplementation())
                {
                    if (lineageRelationship != null)
                    {
                        extractAnchorInfo(lineageRelationship.getEnd1());
                        extractAnchorInfo(lineageRelationship.getEnd2());

                        if (lineageRelationship.getEnd1().getUniqueName() != null)
                        {
                            appendNewMermaidNode(lineageRelationship.getEnd1().getGUID(),
                                                 lineageRelationship.getEnd1().getUniqueName(),
                                                 lineageRelationship.getEnd1().getType().getTypeName(),
                                                 getVisualStyleForEntity(lineageRelationship.getEnd1(), VisualStyle.INFORMATION_SUPPLY_CHAIN_IMPL));
                        }
                        else
                        {
                            appendNewMermaidNode(lineageRelationship.getEnd1().getGUID(),
                                                 lineageRelationship.getEnd1().getGUID(),
                                                 lineageRelationship.getEnd1().getType().getTypeName(),
                                                 getVisualStyleForEntity(lineageRelationship.getEnd1(), VisualStyle.INFORMATION_SUPPLY_CHAIN_IMPL));
                        }

                        if (lineageRelationship.getEnd2().getUniqueName() != null)
                        {
                            appendNewMermaidNode(lineageRelationship.getEnd2().getGUID(),
                                                 lineageRelationship.getEnd2().getUniqueName(),
                                                 lineageRelationship.getEnd2().getType().getTypeName(),
                                                 getVisualStyleForEntity(lineageRelationship.getEnd2(), VisualStyle.INFORMATION_SUPPLY_CHAIN_IMPL));
                        }
                        else
                        {
                            appendNewMermaidNode(lineageRelationship.getEnd2().getGUID(),
                                                 lineageRelationship.getEnd2().getGUID(),
                                                 lineageRelationship.getEnd2().getType().getTypeName(),
                                                 getVisualStyleForEntity(lineageRelationship.getEnd2(), VisualStyle.INFORMATION_SUPPLY_CHAIN_IMPL));
                        }

                        String label = null;

                        if (lineageRelationship.getRelationshipProperties() instanceof LabeledRelationshipProperties labeledRelationshipProperties)
                        {
                            label = labeledRelationshipProperties.getLabel();
                        }
                        else if (lineageRelationship.getRelationshipProperties() instanceof RoledRelationshipProperties roledRelationshipProperties)
                        {
                            label = roledRelationshipProperties.getRole();
                        }

                        if (label != null)
                        {
                            label = label + " [" + super.addSpacesToTypeName(lineageRelationship.getRelationshipHeader().getType().getTypeName()) + "]";
                        }
                        else
                        {
                            label = super.addSpacesToTypeName(lineageRelationship.getRelationshipHeader().getType().getTypeName());
                        }

                        if (propertyHelper.isTypeOf(lineageRelationship.getRelationshipHeader(), OpenMetadataType.IMPLEMENTED_BY_RELATIONSHIP.typeName))
                        {
                            appendMermaidThinLine(lineageRelationship.getRelationshipHeader().getGUID(),
                                                  lineageRelationship.getEnd1().getGUID(),
                                                  label,
                                                  lineageRelationship.getEnd2().getGUID());
                        }
                        else
                        {
                            appendMermaidLine(lineageRelationship.getRelationshipHeader().getGUID(),
                                              lineageRelationship.getEnd1().getGUID(),
                                              label,
                                              lineageRelationship.getEnd2().getGUID());
                        }
                    }
                }

                super.endSubgraph(); // implementation
            }
            else
            {
                implementationAreaName = null;
            }

            String currentAreaName = iscAreaName;

            if (designAreaName != null)
            {
                super.appendInvisibleMermaidLine(currentAreaName, designAreaName);
                currentAreaName = designAreaName;
            }

            if (implementationAreaName != null)
            {
                super.appendInvisibleMermaidLine(currentAreaName, implementationAreaName);
            }
        }
    }
}
