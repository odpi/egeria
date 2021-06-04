/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.dataengine.rest;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.odpi.openmetadata.accessservices.dataengine.model.RelationalTable;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

@JsonAutoDetect(getterVisibility = PUBLIC_ONLY, setterVisibility = PUBLIC_ONLY, fieldVisibility = NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class RelationalTableRequestBody  extends DataEngineOMASAPIRequestBody {
    @JsonProperty("table")
    private RelationalTable relationalTable;

    private String databaseQualifiedName;

    public RelationalTable getRelationalTable() {
        return relationalTable;
    }

    public void setRelationalTable(RelationalTable relationalTable) {
        this.relationalTable = relationalTable;
    }

    public String getDatabaseQualifiedName() {
        return databaseQualifiedName;
    }

    public void setDatabaseQualifiedName(String databaseQualifiedName) {
        this.databaseQualifiedName = databaseQualifiedName;
    }

    @Override
    public String toString() {
        return "RelationalTableRequestBody{" +
                "relationalTable=" + relationalTable +
                ", databaseQualifiedName='" + databaseQualifiedName + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RelationalTableRequestBody that = (RelationalTableRequestBody) o;
        return Objects.equals(relationalTable, that.relationalTable) &&
                Objects.equals(databaseQualifiedName, that.databaseQualifiedName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(relationalTable, databaseQualifiedName);
    }
}


