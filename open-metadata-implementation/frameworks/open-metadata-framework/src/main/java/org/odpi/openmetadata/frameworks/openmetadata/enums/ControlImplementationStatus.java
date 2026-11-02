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
 * ControlImplementationStatus defines how far a control listed in a statement of applicability has been implemented.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public enum ControlImplementationStatus implements OpenMetadataEnum
{
    /**
     * No work has started on implementing the control.
     */
    NOT_STARTED            (0,  "e805a337-0e4c-4b56-b55f-dc0e379710a1",  "Not Started",           "No work has started on implementing the control.", true),

    /**
     * The implementation of the control is planned but has not started.
     */
    PLANNED                (1,  "4d6d0df9-e737-43b7-883f-085e59c7a49b",  "Planned",               "The implementation of the control is planned but has not started.", false),

    /**
     * The control is partially implemented.
     */
    PARTIALLY_IMPLEMENTED  (2,  "fe65e6dc-3489-40f8-8848-402d4892c2e6",  "Partially Implemented", "The control is partially implemented.", false),

    /**
     * The control is fully implemented and its implementation is evidenced.
     */
    IMPLEMENTED            (3,  "f76a684d-beff-4d34-8baf-6d77e59c7179",  "Implemented",           "The control is fully implemented and its implementation is evidenced.", false),

    /**
     * Another implementation status.
     */
    OTHER                  (99, "a41688c9-537a-4baa-ba84-ea5e85fb919a",  "Other",                 "Another implementation status.", false);


    private static final String ENUM_TYPE_GUID  = "88e367c8-62ec-40a4-82d1-597a8896a055";
    private static final String ENUM_TYPE_NAME  = "ControlImplementationStatus";

    private static final String ENUM_DESCRIPTION = "Defines how far a control listed in a statement of applicability has been implemented.";
    private static final String ENUM_DESCRIPTION_GUID = "e4161a4a-3b94-4fe7-bbea-d7f246ec0c18";
    private static final String ENUM_DESCRIPTION_WIKI = OpenMetadataWikiPages.MODEL_0423_SECURITY_DEFINITIONS;

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
    ControlImplementationStatus(int     ordinal,
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
    @Override
    public int getOrdinal() { return ordinal; }


    /**
     * Return the default name of the enumeration.
     *
     * @return String name
     */
    @Override
    public String getDisplayName() { return name; }


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
     * toString() JSON-style
     *
     * @return string description
     */
    @Override
    public String toString()
    {
        return "ControlImplementationStatus : " + name;
    }
}
