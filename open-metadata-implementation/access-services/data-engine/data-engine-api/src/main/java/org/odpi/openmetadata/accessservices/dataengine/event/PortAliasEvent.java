/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.dataengine.event;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.accessservices.dataengine.model.PortAlias;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * The port alias event of Data Engine OMAS.
 */
@JsonAutoDetect(getterVisibility = PUBLIC_ONLY, setterVisibility = PUBLIC_ONLY, fieldVisibility = NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class PortAliasEvent extends DataEngineEventHeader {
    private String processQualifiedName;
    private PortAlias portAlias;

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
     * Gets port.
     *
     * @return the port
     */
    public PortAlias getPort() {
        return portAlias;
    }

    /**
     * Sets port.
     *
     * @param portAlias the port alias
     */
    public void setPort(PortAlias portAlias) {
        this.portAlias = portAlias;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PortAliasEvent that = (PortAliasEvent) o;
        return Objects.equals(portAlias, that.portAlias);
    }

    @Override
    public int hashCode() {
        return Objects.hash(portAlias);
    }

    @Override
    public String toString() {
        return "PortAliasEvent{" +
                "portAlias=" + portAlias +
                "} " + super.toString();
    }
}
