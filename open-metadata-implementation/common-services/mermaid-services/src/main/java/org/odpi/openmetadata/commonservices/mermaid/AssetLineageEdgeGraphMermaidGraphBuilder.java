/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.commonservices.mermaid;

import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.AssetLineageGraph;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.AssetLineageGraphNode;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.AssetLineageGraphRelationship;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        mermaidGraph.append("---\n");
        mermaidGraph.append("title: Lineage Edge Analysis Graph for Asset - ");
        mermaidGraph.append(assetLineageGraph.getProperties().getDisplayName());
        mermaidGraph.append(" [");
        mermaidGraph.append(assetLineageGraph.getElementHeader().getGUID());
        mermaidGraph.append("]\n---\nflowchart LR\n%%{init: {\"flowchart\": {\"htmlLabels\": false}} }%%\n\n");

        String centreNodeName    = assetLineageGraph.getElementHeader().getGUID();
        String centreDisplayName = assetLineageGraph.getProperties().getDisplayName();

        appendNewMermaidNode(centreNodeName,
                             centreDisplayName,
                             assetLineageGraph.getElementHeader().getType().getTypeName(),
                             VisualStyle.PRINCIPAL_ASSET);

        String currentNodeName;
        String currentDisplayName;

        if (ultimateSources != null)
        {
            for (AssetLineageGraphNode node : ultimateSources)
            {
                if (node != null)
                {
                    currentNodeName = node.getElementHeader().getGUID();
                    currentDisplayName   = node.getProperties().getDisplayName();
                    if (currentDisplayName == null)
                    {
                        currentDisplayName = node.getProperties().getName();
                    }
                    if (currentDisplayName == null)
                    {
                        currentDisplayName = node.getProperties().getResourceName();
                    }
                    if (currentDisplayName == null)
                    {
                        currentDisplayName = node.getProperties().getQualifiedName();
                    }

                    appendNewMermaidNode(currentNodeName,
                                         currentDisplayName,
                                         node.getElementHeader().getType().getTypeName(),
                                         VisualStyle.LINKED_ELEMENT);

                    super.appendMermaidLine(currentNodeName,
                                            OpenMetadataType.ULTIMATE_SOURCE.typeName,
                                            centreNodeName);
                }
            }
        }

        if (ultimateDestinations != null)
        {
            for (AssetLineageGraphNode node : ultimateDestinations)
            {
                if (node != null)
                {
                    currentNodeName = node.getElementHeader().getGUID();
                    currentDisplayName   = node.getProperties().getDisplayName();
                    if (currentDisplayName == null)
                    {
                        currentDisplayName = node.getProperties().getName();
                    }
                    if (currentDisplayName == null)
                    {
                        currentDisplayName = node.getProperties().getResourceName();
                    }
                    if (currentDisplayName == null)
                    {
                        currentDisplayName = node.getProperties().getQualifiedName();
                    }

                    appendNewMermaidNode(currentNodeName,
                                         currentDisplayName,
                                         node.getElementHeader().getType().getTypeName(),
                                         VisualStyle.LINKED_ELEMENT);

                    super.appendMermaidLine(centreNodeName,
                                            OpenMetadataType.ULTIMATE_DESTINATION.typeName,
                                            currentNodeName);
                }
            }
        }
    }
}
