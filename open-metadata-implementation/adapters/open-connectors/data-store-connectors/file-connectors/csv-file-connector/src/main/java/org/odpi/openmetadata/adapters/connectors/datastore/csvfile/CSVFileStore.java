/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adapters.connectors.datastore.csvfile;

import org.odpi.openmetadata.adapters.connectors.datastore.basicfile.BasicFileStore;
import org.odpi.openmetadata.adapters.connectors.datastore.basicfile.ffdc.exception.FileException;
import org.odpi.openmetadata.adapters.connectors.datastore.basicfile.ffdc.exception.FileReadException;

import java.util.List;

/**
 * CSVFileStore defines the specific connector api for working with
 * CSV files.  A CSV file is one where each row represents a record
 * that is divided into attributes that are separated by a special character (usually
 * a space).  Each row has the same list of attributes and so the file is
 * effectively a table of data.
 */
public interface CSVFileStore extends BasicFileStore
{
    /**
     * Return the number of records in the file.  This is achieved by scanning the file and counting the records -
     * not recommended for very large files.
     *
     * @return count
     * @throws FileException there is a problem accessing the file
     * @throws FileReadException unable to find, open or scan the file.
     */
    long     getRecordCount() throws FileException, FileReadException;


    /**
     * Return the list of column names associated with this structured file.
     * This may be embedded in the first line of the file or encoded in the
     * connection object used to create a connector instance.
     *
     * @return a list of column names
     * @throws FileException there is a problem accessing the file
     * @throws FileReadException unable to retrieve the column names
     */
    List<String>      getColumnNames() throws FileException, FileReadException;


    /**
     * Return the requested data record.  The first record is record 0.  If the first line of the file is the column
     * names then record 0 is the line following the column names.
     *
     * @param rowNumber long
     * @return List of strings, each string is the value from the column.
     * @throws FileException there is a problem accessing the file
     * @throws FileReadException unable to find, open or read the file, or the file does not include the requested record.
     */
    List<String>      readRecord(long  rowNumber) throws FileException, FileReadException;
}
