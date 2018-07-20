/* SPDX-License-Identifier: Apache-2.0 */

package org.odpi.openmetadata.accessservices.subjectarea.properties.enums;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.io.Serializable;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * Defines the status values of a governance definition.
 */


@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonSerialize(include=JsonSerialize.Inclusion.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum GovernanceDefinitionStatus implements Serializable {
    /**
      * The governance definition is under development.
      */
    Proposed(0, "The governance definition is under development.")
,
    /**
      * The governance definition is approved and in use.
      */
    Active(1, "The governance definition is approved and in use.")
,
    /**
      * The governance definition has been replaced.
      */
    Deprecated(2, "The governance definition has been replaced.")
,
    /**
      * The governance definition must not be used any more.
      */
    Obsolete(3, "The governance definition must not be used any more.")
,
    /**
      * Another governance definition status.
      */
    Other(99, "Another governance definition status.")
;
    private final int ordinal;
    private final String description ;
    /**
     * Default constructor sets up the specific values for an GovernanceDefinitionStatus enum instance.
     *
     * @param ordinal int enum value ordinal
     * @param description String description
     */
    GovernanceDefinitionStatus(int ordinal, String description) {
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
     * Return the descriptive name for the GovernanceDefinitionStatus enum instance
     *
     * @return String name
     */
    public String getName() { return this.name(); }
}
