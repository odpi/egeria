/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.integration.kafkaaudit;

import org.odpi.openmetadata.adapters.connectors.datastore.basicfile.BasicFolderConnector;
import org.odpi.openmetadata.adapters.connectors.integration.kafkaaudit.ffdc.DistributeKafkaAuditCode;
import org.odpi.openmetadata.adapters.connectors.resource.jdbc.JDBCResourceConnector;
import org.odpi.openmetadata.adapters.repositoryservices.auditlogstore.console.ConsoleAuditLogStoreProvider;
import org.odpi.openmetadata.adapters.repositoryservices.auditlogstore.file.FileBasedAuditLogStoreProvider;
import org.odpi.openmetadata.adapters.repositoryservices.auditlogstore.postgres.PostgreSQLAuditLogDestinationProvider;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.connectors.Connector;
import org.odpi.openmetadata.frameworks.connectors.ConnectorBroker;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.Connection;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.ConnectorType;
import org.odpi.openmetadata.frameworks.opengovernance.properties.CatalogTarget;
import org.odpi.openmetadata.frameworks.integration.connectors.CatalogTargetProcessorBase;
import org.odpi.openmetadata.frameworks.integration.context.CatalogTargetContext;
import org.odpi.openmetadata.repositoryservices.connectors.stores.auditlogstore.OMRSAuditLogRecord;
import org.odpi.openmetadata.repositoryservices.connectors.stores.auditlogstore.OMRSAuditLogStoreConnectorBase;

/**
 * Distributes audit log events from one or more embedded topic connectors to one or more embedded audit log destinations.
 */
public class AuditLogDestinationCatalogTargetProcessor extends CatalogTargetProcessorBase
{
    private OMRSAuditLogStoreConnectorBase auditLogDestination = null;


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     * @param catalogTargetContext specialized context for this catalog target
     * @param connectorToTarget connector to access the target resource
     * @param connectorName name of this integration connector
     * @param auditLog logging destination
     */
    public AuditLogDestinationCatalogTargetProcessor(CatalogTarget        template,
                                                     CatalogTargetContext catalogTargetContext,
                                                     Connector            connectorToTarget,
                                                     String               connectorName,
                                                     AuditLog             auditLog)
    {
        super(template, catalogTargetContext, connectorToTarget, connectorName, auditLog);

        final String methodName = "AuditLogDestinationCatalogTargetProcessor constructor";

        Connection auditLogConnection;

        if (connectorToTarget instanceof JDBCResourceConnector)
        {
            auditLogConnection = getAuditLogConnection(connectorToTarget,
                                                       new PostgreSQLAuditLogDestinationProvider().getConnectorType());
        }
        else if (connectorToTarget instanceof BasicFolderConnector)
        {
            auditLogConnection = getAuditLogConnection(connectorToTarget,
                                                       new FileBasedAuditLogStoreProvider().getConnectorType());
        }
        else
        {
            auditLogConnection = getAuditLogConnection(connectorToTarget,
                                                       new ConsoleAuditLogStoreProvider().getConnectorType());
        }

        try
        {
            ConnectorBroker connectorBroker = new ConnectorBroker(auditLog);

            this.auditLogDestination = (OMRSAuditLogStoreConnectorBase) connectorBroker.getConnector(auditLogConnection);
        }
        catch (Exception error)
        {
            auditLog.logMessage(methodName,
                                DistributeKafkaAuditCode.UNEXPECTED_EXCEPTION.getMessageDefinition(connectorName,
                                                                                                   error.getClass().getName(),
                                                                                                   methodName,
                                                                                                   error.getMessage()));
        }
    }


    /**
     * Create a connection for the audit log connector.
     *
     * @param assetConnector destination
     * @param auditLogConnectorType appropriate audit log connector
     * @return connection
     */
    private Connection getAuditLogConnection(Connector     assetConnector,
                                             ConnectorType auditLogConnectorType)
    {
        Connection newConnection = new Connection(assetConnector.getConnection());

        newConnection.setConnectorType(auditLogConnectorType);

        return newConnection;
    }


    /**
     * Store the audit log record in the audit log store.
     *
     * @param logRecord log record to store
     */
    public void storeLogRecord(OMRSAuditLogRecord logRecord)
    {
        final String methodName = "storeLogRecord";

        if (auditLogDestination != null)
        {
            try
            {
                auditLogDestination.storeLogRecord(logRecord);
            }
            catch (Exception error)
            {
                auditLog.logException(methodName,
                                      DistributeKafkaAuditCode.UNEXPECTED_EXCEPTION.getMessageDefinition(connectorName,
                                                                                                         error.getClass().getName(),

                                                                                                         error.getMessage()),
                                      error);
            }
        }
    }


    /**
     * Requests that the connector does a comparison of the metadata in the third party technology and open metadata repositories.
     * Refresh is called when the integration connector first starts and then at intervals defined in the connector's configuration
     * as well as any external REST API calls to explicitly refresh the connector.
     */
    @Override
    public void refresh()
    {
        // nothing to do
    }
}
