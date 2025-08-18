/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworks.openmetadata.mermaid;

import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.AssetGraph;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.MetadataElementSummary;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.RelatedMetadataNodeSummary;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;
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


    /**
     * Extract a suitable label depending on the type of line.
     *
     * @param line relationship
     * @return label
     */
    private String getActionLabel(RelatedMetadataNodeSummary line)
    {
        Object label = null;

        if (line.getRelationshipProperties() != null)
        {
            label = line.getRelationshipProperties().get(OpenMetadataProperty.SEVERITY_LEVEL_IDENTIFIER.name);

            if (label != null)
            {
                label = line.getRelationshipProperties().get(OpenMetadataProperty.ACTION_TARGET_NAME.name);

                if (label != null)
                {
                    Object status = line.getRelationshipProperties().get(OpenMetadataProperty.TERM_RELATIONSHIP_STATUS.name);

                    if (status == null)
                    {
                        status = line.getRelationshipProperties().get(OpenMetadataProperty.TERM_ASSIGNMENT_STATUS.name);
                    }

                    if (status == null)
                    {
                        status = line.getRelationshipProperties().get(OpenMetadataProperty.ACTIVITY_STATUS.name);
                    }

                    if (status != null)
                    {
                        label = label + "[" + status + "]";
                    }
                }
            }

            if (label != null)
            {
                return super.addSpacesToTypeName(line.getRelationshipHeader().getType().getTypeName()) + " " + label;
            }
        }

        return super.addSpacesToTypeName(line.getRelationshipHeader().getType().getTypeName());
    }
}
