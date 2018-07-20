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
 * Defines the type or category of business capability.
 */

// uncomment to generate a class that json serialises
//@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
//@JsonSerialize(include=JsonSerialize.Inclusion.NON_NULL)
//@JsonIgnoreProperties(ignoreUnknown=true)
//@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum BusinessCapabilityType implements Serializable {
    /**
      * The business capability has not been classified.
      */
    Unclassified(0, "The business capability has not been classified.")
,
    /**
      * A functional business capability.
      */
    BusinessService(1, "A functional business capability.")
,
    /**
      * A collection of related business services.
      */
    BusinessArea(2, "A collection of related business services.")
,
    /**
      * Another governance definition status.
      */
    Other(99, "Another governance definition status.")
;
    private final int ordinal;
    private final String description ;
    /**
     * Default constructor sets up the specific values for an BusinessCapabilityType enum instance.
     *
     * @param ordinal int enum value ordinal
     * @param description String description
     */
    BusinessCapabilityType(int ordinal, String description) {
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
     * Return the descriptive name for the BusinessCapabilityType enum instance
     *
     * @return String name
     */
    public String getName() { return this.name(); }
}
