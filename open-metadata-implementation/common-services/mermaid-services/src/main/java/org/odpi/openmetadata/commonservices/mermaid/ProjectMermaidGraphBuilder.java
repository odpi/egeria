/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.commonservices.mermaid;

import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.ElementClassification;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.ElementHeader;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.ProjectElement;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.RelatedMetadataElementSummary;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

/**
 * ProjectMermaidGraphBuilder builds the mermaid graph for a project element.
 */
public class ProjectMermaidGraphBuilder extends MermaidGraphBuilderBase
{
    /**
     * Constructor does all of the work.
     *
     * @param projectElement element with all the data
     */
    public ProjectMermaidGraphBuilder(ProjectElement projectElement)
    {
        mermaidGraph.append("---\n");
        mermaidGraph.append("title: Project - ");
        mermaidGraph.append(projectElement.getProperties().getIdentifier());
        mermaidGraph.append(" - ");
        mermaidGraph.append(projectElement.getProperties().getName());
        mermaidGraph.append(" [");
        mermaidGraph.append(projectElement.getElementHeader().getGUID());
        mermaidGraph.append("]\n---\nflowchart TD\n%%{init: {\"flowchart\": {\"htmlLabels\": false}} }%%\n\n");

        String currentNodeName    = projectElement.getElementHeader().getGUID();
        String currentDisplayName = projectElement.getProperties().getName();
        if (currentDisplayName == null)
        {
            currentDisplayName = projectElement.getProperties().getIdentifier();
        }
        if (currentDisplayName == null)
        {
            currentDisplayName = projectElement.getProperties().getQualifiedName();
        }

        /*
         * Add this node first to set up a different visual style.
         */
        appendNewMermaidNode(currentNodeName,
                             currentDisplayName,
                             getProjectType(projectElement.getElementHeader()),
                             getVisualStyleForEntity(projectElement.getElementHeader(), VisualStyle.PROJECT));

        this.addProjectResources(projectElement);
    }


    /**
     * Retrieve the type of the project.
     *
     * @param projectHeader header of the project element.
     *
     * @return type name
     */
    protected String getProjectType(ElementHeader projectHeader)
    {
        String typeName = OpenMetadataType.PROJECT.typeName;

        if ((projectHeader != null) && (projectHeader.getClassifications() != null))
        {
            typeName = projectHeader.getType().getTypeName();

            for (ElementClassification classification : projectHeader.getClassifications())
            {
                if (classification != null)
                {
                    if (OpenMetadataType.CAMPAIGN_CLASSIFICATION.typeName.equals(classification.getClassificationName()))
                    {
                        typeName = OpenMetadataType.CAMPAIGN_CLASSIFICATION.typeName;
                    }
                    else if (OpenMetadataType.TASK_CLASSIFICATION.typeName.equals(classification.getClassificationName()))
                    {
                        typeName = OpenMetadataType.TASK_CLASSIFICATION.typeName;
                    }
                    else if (OpenMetadataType.PERSONAL_PROJECT_CLASSIFICATION.typeName.equals(classification.getClassificationName()))
                    {
                        typeName = OpenMetadataType.PERSONAL_PROJECT_CLASSIFICATION.typeName;
                    }
                    else if (OpenMetadataType.STUDY_PROJECT_CLASSIFICATION.typeName.equals(classification.getClassificationName()))
                    {
                        typeName = OpenMetadataType.STUDY_PROJECT_CLASSIFICATION.typeName;
                    }
                    else if (OpenMetadataType.GLOSSARY_PROJECT_CLASSIFICATION.typeName.equals(classification.getClassificationName()))
                    {
                        typeName = OpenMetadataType.GLOSSARY_PROJECT_CLASSIFICATION.typeName;
                    }
                    else if (OpenMetadataType.GOVERNANCE_PROJECT_CLASSIFICATION.typeName.equals(classification.getClassificationName()))
                    {
                        typeName = OpenMetadataType.GOVERNANCE_PROJECT_CLASSIFICATION.typeName;
                    }
                }
            }
        }

        return typeName;
    }


    /**
     * Iterate through the hierarchy.
     *
     * @param projectElement current level of hierarchy
     */
    protected void addProjectResources(ProjectElement projectElement)
    {
        String currentNodeName    = projectElement.getElementHeader().getGUID();
        String currentDisplayName = projectElement.getProperties().getName();

        if (currentDisplayName == null)
        {
            currentDisplayName = projectElement.getProperties().getIdentifier();
        }
        if (currentDisplayName == null)
        {
            currentDisplayName = projectElement.getProperties().getQualifiedName();
        }

        appendNewMermaidNode(currentNodeName,
                             currentDisplayName,
                             getProjectType(projectElement.getElementHeader()),
                             VisualStyle.RELATED_PROJECT);

        if (projectElement.getResourceList() != null)
        {
            for (RelatedMetadataElementSummary node : projectElement.getResourceList())
            {
                if (node != null)
                {
                    super.appendNewMermaidNode(node.getRelatedElement(), VisualStyle.PROJECT_RESOURCE);

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
                                                projectElement.getElementHeader().getGUID(),
                                                label,
                                                node.getRelatedElement().getElementHeader().getGUID());
                }
            }

        }

        if (projectElement.getProjectTeam() != null)
        {
            for (RelatedMetadataElementSummary node : projectElement.getProjectTeam())
            {
                if (node != null)
                {
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

                    appendNewMermaidNode(node.getRelatedElement(), VisualStyle.PROJECT_ROLE);

                    super.appendMermaidThinLine(node.getRelationshipHeader().getGUID(),
                                                projectElement.getElementHeader().getGUID(),
                                                label,
                                                node.getRelatedElement().getElementHeader().getGUID());
                }
            }
        }

        if (projectElement.getProjectManagers() != null)
        {
            for (RelatedMetadataElementSummary node : projectElement.getProjectManagers())
            {
                if (node != null)
                {
                    super.appendNewMermaidNode(node.getRelatedElement(), VisualStyle.PROJECT_ROLE);

                    super.appendMermaidThinLine(node.getRelationshipHeader().getGUID(),
                                                projectElement.getElementHeader().getGUID(),
                                                super.addSpacesToTypeName(OpenMetadataType.PROJECT_MANAGEMENT_RELATIONSHIP.typeName),
                                                node.getRelatedElement().getElementHeader().getGUID());
                }
            }
        }
    }
}
