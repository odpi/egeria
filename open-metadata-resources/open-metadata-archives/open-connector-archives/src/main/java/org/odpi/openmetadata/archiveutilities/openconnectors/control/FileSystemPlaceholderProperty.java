/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.archiveutilities.openconnectors.control;


import org.odpi.openmetadata.frameworks.governanceaction.controls.PlaceholderPropertyType;

import java.util.ArrayList;
import java.util.List;

/**
 * PlaceholderProperty provides some standard definitions for placeholder properties used to pass properties
 * to services that use templates.
 */
public enum FileSystemPlaceholderProperty
{
    /**
     * The host IP address or domain name.
     */
    HOST_IDENTIFIER ("hostIdentifier", "The host IP address or domain name of the host.", "string", "coconet.com"),

    /**
     * The display name of the file system being catalogued.
     */
    FILE_SYSTEM_NAME ("fileSystemName", "The display name of the file system being catalogued.", "string", "myMachine"),

    /**
     * The description of the file system being catalogued.
     */
    FILE_SYSTEM_DESCRIPTION ("fileSystemDescription", "The description of the file system being catalogued.", "string", null),

    /**
     * The version of the file system.
     */
    VERSION_IDENTIFIER ("versionIdentifier", "The version of the file system.", "string", "V1.5"),

    /**
     * The format standard used in the file system.
     */
    FORMAT ("format", "The format standard used in the file system.", "string", "APFS"),


    /**
     * Is encryption enabled on this file system? If known, what type of encryption?
     */
    ENCRYPTION ("encryption", "Is encryption enabled on this file system? If known, what type of encryption?", "string", "Not Enabled"),

    ;

    public final String           name;
    public final String           description;
    public final String           dataType;
    public final String           example;


    /**
     * Create a specific Enum constant.
     *
     * @param name name of the placeholder property
     * @param description description of the placeholder property
     * @param dataType type of value of the placeholder property
     * @param example example of the placeholder property
     */
    FileSystemPlaceholderProperty(String name,
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
     * Retrieve the Kafka Topic defined placeholder properties
     *
     * @return list of placeholder property types
     */
    public static List<PlaceholderPropertyType> getPlaceholderPropertyTypes()
    {
        List<PlaceholderPropertyType> placeholderPropertyTypes = new ArrayList<>();

        for (FileSystemPlaceholderProperty placeholderProperty : FileSystemPlaceholderProperty.values())
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
