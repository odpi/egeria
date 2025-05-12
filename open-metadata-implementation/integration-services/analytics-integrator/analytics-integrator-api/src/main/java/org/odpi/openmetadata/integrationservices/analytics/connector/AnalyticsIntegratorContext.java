/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.integrationservices.analytics.connector;

import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.governanceaction.client.ActionControlInterface;
import org.odpi.openmetadata.frameworks.governanceaction.client.GovernanceConfiguration;
import org.odpi.openmetadata.frameworks.openmetadata.client.OpenMetadataClient;
import org.odpi.openmetadata.frameworks.integration.client.OpenIntegrationClient;
import org.odpi.openmetadata.frameworks.integration.context.IntegrationContext;
import org.odpi.openmetadata.frameworks.openmetadata.enums.PermittedSynchronization;


/**
 * AnalyticsIntegratorContext is the context for cataloging metadata from an analytics tool.
 */
public class AnalyticsIntegratorContext extends IntegrationContext
{
    /**
     * Create a new client with no authentication embedded in the HTTP request.
     *
     * @param connectorId unique identifier of the connector (used to configure the event listener)
     * @param connectorName name of connector from config
     * @param connectorUserId userId for the connector
     * @param serverName name of the integration daemon
     * @param openIntegrationClient client for calling the metadata server
     * @param governanceConfiguration client for managing catalog targets
     * @param openMetadataStoreClient client for calling the metadata server
     * @param actionControlInterface client for initiating governance actions
     * @param generateIntegrationReport should the connector generate an integration reports?
     * @param permittedSynchronization the direction of integration permitted by the integration connector
     * @param integrationConnectorGUID unique identifier for the integration connector if it is started via an integration group (otherwise it is
     *                                 null).
     * @param externalSourceGUID unique identifier of the software server capability for the api manager
     * @param externalSourceName unique name of the software server capability for the api manager
     * @param auditLog logging destination
     * @param maxPageSize max number of elements that can be returned on a query
     */
    public AnalyticsIntegratorContext(String                       connectorId,
                                      String                       connectorName,
                                      String                       connectorUserId,
                                      String                       serverName,
                                      OpenIntegrationClient        openIntegrationClient,
                                      GovernanceConfiguration      governanceConfiguration,
                                      OpenMetadataClient           openMetadataStoreClient,
                                      ActionControlInterface       actionControlInterface,
                                      boolean                      generateIntegrationReport,
                                      PermittedSynchronization     permittedSynchronization,
                                      String                       integrationConnectorGUID,
                                      String                       externalSourceGUID,
                                      String                       externalSourceName,
                                      AuditLog                     auditLog,
                                      int                          maxPageSize)
    {
        super(connectorId,
              connectorName,
              connectorUserId,
              serverName,
              openIntegrationClient,
              governanceConfiguration,
              openMetadataStoreClient,
              actionControlInterface,
              generateIntegrationReport,
              permittedSynchronization,
              externalSourceGUID,
              externalSourceName,
              integrationConnectorGUID,
              auditLog,
              maxPageSize);
    }


    /* ========================================================
     * Set up whether the metadata is owned by the analytics tool
     */


    /**
     * Set up the flag that controls the ownership of metadata created for this Analytics Tool. Default is true.
     *
     * @param analyticsToolIsHome should the topic metadata be marked as owned by the analytics tool so others can not update?
     */
    public void setAnalyticsToolIsHome(boolean analyticsToolIsHome)
    {
        super.externalSourceIsHome = analyticsToolIsHome;
    }
}
