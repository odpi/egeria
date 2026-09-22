/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.integration.jdbc;

import org.apache.commons.lang3.StringUtils;
import org.odpi.openmetadata.adapters.connectors.integration.jdbc.ffdc.JDBCIntegrationConnectorAuditCode;
import org.odpi.openmetadata.adapters.connectors.integration.jdbc.transfer.JdbcMetadata;
import org.odpi.openmetadata.adapters.connectors.integration.jdbc.transfer.customization.TransferCustomizations;
import org.odpi.openmetadata.adapters.connectors.resource.jdbc.JDBCResourceConnector;
import org.odpi.openmetadata.adapters.connectors.resource.jdbc.controls.JDBCConfigurationProperty;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.connectors.Connector;
import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectorCheckedException;
import org.odpi.openmetadata.frameworks.integration.connectors.CatalogTargetProcessorBase;
import org.odpi.openmetadata.frameworks.integration.context.CatalogTargetContext;
import org.odpi.openmetadata.frameworks.opengovernance.properties.CatalogTarget;
import org.odpi.openmetadata.frameworks.openmetadata.connectorcontext.AssetClient;
import org.odpi.openmetadata.frameworks.openmetadata.enums.ContentStatus;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.ElementControlHeader;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.OpenMetadataRootElement;
import org.odpi.openmetadata.frameworks.openmetadata.search.PropertyHelper;
import org.odpi.openmetadata.frameworks.openmetadata.properties.ReferenceableProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.databases.DatabaseProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.databases.RelationalDatabaseProperties;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;


/**
 * JDBCIntegrationCatalogTargetProcessor supports the cataloguing of database schema via the JDBC interface for
 * a single database linked as a catalog target.
 */
public class JDBCIntegrationCatalogTargetProcessor extends CatalogTargetProcessorBase
{
    /**
     * The additional property RelationalDatabaseCataloguer records the JDBC schema name under on schemas it
     * discovers.  Kept in step with the constant of the same value there.
     */
    private static final String JDBC_SCHEMA_KEY = "jdbc.schema";


    /**
     * What this connector can be pointed at.
     */
    enum CatalogTargetKind
    {
        /**
         * A whole database: every schema in it, and anything sitting directly under it.
         */
        DATABASE,

        /**
         * One schema of a database, and nothing else in that database.
         */
        SCHEMA
    }


    /**
     * Decide which kind of catalog target this is, or null when this connector cannot catalog it.
     * <br><br>
     * A schema has to be recognised in its own right rather than caught by the database check, because
     * DeployedDatabaseSchema is <b>not</b> a subtype of RelationalDatabase: it descends from DataSet, while
     * RelationalDatabase descends from DataStore.  A connector testing only for RelationalDatabase therefore
     * rejects every schema the catalog-&lt;vendor&gt;-schema governance action processes hand it.
     *
     * @param propertyHelper helper that walks the element's supertypes
     * @param elementHeader header of the catalog target element
     * @return the kind, or null when unsupported
     */
    static CatalogTargetKind catalogTargetKind(PropertyHelper       propertyHelper,
                                               ElementControlHeader elementHeader)
    {
        if (propertyHelper.isTypeOf(elementHeader, OpenMetadataType.RELATIONAL_DATABASE.typeName))
        {
            return CatalogTargetKind.DATABASE;
        }

        if (propertyHelper.isTypeOf(elementHeader, OpenMetadataType.DEPLOYED_DATABASE_SCHEMA.typeName))
        {
            return CatalogTargetKind.SCHEMA;
        }

        return null;
    }


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

        CatalogTargetKind kind = catalogTargetKind(propertyHelper, super.getCatalogTargetElement().getElementHeader());

        if (kind == null)
        {
            super.throwWrongTypeOfCatalogTarget(OpenMetadataType.RELATIONAL_DATABASE.typeName + " or " +
                                                        OpenMetadataType.DEPLOYED_DATABASE_SCHEMA.typeName,
                                                methodName);
            return;
        }

        String targetName = null;

        if (super.getCatalogTargetElement().getProperties() instanceof ReferenceableProperties referenceableProperties)
        {
            targetName = referenceableProperties.getQualifiedName();
        }

        /*
         * A catalog target with no configuration properties of its own, and a connector with none either, is
         * handed a null rather than an empty map.  That is the ordinary case for a schema target, whose schema
         * name comes from its connection, so both paths below are given something they can read from.
         */
        Map<String, Object> configurationProperties = super.getConfigurationProperties();

        if (configurationProperties == null)
        {
            configurationProperties = new HashMap<>();
        }

        try
        {
            JDBCResourceConnector assetConnector = (JDBCResourceConnector) super.connectorToTarget;

            if (kind == CatalogTargetKind.DATABASE)
            {
                refreshDatabase(assetConnector,
                                targetName,
                                super.getCatalogTargetElement(),
                                configurationProperties);
            }
            else
            {
                refreshDatabaseSchema(assetConnector,
                                      targetName,
                                      super.getCatalogTargetElement(),
                                      configurationProperties);
            }
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


    /**
     * Refresh a single database schema: connect through the schema asset's own connection and catalog the tables,
     * views and columns of that one schema.
     * <br><br>
     * This is what the catalog-&lt;vendor&gt;-schema governance action processes in the database vendors' content
     * packs set up.  The schema asset already exists - it is the catalog target - so nothing here creates or
     * deletes it, and no database asset is created above it.
     *
     * @param jdbcResourceConnector connector to the database
     * @param schemaName qualified name of the schema asset, for the audit log
     * @param schemaElement the schema asset
     * @param configurationProperties configuration properties from the catalog target
     */
    public void refreshDatabaseSchema(JDBCResourceConnector   jdbcResourceConnector,
                                      String                  schemaName,
                                      OpenMetadataRootElement schemaElement,
                                      Map<String, Object>     configurationProperties) throws ConnectorCheckedException
    {
        final String methodName = "JDBCIntegrationConnector.refreshDatabaseSchema";

        String jdbcSchemaName = this.getJDBCSchemaName(jdbcResourceConnector, schemaElement, configurationProperties);

        if (jdbcSchemaName == null)
        {
            /*
             * Stopping here is deliberate.  Cataloguing without knowing which schema was meant would hang
             * another schema's tables off this asset, and that failure reports success.
             */
            auditLog.logMessage(methodName,
                                JDBCIntegrationConnectorAuditCode.UNKNOWN_DATABASE_SCHEMA.getMessageDefinition(connectorName,
                                                                                                               schemaElement.getElementHeader().getGUID(),
                                                                                                               schemaName));
            return;
        }

        auditLog.logMessage(methodName,
                            JDBCIntegrationConnectorAuditCode.STARTING_SCHEMA_METADATA_TRANSFER.getMessageDefinition(connectorName,
                                                                                                                     jdbcSchemaName,
                                                                                                                     schemaName));

        Connection connection = this.connectToDatabase(schemaName, jdbcResourceConnector);

        if (connection == null)
        {
            return;
        }

        try
        {
            DatabaseMetaData databaseMetaData = connection.getMetaData();

            /*
             * Force an error now rather than part way through, the same way the database level refresh does.
             */
            databaseMetaData.getCatalogs();

            TransferCustomizations transferCustomizations = new TransferCustomizations(configurationProperties);

            String catalog = null;

            if (configurationProperties.get("catalog") != null)
            {
                catalog = configurationProperties.get("catalog").toString();
            }

            RelationalDatabaseCataloguer cataloguer = new RelationalDatabaseCataloguer(integrationContext,
                                                                                        new JdbcMetadata(databaseMetaData),
                                                                                        transferCustomizations,
                                                                                        catalog,
                                                                                        connectorName,
                                                                                        auditLog);

            cataloguer.catalogSchemaContents(schemaElement, jdbcSchemaName);

            auditLog.logMessage(methodName,
                                JDBCIntegrationConnectorAuditCode.EXITING_ON_COMPLETE.getMessageDefinition(connectorName, schemaName));
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
                                                                                                              schemaName,
                                                                                                              exception.getMessage()),
                                  exception);
        }
        finally
        {
            close(connection);
        }
    }


    /**
     * Work out which schema, as the database names it, this schema asset stands for.
     * <br><br>
     * Three places are consulted, in this order, and the schema asset's display name is deliberately not one of
     * them: a schema this connector discovered has its bare JDBC name as its display name, but one built from a
     * vendor content pack's schema template has "database.schema", so the display name means different things
     * depending on which route created the asset.
     * <ol>
     *     <li>The catalog target's own configuration properties, so an operator can override.</li>
     *     <li>The schema asset's connection, which is where the vendor content packs' schema templates put it.</li>
     *     <li>The jdbc.schema additional property, which this connector writes on schemas it discovers itself.</li>
     * </ol>
     *
     * @param jdbcResourceConnector connector to the database
     * @param schemaElement the schema asset
     * @param configurationProperties configuration properties from the catalog target
     * @return the schema name, or null when none of the three say
     */
    private String getJDBCSchemaName(JDBCResourceConnector   jdbcResourceConnector,
                                     OpenMetadataRootElement schemaElement,
                                     Map<String, Object>     configurationProperties)
    {
        String schemaName = super.getStringConfigurationProperty(JDBCConfigurationProperty.DATABASE_SCHEMA.getName(),
                                                                 configurationProperties);

        if (schemaName == null)
        {
            if ((jdbcResourceConnector.getConnection() != null)
                        && (jdbcResourceConnector.getConnection().getConfigurationProperties() != null))
            {
                Object connectionValue = jdbcResourceConnector.getConnection()
                                                              .getConfigurationProperties()
                                                              .get(JDBCConfigurationProperty.DATABASE_SCHEMA.getName());

                if (connectionValue != null)
                {
                    schemaName = connectionValue.toString();
                }
            }
        }

        if ((schemaName == null)
                    && (schemaElement.getProperties() instanceof ReferenceableProperties referenceableProperties)
                    && (referenceableProperties.getAdditionalProperties() != null))
        {
            schemaName = referenceableProperties.getAdditionalProperties().get(JDBC_SCHEMA_KEY);
        }

        return StringUtils.isBlank(schemaName) ? null : schemaName;
    }


    /**
     * Refresh a single database: connect to it, resolve (or create) its database asset, and then hand off the
     * schema/table/view/column cataloguing to a {@link RelationalDatabaseCataloguer}.
     *
     * @param jdbcResourceConnector connector to the database
     * @param databaseName qualified name of the database
     * @param databaseElement the already-known database asset, or null if this catalog target is a database server address
     * @param configurationProperties configuration properties for the database
     */
    public void refreshDatabase(JDBCResourceConnector   jdbcResourceConnector,
                                String                  databaseName,
                                OpenMetadataRootElement databaseElement,
                                Map<String, Object>     configurationProperties) throws ConnectorCheckedException
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

                TransferCustomizations transferCustomizations = new TransferCustomizations(configurationProperties);

                String address = jdbcResourceConnector.getConnection().getEndpoint().getNetworkAddress();
                String catalog = null;

                if (configurationProperties.get("catalog") != null)
                {
                    catalog = configurationProperties.get("catalog").toString();
                }

                JdbcMetadata jdbcMetadata = new JdbcMetadata(databaseMetaData);

                OpenMetadataRootElement resolvedDatabaseElement = this.catalogDatabase(jdbcMetadata, databaseElement, address, catalog);

                if (resolvedDatabaseElement != null)
                {
                    RelationalDatabaseCataloguer cataloguer = new RelationalDatabaseCataloguer(integrationContext,
                                                                                                jdbcMetadata,
                                                                                                transferCustomizations,
                                                                                                catalog,
                                                                                                connectorName,
                                                                                                auditLog);

                    cataloguer.catalogDatabaseContents(resolvedDatabaseElement);
                }
                else
                {
                    auditLog.logMessage("Verifying database metadata transferred. None found. Stopping transfer",
                                        JDBCIntegrationConnectorAuditCode.EXITING_ON_DATABASE_TRANSFER_FAIL.getMessageDefinition(methodName));
                }

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
            finally
            {
                /*
                 * The connection must go back to the pool on every path, including one where an error escapes the
                 * catch blocks above.
                 */
                close(connection);
            }
        }
    }


    /**
     * Resolve the database asset for this catalog target: if it is already known (the catalog target points directly at
     * a database asset) it is returned as-is; otherwise a database asset is looked up by its deterministic qualified name
     * and either updated (if found) or created.
     *
     * @param jdbcMetadata JDBC metadata access
     * @param databaseElement the already-known database asset, or null
     * @param address network address of the database
     * @param catalog optional display name override
     * @return the database asset, or null if it could not be resolved
     */
    private OpenMetadataRootElement catalogDatabase(JdbcMetadata            jdbcMetadata,
                                                    OpenMetadataRootElement databaseElement,
                                                    String                  address,
                                                    String                  catalog) throws SQLException
    {
        if (databaseElement != null)
        {
            return databaseElement;
        }

        final String methodName = "catalogDatabase";

        DatabaseProperties databaseProperties = buildDatabaseProperties(jdbcMetadata, address, catalog);

        AssetClient databaseClient = integrationContext.getAssetClient(OpenMetadataType.DATABASE.typeName);

        try
        {
            OpenMetadataRootElement existingDatabase = databaseClient.getAssetByUniqueName(databaseProperties.getQualifiedName(),
                                                                                            OpenMetadataProperty.QUALIFIED_NAME.name,
                                                                                            databaseClient.getGetOptions());

            if (existingDatabase != null)
            {
                databaseClient.updateAsset(existingDatabase.getElementHeader().getGUID(),
                                           databaseClient.getUpdateOptions(false),
                                           databaseProperties);
                auditLog.logMessage(methodName,
                                    JDBCIntegrationConnectorAuditCode.TRANSFER_COMPLETE_FOR_DB_OBJECT.getMessageDefinition("database " + databaseProperties.getQualifiedName()));
                return existingDatabase;
            }

            String databaseGUID = databaseClient.createAsset(null, null, databaseProperties, null);
            auditLog.logMessage(methodName,
                                JDBCIntegrationConnectorAuditCode.TRANSFER_COMPLETE_FOR_DB_OBJECT.getMessageDefinition("database " + databaseProperties.getQualifiedName()));

            return databaseClient.getAssetByGUID(databaseGUID, databaseClient.getGetOptions());
        }
        catch (InvalidParameterException | PropertyServerException | UserNotAuthorizedException e)
        {
            auditLog.logException(methodName,
                                  JDBCIntegrationConnectorAuditCode.EXCEPTION_WRITING_OMAS.getMessageDefinition(e.getClass().getName(), methodName, e.getMessage()),
                                  e);
            return null;
        }
    }


    /**
     * Build the properties for the database asset.
     *
     * @param jdbcMetadata JDBC metadata access
     * @param address network address of the database
     * @param catalog optional display name override
     * @return properties
     */
    private DatabaseProperties buildDatabaseProperties(JdbcMetadata jdbcMetadata,
                                                       String       address,
                                                       String       catalog) throws SQLException
    {
        String driverName             = jdbcMetadata.getDriverName();
        String databaseProductVersion = jdbcMetadata.getDatabaseProductVersion();
        String databaseProductName    = jdbcMetadata.getDatabaseProductName();
        String url                    = jdbcMetadata.getUrl();

        RelationalDatabaseProperties databaseProperties = new RelationalDatabaseProperties();
        databaseProperties.setQualifiedName(integrationContext.getMetadataSourceQualifiedName() + "::" + address);
        databaseProperties.setDisplayName(StringUtils.isBlank(catalog) ? address : catalog);
        databaseProperties.setContentStatus(ContentStatus.ACTIVE);
        databaseProperties.setDatabaseInstance(driverName);
        databaseProperties.setVersionIdentifier(databaseProductVersion);
        databaseProperties.setDeployedImplementationType(databaseProductName);
        databaseProperties.setImportedFrom(url);

        return databaseProperties;
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
