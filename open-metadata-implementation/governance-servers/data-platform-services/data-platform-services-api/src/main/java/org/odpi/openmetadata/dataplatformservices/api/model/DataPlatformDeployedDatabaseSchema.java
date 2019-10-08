/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.dataplatformservices.api.model;

import org.odpi.openmetadata.accessservices.dataplatform.properties.asset.DeployedDatabaseSchema;

public class DataPlatformDeployedDatabaseSchema {

    private String userId;
    private DeployedDatabaseSchema deployedDatabaseSchema;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public DeployedDatabaseSchema getDeployedDatabaseSchema() {
        return deployedDatabaseSchema;
    }

    public void setDeployedDatabaseSchema(DeployedDatabaseSchema deployedDatabaseSchema) {
        this.deployedDatabaseSchema = deployedDatabaseSchema;
    }

    @Override
    public String toString() {
        return "DataPlatformDeployedDatabaseSchema{" +
                "userId='" + userId + '\'' +
                ", deployedDatabaseSchema=" + deployedDatabaseSchema +
                '}';
    }
}
