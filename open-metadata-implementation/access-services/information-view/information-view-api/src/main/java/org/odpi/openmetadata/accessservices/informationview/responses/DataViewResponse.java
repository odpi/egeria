/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.informationview.responses;

import org.odpi.openmetadata.accessservices.informationview.events.DataView;

public class DataViewResponse  extends InformationViewOMASAPIResponse{

    private DataView dataView;

    public DataView getDataView() {
        return dataView;
    }

    public void setDataView(DataView dataView) {
        this.dataView = dataView;
    }

    @Override
    public String toString() {
        return "{" +
                "dataView=" + dataView +
                '}';
    }
}
