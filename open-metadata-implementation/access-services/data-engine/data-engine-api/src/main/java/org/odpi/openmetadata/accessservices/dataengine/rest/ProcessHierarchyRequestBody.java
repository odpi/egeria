/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.dataengine.rest;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.accessservices.dataengine.model.PortAlias;
import org.odpi.openmetadata.accessservices.dataengine.model.ProcessHierarchy;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

@JsonAutoDetect(getterVisibility = PUBLIC_ONLY, setterVisibility = PUBLIC_ONLY, fieldVisibility = NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ProcessHierarchyRequestBody extends DataEngineOMASAPIRequestBody {
    private ProcessHierarchy processHierarchy;

    public ProcessHierarchy getProcessHierarchy() {
        return processHierarchy;
    }

    public void setProcessHierarchy(ProcessHierarchy processHierarchy) {
        this.processHierarchy = processHierarchy;
    }

    @Override
    public String toString() {
        return "ProcessHierarchyRequestBody{" +
                "processHierarchy=" + processHierarchy +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProcessHierarchyRequestBody that = (ProcessHierarchyRequestBody) o;
        return Objects.equals(processHierarchy, that.processHierarchy);
    }

    @Override
    public int hashCode() {
        return Objects.hash(processHierarchy);
    }
}
