/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.dataplatformservices.api.model;

import org.odpi.openmetadata.accessservices.dataplatform.properties.SoftwareServerCapability;

/**
 * The type Data platform software server capability.
 */
public class DataPlatformSoftwareServerCapability {

    private String userId;
    private SoftwareServerCapability softwareServerCapability;

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
    public SoftwareServerCapability getSoftwareServerCapability() {
        return softwareServerCapability;
    }

    /**
     * Sets software server capability.
     *
     * @param softwareServerCapability the software server capability
     */
    public void setSoftwareServerCapability(SoftwareServerCapability softwareServerCapability) {
        this.softwareServerCapability = softwareServerCapability;
    }

    @Override
    public String toString() {
        return "DataPlatformSoftwareServerCapability{" +
                "userId='" + userId + '\'' +
                ", softwareServerCapability=" + softwareServerCapability +
                '}';
    }
}
