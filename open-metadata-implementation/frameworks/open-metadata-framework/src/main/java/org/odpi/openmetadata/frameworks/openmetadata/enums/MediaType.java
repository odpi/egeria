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
    IMAGE(0,    0,  "Image",    "The media is an image."),

    /**
     * The media is an audio recording.
     */
    AUDIO(1,    1,  "Audio",    "The media is an audio recording."),

    /**
     * The media is a text document - probably rich text.
     */
    DOCUMENT(2, 2,  "Document", "The media is a text document - probably rich text."),

    /**
     * The media is a video recording.
     */
    VIDEO(3,    3,  "Video",    "The media is a video recording."),

    /**
     * The media type is not supported.
     */
    OTHER(99,   99, "Other",    "The media type is not supported.");

    private static final String ENUM_TYPE_GUID  = "9548390c-69f5-4dc6-950d-6fdffb257b56";
    private static final String ENUM_TYPE_NAME  = "MediaType";

    private static final String ENUM_DESCRIPTION = "Defines the type of media.";
    private static final String ENUM_DESCRIPTION_GUID = "7d2d2830-d95b-4d9e-8f46-26f5eace592b";
    private static final String ENUM_DESCRIPTION_WIKI = OpenMetadataWikiPages.MODEL_0015_LINKED_MEDIA_TYPES;

    private final int    openTypeOrdinal;

    private final int    ordinal;
    private final String name;
    private final String description;




    /**
     * Constructor to set up the instance of this enum.
     *
     * @param ordinal code number
     * @param openTypeOrdinal code number from the equivalent Enum Type
     * @param name default name
     * @param description default description
     */
    MediaType(int    ordinal,
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
     * Return the code for this enum instance
     *
     * @return int media type code
     */
    public int getOrdinal()
    {
        return ordinal;
    }


    /**
     * Return the default name for this enum instance.
     *
     * @return String default name
     */
    public String getName()
    {
        return name;
    }


    /**
     * Return the default description for this enum instance.
     *
     * @return String default description
     */
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
        return null;
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
        return "MediaType{" +
                "mediaTypeCode=" + ordinal +
                ", mediaTypeName='" + name + '\'' +
                ", mediaTypeDescription='" + description + '\'' +
                '}';
    }
}