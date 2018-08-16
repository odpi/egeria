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
 * Level of support or appreciation for an item.
 */

@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum StarRating implements Serializable {
    /**
      * This content is not recommended.
      */
    NotRecommended(0, "This content is not recommended.")
,
    /**
      * One star rating.
      */
    OneStar(1, "One star rating.")
,
    /**
      * Two star rating.
      */
    TwoStar(2, "Two star rating.")
,
    /**
      * Three star rating.
      */
    ThreeStar(3, "Three star rating.")
,
    /**
      * Four star rating.
      */
    FourStar(4, "Four star rating.")
,
    /**
      * Five star rating.
      */
    FiveStar(5, "Five star rating.")
;
    private final int ordinal;
    private final String description ;
    /**
     * Default constructor sets up the specific values for an StarRating enum instance.
     *
     * @param ordinal int enum value ordinal
     * @param description String description
     */
    StarRating(int ordinal, String description) {
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
     * Return the descriptive name for the StarRating enum instance
     *
     * @return String name
     */
    public String getName() { return this.name(); }
}
