/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.integration.jdbc;

import org.odpi.openmetadata.adapters.connectors.integration.jdbc.transfer.JdbcMetadata;
import org.odpi.openmetadata.adapters.connectors.integration.jdbc.transfer.JdbcMetadataTransfer;
import org.odpi.openmetadata.adapters.connectors.integration.jdbc.transfer.customization.TransferCustomizations;
import org.odpi.openmetadata.adapters.connectors.resource.jdbc.JDBCResourceConnector;
import org.odpi.openmetadata.frameworks.connectors.Connector;
import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectorCheckedException;
import org.odpi.openmetadata.integrationservices.database.connector.DatabaseIntegratorConnector;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.odpi.openmetadata.adapters.connectors.integration.jdbc.ffdc.JDBCIntegrationConnectorAuditCode.EXCEPTION_ON_CONTEXT_RETRIEVAL;
import static org.odpi.openmetadata.adapters.connectors.integration.jdbc.ffdc.JDBCIntegrationConnectorAuditCode.EXCEPTION_READING_JDBC;
import static org.odpi.openmetadata.adapters.connectors.integration.jdbc.ffdc.JDBCIntegrationConnectorAuditCode.EXITING_ON_COMPLETE;
import static org.odpi.openmetadata.adapters.connectors.integration.jdbc.ffdc.JDBCIntegrationConnectorAuditCode.EXITING_ON_CONNECTION_FAIL;
import static org.odpi.openmetadata.adapters.connectors.integration.jdbc.ffdc.JDBCIntegrationConnectorAuditCode.EXITING_ON_INTEGRATION_CONTEXT_FAIL;
import static org.odpi.openmetadata.adapters.connectors.integration.jdbc.ffdc.JDBCIntegrationConnectorAuditCode.EXITING_ON_METADATA_TEST;

/**
 * JDBCIntegrationConnector supports the cataloguing of database schema via the JDBC interface.
 */
public class JDBCIntegrationConnector extends DatabaseIntegratorConnector
{

    private JDBCResourceConnector JDBCResourceConnector;

    /**
     * Set up the list of connectors that this virtual connector will use to support its interface.
     * The connectors are initialized waiting to start.  When start() is called on the
     * virtual connector, it needs to pass start() to each of the embedded connectors. Similarly for
     * disconnect().
     *
     * @param embeddedConnectors  list of connectors
     */
    @Override
    public void initializeEmbeddedConnectors(List<Connector> embeddedConnectors)
    {
        super.initializeEmbeddedConnectors(embeddedConnectors);


        JDBCResourceConnector = (JDBCResourceConnector) embeddedConnectors.get(0);
    }


    /**
     * Requests that the connector does a comparison of the metadata in the third party technology and open metadata repositories.
     * Refresh is called when the integration connector first starts and then at intervals defined in the connector's configuration
     * as well as any external REST API calls to explicitly refresh the connector.
     */
    @Override
    public void refresh()
    {
        String methodName = "JDBCIntegrationConnector.refresh";
        String exitAction = "Exiting " + methodName;

        Connection connection = connect();
        if (connection == null)
        {
            auditLog.logMessage(exitAction, EXITING_ON_CONNECTION_FAIL.getMessageDefinition(methodName));
            return;
        }
        DatabaseMetaData databaseMetaData = getDatabaseMetadata(connection);
        if (databaseMetaData == null)
        {
            auditLog.logMessage(exitAction, EXITING_ON_CONNECTION_FAIL.getMessageDefinition(methodName));
            close(connection);
            return;
        }
        if (!test(databaseMetaData))
        {
            auditLog.logMessage(exitAction, EXITING_ON_METADATA_TEST.getMessageDefinition(methodName));
            close(connection);
            return;
        }

        JdbcMetadataTransfer jdbcMetadataTransfer = createJdbcMetadataTransfer(databaseMetaData);
        if (jdbcMetadataTransfer == null)
        {
            auditLog.logMessage(exitAction, EXITING_ON_INTEGRATION_CONTEXT_FAIL.getMessageDefinition(methodName));
            close(connection);
            return;
        }

        jdbcMetadataTransfer.execute();
        auditLog.logMessage(exitAction, EXITING_ON_COMPLETE.getMessageDefinition(methodName));
        close(connection);
    }


    private Connection connect()
    {
        String methodName = "connect";
        try
        {
            return JDBCResourceConnector.asDataSource().getConnection();
        }
        catch (SQLException sqlException)
        {
            auditLog.logException("Connecting to target database server",
                    EXCEPTION_READING_JDBC.getMessageDefinition(methodName, sqlException.getMessage()), sqlException);
        }

        return null;
    }

    public void close(Connection connection)
    {
        String methodName = "close";
        try
        {
            if (!connection.isClosed())
            {
                connection.close();
            }
        }
        catch (SQLException sqlException)
        {
            auditLog.logException("Closing connection to database server",
                    EXCEPTION_READING_JDBC.getMessageDefinition(methodName, sqlException.getMessage()), sqlException);
        }
    }

    private DatabaseMetaData getDatabaseMetadata(Connection connection)
    {
        String methodName = "getDatabaseMetadata";

        try
        {
            return connection.getMetaData();
        }
        catch (SQLException sqlException)
        {
            auditLog.logException("Extracting database metadata",
                    EXCEPTION_READING_JDBC.getMessageDefinition(methodName, sqlException.getMessage()), sqlException);
        }

        return null;
    }

    private boolean test(DatabaseMetaData databaseMetaData)
    {
        String methodName = "test";
        try
        {
            databaseMetaData.getCatalogs();
            return true;
        }
        catch (SQLException sqlException)
        {
            auditLog.logException("Extracting database metadata",
                    EXCEPTION_READING_JDBC.getMessageDefinition(methodName, sqlException.getMessage()), sqlException);
        }
        return false;
    }

    private JdbcMetadataTransfer createJdbcMetadataTransfer(DatabaseMetaData databaseMetaData){
        String methodName = "createJdbcMetadataTransfer";
        try
        {
            Map<String, Object> configurationProperties = Optional.ofNullable(this.getConnection().getConfigurationProperties()).orElse(new HashMap<>());
            TransferCustomizations transferCustomizations = new TransferCustomizations(configurationProperties);
            String connectorTypeQualifiedName = JDBCResourceConnector.getConnection().getConnectorType().getConnectorProviderClassName();
            String address = JDBCResourceConnector.getConnection().getEndpoint().getAddress();
            String catalog = (String)configurationProperties.get("catalog");
            return new JdbcMetadataTransfer(new JdbcMetadata(databaseMetaData), this.getContext(), address,
                    connectorTypeQualifiedName, catalog, transferCustomizations, auditLog);
        }
        catch (ConnectorCheckedException e)
        {
            auditLog.logException("Extracting integration context",
                    EXCEPTION_ON_CONTEXT_RETRIEVAL.getMessageDefinition(methodName), e);
        }
        return null;
    }

}
