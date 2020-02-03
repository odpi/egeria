/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.governanceservers.dataengineproxy.connectors.model;

import org.odpi.openmetadata.accessservices.dataengine.model.SoftwareServerCapability;

/**
 * Wrapper for the Data Engine OMAS's SoftwareServerCapability object, to also be able to include the userId information.
 */
public class DataEngineSoftwareServerCapability {

    private SoftwareServerCapability softwareServerCapability;
    private String userId;

    /**
     * Default constructor
     *
     * @param softwareServerCapability the SoftwareServerCapability to be maintained
     * @param userId                   the user maintaining the SoftwareServerCapability
     */
    public DataEngineSoftwareServerCapability(SoftwareServerCapability softwareServerCapability, String userId) {
        this.softwareServerCapability = softwareServerCapability;
        this.userId = userId;
    }

    /**
     * Retrieve the SoftwareServerCapability being maintained.
     *
     * @return SoftwareServerCapability
     */
    public SoftwareServerCapability getSoftwareServerCapability() { return softwareServerCapability; }

    /**
     * Set the SoftwareServerCapability being maintained.
     *
     * @param softwareServerCapability the SoftwareServerCapability to be maintained
     */
    public void setSoftwareServerCapability(SoftwareServerCapability softwareServerCapability) { this.softwareServerCapability = softwareServerCapability; }

    /**
     * Retrieve the user maintaining the SoftwareServerCapability.
     *
     * @return String
     */
    public String getUserId() { return userId; }

    /**
     * Set the user maintaining the SoftwareServerCapability.
     *
     * @param userId the user maintaining the SoftwareServerCapability
     */
    public void setUserId(String userId) { this.userId = userId; }

}
