/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.dataengine.rest;

import java.util.Objects;

public class ProcessRequestBody extends AssetRequestBody {
    private String displayName;
    private String parentProcessGuid;
    private String portGuid;

    public String getDisplayName() {
        return displayName;
    }

    public String getParentProcessGuid() {
        return parentProcessGuid;
    }

    public String getPortGuid() {
        return portGuid;
    }

    /**
     * JSON-like toString
     *
     * @return string containing the property names and values
     */
    @Override
    public String toString() {
        return "ProcessRequestBody{" +
                ", displayName='" + displayName + '\'' +
                ", parentProcessId='" + parentProcessGuid + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        ProcessRequestBody that = (ProcessRequestBody) o;
        return Objects.equals(displayName, that.displayName) &&
                Objects.equals(parentProcessGuid, that.parentProcessGuid) &&
                Objects.equals(portGuid, that.portGuid);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), displayName, parentProcessGuid, portGuid);
    }
}

