/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.commonservices.mermaid;

import org.odpi.openmetadata.frameworks.openmetadata.properties.AttachedClassification;
import org.odpi.openmetadata.frameworks.openmetadata.properties.OpenMetadataElement;
import org.odpi.openmetadata.frameworks.openmetadata.properties.OpenMetadataElementStub;
import org.odpi.openmetadata.frameworks.openmetadata.properties.datadictionaries.MemberDataFieldProperties;
import org.odpi.openmetadata.frameworks.openmetadata.search.PropertyHelper;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.*;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.util.*;

/**
 * Provides the basic functions for building flowchart based graph visualizations.
 */
public class MermaidGraphBuilderBase
{
    protected StringBuilder                  mermaidGraph      = new StringBuilder();
    private final   Set<String>              usedNodeNames     = new HashSet<>();
    private final   Set<String>              usedLinkNames     = new HashSet<>();
    private final   Set<String>              animatedLinkNames = new HashSet<>();
    protected final Map<String, VisualStyle> nodeColours       = new HashMap<>();
    protected final PropertyHelper           propertyHelper    = new PropertyHelper();
    protected final String                   sourceName        = "MermaidGraphBuilder";

    /**
     * Map from guid to nodeId
     */
    protected final Map<String, String> guidToNodeIdMap = new HashMap<>();

    /**
     * Map from anchorGUID to the AnchorNode record consisting of the anchorGUID and its typeName
     */
    private final Map<String, AnchorNode>    extractedAnchors = new HashMap<>();
    private final Map<String, Set<String>>   anchorLinks      = new HashMap<>();


    /**
     * Description of an anchor node for the lineage graph.
     *
     * @param anchorGUID unique identifier
     * @param anchorTypeName type name
     */
    record AnchorNode (String anchorGUID, String anchorTypeName) { }


    /**
     * Extract any anchor information to allow the graph to include linkages to anchors.  This can help
     * join up lineage graphs.
     *
     * @param elementHeader header of extracted element
     */
    protected void extractAnchorInfo(ElementHeader elementHeader)
    {
        if ((elementHeader != null) && (elementHeader.getClassifications() != null))
        {
            for (ElementClassification classification : elementHeader.getClassifications())
            {
                if (classification != null)
                {
                    if (OpenMetadataType.ANCHORS_CLASSIFICATION.typeName.equals(classification.getClassificationName()))
                    {
                        if (classification.getClassificationProperties() != null)
                        {

                            if (classification.getClassificationProperties().get(OpenMetadataProperty.ANCHOR_GUID.name) != null)
                            {
                                String anchorGUID = classification.getClassificationProperties().get(OpenMetadataProperty.ANCHOR_GUID.name).toString();

                                if ((anchorGUID != null) && (! anchorGUID.equals(elementHeader.getGUID())))
                                {
                                    if (extractedAnchors.get(anchorGUID) == null)
                                    {
                                        String anchorTypeName;

                                        if (classification.getClassificationProperties().get(OpenMetadataProperty.ANCHOR_TYPE_NAME.name) != null)
                                        {
                                            anchorTypeName = classification.getClassificationProperties().get(OpenMetadataProperty.ANCHOR_TYPE_NAME.name).toString();
                                        }
                                        else
                                        {
                                            anchorTypeName = "null";
                                        }

                                        AnchorNode anchorNode = new AnchorNode(anchorGUID, anchorTypeName);

                                        extractedAnchors.put(anchorGUID, anchorNode);
                                    }

                                    Set<String> anchoredElements = anchorLinks.get(anchorGUID);

                                    if (anchoredElements == null)
                                    {
                                        anchoredElements = new HashSet<>();
                                    }

                                    anchoredElements.add(elementHeader.getGUID());

                                    anchorLinks.put(anchorGUID, anchoredElements);
                                }
                            }
                        }
                    }
                }
            }
        }
    }


    /**
     * Add links to anchors to the graph.
     *
     * @param allAnchors should all anchors be added - or just those to known nodes?
     */
    protected void addAnchorLinks(boolean allAnchors)
    {
        for (String anchorGUID : anchorLinks.keySet())
        {
            Set<String> anchoredElements = anchorLinks.get(anchorGUID);
            AnchorNode  anchorNode       = extractedAnchors.get(anchorGUID);

            if (allAnchors)
            {
                appendNewMermaidNode(anchorGUID,
                                     anchorGUID,
                                     anchorNode.anchorTypeName,
                                     VisualStyle.LINEAGE_ANCHOR);

                for (String anchoredElement : anchoredElements)
                {
                    appendMermaidDottedLine(null,
                                            anchorGUID,
                                            "Anchor for",
                                            anchoredElement);
                }
            }
            else if (usedNodeNames.contains(anchorGUID))
            {
                for (String anchoredElement : anchoredElements)
                {
                    appendMermaidDottedLine(null,
                                            anchorGUID,
                                            "Anchor for",
                                            anchoredElement);
                }
            }
        }
    }


    /**
     * Switch the visual style of an element if the Memento or Template/TemplateSubstitute classification is set.
     *
     * @param elementHeader header of element
     * @param defaultVisualStyle normal visual style for element
     * @return visual style to use
     */
    protected VisualStyle checkForClassifications(ElementHeader elementHeader,
                                                  VisualStyle   defaultVisualStyle)
    {
        if ((elementHeader != null) && (elementHeader.getClassifications() != null))
        {
            for (ElementClassification classification : elementHeader.getClassifications())
            {
                if (classification != null)
                {
                    if (OpenMetadataType.MEMENTO_CLASSIFICATION.typeName.equals(classification.getClassificationName()))
                    {
                        return VisualStyle.MEMENTO;
                    }
                    else if (OpenMetadataType.TEMPLATE_CLASSIFICATION.typeName.equals(classification.getClassificationName()))
                    {
                        return VisualStyle.TEMPLATE;
                    }
                    else if (OpenMetadataType.TEMPLATE_SUBSTITUTE_CLASSIFICATION.typeName.equals(classification.getClassificationName()))
                    {
                        return VisualStyle.TEMPLATE;
                    }
                }
            }
        }

        return defaultVisualStyle;
    }


    /**
     * Switch the visual style of an element if the Memento or Template classification is set.
     *
     * @param openMetadataElement header of element
     * @param defaultVisualStyle normal visual style for element
     * @return visual style to use
     */
    protected VisualStyle checkForClassifications(OpenMetadataElement openMetadataElement,
                                                  VisualStyle         defaultVisualStyle)
    {
        if ((openMetadataElement != null) && (openMetadataElement.getClassifications() != null))
        {
            for (AttachedClassification classification : openMetadataElement.getClassifications())
            {
                if (classification != null)
                {
                    if (OpenMetadataType.MEMENTO_CLASSIFICATION.typeName.equals(classification.getClassificationName()))
                    {
                        return VisualStyle.MEMENTO;
                    }
                    else if (OpenMetadataType.TEMPLATE_CLASSIFICATION.typeName.equals(classification.getClassificationName()))
                    {
                        return VisualStyle.TEMPLATE;
                    }
                }
            }
        }

        return defaultVisualStyle;
    }


    /**
     * Switch the visual style of an element if the Memento or Template classification is set.
     *
     * @param openMetadataElement header of element
     * @param defaultVisualStyle normal visual style for element
     * @return visual style to use
     */
    protected VisualStyle checkForClassifications(OpenMetadataElementStub openMetadataElement,
                                                  VisualStyle             defaultVisualStyle)
    {
        if ((openMetadataElement != null) && (openMetadataElement.getClassifications() != null))
        {
            for (AttachedClassification classification : openMetadataElement.getClassifications())
            {
                if (classification != null)
                {
                    if (OpenMetadataType.MEMENTO_CLASSIFICATION.typeName.equals(classification.getClassificationName()))
                    {
                        return VisualStyle.MEMENTO;
                    }
                    else if (OpenMetadataType.TEMPLATE_CLASSIFICATION.typeName.equals(classification.getClassificationName()))
                    {
                        return VisualStyle.TEMPLATE;
                    }
                }
            }
        }

        return defaultVisualStyle;
    }


    /**
     * Use the type of the relationship to determine the shape of a linked entity.
     *
     * @param relationshipHeader header of the relationship
     * @return visual style enum
     */
    protected VisualStyle getVisualStyleForRelationship(ElementControlHeader relationshipHeader)
    {
        VisualStyle visualStyle = VisualStyle.LINKED_ELEMENT;

        if (propertyHelper.isTypeOf(relationshipHeader, OpenMetadataType.IMPLEMENTED_BY_RELATIONSHIP.typeName))
        {
            visualStyle = VisualStyle.DEFAULT_SOLUTION_COMPONENT;
        }
        else if (propertyHelper.isTypeOf(relationshipHeader, OpenMetadataType.DEPLOYED_ON_RELATIONSHIP.typeName))
        {
            visualStyle = VisualStyle.HOST;
        }
        else if ((propertyHelper.isTypeOf(relationshipHeader, OpenMetadataType.DATA_FLOW_RELATIONSHIP.typeName)) ||
                (propertyHelper.isTypeOf(relationshipHeader, OpenMetadataType.PROCESS_CALL_RELATIONSHIP.typeName)) ||
                (propertyHelper.isTypeOf(relationshipHeader, OpenMetadataType.LINEAGE_MAPPING_RELATIONSHIP.typeName)) ||
                (propertyHelper.isTypeOf(relationshipHeader, OpenMetadataType.DATA_SET_CONTENT_RELATIONSHIP.typeName)))
        {
            visualStyle = VisualStyle.LINEAGE_ELEMENT;
        }

        return visualStyle;
    }


    /**
     * Map the solution component type to an appropriate visual style.
     *
     * @param solutionComponentType type of component
     * @return visual style enum
     */
    protected VisualStyle getVisualStyleForSolutionComponent(String solutionComponentType)
    {
        if (solutionComponentType != null)
        {
            for (SolutionComponentVisualStyle solutionComponentVisualStyle : SolutionComponentVisualStyle.values())
            {
                if (solutionComponentType.equals(solutionComponentVisualStyle.getSolutionComponentType().getSolutionComponentType()))
                {
                    return solutionComponentVisualStyle.getVisualStyle();
                }
            }
        }

        return VisualStyle.DEFAULT_SOLUTION_COMPONENT;
    }


    /**
     * Convert a type name into a spaced string to allow the names to wrap nicely in a mermaid box.
     *
     * @param typeName type name
     * @return spaced type name
     */
    protected String addSpacesToTypeName(String typeName)
    {
        StringBuilder spacedName = new StringBuilder();

        for (char c : typeName.toCharArray())
        {
            if ((Character.isUpperCase(c)) && (! spacedName.isEmpty()))
            {
                spacedName.append(' ');
            }

            spacedName.append(c);
        }

        return spacedName.toString();
    }


    /**
     * Convert an array into a comma separated string.
     *
     * @param labelValues array of labels
     * @return string value
     */
    public String getListLabel(List<String> labelValues)
    {
        if (labelValues != null)
        {
            StringBuilder stringBuilder = new StringBuilder();
            boolean firstValue = true;

            for (String labelValue : labelValues)
            {
                if (! firstValue)
                {
                    stringBuilder.append(",\n");
                }

                firstValue = false;
                stringBuilder.append(labelValue);
            }

            return stringBuilder.toString();
        }

        return "";
    }


    /**
     * Create a node in the mermaid graph.
     *
     * @param currentNodeName unique name/identifier
     * @param currentDisplayName display name
     * @param currentType type of element
     */
    public void appendMermaidNode(String        currentNodeName,
                                  String        currentDisplayName,
                                  String        currentType)
    {
        mermaidGraph.append(this.lookupNodeName(currentNodeName));
        mermaidGraph.append("(\"`*");
        mermaidGraph.append(addSpacesToTypeName(currentType));
        mermaidGraph.append("*\n**");
        mermaidGraph.append(removeDoubleSlash(currentDisplayName));
        mermaidGraph.append("**`\")\n");
    }


    /**
     * Create a node in the mermaid graph.
     *
     * @param currentNodeName unique name/identifier
     * @param currentDisplayName display name
     * @param currentType type of element
     * @param visualStyle mermaid defined shape and colour value
     * @return whether a new node was created or not.
     */
    public boolean appendNewMermaidNode(String      currentNodeName,
                                        String      currentDisplayName,
                                        String      currentType,
                                        VisualStyle visualStyle)
    {
        if (!usedNodeNames.contains(currentNodeName))
        {
            usedNodeNames.add(currentNodeName);

            if (visualStyle != null)
            {
                nodeColours.put(this.lookupNodeName(currentNodeName), visualStyle);
            }

            mermaidGraph.append(this.lookupNodeName(currentNodeName));
            mermaidGraph.append("@{ shape: ");
            if ((visualStyle != null) && (visualStyle.getShape() != null))
            {
                mermaidGraph.append(visualStyle.getShape());
            }
            else
            {
                mermaidGraph.append("rounded");
            }
            mermaidGraph.append(", label: \"*");
            mermaidGraph.append(addSpacesToTypeName(currentType));
            mermaidGraph.append("*\n**");
            mermaidGraph.append(removeDoubleSlash(currentDisplayName));
            mermaidGraph.append("**\"}\n");

            return true;
        }

        return false;
    }


    /**
     * Create a node in the mermaid graph.
     *
     * @param currentNodeName unique name/identifier
     * @param currentDisplayName display name
     * @param currentType type of element
     * @param additionalProperties additional properties to add to the node
     * @param visualStyle mermaid defined shape and colour value
     * @return whether a new node was created or not.
     */
    public boolean appendNewMermaidNode(String              currentNodeName,
                                        String              currentDisplayName,
                                        String              currentType,
                                        Map<String, String> additionalProperties,
                                        VisualStyle         visualStyle)
    {
        if (!usedNodeNames.contains(currentNodeName))
        {
            usedNodeNames.add(currentNodeName);

            if (visualStyle != null)
            {
                nodeColours.put(this.lookupNodeName(currentNodeName), visualStyle);
            }

            mermaidGraph.append(this.lookupNodeName(currentNodeName));
            mermaidGraph.append("@{ shape: ");
            if ((visualStyle != null) && (visualStyle.getShape() != null))
            {
                mermaidGraph.append(visualStyle.getShape());
            }
            else
            {
                mermaidGraph.append("rounded");
            }
            mermaidGraph.append(", label: \"*");
            mermaidGraph.append(addSpacesToTypeName(currentType));
            mermaidGraph.append("*\n**");
            mermaidGraph.append(removeDoubleSlash(currentDisplayName));
            mermaidGraph.append("**");

            if ((additionalProperties != null) && (! additionalProperties.isEmpty()))
            {
                for (String propertyName : additionalProperties.keySet())
                {
                    mermaidGraph.append("\n");
                    mermaidGraph.append(propertyName);
                    mermaidGraph.append(": ");
                    mermaidGraph.append(additionalProperties.get(propertyName));
                }
            }
            mermaidGraph.append("\"}\n");

            return true;
        }

        return false;
    }


    /**
     * If a display name has part of a URL in it (eg it is from a qualified name), Mermaid displays "unsupported link"
     * rather than the display name.  This change puts a space between the two slashes to allow the display.
     * In addition, some display names include messages that have double quotes in their content.  This removes them
     * to avoid confusing mermaid.
     *
     * @param displayName original display name
     * @return doctored display name
     */
    private String removeDoubleSlash(String displayName)
    {
        String quotesGone = displayName.replaceAll("\"", "'");
        return quotesGone.replaceAll("//", "/ /");
    }


    /**
     * Append a new single pixel width line to the graph.
     *
     * @param lineName unique identifier of the line - may be null
     * @param end1Id identifier of the starting end
     * @param label label for the line
     * @param end2Id identifier of the ending end
     */
    public void appendMermaidThinLine(String lineName,
                                      String end1Id,
                                      String label,
                                      String end2Id)
    {
        if ((lineName == null) || (! usedLinkNames.contains(lineName)))
        {
            if (lineName != null)
            {
                usedLinkNames.add(lineName);
            }

            mermaidGraph.append(this.lookupNodeName(end1Id));

            if (label != null)
            {
                mermaidGraph.append("-->|\"");
                mermaidGraph.append(label);
                mermaidGraph.append("\"|");
            }
            else
            {
                mermaidGraph.append("-->");
            }

            mermaidGraph.append(this.lookupNodeName(end2Id));
            mermaidGraph.append("\n");
        }
    }



    /**
     * Append a new single pixel width line to the graph.
     *
     * @param lineName unique identifier of the line - may be null
     * @param end1Id identifier of the starting end
     * @param label label for the line
     * @param end2Id identifier of the ending end
     */
    public void appendMermaidLongAnimatedLine(String lineName,
                                              String end1Id,
                                              String label,
                                              String end2Id)
    {
        if ((lineName == null) || (! usedLinkNames.contains(lineName)))
        {
            if (lineName != null)
            {
                usedLinkNames.add(lineName);
            }

            mermaidGraph.append(this.lookupNodeName(end1Id));
            mermaidGraph.append(" ");

            if (lineName != null)
            {
                mermaidGraph.append(this.lookupNodeName(lineName));
                mermaidGraph.append("@");

                animatedLinkNames.add(this.lookupNodeName(lineName));
            }

            if (label != null)
            {

                mermaidGraph.append("-- \"");
                mermaidGraph.append(label);
                mermaidGraph.append("\" ------>");
            }
            else
            {
                mermaidGraph.append("------>");
            }

            mermaidGraph.append(this.lookupNodeName(end2Id));
            mermaidGraph.append("\n");
        }
    }


    /**
     * Append a new single pixel width line to the graph.
     *
     * @param lineName unique identifier of the line - may be null
     * @param end1Id identifier of the starting end
     * @param label label for the line
     * @param end2Id identifier of the ending end
     */
    public void appendMermaidDottedLine(String lineName,
                                        String end1Id,
                                        String label,
                                        String end2Id)
    {
        if ((lineName == null) || (! usedLinkNames.contains(lineName)))
        {
            if (lineName != null)
            {
                usedLinkNames.add(lineName);
            }

            mermaidGraph.append(this.lookupNodeName(end1Id));

            if (label != null)
            {
                mermaidGraph.append("-. \"");
                mermaidGraph.append(label);
                mermaidGraph.append("\" .->");
            }
            else
            {
                mermaidGraph.append("-.->");
            }

            mermaidGraph.append(this.lookupNodeName(end2Id));
            mermaidGraph.append("\n");
        }
    }


    /**
     * Append a line to the graph.
     *
     * @param lineName unique identifier of the line - may be null
     * @param end1Id identifier of the starting end
     * @param label label for the line
     * @param end2Id identifier of the ending end
     */
    public void appendMermaidLine(String lineName,
                                  String end1Id,
                                  String label,
                                  String end2Id)
    {
        if ((lineName == null) || (! usedLinkNames.contains(lineName)))
        {
            if (lineName != null)
            {
                usedLinkNames.add(lineName);
            }
            mermaidGraph.append(this.lookupNodeName(end1Id));

            if (label != null)
            {
                mermaidGraph.append("==>|\"");
                mermaidGraph.append(label);
                mermaidGraph.append("\"|");
            }
            else
            {
                mermaidGraph.append("==>");
            }

            mermaidGraph.append(this.lookupNodeName(end2Id));
            mermaidGraph.append("\n");
        }
    }


    /**
     * Append an invisible line to the graph.  This is used to line up descriptions etc
     *
     * @param end1Id identifier of the starting end
     * @param end2Id identifier of the ending end
     */
    public void appendInvisibleMermaidLine(String end1Id,
                                           String end2Id)
    {
        mermaidGraph.append(this.lookupNodeName(end1Id));
        mermaidGraph.append("~~~");
        mermaidGraph.append(this.lookupNodeName(end2Id));
        mermaidGraph.append("\n");
    }


    /**
     * Start a subgraph in the mermaid graph.
     *
     * @param subgraphName name of subgraph
     * @param visualStyle style of subgraph background
     */
    public void startSubgraph(String      subgraphName,
                              VisualStyle visualStyle)
    {
        startSubgraph(subgraphName, visualStyle, null);
    }


    /**
     * Start a subgraph in the mermaid graph.
     *
     * @param subgraphName name of subgraph
     * @param visualStyle style of subgraph background
     */
    public void startSubgraph(String      subgraphName,
                              VisualStyle visualStyle,
                              String      direction)
    {
        String subgraphId = this.lookupNodeName(subgraphName);

        mermaidGraph.append("subgraph ");
        mermaidGraph.append(subgraphId);
        mermaidGraph.append(" [");
        mermaidGraph.append(subgraphName);
        mermaidGraph.append("]\n");

        nodeColours.put(subgraphId, visualStyle);

        if (direction != null)
        {
            mermaidGraph.append("direction ");
            mermaidGraph.append(direction);
            mermaidGraph.append("\n");
        }
    }


    public void endSubgraph()
    {
        mermaidGraph.append("end\n");
    }



    /**
     * Remove all the spaces from the node name along with the curly braces - found in the templates.
     *
     * @param currentNodeName qualifiedName
     * @return qualified name without spaces
     */
    String removeSpaces(String currentNodeName)
    {
        if (currentNodeName != null)
        {
            String noSpaces = currentNodeName.replaceAll("\\s+", "");
            return noSpaces.replaceAll("[\\[\\](){}]", "");
        }

        return null;
    }


    /**
     * Return the name of the node to use in the mermaid graph.
     *
     * @param guid unique identifier of element
     * @return nodeId to use in mermaid
     */
    String lookupNodeName(String guid)
    {
        String nodeId = guidToNodeIdMap.get(guid);

        if (nodeId == null)
        {
            nodeId = Integer.toString(guidToNodeIdMap.size() + 1);
            guidToNodeIdMap.put(guid, nodeId);
        }

        return nodeId;
    }


    /**
     * Extract the name of a node.
     *
     * @param metadataElementSummary description of an element
     * @return string name
     */
    protected String getNodeDisplayName(MetadataElementSummary metadataElementSummary)
    {
        String nodeDisplayName = null;

        if (metadataElementSummary.getProperties() != null)
        {
            nodeDisplayName = metadataElementSummary.getProperties().get(OpenMetadataProperty.DISPLAY_NAME.name);

            if (nodeDisplayName == null)
            {
                nodeDisplayName = metadataElementSummary.getProperties().get(OpenMetadataProperty.NAME.name);
            }
            if (nodeDisplayName == null)
            {
                nodeDisplayName = metadataElementSummary.getProperties().get(OpenMetadataProperty.RESOURCE_NAME.name);
            }
            if (nodeDisplayName == null)
            {
                nodeDisplayName = metadataElementSummary.getProperties().get(OpenMetadataProperty.ROLE.name);
            }
            if (nodeDisplayName == null)
            {
                nodeDisplayName = metadataElementSummary.getProperties().get(OpenMetadataProperty.FULL_NAME.name);
            }
            if (nodeDisplayName == null)
            {
                nodeDisplayName = metadataElementSummary.getProperties().get(OpenMetadataProperty.USER_ID.name);
            }
            if (nodeDisplayName == null)
            {
                nodeDisplayName = metadataElementSummary.getProperties().get(OpenMetadataProperty.DISTINGUISHED_NAME.name);
            }
            if (nodeDisplayName == null)
            {
                nodeDisplayName = metadataElementSummary.getProperties().get(OpenMetadataProperty.IDENTIFIER.name);
            }
            if (nodeDisplayName == null)
            {
                nodeDisplayName = metadataElementSummary.getProperties().get(OpenMetadataProperty.TAG_NAME.name);
            }
            if (nodeDisplayName == null)
            {
                nodeDisplayName = metadataElementSummary.getProperties().get(OpenMetadataProperty.TITLE.name);
            }
            if (nodeDisplayName == null)
            {
                nodeDisplayName = metadataElementSummary.getProperties().get(OpenMetadataProperty.ANNOTATION_TYPE.name);
            }
            if (nodeDisplayName == null)
            {
                nodeDisplayName = metadataElementSummary.getProperties().get(OpenMetadataProperty.STARS.name);
            }
            if (nodeDisplayName == null)
            {
                nodeDisplayName = metadataElementSummary.getProperties().get(OpenMetadataProperty.QUALIFIED_NAME.name);
            }
        }

        if (nodeDisplayName == null)
        {
            if (metadataElementSummary.getElementHeader().getType().getTypeName().equals(OpenMetadataType.LIKE.typeName))
            {
                nodeDisplayName = metadataElementSummary.getElementHeader().getVersions().getCreatedBy();
            }
            else
            {
                nodeDisplayName = metadataElementSummary.getElementHeader().getGUID();
            }
        }

        return nodeDisplayName;
    }


    /**
     * Add a data field to graph.  If it has sub-fields, they are recursively added.
     *
     * @param dataFieldElement element to process
     */
    protected void addDataFieldToGraph(DataFieldElement dataFieldElement)

    {
        if (dataFieldElement != null)
        {
            String currentDisplayName = dataFieldElement.getProperties().getDisplayName();

            if (currentDisplayName == null)
            {
                currentDisplayName = dataFieldElement.getProperties().getQualifiedName();
            }

            this.appendNewMermaidNode(dataFieldElement.getElementHeader().getGUID(),
                                      currentDisplayName,
                                      dataFieldElement.getElementHeader().getType().getTypeName(),
                                      checkForClassifications(dataFieldElement.getElementHeader(), VisualStyle.DATA_FIELD));

            if (dataFieldElement.getNestedDataFields() != null)
            {
                for (MemberDataField nestedDataField: dataFieldElement.getNestedDataFields())
                {
                    if (nestedDataField != null)
                    {
                        addMemberDataFieldToGraph(dataFieldElement.getElementHeader().getGUID(), nestedDataField);
                    }
                }
            }

            this.addRelatedToElementSummaries(dataFieldElement.getExternalReferences(),
                                              VisualStyle.EXTERNAL_REFERENCE,
                                              dataFieldElement.getElementHeader().getGUID());

            this.addRelatedToElementSummaries(dataFieldElement.getAssignedDataClasses(),
                                              VisualStyle.DATA_CLASS,
                                              dataFieldElement.getElementHeader().getGUID());

            this.addRelatedToElementSummaries(dataFieldElement.getAssignedMeanings(),
                                              VisualStyle.GLOSSARY_TERM,
                                              dataFieldElement.getElementHeader().getGUID());
        }
    }


    /**
     * Add a nested data field to graph.  If it has sub-fields, they are recursively added.
     *
     * @param parentNodeName identifier of the parent node (maybe null)
     * @param memberDataField element to process
     */
    protected void addMemberDataFieldToGraph(String           parentNodeName,
                                             MemberDataField  memberDataField)

    {
        if (memberDataField != null)
        {
            addDataFieldToGraph(memberDataField);

            this.appendMermaidLine(null,
                                   parentNodeName,
                                   this.getListLabel(Collections.singletonList(getCardinalityLinkName(memberDataField.getMemberDataFieldProperties()))),
                                   memberDataField.getElementHeader().getGUID());
        }
    }


    /**
     * Link the list of related elements into the graph.
     *
     * @param relatedMetadataElements list of related element (if null, nothing is added to the graph)
     * @param visualStyle visual style for new nodes
     * @param startingEndId identity of the starting node
     */
    public void addRelatedToElementSummaries(List<RelatedMetadataElementSummary> relatedMetadataElements,
                                             VisualStyle                         visualStyle,
                                             String                              startingEndId)
    {
        if (relatedMetadataElements != null)
        {
            for (RelatedMetadataElementSummary relatedMetadataElement : relatedMetadataElements)
            {
                addRelatedToElementSummary(relatedMetadataElement, visualStyle, startingEndId);
            }
        }
    }


    /**
     * Link the related element into the graph.
     *
     * @param relatedMetadataElement related element (if null, nothing is added to the graph)
     * @param visualStyle visual style for new nodes
     * @param end1Id identity of the starting node
     */
    public void addRelatedToElementSummary(RelatedMetadataElementSummary relatedMetadataElement,
                                           VisualStyle                   visualStyle,
                                           String                        end1Id)
    {
        if (relatedMetadataElement != null)
        {
            appendNewMermaidNode(relatedMetadataElement.getRelatedElement().getElementHeader().getGUID(),
                                 this.getNodeDisplayName(relatedMetadataElement.getRelatedElement()),
                                 this.addSpacesToTypeName(relatedMetadataElement.getRelatedElement().getElementHeader().getType().getTypeName()),
                                 this.checkForClassifications(relatedMetadataElement.getRelatedElement().getElementHeader(), visualStyle));

            String label = null;

            if (relatedMetadataElement.getRelationshipProperties() != null)
            {
                label = relatedMetadataElement.getRelationshipProperties().get(OpenMetadataProperty.LABEL.name);

                if (label == null)
                {
                    label = getCardinalityLabel(relatedMetadataElement.getRelationshipProperties().get(OpenMetadataProperty.POSITION.name),
                                                relatedMetadataElement.getRelationshipProperties().get(OpenMetadataProperty.MIN_CARDINALITY.name),
                                                relatedMetadataElement.getRelationshipProperties().get(OpenMetadataProperty.MAX_CARDINALITY.name));
                }
            }

            if (label == null)
            {
                label = this.addSpacesToTypeName(relatedMetadataElement.getRelationshipHeader().getType().getTypeName());
            }

            appendMermaidLine(relatedMetadataElement.getRelationshipHeader().getGUID(),
                              end1Id,
                              label,
                              relatedMetadataElement.getRelatedElement().getElementHeader().getGUID());
        }
    }


    /**
     * Link the list of related elements into the graph.
     *
     * @param relatedMetadataElements list of related element (if null, nothing is added to the graph)
     * @param visualStyle visual style for new nodes
     * @param startingEndId identity of the starting node
     */
    public void addRelatedFromElementSummaries(List<RelatedMetadataElementSummary> relatedMetadataElements,
                                               VisualStyle                         visualStyle,
                                               String                              startingEndId)
    {
        if (relatedMetadataElements != null)
        {
            for (RelatedMetadataElementSummary relatedMetadataElement : relatedMetadataElements)
            {
                addRelatedFromElementSummary(relatedMetadataElement, visualStyle, startingEndId);
            }
        }
    }


    /**
     * Link the related element into the graph.
     *
     * @param relatedMetadataElement related element (if null, nothing is added to the graph)
     * @param visualStyle visual style for new nodes
     * @param end2Id identity of the starting node
     */
    public void addRelatedFromElementSummary(RelatedMetadataElementSummary relatedMetadataElement,
                                             VisualStyle                   visualStyle,
                                             String                        end2Id)
    {
        if (relatedMetadataElement != null)
        {
            appendNewMermaidNode(relatedMetadataElement.getRelatedElement().getElementHeader().getGUID(),
                                 this.getNodeDisplayName(relatedMetadataElement.getRelatedElement()),
                                 this.addSpacesToTypeName(relatedMetadataElement.getRelatedElement().getElementHeader().getType().getTypeName()),
                                 this.checkForClassifications(relatedMetadataElement.getRelatedElement().getElementHeader(), visualStyle));

            String label = null;

            if (relatedMetadataElement.getRelationshipProperties() != null)
            {
                label = relatedMetadataElement.getRelationshipProperties().get(OpenMetadataProperty.LABEL.name);

                if (label == null)
                {
                    label = getCardinalityLabel(relatedMetadataElement.getRelationshipProperties().get(OpenMetadataProperty.POSITION.name),
                                                relatedMetadataElement.getRelationshipProperties().get(OpenMetadataProperty.MIN_CARDINALITY.name),
                                                relatedMetadataElement.getRelationshipProperties().get(OpenMetadataProperty.MAX_CARDINALITY.name));
                }
            }

            if (label == null)
            {
                label = this.addSpacesToTypeName(relatedMetadataElement.getRelationshipHeader().getType().getTypeName());
            }

            appendMermaidLine(relatedMetadataElement.getRelationshipHeader().getGUID(),
                              relatedMetadataElement.getRelatedElement().getElementHeader().getGUID(),
                              label,
                              end2Id);
        }
    }


    /**
     * Take the string versions of the position, minCardinality and maxCardinality and turn them into a label.
     * If none of these properties are set then null is returned.
     *
     * @param positionString string value of the position attribute
     * @param minCardinalityString string value of the minCardinality attribute
     * @param maxCardinalityString string value of the maxCardinality attribute
     * @return label or null
     */
    protected String getCardinalityLabel(String positionString,
                                         String minCardinalityString,
                                         String maxCardinalityString)
    {
        if ((positionString == null) && (minCardinalityString == null) && (maxCardinalityString == null))
        {
            return null;
        }

        int position = 0;
        int minCardinality = 0;
        int maxCardinality = -1;

        if (positionString != null)
        {
            position = Integer.getInteger(positionString);
        }

        if (minCardinalityString != null)
        {
            minCardinality = Integer.getInteger(minCardinalityString);
        }

        if (minCardinalityString != null)
        {
            minCardinality = Integer.getInteger(minCardinalityString);
        }

        return getCardinalityLabel(position, minCardinality, maxCardinality);
    }


    /**
     * Take the position, minCardinality and maxCardinality attributes and turn them into a label.
     *
     * @param position position attribute
     * @param minCardinality minCardinality attribute
     * @param maxCardinality maxCardinality attribute
     * @return label
     */
    protected String getCardinalityLabel(int position,
                                         int minCardinality,
                                         int maxCardinality)
    {
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append("[");
        stringBuilder.append(position);
        stringBuilder.append("] ");

        if (minCardinality < 0)
        {
            stringBuilder.append("*");
        }
        else
        {
            stringBuilder.append(minCardinality);
        }

        stringBuilder.append("..");

        if (maxCardinality < 0)
        {
            stringBuilder.append("*");
        }
        else
        {
            stringBuilder.append(maxCardinality);
        }

        return stringBuilder.toString();
    }


    /**
     * Determine the cardinality of the data field.
     *
     * @param memberDataFieldProperties relationship properties
     * @return label for the mermaid graph line
     */
    private String getCardinalityLinkName(MemberDataFieldProperties memberDataFieldProperties)
    {
        return this.getCardinalityLabel(memberDataFieldProperties.getDataFieldPosition(),
                                        memberDataFieldProperties.getMinCardinality(),
                                        memberDataFieldProperties.getMaxCardinality());
    }


    /**
     * Add a solution component to graph.
     *
     * @param parentNodeName identifier of the parent node (maybe null)
     * @param parentLinkLabel add label to parent link - also optional
     * @param solutionComponentElement element to process
     * @param solutionLinkingWireGUIDs list of solution wires already defined
     * @param fullDisplay print all elements
     */
    protected void addSolutionComponentToGraph(String                   parentNodeName,
                                               String                   parentLinkLabel,
                                               SolutionComponentElement solutionComponentElement,
                                               List<String>             solutionLinkingWireGUIDs,
                                               boolean                  fullDisplay)

    {
        if (solutionComponentElement != null)
        {
            String currentNodeName    = solutionComponentElement.getElementHeader().getGUID();
            String currentDisplayName = solutionComponentElement.getProperties().getDisplayName();

            if (currentDisplayName == null)
            {
                currentDisplayName = solutionComponentElement.getProperties().getQualifiedName();
            }

            if (solutionComponentElement.getProperties() != null)
            {
                appendNewMermaidNode(currentNodeName,
                                     currentDisplayName,
                                     solutionComponentElement.getElementHeader().getType().getTypeName(),
                                     checkForClassifications(solutionComponentElement.getElementHeader(),
                                                             this.getVisualStyleForSolutionComponent(solutionComponentElement.getProperties().getSolutionComponentType())));
            }
            else
            {
                appendNewMermaidNode(currentNodeName,
                                     currentDisplayName,
                                     solutionComponentElement.getElementHeader().getType().getTypeName(),
                                     checkForClassifications(solutionComponentElement.getElementHeader(),
                                                             VisualStyle.DEFAULT_SOLUTION_COMPONENT));
            }

            if (solutionComponentElement.getWiredToLinks() != null)
            {
                for (WiredSolutionComponent line : solutionComponentElement.getWiredToLinks())
                {
                    if ((line != null) && (! solutionLinkingWireGUIDs.contains(line.getElementHeader().getGUID())))
                    {
                        String relatedComponentDisplayName = getNodeDisplayName(line.getLinkedElement());

                        if (line.getLinkedElement().getProperties() != null)
                        {
                            relatedComponentDisplayName = this.getNodeDisplayName(line.getLinkedElement());
                        }

                        appendNewMermaidNode(line.getLinkedElement().getElementHeader().getGUID(),
                                             relatedComponentDisplayName,
                                             line.getLinkedElement().getElementHeader().getType().getTypeName(),
                                             checkForClassifications(line.getElementHeader(),
                                                                     this.getVisualStyleForSolutionComponent(line.getLinkedElement().getElementHeader().getType().getTypeName())));

                        List<String> labelList = new ArrayList<>();

                        if ((line.getProperties() != null) && (line.getProperties().getLabel() != null))
                        {
                            labelList.add(line.getProperties().getLabel());
                            labelList.add("[" + this.addSpacesToTypeName(line.getElementHeader().getType().getTypeName()) + "]");
                        }
                        else
                        {
                            labelList.add(this.addSpacesToTypeName(line.getElementHeader().getType().getTypeName()));
                        }

                        this.appendMermaidLine(line.getElementHeader().getGUID(),
                                               line.getLinkedElement().getElementHeader().getGUID(),
                                               this.getListLabel(labelList),
                                               solutionComponentElement.getElementHeader().getGUID());

                        solutionLinkingWireGUIDs.add(line.getElementHeader().getGUID());
                    }
                }
            }

            if (solutionComponentElement.getWiredFromLinks() != null)
            {
                for (WiredSolutionComponent line : solutionComponentElement.getWiredFromLinks())
                {
                    if ((line != null) && (! solutionLinkingWireGUIDs.contains(line.getElementHeader().getGUID())))
                    {
                        String relatedComponentDisplayName = getNodeDisplayName(line.getLinkedElement());

                        appendNewMermaidNode(line.getLinkedElement().getElementHeader().getGUID(),
                                             relatedComponentDisplayName,
                                             line.getLinkedElement().getElementHeader().getType().getTypeName(),
                                             checkForClassifications(line.getLinkedElement().getElementHeader(),
                                                                     VisualStyle.DEFAULT_SOLUTION_COMPONENT));

                        List<String> labelList = new ArrayList<>();

                        if ((line.getProperties() != null) && (line.getProperties().getLabel() != null))
                        {
                            labelList.add(line.getProperties().getLabel());
                            labelList.add("[" + this.addSpacesToTypeName(line.getElementHeader().getType().getTypeName()) + "]");
                        }
                        else
                        {
                            labelList.add(this.addSpacesToTypeName(line.getElementHeader().getType().getTypeName()));
                        }

                        this.appendMermaidLine(line.getElementHeader().getGUID(),
                                               solutionComponentElement.getElementHeader().getGUID(),
                                               this.getListLabel(labelList),
                                               line.getLinkedElement().getElementHeader().getGUID());

                        solutionLinkingWireGUIDs.add(line.getElementHeader().getGUID());
                    }
                }
            }

            if (solutionComponentElement.getActors() != null)
            {
                for (RelatedMetadataElementSummary line : solutionComponentElement.getActors())
                {
                    if (line != null)
                    {
                        String actorRoleName = getNodeDisplayName(line.getRelatedElement());

                        appendNewMermaidNode(line.getRelatedElement().getElementHeader().getGUID(),
                                             actorRoleName,
                                             line.getRelatedElement().getElementHeader().getType().getTypeName(),
                                             checkForClassifications(line.getRelatedElement().getElementHeader(),
                                                                     VisualStyle.SOLUTION_ROLE));

                        String actorRoleDescription = line.getRelationshipProperties().get(OpenMetadataProperty.ROLE.name);

                        if (actorRoleDescription == null)
                        {
                            actorRoleDescription = this.addSpacesToTypeName(line.getRelatedElement().getElementHeader().getType().getTypeName());
                        }

                        this.appendMermaidLine(line.getRelationshipHeader().getGUID(),
                                               line.getRelatedElement().getElementHeader().getGUID(),
                                               this.getListLabel(Collections.singletonList(actorRoleDescription)),
                                               solutionComponentElement.getElementHeader().getGUID());
                    }
                }
            }

            if (fullDisplay)
            {
                if (solutionComponentElement.getBlueprints() != null)
                {
                    for (RelatedMetadataElementSummary line : solutionComponentElement.getBlueprints())
                    {
                        if (line != null)
                        {
                            String actorRoleName = getNodeDisplayName(line.getRelatedElement());

                            appendNewMermaidNode(line.getRelatedElement().getElementHeader().getGUID(),
                                                 actorRoleName,
                                                 line.getRelatedElement().getElementHeader().getType().getTypeName(),
                                                 checkForClassifications(line.getRelatedElement().getElementHeader(),
                                                                         VisualStyle.SOLUTION_BLUEPRINT));

                            String label = null;

                            if (line.getRelationshipProperties() != null)
                            {
                                label = line.getRelationshipProperties().get(OpenMetadataProperty.LABEL.name);
                            }

                            if (label == null)
                            {
                                label = this.addSpacesToTypeName(line.getRelatedElement().getElementHeader().getType().getTypeName());
                            }

                            this.appendMermaidLine(line.getRelationshipHeader().getGUID(),
                                                   line.getRelatedElement().getElementHeader().getGUID(),
                                                   this.getListLabel(Collections.singletonList(label)),
                                                   solutionComponentElement.getElementHeader().getGUID());
                        }
                    }
                }

                if (solutionComponentElement.getImplementations() != null)
                {
                    for (RelatedMetadataElementSummary line : solutionComponentElement.getImplementations())
                    {
                        if (line != null)
                        {
                            String actorRoleName = getNodeDisplayName(line.getRelatedElement());

                            appendNewMermaidNode(line.getRelatedElement().getElementHeader().getGUID(),
                                                 actorRoleName,
                                                 this.addSpacesToTypeName(line.getRelatedElement().getElementHeader().getType().getTypeName()),
                                                 checkForClassifications(line.getRelatedElement().getElementHeader(),
                                                                         VisualStyle.INFORMATION_SUPPLY_CHAIN_IMPL));

                            String label = null;

                            if (line.getRelationshipProperties() != null)
                            {
                                label = line.getRelationshipProperties().get(OpenMetadataProperty.LABEL.name);
                            }

                            if (label == null)
                            {
                                label = this.addSpacesToTypeName(line.getRelationshipHeader().getType().getTypeName());
                            }

                            this.appendMermaidLine(line.getRelationshipHeader().getGUID(),
                                                   solutionComponentElement.getElementHeader().getGUID(),
                                                   this.getListLabel(Collections.singletonList(label)),
                                                   line.getRelatedElement().getElementHeader().getGUID());
                        }
                    }
                }

                if (solutionComponentElement.getOtherElements() != null)
                {
                    for (RelatedMetadataElementSummary line : solutionComponentElement.getOtherElements())
                    {
                        if (line != null)
                        {
                            String nodeName = getNodeDisplayName(line.getRelatedElement());

                            appendNewMermaidNode(line.getRelatedElement().getElementHeader().getGUID(),
                                                 nodeName,
                                                 this.addSpacesToTypeName(line.getRelatedElement().getElementHeader().getType().getTypeName()),
                                                 checkForClassifications(line.getRelatedElement().getElementHeader(),
                                                                         VisualStyle.LINKED_ELEMENT));

                            String label = null;

                            if (line.getRelationshipProperties() != null)
                            {
                                label = line.getRelationshipProperties().get(OpenMetadataProperty.LABEL.name);
                            }

                            if (label == null)
                            {
                                label = this.addSpacesToTypeName(line.getRelatedElement().getElementHeader().getType().getTypeName());
                            }

                            this.appendMermaidLine(line.getRelationshipHeader().getGUID(),
                                                   line.getRelatedElement().getElementHeader().getGUID(),
                                                   this.getListLabel(Collections.singletonList(label)),
                                                   solutionComponentElement.getElementHeader().getGUID());
                        }
                    }
                }
            }

            if (parentNodeName != null)
            {
                this.appendMermaidLine(null,
                                       parentNodeName,
                                       this.getListLabel(Collections.singletonList(parentLinkLabel)),
                                       solutionComponentElement.getElementHeader().getGUID());
            }
        }
    }


    /**
     * Add styling for nodes as requested.  These go at the end of the graph.
     */
    protected void addStyles(Map<String, VisualStyle> nodeColours)
    {
        if (! nodeColours.isEmpty())
        {
            for (String nodeId : nodeColours.keySet())
            {
                boolean isFirst = true;

                VisualStyle visualStyle = nodeColours.get(nodeId);

                if (visualStyle.getTextColour() != null || visualStyle.getFillColour() != null || visualStyle.getLineColour() != null)
                {
                    mermaidGraph.append("style ");
                    mermaidGraph.append(nodeId);

                    if (visualStyle.getTextColour() != null)
                    {
                        mermaidGraph.append(" color:");
                        mermaidGraph.append(visualStyle.getTextColour());

                        isFirst = false;
                    }

                    if (visualStyle.getFillColour() != null)
                    {
                        if (isFirst)
                        {
                            isFirst = false;
                        }
                        else
                        {
                            mermaidGraph.append(",");
                        }

                        mermaidGraph.append(" fill:");
                        mermaidGraph.append(visualStyle.getFillColour());
                    }

                    if (visualStyle.getLineColour() != null)
                    {
                        if (! isFirst)
                        {
                            mermaidGraph.append(",");
                        }

                        mermaidGraph.append(" stroke:");
                        mermaidGraph.append(visualStyle.getLineColour());
                    }

                    mermaidGraph.append("\n");
                }
            }
        }
    }


    /**
     * Add the names of links that should be animated
     */
    private void addAnimation()
    {
        for (String lineName : animatedLinkNames)
        {
            mermaidGraph.append(lineName);
            mermaidGraph.append("@{ animation: fast }");
            mermaidGraph.append("\n");
        }
    }



    /**
     * Clear the graph
     */
    protected void clearGraph()
    {
        mermaidGraph = null;
    }


    /**
     * Add anchors, any style requests and return the built mermaid graph.
     *
     * @param allAnchors should all anchors be added - or just those to known nodes?
     * @return string markdown
     */
    public String getMermaidGraph(boolean allAnchors)
    {
        if (mermaidGraph == null)
        {
            return null;
        }

        addAnchorLinks(allAnchors);

        return this.getMermaidGraph();
    }


    /**
     * Add any style requests and return the built mermaid graph.
     *
     * @return string markdown
     */
    public String getMermaidGraph()
    {
        if (mermaidGraph == null)
        {
            return null;
        }

        addStyles(nodeColours);
        addAnimation();

        return mermaidGraph.toString();
    }
}
