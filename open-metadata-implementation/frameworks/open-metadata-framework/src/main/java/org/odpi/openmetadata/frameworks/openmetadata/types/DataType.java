/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.openmetadata.types;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.frameworks.openmetadata.enums.OpenMetadataEnum;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;
import static org.odpi.openmetadata.frameworks.openmetadata.mapper.OpenMetadataValidValues.constructValidValueNamespace;
import static org.odpi.openmetadata.frameworks.openmetadata.mapper.OpenMetadataValidValues.constructValidValueQualifiedName;

/**
 * DataType identifies the type for a data item.
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
     * An ordered list of strings.
     */
    ARRAY_STRING(9, "8e3dad3b-9590-4903-b2d0-3e549b8e1cb9", "array<string>", "An ordered list of strings.", false),

    /**
     * An ordered list of integers.
     */
    ARRAY_INT(10, "fed660e8-f43b-459a-8d6e-76be1117a573", "array<int>", "An ordered list of integers.", false),

    /**
     * A map from string value to string value.
     */
    MAP_STRING_STRING(11, "26eb11f0-2bd1-4d25-8d07-a600b8396de7", "map<string,string>", "A map from string value to string value.", false),

    /**
     * A map from string value to boolean value.
     */
    MAP_STRING_BOOLEAN(12, "63a7dea2-e2ca-4882-809c-47108c97b725", "map<string,boolean>", "A map from string value to boolean value.", false),

    /**
     * A map from string value to integer value.
     */
    MAP_STRING_INT(13, "9d6da07a-173e-443f-828e-01b4079e4122", "map<string,int>", "A map from string value to integer value.", false),

    /**
     * A map from string value to long value.
     */
    MAP_STRING_LONG(14, "34e8937b-b4e5-4042-ba0d-4f9f8e6d553c", "map<string,long>", "A map from string value to long value.", false),

    /**
     * A map from string value to double value.
     */
    MAP_STRING_DOUBLE(15, "f1950f13-ad2e-4a38-b933-7e723c80facc", "map<string,double>", "A map from string value to double value.", false),

    /**
     * A map from string value to date value.
     */
    MAP_STRING_DATE(16, "9d09ac67-d3b5-4c5b-b8bf-e9140ae06877", "map<string,date>", "A map from string value to date value.", false),

    /**
     * A map from string value to object value.
     */
    MAP_STRING_OBJECT(17, "9a99ee58-f18f-432c-b2ac-f5fb99a78690", "map<string,object>", "A map from string value to object value.", false),

    /**
     * Short
     */
    SHORT(18, "142d1d09-9caf-415c-95e5-115f27679d26", "short", "Short", false),

    /**
     * A map from string value to an array of strings
     */
    MAP_STRING_ARRAY_STRING(19, "f5fb27f1-06c7-4ea5-98c6-c45a37231b35", "map<string, array<string>>", "A map from string value to an array of strings", false),

    /**
     * A generic data type.
     */
    OBJECT(99, "0441e0c1-96f0-4887-a038-72c7437628a1", "Other", "An unknown data type.", false),


    ;

    private final String         descriptionGUID;
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
     * Return the qualified name for this value.
     *
     * @return string
     */
    public String getQualifiedName()
    {
        return constructValidValueQualifiedName("DataType",
                                                OpenMetadataProperty.DATA_TYPE.name,
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
        return constructValidValueNamespace("DataType",
                                            OpenMetadataProperty.DATA_TYPE.name,
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
        return "DataType{name='" + name + '}';
    }
}
