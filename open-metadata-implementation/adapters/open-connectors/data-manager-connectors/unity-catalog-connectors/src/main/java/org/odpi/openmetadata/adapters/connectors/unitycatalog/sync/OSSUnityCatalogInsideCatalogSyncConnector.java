/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adapters.connectors.unitycatalog.sync;

import org.odpi.openmetadata.adapters.connectors.unitycatalog.ffdc.UCAuditCode;
import org.odpi.openmetadata.adapters.connectors.unitycatalog.resource.OSSUnityCatalogResourceConnector;
import org.odpi.openmetadata.frameworks.connectors.Connector;
import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectorCheckedException;
import org.odpi.openmetadata.frameworks.integration.connectors.DynamicIntegrationConnectorBase;
import org.odpi.openmetadata.frameworks.integration.context.CatalogTargetContext;
import org.odpi.openmetadata.frameworks.opengovernance.properties.CatalogTarget;
import org.odpi.openmetadata.frameworks.integration.properties.RequestedCatalogTarget;
import org.odpi.openmetadata.frameworks.openmetadata.events.OpenMetadataEventListener;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.UserNotAuthorizedException;

/**
 * OSSUnityCatalogInsideCatalogSyncConnector synchronizes metadata within a specific catalog between Unity Catalog and the Open Metadata Ecosystem.
 */
public class OSSUnityCatalogInsideCatalogSyncConnector extends DynamicIntegrationConnectorBase implements OpenMetadataEventListener
{
    /**
     * Indicates that the connector is completely configured and can begin processing.
     *
     * @throws ConnectorCheckedException the connector detected a problem.
     * @throws UserNotAuthorizedException the connector was disconnected before/during start
     */
    @Override
    public void start() throws ConnectorCheckedException, UserNotAuthorizedException
    {
        final String methodName = "start";

        super.start();

        if (connectionBean.getEndpoint() != null)
        {
            auditLog.logMessage(methodName,
                                UCAuditCode.IGNORING_ENDPOINT.getMessageDefinition(connectorName));
        }

        if ((embeddedConnectors != null) && (!embeddedConnectors.isEmpty()))
        {
            for (Connector embeddedConnector : embeddedConnectors)
            {
                if (embeddedConnector instanceof OSSUnityCatalogResourceConnector)
                {
                    auditLog.logMessage(methodName,
                                        UCAuditCode.IGNORING_ENDPOINT.getMessageDefinition(connectorName));
                }
            }
        }
    }


    /**
     * Create a new catalog target processor (typically inherits from CatalogTargetProcessorBase).
     *
     * @param retrievedCatalogTarget details of the open metadata elements describing the catalog target
     * @param catalogTargetContext   specialized context for this catalog target
     * @param connectorToTarget      connector to access the target resource
     * @return new processor based on the catalog target information
     * @throws ConnectorCheckedException  there is a problem with setting up the catalog target.
     * @throws UserNotAuthorizedException the connector has been disconnected
     */
    @Override
    public RequestedCatalogTarget getNewRequestedCatalogTargetSkeleton(CatalogTarget retrievedCatalogTarget, CatalogTargetContext catalogTargetContext, Connector connectorToTarget) throws ConnectorCheckedException, UserNotAuthorizedException
    {
        return new OSSUnityCatalogInsideCatalogSyncCatalogTargetProcessor(retrievedCatalogTarget,
                                                                          catalogTargetContext,
                                                                          connectorToTarget,
                                                                          connectorName,
                                                                          auditLog);
    }
}
