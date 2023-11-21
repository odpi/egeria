/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.digitalservice.properties;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * CollectionStatus specifies which subset of a collection should be returned.  The relationship
 * that links a member into a collection has an start and end date (called the effective dates).  The
 * Community Profile OMAS uses the CollectionStatus and the effective dates to determine which members to return.
 * <ul>
 *     <li>ACTIVE - all the collection members with a current effective dates.  This is the default.</li>
 *     <li>PAST - all the collection members that have effective dates in the past.</li>
 *     <li>FUTURE - all the collection members that become effective in the future.</li>
 *     <li>ALL - all the collection members linked to the collection irrespective of their effective dates.</li>
 * </ul>
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public enum CollectionStatus
{
    /**
     * All the collection members with a current effective dates (default).
     */
    ACTIVE   (0,  "Active",   "All the collection members with a current effective dates (default)."),

    /**
     * All the collection members that have effective dates in the past.
     */
    PAST     (1,  "Past",     "All the collection members that have effective dates in the past."),

    /**
     * All the collection members that become effective in the future.
     */
    FUTURE   (2,  "Future",   "All the collection members that become effective in the future."),

    /**
     * All the collection members linked to the collection irrespective of their effective dates.
     */
    ALL      (99, "All",      "All the collection members linked to the collection irrespective of their effective dates.");


    private final int            ordinal;
    private final String         name;
    private final String         description;


    /**
     * Default constructor for the enumeration.
     *
     * @param ordinal numerical representation of the enumeration
     * @param name default string name of the enumeration
     * @param description default string description of the enumeration
     */
    CollectionStatus(int  ordinal, String name, String description)
    {
        this.ordinal = ordinal;
        this.name = name;
        this.description = description;
    }


    /**
     * Return the numeric representation of the enumeration.
     *
     * @return int ordinal
     */
    public int getOrdinal() { return ordinal; }


    /**
     * Return the default name of the enumeration.
     *
     * @return String name
     */
    public String getName() { return name; }


    /**
     * Return the default description of the enumeration.
     *
     * @return String description
     */
    public String getDescription() { return description; }


    /**
     * toString() JSON-style
     *
     * @return string description
     */
    @Override
    public String toString()
    {
        return "CollectionStatus : " + name;
    }
}
