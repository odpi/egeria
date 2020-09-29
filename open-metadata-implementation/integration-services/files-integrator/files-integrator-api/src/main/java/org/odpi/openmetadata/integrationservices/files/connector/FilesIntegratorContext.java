/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.integrationservices.files.connector;

import org.odpi.openmetadata.accessservices.datamanager.api.DataManagerEventListener;
import org.odpi.openmetadata.accessservices.datamanager.client.DatabaseManagerClient;
import org.odpi.openmetadata.accessservices.datamanager.client.DataManagerEventClient;
import org.odpi.openmetadata.accessservices.datamanager.client.FilesAndFoldersClient;
import org.odpi.openmetadata.frameworks.connectors.ffdc.*;

/**
 * FilesIntegratorContext provides a wrapper around the Data Manager OMAS clients.
 * It provides the simplified interface to open metadata needed by the FilesIntegratorConnector.
 */
public class FilesIntegratorContext
{
    private FilesAndFoldersClient  client;
    private DataManagerEventClient eventClient;
    private String                 userId;
    private String                 fileSystemGUID;
    private String                 fileSystemName;


    /**
     * Create a new client with no authentication embedded in the HTTP request.
     *
     * @param client client to map request to
     * @param eventClient client to register for events
     * @param userId integration daemon's userId
     * @param fileSystemGUID unique identifier of the software server capability for the file system
     * @param fileSystemName unique name of the software server capability for the file system
     */
    public FilesIntegratorContext(FilesAndFoldersClient  client,
                                  DataManagerEventClient eventClient,
                                  String                 userId,
                                  String                 fileSystemGUID,
                                  String                 fileSystemName)
    {
        this.client         = client;
        this.eventClient    = eventClient;
        this.userId         = userId;
        this.fileSystemGUID = fileSystemGUID;
        this.fileSystemName = fileSystemName;
    }


    /* ========================================================
     * Register for inbound events from the Data Manager OMAS OutTopic
     */


    /**
     * Register a listener object that will be passed each of the events published by
     * the Data Manager OMAS.
     *
     * @param listener listener object
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws ConnectionCheckedException there are errors in the configuration of the connection which is preventing
     *                                      the creation of a connector.
     * @throws ConnectorCheckedException there are errors in the initialization of the connector.
     * @throws PropertyServerException there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void registerListener(DataManagerEventListener listener) throws InvalidParameterException,
                                                                           ConnectionCheckedException,
                                                                           ConnectorCheckedException,
                                                                           PropertyServerException,
                                                                           UserNotAuthorizedException
    {
        eventClient.registerListener(userId, listener);
    }

}
