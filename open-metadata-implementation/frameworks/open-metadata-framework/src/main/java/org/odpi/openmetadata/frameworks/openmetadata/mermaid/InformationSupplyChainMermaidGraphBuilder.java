/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworks.openmetadata.mermaid;

import org.odpi.openmetadata.frameworks.openmetadata.enums.DeploymentStatus;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.*;
import org.odpi.openmetadata.frameworks.openmetadata.properties.LabeledRelationshipProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.RoledRelationshipProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.informationsupplychains.InformationSupplyChainProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.solutions.SolutionComponentProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.solutions.SolutionLinkingWireProperties;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;


/**
 * Creates a mermaid graph rendering of an information supply chain's context, the design of its solution components,
 * and the status of their implementation.  The implementation of the information supply chain is drawn in separate
 * graphs by InformationSupplyChainLayerMermaidGraphBuilder because together they are often too big for mermaid to display.
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

            String iscAreaName    = "Context";
            String designAreaName = "Design";
            String statusAreaName = "Status";

            super.startSubgraph(iscAreaName, VisualStyle.WHITE_SUBGRAPH, "LR");

            appendNewMermaidNode(informationSupplyChainElement.getElementHeader().getGUID(),
                                 super.getNodeDisplayName(informationSupplyChainElement.getElementHeader(), informationSupplyChainElement.getProperties()),
                                 informationSupplyChainElement.getElementHeader().getType().getTypeName(),
                                 informationSupplyChainElement.getProperties(),
                                 VisualStyle.PRINCIPLE_INFORMATION_SUPPLY_CHAIN);

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
            int solutionComponentStatusCount = 0;

            if (informationSupplyChainElement.getCollectionMembers() != null)
            {
                int solutionComponentCount = 0;

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
                    super.startSubgraph(designAreaName, VisualStyle.SOLUTION_SUBGRAPH, "LR");

                    /*
                     * Add the solution components that are explicit members of the information supply chains
                     */
                    for (RelatedMetadataElementSummary collectionMember : informationSupplyChainElement.getCollectionMembers())
                    {
                        if ((collectionMember != null) &&
                                (! propertyHelper.isTypeOf(collectionMember.getRelatedElement().getElementHeader(), OpenMetadataType.INFORMATION_SUPPLY_CHAIN.typeName)))
                        {
                            this.appendNewMermaidNode(collectionMember.getRelatedElement().getElementHeader().getGUID(),
                                                      super.getNodeDisplayName(collectionMember.getRelatedElement()),
                                                      super.getTypeNameForEntity(collectionMember.getRelatedElement().getElementHeader()),
                                                      collectionMember.getRelatedElement().getProperties(),
                                                      VisualStyle.DEFAULT_SOLUTION_COMPONENT);


                            if ((collectionMember.getRelatedElement().getElementHeader().getPromise() != null) ||
                                (collectionMember.getRelatedElement().getElementHeader().getMemento() != null))
                            {
                                solutionComponentStatusCount++;
                            }
                            else if (collectionMember.getRelatedElement().getProperties() instanceof SolutionComponentProperties solutionComponentProperties)
                            {
                                DeploymentStatus deploymentStatus = solutionComponentProperties.getDeploymentStatus();

                                if ((deploymentStatus != null) && (deploymentStatus != DeploymentStatus.ACTIVE))
                                {
                                    solutionComponentStatusCount++;
                                }
                            }
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
            if (solutionComponentStatusCount > 0)
            {
                boolean startedStatusArea = false;

                for (RelatedMetadataElementSummary collectionMember : informationSupplyChainElement.getCollectionMembers())
                {
                    if ((collectionMember != null) &&
                            (! propertyHelper.isTypeOf(collectionMember.getRelatedElement().getElementHeader(), OpenMetadataType.INFORMATION_SUPPLY_CHAIN.typeName)))
                    {
                        if (! startedStatusArea)
                        {
                            super.startSubgraph(statusAreaName, VisualStyle.SOLUTION_SUBGRAPH, "LR");
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
}
