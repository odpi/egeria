/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.dataengine.rest;

import java.util.List;
import java.util.Objects;

public class ProcessRequestBody extends AssetRequestBody {
    private String displayName;
    private List<String> ports;
    private List<String> deployedApis;
    private List<String> assets;

    public String getDisplayName() {
        return displayName;
    }


    public List<String> getPorts() {
        return ports;
    }

    public List<String> getDeployedApis() {
        return deployedApis;
    }

    public List<String> getAssets() {
        return assets;
    }

    @Override
    public String toString() {
        return "ProcessRequestBody{" +
                "displayName='" + displayName + '\'' +
                ", ports=" + ports +
                ", deployedApis=" + deployedApis +
                ", assets=" + assets +
                '}';
    }

    /**
     * JSON-like toString
     *
     * @return string containing the property names and values
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        ProcessRequestBody that = (ProcessRequestBody) o;
        return Objects.equals(displayName, that.displayName) &&
                Objects.equals(ports, that.ports) &&
                Objects.equals(deployedApis, that.deployedApis) &&
                Objects.equals(assets, that.assets);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), displayName, ports, deployedApis, assets);
    }
}

