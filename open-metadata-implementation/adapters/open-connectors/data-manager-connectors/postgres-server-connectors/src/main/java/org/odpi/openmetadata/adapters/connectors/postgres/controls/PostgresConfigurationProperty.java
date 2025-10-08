/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adapters.connectors.postgres.controls;

import org.odpi.openmetadata.frameworks.connectors.controls.ConfigurationPropertyType;
import org.odpi.openmetadata.frameworks.openmetadata.controls.PlaceholderProperty;
import org.odpi.openmetadata.frameworks.openmetadata.types.DataType;

import java.util.ArrayList;
import java.util.List;

/**
 * PostgresConfigurationProperty provides definitions for the configuration properties used with the postgres connectors.
 */
public enum PostgresConfigurationProperty
{
    /**
     * The host IP address or domain name of the database server.
     */
    HOST_IDENTIFIER (PlaceholderProperty.HOST_IDENTIFIER.getName(),
                     PlaceholderProperty.HOST_IDENTIFIER.getDescription(),
                     PlaceholderProperty.HOST_IDENTIFIER.getDataType(),
                     PlaceholderProperty.HOST_IDENTIFIER.getExample(),
                     true),

    /**
     * The number of the port to use to connect to the database server.
     */
    PORT_NUMBER (PlaceholderProperty.PORT_NUMBER.getName(),
                 PlaceholderProperty.PORT_NUMBER.getDescription(),
                 PlaceholderProperty.PORT_NUMBER.getDataType(),
                 PlaceholderProperty.PORT_NUMBER.getExample(),
                 true),

    /**
     * The name of the database server being catalogued.
     */
    SERVER_NAME (PlaceholderProperty.SERVER_NAME.getName(),
                 PlaceholderProperty.SERVER_NAME.getDescription(),
                 PlaceholderProperty.SERVER_NAME.getDataType(),
                 PlaceholderProperty.SERVER_NAME.getExample(),
                 true),

    /**
     * The name of the database being catalogued.
     */
    DATABASE_NAME (PostgresPlaceholderProperty.DATABASE_NAME.getName(),
                   PostgresPlaceholderProperty.DATABASE_NAME.getDescription(),
                   PostgresPlaceholderProperty.DATABASE_NAME.getDataType(),
                   PostgresPlaceholderProperty.DATABASE_NAME.getExample(),
                   true),

    /**
     * The userId to store in the userId attribute of the connection.
     */
    DATABASE_USER_ID (PostgresPlaceholderProperty.DATABASE_USER_ID.getName(),
                      PostgresPlaceholderProperty.DATABASE_USER_ID.getDescription(),
                      PostgresPlaceholderProperty.DATABASE_USER_ID.getDataType(),
                      PostgresPlaceholderProperty.DATABASE_USER_ID.getExample(),
                      true),

    /**
     * The password to store in the clearPassword attribute of the connection.
     */
    DATABASE_PASSWORD (PostgresPlaceholderProperty.DATABASE_PASSWORD.getName(),
                       PostgresPlaceholderProperty.DATABASE_PASSWORD.getDescription(),
                       PostgresPlaceholderProperty.DATABASE_PASSWORD.getDataType(),
                       PostgresPlaceholderProperty.DATABASE_PASSWORD.getExample(),
                       true),

    /**
     * The name of the database schema being catalogued.
     */
    SCHEMA_NAME (PostgresPlaceholderProperty.SCHEMA_NAME.getName(),
                 PostgresPlaceholderProperty.SCHEMA_NAME.getDescription(),
                 PostgresPlaceholderProperty.SCHEMA_NAME.getDataType(),
                 PostgresPlaceholderProperty.SCHEMA_NAME.getExample(),
                 true),

    /**
     * The description of the database schema being catalogued.
     */
    SCHEMA_DESCRIPTION (PostgresPlaceholderProperty.SCHEMA_DESCRIPTION.getName(),
                        PostgresPlaceholderProperty.SCHEMA_DESCRIPTION.getDescription(),
                        PostgresPlaceholderProperty.SCHEMA_DESCRIPTION.getDataType(),
                        PostgresPlaceholderProperty.SCHEMA_DESCRIPTION.getExample(),
                        true),

    /**
     * The name of the database table being catalogued.
     */
    TABLE_NAME (PostgresPlaceholderProperty.TABLE_NAME.getName(),
                PostgresPlaceholderProperty.TABLE_NAME.getDescription(),
                PostgresPlaceholderProperty.TABLE_NAME.getDataType(),
                PostgresPlaceholderProperty.TABLE_NAME.getExample(),
                true),


    /**
     * Provides a list of database names that should not be catalogued.
     */
    TABLE_DESCRIPTION ("tableDescription",
                       "Provides n optional description used when defining the table schema in the database.",
                       DataType.STRING.getName(),
                       "This table is used to store selected open metadata values.",
                       false),

    /**
     * Provides a list of database names that should not be catalogued.
     */
    EXCLUDE_DATABASE_LIST ("excludeDatabaseList",
                        "Provides a list of database names that should not be catalogued.",
                        "array<string>",
                        "dbBoring,dbNotUsed",
                        false),


    /**
     * Provides a list of databases that should be catalogued.  If not set, or null, or *, all databases except the
     * excluded databases are catalogued.
     */
    INCLUDE_DATABASE_LIST ("includeDatabaseList",
                        "Provides a list of databases that should be catalogued.  " +
                                "If not set, or null, or *, all databases except the excluded databases are catalogued.",
                        "array<string>",
                        "*",
                        false),

    /**
     * Unique identifier of the integration connector that is able to catalog the contents of a UC catalog.
     */
    FRIENDSHIP_GUID ("PostgreSQLFriendshipGUID",
                     "Unique identifier of the integration connector that is able to catalog the contents of a PostgreSQL Database.",
                     "string",
                     "48886e79-a822-45a5-ab37-b5cefade9d8a",
                     false),

    ;

    public final String           name;
    public final String           description;
    public final String           dataType;
    public final String           example;
    public final boolean          isPlaceholder;


    /**
     * Create a specific Enum constant.
     *
     * @param name name of the request parameter
     * @param description description of the request parameter
     * @param dataType type of value of the request parameter
     * @param example example of the request parameter
     * @param isPlaceholder is this also used as a placeholder property?
     */
    PostgresConfigurationProperty(String  name,
                                  String  description,
                                  String  dataType,
                                  String  example,
                                  boolean isPlaceholder)
    {
        this.name          = name;
        this.description   = description;
        this.dataType      = dataType;
        this.example       = example;
        this.isPlaceholder = isPlaceholder;
    }


    /**
     * Return the name of the request parameter.
     *
     * @return string name
     */
    public String getName()
    {
        return name;
    }


    /**
     * Return the description of the configuration property.
     *
     * @return text
     */
    public String getDescription()
    {
        return description;
    }


    /**
     * Return the data type for the configuration property.
     *
     * @return data type name
     */
    public String getDataType()
    {
        return dataType;
    }


    /**
     * Return an example of the configuration property to help users understand how to set it up.
     *
     * @return example
     */
    public String getExample()
    {
        return example;
    }


    /**
     * Return whether this value is also used as a placeholder property.
     *
     * @return boolean
     */
    public boolean isPlaceholder()
    {
        return isPlaceholder;
    }


    /**
     * Get recognizedConfigurationProperties for the PostgreSQLServer Integration connector.
     *
     * @return list of property names
     */
    public static List<String> getPostgresServerIntegrationConnectorNames()
    {
        List<String> recognizedConfigurationProperties = new ArrayList<>();

        recognizedConfigurationProperties.add(PostgresConfigurationProperty.EXCLUDE_DATABASE_LIST.getName());
        recognizedConfigurationProperties.add(PostgresConfigurationProperty.INCLUDE_DATABASE_LIST.getName());
        recognizedConfigurationProperties.add(PostgresConfigurationProperty.HOST_IDENTIFIER.getName());
        recognizedConfigurationProperties.add(PostgresConfigurationProperty.PORT_NUMBER.getName());
        recognizedConfigurationProperties.add(PostgresConfigurationProperty.SERVER_NAME.getName());
        recognizedConfigurationProperties.add(PostgresConfigurationProperty.DATABASE_USER_ID.getName());
        recognizedConfigurationProperties.add(PostgresConfigurationProperty.DATABASE_PASSWORD.getName());
        recognizedConfigurationProperties.add(PostgresConfigurationProperty.FRIENDSHIP_GUID.getName());

        return recognizedConfigurationProperties;
    }


    /**
     * Retrieve the defined configuration properties for the
     *
     * @return list of configuration property types
     */
    public static List<ConfigurationPropertyType> getPostgresServerConfigurationPropertyTypes()
    {
        List<ConfigurationPropertyType> configurationPropertyTypes = new ArrayList<>();

        configurationPropertyTypes.add(PostgresConfigurationProperty.EXCLUDE_DATABASE_LIST.getConfigurationPropertyType());
        configurationPropertyTypes.add(PostgresConfigurationProperty.INCLUDE_DATABASE_LIST.getConfigurationPropertyType());
        configurationPropertyTypes.add(PostgresConfigurationProperty.HOST_IDENTIFIER.getConfigurationPropertyType());
        configurationPropertyTypes.add(PostgresConfigurationProperty.PORT_NUMBER.getConfigurationPropertyType());
        configurationPropertyTypes.add(PostgresConfigurationProperty.SERVER_NAME.getConfigurationPropertyType());
        configurationPropertyTypes.add(PostgresConfigurationProperty.DATABASE_USER_ID.getConfigurationPropertyType());
        configurationPropertyTypes.add(PostgresConfigurationProperty.DATABASE_PASSWORD.getConfigurationPropertyType());
        configurationPropertyTypes.add(PostgresConfigurationProperty.FRIENDSHIP_GUID.getConfigurationPropertyType());

        return configurationPropertyTypes;
    }



    /**
     * Get recognizedConfigurationProperties for the PostgreSQL tabular data source resource connector.
     *
     * @return list of property names
     */
    public static List<String> getPostgresTabularDataSourceConnectorNames()
    {
        List<String> recognizedConfigurationProperties = new ArrayList<>();

        recognizedConfigurationProperties.add(PostgresConfigurationProperty.SCHEMA_NAME.getName());
        recognizedConfigurationProperties.add(PostgresConfigurationProperty.SCHEMA_DESCRIPTION.getName());
        recognizedConfigurationProperties.add(PostgresConfigurationProperty.TABLE_NAME.getName());
        recognizedConfigurationProperties.add(PostgresConfigurationProperty.TABLE_DESCRIPTION.getName());

        return recognizedConfigurationProperties;
    }


    /**
     * Retrieve the defined configuration properties for the PostgreSQL tabular data source connector
     *
     * @return list of configuration property types
     */
    public static List<ConfigurationPropertyType> getPostgresTabularDataSourceConfigurationPropertyTypes()
    {
        List<ConfigurationPropertyType> configurationPropertyTypes = new ArrayList<>();

        configurationPropertyTypes.add(PostgresConfigurationProperty.SCHEMA_NAME.getConfigurationPropertyType());
        configurationPropertyTypes.add(PostgresConfigurationProperty.SCHEMA_DESCRIPTION.getConfigurationPropertyType());
        configurationPropertyTypes.add(PostgresConfigurationProperty.TABLE_NAME.getConfigurationPropertyType());
        configurationPropertyTypes.add(PostgresConfigurationProperty.TABLE_DESCRIPTION.getConfigurationPropertyType());

        return configurationPropertyTypes;
    }


    /**
     * Retrieve all the defined configuration properties
     *
     * @return list of configuration property types
     */
    public static List<ConfigurationPropertyType> getConfigurationPropertyTypes()
    {
        List<ConfigurationPropertyType> configurationPropertyTypes = new ArrayList<>();

        for (PostgresConfigurationProperty configurationProperty : PostgresConfigurationProperty.values())
        {
            configurationPropertyTypes.add(configurationProperty.getConfigurationPropertyType());
        }

        return configurationPropertyTypes;
    }


    /**
     * Return a summary of this enum to use in a connector provider.
     *
     * @return request parameter type
     */
    public ConfigurationPropertyType getConfigurationPropertyType()
    {
        ConfigurationPropertyType configurationPropertyType = new ConfigurationPropertyType();

        configurationPropertyType.setName(name);
        configurationPropertyType.setDescription(description);
        configurationPropertyType.setDataType(dataType);
        configurationPropertyType.setExample(example);
        configurationPropertyType.setRequired(isPlaceholder);

        return configurationPropertyType;
    }

    /**
     * Output of this enum class and main value.
     *
     * @return string showing enum value
     */
    @Override
    public String toString()
    {
        return "ConfigurationProperty{ name=" + name + "}";
    }
}
