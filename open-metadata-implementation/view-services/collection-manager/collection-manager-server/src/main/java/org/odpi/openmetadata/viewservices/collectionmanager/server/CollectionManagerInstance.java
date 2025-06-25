/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.viewservices.collectionmanager.server;

import org.odpi.openmetadata.accessservices.digitalservice.client.CollectionsClient;
import org.odpi.openmetadata.accessservices.digitalservice.client.ConnectedAssetClient;
import org.odpi.openmetadata.accessservices.digitalservice.client.OpenMetadataStoreClient;
import org.odpi.openmetadata.adminservices.configuration.registration.ViewServiceDescription;
import org.odpi.openmetadata.commonservices.multitenant.OMVSServiceInstance;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;

/**
 * CollectionManagerInstance caches references to objects it needs for a specific server.
 * It is also responsible for registering itself in the instance map.
 */

public class CollectionManagerInstance extends OMVSServiceInstance
{
    private static final ViewServiceDescription myDescription = ViewServiceDescription.COLLECTION_MANAGER;

    private final CollectionsClient       collectionsClient;
    private final ConnectedAssetClient    connectedAssetClient;
    private final OpenMetadataStoreClient openMetadataStoreClient;

    /**
     * Set up the Collection Manager OMVS instance
     *
     * @param serverName name of this server
     * @param auditLog logging destination
     * @param localServerUserId user id to use on OMRS calls where there is no end user, or as part of an HTTP authentication mechanism with serverUserPassword.
     * @param localServerUserPassword password to use as part of an HTTP authentication mechanism.
     * @param maxPageSize maximum page size
     * @param remoteServerName  remote server name
     * @param remoteServerURL remote server URL
     * @throws InvalidParameterException problem with server name or platform URL
     */
    public CollectionManagerInstance(String       serverName,
                                     AuditLog     auditLog,
                                     String       localServerUserId,
                                     String       localServerUserPassword,
                                     int          maxPageSize,
                                     String       remoteServerName,
                                     String       remoteServerURL) throws InvalidParameterException
    {
        super(serverName,
              myDescription.getViewServiceName(),
              auditLog,
              localServerUserId,
              localServerUserPassword,
              maxPageSize,
              remoteServerName,
              remoteServerURL);

        if (localServerUserPassword == null)
        {
            collectionsClient       = new CollectionsClient(serverName, remoteServerName, remoteServerURL, auditLog, maxPageSize);
            connectedAssetClient    = new ConnectedAssetClient(remoteServerName, remoteServerURL, auditLog);
            openMetadataStoreClient = new OpenMetadataStoreClient(remoteServerName, remoteServerURL, maxPageSize);
        }
        else
        {
            collectionsClient       = new CollectionsClient(serverName, remoteServerName, remoteServerURL, localServerUserId, localServerUserPassword, auditLog, maxPageSize);
            connectedAssetClient    = new ConnectedAssetClient(remoteServerName, remoteServerURL, localServerUserId, localServerUserPassword, auditLog);
            openMetadataStoreClient = new OpenMetadataStoreClient(remoteServerName, remoteServerURL, localServerUserId, localServerUserPassword, maxPageSize);
        }
    }


    /**
     * Return the collection management client.  This client is from Digital Service OMAS and is for maintaining collections.
     *
     * @return client
     */
    public CollectionsClient getCollectionsClient()
    {
        return collectionsClient;
    }


    /**
     * Return the connected asset client.  This client is from Open Connector Framework (OCF) and is for retrieving information about
     * assets and creating connectors.
     *
     * @return client
     */
    public ConnectedAssetClient getConnectedAssetClient()
    {
        return connectedAssetClient;
    }


    /**
     * Return the open metadata store client.  This client is from the Governance Action Framework (GAF) and is for accessing and
     * maintaining all types of metadata.
     *
     * @return client
     */
    public OpenMetadataStoreClient getOpenMetadataStoreClient()
    {
        return openMetadataStoreClient;
    }
}
