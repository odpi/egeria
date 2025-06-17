/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworks.openmetadata.mermaid;

import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.ActorRoleElement;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.SolutionRoleElement;


/**
 * Creates a mermaid graph rendering of the Open Metadata Framework's actor role graph.
 */
public class ActorRoleMermaidGraphBuilder extends MermaidGraphBuilderBase
{
    /**
     * Construct a mermaid markdown graph.
     *
     * @param actorRoleElement content
     */
    public ActorRoleMermaidGraphBuilder(ActorRoleElement actorRoleElement)
    {
        mermaidGraph.append("---\n");
        mermaidGraph.append("title: Actor Role - ");
        mermaidGraph.append(actorRoleElement.getProperties().getName());
        mermaidGraph.append(" [");
        mermaidGraph.append(actorRoleElement.getElementHeader().getGUID());
        mermaidGraph.append("]\n---\nflowchart TD\n%%{init: {\"flowchart\": {\"htmlLabels\": false}} }%%\n\n");

        String currentNodeName    = actorRoleElement.getElementHeader().getGUID();
        String currentDisplayName = actorRoleElement.getProperties().getName();

        appendNewMermaidNode(currentNodeName,
                             currentDisplayName,
                             actorRoleElement.getElementHeader().getType().getTypeName(),
                             getVisualStyleForEntity(actorRoleElement.getElementHeader(),
                                                     VisualStyle.GOVERNANCE_ACTOR));


        this.addRelatedElementSummaries(actorRoleElement.getExternalReferences(),
                                        VisualStyle.EXTERNAL_REFERENCE,
                                        actorRoleElement.getElementHeader().getGUID());

        if (actorRoleElement instanceof SolutionRoleElement solutionRoleElement)
        {
            this.addRelatedElementSummaries(solutionRoleElement.getSolutionComponents(),
                                            VisualStyle.DEFAULT_SOLUTION_COMPONENT,
                                            solutionRoleElement.getElementHeader().getGUID());
        }

        this.addRelatedElementSummaries(actorRoleElement.getOtherRelatedElements(),
                                        VisualStyle.LINKED_ELEMENT,
                                        actorRoleElement.getElementHeader().getGUID());
    }
}
