/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.governanceservers.enginehostservices.properties;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * GovernanceEngineStatus defines the status of a governance engine.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public enum GovernanceEngineStatus
{
    /**
     * The governance engine is assigned to the engine host server but has not yet been configured.
     */
    ASSIGNED    (0, "Assigned", "The governance engine is assigned to the engine host server but has not yet been configured."),

    /**
     * The governance engine is retrieving its configuration from the metadata server.
     */
    CONFIGURING (1, "Configuring",  "The governance engine is retrieving its configuration from the metadata server."),

    /**
     * The governance engine is operational and able to service all defined governance requests on demand.
     */
    RUNNING     (2, "Running", "The governance engine is operational and able to service all defined governance requests on demand."),

    /**
     * The governance engine cannot run successfully due to an error in its configuration.
     */
    FAILED      (3, "Failed", "The governance engine cannot run successfully due to an error in its configuration."),

    /**
     * The governance engine has been disabled. It is waiting to be enabled before it can service any more governance requests.
     */
    DISABLED    (4, "Disabled", "The governance engine has been disabled. It is waiting to be enabled before " +
                                                                   "it can service any more governance requests.");

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
    GovernanceEngineStatus(int     ordinal, String statusName, String statusDescription)
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

    /**
     * toString() JSON-style
     *
     * @return string description
     */
    @Override
    public String toString()
    {
        return "GovernanceEngineStatus{" +
            "ordinal=" + ordinal +
            ", name='" + statusName + '\'' +
            ", description='" + statusDescription + '\'' +
            '}';
    }

}
