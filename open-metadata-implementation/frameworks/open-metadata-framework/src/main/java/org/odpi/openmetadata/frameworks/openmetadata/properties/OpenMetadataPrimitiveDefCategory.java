/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.openmetadata.properties;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

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
public enum OpenMetadataPrimitiveDefCategory
{
    /**
     * java.lang.Object
     */
    OM_PRIMITIVE_TYPE_UNKNOWN   (0,  "object",     "java.lang.Object",      "1c4b21f4-0b67-41a7-a6ed-2af185eb9b3b"),

    /**
     * java.lang.Boolean
     */
    OM_PRIMITIVE_TYPE_BOOLEAN   (1,  "boolean",    "java.lang.Boolean",     "3863f010-611c-41fe-aaae-5d4d427f863b"),

    /**
     * java.lang.Byte
     */
    OM_PRIMITIVE_TYPE_BYTE      (2,  "byte",       "java.lang.Byte",        "6b7d410a-2e8a-4d12-981a-a806449f9bdb"),

    /**
     * java.lang.Character
     */
    OM_PRIMITIVE_TYPE_CHAR      (3,  "char",       "java.lang.Character",   "b0abebe5-cf85-4065-86ad-f3c6360ed9c7"),

    /**
     * java.lang.Short
     */
    OM_PRIMITIVE_TYPE_SHORT     (4,  "short",      "java.lang.Short",       "8e95b966-ab60-46d4-a03f-40c5a1ba6c2a"),

    /**
     * java.lang.Integer
     */
    OM_PRIMITIVE_TYPE_INT       (5,  "int",        "java.lang.Integer",     "7fc49104-fd3a-46c8-b6bf-f16b6074cd35"),

    /**
     * java.lang.Long
     */
    OM_PRIMITIVE_TYPE_LONG      (6,  "long",       "java.lang.Long",        "33a91510-92ee-4825-9f49-facd7a6f9db6"),

    /**
     * java.lang.Float
     */
    OM_PRIMITIVE_TYPE_FLOAT     (7,  "float",      "java.lang.Float",       "52aeb769-37b7-4b30-b949-ddc7dcebcfa2"),

    /**
     * java.lang.Double
     */
    OM_PRIMITIVE_TYPE_DOUBLE    (8,  "double",     "java.lang.Double",      "e13572e8-25c3-4994-acb6-2ea66c95812e"),

    /**
     * java.math.BigInteger
     */
    OM_PRIMITIVE_TYPE_BIGINTEGER(9,  "biginteger", "java.math.BigInteger",  "8aa56e52-1076-4e0d-9b66-3873a1ed7392"),

    /**
     * java.math.BigDecimal
     */
    OM_PRIMITIVE_TYPE_BIGDECIMAL(10, "bigdecimal", "java.math.BigDecimal",  "d5c8ad9f-8fee-4a64-80b3-63ce1e47f6bb"),

    /**
     * java.lang.String
     */
    OM_PRIMITIVE_TYPE_STRING    (11, "string",     "java.lang.String",      "b34a64b9-554a-42b1-8f8a-7d5c2339f9c4"),

    /**
     * java.lang.Long
     */
    OM_PRIMITIVE_TYPE_DATE      (12, "date",       "java.lang.Long",        "1bef35ca-d4f9-48db-87c2-afce4649362d");

    private final int    code;
    private final String name;
    private final String javaClassName;
    private final String guid;


    /**
     * Constructor to set up a single instances of the enum.
     *
     * @param code int code for enum
     * @param name String name of the primitive type
     * @param javaClassName String name of the class that stores the primitive attribute.
     * @param guid unique identifier of the primitive type
     */
    OpenMetadataPrimitiveDefCategory(int   code, String name, String javaClassName, String guid)
    {
        this.code = code;
        this.name = name;
        this.javaClassName = javaClassName;
        this.guid = guid;
    }

    /**
     * Return the numeric code for the primitive type which can be used in optimized data flows.
     *
     * @return int type code
     */
    public int getOrdinal() {
        return code;
    }


    /**
     * Return the name of type which can be used for text-based interchange formats such as JSON or XML.
     *
     * @return String type name
     */
    public String getName()
    {
        return name;
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
    public String getGUID() { return guid; }


    /**
     * toString() JSON-style
     *
     * @return string description
     */
    @Override
    public String toString()
    {
        return "OpenMetadataPrimitiveDefCategory{" +
                "code=" + code +
                ", name='" + name + '\'' +
                ", javaClassName='" + javaClassName + '\'' +
                ", guid='" + guid + '\'' +
                '}';
    }
}
