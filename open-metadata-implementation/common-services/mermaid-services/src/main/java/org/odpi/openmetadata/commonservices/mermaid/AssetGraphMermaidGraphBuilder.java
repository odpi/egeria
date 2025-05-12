/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.commonservices.mermaid;

import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.*;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;

public class AssetGraphMermaidGraphBuilder extends MermaidGraphBuilderBase
{
    private final String informationSupplyChainMermaidGraph;
    private final String fieldLevelLineageGraph;

    /**
     * Constructor for the graph
     *
     * @param assetGraph contents
     */
    public AssetGraphMermaidGraphBuilder(AssetGraph assetGraph)
    {
        mermaidGraph.append("---\n");
        mermaidGraph.append("title: Asset - ");
        mermaidGraph.append(assetGraph.getProperties().getDisplayName());
        mermaidGraph.append(" [");
        mermaidGraph.append(assetGraph.getElementHeader().getGUID());
        mermaidGraph.append("]\n---\nflowchart LR\n%%{init: {\"flowchart\": {\"htmlLabels\": false}} }%%\n\n");

        if ((assetGraph.getElementHeader().getClassifications() != null) || (! assetGraph.getElementHeader().getClassifications().isEmpty()))
        {
            super.startSubgraph("Classifications", VisualStyle.DESCRIPTION, "TB");

            for (ElementClassification classification : assetGraph.getElementHeader().getClassifications())
            {
                if (classification != null)
                {
                    StringBuilder stringBuilder = new StringBuilder();
                    if (classification.getClassificationProperties() != null)
                    {
                        for (String propertyName : classification.getClassificationProperties().keySet())
                        {
                            stringBuilder.append(propertyName);

                            Object propertyValue = classification.getClassificationProperties().get(propertyName);
                            if (propertyValue != null)
                            {
                                stringBuilder.append("\n");
                                stringBuilder.append(" - ");
                                stringBuilder.append(propertyValue);
                                stringBuilder.append("\n");
                            }
                        }
                    }

                    appendNewMermaidNode(assetGraph.getElementHeader().getGUID() + ":" + classification.getClassificationName(),
                                         stringBuilder.toString(),
                                         classification.getClassificationName(),
                                         VisualStyle.ANCHOR_ELEMENT);
                }
            }

            endSubgraph();
        }

        String currentNodeName    = assetGraph.getElementHeader().getGUID();
        String currentDisplayName = assetGraph.getProperties().getDisplayName();

        appendNewMermaidNode(currentNodeName,
                             currentDisplayName,
                             assetGraph.getElementHeader().getType().getTypeName(),
                             checkForClassifications(assetGraph.getElementHeader(), VisualStyle.ANCHOR_ELEMENT));

        if (assetGraph.getAnchoredElements() != null)
        {
            for (MetadataElementSummary node : assetGraph.getAnchoredElements())
            {
                if (node != null)
                {
                    currentNodeName    = node.getElementHeader().getGUID();
                    currentDisplayName = node.getProperties().get(OpenMetadataProperty.DISPLAY_NAME.name);
                    if (currentDisplayName == null)
                    {
                        currentDisplayName = node.getProperties().get(OpenMetadataProperty.NAME.name);
                    }
                    if (currentDisplayName == null)
                    {
                        currentDisplayName = node.getProperties().get(OpenMetadataProperty.RESOURCE_NAME.name);
                    }
                    if (currentDisplayName == null)
                    {
                        currentDisplayName = node.getProperties().get(OpenMetadataProperty.QUALIFIED_NAME.name);
                    }

                    appendNewMermaidNode(currentNodeName,
                                         currentDisplayName,
                                         node.getElementHeader().getType().getTypeName(),
                                         checkForClassifications(node.getElementHeader(), VisualStyle.ANCHORED_ELEMENT));
                }
            }

            for (MetadataRelationship line : assetGraph.getRelationships())
            {
                if (line != null)
                {
                    VisualStyle visualStyle = getVisualStyleForRelationship(line);

                    String endName = line.getEnd1().getGUID();
                    if (line.getEnd1().getUniqueName() != null)
                    {
                        endName = line.getEnd1().getUniqueName();
                    }

                    appendNewMermaidNode(line.getEnd1().getGUID(),
                                         endName,
                                         line.getEnd1().getType().getTypeName(),
                                         checkForClassifications(line.getEnd1(), visualStyle));

                    endName = line.getEnd2().getGUID();
                    if (line.getEnd2().getUniqueName() != null)
                    {
                        endName = line.getEnd2().getUniqueName();
                    }

                    appendNewMermaidNode(line.getEnd2().getGUID(),
                                         endName,
                                         line.getEnd2().getType().getTypeName(),
                                         checkForClassifications(line.getEnd2(),visualStyle));

                    super.appendMermaidLine(line.getGUID(),
                                            line.getEnd1().getGUID(),
                                            super.addSpacesToTypeName(line.getType().getTypeName()),
                                            line.getEnd2().getGUID());
                }
            }
        }

        AssetISCGraphMermaidGraphBuilder iscGraphMermaidGraphBuilder = new AssetISCGraphMermaidGraphBuilder(assetGraph);

        informationSupplyChainMermaidGraph = iscGraphMermaidGraphBuilder.getMermaidGraph();

        FieldLevelLineageMermaidGraphBuilder fieldLevelLineageMermaidGraphBuilder = new FieldLevelLineageMermaidGraphBuilder(assetGraph);

        fieldLevelLineageGraph = fieldLevelLineageMermaidGraphBuilder.getMermaidGraph(true);
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
}
