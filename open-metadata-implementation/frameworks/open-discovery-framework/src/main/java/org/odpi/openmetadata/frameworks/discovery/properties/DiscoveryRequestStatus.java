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
    WAITING         (0,  "Waiting",    "Discovery request is waiting to execute"),
    IN_PROGRESS     (1,  "In Progress","Discovery request is executing"),
    FAILED          (2,  "Failed",     "Discovery request has failed"),
    COMPLETED       (3,  "Completed",  "Discovery request has completed successfully"),
    UNKNOWN_STATUS  (99, "Unknown",    "Discovery request status is unknown");

    private static final long     serialVersionUID = 1L;

    private int            statusCode;
    private String         statusName;
    private String         statusDescription;


    /**
     * Typical Constructor
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