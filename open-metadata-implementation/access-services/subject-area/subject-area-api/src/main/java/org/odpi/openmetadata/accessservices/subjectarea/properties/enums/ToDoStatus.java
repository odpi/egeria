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
 * Progress on completing an action (to do).
 */

@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum ToDoStatus implements Serializable {
    /**
      * No action has been taken.
      */
    Open(0, "No action has been taken.")
,
    /**
      * Work is underway to complete the action.
      */
    InProgress(1, "Work is underway to complete the action.")
,
    /**
      * Work is blocked waiting for resource of another action to complete.
      */
    Waiting(2, "Work is blocked waiting for resource of another action to complete.")
,
    /**
      * The action has been completed successfully.
      */
    Complete(3, "The action has been completed successfully.")
,
    /**
      * Work has stopped on the action and will not recommence.
      */
    Abandoned(99, "Work has stopped on the action and will not recommence.")
;
    private final int ordinal;
    private final String description ;
    /**
     * Default constructor sets up the specific values for an ToDoStatus enum instance.
     *
     * @param ordinal int enum value ordinal
     * @param description String description
     */
    ToDoStatus(int ordinal, String description) {
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
     * Return the descriptive name for the ToDoStatus enum instance
     *
     * @return String name
     */
    public String getName() { return this.name(); }
}
