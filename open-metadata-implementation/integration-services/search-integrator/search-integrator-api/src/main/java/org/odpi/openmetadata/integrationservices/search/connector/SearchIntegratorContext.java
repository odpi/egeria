/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.integrationservices.search.connector;

import org.odpi.openmetadata.accessservices.assetcatalog.eventclient.AssetCatalogEventClient;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;

/**
 * SearchIntegratorContext provides a wrapper around the Asset Catalog OMAS client.
 * It provides the simplified interface to open metadata needed by the SearchIntegratorConnector.
 */
public class SearchIntegratorContext {
    private String userId;
    private String assetManagerGUID;
    private String assetManagerName;
    private String connectorName;
    private String integrationServiceName;
    private AuditLog auditLog;
    private AssetCatalogEventClient eventClient;


    /**
     * Create a new context for a connector.
     *
     * @param userId                 integration daemon's userId
     * @param assetManagerGUID       unique identifier of the software server capability for the asset manager
     * @param assetManagerName       unique name of the software server capability for the asset manager
     * @param connectorName          name of the connector using this context
     * @param assetCatalogEventClient client to access the Asset Catalog OMAS out topic
     * @param integrationServiceName name of this service
     * @param auditLog               logging destination
     */
    public SearchIntegratorContext(String userId, String assetManagerGUID, String assetManagerName, String connectorName,
                                   AssetCatalogEventClient assetCatalogEventClient, String integrationServiceName, AuditLog auditLog) {

        this.userId = userId;
        this.assetManagerGUID = assetManagerGUID;
        this.assetManagerName = assetManagerName;
        this.connectorName = connectorName;
        this.integrationServiceName = integrationServiceName;
        this.auditLog = auditLog;
        this.eventClient = assetCatalogEventClient;
    }
}
