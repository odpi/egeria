/* SPDX-License-Identifier: Apache-2.0 */
package org.apache.atlas.omas.informationview.events;


import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;


/**
 * InformationViewEvent contains information which will be sent out to Information View OMAS in topic
 */
@JsonAutoDetect(getterVisibility = PUBLIC_ONLY, setterVisibility = PUBLIC_ONLY, fieldVisibility = NONE)
@JsonInclude(JsonInclude.Include.ALWAYS)
@JsonIgnoreProperties(ignoreUnknown = true)
public class InformationViewEvent {


    private String tableName;
    private String tableQualifiedName;
    private String tableTypeQualifiedName;
    private String schemaName;
    private String schemaQualifiedName;
    private String schemaTypeQualifiedName;
    private String databaseName;
    private String databaseQualifiedName;
    private ConnectionDetails connectionDetails;

    private List<DerivedColumnDetail> derivedColumns;

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getSchemaName() {
        return schemaName;
    }

    public void setSchemaName(String schemaName) {
        this.schemaName = schemaName;
    }

    public ConnectionDetails getConnectionDetails() {
        return connectionDetails;
    }

    public void setConnectionDetails(ConnectionDetails connectionDetails) {
        this.connectionDetails = connectionDetails;
    }

    public List<DerivedColumnDetail> getDerivedColumns() {
        return derivedColumns;
    }

    public void setDerivedColumns(List<DerivedColumnDetail> derivedColumns) {
        this.derivedColumns = derivedColumns;
    }

    public String getDatabaseName() {
        return databaseName;
    }

    public void setDatabaseName(String databaseName) {
        this.databaseName = databaseName;
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

    public String getSchemaQualifiedName() {
        return schemaQualifiedName;
    }

    public void setSchemaQualifiedName(String schemaQualifiedName) {
        this.schemaQualifiedName = schemaQualifiedName;
    }

    public String getDatabaseQualifiedName() {
        return databaseQualifiedName;
    }

    public void setDatabaseQualifiedName(String databaseQualifiedName) {
        this.databaseQualifiedName = databaseQualifiedName;
    }

    public String getSchemaTypeQualifiedName() {
        return schemaTypeQualifiedName;
    }

    public void setSchemaTypeQualifiedName(String schemaTypeQualifiedName) {
        this.schemaTypeQualifiedName = schemaTypeQualifiedName;
    }
}
