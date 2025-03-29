/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.commonservices.mermaid;

import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.*;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

public class ProjectGraphBuilder extends MermaidGraphBuilderBase
{
    public ProjectGraphBuilder(ProjectGraph projectGraph)
    {
        mermaidGraph.append("---\n");
        mermaidGraph.append("title: Project - ");
        mermaidGraph.append(projectGraph.getProperties().getIdentifier());
        mermaidGraph.append(" - ");
        mermaidGraph.append(projectGraph.getProperties().getName());
        mermaidGraph.append(" [");
        mermaidGraph.append(projectGraph.getElementHeader().getGUID());
        mermaidGraph.append("]\n---\nflowchart TD\n%%{init: {\"flowchart\": {\"htmlLabels\": false}} }%%\n\n");

        String currentNodeName    = projectGraph.getElementHeader().getGUID();
        String currentDisplayName = projectGraph.getProperties().getName();
        if (currentDisplayName == null)
        {
            currentDisplayName = projectGraph.getProperties().getIdentifier();
        }
        if (currentDisplayName == null)
        {
            currentDisplayName = projectGraph.getProperties().getQualifiedName();
        }

        /*
         * Add this node first to set up a different visual style.
         */
        appendNewMermaidNode(currentNodeName,
                             currentDisplayName,
                             projectGraph.getElementHeader().getType().getTypeName(),
                             VisualStyle.PROJECT);

        this.addProjectHierarchy(projectGraph);
    }


    /**
     * Iterate through the hierarchy.
     *
     * @param projectHierarchy current level of hierarchy
     */
    private void addProjectHierarchy(ProjectHierarchy projectHierarchy)
    {
        String currentNodeName    = projectHierarchy.getElementHeader().getGUID();
        String currentDisplayName = projectHierarchy.getProperties().getName();

        if (currentDisplayName == null)
        {
            currentDisplayName = projectHierarchy.getProperties().getIdentifier();
        }
        if (currentDisplayName == null)
        {
            currentDisplayName = projectHierarchy.getProperties().getQualifiedName();
        }

        appendNewMermaidNode(currentNodeName,
                             currentDisplayName,
                             projectHierarchy.getElementHeader().getType().getTypeName(),
                             VisualStyle.RELATED_PROJECT);

        if (projectHierarchy.getChildren() != null)
        {
            for (ProjectHierarchy childProject : projectHierarchy.getChildren())
            {
                if (childProject != null)
                {
                    addProjectHierarchy(childProject);

                    super.appendMermaidThinLine(null,
                                                this.removeSpaces(projectHierarchy.getElementHeader().getGUID()),
                                                super.addSpacesToTypeName(OpenMetadataType.PROJECT_HIERARCHY_RELATIONSHIP.typeName),
                                                this.removeSpaces(childProject.getElementHeader().getGUID()));
                }
            }
        }

        if (projectHierarchy.getDependsOnProjects() != null)
        {
            for (RelatedMetadataElementSummary node : projectHierarchy.getDependsOnProjects())
            {
                if (node != null)
                {
                    currentNodeName    = node.getRelatedElement().getElementHeader().getGUID();
                    currentDisplayName = node.getRelatedElement().getProperties().get(OpenMetadataProperty.DISPLAY_NAME.name);
                    if (currentDisplayName == null)
                    {
                        currentDisplayName = node.getRelatedElement().getProperties().get(OpenMetadataProperty.NAME.name);
                    }
                    if (currentDisplayName == null)
                    {
                        currentDisplayName = node.getRelatedElement().getProperties().get(OpenMetadataProperty.IDENTIFIER.name);
                    }
                    if (currentDisplayName == null)
                    {
                        currentDisplayName = node.getRelatedElement().getProperties().get(OpenMetadataProperty.QUALIFIED_NAME.name);
                    }

                    appendNewMermaidNode(currentNodeName,
                                             currentDisplayName,
                                             node.getRelatedElement().getElementHeader().getType().getTypeName(),
                                             VisualStyle.RELATED_PROJECT);

                    super.appendMermaidLine(node.getRelationshipHeader().getGUID(),
                                            this.removeSpaces(projectHierarchy.getElementHeader().getGUID()),
                                            super.addSpacesToTypeName(OpenMetadataType.PROJECT_DEPENDENCY_RELATIONSHIP.typeName),
                                            this.removeSpaces(node.getRelatedElement().getElementHeader().getGUID()));
                }
            }
        }

        if (projectHierarchy.getDependentProjects() != null)
        {
            for (RelatedMetadataElementSummary node : projectHierarchy.getDependentProjects())
            {
                if (node != null)
                {
                    currentNodeName    = node.getRelatedElement().getElementHeader().getGUID();
                    currentDisplayName = node.getRelatedElement().getProperties().get(OpenMetadataProperty.DISPLAY_NAME.name);
                    if (currentDisplayName == null)
                    {
                        currentDisplayName = node.getRelatedElement().getProperties().get(OpenMetadataProperty.NAME.name);
                    }
                    if (currentDisplayName == null)
                    {
                        currentDisplayName = node.getRelatedElement().getProperties().get(OpenMetadataProperty.IDENTIFIER.name);
                    }
                    if (currentDisplayName == null)
                    {
                        currentDisplayName = node.getRelatedElement().getProperties().get(OpenMetadataProperty.QUALIFIED_NAME.name);
                    }

                    appendNewMermaidNode(currentNodeName,
                                         currentDisplayName,
                                         node.getRelatedElement().getElementHeader().getType().getTypeName(),
                                         VisualStyle.RELATED_PROJECT);

                    super.appendMermaidLine(node.getRelationshipHeader().getGUID(),
                                            this.removeSpaces(node.getRelatedElement().getElementHeader().getGUID()),
                                            super.addSpacesToTypeName(OpenMetadataType.PROJECT_DEPENDENCY_RELATIONSHIP.typeName),
                                            this.removeSpaces(projectHierarchy.getElementHeader().getGUID()));
                }
            }
        }

        if (projectHierarchy.getResourceList() != null)
        {
            for (RelatedMetadataElementSummary node : projectHierarchy.getResourceList())
            {
                if (node != null)
                {
                    currentNodeName    = node.getRelatedElement().getElementHeader().getGUID();
                    currentDisplayName = node.getRelatedElement().getProperties().get(OpenMetadataProperty.DISPLAY_NAME.name);
                    if (currentDisplayName == null)
                    {
                        currentDisplayName = node.getRelatedElement().getProperties().get(OpenMetadataProperty.NAME.name);
                    }
                    if (currentDisplayName == null)
                    {
                        currentDisplayName = node.getRelatedElement().getProperties().get(OpenMetadataProperty.RESOURCE_NAME.name);
                    }
                    if (currentDisplayName == null)
                    {
                        currentDisplayName = node.getRelatedElement().getProperties().get(OpenMetadataProperty.QUALIFIED_NAME.name);
                    }

                    super.appendNewMermaidNode(currentNodeName,
                                               currentDisplayName,
                                               node.getRelatedElement().getElementHeader().getType().getTypeName(),
                                               VisualStyle.PROJECT_RESOURCE);

                    String label = null;

                    if (node.getRelationshipProperties() != null)
                    {
                        label = node.getRelationshipProperties().get(OpenMetadataProperty.RESOURCE_USE.name);

                        if (label != null)
                        {
                            label = label + " [" + super.addSpacesToTypeName(OpenMetadataType.RESOURCE_LIST_RELATIONSHIP.typeName) + "]";
                        }
                    }
                    if (label == null)
                    {
                        label = super.addSpacesToTypeName(OpenMetadataType.RESOURCE_LIST_RELATIONSHIP.typeName);
                    }

                    super.appendMermaidThinLine(node.getRelationshipHeader().getGUID(),
                                                this.removeSpaces(projectHierarchy.getElementHeader().getGUID()),
                                                label,
                                                this.removeSpaces(node.getRelatedElement().getElementHeader().getGUID()));
                }
            }

        }

        if (projectHierarchy.getProjectTeam() != null)
        {
            for (RelatedMetadataElementSummary node : projectHierarchy.getProjectTeam())
            {
                if (node != null)
                {
                    currentNodeName    = node.getRelatedElement().getElementHeader().getGUID();
                    currentDisplayName = node.getRelatedElement().getProperties().get(OpenMetadataProperty.DISPLAY_NAME.name);
                    if (currentDisplayName == null)
                    {
                        currentDisplayName = node.getRelatedElement().getProperties().get(OpenMetadataProperty.NAME.name);
                    }
                    if (currentDisplayName == null)
                    {
                        currentDisplayName = node.getRelatedElement().getProperties().get(OpenMetadataProperty.QUALIFIED_NAME.name);
                    }

                    String label = null;

                    if (node.getRelationshipProperties() != null)
                    {
                        label = node.getRelationshipProperties().get(OpenMetadataProperty.TEAM_ROLE.name);

                        if (label != null)
                        {
                            label = label + " [" + super.addSpacesToTypeName(OpenMetadataType.PROJECT_TEAM_RELATIONSHIP.typeName) + "]";
                        }
                    }
                    if (label == null)
                    {
                        label = super.addSpacesToTypeName(OpenMetadataType.PROJECT_TEAM_RELATIONSHIP.typeName);
                    }

                    appendNewMermaidNode(currentNodeName,
                                         currentDisplayName,
                                         node.getRelatedElement().getElementHeader().getType().getTypeName(),
                                         VisualStyle.SOLUTION_ROLE);

                    super.appendMermaidThinLine(node.getRelationshipHeader().getGUID(),
                                                this.removeSpaces(projectHierarchy.getElementHeader().getGUID()),
                                                label,
                                                this.removeSpaces(node.getRelatedElement().getElementHeader().getGUID()));
                }
            }
        }

        if (projectHierarchy.getProjectManagers() != null)
        {
            for (RelatedMetadataElementSummary node : projectHierarchy.getProjectManagers())
            {
                if (node != null)
                {
                    currentNodeName    = node.getRelatedElement().getElementHeader().getGUID();
                    currentDisplayName = node.getRelatedElement().getProperties().get(OpenMetadataProperty.DISPLAY_NAME.name);
                    if (currentDisplayName == null)
                    {
                        currentDisplayName = node.getRelatedElement().getProperties().get(OpenMetadataProperty.NAME.name);
                    }
                    if (currentDisplayName == null)
                    {
                        currentDisplayName = node.getRelatedElement().getProperties().get(OpenMetadataProperty.QUALIFIED_NAME.name);
                    }

                    appendNewMermaidNode(currentNodeName,
                                         currentDisplayName,
                                         node.getRelatedElement().getElementHeader().getType().getTypeName(),
                                         VisualStyle.SOLUTION_ROLE);

                    super.appendMermaidLine(node.getRelationshipHeader().getGUID(),
                                            this.removeSpaces(projectHierarchy.getElementHeader().getGUID()),
                                            super.addSpacesToTypeName(OpenMetadataType.PROJECT_MANAGEMENT_RELATIONSHIP.typeName),
                                            this.removeSpaces(node.getRelatedElement().getElementHeader().getGUID()));
                }
            }
        }
    }
}
