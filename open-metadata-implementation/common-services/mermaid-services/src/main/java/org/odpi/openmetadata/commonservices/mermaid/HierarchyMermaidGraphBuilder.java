/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.commonservices.mermaid;


/**
 * Creates a mermaid graph rendering of a hierarchy.  It relies on the caller to add the nodes and lines
 */
public class HierarchyMermaidGraphBuilder extends MermaidGraphBuilderBase
{
    /**
     * Construct a mermaid markdown graph.
     *
     * @param title title of graph
     * @param displayName displayName of the starting element
     * @param startingGUID guid of the starting element
     */
    public HierarchyMermaidGraphBuilder(String title,
                                        String startingGUID,
                                        String displayName)
    {
        mermaidGraph.append("---\n");
        mermaidGraph.append("title: ");
        mermaidGraph.append(title);
        mermaidGraph.append(" - ");
        mermaidGraph.append(displayName);
        mermaidGraph.append(" [");
        mermaidGraph.append(startingGUID);
        mermaidGraph.append("]\n---\nflowchart TD\n%%{init: {\"flowchart\": {\"htmlLabels\": false}} }%%\n\n");
    }
}
