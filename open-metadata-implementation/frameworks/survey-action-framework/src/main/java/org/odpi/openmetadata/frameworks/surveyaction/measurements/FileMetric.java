/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworks.surveyaction.measurements;

import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;

import java.util.ArrayList;
import java.util.List;

/**
 * FileMetric describes the metrics for a file that are captured by the File Survey Action Service.
 */
public enum FileMetric implements SurveyMetric
{
    FILE_NAME (OpenMetadataProperty.FILE_NAME.name, OpenMetadataProperty.FILE_NAME.type, "File name", OpenMetadataProperty.FILE_NAME.description),
    PATH_NAME (OpenMetadataProperty.PATH_NAME.name, OpenMetadataProperty.PATH_NAME.type, "Path name", OpenMetadataProperty.PATH_NAME.description),
    FILE_EXTENSION (OpenMetadataProperty.FILE_EXTENSION.name, OpenMetadataProperty.FILE_EXTENSION.type, "File Extension", OpenMetadataProperty.FILE_EXTENSION.description),
    FILE_TYPE (OpenMetadataProperty.FILE_TYPE.name, OpenMetadataProperty.FILE_TYPE.type, "File type", OpenMetadataProperty.FILE_TYPE.description),
    DEPLOYED_IMPLEMENTATION_TYPE (OpenMetadataProperty.DEPLOYED_IMPLEMENTATION_TYPE.name, OpenMetadataProperty.DEPLOYED_IMPLEMENTATION_TYPE.type, "Deployed Implementation Type",  OpenMetadataProperty.DEPLOYED_IMPLEMENTATION_TYPE.description),
    ENCODING (OpenMetadataProperty.ENCODING_TYPE.name, OpenMetadataProperty.ENCODING_TYPE.type, "File encoding", OpenMetadataProperty.ENCODING_TYPE.description),
    ASSET_TYPE_NAME ("assetTypeName", "string","Open metadata type name for asset", "The open metadata type used to catalog this type of file."),
    CAN_READ ("canRead", "boolean", "Readable file?", "Can this file be read?"),
    CAN_WRITE ("canWrite", "boolean", "Writeable file?", "Can this file be updated?"),
    CAN_EXECUTE ("canExecute", "boolean", "Executable file?", "Can this file be executed as a program?"),
    IS_SYM_LINK ("symLink", "boolean", "Linked file?", "Is this file a symbolic link to a file in another location?"),
    IS_HIDDEN ("hidden", "boolean", "Hidden file?", "Is this file hidden from standard users?"),
    CREATION_TIME ("creationTime", "date", "Creation time", "The time that the file was created."),
    LAST_MODIFIED_TIME ("lastModifiedTime", "date", "Last modified time", "The time that the file was last modified."),
    LAST_ACCESSED_TIME ("lastAccessedTime", "date", "Last accessed time", "The time that the file was last accessed."),
    FILE_SIZE ("fileSize", "long", "File Size", "How many bytes are in the file."),
    RECORD_COUNT ("recordCount", "long", "Record Count", "How many record does this data file contain?"),

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
    FileMetric(String propertyName,
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
    public static List<SurveyMetric> getMetrics()
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
        return "FileMetric{" + displayName + "}";
    }
}
