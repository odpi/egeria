/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.dataplatformservices.api.model;

import org.odpi.openmetadata.accessservices.dataplatform.properties.TabularColumn;

/**
 * The type Data platform tabular column.
 */
public class DataPlatformTabularColumn {

    private String userId;
    private TabularColumn tabularColumn;

    /**
     * Gets user id.
     *
     * @return the user id
     */
    public String getUserId() {
        return userId;
    }

    /**
     * Sets user id.
     *
     * @param userId the user id
     */
    public void setUserId(String userId) {
        this.userId = userId;
    }

    /**
     * Gets tabular column.
     *
     * @return the tabular column
     */
    public TabularColumn getTabularColumn() {
        return tabularColumn;
    }

    /**
     * Sets tabular column.
     *
     * @param tabularColumn the tabular column
     */
    public void setTabularColumn(TabularColumn tabularColumn) {
        this.tabularColumn = tabularColumn;
    }

    @Override
    public String toString() {
        return "DataPlatformTabularColumn{" +
                "userId='" + userId + '\'' +
                ", tabularColumn=" + tabularColumn +
                '}';
    }
}
