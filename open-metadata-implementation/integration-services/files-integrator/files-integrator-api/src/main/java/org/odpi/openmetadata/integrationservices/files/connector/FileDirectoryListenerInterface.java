/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.integrationservices.files.connector;

import java.io.File;

/**
 * FileDirectoryListenerInterface is the interface to implement is your integration connector wishes to monitor
 * the changing files (and directories) within a root directory.  The listener is registered with the FilesIntegratorContext
 * that is passed to the connector just before start().
 */
public interface FileDirectoryListenerInterface extends FileListenerInterface
{
    /**
     * Directory created Event.
     *
     * @param directory The directory that was created
     */
    void onDirectoryCreate(final File directory);


    /**
     * Directory changed Event.
     *
     * @param directory The directory that changed
     */
    void onDirectoryChange(final File directory);


    /**
     * Directory deleted Event.
     *
     * @param directory The directory that was deleted
     */
    void onDirectoryDelete(final File directory);

}
