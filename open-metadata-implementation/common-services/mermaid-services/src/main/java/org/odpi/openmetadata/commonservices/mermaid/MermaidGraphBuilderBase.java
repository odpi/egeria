/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.commonservices.mermaid;

import org.odpi.openmetadata.frameworks.governanceaction.search.PropertyHelper;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.MetadataElementSummary;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.RelatedMetadataElementSummary;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.SolutionComponentElement;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.WiredSolutionComponent;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;

import java.util.*;

/**
 * Provides the basic functions for building flowchart based graph visualizations.
 */
public class MermaidGraphBuilderBase
{
    protected final StringBuilder            mermaidGraph   = new StringBuilder();
    private   final Set<String>              usedNodeNames  = new HashSet<>();
    private   final Set<String>              usedLinkNames  = new HashSet<>();
    protected final Map<String, VisualStyle> nodeColours    = new HashMap<>();
    protected final PropertyHelper           propertyHelper = new PropertyHelper();
    protected final String                   sourceName     = "MermaidGraphBuilder";


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
        mermaidGraph.append(this.removeSpaces(currentNodeName));
        mermaidGraph.append("(\"`*");
        mermaidGraph.append(addSpacesToTypeName(currentType));
        mermaidGraph.append("*\n**");
        mermaidGraph.append(currentDisplayName);
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
                nodeColours.put(currentNodeName, visualStyle);
            }

            mermaidGraph.append(this.removeSpaces(currentNodeName));
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
                nodeColours.put(currentNodeName, visualStyle);
            }

            mermaidGraph.append(this.removeSpaces(currentNodeName));
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
     *
     * @param displayName original display name
     * @return doctored display name
     */
    private String removeDoubleSlash(String displayName)
    {
        return displayName.replaceAll("//", "/ /");
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

            mermaidGraph.append(this.removeSpaces(end1Id));

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

            mermaidGraph.append(this.removeSpaces(end2Id));
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

            mermaidGraph.append(this.removeSpaces(end1Id));

            if (label != null)
            {
                mermaidGraph.append("-,|\"");
                mermaidGraph.append(label);
                mermaidGraph.append("\"|");
            }
            else
            {
                mermaidGraph.append("-,->");
            }

            mermaidGraph.append(this.removeSpaces(end2Id));
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
            mermaidGraph.append(this.removeSpaces(end1Id));

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

            mermaidGraph.append(this.removeSpaces(end2Id));
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
        mermaidGraph.append(this.removeSpaces(end1Id));
        mermaidGraph.append("~~~");
        mermaidGraph.append(this.removeSpaces(end2Id));
        mermaidGraph.append("\n");
    }


    /**
     * Remove all the spaces from the qualifiedName along with the curly braces - found in the templates.
     *
     * @param currentQualifiedName qualifiedName
     * @return qualified name without spaces
     */
    String removeSpaces(String currentQualifiedName)
    {
        String noSpaces = currentQualifiedName.replaceAll("\\s+","");
        return noSpaces.replaceAll("[\\[\\](){}]", "");
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


    protected String getNodeDisplayName(MetadataElementSummary metadataElementSummary)
    {
        String nodeDisplayName = metadataElementSummary.getProperties().get(OpenMetadataProperty.DISPLAY_NAME.name);

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
            nodeDisplayName = metadataElementSummary.getProperties().get(OpenMetadataProperty.QUALIFIED_NAME.name);
        }
        if (nodeDisplayName == null)
        {
            nodeDisplayName = metadataElementSummary.getElementHeader().getGUID();
        }

        return nodeDisplayName;
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
                                     this.getVisualStyleForSolutionComponent(solutionComponentElement.getProperties().getSolutionComponentType()));
            }
            else
            {
                appendNewMermaidNode(currentNodeName,
                                     currentDisplayName,
                                     solutionComponentElement.getElementHeader().getType().getTypeName(),
                                     VisualStyle.DEFAULT_SOLUTION_COMPONENT);
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
                                             solutionComponentElement.getElementHeader().getType().getTypeName(),
                                             VisualStyle.DEFAULT_SOLUTION_COMPONENT);

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
                                             solutionComponentElement.getElementHeader().getType().getTypeName(),
                                             VisualStyle.DEFAULT_SOLUTION_COMPONENT);

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
                                             VisualStyle.SOLUTION_ROLE);

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
                                                 VisualStyle.SOLUTION_BLUEPRINT);

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
                                                 VisualStyle.INFORMATION_SUPPLY_CHAIN_IMPL);

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
                                                 VisualStyle.LINKED_ELEMENT);

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
     * Add any style requests and return the built mermaid graph.
     *
     * @return string markdown
     */
    public String getMermaidGraph()
    {
        addStyles(nodeColours);

        return mermaidGraph.toString();
    }
}
