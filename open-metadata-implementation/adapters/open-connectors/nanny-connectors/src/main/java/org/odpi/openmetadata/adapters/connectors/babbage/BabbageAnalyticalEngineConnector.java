/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.babbage;


import org.odpi.openmetadata.adapters.connectors.babbage.ffdc.BabbageAuditCode;
import org.odpi.openmetadata.adapters.connectors.babbage.ffdc.BabbageErrorCode;
import org.odpi.openmetadata.frameworks.connectors.Connector;
import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectorCheckedException;
import org.odpi.openmetadata.frameworks.integration.connectors.DynamicIntegrationConnectorBase;
import org.odpi.openmetadata.frameworks.integration.context.CatalogTargetContext;
import org.odpi.openmetadata.frameworks.integration.properties.RequestedCatalogTarget;
import org.odpi.openmetadata.frameworks.opengovernance.properties.CatalogTarget;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.UserNotAuthorizedException;


/**
 * The Babbage Analytical Engine Connector is an integration connector that ensures that the governance action types
 * attached as catalog targets are regularly invoked to gather and record insight, or, if they are supposed to
 * be long-running, they are still.
 * <p>  
 * All the work is done in the BabbageAnalyticalEngineTargetProcessor.
 */
public class BabbageAnalyticalEngineConnector extends DynamicIntegrationConnectorBase
{
    /**
     * Indicates that the connector is completely configured and can begin processing.
     * It will monitor each of the linked catalog targets and ensure that the governance action types
     * attached as catalog targets are regularly invoked to gather and record insight, or, if they are supposed to
     * be long-running, they are still.
     *
     * @throws ConnectorCheckedException the connector detected a problem.
     */
    @Override
    public  void start() throws ConnectorCheckedException, UserNotAuthorizedException
    {
        super.start();

        final String methodName = "start";

        logRecord(methodName, BabbageAuditCode.STARTING_CONNECTOR.getMessageDefinition(connectorName,
                                                                                       integrationContext.getMetadataAccessServer(),
                                                                                       integrationContext.getMetadataAccessServerPlatformURLRoot()));
    }


    /**
     * Create a new catalog target processor (typically inherits from CatalogTargetProcessorBase).
     *
     * @param retrievedCatalogTarget details of the open metadata elements describing the catalog target
     * @param catalogTargetContext   specialized context for this catalog target
     * @param connectorToTarget      connector to access the target resource
     * @return new processor based on the catalog target information
     * @throws ConnectorCheckedException  a problem with setting up the catalog target.
     * @throws UserNotAuthorizedException the connector has been disconnected
     */
    @Override
    public RequestedCatalogTarget getNewRequestedCatalogTargetSkeleton(CatalogTarget retrievedCatalogTarget, CatalogTargetContext catalogTargetContext, Connector connectorToTarget) throws ConnectorCheckedException, UserNotAuthorizedException
    {
        final String methodName = "getNewRequestedCatalogTargetSkeleton";

        try
        {
            return new BabbageAnalyticalEngineTargetProcessor(retrievedCatalogTarget,
                                                              catalogTargetContext,
                                                              connectorToTarget,
                                                              connectorName,
                                                              auditLog);
        }
        catch (Exception error)
        {
            throw new ConnectorCheckedException(BabbageErrorCode.UNEXPECTED_EXCEPTION.getMessageDefinition(connectorName,
                                                                                                            error.getClass().getName(),
                                                                                                            methodName,
                                                                                                            error.getMessage()),
                                                this.getClass().getName(),
                                                methodName,
                                                error);
        }
    }

    /**
     * Free up any resources held since the connector is no longer needed.
     *
     * @throws ConnectorCheckedException the connector detected a problem.
     */
    public void disconnect() throws ConnectorCheckedException
    {
        final String methodName = "disconnect";

        logRecord(methodName, BabbageAuditCode.CONNECTOR_STOPPING.getMessageDefinition(connectorName,
                                                                                       integrationContext.getMetadataAccessServer(),
                                                                                       integrationContext.getMetadataAccessServerPlatformURLRoot()));

        super.disconnect();
    }
}
