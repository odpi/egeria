/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.dataengine.event;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.accessservices.dataengine.model.PortImplementation;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * The port implementation event of Data Engine OMAS.
 */
@JsonAutoDetect(getterVisibility = PUBLIC_ONLY, setterVisibility = PUBLIC_ONLY, fieldVisibility = NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class PortImplementationEvent extends DataEngineEventHeader {
    private String processQualifiedName;
    private PortImplementation portImplementation;

    /**
     * Gets the process qualified name.
     *
     * @return the process qualified name
     */
    public String getProcessQualifiedName() {
        return processQualifiedName;
    }

    /**
     * Sets the process qualified name.
     *
     * @param processQualifiedName the process qualified name
     */
    public void setProcessQualifiedName(String processQualifiedName) {
        this.processQualifiedName = processQualifiedName;
    }

    /**
     * Gets port implementation.
     *
     * @return the port implementation
     */
    public PortImplementation getPortImplementation() {

        return portImplementation;
    }

    /**
     * Sets port implementation.
     *
     * @param portImplementation the port implementation
     */
    public void setPortImplementation(PortImplementation portImplementation) {
        this.portImplementation = portImplementation;
    }

    @Override
    public String toString() {
        return "PortImplementationEvent{" +
                "processQualifiedName='" + processQualifiedName + '\'' +
                ", portImplementation=" + portImplementation +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PortImplementationEvent that = (PortImplementationEvent) o;
        return Objects.equals(processQualifiedName, that.processQualifiedName) &&
                Objects.equals(portImplementation, that.portImplementation);
    }

    @Override
    public int hashCode() {
        return Objects.hash(processQualifiedName, portImplementation);
    }
}
