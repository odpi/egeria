/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.datastore.parquetfile.properties;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * ParquetFileMetadata describes the contents of a Parquet file using the information that the writer stored
 * in the file's footer.
 */
public class ParquetFileMetadata
{
    /**
     * The file only uses the original (version 1) style of annotation to describe how values are interpreted.
     */
    public static final String CONVERTED_TYPE_ANNOTATIONS = "ConvertedType";

    /**
     * The file uses the logical type annotations that were introduced in version 2 of the format.
     */
    public static final String LOGICAL_TYPE_ANNOTATIONS   = "LogicalType";

    /**
     * The file's columns are described only by the type they are stored as.
     */
    public static final String NO_ANNOTATIONS             = "None";

    private long                             fileLength          = 0L;
    private int                              footerLength        = 0;
    private int                              formatVersion       = 0;
    private String                           createdBy           = null;
    private long                             rowCount            = 0L;
    private String                           encryptionAlgorithm = null;
    private ParquetSchemaElement             rootSchemaElement   = null;
    private final Map<String, String>        keyValueMetadata    = new LinkedHashMap<>();
    private final List<ParquetRowGroup>      rowGroups           = new ArrayList<>();
    private final List<ParquetColumnProfile> columnProfiles      = new ArrayList<>();


    /**
     * Default constructor.
     */
    public ParquetFileMetadata()
    {
    }


    /**
     * Return the size of the file.
     *
     * @return number of bytes
     */
    public long getFileLength()
    {
        return fileLength;
    }


    /**
     * Set up the size of the file.
     *
     * @param fileLength number of bytes
     */
    public void setFileLength(long fileLength)
    {
        this.fileLength = fileLength;
    }


    /**
     * Return the size of the footer.
     *
     * @return number of bytes
     */
    public int getFooterLength()
    {
        return footerLength;
    }


    /**
     * Set up the size of the footer.
     *
     * @param footerLength number of bytes
     */
    public void setFooterLength(int footerLength)
    {
        this.footerLength = footerLength;
    }


    /**
     * Return the version of the Parquet format that the writer declared in the footer.  This is 1 or 2.
     * It is not a reliable guide to the features used in the file since many writers always set it to 1.
     * See also getDataPageVersions(), getEncodings() and getTypeAnnotationStyle().
     *
     * @return version number
     */
    public int getFormatVersion()
    {
        return formatVersion;
    }


    /**
     * Set up the version of the Parquet format that the writer declared in the footer.
     *
     * @param formatVersion version number
     */
    public void setFormatVersion(int formatVersion)
    {
        this.formatVersion = formatVersion;
    }


    /**
     * Return the description of the software that wrote the file.
     *
     * @return string or null
     */
    public String getCreatedBy()
    {
        return createdBy;
    }


    /**
     * Set up the description of the software that wrote the file.
     *
     * @param createdBy string or null
     */
    public void setCreatedBy(String createdBy)
    {
        this.createdBy = createdBy;
    }


    /**
     * Return the number of rows in the file.
     *
     * @return count
     */
    public long getRowCount()
    {
        return rowCount;
    }


    /**
     * Set up the number of rows in the file.
     *
     * @param rowCount count
     */
    public void setRowCount(long rowCount)
    {
        this.rowCount = rowCount;
    }


    /**
     * Return the name of the algorithm used to encrypt columns in the file.
     *
     * @return string, or null if the file is not encrypted
     */
    public String getEncryptionAlgorithm()
    {
        return encryptionAlgorithm;
    }


    /**
     * Set up the name of the algorithm used to encrypt columns in the file.
     *
     * @param encryptionAlgorithm string or null
     */
    public void setEncryptionAlgorithm(String encryptionAlgorithm)
    {
        this.encryptionAlgorithm = encryptionAlgorithm;
    }


    /**
     * Return the root of the schema.  This is a group that contains the top-level columns.
     *
     * @return schema element
     */
    public ParquetSchemaElement getRootSchemaElement()
    {
        return rootSchemaElement;
    }


    /**
     * Set up the root of the schema.
     *
     * @param rootSchemaElement schema element
     */
    public void setRootSchemaElement(ParquetSchemaElement rootSchemaElement)
    {
        this.rootSchemaElement = rootSchemaElement;
    }


    /**
     * Return the additional properties that the writer added to the file.  These often include the schema
     * used by the writing technology (for example, Apache Spark, Apache Arrow or pandas).
     *
     * @return map of property name to property value
     */
    public Map<String, String> getKeyValueMetadata()
    {
        return keyValueMetadata;
    }


    /**
     * Return the description of each row group.
     *
     * @return list
     */
    public List<ParquetRowGroup> getRowGroups()
    {
        return rowGroups;
    }


    /**
     * Return the summary of the values in each of the leaf columns, in the order that they appear in the schema.
     *
     * @return list
     */
    public List<ParquetColumnProfile> getColumnProfiles()
    {
        return columnProfiles;
    }


    /**
     * Return the top-level columns.  A column may be a group if the file contains nested data.
     *
     * @return list
     */
    public List<ParquetSchemaElement> getColumns()
    {
        if (rootSchemaElement == null)
        {
            return new ArrayList<>();
        }

        return rootSchemaElement.getChildren();
    }


    /**
     * Return the names of the top-level columns.
     *
     * @return list
     */
    public List<String> getColumnNames()
    {
        List<String> columnNames = new ArrayList<>();

        for (ParquetSchemaElement column : this.getColumns())
        {
            columnNames.add(column.getName());
        }

        return columnNames;
    }


    /**
     * Return the number of bytes that the values occupy in the file.
     *
     * @return number of bytes
     */
    public long getCompressedSize()
    {
        long size = 0L;

        for (ParquetColumnProfile columnProfile : columnProfiles)
        {
            size = size + columnProfile.getCompressedSize();
        }

        return size;
    }


    /**
     * Return the number of bytes that the values occupy once they are decompressed.
     *
     * @return number of bytes
     */
    public long getUncompressedSize()
    {
        long size = 0L;

        for (ParquetColumnProfile columnProfile : columnProfiles)
        {
            size = size + columnProfile.getUncompressedSize();
        }

        return size;
    }


    /**
     * Return the names of the compression algorithms used in the file.
     *
     * @return list
     */
    public List<String> getCompressionCodecs()
    {
        List<String> results = new ArrayList<>();

        for (ParquetColumnProfile columnProfile : columnProfiles)
        {
            addMissing(results, columnProfile.getCompressionCodecs());
        }

        return results;
    }


    /**
     * Return the names of the encodings used in the file.
     *
     * @return list
     */
    public List<String> getEncodings()
    {
        List<String> results = new ArrayList<>();

        for (ParquetColumnProfile columnProfile : columnProfiles)
        {
            addMissing(results, columnProfile.getEncodings());
        }

        return results;
    }


    /**
     * Return the versions of the data page layout (V1 and/or V2) used in the file.
     *
     * @return list
     */
    public List<String> getDataPageVersions()
    {
        List<String> results = new ArrayList<>();

        for (ParquetColumnProfile columnProfile : columnProfiles)
        {
            addMissing(results, columnProfile.getDataPageVersions());
        }

        return results;
    }


    /**
     * Return which style of annotation the writer used to describe how the stored values are to be interpreted.
     *
     * @return LogicalType, ConvertedType or None
     */
    public String getTypeAnnotationStyle()
    {
        return getTypeAnnotationStyle(rootSchemaElement, NO_ANNOTATIONS);
    }


    /**
     * Step through the schema looking for annotations.
     *
     * @param schemaElement element to inspect, along with its children
     * @param styleSoFar the style determined from the elements inspected so far
     * @return LogicalType, ConvertedType or None
     */
    private String getTypeAnnotationStyle(ParquetSchemaElement schemaElement,
                                          String               styleSoFar)
    {
        String style = styleSoFar;

        if (schemaElement != null)
        {
            if (schemaElement.getLogicalType() != null)
            {
                return LOGICAL_TYPE_ANNOTATIONS;
            }

            if (schemaElement.getConvertedType() != null)
            {
                style = CONVERTED_TYPE_ANNOTATIONS;
            }

            for (ParquetSchemaElement child : schemaElement.getChildren())
            {
                style = getTypeAnnotationStyle(child, style);

                if (LOGICAL_TYPE_ANNOTATIONS.equals(style))
                {
                    return style;
                }
            }
        }

        return style;
    }


    /**
     * Add the values that are not already in the results.
     *
     * @param results list to update
     * @param values values to add
     */
    private void addMissing(List<String> results,
                            List<String> values)
    {
        for (String value : values)
        {
            if (! results.contains(value))
            {
                results.add(value);
            }
        }
    }
}
