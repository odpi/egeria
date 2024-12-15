/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.commonservices.mermaid;

import java.util.List;

/**
 * Provides the basic functions for building flowchart based graph visualizations
 */
public class MermaidGraphBuilderBase
{
    protected final StringBuilder mermaidGraph = new StringBuilder();

    /**
     * Convert an array into a comma separated string.
     *
     * @param labelValues array of labels
     * @return string value without square brackets (Mermaid does not allow them)
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
                    stringBuilder.append(",");
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
        mermaidGraph.append(currentType);
        mermaidGraph.append("*\n**");
        mermaidGraph.append(currentDisplayName);
        mermaidGraph.append("**`\")\n");
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
            mermaidGraph.append("-->|");
            mermaidGraph.append(label);
            mermaidGraph.append("|");
        }
        else
        {
            mermaidGraph.append("-->");
        }

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
     * Return the built mermaid graph.
     *
     * @return string markdown
     */
    public String getMermaidGraph()
    {
        return mermaidGraph.toString();
    }
}
