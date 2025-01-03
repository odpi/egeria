/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworks.surveyaction.measurements;

import java.util.ArrayList;
import java.util.List;

/**
 * FileDirectoryMetric describes the metrics for a directory (folder) that are captured by the Folder Survey Action Service.
 */
public enum FileDirectoryMetric implements SurveyMetric
{
    /**
     * Number of files found under the surveyed directory (and all subdirectories).
     */
    FILE_COUNT ("fileCount", "long", "Number of files", "Number of files found under the surveyed directory (and all subdirectories)."),

    /**
     * Total number of bytes from all files found under the surveyed directory (and all subdirectories).
     */
    TOTAL_FILE_SIZE ("totalFileSize", "double", "Total file size", "Total number of bytes from all files found under the surveyed directory (and all subdirectories)."),

    /**
     * Number of subdirectories found under the surveyed directory.
     */
    SUB_DIRECTORY_COUNT ("subDirectoryCount", "long", "Number of subdirectories (folders)", "Number of subdirectories found under the surveyed directory."),

    /**
     * Number of files and directories found under the surveyed directory.
     */
    READABLE_FILE_COUNT ("readableFileCount", "long", "Readable files/directories", "Number of files and directories found under the surveyed directory."),

    /**
     * Number of files and directories found under the surveyed directory that can be written to.
     */
    WRITEABLE_FILE_COUNT ("writeableFileCount", "long", "Writable files/directories", "Number of files and directories found under the surveyed directory that can be written to."),

    /**
     * Number of files and directories found under the surveyed that can be executed.
     */
    EXECUTABLE_FILE_COUNT ("executableFileCount", "long", "Executable files/directories", "Number of files and directories found under the surveyed that can be executed."),

    /**
     * Number of files under the surveyed directory that are symbolic links to files in other locations.
     */
    SYM_LINK_COUNT ("symLinkFileCount", "long", "Symbolic Link File Count", "Number of files under the surveyed directory that are symbolic links to files in other locations."),

    /**
     * Number of files under the surveyed directory are hidden from standard users.
     */
    HIDDEN_FILE_COUNT ("hiddenFileCount", "long", "Hidden File Count", "Number of files under the surveyed directory are hidden from standard users."),

    /**
     * Number of different file names found under the surveyed directory.
     */
    FILE_NAME_COUNT ("fileNameCount", "long", "Number of unique filenames", "Number of different file names found under the surveyed directory."),

    /**
     * Number of different file extensions found under the surveyed directory.
     */
    FILE_EXTENSION_COUNT ("fileExtensionCount", "long", "Number of unique file extensions", "Number of different file extensions found under the surveyed directory."),

    /**
     * Number of different file types detected when classifying the files under the surveyed directory.
     */
    FILE_TYPE_COUNT ("fileTypeCount", "long", "Number of file types", "Number of different file types detected when classifying the files under the surveyed directory."),

    /**
     * Number of different file asset types detected when classifying the files under the surveyed directory.
     */
    ASSET_TYPE_COUNT ("assetTypeCount", "long", "Number of asset types", "Number of different file asset types detected when classifying the files under the surveyed directory."),

    /**
     * Number of different file deployed implementation types detected when classifying the files under the surveyed directory.
     */
    DEPLOYED_IMPL_TYPE_COUNT ("deployedImplementationTypeCount", "long", "Number of deployed implementation types", "Number of different file deployed implementation types detected when classifying the files under the surveyed directory."),

    /**
     * Number of files under the surveyed directory that could not be classified using the existing reference data.
     */
    UNCLASSIFIED_FILE_COUNT ("unclassifiedFileCount", "long", "Number of unclassified files", "Number of files under the surveyed directory that could not be classified using the existing reference data."),

    /**
     * Number of files under the surveyed folder that could not be accessed even to extract their basic properties.
     */
    INACCESSIBLE_FILE_COUNT ("inaccessibleFileCount", "long", "Number of inaccessible files", "Number of files under the surveyed folder that could not be accessed even to extract their basic properties."),

    /**
     * The last time that a file was created in the surveyed directory (or any subdirectory).
     */
    LAST_FILE_CREATION_TIME ("lastFileCreationTime", "date", "Last file creation time", "The last time that a file was created in the surveyed directory (or any subdirectory)."),

    /**
     * The last time that a file was created in the surveyed directory (or any subdirectory).
     */
    LAST_FILE_MODIFIED_TIME ("lastFileModificationTime", "date", "Last file modification time", "The last time that a file was updated in the surveyed directory (or any subdirectory)."),

    /**
     * The last time that a file was accessed in the surveyed directory (or any subdirectory).
     */
    LAST_FILE_ACCESSED_TIME ("lastFileAccessedTime", "date", "Last file access time", "The last time that a file was accessed in the surveyed directory (or any subdirectory)."),
    ;


    public final String propertyName;
    public final String dataType;
    public final String displayName;
    public final String description;



    /**
     * Create a specific Enum constant.
     *
     * @param propertyName name of the property used to store the measurement
     * @param dataType data type of property
     * @param displayName name of the request type
     * @param description description of the request type
     */
    FileDirectoryMetric(String propertyName,
                        String dataType,
                        String displayName,
                        String description)
    {
        this.propertyName = propertyName;
        this.dataType     = dataType;
        this.displayName  = displayName;
        this.description  = description;
    }


    /**
     * Return the property name used to store the measurement.
     *
     * @return name
     */
    @Override
    public String getPropertyName()
    {
        return propertyName;
    }


    /**
     * Return the data type of the property used to store the measure.
     *
     * @return data type name
     */
    @Override
    public String getDataType()
    {
        return dataType;
    }


    /**
     * Return the name of the metric.
     *
     * @return string name
     */
    @Override
    public String getDisplayName()
    {
        return displayName;
    }


    /**
     * Return the description of the metric.
     *
     * @return text
     */
    @Override
    public String getDescription()
    {
        return description;
    }


    /**
     * Return the defined metrics as a list
     *
     * @return list
     */
    public static List<FileDirectoryMetric> getMetrics()
    {
        return new ArrayList<>(List.of(FileDirectoryMetric.values()));
    }


    /**
     * Output of this enum class and main value.
     *
     * @return string showing enum value
     */
    @Override
    public String toString()
    {
        return "FileDirectoryMetric{" + displayName + "}";
    }
}
