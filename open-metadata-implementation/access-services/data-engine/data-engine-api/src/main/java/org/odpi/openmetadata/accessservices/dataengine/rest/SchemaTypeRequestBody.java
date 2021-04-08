/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.dataengine.rest;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.odpi.openmetadata.accessservices.dataengine.model.SchemaType;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

@JsonAutoDetect(getterVisibility = PUBLIC_ONLY, setterVisibility = PUBLIC_ONLY, fieldVisibility = NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class SchemaTypeRequestBody extends DataEngineOMASAPIRequestBody {
    private String portQualifiedName;
    @JsonProperty("schema")
    private SchemaType schemaType;

    public String getPortQualifiedName() {
        return portQualifiedName;
    }

    public void setPortQualifiedName(String portQualifiedName) {
        this.portQualifiedName = portQualifiedName;
    }

    public SchemaType getSchemaType() {
        return schemaType;
    }

    public void setSchemaType(SchemaType schemaType) {
        this.schemaType = schemaType;
    }

    @Override
    public String toString() {
        return "SchemaTypeRequestBody{" +
                "portQualifiedName='" + portQualifiedName + '\'' +
                ", schemaType=" + schemaType +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SchemaTypeRequestBody that = (SchemaTypeRequestBody) o;
        return Objects.equals(portQualifiedName, that.portQualifiedName) &&
                Objects.equals(schemaType, that.schemaType);
    }

    @Override
    public int hashCode() {
        return Objects.hash(portQualifiedName, schemaType);
    }
}
