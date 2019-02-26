/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.governanceenginesplugins.gaianrangerplugin;

import java.util.List;
import java.util.Set;

/**
 * Query context is used to build up the context of the query through a series of calls made to this class by GaianDB.
 */
public class QueryContext {

    private String user;
    private Set<String> userGroups;
    private String schema;
    private String tableName;
    private List<String> columns;
    private String actionType;
    private String resourceType;
    private Boolean isNullMasking;
    private List<String> columnTransformers;

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public Set<String> getUserGroups() {
        return userGroups;
    }

    public void setUserGroups(Set<String> userGroups) {
        this.userGroups = userGroups;
    }

    public String getSchema() {
        return schema;
    }

    public void setSchema(String schema) {
        this.schema = schema;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public List<String> getColumns() {
        return columns;
    }

    public void setColumns(List<String> columns) {
        this.columns = columns;
    }

    public String getActionType() {
        return actionType;
    }

    public void setActionType(String actionType) {
        this.actionType = actionType;
    }

    public String getResourceType() {
        return resourceType;
    }

    public void setResourceType(String resourceType) {
        this.resourceType = resourceType;
    }

    public Boolean getNullMasking() {
        return isNullMasking;
    }

    public void setNullMasking(Boolean nullMasking) {
        isNullMasking = nullMasking;
    }

    public List<String> getColumnTransformers() {
        return columnTransformers;
    }

    public void setColumnTransformers(List<String> columnTransformers) {
        this.columnTransformers = columnTransformers;
    }

    @Override
    public String toString() {
        return "QueryContext{" +
                "user='" + user + '\'' +
                ", userGroups=" + userGroups +
                ", schema='" + schema + '\'' +
                ", tableName='" + tableName + '\'' +
                ", columns=" + columns +
                ", actionType='" + actionType + '\'' +
                ", resourceType='" + resourceType + '\'' +
                ", isNullMasking='" + isNullMasking + '\'' +
                ", columnTransformers=" + columnTransformers +
                '}';
    }
}