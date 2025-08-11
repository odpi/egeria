/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworks.openmetadata.mermaid;

import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.OpenMetadataRootHierarchy;

import java.util.List;
import java.util.UUID;


/**
 * Creates a mermaid graph rendering of the Open Metadata Framework's root element hierarchy.
 */
public class OpenMetadataRootHierarchyMermaidGraphBuilder extends OpenMetadataRootMermaidGraphBuilder
{

    /**
     * Construct a mermaid markdown graph.
     *
     * @param openMetadataRootHierarchy content
     */
    public OpenMetadataRootHierarchyMermaidGraphBuilder(OpenMetadataRootHierarchy openMetadataRootHierarchy,
                                                        String                    membershipName,
                                                        VisualStyle               principleStyle)
    {
        super(openMetadataRootHierarchy);

        if (openMetadataRootHierarchy.getOpenMetadataRootHierarchies() != null)
        {
            /*
             * Build a grid of elements in a subgraph
             */
            super.startSubgraph(membershipName, VisualStyle.DESCRIPTION, "LR");

            this.addHierarchyMembers(null,
                                     openMetadataRootHierarchy.getOpenMetadataRootHierarchies(),
                                     principleStyle);

            super.endSubgraph();

            appendMermaidDottedLine(null, openMetadataRootHierarchy.getElementHeader().getGUID(), null, membershipName);
        }
    }


    /**
     * Add the child members to the graph.
     *
     * @param parentNodeName id of the parent
     * @param openMetadataRootHierarchies list of child categories
     * @param principleStyle style to use as default
     */
    private void addHierarchyMembers(String                          parentNodeName,
                                     List<OpenMetadataRootHierarchy> openMetadataRootHierarchies,
                                     VisualStyle                     principleStyle)
    {
        if (openMetadataRootHierarchies != null)
        {
            for (OpenMetadataRootHierarchy openMetadataRootHierarchy : openMetadataRootHierarchies)
            {
                addRootMemberGraph(parentNodeName, openMetadataRootHierarchy, principleStyle);
            }
        }
    }


    /**
     * Add the child category to the graph.
     *
     * @param parentNodeName id of the parent
     * @param openMetadataRootHierarchy child element
     * @param principleStyle style to use as default
     */
    private void addRootMemberGraph(String                    parentNodeName,
                                    OpenMetadataRootHierarchy openMetadataRootHierarchy,
                                    VisualStyle               principleStyle)
    {
        appendNewMermaidNode(openMetadataRootHierarchy.getElementHeader().getGUID(),
                             super.getNodeDisplayName(openMetadataRootHierarchy.getElementHeader(), openMetadataRootHierarchy.getProperties()),
                             super.getTypeNameForEntity(openMetadataRootHierarchy.getElementHeader()),
                             super.getVisualStyleForEntity(openMetadataRootHierarchy.getElementHeader(), principleStyle));

        if (parentNodeName != null)
        {
            appendMermaidLine(UUID.randomUUID().toString(),
                              parentNodeName,
                              this.addSpacesToTypeName(openMetadataRootHierarchy.getElementHeader().getType().getTypeName()),
                              openMetadataRootHierarchy.getElementHeader().getGUID());
        }

        addHierarchyMembers(openMetadataRootHierarchy.getElementHeader().getGUID(),
                            openMetadataRootHierarchy.getOpenMetadataRootHierarchies(),
                            principleStyle);
    }
}
