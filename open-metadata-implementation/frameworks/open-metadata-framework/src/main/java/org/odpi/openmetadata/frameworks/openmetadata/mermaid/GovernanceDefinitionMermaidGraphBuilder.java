/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworks.openmetadata.mermaid;

import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.GovernanceDefinitionElement;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.GovernanceDefinitionGraph;

import java.util.UUID;

/**
 * Display a governance definition in context.
 */
public class GovernanceDefinitionMermaidGraphBuilder extends MermaidGraphBuilderBase
{
    /**
     * Constructor for the graph
     *
     * @param governanceDefinitionGraph contents
     */
    public GovernanceDefinitionMermaidGraphBuilder(GovernanceDefinitionGraph governanceDefinitionGraph)
    {
        mermaidGraph.append("---\n");
        mermaidGraph.append("title: Governance Definition - ");
        mermaidGraph.append(governanceDefinitionGraph.getProperties().getTitle());
        mermaidGraph.append(" [");
        mermaidGraph.append(governanceDefinitionGraph.getElementHeader().getGUID());
        mermaidGraph.append("]\n---\nflowchart LR\n%%{init: {\"flowchart\": {\"htmlLabels\": false}} }%%\n\n");

        String currentNodeName = governanceDefinitionGraph.getElementHeader().getGUID();
        String currentDisplayName = governanceDefinitionGraph.getProperties().getTitle();

        appendNewMermaidNode(currentNodeName,
                             currentDisplayName,
                             this.getTypeNameForEntity(governanceDefinitionGraph.getElementHeader()),
                             getVisualStyleForEntity(governanceDefinitionGraph.getElementHeader(), VisualStyle.GOVERNANCE_DEFINITION));

        this.addDescription(governanceDefinitionGraph);

        if (governanceDefinitionGraph.getParents() != null)
        {
            this.addRelatedElementSummaries(governanceDefinitionGraph.getParents(),
                                            VisualStyle.SUPPORTING_GOVERNANCE_DEFINITION,
                                            governanceDefinitionGraph.getElementHeader().getGUID());
        }

        if (governanceDefinitionGraph.getPeers() != null)
        {
            this.addRelatedElementSummaries(governanceDefinitionGraph.getPeers(),
                                            VisualStyle.SUPPORTING_GOVERNANCE_DEFINITION,
                                            governanceDefinitionGraph.getElementHeader().getGUID());
        }

        if (governanceDefinitionGraph.getChildren() != null)
        {
            this.addRelatedElementSummaries(governanceDefinitionGraph.getChildren(),
                                            VisualStyle.SUPPORTING_GOVERNANCE_DEFINITION,
                                            governanceDefinitionGraph.getElementHeader().getGUID());
        }

        if (governanceDefinitionGraph.getMetrics() != null)
        {
            this.addRelatedElementSummaries(governanceDefinitionGraph.getMetrics(),
                                            VisualStyle.GOVERNANCE_METRIC,
                                            governanceDefinitionGraph.getElementHeader().getGUID());
        }

        if (governanceDefinitionGraph.getExternalReferences() != null)
        {
            this.addRelatedElementSummaries(governanceDefinitionGraph.getExternalReferences(),
                                            VisualStyle.EXTERNAL_REFERENCE,
                                            governanceDefinitionGraph.getElementHeader().getGUID());
        }

        if (governanceDefinitionGraph.getOthers() != null)
        {
            this.addRelatedElementSummaries(governanceDefinitionGraph.getOthers(),
                                            VisualStyle.GOVERNED_ELEMENT,
                                            governanceDefinitionGraph.getElementHeader().getGUID());
        }
    }


    /**
     * Add a text boxes with the description of the process (if any)
     *
     * @param governanceDefinitionElement element with the potential description
     */
    private void addDescription(GovernanceDefinitionElement governanceDefinitionElement)
    {
        if (governanceDefinitionElement.getProperties() != null)
        {
            String lastNodeName = null;

            if (governanceDefinitionElement.getProperties().getSummary() != null)
            {
                String descriptionNodeName = UUID.randomUUID().toString();

                appendNewMermaidNode(descriptionNodeName,
                                     governanceDefinitionElement.getProperties().getSummary(),
                                     "Summary",
                                     VisualStyle.DESCRIPTION);

                lastNodeName = descriptionNodeName;
            }

            if (governanceDefinitionElement.getProperties().getDescription() != null)
            {
                String descriptionNodeName = UUID.randomUUID().toString();

                appendNewMermaidNode(descriptionNodeName,
                                     governanceDefinitionElement.getProperties().getDescription(),
                                     "Description",
                                     VisualStyle.DESCRIPTION);

                if (lastNodeName != null)
                {
                    super.appendInvisibleMermaidLine(lastNodeName, descriptionNodeName);
                }

                lastNodeName = descriptionNodeName;
            }

            if (governanceDefinitionElement.getProperties().getScope() != null)
            {
                String descriptionNodeName = UUID.randomUUID().toString();

                appendNewMermaidNode(descriptionNodeName,
                                     governanceDefinitionElement.getProperties().getScope(),
                                     "Scope",
                                     VisualStyle.DESCRIPTION);

                if (lastNodeName != null)
                {
                    super.appendInvisibleMermaidLine(lastNodeName, descriptionNodeName);
                }

                lastNodeName = descriptionNodeName;
            }

            if (governanceDefinitionElement.getProperties().getImportance() != null)
            {
                String descriptionNodeName = UUID.randomUUID().toString();

                appendNewMermaidNode(descriptionNodeName,
                                     governanceDefinitionElement.getProperties().getImportance(),
                                     "Importance",
                                     VisualStyle.DESCRIPTION);

                if (lastNodeName != null)
                {
                    super.appendInvisibleMermaidLine(lastNodeName, descriptionNodeName);
                }

                lastNodeName = descriptionNodeName;
            }

            if (governanceDefinitionElement.getProperties().getImplications() != null)
            {
                for (String implication : governanceDefinitionElement.getProperties().getImplications())
                {
                    String descriptionNodeName = UUID.randomUUID().toString();

                    appendNewMermaidNode(descriptionNodeName,
                                         implication,
                                         "Implication",
                                         VisualStyle.DESCRIPTION);

                    if (lastNodeName != null)
                    {
                        super.appendInvisibleMermaidLine(lastNodeName, descriptionNodeName);
                    }

                    lastNodeName = descriptionNodeName;
                }
            }

            if (governanceDefinitionElement.getProperties().getOutcomes() != null)
            {
                for (String outcome : governanceDefinitionElement.getProperties().getOutcomes())
                {
                    String descriptionNodeName = UUID.randomUUID().toString();

                    appendNewMermaidNode(descriptionNodeName,
                                         outcome,
                                         "Implication",
                                         VisualStyle.DESCRIPTION);

                    if (lastNodeName != null)
                    {
                        super.appendInvisibleMermaidLine(lastNodeName, descriptionNodeName);
                    }

                    lastNodeName = descriptionNodeName;
                }
            }

            if (governanceDefinitionElement.getProperties().getResults() != null)
            {
                for (String result : governanceDefinitionElement.getProperties().getResults())
                {
                    String descriptionNodeName = UUID.randomUUID().toString();

                    appendNewMermaidNode(descriptionNodeName,
                                         result,
                                         "Result",
                                         VisualStyle.DESCRIPTION);

                    if (lastNodeName != null)
                    {
                        super.appendInvisibleMermaidLine(lastNodeName, descriptionNodeName);
                    }

                    lastNodeName = descriptionNodeName;
                }
            }
        }
    }
}
