/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.dataplatform.properties.cassandra;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.accessservices.dataplatform.properties.Source;

import java.util.List;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * The type Table.
 */
@JsonAutoDetect(getterVisibility = PUBLIC_ONLY, setterVisibility = PUBLIC_ONLY, fieldVisibility = NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Table extends Source {


    private String tableId;
    private String tableName;
    private List<Column> columns;
    private List<Column> primaryKeys;
    private String clusteringOrder;

    /**
     * Gets table id.
     *
     * @return the table id
     */
    public String getTableId() {
        return tableId;
    }

    /**
     * Sets table id.
     *
     * @param tableId the table id
     */
    public void setTableId(String tableId) {
        this.tableId = tableId;
    }

    /**
     * Gets table name.
     *
     * @return the table name
     */
    public String getTableName() {
        return tableName;
    }

    /**
     * Sets table name.
     *
     * @param tableName the table name
     */
    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    /**
     * Gets columns.
     *
     * @return the columns
     */
    public List<Column> getColumns() {
        return columns;
    }

    /**
     * Sets columns.
     *
     * @param columns the columns
     */
    public void setColumns(List<Column> columns) {
        this.columns = columns;
    }

    /**
     * Gets primary keys.
     *
     * @return the primary keys
     */
    public List<Column> getPrimaryKeys() {
        return primaryKeys;
    }

    /**
     * Sets primary keys.
     *
     * @param primaryKeys the primary keys
     */
    public void setPrimaryKeys(List<Column> primaryKeys) {
        this.primaryKeys = primaryKeys;
    }

    /**
     * Gets clustering order.
     *
     * @return the clustering order
     */
    public String getClusteringOrder() {
        return clusteringOrder;
    }

    /**
     * Sets clustering order.
     *
     * @param clusteringOrder the clustering order
     */
    public void setClusteringOrder(String clusteringOrder) {
        this.clusteringOrder = clusteringOrder;
    }

    @Override
    public String toString() {
        return "Table{" +
                "tableId='" + tableId + '\'' +
                ", tableName='" + tableName + '\'' +
                ", columns=" + columns +
                ", primaryKeys=" + primaryKeys +
                ", clusteringOrder='" + clusteringOrder + '\'' +
                ", additionalProperties=" + additionalProperties +
                ", qualifiedName='" + qualifiedName + '\'' +
                ", guid='" + guid + '\'' +
                "} " + super.toString();
    }
}
