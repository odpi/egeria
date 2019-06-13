/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.dataengine.rest;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.accessservices.dataengine.model.PortType;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

@JsonAutoDetect(getterVisibility = PUBLIC_ONLY, setterVisibility = PUBLIC_ONLY, fieldVisibility = NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class PortImplementationRequestBody extends PortRequestBody {
    private String schemaTypeGUID;
    private PortType portType;

    public String getSchemaTypeGUID() {
        return schemaTypeGUID;
    }

    public void setSchemaTypeGUID(String schemaTypeGUID) {
        this.schemaTypeGUID = schemaTypeGUID;
    }

    public PortType getPortType() {
        return portType;
    }

    public void setPortType(PortType portType) {
        this.portType = portType;
    }

    /**
     * JSON-like toString
     *
     * @return string containing the property names and values
     */
    @Override
    public String toString() {
        return "PortImplementationRequestBody{" +
                "schemaTypeGUID='" + schemaTypeGUID + '\'' +
                ", portType=" + portType +
                ", displayName='" + displayName + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PortImplementationRequestBody that = (PortImplementationRequestBody) o;
        return Objects.equals(schemaTypeGUID, that.schemaTypeGUID) &&
                portType == that.portType;
    }

    @Override
    public int hashCode() {
        return Objects.hash(schemaTypeGUID, portType);
    }
}