/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.assetlineage;

import java.util.Objects;

public class GraphContext {

    private String relationshipType;
    private String relationshipGuid;
    private LineageEntity fromVertex;
    private LineageEntity toVertex;

    public GraphContext(){}
    
    public GraphContext(String relationshipType, String relationshipGuid, LineageEntity fromVertex, LineageEntity toVertex) {
       this.relationshipType = relationshipType;
       this.relationshipGuid = relationshipGuid;
       this.fromVertex = fromVertex;
       this.toVertex = toVertex;
    }

    public String getRelationshipType() {
        return relationshipType;
    }

    public void setRelationshipType(String relationshipType) {
        this.relationshipType = relationshipType;
    }

    public String getRelationshipGuid() {
        return relationshipGuid;
    }

    public void setRelationshipGuid(String relationshipGuid) {
        this.relationshipGuid = relationshipGuid;
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
        GraphContext edgeTest = (GraphContext) o;
        return Objects.equals(relationshipType, edgeTest.relationshipType) &&
                Objects.equals(fromVertex, edgeTest.fromVertex) &&
                Objects.equals(toVertex, edgeTest.toVertex);
    }

    @Override
    public int hashCode() {
        return Objects.hash(relationshipType, fromVertex, toVertex);
    }
}
