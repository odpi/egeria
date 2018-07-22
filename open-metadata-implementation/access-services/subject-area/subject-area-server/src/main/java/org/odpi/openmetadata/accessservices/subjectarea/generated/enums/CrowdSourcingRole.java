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
 * Type of contributor to new information and/or assets.
 */

// uncomment to generate a class that json serialises
//@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
//@JsonSerialize(include=JsonSerialize.Inclusion.NON_NULL)
//@JsonIgnoreProperties(ignoreUnknown=true)
//@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum CrowdSourcingRole implements Serializable {
    /**
      * Actor that creates the initial version.
      */
    Proposer(0, "Actor that creates the initial version.")
,
    /**
      * Actor that provided feedback.
      */
    Reviewer(1, "Actor that provided feedback.")
,
    /**
      * Actor that agrees with the definition.
      */
    Supporter(2, "Actor that agrees with the definition.")
,
    /**
      * Actor that declares the definition should be used.
      */
    Approver(3, "Actor that declares the definition should be used.")
,
    /**
      * Another role.
      */
    Other(99, "Another role.")
;
    private final int ordinal;
    private final String description ;
    /**
     * Default constructor sets up the specific values for an CrowdSourcingRole enum instance.
     *
     * @param ordinal int enum value ordinal
     * @param description String description
     */
    CrowdSourcingRole(int ordinal, String description) {
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
     * Return the descriptive name for the CrowdSourcingRole enum instance
     *
     * @return String name
     */
    public String getName() { return this.name(); }
}
