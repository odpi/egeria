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
     * The name of the leaf directory, without its enclosing directories.
     */
    FOLDER_NAME("folderName", "The name of the leaf directory, without its enclosing directories.", "string", "myFolder"),

    /**
     * The display name of the data set.
     */
    DATA_SET_NAME("dataSetName", "The display name of the data set.", "string", "myDataSet"),

    /**
     * The formula used to populate the data set.
     */
    FORMULA("formula", "The formula used to populate the data set.", "string", null),

    /**
     * The language/format used in the data set's formula.
     */
    FORMULA_TYPE("formulaType", "The language/format used in the data set's formula.", "string", null),

    /**
     * The full pathname of the file including the directory names, file name and file extension.
     */
    PATH_NAME ("pathName", "The full pathname of the file including the directory names, file name and optional file extension, if applicable.", "string", "/a/b/c/myFile.txt"),

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

    /**
     * The deployed implementation type for the file.
     */
    DEPLOYED_IMPLEMENTATION_TYPE ("deployedImplementationType",
                   "The deployed implementation type for the file.",
                   "string",
                   "Build File"),


    /**
     * The description of the resource to help a consumer understand its content and purpose.
     */
    DESCRIPTION ("description",
                 "The description of the resource to help a consumer understand its content and purpose.",
                 "string",
                 "This file contains a moth-worth of patient data for the Teddy Bear Drop Foot clinical trial."),


    /**
     * The programming language used to encode the file.
     */
    PROGRAMMING_LANGUAGE ("programmingLanguage",
                          "The programming language used to encode the file.",
                          "string",
                          "Java"),

    /**
     * Descriptive metadata values embedded within the file.
     */
    EMBEDDED_METADATA ("embeddedMetadata",
                          "Descriptive metadata values embedded within the file.",
                          "map<string, string>",
                          null),

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
     * Retrieve all the defined placeholder properties for data files
     *
     * @return list of placeholder property types
     */
    public static List<PlaceholderPropertyType> getDataFilesPlaceholderPropertyTypes()
    {
        List<PlaceholderPropertyType> placeholderPropertyTypes = new ArrayList<>();

        placeholderPropertyTypes.add(PATH_NAME.getPlaceholderType());
        placeholderPropertyTypes.add(FILE_NAME.getPlaceholderType());
        placeholderPropertyTypes.add(FILE_TYPE.getPlaceholderType());
        placeholderPropertyTypes.add(FILE_EXTENSION.getPlaceholderType());
        placeholderPropertyTypes.add(FILE_ENCODING.getPlaceholderType());

        return placeholderPropertyTypes;
    }


    /**
     * Retrieve all the defined placeholder properties for data files
     *
     * @return list of placeholder property types
     */
    public static List<PlaceholderPropertyType> getMediaFilesPlaceholderPropertyTypes()
    {
        List<PlaceholderPropertyType> placeholderPropertyTypes = new ArrayList<>();

        placeholderPropertyTypes.add(PATH_NAME.getPlaceholderType());
        placeholderPropertyTypes.add(FILE_NAME.getPlaceholderType());
        placeholderPropertyTypes.add(FILE_TYPE.getPlaceholderType());
        placeholderPropertyTypes.add(FILE_EXTENSION.getPlaceholderType());
        placeholderPropertyTypes.add(FILE_ENCODING.getPlaceholderType());
        placeholderPropertyTypes.add(EMBEDDED_METADATA.getPlaceholderType());

        return placeholderPropertyTypes;
    }


    /**
     * Retrieve all the defined placeholder properties for directories (file folder)
     *
     * @return list of placeholder property types
     */
    public static List<PlaceholderPropertyType> getFolderPlaceholderPropertyTypes()
    {
        List<PlaceholderPropertyType> placeholderPropertyTypes = new ArrayList<>();

        placeholderPropertyTypes.add(PATH_NAME.getPlaceholderType());
        placeholderPropertyTypes.add(FOLDER_NAME.getPlaceholderType());

        return placeholderPropertyTypes;
    }


    /**
     * Retrieve all the defined placeholder properties for directories (file folder)
     *
     * @return list of placeholder property types
     */
    public static List<PlaceholderPropertyType> getDataSetPlaceholderPropertyTypes()
    {
        List<PlaceholderPropertyType> placeholderPropertyTypes = new ArrayList<>();

        placeholderPropertyTypes.add(DEPLOYED_IMPLEMENTATION_TYPE.getPlaceholderType());
        placeholderPropertyTypes.add(DATA_SET_NAME.getPlaceholderType());
        placeholderPropertyTypes.add(FORMULA.getPlaceholderType());
        placeholderPropertyTypes.add(FORMULA_TYPE.getPlaceholderType());

        return placeholderPropertyTypes;
    }


    /**
     * Retrieve all the defined placeholder properties for files associated with software
     *
     * @return list of placeholder property types
     */
    public static List<PlaceholderPropertyType> getSoftwareFilesPlaceholderPropertyTypes()
    {
        List<PlaceholderPropertyType> placeholderPropertyTypes = new ArrayList<>();

        placeholderPropertyTypes.add(DEPLOYED_IMPLEMENTATION_TYPE.getPlaceholderType());
        placeholderPropertyTypes.add(PATH_NAME.getPlaceholderType());
        placeholderPropertyTypes.add(FILE_NAME.getPlaceholderType());
        placeholderPropertyTypes.add(FILE_TYPE.getPlaceholderType());
        placeholderPropertyTypes.add(FILE_EXTENSION.getPlaceholderType());
        placeholderPropertyTypes.add(FILE_ENCODING.getPlaceholderType());
        placeholderPropertyTypes.add(PROGRAMMING_LANGUAGE.getPlaceholderType());

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
