/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.commonservices.mermaid;

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
        mermaidGraph.append("---\n");
        mermaidGraph.append("title: Information Supply Chain Analysis Graph for Asset - ");
        mermaidGraph.append(assetGraph.getProperties().getDisplayName());
        mermaidGraph.append(" [");
        mermaidGraph.append(assetGraph.getElementHeader().getGUID());
        mermaidGraph.append("]\n---\nflowchart TD\n%%{init: {\"flowchart\": {\"htmlLabels\": false}} }%%\n\n");

        String centreNodeName    = assetGraph.getElementHeader().getGUID();
        String centreDisplayName = assetGraph.getProperties().getDisplayName();

        appendNewMermaidNode(centreNodeName,
                             centreDisplayName,
                             assetGraph.getElementHeader().getType().getTypeName(),
                             VisualStyle.ANCHOR_ELEMENT);

        String currentNodeName;
        String currentDisplayName;

        if (assetGraph.getInformationSupplyChains() != null)
        {
            for (MetadataElementSummary node : assetGraph.getInformationSupplyChains())
            {
                if (node != null)
                {
                    currentNodeName = node.getElementHeader().getGUID();
                    currentDisplayName   = node.getProperties().get(OpenMetadataProperty.DISPLAY_NAME.name);
                    if (currentDisplayName == null)
                    {
                        currentDisplayName = node.getProperties().get(OpenMetadataProperty.QUALIFIED_NAME.name);
                    }

                    appendNewMermaidNode(currentNodeName,
                                         currentDisplayName,
                                         node.getElementHeader().getType().getTypeName(),
                                         VisualStyle.INFORMATION_SUPPLY_CHAIN);

                    super.appendMermaidLine(null,
                                            currentNodeName,
                                            null,
                                            centreNodeName);
                }
            }
        }
        else
        {
            super.clearGraph();
        }
    }
}
