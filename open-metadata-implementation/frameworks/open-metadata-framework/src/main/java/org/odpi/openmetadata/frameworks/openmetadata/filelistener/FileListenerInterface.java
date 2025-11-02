/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.openmetadata.filelistener;


import java.io.File;

/**
 * FileListenerInterface is the interface to implement is your connector wishes to monitor
 * the changing files within a root directory.  The listener is registered with the connector's context object
 * that is passed to the connector just before start().
 */
public interface FileListenerInterface
{
    /**
     * File created Event.
     *
     * @param file The file that was created
     */
    void onFileCreate(final File file);


    /**
     * File changed Event.
     *
     * @param file The file that changed
     */
    void onFileChange(final File file);


    /**
     * File deleted Event.
     *
     * @param file The file that was deleted
     */
    void onFileDelete(final File file);
}
