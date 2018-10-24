/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.informationview.events;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.ArrayList;
import java.util.List;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

@JsonAutoDetect(getterVisibility = PUBLIC_ONLY, setterVisibility = PUBLIC_ONLY, fieldVisibility = NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ColumnContextEvent {

    private final TableContext tableContext = new TableContext();
    private ConnectionDetails connectionDetails;
    private List<ColumnDetails> tableColumns = new ArrayList<>();

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



