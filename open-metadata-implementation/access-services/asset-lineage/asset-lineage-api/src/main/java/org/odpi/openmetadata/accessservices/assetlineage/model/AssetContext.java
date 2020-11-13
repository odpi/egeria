/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.assetlineage.model;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * The asset context provides the entire lineage graph with vertices and edges.
 */
public class AssetContext {

    private Set<LineageEntity> vertices;
    private Set<GraphContext> graphContexts;
    private Map<String, Set<GraphContext>> neighbors;

    /**
     * Instantiates a new Asset context.
     */
    public AssetContext() {
        vertices = new HashSet<>();
        graphContexts = new HashSet<>();
        neighbors = new HashMap<>();
    }

    /**
     * Add vertex boolean.
     *
     * @param Vertex the vertex
     * @return the boolean
     */
    public boolean addVertex(LineageEntity Vertex) {
        return vertices.add(Vertex);
    }


    /**
     * Add graphContext boolean.
     *
     * @param graphContext the graphContext
     * @return the boolean
     */
    public boolean addGraphContext(GraphContext graphContext) {
        if (!graphContexts.add(graphContext)) return false;
        String guid = graphContext.getFromVertex().getGuid();
        neighbors.putIfAbsent(guid, new HashSet<>());
        neighbors.get(guid).add(graphContext);
        return true;
    }

    /**
     * Get vertices set.
     *
     * @return the set
     */
    public Set<LineageEntity> getVertices() {
        return vertices;
    }

    /**
     * Gets edges.
     *
     * @return the edges
     */
    public Set<GraphContext> getGraphContexts() {
        return graphContexts;
    }

    /**
     * Sets edges.
     *
     * @param graphContexts the edges
     */
    public void setGraphContexts(Set<GraphContext> graphContexts) {
        this.graphContexts = graphContexts;
    }

    /**
     * Gets neighbors.
     *
     * @return the neighbors
     */
    public Map<String, Set<GraphContext>> getNeighbors() {
        return neighbors;
    }

    @Override
    public String toString() {
        return "AssetContext{" +
                "vertices=" + vertices +
                ", graphContexts=" + graphContexts +
                ", neighbors=" + neighbors +
                '}';
    }
}




