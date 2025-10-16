/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworks.openmetadata.mermaid;

import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.AssetGraph;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.RelatedMetadataNodeSummary;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.util.Arrays;

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
        String currentDisplayName = super.getNodeDisplayName(assetGraph);

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
                             assetGraph.getProperties(),
                             VisualStyle.ANCHOR_ELEMENT);

        if (assetGraph.getAnchoredElements() != null)
        {
            for (RelatedMetadataNodeSummary line : assetGraph.getRelationships())
            {
                if ((line != null) &&
                        (! propertyHelper.isTypeOf(line.getRelationshipHeader(), Arrays.asList(OpenMetadataType.DATA_FLOW_RELATIONSHIP.typeName,
                                                                       OpenMetadataType.CONTROL_FLOW_RELATIONSHIP.typeName,
                                                                       OpenMetadataType.DATA_SET_CONTENT_RELATIONSHIP.typeName,
                                                                       OpenMetadataType.PROCESS_CALL_RELATIONSHIP.typeName,
                                                                       OpenMetadataType.LINEAGE_MAPPING_RELATIONSHIP.typeName,
                                                                       OpenMetadataType.DATA_MAPPING_RELATIONSHIP.typeName,
                                                                       OpenMetadataType.ACTION_TARGET_RELATIONSHIP.typeName,
                                                                       OpenMetadataType.ACTION_TARGET_RELATIONSHIP.typeName,
                                                                       OpenMetadataType.TARGET_FOR_GOVERNANCE_ACTION_RELATIONSHIP.typeName,
                                                                       OpenMetadataType.TARGET_FOR_GOVERNANCE_ACTION_RELATIONSHIP.typeName,
                                                                       OpenMetadataType.IMPACTED_RESOURCE_RELATIONSHIP.typeName))))
                {
                    VisualStyle visualStyle = getVisualStyleForRelationship(line.getRelationshipHeader());

                    super.addRelatedNodeSummary(line, visualStyle);
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
