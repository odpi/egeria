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
 * LatestChangeAction identifies the type of a change to an asset and its connected entities.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public enum LatestChangeAction implements OpenMetadataEnum
{
    /**
     * The target element has been created.
     */
    CREATED ("a26c42ff-7a74-45c6-94c0-52e45c425907", 0, "Created", "The target element has been created.", false),

    /**
     * The properties of the target element have been changed.
     */
    UPDATED ("ee5c4749-15cb-436b-a261-499b519ba895", 1,"Updated", "The properties of the target element have been changed.", false),

    /**
     * The target element has been deleted.
     */
    DELETED ("be5428bd-c055-4bf1-91b4-628f51e88c07", 2,"Deleted", "The target element has been deleted.", false),

    /**
     * Another type of action.
     */
    OTHER   ("403498f0-aa2e-4495-9430-72fd10a3988a", 99,"Other",  "Another type of action.", false);

    private static final String ENUM_TYPE_GUID  = "032d844b-868f-4c4a-bc5d-81f0f9704c4d";
    private static final String ENUM_TYPE_NAME  = "LatestChangeAction";
    private static final String ENUM_DESCRIPTION = "Defines the type of change that was made to a repository instance.";
    private static final String ENUM_DESCRIPTION_GUID = "2e93fb5a-7671-4149-8b7a-6a93733606f5";
    private static final String ENUM_DESCRIPTION_WIKI = OpenMetadataWikiPages.MODEL_0011_MANAGING_REFERENCEABLES;

    private final String  descriptionGUID;
    private final int     ordinal;
    private final String  name;
    private final String  description;
    private final boolean isDefault;

    /**
     * Default constructor for the enumeration.
     *
     * @param ordinal numerical representation of the enumeration
     * @param descriptionGUID identifier for valid value
     * @param name default string name of the enumeration
     * @param description default string description of the enumeration
     * @param isDefault is this the default value for the enum?
     */
    LatestChangeAction(String  descriptionGUID,
                       int     ordinal,
                       String  name,
                       String  description,
                       boolean isDefault)
    {
        this.ordinal         = ordinal;
        this.name            = name;
        this.descriptionGUID = descriptionGUID;
        this.description     = description;
        this.isDefault       = isDefault;
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
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "LatestChangeAction{" +
                "ordinal=" + ordinal +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
