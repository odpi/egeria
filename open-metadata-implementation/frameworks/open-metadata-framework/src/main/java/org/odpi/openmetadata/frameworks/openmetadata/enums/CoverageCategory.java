/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworks.openmetadata.enums;

import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataWikiPages;

/**
 * Used to describe how a collection of data values for an attribute cover the domain of the possible values to the linked attribute.
 */
public enum CoverageCategory implements OpenMetadataEnum
{
    UNKNOWN("c7d696a0-0172-43c8-832f-ed2ddb8179dd", 0, "Unknown", "The attribute's coverage category is unknown - this is the default.", true),
    UNIQUE_IDENTIFIER("0c9f3cdf-46c5-4371-a91d-62e18be07a8a", 1, "UniqueIdentifier", "The attribute uniquely identifies the concept bead.", false),

    IDENTIFIER("6355fee3-e42c-4e8c-b07b-7869c2d0d0bf", 2, "Identifier", "The attribute is a good indicator of the identity of the concept bead but not guaranteed to be unique.", false),

    CORE_DETAIL("dd3bc662-d093-4950-aea9-c2366399e87b", 3, "CoreDetail", "The attribute provides information that is typically required by all of the consumers of the concept bead.", false),

    EXTENDED_DETAIL("26c116d1-fe89-4743-bfd9-024753af9c56", 4, "ExtendedDetail", "The attribute contains supplementary information that is of interest to specific consumers of the concept bead.", false),

    ;

    private static final String ENUM_TYPE_GUID  = "2c0ac237-e02e-431a-89fd-3107d94d4007";
    private static final String ENUM_TYPE_NAME  = "CoverageCategory";

    private static final String ENUM_DESCRIPTION = "Used to describe how a collection of data values for an attribute cover the domain of the possible values to the linked attribute.";
    private static final String ENUM_DESCRIPTION_GUID = "e9dafc67-8f64-4df6-99bf-c869e40a8223";
    private static final String ENUM_DESCRIPTION_WIKI = OpenMetadataWikiPages.MODEL_0505_SCHEMA_ATTRIBUTES;

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
    CoverageCategory(String  descriptionGUID,
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
        return "CoverageCategory{keyPatternName='" + name + '}';
    }
}
