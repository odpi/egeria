/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adminservices.properties;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * Server active status defines the state of the server (or a service) on a single platform.
 */
@JsonAutoDetect(getterVisibility = PUBLIC_ONLY, setterVisibility = PUBLIC_ONLY, fieldVisibility = NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@Deprecated
public enum ServerActiveStatus implements Serializable
{
    UNKNOWN     (0,   "Unknown",  "The state of the server is unknown.  This is equivalent to a null value"),
    STARTING    (1,   "Starting", "The server is starting."),
    RUNNING     (2,   "Running",  "The server has completed start up and is running."),
    STOPPING    (3,   "Stopping", "The server has received a request to shutdown."),
    INACTIVE    (99,  "Inactive", "The server is not running.");

    private static final long serialVersionUID = 1L;

    private int    ordinal;
    private String name;
    private String description;


    /**
     * Constructor to set up the instance of this enum.
     *
     * @param ordinal code number
     * @param name default name
     * @param description default description
     */
    ServerActiveStatus(int    ordinal,
                       String name,
                       String description)
    {
        this.ordinal         = ordinal;
        this.name            = name;
        this.description     = description;
    }


    /**
     * Return the code for this enum used for indexing based on the enum value.
     *
     * @return int code number
     */
    public int getOrdinal()
    {
        return ordinal;
    }


    /**
     * Return the default name for this enum type.
     *
     * @return String name
     */
    public String getName()
    {
        return name;
    }


    /**
     * Return the default description for this enum.
     *
     * @return String description
     */
    public String getDescription()
    {
        return description;
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "ServerActiveStatus{" +
                       "ordinal=" + ordinal +
                       ", name='" + name + '\'' +
                       ", description='" + description + '\'' +
                       '}';
    }
}

