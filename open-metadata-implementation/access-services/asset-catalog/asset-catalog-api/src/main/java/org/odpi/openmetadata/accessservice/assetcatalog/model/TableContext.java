/* SPDX-License-Identifier: Apache-2.0 */
package org.odpi.openmetadata.accessservice.assetcatalog.model;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.ArrayList;
import java.util.List;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * The TableContext object provide the full context to identify the table in the repository and to recreate the
 * associated complex schema types (columns).
 */
@JsonAutoDetect(getterVisibility = PUBLIC_ONLY, setterVisibility = PUBLIC_ONLY, fieldVisibility = NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class TableContext {

    private String tableName;
    private String tableQualifiedName;
    private String tableTypeQualifiedName;

    private List<Column> columns = new ArrayList<>();

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

    public List<Column> getColumns() {
        return columns;
    }

    public void setColumns(List<Column> columns) {
        this.columns = columns;
    }
}