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
 * BusinessCapabilityType defines the type of business capability supported by an organization.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public enum BusinessCapabilityType implements OpenMetadataEnum
{
    /**
     * The business capability has not been classified.
     */
    UNCLASSIFIED     (0,  "ba489093-0bd4-416a-9b24-ed1574b93789",  "Unclassified",    "The business capability has not been classified.", true),

    /**
     * A functional business capability.
     */
    BUSINESS_SERVICE  (1,  "612a3ecc-218b-47fd-bd75-fa8b6cdae87c",  "BusinessService", "A functional business capability.", false),

    /**
     * A collection of related business services.
     */
    BUSINESS_AREA  (2,  "fd762806-e784-4990-916f-acba931e8807",  "BusinessArea", "A collection of related business services.", false),

    /**
     * An overall area of activity in which a business operates.  A single organization may operate multiple business domains, such as retail, distribution, banking, ...
     */
    BUSINESS_DOMAIN  (3,  "cbae16ce-3941-4f1b-8170-403b5b46725e",  "BusinessDomain", "An overall area of activity in which a business operates.  A single organization may operate multiple business domains, such as retail, distribution, banking, ...", false),

    /**
     * Another business capability type.
     */
    OTHER  (99,  "769813b7-4e0f-40b4-a0f7-a74165de0ed5",  "Other", "Another business capability type.", false),

    ;

    private static final String ENUM_TYPE_GUID  = "fb7c40cf-8d95-48ff-ba8b-e22bff6f5a91";
    private static final String ENUM_TYPE_NAME  = "BusinessCapabilityType";

    private static final String ENUM_DESCRIPTION = "Defines the type or category of a business capability.";
    private static final String ENUM_DESCRIPTION_GUID = "ad3fcd68-4371-421e-9c9c-e944b11ad288";
    private static final String ENUM_DESCRIPTION_WIKI = OpenMetadataWikiPages.MODEL_0715_DIGITAL_BUSINESS;

    private final String descriptionGUID;

    private final int            ordinal;
    private final String         name;
    private final String         description;
    private final boolean        isDefault;


    /**
     * Default constructor for the enumeration.
     *
     * @param ordinal numerical representation of the enumeration
     * @param descriptionGUID identifier for valid value
     * @param name default string name of the enumeration
     * @param description default string description of the enumeration
     * @param isDefault is this the default value for the enum?
     */
    BusinessCapabilityType(int     ordinal,
                           String  descriptionGUID,
                           String  name,
                           String  description,
                           boolean isDefault)
    {
        this.ordinal = ordinal;
        this.name            = name;
        this.descriptionGUID = descriptionGUID;
        this.description     = description;
        this.isDefault = isDefault;
    }



    /**
     * Return the numeric representation of the enumeration.
     *
     * @return int ordinal
     */
    @Override
    public int getOrdinal() { return ordinal; }


    /**
     * Return the default name of the enumeration.
     *
     * @return String name
     */
    @Override
    public String getName() { return name; }


    /**
     * Return the default description of the enumeration.
     *
     * @return String description
     */
    @Override
    public String getDescription() { return description; }


    /**
     * Return the unique identifier for the valid value that represents the enum value.
     *
     * @return  guid
     */
    @Override
    public  String getDescriptionGUID()
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
        return isDefault;
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
        return "BusinessCapabilityType{" +
            "ordinal=" + ordinal +
            ", name='" + name + '\'' +
            ", description='" + description + '\'' +
            ", descriptionGUID='" + descriptionGUID + '\'' +
            ", isDefault='" + isDefault + '\'' +
            '}';
    }
}
