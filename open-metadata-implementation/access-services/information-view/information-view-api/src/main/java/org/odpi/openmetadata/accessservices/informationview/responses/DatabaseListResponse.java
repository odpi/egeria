/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.informationview.responses;

import org.odpi.openmetadata.accessservices.informationview.events.DatabaseSource;

import java.util.List;

public class DatabaseListResponse extends InformationViewOMASAPIResponse{

   private List<DatabaseSource> databasesList;

    public List<DatabaseSource> getDatabasesList() {
        return databasesList;
    }

    public void setDatabasesList(List<DatabaseSource> databasesList) {
        this.databasesList = databasesList;
    }

    @Override
    public String toString() {
        return "{" +
                "databasesList=" + databasesList +
                '}';
    }
}
