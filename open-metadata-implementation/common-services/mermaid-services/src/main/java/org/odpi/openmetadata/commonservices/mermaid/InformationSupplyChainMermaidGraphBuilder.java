/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.commonservices.mermaid;

import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.*;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


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
        mermaidGraph.append("---\n");
        mermaidGraph.append("title: Information Supply Chain - ");
        mermaidGraph.append(informationSupplyChainElement.getProperties().getDisplayName());
        mermaidGraph.append(" [");
        mermaidGraph.append(informationSupplyChainElement.getElementHeader().getGUID());
        mermaidGraph.append("]\n---\nflowchart TD\n%%{init: {\"flowchart\": {\"htmlLabels\": false}} }%%\n\n");

        boolean detailSubgraphStarted = false;
        boolean designSubgraphStarted = false;
        boolean implementationSubgraphStarted = false;

        if ((informationSupplyChainElement.getSegments() != null) &&
                (! informationSupplyChainElement.getSegments().isEmpty()))
        {
            String areaName = "Details";

            super.startSubgraph(areaName, VisualStyle.WHITE_SUBGRAPH);
            detailSubgraphStarted = true;

            areaName = "Segments";

            super.startSubgraph(areaName, VisualStyle.INFORMATION_SUPPLY_CHAIN_SEG_GRAPH);

            for (InformationSupplyChainSegmentElement node : informationSupplyChainElement.getSegments())
            {
                if (node != null)
                {
                    String currentNodeName    = node.getElementHeader().getGUID();
                    String currentDisplayName = node.getProperties().getDisplayName();
                    if (currentDisplayName == null)
                    {
                        currentDisplayName = node.getProperties().getQualifiedName();
                    }

                    appendNewMermaidNode(currentNodeName,
                                         currentDisplayName,
                                         node.getElementHeader().getType().getTypeName(),
                                         getVisualStyleForEntity(node.getElementHeader(), VisualStyle.INFORMATION_SUPPLY_CHAIN_SEG));


                    if (node.getLinks() != null)
                    {
                        for (InformationSupplyChainLink link : node.getLinks())
                        {
                            if (link != null)
                            {
                                List<String> labelList = new ArrayList<>();

                                if ((link.getProperties() != null) && (link.getProperties().getLabel() != null))
                                {
                                    labelList.add(link.getProperties().getLabel());
                                    labelList.add("[" + super.addSpacesToTypeName(link.getElementHeader().getType().getTypeName()) + "]");
                                }
                                else
                                {
                                    labelList.add(super.addSpacesToTypeName(link.getElementHeader().getType().getTypeName()));
                                }

                                super.appendMermaidLine(link.getElementHeader().getGUID(),
                                                        link.getEnd1Element().getGUID(),
                                                        this.getListLabel(labelList),
                                                        link.getEnd2Element().getGUID());
                            }
                        }
                    }
                }
            }

            super.endSubgraph(); // segments

            List<String> solutionLinkingWireGUIDs = new ArrayList<>();

            for (InformationSupplyChainSegmentElement node : informationSupplyChainElement.getSegments())
            {
                VisualStyle visualStyle = VisualStyle.DEFAULT_SOLUTION_COMPONENT;

                if (node != null)
                {
                    if (node.getImplementedByList() != null)
                    {
                        for (ImplementedByRelationship implementedByRelationship : node.getImplementedByList())
                        {
                            if (implementedByRelationship != null)
                            {
                                if (propertyHelper.isTypeOf(implementedByRelationship.getEnd2Element(), OpenMetadataType.SOLUTION_COMPONENT.typeName))
                                {
                                    if (! designSubgraphStarted)
                                    {
                                        areaName = "Design";

                                        super.startSubgraph(areaName, VisualStyle.SOLUTION_SUBGRAPH);
                                        designSubgraphStarted = true;
                                    }

                                    appendNewMermaidNode(implementedByRelationship.getEnd2Element().getGUID(),
                                                         implementedByRelationship.getEnd2Element().getUniqueName(),
                                                         implementedByRelationship.getEnd2Element().getType().getTypeName(),
                                                         getVisualStyleForEntity(implementedByRelationship.getEnd2Element(), visualStyle));
                                }
                            }
                        }
                    }
                }
            }

            for (InformationSupplyChainSegmentElement node : informationSupplyChainElement.getSegments())
            {
                VisualStyle visualStyle = VisualStyle.INFORMATION_SUPPLY_CHAIN_IMPL;

                if (node != null)
                {
                    if (node.getImplementedByList() != null)
                    {
                        for (ImplementedByRelationship implementedByRelationship : node.getImplementedByList())
                        {
                            if (implementedByRelationship != null)
                            {
                                if (! propertyHelper.isTypeOf(implementedByRelationship.getEnd2Element(), OpenMetadataType.SOLUTION_COMPONENT.typeName))
                                {
                                    if (! implementationSubgraphStarted)
                                    {
                                        areaName = "Implementation";
                                        super.startSubgraph(areaName, VisualStyle.INFORMATION_SUPPLY_CHAIN_SEG);
                                        implementationSubgraphStarted = true;
                                    }

                                    appendNewMermaidNode(implementedByRelationship.getEnd2Element().getGUID(),
                                                         implementedByRelationship.getEnd2Element().getUniqueName(),
                                                         implementedByRelationship.getEnd2Element().getType().getTypeName(),
                                                         getVisualStyleForEntity(implementedByRelationship.getEnd2Element(), visualStyle));
                                }

                                List<String> labelList = new ArrayList<>();

                                if ((implementedByRelationship.getProperties() != null) && (implementedByRelationship.getProperties().getRole() != null))
                                {
                                    labelList.add(implementedByRelationship.getProperties().getRole());
                                    labelList.add("[" + super.addSpacesToTypeName(implementedByRelationship.getElementHeader().getType().getTypeName()) + "]");
                                }
                                else
                                {
                                    labelList.add(super.addSpacesToTypeName(implementedByRelationship.getElementHeader().getType().getTypeName()));
                                }

                                super.appendMermaidThinLine(implementedByRelationship.getElementHeader().getGUID(),
                                                            implementedByRelationship.getEnd1Element().getGUID(),
                                                            this.getListLabel(labelList),
                                                            implementedByRelationship.getEnd2Element().getGUID());
                            }
                        }
                    }

                    if (node.getSolutionLinkingWires() != null)
                    {
                        for (SolutionLinkingWireRelationship relationship : node.getSolutionLinkingWires())
                        {
                            if (relationship != null)
                            {
                                appendNewMermaidNode(relationship.getEnd1Element().getGUID(),
                                                     relationship.getEnd1Element().getUniqueName(),
                                                     relationship.getEnd1Element().getType().getTypeName(),
                                                     getVisualStyleForEntity(relationship.getEnd1Element(), VisualStyle.DEFAULT_SOLUTION_COMPONENT));

                                appendNewMermaidNode(relationship.getEnd2Element().getGUID(),
                                                     relationship.getEnd2Element().getUniqueName(),
                                                     relationship.getEnd2Element().getType().getTypeName(),
                                                     getVisualStyleForEntity(relationship.getEnd2Element(), VisualStyle.DEFAULT_SOLUTION_COMPONENT));

                                if (!solutionLinkingWireGUIDs.contains(relationship.getElementHeader().getGUID()))
                                {
                                    List<String> labelList = new ArrayList<>();

                                    if ((relationship.getProperties() != null) && (relationship.getProperties().getLabel() != null))
                                    {
                                        labelList.add(relationship.getProperties().getLabel());
                                        labelList.add("[" + super.addSpacesToTypeName(relationship.getElementHeader().getType().getTypeName()) + "]");
                                    }
                                    else
                                    {
                                        labelList.add(super.addSpacesToTypeName(relationship.getElementHeader().getType().getTypeName()));
                                    }

                                    if (propertyHelper.isTypeOf(relationship.getElementHeader(), OpenMetadataType.IMPLEMENTED_BY_RELATIONSHIP.typeName))
                                    {
                                        extractAnchorInfo(relationship.getEnd2Element());

                                        super.appendMermaidThinLine(relationship.getElementHeader().getGUID(),
                                                                    relationship.getEnd1Element().getGUID(),
                                                                    this.getListLabel(labelList),
                                                                    relationship.getEnd2Element().getGUID());
                                    }
                                    else
                                    {
                                        super.appendMermaidLine(relationship.getElementHeader().getGUID(),
                                                                relationship.getEnd1Element().getGUID(),
                                                                this.getListLabel(labelList),
                                                                relationship.getEnd2Element().getGUID());
                                    }

                                    solutionLinkingWireGUIDs.add(relationship.getElementHeader().getGUID());
                                }
                            }
                        }
                    }
                }
            }
        }

        if (designSubgraphStarted)
        {
            super.endSubgraph(); // design
        }

        if ((informationSupplyChainElement.getImplementation() != null) &&
                (! informationSupplyChainElement.getImplementation().isEmpty()))
        {
            if (! detailSubgraphStarted)
            {
                String areaName = "Details";

                super.startSubgraph(areaName, VisualStyle.WHITE_SUBGRAPH);
                detailSubgraphStarted = true;
            }

            if (! implementationSubgraphStarted)
            {
                String areaName = "Implementation";
                super.startSubgraph(areaName, VisualStyle.INFORMATION_SUPPLY_CHAIN_SEG);
                implementationSubgraphStarted = true;
            }

            for (RelationshipElement lineageRelationship : informationSupplyChainElement.getImplementation())
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

                    String label = super.addSpacesToTypeName(lineageRelationship.getRelationshipHeader().getType().getTypeName());

                    if ((lineageRelationship.getRelationshipProperties() != null) && (lineageRelationship.getRelationshipProperties().getExtendedProperties() != null))
                    {
                        Object labelObj = lineageRelationship.getRelationshipProperties().getExtendedProperties().get(OpenMetadataProperty.LABEL.name);

                        if (labelObj != null)
                        {
                            label = labelObj + " [" + super.addSpacesToTypeName(lineageRelationship.getRelationshipHeader().getType().getTypeName()) + "]";
                        }
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
        }

        if (implementationSubgraphStarted)
        {
            super.endSubgraph(); // implementation
        }

        if (detailSubgraphStarted)
        {
            super.endSubgraph(); // details
        }

        String currentNodeName    = informationSupplyChainElement.getElementHeader().getGUID();
        String currentDisplayName = informationSupplyChainElement.getProperties().getDisplayName();

        String areaName = "Overview";
        super.startSubgraph(areaName, VisualStyle.DESCRIPTION);

        appendNewMermaidNode(currentNodeName,
                             currentDisplayName,
                             informationSupplyChainElement.getElementHeader().getType().getTypeName(),
                             getVisualStyleForEntity( informationSupplyChainElement.getElementHeader(), VisualStyle.INFORMATION_SUPPLY_CHAIN));

        this.addDescriptionArea(informationSupplyChainElement);

        super.endSubgraph(); // end

        if (detailSubgraphStarted)
        {
            super.appendInvisibleMermaidLine("Overview", "Details");
        }
    }


    /**
     * Add a subgraph for the text boxes with the description and purposes (if any)
     *
     * @param informationSupplyChainElement element with the potential description
     */
    private void addDescriptionArea(InformationSupplyChainElement informationSupplyChainElement)
    {
        if (informationSupplyChainElement.getProperties() != null)
        {
            if (informationSupplyChainElement.getProperties().getDescription() != null)
            {
                String descriptionNodeName = UUID.randomUUID().toString();

                appendNewMermaidNode(descriptionNodeName,
                                     informationSupplyChainElement.getProperties().getDescription(),
                                     "Description",
                                     VisualStyle.DESCRIPTION);
            }

            if (informationSupplyChainElement.getProperties().getPurposes() != null)
            {
                for (String purpose : informationSupplyChainElement.getProperties().getPurposes())
                {
                    if (purpose != null)
                    {
                        String purposeNodeName = UUID.randomUUID().toString();

                        appendNewMermaidNode(purposeNodeName,
                                             purpose,
                                             "Purpose",
                                             VisualStyle.DESCRIPTION);
                    }
                }
            }
        }
    }
}
