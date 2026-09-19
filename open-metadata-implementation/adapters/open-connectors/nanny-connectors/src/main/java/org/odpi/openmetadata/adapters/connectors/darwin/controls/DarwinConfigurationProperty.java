/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adapters.connectors.darwin.controls;

import org.odpi.openmetadata.frameworks.openmetadata.specificationproperties.ConfigurationPropertyType;
import org.odpi.openmetadata.frameworks.openmetadata.types.DataType;

import java.util.ArrayList;
import java.util.List;

/**
 * DarwinConfigurationProperty provides definitions for the configuration properties used with the
 * Darwin Product Dependency Manager.
 */
public enum DarwinConfigurationProperty
{
    /**
     * The maximum number of lineage relationships that are followed downstream from an asset that is a member of a
     * digital product before the search for a dependent product is abandoned.
     */
    MAX_LINEAGE_DEPTH("maxLineageDepth",
                      "The maximum number of lineage relationships that are followed downstream from an asset that is a member of a digital product before the search for a dependent product is abandoned.",
                      DataType.INT.getDisplayName(),
                      "20",
                      false),

    ;

    public final String  name;
    public final String  description;
    public final String  dataType;
    public final String  example;
    public final boolean isPlaceholder;


    /**
     * The default maximum number of lineage relationships followed downstream from a product's asset.
     */
    public static final int DEFAULT_MAX_LINEAGE_DEPTH = 20;


    /**
     * Create a specific Enum constant.
     *
     * @param name name of the configuration property
     * @param description description of the configuration property
     * @param dataType type of value of the configuration property
     * @param example example of the configuration property
     * @param isPlaceholder is this also used as a placeholder property?
     */
    DarwinConfigurationProperty(String  name,
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
     * @return string description
     */
    public String getDescription()
    {
        return description;
    }


    /**
     * Return the data type for the configuration property.
     *
     * @return string data type name
     */
    public String getDataType()
    {
        return dataType;
    }


    /**
     * Return an example of the configuration property to help users understand how to set it up.
     *
     * @return string example
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
     * Retrieve the defined configuration properties as a list of the type used in the connector's specification.
     *
     * @return list
     */
    public static List<ConfigurationPropertyType> getConfigurationPropertyTypes()
    {
        List<ConfigurationPropertyType> configurationPropertyTypes = new ArrayList<>();

        for (DarwinConfigurationProperty configurationProperty : DarwinConfigurationProperty.values())
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
        return "DarwinConfigurationProperty{ name=" + name + "}";
    }
}
