/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworks.openmetadata.mermaid;

import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.ActorRoleElement;


/**
 * Creates a mermaid graph rendering of the Open Metadata Framework's actor role graph.
 */
public class ActorRoleMermaidGraphBuilder extends OpenMetadataRootMermaidGraphBuilder
{
    /**
     * Construct a mermaid markdown graph.
     *
     * @param actorRoleElement content
     */
    public ActorRoleMermaidGraphBuilder(ActorRoleElement actorRoleElement)
    {
        super(actorRoleElement);

        this.addRelatedElementSummaries(actorRoleElement.getSolutionComponents(),
                                        VisualStyle.DEFAULT_SOLUTION_COMPONENT,
                                        actorRoleElement.getElementHeader().getGUID());
    }
}
