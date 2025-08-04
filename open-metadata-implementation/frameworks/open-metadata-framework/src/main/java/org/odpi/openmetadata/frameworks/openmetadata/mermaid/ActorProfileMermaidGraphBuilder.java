/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworks.openmetadata.mermaid;

import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.ActorProfileElement;


/**
 * Creates a mermaid graph rendering of the Open Metadata Framework's actor profile graph.
 */
public class ActorProfileMermaidGraphBuilder extends OpenMetadataRootMermaidGraphBuilder
{
    /**
     * Construct a mermaid markdown graph.
     *
     * @param actorProfileElement content
     */
    public ActorProfileMermaidGraphBuilder(ActorProfileElement actorProfileElement)
    {
        super(actorProfileElement);

        addRelatedElementSummary(actorProfileElement.getContributionRecord(), VisualStyle.GOVERNANCE_ACTOR, actorProfileElement.getElementHeader().getGUID(), LineStyle.NORMAL);
        addRelatedElementSummary(actorProfileElement.getSuperTeam(), VisualStyle.GOVERNANCE_ACTOR, actorProfileElement.getElementHeader().getGUID(), LineStyle.NORMAL);

        this.addRelatedElementSummaries(actorProfileElement.getActorRoles(),
                                        VisualStyle.GOVERNANCE_ACTOR,
                                        actorProfileElement.getElementHeader().getGUID());

        this.addRelatedElementSummaries(actorProfileElement.getPeerGovernanceDefinitions(),
                                        VisualStyle.GOVERNANCE_ACTOR,
                                        actorProfileElement.getElementHeader().getGUID());

        this.addRelatedElementSummaries(actorProfileElement.getSubTeams(),
                                        VisualStyle.GOVERNANCE_ACTOR,
                                        actorProfileElement.getElementHeader().getGUID());

        this.addRelatedElementSummaries(actorProfileElement.getUserIdentities(),
                                        VisualStyle.GOVERNANCE_ACTOR,
                                        actorProfileElement.getElementHeader().getGUID());

        this.addRelatedElementSummaries(actorProfileElement.getLocations(),
                                        VisualStyle.LINKED_ELEMENT,
                                        actorProfileElement.getElementHeader().getGUID());

        this.addRelatedElementSummaries(actorProfileElement.getBusinessCapabilities(),
                                        VisualStyle.LINKED_ELEMENT,
                                        actorProfileElement.getElementHeader().getGUID());

        this.addRelatedElementSummaries(actorProfileElement.getLinkedInfrastructure(),
                                        VisualStyle.LINKED_ELEMENT,
                                        actorProfileElement.getElementHeader().getGUID());
    }
}
