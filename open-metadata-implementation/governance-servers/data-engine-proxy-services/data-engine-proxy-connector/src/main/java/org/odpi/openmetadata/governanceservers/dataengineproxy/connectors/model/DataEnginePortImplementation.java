/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.governanceservers.dataengineproxy.connectors.model;

import org.odpi.openmetadata.accessservices.dataengine.model.PortImplementation;

/**
 * Wrapper for the Data Engine OMAS's PortImplementation object, to also be able to include the userId information.
 */
public class DataEnginePortImplementation {

    private PortImplementation portImplementation;
    private String userId;

    /**
     * Default constructor
     *
     * @param portImplementation the PortImplementation to be maintained
     * @param userId             the user maintaining the PortImplementation
     */
    public DataEnginePortImplementation(PortImplementation portImplementation, String userId) {
        this.portImplementation = portImplementation;
        this.userId = userId;
    }

    /**
     * Retrieve the PortImplementation being maintained.
     *
     * @return PortImplementation
     */
    public PortImplementation getPortImplementation() { return portImplementation; }

    /**
     * Set the PortImplementation to be maintained.
     *
     * @param portImplementation the PortImplementation to be maintained
     */
    public void setPortImplementation(PortImplementation portImplementation) { this.portImplementation = portImplementation; }

    /**
     * Retrieve the user maintaining the PortImplementation.
     *
     * @return String
     */
    public String getUserId() { return userId; }

    /**
     * Set the user maintaining the PortImplementation.
     *
     * @param userId the user maintaining the PortImplementation
     */
    public void setUserId(String userId) { this.userId = userId; }

}
