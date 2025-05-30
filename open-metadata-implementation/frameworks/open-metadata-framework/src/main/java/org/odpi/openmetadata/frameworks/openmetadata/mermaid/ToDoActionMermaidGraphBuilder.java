/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworks.openmetadata.mermaid;

import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.ToDoActionTargetElement;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.ToDoElement;

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
        mermaidGraph.append("title: Governance Definition - ");
        mermaidGraph.append(toDoElement.getProperties().getName());
        mermaidGraph.append(" [");
        mermaidGraph.append(toDoElement.getElementHeader().getGUID());
        mermaidGraph.append("]\n---\nflowchart LR\n%%{init: {\"flowchart\": {\"htmlLabels\": false}} }%%\n\n");

        String currentNodeName = toDoElement.getElementHeader().getGUID();
        String currentDisplayName = toDoElement.getProperties().getName();

        appendNewMermaidNode(currentNodeName,
                             currentDisplayName,
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
        if (toDoElement.getProperties() != null)
        {
            super.startSubgraph("Properties", VisualStyle.DESCRIPTION, "TB");

            String lastNodeName = null;

            if (toDoElement.getProperties().getToDoType() != null)
            {
                String descriptionNodeName = UUID.randomUUID().toString();

                appendNewMermaidNode(descriptionNodeName,
                                     toDoElement.getProperties().getToDoType(),
                                     "Type",
                                     VisualStyle.DESCRIPTION);

                lastNodeName = descriptionNodeName;
            }

            if (toDoElement.getProperties().getDescription() != null)
            {
                String descriptionNodeName = UUID.randomUUID().toString();

                appendNewMermaidNode(descriptionNodeName,
                                     toDoElement.getProperties().getDescription(),
                                     "Description",
                                     VisualStyle.DESCRIPTION);

                if (lastNodeName != null)
                {
                    super.appendInvisibleMermaidLine(lastNodeName, descriptionNodeName);
                }

                lastNodeName = descriptionNodeName;
            }

            if (toDoElement.getProperties().getToDoStatus() != null)
            {
                String descriptionNodeName = UUID.randomUUID().toString();

                appendNewMermaidNode(descriptionNodeName,
                                     toDoElement.getProperties().getToDoStatus().getName(),
                                     "Status",
                                     VisualStyle.DESCRIPTION);

                if (lastNodeName != null)
                {
                    super.appendInvisibleMermaidLine(lastNodeName, descriptionNodeName);
                }

                lastNodeName = descriptionNodeName;
            }

            if (toDoElement.getProperties().getCreationTime() != null)
            {
                String descriptionNodeName = UUID.randomUUID().toString();

                appendNewMermaidNode(descriptionNodeName,
                                     toDoElement.getProperties().getCreationTime().toString(),
                                     "Creation Time",
                                     VisualStyle.DESCRIPTION);

                if (lastNodeName != null)
                {
                    super.appendInvisibleMermaidLine(lastNodeName, descriptionNodeName);
                }

                lastNodeName = descriptionNodeName;
            }

            if (toDoElement.getProperties().getDueTime() != null)
            {
                String descriptionNodeName = UUID.randomUUID().toString();

                appendNewMermaidNode(descriptionNodeName,
                                     toDoElement.getProperties().getDueTime().toString(),
                                     "Due Time",
                                     VisualStyle.DESCRIPTION);

                if (lastNodeName != null)
                {
                    super.appendInvisibleMermaidLine(lastNodeName, descriptionNodeName);
                }

                lastNodeName = descriptionNodeName;
            }

            if (toDoElement.getProperties().getLastReviewTime() != null)
            {
                String descriptionNodeName = UUID.randomUUID().toString();

                appendNewMermaidNode(descriptionNodeName,
                                     toDoElement.getProperties().getLastReviewTime().toString(),
                                     "Last Review Time",
                                     VisualStyle.DESCRIPTION);

                if (lastNodeName != null)
                {
                    super.appendInvisibleMermaidLine(lastNodeName, descriptionNodeName);
                }

                lastNodeName = descriptionNodeName;
            }

            if (toDoElement.getProperties().getCompletionTime() != null)
            {
                String descriptionNodeName = UUID.randomUUID().toString();

                appendNewMermaidNode(descriptionNodeName,
                                     toDoElement.getProperties().getCompletionTime().toString(),
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
