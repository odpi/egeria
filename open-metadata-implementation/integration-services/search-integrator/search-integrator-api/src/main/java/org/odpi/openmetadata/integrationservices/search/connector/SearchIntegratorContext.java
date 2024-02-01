/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.integrationservices.search.connector;

import org.odpi.openmetadata.accessservices.assetcatalog.eventclient.AssetCatalogEventClient;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.governanceaction.client.OpenMetadataClient;
import org.odpi.openmetadata.frameworks.integration.client.OpenIntegrationClient;
import org.odpi.openmetadata.frameworks.integration.context.IntegrationContext;
import org.odpi.openmetadata.frameworks.integration.contextmanager.PermittedSynchronization;

/**
 * SearchIntegratorContext provides a wrapper around the Asset Catalog OMAS client.
 * It provides the simplified interface to open metadata needed by the SearchIntegratorConnector.
 */
public class SearchIntegratorContext extends IntegrationContext
{
    private final String integrationServiceName;
    private final AuditLog auditLog;
    private final AssetCatalogEventClient eventClient;


    /**
     * Create a new context for a connector.
     *
     * @param connectorId unique identifier of the connector (used to configure the event listener)
     * @param connectorName name of connector from config
     * @param connectorUserId userId for the connector
     * @param serverName name of the integration daemon
     * @param openIntegrationClient client for calling the metadata server
     * @param openMetadataStoreClient client for calling the metadata server
     * @param assetCatalogEventClient client to access the Asset Catalog OMAS out topic
     * @param generateIntegrationReport should the connector generate an integration reports?
     * @param permittedSynchronization the direction of integration permitted by the integration connector
     * @param integrationConnectorGUID unique identifier for the integration connector if it is started via an integration group (otherwise it is
     *                                 null).
     * @param externalSourceGUID unique identifier of the software server capability for the asset manager
     * @param externalSourceName unique name of the software server capability for the asset manager
     * @param integrationServiceName name of this service
     * @param maxPageSize max number of elements that can be returned on a query
     * @param auditLog               logging destination
     */
    public SearchIntegratorContext(String                       connectorId,
                                   String                       connectorName,
                                   String                       connectorUserId,
                                   String                       serverName,
                                   OpenIntegrationClient        openIntegrationClient,
                                   OpenMetadataClient           openMetadataStoreClient,
                                   AssetCatalogEventClient      assetCatalogEventClient,
                                   boolean                      generateIntegrationReport,
                                   PermittedSynchronization     permittedSynchronization,
                                   String                       integrationConnectorGUID,
                                   String                       externalSourceGUID,
                                   String                       externalSourceName,
                                   String                       integrationServiceName,
                                   int                          maxPageSize,
                                   AuditLog                     auditLog) {

        super(connectorId,
              connectorName,
              connectorUserId,
              serverName,
              openIntegrationClient,
              openMetadataStoreClient,
              generateIntegrationReport,
              permittedSynchronization,
              externalSourceGUID,
              externalSourceName,
              integrationConnectorGUID,
              auditLog,
              maxPageSize);

        this.integrationServiceName = integrationServiceName;
        this.auditLog = auditLog;
        this.eventClient = assetCatalogEventClient;
    }
}
