/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.openconnectors.governancedaemonconnectors.dataengineproxy.model;

import org.odpi.openmetadata.accessservices.dataengine.model.PortAlias;

public class DataEnginePortAlias {

    private PortAlias portAlias;
    private String userId;

    public DataEnginePortAlias(PortAlias portAlias, String userId) {
        this.portAlias = portAlias;
        this.userId = userId;
    }

    public PortAlias getPortAlias() { return portAlias; }
    public void setPortAlias(PortAlias portAlias) { this.portAlias = portAlias; }

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

}
