/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworks.openmetadata.mermaid;

import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.AssetLineageGraph;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.AssetLineageGraphNode;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.util.List;

/**
 * Creates a mermaid graph rendering of the Open Metadata Framework's asset lineage edge graph.
 */
public class AssetLineageEdgeGraphMermaidGraphBuilder extends MermaidGraphBuilderBase
{
    /**
     * Construct a mermaid markdown graph.
     *
     * @param assetLineageGraph content
     * @param ultimateSources list of edge elements feeding the asset
     * @param ultimateDestinations list of edge elements receiving data from the asset
     */
    public AssetLineageEdgeGraphMermaidGraphBuilder(AssetLineageGraph           assetLineageGraph,
                                                    List<AssetLineageGraphNode> ultimateSources,
                                                    List<AssetLineageGraphNode> ultimateDestinations)
    {
        String currentDisplayName = super.getNodeDisplayName(assetLineageGraph);;

        mermaidGraph.append("---\n");
        mermaidGraph.append("title: Lineage Edge Analysis Graph for Asset - ");
        mermaidGraph.append(currentDisplayName);
        mermaidGraph.append(" [");
        mermaidGraph.append(assetLineageGraph.getElementHeader().getGUID());
        mermaidGraph.append("]\n---\nflowchart LR\n%%{init: {\"flowchart\": {\"htmlLabels\": false}} }%%\n\n");


        appendNewMermaidNode(assetLineageGraph.getElementHeader().getGUID(),
                             currentDisplayName,
                             assetLineageGraph.getElementHeader().getType().getTypeName(),
                             getVisualStyleForEntity(assetLineageGraph.getElementHeader(), VisualStyle.PRINCIPAL_ASSET));

        if (ultimateSources != null)
        {
            for (AssetLineageGraphNode node : ultimateSources)
            {
                if (node != null)
                {
                    currentDisplayName = super.getNodeDisplayName(node);

                    appendNewMermaidNode(node.getElementHeader().getGUID(),
                                         currentDisplayName,
                                         node.getElementHeader().getType().getTypeName(),
                                         getVisualStyleForEntity(node.getElementHeader(), VisualStyle.LINKED_ELEMENT));

                    super.appendMermaidLine(null,
                                            node.getElementHeader().getGUID(),
                                            OpenMetadataType.ULTIMATE_SOURCE.typeName,
                                            assetLineageGraph.getElementHeader().getGUID());
                }
            }
        }

        if (ultimateDestinations != null)
        {
            for (AssetLineageGraphNode node : ultimateDestinations)
            {
                if (node != null)
                {
                    currentDisplayName   = super.getNodeDisplayName(node);

                    appendNewMermaidNode(node.getElementHeader().getGUID(),
                                         currentDisplayName,
                                         node.getElementHeader().getType().getTypeName(),
                                         getVisualStyleForEntity(node.getElementHeader(), VisualStyle.LINKED_ELEMENT));

                    super.appendMermaidLine(null,
                                            assetLineageGraph.getElementHeader().getGUID(),
                                            OpenMetadataType.ULTIMATE_DESTINATION.typeName,
                                            node.getElementHeader().getGUID());
                }
            }
        }
    }
}
