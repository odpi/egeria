/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworks.openmetadata.controls;

import org.odpi.openmetadata.frameworks.openmetadata.specificationproperties.ConfigurationPropertyType;
import org.odpi.openmetadata.frameworks.openmetadata.types.DataType;

import java.util.ArrayList;
import java.util.List;

/**
 * FileSystemConfigurationProperty provides definitions for the configuration properties used to pass informaiton about the local file system.
 */
public enum FileSystemConfigurationProperty
{
    /**
     * The logical name of the physical file system.  This may be different from the file system retrieved by a program since files can be mounted at different points.
     */
    FILE_SYSTEM_NAME( "fileSystemName",
                  "The logical name of the physical file system.  This may be different from the file system retrieved by a program since files can be mounted at different points.",
                  DataType.STRING.getDisplayName(),
                  "egeria-deployment",
                  false),

    /**
     * The root part of the path name that is defined by the mount point.
     */
    LOCAL_MOUNT_POINT("localMountPoint",
                        "The root part of the path name that is defined by the mount point.",
                        DataType.STRING.getDisplayName(),
                        ".",
                        false),

    /**
     * The character used to use as the quote character
     */
    CANONICAL_MOUNT_POINT("canonicalMountPoint",
                    "The value to replace the localMountPoint part of the path name with when cataloguing in Egeria.  This is to allow files catalogued by different runtimes to be aligned.",
                    DataType.STRING.getDisplayName(),
                    "/deployments",
                    false),
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
    FileSystemConfigurationProperty(String  name,
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
     * Retrieve all the defined configuration properties
     *
     * @return list of configuration property types
     */
    public static List<ConfigurationPropertyType> getAllConfigurationPropertyTypes()
    {
        List<ConfigurationPropertyType> configurationPropertyTypes = new ArrayList<>();

        for (FileSystemConfigurationProperty configurationProperty : FileSystemConfigurationProperty.values())
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
