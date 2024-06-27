/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adapters.connectors.postgres.controls;


import org.odpi.openmetadata.frameworks.governanceaction.controls.PlaceholderPropertyType;

import java.util.ArrayList;
import java.util.List;

/**
 * PlaceholderProperty provides some standard definitions for placeholder properties used to pass properties
 * to services that use templates.
 */
public enum PostgresPlaceholderProperty
{
    /**
     * The host IP address or domain name.
     */
    HOST_IDENTIFIER ("hostIdentifier", "The host IP address or domain name.", "string", "coconet.com"),

    /**
     * The number of the port to use to connect to a service.
     */
    PORT_NUMBER ("portNumber", "The number of the port to use to connect to the server.", "string", "1234"),

    /**
     * The name of the database being catalogued.
     */
    DATABASE_NAME ("databaseName",
                   "The name of the database being catalogued.",
                   "string",
                   "myDatabase"),

    /**
     * The name of the database being catalogued.
     */
    DATABASE_DESCRIPTION ("databaseDescription",
                          "The description of the database being catalogued.",
                          "string",
                          null),

    /**
     * The userId to store in the userId attribute of the connection.
     */
    DATABASE_USER_ID ("databaseUserId",
                      "The userId to store in the userId attribute of the connection. This is a user that is defined to the database and it is used when connecting to the database.",
                      "string",
                      "myDatabase"),

    /**
     * The password to store in the clearPassword attribute of the connection.
     */
    DATABASE_PASSWORD ("databasePassword",
                       "The password to store in the clearPassword attribute of the connection.  This is the password for the databaseUserId and it is used when connecting to the database.",
                       "string",
                       "myDatabase"),

    /**
     * The name of the server being catalogued.
     */
    SERVER_NAME ("serverName", "The name of the server being catalogued.", "string", "myServer"),

    /**
     * The description of the server being catalogued.
     */
    SERVER_DESCRIPTION ("serverDescription", "The description of the server being catalogued.", "string", null),

    /**
     * The name of the schema being catalogued.
     */
    SCHEMA_NAME ("schemaName", "The name of the database schema being catalogued.", "string", "MyServer.schema"),

    /**
     * The description of the schema being catalogued.
     */
    SCHEMA_DESCRIPTION ("schemaDescription", "The description of the database schema being catalogued.", "string", null),

    /**
     * The name of the database table being catalogued.
     */
    TABLE_NAME ("tableName", "The name of the database table being catalogued.", "string", "myTable"),
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
    PostgresPlaceholderProperty(String name,
                                String description,
                                String dataType,
                                String example)
    {
        this.name        = name;
        this.description = description;
        this.dataType    = dataType;
        this.example     = example;
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
        return "{{" + name + "}}";
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
     * Retrieve all the defined placeholder properties
     *
     * @return list of placeholder property types
     */
    public static List<PlaceholderPropertyType> getPostgresServerPlaceholderPropertyTypes()
    {
        List<PlaceholderPropertyType> placeholderPropertyTypes = new ArrayList<>();

        placeholderPropertyTypes.add(PostgresPlaceholderProperty.HOST_IDENTIFIER.getPlaceholderType());
        placeholderPropertyTypes.add(PostgresPlaceholderProperty.PORT_NUMBER.getPlaceholderType());
        placeholderPropertyTypes.add(PostgresPlaceholderProperty.SERVER_NAME.getPlaceholderType());
        placeholderPropertyTypes.add(PostgresPlaceholderProperty.SERVER_DESCRIPTION.getPlaceholderType());
        placeholderPropertyTypes.add(PostgresPlaceholderProperty.DATABASE_USER_ID.getPlaceholderType());
        placeholderPropertyTypes.add(PostgresPlaceholderProperty.DATABASE_PASSWORD.getPlaceholderType());

        return placeholderPropertyTypes;
    }


    /**
     * Retrieve all the defined placeholder properties
     *
     * @return list of placeholder property types
     */
    public static List<PlaceholderPropertyType> getPostgresDatabasePlaceholderPropertyTypes()
    {
        List<PlaceholderPropertyType> placeholderPropertyTypes = new ArrayList<>();

        placeholderPropertyTypes.add(PostgresPlaceholderProperty.HOST_IDENTIFIER.getPlaceholderType());
        placeholderPropertyTypes.add(PostgresPlaceholderProperty.PORT_NUMBER.getPlaceholderType());
        placeholderPropertyTypes.add(PostgresPlaceholderProperty.SERVER_NAME.getPlaceholderType());
        placeholderPropertyTypes.add(PostgresPlaceholderProperty.DATABASE_NAME.getPlaceholderType());
        placeholderPropertyTypes.add(PostgresPlaceholderProperty.DATABASE_DESCRIPTION.getPlaceholderType());
        placeholderPropertyTypes.add(PostgresPlaceholderProperty.DATABASE_USER_ID.getPlaceholderType());
        placeholderPropertyTypes.add(PostgresPlaceholderProperty.DATABASE_PASSWORD.getPlaceholderType());

        return placeholderPropertyTypes;
    }


    /**
     * Retrieve all the defined placeholder properties
     *
     * @return list of placeholder property types
     */
    public static List<PlaceholderPropertyType> getPostgresSchemaPlaceholderPropertyTypes()
    {
        List<PlaceholderPropertyType> placeholderPropertyTypes = new ArrayList<>();

        placeholderPropertyTypes.add(PostgresPlaceholderProperty.HOST_IDENTIFIER.getPlaceholderType());
        placeholderPropertyTypes.add(PostgresPlaceholderProperty.PORT_NUMBER.getPlaceholderType());
        placeholderPropertyTypes.add(PostgresPlaceholderProperty.SERVER_NAME.getPlaceholderType());
        placeholderPropertyTypes.add(PostgresPlaceholderProperty.DATABASE_NAME.getPlaceholderType());
        placeholderPropertyTypes.add(PostgresPlaceholderProperty.SCHEMA_NAME.getPlaceholderType());
        placeholderPropertyTypes.add(PostgresPlaceholderProperty.SCHEMA_DESCRIPTION.getPlaceholderType());
        placeholderPropertyTypes.add(PostgresPlaceholderProperty.DATABASE_USER_ID.getPlaceholderType());
        placeholderPropertyTypes.add(PostgresPlaceholderProperty.DATABASE_PASSWORD.getPlaceholderType());

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
