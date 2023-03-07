/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.securityofficer.api.model;

import java.io.Serializable;
import java.util.Objects;

public class Context implements Serializable {

    private static final long serialVersionUID = 1L;

    private String column;
    private String table;
    private String schema;

    public String getColumn() {
        return column;
    }

    public void setColumn(String column) {
        this.column = column;
    }

    public String getTable() {
        return table;
    }

    public void setTable(String table) {
        this.table = table;
    }

    public String getSchema() {
        return schema;
    }

    public void setSchema(String schema) {
        this.schema = schema;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Context context = (Context) o;
        return Objects.equals(column, context.column) &&
                Objects.equals(table, context.table) &&
                Objects.equals(schema, context.schema);
    }

    @Override
    public int hashCode() {
        return Objects.hash(column, table, schema);
    }
}
