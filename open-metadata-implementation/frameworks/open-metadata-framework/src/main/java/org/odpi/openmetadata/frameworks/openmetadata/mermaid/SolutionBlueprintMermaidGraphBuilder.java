/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworks.openmetadata.mermaid;

import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.OpenMetadataRootElement;
import org.odpi.openmetadata.frameworks.openmetadata.properties.collections.CollectionProperties;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;


/**
 * Creates a mermaid graph rendering of the Open Metadata Framework's solution blueprint graph.
 * The same graph is created for a solution boundary, which shows the part of the blueprint that is inside the boundary.
 * Any solution boundaries nested in the blueprint (or boundary) are shown as subgraphs with their members inside.
 */
public class SolutionBlueprintMermaidGraphBuilder extends MermaidGraphBuilderBase
{
    /**
     * Construct a mermaid markdown graph.
     *
     * @param solutionBlueprintElement content - either a solution blueprint or a solution boundary
     */
    public SolutionBlueprintMermaidGraphBuilder(OpenMetadataRootElement solutionBlueprintElement)
    {
        if (solutionBlueprintElement.getProperties() instanceof CollectionProperties solutionBlueprintProperties)
        {
            mermaidGraph.append("---\n");

            if (propertyHelper.isTypeOf(solutionBlueprintElement.getElementHeader(), OpenMetadataType.SOLUTION_BOUNDARY.typeName))
            {
                mermaidGraph.append("title: Components and Roles for Solution Boundary - ");
            }
            else
            {
                mermaidGraph.append("title: Components and Roles for Solution Blueprint - ");
            }

            mermaidGraph.append(super.removeTroublesomeTitleCharacters(solutionBlueprintProperties.getDisplayName()));
            mermaidGraph.append(" [");
            mermaidGraph.append(solutionBlueprintElement.getElementHeader().getGUID());
            mermaidGraph.append("]\n---\nflowchart TD\n%%{init: {\"flowchart\": {\"htmlLabels\": false}} }%%\n\n");

            if (solutionBlueprintElement.getCollectionMembers() != null)
            {
                super.startSubgraph("Components and Actors", VisualStyle.SOLUTION_BLUEPRINT_GRAPH);

                super.addSolutionComponentListToGraph(solutionBlueprintElement.getCollectionMembers(), false);

                super.endSubgraph();
            }
            else
            {
                clearGraph();
            }
        }
    }


}
