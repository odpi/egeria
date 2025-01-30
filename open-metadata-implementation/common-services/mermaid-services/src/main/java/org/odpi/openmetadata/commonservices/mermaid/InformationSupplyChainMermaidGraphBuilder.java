/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.commonservices.mermaid;

import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.*;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;


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
                                super.appendMermaidLine(link.getEnd1Element().getGUID(),
                                                        this.getListLabel(Collections.singletonList(super.addSpacesToTypeName(link.getElementHeader().getType().getTypeName()))),
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
                                                     VisualStyle.SOLUTION_COMPONENT);

                                appendNewMermaidNode(implementedByRelationship.getEnd2Element().getGUID(),
                                                     implementedByRelationship.getEnd2Element().getUniqueName(),
                                                     implementedByRelationship.getEnd2Element().getType().getTypeName(),
                                                     VisualStyle.SOLUTION_COMPONENT);
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
                                                     VisualStyle.SOLUTION_COMPONENT);

                                appendNewMermaidNode(relationship.getEnd2Element().getGUID(),
                                                     relationship.getEnd2Element().getUniqueName(),
                                                     relationship.getEnd2Element().getType().getTypeName(),
                                                     VisualStyle.SOLUTION_COMPONENT);

                                if (! solutionLinkingWireGUIDs.contains(relationship.getElementHeader().getGUID()))
                                {
                                    super.appendMermaidLine(relationship.getEnd1Element().getGUID(),
                                                            this.getListLabel(Collections.singletonList(super.addSpacesToTypeName(relationship.getElementHeader().getType().getTypeName()))),
                                                            relationship.getEnd2Element().getGUID());

                                    solutionLinkingWireGUIDs.add(relationship.getElementHeader().getGUID());
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
