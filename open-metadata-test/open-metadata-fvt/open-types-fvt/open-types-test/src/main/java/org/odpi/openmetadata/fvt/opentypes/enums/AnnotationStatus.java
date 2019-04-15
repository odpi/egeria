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
 * Defines the status of an annotation.
 */

@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public enum AnnotationStatus implements Serializable {
    /**
      * The annotation is new.
      */
    New(0, "The annotation is new.")
,
    /**
      * The annotation has been reviewed by a steward.
      */
    Reviewed(1, "The annotation has been reviewed by a steward.")
,
    /**
      * The annotation has been approved.
      */
    Approved(2, "The annotation has been approved.")
,
    /**
      * The request has been actioned.
      */
    Actioned(3, "The request has been actioned.")
,
    /**
      * The annotation is invalid or incorrect.
      */
    Invalid(4, "The annotation is invalid or incorrect.")
,
    /**
      * The annotation should be ignored.
      */
    Ignore(5, "The annotation should be ignored.")
,
    /**
      * Another status.
      */
    Other(99, "Another status.")
;
    private final int ordinal;
    private final String description ;
    /**
     * Default constructor sets up the specific values for an AnnotationStatus enum instance.
     *
     * @param ordinal int enum value ordinal
     * @param description String description
     */
    AnnotationStatus(int ordinal, String description) {
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
     * Return the descriptive name for the AnnotationStatus enum instance
     *
     * @return String name
     */
    public String getName() { return this.name(); }
}
