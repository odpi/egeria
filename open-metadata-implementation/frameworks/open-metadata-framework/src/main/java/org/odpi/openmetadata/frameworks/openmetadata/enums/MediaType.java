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
 * The MediaType defines the type of resource referenced in a related media reference.
 * <ul>
 *     <li>Image - The media is an image.</li>
 *     <li>Audio - The media is an audio recording.</li>
 *     <li>Document - The media is a text document - probably rich text.</li>
 *     <li>Video - The media is a video recording.</li>
 *     <li>Other - The media type is not supported.</li>
 * </ul>
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public enum MediaType implements OpenMetadataEnum
{
    /**
     * The media is an image.
     */
    IMAGE("9f7d9fff-1b66-4461-b9c1-32b84ab6f8b70",    0,  "Image",    "The media is an image."),

    /**
     * The media is an audio recording.
     */
    AUDIO("53dad430-81c0-422e-a265-8ef49748ab1a",    1,  "Audio",    "The media is an audio recording."),

    /**
     * The media is a text document - probably rich text.
     */
    DOCUMENT("9e1447cd-a053-4c55-9a99-b6771697fcfb", 2,  "Document", "The media is a text document - probably rich text."),

    /**
     * The media is a video recording.
     */
    VIDEO("3e351708-b5ae-46c9-890e-83892cd96bb6",    3,  "Video",    "The media is a video recording."),

    /**
     * The media type is not supported.
     */
    OTHER("340ffbeb-3076-4038-ae07-417b685c4825",   99, "Other",    "The media type is not supported.");

    private static final String ENUM_TYPE_GUID  = "9548390c-69f5-4dc6-950d-6fdffb257b56";
    private static final String ENUM_TYPE_NAME  = "MediaType";

    private static final String ENUM_DESCRIPTION = "Defines the type of media.";
    private static final String ENUM_DESCRIPTION_GUID = "7d2d2830-d95b-4d9e-8f46-26f5eace592b";
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
    MediaType(String  descriptionGUID,
              int     ordinal,
              String  name,
              String  description)
    {
        this.ordinal = ordinal;
        this.name            = name;
        this.descriptionGUID = descriptionGUID;
        this.description     = description;
    }


    /**
     * Return the code for this enum instance
     *
     * @return int media type code
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
     * Return the default description for this enum instance.
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
     * Return the description for the open metadata enum type that this enum class represents.
     *
     * @return string description
     */
    public static String getOpenTypeDescription()
    {
        return ENUM_DESCRIPTION;
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "MediaType{mediaTypeName='" + name + '\'' + '}';
    }
}