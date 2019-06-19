/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.dataengine.model;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

@JsonAutoDetect(getterVisibility = PUBLIC_ONLY, setterVisibility = PUBLIC_ONLY, fieldVisibility = NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class PortDelegation {
    private String portSource;
    private String portTarget;

    public String getPortSource() {
        return portSource;
    }

    public void setPortSource(String portSource) {
        this.portSource = portSource;
    }

    public String getPortTarget() {
        return portTarget;
    }

    public void setPortTarget(String portTarget) {
        this.portTarget = portTarget;
    }

    @Override
    public String toString() {
        return "PortDelegation{" +
                "portSource='" + portSource + '\'' +
                ", portTarget='" + portTarget + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PortDelegation that = (PortDelegation) o;
        return Objects.equals(portSource, that.portSource) &&
                Objects.equals(portTarget, that.portTarget);
    }

    @Override
    public int hashCode() {
        return Objects.hash(portSource, portTarget);
    }
}
