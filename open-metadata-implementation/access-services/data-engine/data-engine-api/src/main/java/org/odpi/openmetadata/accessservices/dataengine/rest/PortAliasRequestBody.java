/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.dataengine.rest;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.accessservices.dataengine.model.PortAlias;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

@JsonAutoDetect(getterVisibility = PUBLIC_ONLY, setterVisibility = PUBLIC_ONLY, fieldVisibility = NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class PortAliasRequestBody extends DataEngineOMASAPIRequestBody {
    private String processQualifiedName;
    private PortAlias portAlias;

    public String getProcessQualifiedName() {
        return processQualifiedName;
    }

    public void setProcessQualifiedName(String processQualifiedName) {
        this.processQualifiedName = processQualifiedName;
    }

    public PortAlias getPortAlias() {
        return portAlias;
    }

    public void setPortAlias(PortAlias portAlias) {
        this.portAlias = portAlias;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PortAliasRequestBody that = (PortAliasRequestBody) o;
        return Objects.equals(processQualifiedName, that.processQualifiedName) &&
                Objects.equals(portAlias, that.portAlias);
    }

    @Override
    public int hashCode() {
        return Objects.hash(processQualifiedName, portAlias);
    }

    @Override
    public String toString() {
        return "PortAliasRequestBody{" +
                "processQualifiedName='" + processQualifiedName + '\'' +
                ", portAlias=" + portAlias +
                '}';
    }
}
