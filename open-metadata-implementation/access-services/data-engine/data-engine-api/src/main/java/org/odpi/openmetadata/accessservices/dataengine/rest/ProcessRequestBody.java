/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.dataengine.rest;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

@JsonAutoDetect(getterVisibility = PUBLIC_ONLY, setterVisibility = PUBLIC_ONLY, fieldVisibility = NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ProcessRequestBody extends DataEngineOMASAPIRequestBody {
    private org.odpi.openmetadata.accessservices.dataengine.model.Process process;

    public org.odpi.openmetadata.accessservices.dataengine.model.Process getProcess() {
        return process;
    }

    public void setProcess(org.odpi.openmetadata.accessservices.dataengine.model.Process process) {
        this.process = process;
    }

    @Override
    public String toString() {
        return "ProcessRequestBody{" +
                "process=" + process +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProcessRequestBody that = (ProcessRequestBody) o;
        return Objects.equals(process, that.process);
    }

    @Override
    public int hashCode() {
        return Objects.hash(process);
    }
}