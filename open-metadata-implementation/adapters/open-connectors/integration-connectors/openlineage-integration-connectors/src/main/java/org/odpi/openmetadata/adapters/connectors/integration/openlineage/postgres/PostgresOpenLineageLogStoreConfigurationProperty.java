/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adapters.connectors.integration.openlineage.postgres;

import org.odpi.openmetadata.frameworks.openmetadata.specificationproperties.ConfigurationPropertyType;
import org.odpi.openmetadata.frameworks.openmetadata.types.DataType;

import java.util.ArrayList;
import java.util.List;

/**
 * PostgresOpenLineageLogStoreConfigurationProperty provides definitions for the configuration properties used with
 * the PostgreSQL OpenLineage log store integration connector.  Each can be set in the connector's connection, or in
 * the configuration properties of a catalog target relationship, which take precedence for that database schema.
 */
public enum PostgresOpenLineageLogStoreConfigurationProperty
{
    /**
     * The number of days that the records are kept.  The name matches the Pasteur action history clean up connector.
     */
    DAYS_TO_CLEAN_UP("daysToCleanUp",
                     "The number of days that open lineage events, and their digest, are kept in the log store.  Each refresh deletes the events, assertions and tests recorded before this many days ago, and the runs whose last event was before then (along with the datasets they read and wrote).  If it is not set, nothing is deleted.  Zero deletes everything stored before the refresh.  The analysis services that read the log store can only look back this far, so it should be at least as long as their analysis window.",
                     DataType.LONG.getDisplayName(),
                     "30",
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
     * @param name name of the property
     * @param description description of the property
     * @param dataType type of value
     * @param example example of the value
     * @param isPlaceholder is this also used as a placeholder property?
     */
    PostgresOpenLineageLogStoreConfigurationProperty(String  name,
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
     * Return the data type of the configuration property.
     *
     * @return data type name
     */
    public String getDataType()
    {
        return dataType;
    }


    /**
     * Return an example of the configuration property.
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
     * Return the names of the configuration properties.
     *
     * @return list of names
     */
    public static List<String> getRecognizedConfigurationProperties()
    {
        List<String> recognizedConfigurationProperties = new ArrayList<>();

        for (PostgresOpenLineageLogStoreConfigurationProperty configurationProperty : PostgresOpenLineageLogStoreConfigurationProperty.values())
        {
            recognizedConfigurationProperties.add(configurationProperty.getName());
        }

        return recognizedConfigurationProperties;
    }


    /**
     * Retrieve the defined configuration properties as a list of the type used in the connector's specification.
     *
     * @return list
     */
    public static List<ConfigurationPropertyType> getConfigurationPropertyTypes()
    {
        List<ConfigurationPropertyType> configurationPropertyTypes = new ArrayList<>();

        for (PostgresOpenLineageLogStoreConfigurationProperty configurationProperty : PostgresOpenLineageLogStoreConfigurationProperty.values())
        {
            ConfigurationPropertyType configurationPropertyType = new ConfigurationPropertyType();

            configurationPropertyType.setName(configurationProperty.name);
            configurationPropertyType.setDescription(configurationProperty.description);
            configurationPropertyType.setDataType(configurationProperty.dataType);
            configurationPropertyType.setExample(configurationProperty.example);
            configurationPropertyType.setRequired(configurationProperty.isPlaceholder);

            configurationPropertyTypes.add(configurationPropertyType);
        }

        return configurationPropertyTypes;
    }


    /**
     * Output of this enum class and main value.
     *
     * @return string showing enum value
     */
    @Override
    public String toString()
    {
        return "PostgresOpenLineageLogStoreConfigurationProperty{ name=" + name + "}";
    }
}
