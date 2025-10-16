/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworks.openmetadata.mermaid;

import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.*;

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
     * @param fullDisplay print all elements
     */
    public SolutionComponentMermaidGraphBuilder(SolutionComponentElement solutionComponentElement,
                                                boolean                  fullDisplay)
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
                                         solutionWireGUIDs,
                                         fullDisplay);

        if (solutionComponentElement.getContext() != null)
        {
            for (InformationSupplyChainContext informationSupplyChainContext : solutionComponentElement.getContext())
            {
                if (informationSupplyChainContext != null)
                {
                    String currentNodeName = solutionComponentElement.getElementHeader().getGUID();

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
                                                     parentComponent.getRelatedElement().getProperties(),
                                                     getVisualStyleForClassifications(parentComponent.getRelatedElement().getElementHeader(),
                                                                                      super.getVisualStyleForSolutionComponent(parentComponent.getRelatedElement().getElementHeader().getType().getTypeName())));

                                appendMermaidLine(parentComponent.getRelationshipHeader().getGUID(),
                                                  parentComponentName,
                                                  super.addSpacesToTypeName(parentComponent.getRelationshipHeader().getType().getTypeName()),
                                                  currentNodeName);

                                currentNodeName = parentComponentName;
                            }
                        }
                    }

                    super.addRelatedElementSummaries(informationSupplyChainContext.owningInformationSupplyChains(), VisualStyle.INFORMATION_SUPPLY_CHAIN, currentNodeName);
                }
            }
        }

        if ((solutionComponentElement.getSubComponents() != null) &&
                (! solutionComponentElement.getSubComponents().isEmpty()))
        {
            final String subcomponentArea = "Subcomponents";
            super.startSubgraph(subcomponentArea, VisualStyle.SOLUTION_SUBGRAPH);

            for (SolutionComponentElement node : solutionComponentElement.getSubComponents())
            {
                if (node != null)
                {
                    this.addSolutionComponentToGraph(null,
                                                     null,
                                                     node,
                                                     solutionWireGUIDs,
                                                     false);
                }
            }

            super.endSubgraph(); // subcomponents

            super.appendMermaidLine(null,
                                    solutionComponentElement.getElementHeader().getGUID(),
                                    null,
                                    subcomponentArea);
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
