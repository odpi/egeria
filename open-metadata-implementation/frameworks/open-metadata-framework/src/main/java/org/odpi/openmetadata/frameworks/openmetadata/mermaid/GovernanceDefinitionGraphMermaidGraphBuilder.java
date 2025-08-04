/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworks.openmetadata.mermaid;

import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.GovernanceDefinitionGraph;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.OpenMetadataRootElement;

/**
 * Display a governance definition in context.
 */
public class GovernanceDefinitionGraphMermaidGraphBuilder extends OpenMetadataRootMermaidGraphBuilder
{
    /**
     * Constructor for the graph
     *
     * @param governanceDefinitionGraph contents
     */
    public GovernanceDefinitionGraphMermaidGraphBuilder(GovernanceDefinitionGraph governanceDefinitionGraph)
    {
        super(governanceDefinitionGraph);

        if (governanceDefinitionGraph.getRelatedGovernanceDefinitions() != null)
        {
            for (OpenMetadataRootElement governanceDefinitionElement : governanceDefinitionGraph.getRelatedGovernanceDefinitions())
            {
                if (governanceDefinitionElement != null)
                {
                    super.addElementToMermaidGraph(governanceDefinitionElement);
                }
            }
        }
    }
}
