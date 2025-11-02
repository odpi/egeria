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
import java.util.HashMap;
import java.util.Map;


/**
 * FilesListenerManager uses the Java WatchService capabilities to support the ability for an Integration Connector to
 * monitor changes in the file system.  Each listener that is registered is given its own watch service
 * running in its own thread.  The watch service blocks waiting for file/directory change events.
 */
public class FilesListenerManager
{
    private static final Logger log = LoggerFactory.getLogger(FilesListenerManager.class);
    private final AuditLog auditLog;
    private final String   connectorName;


    /*
     * This map is used when unregistering the directory watchers.
     */
    private final Map<String, ActiveWatcher> registeredDirectoryWatchers = new HashMap<>();

    /**
     * Constructor.
     *
     * @param auditLog logging destination
     * @param connectorName name of the connector
     */
    public FilesListenerManager(AuditLog auditLog,
                                String   connectorName)
    {
        this.auditLog = auditLog;
        this.connectorName = connectorName;
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
    public void registerDirectoryListener(FileListenerInterface listener,
                                          File                  directoryToMonitor,
                                          FileFilter            fileFilter) throws InvalidParameterException
    {
        final String methodName = "registerDirectoryListener";
        final String listenerParameterName = "listener";
        final String directoryParameterName = "directoryToMonitor";

        validateParameter(listener, listenerParameterName, methodName);
        validateParameter(directoryToMonitor, directoryParameterName, methodName);
        validateIsDirectory(directoryToMonitor, directoryParameterName, methodName);

        try
        {
            DirectoryWatcher directoryWatcher = new DirectoryWatcher(listener,
                                                                     directoryToMonitor,
                                                                     fileFilter,
                                                                     auditLog,
                                                                     connectorName);

            Thread thread = new Thread(directoryWatcher, "Watch " + directoryToMonitor.getName());
            thread.start();

            ActiveWatcher activeWatcher = new ActiveWatcher(directoryToMonitor, thread, directoryWatcher);

            registeredDirectoryWatchers.put(directoryToMonitor.getCanonicalPath(), activeWatcher);

            if (auditLog != null)
            {
                auditLog.logMessage(methodName,
                                    OMFAuditCode.DIRECTORY_MONITORING_STARTING.getMessageDefinition(connectorName,
                                                                                                    directoryToMonitor.getCanonicalPath()));
            }
        }
        catch (IOException exception)
        {
            throw new InvalidParameterException(OMFErrorCode.UNEXPECTED_IO_EXCEPTION.getMessageDefinition(directoryToMonitor.getName(),
                                                                                                          exception.getMessage()),
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
        final String methodName = "unregisterDirectoryListener";
        final String listenerParameterName = "listener";
        final String directoryParameterName = "directoryToMonitor";

        validateParameter(listener, listenerParameterName, methodName);
        validateParameter(directoryToMonitor, directoryParameterName, methodName);
        validateIsDirectory(directoryToMonitor, directoryParameterName, methodName);

        try
        {
            stopMonitoringDirectory(directoryToMonitor.getCanonicalPath(), methodName);
        }
        catch (IOException exception)
        {
            throw new InvalidParameterException(OMFErrorCode.UNEXPECTED_IO_EXCEPTION.getMessageDefinition(directoryToMonitor.getName(),
                                                                                                          exception.getMessage()),
                                                this.getClass().getName(),
                                                methodName,
                                                exception,
                                                OpenMetadataProperty.PATH_NAME.name);
        }
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
        validateIsDirectory(directoryToMonitor, directoryParameterName, methodName);

        try
        {
            DirectoryWatcher directoryWatcher = new DirectoryTreeWatcher(listener,
                                                                         directoryToMonitor,
                                                                         fileFilter,
                                                                         auditLog,
                                                                         connectorName);

            Thread thread = new Thread(directoryWatcher, "Watch " + directoryToMonitor.getName());
            thread.start();

            ActiveWatcher activeWatcher = new ActiveWatcher(directoryToMonitor, thread, directoryWatcher);

            registeredDirectoryWatchers.put(directoryToMonitor.getCanonicalPath(), activeWatcher);

            if (auditLog != null)
            {
                auditLog.logMessage(methodName,
                                    OMFAuditCode.DIRECTORY_MONITORING_STARTING.getMessageDefinition(connectorName,
                                                                                                    directoryToMonitor.getCanonicalPath()));
            }
        }
        catch (IOException exception)
        {
            throw new InvalidParameterException(OMFErrorCode.UNEXPECTED_IO_EXCEPTION.getMessageDefinition(directoryToMonitor.getName(),
                                                                                                          exception.getMessage()),
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
        final String methodName = "unregisterDirectoryTreeListener";
        final String listenerParameterName = "listener";
        final String directoryParameterName = "directoryToMonitor";

        validateParameter(listener, listenerParameterName, methodName);
        validateParameter(directoryToMonitor, directoryParameterName, methodName);
        validateIsDirectory(directoryToMonitor, directoryParameterName, methodName);

        try
        {
            stopMonitoringDirectory(directoryToMonitor.getCanonicalPath(), methodName);
        }
        catch (IOException exception)
        {
            throw new InvalidParameterException(OMFErrorCode.UNEXPECTED_IO_EXCEPTION.getMessageDefinition(directoryToMonitor.getName(),
                                                                                                          exception.getMessage()),
                                                this.getClass().getName(),
                                                methodName,
                                                exception,
                                                OpenMetadataProperty.PATH_NAME.name);
        }
    }


    /**
     * Tell the watch service for the directory to stop monitoring.
     *
     * @param monitoredDirectory canonical path name of directory to monitor
     * @param methodName calling method
     */
    private void stopMonitoringDirectory(String monitoredDirectory,
                                         String methodName)
    {
        ActiveWatcher activeWatcher = registeredDirectoryWatchers.get(monitoredDirectory);

        if (activeWatcher != null)
        {
            activeWatcher.directoryWatcher.disconnect();
            activeWatcher.runningThread.interrupt();

            if (auditLog != null)
            {
                auditLog.logMessage(methodName, OMFAuditCode.DIRECTORY_MONITORING_STOPPING.getMessageDefinition(connectorName,
                                                                                                                monitoredDirectory));
            }
        }
    }


    /**
     * Shutdown file monitoring
     */
    public void disconnect()
    {
        final String methodName = "disconnect";

        try
        {
            for (String monitoredDirectory : registeredDirectoryWatchers.keySet())
            {
                stopMonitoringDirectory(monitoredDirectory, methodName);
            }

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
                                      OMFAuditCode.UNEXPECTED_FILE_MONITORING_EXCEPTION.getMessageDefinition(error.getClass().getName(),
                                                                                                             connectorName,
                                                                                                             error.getMessage()),
                                      error);
            }
        }
    }


    /**
     * Record to allow the shutdown a listener.
     *
     * @param directoryToMonitor monitored directory
     * @param runningThread watcher's thread
     * @param directoryWatcher watcher performing the monitoring
     */
    record ActiveWatcher(File             directoryToMonitor,
                         Thread           runningThread,
                         DirectoryWatcher directoryWatcher)
    {
    }


    /**
     * Throw an exception if the supplied parameter is not a directory
     *
     * @param directoryToMonitor "file" to validate
     * @param nameParameter  name of the parameter that passed the object.
     * @param methodName     name of the method making the call.
     *
     * @throws InvalidParameterException the object is null
     */
    public void validateIsDirectory(File   directoryToMonitor,
                                    String nameParameter,
                                    String methodName) throws InvalidParameterException
    {
        if (! directoryToMonitor.isDirectory())
        {
            throw new InvalidParameterException(OMFErrorCode.NOT_DIRECTORY.getMessageDefinition(directoryToMonitor.getAbsolutePath(), methodName, connectorName),
                                                this.getClass().getName(),
                                                methodName,
                                                nameParameter);
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
