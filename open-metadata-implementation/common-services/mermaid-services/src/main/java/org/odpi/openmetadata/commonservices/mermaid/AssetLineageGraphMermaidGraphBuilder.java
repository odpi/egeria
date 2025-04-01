/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.commonservices.mermaid;

import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.*;

import java.util.*;

/**
 * Creates a mermaid graph rendering of the Open Metadata Framework's asset lineage graph.
 */
public class AssetLineageGraphMermaidGraphBuilder extends MermaidGraphBuilderBase
{
    private String edgeMermaidGraph = null;


    /**
     * Construct a mermaid markdown graph.
     *
     * @param assetLineageGraph content
     * @param highlightInformationSupplyChain optional information supply chain to highlight
     */
    public AssetLineageGraphMermaidGraphBuilder(AssetLineageGraph assetLineageGraph,
                                                String            highlightInformationSupplyChain)
    {
        final Set<String> end1Elements = new HashSet<>();
        final Set<String> end2Elements = new HashSet<>();

        mermaidGraph.append("---\n");
        mermaidGraph.append("title: Lineage Analysis Graph for Asset - ");
        mermaidGraph.append(assetLineageGraph.getProperties().getDisplayName());
        mermaidGraph.append(" [");
        mermaidGraph.append(assetLineageGraph.getElementHeader().getGUID());
        mermaidGraph.append("]\n---\nflowchart TD\n%%{init: {\"flowchart\": {\"htmlLabels\": false}} }%%\n\n");

        String currentNodeName    = assetLineageGraph.getElementHeader().getGUID();
        String currentDisplayName = assetLineageGraph.getProperties().getDisplayName();

        extractAnchorInfo(assetLineageGraph.getElementHeader());

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

                    extractAnchorInfo(node.getElementHeader());

                    appendNewMermaidNode(currentNodeName,
                                         currentDisplayName,
                                         node.getElementHeader().getType().getTypeName(),
                                         VisualStyle.LINEAGE_ELEMENT);
                }
            }

            for (AssetLineageGraphRelationship line : assetLineageGraph.getLineageRelationships())
            {
                if (line != null)
                {
                    end1Elements.add(line.getEnd1AssetGUID());
                    end2Elements.add(line.getEnd2AssetGUID());

                    if ((highlightInformationSupplyChain != null) && (line.getInformationSupplyChains() != null) && (! line.getInformationSupplyChains().contains(highlightInformationSupplyChain)))
                    {
                        super.appendMermaidThinLine(null,
                                                    line.getEnd1AssetGUID(),
                                                    this.getListLabel(line.getRelationshipTypes()),
                                                    line.getEnd2AssetGUID());
                    }
                    else
                    {
                        super.appendMermaidLine(null,
                                                line.getEnd1AssetGUID(),
                                                this.getListLabel(line.getRelationshipTypes()),
                                                line.getEnd2AssetGUID());
                    }
                }
            }

            Map<String, AssetLineageGraphNode> ultimateSources      = new HashMap<>();
            Map<String, AssetLineageGraphNode> ultimateDestinations = new HashMap<>();

            for (AssetLineageGraphNode node : assetLineageGraph.getLinkedAssets())
            {
                if (node != null)
                {
                    if (! end1Elements.contains(node.getElementHeader().getGUID()))
                    {
                        ultimateDestinations.put(node.getElementHeader().getGUID(), node);
                    }

                    if (! end2Elements.contains(node.getElementHeader().getGUID()))
                    {
                        ultimateSources.put(node.getElementHeader().getGUID(), node);
                    }
                }
            }

            if ((! ultimateSources.isEmpty()) || (! ultimateDestinations.isEmpty()))
            {
                AssetLineageEdgeGraphMermaidGraphBuilder edgeGraphBuilder = new AssetLineageEdgeGraphMermaidGraphBuilder(assetLineageGraph,
                                                                                                                         new ArrayList<>(ultimateSources.values()),
                                                                                                                         new ArrayList<>(ultimateDestinations.values()));

                edgeMermaidGraph = edgeGraphBuilder.getMermaidGraph();
            }
        }
    }


    /**
     * Return the pre-built mermaid graph.
     *
     * @return string markdown
     */
    public String getEdgeMermaidGraph()
    {
        return edgeMermaidGraph;
    }

}
