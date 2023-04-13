/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.assetmanager.properties;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * A GlossaryTermActivityType defines the type of activity described by a glossary term.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public enum GlossaryTermActivityType implements Serializable
{
    /**
     * Operation - A small, well defined processing operation.
     */
    OPERATION  (0,  0,  "Operation","A small, well defined processing operation."),

    /**
     * Action - A requested or required change.
     */
    ACTION     (1,  1,  "Action",   "A requested or required change."),

    /**
     * Task - A piece of work for a person, organization or engine.
     */
    TASK       (2,  2,  "Task",     "A piece of work for a person, organization or engine."),

    /**
     * Process - A sequence of tasks.
     */
    PROCESS    (3,  3,  "Process",  "A sequence of tasks."),

    /**
     * Project - An organized activity to achieve a specific goal.
     */
    PROJECT    (4,  4,  "Project",  "An organized activity to achieve a specific goal."),

    /**
     * Other - An organized activity to achieve a specific goal.
     */
    OTHER      (99, 99, "Other",    "Another type of activity.");

    public static final String ENUM_TYPE_GUID  = "af7e403d-9865-4ebb-8c1a-1fd57b4f4bca";
    public static final String ENUM_TYPE_NAME  = "ActivityType";

    private final int    openTypeOrdinal;

    private final int    ordinal;
    private final String name;
    private final String description;

    private static final long     serialVersionUID = 1L;


    /**
     * Constructor to set up the instance of this enum.
     *
     * @param ordinal code number
     * @param openTypeOrdinal code number from the equivalent Enum Type
     * @param name default name
     * @param description default description
     */
    GlossaryTermActivityType(int    ordinal,
                             int    openTypeOrdinal,
                             String name,
                             String description)
    {
        this.ordinal         = ordinal;
        this.openTypeOrdinal = openTypeOrdinal;
        this.name            = name;
        this.description     = description;
    }

    /**
     * Return the code for this enum instance
     *
     * @return int key pattern code
     */
    public int getOrdinal()
    {
        return ordinal;
    }


    /**
     * Return the default name for this enum instance.
     *
     * @return String default name
     */
    public String getName()
    {
        return name;
    }


    /**
     * Return the default description for the key pattern for this enum instance.
     *
     * @return String default description
     */
    public String getDescription()
    {
        return description;
    }


    /**
     * Return the code for this enum that comes from the Open Metadata Type that this enum represents.
     *
     * @return int code number
     */
    public int getOpenTypeOrdinal()
    {
        return openTypeOrdinal;
    }


    /**
     * Return the unique identifier for the open metadata enum type that this enum class represents.
     *
     * @return string guid
     */
    public String getOpenTypeGUID() { return ENUM_TYPE_GUID; }


    /**
     * Return the unique name for the open metadata enum type that this enum class represents.
     *
     * @return string name
     */
    public String getOpenTypeName() { return ENUM_TYPE_NAME; }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "GlossaryTermActivityType{" +
                       "openTypeOrdinal=" + openTypeOrdinal +
                       ", ordinal=" + ordinal +
                       ", name='" + name + '\'' +
                       ", description='" + description + '\'' +
                       ", openTypeGUID='" + getOpenTypeGUID() + '\'' +
                       ", openTypeName='" + getOpenTypeName() + '\'' +
                       '}';
    }
}