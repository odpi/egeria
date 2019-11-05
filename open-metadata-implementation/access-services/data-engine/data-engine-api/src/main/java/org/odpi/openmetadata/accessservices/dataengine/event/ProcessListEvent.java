/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.dataengine.event;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * The process list event of Data Engine OMAS.
 */
@JsonAutoDetect(getterVisibility = PUBLIC_ONLY, setterVisibility = PUBLIC_ONLY, fieldVisibility = NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ProcessListEvent extends DataEngineEventHeader{

    private List<String> failedGUIDs;

    /**
     * Gets failed gui ds.
     *
     * @return the failed gui ds
     */
    public List<String> getFailedGUIDs() {
        return failedGUIDs;
    }

    /**
     * Sets failed gui ds.
     *
     * @param failedGUIDs the failed gui ds
     */
    public void setFailedGUIDs(List<String> failedGUIDs) {
        this.failedGUIDs = failedGUIDs;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProcessListEvent that = (ProcessListEvent) o;
        return Objects.equals(failedGUIDs, that.failedGUIDs);
    }

    @Override
    public int hashCode() {
        return Objects.hash(failedGUIDs);
    }

    @Override
    public String toString() {
        return "ProcessListEvent{" +
                "failedGUIDs=" + failedGUIDs +
                "} " + super.toString();
    }
}
