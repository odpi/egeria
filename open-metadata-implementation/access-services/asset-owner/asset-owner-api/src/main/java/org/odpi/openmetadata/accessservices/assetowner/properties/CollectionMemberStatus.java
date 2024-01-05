/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.assetowner.properties;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * CollectionMemberStatus specifies the the status of the member in a collection.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public enum CollectionMemberStatus implements Serializable
{
    /**
     * The status of the member is not known or not specified. This is the default value.
     */
    UNKNOWN     (0,  0,  "Unknown",    "The status of the member is not known or not specified. This is the default value."),

    /**
     * The member was added by a discovery process.
     */
    DISCOVERED  (1,  1,  "Discovered", "The member was added by a discovery process."),

    /**
     * The member was proposed by a consumer.
     */
    PROPOSED    (2,  2,  "Proposed",   "The member was proposed by a consumer."),

    /**
     * The member was imported from another system.
     */
    IMPORTED    (3,  3,  "Imported",   "The member was imported from another system."),

    /**
     * The member has been validated by a custodian/steward/approver and can be trusted.
     */
    VALIDATED   (4,  4,  "Validated",  "The member has been validated by a custodian/steward/approver and can be trusted."),

    /**
     * The membership has been deprecated. Consider stopping using it.
     */
    DEPRECATED  (5,  5,  "Deprecated", "The membership has been deprecated. Consider stopping using it."),

    /**
     * The membership is obsolete and should not be used.
     */
    OBSOLETE    (6,  6,  "Obsolete",   "The membership is obsolete and should not be used."),

    /**
     * The membership has a different status not covered by the open metadata types.
     */
    OTHER       (99, 99, "Other",      "The membership has a different status not covered by the open metadata types.");

    private static final long serialVersionUID = 1L;

    private static final String ENUM_TYPE_GUID  = "a3bdb2ac-c28e-4e5a-8ab7-76aa01038832";
    private static final String ENUM_TYPE_NAME  = "MembershipStatus";

    private final int    openTypeOrdinal;

    private final int            ordinal;
    private final String         name;
    private final String         description;


    /**
     * Default constructor for the enumeration.
     *
     * @param ordinal numerical representation of the enumeration
     * @param openTypeOrdinal code number from the equivalent Enum Type
     * @param name default string name of the enumeration
     * @param description default string description of the enumeration
     */
    CollectionMemberStatus(int    ordinal,
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
     * toString() JSON-style
     *
     * @return string description
     */
    @Override
    public String toString()
    {
        return "CollectionMemberStatus : " + name;
    }
}
