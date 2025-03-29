/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.commonservices.mermaid;

import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.*;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;

import java.util.Collections;


/**
 * Creates a mermaid graph rendering of the Open Metadata Framework's solution role graph.
 */
public class SolutionRoleMermaidGraphBuilder extends MermaidGraphBuilderBase
{
    /**
     * Construct a mermaid markdown graph.
     *
     * @param solutionRoleElement content
     */
    public SolutionRoleMermaidGraphBuilder(SolutionRoleElement solutionRoleElement)
    {
        mermaidGraph.append("---\n");
        mermaidGraph.append("title: Solution Components for Solution Role - ");
        mermaidGraph.append(solutionRoleElement.getProperties().getTitle());
        mermaidGraph.append(" [");
        mermaidGraph.append(solutionRoleElement.getElementHeader().getGUID());
        mermaidGraph.append("]\n---\nflowchart TD\n%%{init: {\"flowchart\": {\"htmlLabels\": false}} }%%\n\n");

        String currentNodeName    = solutionRoleElement.getElementHeader().getGUID();
        String currentDisplayName = solutionRoleElement.getProperties().getTitle();

        appendNewMermaidNode(currentNodeName,
                             currentDisplayName,
                             solutionRoleElement.getElementHeader().getType().getTypeName(),
                             VisualStyle.SOLUTION_ROLE);


        if (solutionRoleElement.getSolutionComponents() != null)
        {
            for (RelatedMetadataElementSummary node : solutionRoleElement.getSolutionComponents())
            {
                if (node != null)
                {
                    currentNodeName = node.getRelatedElement().getElementHeader().getGUID();
                    currentDisplayName   = node.getRelatedElement().getProperties().get(OpenMetadataProperty.DISPLAY_NAME.name);
                    if (currentDisplayName == null)
                    {
                        currentDisplayName = node.getRelatedElement().getProperties().get(OpenMetadataProperty.QUALIFIED_NAME.name);
                    }

                    appendNewMermaidNode(currentNodeName,
                                         currentDisplayName,
                                         node.getRelatedElement().getElementHeader().getType().getTypeName(),
                                         VisualStyle.DEFAULT_SOLUTION_COMPONENT);


                    super.appendMermaidLine(node.getRelationshipHeader().getGUID(),
                                            solutionRoleElement.getElementHeader().getGUID(),
                                            this.getListLabel(Collections.singletonList(node.getRelationshipProperties().get(OpenMetadataProperty.ROLE.name))),
                                            node.getRelatedElement().getElementHeader().getGUID());
                }
            }
        }
    }
}
