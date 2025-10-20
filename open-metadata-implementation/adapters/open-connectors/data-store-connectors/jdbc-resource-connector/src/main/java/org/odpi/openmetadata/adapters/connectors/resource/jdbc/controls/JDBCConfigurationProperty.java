/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adapters.connectors.resource.jdbc.controls;


import org.odpi.openmetadata.frameworks.connectors.controls.ConfigurationPropertyType;
import org.odpi.openmetadata.frameworks.openmetadata.types.DataType;

import java.util.ArrayList;
import java.util.List;

/**
 * JDBCConfigurationProperty describes the configuration properties supported by the JDBC Resource connector.
 */
public enum JDBCConfigurationProperty
{
    /**
     * Connection string for the database where the schema is located. This should set the current schema to your chosen schema.
     */
    DATABASE_URL("databaseURL", "Connection string for the database where the schema is located. This should set the current schema to your chosen schema.", DataType.STRING.getName(), "~{postgreSQLDatabaseURL}~"),

    /**
     * Provides a name to use in messages about the database.  If it is not set then the connection URL string is used.
     */
    DATABASE_NAME("databaseName", "Provides a name to use in messages about the database.  If it is not set then the connection URL string is used.", DataType.STRING.getName(), null),

    /**
     * The name of the database schema used to store the data.  This should match the schema set up in the database URL.
     */
    DATABASE_SCHEMA ("databaseSchema", "The name of the database schema used to store the data.  This should match the schema set up in the database URL.", DataType.STRING.getName(), null),

    /**
     * The file name of the secrets store containing the log on credentials to access the database.
     */
    SECRETS_STORE ("secretsStore", "The file name of the secrets store containing the log on credentials to access the database.", DataType.STRING.getName(), "~{secretsStore}~"),

    /**
     * The name of the secrets collection within the secrets store to use for login credentials.
     */
    SECRETS_COLLECTION_NAME ("secretsCollectionName", "The name of the secrets collection within the secrets store to use for login credentials.", DataType.STRING.getName(), "~{postgreSQLServerCollectionName}~"),


    /**
     * An optional configuration property that causes the named class to be loaded and registered as a driver.
     * This property only needs to be defined if the connector is experiencing exceptions related to a missing DriverManager class for
     * the database URL.
     */
    JDBC_DRIVER_MANAGER_CLASS_NAME ("jdbcDriverManagerClassName", "An optional configuration property that causes the named class to be loaded and registered as a driver. This property only needs to be defined if the connector is experiencing exceptions related to a missing DriverManager class for the database URL.", DataType.STRING.getName(), null),

    /**
     * Sets the maximum time in seconds that this data source will wait while attempting to connect to a database.
     * The default value is 0 which means use the system default timeout, if any; otherwise it means no timeout.
     */
    JDBC_CONNECTION_TIMEOUT("jdbcConnectionTimeout", "Sets the maximum time in seconds that this data source will wait while attempting to connect to a database. The default value is 0 which means use the system default timeout, if any; otherwise it means no timeout.", DataType.INT.getName(), "10"),


    ;

    public final String           name;
    public final String           description;
    public final String           dataType;
    public final String           example;


    /**
     * Create a specific Enum constant.
     *
     * @param name name of the request parameter
     * @param description description of the request parameter
     * @param dataType type of value of the request parameter
     * @param example example of the request parameter
     */
    JDBCConfigurationProperty(String name,
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
     * Return the name of the request parameter.
     *
     * @return string name
     */
    public String getName()
    {
        return name;
    }


    /**
     * Return the description of the request parameter.
     *
     * @return text
     */
    public String getDescription()
    {
        return description;
    }


    /**
     * Return the data type for the request parameter.
     *
     * @return data type name
     */
    public String getDataType()
    {
        return dataType;
    }


    /**
     * Return an example of the request parameter to help users understand how to set it up.
     *
     * @return example
     */
    public String getExample()
    {
        return example;
    }


    /**
     * Get recognizedConfigurationProperties for the connector.
     *
     * @return list of property names
     */
    public static List<String> getRecognizedConfigurationProperties()
    {
        List<String> recognizedConfigurationProperties = new ArrayList<>();

        for (JDBCConfigurationProperty configurationProperty : JDBCConfigurationProperty.values())
        {
            recognizedConfigurationProperties.add(configurationProperty.getName());
        }
        return recognizedConfigurationProperties;
    }


    /**
     * Retrieve all the defined configuration properties
     *
     * @return list
     */
    public static List<ConfigurationPropertyType> getConfigurationPropertyTypes()
    {
        List<ConfigurationPropertyType> configurationPropertyTypes = new ArrayList<>();

        for (JDBCConfigurationProperty configurationProperty : JDBCConfigurationProperty.values())
        {
            configurationPropertyTypes.add(configurationProperty.getConfigurationPropertyType());
        }

        return configurationPropertyTypes;
    }



    /**
     * Return a summary of this enum to use in a service provider.
     *
     * @return request parameter type
     */
    public ConfigurationPropertyType getConfigurationPropertyType()
    {
        ConfigurationPropertyType requestParameterType = new ConfigurationPropertyType();

        requestParameterType.setName(name);
        requestParameterType.setDescription(description);
        requestParameterType.setDataType(dataType);
        requestParameterType.setExample(example);

        return requestParameterType;
    }

    /**
     * Output of this enum class and main value.
     *
     * @return string showing enum value
     */
    @Override
    public String toString()
    {
        return "RequestParameter{ name=" + name + "}";
    }
}
