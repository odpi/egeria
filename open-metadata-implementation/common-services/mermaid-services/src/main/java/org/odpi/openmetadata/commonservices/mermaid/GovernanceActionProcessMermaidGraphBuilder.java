/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.commonservices.mermaid;

import org.odpi.openmetadata.frameworks.governanceaction.properties.GovernanceActionProcessGraph;
import org.odpi.openmetadata.frameworks.governanceaction.properties.GovernanceActionProcessStepExecutionElement;
import org.odpi.openmetadata.frameworks.governanceaction.properties.NextGovernanceActionProcessStepLink;

import java.util.ArrayList;
import java.util.List;


public class GovernanceActionProcessMermaidGraphBuilder extends MermaidGraphBuilderBase
{
    /**
     * Constructor for the graph
     *
     * @param processGraph contents
     */
    public GovernanceActionProcessMermaidGraphBuilder(GovernanceActionProcessGraph processGraph)
    {
        mermaidGraph.append("---\n");
        mermaidGraph.append("title: Process - ");
        mermaidGraph.append(processGraph.getGovernanceActionProcess().getProcessProperties().getDisplayName());
        mermaidGraph.append(" [");
        mermaidGraph.append(processGraph.getGovernanceActionProcess().getElementHeader().getGUID());
        mermaidGraph.append("]\n---\nflowchart LR\n%%{init: {\"flowchart\": {\"htmlLabels\": false}} }%%\n\n");

        List<String> usedNodeNames = new ArrayList<>();

        String currentNodeName;
        String currentDisplayName;

        if (processGraph.getFirstProcessStep() != null)
        {
            currentNodeName    = processGraph.getFirstProcessStep().getElement().getElementHeader().getGUID();
            currentDisplayName = processGraph.getFirstProcessStep().getElement().getProcessStepProperties().getDisplayName();

            appendMermaidNode(currentNodeName,
                              currentDisplayName,
                              processGraph.getFirstProcessStep().getElement().getElementHeader().getType().getTypeName());

            usedNodeNames.add(currentNodeName);
        }

        if (processGraph.getNextProcessSteps() != null)
        {
            for (GovernanceActionProcessStepExecutionElement node : processGraph.getNextProcessSteps())
            {
                if (node != null)
                {
                    currentNodeName    = node.getElementHeader().getGUID();
                    currentDisplayName = node.getProcessStepProperties().getDisplayName();

                    if (currentDisplayName == null)
                    {
                        currentDisplayName = node.getProcessStepProperties().getProcessStepName();
                    }

                    if (!usedNodeNames.contains(currentNodeName))
                    {
                        appendMermaidNode(currentNodeName,
                                          currentDisplayName,
                                          node.getElementHeader().getType().getTypeName());

                        usedNodeNames.add(currentNodeName);
                    }
                }
            }

            for (NextGovernanceActionProcessStepLink line : processGraph.getProcessStepLinks())
            {
                if (line != null)
                {
                    super.appendMermaidLine(this.removeSpaces(line.getPreviousProcessStep().getGUID()),
                                            line.getGuard(),
                                            this.removeSpaces(line.getNextProcessStep().getGUID()));
                }
            }
        }
    }
}
