/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.openmetadata.enums;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataWikiPages;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;
import static org.odpi.openmetadata.frameworks.openmetadata.mapper.OpenMetadataValidValues.constructValidValueCategory;
import static org.odpi.openmetadata.frameworks.openmetadata.mapper.OpenMetadataValidValues.constructValidValueQualifiedName;

/**
 * A GlossaryTermActivityType defines the type of activity described by a glossary term.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public enum GlossaryTermActivityType implements OpenMetadataEnum
{
    /**
     * Operation - A small, well-defined processing operation.
     */
    OPERATION  ("bbbc23c1-62b0-412a-a77e-6d41ada10c7c",  0,  "Operation","A small, well defined processing operation.", false),

    /**
     * Action - A requested or required change.
     */
    ACTION     ("7785980d-3d52-438d-9ece-09e28be5d09a",  1,  "Action",   "A requested or required change.", false),

    /**
     * Task - A piece of work for a person, organization or engine.
     */
    TASK       ("c202f631-dc9c-4bd7-ab9d-f6eb6c7d53d0",  2,  "Task",     "A piece of work for a person, organization or engine.", false),

    /**
     * Process - A sequence of tasks.
     */
    PROCESS    ("15039274-9069-4da1-a026-a15c7ba17b02",  3,  "Process",  "A sequence of tasks.", false),

    /**
     * Project - An organized activity to achieve a specific goal.
     */
    PROJECT    ("21d0f37e-398d-4418-9eb4-49f2d6ce556f",  4,  "Project",  "An organized activity to achieve a specific goal.", false),

    /**
     * Other - An organized activity to achieve a specific goal.
     */
    OTHER      ("e207c15f-e30e-4585-a483-032f46eb6b16", 99, "Other",    "Another type of activity.", false);

    private static final String ENUM_TYPE_GUID  = "af7e403d-9865-4ebb-8c1a-1fd57b4f4bca";
    private static final String ENUM_TYPE_NAME  = "ActivityType";

    private static final String ENUM_DESCRIPTION = "Different types of activities.";
    private static final String ENUM_DESCRIPTION_GUID = "0b24f61f-5f13-4e8a-9b97-4fc3783b23e4";
    private static final String ENUM_DESCRIPTION_WIKI = OpenMetadataWikiPages.MODEL_0340_DICTIONARY;

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
    GlossaryTermActivityType(String  descriptionGUID,
                             int     ordinal,
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
    @Override
    public int getOrdinal() { return ordinal; }


    /**
     * Return the default name of the enumeration.
     *
     * @return String name
     */
    @Override
    public String getName() { return name; }


    /**
     * Return the default description of the enumeration.
     *
     * @return String description
     */
    @Override
    public String getDescription() { return description; }


    /**
     * Return the unique identifier for the valid value that represents the enum value.
     *
     * @return  guid
     */
    @Override
    public  String getDescriptionGUID()
    {
        return descriptionGUID;
    }


    /**
     * Return whether the enum is the default value or not.
     *
     * @return boolean
     */
    @Override
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
     * Return the qualified name for this value.
     *
     * @return string
     */
    public String getQualifiedName()
    {
        return constructValidValueQualifiedName(ENUM_TYPE_NAME,
                                                OpenMetadataProperty.ACTIVITY_TYPE.name,
                                                null,
                                                name);
    }


    /**
     * Return the category for this value.
     *
     * @return string
     */
    public String getCategory()
    {
        return constructValidValueCategory(ENUM_TYPE_NAME,
                                           OpenMetadataProperty.ACTIVITY_TYPE.name,
                                           null);
    }

    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "GlossaryTermActivityType{name='" + name + '}';
    }
}