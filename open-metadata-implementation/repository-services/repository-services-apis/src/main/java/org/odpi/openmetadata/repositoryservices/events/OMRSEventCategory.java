/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.repositoryservices.events;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * OMRSEventCategory defines the different categories of events that pass through the OMRS Topic.
 * <ul>
 *     <li>
 *         UNKNOWN: this is either an uninitialized event, or the incoming event is not supported by the
 *         local server.
 *     </li>
 *     <li>
 *         REGISTRY: this is an event used by the cohort registries to manage the membership
 *         of the cohort.
 *     </li>
 *     <li>
 *         TYPEDEF: this is an event used by the metadata repository connectors to synchronize the metadata types
 *         (stored in TypeDefs) across the metadata repository cohort.
 *     </li>
 * </ul>
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public enum OMRSEventCategory implements Serializable
{
    /**
     * Unknown Event - Unknown event category.
     */
    UNKNOWN (0, "Unknown Event",  "Unknown event category."),

    /**
     * Registry Event - Event used to manage the membership of the metadata repository cohort.
     */
    REGISTRY(1, "Registry Event", "Event used to manage the membership of the metadata repository cohort."),

    /**
     * TypeDef Event - Event used to manage the synchronization of TypeDefs within the metadata repository cohort.
     */
    TYPEDEF (2, "TypeDef Event",  "Event used to manage the synchronization of TypeDefs within the metadata repository cohort."),

    /**
     * Instance Event - Event used to manage the replication of metadata instances within the metadata repository cohort.
     */
    INSTANCE(3, "Instance Event", "Event used to manage the replication of metadata instances within the metadata repository cohort."),

    /**
     * Generic Event - Event used for sending generic messages, typically error messages.
     */
    GENERIC (99, "Generic Event", "Event used for sending generic messages, typically error messages.");

    private static final long serialVersionUID = 1L;

    private final int    ordinal;
    private final String name;
    private final String description;


    /**
     * Default constructor.
     *
     * @param ordinal int category code number
     * @param name String category name
     * @param description String category description
     */
    OMRSEventCategory(int ordinal,
                      String name,
                      String description)
    {
        this.ordinal = ordinal;
        this.name = name;
        this.description = description;
    }


    /**
     * Return the code number for the event category.
     *
     * @return int code number
     */
    public int getOrdinal()
    {
        return ordinal;
    }


    /**
     * Return the name of the event category.
     *
     * @return String name
     */
    public String getName()
    {
        return name;
    }


    /**
     * Return the default description of the event category.  This description is in English and is a default
     * value for the situation when the natural language resource bundle for Event Category is not available.
     *
     * @return String default description
     */
    public String getDescription()
    {
        return description;
    }


    /**
     * toString() JSON-style
     *
     * @return string description
     */
    @Override
    public String toString()
    {
        return "OMRSEventCategory{" +
                "ordinal=" + ordinal +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
