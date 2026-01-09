/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.integration.jdbc;

import org.odpi.openmetadata.adapters.connectors.integration.jdbc.ffdc.JDBCIntegrationConnectorAuditCode;
import org.odpi.openmetadata.adapters.connectors.integration.jdbc.transfer.JdbcMetadata;
import org.odpi.openmetadata.adapters.connectors.integration.jdbc.transfer.JdbcMetadataTransfer;
import org.odpi.openmetadata.adapters.connectors.integration.jdbc.transfer.customization.TransferCustomizations;
import org.odpi.openmetadata.adapters.connectors.resource.jdbc.JDBCResourceConnector;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.connectors.Connector;
import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectorCheckedException;
import org.odpi.openmetadata.frameworks.integration.connectors.CatalogTargetIntegrator;
import org.odpi.openmetadata.frameworks.integration.connectors.CatalogTargetProcessorBase;
import org.odpi.openmetadata.frameworks.integration.connectors.DynamicIntegrationConnectorBase;
import org.odpi.openmetadata.frameworks.integration.context.CatalogTargetContext;
import org.odpi.openmetadata.frameworks.integration.properties.RequestedCatalogTarget;
import org.odpi.openmetadata.frameworks.opengovernance.properties.CatalogTarget;
import org.odpi.openmetadata.frameworks.openmetadata.connectorcontext.AssetClient;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.OpenMetadataRootElement;
import org.odpi.openmetadata.frameworks.openmetadata.properties.ReferenceableProperties;
import org.odpi.openmetadata.frameworks.openmetadata.search.PropertyHelper;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.util.Map;


/**
 * JDBCIntegrationCatalogTargetProcessor supports the cataloguing of database schema via the JDBC interface for
 * a single database that is linked as a catalog target.
 */
public class JDBCIntegrationCatalogTargetProcessor extends CatalogTargetProcessorBase
{
    /**
     * Primary constructor
     *
     * @param template object to copy
     * @param catalogTargetContext specialized context for this catalog target
     * @param connectorToTarget connector to access the target resource
     * @param connectorName name of this integration connector
     * @param auditLog logging destination
     */
    public JDBCIntegrationCatalogTargetProcessor(CatalogTarget        template,
                                                 CatalogTargetContext catalogTargetContext,
                                                 Connector            connectorToTarget,
                                                 String               connectorName,
                                                 AuditLog             auditLog)
    {
        super(template, catalogTargetContext, connectorToTarget, connectorName, auditLog);
    }


    /**
     * Requests that the connector does a comparison of the metadata in the third party technology and open metadata repositories.
     * Refresh is called when the integration connector first starts and then at intervals defined in the connector's configuration
     * as well as any external REST API calls to explicitly refresh the connector.
     * @throws ConnectorCheckedException the connector detected a problem.
     */
    @Override
    public void refresh() throws ConnectorCheckedException
    {
        final String methodName = "refresh";

        if (propertyHelper.isTypeOf(super.getCatalogTargetElement().getElementHeader(), OpenMetadataType.RELATIONAL_DATABASE.typeName))
        {
            String databaseGUID = super.getCatalogTargetElement().getElementHeader().getGUID();
            String databaseName = null;

            if (super.getCatalogTargetElement().getProperties() instanceof ReferenceableProperties referenceableProperties)
            {
                databaseName = referenceableProperties.getQualifiedName();
            }

            try
            {
                JDBCResourceConnector assetConnector = (JDBCResourceConnector)super.connectorToTarget;

                refreshDatabase(assetConnector,
                                databaseName,
                                super.getCatalogTargetElement(),
                                super.getConfigurationProperties());
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
            super.throwWrongTypeOfCatalogTarget(OpenMetadataType.RELATIONAL_DATABASE.typeName,
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
                                OpenMetadataRootElement          databaseElement,
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
                String address = jdbcResourceConnector.getConnection().getEndpoint().getNetworkAddress();
                String catalog = "";

                if (configurationProperties.get("catalog") != null)
                {
                    catalog = configurationProperties.get("catalog").toString();
                }

                JdbcMetadataTransfer jdbcMetadataTransfer = new JdbcMetadataTransfer(new JdbcMetadata(databaseMetaData),
                                                                                     integrationContext,
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
