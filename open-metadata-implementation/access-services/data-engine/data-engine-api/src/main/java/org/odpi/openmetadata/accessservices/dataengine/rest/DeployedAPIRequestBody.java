/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.dataengine.rest;

import java.util.Objects;

public class DeployedAPIRequestBody extends AssetRequestBody {
    private String assetGuid;

    public String getAssetGuid() {
        return assetGuid;
    }

    /**
     * JSON-like toString
     *
     * @return string containing the property names and values
     */
    @Override
    public String toString() {
        return "DeployedAPIRequestBody{" +
                "assetGuid='" + assetGuid + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        DeployedAPIRequestBody that = (DeployedAPIRequestBody) o;
        return Objects.equals(assetGuid, that.assetGuid);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), assetGuid);
    }
}