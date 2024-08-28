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
 * DataType identifies the primitive type for a data item.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public enum DataType implements OpenMetadataEnum
{
    /**
     * String
     */
    STRING(0, "34684e01-d719-4a90-a4bb-1b4d4fe43722", "string", "String", true),

    /**
     * Integer
     */
    INT(1, "7a9dfeca-cadd-44ae-8b25-e5c49f124528", "int", "Integer", false),

    /**
     * Long
     */
    LONG(2, "12a9bac1-f3bc-49a4-968a-997053a7ebf7", "long", "Long", false),

    /**
     * Date
     */
    DATE(3, "7e1c7d05-3c17-423b-a66c-7bab8fd67813", "date", "Date", false),

    /**
     * Boolean
     */
    BOOLEAN(4, "23e5e53d-adf5-4394-926f-00f1c220b1c1", "boolean", "Boolean", false),

    /**
     * Character
     */
    CHAR(5, "cff2d7f7-a407-4001-a365-7f351860e5e7", "char", "Character", false),

    /**
     * Byte
     */
    BYTE(6, "501bd09a-f611-4df1-ace6-fe165ac6388d", "byte", "Byte", false),

    /**
     * Float
     */
    FLOAT(7, "25409e3d-e037-4333-a973-8dc9306a61c0", "float", "Float", false),

    /**
     * Double
     */
    DOUBLE(8, "2a6d2399-1aa3-4888-ad45-5042d1734c88", "double", "Double", false),

    /**
     * Big Integer
     */
    BIGINTEGER(8, "742e07ae-ec43-4c56-90c2-af75021d5854", "biginteger", "Big Integer", false),

    /**
     * Big Decimal
     */
    BIGDECIMAL(8, "1ea84469-27c0-4b70-81ba-18f4099f4a6f", "bigdecimal", "Big Decimal", false),


    /**
     * An unknown data type.
     */
    OBJECT(99, "0441e0c1-96f0-4887-a038-72c7437628a1", "Other", "An unknown data type.", false);

    private static final String ENUM_TYPE_GUID  = "ae846797-d88a-4421-ad9a-318bf7c1fe6f";
    private static final String ENUM_TYPE_NAME  = "ConfidenceLevel";

    private static final String ENUM_DESCRIPTION = "Defines the level of confidence to place in the accuracy of a data item.";
    private static final String ENUM_DESCRIPTION_GUID = "1f7a3730-8471-496b-a6e5-21eac7bb3b98";
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
    DataType(int     ordinal,
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
     */@Override
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
                                                OpenMetadataProperty.CONFIDENCE_LEVEL_IDENTIFIER.name,
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
                                           OpenMetadataProperty.CONFIDENCE_LEVEL_IDENTIFIER.name,
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
        return "ConfidenceLevel{name='" + name + '}';
    }
}
