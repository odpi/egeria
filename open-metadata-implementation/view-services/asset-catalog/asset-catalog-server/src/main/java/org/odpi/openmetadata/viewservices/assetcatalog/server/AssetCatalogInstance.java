/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.viewservices.assetcatalog.server;

import org.odpi.openmetadata.accessservices.assetconsumer.client.AssetConsumer;
import org.odpi.openmetadata.accessservices.assetconsumer.client.OpenIntegrationServiceClient;
import org.odpi.openmetadata.accessservices.assetconsumer.client.OpenMetadataStoreClient;
import org.odpi.openmetadata.adminservices.configuration.registration.ViewServiceDescription;
import org.odpi.openmetadata.commonservices.multitenant.OMVSServiceInstance;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;

/**
 * AssetCatalogInstance caches references to objects it needs for a specific server.
 * It is also responsible for registering itself in the instance map.
 */

public class AssetCatalogInstance extends OMVSServiceInstance
{
    private static final ViewServiceDescription myDescription = ViewServiceDescription.ASSET_CATALOG;

    private final AssetConsumer                assetConsumerClient;
    private final OpenIntegrationServiceClient openIntegrationServiceClient;
    private final OpenMetadataStoreClient      openMetadataStoreClient;


    /**
     * Set up the Asset Catalog OMVS instance
     *
     * @param serverName name of this server
     * @param auditLog logging destination
     * @param localServerUserId userId used for server initiated actions
     * @param maxPageSize maximum page size
     * @param remoteServerName  remote server name
     * @param remoteServerURL remote server URL
     * @throws InvalidParameterException problem with server name or platform URL
     */
    public AssetCatalogInstance(String       serverName,
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

        assetConsumerClient = new AssetConsumer(remoteServerName, remoteServerURL, auditLog);
        openIntegrationServiceClient = new OpenIntegrationServiceClient(remoteServerName, remoteServerURL);
        openMetadataStoreClient = new OpenMetadataStoreClient(remoteServerName, remoteServerURL);
    }


    /**
     * Return the main Asset Consumer OMAS client.
     *
     * @return client
     */
    public AssetConsumer getAssetConsumerClient()
    {
        return assetConsumerClient;
    }


    /**
     * Return the open integration client.  This client is from the Open Integration Framework (OIF).
     *
     * @return client
     */
    public OpenIntegrationServiceClient getOpenIntegrationServiceClient()
    {
        return openIntegrationServiceClient;
    }


    /**
     * Return the open metadata store client.  This client is from the Governance Action Framework (GAF).
     *
     * @return client
     */
    public OpenMetadataStoreClient getOpenMetadataStoreClient()
    {
        return openMetadataStoreClient;
    }
}
