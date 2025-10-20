/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworks.openmetadata.mermaid;

import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.*;
import org.odpi.openmetadata.frameworks.openmetadata.properties.solutions.SolutionComponentProperties;


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
    public SolutionComponentMermaidGraphBuilder(OpenMetadataRootElement solutionComponentElement)
    {
        if (solutionComponentElement.getProperties() instanceof SolutionComponentProperties solutionComponentProperties)
        {
            mermaidGraph.append("---\n");
            mermaidGraph.append("title: Solution Component - ");
            mermaidGraph.append(solutionComponentProperties.getDisplayName());
            mermaidGraph.append(" [");
            mermaidGraph.append(solutionComponentElement.getElementHeader().getGUID());
            mermaidGraph.append("]\n---\nflowchart TD\n%%{init: {\"flowchart\": {\"htmlLabels\": false}} }%%\n\n");

            if (solutionComponentProperties.getSolutionComponentType() != null)
            {
                appendNewMermaidNode(solutionComponentElement.getElementHeader().getGUID(),
                                     solutionComponentProperties.getDisplayName(),
                                     solutionComponentElement.getElementHeader().getType().getTypeName(),
                                     solutionComponentProperties,
                                     getVisualStyleForClassifications(solutionComponentElement.getElementHeader(),
                                                                      this.getVisualStyleForSolutionComponent(solutionComponentProperties.getSolutionComponentType())));
            }
            else
            {
                appendNewMermaidNode(solutionComponentElement.getElementHeader().getGUID(),
                                     solutionComponentProperties.getDisplayName(),
                                     solutionComponentElement.getElementHeader().getType().getTypeName(),
                                     getVisualStyleForEntity(solutionComponentElement.getElementHeader(),
                                                             VisualStyle.DEFAULT_SOLUTION_COMPONENT));
            }

            if ((solutionComponentElement.getNestedSolutionComponents() != null) &&
                    (!solutionComponentElement.getNestedSolutionComponents().isEmpty()))
            {
                final String subcomponentArea = "Subcomponents";
                super.startSubgraph(subcomponentArea, VisualStyle.SOLUTION_SUBGRAPH);

                super.addSolutionComponentListToGraph(solutionComponentElement.getNestedSolutionComponents());

                super.endSubgraph(); // subcomponents

                super.appendMermaidLine(null,
                                        solutionComponentElement.getElementHeader().getGUID(),
                                        null,
                                        subcomponentArea);
            }
        }
    }
}
