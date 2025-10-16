/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworks.openmetadata.mermaid;

import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.OpenMetadataRootElement;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.RelatedMetadataElementSummary;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.RelatedMetadataHierarchySummary;
import org.odpi.openmetadata.frameworks.openmetadata.properties.solutions.SolutionBlueprintProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.solutions.SolutionComponentProperties;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.util.ArrayList;
import java.util.List;


/**
 * Creates a mermaid graph rendering of the Open Metadata Framework's solution blueprint graph.
 */
public class SolutionBlueprintMermaidGraphBuilder extends MermaidGraphBuilderBase
{
    /**
     * Construct a mermaid markdown graph.
     *
     * @param solutionBlueprintElement content
     */
    public SolutionBlueprintMermaidGraphBuilder(OpenMetadataRootElement solutionBlueprintElement)
    {
        if (solutionBlueprintElement.getProperties() instanceof SolutionBlueprintProperties solutionBlueprintProperties)
        {
            mermaidGraph.append("---\n");
            mermaidGraph.append("title: Components and Roles for Solution Blueprint - ");
            mermaidGraph.append(solutionBlueprintProperties.getDisplayName());
            mermaidGraph.append(" [");
            mermaidGraph.append(solutionBlueprintElement.getElementHeader().getGUID());
            mermaidGraph.append("]\n---\nflowchart TD\n%%{init: {\"flowchart\": {\"htmlLabels\": false}} }%%\n\n");

            if (solutionBlueprintElement.getCollectionMembers() != null)
            {
                super.startSubgraph("Components and Actors", VisualStyle.SOLUTION_BLUEPRINT_GRAPH);

                List<String> solutionLinkingWireGUIDs = new ArrayList<>();
                List<String> validNodeGUIDs           = new ArrayList<>();

                /*
                 * The solution components displayed are the ones directly linked to the blueprint.
                 * Build a list of value guids.  Later processing will hunt around for links to tie them
                 * together.
                 */
                for (RelatedMetadataElementSummary node : solutionBlueprintElement.getCollectionMembers())
                {
                    if ((node != null) && (propertyHelper.isTypeOf(node.getRelatedElement().getElementHeader(), OpenMetadataType.SOLUTION_COMPONENT.typeName)))
                    {
                        validNodeGUIDs.add(node.getRelatedElement().getElementHeader().getGUID());

                        if (node.getRelatedElement().getProperties() instanceof SolutionComponentProperties solutionComponentProperties)
                        {
                            appendNewMermaidNode(node.getRelatedElement().getElementHeader().getGUID(),
                                                 getNodeDisplayName(node.getRelatedElement()),
                                                 node.getRelatedElement().getElementHeader().getType().getTypeName(),
                                                 node.getRelatedElement().getProperties(),
                                                 getVisualStyleForClassifications(node.getRelatedElement().getElementHeader(),
                                                                                  this.getVisualStyleForSolutionComponent(solutionComponentProperties.getSolutionComponentType())));
                        }
                        else
                        {
                            appendNewMermaidNode(node.getRelatedElement().getElementHeader().getGUID(),
                                                 getNodeDisplayName(node.getRelatedElement()),
                                                 node.getRelatedElement().getElementHeader().getType().getTypeName(),
                                                 node.getRelatedElement().getProperties(),
                                                 getVisualStyleForClassifications(node.getRelatedElement().getElementHeader(),
                                                                                  this.getVisualStyleForEntity(node.getRelatedElement().getElementHeader(), VisualStyle.GOVERNED_ELEMENT)));
                        }
                    }
                }

                for (RelatedMetadataElementSummary node : solutionBlueprintElement.getCollectionMembers())
                {
                    if ((node != null) && (propertyHelper.isTypeOf(node.getRelatedElement().getElementHeader(), OpenMetadataType.SOLUTION_COMPONENT.typeName)))
                    {
                        /*
                         * Build out the relationships between the components in the graph.
                         * Also add the actors.
                         */
                        this.addSolutionComponentLinksToGraph(node,
                                                              solutionLinkingWireGUIDs,
                                                              validNodeGUIDs);
                    }
                }

                super.endSubgraph();
            }
            else
            {
                clearGraph();
            }
        }
    }


    /**
     * Add a solution component to graph.

     * @param solutionComponentElement element to process
     * @param solutionLinkingWireGUIDs list of solution wires already defined
     * @param validNodeGUIDs list of solution component nodes that ate allowed in the blueprint
     */
    public void addSolutionComponentLinksToGraph(RelatedMetadataElementSummary solutionComponentElement,
                                                 List<String>                  solutionLinkingWireGUIDs,
                                                 List<String>                  validNodeGUIDs)

    {
        if (solutionComponentElement != null)
        {
            if (solutionComponentElement instanceof RelatedMetadataHierarchySummary relatedMetadataHierarchySummary)
            {
                if (relatedMetadataHierarchySummary.getSideLinks() != null)
                {
                    for (RelatedMetadataElementSummary line : relatedMetadataHierarchySummary.getSideLinks())
                    {
                        if ((line != null) &&
                                (! solutionLinkingWireGUIDs.contains(line.getRelationshipHeader().getGUID())))
                        {
                            String relatedComponentDisplayName = getNodeDisplayName(line.getRelatedElement());

                            /*
                             * Only actors can be added to the graph at this point.
                             */
                            if (propertyHelper.isTypeOf(line.getRelatedElement().getElementHeader(), OpenMetadataType.ACTOR.typeName))
                            {
                                appendNewMermaidNode(line.getRelatedElement().getElementHeader().getGUID(),
                                                     relatedComponentDisplayName,
                                                     line.getRelatedElement().getElementHeader().getType().getTypeName(),
                                                     line.getRelatedElement().getProperties(),
                                                     getVisualStyleForClassifications(line.getRelationshipHeader(), VisualStyle.GOVERNANCE_ACTOR));
                                validNodeGUIDs.add(line.getRelatedElement().getElementHeader().getGUID());
                            }

                            if (validNodeGUIDs.contains(line.getRelatedElement().getElementHeader().getGUID()))
                            {
                                List<String> labelList = new ArrayList<>();

                                String label = super.getRelationshipLabel(line);

                                if (label != null)
                                {
                                    labelList.add(label);
                                    labelList.add("[" + this.addSpacesToTypeName(line.getRelationshipHeader().getType().getTypeName()) + "]");
                                }
                                else
                                {
                                    labelList.add(this.addSpacesToTypeName(line.getRelationshipHeader().getType().getTypeName()));
                                }

                                if (line.getRelatedElementAtEnd1())
                                {
                                    this.appendMermaidLine(line.getRelationshipHeader().getGUID(),
                                                           line.getRelatedElement().getElementHeader().getGUID(),
                                                           this.getListLabel(labelList),
                                                           solutionComponentElement.getRelatedElement().getElementHeader().getGUID());
                                }
                                else
                                {
                                    this.appendMermaidLine(line.getRelationshipHeader().getGUID(),
                                                           solutionComponentElement.getRelatedElement().getElementHeader().getGUID(),
                                                           this.getListLabel(labelList),
                                                           line.getRelatedElement().getElementHeader().getGUID());
                                }

                                solutionLinkingWireGUIDs.add(line.getRelationshipHeader().getGUID());
                            }
                        }
                    }
                }
            }
        }
    }
}
