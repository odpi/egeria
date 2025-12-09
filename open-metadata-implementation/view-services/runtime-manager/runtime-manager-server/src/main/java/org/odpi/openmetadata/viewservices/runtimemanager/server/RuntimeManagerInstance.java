/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.viewservices.runtimemanager.server;

import org.odpi.openmetadata.commonservices.multitenant.OMVSServiceInstance;
import org.odpi.openmetadata.adminservices.configuration.registration.ViewServiceDescription;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.connectors.client.ConnectedAssetClient;
import org.odpi.openmetadata.frameworks.openmetadata.client.OpenMetadataClient;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.openmetadata.handlers.AssetHandler;
import org.odpi.openmetadata.frameworks.openmetadata.handlers.MetadataRepositoryCohortHandler;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;
import org.odpi.openmetadata.frameworkservices.ocf.metadatamanagement.client.EgeriaConnectedAssetClient;
import org.odpi.openmetadata.frameworkservices.omf.client.EgeriaOpenMetadataStoreClient;

/**
 * RuntimeManagerInstance caches references to the objects it needs for a specific server.
 * It is also responsible for registering itself in the instance map.
 */

public class RuntimeManagerInstance extends OMVSServiceInstance
{
    private static final ViewServiceDescription myDescription = ViewServiceDescription.RUNTIME_MANAGER;


    private final ConnectedAssetClient            connectedAssetClient;
    private final OpenMetadataClient              openMetadataClient;
    private final AssetHandler                    softwarePlatformHandler;
    private final AssetHandler                    softwareServerHandler;
    private final MetadataRepositoryCohortHandler metadataRepositoryCohortHandler;


    /**
     * Set up the Runtime Manager OMVS instance
     *
     * @param serverName name of this server
     * @param auditLog logging destination
     * @param localServerUserId userId used for server initiated actions
     * @param localServerSecretsStoreProvider secrets store connector for bearer token
     * @param localServerSecretsStoreLocation secrets store location for bearer token
     * @param localServerSecretsStoreCollection secrets store collection for bearer token
     * @param maxPageSize maximum page size
     * @param remoteServerName  remote server name
     * @param remoteServerURL remote server URL
     * @throws InvalidParameterException problem with server name or platform URL
     */
    public RuntimeManagerInstance(String   serverName,
                                  AuditLog auditLog,
                                  String   localServerUserId,
                                  String   localServerSecretsStoreProvider,
                                  String   localServerSecretsStoreLocation,
                                  String   localServerSecretsStoreCollection,
                                  int      maxPageSize,
                                  String   remoteServerName,
                                  String   remoteServerURL) throws InvalidParameterException
    {
        super(serverName,
              myDescription.getViewServiceFullName(),
              auditLog,
              localServerUserId,
              maxPageSize,
              remoteServerName,
              remoteServerURL);

        openMetadataClient = new EgeriaOpenMetadataStoreClient(remoteServerName,
                                                               remoteServerURL,
                                                               localServerSecretsStoreProvider,
                                                               localServerSecretsStoreLocation,
                                                               localServerSecretsStoreCollection,
                                                               maxPageSize,
                                                               auditLog);

        connectedAssetClient = new EgeriaConnectedAssetClient(remoteServerName,
                                                              remoteServerURL,
                                                              localServerSecretsStoreProvider,
                                                              localServerSecretsStoreLocation,
                                                              localServerSecretsStoreCollection,
                                                              maxPageSize,
                                                              auditLog);

        softwarePlatformHandler = new AssetHandler(serverName,
                                                   auditLog,
                                                   myDescription.getViewServiceFullName(),
                                                   openMetadataClient,
                                                   OpenMetadataType.SOFTWARE_SERVER_PLATFORM.typeName);

        softwareServerHandler = new AssetHandler(serverName,
                                                 auditLog,
                                                 myDescription.getViewServiceFullName(),
                                                 openMetadataClient,
                                                 OpenMetadataType.SOFTWARE_SERVER.typeName);

        metadataRepositoryCohortHandler = new MetadataRepositoryCohortHandler(serverName,
                                                                              auditLog,
                                                                              myDescription.getViewServiceFullName(),
                                                                              openMetadataClient);
    }


    /**
     * Return the platform manager client.  This client is from the Open Metadata Framework (OMF).
     *
     * @return client
     */
    public AssetHandler getSoftwarePlatformHandler()
    {
        return softwarePlatformHandler;
    }


    /**
     * Return the server manager client. This client is from the Open Metadata Framework (OMF).
     *
     * @return client
     */
    public AssetHandler getSoftwareServerHandler()
    {
        return softwareServerHandler;
    }

    /**
     * Return the cohort manager client. This client is from the Open Metadata Framework (OMF).
     *
     * @return client
     */
    public MetadataRepositoryCohortHandler getMetadataRepositoryCohortHandler()
    {
        return metadataRepositoryCohortHandler;
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
     * Return the open metadata store client.  This client is from the Open Metadata Framework (OMF) and is for accessing and
     * maintaining all types of metadata.
     *
     * @return client
     */
    public OpenMetadataClient getOpenMetadataClient()
    {
        return openMetadataClient;
    }
}
