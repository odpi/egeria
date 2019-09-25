package org.odpi.openmetadata.dataplatformservices.api.model;

import org.odpi.openmetadata.accessservices.dataplatform.properties.SoftwareServerCapability;

public class DataPlatformSoftwareServerCapability {

    private String userId;
    private SoftwareServerCapability softwareServerCapability;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public SoftwareServerCapability getSoftwareServerCapability() {
        return softwareServerCapability;
    }

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
