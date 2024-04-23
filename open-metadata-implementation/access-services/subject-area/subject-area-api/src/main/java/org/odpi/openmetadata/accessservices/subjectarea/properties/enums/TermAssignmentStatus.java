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
 * Defines the provenance and confidence of a term assignment.
 */

@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public enum TermAssignmentStatus implements Serializable {
    /**
      * The term assignment was discovered by an automated process.
      */
    Discovered(0, "The term assignment was discovered by an automated process.")
,
    /**
      * The term assignment was proposed by a subject matter expert.
      */
    Proposed(1, "The term assignment was proposed by a subject matter expert.")
,
    /**
      * The term assignment was imported from another metadata system.
      */
    Imported(2, "The term assignment was imported from another metadata system.")
,
    /**
      * The term assignment has been validated and approved by a subject matter expert.
      */
    Validated(3, "The term assignment has been validated and approved by a subject matter expert.")
,
    /**
      * The term assignment should no longer be used.
      */
    Deprecated(4, "The term assignment should no longer be used.")
,
    /**
      * The term assignment must no longer be used.
      */
    Obsolete(5, "The term assignment must no longer be used.")
,
    /**
      * Another term assignment status.
      */
    Other(99, "Another term assignment status.")
;
    private final int ordinal;
    private final String description ;
    /**
     * Default constructor sets up the specific values for an GlossaryTermAssignmentStatus enum instance.
     *
     * @param ordinal int enum value ordinal
     * @param description String description
     */
    TermAssignmentStatus(int ordinal, String description) {
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
     * Return the descriptive name for the GlossaryTermAssignmentStatus enum instance
     *
     * @return String name
     */
    public String getName() { return this.name(); }
}
