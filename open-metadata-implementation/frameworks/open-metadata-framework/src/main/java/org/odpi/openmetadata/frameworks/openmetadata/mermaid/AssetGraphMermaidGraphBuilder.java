/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworks.openmetadata.mermaid;

import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.*;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class AssetGraphMermaidGraphBuilder extends MermaidGraphBuilderBase
{
    private final String informationSupplyChainMermaidGraph;
    private final String fieldLevelLineageGraph;
    private final String localLineageGraph;
    private final String actionGraph;

    /**
     * Constructor for the graph
     *
     * @param assetGraph contents
     */
    public AssetGraphMermaidGraphBuilder(AssetGraph assetGraph)
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
        mermaidGraph.append("title: Asset - ");
        mermaidGraph.append(currentDisplayName);
        mermaidGraph.append(" [");
        mermaidGraph.append(assetGraph.getElementHeader().getGUID());
        mermaidGraph.append("]\n---\nflowchart LR\n%%{init: {\"flowchart\": {\"htmlLabels\": false}} }%%\n\n");

        super.addClassifications(assetGraph.getElementHeader());


        appendNewMermaidNode(assetGraph.getElementHeader().getGUID(),
                             currentDisplayName,
                             assetGraph.getElementHeader().getType().getTypeName(),
                             VisualStyle.ANCHOR_ELEMENT);

        if (assetGraph.getAnchoredElements() != null)
        {
            Map<String, MetadataElementSummary> nodeMap = new HashMap<>();

            for (MetadataElementSummary node : assetGraph.getAnchoredElements())
            {
                if (node != null)
                {
                    nodeMap.put(node.getElementHeader().getGUID(), node);
                }
            }

            for (MetadataRelationship line : assetGraph.getRelationships())
            {
                if ((line != null) &&
                        (! propertyHelper.isTypeOf(line, Arrays.asList(OpenMetadataType.DATA_FLOW_RELATIONSHIP.typeName,
                                                                       OpenMetadataType.CONTROL_FLOW_RELATIONSHIP.typeName,
                                                                       OpenMetadataType.DATA_SET_CONTENT_RELATIONSHIP.typeName,
                                                                       OpenMetadataType.PROCESS_CALL_RELATIONSHIP.typeName,
                                                                       OpenMetadataType.LINEAGE_MAPPING_RELATIONSHIP.typeName,
                                                                       OpenMetadataType.DATA_MAPPING_RELATIONSHIP.typeName,
                                                                       OpenMetadataType.ACTION_TARGET_RELATIONSHIP.typeName,
                                                                       OpenMetadataType.TARGET_FOR_ACTION_RELATIONSHIP.typeName,
                                                                       OpenMetadataType.TARGET_FOR_ACTION_TYPE_RELATIONSHIP.typeName,
                                                                       OpenMetadataType.TARGET_FOR_ACTION_PROCESS_RELATIONSHIP.typeName,
                                                                       OpenMetadataType.IMPACTED_RESOURCE_RELATIONSHIP.typeName))))
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

                    super.appendMermaidLine(line.getGUID(),
                                            line.getEnd1().getGUID(),
                                            super.addSpacesToTypeName(line.getType().getTypeName()),
                                            line.getEnd2().getGUID());
                }
            }
        }

        AssetISCGraphMermaidGraphBuilder iscGraphMermaidGraphBuilder = new AssetISCGraphMermaidGraphBuilder(assetGraph);

        informationSupplyChainMermaidGraph = iscGraphMermaidGraphBuilder.getMermaidGraph();

        AssetFieldLevelLineageMermaidGraphBuilder fieldLevelLineageMermaidGraphBuilder = new AssetFieldLevelLineageMermaidGraphBuilder(assetGraph);

        fieldLevelLineageGraph = fieldLevelLineageMermaidGraphBuilder.getMermaidGraph(true);

        AssetLocalLineageGraphMermaidGraphBuilder localLineageGraphMermaidGraphBuilder = new AssetLocalLineageGraphMermaidGraphBuilder(assetGraph);

        localLineageGraph = localLineageGraphMermaidGraphBuilder.getMermaidGraph();

        AssetActionGraphMermaidGraphBuilder actionGraphMermaidGraphBuilder = new AssetActionGraphMermaidGraphBuilder(assetGraph);

        actionGraph = actionGraphMermaidGraphBuilder.getMermaidGraph();
    }


    /**
     * Return the information supply chain graph.
     *
     * @return mermaid graph
     */
    public String getInformationSupplyChainMermaidGraph()
    {
        return informationSupplyChainMermaidGraph;
    }


    /**
     * Return the field level lineage graph.
     *
     * @return mermaid graph
     */
    public String getFieldLevelLineageGraph()
    {
        return fieldLevelLineageGraph;
    }


    /**
     * Return the graph of the local lineage relationships.
     *
     * @return mermaid graph
     */
    public String getLocalLineageGraph()
    {
        return localLineageGraph;
    }

    /**
     * Return the graph showing the governance actions on the asset.
     *
     * @return  mermaid graph
     */
    public String getActionGraph()
    {
        return actionGraph;
    }
}
