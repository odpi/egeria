/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adapters.connectors.datastore.parquetfile;

import org.odpi.openmetadata.adapters.connectors.datastore.basicfile.BasicFileStore;
import org.odpi.openmetadata.adapters.connectors.datastore.basicfile.ffdc.exception.FileException;
import org.odpi.openmetadata.adapters.connectors.datastore.basicfile.ffdc.exception.FileReadException;
import org.odpi.openmetadata.adapters.connectors.datastore.parquetfile.properties.ParquetFileMetadata;

import org.odpi.openmetadata.frameworks.connectors.tabulardatasets.ReadableTabularDataSource;

import java.util.List;
import java.util.Map;

/**
 * ParquetFileStore defines the specific connector api for working with Apache Parquet files.  A Parquet file
 * holds a table of data organized by column.  It ends with a footer that describes the schema of the table,
 * the number of rows, how the values are stored and statistics about the values in each column.
 * This interface provides access to the information in the footer.
 * <br><br>
 * It also provides access to the rows of data in the file, both through its own methods and by extending
 * the standard interface for a tabular data source.  Reading the data relies on the DuckDB JDBC driver being
 * available at runtime - see isDataAccessAvailable().  The methods that return information from the footer do not
 * need it.
 */
public interface ParquetFileStore extends BasicFileStore, ReadableTabularDataSource
{
    /**
     * Return the description of the file's contents from its footer.
     *
     * @return file metadata
     * @throws FileException a problem accessing the file
     * @throws FileReadException unable to read the file, or it is not a Parquet file that the connector can describe
     */
    ParquetFileMetadata getFileMetadata() throws FileException, FileReadException;


    /**
     * Return the number of rows in the file.  This value comes from the footer, so the size of the file makes no
     * difference to how long it takes.
     *
     * @return count
     * @throws FileException a problem accessing the file
     * @throws FileReadException unable to read the file, or it is not a Parquet file that the connector can describe
     */
    @Override
    long getRecordCount() throws FileException, FileReadException;


    /**
     * Return the list of top-level column names in the file.
     *
     * @return a list of column names
     * @throws FileException a problem accessing the file
     * @throws FileReadException unable to read the file, or it is not a Parquet file that the connector can describe
     */
    List<String> getColumnNames() throws FileException, FileReadException;


    /**
     * Return whether the rows of data in the file can be read.  This is true when the DuckDB JDBC driver
     * is on the class path.
     *
     * @return boolean
     */
    boolean isDataAccessAvailable();


    /**
     * Return the requested row of data.  The first row is row 0.
     *
     * @param rowNumber long
     * @return list of strings, each string is the value from a top-level column.  A null value is returned as null.
     *         The value of a column that holds nested data is formatted as a string.
     * @throws FileException a problem accessing the file
     * @throws FileReadException unable to read the data, or the file does not include the requested row.
     */
    @Override
    List<String> readRecord(long rowNumber) throws FileException, FileReadException;


    /**
     * Return how many times each value occurs in a top-level column, with the most frequent value first.
     * Nulls are not included.  This reads all the values in the column.
     *
     * @param columnName the name of a top-level column
     * @param maxDistinctValues the largest number of different values to return
     * @return map of value to the number of times it occurs, or null if the column has more
     *         than the maximum number of different values
     * @throws FileException a problem accessing the file
     * @throws FileReadException unable to read the data
     */
    Map<String, Long> getValueCounts(String columnName,
                                     int    maxDistinctValues) throws FileException, FileReadException;
}
