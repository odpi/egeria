/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworks.openmetadata.mermaid;

import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.DataFieldElement;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.DataStructureElement;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.MemberDataField;
import org.odpi.openmetadata.frameworks.openmetadata.properties.datadictionaries.MemberDataFieldProperties;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;

import java.util.Collections;
import java.util.UUID;


/**
 * Creates a mermaid graph rendering of the Open Metadata Framework's data field graph.
 */
public class DataFieldMermaidGraphBuilder extends MermaidGraphBuilderBase
{
    /**
     * Construct a mermaid markdown graph.
     *
     * @param dataFieldElement content
     */
    public DataFieldMermaidGraphBuilder(DataFieldElement dataFieldElement)
    {
        mermaidGraph.append("---\n");
        mermaidGraph.append("title: Data Field - ");
        mermaidGraph.append(dataFieldElement.getProperties().getDisplayName());
        mermaidGraph.append(" [");
        mermaidGraph.append(dataFieldElement.getElementHeader().getGUID());
        mermaidGraph.append("]\n---\nflowchart TD\n%%{init: {\"flowchart\": {\"htmlLabels\": false}} }%%\n\n");

        this.addDescription(dataFieldElement);

        super.addDataFieldToGraph(dataFieldElement);
    }


    /**
     * Add a text box with the description (if any)
     *
     * @param dataFieldElement element with the potential description
     */
    private void addDescription(DataFieldElement dataFieldElement)
    {
        if (dataFieldElement.getProperties() != null)
        {
            if (dataFieldElement.getProperties().getDescription() != null)
            {
                String descriptionNodeName = UUID.randomUUID().toString();

                appendNewMermaidNode(descriptionNodeName,
                                     dataFieldElement.getProperties().getDescription(),
                                     "Description",
                                     VisualStyle.DESCRIPTION);
            }
        }
    }
}
