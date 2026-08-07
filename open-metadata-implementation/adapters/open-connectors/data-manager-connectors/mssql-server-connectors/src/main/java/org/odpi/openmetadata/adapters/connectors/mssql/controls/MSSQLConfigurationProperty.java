/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adapters.connectors.mssql.controls;

import org.odpi.openmetadata.frameworks.openmetadata.specificationproperties.ConfigurationPropertyType;
import org.odpi.openmetadata.frameworks.openmetadata.controls.PlaceholderProperty;

import java.util.ArrayList;
import java.util.List;

/**
 * MSSQLConfigurationProperty provides definitions for the configuration properties used with the mssql connectors.
 */
public enum MSSQLConfigurationProperty
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
    DATABASE_NAME (MSSQLPlaceholderProperty.DATABASE_NAME.getName(),
                   MSSQLPlaceholderProperty.DATABASE_NAME.getDescription(),
                   MSSQLPlaceholderProperty.DATABASE_NAME.getDataType(),
                   MSSQLPlaceholderProperty.DATABASE_NAME.getExample(),
                   true),

    /**
     * The userId to store in the userId attribute of the connection.
     */
    DATABASE_USER_ID (MSSQLPlaceholderProperty.DATABASE_USER_ID.getName(),
                      MSSQLPlaceholderProperty.DATABASE_USER_ID.getDescription(),
                      MSSQLPlaceholderProperty.DATABASE_USER_ID.getDataType(),
                      MSSQLPlaceholderProperty.DATABASE_USER_ID.getExample(),
                      true),

    /**
     * The password to store in the clearPassword attribute of the connection.
     */
    DATABASE_PASSWORD (MSSQLPlaceholderProperty.DATABASE_PASSWORD.getName(),
                       MSSQLPlaceholderProperty.DATABASE_PASSWORD.getDescription(),
                       MSSQLPlaceholderProperty.DATABASE_PASSWORD.getDataType(),
                       MSSQLPlaceholderProperty.DATABASE_PASSWORD.getExample(),
                       true),

    /**
     * The name of the database schema being catalogued.
     */
    SCHEMA_NAME (PlaceholderProperty.SCHEMA_NAME.getName(),
                 PlaceholderProperty.SCHEMA_NAME.getDescription(),
                 PlaceholderProperty.SCHEMA_NAME.getDataType(),
                 PlaceholderProperty.SCHEMA_NAME.getExample(),
                 true),

    /**
     * The description of the database schema being catalogued.
     */
    SCHEMA_DESCRIPTION(PlaceholderProperty.DESCRIPTION.getName(),
                       PlaceholderProperty.DESCRIPTION.getDescription(),
                       PlaceholderProperty.DESCRIPTION.getDataType(),
                       PlaceholderProperty.DESCRIPTION.getExample(),
                       true),

    /**
     * The name of the database table being catalogued.
     */
    TABLE_NAME (PlaceholderProperty.TABLE_NAME.getName(),
                PlaceholderProperty.TABLE_NAME.getDescription(),
                PlaceholderProperty.TABLE_NAME.getDataType(),
                PlaceholderProperty.TABLE_NAME.getExample(),
                true),


    /**
     * The description of the database table being catalogued.
     */
    TABLE_DESCRIPTION (PlaceholderProperty.TABLE_DESCRIPTION.getName(),
                       PlaceholderProperty.TABLE_DESCRIPTION.getDescription(),
                       PlaceholderProperty.TABLE_DESCRIPTION.getDataType(),
                       PlaceholderProperty.TABLE_DESCRIPTION.getExample(),
                       true),


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
     * Unique identifier of the integration connector that is able to catalog the contents of a Microsoft SQL Server database.
     */
    FRIENDSHIP_GUID ("MSSQLFriendshipGUID",
                     "Unique identifier of the integration connector that is able to catalog the contents of a Microsoft SQL Server Database.",
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
    MSSQLConfigurationProperty(String  name,
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
     * Get recognizedConfigurationProperties for the Microsoft SQL Server Integration connector.
     *
     * @return list of property names
     */
    public static List<String> getMSSQLServerIntegrationConnectorNames()
    {
        List<String> recognizedConfigurationProperties = new ArrayList<>();

        recognizedConfigurationProperties.add(MSSQLConfigurationProperty.EXCLUDE_DATABASE_LIST.getName());
        recognizedConfigurationProperties.add(MSSQLConfigurationProperty.INCLUDE_DATABASE_LIST.getName());
        recognizedConfigurationProperties.add(MSSQLConfigurationProperty.HOST_IDENTIFIER.getName());
        recognizedConfigurationProperties.add(MSSQLConfigurationProperty.PORT_NUMBER.getName());
        recognizedConfigurationProperties.add(MSSQLConfigurationProperty.SERVER_NAME.getName());
        recognizedConfigurationProperties.add(MSSQLConfigurationProperty.DATABASE_USER_ID.getName());
        recognizedConfigurationProperties.add(MSSQLConfigurationProperty.DATABASE_PASSWORD.getName());
        recognizedConfigurationProperties.add(MSSQLConfigurationProperty.FRIENDSHIP_GUID.getName());

        return recognizedConfigurationProperties;
    }


    /**
     * Retrieve the defined configuration properties for the Microsoft SQL Server integration connector.
     *
     * @return list of configuration property types
     */
    public static List<ConfigurationPropertyType> getMSSQLServerConfigurationPropertyTypes()
    {
        List<ConfigurationPropertyType> configurationPropertyTypes = new ArrayList<>();

        configurationPropertyTypes.add(MSSQLConfigurationProperty.EXCLUDE_DATABASE_LIST.getConfigurationPropertyType());
        configurationPropertyTypes.add(MSSQLConfigurationProperty.INCLUDE_DATABASE_LIST.getConfigurationPropertyType());
        configurationPropertyTypes.add(MSSQLConfigurationProperty.HOST_IDENTIFIER.getConfigurationPropertyType());
        configurationPropertyTypes.add(MSSQLConfigurationProperty.PORT_NUMBER.getConfigurationPropertyType());
        configurationPropertyTypes.add(MSSQLConfigurationProperty.SERVER_NAME.getConfigurationPropertyType());
        configurationPropertyTypes.add(MSSQLConfigurationProperty.DATABASE_USER_ID.getConfigurationPropertyType());
        configurationPropertyTypes.add(MSSQLConfigurationProperty.DATABASE_PASSWORD.getConfigurationPropertyType());
        configurationPropertyTypes.add(MSSQLConfigurationProperty.FRIENDSHIP_GUID.getConfigurationPropertyType());

        return configurationPropertyTypes;
    }



    /**
     * Get recognizedConfigurationProperties for the Microsoft SQL Server tabular data source resource connector.
     *
     * @return list of property names
     */
    public static List<String> getMSSQLTabularDataSourceConfigPropertyNames()
    {
        List<String> recognizedConfigurationProperties = new ArrayList<>();

        recognizedConfigurationProperties.add(MSSQLConfigurationProperty.SCHEMA_NAME.getName());
        recognizedConfigurationProperties.add(MSSQLConfigurationProperty.SCHEMA_DESCRIPTION.getName());
        recognizedConfigurationProperties.add(MSSQLConfigurationProperty.TABLE_NAME.getName());
        recognizedConfigurationProperties.add(MSSQLConfigurationProperty.TABLE_DESCRIPTION.getName());

        return recognizedConfigurationProperties;
    }


    /**
     * Retrieve the defined configuration properties for the Microsoft SQL Server tabular data source connector
     *
     * @return list of configuration property types
     */
    public static List<ConfigurationPropertyType> getMSSQLTabularDataSourceConfigurationPropertyTypes()
    {
        List<ConfigurationPropertyType> configurationPropertyTypes = new ArrayList<>();

        configurationPropertyTypes.add(MSSQLConfigurationProperty.SCHEMA_NAME.getConfigurationPropertyType());
        configurationPropertyTypes.add(MSSQLConfigurationProperty.SCHEMA_DESCRIPTION.getConfigurationPropertyType());
        configurationPropertyTypes.add(MSSQLConfigurationProperty.TABLE_NAME.getConfigurationPropertyType());
        configurationPropertyTypes.add(MSSQLConfigurationProperty.TABLE_DESCRIPTION.getConfigurationPropertyType());

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

        for (MSSQLConfigurationProperty configurationProperty : MSSQLConfigurationProperty.values())
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
