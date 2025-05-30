/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworks.openmetadata.mermaid;

import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.*;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

/**
 * ProjectGraphMermaidGraphBuilder builds the mermaid graph for a project graph.
 */
public class ProjectGraphMermaidGraphBuilder extends ProjectMermaidGraphBuilder
{
    /**
     * Constructor does all of the work.
     *
     * @param projectGraph element with all the data
     */
    public ProjectGraphMermaidGraphBuilder(ProjectGraph projectGraph)
    {
        super(projectGraph);

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
                             getProjectType(projectHierarchy.getElementHeader()),
                             VisualStyle.PROJECT);

        if (projectHierarchy.getChildren() != null)
        {
            for (ProjectHierarchy childProject : projectHierarchy.getChildren())
            {
                if (childProject != null)
                {
                    addProjectResources(childProject);
                    addProjectHierarchy(childProject);

                    super.appendMermaidThinLine(childProject.getRelatedBy().getRelationshipHeader().getGUID(),
                                                projectHierarchy.getElementHeader().getGUID(),
                                                super.addSpacesToTypeName(OpenMetadataType.PROJECT_HIERARCHY_RELATIONSHIP.typeName),
                                                childProject.getElementHeader().getGUID());
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
                                         getProjectType(node.getRelatedElement().getElementHeader()),
                                         VisualStyle.PROJECT);

                    super.appendMermaidLine(node.getRelationshipHeader().getGUID(),
                                            projectHierarchy.getElementHeader().getGUID(),
                                            super.addSpacesToTypeName(OpenMetadataType.PROJECT_DEPENDENCY_RELATIONSHIP.typeName),
                                            node.getRelatedElement().getElementHeader().getGUID());
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
                                         getProjectType(node.getRelatedElement().getElementHeader()),
                                         VisualStyle.PROJECT);

                    super.appendMermaidLine(node.getRelationshipHeader().getGUID(),
                                            node.getRelatedElement().getElementHeader().getGUID(),
                                            super.addSpacesToTypeName(OpenMetadataType.PROJECT_DEPENDENCY_RELATIONSHIP.typeName),
                                            projectHierarchy.getElementHeader().getGUID());
                }
            }
        }
    }
}
