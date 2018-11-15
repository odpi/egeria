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
 * Defines the status values of a governance action classification.
 */

@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public enum GovernanceClassificationStatus implements Serializable {
    /**
      * The classification assignment was discovered by an automated process.
      */
    Discovered(0, "The classification assignment was discovered by an automated process.")
,
    /**
      * The classification assignment was proposed by a subject matter expert.
      */
    Proposed(1, "The classification assignment was proposed by a subject matter expert.")
,
    /**
      * The classification assignment was imported from another metadata system.
      */
    Imported(2, "The classification assignment was imported from another metadata system.")
,
    /**
      * The classification assignment has been validated and approved by a subject matter expert.
      */
    Validated(3, "The classification assignment has been validated and approved by a subject matter expert.")
,
    /**
      * The classification assignment should no longer be used.
      */
    Deprecated(4, "The classification assignment should no longer be used.")
,
    /**
      * The classification assignment must no longer be used.
      */
    Obsolete(5, "The classification assignment must no longer be used.")
,
    /**
      * Another classification assignment status.
      */
    Other(99, "Another classification assignment status.")
;
    private final int ordinal;
    private final String description ;
    /**
     * Default constructor sets up the specific values for an GovernanceClassificationStatus enum instance.
     *
     * @param ordinal int enum value ordinal
     * @param description String description
     */
    GovernanceClassificationStatus(int ordinal, String description) {
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
     * Return the descriptive name for the GovernanceClassificationStatus enum instance
     *
     * @return String name
     */
    public String getName() { return this.name(); }
}
