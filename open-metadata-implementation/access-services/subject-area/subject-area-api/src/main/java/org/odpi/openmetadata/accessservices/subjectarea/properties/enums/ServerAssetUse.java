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
 * Defines how a server capability may use an asset.
 */

@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public enum ServerAssetUse implements Serializable {
    /**
      * The server capability is accountable for the maintenance and protection of the asset.
      */
    Owns(0, "The server capability is accountable for the maintenance and protection of the asset.")
,
    /**
      * The server capability provides management or oversight of the asset.
      */
    Governs(1, "The server capability provides management or oversight of the asset.")
,
    /**
      * The server capability keeps the asset up-to-date.
      */
    Maintains(2, "The server capability keeps the asset up-to-date.")
,
    /**
      * The server capability consumes the content of the asset.
      */
    Uses(3, "The server capability consumes the content of the asset.")
,
    /**
      * Another usage.
      */
    Other(99, "Another usage.")
;
    private final int ordinal;
    private final String description ;
    /**
     * Default constructor sets up the specific values for an ServerAssetUse enum instance.
     *
     * @param ordinal int enum value ordinal
     * @param description String description
     */
    ServerAssetUse(int ordinal, String description) {
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
     * Return the descriptive name for the ServerAssetUse enum instance
     *
     * @return String name
     */
    public String getName() { return this.name(); }
}
