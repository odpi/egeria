/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.governanceservers.discoveryengineservices.properties;

/**
 * DiscoveryEngineStatus defines the status of a discovery engine.
 */
public enum DiscoveryEngineStatus
{
    ASSIGNED    (0, "Assigned", "The discovery engine is assigned to the discovery server but has not yet been configured."),
    CONFIGURING (1, "Configuring",  "The discovery engine is retrieving its configuration from the metadata server."),
    RUNNING     (2, "Running", "The discovery engine is operational and able to service all defined discovery requests on demand."),
    FAILED      (3, "Failed", "The discovery engine is unable to run successfully due to an error in its configuration."),
    DISABLED    (4, "Disabled", "The discovery engine has been disabled. It is waiting to be enabled before " +
                                                                   "it can service any more discovery requests.");

    private static final long serialVersionUID = 1L;

    private int    ordinal;
    private String statusName;
    private String statusDescription;


    /**
     * Default Constructor
     *
     * @param ordinal ordinal for this enum
     * @param statusName symbolic name for this enum
     * @param statusDescription short description for this enum
     */
    DiscoveryEngineStatus(int     ordinal, String statusName, String statusDescription)
    {
        this.ordinal           = ordinal;
        this.statusName        = statusName;
        this.statusDescription = statusDescription;
    }


    /**
     * Return the code for this enum instance
     *
     * @return int type code
     */
    public int getOrdinal()
    {
        return ordinal;
    }


    /**
     * Return the default name for this enum instance.
     *
     * @return String default name
     */
    public String getStatusName()
    {
        return statusName;
    }


    /**
     * Return the default description for the type for this enum instance.
     *
     * @return String default description
     */
    public String getStatusDescription()
    {
        return statusDescription;
    }
}
