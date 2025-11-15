/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adapters.connectors.apachekafka.control;

import org.odpi.openmetadata.frameworks.openmetadata.specificationproperties.ConfigurationPropertyType;

import java.util.ArrayList;
import java.util.List;

/**
 * KafkaTemplateConfigurationProperty provides definitions for the configuration properties used with the Kafka Integration Connector.
 */
public enum KafkaTemplateConfigurationProperty
{
    /**
     * The property of the template to use when creating Kafka Topic assets.
     */
    TEMPLATE_NAME ("templateName",
                   "The property of the template to use when creating Kafka Topic assets.",
                   "string",
                   KafkaTemplateType.KAFKA_TOPIC_TEMPLATE.getTemplateName(),
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
    KafkaTemplateConfigurationProperty(String  name,
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

        for (KafkaTemplateConfigurationProperty configurationProperty : KafkaTemplateConfigurationProperty.values())
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

        for (KafkaTemplateConfigurationProperty configurationProperty : KafkaTemplateConfigurationProperty.values())
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
