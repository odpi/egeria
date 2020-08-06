/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.assetlineage.model;

import java.util.Objects;

/**
 * The lineage relationship represents the a lineage relationship in the lineage graph with self contained properties.
 */
public class LineageRelationship extends LineageEntity {

    private LineageEntity firstEntity;
    private LineageEntity secondEntity;

    /**
     * Gets the entity for the first end of the relationship
     *
     * @return the guid
     */
    public LineageEntity getFirstEntity() {
        return firstEntity;
    }

    /**
     * Sets the entity for the first end of the relationship
     *
     * @param firstEntity the guid
     */
    public void setFirstEntity(LineageEntity firstEntity) {
        this.firstEntity = firstEntity;
    }

    /**
     * Gets the entity for the second end of the relationship
     *
     * @return the guid
     */
    public LineageEntity getSecondEntity() {
        return secondEntity;
    }


    /**
     * Sets the entity for the second end of the relationship
     *
     * @param secondEntity the guid
     */
    public void setSecondEntity(LineageEntity secondEntity) {
        this.secondEntity = secondEntity;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        LineageRelationship that = (LineageRelationship) o;
        return Objects.equals(firstEntity, that.firstEntity) &&
                Objects.equals(secondEntity, that.secondEntity);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), firstEntity, secondEntity);
    }

    @Override
    public String toString() {
        return "LineageRelationship{" +
                "firstEntity=" + firstEntity +
                ", secondEntity=" + secondEntity +
                '}';
    }
}


