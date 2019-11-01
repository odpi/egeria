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

@JsonAutoDetect(getterVisibility = PUBLIC_ONLY, setterVisibility = PUBLIC_ONLY, fieldVisibility = NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class PortImplementationEvent extends DataEngineEventHeader{

    private PortImplementation portImplementation;

    public PortImplementation getPortImplementation() {

        return portImplementation;
    }

    public void setPortImplementation(PortImplementation portImplementation) {
        this.portImplementation = portImplementation;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PortImplementationEvent that = (PortImplementationEvent) o;
        return Objects.equals(portImplementation, that.portImplementation);
    }

    @Override
    public int hashCode() {
        return Objects.hash(portImplementation);
    }

    @Override
    public String toString() {
        return "PortImplementationEvent{" +
                "portImplementation=" + portImplementation +
                '}';
    }
}
