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
import java.nio.file.*;
import java.util.HashMap;
import java.util.Map;


/**
 * DirectoryWatcher uses the Java WatchService capabilities to support the ability for an Integration Connector to
 * monitor changes in the file system.
 */
public class DirectoryWatcher implements Runnable
{
    private static final Logger log = LoggerFactory.getLogger(DirectoryWatcher.class);

    protected final AuditLog auditLog;
    protected final String   connectorName;

    private volatile boolean keepRunning = true;
    protected final FileListenerInterface listener;
    protected final FileFilter            fileFilter;
    protected WatchService watchService;
    protected final Map<WatchKey, Path> keyToPathMap = new HashMap<>();


    /**
     * Common constructor.
     *
     * @param listener           listener object
     * @param fileFilter         a file filter implementation that restricts the files/directories that will be returned to the listener
     * @param auditLog logging destination
     * @param connectorName name of the connector
     */
    public DirectoryWatcher(FileListenerInterface listener,
                            FileFilter            fileFilter,
                            AuditLog              auditLog,
                            String                connectorName) throws InvalidParameterException
    {
        final String methodName = "DirectoryWatcher(initialize)";

        this.listener = listener;
        this.fileFilter = fileFilter;
        this.auditLog = auditLog;
        this.connectorName = connectorName;

        try
        {
            watchService = FileSystems.getDefault().newWatchService();
        }
        catch (IOException error)
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
                                                                                                       WatchService.class.getName(),
                                                                                                       methodName,
                                                                                                       error.getMessage()),
                                                this.getClass().getName(),
                                                methodName,
                                                error,
                                                OpenMetadataProperty.PATH_NAME.name);
        }
    }


    /**
     * Constructor for single directory watcher.
     *
     * @param listener           listener object
     * @param directoryToMonitor details of the file directory to monitor
     * @param fileFilter         a file filter implementation that restricts the files/directories that will be returned to the listener
     * @param auditLog logging destination
     * @param connectorName name of the connector
     */
    public DirectoryWatcher(FileListenerInterface listener,
                            File                  directoryToMonitor,
                            FileFilter            fileFilter,
                            AuditLog              auditLog,
                            String                connectorName) throws InvalidParameterException
    {
        this(listener, fileFilter, auditLog, connectorName);

        final String methodName = "DirectoryWatcher";

        try
        {
            registerDirectory(directoryToMonitor.toPath(), methodName);
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
     * Does the file match the filter?
     *
     * @param file file with notification
     * @return boolean
     */
    protected boolean matchFile(File file)
    {
        if (fileFilter != null)
        {
            return fileFilter.accept(file);
        }
        else
        {
            return true;
        }
    }


    /**
     * Register a single directory with the watch service.
     *
     * @param directory path of directory
     * @param methodName calling method
     * @throws IOException problem with the directory
     */
    protected synchronized void registerDirectory(Path   directory,
                                                  String methodName) throws IOException
    {
        try
        {
            WatchKey key = directory.register(watchService,
                                              StandardWatchEventKinds.ENTRY_CREATE,
                                              StandardWatchEventKinds.ENTRY_MODIFY,
                                              StandardWatchEventKinds.ENTRY_DELETE);

            log.debug("Registering directory " + directory);

            keyToPathMap.put(key, directory);
        }
        catch (IOException error)
        {
            if (auditLog != null)
            {
                auditLog.logException(methodName,
                                      OMFAuditCode.UNEXPECTED_FILE_MONITORING_EXCEPTION.getMessageDefinition(error.getClass().getName(),
                                                                                                             connectorName,
                                                                                                             error.getMessage()),
                                      error);
            }

            throw error;
        }
    }


    /**
     * Remove the appropriate directory for the key.
     *
     * @param key monitor key
     */
    protected synchronized void unregisterDirectory(WatchKey  key)
    {
        if (keyToPathMap.get(key) != null)
        {
            keyToPathMap.remove(key);
        }
    }


    /**
     * Retrieve the appropriate directory for the key.
     *
     * @param key monitor key
     */
    protected synchronized Path getDirectory(WatchKey key)
    {
        return keyToPathMap.get(key);
    }


    /**
     * Shutdown file monitoring
     */
    public void disconnect()
    {
        log.debug("Disconnecting file monitoring for " + connectorName);

        keepRunning = false;
    }


    /**
     * When an object implementing interface {@code Runnable} is used
     * to create a thread, starting the thread causes the object's
     * {@code run} method to be called in that separately executing
     * thread.
     * <p>
     * The general contract of the method {@code run} is that it may
     * take any action whatsoever.
     *
     * @see Thread#run()
     */
    @Override
    public void run()
    {
        final String methodName = "Thread.run";

        while (keepRunning)
        {
            try
            {
                /*
                 * Wait for a file change notification
                 */
                WatchKey key = watchService.take();

                if (key != null)
                {
                    Path directory = getDirectory(key);
                    if (directory != null)
                    {
                        /*
                         * Only processing events for registered directories.
                         */
                        for (WatchEvent<?> event : key.pollEvents())
                        {
                            processEvent(directory, event);
                        }
                    }

                    /*
                     * Unregister a directory if the watcher is no longer valid
                     */
                    boolean stillValid = key.reset();
                    if (! stillValid)
                    {
                        unregisterDirectory(key);
                    }
                }
            }
            catch (InterruptedException interrupt)
            {
                if (auditLog != null)
                {
                    auditLog.logMessage(methodName, OMFAuditCode.THREAD_INTERRUPT.getMessageDefinition(connectorName, interrupt.getMessage()));
                }
            }
        }


        try
        {
            watchService.close();
        }
        catch (IOException error)
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
     * Process a file change event.  Depending on the event type, the file filter and the listener type.
     * It is also possible that new directories will be registered.
     *
     * @param directory path with the event
     * @param event event
     */
    protected void processEvent(Path          directory,
                                WatchEvent<?> event)
    {
        final String methodName = "processEvent";

        WatchEvent.Kind<?> kind = event.kind();

        /*
         * The context for directory entry event is the file name.   It is aligned with the name of the directory.
         */
        Path name = (Path) event.context();
        Path child = directory.resolve(name);

        File file = child.toFile();

        if (auditLog != null)
        {
            auditLog.logMessage(methodName, OMFAuditCode.FILE_CHANGE_EVENT.getMessageDefinition(connectorName, kind.name(), child.toString()));
        }

        if (file.isDirectory())
        {
            if (listener instanceof FileDirectoryListenerInterface fileDirectoryListener)
            {
                if (kind.equals(StandardWatchEventKinds.ENTRY_CREATE))
                {
                    log.debug("Folder created: " + file.getName());
                    fileDirectoryListener.onDirectoryCreate(file);

                    try
                    {
                        registerDirectory(child, methodName);
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
                else if (kind.equals(StandardWatchEventKinds.ENTRY_MODIFY))
                {
                    log.debug("Folder changed: " + file.getName());
                    fileDirectoryListener.onDirectoryChange(file);
                }
                else if (kind.equals(StandardWatchEventKinds.ENTRY_DELETE))
                {
                    log.debug("Folder deleted: " + file.getName());
                    fileDirectoryListener.onDirectoryDelete(file);
                }
            }
        }
        else if (matchFile(file))
        {
            if (kind.equals(StandardWatchEventKinds.ENTRY_CREATE))
            {
                log.debug("File created: " + file.getName());
                listener.onFileCreate(file);
            }
            else if (kind.equals(StandardWatchEventKinds.ENTRY_MODIFY))
            {
                log.debug("File changed: " + file.getName());
                listener.onFileChange(file);
            }
            else if (kind.equals(StandardWatchEventKinds.ENTRY_DELETE))
            {
                log.debug("File deleted: " + file.getName());
                listener.onFileDelete(file);
            }
        }
    }
}
