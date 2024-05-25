/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adapters.connectors.surveyaction.surveyfolder;

import java.util.ArrayList;
import java.util.List;

/**
 * FolderMetric describes the metrics for a directory (folder) that are captured by the Folder Survey Action Service.
 */
public enum FolderMetric
{
    /**
     * Number of files found in the directory (and all subdirectories).
     */
    FILE_COUNT ("Number of Files", "Number of files found in the directory (and all subdirectories)."),

    /**
     * Number of subdirectories found under the surveyed directory.
     */
    SUB_DIRECTORY_COUNT ("Number of Subdirectories (folders)", "Number of subdirectories found under the surveyed directory."),

    /**
     * Number of files and directories found under the surveyed directory.
     */
    READABLE_FILE_COUNT ("Readable files/directories", "Number of files and directories found under the surveyed directory."),

    /**
     * Number of files and directories found under the surveyed directory that can be written to.
     */
    WRITEABLE_FILE_COUNT ("Writable files/directories", "Number of files and directories found under the surveyed directory that can be written to."),

    /**
     * Number of files and directories found under the surveyed that can be executed.
     */
    EXECUTABLE_FILE_COUNT ("Executable files/directories", "Number of files and directories found under the surveyed that can be executed."),

    /**
     * The last time that a file was created in the surveyed directory (or any subdirectory).
     */
    LAST_FILE_CREATION_TIME ("Last file creation time", "The last time that a file was created in the surveyed directory (or any subdirectory)."),

    /**
     * The last time that a file was created in the surveyed directory (or any subdirectory).
     */
    LAST_FILE_MODIFIED_TIME ("Last file modification time", "The last time that a file was updated in the surveyed directory (or any subdirectory)."),

    /**
     * The last time that a file was accessed in the surveyed directory (or any subdirectory).
     */
    LAST_FILE_ACCESSED_TIME ("Last file access time", "The last time that a file was accessed in the surveyed directory (or any subdirectory)."),
    ;

    public final String name;
    public final String description;



    /**
     * Create a specific Enum constant.
     *
     * @param name name of the request type
     * @param description description of the request type
     */
    FolderMetric(String name,
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
    static List<FolderMetric> getMetrics()
    {
        return new ArrayList<>(List.of(FolderMetric.values()));
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
