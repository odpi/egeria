/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.datastore.basicfile;

import org.odpi.openmetadata.adapters.connectors.datastore.basicfile.ffdc.exception.FileException;

import java.io.File;

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
}
