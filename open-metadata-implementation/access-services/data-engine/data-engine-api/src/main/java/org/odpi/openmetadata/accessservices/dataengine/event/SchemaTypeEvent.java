/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.dataengine.event;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.accessservices.dataengine.model.SchemaType;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * The Schema type event of Data Engine OMAS.
 */
@JsonAutoDetect(getterVisibility = PUBLIC_ONLY, setterVisibility = PUBLIC_ONLY, fieldVisibility = NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class SchemaTypeEvent extends DataEngineEventHeader{
    private String portQualifiedName;
    private SchemaType schemaType;

    /**
     * Gets the port qualified name.
     *
     * @return the port qualified name
     */
    public String getPortQualifiedName() {
        return portQualifiedName;
    }

    /**
     * Sets the port qualified name.
     *
     * @param portQualifiedName the port qualified name
     */
    public void setPortQualifiedName(String portQualifiedName) {
        this.portQualifiedName = portQualifiedName;
    }

    /**
     * Gets schema type.
     *
     * @return the schema type
     */
    public SchemaType getSchemaType() {
        return schemaType;
    }

    /**
     * Sets schema type.
     *
     * @param schemaType the schema type
     */
    public void setSchemaType(SchemaType schemaType) {
        this.schemaType = schemaType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SchemaTypeEvent that = (SchemaTypeEvent) o;
        return Objects.equals(portQualifiedName, that.portQualifiedName) &&
                Objects.equals(schemaType, that.schemaType);
    }

    @Override
    public int hashCode() {
        return Objects.hash(portQualifiedName, schemaType);
    }

    @Override
    public String toString() {
        return "SchemaTypeEvent{" +
                "portQualifiedName='" + portQualifiedName + '\'' +
                ", schemaType=" + schemaType +
                '}';
    }
}
