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
     * The instance is approved and in use.
     */
    ACTIVE                  (15, "Active",                  "The instance is approved and in use.", "10e474c2-e98d-4cfc-b10c-a00998369c3b"),

    /**
     * The instance is not in use due to failure.
     */
    INVALID                 (20, "Invalid", "The instance is not in use due to bad content.", "8a01f280-3360-4d48-a1a2-0351de57c5af"),

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
    @Override
    public int getOrdinal() { return ordinal; }


    /**
     * Return the descriptive name for the enum.
     *
     * @return String name
     */
    @Override
    public String getName() { return name; }


    /**
     * Return the description for the enum.
     *
     * @return String description
     */
    @Override
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
