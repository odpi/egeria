/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.commonservices.mermaid;

import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.DataStructureElement;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.MemberDataField;
import org.odpi.openmetadata.frameworks.openmetadata.properties.datadictionaries.MemberDataFieldProperties;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;

import java.util.Collections;
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
                             checkForClassifications(dataStructureElement.getElementHeader(), VisualStyle.DATA_STRUCTURE));

        if ((dataStructureElement.getMemberDataFields() != null) && (! dataStructureElement.getMemberDataFields().isEmpty()))
        {
            final String subcomponentArea = "Data fields";
            mermaidGraph.append("subgraph ");
            mermaidGraph.append(subcomponentArea);
            mermaidGraph.append("\n");
            nodeColours.put(subcomponentArea, VisualStyle.DATA_STRUCTURE_INTERNALS);

            for (MemberDataField node : dataStructureElement.getMemberDataFields())
            {
                if (node != null)
                {
                    this.addDataFieldToGraph(dataStructureNodeName, node);
                }
            }

            mermaidGraph.append("end\n");
            mermaidGraph.append(dataStructureElement.getElementHeader().getGUID());
            mermaidGraph.append(" ===> Data fields\n");
        }

        if (dataStructureElement.getEquivalentSchemaType() != null)
        {
            String schemaNodeName = dataStructureElement.getEquivalentSchemaType().getRelatedElement().getProperties().get(OpenMetadataProperty.QUALIFIED_NAME.name);

            appendNewMermaidNode(schemaNodeName,
                                 dataStructureElement.getEquivalentSchemaType().getRelatedElement().getElementHeader().getGUID(),
                                 dataStructureElement.getEquivalentSchemaType().getRelatedElement().getElementHeader().getType().getTypeName(),
                                 checkForClassifications(dataStructureElement.getEquivalentSchemaType().getRelatedElement().getElementHeader(),VisualStyle.DATA_STRUCTURE));

            super.appendMermaidLine(null,
                                    dataStructureNodeName,
                                    null,
                                    dataStructureElement.getEquivalentSchemaType().getRelatedElement().getElementHeader().getGUID());
        }
    }



    /**
     * Add a data field to graph.  If it has sub-fields, they are recursively added.
     *
     * @param parentNodeName identifier of the parent node (maybe null)
     * @param dataFieldElement element to process
     */
    protected void addDataFieldToGraph(String           parentNodeName,
                                       MemberDataField  dataFieldElement)

    {
        if (dataFieldElement != null)
        {
            String currentNodeName    = dataFieldElement.getElementHeader().getGUID();
            String currentDisplayName = dataFieldElement.getProperties().getDisplayName();

            if (currentDisplayName == null)
            {
                currentDisplayName = dataFieldElement.getProperties().getQualifiedName();
            }

            super.appendNewMermaidNode(currentNodeName,
                                       currentDisplayName,
                                       dataFieldElement.getElementHeader().getType().getTypeName(),
                                       checkForClassifications(dataFieldElement.getElementHeader(), VisualStyle.DATA_FIELD));

            super.appendMermaidLine(null,
                                    parentNodeName,
                                    this.getListLabel(Collections.singletonList(getCardinalityLinkName(dataFieldElement.getMemberDataFieldProperties()))),
                                    dataFieldElement.getElementHeader().getGUID());

            if (dataFieldElement.getNestedDataFields() != null)
            {
                for (MemberDataField nestedDataField: dataFieldElement.getNestedDataFields())
                {
                    if (nestedDataField != null)
                    {
                        addDataFieldToGraph(currentNodeName, nestedDataField);
                    }
                }
            }
        }
    }


    /**
     * Determine the cardinality of the data field.
     *
     * @param memberDataFieldProperties relationship properties
     * @return label for the mermaid graph line
     */
    private String getCardinalityLinkName(MemberDataFieldProperties memberDataFieldProperties)
    {
        StringBuilder stringBuilder = new StringBuilder();

        if (memberDataFieldProperties != null)
        {
            stringBuilder.append("[");
            stringBuilder.append(memberDataFieldProperties.getDataFieldPosition());
            stringBuilder.append("] ");
            if (memberDataFieldProperties.getMinCardinality() < 1)
            {
                stringBuilder.append(0);
            }
            else
            {
                stringBuilder.append(memberDataFieldProperties.getMinCardinality());
            }
            stringBuilder.append("..");
            if (memberDataFieldProperties.getMaxCardinality() < 1)
            {
                stringBuilder.append("*");
            }
            else
            {
                stringBuilder.append(memberDataFieldProperties.getMaxCardinality());
            }
        }

        return stringBuilder.toString();
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
