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
 * The process to port list event of Data Engine OMAS.
 */
@JsonAutoDetect(getterVisibility = PUBLIC_ONLY, setterVisibility = PUBLIC_ONLY, fieldVisibility = NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ProcessToPortListEvent extends DataEngineEventHeader{

    private String processGUID;

    private List<String> ports;

    /**
     * Gets process guid.
     *
     * @return the process guid
     */
    public String getProcessGUID() {
        return processGUID;
    }

    /**
     * Sets process guid.
     *
     * @param processGUID the process guid
     */
    public void setProcessGUID(String processGUID) {
        this.processGUID = processGUID;
    }

    /**
     * Gets ports.
     *
     * @return the ports
     */
    public List<String> getPorts() {
        return ports;
    }

    /**
     * Sets ports.
     *
     * @param ports the ports
     */
    public void setPorts(List<String> ports) {
        this.ports = ports;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProcessToPortListEvent that = (ProcessToPortListEvent) o;
        return Objects.equals(processGUID, that.processGUID) &&
                Objects.equals(ports, that.ports);
    }

    @Override
    public int hashCode() {
        return Objects.hash(processGUID, ports);
    }

    @Override
    public String toString() {
        return "ProcessToPortListEvent{" +
                "processGUID='" + processGUID + '\'' +
                ", ports=" + ports +
                "} " + super.toString();
    }
}
