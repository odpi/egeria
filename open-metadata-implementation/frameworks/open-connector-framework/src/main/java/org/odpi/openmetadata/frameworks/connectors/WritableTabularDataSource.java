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
public interface WritableTabularDataSource
{
    /**
     * Return the number of records in the data source.
     *
     * @return count
     * @throws ConnectorCheckedException there is a problem accessing the data
     */
    long     getRecordCount() throws ConnectorCheckedException;


    /**
     * Return the list of column descriptions associated with this data source.  The information
     * should be sufficient to define the schema in a target data store.
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

    /**
     * Remove the requested data record.  The first data record is record 0.
     *
     * @param rowNumber long
     * @throws ConnectorCheckedException there is a problem accessing the data.
     */
    void deleteRecord(long rowNumber) throws ConnectorCheckedException;
}
