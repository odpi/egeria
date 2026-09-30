/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworks.openmetadata.mermaid;

import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.*;
import org.odpi.openmetadata.frameworks.openmetadata.properties.LabeledRelationshipProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.RoledRelationshipProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.informationsupplychains.InformationSupplyChainProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.solutions.SolutionLinkingWireProperties;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Creates a mermaid graph rendering of the Open Metadata Framework's information supply chain graph.
 */
public class InformationSupplyChainMermaidGraphBuilder extends MermaidGraphBuilderBase
{
    private static final String STATUS_NODE_SUFFIX = "~status";

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
            String systemAreaName         = "System Fabric";
            String statusAreaName         = "Status";

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
                for (RelatedMetadataElementSummary collectionMember : informationSupplyChainElement.getCollectionMembers())
                {
                    if ((collectionMember != null) &&
                            (! propertyHelper.isTypeOf(collectionMember.getRelatedElement().getElementHeader(), OpenMetadataType.INFORMATION_SUPPLY_CHAIN.typeName)))
                    {
                        solutionComponentCount++;
                    }
                }

                if (solutionComponentCount > 0)
                {
                    super.startSubgraph(designAreaName, VisualStyle.SOLUTION_SUBGRAPH);

                    /*
                     * Add the solution components that are explicit members of the information supply chains
                     */
                    for (RelatedMetadataElementSummary collectionMember : informationSupplyChainElement.getCollectionMembers())
                    {
                        if ((collectionMember != null) &&
                                (! propertyHelper.isTypeOf(collectionMember.getRelatedElement().getElementHeader(), OpenMetadataType.INFORMATION_SUPPLY_CHAIN.typeName)))
                        {
                            super.appendNewMermaidNode(collectionMember.getRelatedElement(), VisualStyle.DEFAULT_SOLUTION_COMPONENT);
                        }
                    }

                    /*
                     * Only add the links between the solution components that are part of this information supply chain.
                     */
                    addSolutionLinkingWires(informationSupplyChainElement, informationSupplyChainProperties.getQualifiedName(), "");

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
             * The status area shows the same solution components, styled by how far their implementation has progressed.
             * They need their own node identifiers because the design area already uses the element's guid.
             */
            if (informationSupplyChainElement.getCollectionMembers() != null)
            {
                boolean startedStatusArea = false;

                for (RelatedMetadataElementSummary collectionMember : informationSupplyChainElement.getCollectionMembers())
                {
                    if ((collectionMember != null) &&
                            (! propertyHelper.isTypeOf(collectionMember.getRelatedElement().getElementHeader(), OpenMetadataType.INFORMATION_SUPPLY_CHAIN.typeName)))
                    {
                        if (! startedStatusArea)
                        {
                            super.startSubgraph(statusAreaName, VisualStyle.SOLUTION_SUBGRAPH);
                            startedStatusArea = true;
                        }

                        super.appendNewSolutionComponentNode(collectionMember.getRelatedElement(),
                                                             collectionMember.getRelatedElement().getElementHeader().getGUID() + STATUS_NODE_SUFFIX,
                                                             VisualStyle.DEFAULT_SOLUTION_COMPONENT);
                    }
                }

                if (startedStatusArea)
                {
                    addSolutionLinkingWires(informationSupplyChainElement, informationSupplyChainProperties.getQualifiedName(), STATUS_NODE_SUFFIX);

                    super.endSubgraph(); // status
                }
                else
                {
                    statusAreaName = null;
                }
            }
            else
            {
                statusAreaName = null;
            }

            /*
             * Two graphs are made from the implementation relationships.  One for the product dependencies and one for the implementation (typically assets).
             */
            if ((informationSupplyChainElement.getImplementation() != null) && (! informationSupplyChainElement.getImplementation().isEmpty()))
            {
                Map<String, ElementStub> productMap = new HashMap<>();
                Map<String, ElementStub> implementationMap = new HashMap<>();
                Map<String, ElementStub> systemMap = new HashMap<>();

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
                        else if (isSystemElement(lineageRelationship.getEnd1()))
                        {
                            systemMap.put(lineageRelationship.getEnd1().getGUID(), lineageRelationship.getEnd1());
                        }
                        else
                        {
                            implementationMap.put(lineageRelationship.getEnd1().getGUID(), lineageRelationship.getEnd1());
                        }

                        if (propertyHelper.isTypeOf(lineageRelationship.getEnd2(), OpenMetadataType.DIGITAL_PRODUCT.typeName))
                        {
                            productMap.put(lineageRelationship.getEnd2().getGUID(), lineageRelationship.getEnd2());
                        }
                        else if (isSystemElement(lineageRelationship.getEnd2()))
                        {
                            systemMap.put(lineageRelationship.getEnd2().getGUID(), lineageRelationship.getEnd2());
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
                    super.startSubgraph(productAreaName, VisualStyle.DIGITAL_PRODUCT_GRAPH, "BT"); // BT = Bottom to Top; may want RL = Right to Left

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
                 * If there are no data or process implementations, then the data fabric area is not needed.
                 * Otherwise, populate it with the extracted nodes.
                 */
                if (implementationMap.isEmpty())
                {
                    implementationAreaName = null;
                }
                else
                {
                    addFabricArea(implementationAreaName, implementationMap);
                }

                /*
                 * The IT infrastructure and software capabilities go in the system fabric area, if there are any.
                 */
                if (systemMap.isEmpty())
                {
                    systemAreaName = null;
                }
                else
                {
                    addFabricArea(systemAreaName, systemMap);
                }
            }
            else // Neither graph is needed
            {
                productAreaName = null;
                implementationAreaName = null;
                systemAreaName = null;
            }

            /*
             * Add the relationships
             */
            List<MetadataRelationshipSummary> implementation = informationSupplyChainElement.getImplementation();

            for (MetadataRelationshipSummary lineageRelationship : implementation == null ? new ArrayList<MetadataRelationshipSummary>() : implementation)
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

            if (statusAreaName != null)
            {
                super.appendInvisibleMermaidLine(currentAreaName, statusAreaName);
                currentAreaName = statusAreaName;
            }

            if (productAreaName != null)
            {
                super.appendInvisibleMermaidLine(currentAreaName, productAreaName);
                currentAreaName = productAreaName;
            }

            if (implementationAreaName != null)
            {
                super.appendInvisibleMermaidLine(currentAreaName, implementationAreaName);
                currentAreaName = implementationAreaName;
            }

            if (systemAreaName != null)
            {
                super.appendInvisibleMermaidLine(currentAreaName, systemAreaName);
            }
        }
    }


    /**
     * Add the solution linking wires that belong to this information supply chain.  The same wires are drawn in
     * both the design and the status areas, so the suffix is used to keep the node identifiers of the areas apart.
     *
     * @param informationSupplyChainElement element containing the solution components
     * @param iscQualifiedName qualified name of the information supply chain
     * @param nodeSuffix suffix appended to the identifiers of the nodes and lines (empty for the design area)
     */
    private void addSolutionLinkingWires(InformationSupplyChainElement informationSupplyChainElement,
                                         String                        iscQualifiedName,
                                         String                        nodeSuffix)
    {
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
                                    (solutionLinkingWireProperties.getISCQualifiedNames() != null) && (solutionLinkingWireProperties.getISCQualifiedNames().contains(iscQualifiedName)))
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
                                    appendMermaidDottedLine(sideLink.getRelationshipHeader().getGUID() + nodeSuffix,
                                                            sideLink.getRelatedElement().getElementHeader().getGUID() + nodeSuffix,
                                                            label,
                                                            collectionMember.getRelatedElement().getElementHeader().getGUID() + nodeSuffix);
                                }
                                else
                                {
                                    appendMermaidDottedLine(sideLink.getRelationshipHeader().getGUID() + nodeSuffix,
                                                            collectionMember.getRelatedElement().getElementHeader().getGUID() + nodeSuffix,
                                                            label,
                                                            sideLink.getRelatedElement().getElementHeader().getGUID() + nodeSuffix);
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * IT infrastructure and software capabilities are the systems that the data and processes run on.
     *
     * @param elementStub element to test
     * @return boolean flag
     */
    private boolean isSystemElement(ElementStub elementStub)
    {
        return (propertyHelper.isTypeOf(elementStub, OpenMetadataType.IT_INFRASTRUCTURE.typeName)) ||
                (propertyHelper.isTypeOf(elementStub, OpenMetadataType.SOFTWARE_CAPABILITY.typeName));
    }


    /**
     * Add a subgraph containing the supplied implementation elements.
     *
     * @param areaName name of the subgraph
     * @param elementMap elements to add
     */
    private void addFabricArea(String                   areaName,
                               Map<String, ElementStub> elementMap)
    {
        super.startSubgraph(areaName, VisualStyle.INFORMATION_SUPPLY_CHAIN_SEG);

        for (ElementStub elementStub : elementMap.values())
        {
            if (elementStub != null)
            {
                String displayName = elementStub.getUniqueName();

                if (displayName == null)
                {
                    displayName = elementStub.getGUID();
                }

                appendNewMermaidNode(elementStub.getGUID(),
                                     displayName,
                                     elementStub.getType().getTypeName(),
                                     getVisualStyleForEntity(elementStub, VisualStyle.INFORMATION_SUPPLY_CHAIN_IMPL));
            }
        }

        super.endSubgraph();
    }
}
