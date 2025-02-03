/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.commonservices.mermaid;

import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.*;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;

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

        String currentNodeName    = informationSupplyChainElement.getElementHeader().getGUID();
        String currentDisplayName = informationSupplyChainElement.getProperties().getDisplayName();


        appendNewMermaidNode(currentNodeName,
                             currentDisplayName,
                             informationSupplyChainElement.getElementHeader().getType().getTypeName(),
                             VisualStyle.INFORMATION_SUPPLY_CHAIN);

        this.addDescription(informationSupplyChainElement);

        if (informationSupplyChainElement.getSegments() != null)
        {
            for (InformationSupplyChainSegmentElement node : informationSupplyChainElement.getSegments())
            {
                if (node != null)
                {
                    currentNodeName    = node.getElementHeader().getGUID();
                    currentDisplayName = node.getProperties().getDisplayName();
                    if (currentDisplayName == null)
                    {
                        currentDisplayName = node.getProperties().getQualifiedName();
                    }

                    appendNewMermaidNode(currentNodeName,
                                         currentDisplayName,
                                         node.getElementHeader().getType().getTypeName(),
                                         VisualStyle.INFORMATION_SUPPLY_CHAIN_SEG);

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

                                super.appendMermaidLine(link.getEnd1Element().getGUID(),
                                                        this.getListLabel(labelList),
                                                        link.getEnd2Element().getGUID());
                            }
                        }
                    }
                }
            }

            List<String> solutionLinkingWireGUIDs = new ArrayList<>();

            for (InformationSupplyChainSegmentElement node : informationSupplyChainElement.getSegments())
            {
                if (node != null)
                {
                    if (node.getImplementedByList() != null)
                    {
                        for (ImplementedByRelationship implementedByRelationship : node.getImplementedByList())
                        {
                            if (implementedByRelationship != null)
                            {
                                appendNewMermaidNode(implementedByRelationship.getEnd1Element().getGUID(),
                                                     implementedByRelationship.getEnd1Element().getUniqueName(),
                                                     implementedByRelationship.getEnd1Element().getType().getTypeName(),
                                                     VisualStyle.DEFAULT_SOLUTION_COMPONENT);

                                appendNewMermaidNode(implementedByRelationship.getEnd2Element().getGUID(),
                                                     implementedByRelationship.getEnd2Element().getUniqueName(),
                                                     implementedByRelationship.getEnd2Element().getType().getTypeName(),
                                                     VisualStyle.DEFAULT_SOLUTION_COMPONENT);
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
                                                     VisualStyle.DEFAULT_SOLUTION_COMPONENT);

                                appendNewMermaidNode(relationship.getEnd2Element().getGUID(),
                                                     relationship.getEnd2Element().getUniqueName(),
                                                     relationship.getEnd2Element().getType().getTypeName(),
                                                     VisualStyle.DEFAULT_SOLUTION_COMPONENT);

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

                                    super.appendMermaidLine(relationship.getEnd1Element().getGUID(),
                                                            this.getListLabel(labelList),
                                                            relationship.getEnd2Element().getGUID());

                                    solutionLinkingWireGUIDs.add(relationship.getElementHeader().getGUID());
                                }
                            }
                        }
                    }
                }
            }
        }

        if (informationSupplyChainElement.getImplementation() != null)
        {
            for (RelationshipElement lineageRelationship : informationSupplyChainElement.getImplementation())
            {
                if (lineageRelationship != null)
                {
                    if (lineageRelationship.getEnd1().getUniqueName() != null)
                    {
                        appendNewMermaidNode(lineageRelationship.getEnd1().getGUID(),
                                             lineageRelationship.getEnd1().getUniqueName(),
                                             lineageRelationship.getEnd1().getType().getTypeName(),
                                             VisualStyle.INFORMATION_SUPPLY_CHAIN_IMPL);
                    }
                    else
                    {
                        appendNewMermaidNode(lineageRelationship.getEnd1().getGUID(),
                                             lineageRelationship.getEnd1().getGUID(),
                                             lineageRelationship.getEnd1().getType().getTypeName(),
                                             VisualStyle.INFORMATION_SUPPLY_CHAIN_IMPL);
                    }

                    if (lineageRelationship.getEnd2().getUniqueName() != null)
                    {
                        appendNewMermaidNode(lineageRelationship.getEnd2().getGUID(),
                                             lineageRelationship.getEnd2().getUniqueName(),
                                             lineageRelationship.getEnd2().getType().getTypeName(),
                                             VisualStyle.INFORMATION_SUPPLY_CHAIN_IMPL);
                    }
                    else
                    {
                        appendNewMermaidNode(lineageRelationship.getEnd2().getGUID(),
                                             lineageRelationship.getEnd2().getGUID(),
                                             lineageRelationship.getEnd2().getType().getTypeName(),
                                             VisualStyle.INFORMATION_SUPPLY_CHAIN_IMPL);
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

                    appendMermaidLine(lineageRelationship.getEnd1().getGUID(),
                                      label,
                                      lineageRelationship.getEnd2().getGUID());
                }
            }
        }
    }


    /**
     * Add a text boxes with the description and purposes (if any)
     *
     * @param informationSupplyChainElement element with the potential description
     */
    private void addDescription(InformationSupplyChainElement informationSupplyChainElement)
    {
        if (informationSupplyChainElement.getProperties() != null)
        {
            String lastNodeName = informationSupplyChainElement.getElementHeader().getGUID();
            if (informationSupplyChainElement.getProperties().getDescription() != null)
            {
                String descriptionNodeName = UUID.randomUUID().toString();

                appendNewMermaidNode(descriptionNodeName,
                                     informationSupplyChainElement.getProperties().getDescription(),
                                     "Description",
                                     VisualStyle.DESCRIPTION);

                super.appendInvisibleMermaidLine(lastNodeName, descriptionNodeName);

                lastNodeName = descriptionNodeName;
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

                        super.appendInvisibleMermaidLine(lastNodeName, purposeNodeName);

                        lastNodeName = purposeNodeName;
                    }
                }
            }
        }
    }
}
