/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adapters.connectors.surveyaction.surveyfile;

import org.odpi.openmetadata.frameworks.governanceaction.mapper.OpenMetadataProperty;

import java.util.ArrayList;
import java.util.List;

/**
 * FolderMetric describes the metrics for a directory (folder) that are captured by the Folder Survey Action Service.
 */
public enum FileMetric
{
    FILE_NAME (OpenMetadataProperty.FILE_NAME.name, OpenMetadataProperty.FILE_NAME.description),
    PATH_NAME (OpenMetadataProperty.PATH_NAME.name, OpenMetadataProperty.PATH_NAME.description),
    FILE_EXTENSION (OpenMetadataProperty.FILE_EXTENSION.name, OpenMetadataProperty.FILE_EXTENSION.description),
    FILE_TYPE (OpenMetadataProperty.FILE_TYPE.name, OpenMetadataProperty.FILE_TYPE.description),
    DEPLOYED_IMPLEMENTATION_TYPE (OpenMetadataProperty.DEPLOYED_IMPLEMENTATION_TYPE.name, OpenMetadataProperty.DEPLOYED_IMPLEMENTATION_TYPE.description),
    ENCODING (OpenMetadataProperty.ENCODING.name, OpenMetadataProperty.ENCODING.description),
    ASSET_TYPE_NAME ("AssetTypeName", "The open metadata type used to catalog this type of file."),
    CAN_READ ("canRead", "Can this file be read?"),
    CAN_WRITE ("canWrite", "Can this file be updated?"),
    CAN_EXECUTE ("canExecute", "Can this file be executed as a program?"),
    IS_SYM_LINK ("isSymLink", "Is this file a symbolic link to a file in another location?"),
    IS_HIDDEN ("isHidden", "Is this file hidden from standard users?"),

    ;

    public final String name;
    public final String description;



    /**
     * Create a specific Enum constant.
     *
     * @param name name of the request type
     * @param description description of the request type
     */
    FileMetric(String name,
               String description)
    {
        this.name        = name;
        this.description = description;
    }


    /**
     * Return the name of the metric.
     *
     * @return string name
     */
    public String getName()
    {
        return name;
    }


    /**
     * Return the description of the metric.
     *
     * @return text
     */
    public String getDescription()
    {
        return description;
    }


    /**
     * Return the defined metrics as a list
     *
     * @return list
     */
    static List<FileMetric> getMetrics()
    {
        return new ArrayList<>(List.of(FileMetric.values()));
    }


    /**
     * Output of this enum class and main value.
     *
     * @return string showing enum value
     */
    @Override
    public String toString()
    {
        return "FolderMetric{" + name + "}";
    }
}
