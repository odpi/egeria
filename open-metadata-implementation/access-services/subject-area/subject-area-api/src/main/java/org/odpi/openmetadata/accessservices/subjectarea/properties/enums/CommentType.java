/* SPDX-License-Identifier: Apache-2.0 */

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
 * Descriptor for a comment that indicated its intent.
 */

@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum CommentType implements Serializable {
    /**
      * General comment.
      */
    GeneralComment(0, "General comment.")
,
    /**
      * A question.
      */
    Question(1, "A question.")
,
    /**
      * An answer to a previously asked question.
      */
    Answer(2, "An answer to a previously asked question.")
,
    /**
      * A suggestion for improvement.
      */
    Suggestion(3, "A suggestion for improvement.")
,
    /**
      * An account of an experience.
      */
    Experience(3, "An account of an experience.")
;
    private final int ordinal;
    private final String description ;
    /**
     * Default constructor sets up the specific values for an CommentType enum instance.
     *
     * @param ordinal int enum value ordinal
     * @param description String description
     */
    CommentType(int ordinal, String description) {
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
     * Return the descriptive name for the CommentType enum instance
     *
     * @return String name
     */
    public String getName() { return this.name(); }
}
