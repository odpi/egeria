/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.communityprofile.properties;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * ToDoStatus defines the progress towards completing a to do.
 * <ul>
 *     <li>Open - The to do has been documented but no action taken.</li>
 *     <li>In Progress - The assigned person is working on the action defined in the to do.</li>
 *     <li>Waiting - The assigned person is unable to proceed because another action needs to complete first.</li>
 *     <li>Complete - The requested action is complete.</li>
 *     <li>Abandoned - The requested action has been abandoned and will never complete.</li>
 * </ul>
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public enum ToDoStatus implements Serializable
{
    OPEN        (0,  0,  "Open",        "The to do has been documented but no action taken."),
    IN_PROGRESS (1,  1,  "In Progress", "The assigned person is working on the action defined in the to do."),
    WAITING     (2,  2,  "Waiting",     "The assigned person is unable to proceed because another action needs to complete first."),
    COMPLETE    (3,  3,  "Complete",    "The requested action is complete."),
    ABANDONED   (99, 99, "Abandoned",   "The requested action has been abandoned and will never complete.");

    private static final long serialVersionUID = 1L;

    private static final String ENUM_TYPE_GUID  = "7197ea39-334d-403f-a70b-d40231092df7";
    private static final String ENUM_TYPE_NAME  = "ToDoStatus";

    private final int    openTypeOrdinal;

    private final int            ordinal;
    private final String         name;
    private final String         description;


    /**
     * Default constructor for the enumeration.
     *
     * @param ordinal numerical representation of the enumeration
     * @param name default string name of the enumeration
     * @param description default string description of the enumeration
     */
    ToDoStatus(int    ordinal,
               int    openTypeOrdinal,
               String name,
               String description)
    {
        this.ordinal = ordinal;
        this.name = name;
        this.openTypeOrdinal = openTypeOrdinal;
        this.description = description;
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
     * Return the numeric representation of the enumeration.
     *
     * @return int ordinal
     */
    public int getOrdinal() { return ordinal; }


    /**
     * Return the default name of the enumeration.
     *
     * @return String name
     */
    public String getName() { return name; }


    /**
     * Return the default description of the enumeration.
     *
     * @return String description
     */
    public String getDescription() { return description; }


    /**
     * toString() JSON-style
     *
     * @return string description
     */
    @Override
    public String toString()
    {
        return "ToDoStatus : " + name;
    }
}
