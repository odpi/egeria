/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworks.openmetadata.mermaid;

import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.*;
import org.odpi.openmetadata.frameworks.openmetadata.properties.LabeledRelationshipProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.RoledRelationshipProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.informationsupplychains.InformationSupplyChainProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.solutions.SolutionLinkingWireProperties;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;


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
            mermaidGraph.append(super.removeTroublesomeTitleCharacters(informationSupplyChainProperties.getDisplayName()));
            mermaidGraph.append(" [");
            mermaidGraph.append(informationSupplyChainElement.getElementHeader().getGUID());
            mermaidGraph.append("]\n---\nflowchart TD\n%%{init: {\"flowchart\": {\"htmlLabels\": false}} }%%\n\n");

            String iscAreaName            = "Context";
            String designAreaName         = "Design";
            String productAreaName        = "Data Mesh";
            String implementationAreaName = "Data Fabric";

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

            /*
             * Fill out the design area if there is one.
             */
            if (informationSupplyChainElement.getCollectionMembers() != null)
            {
                int         solutionComponentCount = 0;
                Set<String> designTypeNames        = new HashSet<>();
                for (RelatedMetadataElementSummary collectionMember : informationSupplyChainElement.getCollectionMembers())
                {
                    if ((collectionMember != null) &&
                            (! propertyHelper.isTypeOf(collectionMember.getRelatedElement().getElementHeader(), OpenMetadataType.INFORMATION_SUPPLY_CHAIN.typeName)))
                    {
                        solutionComponentCount++;
                        designTypeNames.add(collectionMember.getRelatedElement().getElementHeader().getType().getTypeName());
                    }
                }

                if (solutionComponentCount > 0)
                {
                    super.startSubgraph(designAreaName, VisualStyle.SOLUTION_SUBGRAPH);

                    /*
                     * Add the solution components that are explicit members of the information supply chains
                     */
                    for (String designTypeName : designTypeNames)
                    {
                        super.addUnlinkedRelatedElementSummaries(informationSupplyChainElement.getCollectionMembers(), designTypeName, VisualStyle.DEFAULT_SOLUTION_COMPONENT, informationSupplyChainElement.getElementHeader().getGUID());
                    }

                    /*
                     * Only add the links between the solution components that are part of this informaito nsupply chain.
                     */
                    for (RelatedMetadataElementSummary collectionMember : informationSupplyChainElement.getCollectionMembers())
                    {
                        if ((collectionMember != null) &&
                                (! propertyHelper.isTypeOf(collectionMember.getRelatedElement().getElementHeader(), OpenMetadataType.INFORMATION_SUPPLY_CHAIN.typeName)))
                        {
                            if (collectionMember instanceof RelatedMetadataHierarchySummary hierarchySummary)
                            {
                                if (hierarchySummary.getSideLinks() != null)
                                {
                                    for (RelatedMetadataElementSummary sideLink : hierarchySummary.getSideLinks())
                                    {
                                        if ((sideLink != null) &&
                                                (sideLink.getRelationshipProperties() instanceof SolutionLinkingWireProperties solutionLinkingWireProperties) &&
                                                (solutionLinkingWireProperties.getISCQualifiedNames() != null) && (solutionLinkingWireProperties.getISCQualifiedNames().contains(informationSupplyChainProperties.getQualifiedName())))
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

            /*
             * Two graphs are made from the implementation relationships.  One for the product dependencies and one for the implementation (typically assets).
             */
            if ((informationSupplyChainElement.getImplementation() != null) && (! informationSupplyChainElement.getImplementation().isEmpty()))
            {
                Map<String, ElementStub> productMap = new HashMap<>();
                Map<String, ElementStub> implementationMap = new HashMap<>();

                /*
                 * Work out how many nodes in each subgraph
                 */
                for (MetadataRelationshipSummary lineageRelationship : informationSupplyChainElement.getImplementation())
                {
                    if (lineageRelationship != null)
                    {
                        extractAnchorInfo(lineageRelationship.getEnd1());
                        extractAnchorInfo(lineageRelationship.getEnd2());

                        if (propertyHelper.isTypeOf(lineageRelationship.getEnd1(), OpenMetadataType.DIGITAL_PRODUCT.typeName))
                        {
                            productMap.put(lineageRelationship.getEnd1().getGUID(), lineageRelationship.getEnd1());
                        }
                        else
                        {
                            implementationMap.put(lineageRelationship.getEnd1().getGUID(), lineageRelationship.getEnd1());
                        }

                        if (propertyHelper.isTypeOf(lineageRelationship.getEnd2(), OpenMetadataType.DIGITAL_PRODUCT.typeName))
                        {
                            productMap.put(lineageRelationship.getEnd2().getGUID(), lineageRelationship.getEnd2());
                        }
                        else
                        {
                            implementationMap.put(lineageRelationship.getEnd2().getGUID(), lineageRelationship.getEnd2());
                        }
                    }
                }

                /*
                * If there are no products, then the product area is not needed.
                * Otherwise populate the subgraph with the extracted nodes.
                 */
                if (productMap.isEmpty())
                {
                    productAreaName = null;
                }
                else
                {
                    super.startSubgraph(productAreaName, VisualStyle.DIGITAL_PRODUCT_GRAPH);

                    for (ElementStub elementStub : productMap.values())
                    {
                        if (elementStub != null)
                        {
                            if (elementStub.getUniqueName() != null)
                            {
                                appendNewMermaidNode(elementStub.getGUID(),
                                                     elementStub.getUniqueName(),
                                                     elementStub.getType().getTypeName(),
                                                     getVisualStyleForEntity(elementStub, VisualStyle.DIGITAL_PRODUCT));
                            }
                            else
                            {
                                appendNewMermaidNode(elementStub.getGUID(),
                                                     elementStub.getGUID(),
                                                     elementStub.getType().getTypeName(),
                                                     getVisualStyleForEntity(elementStub, VisualStyle.DIGITAL_PRODUCT));
                            }
                        }
                    }

                    super.endSubgraph(); // data mesh
                }


                /*
                 * If there are no implementations, then the implementation area is not needed.
                 * Otherwise, populate it with the extracted nodes.
                 */
                if (implementationMap.isEmpty())
                {
                    implementationAreaName = null;
                }
                else
                {
                    super.startSubgraph(implementationAreaName, VisualStyle.INFORMATION_SUPPLY_CHAIN_SEG);

                    for (ElementStub elementStub : implementationMap.values())
                    {
                        if (elementStub != null)
                        {
                            if (elementStub.getUniqueName() != null)
                            {
                                appendNewMermaidNode(elementStub.getGUID(),
                                                     elementStub.getUniqueName(),
                                                     elementStub.getType().getTypeName(),
                                                     getVisualStyleForEntity(elementStub, VisualStyle.INFORMATION_SUPPLY_CHAIN_IMPL));
                            }
                            else
                            {
                                appendNewMermaidNode(elementStub.getGUID(),
                                                     elementStub.getGUID(),
                                                     elementStub.getType().getTypeName(),
                                                     getVisualStyleForEntity(elementStub, VisualStyle.INFORMATION_SUPPLY_CHAIN_IMPL));
                            }

                        }
                    }

                    super.endSubgraph(); // implementation
                }
            }
            else // Neither graph is needed
            {
                productAreaName = null;
                implementationAreaName = null;
            }

            /*
             * Add the relationships
             */
            for (MetadataRelationshipSummary lineageRelationship : informationSupplyChainElement.getImplementation())
            {
                if (lineageRelationship != null)
                {
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

            /*
             * Link the subgraphs together.
             */
            String currentAreaName = iscAreaName;

            if (designAreaName != null)
            {
                super.appendInvisibleMermaidLine(currentAreaName, designAreaName);
                currentAreaName = designAreaName;
            }

            if (productAreaName != null)
            {
                super.appendInvisibleMermaidLine(currentAreaName, productAreaName);
                currentAreaName = productAreaName;
            }

            if (implementationAreaName != null)
            {
                super.appendInvisibleMermaidLine(currentAreaName, implementationAreaName);
            }
        }
    }
}
