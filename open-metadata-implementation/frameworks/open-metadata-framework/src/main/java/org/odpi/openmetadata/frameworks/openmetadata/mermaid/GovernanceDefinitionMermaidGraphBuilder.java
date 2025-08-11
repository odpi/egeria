/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworks.openmetadata.mermaid;

import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.GovernanceDefinitionElement;

/**
 * Display a governance definition in context.
 */
public class GovernanceDefinitionMermaidGraphBuilder extends OpenMetadataRootMermaidGraphBuilder
{
    /**
     * Constructor for the graph
     *
     * @param governanceDefinitionElement contents
     */
    public GovernanceDefinitionMermaidGraphBuilder(GovernanceDefinitionElement governanceDefinitionElement)
    {
        super(governanceDefinitionElement);

        this.addRelatedGovernanceDefinitions(governanceDefinitionElement);
    }


    /**
     * Extract related elements to include in the graph.
     *
     * @param governanceDefinitionElement focus element
     */
    protected void addRelatedGovernanceDefinitions(GovernanceDefinitionElement governanceDefinitionElement)
    {
        this.addRelatedElementSummaries(governanceDefinitionElement.getPeerGovernanceDefinitions(),
                                        VisualStyle.SUPPORTING_GOVERNANCE_DEFINITION,
                                        governanceDefinitionElement.getElementHeader().getGUID());

        this.addRelatedElementSummaries(governanceDefinitionElement.getSupportedGovernanceDefinitions(),
                                        VisualStyle.SUPPORTING_GOVERNANCE_DEFINITION,
                                        governanceDefinitionElement.getElementHeader().getGUID());

        this.addRelatedElementSummaries(governanceDefinitionElement.getSupportingGovernanceDefinitions(),
                                        VisualStyle.SUPPORTING_GOVERNANCE_DEFINITION,
                                        governanceDefinitionElement.getElementHeader().getGUID());

        this.addRelatedElementSummaries(governanceDefinitionElement.getGovernedElements(),
                                        VisualStyle.LINKED_ELEMENT,
                                        governanceDefinitionElement.getElementHeader().getGUID());
    }
}
