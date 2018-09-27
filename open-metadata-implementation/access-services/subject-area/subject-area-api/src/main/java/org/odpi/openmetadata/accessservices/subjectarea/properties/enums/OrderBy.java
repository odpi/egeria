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
 * Defines the sequencing for a collection.
 */

@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum OrderBy implements Serializable {
    /**
      * Order by name property.
      */
    Name(0, "Order by name property.")
,
    /**
      * Order by owner property.
      */
    Owner(1, "Order by owner property.")
,
    /**
      * Order by date added to the metadata collection.
      */
    DateAdded(2, "Order by date added to the metadata collection.")
,
    /**
      * Order by date that the asset was updated.
      */
    DateUpdated(3, "Order by date that the asset was updated.")
,
    /**
      * Order by date that the asset was created.
      */
    DateCreated(4, "Order by date that the asset was created.")
,
    /**
      * Order by another property.
      */
    Other(99, "Order by another property.")
;
    private final int ordinal;
    private final String description ;
    /**
     * Default constructor sets up the specific values for an OrderBy enum instance.
     *
     * @param ordinal int enum value ordinal
     * @param description String description
     */
    OrderBy(int ordinal, String description) {
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
     * Return the descriptive name for the OrderBy enum instance
     *
     * @return String name
     */
    public String getName() { return this.name(); }
}
