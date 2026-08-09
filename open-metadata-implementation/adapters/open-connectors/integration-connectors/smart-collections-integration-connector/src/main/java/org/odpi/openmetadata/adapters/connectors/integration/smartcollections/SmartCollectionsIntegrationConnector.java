/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.integration.smartcollections;

import org.odpi.openmetadata.adapters.connectors.integration.smartcollections.ffdc.SmartCollectionsErrorCode;
import org.odpi.openmetadata.commonservices.ffdc.rest.FFDCRESTClientBase;
import org.odpi.openmetadata.frameworks.connectors.Connector;
import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectorCheckedException;
import org.odpi.openmetadata.frameworks.integration.connectors.DynamicIntegrationConnectorBase;
import org.odpi.openmetadata.frameworks.integration.context.CatalogTargetContext;
import org.odpi.openmetadata.frameworks.integration.properties.RequestedCatalogTarget;
import org.odpi.openmetadata.frameworks.opengovernance.properties.CatalogTarget;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.UserNotAuthorizedException;


/**
 * SmartCollectionsIntegrationConnector maintains the membership of Results Set collections that are linked as catalog targets.
 * For each results set, it navigates the SmartQuery relationship to find the SavedQuery that defines how to work out the
 * current membership, issues the query's REST call and adjusts the results set's membership to match the elements returned.
 */
public class SmartCollectionsIntegrationConnector extends DynamicIntegrationConnectorBase
{
    /**
     * Indicates that the connector is completely configured and can begin processing.  It checks that a secret is
     * available for authenticating the REST calls it will issue to run each results set's saved query.
     *
     * @throws ConnectorCheckedException the connector detected a problem.
     */
    @Override
    public void start() throws ConnectorCheckedException, UserNotAuthorizedException
    {
        super.start();

        final String methodName = "start";

        if ((secretsStoreConnectorMap == null) || (secretsStoreConnectorMap.isEmpty()))
        {
            throw new ConnectorCheckedException(SmartCollectionsErrorCode.NO_SECRETS.getMessageDefinition(connectorName),
                                                this.getClass().getName(),
                                                methodName);
        }
    }


    /**
     * Create a new catalog target processor for a results set catalog target.
     *
     * @param retrievedCatalogTarget details of the open metadata elements describing the catalog target
     * @param catalogTargetContext   specialized context for this catalog target
     * @param connectorToTarget      connector to access the target resource (not used - the query is run over REST)
     * @return new processor based on the catalog target information
     * @throws ConnectorCheckedException  a problem with setting up the catalog target.
     * @throws UserNotAuthorizedException the connector has been disconnected
     */
    @Override
    public RequestedCatalogTarget getNewRequestedCatalogTargetSkeleton(CatalogTarget        retrievedCatalogTarget,
                                                                       CatalogTargetContext catalogTargetContext,
                                                                       Connector            connectorToTarget) throws ConnectorCheckedException, UserNotAuthorizedException
    {
        final String methodName = "getNewRequestedCatalogTargetSkeleton";

        try
        {
            FFDCRESTClientBase restClient = new FFDCRESTClientBase(integrationContext.getMetadataAccessServer(),
                                                                    integrationContext.getMetadataAccessServerPlatformURLRoot(),
                                                                    secretsStoreConnectorMap,
                                                                    auditLog);

            return new SmartCollectionsCatalogTargetProcessor(retrievedCatalogTarget,
                                                              catalogTargetContext,
                                                              connectorToTarget,
                                                              connectorName,
                                                              restClient,
                                                              auditLog);
        }
        catch (InvalidParameterException error)
        {
            throw new ConnectorCheckedException(SmartCollectionsErrorCode.UNEXPECTED_EXCEPTION.getMessageDefinition(connectorName,
                                                                                                                    error.getClass().getName(),
                                                                                                                    methodName,
                                                                                                                    error.getMessage()),
                                                this.getClass().getName(),
                                                methodName,
                                                error);
        }
    }
}
