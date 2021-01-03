/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.governanceaction.properties;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * The ElementStatus defines the status of an element in the metadata collection.  It effectively
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
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public enum ElementStatus implements Serializable
{
    UNKNOWN                 (0,  "<Unknown>",               "Unknown instance status."),
    DRAFT                   (1,  "Draft",                   "The content is incomplete."),
    PREPARED                (2,  "Prepared",                "The content is ready for review."),
    PROPOSED                (3,  "Proposed",                "The content is in review."),
    APPROVED                (4,  "Approved",                "The content is approved."),
    REJECTED                (5,  "Rejected",                "The request or proposal is rejected."),
    APPROVED_CONCEPT        (6,  "Approved concept",        "The request or proposal is approved for development."),
    UNDER_DEVELOPMENT       (7,  "Under development",       "The instance is being developed."),
    DEVELOPMENT_COMPLETE    (8,  "Development complete",    "The development of the instance is complete."),
    APPROVED_FOR_DEPLOYMENT (9,  "Approved for deployment", "The instance is approved for deployment."),
    STANDBY                 (10, "StandBy",                 "The instance is deployed in standby mode."),
    ACTIVE                  (15, "Active",                  "The instance is approved and in use."),
    FAILED                  (20, "Failed",                  "The instance is not in use due to failure."),
    DISABLED                (21, "Disabled",                "The instance is shutdown or disabled."),
    COMPLETE                (22, "Complete",                "The activity associated with the instance is complete."),
    DEPRECATED              (30, "Deprecated",              "The instance is out of date and should not be used."),
    OTHER                   (50, "Other",                   "The instance is in a locally defined state.");

    private static final long serialVersionUID = 1L;

    private  int    ordinal;
    private  String name;
    private  String description;


    /**
     * Default constructor sets up the specific values for an enum instance.
     *
     * @param ordinal int enum value ordinal
     * @param name String name
     * @param description String description
     */
    ElementStatus(int     ordinal,
                  String  name,
                  String  description)
    {
        this.ordinal = ordinal;
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
        return "ElementStatus{" +
                "ordinal=" + ordinal +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
