/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworks.openmetadata.mermaid;

import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.AssetGraph;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.MetadataElementSummary;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.RelatedMetadataNodeSummary;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Creates a mermaid graph of the part of an asset graph that relates to actions - ie
 */
public class AssetActionGraphMermaidGraphBuilder extends MermaidGraphBuilderBase
{
    /**
     * Constructor for the graph
     *
     * @param assetGraph contents
     */
    public AssetActionGraphMermaidGraphBuilder(AssetGraph assetGraph)
    {
        String currentDisplayName = super.getNodeDisplayName(assetGraph);

        mermaidGraph.append("---\n");
        mermaidGraph.append("title: Actions associated with Asset - ");
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
                        (propertyHelper.isTypeOf(line.getRelationshipHeader(), Arrays.asList(OpenMetadataType.ACTION_TARGET_RELATIONSHIP.typeName,
                                                                     OpenMetadataType.ACTION_TARGET_RELATIONSHIP.typeName,
                                                                     OpenMetadataType.TARGET_FOR_GOVERNANCE_ACTION_RELATIONSHIP.typeName,
                                                                     OpenMetadataType.TARGET_FOR_GOVERNANCE_ACTION_RELATIONSHIP.typeName,
                                                                     OpenMetadataType.IMPACTED_RESOURCE_RELATIONSHIP.typeName))))
                {
                    VisualStyle visualStyle = getVisualStyleForRelationship(line.getRelationshipHeader());

                    super.addRelatedNodeSummary(line, getVisualStyleForEntity(line.getRelationshipHeader(), visualStyle));
                }
            }
        }
        else
        {
            super.clearGraph();
        }
    }
}
