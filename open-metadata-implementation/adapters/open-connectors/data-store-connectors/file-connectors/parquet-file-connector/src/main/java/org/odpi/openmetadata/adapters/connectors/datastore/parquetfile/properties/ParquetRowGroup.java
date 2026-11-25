/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.datastore.parquetfile.properties;

import java.util.ArrayList;
import java.util.List;

/**
 * ParquetRowGroup describes a row group in a Parquet file.  A row group is a horizontal slice of the data
 * that holds the values of every column for a set of rows.
 */
public class ParquetRowGroup
{
    private long                           rowCount         = 0L;
    private long                           uncompressedSize = 0L;
    private long                           compressedSize   = 0L;
    private final List<ParquetColumnChunk> columnChunks     = new ArrayList<>();


    /**
     * Default constructor.
     */
    public ParquetRowGroup()
    {
    }


    /**
     * Return the number of rows in the row group.
     *
     * @return count
     */
    public long getRowCount()
    {
        return rowCount;
    }


    /**
     * Set up the number of rows in the row group.
     *
     * @param rowCount count
     */
    public void setRowCount(long rowCount)
    {
        this.rowCount = rowCount;
    }


    /**
     * Return the number of bytes that the row group's values occupy once they are decompressed.
     *
     * @return number of bytes
     */
    public long getUncompressedSize()
    {
        return uncompressedSize;
    }


    /**
     * Set up the number of bytes that the row group's values occupy once they are decompressed.
     *
     * @param uncompressedSize number of bytes
     */
    public void setUncompressedSize(long uncompressedSize)
    {
        this.uncompressedSize = uncompressedSize;
    }


    /**
     * Return the number of bytes that the row group's values occupy in the file.
     *
     * @return number of bytes
     */
    public long getCompressedSize()
    {
        return compressedSize;
    }


    /**
     * Set up the number of bytes that the row group's values occupy in the file.
     *
     * @param compressedSize number of bytes
     */
    public void setCompressedSize(long compressedSize)
    {
        this.compressedSize = compressedSize;
    }


    /**
     * Return the description of the values stored for each column.
     *
     * @return list
     */
    public List<ParquetColumnChunk> getColumnChunks()
    {
        return columnChunks;
    }
}
