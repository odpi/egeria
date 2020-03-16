/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.assetlineage.model;

import java.util.Objects;

/**
 * The lineage relationship represents the a lineage relationship in the lineage graph with self contained properties.
 */
public class LineageRelationship extends LineageEntity {
    private String firstEndGUID;
    private String secondEndGUID;

    /**
     * Gets the guid for the first end of the relationship
     *
     * @return the guid
     */
    public String getFirstEndGUID() {
        return firstEndGUID;
    }

    /**
     * Sets guid for the second end of the relationship
     *
     * @param firstEndGUID the guid
     */
    public void setFirstEndGUID(String firstEndGUID) {
        this.firstEndGUID = firstEndGUID;
    }

    /**
     * Gets the guid for the first end of the relationship
     *
     * @return the guid
     */
    public String getSecondEndGUID() {
        return secondEndGUID;
    }

    /**
     * Sets guid for the second end of the relationship
     *
     * @param secondEndGUID the guid
     */
    public void setSecondEndGUID(String secondEndGUID) {
        this.secondEndGUID = secondEndGUID;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        LineageRelationship that = (LineageRelationship) o;
        return Objects.equals(firstEndGUID, that.firstEndGUID) &&
                Objects.equals(secondEndGUID, that.secondEndGUID);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), firstEndGUID, secondEndGUID);
    }

    @Override
    public String toString() {
        return "LineageRelationship{" +
                "firstEndGUID='" + firstEndGUID + '\'' +
                ", secondEndGUID='" + secondEndGUID + '\'' +
                '}';
    }
}


