/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworks.openmetadata.mermaid;

import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.DataStructureElement;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.MemberDataField;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;

import java.util.UUID;


/**
 * Creates a mermaid graph rendering of the Open Metadata Framework's data structure graph.
 */
public class DataStructureMermaidGraphBuilder extends MermaidGraphBuilderBase
{
    /**
     * Construct a mermaid markdown graph.
     *
     * @param dataStructureElement content
     */
    public DataStructureMermaidGraphBuilder(DataStructureElement dataStructureElement)
    {
        mermaidGraph.append("---\n");
        mermaidGraph.append("title: Data Structure - ");
        mermaidGraph.append(dataStructureElement.getProperties().getDisplayName());
        mermaidGraph.append(" [");
        mermaidGraph.append(dataStructureElement.getElementHeader().getGUID());
        mermaidGraph.append("]\n---\nflowchart TD\n%%{init: {\"flowchart\": {\"htmlLabels\": false}} }%%\n\n");

        this.addDescription(dataStructureElement);

        String dataStructureNodeName = dataStructureElement.getProperties().getDisplayName();

        if (dataStructureNodeName == null)
        {
            dataStructureNodeName = dataStructureElement.getProperties().getQualifiedName();
        }

        appendNewMermaidNode(dataStructureNodeName,
                             dataStructureElement.getElementHeader().getGUID(),
                             dataStructureElement.getElementHeader().getType().getTypeName(),
                             getVisualStyleForEntity(dataStructureElement.getElementHeader(), VisualStyle.DATA_STRUCTURE));

        if ((dataStructureElement.getMemberDataFields() != null) && (! dataStructureElement.getMemberDataFields().isEmpty()))
        {
            final String subcomponentArea = "Data fields";

            super.startSubgraph(subcomponentArea, VisualStyle.DATA_STRUCTURE_INTERNALS);

            for (MemberDataField node : dataStructureElement.getMemberDataFields())
            {
                if (node != null)
                {
                    this.addMemberDataFieldToGraph(dataStructureNodeName, node);
                }
            }

            super.endSubgraph();

            super.appendMermaidLine(null,
                                    dataStructureElement.getElementHeader().getGUID(),
                                    "contains",
                                    subcomponentArea);
        }

        if (dataStructureElement.getEquivalentSchemaType() != null)
        {
            String schemaNodeName = dataStructureElement.getEquivalentSchemaType().getRelatedElement().getProperties().get(OpenMetadataProperty.QUALIFIED_NAME.name);

            appendNewMermaidNode(schemaNodeName,
                                 dataStructureElement.getEquivalentSchemaType().getRelatedElement().getElementHeader().getGUID(),
                                 dataStructureElement.getEquivalentSchemaType().getRelatedElement().getElementHeader().getType().getTypeName(),
                                 getVisualStyleForEntity(dataStructureElement.getEquivalentSchemaType().getRelatedElement().getElementHeader(),VisualStyle.DATA_STRUCTURE));

            super.appendMermaidLine(null,
                                    dataStructureNodeName,
                                    null,
                                    dataStructureElement.getEquivalentSchemaType().getRelatedElement().getElementHeader().getGUID());
        }

        super.addRelatedElementSummaries(dataStructureElement.getExternalReferences(),
                                         VisualStyle.EXTERNAL_REFERENCE,
                                         dataStructureElement.getElementHeader().getGUID());

        super.addRelatedElementSummaries(dataStructureElement.getMemberOfCollections(),
                                         VisualStyle.COLLECTION,
                                         dataStructureElement.getElementHeader().getGUID());

        super.addRelatedElementSummaries(dataStructureElement.getOtherRelatedElements(),
                                         VisualStyle.LINKED_ELEMENT,
                                         dataStructureElement.getElementHeader().getGUID());
    }


    /**
     * Add a text box with the description (if any)
     *
     * @param dataStructureElement element with the potential description
     */
    private void addDescription(DataStructureElement dataStructureElement)
    {
        if (dataStructureElement.getProperties() != null)
        {
            if (dataStructureElement.getProperties().getDescription() != null)
            {
                String descriptionNodeName = UUID.randomUUID().toString();

                appendNewMermaidNode(descriptionNodeName,
                                     dataStructureElement.getProperties().getDescription(),
                                     "Description",
                                     VisualStyle.DESCRIPTION);
            }
        }
    }
}
