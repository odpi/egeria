/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adapters.connectors.pepys.controls;

import org.odpi.openmetadata.frameworks.openmetadata.specificationproperties.ConfigurationPropertyType;
import org.odpi.openmetadata.frameworks.openmetadata.types.DataType;

import java.util.ArrayList;
import java.util.List;

/**
 * PepysConfigurationProperty provides definitions for the configuration properties used with the
 * Pepys Activity Observer.
 */
public enum PepysConfigurationProperty
{
    /**
     * The patterns that match the userIds whose activity is not counted.
     */
    IGNORED_USER_ID_PATTERNS("ignoredUserIdPatterns",
                             "An optional comma-separated list of regular expressions (Java syntax), each matched against the whole userId.  The activity of a user whose userId matches any of them is not counted.  They typically match the non-personal accounts (userIds ending in npa) of the OMAG servers and integration connectors, and the userIds of the governance engines (ending in engine), so that only the activity of people and external tools is recorded.  A plain userId is an exact match.  Pepys's own userId is always ignored, whether a pattern matches it or not, since otherwise each update it makes to a UsageActivity classification would be counted as more activity.",
                             DataType.ARRAY_STRING.getDisplayName(),
                             ".*npa,.*engine",
                             false),

    ;

    public final String  name;
    public final String  description;
    public final String  dataType;
    public final String  example;
    public final boolean isPlaceholder;


    /**
     * The patterns set up by the core content pack: the non-personal accounts of the OMAG servers and integration
     * connectors (ending in npa) and the governance engines (ending in engine).
     */
    public static final List<String> DEFAULT_IGNORED_USER_ID_PATTERNS = List.of(".*npa", ".*engine");


    /**
     * Create a specific Enum constant.
     *
     * @param name name of the configuration property
     * @param description description of the configuration property
     * @param dataType type of value of the configuration property
     * @param example example of the configuration property
     * @param isPlaceholder is this also used as a placeholder property?
     */
    PepysConfigurationProperty(String  name,
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

        for (PepysConfigurationProperty configurationProperty : PepysConfigurationProperty.values())
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
        return "PepysConfigurationProperty{ name=" + name + "}";
    }
}
