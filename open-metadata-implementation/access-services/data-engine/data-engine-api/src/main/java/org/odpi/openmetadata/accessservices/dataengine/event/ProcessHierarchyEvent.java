/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.dataengine.event;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.accessservices.dataengine.model.ProcessHierarchy;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * The process hierarchy event of Data Engine OMAS.
 */
@JsonAutoDetect(getterVisibility = PUBLIC_ONLY, setterVisibility = PUBLIC_ONLY, fieldVisibility = NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ProcessHierarchyEvent extends DataEngineEventHeader{

    private ProcessHierarchy processHierarchy;

    /**
     * Gets process hierarchy.
     *
     * @return the process hierarchy
     */
    public ProcessHierarchy getProcessHierarchy() {
        return processHierarchy;
    }

    /**
     * Sets process hierarchy.
     *
     * @param processHierarchy the process hierarchy
     */
    public void setProcessHierarchy(ProcessHierarchy processHierarchy) {
        this.processHierarchy = processHierarchy;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProcessHierarchyEvent that = (ProcessHierarchyEvent) o;
        return Objects.equals(processHierarchy, that.processHierarchy);
    }

    @Override
    public int hashCode() {
        return Objects.hash(processHierarchy);
    }

    @Override
    public String toString() {
        return "ProcessHierarchyEvent{" +
                "processHierarchy=" + processHierarchy +
                "} " + super.toString();
    }
}
