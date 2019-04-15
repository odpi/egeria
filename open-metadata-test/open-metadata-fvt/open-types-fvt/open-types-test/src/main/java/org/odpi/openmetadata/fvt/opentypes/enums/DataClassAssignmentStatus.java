/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.fvt.opentypes.enums;
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
 * Defines the provenance and confidence of a data class assignment.
 */

@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public enum DataClassAssignmentStatus implements Serializable {
    /**
      * The data class assignment was discovered by an automated process.
      */
    Discovered(0, "The data class assignment was discovered by an automated process.")
,
    /**
      * The data class assignment was proposed by a subject matter expert.
      */
    Proposed(1, "The data class assignment was proposed by a subject matter expert.")
,
    /**
      * The data class assignment was imported from another metadata system.
      */
    Imported(2, "The data class assignment was imported from another metadata system.")
,
    /**
      * The data class assignment has been validated and approved by a subject matter expert.
      */
    Validated(3, "The data class assignment has been validated and approved by a subject matter expert.")
,
    /**
      * The data class assignment should no longer be used.
      */
    Deprecated(4, "The data class assignment should no longer be used.")
,
    /**
      * The data class assignment must no longer be used.
      */
    Obsolete(5, "The data class assignment must no longer be used.")
,
    /**
      * Another data class assignment status.
      */
    Other(99, "Another data class assignment status.")
;
    private final int ordinal;
    private final String description ;
    /**
     * Default constructor sets up the specific values for an DataClassAssignmentStatus enum instance.
     *
     * @param ordinal int enum value ordinal
     * @param description String description
     */
    DataClassAssignmentStatus(int ordinal, String description) {
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
     * Return the descriptive name for the DataClassAssignmentStatus enum instance
     *
     * @return String name
     */
    public String getName() { return this.name(); }
}
