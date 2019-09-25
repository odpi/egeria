package org.odpi.openmetadata.dataplatformservices.api.model;

import org.odpi.openmetadata.accessservices.dataplatform.properties.schema.TabularColumn;

public class DataPlatformTabularColumn {

    private String userId;
    private TabularColumn tabularColumn;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public TabularColumn getTabularColumn() {
        return tabularColumn;
    }

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
