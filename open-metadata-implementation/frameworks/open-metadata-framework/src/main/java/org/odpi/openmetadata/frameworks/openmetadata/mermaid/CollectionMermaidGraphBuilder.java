/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworks.openmetadata.mermaid;

import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.CollectionHierarchy;

import java.util.List;
import java.util.UUID;


/**
 * Creates a mermaid graph rendering of the Open Metadata Framework's collection graph.
 */
public class CollectionMermaidGraphBuilder extends OpenMetadataRootMermaidGraphBuilder
{

    /**
     * Construct a mermaid markdown graph.
     *
     * @param collectionHierarchy content
     */
    public CollectionMermaidGraphBuilder(CollectionHierarchy collectionHierarchy)
    {
        super(collectionHierarchy);

        if (collectionHierarchy.getCollectionMemberGraphs() != null)
        {
            /*
             * Build a grid of elements in a subgraph
             */
            super.startSubgraph("Collection Membership", VisualStyle.DESCRIPTION, "LR");

            this.addCollectionMembers(null, collectionHierarchy.getCollectionMemberGraphs());

            super.endSubgraph();

            appendMermaidDottedLine(null, collectionHierarchy.getElementHeader().getGUID(), null, "Collection Membership");
        }
    }


    /**
     * Add the child members to the graph.
     *
     * @param parentNodeName id of the parent
     * @param collectionMemberGraphs list of child categories
     */
    private void addCollectionMembers(String                    parentNodeName,
                                      List<CollectionHierarchy> collectionMemberGraphs)
    {
        if (collectionMemberGraphs != null)
        {
            for (CollectionHierarchy collectionMemberGraph : collectionMemberGraphs)
            {
                addCollectionMemberGraph(parentNodeName, collectionMemberGraph);
            }
        }
    }


    /**
     * Add the child category to the graph.
     *
     * @param parentNodeName id of the parent
     * @param collectionMemberGraph child category
     */
    private void addCollectionMemberGraph(String              parentNodeName,
                                          CollectionHierarchy collectionMemberGraph)
    {

        appendNewMermaidNode(collectionMemberGraph.getElementHeader().getGUID(),
                             super.getNodeDisplayName(collectionMemberGraph.getElementHeader(), collectionMemberGraph.getProperties()),
                             super.getTypeNameForEntity(collectionMemberGraph.getElementHeader()),
                             super.getVisualStyleForEntity(collectionMemberGraph.getElementHeader(), VisualStyle.COLLECTION));

        if (parentNodeName != null)
        {
            appendMermaidLine(UUID.randomUUID().toString(),
                              parentNodeName,
                              this.addSpacesToTypeName(collectionMemberGraph.getElementHeader().getType().getTypeName()),
                              collectionMemberGraph.getElementHeader().getGUID());
        }

        addCollectionMembers(collectionMemberGraph.getElementHeader().getGUID(),
                             collectionMemberGraph.getCollectionMemberGraphs());
    }
}
