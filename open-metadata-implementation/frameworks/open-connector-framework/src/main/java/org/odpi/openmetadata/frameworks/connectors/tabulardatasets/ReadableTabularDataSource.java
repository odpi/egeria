/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworks.connectors.tabulardatasets;

import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectorCheckedException;

import java.util.List;

/**
 * ReadableTabularDataSource defines the specific connector api for reading
 * simple tabular data.  Each row represents a record
 * that is divided into columns.  Each row has the same list of columns and so the data source is
 * effectively a table of data.
 */
public interface ReadableTabularDataSource
{
    /**
     * Return the number of records in the data source.
     *
     * @return count
     * @throws ConnectorCheckedException there is a problem accessing the data
     */
    long getRecordCount() throws ConnectorCheckedException;


    /**
     * Return the table name for this data source.  This is in canonical word format where each word in the name
     * should be capitalized, with spaces between the words.
     * This format allows easy translation between different naming conventions.
     *
     * @return string
     * @throws ConnectorCheckedException there is a problem accessing the data
     */
    String getTableName() throws ConnectorCheckedException;


    /**
     * Return the description for this data source.
     *
     * @return string
     * @throws ConnectorCheckedException there is a problem accessing the data
     */
    String getTableDescription() throws ConnectorCheckedException;


    /**
     * Return the list of column descriptions associated with this data source.  The information
     * should be sufficient to define the schema in a target data store.  The names of the columns should be in
     * a canonical name format where each word in the name is capitalized with a space between each word.
     * This allows simple translation between the naming conventions supported by different technologies.
     *
     * @return a list of column descriptions or null if not available.
     * @throws ConnectorCheckedException there is a problem accessing the data
     */
    List<TabularColumnDescription> getColumnDescriptions() throws ConnectorCheckedException;


    /**
     * Locate the named column. A negative number means the column is not present.
     *
     * @param columnName name of the column to return
     * @return column
     * @throws ConnectorCheckedException problem extracting the column descriptions
     */
    int getColumnNumber(String columnName) throws ConnectorCheckedException;


    /**
     * Return the requested data record.  The first record is record 0.
     *
     * @param rowNumber long
     * @return list of  values (as strings) where each string is the value from a column.  The order is the same as the columns
     * @throws ConnectorCheckedException there is a problem accessing the data.
     */
    List<String> readRecord(long rowNumber) throws ConnectorCheckedException;
}
