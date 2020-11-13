/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.connectors.properties.beans;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * The RelatedMediaType defines the type of resource referenced in a related media reference.
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
public enum RelatedMediaType implements Serializable
{
    IMAGE(0,    0,  "Image",    "The media is an image."),
    AUDIO(1,    1,  "Audio",    "The media is an audio recording."),
    DOCUMENT(2, 2,  "Document", "The media is a text document - probably rich text."),
    VIDEO(3,    3,  "Video",    "The media is a video recording."),
    OTHER(99,   99, "Other",    "The media type is not supported.");

    public static final String ENUM_TYPE_GUID  = "9548390c-69f5-4dc6-950d-6fdffb257b56";
    public static final String ENUM_TYPE_NAME  = "MediaType";

    private int    openTypeOrdinal;

    private int    ordinal;
    private String name;
    private String description;

    private static final long     serialVersionUID = 1L;


    /**
     * Constructor to set up the instance of this enum.
     *
     * @param ordinal code number
     * @param openTypeOrdinal code number from the equivalent Enum Type
     * @param name default name
     * @param description default description
     */
    RelatedMediaType(int    ordinal,
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
    public String getOpenTypeGUID() { return ENUM_TYPE_GUID; }


    /**
     * Return the unique name for the open metadata enum type that this enum class represents.
     *
     * @return string name
     */
    public String getOpenTypeName() { return ENUM_TYPE_NAME; }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "RelatedMediaType{" +
                "mediaTypeCode=" + ordinal +
                ", mediaTypeName='" + name + '\'' +
                ", mediaTypeDescription='" + description + '\'' +
                '}';
    }
}