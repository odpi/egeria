/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.commonservices.mermaid;

import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.*;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;
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
     * @param linkedSegmentsAndSupplyChains an optional map of linked information supply chain segment mapped to owning information supply chain
     */
    public SolutionComponentMermaidGraphBuilder(SolutionComponentElement            solutionComponentElement,
                                                List<InformationSupplyChainContext> linkedSegmentsAndSupplyChains)
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

        if (linkedSegmentsAndSupplyChains != null)
        {
            String currentNodeName    = solutionComponentElement.getElementHeader().getGUID();

            for (InformationSupplyChainContext informationSupplyChainContext : linkedSegmentsAndSupplyChains)
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
                                String parentComponentDisplayName = parentComponentName;

                                if (parentComponent.getRelatedElement().getProperties() != null)
                                {
                                    parentComponentDisplayName = parentComponent.getRelatedElement().getProperties().get(OpenMetadataProperty.DISPLAY_NAME.name);

                                    if (parentComponentDisplayName == null)
                                    {
                                        parentComponentDisplayName = parentComponent.getRelatedElement().getProperties().get(OpenMetadataProperty.NAME.name);
                                    }
                                    if (parentComponentDisplayName == null)
                                    {
                                        parentComponentDisplayName = parentComponent.getRelatedElement().getProperties().get(OpenMetadataProperty.QUALIFIED_NAME.name);
                                    }
                                }

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
                        String segmentDisplayName = segmentName;

                        if (informationSupplyChainContext.linkedSegment().getRelatedElement().getProperties() != null)
                        {
                            segmentDisplayName = informationSupplyChainContext.linkedSegment().getRelatedElement().getProperties().get(OpenMetadataProperty.DISPLAY_NAME.name);

                            if (segmentDisplayName == null)
                            {
                                segmentDisplayName = informationSupplyChainContext.linkedSegment().getRelatedElement().getProperties().get(OpenMetadataProperty.NAME.name);
                            }
                            if (segmentDisplayName == null)
                            {
                                segmentDisplayName = informationSupplyChainContext.linkedSegment().getRelatedElement().getProperties().get(OpenMetadataProperty.QUALIFIED_NAME.name);
                            }
                        }

                        appendNewMermaidNode(segmentName,
                                             segmentDisplayName,
                                             informationSupplyChainContext.linkedSegment().getRelatedElement().getElementHeader().getType().getTypeName(),
                                             super.getVisualStyleForSolutionComponent(informationSupplyChainContext.linkedSegment().getRelatedElement().getElementHeader().getType().getTypeName()));

                        appendMermaidLine(segmentName,
                                          informationSupplyChainContext.linkedSegment().getRelationshipHeader().getType().getTypeName(),
                                          currentNodeName);

                        currentNodeName = segmentName;
                    }

                    if (informationSupplyChainContext.owningInformationSupplyChain() != null)
                    {
                        String iscName = informationSupplyChainContext.owningInformationSupplyChain().getRelatedElement().getElementHeader().getGUID();
                        String iscDisplayName = iscName;

                        if (informationSupplyChainContext.owningInformationSupplyChain().getRelatedElement().getProperties() != null)
                        {
                            iscDisplayName = informationSupplyChainContext.owningInformationSupplyChain().getRelatedElement().getProperties().get(OpenMetadataProperty.DISPLAY_NAME.name);

                            if (iscDisplayName == null)
                            {
                                iscDisplayName = informationSupplyChainContext.owningInformationSupplyChain().getRelatedElement().getProperties().get(OpenMetadataProperty.NAME.name);
                            }
                            if (iscDisplayName == null)
                            {
                                iscDisplayName = informationSupplyChainContext.owningInformationSupplyChain().getRelatedElement().getProperties().get(OpenMetadataProperty.QUALIFIED_NAME.name);
                            }
                        }

                        appendNewMermaidNode(iscName,
                                             iscDisplayName,
                                             informationSupplyChainContext.owningInformationSupplyChain().getRelatedElement().getElementHeader().getType().getTypeName(),
                                             super.getVisualStyleForSolutionComponent(informationSupplyChainContext.owningInformationSupplyChain().getRelatedElement().getElementHeader().getType().getTypeName()));

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
