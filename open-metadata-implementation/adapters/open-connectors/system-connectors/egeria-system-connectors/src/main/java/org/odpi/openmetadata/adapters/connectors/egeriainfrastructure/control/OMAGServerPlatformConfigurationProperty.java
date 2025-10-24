/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adapters.connectors.egeriainfrastructure.control;

import org.odpi.openmetadata.frameworks.openmetadata.specificationproperties.ConfigurationPropertyType;

import java.util.ArrayList;
import java.util.List;

/**
 * OMAGServerPlatformConfigurationProperty defines the configuration properties used with the OMAG Server Platform connectors.
 */
public enum OMAGServerPlatformConfigurationProperty
{
    /**
     * Unique identifier of a server running on the platform.
     */
    SERVER_NAME ("serverName",
                 "Unique name of the OMAG Server.",
                 "string",
                 "cocoMDS1",
                 false),

    /**
     * Location of the secrets store.
     */
    SECRETS_STORE (OMAGServerPlatformPlaceholderProperty.SECRETS_STORE.getName(),
                   OMAGServerPlatformPlaceholderProperty.SECRETS_STORE.getDescription(),
                   OMAGServerPlatformPlaceholderProperty.SECRETS_STORE.getDataType(),
                   OMAGServerPlatformPlaceholderProperty.SECRETS_STORE.getExample(),
                   true),

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
    OMAGServerPlatformConfigurationProperty(String  name,
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
     * Get recognizedConfigurationProperties for the connector.
     *
     * @return list of property names
     */
    public static List<String> getRecognizedConfigurationProperties()
    {
        List<String> recognizedConfigurationProperties = new ArrayList<>();

        for (OMAGServerPlatformConfigurationProperty configurationProperty : OMAGServerPlatformConfigurationProperty.values())
        {
            recognizedConfigurationProperties.add(configurationProperty.getName());
        }
        return recognizedConfigurationProperties;
    }


    /**
     * Retrieve all the defined configuration properties
     *
     * @return list of configuration property types
     */
    public static List<ConfigurationPropertyType> getConfigurationPropertyTypes()
    {
        List<ConfigurationPropertyType> configurationPropertyTypes = new ArrayList<>();

        for (OMAGServerPlatformConfigurationProperty configurationProperty : OMAGServerPlatformConfigurationProperty.values())
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
