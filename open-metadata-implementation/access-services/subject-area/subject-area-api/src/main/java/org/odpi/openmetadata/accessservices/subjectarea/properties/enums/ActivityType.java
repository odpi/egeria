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
 * Different types of activities.
 */


@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonSerialize(include=JsonSerialize.Inclusion.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum ActivityType implements Serializable {
    /**
      * Normal processing.
      */
    Operation(0, "Normal processing.")
,
    /**
      * A requested of required change.
      */
    Action(1, "A requested of required change.")
,
    /**
      * A piece of work for a person, organization or engine.
      */
    Task(2, "A piece of work for a person, organization or engine.")
,
    /**
      * A sequence of tasks.
      */
    Process(3, "A sequence of tasks.")
,
    /**
      * An organized activity to achieve a specific goal.
      */
    Project(3, "An organized activity to achieve a specific goal.")
,
    /**
      * Another type of activity.
      */
    Other(99, "Another type of activity.")
;
    private final int ordinal;
    private final String description ;
    /**
     * Default constructor sets up the specific values for an ActivityType enum instance.
     *
     * @param ordinal int enum value ordinal
     * @param description String description
     */
    ActivityType(int ordinal, String description) {
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
     * Return the descriptive name for the ActivityType enum instance
     *
     * @return String name
     */
    public String getName() { return this.name(); }
}
