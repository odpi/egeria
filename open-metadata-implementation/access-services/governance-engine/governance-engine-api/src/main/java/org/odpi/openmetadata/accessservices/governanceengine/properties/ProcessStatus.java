/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.governanceengine.properties;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * The ProcessStatus defines the status of a process.  It effectively
 * defines its visibility to different types of queries.  Most queries by default will only return instances in the
 * active status.
 * <ul>
 *     <li>Unknown: Unknown process status.</li>
 *     <li>Draft: The process is incomplete.</li>
 *     <li>Proposed: The process is in review.</li>
 *     <li>Approved: The process is approved.</li>
 *     <li>Active: The process is approved and in use.</li>
 * </ul>
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public enum ProcessStatus implements Serializable
{
    UNKNOWN   (0,0,  "<Unknown>","Unknown process status."),
    DRAFT     (1,1,  "Draft",    "The process is incomplete."),
    PROPOSED  (2,3,  "Proposed", "The process is in review."),
    APPROVED  (3,4,  "Approved", "The process is approved."),
    ACTIVE    (4,15, "Active",   "The process is approved and in use.");


    private final int    ordinal;
    private final int    openTypeOrdinal;
    private final String name;
    private final String description;

    private static final long     serialVersionUID = 1L;


    /**
     * Constructor to set up the instance of this enum.
     *
     * @param ordinal code number
     * @param openTypeOrdinal code number from the equivalent Enum Type
     * @param name default name
     * @param description default description
     */
    ProcessStatus(int    ordinal,
                  int    openTypeOrdinal,
                  String name,
                  String description)
    {
        this.ordinal         = ordinal;
        this.openTypeOrdinal = openTypeOrdinal;
        this.name            = name;
        this.description     = description;
    }

    /**
     * Return the code for this enum instance
     *
     * @return int key pattern code
     */
    public int getOrdinal()
    {
        return ordinal;
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
     * Return the default name for this enum instance.
     *
     * @return String default name
     */
    public String getName()
    {
        return name;
    }


    /**
     * Return the default description for the key pattern for this enum instance.
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
        return "GlossaryTermStatus{" +
                       "ordinal=" + ordinal +
                       ", openTypeOrdinal=" + openTypeOrdinal +
                       ", name='" + name + '\'' +
                       ", description='" + description + '\'' +
                       '}';
    }}
