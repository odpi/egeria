/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.integration.jdbc;

import org.odpi.openmetadata.adapters.connectors.integration.jdbc.ffdc.JDBCIntegrationConnectorAuditCode;
import org.odpi.openmetadata.adapters.connectors.integration.jdbc.transfer.JdbcMetadata;
import org.odpi.openmetadata.adapters.connectors.integration.jdbc.transfer.JdbcMetadataTransfer;
import org.odpi.openmetadata.adapters.connectors.integration.jdbc.transfer.customization.TransferCustomizations;
import org.odpi.openmetadata.adapters.connectors.resource.jdbc.JDBCResourceConnector;
import org.odpi.openmetadata.frameworks.connectors.Connector;
import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectorCheckedException;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.DatabaseElement;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;
import org.odpi.openmetadata.frameworks.governanceaction.search.PropertyHelper;
import org.odpi.openmetadata.frameworks.integration.connectors.CatalogTargetIntegrator;
import org.odpi.openmetadata.frameworks.integration.properties.RequestedCatalogTarget;
import org.odpi.openmetadata.integrationservices.database.connector.DatabaseIntegratorConnector;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.util.Map;



/**
 * JDBCIntegrationConnector supports the cataloguing of database schema via the JDBC interface.
 */
public class JDBCIntegrationConnector extends DatabaseIntegratorConnector implements CatalogTargetIntegrator
{
    final PropertyHelper propertyHelper = new PropertyHelper();

    /**
     * Requests that the connector does a comparison of the metadata in the third party technology and open metadata repositories.
     * Refresh is called when the integration connector first starts and then at intervals defined in the connector's configuration
     * as well as any external REST API calls to explicitly refresh the connector.
     * @throws ConnectorCheckedException there is a problem within the connector.
     */
    @Override
    public void refresh() throws ConnectorCheckedException
    {
        final String methodName = "refresh";

        if ((embeddedConnectors != null) && (!embeddedConnectors.isEmpty()))
        {
            for (Connector embeddedConnector : embeddedConnectors)
            {
                if (embeddedConnector instanceof JDBCResourceConnector jdbcResourceConnector)
                {
                    try
                    {
                        refreshDatabase(jdbcResourceConnector,
                                        jdbcResourceConnector.getConnection().getConnectionName(),
                                        null,
                                        connectionProperties.getConfigurationProperties());
                    }
                    catch (ConnectorCheckedException exception)
                    {
                        throw exception;
                    }
                    catch (Exception exception)
                    {
                        auditLog.logException(methodName,
                                              JDBCIntegrationConnectorAuditCode.UNEXPECTED_EXCEPTION.getMessageDefinition(connectorName,
                                                                                                                          exception.getClass().getName(),
                                                                                                                          methodName,
                                                                                                                          jdbcResourceConnector.getConnection().getConnectionName(),
                                                                                                                          exception.getMessage()),
                                              exception);
                    }
                }
            }
        }

        this.refreshCatalogTargets(this);
    }


    /**
     * Perform the required integration logic for the assigned catalog target.
     *
     * @param requestedCatalogTarget the catalog target
     * @throws ConnectorCheckedException there is an unrecoverable error and the connector should stop processing.
     */
    @Override
    public void integrateCatalogTarget(RequestedCatalogTarget requestedCatalogTarget) throws ConnectorCheckedException
    {
        final String methodName = "integrateCatalogTarget";

        if (propertyHelper.isTypeOf(requestedCatalogTarget.getCatalogTargetElement(), OpenMetadataType.RELATIONAL_DATABASE.typeName))
        {
            String databaseGUID = requestedCatalogTarget.getCatalogTargetElement().getGUID();
            String databaseName = requestedCatalogTarget.getCatalogTargetElement().getUniqueName();

            try
            {
                DatabaseElement databaseElement = getContext().getDatabaseByGUID(databaseGUID);

                Connector connector = getContext().getConnectedAssetContext().getConnectorToAsset(databaseGUID, auditLog);

                JDBCResourceConnector assetConnector = (JDBCResourceConnector)connector;

                refreshDatabase(assetConnector,
                                databaseName,
                                databaseElement,
                                requestedCatalogTarget.getConfigurationProperties());
            }
            catch (Exception exception)
            {
                auditLog.logException(methodName,
                                      JDBCIntegrationConnectorAuditCode.UNEXPECTED_EXCEPTION.getMessageDefinition(connectorName,
                                                                                                                  exception.getClass().getName(),
                                                                                                                  methodName,
                                                                                                                  exception.getMessage()),
                                      exception);
            }
        }
        else
        {
            super.throwWrongTypeOfAsset(requestedCatalogTarget.getCatalogTargetElement().getGUID(),
                                        requestedCatalogTarget.getCatalogTargetElement().getType().getTypeName(),
                                        OpenMetadataType.RELATIONAL_DATABASE.typeName,
                                        connectorName,
                                        methodName);
        }
    }


    /**
     * Refresh a single database.
     *
     * @param jdbcResourceConnector connector to the database
     * @param databaseName qualified name of the database
     * @param configurationProperties configuration properties for the database
     */
    public void refreshDatabase(JDBCResourceConnector jdbcResourceConnector,
                                String                databaseName,
                                DatabaseElement       databaseElement,
                                Map<String, Object>   configurationProperties) throws ConnectorCheckedException
    {
        final String methodName = "JDBCIntegrationConnector.refresh";

        auditLog.logMessage(methodName,
                            JDBCIntegrationConnectorAuditCode.STARTING_METADATA_TRANSFER.getMessageDefinition(connectorName,
                                                                                                              databaseName));

        Connection connection = this.connectToDatabase(databaseName, jdbcResourceConnector);

        if (connection != null)
        {
            try
            {
                /*
                 * This object gives access to the metadata catalog.
                 */
                DatabaseMetaData databaseMetaData = connection.getMetaData();

                /*
                 * This method checks that the catalog is accessible.
                 * The aim is to force an error before going much further.
                 */
                databaseMetaData.getCatalogs();

                /*
                 * Extract the meaningful values from the configuration properties.
                 */
                TransferCustomizations transferCustomizations = new TransferCustomizations(configurationProperties);

                /*
                 * Create the object that does all the work.
                 */
                String address = jdbcResourceConnector.getConnection().getEndpoint().getAddress();
                String catalog = "";

                if (configurationProperties.get("catalog") != null)
                {
                    catalog = configurationProperties.get("catalog").toString();
                }

                JdbcMetadataTransfer jdbcMetadataTransfer = new JdbcMetadataTransfer(new JdbcMetadata(databaseMetaData),
                                                                                     getContext(),
                                                                                     databaseElement,
                                                                                     address,
                                                                                     catalog,
                                                                                     transferCustomizations,
                                                                                     auditLog);

                /*
                 * Extract metadata.
                 */
                jdbcMetadataTransfer.execute();

                auditLog.logMessage(methodName, JDBCIntegrationConnectorAuditCode.EXITING_ON_COMPLETE.getMessageDefinition(connectorName, databaseName));
            }
            catch (SQLException sqlException)
            {
                auditLog.logException(methodName,
                                      JDBCIntegrationConnectorAuditCode.EXCEPTION_READING_JDBC.getMessageDefinition(methodName, sqlException.getMessage()),
                                      sqlException);
            }
            catch (ConnectorCheckedException error)
            {
                throw error;
            }
            catch (Exception exception)
            {
                auditLog.logException(methodName,
                                      JDBCIntegrationConnectorAuditCode.UNEXPECTED_EXCEPTION.getMessageDefinition(connectorName,
                                                                                                                  exception.getClass().getName(),
                                                                                                                  methodName,
                                                                                                                  databaseName,
                                                                                                                  exception.getMessage()),
                                      exception);
            }

            close(connection);
        }
    }


    /**
     * Connect to the database.
     *
     * @param jdbcResourceConnector connector to the database
     * @return JDBC database connection
     */
    private Connection connectToDatabase(String                databaseName,
                                         JDBCResourceConnector jdbcResourceConnector)
    {
        final String methodName = "connectToDatabase";

        try
        {
            return jdbcResourceConnector.getDataSource().getConnection();
        }
        catch (SQLException sqlException)
        {
            auditLog.logException(methodName,
                                  JDBCIntegrationConnectorAuditCode.CONNECTION_FAILED.getMessageDefinition(connectorName,
                                                                                                           databaseName,
                                                                                                           sqlException.getClass().getName(),
                                                                                                           sqlException.getMessage()),
                                  sqlException);
        }

        return null;
    }


    /**
     * Work has completed, close the database connection.
     *
     * @param connection database connection
     */
    public void close(Connection connection)
    {
        String methodName = "close";
        try
        {
            connection.commit();
            if (!connection.isClosed())
            {
                connection.close();
            }
        }
        catch (SQLException sqlException)
        {
            auditLog.logException("Closing connection to database server",
                                  JDBCIntegrationConnectorAuditCode.EXCEPTION_READING_JDBC.getMessageDefinition(methodName, sqlException.getMessage()), sqlException);
        }
    }
}
