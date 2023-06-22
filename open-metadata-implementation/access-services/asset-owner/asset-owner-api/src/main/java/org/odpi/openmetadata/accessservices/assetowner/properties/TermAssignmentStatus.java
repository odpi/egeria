/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.assetowner.properties;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * The TermAssignmentStatus defines the status of the relationship between a glossary term and an element that represents data.
 * It indicates how much trust a steward or process should give to the relationship based on its source.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public enum TermAssignmentStatus
{
    /**
     * Discovered - The term assignment was discovered by an automated process.
     */
    DISCOVERED (0,0,  "Discovered","The term assignment was discovered by an automated process."),

    /**
     * Proposed - The term assignment was proposed by a subject-matter expert.
     */
    PROPOSED  (1,1,  "Proposed",  "The term assignment was proposed by a subject-matter expert."),

    /**
     * Imported - The term assignment was imported from another metadata system.
     */
    IMPORTED  (2,2,  "Imported",  "The term assignment was imported from another metadata system."),

    /**
     * Validated - The term assignment has been validated and approved by a subject-matter expert.
     */
    VALIDATED  (3,3,  "Validated",  "The term assignment has been validated and approved by a subject-matter expert."),

    /**
     * Deprecated - The term assignment is out of date and should not be used.
     */
    DEPRECATED(4,4, "Deprecated","The term assignment should no longer be used."),

    /**
     * Obsolete - The term assignment must no longer be used.
     */
    OBSOLETE(5,5, "Obsolete","The term assignment must no longer be used."),

    /**
     * Other - The term assignment is in a locally defined state.
     */
    OTHER     (99,99, "Other",     "The term assignment is in a locally defined state."),
    ;


    private final int    ordinal;
    private final int    openTypeOrdinal;
    private final String name;
    private final String description;


    /**
     * Constructor to set up the instance of this enum.
     *
     * @param ordinal code number
     * @param openTypeOrdinal code number from the equivalent Enum Type
     * @param name default name
     * @param description default description
     */
    TermAssignmentStatus(int    ordinal,
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
        return "TermAssignmentStatus{" +
                       "ordinal=" + ordinal +
                       ", openTypeOrdinal=" + openTypeOrdinal +
                       ", name='" + name + '\'' +
                       ", description='" + description + '\'' +
                       '}';
    }}
