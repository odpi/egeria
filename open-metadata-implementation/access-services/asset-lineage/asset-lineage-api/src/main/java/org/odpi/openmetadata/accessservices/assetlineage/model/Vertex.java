/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.assetlineage.model;

import java.util.Objects;

public class Vertex {

    private LineageEntity entity;

    public Vertex(LineageEntity entity){
        this.entity = entity;
    }

    public LineageEntity getEntity() {
        return entity;
    }

    public void setEntity(LineageEntity entity) {
        this.entity = entity;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Vertex that = (Vertex) o;
        return Objects.equals(entity, that.entity);
    }

    @Override
    public int hashCode() {
        return Objects.hash(entity);
    }

    @Override
    public String toString() {
        return "Vertex";
    }
}
