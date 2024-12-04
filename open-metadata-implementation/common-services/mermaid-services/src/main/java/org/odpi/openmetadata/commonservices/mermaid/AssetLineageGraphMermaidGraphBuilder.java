/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.commonservices.mermaid;

import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.*;

import java.util.ArrayList;
import java.util.List;


/**
 * Creates a mermaid graph rendering of the Open Metadata Framework's asset lineage graph.
 */
public class AssetLineageGraphMermaidGraphBuilder extends MermaidGraphBuilderBase
{
    /**
     * Construct a mermaid markdown graph.
     *
     * @param assetLineageGraph content
     */
    public AssetLineageGraphMermaidGraphBuilder(AssetLineageGraph assetLineageGraph)
    {
        mermaidGraph.append("---\n");
        mermaidGraph.append("title: Lineage Graph for Asset - ");
        mermaidGraph.append(assetLineageGraph.getProperties().getDisplayName());
        mermaidGraph.append(" [");
        mermaidGraph.append(assetLineageGraph.getElementHeader().getGUID());
        mermaidGraph.append("]\n---\nflowchart TD\n%%{init: {\"flowchart\": {\"htmlLabels\": false}} }%%\n\n");

        List<String> usedNodeNames = new ArrayList<>();

        String currentNodeName    = assetLineageGraph.getElementHeader().getGUID();
        String currentDisplayName = assetLineageGraph.getProperties().getDisplayName();

        appendMermaidNode(mermaidGraph,
                          currentNodeName,
                          currentDisplayName,
                          assetLineageGraph.getElementHeader().getType().getTypeName());

        usedNodeNames.add(currentNodeName);


        if (assetLineageGraph.getLinkedAssets() != null)
        {
            for (AssetLineageGraphNode node : assetLineageGraph.getLinkedAssets())
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

                    if (!usedNodeNames.contains(currentNodeName))
                    {
                        appendMermaidNode(mermaidGraph,
                                          currentNodeName,
                                          currentDisplayName,
                                          node.getElementHeader().getType().getTypeName());

                        usedNodeNames.add(currentNodeName);
                    }
                }
            }

            for (AssetLineageGraphRelationship line : assetLineageGraph.getLineageRelationships())
            {
                if (line != null)
                {
                    mermaidGraph.append(line.getEnd1AssetGUID());
                    mermaidGraph.append("-->|");
                    mermaidGraph.append(this.getListLabel(line.getRelationshipTypes()));
                    mermaidGraph.append("|");
                    mermaidGraph.append(line.getEnd2AssetGUID());
                    mermaidGraph.append("\n");
                }
            }
        }
    }
}
