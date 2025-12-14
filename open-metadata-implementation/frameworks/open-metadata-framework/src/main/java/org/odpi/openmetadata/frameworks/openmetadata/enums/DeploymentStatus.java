/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.openmetadata.enums;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataWikiPages;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * The DeploymentStatus defines the status of an infrastructure element in the metadata collection.  It is optional.
 * If it is not included in the element, the element is assumed to be in ACTIVE status.  If the status is set to OTHER
 * it means that the userDefinedDeploymentStatus is in use.
 * <ul>
 *     <li>Proposed: The content is in review.</li>
 *     <li>Under development: The instance is being developed.</li>
 *     <li>Development complete: The development of the instance is complete.</li>
 *     <li>Approved for deployment: The instance is approved for deployment.</li>
 *     <li>Rejected: The request to deploy is rejected.</li>
 *     <li>StandBy: The instance is deployed in standby mode.</li>
 *     <li>Active: The instance is approved and in use.</li>
 *     <li>Disabled: The instance is shutdown or disabled.</li>
 *     <li>Failed: The instance is not in use due to failure.</li>
 *     <li>Other: The instance is in a locally defined state.</li>
 * </ul>
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public enum DeploymentStatus implements OpenMetadataEnum
{
    /**
     * The content is in review.
     */
    PROPOSED                (0,  "Proposed",                "The content is in review.", "d94fa65e-6104-4770-ab76-54290f124b30"),

    /**
     * The instance is being developed.
     */
    UNDER_DEVELOPMENT       (1,  "Under development",       "The instance is being developed.", "4c12b07e-456e-4d9f-896f-af658e2e56af"),

    /**
     * The development of the instance is complete.
     */
    DEVELOPMENT_COMPLETE    (2,  "Development complete",    "The development of the instance is complete.", "5021b989-9366-45b0-b319-96c73e3f8327"),

    /**
     * The instance is approved for deployment.
     */
    APPROVED_FOR_DEPLOYMENT (3,  "Approved for deployment", "The instance is approved for deployment.", "e12fbb93-68e9-47dd-8358-0210333596f6"),

    /**
     * The request to deploy is rejected.
     */
    REJECTED                (4,  "Rejected",                "The request to deploy is not approved.", "fea9cadb-c6ad-4cd1-a7b8-8ec828763f06"),

    /**
     * The instance is deployed in standby mode.
     */
    STANDBY                 (5, "StandBy",                 "The instance is deployed in standby mode.", "eb6c2178-fb0d-4cde-8765-5bf1ea9f2b04"),

    /**
     * The instance is approved and in use.
     */
    ACTIVE                  (6, "Active",                  "The instance is approved and in use.", "1246d30b-bd49-47e6-a9ff-4dfc27b10cfb"),

    /**
     * The instance is shutdown or disabled.
     */
    DISABLED                (7, "Disabled",                "The instance is shutdown or disabled.", "ebf70b34-2cc8-47eb-9123-d5c212aa03a8"),

    /**
     * The instance is not in use due to failure.
     */
    FAILED                  (8, "Failed",                  "The instance is not in use due to failure.", "e16ea92f-0e58-4953-a073-d77c52287426"),

    /**
     * The instance is in a locally defined state.
     */
    OTHER                   (99, "Other",                   "The instance is in a locally defined state.", "655a377c-c665-4f89-bb85-c3aa3e60877e");

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
    DeploymentStatus(int     ordinal,
                     String  name,
                     String  description,
                     String  descriptionGUID)
    {
        this.ordinal = ordinal;
        this.name = name;
        this.description = description;
        this.descriptionGUID = descriptionGUID;
    }


    private static final String ENUM_TYPE_GUID  = "77b057f0-8088-461f-bcc4-e557d58ec94b";
    private static final String ENUM_TYPE_NAME  = "DeploymentStatus";

    private static final String ENUM_DESCRIPTION = "Defines the current status of an infrastructure element.";
    private static final String ENUM_DESCRIPTION_GUID = "25d51c94-1be1-4504-a58e-f9ca230b309f";
    private static final String ENUM_DESCRIPTION_WIKI = OpenMetadataWikiPages.MODEL_0010_BASE_MODEL;



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
     * Return the unique identifier for the open metadata enum type that this enum class represents.
     *
     * @return string guid
     */
    public static String getOpenTypeGUID() { return ENUM_TYPE_GUID; }


    /**
     * Return the unique name for the open metadata enum type that this enum class represents.
     *
     * @return string name
     */
    public static String getOpenTypeName() { return ENUM_TYPE_NAME; }


    /**
     * Return the description for the open metadata enum type that this enum class represents.
     *
     * @return string description
     */
    public static String getOpenTypeDescription()
    {
        return ENUM_DESCRIPTION;
    }


    /**
     * Return the unique identifier for the valid value element for the open metadata enum type that this enum class represents.
     *
     * @return string guid
     */
    public static String getOpenTypeDescriptionGUID()
    {
        return ENUM_DESCRIPTION_GUID;
    }


    /**
     * Return the unique identifier for the valid value element for the open metadata enum type that this enum class represents.
     *
     * @return string guid
     */
    public static String getOpenTypeDescriptionWiki()
    {
        return ENUM_DESCRIPTION_WIKI;
    }



    /**
     * toString() JSON-style
     *
     * @return string description
     */
    @Override
    public String toString()
    {
        return "DeploymentStatus{" +
                "ordinal=" + ordinal +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
