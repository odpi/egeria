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
 * Defines the confidence in the assigned relationship.
 */

@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public enum TermRelationshipStatus implements Serializable {
    /**
      * The term relationship is in development.
      */
    Draft(0, "The term relationship is in development.")
,
    /**
      * The term relationship is approved and in use.
      */
    Active(1, "The term relationship is approved and in use.")
,
    /**
      * The term relationship should no longer be used.
      */
    Deprecated(2, "The term relationship should no longer be used.")
,
    /**
      * The term relationship must no longer be used.
      */
    Obsolete(3, "The term relationship must no longer be used.")
,
    /**
      * Another term relationship status.
      */
    Other(99, "Another term relationship status.")
;
    private final int ordinal;
    private final String description ;
    /**
     * Default constructor sets up the specific values for an TermRelationshipStatus enum instance.
     *
     * @param ordinal int enum value ordinal
     * @param description String description
     */
    TermRelationshipStatus(int ordinal, String description) {
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
     * Return the descriptive name for the TermRelationshipStatus enum instance
     *
     * @return String name
     */
    public String getName() { return this.name(); }
}
