/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.integration.jdbc;

import org.odpi.openmetadata.frameworks.connectors.Connector;
import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectorCheckedException;
import org.odpi.openmetadata.frameworks.integration.connectors.DynamicIntegrationConnectorBase;
import org.odpi.openmetadata.frameworks.integration.context.CatalogTargetContext;
import org.odpi.openmetadata.frameworks.integration.properties.RequestedCatalogTarget;
import org.odpi.openmetadata.frameworks.opengovernance.properties.CatalogTarget;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.UserNotAuthorizedException;



/**
 * JDBCIntegrationConnector supports the cataloguing of database schema via the JDBC interface.
 */
public class JDBCIntegrationConnector extends DynamicIntegrationConnectorBase
{
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
        return new JDBCIntegrationCatalogTargetProcessor(retrievedCatalogTarget,
                                                         catalogTargetContext,
                                                         connectorToTarget,
                                                         connectorName,
                                                         auditLog);
    }
}
