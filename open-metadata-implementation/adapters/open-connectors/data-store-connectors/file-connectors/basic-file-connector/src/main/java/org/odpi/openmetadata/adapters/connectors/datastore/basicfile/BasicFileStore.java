/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.datastore.basicfile;

import org.odpi.openmetadata.adapters.connectors.datastore.basicfile.ffdc.exception.FileException;
import org.odpi.openmetadata.adapters.connectors.datastore.basicfile.ffdc.exception.FileReadException;

import java.io.File;
import java.util.Date;

/**
 * BasicFileStore defines the interface to access a file.
 */
public interface BasicFileStore
{
    /**
     * Return the Java File object that provides access to the file.
     *
     * @return File object
     * @throws FileException unable to locate the file
     */
    File  getFile() throws FileException;


    /**
     * Return the number of bytes in the file
     *
     * @return number
     * @throws FileException unable to locate the file
     */
    long  getFileLength() throws FileException;


    /**
     * Return the name of the file to read.
     *
     * @return file name
     * @throws FileException a problem accessing the file
     * @throws FileReadException - the file name is null, the file does not exist, or is a directory or
     *                             is not readable.
     */
    String   getFileName() throws FileException, FileReadException;


    /**
     * Return the creation date for the file.
     *
     * @return Date object
     * @throws FileException a problem accessing the file
     * @throws FileReadException - the file name is null, the file does not exist, or is a directory or
     *                             is not readable.
     */
    Date getCreationDate() throws FileException, FileReadException;


    /**
     * Return the last update date for the file.
     *
     * @return Date object
     * @throws FileException a problem accessing the file
     * @throws FileReadException - the file name is null, the file does not exist, or is a directory or
     *                             is not readable.
     */
    Date     getLastUpdateDate() throws FileException, FileReadException;


    /**
     * Return the last access date for the file.
     *
     * @return Date object
     * @throws FileException a problem accessing the file
     * @throws FileReadException - the file name is null, the file does not exist, or is a directory or
     *                             is not readable.
     */
    Date     getLastAccessDate() throws FileException, FileReadException;
}
