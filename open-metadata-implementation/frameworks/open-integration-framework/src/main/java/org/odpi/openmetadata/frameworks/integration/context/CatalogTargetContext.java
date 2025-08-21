/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworks.integration.context;

import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.connectors.client.ConnectedAssetClient;
import org.odpi.openmetadata.frameworks.opengovernance.client.GovernanceConfiguration;
import org.odpi.openmetadata.frameworks.opengovernance.client.OpenGovernanceClient;
import org.odpi.openmetadata.frameworks.integration.client.OpenIntegrationClient;
import org.odpi.openmetadata.frameworks.integration.openlineage.OpenLineageListenerManager;
import org.odpi.openmetadata.frameworks.openmetadata.client.OpenMetadataClient;
import org.odpi.openmetadata.frameworks.openmetadata.enums.DeleteMethod;
import org.odpi.openmetadata.frameworks.openmetadata.enums.PermittedSynchronization;
import org.odpi.openmetadata.frameworks.openmetadata.events.OpenMetadataEventClient;


/**
 * A specialized integration context for a catalog target processor.
 */
public class CatalogTargetContext extends IntegrationContext
{
    /**
     * Constructor.
     *
     * @param localServerName name of local server
     * @param localServiceName name of the service to call
     * @param externalSourceGUID metadata collection unique id
     * @param externalSourceName metadata collection unique name
     * @param connectorId id of this connector instance
     * @param connectorName name of this connector instance
     * @param connectorUserId userId to use when issuing open metadata requests
     * @param connectorGUID unique identifier of the connector element that describes this connector in the open metadata store(s)
     * @param generateIntegrationReport should the context generate an integration report?
     * @param permittedSynchronization enum
     * @param openMetadataClient client to access open metadata store
     * @param openMetadataEventClient client to access open metadata events
     * @param connectedAssetClient client for working with connectors
     * @param openIntegrationClient client for calling the metadata server
     * @param governanceConfiguration client for managing catalog targets
     * @param openGovernanceClient client for initiating governance actions
     * @param auditLog logging destination
     * @param maxPageSize max number of results
     * @param deleteMethod default delete method
     */
    public CatalogTargetContext(String                     localServerName,
                                String                     localServiceName,
                                String                     externalSourceGUID,
                                String                     externalSourceName,
                                String                     connectorId,
                                String                     connectorName,
                                String                     connectorUserId,
                                String                     connectorGUID,
                                boolean                    generateIntegrationReport,
                                PermittedSynchronization   permittedSynchronization,
                                OpenMetadataClient         openMetadataClient,
                                OpenMetadataEventClient    openMetadataEventClient,
                                ConnectedAssetClient       connectedAssetClient,
                                OpenIntegrationClient      openIntegrationClient,
                                OpenLineageListenerManager openLineageListenerManager,
                                GovernanceConfiguration    governanceConfiguration,
                                OpenGovernanceClient       openGovernanceClient,
                                AuditLog                   auditLog,
                                int                        maxPageSize,
                                DeleteMethod               deleteMethod)
    {
        super(localServerName,
              localServiceName,
              externalSourceGUID,
              externalSourceName,
              connectorId,
              connectorName,
              connectorUserId,
              connectorGUID,
              generateIntegrationReport,
              permittedSynchronization,
              openMetadataClient,
              openMetadataEventClient,
              connectedAssetClient,
              openIntegrationClient,
              openLineageListenerManager,
              governanceConfiguration,
              openGovernanceClient,
              auditLog,
              maxPageSize,
              deleteMethod);
    }
}
