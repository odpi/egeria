/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.openmetadata.enums;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * The ElementStatus defines the status of an element in the metadata collection.  It effectively
 * defines its visibility to different types of queries.  Most queries by default will only return instances not in the
 * deleted status.
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
public enum ElementStatus implements OpenMetadataEnum
{
    /**
     * Unknown instance status.
     */
    UNKNOWN                 (0,  "<Unknown>",               "Unknown instance status.", "b557752a-e44a-467e-85c1-9716d105f8fb"),

    /**
     * The content is incomplete.
     */
    DRAFT                   (1,  "Draft",                   "The content is incomplete.", "62363ab9-e290-45de-8f48-40476295cc2e"),

    /**
     * The content is ready for review.
     */
    PREPARED                (2,  "Prepared",                "The content is ready for review.", "05ce396b-32fa-4d10-99cc-b51bb3bd14fa"),

    /**
     * The content is in review.
     */
    PROPOSED                (3,  "Proposed",                "The content is in review.", "192f17f9-4c4e-4475-867a-08c1049490c5"),

    /**
     * The content is approved.
     */
    APPROVED                (4,  "Approved",                "The content is approved.", "79bf466a-6c29-4ccb-ac3e-6dea5c615105"),

    /**
     * The request or proposal is rejected.
     */
    REJECTED                (5,  "Rejected",                "The request or proposal is rejected.", "7133d0d1-0ff9-4de0-8221-75ba5a06948c"),

    /**
     * The request or proposal is approved for development.
     */
    APPROVED_CONCEPT        (6,  "Approved concept",        "The request or proposal is approved for development.", "0117b3e2-e3eb-4121-adce-b3ee690f0927"),

    /**
     * The instance is being developed.
     */
    UNDER_DEVELOPMENT       (7,  "Under development",       "The instance is being developed.", "5770306b-e138-47a1-9a19-795961ec6eed"),

    /**
     * The development of the instance is complete.
     */
    DEVELOPMENT_COMPLETE    (8,  "Development complete",    "The development of the instance is complete.", "161e0435-cb28-48d6-b403-b408b547b217"),

    /**
     * The instance is approved for deployment.
     */
    APPROVED_FOR_DEPLOYMENT (9,  "Approved for deployment", "The instance is approved for deployment.", "81e5b82e-e354-4b26-88bb-781a62f2c5a7"),

    /**
     * The instance is deployed in standby mode.
     */
    STANDBY                 (10, "StandBy",                 "The instance is deployed in standby mode.", "9fd7eefe-2ee5-4fac-850f-9a78cfe59ac1"),

    /**
     * The instance is approved and in use.
     */
    ACTIVE                  (15, "Active",                  "The instance is approved and in use.", "10e474c2-e98d-4cfc-b10c-a00998369c3b"),

    /**
     * The instance is not in use due to failure.
     */
    FAILED                  (20, "Failed",                  "The instance is not in use due to failure.", "8a01f280-3360-4d48-a1a2-0351de57c5af"),

    /**
     * The instance is shutdown or disabled.
     */
    DISABLED                (21, "Disabled",                "The instance is shutdown or disabled.", "a414db34-20a5-4394-b33a-94eeee7f44c2"),

    /**
     * The activity associated with the instance is complete.
     */
    COMPLETE                (22, "Complete",                "The activity associated with the instance is complete.", "34d96768-bf4c-460e-ac86-42907188fe22"),

    /**
     * The instance is out of date and should not be used.
     */
    DEPRECATED              (30, "Deprecated",              "The instance is out of date and should not be used.", "b1f5b468-60ad-452e-957e-82d1bbbb2ea9"),

    /**
     * The instance has been deleted and is no longer available.
     */
    DELETED                (99, "Deleted",              "The instance has been deleted and is no longer available.", "44dc841f-f406-443d-8b3f-c87a810e29f6"),

    /**
     * The instance is in a locally defined state.
     */
    OTHER                   (50, "Other",                   "The instance is in a locally defined state.", "40c72f5b-9342-405e-a69c-710cb605ef12");

    private final int    ordinal;
    private final String name;
    private final String description;
    private final String descriptionGUID;


    /**
     * Default constructor sets up the specific values for an enum instance.
     *
     * @param ordinal int enum value ordinal
     * @param name String name
     * @param description String description
     * @param descriptionGUID unique identifier for the valid value
     */
    ElementStatus(int     ordinal,
                  String  name,
                  String  description,
                  String  descriptionGUID)
    {
        this.ordinal = ordinal;
        this.name = name;
        this.description = description;
        this.descriptionGUID = descriptionGUID;
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
     * Return the unique identifier for the valid value that represents the enum value.
     *
     * @return guid
     */
    @Override
    public String getDescriptionGUID()
    {
        return descriptionGUID;
    }


    /**
     * Return whether the enum is the default value or not.
     *
     * @return boolean
     */
    @Override
    public boolean isDefault()
    {
        return false;
    }


    /**
     * toString() JSON-style
     *
     * @return string description
     */
    @Override
    public String toString()
    {
        return "InstanceStatus{" +
                "ordinal=" + ordinal +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
