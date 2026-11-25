/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworks.openmetadata.controls;

import org.odpi.openmetadata.frameworks.openmetadata.specificationproperties.ConfigurationPropertyType;
import org.odpi.openmetadata.frameworks.openmetadata.types.DataType;

import java.util.ArrayList;
import java.util.List;

/**
 * TabularFileConfigurationProperty provides definitions for the configuration properties used with the connectors
 * that work with files holding tabular data.  The directory, table name and table description properties are
 * used by the tabular data set connectors for both CSV and Apache Parquet files.  The column names, delimiter
 * character and quote character properties are specific to CSV files.
 */
public enum TabularFileConfigurationProperty
{
    /**
     * The list of column names to use for the data stored in the file.
     * This is when the column names are not stored in the first line of the file.
     */
    COLUMN_NAMES( "columnNames",
                  "The list of column names to use for the data stored in the file.  This is when the column names are not stored in the first line of the file.",
                  DataType.ARRAY_STRING.getDisplayName(),
                  null,
                  false),

    /**
     * The character to use as the delimiter between data values in each column.
     */
    DELIMITER_CHARACTER("delimiterCharacter",
                        "The character to use as the delimiter between data values in each column.",
                        DataType.CHAR.getDisplayName(),
                        ",",
                        false),

    /**
     * The character used to use as the quote character
     */
    QUOTE_CHARACTER("quoteCharacter",
                    "The character used to use as the quote character",
                    DataType.CHAR.getDisplayName(),
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
    TabularFileConfigurationProperty(String  name,
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

        recognizedConfigurationProperties.add(TabularFileConfigurationProperty.COLUMN_NAMES.getName());
        recognizedConfigurationProperties.add(TabularFileConfigurationProperty.DELIMITER_CHARACTER.getName());
        recognizedConfigurationProperties.add(TabularFileConfigurationProperty.QUOTE_CHARACTER.getName());

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

        configurationPropertyTypes.add(TabularFileConfigurationProperty.COLUMN_NAMES.getConfigurationPropertyType());
        configurationPropertyTypes.add(TabularFileConfigurationProperty.DELIMITER_CHARACTER.getConfigurationPropertyType());
        configurationPropertyTypes.add(TabularFileConfigurationProperty.QUOTE_CHARACTER.getConfigurationPropertyType());

        return configurationPropertyTypes;
    }



    /**
     * Get recognizedConfigurationProperties for the tabular data set resource connectors that read files (CSV and Parquet).
     *
     * @return list of property names
     */
    public static List<String> getTabularDataSetConfigPropertyNames()
    {
        List<String> recognizedConfigurationProperties = new ArrayList<>();

        recognizedConfigurationProperties.add(TabularFileConfigurationProperty.DIRECTORY_PATH_NAME.getName());
        recognizedConfigurationProperties.add(TabularFileConfigurationProperty.TABLE_NAME.getName());
        recognizedConfigurationProperties.add(TabularFileConfigurationProperty.TABLE_DESCRIPTION.getName());

        return recognizedConfigurationProperties;
    }


    /**
     * Retrieve the defined configuration properties for the tabular data set connectors that read files (CSV and Parquet)
     *
     * @return list of configuration property types
     */
    public static List<ConfigurationPropertyType> getTabularDataSetConfigurationPropertyTypes()
    {
        List<ConfigurationPropertyType> configurationPropertyTypes = new ArrayList<>();

        configurationPropertyTypes.add(TabularFileConfigurationProperty.DIRECTORY_PATH_NAME.getConfigurationPropertyType());
        configurationPropertyTypes.add(TabularFileConfigurationProperty.TABLE_NAME.getConfigurationPropertyType());
        configurationPropertyTypes.add(TabularFileConfigurationProperty.TABLE_DESCRIPTION.getConfigurationPropertyType());

        return configurationPropertyTypes;
    }


    /**
     * Retrieve the defined configuration properties for the tabular data set collection connectors that read files (CSV and Parquet)
     *
     * @return list of configuration property types
     */
    public static List<ConfigurationPropertyType> getTabularDataSetCollectionConfigurationPropertyTypes()
    {
        List<ConfigurationPropertyType> configurationPropertyTypes = new ArrayList<>();

        configurationPropertyTypes.add(TabularFileConfigurationProperty.DIRECTORY_PATH_NAME.getConfigurationPropertyType());

        return configurationPropertyTypes;
    }


    /**
     * Get recognizedConfigurationProperties for the tabular data set collection resource connectors that read files (CSV and Parquet).
     *
     * @return list of property names
     */
    public static List<String> getTabularDataSetCollectionConfigPropertyNames()
    {
        List<String> recognizedConfigurationProperties = new ArrayList<>();

        recognizedConfigurationProperties.add(TabularFileConfigurationProperty.DIRECTORY_PATH_NAME.getName());

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

        for (TabularFileConfigurationProperty configurationProperty : TabularFileConfigurationProperty.values())
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
        return "TabularFileConfigurationProperty{ name=" + name + "}";
    }
}
