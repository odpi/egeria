/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.commonservices.mermaid;

import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.AssetGraph;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.MetadataRelationship;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.util.ArrayList;
import java.util.List;

/**
 * Creates a mermaid graph rendering of the Open Metadata Framework's asset field level lineage graph.
 */
public class FieldLevelLineageMermaidGraphBuilder extends MermaidGraphBuilderBase
{
    /**
     * Construct a mermaid markdown graph.
     *
     * @param assetGraph content
     */
    public FieldLevelLineageMermaidGraphBuilder(AssetGraph assetGraph)
    {
        mermaidGraph.append("---\n");
        mermaidGraph.append("title: Field-Level Lineage Analysis Graph for Asset - ");
        mermaidGraph.append(assetGraph.getProperties().getDisplayName());
        mermaidGraph.append(" [");
        mermaidGraph.append(assetGraph.getElementHeader().getGUID());
        mermaidGraph.append("]\n---\nflowchart TD\n%%{init: {\"flowchart\": {\"htmlLabels\": false}} }%%\n\n");

        String centreNodeName    = assetGraph.getElementHeader().getGUID();
        String centreDisplayName = assetGraph.getProperties().getDisplayName();

        appendNewMermaidNode(centreNodeName,
                             centreDisplayName,
                             assetGraph.getElementHeader().getType().getTypeName(),
                             VisualStyle.ANCHOR_ELEMENT);

        if (assetGraph.getRelationships() != null)
        {
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
                for (MetadataRelationship relationship : dataMappingRelationships)
                {
                    String end1NodeName = relationship.getEnd1().getGUID();
                    String end1DisplayName   = relationship.getEnd1().getUniqueName();
                    if (end1DisplayName == null)
                    {
                        end1DisplayName = relationship.getEnd1().getGUID();
                    }

                    if (! centreNodeName.equals(end1NodeName))
                    {
                        appendNewMermaidNode(end1NodeName,
                                             end1DisplayName,
                                             relationship.getEnd1().getType().getTypeName(),
                                             checkForClassifications(relationship.getEnd1(), VisualStyle.SCHEMA_ELEMENT));

                    }

                    String end2NodeName = relationship.getEnd2().getGUID();
                    String end2DisplayName   = relationship.getEnd2().getUniqueName();
                    if (end2DisplayName == null)
                    {
                        end2DisplayName = relationship.getEnd2().getGUID();
                    }

                    if (! centreNodeName.equals(end2NodeName))
                    {
                        appendNewMermaidNode(end2NodeName,
                                             end2DisplayName,
                                             relationship.getEnd2().getType().getTypeName(),
                                             checkForClassifications(relationship.getEnd2(), VisualStyle.SCHEMA_ELEMENT));

                    }

                    if ((relationship.getProperties() != null) && (relationship.getProperties().get(OpenMetadataProperty.LABEL.name) != null))
                    {
                        super.appendMermaidLongAnimatedLine(relationship.getGUID(),
                                                            end1NodeName,
                                                            relationship.getProperties().get(OpenMetadataProperty.LABEL.name).toString(),
                                                            end2NodeName);
                    }
                    else
                    {
                        super.appendMermaidLongAnimatedLine(relationship.getGUID(),
                                                            end1NodeName,
                                                            null,
                                                            end2NodeName);
                    }
                }
            }
        }
    }
}
