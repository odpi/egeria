/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.assetowner.properties;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * CollectionMemberStatus specifies the status of an element's membership in a collection.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public enum CollectionMembershipStatus
{
    UNKNOWN     (0, 0,  "Unknown", "The membership origin is unknown."),
    DISCOVERED  (1, 1,  "Discovered", "The membership was discovered by an automated process."),
    ASSIGNED    (2, 2,  "Assigned", "The membership was proposed by an expert curator."),
    IMPORTED    (3, 3,  "Imported", "The membership was imported from another metadata system."),
    VALIDATED   (4, 4,  "Validated", "The membership created by an automated process has been validated and approved by an expert curator."),
    DEPRECATED  (5, 5,  "Deprecated", "The membership should no longer be used."),
    OBSOLETE    (6, 6,  "Obsolete", "The membership must no longer be used."),
    OTHER       (7, 99, "Other", "Another membership status. See userDefinedStatus.");

    private final int            ordinal;
    private final int            openMetadataOrdinal;
    private final String         name;
    private final String         description;


    /**
     * Default constructor for the enumeration.
     *
     * @param ordinal numerical representation of the enumeration
     * @param openMetadataOrdinal numerical representation of the enumeration from the open metadata types
     * @param name default string name of the enumeration
     * @param description default string description of the enumeration
     */
    CollectionMembershipStatus(int    ordinal,
                               int    openMetadataOrdinal,
                               String name,
                               String description)
    {
        this.ordinal = ordinal;
        this.openMetadataOrdinal = openMetadataOrdinal;
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
     * Return the numeric representation of the enumeration from the open metadata types.
     *
     * @return int ordinal
     */
    public int getOpenMetadataOrdinal() { return openMetadataOrdinal; }


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
        return "CollectionMembershipStatus : " + name;
    }
}
