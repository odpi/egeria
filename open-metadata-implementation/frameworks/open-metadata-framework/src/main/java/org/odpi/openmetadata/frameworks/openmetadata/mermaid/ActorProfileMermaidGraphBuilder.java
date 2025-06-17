/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworks.openmetadata.mermaid;

import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.ActorProfileElement;


/**
 * Creates a mermaid graph rendering of the Open Metadata Framework's actor role graph.
 */
public class ActorProfileMermaidGraphBuilder extends MermaidGraphBuilderBase
{
    /**
     * Construct a mermaid markdown graph.
     *
     * @param actorProfileElement content
     */
    public ActorProfileMermaidGraphBuilder(ActorProfileElement actorProfileElement)
    {
        mermaidGraph.append("---\n");
        mermaidGraph.append("title: Actor Profile - ");
        mermaidGraph.append(actorProfileElement.getProfileProperties().getKnownName());
        mermaidGraph.append(" [");
        mermaidGraph.append(actorProfileElement.getElementHeader().getGUID());
        mermaidGraph.append("]\n---\nflowchart TD\n%%{init: {\"flowchart\": {\"htmlLabels\": false}} }%%\n\n");

        String currentNodeName    = actorProfileElement.getElementHeader().getGUID();
        String currentDisplayName = actorProfileElement.getProfileProperties().getKnownName();

        appendNewMermaidNode(currentNodeName,
                             currentDisplayName,
                             actorProfileElement.getElementHeader().getType().getTypeName(),
                             getVisualStyleForEntity(actorProfileElement.getElementHeader(),
                                                     VisualStyle.GOVERNANCE_ACTOR));

        this.addRelatedElementSummaries(actorProfileElement.getExternalReferences(),
                                        VisualStyle.EXTERNAL_REFERENCE,
                                        actorProfileElement.getElementHeader().getGUID());

        this.addRelatedElementSummaries(actorProfileElement.getOtherRelatedElements(),
                                        VisualStyle.LINKED_ELEMENT,
                                        actorProfileElement.getElementHeader().getGUID());
    }
}
