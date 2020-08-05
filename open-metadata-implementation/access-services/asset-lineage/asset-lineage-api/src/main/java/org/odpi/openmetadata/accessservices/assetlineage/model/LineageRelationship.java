/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.assetlineage.model;

import java.util.Objects;

/**
 * The lineage relationship represents the a lineage relationship in the lineage graph with self contained properties.
 */
public class LineageRelationship extends LineageEntity {

    private LineageEntity sourceEntity;
    private LineageEntity targetEntity;

    /**
     * Gets the source entity of the relationship
     *
     * @return the guid
     */
    public LineageEntity getSourceEntity() {
        return sourceEntity;
    }

    /**
     * Sets the source entity of the relationship
     *
     * @param sourceEntity the guid
     */
    public void setSourceEntity(LineageEntity sourceEntity) {
        this.sourceEntity = sourceEntity;
    }

    /**
     * Gets the target entity of the relationship
     *
     * @return the guid
     */
    public LineageEntity getTargetEntity() {
        return targetEntity;
    }


    /**
     * Sets the target entity of the relationship
     *
     * @param targetEntity the guid
     */
    public void setTargetEntity(LineageEntity targetEntity) {
        this.targetEntity = targetEntity;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        LineageRelationship that = (LineageRelationship) o;
        return Objects.equals(sourceEntity, that.sourceEntity) &&
                Objects.equals(targetEntity, that.targetEntity);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), sourceEntity, targetEntity);
    }

    @Override
    public String toString() {
        return "LineageRelationship{" +
                "firstEntity=" + sourceEntity +
                ", secondEntity=" + targetEntity +
                '}';
    }
}


