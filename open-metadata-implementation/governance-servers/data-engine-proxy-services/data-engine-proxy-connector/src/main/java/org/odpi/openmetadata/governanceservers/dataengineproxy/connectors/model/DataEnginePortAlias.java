/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.governanceservers.dataengineproxy.connectors.model;

import org.odpi.openmetadata.accessservices.dataengine.model.PortAlias;

/**
 * Wrapper for the Data Engine OMAS's PortAlias object, to also be able to include the userId information.
 */
public class DataEnginePortAlias {

    private PortAlias portAlias;
    private String userId;

    /**
     * Default constructor
     *
     * @param portAlias the PortAlias being maintained
     * @param userId    the user maintaining the PortAlias
     */
    public DataEnginePortAlias(PortAlias portAlias, String userId) {
        this.portAlias = portAlias;
        this.userId = userId;
    }

    /**
     * Retrieve the PortAlias being maintained.
     *
     * @return PortAlias
     */
    public PortAlias getPortAlias() { return portAlias; }

    /**
     * Set the PortAlias to be maintained.
     *
     * @param portAlias the PortAlias to be maintained
     */
    public void setPortAlias(PortAlias portAlias) { this.portAlias = portAlias; }

    /**
     * Retrieve the user maintaining the PortAlias.
     *
     * @return String
     */
    public String getUserId() { return userId; }

    /**
     * Set the user maintaining the PortAlias.
     *
     * @param userId the user maintaining the PortAlias
     */
    public void setUserId(String userId) { this.userId = userId; }

}
