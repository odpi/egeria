/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworks.openmetadata.mermaid;

import org.odpi.openmetadata.frameworks.openmetadata.properties.OpenMetadataElement;
import org.odpi.openmetadata.frameworks.openmetadata.properties.OpenMetadataElementGraph;
import org.odpi.openmetadata.frameworks.openmetadata.properties.OpenMetadataRelationship;
import org.odpi.openmetadata.frameworks.openmetadata.properties.RelatedMetadataElement;
import org.odpi.openmetadata.frameworks.openmetadata.search.ElementProperties;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;

import java.util.List;

/**
 * Construct mermaid markdown graph from Governance Action Framework open metadata store structures.
 */
public class OpenMetadataMermaidGraphBuilder extends MermaidGraphBuilderBase
{
    /**
     * Construct a graph.
     *
     * @param elementGraph content
     */
    public OpenMetadataMermaidGraphBuilder(OpenMetadataElementGraph elementGraph)
    {
        mermaidGraph.append("---\n");
        mermaidGraph.append("title: Anchor Element - ");
        mermaidGraph.append(this.getNodeDisplayName(elementGraph.getElementProperties(), elementGraph.getType().getTypeName()));
        mermaidGraph.append(" [");
        mermaidGraph.append(elementGraph.getElementGUID());
        mermaidGraph.append("]\n---\nflowchart LR\n%%{init: {\"flowchart\": {\"htmlLabels\": false}} }%%\n\n");

        String currentNodeName    = elementGraph.getElementGUID();
        String currentDisplayName = this.getNodeDisplayName(elementGraph.getElementProperties(), elementGraph.getType().getTypeName());

        appendNewMermaidNode(currentNodeName,
                             currentDisplayName,
                             elementGraph.getType().getTypeName(),
                             this.getVisualStyleForEntity(elementGraph, VisualStyle.ANCHOR_ELEMENT));

        if (elementGraph.getAnchoredElements() != null)
        {
            for (OpenMetadataElement node : elementGraph.getAnchoredElements())
            {
                if (node != null)
                {
                    currentNodeName    = node.getElementGUID();
                    currentDisplayName = this.getNodeDisplayName(node.getElementProperties(), node.getType().getTypeName());

                    appendNewMermaidNode(currentNodeName,
                                         currentDisplayName,
                                         node.getType().getTypeName(),
                                         this.getVisualStyleForEntity(node, VisualStyle.ANCHORED_ELEMENT));
                }
            }
        }

        if (elementGraph.getRelationships() != null)
        {
            for (OpenMetadataRelationship line : elementGraph.getRelationships())
            {
                if (line != null)
                {
                    VisualStyle visualStyle = getVisualStyleForRelationship(line);

                    String endName = line.getElementGUIDAtEnd1();
                    if (line.getElementAtEnd1().getUniqueName() != null)
                    {
                        endName = line.getElementAtEnd1().getUniqueName();
                    }

                    appendNewMermaidNode(line.getElementAtEnd1().getGUID(),
                                         endName,
                                         line.getElementAtEnd1().getType().getTypeName(),
                                         getVisualStyleForClassifications(line.getElementAtEnd1(), visualStyle));

                    endName = line.getElementAtEnd2().getGUID();
                    if (line.getElementAtEnd2().getUniqueName() != null)
                    {
                        endName = line.getElementAtEnd2().getUniqueName();
                    }

                    appendNewMermaidNode(line.getElementAtEnd2().getGUID(),
                                         endName,
                                         line.getElementAtEnd2().getType().getTypeName(),
                                         getVisualStyleForClassifications(line.getElementAtEnd2(), visualStyle));

                    super.appendMermaidLine(line.getRelationshipGUID(),
                                            line.getElementAtEnd1().getGUID(),
                                            super.addSpacesToTypeName(line.getType().getTypeName()),
                                            line.getElementAtEnd2().getGUID());
                }
            }
        }
    }


    /**
     * Construct a graph of related metadata elements.
     *
     * @param element central node
     * @param relatedMetadataElements list of related metadata elements
     */
    public OpenMetadataMermaidGraphBuilder(OpenMetadataElement          element,
                                           List<RelatedMetadataElement> relatedMetadataElements)
    {
        mermaidGraph.append("---\n");
        mermaidGraph.append("title: Starting Element - ");
        mermaidGraph.append(element.getElementGUID());
        mermaidGraph.append("\n---\nflowchart LR\n%%{init: {\"flowchart\": {\"htmlLabels\": false}} }%%\n\n");

        super.appendNewMermaidNode(element.getElementGUID(),
                                   this.getNodeDisplayName(element.getElementProperties(), element.getType().getTypeName()),
                                   super.getTypeNameForEntity(element),
                                   super.getVisualStyleForEntity(element, VisualStyle.ANCHORED_ELEMENT));

        if (relatedMetadataElements != null)
        {
            for (RelatedMetadataElement node : relatedMetadataElements)
            {
                if (node != null)
                {
                    super.appendNewMermaidNode(node.getElement().getElementGUID(),
                                               this.getNodeDisplayName(node.getElement().getElementProperties(), node.getElement().getType().getTypeName()),
                                               super.getTypeNameForEntity(node.getElement()),
                                               super.getVisualStyleForEntity(node.getElement(), VisualStyle.LINKED_ELEMENT));

                }
            }

            for (RelatedMetadataElement line : relatedMetadataElements)
            {
                if (line != null)
                {
                    if (line.getElementAtEnd1())
                    {
                        super.appendMermaidLine(line.getRelationshipGUID(),
                                                line.getElement().getElementGUID(),
                                                super.addSpacesToTypeName(line.getType().getTypeName()),
                                                element.getElementGUID());
                    }
                    else
                    {
                        super.appendMermaidLine(line.getRelationshipGUID(),
                                                element.getElementGUID(),
                                                super.addSpacesToTypeName(line.getType().getTypeName()),
                                                line.getElement().getElementGUID());
                    }
                }
            }
        }
    }


    /**
     * Construct a graph of related metadata elements.
     *
     * @param end1GUID end guid
     * @param end2GUID end guid
     * @param openMetadataRelationships list of related metadata elements
     */
    public OpenMetadataMermaidGraphBuilder(String                         end1GUID,
                                           String                         end2GUID,
                                           List<OpenMetadataRelationship> openMetadataRelationships)
    {
        mermaidGraph.append("---\n");
        mermaidGraph.append("title: Relationships for Element - ");
        mermaidGraph.append(end1GUID);
        mermaidGraph.append(" - Linked to - ");
        mermaidGraph.append(end2GUID);
        mermaidGraph.append("]\n---\nflowchart LR\n%%{init: {\"flowchart\": {\"htmlLabels\": false}} }%%\n\n");

        addOpenMetadataRelationshipsToGraph(openMetadataRelationships);
    }


    /**
     * Construct a graph of related metadata elements.
     *
     * @param openMetadataRelationships list of related metadata elements
     */
    public OpenMetadataMermaidGraphBuilder(List<OpenMetadataRelationship> openMetadataRelationships)
    {
        mermaidGraph.append("---\n");
        mermaidGraph.append("title: Relationships matching Query");
        mermaidGraph.append("\n---\nflowchart LR\n%%{init: {\"flowchart\": {\"htmlLabels\": false}} }%%\n\n");

        addOpenMetadataRelationshipsToGraph(openMetadataRelationships);
    }


    /**
     * Add the relationships to the mermaid graph.
     *
     * @param openMetadataRelationships list of related metadata elements
     */
    private void addOpenMetadataRelationshipsToGraph(List<OpenMetadataRelationship> openMetadataRelationships)
    {
        String currentNodeName;
        String currentDisplayName;

        if (openMetadataRelationships != null)
        {
            for (OpenMetadataRelationship node : openMetadataRelationships)
            {
                if (node != null)
                {
                    if (node.getElementAtEnd1().getUniqueName() != null)
                    {
                        currentDisplayName = node.getElementAtEnd1().getUniqueName();
                    }
                    else
                    {
                        currentDisplayName = node.getElementAtEnd1().getType().getTypeName();
                    }

                    appendNewMermaidNode(node.getElementGUIDAtEnd1(),
                                         currentDisplayName,
                                         super.getTypeNameForEntity(node.getElementAtEnd1()),
                                         getVisualStyleForEntity(node.getElementAtEnd1(), VisualStyle.LINKED_ELEMENT));

                    if (node.getElementAtEnd2().getUniqueName() != null)
                    {
                        currentDisplayName = node.getElementAtEnd2().getUniqueName();
                    }
                    else
                    {
                        currentDisplayName = node.getElementAtEnd2().getType().getTypeName();
                    }

                    appendNewMermaidNode(node.getElementGUIDAtEnd2(),
                                         currentDisplayName,
                                         super.getTypeNameForEntity(node.getElementAtEnd2()),
                                         getVisualStyleForEntity(node.getElementAtEnd2(), VisualStyle.LINKED_ELEMENT));
                }
            }

            for (OpenMetadataRelationship line : openMetadataRelationships)
            {
                if (line != null)
                {
                    super.appendMermaidLine(line.getRelationshipGUID(),
                                            line.getElementGUIDAtEnd1(),
                                            super.addSpacesToTypeName(line.getType().getTypeName()),
                                            line.getElementGUIDAtEnd2());
                }
            }
        }
    }


    /**
     * Extract a display name from a variety of properties.
     *
     * @param elementProperties properties from the element
     * @param typeName type name as a last resource
     * @return display name to use
     */
    private String getNodeDisplayName(ElementProperties elementProperties,
                                      String            typeName)
    {
        final String methodName = "getDisplayName";

        String currentDisplayName = propertyHelper.getStringProperty(sourceName,
                                                                     OpenMetadataProperty.DISPLAY_NAME.name,
                                                                     elementProperties,
                                                                     methodName);
        if (currentDisplayName == null)
        {
            currentDisplayName = propertyHelper.getStringProperty(sourceName,
                                                                  OpenMetadataProperty.DISPLAY_NAME.name,
                                                                  elementProperties,
                                                                  methodName);
        }

        if (currentDisplayName == null)
        {
            currentDisplayName = propertyHelper.getStringProperty(sourceName,
                                                                  OpenMetadataProperty.RESOURCE_NAME.name,
                                                                  elementProperties,
                                                                  methodName);
        }

        if (currentDisplayName == null)
        {
            currentDisplayName = propertyHelper.getStringProperty(sourceName,
                                                                  OpenMetadataProperty.ROLE.name,
                                                                  elementProperties,
                                                                  methodName);
        }

        if (currentDisplayName == null)
        {
            currentDisplayName = propertyHelper.getStringProperty(sourceName,
                                                                  OpenMetadataProperty.FULL_NAME.name,
                                                                  elementProperties,
                                                                  methodName);
        }

        if (currentDisplayName == null)
        {
            currentDisplayName = propertyHelper.getStringProperty(sourceName,
                                                                  OpenMetadataProperty.USER_ID.name,
                                                                  elementProperties,
                                                                  methodName);
        }

        if (currentDisplayName == null)
        {
            currentDisplayName = propertyHelper.getStringProperty(sourceName,
                                                                  OpenMetadataProperty.DISTINGUISHED_NAME.name,
                                                                  elementProperties,
                                                                  methodName);
        }

        if (currentDisplayName == null)
        {
            currentDisplayName = propertyHelper.getStringProperty(sourceName,
                                                                  OpenMetadataProperty.IDENTIFIER.name,
                                                                  elementProperties,
                                                                  methodName);
        }

        if (currentDisplayName == null)
        {
            currentDisplayName = propertyHelper.getStringProperty(sourceName,
                                                                  OpenMetadataProperty.DISPLAY_NAME.name,
                                                                  elementProperties,
                                                                  methodName);
        }

        if (currentDisplayName == null)
        {
            currentDisplayName = propertyHelper.getStringProperty(sourceName,
                                                                  OpenMetadataProperty.ANNOTATION_TYPE.name,
                                                                  elementProperties,
                                                                  methodName);
        }

        if (currentDisplayName == null)
        {
            currentDisplayName = propertyHelper.getEnumPropertySymbolicName(sourceName,
                                                                            OpenMetadataProperty.STARS.name,
                                                                            elementProperties,
                                                                            methodName);
        }

        if (currentDisplayName == null)
        {
            currentDisplayName = propertyHelper.getStringProperty(sourceName,
                                                                  OpenMetadataProperty.URL.name,
                                                                  elementProperties,
                                                                  methodName);
        }


        if (currentDisplayName == null)
        {
            currentDisplayName = propertyHelper.getStringProperty(sourceName,
                                                                  OpenMetadataProperty.QUALIFIED_NAME.name,
                                                                  elementProperties,
                                                                  methodName);
        }

        if (currentDisplayName == null)
        {
            currentDisplayName = typeName;
        }

        return currentDisplayName;
    }

}
