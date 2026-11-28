/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.datastore.parquetfile.properties;

import java.util.ArrayList;
import java.util.List;

/**
 * ParquetColumnProfile summarizes the values stored in a column across all the row groups in the file.
 * It is built from the statistics that the writer saved in the file's footer and so it is available
 * without reading (or being able to decompress and decode) the values themselves.
 */
public class ParquetColumnProfile
{
    private final ParquetSchemaElement column;
    private final List<String>         compressionCodecs = new ArrayList<>();
    private final List<String>         encodings         = new ArrayList<>();
    private final List<String>         dataPageVersions  = new ArrayList<>();
    private long                       valueCount        = 0L;
    private long                       compressedSize    = 0L;
    private long                       uncompressedSize  = 0L;
    private Long                       nullCount         = null;
    private Long                       distinctCount     = null;
    private String                     minValue          = null;
    private String                     maxValue          = null;
    private boolean                    encrypted         = false;


    /**
     * Constructor.
     *
     * @param column description of the column from the schema
     */
    public ParquetColumnProfile(ParquetSchemaElement column)
    {
        this.column = column;
    }


    /**
     * Return the description of the column from the schema.
     *
     * @return schema element
     */
    public ParquetSchemaElement getColumn()
    {
        return column;
    }


    /**
     * Return the names of the algorithms used to compress the values.
     *
     * @return list
     */
    public List<String> getCompressionCodecs()
    {
        return compressionCodecs;
    }


    /**
     * Return the names of the encodings used for the values.
     *
     * @return list
     */
    public List<String> getEncodings()
    {
        return encodings;
    }


    /**
     * Return the versions of the data page layout (V1 and/or V2) used to store the values.
     *
     * @return list
     */
    public List<String> getDataPageVersions()
    {
        return dataPageVersions;
    }


    /**
     * Return the number of values stored, including nulls.  For a column that is not nested in a list or map this
     * is the same as the number of rows.
     *
     * @return count
     */
    public long getValueCount()
    {
        return valueCount;
    }


    /**
     * Set up the number of values stored, including nulls.
     *
     * @param valueCount count
     */
    public void setValueCount(long valueCount)
    {
        this.valueCount = valueCount;
    }


    /**
     * Return the number of bytes that the values occupy in the file.
     *
     * @return number of bytes
     */
    public long getCompressedSize()
    {
        return compressedSize;
    }


    /**
     * Set up the number of bytes that the values occupy in the file.
     *
     * @param compressedSize number of bytes
     */
    public void setCompressedSize(long compressedSize)
    {
        this.compressedSize = compressedSize;
    }


    /**
     * Return the number of bytes that the values occupy once they are decompressed.
     *
     * @return number of bytes
     */
    public long getUncompressedSize()
    {
        return uncompressedSize;
    }


    /**
     * Set up the number of bytes that the values occupy once they are decompressed.
     *
     * @param uncompressedSize number of bytes
     */
    public void setUncompressedSize(long uncompressedSize)
    {
        this.uncompressedSize = uncompressedSize;
    }


    /**
     * Return the number of null values.
     *
     * @return count, or null if it is not recorded for every row group
     */
    public Long getNullCount()
    {
        return nullCount;
    }


    /**
     * Set up the number of null values.
     *
     * @param nullCount count or null
     */
    public void setNullCount(Long nullCount)
    {
        this.nullCount = nullCount;
    }


    /**
     * Return the number of distinct values.  This is only known when the file has a single row group, and
     * the writer recorded it, since the counts from different row groups cannot be combined.
     *
     * @return count or null
     */
    public Long getDistinctCount()
    {
        return distinctCount;
    }


    /**
     * Set up the number of distinct values.
     *
     * @param distinctCount count or null
     */
    public void setDistinctCount(Long distinctCount)
    {
        this.distinctCount = distinctCount;
    }


    /**
     * Return the smallest value in the column, formatted as a string.
     *
     * @return string, or null if it is not recorded for every row group
     */
    public String getMinValue()
    {
        return minValue;
    }


    /**
     * Set up the smallest value in the column.
     *
     * @param minValue string or null
     */
    public void setMinValue(String minValue)
    {
        this.minValue = minValue;
    }


    /**
     * Return the largest value in the column, formatted as a string.
     *
     * @return string, or null if it is not recorded for every row group
     */
    public String getMaxValue()
    {
        return maxValue;
    }


    /**
     * Set up the largest value in the column.
     *
     * @param maxValue string or null
     */
    public void setMaxValue(String maxValue)
    {
        this.maxValue = maxValue;
    }


    /**
     * Return whether the description of the column's values is encrypted in any of the row groups.
     * If it is, then the statistics are not available.
     *
     * @return boolean
     */
    public boolean isEncrypted()
    {
        return encrypted;
    }


    /**
     * Set up whether the description of the column's values is encrypted.
     *
     * @param encrypted boolean
     */
    public void setEncrypted(boolean encrypted)
    {
        this.encrypted = encrypted;
    }
}
