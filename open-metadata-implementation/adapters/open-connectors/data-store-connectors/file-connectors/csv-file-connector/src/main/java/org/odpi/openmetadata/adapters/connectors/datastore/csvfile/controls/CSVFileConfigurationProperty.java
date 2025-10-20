/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adapters.connectors.datastore.csvfile.controls;

import org.odpi.openmetadata.frameworks.connectors.controls.ConfigurationPropertyType;
import org.odpi.openmetadata.frameworks.openmetadata.controls.PlaceholderProperty;
import org.odpi.openmetadata.frameworks.openmetadata.types.DataType;

import java.util.ArrayList;
import java.util.List;

/**
 * CSVFileConfigurationProperty provides definitions for the configuration properties used with the CSV File connectors.
 */
public enum CSVFileConfigurationProperty
{
    /**
     * The list of column names to use for the data stored in the file.
     * This is when the column names are not stored in the first line of the file.
     */
    COLUMN_NAMES( "columnNames",
                  "The list of column names to use for the data stored in the file.  This is when the column names are not stored in the first line of the file.",
                  DataType.ARRAY_STRING.getName(),
                  null,
                  false),

    /**
     * The character to use as the delimiter between data values in each column.
     */
    DELIMITER_CHARACTER("delimiterCharacter",
                        "The character to use as the delimiter between data values in each column.",
                        DataType.CHAR.getName(),
                        ",",
                        false),

    /**
     * The character used to use as the quote character
     */
    QUOTE_CHARACTER("quoteCharacter",
                    "The character used to use as the quote character",
                    DataType.CHAR.getName(),
                    "\"",
                    false),

    /**
     * The pathname of the .
     */
    DIRECTORY_PATH_NAME (PlaceholderProperty.DIRECTORY_PATH_NAME.getName(),
                         PlaceholderProperty.DIRECTORY_PATH_NAME.getDescription(),
                         PlaceholderProperty.DIRECTORY_PATH_NAME.getDataType(),
                         PlaceholderProperty.DIRECTORY_PATH_NAME.getExample(),
                         true),

    /**
     * The name of the database table being catalogued.
     */
    TABLE_NAME (PlaceholderProperty.TABLE_NAME.getName(),
                PlaceholderProperty.TABLE_NAME.getDescription(),
                PlaceholderProperty.TABLE_NAME.getDataType(),
                PlaceholderProperty.TABLE_NAME.getExample(),
                true),

    /**
     * The description of the table being catalogued.
     */
    TABLE_DESCRIPTION (PlaceholderProperty.TABLE_DESCRIPTION.getName(),
                       PlaceholderProperty.TABLE_DESCRIPTION.getDescription(),
                       PlaceholderProperty.TABLE_DESCRIPTION.getDataType(),
                       PlaceholderProperty.TABLE_DESCRIPTION.getExample(),
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
    CSVFileConfigurationProperty(String  name,
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
     * Get recognizedConfigurationProperties for the CSVFileStore resource connector.
     *
     * @return list of property names
     */
    public static List<String> getCSVFileStoreConfigPropertyNames()
    {
        List<String> recognizedConfigurationProperties = new ArrayList<>();

        recognizedConfigurationProperties.add(CSVFileConfigurationProperty.COLUMN_NAMES.getName());
        recognizedConfigurationProperties.add(CSVFileConfigurationProperty.DELIMITER_CHARACTER.getName());
        recognizedConfigurationProperties.add(CSVFileConfigurationProperty.QUOTE_CHARACTER.getName());

        return recognizedConfigurationProperties;
    }


    /**
     * Retrieve the defined configuration properties for the CSVFileStore resource connector.
     *
     * @return list of configuration property types
     */
    public static List<ConfigurationPropertyType> getCSVFileStoreConfigConfigurationPropertyTypes()
    {
        List<ConfigurationPropertyType> configurationPropertyTypes = new ArrayList<>();

        configurationPropertyTypes.add(CSVFileConfigurationProperty.COLUMN_NAMES.getConfigurationPropertyType());
        configurationPropertyTypes.add(CSVFileConfigurationProperty.DELIMITER_CHARACTER.getConfigurationPropertyType());
        configurationPropertyTypes.add(CSVFileConfigurationProperty.QUOTE_CHARACTER.getConfigurationPropertyType());

        return configurationPropertyTypes;
    }



    /**
     * Get recognizedConfigurationProperties for the CSV tabular data set resource connector.
     *
     * @return list of property names
     */
    public static List<String> getCSVTabularDataSetConfigPropertyNames()
    {
        List<String> recognizedConfigurationProperties = new ArrayList<>();

        recognizedConfigurationProperties.add(CSVFileConfigurationProperty.DIRECTORY_PATH_NAME.getName());
        recognizedConfigurationProperties.add(CSVFileConfigurationProperty.TABLE_NAME.getName());
        recognizedConfigurationProperties.add(CSVFileConfigurationProperty.TABLE_DESCRIPTION.getName());

        return recognizedConfigurationProperties;
    }


    /**
     * Retrieve the defined configuration properties for the CSV tabular data set connector
     *
     * @return list of configuration property types
     */
    public static List<ConfigurationPropertyType> getCSVTabularDataSetConfigurationPropertyTypes()
    {
        List<ConfigurationPropertyType> configurationPropertyTypes = new ArrayList<>();

        configurationPropertyTypes.add(CSVFileConfigurationProperty.DIRECTORY_PATH_NAME.getConfigurationPropertyType());
        configurationPropertyTypes.add(CSVFileConfigurationProperty.TABLE_NAME.getConfigurationPropertyType());
        configurationPropertyTypes.add(CSVFileConfigurationProperty.TABLE_DESCRIPTION.getConfigurationPropertyType());

        return configurationPropertyTypes;
    }


    /**
     * Retrieve the defined configuration properties for the CSV tabular data set collection connector
     *
     * @return list of configuration property types
     */
    public static List<ConfigurationPropertyType> getCSVTabularDataSetCollectionConfigurationPropertyTypes()
    {
        List<ConfigurationPropertyType> configurationPropertyTypes = new ArrayList<>();

        configurationPropertyTypes.add(CSVFileConfigurationProperty.DIRECTORY_PATH_NAME.getConfigurationPropertyType());

        return configurationPropertyTypes;
    }


    /**
     * Get recognizedConfigurationProperties for the CSV tabular data set collection resource connector.
     *
     * @return list of property names
     */
    public static List<String> getCSVTabularDataSetCollectionConfigPropertyNames()
    {
        List<String> recognizedConfigurationProperties = new ArrayList<>();

        recognizedConfigurationProperties.add(CSVFileConfigurationProperty.DIRECTORY_PATH_NAME.getName());

        return recognizedConfigurationProperties;
    }

    /**
     * Retrieve all the defined configuration properties
     *
     * @return list of configuration property types
     */
    public static List<ConfigurationPropertyType> getAllConfigurationPropertyTypes()
    {
        List<ConfigurationPropertyType> configurationPropertyTypes = new ArrayList<>();

        for (CSVFileConfigurationProperty configurationProperty : CSVFileConfigurationProperty.values())
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
