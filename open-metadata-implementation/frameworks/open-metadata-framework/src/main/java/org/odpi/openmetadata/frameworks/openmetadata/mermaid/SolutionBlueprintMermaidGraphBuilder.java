/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworks.openmetadata.mermaid;

import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.OpenMetadataRootElement;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.RelatedMetadataElementSummary;
import org.odpi.openmetadata.frameworks.openmetadata.properties.solutions.SolutionBlueprintProperties;

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

            if (solutionBlueprintElement.getContainsSolutionComponents() != null)
            {
                super.startSubgraph("Components and Actors", VisualStyle.SOLUTION_BLUEPRINT_GRAPH);

                List<String> solutionLinkingWireGUIDs = new ArrayList<>();

                for (RelatedMetadataElementSummary node : solutionBlueprintElement.getContainsSolutionComponents())
                {
                    if (node != null)
                    {
                        super.addSolutionComponentToGraph(null,
                                                          null,
                                                          node,
                                                          solutionLinkingWireGUIDs);
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
}
