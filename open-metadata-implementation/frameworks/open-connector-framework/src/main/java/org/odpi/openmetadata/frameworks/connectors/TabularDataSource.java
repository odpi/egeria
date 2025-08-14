/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworks.connectors;

import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectorCheckedException;

import java.util.List;

/**
 * TabularDataSource defines the specific connector api for working with
 * simple tabular data.  Each row represents a record
 * that is divided into columns.  Each row has the same list of columns and so the data source is
 * effectively a table of data.
 */
public interface TabularDataSource
{
    /**
     * Return the number of records in the data source.
     *
     * @return count
     * @throws ConnectorCheckedException there is a problem accessing the data
     */
    long     getRecordCount() throws ConnectorCheckedException;


    /**
     * Return the list of column names associated with this data source.
     *
     * @return a list of column descriptions or null if not available.
     * @throws ConnectorCheckedException there is a problem accessing the data
     */
    List<TabularColumnDescription> getColumnDescriptions() throws ConnectorCheckedException;


    /**
     * Set up the columns associated with this tabular data source.  These may be stored in the data set or kept in
     * memory for this instance.
     *
     * @param columnDescriptions   a list of column descriptions
     * @throws ConnectorCheckedException there is a problem accessing the data
     */
    void setColumnDescriptions(List<TabularColumnDescription> columnDescriptions) throws ConnectorCheckedException;


    /**
     * Return the requested data record.  The first record is record 0.
     *
     * @param rowNumber long
     * @return list of  values (as strings) where each string is the value from a column.  The order is the same as the columns
     * @throws ConnectorCheckedException there is a problem accessing the data.
     */
    List<String> readRecord(long rowNumber) throws ConnectorCheckedException;


    /**
     * Write the requested data record.  The first data record is record 0.
     *
     * @param rowNumber long
     * @param dataValues  Map of column descriptions to strings, each string is the value for the column.
     * @throws ConnectorCheckedException there is a problem accessing the data.
     */
    void writeRecord(long          rowNumber,
                     List<String>  dataValues) throws ConnectorCheckedException;


    /**
     * Write the requested data record to the end of the data source.
     *
     * @param dataValues  Map of column descriptions to strings, each string is the value for the column.
     * @throws ConnectorCheckedException there is a problem accessing the data.
     */
    void appendRecord(List<String>  dataValues) throws ConnectorCheckedException;


}
