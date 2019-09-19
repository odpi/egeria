/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.assetlineage;

import java.util.Objects;

public class Edge {

    private String relationshipType;
    private LineageEntity fromVertex;
    private LineageEntity toVertex;

   public  Edge(String relationshipType, LineageEntity fromVertex, LineageEntity toVertex) {
       this.relationshipType = relationshipType;
        this.fromVertex = fromVertex;
        this.toVertex = toVertex;
    }

    public String getRelationshipType() {
        return relationshipType;
    }

    public void setRelationshipType(String relationshipType) {
        this.relationshipType = relationshipType;
    }

    public LineageEntity getFromVertex() {
        return fromVertex;
    }

    public void setFromVertex(LineageEntity fromVertex) {
        this.fromVertex = fromVertex;
    }

    public LineageEntity getToVertex() {
        return toVertex;
    }

    public void setToVertex(LineageEntity toVertex) {
        this.toVertex = toVertex;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Edge edgeTest = (Edge) o;
        return Objects.equals(relationshipType, edgeTest.relationshipType) &&
                Objects.equals(fromVertex, edgeTest.fromVertex) &&
                Objects.equals(toVertex, edgeTest.toVertex);
    }

    @Override
    public int hashCode() {
        return Objects.hash(relationshipType, fromVertex, toVertex);
    }
}
