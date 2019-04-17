/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.informationview.responses;

import org.odpi.openmetadata.accessservices.informationview.events.TableSource;

import java.util.List;

public class TableListResponse extends InformationViewOMASAPIResponse{

    private List<TableSource> tableList;


    public List<TableSource> getTableList() {
        return tableList;
    }

    public void setTableList(List<TableSource> tableList) {
        this.tableList = tableList;
    }


    @Override
    public String toString() {
        return "{" +
                "tableList=" + tableList +
                '}';
    }
}
