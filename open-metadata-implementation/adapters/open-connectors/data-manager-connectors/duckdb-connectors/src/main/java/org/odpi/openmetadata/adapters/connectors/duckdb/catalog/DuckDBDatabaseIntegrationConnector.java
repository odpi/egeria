/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adapters.connectors.duckdb.catalog;

import org.odpi.openmetadata.adapters.connectors.duckdb.controls.DuckDBConfigurationProperty;
import org.odpi.openmetadata.adapters.connectors.duckdb.ffdc.DuckDBAuditCode;
import org.odpi.openmetadata.adapters.connectors.duckdb.ffdc.DuckDBErrorCode;
import org.odpi.openmetadata.frameworks.connectors.Connector;
import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectorCheckedException;
import org.odpi.openmetadata.frameworks.integration.connectors.DynamicIntegrationConnectorBase;
import org.odpi.openmetadata.frameworks.integration.context.CatalogTargetContext;
import org.odpi.openmetadata.frameworks.integration.properties.RequestedCatalogTarget;
import org.odpi.openmetadata.frameworks.opengovernance.properties.CatalogTarget;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.UserNotAuthorizedException;

import java.util.Map;

/**
 * DuckDBDatabaseIntegrationConnector catalogs a single DuckDB database (an embedded ".duckdb" file, or an
 * in-memory ":memory:" session).  DuckDB has no server tier, so - unlike the equivalent connectors for the other
 * database technologies supported by Egeria - there is no server-level hand-off logic here: each catalog target
 * is processed directly by a DuckDBDatabaseCatalogTargetProcessor.
 */
public class DuckDBDatabaseIntegrationConnector extends DynamicIntegrationConnectorBase
{
    String defaultFriendshipGUID = null;


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

        defaultFriendshipGUID = this.getFriendshipGUID(connectionBean.getConfigurationProperties());

        if (defaultFriendshipGUID != null)
        {
            logRecord(methodName,
                      DuckDBAuditCode.FRIENDSHIP_GUID.getMessageDefinition(connectorName,
                                                                           defaultFriendshipGUID));
        }
    }


    /**
     * Create a new catalog target processor (typically inherits from CatalogTargetProcessorBase).
     *
     * @param retrievedCatalogTarget details of the open metadata elements describing the catalog target
     * @param catalogTargetContext specialized context for this catalog target
     * @param connectorToTarget connector to access the target resource
     * @return new processor based on the catalog target information
     */
    @Override
    public RequestedCatalogTarget getNewRequestedCatalogTargetSkeleton(CatalogTarget        retrievedCatalogTarget,
                                                                       CatalogTargetContext catalogTargetContext,
                                                                       Connector            connectorToTarget) throws ConnectorCheckedException
    {
        final String methodName = "getNewRequestedCatalogTargetSkeleton";

        try
        {
            return new DuckDBDatabaseCatalogTargetProcessor(retrievedCatalogTarget,
                                                             catalogTargetContext,
                                                             connectorToTarget,
                                                             connectorName,
                                                             auditLog);
        }
        catch (Exception error)
        {
            throw new ConnectorCheckedException(DuckDBErrorCode.UNEXPECTED_EXCEPTION.getMessageDefinition(connectorName,
                                                                                                            error.getClass().getName(),
                                                                                                            methodName,
                                                                                                            error.getMessage()),
                                                this.getClass().getName(),
                                                methodName,
                                                error);
        }
    }


    /**
     * Extract the friendship GUID from the configuration properties - or use the default.
     *
     * @param configurationProperties configuration properties for connection to the DuckDB database
     * @return friendship GUID or null
     */
    private String getFriendshipGUID(Map<String, Object> configurationProperties)
    {
        String friendshipGUID = defaultFriendshipGUID;

        if ((configurationProperties != null) &&
                (configurationProperties.get(DuckDBConfigurationProperty.FRIENDSHIP_GUID.getName()) != null))
        {
            friendshipGUID = connectionBean.getConfigurationProperties().get(DuckDBConfigurationProperty.FRIENDSHIP_GUID.getName()).toString();
        }

        return friendshipGUID;
    }


    /**
     * Free up any resources held since the connector is no longer needed.
     *
     * @throws ConnectorCheckedException the connector detected a problem.
     */
    @Override
    public  void disconnect() throws ConnectorCheckedException
    {
        /*
         * This disconnects any embedded connections such as secrets connectors.
         */
        super.disconnect();
    }
}
