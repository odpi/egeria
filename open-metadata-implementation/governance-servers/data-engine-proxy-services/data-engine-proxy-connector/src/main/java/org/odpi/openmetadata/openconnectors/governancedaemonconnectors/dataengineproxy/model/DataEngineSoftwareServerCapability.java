/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.openconnectors.governancedaemonconnectors.dataengineproxy.model;

import org.odpi.openmetadata.accessservices.dataengine.model.SoftwareServerCapability;

public class DataEngineSoftwareServerCapability {

    private SoftwareServerCapability softwareServerCapability;
    private String userId;

    public DataEngineSoftwareServerCapability(SoftwareServerCapability softwareServerCapability, String userId) {
        this.softwareServerCapability = softwareServerCapability;
        this.userId = userId;
    }

    public SoftwareServerCapability getSoftwareServerCapability() { return softwareServerCapability; }
    public void setSoftwareServerCapability(SoftwareServerCapability softwareServerCapability) { this.softwareServerCapability = softwareServerCapability; }

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

}
