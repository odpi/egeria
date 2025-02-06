/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.commonservices.mermaid;

import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.*;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.util.*;


/**
 * Creates a mermaid graph rendering of the Open Metadata Framework's solution component graph.
 */
public class SolutionComponentMermaidGraphBuilder extends MermaidGraphBuilderBase
{
    /**
     * Construct a mermaid markdown graph.
     *
     * @param solutionComponentElement content
     */
    public SolutionComponentMermaidGraphBuilder(SolutionComponentElement solutionComponentElement)
    {
        mermaidGraph.append("---\n");
        mermaidGraph.append("title: Solution Component - ");
        mermaidGraph.append(solutionComponentElement.getProperties().getDisplayName());
        mermaidGraph.append(" [");
        mermaidGraph.append(solutionComponentElement.getElementHeader().getGUID());
        mermaidGraph.append("]\n---\nflowchart TD\n%%{init: {\"flowchart\": {\"htmlLabels\": false}} }%%\n\n");

        this.addDescription(solutionComponentElement);

        List<String> solutionWireGUIDs = new ArrayList<>();

        this.addSolutionComponentToGraph(null,
                                         null,
                                         solutionComponentElement,
                                         solutionWireGUIDs);

        if (solutionComponentElement.getContext() != null)
        {
            String currentNodeName = solutionComponentElement.getElementHeader().getGUID();

            for (InformationSupplyChainContext informationSupplyChainContext : solutionComponentElement.getContext())
            {
                if (informationSupplyChainContext != null)
                {
                    if (informationSupplyChainContext.parentComponents() != null)
                    {
                        for (RelatedMetadataElementSummary parentComponent : informationSupplyChainContext.parentComponents())
                        {
                            if (parentComponent != null)
                            {
                                String parentComponentName = parentComponent.getRelatedElement().getElementHeader().getGUID();
                                String parentComponentDisplayName = super.getNodeDisplayName(parentComponent.getRelatedElement());

                                appendNewMermaidNode(parentComponentName,
                                                     parentComponentDisplayName,
                                                     parentComponent.getRelatedElement().getElementHeader().getType().getTypeName(),
                                                     super.getVisualStyleForSolutionComponent(parentComponent.getRelatedElement().getElementHeader().getType().getTypeName()));

                                appendMermaidLine(parentComponentName,
                                                  parentComponent.getRelationshipHeader().getType().getTypeName(),
                                                  currentNodeName);

                                currentNodeName = parentComponentName;
                            }
                        }
                    }

                    if (informationSupplyChainContext.linkedSegment() != null)
                    {
                        String segmentName = informationSupplyChainContext.linkedSegment().getRelatedElement().getElementHeader().getGUID();
                        String segmentDisplayName = super.getNodeDisplayName(informationSupplyChainContext.linkedSegment().getRelatedElement());

                        appendNewMermaidNode(segmentName,
                                             segmentDisplayName,
                                             informationSupplyChainContext.linkedSegment().getRelatedElement().getElementHeader().getType().getTypeName(),
                                             VisualStyle.INFORMATION_SUPPLY_CHAIN_SEG);

                        appendMermaidLine(segmentName,
                                          informationSupplyChainContext.linkedSegment().getRelationshipHeader().getType().getTypeName(),
                                          currentNodeName);

                        currentNodeName = segmentName;
                    }

                    if (informationSupplyChainContext.owningInformationSupplyChain() != null)
                    {
                        String iscName = informationSupplyChainContext.owningInformationSupplyChain().getRelatedElement().getElementHeader().getGUID();
                        String iscDisplayName = super.getNodeDisplayName(informationSupplyChainContext.owningInformationSupplyChain().getRelatedElement());

                        appendNewMermaidNode(iscName,
                                             iscDisplayName,
                                             informationSupplyChainContext.owningInformationSupplyChain().getRelatedElement().getElementHeader().getType().getTypeName(),
                                             VisualStyle.INFORMATION_SUPPLY_CHAIN);

                        appendMermaidLine(iscName,
                                          informationSupplyChainContext.owningInformationSupplyChain().getRelationshipHeader().getType().getTypeName(),
                                          currentNodeName);

                        currentNodeName = iscName;
                    }
                }
            }
        }

        if (solutionComponentElement.getSubComponents() != null)
        {
            for (SolutionComponentElement node : solutionComponentElement.getSubComponents())
            {
                if (node != null)
                {
                    this.addSolutionComponentToGraph(solutionComponentElement.getElementHeader().getGUID(),
                                                     super.addSpacesToTypeName(OpenMetadataType.SOLUTION_COMPOSITION_RELATIONSHIP.typeName),
                                                     solutionComponentElement,
                                                     solutionWireGUIDs);
                }
            }
        }
    }


    /**
     * Add a text box with the description (if any)
     *
     * @param solutionComponentElement element with the potential description
     */
    private void addDescription(SolutionComponentElement solutionComponentElement)
    {
        if (solutionComponentElement.getProperties() != null)
        {
            if (solutionComponentElement.getProperties().getDescription() != null)
            {
                String descriptionNodeName = UUID.randomUUID().toString();

                appendNewMermaidNode(descriptionNodeName,
                                     solutionComponentElement.getProperties().getDescription(),
                                     "Description",
                                     VisualStyle.DESCRIPTION);
            }
        }
    }
}
