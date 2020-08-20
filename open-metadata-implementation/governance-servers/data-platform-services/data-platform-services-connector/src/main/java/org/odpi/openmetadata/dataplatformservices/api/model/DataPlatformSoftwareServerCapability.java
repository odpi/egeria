/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.dataplatformservices.api.model;

import org.odpi.openmetadata.accessservices.dataplatform.properties.DataPlatform;

/**
 * The type Data platform software server capability.
 */
public class DataPlatformSoftwareServerCapability {

    private String       userId;
    private DataPlatform dataPlatformProperties;

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
     * Gets software server capability.
     *
     * @return the software server capability
     */
    public DataPlatform getDataPlatformProperties() {
        return dataPlatformProperties;
    }

    /**
     * Sets software server capability.
     *
     * @param dataPlatformProperties the software server capability
     */
    public void setDataPlatformProperties(DataPlatform dataPlatformProperties) {
        this.dataPlatformProperties = dataPlatformProperties;
    }

    @Override
    public String toString() {
        return "DataPlatformSoftwareServerCapability{" +
                "userId='" + userId + '\'' +
                ", dataPlatformProperties=" + dataPlatformProperties +
                '}';
    }
}
