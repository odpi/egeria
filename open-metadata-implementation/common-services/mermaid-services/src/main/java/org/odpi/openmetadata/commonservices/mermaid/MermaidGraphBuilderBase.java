/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.commonservices.mermaid;

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
    protected final StringBuilder            mermaidGraph  = new StringBuilder();
    private   final List<String>             usedNodeNames = new ArrayList<>();
    protected final Map<String, VisualStyle> nodeColours   = new HashMap<>();


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
     */
    public void appendNewMermaidNode(String      currentNodeName,
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
            mermaidGraph.append(currentDisplayName);
            mermaidGraph.append("**\"}\n");
        }
    }


    /**
     * Append a line to the graph.
     *
     * @param end1Id identifier of the starting end
     * @param label label for the line
     * @param end2Id identifier of the ending end
     */
    public void appendMermaidLine(String end1Id,
                                  String label,
                                  String end2Id)
    {
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
    protected void addStyles()
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
     * Add a solution component to graph.
     *
     * @param parentNodeName identifier of the parent node (maybe null)
     * @param parentLinkLabel add label to parent link - also optional
     * @param solutionComponentElement element to process
     * @param solutionWireGUIDs list of solution wires already defined
     */
    protected void addSolutionComponentToGraph(String                   parentNodeName,
                                               String                   parentLinkLabel,
                                               SolutionComponentElement solutionComponentElement,
                                               List<String>             solutionWireGUIDs)

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
                    if ((line != null) && (! solutionWireGUIDs.contains(line.getElementHeader().getGUID())))
                    {
                        String relatedComponentDisplayName = line.getLinkedElement().getElementHeader().getGUID();

                        if (line.getLinkedElement().getProperties() != null)
                        {
                            relatedComponentDisplayName = line.getLinkedElement().getProperties().get(OpenMetadataProperty.DISPLAY_NAME.name);

                            if (relatedComponentDisplayName == null)
                            {
                                line.getLinkedElement().getProperties().get(OpenMetadataProperty.NAME.name);
                            }
                            if (relatedComponentDisplayName == null)
                            {
                                line.getLinkedElement().getProperties().get(OpenMetadataProperty.QUALIFIED_NAME.name);
                            }
                            if (relatedComponentDisplayName == null)
                            {
                                relatedComponentDisplayName = line.getLinkedElement().getElementHeader().getGUID();
                            }
                        }

                        appendNewMermaidNode(line.getLinkedElement().getElementHeader().getGUID(),
                                             relatedComponentDisplayName,
                                             solutionComponentElement.getElementHeader().getType().getTypeName(),
                                             VisualStyle.DEFAULT_SOLUTION_COMPONENT);

                        this.appendMermaidLine(line.getLinkedElement().getElementHeader().getGUID(),
                                               this.getListLabel(Collections.singletonList(this.addSpacesToTypeName(line.getElementHeader().getType().getTypeName()))),
                                               solutionComponentElement.getElementHeader().getGUID());

                        solutionWireGUIDs.add(line.getElementHeader().getGUID());
                    }
                }
            }

            if (solutionComponentElement.getWiredFromLinks() != null)
            {
                for (WiredSolutionComponent line : solutionComponentElement.getWiredFromLinks())
                {
                    if ((line != null) && (! solutionWireGUIDs.contains(line.getElementHeader().getGUID())))
                    {
                        this.appendMermaidLine(solutionComponentElement.getElementHeader().getGUID(),
                                               this.getListLabel(Collections.singletonList(this.addSpacesToTypeName(line.getElementHeader().getType().getTypeName()))),
                                               line.getLinkedElement().getElementHeader().getGUID());

                        solutionWireGUIDs.add(line.getElementHeader().getGUID());
                    }
                }
            }

            if (solutionComponentElement.getActors() != null)
            {
                for (RelatedMetadataElementSummary line : solutionComponentElement.getActors())
                {
                    if (line != null)
                    {
                        String actorRoleName = line.getRelatedElement().getProperties().get(OpenMetadataProperty.NAME.name);

                        if (actorRoleName == null)
                        {
                            actorRoleName = line.getRelatedElement().getProperties().get(OpenMetadataProperty.QUALIFIED_NAME.name);
                        }

                        appendNewMermaidNode(line.getRelatedElement().getElementHeader().getGUID(),
                                             actorRoleName,
                                             line.getRelatedElement().getElementHeader().getType().getTypeName(),
                                             VisualStyle.SOLUTION_ROLE);

                        String actorRoleDescription = line.getRelationshipProperties().get(OpenMetadataProperty.ROLE.name);

                        if (actorRoleDescription == null)
                        {
                            actorRoleDescription = this.addSpacesToTypeName(line.getRelatedElement().getElementHeader().getType().getTypeName());
                        }
                        this.appendMermaidLine(line.getRelatedElement().getElementHeader().getGUID(),
                                               this.getListLabel(Collections.singletonList(actorRoleDescription)),
                                               solutionComponentElement.getElementHeader().getGUID());
                    }
                }
            }

            if (parentNodeName != null)
            {
                this.appendMermaidLine(parentNodeName,
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
        addStyles();

        return mermaidGraph.toString();
    }
}
