/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.commonservices.mermaid;

import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.CollectionGraph;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.CollectionMemberGraph;

import java.util.List;
import java.util.UUID;


/**
 * Creates a mermaid graph rendering of the Open Metadata Framework's collection graph.
 */
public class CollectionMermaidGraphBuilder extends MermaidGraphBuilderBase
{
    /**
     * Construct a mermaid markdown graph.
     *
     * @param collectionGraph content
     */
    public CollectionMermaidGraphBuilder(CollectionGraph collectionGraph)
    {
        mermaidGraph.append("---\n");
        mermaidGraph.append("title: Collection - ");
        mermaidGraph.append(collectionGraph.getProperties().getName());
        mermaidGraph.append(" [");
        mermaidGraph.append(collectionGraph.getElementHeader().getGUID());
        mermaidGraph.append("]\n---\nflowchart TD\n%%{init: {\"flowchart\": {\"htmlLabels\": false}} }%%\n\n");

        if (collectionGraph != null)
        {
            String currentDisplayName = collectionGraph.getProperties().getName();

            if (currentDisplayName == null)
            {
                currentDisplayName = collectionGraph.getProperties().getQualifiedName();
            }

            super.appendNewMermaidNode(collectionGraph.getElementHeader().getGUID(),
                                       currentDisplayName,
                                       collectionGraph.getElementHeader().getType().getTypeName(),
                                       getVisualStyleForEntity(collectionGraph.getElementHeader(), VisualStyle.COLLECTION));

            this.addDescription(collectionGraph);

            super.addClassifications(collectionGraph.getElementHeader());
            super.addRelatedElementSummaries(collectionGraph.getExternalReferences(), VisualStyle.EXTERNAL_REFERENCE, collectionGraph.getElementHeader().getGUID());
            super.addRelatedElementSummaries(collectionGraph.getOtherRelatedElements(), VisualStyle.LINKED_ELEMENT, collectionGraph.getElementHeader().getGUID());

            if (collectionGraph.getCollectionMemberGraphs() != null)
            {
                /*
                 * Build a grid of elements in a subgraph
                 */
                super.startSubgraph("Collection Membership", VisualStyle.DESCRIPTION, "LR");

                this.addCollectionMembers(null, collectionGraph.getCollectionMemberGraphs());

                super.endSubgraph();

                appendMermaidDottedLine(null, collectionGraph.getElementHeader().getGUID(), null, "Collection Membership");
            }
        }
    }


    /**
     * Add the child members to the graph.
     *
     * @param parentNodeName id of the parent
     * @param collectionMemberGraphs list of child categories
     */
    private void addCollectionMembers(String                      parentNodeName,
                                      List<CollectionMemberGraph> collectionMemberGraphs)
    {
        if (collectionMemberGraphs != null)
        {
            for (CollectionMemberGraph collectionMemberGraph : collectionMemberGraphs)
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
    private void addCollectionMemberGraph(String                parentNodeName,
                                          CollectionMemberGraph collectionMemberGraph)
    {

        appendNewMermaidNode(collectionMemberGraph.getElementHeader().getGUID(),
                             super.getNodeDisplayName(collectionMemberGraph.getElementHeader(), collectionMemberGraph.getProperties()),
                             super.getTypeNameForEntity(collectionMemberGraph.getElementHeader()),
                             super.getVisualStyleForEntity(collectionMemberGraph.getElementHeader(), VisualStyle.COLLECTION));

        if (parentNodeName != null)
        {
            appendMermaidLine(collectionMemberGraph.getRelationshipHeader().getGUID(),
                              parentNodeName,
                              this.addSpacesToTypeName(collectionMemberGraph.getElementHeader().getType().getTypeName()),
                              collectionMemberGraph.getElementHeader().getGUID());
        }

        addCollectionMembers(collectionMemberGraph.getElementHeader().getGUID(),
                             collectionMemberGraph.getNestedMembers());
    }



    /**
     * Add a text box with the description (if any)
     *
     * @param collectionGraph element with the potential description
     */
    private void addDescription(CollectionGraph collectionGraph)
    {
        if (collectionGraph.getProperties() != null)
        {
            if (collectionGraph.getProperties().getDescription() != null)
            {
                String descriptionNodeName = UUID.randomUUID().toString();

                appendNewMermaidNode(descriptionNodeName,
                                     collectionGraph.getProperties().getDescription(),
                                     "Description",
                                     VisualStyle.DESCRIPTION);

                appendInvisibleMermaidLine(descriptionNodeName, collectionGraph.getElementHeader().getGUID());
            }
        }
    }
}
