/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworks.openmetadata.mermaid;

import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.AssetGraph;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.MetadataElementSummary;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.MetadataRelationship;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Creates a mermaid graph rendering of the Open Metadata Framework's asset field level lineage graph.
 */
public class AssetFieldLevelLineageMermaidGraphBuilder extends MermaidGraphBuilderBase
{
    /**
     * Construct a mermaid markdown graph.
     *
     * @param assetGraph content
     */
    public AssetFieldLevelLineageMermaidGraphBuilder(AssetGraph assetGraph)
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
        mermaidGraph.append("title: Field-Level Lineage Analysis Graph for Asset - ");
        mermaidGraph.append(currentDisplayName);
        mermaidGraph.append(" [");
        mermaidGraph.append(assetGraph.getElementHeader().getGUID());
        mermaidGraph.append("]\n---\nflowchart TD\n%%{init: {\"flowchart\": {\"htmlLabels\": false}} }%%\n\n");

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

            List<MetadataRelationship> dataMappingRelationships = new ArrayList<>();

            for (MetadataRelationship relationship : assetGraph.getRelationships())
            {
                if ((relationship != null) &&
                        (propertyHelper.isTypeOf(relationship,
                                                 OpenMetadataType.DATA_MAPPING_RELATIONSHIP.typeName)))
                {
                    extractAnchorInfo(relationship.getEnd1());
                    extractAnchorInfo(relationship.getEnd2());
                    dataMappingRelationships.add(relationship);
                }
            }

            if (! dataMappingRelationships.isEmpty())
            {
                for (MetadataRelationship line : dataMappingRelationships)
                {
                    String end1DisplayName   = line.getEnd1().getUniqueName();
                    if (end1DisplayName == null)
                    {
                        end1DisplayName = line.getEnd1().getGUID();
                    }

                    if (! assetGraph.getElementHeader().getGUID().equals(line.getEnd1().getGUID()))
                    {
                        if (nodeMap.get(line.getEnd1().getGUID()) != null)
                        {
                            MetadataElementSummary node = nodeMap.get(line.getEnd1().getGUID());

                            appendNewMermaidNode(node.getElementHeader().getGUID(),
                                                 super.getNodeDisplayName(node),
                                                 node.getElementHeader().getType().getTypeName(),
                                                 getVisualStyleForEntity(node.getElementHeader(), VisualStyle.SCHEMA_ELEMENT));
                        }
                        else
                        {
                            appendNewMermaidNode(line.getEnd1().getGUID(),
                                                 end1DisplayName,
                                                 line.getEnd1().getType().getTypeName(),
                                                 getVisualStyleForEntity(line.getEnd1(), VisualStyle.SCHEMA_ELEMENT));
                        }
                    }

                    String end2DisplayName   = line.getEnd2().getUniqueName();
                    if (end2DisplayName == null)
                    {
                        end2DisplayName = line.getEnd2().getGUID();
                    }

                    if (! assetGraph.getElementHeader().getGUID().equals(line.getEnd2().getGUID()))
                    {
                        if (nodeMap.get(line.getEnd2().getGUID()) != null)
                        {
                            MetadataElementSummary node = nodeMap.get(line.getEnd2().getGUID());

                            appendNewMermaidNode(node.getElementHeader().getGUID(),
                                                 super.getNodeDisplayName(node),
                                                 node.getElementHeader().getType().getTypeName(),
                                                 getVisualStyleForEntity(node.getElementHeader(), VisualStyle.SCHEMA_ELEMENT));
                        }
                        else
                        {
                            appendNewMermaidNode(line.getEnd2().getGUID(),
                                                 end2DisplayName,
                                                 line.getEnd2().getType().getTypeName(),
                                                 getVisualStyleForEntity(line.getEnd2(), VisualStyle.SCHEMA_ELEMENT));
                        }
                    }

                    if ((line.getProperties() != null) && (line.getProperties().get(OpenMetadataProperty.LABEL.name) != null))
                    {
                        super.appendMermaidLongAnimatedLine(line.getGUID(),
                                                            line.getEnd1().getGUID(),
                                                            line.getProperties().get(OpenMetadataProperty.LABEL.name).toString(),
                                                            line.getEnd2().getGUID());
                    }
                    else
                    {
                        super.appendMermaidLongAnimatedLine(line.getGUID(),
                                                            line.getEnd1().getGUID(),
                                                            null,
                                                            line.getEnd2().getGUID());
                    }
                }
            }
            else
            {
                /*
                 * No useful content in the graph.
                 */
                super.clearGraph();
            }
        }
    }
}
