/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.informationview.responses;

import org.odpi.openmetadata.accessservices.informationview.events.TableContextEvent;

import java.util.List;

public class TableContextResponse extends InformationViewOMASAPIResponse{

    private List<TableContextEvent> tableContexts;

    public List<TableContextEvent> getTableContexts() {
        return tableContexts;
    }

    public void setTableContexts(List<TableContextEvent> tableContexts) {
        this.tableContexts = tableContexts;
    }

    @Override
    public String toString() {
        return "{" +
                "tableContexts=" + tableContexts +
                '}';
    }
}
