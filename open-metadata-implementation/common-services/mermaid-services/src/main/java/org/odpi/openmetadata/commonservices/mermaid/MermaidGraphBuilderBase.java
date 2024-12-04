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
     String getListLabel(List<String> labelValues)
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
     * @param mermaidGraph current state of the graph
     * @param currentNodeName unique name/identifier
     * @param currentDisplayName display name
     * @param currentType type of element
     */
    void appendMermaidNode(StringBuilder mermaidGraph,
                           String currentNodeName,
                           String currentDisplayName,
                           String currentType)
    {
        mermaidGraph.append(this.removeSpaces(currentNodeName));
        mermaidGraph.append("(\"`*");
        mermaidGraph.append(currentType);
        mermaidGraph.append("*\n**");
        mermaidGraph.append(currentDisplayName);
        mermaidGraph.append("**`\")\n");
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
