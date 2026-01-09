/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.repositoryservices.properties;


import java.io.Serializable;

/**
 * CohortConnectionStatus defines the status of the local server's connection to the metadata highway for
 * a specific cohort.
 * <ul>
 *     <li>
 *         NOT_INITIALIZED: the local components for communicating with other members of the cohort are not initialized.
 *         This should never been seen on the adminservices console since it is the value for the cohort manager used on the
 *         variable declaration.
 *     </li>
 *     <li>
 *         INITIALIZING: the local components for communicating with the other members of the cohort are initializing.
 *         It the server is working properly this status is only set for a split-second.
 *         If it is seen on the admin services console it probably means one of the underlying connectors is hanging during its
 *         initialization.
 *     </li>
 *     <li>
 *         NEW: the local components for the cohort are initialized but they have not yet exchanged messages with the other
 *         members of the cohort.  At this point, the local metadata collection id may be changed.
 *     </li>
 *     <li>
 *         CONFIGURATION_ERROR: means there is an error in the configuration and connection is not possible.
 *         The administrator needs to either fix the config or fix the system's infrastructure around the server.
 *     </li>
 *     <li>
 *         CONNECTED: means the server is connected to the metadata highway for this cohort and is exchanging messages.
 *     </li>
 *     <li>
 *         DISCONNECTING: means the server is disconnecting from the metadata highway and is in the process of shutting
 *         down the local components that manage communication with the other members of this cohort.
 *     </li>
 *     <li>
 *         DISCONNECTED: means the server is disconnected from the metadata highway for this cohort.  This may be because the
 *         local server is shutting down or the configuration is being adjusted.
 *     </li>
 * </ul>
 */
public enum CohortConnectionStatus implements Serializable
{
    /**
     * NotInitialized - The local components for communicating with the cohort are not initialized.
     */
    NOT_INITIALIZED      (0, "NotInitialized",     "The local components for communicating with the cohort are not initialized."),

    /**
     * Initializing - The local components for communicating with the cohort are initializing.
     */
    INITIALIZING         (1, "Initializing",       "The local components for communicating with the cohort are initializing."),

    /**
     * New - The local components for communicating with the cohort are initialized, but they have not exchanged messages with the other members of cohort.
     */
    NEW                  (2, "New",                "The local components for communicating with the cohort are initialized, " +
                                                           "but they have not exchanged messages with the other members of cohort."),

    /**
     * ConfigurationError -  an error in the configuration and connection is not possible.
     */
    CONFIGURATION_ERROR  (3, "ConfigurationError", "There is an error in the configuration and connection is not possible."),

    /**
     * Connected - The server is connected to the metadata highway for this cohort, and messages are being exchanged with other members of the cohort.
     */
    CONNECTED            (4, "Connected",          "The server is connected to the metadata highway for this cohort and messages " +
                                                           "are being exchanged with other members of the cohort."),

    /**
     * Initializing - The local components for communicating with the cohort are disconnecting.
     */
    DISCONNECTING        (5, "Disconnecting",       "The local components for communicating with the cohort are disconnecting."),

    /**
     * Disconnected - The server is disconnected from the metadata highway for this cohort.  This may be because the local server is shutting down or the configuration is being adjusted.
     */
    DISCONNECTED         (6, "Disconnected",       "The server is disconnected from the metadata highway for this cohort.  This may be because the " +
                                                           "local server is shutting down or the configuration is being adjusted.");


    private static final long    serialVersionUID = 1L;

    private final int    statusCode;
    private final String statusName;
    private final String statusDescription;


    /**
     * Create an instance of the enum.
     *
     * @param statusCode numeric code
     * @param statusName name
     * @param statusDescription description
     */
    CohortConnectionStatus(int statusCode, String statusName, String statusDescription)
    {
        this.statusCode = statusCode;
        this.statusName = statusName;
        this.statusDescription = statusDescription;
    }


    /**
     * Return the numeric code for the enum.
     *
     * @return int code
     */
    public int getStatusCode()
    {
        return statusCode;
    }


    /**
     * Return the name for the enum.
     *
     * @return String name
     */
    public String getStatusName()
    {
        return statusName;
    }


    /**
     * Return the description of the enum.
     *
     * @return String description
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
        return "CohortConnectionStatus{" +
                "ordinal=" + statusCode +
                ", name='" + statusName + '\'' +
                ", description='" + statusDescription + '\'' +
                '}';
    }
}
