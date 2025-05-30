/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworks.openmetadata.mermaid;

import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.AssetGraph;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.AssetLineageGraph;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.AssetLineageGraphNode;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.MetadataElementSummary;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.util.List;

/**
 * Creates a mermaid graph rendering of the Open Metadata Framework's asset lineage edge graph.
 */
public class AssetISCGraphMermaidGraphBuilder extends MermaidGraphBuilderBase
{
    /**
     * Construct a mermaid markdown graph.
     *
     * @param assetGraph content
     */
    public AssetISCGraphMermaidGraphBuilder(AssetGraph assetGraph)
    {
        String currentDisplayName = assetGraph.getProperties().getDisplayName();

        if (currentDisplayName == null)
        {
            currentDisplayName = assetGraph.getProperties().getName();
        }

        if (currentDisplayName == null)
        {
            currentDisplayName = assetGraph.getProperties().getQualifiedName();
        }

        mermaidGraph.append("---\n");
        mermaidGraph.append("title: Information Supply Chain Analysis Graph for Asset - ");
        mermaidGraph.append(currentDisplayName);
        mermaidGraph.append(" [");
        mermaidGraph.append(assetGraph.getElementHeader().getGUID());
        mermaidGraph.append("]\n---\nflowchart TD\n%%{init: {\"flowchart\": {\"htmlLabels\": false}} }%%\n\n");

        appendNewMermaidNode(assetGraph.getElementHeader().getGUID(),
                             currentDisplayName,
                             assetGraph.getElementHeader().getType().getTypeName(),
                             VisualStyle.ANCHOR_ELEMENT);

        if (assetGraph.getInformationSupplyChains() != null)
        {
            for (MetadataElementSummary node : assetGraph.getInformationSupplyChains())
            {
                if (node != null)
                {
                    appendNewMermaidNode(node.getElementHeader().getGUID(),
                                         super.getNodeDisplayName(node),
                                         node.getElementHeader().getType().getTypeName(),
                                         VisualStyle.INFORMATION_SUPPLY_CHAIN);

                    super.appendMermaidLine(null,
                                            node.getElementHeader().getGUID(),
                                            null,
                                            assetGraph.getElementHeader().getGUID());
                }
            }
        }
        else
        {
            super.clearGraph();
        }
    }
}
