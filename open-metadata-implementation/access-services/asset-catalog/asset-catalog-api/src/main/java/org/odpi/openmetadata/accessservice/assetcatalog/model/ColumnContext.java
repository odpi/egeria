/* SPDX-License-Identifier: Apache-2.0 */
package org.odpi.openmetadata.accessservice.assetcatalog.model;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * The Column Context object provide the full context to identify the column in the repository.
 */
@JsonAutoDetect(getterVisibility = PUBLIC_ONLY, setterVisibility = PUBLIC_ONLY, fieldVisibility = NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ColumnContext implements Serializable {

    private static final long serialVersionUID = 1L;

    private String columnGuid;
    private String columnQualifiedName;
    private String columnAttributeName;
    private String columnType;
    private String columnQualifiedNameColumnType;

    private String tableName;
    private String tableQualifiedName;
    private String tableTypeQualifiedName;

    private String schemaName;
    private String schemaQualifiedName;
    private String schemaTypeQualifiedName;

    private String databaseName;
    private String databaseQualifiedName;

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getColumnGuid() {
        return columnGuid;
    }

    public void setColumnGuid(String columnGuid) {
        this.columnGuid = columnGuid;
    }

    public String getColumnQualifiedName() {
        return columnQualifiedName;
    }

    public void setColumnQualifiedName(String columnQualifiedName) {
        this.columnQualifiedName = columnQualifiedName;
    }

    public String getColumnAttributeName() {
        return columnAttributeName;
    }

    public void setColumnAttributeName(String columnAttributeName) {
        this.columnAttributeName = columnAttributeName;
    }

    public String getColumnType() {
        return columnType;
    }

    public void setColumnType(String columnType) {
        this.columnType = columnType;
    }

    public String getColumnQualifiedNameColumnType() {
        return columnQualifiedNameColumnType;
    }

    public void setColumnQualifiedNameColumnType(String columnQualifiedNameColumnType) {
        this.columnQualifiedNameColumnType = columnQualifiedNameColumnType;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getTableQualifiedName() {
        return tableQualifiedName;
    }

    public void setTableQualifiedName(String tableQualifiedName) {
        this.tableQualifiedName = tableQualifiedName;
    }

    public String getTableTypeQualifiedName() {
        return tableTypeQualifiedName;
    }

    public void setTableTypeQualifiedName(String tableTypeQualifiedName) {
        this.tableTypeQualifiedName = tableTypeQualifiedName;
    }

    public String getSchemaName() {
        return schemaName;
    }

    public void setSchemaName(String schemaName) {
        this.schemaName = schemaName;
    }

    public String getSchemaQualifiedName() {
        return schemaQualifiedName;
    }

    public void setSchemaQualifiedName(String schemaQualifiedName) {
        this.schemaQualifiedName = schemaQualifiedName;
    }

    public String getSchemaTypeQualifiedName() {
        return schemaTypeQualifiedName;
    }

    public void setSchemaTypeQualifiedName(String schemaTypeQualifiedName) {
        this.schemaTypeQualifiedName = schemaTypeQualifiedName;
    }

    public String getDatabaseName() {
        return databaseName;
    }

    public void setDatabaseName(String databaseName) {
        this.databaseName = databaseName;
    }

    public String getDatabaseQualifiedName() {
        return databaseQualifiedName;
    }

    public void setDatabaseQualifiedName(String databaseQualifiedName) {
        this.databaseQualifiedName = databaseQualifiedName;
    }
}