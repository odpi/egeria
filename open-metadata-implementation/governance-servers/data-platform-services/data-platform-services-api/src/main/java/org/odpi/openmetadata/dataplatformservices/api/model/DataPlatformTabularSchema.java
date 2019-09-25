package org.odpi.openmetadata.dataplatformservices.api.model;

import org.odpi.openmetadata.accessservices.dataplatform.properties.schema.TabularSchema;

public class DataPlatformTabularSchema {

    private String userId;
    private TabularSchema tabularSchema;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public TabularSchema getTabularSchema() {
        return tabularSchema;
    }

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
