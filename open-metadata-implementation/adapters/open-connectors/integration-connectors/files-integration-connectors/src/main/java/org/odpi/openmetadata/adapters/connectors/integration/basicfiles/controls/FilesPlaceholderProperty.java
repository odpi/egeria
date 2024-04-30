/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adapters.connectors.integration.basicfiles.controls;


import org.odpi.openmetadata.frameworks.governanceaction.controls.PlaceholderPropertyType;

import java.util.ArrayList;
import java.util.List;

/**
 * PlaceholderProperty provides some standard definitions for placeholder properties used to pass properties
 * to services that use templates.
 */
public enum FilesPlaceholderProperty
{
    /**
     * The full pathname of the file including the directory names, file name and file extension.
     */
    PATH_NAME ("pathName", "The full pathname of the file including the directory names, file name and file extension.", "string", "/a/b/c/myFile.txt"),

    /**
     * The short name of the file with its extension but without the directory names.
     */
    FILE_NAME("fileName", "The short name of the file with its extension but without the directory names.", "string", "myFile.txt"),

    /**
     * The logical file type of the file.
     */
    FILE_TYPE("fileType",
              "The logical file type of the file.",
              "string",
              "Text File"),

    /**
     * The postfix identifier in the file name that indicates the format of the file.
     */
    FILE_EXTENSION ("fileExtension",
                      "The postfix identifier in the file name that indicates the format of the file.",
                      "string",
                      "txt"),

    /**
     * The encoding scheme used on the file.
     */
    FILE_ENCODING ("fileEncoding",
                       "The encoding scheme used on the file.",
                       "string",
                       "JSON"),

        ;

    public final String name;
    public final String description;
    public final String dataType;
    public final String example;


    /**
     * Create a specific Enum constant.
     *
     * @param name name of the placeholder property
     * @param description description of the placeholder property
     * @param dataType type of value of the placeholder property
     * @param example example of the placeholder property
     */
    FilesPlaceholderProperty(String name,
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
     * Return the name of the placeholder property.
     *
     * @return string name
     */
    public String getName()
    {
        return name;
    }


    /**
     * Return the placeholder to use when building templates.
     *
     * @return placeholder property
     */
    public String getPlaceholder()
    {
        return "{{" + name + "}}";
    }


    /**
     * Return the description of the placeholder property.
     *
     * @return text
     */
    public String getDescription()
    {
        return description;
    }


    /**
     * Return the data type for the placeholder property.
     *
     * @return data type name
     */
    public String getDataType()
    {
        return dataType;
    }


    /**
     * Return an example of the placeholder property to help users understand how to set it up.
     *
     * @return example
     */
    public String getExample()
    {
        return example;
    }


    /**
     * Retrieve all the defined placeholder properties
     *
     * @return list of placeholder property types
     */
    public static List<PlaceholderPropertyType> getFilesPlaceholderPropertyTypes()
    {
        List<PlaceholderPropertyType> placeholderPropertyTypes = new ArrayList<>();

        for (FilesPlaceholderProperty placeholderProperty : FilesPlaceholderProperty.values())
        {
            placeholderPropertyTypes.add(placeholderProperty.getPlaceholderType());
        }

        return placeholderPropertyTypes;
    }


    /**
     * Return a summary of this enum to use in a service provider.
     *
     * @return placeholder property type
     */
    public PlaceholderPropertyType getPlaceholderType()
    {
        PlaceholderPropertyType placeholderPropertyType = new PlaceholderPropertyType();

        placeholderPropertyType.setName(name);
        placeholderPropertyType.setDescription(description);
        placeholderPropertyType.setDataType(dataType);
        placeholderPropertyType.setExample(example);
        placeholderPropertyType.setRequired(true);

        return placeholderPropertyType;
    }


    /**
     * Output of this enum class and main value.
     *
     * @return string showing enum value
     */
    @Override
    public String toString()
    {
        return "PlaceholderProperty{ name=" + name + "}";
    }
}
