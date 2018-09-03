/* SPDX-License-Identifier: Apache-2.0 */

package org.odpi.openmetadata.accessservices.informationview.events;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.ArrayList;
import java.util.List;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

@JsonAutoDetect(getterVisibility = PUBLIC_ONLY, setterVisibility = PUBLIC_ONLY, fieldVisibility = NONE)
@JsonInclude(JsonInclude.Include.ALWAYS)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ColumnContextEvent {

    private final TableContext tableContext = new TableContext();
    private ConnectionDetails connectionDetails;
    private List<ColumnDetails> tableColumns = new ArrayList<>();


    /**
     * Return the name of the table
     *
     * @return name of the table
     */
    public String getTableName() {
        return tableContext.getTableName();
    }

    /**
     * set up the name of the table
     *
     * @param tableName - name of the table
     */
    public void setTableName(String tableName) {
        tableContext.setTableName(tableName);
    }

    /**
     * Return the name of the schema
     *
     * @return name of the schema
     */
    public String getSchemaName() {
        return tableContext.getSchemaName();
    }

    /**
     * set up the name of the schema
     *
     * @param schemaName - name of the schema
     */
    public void setSchemaName(String schemaName) {
        tableContext.setSchemaName(schemaName);
    }

    /**
     * Return the connection details
     *
     * @return connection details
     */
    public ConnectionDetails getConnectionDetails() {
        return connectionDetails;
    }

    /**
     * set up the connection details
     *
     * @param connectionDetails - details of the connection
     */
    public void setConnectionDetails(ConnectionDetails connectionDetails) {
        this.connectionDetails = connectionDetails;
    }

    /**
     * Return the list of columns
     *
     * @return list of columns
     */
    public List<ColumnDetails> getTableColumns() {
        return tableColumns;
    }

    /**
     * set up the column list of the table
     *
     * @param tableColumns - columns of the table
     */
    public void setTableColumns(List<ColumnDetails> tableColumns) {
        this.tableColumns = tableColumns;
    }

    public TableContext getTableContext() {
        return tableContext;
    }

    @Override
    public String toString() {
        return "ColumnContextEvent{" +
                "tableContext=" + tableContext +
                ", connectionDetails=" + connectionDetails +
                ", tableColumns=" + tableColumns +
                '}';
    }
}



