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
 * The MediaUsage defines how a related media reference can be used in conjunction with the asset properties.
 * These usage options are not mutually exclusive.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public enum MediaUsage implements OpenMetadataEnum
{
    /**
     * Provides a small image to represent the asset in tree views and graphs.
     */
    ICON           ("a54bdf05-b0e4-4b7d-a4c6-171eb5a1106f", 0, "Icon", "Provides a small image to represent the asset in tree views and graphs."),

    /**
     * Provides a small image about the asset that can be used in lists.
     */
    THUMBNAIL      ("af0ba30c-dc67-4b12-863c-9a4bf955a197", 1, "Thumbnail", "Provides a small image about the asset that can be used in lists."),

    /**
     * Illustrates how the asset works or what it contains. It is complementary to the asset's description.
     */
    ILLUSTRATION   ("cc3cc0b9-dbd1-4e3e-a97b-8a57d77a09cf", 2, "Illustration", "Illustrates how the asset works or what it contains. It is complementary to the asset's description."),

    /**
     * Provides guidance to a person on how to use the asset.
     */
    USAGE_GUIDANCE ("7d51d180-52f4-46a5-b4f4-14fed97dcdb6", 3, "Usage Guidance", "Provides guidance to a person on how to use the asset."),

    /**
     * Another usage.
     */
    OTHER          ("2eb7afc3-1c9f-42fb-8634-6ec06d6023af", 99, "Other", "Another usage.");

    private static final String ENUM_TYPE_GUID  = "c6861a72-7485-48c9-8040-876f6c342b61";
    private static final String ENUM_TYPE_NAME  = "MediaUsage";

    private static final String ENUM_DESCRIPTION = "Defines how a related media reference should be used.";
    private static final String ENUM_DESCRIPTION_GUID = "55f8bcb1-2cd5-4965-859c-62cd083985ce";
    private static final String ENUM_DESCRIPTION_WIKI = OpenMetadataWikiPages.MODEL_0015_LINKED_MEDIA_TYPES;
    private final String descriptionGUID;

    private final int            ordinal;
    private final String         name;
    private final String         description;


    /**
     * Default constructor for the enumeration.
     *
     * @param ordinal numerical representation of the enumeration
     * @param descriptionGUID identifier for valid value
     * @param name default string name of the enumeration
     * @param description default string description of the enumeration
     */
    MediaUsage(String  descriptionGUID,
               int     ordinal,
               String  name,
               String  description)
    {
        this.ordinal         = ordinal;
        this.name            = name;
        this.descriptionGUID = descriptionGUID;
        this.description     = description;
    }


    /**
     * Return the code for this enum instance
     *
     * @return int media usage code
     */
    @Override
    public int getOrdinal()
    {
        return ordinal;
    }


    /**
     * Return the default name for this enum instance.
     *
     * @return String default name
     */
    @Override
    public String getName()
    {
        return name;
    }


    /**
     * Return the default description for the media usage pattern for this enum instance.
     *
     * @return String default description
     */
    @Override
    public String getDescription()
    {
        return description;
    }


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
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "MediaUsage{" + 
              "ordinal=" + ordinal +
            ", name='" + name + '\'' +
            ", description='" + description + '\'' +
            ", descriptionGUID='" + descriptionGUID + '\'' +
            '}';
    }
}
