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
import static org.odpi.openmetadata.frameworks.openmetadata.mapper.OpenMetadataValidValues.constructValidValueNamespace;
import static org.odpi.openmetadata.frameworks.openmetadata.mapper.OpenMetadataValidValues.constructValidValueQualifiedName;

/**
 * ProcessContainmentType defines the ownership of a process withing a sub process. It is used in a
 * ProcessHierarchy relationship.
 * <ul>
 * <li>OWNED - The parent process owns the child process in the relationship, such that if the parent is removed the child should also be removed.
 * A child can have at most one such parent.</li>
 * <li>USED - The child process is simply used by the parent. A child process can have many such relationships to parents.</li>
 * <li>OTHER - None of the above.</li>
 * </ul>
 */
@JsonAutoDetect(getterVisibility = PUBLIC_ONLY, setterVisibility = PUBLIC_ONLY, fieldVisibility = NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public enum ProcessContainmentType implements OpenMetadataEnum
{
    OWNED ("b05e2d4b-91e3-479f-96cc-6bf32c27ab9f",  0,  "Owned",  "The parent process owns the child process in the relationship, such that if the parent is removed the child should also be removed. A child can have at most one such parent."),
    USED  ("b63ef91c-e496-40e3-85d0-eca4899b9980",  1,  "Used",   "The child process is simply used by the parent. A child process can have many such relationships to parents."),
    OTHER ("5afd9a09-3215-4ed2-b005-add5888de74e", 99, "Other",  "None of the above.");

    private static final String ENUM_TYPE_GUID  = "1bb4b908-7983-4802-a2b5-91b095552ee9";
    private static final String ENUM_TYPE_NAME  = "ProcessContainmentType";

    private static final String ENUM_DESCRIPTION = "The containment relationship between two processes: the parent and one of its children.";
    private static final String ENUM_DESCRIPTION_GUID = "bb3cd2b2-d3c9-449c-b2f7-39db7d217caf";
    private static final String ENUM_DESCRIPTION_WIKI = OpenMetadataWikiPages.MODEL_0215_SOFTWARE_COMPONENTS;
    private final String descriptionGUID;

    private final int            ordinal;
    private final String         name;
    private final String         description;


    /**
     * Default constructor for the enumeration.
     *
     * @param ordinal numerical representation of the enumeration
     * @param descriptionGUID identifier for valid value
     * @param name default string name of the enumeration
     * @param description default string description of the enumeration
     */
    ProcessContainmentType(String  descriptionGUID,
                           int     ordinal,
                           String  name,
                           String  description)
    {
        this.ordinal = ordinal;
        this.name            = name;
        this.descriptionGUID = descriptionGUID;
        this.description     = description;
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
        return false;
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
                                                OpenMetadataProperty.CONTAINMENT_TYPE.name,
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
        return constructValidValueNamespace(ENUM_TYPE_NAME,
                                            OpenMetadataProperty.CONTAINMENT_TYPE.name,
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
        return "ProcessContainmentType{codeName='" + name + '\'' + '}';
    }
}

