/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.metadatasecurity.properties;

/**
 * The ReferenceableStatus enum defines the status of an instance (classification, relationship or an entity) in the metadata collection.  It
 * effectively
 * defines its visibility to different types of queries.  Most queries by default will only return instances in the
 * active status.
 * <ul>
 *     <li>Unknown: Unknown instance status.</li>
 *     <li>Draft: The content is incomplete.</li>
 *     <li>Proposed: The content is in review.</li>
 *     <li>Approved: The content is approved.</li>
 *     <li>Rejected: The request or proposal is rejected.</li>
 *     <li>Approved concept: The request or proposal is approved for development.</li>
 *     <li>Under development: The instance is being developed.</li>
 *     <li>Development complete: The development of the instance is complete.</li>
 *     <li>Approved for deployment: The instance is approved for deployment.</li>
 *     <li>StandBy: The instance is deployed in standby mode.</li>
 *     <li>Active: The instance is approved and in use.</li>
 *     <li>Failed: The instance is not in use due to failure.</li>
 *     <li>Disabled: The instance is shutdown or disabled.</li>
 *     <li>Complete: The activity associated with the instance is complete.</li>
 *     <li>Deprecated: The instance is out of date and should not be used.</li>
 *     <li>Other: The instance is in a locally defined state.</li>
 *     <li>Deleted: The instance has been deleted and is waiting to be purged.  It is kept in the metadata collection
 *     to support a restore request.  It is not returned on normal queries.</li>
 * </ul>
 */

public enum ReferenceableStatus
{
    /**
     * Unknown instance status.
     */
    UNKNOWN                 (0,  0,  "<Unknown>",               "Unknown instance status."),

    /**
     * The content is incomplete.
     */
    DRAFT                   (1,  1,  "Draft",                   "The content is incomplete."),

    /**
     * The content is ready for review.
     */
    PREPARED                (2,  2,  "Prepared",                "The content is ready for review."),

    /**
     * The content is in review.
     */
    PROPOSED                (3,  3,  "Proposed",                "The content is in review."),

    /**
     * The content is approved.
     */
    APPROVED                (4,  4,  "Approved",                "The content is approved."),

    /**
     * The request or proposal is rejected.
     */
    REJECTED                (5,  5,  "Rejected",                "The request or proposal is rejected."),

    /**
     * The request or proposal is approved for development.
     */
    APPROVED_CONCEPT        (6,  6,  "Approved concept",        "The request or proposal is approved for development."),

    /**
     * The instance is being developed.
     */
    UNDER_DEVELOPMENT       (7,  7,  "Under development",       "The instance is being developed."),

    /**
     * The development of the instance is complete.
     */
    DEVELOPMENT_COMPLETE    (8,  8,  "Development complete",    "The development of the instance is complete."),

    /**
     * The instance is approved for deployment.
     */
    APPROVED_FOR_DEPLOYMENT (9,  9,  "Approved for deployment", "The instance is approved for deployment."),

    /**
     * The instance is deployed in standby mode.
     */
    STANDBY                 (10, 10, "StandBy",                 "The instance is deployed in standby mode."),

    /**
     * The instance is approved and in use.
     */
    ACTIVE                  (15, 15, "Active",                  "The instance is approved and in use."),

    /**
     * The instance is not in use due to failure.
     */
    FAILED                  (20, 20, "Failed",                  "The instance is not in use due to failure."),

    /**
     * The instance is shutdown or disabled.
     */
    DISABLED                (21, 21, "Disabled",                "The instance is shutdown or disabled."),

    /**
     * The activity associated with the instance is complete.
     */
    COMPLETE                (22, 22, "Complete",                "The activity associated with the instance is complete."),

    /**
     * The instance is out of date and should not be used.
     */
    DEPRECATED              (30, 30, "Deprecated",              "The instance is out of date and should not be used."),

    /**
     * The instance is in a locally defined state.
     */
    OTHER                   (50, 50, "Other",                   "The instance is in a locally defined state."),

    /**
     * The instance has been deleted and is no longer available.
     */
    DELETED                 (99, 99, "Deleted",                 "The instance has been deleted and is no longer available.");


    private  final int    ordinal;
    private  final int    omrsOrdinal;
    private  final String name;
    private  final String description;


    /**
     * Default constructor sets up the specific values for an enum instance.
     *
     * @param ordinal int enum value ordinal
     * @param name     String name
     * @param description String description
     */
    ReferenceableStatus(int     ordinal,
                        int     omrsOrdinal,
                        String  name,
                        String  description)
    {
        this.ordinal = ordinal;
        this.omrsOrdinal = omrsOrdinal;
        this.name = name;
        this.description = description;
    }


    /**
     * Return the numerical value for the enum.
     *
     * @return int enum value ordinal
     */
    public int getOrdinal() { return ordinal; }


    /**
     * Return the ordinal for the equivalent OMRS status.  This helps convert between an OMRS entity and the security bean.
     *
     * @return int enum value ordinal from OMRS
     */
    public int getOMRSOrdinal()
    {
        return omrsOrdinal;
    }


    /**
     * Return the descriptive name for the enum.
     *
     * @return String name
     */
    public String getName() { return name; }


    /**
     * Return the description for the enum.
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
        return "ReferenceableStatus{" +
                "ordinal=" + ordinal +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
