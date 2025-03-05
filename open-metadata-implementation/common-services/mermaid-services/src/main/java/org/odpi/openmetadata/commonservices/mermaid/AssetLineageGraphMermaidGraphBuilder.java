/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.commonservices.mermaid;

import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.*;

/**
 * Creates a mermaid graph rendering of the Open Metadata Framework's asset lineage graph.
 */
public class AssetLineageGraphMermaidGraphBuilder extends MermaidGraphBuilderBase
{
    /**
     * Construct a mermaid markdown graph.
     *
     * @param assetLineageGraph content
     * @param highlightInformationSupplyChain optional information supply chain to highlight
     */
    public AssetLineageGraphMermaidGraphBuilder(AssetLineageGraph assetLineageGraph,
                                                String            highlightInformationSupplyChain)
    {
        mermaidGraph.append("---\n");
        mermaidGraph.append("title: Lineage Graph for Asset - ");
        mermaidGraph.append(assetLineageGraph.getProperties().getDisplayName());
        mermaidGraph.append(" [");
        mermaidGraph.append(assetLineageGraph.getElementHeader().getGUID());
        mermaidGraph.append("]\n---\nflowchart TD\n%%{init: {\"flowchart\": {\"htmlLabels\": false}} }%%\n\n");

        String currentNodeName    = assetLineageGraph.getElementHeader().getGUID();
        String currentDisplayName = assetLineageGraph.getProperties().getDisplayName();

        appendNewMermaidNode(currentNodeName,
                             currentDisplayName,
                             assetLineageGraph.getElementHeader().getType().getTypeName(),
                             VisualStyle.PRINCIPAL_ASSET);

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

                    appendNewMermaidNode(currentNodeName,
                                         currentDisplayName,
                                         node.getElementHeader().getType().getTypeName(),
                                         VisualStyle.INFORMATION_SUPPLY_CHAIN_IMPL);
                }
            }

            for (AssetLineageGraphRelationship line : assetLineageGraph.getLineageRelationships())
            {
                if (line != null)
                {
                    if ((highlightInformationSupplyChain != null) && (line.getInformationSupplyChains() != null) && (! line.getInformationSupplyChains().contains(highlightInformationSupplyChain)))
                    {
                        super.appendMermaidThinLine(line.getEnd1AssetGUID(),
                                                    this.getListLabel(line.getRelationshipTypes()),
                                                    line.getEnd2AssetGUID());
                    }
                    else
                    {
                        super.appendMermaidLine(line.getEnd1AssetGUID(),
                                                this.getListLabel(line.getRelationshipTypes()),
                                                line.getEnd2AssetGUID());
                    }
                }
            }
        }
    }
}
