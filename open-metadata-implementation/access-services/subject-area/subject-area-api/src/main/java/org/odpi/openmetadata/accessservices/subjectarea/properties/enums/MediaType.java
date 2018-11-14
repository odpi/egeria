/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.subjectarea.properties.enums;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonInclude;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * Defines the type of media.
 */

@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public enum MediaType implements Serializable {
    /**
      * The media is an image.
      */
    Image(0, "The media is an image.")
,
    /**
      * The media is an audio recording.
      */
    Audio(1, "The media is an audio recording.")
,
    /**
      * The media is a text document, probably rich text.
      */
    Document(2, "The media is a text document, probably rich text.")
,
    /**
      * The media is a video recording.
      */
    Video(3, "The media is a video recording.")
,
    /**
      * Another type of media, probably not supported.
      */
    Other(99, "Another type of media, probably not supported.")
;
    private final int ordinal;
    private final String description ;
    /**
     * Default constructor sets up the specific values for an MediaType enum instance.
     *
     * @param ordinal int enum value ordinal
     * @param description String description
     */
    MediaType(int ordinal, String description) {
        this.ordinal = ordinal;
        this.description = description;
    }

    /**
     * Return the numerical value for the enum.
     *
     * @return int enum value ordinal
     */
    public int getOrdinal() { return this.ordinal; }
    /**
     * Return the description for the enum.
     *
     * @return String description
     */
    public String getDescription() { return this.description; }
    /**
     * Return the descriptive name for the MediaType enum instance
     *
     * @return String name
     */
    public String getName() { return this.name(); }
}
