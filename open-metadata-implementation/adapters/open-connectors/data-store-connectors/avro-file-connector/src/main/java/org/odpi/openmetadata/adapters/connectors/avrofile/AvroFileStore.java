/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.avrofile;

import org.odpi.openmetadata.adapters.connectors.avrofile.ffdc.exception.FileException;

import java.io.File;

/**
 * AvroFileStore defines the interface to access a file that supports the Apache Avro format.
 * The Apache Avro API is built around the Java File API.  This connector returns the File
 * object for the Avro file so it can be accessed.
 */
public interface AvroFileStore
{
    /**
     * Return the Java File object that provides access to the Avro file.
     *
     * @return File object
     * @throws FileException unable to locate the file
     */
    File  getAvroFile() throws FileException;
}
