/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.governanceaction.properties;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.frameworks.governanceaction.search.ArrayTypePropertyValue;
import org.odpi.openmetadata.frameworks.governanceaction.search.MapTypePropertyValue;
import org.odpi.openmetadata.frameworks.governanceaction.search.StructTypePropertyValue;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * This enum defines the list of open metadata collection types.  These types are generic types that need to
 * be configured with specific primitive types before they can be used as an attribute type.
 * <br><br>
 * The enum includes a code value, a string name for the type (used in self describing structures such as JSON or XML)
 * and the name of the Java Class that supports this type.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public enum OpenMetadataCollectionDefCategory
{
    /**
     * Unknown collection type
     */
    OM_COLLECTION_UNKNOWN (0, "<>",              0, null),

    /**
     * A map from values of one type to another.
     */
    OM_COLLECTION_MAP     (1, "map<{$0}, {$1}>", 2, MapTypePropertyValue.class.getName()),

    /**
     * An array of values of the same type.
     */
    OM_COLLECTION_ARRAY   (2, "array<{$0}>",     1, ArrayTypePropertyValue.class.getName()),

    /**
     * A record with a variety of values.
     */
    OM_COLLECTION_STRUCT  (3, "struct<>",        0, StructTypePropertyValue.class.getName());

    private  final int         code;
    private  final String      name;
    private  final int         argumentCount;
    private  final String      javaClassName;


    /**
     * Constructor to set up a single instances of the enum.
     *
     * @param code code for the enum
     * @param name String name for the enum before it is configured with primitive types
     * @param argumentCount number of arguments needed to configure the collection type
     * @param javaClassName Java class used to manage this type of collection
     */
    OpenMetadataCollectionDefCategory(int   code, String name, int argumentCount, String javaClassName)
    {
        this.code = code;
        this.name = name;
        this.argumentCount = argumentCount;
        this.javaClassName = javaClassName;
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
     * Return the name of type that can be used for text-based interchange formats such as JSON or XML.
     *
     * @return String type name
     */
    public String getName()
    {
        return name;
    }


    /**
     * Return the number of arguments for this collection type.
     *
     * @return int number of elements
     */
    public int getArgumentCount() { return argumentCount; }


    /**
     * Return the name of the java class that can be used to store properties of this type.
     *
     * @return String java class name.
     */
    public String getJavaClassName() {
        return javaClassName;
    }


    /**
     * toString() JSON-style
     *
     * @return string description
     */
    @Override
    public String toString()
    {
        return "OpenMetadataCollectionDefCategory{" +
                "code=" + code +
                ", name='" + name + '\'' +
                ", argumentCount=" + argumentCount +
                ", javaClassName='" + javaClassName + '\'' +
                '}';
    }
}
