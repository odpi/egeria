/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworks.openmetadata.mermaid;

import org.odpi.openmetadata.frameworks.openmetadata.properties.OpenMetadataTypeDef;

import java.util.List;
import java.util.UUID;


/**
 * Show the subtypes of a type.
 */
public class SubtypesMermaidGraphBuilder extends MermaidGraphBuilderBase
{
    static final String metaTypeName = "Open Metadata Type";

    /**
     * Constructor for the graph
     *
     * @param superType starting type
     * @param subtypes contents
     */
    public SubtypesMermaidGraphBuilder(OpenMetadataTypeDef       superType,
                                       List<OpenMetadataTypeDef> subtypes)
    {
        String rootNodeId = superType.getGUID();
        mermaidGraph.append("---\n");
        mermaidGraph.append("title: Subtypes for - ");
        mermaidGraph.append(superType.getName());
        mermaidGraph.append(" [");
        mermaidGraph.append(rootNodeId);
        mermaidGraph.append("]\n---\nflowchart RL\n%%{init: {\"flowchart\": {\"htmlLabels\": false}} }%%\n\n");

        appendNewMermaidNode(rootNodeId,
                             superType.getName(),
                             metaTypeName,
                             superType.getDescriptionWiki(),
                             VisualStyle.TYPE);

        if (subtypes != null)
        {
            /*
             * Add all the nodes first as not sure of the order of types returned ensures that all supertypes are
             * returned before their subtypes.
             */
            for (OpenMetadataTypeDef subtype : subtypes)
            {
                if (subtype != null)
                {
                    appendNewMermaidNode(subtype.getGUID(),
                                         this.removeSpaces(subtype.getName()),
                                         metaTypeName,
                                         subtype.getDescriptionWiki(),
                                         VisualStyle.SUBTYPE);
                }
            }

            /*
             * Now link them all together.
             */
            for (OpenMetadataTypeDef subtype : subtypes)
            {
                if ((subtype != null) && (subtype.getSuperType() != null))
                {
                    super.appendMermaidLine(null,
                                                subtype.getGUID(),
                                                null,
                                                subtype.getSuperType().getGUID());
                }
            }
        }
    }
}
