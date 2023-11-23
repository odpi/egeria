/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.integrationservices.files.contextmanager;

import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectionCheckedException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectorCheckedException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.integrationservices.files.connector.FileDirectoryListenerInterface;
import org.odpi.openmetadata.integrationservices.files.connector.FileListenerInterface;

import java.io.File;
import java.io.FileFilter;

/**
 * FilesListenerManager uses the Apache Commons IO capabilities to support the ability for a Files Integration Connector to
 * monitor changes in the file system.  The implementation is a thin wrapper around the Apache Commons IO to allow the
 * implementation to be swapped for other types of file system monitors.
 */
public class FilesListenerManager
{
    /**
     * Register a listener object that will be called each time a specific file is created, changed or deleted.
     *
     * @param listener      listener object
     * @param fileToMonitor name of the file to monitor
     *
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws ConnectionCheckedException there are errors in the configuration of the connection which is preventing
     *                                    the creation of a connector.
     * @throws ConnectorCheckedException  there are errors in the initialization of the connector.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void registerFileListener(FileListenerInterface listener,
                                     File                  fileToMonitor) throws InvalidParameterException,
                                                                                 ConnectionCheckedException,
                                                                                 ConnectorCheckedException,
                                                                                 PropertyServerException,
                                                                                 UserNotAuthorizedException
    {

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
     * @throws ConnectionCheckedException there are errors in the configuration of the connection which is preventing
     *                                    the creation of a connector.
     * @throws ConnectorCheckedException  there are errors in the initialization of the connector.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void registerDirectoryListener(FileDirectoryListenerInterface listener,
                                          File                           directoryToMonitor,
                                          FileFilter                     fileFilter) throws InvalidParameterException,
                                                                                            ConnectionCheckedException,
                                                                                            ConnectorCheckedException,
                                                                                            PropertyServerException,
                                                                                            UserNotAuthorizedException
    {

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
     * @throws ConnectionCheckedException there are errors in the configuration of the connection which is preventing
     *                                    the creation of a connector.
     * @throws ConnectorCheckedException  there are errors in the initialization of the connector.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void registerDirectoryTreeListener(FileDirectoryListenerInterface listener,
                                              File                           directoryToMonitor,
                                              FileFilter                     fileFilter) throws InvalidParameterException,
                                                                                                ConnectionCheckedException,
                                                                                                ConnectorCheckedException,
                                                                                                PropertyServerException,
                                                                                                UserNotAuthorizedException
    {

    }
}
