/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.dataplatformservices.api.model;

import org.odpi.openmetadata.accessservices.dataplatform.properties.DeployedDatabaseSchema;

/**
 * The type Data platform deployed database schema.
 */
public class DataPlatformDeployedDatabaseSchema {

    private String userId;
    private DeployedDatabaseSchema deployedDatabaseSchema;

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
     * Gets deployed database schema.
     *
     * @return the deployed database schema
     */
    public DeployedDatabaseSchema getDeployedDatabaseSchema() {
        return deployedDatabaseSchema;
    }

    /**
     * Sets deployed database schema.
     *
     * @param deployedDatabaseSchema the deployed database schema
     */
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
