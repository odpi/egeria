/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.assetowner.properties;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * The GlossaryTermStatus defines the status of a glossary term.  It effectively
 * defines its visibility to different types of queries.  Most queries by default will only return instances in the
 * active status.
 * <ul>
 *     <li>Unknown: Unknown instance status.</li>
 *     <li>Draft: The content is incomplete.</li>
 *     <li>Proposed: The content is in review.</li>
 *     <li>Approved: The content is approved.</li>
 *     <li>Active: The instance is approved and in use.</li>
 * </ul>
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public enum GlossaryTermStatus
{
    /**
     * Draft - The term is incomplete.
     */
    DRAFT     (1,1,  "Draft",     "The term is incomplete."),

    /**
     * Prepared - The term is ready for review.
     */
    PREPARED  (2,2,  "Prepared",  "The term is ready for review."),

    /**
     * Proposed - The term is in review.
     */
    PROPOSED  (3,3,  "Proposed",  "The term is in review."),

    /**
     * Approved - The term is approved and ready to be activated.
     */
    APPROVED  (4,4,  "Approved",  "The term is approved and ready to be activated."),

    /**
     * Rejected - The term is rejected and should not be used.
     */
    REJECTED  (5,5,  "Rejected",  "The term is rejected and should not be used."),

    /**
     * Active - The term is approved and in use.
     */
    ACTIVE    (6,15, "Active",    "The term is approved and in use."),

    /**
     * Deprecated - The term is out of date and should not be used.
     */
    DEPRECATED(7,30, "Deprecated","The term is out of date and should not be used."),

    /**
     * Other - The term is in a locally defined state.
     */
    OTHER     (8,50, "Other",     "The term is in a locally defined state."),
    ;


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
    GlossaryTermStatus(int    ordinal,
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
