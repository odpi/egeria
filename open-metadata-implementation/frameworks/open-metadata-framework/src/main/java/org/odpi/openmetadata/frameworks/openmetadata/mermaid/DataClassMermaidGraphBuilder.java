/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworks.openmetadata.mermaid;

import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.DataClassElement;

import java.util.UUID;


/**
 * Creates a mermaid graph rendering of the Open Metadata Framework's data class graph.
 */
public class DataClassMermaidGraphBuilder extends MermaidGraphBuilderBase
{
    /**
     * Construct a mermaid markdown graph.
     *
     * @param dataClassElement content
     */
    public DataClassMermaidGraphBuilder(DataClassElement dataClassElement)
    {
        mermaidGraph.append("---\n");
        mermaidGraph.append("title: Data Class - ");
        mermaidGraph.append(dataClassElement.getProperties().getDisplayName());
        mermaidGraph.append(" [");
        mermaidGraph.append(dataClassElement.getElementHeader().getGUID());
        mermaidGraph.append("]\n---\nflowchart TD\n%%{init: {\"flowchart\": {\"htmlLabels\": false}} }%%\n\n");

        if (dataClassElement != null)
        {
            this.addDescription(dataClassElement);

            String currentDisplayName = dataClassElement.getProperties().getDisplayName();

            if (currentDisplayName == null)
            {
                currentDisplayName = dataClassElement.getProperties().getQualifiedName();
            }

            super.appendNewMermaidNode(dataClassElement.getElementHeader().getGUID(),
                                       currentDisplayName,
                                       dataClassElement.getElementHeader().getType().getTypeName(),
                                       getVisualStyleForEntity(dataClassElement.getElementHeader(), VisualStyle.DATA_CLASS));

            super.addRelatedElementSummaries(dataClassElement.getExternalReferences(), VisualStyle.EXTERNAL_REFERENCE, dataClassElement.getElementHeader().getGUID());
            super.addRelatedElementSummaries(dataClassElement.getNestedDataClasses(), VisualStyle.DATA_CLASS, dataClassElement.getElementHeader().getGUID());
            super.addRelatedElementSummaries(dataClassElement.getSpecializedDataClasses(), VisualStyle.DATA_CLASS, dataClassElement.getElementHeader().getGUID());
            super.addRelatedElementSummaries(dataClassElement.getOtherRelatedElements(), VisualStyle.LINKED_ELEMENT, dataClassElement.getElementHeader().getGUID());
        }
    }


    /**
     * Add a text box with the description (if any)
     *
     * @param dataClassElement element with the potential description
     */
    private void addDescription(DataClassElement dataClassElement)
    {
        if (dataClassElement.getProperties() != null)
        {
            if (dataClassElement.getProperties().getDescription() != null)
            {
                String descriptionNodeName = UUID.randomUUID().toString();

                appendNewMermaidNode(descriptionNodeName,
                                     dataClassElement.getProperties().getDescription(),
                                     "Description",
                                     VisualStyle.DESCRIPTION);
            }
        }
    }
}
