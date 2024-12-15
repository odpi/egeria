/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.commonservices.mermaid;

import org.odpi.openmetadata.frameworks.governanceaction.properties.OpenMetadataElement;
import org.odpi.openmetadata.frameworks.governanceaction.properties.OpenMetadataElementGraph;
import org.odpi.openmetadata.frameworks.governanceaction.properties.OpenMetadataRelationship;
import org.odpi.openmetadata.frameworks.governanceaction.properties.RelatedMetadataElement;
import org.odpi.openmetadata.frameworks.governanceaction.search.ElementProperties;
import org.odpi.openmetadata.frameworks.governanceaction.search.PropertyHelper;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;

import java.util.ArrayList;
import java.util.List;

/**
 * Construct mermaid markdown graph from Governance Action Framework open metadata store structures.
 */
public class OpenMetadataMermaidGraphBuilder extends MermaidGraphBuilderBase
{
    private final PropertyHelper propertyHelper = new PropertyHelper();
    private final String         serviceName = "Mermaid Graph Builder";

    /**
     * Construct a graph.
     *
     * @param elementGraph content
     */
    public OpenMetadataMermaidGraphBuilder(OpenMetadataElementGraph elementGraph)
    {
        mermaidGraph.append("---\n");
        mermaidGraph.append("title: Anchor Element - ");
        mermaidGraph.append(this.getDisplayName(elementGraph.getElementProperties(), elementGraph.getType().getTypeName()));
        mermaidGraph.append(" [");
        mermaidGraph.append(elementGraph.getElementGUID());
        mermaidGraph.append("]\n---\nflowchart LR\n%%{init: {\"flowchart\": {\"htmlLabels\": false}} }%%\n\n");

        List<String> usedNodeNames = new ArrayList<>();

        String currentNodeName    = elementGraph.getElementGUID();
        String currentDisplayName = this.getDisplayName(elementGraph.getElementProperties(), elementGraph.getType().getTypeName());

        appendMermaidNode(currentNodeName,
                          currentDisplayName,
                          elementGraph.getType().getTypeName());

        usedNodeNames.add(currentNodeName);

        if (elementGraph.getAnchoredElements() != null)
        {
            for (OpenMetadataElement node : elementGraph.getAnchoredElements())
            {
                if (node != null)
                {
                    currentNodeName    = node.getElementGUID();
                    currentDisplayName = this.getDisplayName(node.getElementProperties(), node.getType().getTypeName());

                    if (!usedNodeNames.contains(currentNodeName))
                    {
                        appendMermaidNode(currentNodeName,
                                          currentDisplayName,
                                          node.getType().getTypeName());

                        usedNodeNames.add(currentNodeName);
                    }
                }
            }

            for (OpenMetadataRelationship line : elementGraph.getRelationships())
            {
                if (line != null)
                {
                    mermaidGraph.append(this.removeSpaces(line.getElementGUIDAtEnd1()));
                    mermaidGraph.append("-->|");
                    mermaidGraph.append(line.getType().getTypeName());
                    mermaidGraph.append("|");
                    mermaidGraph.append(this.removeSpaces(line.getElementGUIDAtEnd2()));
                    mermaidGraph.append("\n");
                }
            }
        }
    }


    /**
     * Construct a graph of related metadata elements.
     *
     * @param elementGUID central guid
     * @param relatedMetadataElements list of related metadata elements
     */
    public OpenMetadataMermaidGraphBuilder(String                       elementGUID,
                                           List<RelatedMetadataElement> relatedMetadataElements)
    {
        mermaidGraph.append("---\n");
        mermaidGraph.append("title: Starting Element - ");
        mermaidGraph.append(elementGUID);
        mermaidGraph.append("\n---\nflowchart LR\n%%{init: {\"flowchart\": {\"htmlLabels\": false}} }%%\n\n");

        List<String> usedNodeNames = new ArrayList<>();

        String currentNodeName    = elementGUID;
        String currentDisplayName = elementGUID;

        appendMermaidNode(currentNodeName,
                          currentDisplayName,
                          "Starting from");

        usedNodeNames.add(currentNodeName);

        if (relatedMetadataElements != null)
        {
            for (RelatedMetadataElement node : relatedMetadataElements)
            {
                if (node != null)
                {
                    currentNodeName    = node.getElement().getElementGUID();
                    currentDisplayName = this.getDisplayName(node.getElement().getElementProperties(), node.getElement().getType().getTypeName());

                    if (!usedNodeNames.contains(currentNodeName))
                    {
                        appendMermaidNode(currentNodeName,
                                          currentDisplayName,
                                          node.getType().getTypeName());

                        usedNodeNames.add(currentNodeName);
                    }
                }
            }

            for (RelatedMetadataElement line : relatedMetadataElements)
            {
                if (line != null)
                {
                    mermaidGraph.append(this.removeSpaces(elementGUID));
                    mermaidGraph.append("-->|");
                    mermaidGraph.append(line.getType().getTypeName());
                    mermaidGraph.append("|");
                    mermaidGraph.append(this.removeSpaces(line.getElement().getElementGUID()));
                    mermaidGraph.append("\n");
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

        List<String> usedNodeNames = new ArrayList<>();

        String currentNodeName;
        String currentDisplayName;

        if (openMetadataRelationships != null)
        {
            for (OpenMetadataRelationship node : openMetadataRelationships)
            {
                if (node != null)
                {
                    currentNodeName = node.getElementGUIDAtEnd1();

                    if (node.getElementAtEnd1().getUniqueName() != null)
                    {
                        currentDisplayName = node.getElementAtEnd1().getUniqueName();
                    }
                    else
                    {
                        currentDisplayName = node.getElementAtEnd1().getType().getTypeName();
                    }

                    if (!usedNodeNames.contains(currentNodeName))
                    {
                        appendMermaidNode(currentNodeName,
                                          currentDisplayName,
                                          node.getType().getTypeName());

                        usedNodeNames.add(currentNodeName);
                    }

                    currentNodeName = node.getElementGUIDAtEnd2();

                    if (node.getElementAtEnd2().getUniqueName() != null)
                    {
                        currentDisplayName = node.getElementAtEnd2().getUniqueName();
                    }
                    else
                    {
                        currentDisplayName = node.getElementAtEnd2().getType().getTypeName();
                    }

                    if (!usedNodeNames.contains(currentNodeName))
                    {
                        appendMermaidNode(currentNodeName,
                                          currentDisplayName,
                                          node.getType().getTypeName());

                        usedNodeNames.add(currentNodeName);
                    }
                }
            }

            for (OpenMetadataRelationship line : openMetadataRelationships)
            {
                if (line != null)
                {
                    mermaidGraph.append(this.removeSpaces(line.getElementGUIDAtEnd1()));
                    mermaidGraph.append("-->|");
                    mermaidGraph.append(line.getType().getTypeName());
                    mermaidGraph.append("|");
                    mermaidGraph.append(this.removeSpaces(line.getElementGUIDAtEnd2()));
                    mermaidGraph.append("\n");
                }
            }
        }
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

        List<String> usedNodeNames = new ArrayList<>();

        String currentNodeName;
        String currentDisplayName;

        if (openMetadataRelationships != null)
        {
            for (OpenMetadataRelationship node : openMetadataRelationships)
            {
                if (node != null)
                {
                    currentNodeName = node.getElementGUIDAtEnd1();

                    if (node.getElementAtEnd1().getUniqueName() != null)
                    {
                        currentDisplayName = node.getElementAtEnd1().getUniqueName();
                    }
                    else
                    {
                        currentDisplayName = node.getElementAtEnd1().getType().getTypeName();
                    }

                    if (!usedNodeNames.contains(currentNodeName))
                    {
                        appendMermaidNode(currentNodeName,
                                          currentDisplayName,
                                          node.getType().getTypeName());

                        usedNodeNames.add(currentNodeName);
                    }

                    currentNodeName = node.getElementGUIDAtEnd2();

                    if (node.getElementAtEnd2().getUniqueName() != null)
                    {
                        currentDisplayName = node.getElementAtEnd2().getUniqueName();
                    }
                    else
                    {
                        currentDisplayName = node.getElementAtEnd2().getType().getTypeName();
                    }

                    if (!usedNodeNames.contains(currentNodeName))
                    {
                        appendMermaidNode(currentNodeName,
                                          currentDisplayName,
                                          node.getType().getTypeName());

                        usedNodeNames.add(currentNodeName);
                    }
                }
            }

            for (OpenMetadataRelationship line : openMetadataRelationships)
            {
                if (line != null)
                {
                    mermaidGraph.append(this.removeSpaces(line.getElementGUIDAtEnd1()));
                    mermaidGraph.append("-->|");
                    mermaidGraph.append(line.getType().getTypeName());
                    mermaidGraph.append("|");
                    mermaidGraph.append(this.removeSpaces(line.getElementGUIDAtEnd2()));
                    mermaidGraph.append("\n");
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
    private String getDisplayName(ElementProperties elementProperties,
                                  String            typeName)
    {
        final String methodName = "getDisplayName";

        String currentDisplayName = propertyHelper.getStringProperty(serviceName,
                                                                     OpenMetadataProperty.NAME.name,
                                                                     elementProperties,
                                                                     methodName);
        if (currentDisplayName == null)
        {
            currentDisplayName = propertyHelper.getStringProperty(serviceName,
                                                                  OpenMetadataProperty.DISPLAY_NAME.name,
                                                                  elementProperties,
                                                                  methodName);
        }

        if (currentDisplayName == null)
        {
            currentDisplayName = propertyHelper.getStringProperty(serviceName,
                                                                  OpenMetadataProperty.RESOURCE_NAME.name,
                                                                  elementProperties,
                                                                  methodName);
        }

        if (currentDisplayName == null)
        {
            currentDisplayName = propertyHelper.getStringProperty(serviceName,
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
