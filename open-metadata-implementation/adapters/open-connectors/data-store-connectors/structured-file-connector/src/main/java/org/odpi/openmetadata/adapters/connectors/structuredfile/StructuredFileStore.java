/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adapters.connectors.structuredfile;

import org.odpi.openmetadata.adapters.connectors.structuredfile.ffdc.exception.FileReadException;

import java.util.Date;
import java.util.List;

/**
 * StructuredFileStore defines the specific connector api for working with
 * structured files.  A structured file is one where each row represents a record
 * that is divided into attributes that are separated by a special character (usually
 * a space).  Each row has the same list of attributes and so the file is
 * effectively a table of data.
 */
public interface StructuredFileStore
{
    /**
     * Return the name of the file to read.
     *
     * @return file name
     * @throws FileReadException - the file name is null, the file does not exist, or is a directory or
     *                             is not readable.
     */
    String   getFileName() throws FileReadException;


    /**
     * Return the last update data for the file.
     *
     * @return Date object
     * @throws FileReadException - the file name is null, the file does not exist, or is a directory or
     *                             is not readable.
     */
    Date     getLastUpdateDate() throws FileReadException;


    /**
     * Return the number of records in the file.  This is achieved by scanning the file and counting the records -
     * not recommended for very large files.
     *
     * @return count
     * @throws FileReadException unable to find, open or scan the file.
     */
    long     getRecordCount() throws FileReadException;


    /**
     * Return the list of column names associated with this structured file.
     * This may be embedded in the first line of the file or encoded in the
     * connection object used to create a connector instance.
     *
     * @return a list of column names
     * @throws FileReadException unable to retrieve the column names
     */
    List<String>      getColumnNames() throws FileReadException;


    /**
     * Return the requested data record.  The first record is record 0.  If the first line of the file is the column
     * names then record 0 is the line following the column names.
     *
     * @param rowNumber long
     * @return List of strings, each string is the value from the column.
     * @throws FileReadException unable to find, open or read the file, or the file does not include the requested record.
     */
    List<String>      readRecord(int  rowNumber) throws FileReadException;
}
