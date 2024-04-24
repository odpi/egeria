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
 * Defines how confidential a data item is.
 */

@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public enum ConfidentialityLevel implements OpenMetadataEnum
{
    /**
      * The data is public information.
      */
    UNCLASSIFIED("472d15fa-5713-40dd-892b-9bf91313957e", 0, "Unclassified", "The data is public information.", false),

    /**
      * The data should not be exposed outside of this organization.
      */
    INTERNAL("e7813b82-8447-43ce-8a67-c941c265257f", 1, "Internal", "The data should not be exposed outside of this organization.", false),

    /**
      * The data should be protected and only shared with people with a need to see it.
      */
    CONFIDENTIAL("b326cfae-1579-4669-b614-e33e5a7e3b18", 2, "Confidential", "The data should be protected and only shared with people with a need to see it.", false),

    /**
      * The data is sensitive and inappropriate use may adversely impact the data subject.
      */
    SENSITIVE("18011377-8dd0-4211-abdf-039da2415f1c", 3, "Sensitive", "The data is sensitive and inappropriate use may adversely impact the data subject.", false),

    /**
      * The data is very valuable and must be restricted to a very small number of people.
      */
    RESTRICTED("b0d939c5-6c06-4bb3-ade3-cc5957182ff4", 4, "Restricted", "The data is very valuable and must be restricted to a very small number of people.", false),

    /**
      * Another confidentially level.
      */
    OTHER("3f052d7e-de9d-4b94-a7d4-01ae0242fab6", 99, "Other", "Another confidentially level.", false),

;
    private static final String ENUM_TYPE_GUID  = "0efe1125-a8c7-452a-a635-47c4466b0cc2";
    private static final String ENUM_TYPE_NAME  = "ConfidentialityLevel";

    private static final String ENUM_DESCRIPTION = "Defines how confidential the data associated with this element is.";
    private static final String ENUM_DESCRIPTION_GUID = "98e92fcb-175c-485b-97f6-75dcd777078b";
    private static final String ENUM_DESCRIPTION_WIKI = OpenMetadataWikiPages.MODEL_0421_GOVERNANCE_CLASSIFICATION;

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
    ConfidentialityLevel(String  descriptionGUID,
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
     * Return the qualified name for this value.
     *
     * @return string
     */
    public String getQualifiedName()
    {
        return constructValidValueQualifiedName(ENUM_TYPE_NAME,
                                                OpenMetadataProperty.CONFIDENTIALITY_LEVEL_IDENTIFIER.name,
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
                                           OpenMetadataProperty.CONFIDENTIALITY_LEVEL_IDENTIFIER.name,
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
        return "ConfidentialityLevel{" + name + "}";
    }
}
