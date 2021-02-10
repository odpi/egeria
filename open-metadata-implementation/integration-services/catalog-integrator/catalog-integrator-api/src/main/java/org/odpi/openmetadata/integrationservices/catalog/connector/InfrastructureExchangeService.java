/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.integrationservices.catalog.connector;

import org.odpi.openmetadata.accessservices.assetmanager.client.InfrastructureExchangeClient;
import org.odpi.openmetadata.accessservices.assetmanager.properties.SynchronizationDirection;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;


/**
 * InfrastructureExchangeService is the context for managing infrastructure definitions such as applications, servers
 * and hosts.
 */
public class InfrastructureExchangeService
{
    private InfrastructureExchangeClient infrastructureExchangeClient;
    private String                       userId;
    private String                       assetManagerGUID;
    private String                       assetManagerName;
    private String                       connectorName;
    private SynchronizationDirection     synchronizationDirection;
    private AuditLog                     auditLog;


    /**
     * Create a new client to exchange data asset content with open metadata.
     *
     * @param infrastructureExchangeClient client for exchange requests
     * @param synchronizationDirection direction(s) that metadata can flow
     * @param userId integration daemon's userId
     * @param assetManagerGUID unique identifier of the software server capability for the asset manager
     * @param assetManagerName unique name of the software server capability for the asset manager
     * @param connectorName name of the connector using this context
     * @param auditLog logging destination
     */
    InfrastructureExchangeService(InfrastructureExchangeClient infrastructureExchangeClient,
                                  SynchronizationDirection     synchronizationDirection,
                                  String                       userId,
                                  String                       assetManagerGUID,
                                  String                       assetManagerName,
                                  String                       connectorName,
                                  AuditLog                     auditLog)
    {
        this.infrastructureExchangeClient = infrastructureExchangeClient;
        this.synchronizationDirection     = synchronizationDirection;
        this.userId                       = userId;
        this.assetManagerGUID             = assetManagerGUID;
        this.assetManagerName             = assetManagerName;
        this.connectorName                = connectorName;
        this.auditLog                     = auditLog;
    }

}
