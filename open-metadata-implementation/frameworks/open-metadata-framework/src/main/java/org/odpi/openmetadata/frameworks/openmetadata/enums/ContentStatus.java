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
 * The ContentStatus defines the status of an authored referenceable or data asset element in the metadata collection.
 * It is optional.
 * If it is not included in the element, the element is assumed to be in ACTIVE status.  If the status is set to OTHER
 * it means that the userDefinedContentStatus is in use.
 * <ul>
 *     <li>Draft: The content is incomplete.</li>
 *     <li>Prepared: The content is ready for review.</li>
 *     <li>Proposed: The content is in review.</li>
 *     <li>Approved: The content is approved.</li>
 *     <li>Rejected: The request or proposal is rejected.</li>
 *     <li>Active: The instance is approved and in use.</li>
 *     <li>Deprecated: The instance is out of date and should not be used.</li>
 *     <li>Other: The instance is in a locally defined state.</li>
 * </ul>
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public enum ContentStatus implements OpenMetadataEnum
{
    /**
     * The content is incomplete.
     */
    DRAFT                   (0,  "Draft",                   "The content is incomplete.", "44084c0e-f893-4a6b-b364-c48f50338702"),

    /**
     * The content is ready for review.
     */
    PREPARED                (1,  "Prepared",                "The content is ready for review.", "bc8fe5f2-d605-4c05-9054-d37ac9fe2811"),

    /**
     * The content is in review.
     */
    PROPOSED                (2,  "Proposed",                "The content is in review.", "f376157b-a596-4b83-946b-d7c0e4aadefd"),

    /**
     * The content is approved.
     */
    APPROVED                (3,  "Approved",                "The content is approved.", "48c5d720-1001-421a-ba13-634c946bc605"),

    /**
     * The request or proposal is rejected.
     */
    REJECTED                (4,  "Rejected",                "The request or proposal is rejected.", "cf9ce6fb-d90a-4cb1-8c05-82fed71640e8"),

    /**
     * The instance is approved and in use.
     */
    ACTIVE                  (5, "Active",                  "The instance is approved and in use.", "9be9aaee-f394-4342-83ed-ba29cc107398"),

    /**
     * The instance is out of date and should not be used.
     */
    DEPRECATED              (6, "Deprecated",              "The instance is out of date and should not be used.", "c831d2a8-0b08-45ae-930d-e19038e7e1b1"),

    /**
     * The instance is in a locally defined state.
     */
    OTHER                   (99, "Other",                   "The instance is in a locally defined state.", "2fd04d45-d6f2-4008-a679-42f7326f9b01");

    private static final String ENUM_TYPE_GUID  = "49f2808c-b5c8-48a9-8a36-672f9ea9a45f";
    private static final String ENUM_TYPE_NAME  = "ContentStatus";

    private static final String ENUM_DESCRIPTION = "Defines the current status of an authored referenceable.";
    private static final String ENUM_DESCRIPTION_GUID = "034dbb1b-9f95-4c57-8d83-28bb435e6584";
    private static final String ENUM_DESCRIPTION_WIKI = OpenMetadataWikiPages.MODEL_0010_BASE_MODEL;

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
    ContentStatus(int     ordinal,
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
        return "ContentStatus{" +
                "ordinal=" + ordinal +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
