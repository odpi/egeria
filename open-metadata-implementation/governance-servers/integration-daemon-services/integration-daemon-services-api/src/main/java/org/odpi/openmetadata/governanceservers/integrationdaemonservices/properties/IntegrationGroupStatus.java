/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.governanceservers.integrationdaemonservices.properties;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serial;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * IntegrationGroupStatus defines the status of a integration group.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public enum IntegrationGroupStatus
{
    ASSIGNED    (0, "Assigned", "The integration group is assigned to the integration daemon server but has not yet been configured."),
    CONFIGURING (1, "Configuring",  "The integration group is retrieving its configuration from the metadata server."),
    RUNNING     (2, "Running", "The integration group is operational and able to service all defined governance requests on demand."),
    FAILED      (3, "Failed", "The integration group is unable to run successfully due to an error in its configuration."),
    DISABLED    (4, "Disabled", "The integration group has been disabled. It is waiting to be enabled before " +
                                                                   "it can service any more governance requests.");

    private static final long serialVersionUID = 1L;

    private final int    ordinal;
    private final String statusName;
    private final String statusDescription;


    /**
     * Default Constructor
     *
     * @param ordinal ordinal for this enum
     * @param statusName symbolic name for this enum
     * @param statusDescription short description for this enum
     */
    IntegrationGroupStatus(int     ordinal, String statusName, String statusDescription)
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
