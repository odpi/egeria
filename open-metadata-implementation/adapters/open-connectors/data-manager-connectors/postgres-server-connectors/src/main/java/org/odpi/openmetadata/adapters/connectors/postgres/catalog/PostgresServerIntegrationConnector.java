/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adapters.connectors.postgres.catalog;

import org.odpi.openmetadata.adapters.connectors.postgres.controls.PostgreSQLTemplateType;
import org.odpi.openmetadata.adapters.connectors.postgres.controls.PostgresConfigurationProperty;
import org.odpi.openmetadata.adapters.connectors.postgres.ffdc.PostgresAuditCode;
import org.odpi.openmetadata.adapters.connectors.postgres.ffdc.PostgresErrorCode;
import org.odpi.openmetadata.frameworks.connectors.Connector;
import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectorCheckedException;
import org.odpi.openmetadata.frameworks.integration.connectors.DynamicIntegrationConnectorBase;
import org.odpi.openmetadata.frameworks.integration.context.CatalogTargetContext;
import org.odpi.openmetadata.frameworks.integration.properties.RequestedCatalogTarget;
import org.odpi.openmetadata.frameworks.opengovernance.properties.CatalogTarget;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.UserNotAuthorizedException;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * PostgresServerIntegrationConnector retrieves details of the databases hosted on a PostgreSQL Database Server
 * and creates associated data assets/server capabilities/connections for them.
 */
public class PostgresServerIntegrationConnector extends DynamicIntegrationConnectorBase
{
    List<String> defaultExcludeDatabases = null;
    List<String> defaultIncludeDatabases = null;
    String       defaultFriendshipGUID   = null;
    Map<String, String> defaultTemplates = new HashMap<>();


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

        defaultExcludeDatabases = super.getArrayConfigurationProperty(PostgresConfigurationProperty.EXCLUDE_DATABASE_LIST.getName(),
                                                                      connectionBean.getConfigurationProperties(),
                                                                      Collections.singletonList("postgres"));

        defaultIncludeDatabases = super.getArrayConfigurationProperty(PostgresConfigurationProperty.INCLUDE_DATABASE_LIST.getName(),
                                                                      connectionBean.getConfigurationProperties());

        defaultFriendshipGUID = this.getFriendshipGUID(connectionBean.getConfigurationProperties());

        if (defaultFriendshipGUID != null)
        {
            auditLog.logMessage(methodName,
                                PostgresAuditCode.FRIENDSHIP_GUID.getMessageDefinition(connectorName,
                                                                                       defaultFriendshipGUID));
        }

        for (PostgreSQLTemplateType templateType : PostgreSQLTemplateType.values())
        {
            defaultTemplates.put(templateType.getTemplateName(), templateType.getTemplateGUID());
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
            return new PostgresServerCatalogTargetProcessor(retrievedCatalogTarget,
                                                            catalogTargetContext,
                                                            connectorToTarget,
                                                            connectorName,
                                                            auditLog);
        }
        catch (Exception error)
        {
            throw new ConnectorCheckedException(PostgresErrorCode.UNEXPECTED_EXCEPTION.getMessageDefinition(connectorName,
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
     * @param configurationProperties configuration properties for connection to UC
     * @return friendship GUID or null
     */
    private String getFriendshipGUID(Map<String, Object> configurationProperties)
    {
        String friendshipGUID = defaultFriendshipGUID;

        if ((configurationProperties != null) &&
                (configurationProperties.get(PostgresConfigurationProperty.FRIENDSHIP_GUID.getName()) != null))
        {
            friendshipGUID = connectionBean.getConfigurationProperties().get(PostgresConfigurationProperty.FRIENDSHIP_GUID.getName()).toString();
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
