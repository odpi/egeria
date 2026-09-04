/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.jacquard.controls;

import org.odpi.openmetadata.frameworks.openmetadata.specificationproperties.ConfigurationPropertyType;
import org.odpi.openmetadata.frameworks.openmetadata.types.DataType;

import java.util.ArrayList;
import java.util.List;


/**
 * JacquardConfigurationProperty provides some standard definitions for configuration properties used to
 * pass properties to the Jacquard Digital Product Loom integration connector.
 */
public enum JacquardConfigurationProperty
{
    /**
     * Unique identifier of the Baudot Subscription Manager integration connector that notifies the subscribers of the digital products' notification types.
     */
    SUBSCRIPTION_MANAGER_GUID("subscriptionManagerGUID",
                              "Unique identifier of the Baudot Subscription Manager integration connector that notifies the subscribers of the digital products' notification types.  Each notification type Jacquard creates is added to this connector as a catalog target.  Without it, subscriptions are taken out but nothing delivers them.",
                              DataType.STRING.getDisplayName(),
                              "fed3e17d-6aa0-4959-8af4-a2cbfde1717b",
                              false),
    ;

    public final String  name;
    public final String  description;
    public final String  dataType;
    public final String  example;
    public final boolean isPlaceholder;


    /**
     * Create a specific Enum constant.
     *
     * @param name name of the configuration property
     * @param description description of the configuration property
     * @param dataType type of value of the configuration property
     * @param example example of the configuration property
     * @param isPlaceholder is this also used as a placeholder property?
     */
    JacquardConfigurationProperty(String  name,
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
     * Return the name of the configuration property.
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
     * Retrieve all the defined configuration properties
     *
     * @return list of configuration property types
     */
    public static List<ConfigurationPropertyType> getConfigurationPropertyTypes()
    {
        List<ConfigurationPropertyType> configurationPropertyTypes = new ArrayList<>();

        for (JacquardConfigurationProperty configurationProperty : JacquardConfigurationProperty.values())
        {
            configurationPropertyTypes.add(configurationProperty.getConfigurationPropertyType());
        }

        return configurationPropertyTypes;
    }


    /**
     * Return a summary of this enum to use in the connector's specification.
     *
     * @return configuration property type
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
        return "JacquardConfigurationProperty{ name=" + name + "}";
    }
}
