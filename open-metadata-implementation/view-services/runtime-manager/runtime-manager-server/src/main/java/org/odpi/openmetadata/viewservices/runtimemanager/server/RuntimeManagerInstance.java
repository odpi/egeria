/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.viewservices.runtimemanager.server;

import org.odpi.openmetadata.accessservices.itinfrastructure.client.ConnectedAssetClient;
import org.odpi.openmetadata.accessservices.itinfrastructure.client.OpenMetadataStoreClient;
import org.odpi.openmetadata.accessservices.itinfrastructure.client.PlatformManagerClient;
import org.odpi.openmetadata.accessservices.itinfrastructure.client.ServerManagerClient;
import org.odpi.openmetadata.commonservices.multitenant.OMVSServiceInstance;
import org.odpi.openmetadata.adminservices.configuration.registration.ViewServiceDescription;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;

/**
 * RuntimeManagerInstance caches references to the objects it needs for a specific server.
 * It is also responsible for registering itself in the instance map.
 */

public class RuntimeManagerInstance extends OMVSServiceInstance
{
    private static final ViewServiceDescription myDescription = ViewServiceDescription.RUNTIME_MANAGER;


    private final PlatformManagerClient   platformManagerClient;
    private final ServerManagerClient     serverManagerClient;
    private final ConnectedAssetClient    connectedAssetClient;
    private final OpenMetadataStoreClient openMetadataStoreClient;

    /**
     * Set up the Runtime Manager OMVS instance
     *
     * @param serverName name of this server
     * @param auditLog logging destination
     * @param localServerUserId userId used for server initiated actions
     * @param maxPageSize maximum page size
     * @param remoteServerName  remote server name
     * @param remoteServerURL remote server URL
     * @throws InvalidParameterException problem with server name or platform URL
     */
    public RuntimeManagerInstance(String       serverName,
                                  AuditLog     auditLog,
                                  String       localServerUserId,
                                  int          maxPageSize,
                                  String       remoteServerName,
                                  String       remoteServerURL) throws InvalidParameterException
    {
        super(serverName,
              myDescription.getViewServiceName(),
              auditLog,
              localServerUserId,
              maxPageSize,
              remoteServerName,
              remoteServerURL);

        platformManagerClient       = new PlatformManagerClient(remoteServerName, remoteServerURL, maxPageSize);
        serverManagerClient       = new ServerManagerClient(remoteServerName, remoteServerURL, maxPageSize);
        connectedAssetClient    = new ConnectedAssetClient(remoteServerName, remoteServerURL, maxPageSize);
        openMetadataStoreClient = new OpenMetadataStoreClient(remoteServerName, remoteServerURL, maxPageSize);
    }



    /**
     * Return the platform manager client.  This client is from IT Infrastructure OMAS and is for maintaining server platforms.
     *
     * @return client
     */
    public PlatformManagerClient getPlatformManagerClient()
    {
        return platformManagerClient;
    }


    /**
     * Return the server manager client.  This client is from IT Infrastructure OMAS and is for maintaining servers.
     *
     * @return client
     */
    public ServerManagerClient getServerManagerClient()
    {
        return serverManagerClient;
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
