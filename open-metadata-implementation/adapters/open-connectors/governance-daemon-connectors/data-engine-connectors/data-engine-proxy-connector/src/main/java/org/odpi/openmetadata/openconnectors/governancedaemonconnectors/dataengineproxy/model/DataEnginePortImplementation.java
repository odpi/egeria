/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.openconnectors.governancedaemonconnectors.dataengineproxy.model;

import org.odpi.openmetadata.accessservices.dataengine.model.PortImplementation;

public class DataEnginePortImplementation {

    private PortImplementation portImplementation;
    private String userId;

    public DataEnginePortImplementation(PortImplementation portImplementation, String userId) {
        this.portImplementation = portImplementation;
        this.userId = userId;
    }

    public PortImplementation getPortImplementation() { return portImplementation; }
    public void setPortImplementation(PortImplementation portImplementation) { this.portImplementation = portImplementation; }

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

}
