/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adapters.connectors.surveyaction.controls;

import org.odpi.openmetadata.frameworks.opensurvey.measurements.SurveyMetric;

import java.util.ArrayList;
import java.util.List;

/**
 * ParquetFileMetric describes the measurements that the Parquet survey action service takes from the footer of an
 * Apache Parquet file.
 */
public enum ParquetFileMetric implements SurveyMetric
{
    /**
     * The version of the Parquet format declared by the writer of the file.
     */
    FORMAT_VERSION ("formatVersion", "int", "Format Version", "The version of the Parquet format declared by the writer of the file.  Many writers always set this to 1 and so the encodings, data page versions and type annotation style are a better guide to the features used in the file."),

    /**
     * The description of the software that wrote the file.
     */
    CREATED_BY ("createdBy", "string", "Created By", "The description of the software that wrote the file."),

    /**
     * The number of rows in the file.
     */
    ROW_COUNT ("rowCount", "long", "Row Count", "The number of rows in the file."),

    /**
     * The number of row groups that the rows are divided into.
     */
    ROW_GROUP_COUNT ("rowGroupCount", "int", "Row Group Count", "The number of row groups that the rows are divided into."),

    /**
     * The number of top-level columns in the file's schema.
     */
    COLUMN_COUNT ("columnCount", "int", "Column Count", "The number of top-level columns in the file's schema."),

    /**
     * The number of columns that hold values.
     */
    LEAF_COLUMN_COUNT ("leafColumnCount", "int", "Leaf Column Count", "The number of columns that hold values.  This is greater than the column count when the file contains nested data."),

    /**
     * The number of bytes in the file's footer.
     */
    FOOTER_SIZE ("footerSize", "int", "Footer Size", "The number of bytes in the file's footer."),

    /**
     * The number of bytes that the values occupy in the file.
     */
    COMPRESSED_SIZE ("compressedSize", "long", "Compressed Size", "The number of bytes that the values occupy in the file."),

    /**
     * The number of bytes that the values occupy once they are decompressed.
     */
    UNCOMPRESSED_SIZE ("uncompressedSize", "long", "Uncompressed Size", "The number of bytes that the values occupy once they are decompressed."),

    /**
     * The compression algorithms used in the file.
     */
    COMPRESSION_CODECS ("compressionCodecs", "array<string>", "Compression Codecs", "The compression algorithms used in the file."),

    /**
     * The encodings used for the values in the file.
     */
    ENCODINGS ("encodings", "array<string>", "Encodings", "The encodings used for the values in the file."),

    /**
     * The versions of the data page layout used in the file.
     */
    DATA_PAGE_VERSIONS ("dataPageVersions", "array<string>", "Data Page Versions", "The versions of the data page layout (V1 and/or V2) used in the file."),

    /**
     * The style of annotation used to describe how the stored values are interpreted.
     */
    TYPE_ANNOTATION_STYLE ("typeAnnotationStyle", "string", "Type Annotation Style", "The style of annotation used to describe how the stored values are interpreted: LogicalType (introduced in version 2 of the format), ConvertedType (the original style) or None."),

    /**
     * The algorithm used to encrypt columns in the file.
     */
    ENCRYPTION_ALGORITHM ("encryptionAlgorithm", "string", "Encryption Algorithm", "The algorithm used to encrypt columns in the file.  This is only present when the file uses Parquet modular encryption."),

    /**
     * The names of the additional properties that the writer added to the file.
     */
    WRITER_PROPERTY_NAMES ("writerPropertyNames", "array<string>", "Writer Property Names", "The names of the additional properties that the writer added to the file."),
    ;

    /**
     * Name of the property in the measurement bean.
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
    ParquetFileMetric(String propertyName,
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
        return new ArrayList<>(List.of(ParquetFileMetric.values()));
    }


    /**
     * Output of this enum class and main value.
     *
     * @return string showing enum value
     */
    @Override
    public String toString()
    {
        return "ParquetFileMetric{" + displayName + "}";
    }
}
