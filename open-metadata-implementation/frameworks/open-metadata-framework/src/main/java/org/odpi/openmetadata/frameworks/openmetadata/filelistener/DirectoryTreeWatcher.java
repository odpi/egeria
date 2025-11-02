/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.openmetadata.filelistener;

import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.OMFAuditCode;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.OMFErrorCode;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;


/**
 * DirectoryTreeWatcher uses the Java WatchService capabilities to support the ability for an Integration Connector to
 * monitor changes in the file system.
 */
public class DirectoryTreeWatcher extends DirectoryWatcher
{
    private static final Logger log = LoggerFactory.getLogger(DirectoryTreeWatcher.class);


    /**
     * Constructor.
     *
     * @param auditLog logging destination
     * @param connectorName name of the connector
     */
    public DirectoryTreeWatcher(FileDirectoryListenerInterface listener,
                                File                           directoryToMonitor,
                                FileFilter                     fileFilter,
                                AuditLog                       auditLog,
                                String                         connectorName) throws InvalidParameterException
    {
        super(listener, fileFilter, auditLog, connectorName);

        final String methodName = "DirectoryTreeWatcher";

        try
        {
            registerAllDirectories(Path.of(directoryToMonitor.getPath()));
        }
        catch (Exception error)
        {
            if (auditLog != null)
            {
                auditLog.logException(methodName,
                                      OMFAuditCode.UNEXPECTED_FILE_MONITORING_EXCEPTION.getMessageDefinition(error.getClass().getName(),
                                                                                                             connectorName,
                                                                                                             error.getMessage()),
                                      error);
            }

            throw new InvalidParameterException(OMFErrorCode.UNEXPECTED_EXCEPTION.getMessageDefinition(error.getClass().getName(),
                                                                                                       connectorName,
                                                                                                       methodName,
                                                                                                       error.getMessage()),
                                                this.getClass().getName(),
                                                methodName,
                                                error,
                                                OpenMetadataProperty.PATH_NAME.name);
        }
    }


    /**
     * Walk the directory structure, registering all of the directories.
     *
     * @param directoryToMonitor root directory
     * @throws IOException problem accessing a file
     */
    private void registerAllDirectories(Path directoryToMonitor) throws IOException
    {
        /*
         * Does not follow symbolic links.
         */
        Files.walkFileTree(directoryToMonitor, new SimpleFileVisitor<>()
        {
            /**
             * Called for each directory.
             *
             * @param directory a reference to the directory
             * @param attrs  the directory's basic attributes
             *
             * @return continue
             * @throws IOException problem accessing file
             */
            @Override
            public FileVisitResult preVisitDirectory(Path                directory,
                                                     BasicFileAttributes attrs) throws IOException
            {
                final String methodName = "preVisitDirectory";

                registerDirectory(directory, methodName);
                log.debug("Registering directory " + directory);

                /*
                 * Read the contents of this directory.
                 */
                return FileVisitResult.CONTINUE;
            }
        });
    }
}
