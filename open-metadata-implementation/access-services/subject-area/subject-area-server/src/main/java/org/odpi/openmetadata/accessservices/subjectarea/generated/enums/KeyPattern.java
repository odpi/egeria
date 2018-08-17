/* SPDX-License-Identifier: Apache-2.0 */

package org.odpi.openmetadata.accessservices.subjectarea.generated.enums;
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
 * Defines the type of identifier used for an asset.
 */

@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum KeyPattern implements Serializable {
    /**
      * Unique key allocated and used within the scope of a single system.
      */
    LocalKey(0, "Unique key allocated and used within the scope of a single system.")
,
    /**
      * Key allocated and used within the scope of a single system that is periodically reused for different records.
      */
    RecycledKey(1, "Key allocated and used within the scope of a single system that is periodically reused for different records.")
,
    /**
      * Key derived from an attribute of the entity, such as email address, passport number.
      */
    NaturalKey(2, "Key derived from an attribute of the entity, such as email address, passport number.")
,
    /**
      * Key value copied from another system.
      */
    MirrorKey(3, "Key value copied from another system.")
,
    /**
      * Key formed by combining keys from multiple systems.
      */
    AggregateKey(4, "Key formed by combining keys from multiple systems.")
,
    /**
      * Key from another system can bey used if system name provided.
      */
    CallersKey(5, "Key from another system can bey used if system name provided.")
,
    /**
      * Key value will remain active even if records are merged.
      */
    StableKey(6, "Key value will remain active even if records are merged.")
,
    /**
      * Another key pattern.
      */
    Other(99, "Another key pattern.")
;
    private final int ordinal;
    private final String description ;
    /**
     * Default constructor sets up the specific values for an KeyPattern enum instance.
     *
     * @param ordinal int enum value ordinal
     * @param description String description
     */
    KeyPattern(int ordinal, String description) {
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
     * Return the descriptive name for the KeyPattern enum instance
     *
     * @return String name
     */
    public String getName() { return this.name(); }
}
