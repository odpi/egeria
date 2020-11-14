/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.integrationservices.catalogintegrator.connector;

import org.odpi.openmetadata.accessservices.assetmanager.client.DataAssetExchangeClient;

import org.odpi.openmetadata.accessservices.assetmanager.properties.*;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;


/**
 * DataAssetExchangeService is the context for managing data assets and associated elements such as schemas and
 * connections.
 */
public class DataAssetExchangeService
{
    private DataAssetExchangeClient  dataAssetExchangeClient;
    private String                   userId;
    private String                   assetManagerGUID;
    private String                   assetManagerName;
    private String                   connectorName;
    private SynchronizationDirection synchronizationDirection;
    private AuditLog                 auditLog;


    /**
     * Create a new client to exchange data asset content with open metadata.
     *
     * @param dataAssetExchangeClient client for exchange requests
     * @param synchronizationDirection direction(s) that metadata can flow
     * @param userId integration daemon's userId
     * @param assetManagerGUID unique identifier of the software server capability for the asset manager
     * @param assetManagerName unique name of the software server capability for the asset manager
     * @param connectorName name of the connector using this context
     * @param auditLog logging destination
     */
    DataAssetExchangeService(DataAssetExchangeClient dataAssetExchangeClient,
                             SynchronizationDirection synchronizationDirection,
                             String                   userId,
                             String                   assetManagerGUID,
                             String                   assetManagerName,
                             String                   connectorName,
                             AuditLog                 auditLog)
    {
        this.dataAssetExchangeClient = dataAssetExchangeClient;
        this.synchronizationDirection = synchronizationDirection;
        this.userId                   = userId;
        this.assetManagerGUID         = assetManagerGUID;
        this.assetManagerName         = assetManagerName;
        this.connectorName            = connectorName;
        this.auditLog                 = auditLog;
    }

}
