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
 * Defines the type of identifier for an asset's owner.
 */

@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public enum AssetOwnerType implements Serializable {
    /**
      * The owner's userId is specified (default).
      */
    UserId(0, "The owner's userId is specified (default).")
,
    /**
      * The unique identifier (guid) of the profile of the owner.
      */
    ProfileId(1, "The unique identifier (guid) of the profile of the owner.")
,
    /**
      * Another type of owner identifier, probably not supported by open metadata.
      */
    Other(99, "Another type of owner identifier, probably not supported by open metadata.")
;
    private final int ordinal;
    private final String description ;
    /**
     * Default constructor sets up the specific values for an AssetOwnerType enum instance.
     *
     * @param ordinal int enum value ordinal
     * @param description String description
     */
    AssetOwnerType(int ordinal, String description) {
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
     * Return the descriptive name for the AssetOwnerType enum instance
     *
     * @return String name
     */
    public String getName() { return this.name(); }
}
