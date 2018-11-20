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
public class TableContextEvent extends InformationViewBean {

    private TableSource tableSource;
    private List<DatabaseColumn> tableColumns = new ArrayList<>();


       /**
     * Return the list of columns
     *
     * @return list of columns
     */
    public List<DatabaseColumn> getTableColumns() {
        return tableColumns;
    }

    /**
     * set up the column list of the table
     *
     * @param tableColumns - columns of the table
     */
    public void setTableColumns(List<DatabaseColumn> tableColumns) {
        this.tableColumns = tableColumns;
    }

    public TableSource getTableSource() {
        return tableSource;
    }

    public void setTableSource(TableSource tableSource) {
        this.tableSource = tableSource;
    }

    @Override
    public String toString() {
        return "TableContextEvent{" +
                "tableSource=" + tableSource +
                ", tableColumns=" + tableColumns +
                '}';
    }
}



