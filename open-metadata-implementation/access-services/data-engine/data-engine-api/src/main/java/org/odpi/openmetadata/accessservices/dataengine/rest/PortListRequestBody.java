/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.dataengine.rest;

import java.util.List;
import java.util.Objects;

public class PortListRequestBody extends DataEngineOMASAPIRequestBody {
    private List<String> ports;

    public List<String> getPorts() {
        return ports;
    }

    @Override
    public String toString() {
        return "PortListRequestBody{" +
                ", ports=" + ports +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PortListRequestBody that = (PortListRequestBody) o;
        return Objects.equals(ports, that.ports);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ports);
    }
}
