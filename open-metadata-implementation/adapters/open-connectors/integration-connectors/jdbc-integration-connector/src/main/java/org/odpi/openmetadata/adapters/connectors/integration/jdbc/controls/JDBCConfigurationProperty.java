/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adapters.connectors.integration.jdbc.controls;

import org.odpi.openmetadata.frameworks.connectors.properties.beans.ConfigurationPropertyType;

import java.util.ArrayList;
import java.util.List;

/**
 * JDBCConfigurationProperty provides definitions for the configuration properties used with the JDBC Integration Connector.
 */
public enum JDBCConfigurationProperty
{
    /**
     * An optional property used to provide a display name to a new database asset created by this connector.
     */
    CATALOG ("catalog",
             "An optional property used to provide a display name to a new database asset created by this connector.",
             "string",
             "MyDatabase",
             false),

    /**
     * Provide a comma-separated list of schema names that should be catalogued.  Other schemas will be ignored.
     */
    INCLUDE_SCHEMA_NAMES ("includeSchemaNames",
                 "Provide a comma-separated list of schema names that should be catalogued.  Other schemas will be ignored.",
                 "array<string>",
                 "schema1,schema2",
                 false),

    /**
     * Provide a comma-separated list of schema names that should not be catalogued.  Only schemas not in this list will be catalogued.
     */
    EXCLUDE_SCHEMA_NAMES ("excludeSchemaNames",
                          "Provide a comma-separated list of schema names that should not be catalogued.  Only schemas not in this list will be catalogued.",
                          "array<string>",
                          "schema1,schema2",
                          false),

    /**
     * Provide a comma-separated list of table names that should be catalogued.  Other tables will be ignored.
     */
    INCLUDE_TABLE_NAMES ("includeTableNames",
                          "Provide a comma-separated list of table names that should be catalogued.  Other tables will be ignored.",
                          "array<string>",
                          "table1,table2",
                          false),

    /**
     * Provide a comma-separated list of table names that should not be catalogued.  Only tables not in this list will be catalogued.
     */
    EXCLUDE_TABLE_NAMES ("excludeTableNames",
                          "Provide a comma-separated list of table names that should not be catalogued.  Only tables not in this list will be catalogued.",
                          "array<string>",
                          "schema1,schema2",
                          false),


    /**
     * Provide a comma-separated list of view names that should be catalogued.  Other views will be ignored.
     */
    INCLUDE_VIEW_NAMES ("includeViewNames",
                         "Provide a comma-separated list of view names that should be catalogued.  Other views will be ignored.",
                         "array<string>",
                         "table1,table2",
                         false),

    /**
     * Provide a comma-separated list of view names that should not be catalogued.  Only views not in this list will be catalogued.
     */
    EXCLUDE_VIEW_NAMES ("excludeViewNames",
                         "Provide a comma-separated list of view names that should not be catalogued.  Only views not in this list will be catalogued.",
                         "array<string>",
                         "schema1,schema2",
                         false),

    /**
     * Provide a comma-separated list of column names that should be catalogued.  Other columns will be ignored.
     */
    INCLUDE_COLUMN_NAMES ("includeColumnNames",
                        "Provide a comma-separated list of column names that should be catalogued.  Other columns will be ignored.",
                        "array<string>",
                        "table1,table2",
                        false),

    /**
     * Provide a comma-separated list of column names that should not be catalogued.  Only columns not in this list will be catalogued.
     */
    EXCLUDE_COLUMN_NAMES ("excludeColumnNames",
                        "Provide a comma-separated list of column names that should not be catalogued.  Only columns not in this list will be catalogued.",
                        "array<string>",
                        "schema1,schema2",
                        false),

    ;

    public final String  name;
    public final String  description;
    public final String  dataType;
    public final String  example;
    public final boolean isRequired;


    /**
     * Create a specific Enum constant.
     *
     * @param name name of the request parameter
     * @param description description of the request parameter
     * @param dataType type of value of the request parameter
     * @param example example of the request parameter
     * @param isRequired is this also used as a placeholder property?
     */
    JDBCConfigurationProperty(String  name,
                              String  description,
                              String  dataType,
                              String  example,
                              boolean isRequired)
    {
        this.name          = name;
        this.description   = description;
        this.dataType      = dataType;
        this.example    = example;
        this.isRequired = isRequired;
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
     * Return whether this value is required.
     *
     * @return boolean
     */
    public boolean isRequired()
    {
        return isRequired;
    }


    /**
     * Retrieve all the defined configuration properties
     *
     * @return list of configuration property types
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
     * Retrieve all the defined configuration property names
     *
     * @return list of configuration property types
     */
    public static List<String> getRecognizedConfigurationProperties()
    {
        List<String> configurationPropertyNames = new ArrayList<>();

        for (JDBCConfigurationProperty configurationProperty : JDBCConfigurationProperty.values())
        {
            configurationPropertyNames.add(configurationProperty.getName());
        }

        return configurationPropertyNames;
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
