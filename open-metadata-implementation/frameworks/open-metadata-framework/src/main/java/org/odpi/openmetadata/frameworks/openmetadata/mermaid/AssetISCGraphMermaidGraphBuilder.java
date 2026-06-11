/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworks.openmetadata.mermaid;

import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.AssetGraph;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.MetadataElementSummary;
import org.odpi.openmetadata.frameworks.openmetadata.properties.informationsupplychains.InformationSupplyChainProperties;

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
        String currentDisplayName = super.getNodeDisplayName(assetGraph);;

        mermaidGraph.append("---\n");
        mermaidGraph.append("title: Information Supply Chain Analysis Graph for Asset - ");
        mermaidGraph.append(super.removeTroublesomeCharacters(currentDisplayName));
        mermaidGraph.append(" [");
        mermaidGraph.append(assetGraph.getElementHeader().getGUID());
        mermaidGraph.append("]\n---\nflowchart TD\n%%{init: {\"flowchart\": {\"htmlLabels\": false}} }%%\n\n");

        appendNewMermaidNode(assetGraph.getElementHeader().getGUID(),
                             currentDisplayName,
                             assetGraph.getElementHeader().getType().getTypeName(),
                             assetGraph.getProperties(),
                             VisualStyle.ANCHOR_ELEMENT);

        if (assetGraph.getPartOfInformationSupplyChains() != null)
        {
            for (MetadataElementSummary node : assetGraph.getPartOfInformationSupplyChains())
            {
                if ((node != null) && (node.getProperties() instanceof InformationSupplyChainProperties))
                {
                    appendNewMermaidNode(node.getElementHeader().getGUID(),
                                         super.getNodeDisplayName(node),
                                         node.getElementHeader().getType().getTypeName(),
                                         node.getProperties(),
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
