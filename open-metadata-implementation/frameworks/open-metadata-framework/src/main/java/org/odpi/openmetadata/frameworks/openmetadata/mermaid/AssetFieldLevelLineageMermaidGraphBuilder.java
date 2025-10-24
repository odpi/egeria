/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworks.openmetadata.mermaid;

import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.AssetGraph;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.RelatedMetadataNodeSummary;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.util.ArrayList;
import java.util.List;

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
        String currentDisplayName = super.getNodeDisplayName(assetGraph);;

        mermaidGraph.append("---\n");
        mermaidGraph.append("title: Field-Level Lineage Analysis Graph for Asset - ");
        mermaidGraph.append(currentDisplayName);
        mermaidGraph.append(" [");
        mermaidGraph.append(assetGraph.getElementHeader().getGUID());
        mermaidGraph.append("]\n---\nflowchart TD\n%%{init: {\"flowchart\": {\"htmlLabels\": false}} }%%\n\n");

        appendNewMermaidNode(assetGraph.getElementHeader().getGUID(),
                             currentDisplayName,
                             assetGraph.getElementHeader().getType().getTypeName(),
                             assetGraph.getProperties(),
                             VisualStyle.ANCHOR_ELEMENT);

        if (assetGraph.getRelationships() != null)
        {
            List<RelatedMetadataNodeSummary> dataMappingRelationships = new ArrayList<>();

            for (RelatedMetadataNodeSummary relationship : assetGraph.getRelationships())
            {
                if ((relationship != null) &&
                        (propertyHelper.isTypeOf(relationship.getRelationshipHeader(),
                                                 OpenMetadataType.DATA_MAPPING_RELATIONSHIP.typeName)))
                {
                    extractAnchorInfo(relationship.getRelatedElement().getElementHeader());
                    dataMappingRelationships.add(relationship);
                }
            }

            if (! dataMappingRelationships.isEmpty())
            {
                for (RelatedMetadataNodeSummary line : dataMappingRelationships)
                {
                    super.addRelatedNodeSummary(line, getVisualStyleForEntity(line.getRelatedElement().getElementHeader(), VisualStyle.SCHEMA_ELEMENT), LineStyle.ANIMATED_LONG);
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
