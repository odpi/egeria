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
    private Set<GraphContext> edges;
    private Map<String, Set<GraphContext>> neighbors;

    /**
     * Instantiates a new Asset context.
     */
    public AssetContext() {
        vertices = new HashSet<>();
        edges = new HashSet<>();
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
     * Add edge boolean.
     *
     * @param edge the edge
     * @return the boolean
     */
    public boolean addEdge(GraphContext edge) {
       if (!edges.add(edge)) return false;

        String guid = edge.getFromVertex().getGuid();

        neighbors.putIfAbsent(guid, new HashSet<>());

        neighbors.get(guid).add(edge);

        return true;
    }

    /**
     * Get vertices set.
     *
     * @return the set
     */
    public Set<LineageEntity> getVertices(){
        return vertices;
    }

    /**
     * Gets edges.
     *
     * @return the edges
     */
    public Set<GraphContext> getEdges() {
        return edges;
    }

    /**
     * Sets edges.
     *
     * @param edges the edges
     */
    public void setEdges(Set<GraphContext> edges) {
        this.edges = edges;
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
                ", edges=" + edges +
                ", neighbors=" + neighbors +
                '}';
    }
}




