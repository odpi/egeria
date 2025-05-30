/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworks.openmetadata.mermaid;

import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.AssetGraph;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.MetadataElementSummary;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.MetadataRelationship;
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
        mermaidGraph.append("title: Lineage associated with Asset - ");
        mermaidGraph.append(currentDisplayName);
        mermaidGraph.append(" [");
        mermaidGraph.append(assetGraph.getElementHeader().getGUID());
        mermaidGraph.append("]\n---\nflowchart LR\n%%{init: {\"flowchart\": {\"htmlLabels\": false}} }%%\n\n");

        super.addClassifications(assetGraph.getElementHeader());

        appendNewMermaidNode(assetGraph.getElementHeader().getGUID(),
                             currentDisplayName,
                             assetGraph.getElementHeader().getType().getTypeName(),
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

            for (MetadataRelationship line : assetGraph.getRelationships())
            {
                if ((line != null) &&
                        (propertyHelper.isTypeOf(line, Arrays.asList(OpenMetadataType.DATA_FLOW_RELATIONSHIP.typeName,
                                                                     OpenMetadataType.CONTROL_FLOW_RELATIONSHIP.typeName,
                                                                     OpenMetadataType.DATA_SET_CONTENT_RELATIONSHIP.typeName,
                                                                     OpenMetadataType.PROCESS_CALL_RELATIONSHIP.typeName,
                                                                     OpenMetadataType.LINEAGE_MAPPING_RELATIONSHIP.typeName))))
                {
                    VisualStyle visualStyle = getVisualStyleForRelationship(line);

                    String endName = line.getEnd1().getGUID();
                    if (line.getEnd1().getUniqueName() != null)
                    {
                        endName = line.getEnd1().getUniqueName();
                    }

                    if (nodeMap.get(line.getEnd1().getGUID()) != null)
                    {
                        MetadataElementSummary node = nodeMap.get(line.getEnd1().getGUID());

                        appendNewMermaidNode(node.getElementHeader().getGUID(),
                                             super.getNodeDisplayName(node),
                                             node.getElementHeader().getType().getTypeName(),
                                             getVisualStyleForEntity(node.getElementHeader(), visualStyle));
                    }
                    else
                    {
                        appendNewMermaidNode(line.getEnd1().getGUID(),
                                             endName,
                                             line.getEnd1().getType().getTypeName(),
                                             getVisualStyleForEntity(line.getEnd1(), visualStyle));
                    }

                    endName = line.getEnd2().getGUID();
                    if (line.getEnd2().getUniqueName() != null)
                    {
                        endName = line.getEnd2().getUniqueName();
                    }

                    if (nodeMap.get(line.getEnd2().getGUID()) != null)
                    {
                        MetadataElementSummary node = nodeMap.get(line.getEnd2().getGUID());

                        appendNewMermaidNode(node.getElementHeader().getGUID(),
                                             super.getNodeDisplayName(node),
                                             node.getElementHeader().getType().getTypeName(),
                                             getVisualStyleForEntity(node.getElementHeader(), visualStyle));
                    }
                    else
                    {
                        appendNewMermaidNode(line.getEnd2().getGUID(),
                                             endName,
                                             line.getEnd2().getType().getTypeName(),
                                             getVisualStyleForEntity(line.getEnd2(), visualStyle));
                    }

                    super.appendMermaidLongAnimatedLine(line.getGUID(),
                                                        line.getEnd1().getGUID(),
                                                        super.addSpacesToTypeName(line.getType().getTypeName()),
                                                        line.getEnd2().getGUID());
                }
            }
        }
        else
        {
            super.clearGraph();
        }
    }
}
