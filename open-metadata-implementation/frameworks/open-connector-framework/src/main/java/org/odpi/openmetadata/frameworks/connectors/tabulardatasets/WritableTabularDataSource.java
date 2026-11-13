/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworks.connectors.tabulardatasets;

import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectorCheckedException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

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


    /**
     * Write several data records, each at its row number.  A data source that can write many records more
     * cheaply together than one at a time - in one transaction, say - overrides this; by default each record is
     * written with {@link #writeRecord}, in the map's order.
     *
     * @param dataRecords map of row number to the values of the record to write there
     * @throws ConnectorCheckedException a problem occurred accessing the data.
     */
    default void writeRecords(Map<Long, List<String>> dataRecords) throws ConnectorCheckedException
    {
        if (dataRecords != null)
        {
            for (Map.Entry<Long, List<String>> dataRecord : dataRecords.entrySet())
            {
                this.writeRecord(dataRecord.getKey(), dataRecord.getValue());
            }
        }
    }


    /**
     * Write several data records to the end of the data source, in the order supplied.  By default each record
     * is written with {@link #appendRecord}.
     *
     * @param dataRecords values of each record to append
     * @throws ConnectorCheckedException a problem occurred accessing the data.
     */
    default void appendRecords(List<List<String>> dataRecords) throws ConnectorCheckedException
    {
        if (dataRecords != null)
        {
            for (List<String> dataValues : dataRecords)
            {
                this.appendRecord(dataValues);
            }
        }
    }


    /**
     * Remove several data records.  Each row number is the record's position before any of them is removed.
     * By default they are removed with {@link #deleteRecord} from the highest row number down, so that each
     * removal leaves the positions of those still to go unchanged.
     *
     * @param rowNumbers positions of the records to remove; the same position listed twice is removed once
     * @throws ConnectorCheckedException a problem occurred accessing the data.
     */
    default void deleteRecords(List<Long> rowNumbers) throws ConnectorCheckedException
    {
        if (rowNumbers != null)
        {
            List<Long> sortedRowNumbers = new ArrayList<>(rowNumbers);

            sortedRowNumbers.sort(Collections.reverseOrder());

            Long previousRowNumber = null;

            for (Long rowNumber : sortedRowNumbers)
            {
                if ((rowNumber != null) && (! rowNumber.equals(previousRowNumber)))
                {
                    this.deleteRecord(rowNumber);
                }

                previousRowNumber = rowNumber;
            }
        }
    }
}
