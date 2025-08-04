/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworks.openmetadata.mermaid;

import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.ToDoActionTargetElement;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.ToDoElement;
import org.odpi.openmetadata.frameworks.openmetadata.properties.actions.ToDoProperties;

import java.util.UUID;

/**
 * Display  To Do element in context
 */
public class ToDoActionMermaidGraphBuilder extends MermaidGraphBuilderBase
{
    /**
     * Constructor for the graph
     *
     * @param toDoElement contents
     */
    public ToDoActionMermaidGraphBuilder(ToDoElement toDoElement)
    {
        mermaidGraph.append("---\n");
        mermaidGraph.append("title: To Do Action - ");

        if (toDoElement.getProperties() instanceof ToDoProperties toDoProperties)
        {
            mermaidGraph.append(toDoProperties.getDisplayName());
        }

        mermaidGraph.append(" [");
        mermaidGraph.append(toDoElement.getElementHeader().getGUID());
        mermaidGraph.append("]\n---\nflowchart LR\n%%{init: {\"flowchart\": {\"htmlLabels\": false}} }%%\n\n");

        String currentNodeName    = toDoElement.getElementHeader().getGUID();

        appendNewMermaidNode(currentNodeName,
                             super.getNodeDisplayName(toDoElement),
                             this.getTypeNameForEntity(toDoElement.getElementHeader()),
                             getVisualStyleForEntity(toDoElement.getElementHeader(), VisualStyle.TO_DO));

        this.addDescription(toDoElement);

        if (toDoElement.getAssignedActors() != null)
        {
            this.addRelatedElementSummaries(toDoElement.getAssignedActors(),
                                            VisualStyle.GOVERNANCE_ACTOR,
                                            toDoElement.getElementHeader().getGUID());
        }

        if (toDoElement.getSponsors() != null)
        {
            this.addRelatedElementSummaries(toDoElement.getSponsors(),
                                            VisualStyle.GOVERNANCE_ACTOR,
                                            toDoElement.getElementHeader().getGUID());
        }

        if (toDoElement.getToDoSource() != null)
        {
            this.addRelatedElementSummary(toDoElement.getToDoSource(),
                                          VisualStyle.PRINCIPAL_ASSET,
                                          toDoElement.getElementHeader().getGUID());
        }

        if (toDoElement.getActionTargets() != null)
        {
            for (ToDoActionTargetElement actionTargetElement : toDoElement.getActionTargets())
            {
                if (actionTargetElement != null)
                {
                    appendNewMermaidNode(actionTargetElement.getTargetElement(), VisualStyle.GOVERNED_ELEMENT);

                    String label = super.addSpacesToTypeName(actionTargetElement.getRelationshipHeader().getType().getTypeName());

                    if (actionTargetElement.getRelationshipProperties() != null)
                    {
                        if (actionTargetElement.getRelationshipProperties().getActionTargetName() != null)
                        {
                            label = label + " " + actionTargetElement.getRelationshipProperties().getActionTargetName();
                        }

                        if (actionTargetElement.getRelationshipProperties().getStatus() != null)
                        {
                            label = label + " - " + actionTargetElement.getRelationshipProperties().getStatus().getName();
                        }
                    }

                    appendMermaidLongLine(actionTargetElement.getRelationshipHeader().getGUID(),
                                          toDoElement.getElementHeader().getGUID(),
                                          label,
                                          actionTargetElement.getTargetElement().getElementHeader().getGUID());
                }
            }
        }

        if (toDoElement.getExternalReferences() != null)
        {
            this.addRelatedElementSummaries(toDoElement.getExternalReferences(),
                                            VisualStyle.EXTERNAL_REFERENCE,
                                            toDoElement.getElementHeader().getGUID());
        }

        if (toDoElement.getOtherRelatedElements() != null)
        {
            this.addRelatedElementSummaries(toDoElement.getOtherRelatedElements(),
                                            VisualStyle.GOVERNED_ELEMENT,
                                            toDoElement.getElementHeader().getGUID());
        }
    }


    /**
     * Add a text boxes with the description of the process (if any)
     *
     * @param toDoElement element with the potential description
     */
    private void addDescription(ToDoElement toDoElement)
    {
        if (toDoElement.getProperties() instanceof ToDoProperties toDoProperties)
        {
            super.startSubgraph("Properties", VisualStyle.DESCRIPTION, "TB");

            String lastNodeName = null;

            if (toDoProperties.getUserDefinedActivityStatus() != null)
            {
                String descriptionNodeName = UUID.randomUUID().toString();

                appendNewMermaidNode(descriptionNodeName,
                                     toDoProperties.getUserDefinedActivityStatus(),
                                     "Type",
                                     VisualStyle.DESCRIPTION);

                lastNodeName = descriptionNodeName;
            }

            if (toDoProperties.getDescription() != null)
            {
                String descriptionNodeName = UUID.randomUUID().toString();

                appendNewMermaidNode(descriptionNodeName,
                                     toDoProperties.getDescription(),
                                     "Description",
                                     VisualStyle.DESCRIPTION);

                if (lastNodeName != null)
                {
                    super.appendInvisibleMermaidLine(lastNodeName, descriptionNodeName);
                }

                lastNodeName = descriptionNodeName;
            }

            if (toDoProperties.getActivityStatus() != null)
            {
                String descriptionNodeName = UUID.randomUUID().toString();

                appendNewMermaidNode(descriptionNodeName,
                                     toDoProperties.getActivityStatus().getName(),
                                     "Status",
                                     VisualStyle.DESCRIPTION);

                if (lastNodeName != null)
                {
                    super.appendInvisibleMermaidLine(lastNodeName, descriptionNodeName);
                }

                lastNodeName = descriptionNodeName;
            }

            if (toDoProperties.getRequestedTime() != null)
            {
                String descriptionNodeName = UUID.randomUUID().toString();

                appendNewMermaidNode(descriptionNodeName,
                                     toDoProperties.getRequestedTime().toString(),
                                     "Creation Time",
                                     VisualStyle.DESCRIPTION);

                if (lastNodeName != null)
                {
                    super.appendInvisibleMermaidLine(lastNodeName, descriptionNodeName);
                }

                lastNodeName = descriptionNodeName;
            }

            if (toDoProperties.getDueTime() != null)
            {
                String descriptionNodeName = UUID.randomUUID().toString();

                appendNewMermaidNode(descriptionNodeName,
                                     toDoProperties.getDueTime().toString(),
                                     "Due Time",
                                     VisualStyle.DESCRIPTION);

                if (lastNodeName != null)
                {
                    super.appendInvisibleMermaidLine(lastNodeName, descriptionNodeName);
                }

                lastNodeName = descriptionNodeName;
            }

            if (toDoProperties.getLastReviewTime() != null)
            {
                String descriptionNodeName = UUID.randomUUID().toString();

                appendNewMermaidNode(descriptionNodeName,
                                     toDoProperties.getLastReviewTime().toString(),
                                     "Last Review Time",
                                     VisualStyle.DESCRIPTION);

                if (lastNodeName != null)
                {
                    super.appendInvisibleMermaidLine(lastNodeName, descriptionNodeName);
                }

                lastNodeName = descriptionNodeName;
            }

            if (toDoProperties.getLastPauseTime() != null)
            {
                String descriptionNodeName = UUID.randomUUID().toString();

                appendNewMermaidNode(descriptionNodeName,
                                     toDoProperties.getLastPauseTime().toString(),
                                     "Completion Time",
                                     VisualStyle.DESCRIPTION);

                if (lastNodeName != null)
                {
                    super.appendInvisibleMermaidLine(lastNodeName, descriptionNodeName);
                }
            }

            super.endSubgraph();
        }
    }
}
