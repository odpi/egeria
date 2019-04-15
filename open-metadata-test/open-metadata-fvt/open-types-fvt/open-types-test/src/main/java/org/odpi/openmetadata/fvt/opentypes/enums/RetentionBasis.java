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
 * Defines the retention requirements associated with a data item.
 */

@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public enum RetentionBasis implements Serializable {
    /**
      * There is no assessment of the retention requirements for this data.
      */
    Unclassified(0, "There is no assessment of the retention requirements for this data.")
,
    /**
      * This data is temporary.
      */
    Temporary(1, "This data is temporary.")
,
    /**
      * The data is needed for the lifetime of the referenced project.
      */
    ProjectLifetime(2, "The data is needed for the lifetime of the referenced project.")
,
    /**
      * The data is needed for the lifetime of the referenced team.
      */
    TeamLifetime(3, "The data is needed for the lifetime of the referenced team.")
,
    /**
      * The data is needed for the lifetime of the referenced contract.
      */
    ContractLifetime(4, "The data is needed for the lifetime of the referenced contract.")
,
    /**
      * The retention period for the data is defined by the referenced regulation.
      */
    RegulatedLifetime(5, "The retention period for the data is defined by the referenced regulation.")
,
    /**
      * The data is needed for the specified time.
      */
    TimeBoxedLifetime(6, "The data is needed for the specified time.")
,
    /**
      * Another basis for determining the retention requirement.
      */
    Other(99, "Another basis for determining the retention requirement.")
;
    private final int ordinal;
    private final String description ;
    /**
     * Default constructor sets up the specific values for an RetentionBasis enum instance.
     *
     * @param ordinal int enum value ordinal
     * @param description String description
     */
    RetentionBasis(int ordinal, String description) {
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
     * Return the descriptive name for the RetentionBasis enum instance
     *
     * @return String name
     */
    public String getName() { return this.name(); }
}
