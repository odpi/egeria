/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworks.connectors.tabulardatasets;

import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectorCheckedException;

import java.util.List;

/**
 * WritableTabularDataSource defines the specific connector api for working with
 * simple tabular data.  Each row represents a record
 * that is divided into columns.  Each row has the same list of columns and so the data source is
 * effectively a table of data.
 */
public interface WritableTabularDataSource
{
    /**
     * Return the record count in the data source.
     *
     * @return count
     * @throws ConnectorCheckedException data access problem
     */
    long     getRecordCount() throws ConnectorCheckedException;


    /**
     * Set up the columns associated with this tabular data source.  These may be stored in the data set or kept in
     * memory for this instance.  The names of the columns will be in
     * a canonical format where each word in the name is capitalized with a space between each word.
     * This allows simple translation between the naming conventions supported by different technologies.
     *
     * @param columnDescriptions   a list of column descriptions
     * @throws ConnectorCheckedException data access problem
     */
    void setColumnDescriptions(List<TabularColumnDescription> columnDescriptions) throws ConnectorCheckedException;


    /**
     * Write the requested data record.  The first data record is record 0.
     *
     * @param rowNumber long
     * @param dataValues  Map of column descriptions to strings, each string is the value for the column.
     * @throws ConnectorCheckedException a problem occurred accessing the data.
     */
    void writeRecord(long          rowNumber,
                     List<String>  dataValues) throws ConnectorCheckedException;


    /**
     * Write the requested data record to the end of the data source.
     *
     * @param dataValues  Map of column descriptions to strings, each string is the value for the column.
     * @throws ConnectorCheckedException a problem occurred accessing the data.
     */
    void appendRecord(List<String>  dataValues) throws ConnectorCheckedException;


    /**
     * Remove the requested data record.  The first data record is record 0.
     *
     * @param rowNumber long
     * @throws ConnectorCheckedException a problem occurred accessing the data.
     */
    void deleteRecord(long rowNumber) throws ConnectorCheckedException;
}
