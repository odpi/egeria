/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.dataengine.model;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

@JsonAutoDetect(getterVisibility = PUBLIC_ONLY, setterVisibility = PUBLIC_ONLY, fieldVisibility = NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class PortImplementation extends Port {
    private static final long serialVersionUID = 1L;
    private PortType portType;
    @JsonProperty("schema")
    private SchemaType schemaType;

    public PortType getPortType() {
        return portType;
    }

    public void setPortType(PortType portType) {
        this.portType = portType;
    }

    public SchemaType getSchemaType() {
        return schemaType;
    }

    public void setSchemaType(SchemaType schemaType) {
        this.schemaType = schemaType;
    }

    /**
     * JSON-like toString
     *
     * @return string containing the property names and values
     */
    @Override
    public String toString() {
        return "PortImplementation{" +
                "portType=" + portType +
                ", schemaType=" + schemaType +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        PortImplementation that = (PortImplementation) o;
        return portType == that.portType &&
                Objects.equals(schemaType, that.schemaType);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), portType, schemaType);
    }
}
