/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.openconnectors.governancedaemonconnectors.dataengineproxy.model;

import org.odpi.openmetadata.accessservices.dataengine.model.SchemaType;

public class DataEngineSchemaType {

    private SchemaType schemaType;
    private String userId;

    public DataEngineSchemaType(SchemaType schemaType, String userId) {
        this.schemaType = schemaType;
        this.userId = userId;
    }

    public SchemaType getSchemaType() { return schemaType; }
    public void setSchemaType(SchemaType schemaType) { this.schemaType = schemaType; }

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

}
