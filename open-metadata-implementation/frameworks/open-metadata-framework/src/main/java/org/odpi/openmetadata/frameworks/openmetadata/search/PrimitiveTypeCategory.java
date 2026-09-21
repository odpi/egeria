/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.openmetadata.search;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.frameworks.openmetadata.refdata.OpenMetadataRefData;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * This enumeration defines the list of open metadata primitive types.  This includes a code value, a string
 * name for the type (used in self describing structures such as JSON or XML) and the name of the Java Class
 * that supports this type.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public enum PrimitiveTypeCategory implements OpenMetadataRefData
{
    OM_PRIMITIVE_TYPE_UNKNOWN   (0,  "object",     "java.lang.Object",      "1c4b21f4-0b67-41a7-a6ed-2af185eb9b3b", "e47ff0b4-5782-4444-a811-ed8d2d1b6ed4"),
    OM_PRIMITIVE_TYPE_BOOLEAN   (1,  "boolean",    "java.lang.Boolean",     "3863f010-611c-41fe-aaae-5d4d427f863b", "84558a00-e9f1-49cd-a716-f9184e2a1648"),
    OM_PRIMITIVE_TYPE_BYTE      (2,  "byte",       "java.lang.Byte",        "6b7d410a-2e8a-4d12-981a-a806449f9bdb", "40a7474b-f51a-4e67-979e-945473d1462d"),
    OM_PRIMITIVE_TYPE_CHAR      (3,  "char",       "java.lang.Character",   "b0abebe5-cf85-4065-86ad-f3c6360ed9c7", "3f481d11-5f9f-4919-9e63-88afc677f13e"),
    OM_PRIMITIVE_TYPE_SHORT     (4,  "short",      "java.lang.Short",       "8e95b966-ab60-46d4-a03f-40c5a1ba6c2a", "804ad70e-801d-46cf-a6ad-b2f443bcf8e7"),
    OM_PRIMITIVE_TYPE_INT       (5,  "int",        "java.lang.Integer",     "7fc49104-fd3a-46c8-b6bf-f16b6074cd35", "2ac75704-aa26-4f43-adfc-0356edc123a5"),
    OM_PRIMITIVE_TYPE_LONG      (6,  "long",       "java.lang.Long",        "33a91510-92ee-4825-9f49-facd7a6f9db6", "36938a72-1469-443b-a302-d36f6fdb0a07"),
    OM_PRIMITIVE_TYPE_FLOAT     (7,  "float",      "java.lang.Float",       "52aeb769-37b7-4b30-b949-ddc7dcebcfa2", "a8d79a9d-7777-4d30-8fda-3f7f95ef3160"),
    OM_PRIMITIVE_TYPE_DOUBLE    (8,  "double",     "java.lang.Double",      "e13572e8-25c3-4994-acb6-2ea66c95812e", "6c12ad99-a7a0-4bd2-81d5-5a6be71c5a17"),
    OM_PRIMITIVE_TYPE_BIGINTEGER(9,  "biginteger", "java.math.BigInteger",  "8aa56e52-1076-4e0d-9b66-3873a1ed7392", "721a7c66-0c01-43da-94bc-d7ce1755c527"),
    OM_PRIMITIVE_TYPE_BIGDECIMAL(10, "bigdecimal", "java.math.BigDecimal",  "d5c8ad9f-8fee-4a64-80b3-63ce1e47f6bb", "8c2ebbb9-af96-4a19-893f-b3232b2d8914"),
    OM_PRIMITIVE_TYPE_STRING    (11, "string",     "java.lang.String",      "b34a64b9-554a-42b1-8f8a-7d5c2339f9c4", "7710d677-b72a-4288-9bb0-f06812f94950"),
    OM_PRIMITIVE_TYPE_DATE      (12, "date",       "java.lang.Long",        "1bef35ca-d4f9-48db-87c2-afce4649362d", "63df5ed0-c3a1-4cb6-92ed-f26a1522121d");

    private final int    code;
    private final String name;
    private final String javaClassName;
    private final String typeGUID;
    private final String descriptionGUID;

    /**
     * Constructor to set up a single instances of the enum.
     *
     * @param code            int code for enum
     * @param name            String name of the primitive type
     * @param javaClassName   String name of the class that stores the primitive attribute.
     * @param typeGUID            unique identifier of the primitive type
     * @param descriptionGUID unique identifier for the valid value that represents the enum value
     */
    PrimitiveTypeCategory(int    code,
                          String name,
                          String javaClassName,
                          String typeGUID,
                          String descriptionGUID)
    {
        this.code            = code;
        this.name            = name;
        this.javaClassName   = javaClassName;
        this.typeGUID        = typeGUID;
        this.descriptionGUID = descriptionGUID;
    }

    /**
     * Return the numeric code for the primitive type which can be used in optimized data flows.
     *
     * @return int type code
     */
    @Override
    public int getOrdinal() {
        return code;
    }


    /**
     * Return the name of type which can be used for text-based interchange formats such as JSON or XML.
     *
     * @return String type name
     */
    @Override
    public String getDisplayName()
    {
        return name;
    }

    /**
     * Return the default description of the enumeration.
     *
     * @return String description
     */
    @Override
    public String getDescription()
    {
        return javaClassName;
    }


    /**
     * Return the unique identifier for the valid value that represents the enum value.
     *
     * @return guid
     */
    @Override
    public String getDescriptionGUID()
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
     * Return the name of the java class that can be used to store properties of this type.
     *
     * @return String java class name.
     */
    public String getJavaClassName() {
        return javaClassName;
    }


    /**
     * Return the guid for this primitive type.
     *
     * @return String guid
     */
    public String getTypeGUID() { return typeGUID; }


    /**
     * toString() JSON-style
     *
     * @return string description
     */
    @Override
    public String toString()
    {
        return "PrimitiveTypeCategory{" +
            "code=" + code +
            ", name='" + name + '\'' +
            ", javaClassName='" + javaClassName + '\'' +
            '}';

    }
}
