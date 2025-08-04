/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.openmetadata.filelistener;

import org.apache.commons.io.monitor.FileAlterationListener;
import org.apache.commons.io.monitor.FileAlterationListenerAdaptor;
import org.apache.commons.io.monitor.FileAlterationMonitor;
import org.apache.commons.io.monitor.FileAlterationObserver;
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


/**
 * FilesListenerManager uses the Apache Commons IO capabilities to support the ability for an Integration Connector to
 * monitor changes in the file system.  The implementation is a thin wrapper around the Apache Commons IO to allow the
 * implementation to be swapped for other types of file system monitors.
 */
public class FilesListenerManager
{
    private static final Logger log = LoggerFactory.getLogger(FilesListenerManager.class);
    private static final int POLL_INTERVAL = 500; // milliseconds

    private final AuditLog auditLog;
    private final String   connectorName;

    private final FileAlterationMonitor  monitor  = new FileAlterationMonitor(POLL_INTERVAL);


    /**
     * Constructor.
     *
     * @param auditLog logging destination
     * @param connectorName name of the connector
     */
    public FilesListenerManager(AuditLog auditLog, String connectorName)
    {
        final String methodName = "FilesListenerManager";

        this.auditLog = auditLog;
        this.connectorName = connectorName;

        try
        {
            monitor.start();
        }
        catch (Exception error)
        {
            if (auditLog != null)
            {
                auditLog.logException(methodName,
                                      OMFAuditCode.UNEXPECTED_EXC_MONITOR_START.getMessageDefinition(error.getClass().getName(),
                                                                                                     connectorName,
                                                                                                     error.getMessage()),
                                      error);
            }
        }
    }


    /**
     * Register a listener object that will be called each time a specific file is created, changed or deleted.
     *
     * @param listener      listener object
     * @param fileToMonitor name of the file to monitor
     *
     * @throws InvalidParameterException  one of the parameters is null or invalid.
    */
    public void registerFileListener(FileListenerInterface listener,
                                     File                  fileToMonitor) throws InvalidParameterException
    {
        final String methodName = "registerFileListener";
        final String listenerParameterName = "listener";
        final String fileParameterName = "fileToMonitor";

        validateParameter(listener, listenerParameterName, methodName);
        validateParameter(fileToMonitor, fileParameterName, methodName);

        try
        {
            FileAlterationObserver observer        = new FileAlterationObserver(fileToMonitor.getCanonicalPath());
            FileAlterationListener managedListener = new FileMonitoringListener(listener);

            /*
             * The observer watches the specific file
             */
            observer.addListener(managedListener);

            /*
             * The monitor polls the observer at regular intervals.
             */
            monitor.addObserver(observer);


            if (auditLog != null)
            {
                auditLog.logMessage(methodName,
                                    OMFAuditCode.FILE_MONITORING_STARTING.getMessageDefinition(connectorName,
                                                                                               fileToMonitor.getCanonicalPath()));
            }
        }
        catch (IOException exception)
        {
            throw new InvalidParameterException(OMFErrorCode.UNEXPECTED_IO_EXCEPTION.getMessageDefinition(),
                                                this.getClass().getName(),
                                                methodName,
                                                exception,
                                                OpenMetadataProperty.PATH_NAME.name);
        }
    }


    /**
     * Unregister a listener object that will be called each time a specific file is created, changed or deleted.
     *
     * @param listener      listener object
     * @param fileToMonitor name of the file to unregister
     *
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     */
    public void unregisterFileListener(FileListenerInterface listener,
                                       File                  fileToMonitor) throws InvalidParameterException
    {
        // todo add unregister logic
    }


    /**
     * Register a listener object that will be called each time a file is created, changed or deleted in a specific root directory.
     * The file filter lets you request that only certain types of files are returned.
     *
     * @param listener           listener object
     * @param directoryToMonitor details of the file directory to monitor
     * @param fileFilter         a file filter implementation that restricts the files/directories that will be returned to the listener
     *
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     */
    public void registerDirectoryListener(FileDirectoryListenerInterface listener,
                                          File                           directoryToMonitor,
                                          FileFilter                     fileFilter) throws InvalidParameterException
    {
        final String methodName = "registerDirectoryListener";
        final String listenerParameterName = "listener";
        final String directoryParameterName = "directoryToMonitor";

        validateParameter(listener, listenerParameterName, methodName);
        validateParameter(directoryToMonitor, directoryParameterName, methodName);

        try
        {
            FileAlterationObserver observer = new FileAlterationObserver(directoryToMonitor.getCanonicalPath(), fileFilter);
            FileAlterationListener managedListener = new FolderMonitoringListener(listener, false, fileFilter);

            /*
             * The observer watches the specific file
             */
            observer.addListener(managedListener);

            /*
             * The monitor polls the observer at regular intervals.
             */
            monitor.addObserver(observer);

            if (auditLog != null)
            {
                auditLog.logMessage(methodName,
                                    OMFAuditCode.DIRECTORY_MONITORING_STARTING.getMessageDefinition(connectorName,
                                                                                                    directoryToMonitor.getCanonicalPath()));
            }
        }
        catch (IOException exception)
        {
            throw new InvalidParameterException(OMFErrorCode.UNEXPECTED_IO_EXCEPTION.getMessageDefinition(),
                                                this.getClass().getName(),
                                                methodName,
                                                exception,
                                                OpenMetadataProperty.PATH_NAME.name);
        }
    }


    /**
     * Unregister a listener object for the directory.
     *
     * @param listener           listener object
     * @param directoryToMonitor details of the file directory unregister
     *
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     */
    public void unregisterDirectoryListener(FileDirectoryListenerInterface listener,
                                            File                           directoryToMonitor) throws InvalidParameterException
    {
        // todo add unregister logic
    }


    /**
     * Register a listener object that will be called each time a file is created, changed or deleted in a specific root directory
     * and any of its subdirectories.  The file filter lets you request that only certain types of files and/or directories are returned.
     *
     * @param listener           listener object
     * @param directoryToMonitor details of the root file directory to monitor from
     * @param fileFilter         a file filter implementation that restricts the files/directories that will be returned to the listener
     *
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     */
    public void registerDirectoryTreeListener(FileDirectoryListenerInterface listener,
                                              File                           directoryToMonitor,
                                              FileFilter                     fileFilter) throws InvalidParameterException
    {
        final String methodName = "registerDirectoryTreeListener";
        final String listenerParameterName = "listener";
        final String directoryParameterName = "directoryToMonitor";

        validateParameter(listener, listenerParameterName, methodName);
        validateParameter(directoryToMonitor, directoryParameterName, methodName);

        try
        {
            FileAlterationObserver observer = new FileAlterationObserver(directoryToMonitor.getCanonicalPath(), fileFilter);
            FileAlterationListener managedListener = new FolderMonitoringListener(listener, false, fileFilter);

            /*
             * The observer watches the specific file
             */
            observer.addListener(managedListener);

            /*
             * The monitor polls the observer at regular intervals.
             */
            monitor.addObserver(observer);

            // todo add monitoring of subdirectories?

            if (auditLog != null)
            {
                auditLog.logMessage(methodName,
                                    OMFAuditCode.DIRECTORY_MONITORING_STARTING.getMessageDefinition(connectorName,
                                                                                                    directoryToMonitor.getCanonicalPath()));
            }
        }
        catch (IOException exception)
        {
            throw new InvalidParameterException(OMFErrorCode.UNEXPECTED_IO_EXCEPTION.getMessageDefinition(),
                                                this.getClass().getName(),
                                                methodName,
                                                exception,
                                                OpenMetadataProperty.PATH_NAME.name);
        }
    }


    /**
     * Unregister a listener object for the directory.
     *
     * @param listener           listener object
     * @param directoryToMonitor details of the root file directory to unregister
     *
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     */
    public void unregisterDirectoryTreeListener(FileDirectoryListenerInterface listener,
                                                File                           directoryToMonitor) throws InvalidParameterException
    {
        // todo add unregister logic
    }


    /**
     * Shutdown file monitoring
     */
    public void disconnect()
    {
        final String methodName = "disconnect";

        try
        {
            monitor.stop(POLL_INTERVAL * 2);

            if (auditLog != null)
            {
                auditLog.logMessage(methodName, OMFAuditCode.FILE_SYSTEM_MONITORING_STOPPING.getMessageDefinition(connectorName));
            }
        }
        catch (Exception error)
        {
            if (auditLog != null)
            {
                auditLog.logException(methodName,
                                      OMFAuditCode.UNEXPECTED_EXC_MONITOR_STOP.getMessageDefinition(error.getClass().getName(),
                                                                                                    connectorName,
                                                                                                    error.getMessage()),
                                      error);
            }
        }
    }


    /**
     * Inner class for the directory listener logic
     */
    static class FileMonitoringListener extends FileAlterationListenerAdaptor
    {
        private final FileListenerInterface fileListenerInterface;


        /**
         * Constructor provides the listener to call.
         *
         * @param fileListenerInterface listener from connector
         */
        FileMonitoringListener(FileListenerInterface fileListenerInterface)
        {
            this.fileListenerInterface = fileListenerInterface;
        }


        /**
         * Notification that a new file has been created under the directory.
         *
         * @param file The file created (ignored)
         */
        @Override
        public void onFileCreate(File file)
        {
            log.debug("File created: " + file.getName());
            fileListenerInterface.onFileCreate(file);
        }


        /**
         * Notification that a file has been deleted.
         *
         * @param file The file deleted (ignored)
         */
        @Override
        public void onFileDelete(File file)
        {
            log.debug("File deleted: " + file.getName());
            fileListenerInterface.onFileDelete(file);
        }


        /**
         * Notification that a file has been changed.
         *
         * @param file The file changed (ignored)
         */
        @Override
        public void onFileChange(File file)
        {
            log.debug("File changed: " + file.getName());
            fileListenerInterface.onFileChange(file);
        }
    }


    /**
     * Inner class for the directory listener logic
     */
    class FolderMonitoringListener extends FileAlterationListenerAdaptor
    {
        private final FileDirectoryListenerInterface directoryListenerInterface;
        private final boolean                        monitorSubDirectories;
        private final FileFilter                     fileFilter;

        /**
         * Constructor provides the listener to call.
         *
         * @param directoryListenerInterface listener from connector
         * @param monitorSubDirectories should subdirectories be monitored?
         */
        FolderMonitoringListener(FileDirectoryListenerInterface directoryListenerInterface,
                                 boolean                        monitorSubDirectories,
                                 FileFilter                     fileFilter)
        {
            this.directoryListenerInterface = directoryListenerInterface;
            this.monitorSubDirectories = monitorSubDirectories;
            this.fileFilter = fileFilter;
        }


        /**
         * Notification that a new file has been created under the directory.
         *
         * @param file The file created (ignored)
         */
        @Override
        public void onFileCreate(File file)
        {
            log.debug("File created: " + file.getName());
            directoryListenerInterface.onFileCreate(file);
        }


        /**
         * Notification that a file has been deleted.
         *
         * @param file The file deleted (ignored)
         */
        @Override
        public void onFileDelete(File file)
        {
            log.debug("File deleted: " + file.getName());
            directoryListenerInterface.onFileDelete(file);
        }


        /**
         * Notification that a file has been changed.
         *
         * @param file The file changed (ignored)
         */
        @Override
        public void onFileChange(File file)
        {
            log.debug("File changed: " + file.getName());
            directoryListenerInterface.onFileChange(file);
        }


        /**
         * Notification that a directory has been created.
         *
         * @param directory The directory created (ignored)
         */
        @Override
        public void onDirectoryCreate(File directory)
        {
            final String methodName = "onDirectoryCreate";

            log.debug("Folder created: " + directory.getName());
            directoryListenerInterface.onDirectoryCreate(directory);
            if (monitorSubDirectories)
            {
                try
                {
                    registerDirectoryListener(directoryListenerInterface, directory, fileFilter);
                }
                catch (Exception error)
                {
                    if (auditLog != null)
                    {
                        auditLog.logException(methodName,
                                              OMFAuditCode.UNEXPECTED_EXC_MONITOR_START.getMessageDefinition(error.getClass().getName(),
                                                                                                             connectorName,
                                                                                                             error.getMessage()),
                                              error);
                    }
                }
            }
        }

        @Override
        public void onDirectoryDelete(File directory)
        {
            log.debug("Folder deleted: " + directory.getName());
            directoryListenerInterface.onDirectoryDelete(directory);
        }
    }


    /**
     * Throw an exception if the supplied parameter is null
     *
     * @param object         object to validate
     * @param nameParameter  name of the parameter that passed the object.
     * @param methodName     name of the method making the call.
     *
     * @throws InvalidParameterException the object is null
     */
    public void validateParameter(Object object,
                                  String nameParameter,
                                  String methodName) throws InvalidParameterException
    {
        if (object == null)
        {
            throw new InvalidParameterException(OMFErrorCode.NULL_OBJECT.getMessageDefinition(nameParameter, methodName),
                                                this.getClass().getName(),
                                                methodName,
                                                nameParameter);
        }
    }
}
