/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworks.openmetadata.mermaid;

import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.OpenMetadataRootElement;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.RelatedMetadataElementSummary;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.RelatedMetadataHierarchySummary;
import org.odpi.openmetadata.frameworks.openmetadata.properties.actors.TeamProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.actors.TeamStructureProperties;

import java.util.List;


/**
 * Show the structure of an organization as a mermaid graph.
 */
public class OrganizationTreeMermaidGraphBuilder extends MermaidGraphBuilderBase
{
    /**
     * Constructor for the graph
     *
     * @param organization starting element
     */
    public OrganizationTreeMermaidGraphBuilder(OpenMetadataRootElement organization)
    {
        if (organization.getProperties() instanceof TeamProperties organizationProperties)
        {
            String rootNodeId = organization.getElementHeader().getGUID();
            mermaidGraph.append("---\n");
            mermaidGraph.append("title: Team structure - ");
            mermaidGraph.append(super.removeTroublesomeTitleCharacters(organizationProperties.getDisplayName()));
            mermaidGraph.append(" [");
            mermaidGraph.append(rootNodeId);
            mermaidGraph.append("]\n---\nflowchart RL\n%%{init: {\"flowchart\": {\"htmlLabels\": false}} }%%\n\n");

            appendNewMermaidNode(rootNodeId,
                                 organizationProperties.getDisplayName(),
                                 organizationProperties.getTypeName(),
                                 organizationProperties.getURL(),
                                 VisualStyle.ORGANIZATION);


            addSubteams(rootNodeId, organization.getSubTeams());
        }
    }


    /**
     * Add the subteams to the graph.
     *
     * @param parentNodeGUID unique identifier of the parent node
     * @param subteams list of subteams to add
     */
    private void addSubteams(String                              parentNodeGUID,
                             List<RelatedMetadataElementSummary> subteams)
    {
        for (RelatedMetadataElementSummary subteam : subteams)
        {
            if ((subteam != null) && (subteam.getRelatedElement().getProperties() instanceof TeamProperties teamProperties))
            {
                appendNewMermaidNode(subteam.getRelatedElement().getElementHeader().getGUID(),
                                     teamProperties.getDisplayName(),
                                     subteam.getRelatedElement().getElementHeader().getType().getTypeName(),
                                     teamProperties.getURL(),
                                     VisualStyle.GOVERNANCE_TEAM);

                LineStyle lineStyle = LineStyle.DOTTED;
                if ((subteam.getRelationshipProperties() instanceof TeamStructureProperties teamStructureProperties) &&
                    (teamStructureProperties.getDelegationEscalationAuthority()))
                {
                   lineStyle = LineStyle.NORMAL;
                }

                super.appendMermaidStyledLine(subteam.getRelationshipHeader().getGUID(),
                                              parentNodeGUID,
                                              null,
                                              subteam.getRelatedElement().getElementHeader().getGUID(),
                                              lineStyle);
            }
        }

        /*
         * Iterate down the hierarchy
         */
        for (RelatedMetadataElementSummary subteam : subteams)
        {
            if ((subteam instanceof RelatedMetadataHierarchySummary relatedMetadataHierarchySummary) &&
                    (relatedMetadataHierarchySummary.getNestedElements() != null))
            {
                addSubteams(subteam.getRelatedElement().getElementHeader().getGUID(),
                            relatedMetadataHierarchySummary.getNestedElements());
            }
        }
    }
}


