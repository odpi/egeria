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
    WAITING         (0,  0,  "Waiting",    "Discovery service is waiting to execute in the discovery engine"),
    ACTIVATING      (1,  1,  "Activating", "Discovery service is being initialized in the discovery engine"),
    IN_PROGRESS     (2,  2,  "In Progress","Discovery service is executing"),
    FAILED          (3,  3,  "Failed",     "Discovery service has failed"),
    COMPLETED       (4,  4,  "Completed",  "Discovery service has completed successfully"),
    OTHER           (5,  5,  "Other",      "Discovery service has a status that is not covered by this enum"),
    UNKNOWN_STATUS  (99, 99, "Unknown",    "Discovery service status is unknown");

    private static final long     serialVersionUID = 1L;

    public static final String ENUM_TYPE_GUID  = "b2fdeddd-24eb-4e9c-a2a4-2693828d4a69";
    public static final String ENUM_TYPE_NAME  = "DiscoveryServiceRequestStatus";

    private String statusName;
    private String statusDescription;
    private int    statusCode;

    private int openTypeOrdinal;

    /**
     * Typical Constructor
     *
     * @param statusCode ordinal
     * @param statusName short name
     * @param statusDescription longer explanation
     */
    DiscoveryRequestStatus(int    statusCode,
                           int    openTypeOrdinal,
                           String statusName,
                           String statusDescription)
    {
        this.statusCode        = statusCode;
        this.openTypeOrdinal   = openTypeOrdinal;
        this.statusName        = statusName;
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
     * Return the code for this enum that comes from the Open Metadata Type that this enum represents.
     *
     * @return int code number
     */
    public int getOpenTypeOrdinal()
    {
        return openTypeOrdinal;
    }


    /**
     * Return the unique identifier for the open metadata enum type that this enum class represents.
     *
     * @return string guid
     */
    public String getOpenTypeGUID() { return ENUM_TYPE_GUID; }


    /**
     * Return the unique name for the open metadata enum type that this enum class represents.
     *
     * @return string name
     */
    public String getOpenTypeName() { return ENUM_TYPE_NAME; }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "DiscoveryRequestStatus{" +
                "statusName='" + statusName + '\'' +
                ", statusDescription='" + statusDescription + '\'' +
                ", statusCode=" + statusCode +
                ", openTypeOrdinal=" + openTypeOrdinal +
                '}';
    }
}