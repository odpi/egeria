/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.dataplatformservices.api.model;

import org.odpi.openmetadata.accessservices.dataplatform.properties.TabularSchema;

/**
 * The type Data platform tabular schema.
 */
public class DataPlatformTabularSchema {

    private String userId;
    private TabularSchema tabularSchema;

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
     * Gets tabular schema.
     *
     * @return the tabular schema
     */
    public TabularSchema getTabularSchema() {
        return tabularSchema;
    }

    /**
     * Sets tabular schema.
     *
     * @param tabularSchema the tabular schema
     */
    public void setTabularSchema(TabularSchema tabularSchema) {
        this.tabularSchema = tabularSchema;
    }

    @Override
    public String toString() {
        return "DataPlatformTabularSchema{" +
                "userId='" + userId + '\'' +
                ", tabularSchema=" + tabularSchema +
                '}';
    }
}
