/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.integrationservices.analytics.connector;

import org.odpi.openmetadata.frameworks.governanceaction.client.OpenMetadataClient;
import org.odpi.openmetadata.frameworks.integration.client.OpenIntegrationClient;
import org.odpi.openmetadata.frameworks.integration.context.IntegrationContext;
import org.odpi.openmetadata.frameworks.integration.context.IntegrationGovernanceContext;
import org.odpi.openmetadata.frameworks.integration.contextmanager.PermittedSynchronization;


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
     * @param openMetadataStoreClient client for calling the metadata server
     * @param generateIntegrationReport should the connector generate an integration reports?
     * @param permittedSynchronization the direction of integration permitted by the integration connector
     * @param integrationConnectorGUID unique identifier for the integration connector if it is started via an integration group (otherwise it is
     *                                 null).
     * @param integrationGovernanceContext populated governance context for the connector's use
     * @param externalSourceGUID unique identifier of the software server capability for the api manager
     * @param externalSourceName unique name of the software server capability for the api manager
     */
    public AnalyticsIntegratorContext(String                       connectorId,
                                      String                       connectorName,
                                      String                       connectorUserId,
                                      String                       serverName,
                                      OpenIntegrationClient        openIntegrationClient,
                                      OpenMetadataClient           openMetadataStoreClient,
                                      boolean                      generateIntegrationReport,
                                      PermittedSynchronization     permittedSynchronization,
                                      String                       integrationConnectorGUID,
                                      IntegrationGovernanceContext integrationGovernanceContext,
                                      String                       externalSourceGUID,
                                      String                       externalSourceName)
    {
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
              integrationGovernanceContext);
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
