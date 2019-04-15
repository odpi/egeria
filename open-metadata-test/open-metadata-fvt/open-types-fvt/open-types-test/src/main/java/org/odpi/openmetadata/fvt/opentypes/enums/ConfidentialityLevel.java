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
 * Defines how confidential a data item is.
 */

@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public enum ConfidentialityLevel implements Serializable {
    /**
      * The data is public information.
      */
    Unclassified(0, "The data is public information.")
,
    /**
      * The data should not be exposed outside of this organization.
      */
    Internal(1, "The data should not be exposed outside of this organization.")
,
    /**
      * The data should be protected and only shared with people with a need to see it.
      */
    Confidential(2, "The data should be protected and only shared with people with a need to see it.")
,
    /**
      * The data is sensitive and inappropriate use may adversely impact the data subject.
      */
    Sensitive(3, "The data is sensitive and inappropriate use may adversely impact the data subject.")
,
    /**
      * The data is very valuable and must be restricted to a very small number of people.
      */
    Restricted(4, "The data is very valuable and must be restricted to a very small number of people.")
,
    /**
      * Another confidentially level.
      */
    Other(99, "Another confidentially level.")
;
    private final int ordinal;
    private final String description ;
    /**
     * Default constructor sets up the specific values for an ConfidentialityLevel enum instance.
     *
     * @param ordinal int enum value ordinal
     * @param description String description
     */
    ConfidentialityLevel(int ordinal, String description) {
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
     * Return the descriptive name for the ConfidentialityLevel enum instance
     *
     * @return String name
     */
    public String getName() { return this.name(); }
}
