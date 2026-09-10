/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.viewservices.productmanager.server;

import org.odpi.openmetadata.commonservices.multitenant.OMVSServiceInstance;
import org.odpi.openmetadata.adminservices.configuration.registration.ViewServiceDescription;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.connectors.client.ConnectedAssetClient;
import org.odpi.openmetadata.frameworks.openmetadata.client.OpenMetadataClient;
import org.odpi.openmetadata.frameworks.openmetadata.connectorcontext.ConnectorContextBase;
import org.odpi.openmetadata.frameworks.openmetadata.enums.DeleteMethod;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.openmetadata.handlers.CollectionHandler;
import org.odpi.openmetadata.frameworkservices.ocf.metadatamanagement.client.EgeriaConnectedAssetClient;
import org.odpi.openmetadata.frameworkservices.omf.client.EgeriaOpenMetadataStoreClient;

import java.util.UUID;

/**
 * ProductManagerInstance caches references to the objects it needs for a specific server.
 * It is also responsible for registering itself in the instance map.
 */

public class ProductManagerInstance extends OMVSServiceInstance
{
    private static final ViewServiceDescription myDescription = ViewServiceDescription.PRODUCT_MANAGER;

    private final CollectionHandler    collectionHandler;
    private final ConnectedAssetClient connectedAssetClient;
    private final OpenMetadataClient   openMetadataClient;
    private final AuditLog             auditLog;
    private final int                  maxPageSize;

    /**
     * Set up the Product Manager OMVS instance
     *
     * @param serverName name of this server
     * @param auditLog logging destination
     * @param localServerUserId userId used for server initiated actions
     * @param localServerSecretsStoreProvider class name of the secrets store connector for bearer token
     * @param localServerSecretsStoreLocation location of the secrets store
     * @param localServerSecretsStoreCollection collection within the secrets store
     * @param maxPageSize maximum page size
     * @param remoteServerName  remote server name
     * @param remoteServerURL remote server URL
     * @throws InvalidParameterException problem with server name or platform URL
     */
    public ProductManagerInstance(String   serverName,
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

        this.auditLog    = auditLog;
        this.maxPageSize = maxPageSize;

        openMetadataClient = new EgeriaOpenMetadataStoreClient(remoteServerName,
                                                                                  remoteServerURL,
                                                                                  localServerSecretsStoreProvider,
                                                                                  localServerSecretsStoreLocation,
                                                                                  localServerSecretsStoreCollection,
                                                                                  maxPageSize,
                                                                                  auditLog);
        collectionHandler = new CollectionHandler(serverName,
                                                  auditLog,
                                                  myDescription.getViewServiceFullName(),
                                                  openMetadataClient);

        connectedAssetClient = new EgeriaConnectedAssetClient(remoteServerName,
                                                              remoteServerURL,
                                                              localServerSecretsStoreProvider,
                                                              localServerSecretsStoreLocation,
                                                              localServerSecretsStoreCollection,
                                                              maxPageSize,
                                                              auditLog);
    }


    /**
     * Return a connector context for the calling user.  This is the object that the Open Integration Framework's
     * Bitol mappers and generators work through; it gives them the same clients that an integration connector has.
     *
     * @param userId calling user
     * @return connector context
     */
    public ConnectorContextBase getConnectorContext(String userId)
    {
        return new ConnectorContextBase(serverName,
                                        myDescription.getViewServiceFullName(),
                                        null,
                                        null,
                                        UUID.randomUUID().toString(),
                                        myDescription.getViewServiceFullName(),
                                        userId,
                                        null,
                                        false,
                                        openMetadataClient,
                                        auditLog,
                                        maxPageSize,
                                        DeleteMethod.SOFT_DELETE);
    }


    /**
     * Return the collection handler.  This handler is from the Open Metadata Framework (OMF) and is for accessing and
     * maintaining collections, including digital products and their families.
     *
     * @return handler
     */
    public CollectionHandler getCollectionHandler()
    {
        return collectionHandler;
    }


    /**
     * Return the connected asset client.  This client is from the Open Connector Framework (OCF) and is used to create
     * connectors to the assets catalogued in open metadata - in particular the integration daemons that distribute
     * Bitol documents.
     *
     * @return client
     */
    public ConnectedAssetClient getConnectedAssetClient()
    {
        return connectedAssetClient;
    }
}
