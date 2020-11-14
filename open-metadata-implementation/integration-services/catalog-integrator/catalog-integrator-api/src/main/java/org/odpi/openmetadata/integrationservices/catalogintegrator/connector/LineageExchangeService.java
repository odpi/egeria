/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.integrationservices.catalogintegrator.connector;

import org.odpi.openmetadata.accessservices.assetmanager.client.LineageExchangeClient;
import org.odpi.openmetadata.accessservices.assetmanager.properties.SynchronizationDirection;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;


/**
 * LineageExchangeService is the context for managing process definitions and lineage linkage.
 */
public class LineageExchangeService
{
    private LineageExchangeClient    lineageExchangeClient;
    private String                   userId;
    private String                   assetManagerGUID;
    private String                   assetManagerName;
    private String                   connectorName;
    private SynchronizationDirection synchronizationDirection;
    private AuditLog                 auditLog;


    /**
     * Create a new client to exchange lineage content with open metadata.
     *
     * @param lineageExchangeClient client for exchange requests
     * @param synchronizationDirection direction(s) that metadata can flow
     * @param userId integration daemon's userId
     * @param assetManagerGUID unique identifier of the software server capability for the asset manager
     * @param assetManagerName unique name of the software server capability for the asset manager
     * @param connectorName name of the connector using this context
     * @param auditLog logging destination
     */
    LineageExchangeService(LineageExchangeClient    lineageExchangeClient,
                           SynchronizationDirection synchronizationDirection,
                           String                   userId,
                           String                   assetManagerGUID,
                           String                   assetManagerName,
                           String                   connectorName,
                           AuditLog                 auditLog)
    {
        this.lineageExchangeClient    = lineageExchangeClient;
        this.synchronizationDirection = synchronizationDirection;
        this.userId                   = userId;
        this.assetManagerGUID         = assetManagerGUID;
        this.assetManagerName         = assetManagerName;
        this.connectorName            = connectorName;
        this.auditLog                 = auditLog;
    }

}
