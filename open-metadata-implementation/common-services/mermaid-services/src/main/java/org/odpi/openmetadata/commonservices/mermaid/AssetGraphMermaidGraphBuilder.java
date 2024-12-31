/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.commonservices.mermaid;

import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.*;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;

import java.util.ArrayList;
import java.util.List;


public class AssetGraphMermaidGraphBuilder extends MermaidGraphBuilderBase
{
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

        List<String> usedNodeNames = new ArrayList<>();

        String currentNodeName    = assetGraph.getElementHeader().getGUID();
        String currentDisplayName = assetGraph.getProperties().getDisplayName();

        appendMermaidNode(currentNodeName,
                          currentDisplayName,
                          assetGraph.getElementHeader().getType().getTypeName());

        usedNodeNames.add(currentNodeName);


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

                    if (!usedNodeNames.contains(currentNodeName))
                    {
                        appendMermaidNode(currentNodeName,
                                          currentDisplayName,
                                          node.getElementHeader().getType().getTypeName());

                        usedNodeNames.add(currentNodeName);
                    }
                }
            }

            for (MetadataRelationship line : assetGraph.getRelationships())
            {
                if (line != null)
                {
                    super.appendMermaidLine(this.removeSpaces(line.getEnd1().getGUID()),
                                            line.getType().getTypeName(),
                                            this.removeSpaces(line.getEnd2().getGUID()));
                }
            }
        }
    }
}
