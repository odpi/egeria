/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adapters.connectors.duckdb.controls;


import org.odpi.openmetadata.frameworks.openmetadata.controls.PlaceholderProperty;
import org.odpi.openmetadata.frameworks.openmetadata.specificationproperties.PlaceholderPropertyType;

import java.util.ArrayList;
import java.util.List;

/**
 * DuckDBPlaceholderProperty provides some standard definitions for placeholder properties used to pass properties
 * to services that use templates for DuckDB.
 */
public enum DuckDBPlaceholderProperty
{
    /**
     * The path to the DuckDB database file being catalogued - or the literal value ":memory:" for an in-memory session.
     */
    DATABASE_PATH ("databasePath",
                   "The path to the DuckDB database file being catalogued - or the literal value \":memory:\" for an in-memory session.",
                   "string",
                   "/data/databases/myDatabase.duckdb"),

    /**
     * The name of the database being catalogued.
     */
    DATABASE_NAME ("databaseName",
                   "The name of the database being catalogued.",
                   "string",
                   "myDatabase"),

    /**
     * The description of the database being catalogued.
     */
    DATABASE_DESCRIPTION ("databaseDescription",
                          "The description of the database being catalogued.",
                          "string",
                          null),

    ;

    public final String name;
    public final String description;
    public final String dataType;
    public final String example;


    /**
     * Create a specific Enum constant.
     *
     * @param name name of the placeholder property
     * @param description description of the placeholder property
     * @param dataType type of value of the placeholder property
     * @param example example of the placeholder property
     */
    DuckDBPlaceholderProperty(String name,
                              String description,
                              String dataType,
                              String example)
    {
        this.name        = name;
        this.description = description;
        this.dataType     = dataType;
        this.example      = example;
    }


    /**
     * Return the name of the placeholder property.
     *
     * @return string name
     */
    public String getName()
    {
        return name;
    }


    /**
     * Return the placeholder to use when building templates.
     *
     * @return placeholder property
     */
    public String getPlaceholder()
    {
        return "~{" + name + "}~";
    }


    /**
     * Return the description of the placeholder property.
     *
     * @return text
     */
    public String getDescription()
    {
        return description;
    }


    /**
     * Return the data type for the placeholder property.
     *
     * @return data type name
     */
    public String getDataType()
    {
        return dataType;
    }


    /**
     * Return an example of the placeholder property to help users understand how to set it up.
     *
     * @return example
     */
    public String getExample()
    {
        return example;
    }


    /**
     * Retrieve all the defined placeholder properties for the DuckDB database template.
     *
     * @return list of placeholder property types
     */
    public static List<PlaceholderPropertyType> getDuckDBDatabasePlaceholderPropertyTypes()
    {
        List<PlaceholderPropertyType> placeholderPropertyTypes = new ArrayList<>();

        placeholderPropertyTypes.add(DuckDBPlaceholderProperty.DATABASE_PATH.getPlaceholderType());
        placeholderPropertyTypes.add(DuckDBPlaceholderProperty.DATABASE_NAME.getPlaceholderType());
        placeholderPropertyTypes.add(DuckDBPlaceholderProperty.DATABASE_DESCRIPTION.getPlaceholderType());
        placeholderPropertyTypes.add(PlaceholderProperty.VERSION_IDENTIFIER.getPlaceholderType());
        placeholderPropertyTypes.add(PlaceholderProperty.SECRETS_STORE.getPlaceholderType());
        placeholderPropertyTypes.add(PlaceholderProperty.SECRETS_COLLECTION_NAME.getPlaceholderType());

        return placeholderPropertyTypes;
    }


    /**
     * Return a summary of this enum to use in a service provider.
     *
     * @return placeholder property type
     */
    public PlaceholderPropertyType getPlaceholderType()
    {
        PlaceholderPropertyType placeholderPropertyType = new PlaceholderPropertyType();

        placeholderPropertyType.setName(name);
        placeholderPropertyType.setDescription(description);
        placeholderPropertyType.setDataType(dataType);
        placeholderPropertyType.setExample(example);
        placeholderPropertyType.setRequired(true);

        return placeholderPropertyType;
    }


    /**
     * Output of this enum class and main value.
     *
     * @return string showing enum value
     */
    @Override
    public String toString()
    {
        return "PlaceholderProperty{ name=" + name + "}";
    }
}
