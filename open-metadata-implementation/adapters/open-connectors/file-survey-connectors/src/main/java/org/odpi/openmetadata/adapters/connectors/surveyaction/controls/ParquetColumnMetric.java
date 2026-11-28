/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adapters.connectors.surveyaction.controls;

import org.odpi.openmetadata.frameworks.opensurvey.measurements.SurveyMetric;

import java.util.ArrayList;
import java.util.List;

/**
 * ParquetColumnMetric describes the measurements that the Parquet survey action service takes for each column
 * that holds values in an Apache Parquet file.  They come from the statistics, and the descriptions of how the values
 * are stored, in the file's footer.
 */
public enum ParquetColumnMetric implements SurveyMetric
{
    /**
     * The number of values stored for the column, including nulls.
     */
    VALUE_COUNT ("valueCount", "long", "Value Count", "The number of values stored for the column, including nulls.  For a column that is not nested in a list or map, this is the number of rows."),

    /**
     * The number of null values in the column.
     */
    NULL_COUNT ("nullCount", "long", "Null Count", "The number of null values in the column."),

    /**
     * The number of distinct values in the column.
     */
    DISTINCT_COUNT ("distinctCount", "long", "Distinct Count", "The number of distinct values in the column.  This is an exact count when the values have been read to count how many times each occurs.  Otherwise, it is only available if the writer recorded it and the file has a single row group."),

    /**
     * The number of bytes that the column's values occupy in the file.
     */
    COMPRESSED_SIZE ("compressedSize", "long", "Compressed Size", "The number of bytes that the column's values occupy in the file."),

    /**
     * The number of bytes that the column's values occupy once they are decompressed.
     */
    UNCOMPRESSED_SIZE ("uncompressedSize", "long", "Uncompressed Size", "The number of bytes that the column's values occupy once they are decompressed."),

    /**
     * The location of the column in the file's schema.
     */
    PATH_NAME ("pathName", "string", "Path Name", "The location of the column in the file's schema.  It is the names of the fields from the top-level column down to the column holding the values, separated by dots."),

    /**
     * The type used to store the values.
     */
    PHYSICAL_TYPE ("physicalType", "string", "Physical Type", "The type used to store the values."),

    /**
     * The logical type annotation that describes how to interpret the stored values.
     */
    LOGICAL_TYPE ("logicalType", "string", "Logical Type", "The logical type annotation that describes how to interpret the stored values."),

    /**
     * The original style of annotation that describes how to interpret the stored values.
     */
    CONVERTED_TYPE ("convertedType", "string", "Converted Type", "The original style of annotation that describes how to interpret the stored values."),

    /**
     * Whether the column is required, optional or repeated.
     */
    REPETITION ("repetition", "string", "Repetition", "Whether the column is REQUIRED, OPTIONAL or REPEATED."),

    /**
     * The compression algorithms used for the column's values.
     */
    COMPRESSION_CODECS ("compressionCodecs", "array<string>", "Compression Codecs", "The compression algorithms used for the column's values."),

    /**
     * The encodings used for the column's values.
     */
    ENCODINGS ("encodings", "array<string>", "Encodings", "The encodings used for the column's values."),

    /**
     * The versions of the data page layout used for the column's values.
     */
    DATA_PAGE_VERSIONS ("dataPageVersions", "array<string>", "Data Page Versions", "The versions of the data page layout (V1 and/or V2) used for the column's values."),

    /**
     * Whether the description of the column's values is encrypted.
     */
    ENCRYPTED ("encrypted", "boolean", "Encrypted", "Whether the description of the column's values is encrypted.  If it is, the statistics are not available."),
    ;

    /**
     * Name of the property.
     */
    public final String propertyName;

    /**
     * Type of the property.
     */
    public final String dataType;

    /**
     * Name used for the property in the annotation.
     */
    public final String displayName;

    /**
     * Description of the property.
     */
    public final String description;


    /**
     * Create a specific Enum constant.
     *
     * @param propertyName name of the property used to store the measurement
     * @param dataType data type of property
     * @param displayName name of the request type
     * @param description description of the request type
     */
    ParquetColumnMetric(String propertyName,
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
        return new ArrayList<>(List.of(ParquetColumnMetric.values()));
    }


    /**
     * Output of this enum class and main value.
     *
     * @return string showing enum value
     */
    @Override
    public String toString()
    {
        return "ParquetColumnMetric{" + displayName + "}";
    }
}
