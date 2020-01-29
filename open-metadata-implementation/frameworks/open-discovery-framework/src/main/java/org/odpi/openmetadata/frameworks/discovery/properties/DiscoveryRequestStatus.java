/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.discovery.properties;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * DiscoveryRequestStatus defines the current status for a discovery request made to a discovery engine.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public enum DiscoveryRequestStatus implements Serializable
{
    WAITING         (0,  "Waiting",    "Discovery service is waiting to execute in the discovery engine"),
    ACTIVATING      (1,  "Activating", "Discovery service is being initialized in the discovery engine"),
    IN_PROGRESS     (2,  "In Progress","Discovery service is executing"),
    FAILED          (3,  "Failed",     "Discovery service has failed"),
    COMPLETED       (4,  "Completed",  "Discovery service has completed successfully"),
    OTHER           (5,  "Other",      "Discovery service has a status that is not covered by this enum"),
    UNKNOWN_STATUS  (99, "Unknown",    "Discovery service status is unknown");

    private static final long     serialVersionUID = 1L;

    private int            statusCode;
    private String         statusName;
    private String         statusDescription;


    /**
     * Typical Constructor
     *
     * @param statusCode ordinal
     * @param statusName short name
     * @param statusDescription longer explanation
     */
    DiscoveryRequestStatus(int     statusCode, String   statusName, String   statusDescription)
    {
        /*
         * Save the values supplied
         */
        this.statusCode = statusCode;
        this.statusName = statusName;
        this.statusDescription = statusDescription;
    }


    /**
     * Return the status code for this enum instance
     *
     * @return int status code
     */
    public int getOrdinal()
    {
        return statusCode;
    }


    /**
     * Return the default name for the status for this enum instance.
     *
     * @return String default status name
     */
    public String getName()
    {
        return statusName;
    }


    /**
     * Return the default description for the status for this enum instance.
     *
     * @return String default status description
     */
    public String getDescription()
    {
        return statusDescription;
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "DiscoveryRequestStatus{" +
                "statusCode=" + statusCode +
                ", statusName='" + statusName + '\'' +
                ", statusDescription='" + statusDescription + '\'' +
                '}';
    }
}