/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.dataengine.rest;

import java.util.Objects;

public class PortRequestBody extends DataEngineOMASAPIRequestBody {
    private String deployedAPIGuid;
    private String displayName;

    public String getDeployedAPIGuid() {
        return deployedAPIGuid;
    }

    /**
     * JSON-like toString
     *
     * @return string containing the property names and values
     */
    @Override
    public String toString() {
        return "PortRequestBody{" +
                "deployedAPIGuid='" + deployedAPIGuid + '\'' +
                ", displayName='" + displayName + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        PortRequestBody that = (PortRequestBody) o;
        return Objects.equals(deployedAPIGuid, that.deployedAPIGuid) &&
                Objects.equals(displayName, that.displayName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), deployedAPIGuid, displayName);
    }

    public String getDisplayName() {
        return displayName;
    }

}