/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.openmetadata.enums;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataWikiPages;

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
public enum ToDoStatus implements OpenMetadataEnum
{
    /**
     * The to do has been documented but no action taken.
     */
    OPEN        (0,  "1bbd26ec-d544-4989-aa9d-ef64651b8de4",  "Open",        "The to do has been documented but no action taken.", true),

    /**
     * The assigned person is working on the action defined in the to do.
     */
    IN_PROGRESS (1,  "99bf6fc9-dd52-4b8b-b971-cba871d5be2b",  "In Progress", "The assigned person is working on the action defined in the to do.", false),

    /**
     * The assigned person is unable to proceed because another action needs to complete first.
     */
    WAITING     (2,  "4de31b2c-d01a-4109-9c20-e7a7621284b6",  "Waiting",     "The assigned person is unable to proceed because another action needs to complete first, or a needed resource is unavailable.", false),

    /**
     * The requested action is complete.
     */
    COMPLETE    (3,  "e8ce8601-4929-460e-9840-0ca74d797074",  "Complete",    "The requested action is complete.", false),

    /**
     * The requested action has been abandoned and will never complete.
     */
    ABANDONED   (99, "1538ada4-cb2d-4f26-8bbc-bb9887289705", "Abandoned",   "The requested action has been abandoned and will never complete.", false);


    private static final String ENUM_TYPE_GUID  = "7197ea39-334d-403f-a70b-d40231092df7";
    private static final String ENUM_TYPE_NAME  = "ToDoStatus";
    private static final String ENUM_DESCRIPTION = "Progress on completing an action (to do).";
    private static final String ENUM_DESCRIPTION_GUID = "ceaecca1-6489-4ab7-b0d3-cc1c69ccfaa5";
    private static final String ENUM_DESCRIPTION_WIKI = OpenMetadataWikiPages.MODEL_0137_ACTIONS_FOR_PEOPLE;

    private final String descriptionGUID;

    private final int            ordinal;
    private final String         name;
    private final String         description;
    private final boolean        isDefault;

    /**
     * Default constructor for the enumeration.
     *
     * @param ordinal numerical representation of the enumeration
     * @param descriptionGUID identifier for valid value
     * @param name default string name of the enumeration
     * @param description default string description of the enumeration
     * @param isDefault is this the default value for the enum?
     */
    ToDoStatus(int     ordinal,
               String  descriptionGUID,
               String  name,
               String  description,
               boolean isDefault)
    {
        this.ordinal = ordinal;
        this.name            = name;
        this.descriptionGUID = descriptionGUID;
        this.description     = description;
        this.isDefault = isDefault;
    }


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
     * Return the unique identifier for the valid value that represents the enum value.
     *
     * @return  guid
     */
    public  String getDescriptionGUID()
    {
        return descriptionGUID;
    }


    /**
     * Return whether the enum is the default value or not.
     *
     * @return boolean
     */
    public boolean isDefault()
    {
        return isDefault;
    }

    /**
     * Return the unique identifier for the open metadata enum type that this enum class represents.
     *
     * @return string guid
     */
    public static String getOpenTypeGUID() { return ENUM_TYPE_GUID; }


    /**
     * Return the unique name for the open metadata enum type that this enum class represents.
     *
     * @return string name
     */
    public static String getOpenTypeName() { return ENUM_TYPE_NAME; }


    /**
     * Return the description for the open metadata enum type that this enum class represents.
     *
     * @return string description
     */
    public static String getOpenTypeDescription()
    {
        return ENUM_DESCRIPTION;
    }


    /**
     * Return the unique identifier for the valid value element for the open metadata enum type that this enum class represents.
     *
     * @return string guid
     */
    public static String getOpenTypeDescriptionGUID()
    {
        return ENUM_DESCRIPTION_GUID;
    }


    /**
     * Return the unique identifier for the valid value element for the open metadata enum type that this enum class represents.
     *
     * @return string guid
     */
    public static String getOpenTypeDescriptionWiki()
    {
        return ENUM_DESCRIPTION_WIKI;
    }


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
