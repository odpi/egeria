/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adapters.repositoryservices.postgres.repositoryconnector.controls;


import org.odpi.openmetadata.frameworks.connectors.controls.ConfigurationPropertyType;
import org.odpi.openmetadata.frameworks.openmetadata.types.DataType;

import java.util.ArrayList;
import java.util.List;

/**
 * PostgresConfigurationProperty provides some standard definitions for configuration properties used to
 * pass properties to connectors when they run.  Using standard names for request parameters wherever possible
 * helps to simplify the integration of governance services.
 */
public enum PostgresConfigurationProperty
{
    DATABASE_URL("databaseURL", "Connection string for the database where the schema is located. This should set the current schema to your chosen schema.", DataType.STRING.getName(), "~{postgreSQLDatabaseURL}~"),
    DATABASE_SCHEMA ("databaseSchema", "The name of the database schema used to store the repository contents.", DataType.STRING.getName(), "repository_~{serverName}~"),
    SECRETS_STORE ("secretsStore", "The file name of the secrets store containing the log on credentials to access the database schema.", DataType.STRING.getName(), "~{secretsStore}~"),
    SECRETS_COLLECTION_NAME ("secretsCollectionName", "The name of the secrets collection.", DataType.STRING.getName(), "~{postgreSQLServerCollectionName}~"),
    DEFAULT_AS_OF_TIME ("defaultAsOfTime", "Optional value that changes the default value for 'asOfTime'. The 'asOfTime' parameter is used on queries to control which point in time to take the open metadata from.  The standard default is 'null' which means use the current time. If the 'defaultAsOfTime' option is specified then, by default, queries will use the supplied asOfTime value.  This can be overridden on individual requests.  Creates, updates, deletes continue to be appended to the database.  The mode recent data can be retrieved by setting the asOfTime to the current time on queries.", DataType.DATE.getName(), null),
    REPOSITORY_MODE ("repositoryMode", "Optional value that controls which functions are active in the repository. If it is set to 'readOnly' the repository is switched into read-only mode.  Any other value (or if it is not specified) results in a read-write repository.", DataType.DATE.getName(), "yyyy/MM/dd HH:mm:ss"),
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
    PostgresConfigurationProperty(String name,
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
     * Retrieve all the defined configuration properties
     *
     * @return list
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
        return "MoveCopyFileRequestParameter{ name=" + name + "}";
    }
}
