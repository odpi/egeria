/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adapters.repositoryservices.auditlogstore.postgres.controls;


import org.odpi.openmetadata.adapters.connectors.resource.jdbc.controls.JDBCConfigurationProperty;
import org.odpi.openmetadata.frameworks.openmetadata.specificationproperties.ConfigurationPropertyType;
import org.odpi.openmetadata.frameworks.openmetadata.types.DataType;

import java.util.List;

/**
 * PostgresConfigurationProperty provides some standard definitions for configuration properties used to
 * pass properties to connectors when they run.  Using standard names for request parameters wherever possible
 * helps to simplify the integration of governance services.
 */
public enum PostgreSQLAuditLogConfigurationProperty
{
    /**
     * Provide a list of supported severities that should be logged to this destination. An empty list means all severities.
     */
    SUPPORTED_SEVERITIES("supportedSeverities", "Provide a list of supported severities that should be logged to this destination. An empty list means all severities.", DataType.ARRAY_STRING.getName(), "[\"Error\", \"Exception\", \"Activity\", \"Action\", \"Decision\"]"),

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
    PostgreSQLAuditLogConfigurationProperty(String name,
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
        List<String> recognizedConfigurationProperties = JDBCConfigurationProperty.getRecognizedConfigurationProperties();

        for (PostgreSQLAuditLogConfigurationProperty configurationProperty : PostgreSQLAuditLogConfigurationProperty.values())
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
        List<ConfigurationPropertyType> configurationPropertyTypes = JDBCConfigurationProperty.getConfigurationPropertyTypes();

        for (PostgreSQLAuditLogConfigurationProperty configurationProperty : PostgreSQLAuditLogConfigurationProperty.values())
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
        return "PostgreSQLAuditLogConfigurationProperty{ name=" + name + "}";
    }
}
