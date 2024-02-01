/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.viewservices.assetcatalog.beans;

import java.util.List;

/**
 * Graph holds the nodes (vertices) and edges of a subgraph extracted from the open metadata repositories.
 */
public class Graph
{
    private List<RuleNode> nodes;
    private List<Edge>     edges;

    public Graph(List<RuleNode> nodes, List<Edge> edge)
    {
        this.nodes = nodes;
        this.edges = edge;
    }

    public List<RuleNode> getNodes()
    {
        return nodes;
    }

    public void setNodes(List<RuleNode> nodes)
    {
        this.nodes = nodes;
    }

    public List<Edge> getEdges()
    {
        return edges;
    }

    public void setEdges(List<Edge> edges)
    {
        this.edges = edges;
    }
}
