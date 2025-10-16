/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworks.openmetadata.mermaid;

import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.AssetGraph;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.MetadataElementSummary;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.MetadataRelationship;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.RelatedMetadataNodeSummary;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Creates a mermaid graph of the part of an asset graph that relates to locally connected
 * lineage relationships
 */
public class AssetLocalLineageGraphMermaidGraphBuilder extends MermaidGraphBuilderBase
{
    /**
     * Constructor for the graph
     *
     * @param assetGraph contents
     */
    public AssetLocalLineageGraphMermaidGraphBuilder(AssetGraph assetGraph)
    {
        String currentDisplayName = super.getNodeDisplayName(assetGraph);

        mermaidGraph.append("---\n");
        mermaidGraph.append("title: Lineage associated with Asset - ");
        mermaidGraph.append(currentDisplayName);
        mermaidGraph.append(" [");
        mermaidGraph.append(assetGraph.getElementHeader().getGUID());
        mermaidGraph.append("]\n---\nflowchart LR\n%%{init: {\"flowchart\": {\"htmlLabels\": false}} }%%\n\n");

        appendNewMermaidNode(assetGraph.getElementHeader().getGUID(),
                             currentDisplayName,
                             assetGraph.getElementHeader().getType().getTypeName(),
                             assetGraph.getProperties(),
                             VisualStyle.ANCHOR_ELEMENT);

        if (assetGraph.getRelationships() != null)
        {
            Map<String, MetadataElementSummary> nodeMap = new HashMap<>();

            if (assetGraph.getAnchoredElements() != null)
            {
                for (MetadataElementSummary node : assetGraph.getAnchoredElements())
                {
                    if (node != null)
                    {
                        nodeMap.put(node.getElementHeader().getGUID(), node);
                    }
                }
            }

            for (RelatedMetadataNodeSummary line : assetGraph.getRelationships())
            {
                if ((line != null) &&
                        (propertyHelper.isTypeOf(line.getRelationshipHeader(), Arrays.asList(OpenMetadataType.DATA_FLOW_RELATIONSHIP.typeName,
                                                                     OpenMetadataType.CONTROL_FLOW_RELATIONSHIP.typeName,
                                                                     OpenMetadataType.DATA_SET_CONTENT_RELATIONSHIP.typeName,
                                                                     OpenMetadataType.PROCESS_CALL_RELATIONSHIP.typeName,
                                                                     OpenMetadataType.LINEAGE_MAPPING_RELATIONSHIP.typeName))))
                {
                    VisualStyle visualStyle = getVisualStyleForRelationship(line.getRelationshipHeader());

                    super.addRelatedNodeSummary(line, visualStyle, LineStyle.ANIMATED_LONG);
                }
            }
        }
        else
        {
            super.clearGraph();
        }
    }
}
