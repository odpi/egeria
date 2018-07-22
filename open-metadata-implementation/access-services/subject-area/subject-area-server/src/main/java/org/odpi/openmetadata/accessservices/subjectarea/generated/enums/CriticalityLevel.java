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
 * Defines how important a data item is to the organization.
 */

// uncomment to generate a class that json serialises
//@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
//@JsonSerialize(include=JsonSerialize.Inclusion.NON_NULL)
//@JsonIgnoreProperties(ignoreUnknown=true)
//@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum CriticalityLevel implements Serializable {
    /**
      * There is no assessment of the criticality of this data.
      */
    Unclassified(0, "There is no assessment of the criticality of this data.")
,
    /**
      * The data is of minor importance to the organization.
      */
    Marginal(1, "The data is of minor importance to the organization.")
,
    /**
      * The data is important to the running of the organization.
      */
    Important(2, "The data is important to the running of the organization.")
,
    /**
      * The data is critical to the operation of the organization.
      */
    Critical(3, "The data is critical to the operation of the organization.")
,
    /**
      * The data is so important that its loss is catastrophic putting the future of the organization in doubt.
      */
    Catastrophic(4, "The data is so important that its loss is catastrophic putting the future of the organization in doubt.")
,
    /**
      * Another criticality level.
      */
    Other(99, "Another criticality level.")
;
    private final int ordinal;
    private final String description ;
    /**
     * Default constructor sets up the specific values for an CriticalityLevel enum instance.
     *
     * @param ordinal int enum value ordinal
     * @param description String description
     */
    CriticalityLevel(int ordinal, String description) {
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
     * Return the descriptive name for the CriticalityLevel enum instance
     *
     * @return String name
     */
    public String getName() { return this.name(); }
}
