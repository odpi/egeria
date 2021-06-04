/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.assetlineage.model;

import java.util.Objects;

/**
 * A graph context contains two LineageEntity and the relationship between them.
 */
public class GraphContext {

    private String relationshipType;
    private String relationshipGuid;
    private LineageEntity fromVertex;
    private LineageEntity toVertex;

    /**
     * Instantiates a new Graph context.
     */
    public GraphContext() {
    }

    /**
     * Instantiates a new Graph context.
     *
     * @param relationshipType the relationship type
     * @param relationshipGuid the relationship guid
     * @param fromVertex       the from vertex
     * @param toVertex         the to vertex
     */
    public GraphContext(String relationshipType, String relationshipGuid, LineageEntity fromVertex, LineageEntity toVertex) {
        this.relationshipType = relationshipType;
        this.relationshipGuid = relationshipGuid;
        this.fromVertex = fromVertex;
        this.toVertex = toVertex;
    }

    /**
     * Gets relationship type.
     *
     * @return the relationship type
     */
    public String getRelationshipType() {
        return relationshipType;
    }

    /**
     * Sets relationship type.
     *
     * @param relationshipType the relationship type
     */
    public void setRelationshipType(String relationshipType) {
        this.relationshipType = relationshipType;
    }

    /**
     * Gets relationship guid.
     *
     * @return the relationship guid
     */
    public String getRelationshipGuid() {
        return relationshipGuid;
    }

    /**
     * Sets relationship guid.
     *
     * @param relationshipGuid the relationship guid
     */
    public void setRelationshipGuid(String relationshipGuid) {
        this.relationshipGuid = relationshipGuid;
    }

    /**
     * Gets from vertex.
     *
     * @return the from vertex
     */
    public LineageEntity getFromVertex() {
        return fromVertex;
    }

    /**
     * Sets from vertex.
     *
     * @param fromVertex the from vertex
     */
    public void setFromVertex(LineageEntity fromVertex) {
        this.fromVertex = fromVertex;
    }

    /**
     * Gets to vertex.
     *
     * @return the to vertex
     */
    public LineageEntity getToVertex() {
        return toVertex;
    }

    /**
     * Sets to vertex.
     *
     * @param toVertex the to vertex
     */
    public void setToVertex(LineageEntity toVertex) {
        this.toVertex = toVertex;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GraphContext edgeTest = (GraphContext) o;
        return Objects.equals(relationshipType, edgeTest.relationshipType) &&
                Objects.equals(fromVertex, edgeTest.fromVertex) &&
                Objects.equals(toVertex, edgeTest.toVertex);
    }

    @Override
    public int hashCode() {
        return Objects.hash(relationshipType, fromVertex, toVertex);
    }

    @Override
    public String toString() {
        return "GraphContext{" +
                "relationshipType='" + relationshipType + '\'' +
                ", relationshipGuid='" + relationshipGuid + '\'' +
                ", fromVertex=" + fromVertex +
                ", toVertex=" + toVertex +
                '}';
    }
}

