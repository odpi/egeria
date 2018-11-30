/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.odpi.openmetadata.adapters.governanceenginesplugins.gaianrangerplugin;

import java.util.List;
import java.util.Set;

public class QueryContext{

    private String user;

    private String schema;

    private String tableName;

    private List<String> columns;

    private Set<String> userGroups;

    private String action;

    private String resource;

    private List<String> columnTransformers;



    public String getUser(){ return this.user; }

    public String getSchema() { return this.schema; }

    public String getTableName() { return this.tableName;}

    public List<String> getColumns() { return this.columns; }

    public Set<String> getUserGroups() { return this.userGroups; }

    public String getActionType() { return this.action; }

    public String getResourceType() { return this.resource; }

    public void setUser(String user) {
        this.user = user;
    }

    public void setSchema(String schema) {
        this.schema = schema;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public void setColumns(List<String> columns) {
        this.columns = columns;
    }

    public void setUserGroups(Set<String> userGroups) {
        this.userGroups = userGroups;
    }

    public void setActionType(String action) {
        this.action = action;
    }

    public void setResourceType(String resource) {
        this.resource = resource;
    }

    public List<String> getColumnTransformers() {
        return columnTransformers;
    }

    public void setColumnTransformers(List<String> columnTransformers) {
        this.columnTransformers = columnTransformers;
    }

    @Override
    public String toString() {
        return "QueryContext [resourceType=" + resource + ", action=" + action
                + ", user=" + user + ", userGroups=" + String.join(",", userGroups) + ", schema="
                + schema + ", tableName=" + tableName + ", columns=" + String.join(",", columns) + "]";
    }
}
