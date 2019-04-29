/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.informationview.responses;


import org.odpi.openmetadata.accessservices.informationview.events.TableColumn;

import java.util.List;

public class TableColumnsResponse extends InformationViewOMASAPIResponse{

    private List<TableColumn> tableColumns;

    public List<TableColumn> getTableColumns() {
        return tableColumns;
    }

    public void setTableColumns(List<TableColumn> tableColumns) {
        this.tableColumns = tableColumns;
    }

    @Override
    public String toString() {
        return "{" +
                "tableColumns=" + tableColumns +
                '}';
    }

}
