/* SPDX-License-Identifier: Apache-2.0 */

package org.odpi.openmetadata.accessservices.subjectarea.generated.enums;
//import com.fasterxml.jackson.annotation.JsonAutoDetect;
//import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
//import com.fasterxml.jackson.annotation.JsonFormat;
//import com.fasterxml.jackson.databind.annotation.JsonSerialize;
//import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * Defines the level of confidence to place in the accuracy of a data item.
 */

// uncomment to generate a class that json serialises
//@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
//@JsonSerialize(include=JsonSerialize.Inclusion.NON_NULL)
//@JsonIgnoreProperties(ignoreUnknown=true)
//@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum ConfidenceLevel implements Serializable {
    /**
      * There is no assessment of the confidence level of this data.
      */
    Unclassified(0, "There is no assessment of the confidence level of this data.")
,
    /**
      * The data comes from an ad hoc process.
      */
    AdHoc(1, "The data comes from an ad hoc process.")
,
    /**
      * The data comes from a transactional system so it may have a narrow scope.
      */
    Transactional(2, "The data comes from a transactional system so it may have a narrow scope.")
,
    /**
      * The data comes from an authoritative source.
      */
    Authoritative(3, "The data comes from an authoritative source.")
,
    /**
      * The data is derived from other data through an analytical process.
      */
    Derived(4, "The data is derived from other data through an analytical process.")
,
    /**
      * The data comes from an obsolete source and must no longer be used.
      */
    Obsolete(5, "The data comes from an obsolete source and must no longer be used.")
,
    /**
      * Another confidence level.
      */
    Other(99, "Another confidence level.")
;
    private final int ordinal;
    private final String description ;
    /**
     * Default constructor sets up the specific values for an ConfidenceLevel enum instance.
     *
     * @param ordinal int enum value ordinal
     * @param description String description
     */
    ConfidenceLevel(int ordinal, String description) {
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
     * Return the descriptive name for the ConfidenceLevel enum instance
     *
     * @return String name
     */
    public String getName() { return this.name(); }
}
