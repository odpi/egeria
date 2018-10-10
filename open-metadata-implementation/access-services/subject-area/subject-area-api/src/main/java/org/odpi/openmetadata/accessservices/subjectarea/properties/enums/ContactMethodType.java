/* SPDX-License-Identifier: Apache-2.0 */

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
 * Mechanism to contact an individual.
 */

@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum ContactMethodType implements Serializable {
    /**
      * Contact through email.
      */
    Email(0, "Contact through email.")
,
    /**
      * Contact through telephone number.
      */
    Phone(1, "Contact through telephone number.")
,
    /**
      * Contact through social media or similar account.
      */
    Account(2, "Contact through social media or similar account.")
,
    /**
      * Another usage.
      */
    Other(99, "Another usage.")
;
    private final int ordinal;
    private final String description ;
    /**
     * Default constructor sets up the specific values for an ContactMethodType enum instance.
     *
     * @param ordinal int enum value ordinal
     * @param description String description
     */
    ContactMethodType(int ordinal, String description) {
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
     * Return the descriptive name for the ContactMethodType enum instance
     *
     * @return String name
     */
    public String getName() { return this.name(); }
}
