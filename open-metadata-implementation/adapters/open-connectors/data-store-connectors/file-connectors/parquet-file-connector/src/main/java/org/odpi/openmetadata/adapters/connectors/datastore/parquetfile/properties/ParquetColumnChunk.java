/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.datastore.parquetfile.properties;

import java.util.ArrayList;
import java.util.List;

/**
 * ParquetColumnChunk describes the values stored for one column in one row group.
 */
public class ParquetColumnChunk
{
    private String       pathName              = null;
    private String       compressionCodec      = null;
    private List<String> encodings             = new ArrayList<>();
    private List<String> dataPageVersions      = new ArrayList<>();
    private long         valueCount            = 0L;
    private long         compressedSize        = 0L;
    private long         uncompressedSize      = 0L;
    private long         dataPageOffset        = 0L;
    private boolean      dictionaryPagePresent = false;
    private boolean      bloomFilterPresent    = false;
    private boolean      encrypted             = false;
    private Long         nullCount             = null;
    private Long         distinctCount         = null;
    private Object       minValue              = null;
    private Object       maxValue              = null;


    /**
     * Default constructor.
     */
    public ParquetColumnChunk()
    {
    }


    /**
     * Return the dot-separated path name of the column in the schema.
     *
     * @return string
     */
    public String getPathName()
    {
        return pathName;
    }


    /**
     * Set up the dot-separated path name of the column in the schema.
     *
     * @param pathName string
     */
    public void setPathName(String pathName)
    {
        this.pathName = pathName;
    }


    /**
     * Return the name of the algorithm used to compress the values (UNCOMPRESSED, SNAPPY, GZIP, ZSTD ...).
     *
     * @return string
     */
    public String getCompressionCodec()
    {
        return compressionCodec;
    }


    /**
     * Set up the name of the algorithm used to compress the values.
     *
     * @param compressionCodec string
     */
    public void setCompressionCodec(String compressionCodec)
    {
        this.compressionCodec = compressionCodec;
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
     * Set up the names of the encodings used for the values.
     *
     * @param encodings list
     */
    public void setEncodings(List<String> encodings)
    {
        this.encodings = encodings;
    }


    /**
     * Return the versions of the data page layout (V1 and/or V2) used to store the values.  The list is empty if the
     * file does not say and the pages were not inspected.
     *
     * @return list
     */
    public List<String> getDataPageVersions()
    {
        return dataPageVersions;
    }


    /**
     * Set up the versions of the data page layout used to store the values.
     *
     * @param dataPageVersions list
     */
    public void setDataPageVersions(List<String> dataPageVersions)
    {
        this.dataPageVersions = dataPageVersions;
    }


    /**
     * Return the number of values stored, including nulls.
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
     * Return the location of the first data page in the file.
     *
     * @return offset from the start of the file
     */
    public long getDataPageOffset()
    {
        return dataPageOffset;
    }


    /**
     * Set up the location of the first data page in the file.
     *
     * @param dataPageOffset offset from the start of the file
     */
    public void setDataPageOffset(long dataPageOffset)
    {
        this.dataPageOffset = dataPageOffset;
    }


    /**
     * Return whether the values are encoded using a dictionary.
     *
     * @return boolean
     */
    public boolean isDictionaryPagePresent()
    {
        return dictionaryPagePresent;
    }


    /**
     * Set up whether the values are encoded using a dictionary.
     *
     * @param dictionaryPagePresent boolean
     */
    public void setDictionaryPagePresent(boolean dictionaryPagePresent)
    {
        this.dictionaryPagePresent = dictionaryPagePresent;
    }


    /**
     * Return whether there is a bloom filter for the values.
     *
     * @return boolean
     */
    public boolean isBloomFilterPresent()
    {
        return bloomFilterPresent;
    }


    /**
     * Set up whether there is a bloom filter for the values.
     *
     * @param bloomFilterPresent boolean
     */
    public void setBloomFilterPresent(boolean bloomFilterPresent)
    {
        this.bloomFilterPresent = bloomFilterPresent;
    }


    /**
     * Return whether the description of the values is encrypted.  If it is then only the path name is known.
     *
     * @return boolean
     */
    public boolean isEncrypted()
    {
        return encrypted;
    }


    /**
     * Set up whether the description of the values is encrypted.
     *
     * @param encrypted boolean
     */
    public void setEncrypted(boolean encrypted)
    {
        this.encrypted = encrypted;
    }


    /**
     * Return the number of null values.
     *
     * @return count, or null if the writer did not record it
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
     * Return the number of distinct values.
     *
     * @return count, or null if the writer did not record it
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
     * Return the smallest value.  The class of the object depends on the type of the column
     * (see ParquetStatisticsDecoder).
     *
     * @return value, or null if the writer did not record it, or it is not reliable
     */
    public Object getMinValue()
    {
        return minValue;
    }


    /**
     * Set up the smallest value.
     *
     * @param minValue value or null
     */
    public void setMinValue(Object minValue)
    {
        this.minValue = minValue;
    }


    /**
     * Return the largest value.  The class of the object depends on the type of the column
     * (see ParquetStatisticsDecoder).
     *
     * @return value, or null if the writer did not record it, or it is not reliable
     */
    public Object getMaxValue()
    {
        return maxValue;
    }


    /**
     * Set up the largest value.
     *
     * @param maxValue value or null
     */
    public void setMaxValue(Object maxValue)
    {
        this.maxValue = maxValue;
    }
}
