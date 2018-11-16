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
 * Defines the sequential order in which bytes are arranged into larger numerical values when stored in memory or when transmitted over digital links.
 */

@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public enum Endianness implements Serializable {
    /**
      * Bits or bytes order from the big end.
      */
    BigEndian(0, "Bits or bytes order from the big end.")
,
    /**
      * Bits or bytes ordered from the little end.
      */
    LittleEndian(1, "Bits or bytes ordered from the little end.")
;
    private final int ordinal;
    private final String description ;
    /**
     * Default constructor sets up the specific values for an Endianness enum instance.
     *
     * @param ordinal int enum value ordinal
     * @param description String description
     */
    Endianness(int ordinal, String description) {
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
     * Return the descriptive name for the Endianness enum instance
     *
     * @return String name
     */
    public String getName() { return this.name(); }
}
