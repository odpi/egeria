/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.connectors.properties.beans;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * SchemaAttributeCategory summarizes the category of a schema attribute based on its cardinality, ordering and uniques of
 * values stored within it.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public enum SchemaAttributeCategory implements Serializable
{
    UNKNOWN   (0, "<Unknown>", "The schema type is unknown."),
    ARRAY     (1, "Array"    , "There may be multiple instances of this attribute.  " +
               "The instances are ordered and the same value may appear in different instances."),
    SET       (2, "Set"      , "There may be multiple instances of this attribute.  " +
               "The instances are unordered and the same value can not appear in more than one instance."),
    BAG       (3, "Bag", "There may be multiple instances of this attribute.  " +
               "The instances are unordered and the same value may appear in different instances."),
    SINGLETON (4, "Singleton", "There is at maximum, only one instance of the attribute.");

    private int      schemaTypeCode;
    private String   schemaTypeName;
    private String   schemaTypeDescription;

    private static final long     serialVersionUID = 1L;

    /**
     * Constructor to set up the instance of this enum.
     *
     * @param schemaTypeCode code number
     * @param schemaTypeName default name
     * @param schemaTypeDescription default description
     */
    SchemaAttributeCategory(int schemaTypeCode, String schemaTypeName, String schemaTypeDescription)
    {
        this.schemaTypeCode = schemaTypeCode;
        this.schemaTypeName = schemaTypeName;
        this.schemaTypeDescription = schemaTypeDescription;
    }


    /**
     * Return the code for this enum used for indexing based on the enum value.
     *
     * @return int code number
     */
    public int getOrdinal()
    {
        return schemaTypeCode;
    }


    /**
     * Return the default name for this enum type.
     *
     * @return String name
     */
    public String getName()
    {
        return schemaTypeName;
    }


    /**
     * Return the default description for this enum.
     *
     * @return String description
     */
    public String getDescription()
    {
        return schemaTypeDescription;
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "BoundedSchemaCategory{" +
                "schemaTypeCode=" + schemaTypeCode +
                ", schemaTypeName='" + schemaTypeName + '\'' +
                ", schemaTypeDescription='" + schemaTypeDescription + '\'' +
                '}';
    }
}
