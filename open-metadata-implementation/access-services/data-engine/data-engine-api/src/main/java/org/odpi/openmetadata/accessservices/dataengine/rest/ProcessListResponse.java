/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.dataengine.rest;

import org.odpi.openmetadata.commonservices.ffdc.rest.GUIDListResponse;

import java.util.List;
import java.util.Objects;

/**
 * ProcessListResponse is the response structure used on the DE OMAS REST API calls that return a
 * list of process unique identifiers (guids) as a response.
 */
public class ProcessListResponse extends GUIDListResponse {
    private List<String> failedGUIDs;

    public List<String> getFailedGUIDs() {
        return failedGUIDs;
    }

    public void setFailedGUIDs(List<String> failedProcesses) {
        this.failedGUIDs = failedProcesses;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        ProcessListResponse that = (ProcessListResponse) o;
        return Objects.equals(failedGUIDs, that.failedGUIDs);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), failedGUIDs);
    }

    @Override
    public String toString() {
        return "ProcessListResponse{" +
                "failedGUIDs=" + failedGUIDs +
                '}';
    }
}
