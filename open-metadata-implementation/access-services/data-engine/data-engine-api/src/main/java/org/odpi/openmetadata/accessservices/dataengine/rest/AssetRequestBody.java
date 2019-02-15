/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.dataengine.rest;

import java.util.List;
import java.util.Objects;

public abstract class AssetRequestBody extends DataEngineOMASAPIRequestBody {
    private String name;
    private String description;
    private String latestChange;
    private List<String> zoneMembership;

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getLatestChange() {
        return latestChange;
    }

    public List<String> getZoneMembership() {
        return zoneMembership;
    }

    /**
     * JSON-like toString
     *
     * @return string containing the property names and values
     */
    @Override
    public String toString() {
        return "ProcessRequestBody{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", latestChange='" + latestChange + '\'' +
                ", zoneMembership='" + zoneMembership + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AssetRequestBody that = (AssetRequestBody) o;
        return Objects.equals(name, that.name) &&

                Objects.equals(description, that.description) &&
                Objects.equals(latestChange, that.latestChange) &&
                Objects.equals(zoneMembership, that.zoneMembership);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, description, latestChange, zoneMembership);
    }
}

