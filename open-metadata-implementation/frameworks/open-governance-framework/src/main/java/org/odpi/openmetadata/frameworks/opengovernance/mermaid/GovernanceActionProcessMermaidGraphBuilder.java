/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworks.opengovernance.mermaid;

import org.odpi.openmetadata.frameworks.opengovernance.properties.*;
import org.odpi.openmetadata.frameworks.openmetadata.enums.ActivityStatus;
import org.odpi.openmetadata.frameworks.openmetadata.mermaid.MermaidGraphBuilderBase;
import org.odpi.openmetadata.frameworks.openmetadata.mermaid.VisualStyle;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.util.*;


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
        mermaidGraph.append(super.removeTroublesomeTitleCharacters(processGraph.getGovernanceActionProcess().getProperties().getDisplayName()));
        mermaidGraph.append(" [");
        mermaidGraph.append(processGraph.getGovernanceActionProcess().getElementHeader().getGUID());
        mermaidGraph.append("]\n---\nflowchart LR\n%%{init: {\"flowchart\": {\"htmlLabels\": false}} }%%\n\n");

        String currentNodeName = processGraph.getGovernanceActionProcess().getElementHeader().getGUID();
        String currentDisplayName = processGraph.getGovernanceActionProcess().getProperties().getDisplayName();

        if (currentDisplayName == null)
        {
            currentDisplayName = processGraph.getGovernanceActionProcess().getProperties().getQualifiedName();
        }

        appendNewMermaidNode(currentNodeName,
                             super.removeTroublesomeCharacters(currentDisplayName),
                             super.getTypeNameForEntity(processGraph.getGovernanceActionProcess().getElementHeader()),
                             processGraph.getGovernanceActionProcess().getProperties(),
                             getVisualStyleForEntity(processGraph.getGovernanceActionProcess().getElementHeader(), VisualStyle.GOVERNANCE_ACTION));

        if (processGraph.getFirstProcessStep() != null)
        {
            currentNodeName    = processGraph.getFirstProcessStep().getElement().getElementHeader().getGUID();
            currentDisplayName = processGraph.getFirstProcessStep().getElement().getProcessStepProperties().getDisplayName();

            if (currentDisplayName == null)
            {
                currentDisplayName = processGraph.getFirstProcessStep().getElement().getProcessStepProperties().getQualifiedName();
            }

            if (processGraph.getFirstProcessStep().getElement().getProcessStepProperties().getActionStatus() == ActivityStatus.FAILED)
            {
                appendNewMermaidNode(currentNodeName,
                                     super.removeTroublesomeCharacters(currentDisplayName),
                                     super.getTypeNameForEntity(processGraph.getFirstProcessStep().getElement().getElementHeader()),
                                     this.getAdditionalProcessStepProperties(processGraph.getFirstProcessStep().getElement()),
                                     VisualStyle.FAILED_GOVERNANCE_ACTION_PROCESS_STEP);
            }
            else
            {
                appendNewMermaidNode(currentNodeName,
                                     super.removeTroublesomeCharacters(currentDisplayName),
                                     super.getTypeNameForEntity(processGraph.getFirstProcessStep().getElement().getElementHeader()),
                                     this.getAdditionalProcessStepProperties(processGraph.getFirstProcessStep().getElement()),
                                     VisualStyle.GOVERNANCE_ACTION_PROCESS_STEP);
            }

            appendMermaidLine(processGraph.getFirstProcessStep().getLinkGUID(),
                              processGraph.getGovernanceActionProcess().getElementHeader().getGUID(),
                              super.addSpacesToTypeName(OpenMetadataType.GOVERNANCE_ACTION_PROCESS_FLOW_RELATIONSHIP.typeName),
                              currentNodeName);
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

                    if (node.getProcessStepProperties().getActionStatus() == ActivityStatus.FAILED)
                    {
                        appendNewMermaidNode(currentNodeName,
                                             super.removeTroublesomeCharacters(currentDisplayName),
                                             node.getElementHeader().getType().getTypeName(),
                                             this.getAdditionalProcessStepProperties(node),
                                             VisualStyle.FAILED_GOVERNANCE_ACTION_PROCESS_STEP);
                    }
                    else
                    {
                        appendNewMermaidNode(currentNodeName,
                                             super.removeTroublesomeCharacters(currentDisplayName),
                                             node.getElementHeader().getType().getTypeName(),
                                             this.getAdditionalProcessStepProperties(node),
                                             VisualStyle.GOVERNANCE_ACTION_PROCESS_STEP);
                    }
                }
            }

            for (NextGovernanceActionProcessStepLink line : processGraph.getProcessStepLinks())
            {
                if (line != null)
                {
                    super.appendMermaidLine(null,
                                            line.getPreviousProcessStep().getGUID(),
                                            line.getGuard(),
                                            line.getNextProcessStep().getGUID());
                }
            }
        }
    }

    /**
     * Extract some additional properties for a process step.
     *
     * @param processStep retrieved process step
     * @return map of additional properties
     */
    private Map<String, String> getAdditionalProcessStepProperties(GovernanceActionProcessStepExecutionElement processStep)
    {
        if ((processStep != null) && (processStep.getProcessStepProperties() != null))
        {
            Map<String, String> additionalProperties = new HashMap<>();

            if (processStep.getProcessStepProperties().getActionStatus() != null)
            {
                additionalProperties.put(OpenMetadataProperty.ACTIVITY_STATUS.name,
                                         processStep.getProcessStepProperties().getActionStatus().name());
            }
            if ((processStep.getProcessStepProperties().getCompletionGuards() != null) &&
                (! processStep.getProcessStepProperties().getCompletionGuards().isEmpty()))
            {
                additionalProperties.put(OpenMetadataProperty.COMPLETION_GUARDS.name,
                                         processStep.getProcessStepProperties().getCompletionGuards().toString());
            }
            if (processStep.getProcessStepProperties().getCompletionMessage() != null)
            {
                additionalProperties.put(OpenMetadataProperty.COMPLETION_MESSAGE.name,
                                         processStep.getProcessStepProperties().getCompletionMessage());
            }

            return additionalProperties;
        }

        return null;
    }
}
