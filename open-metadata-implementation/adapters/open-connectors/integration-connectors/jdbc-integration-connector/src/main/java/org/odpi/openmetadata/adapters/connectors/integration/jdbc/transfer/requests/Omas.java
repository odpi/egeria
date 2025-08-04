/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.integration.jdbc.transfer.requests;

import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.integration.context.IntegrationContext;
import org.odpi.openmetadata.frameworks.openmetadata.connectorcontext.*;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.*;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.databases.DatabaseProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.databases.DeployedDatabaseSchemaProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.connections.EndpointProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.schema.CalculatedValueProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.schema.ForeignKeyProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.schema.PrimaryKeyProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.schema.databases.RelationalColumnProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.schema.databases.RelationalTableProperties;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Utility class that delegates requests to designated access service
 */
public class Omas {

    private final AssetClient           dataAssetClient;
    private final AssetClient           databaseClient;
    private final AssetClient           databaseSchemaClient;
    private final SchemaTypeClient      databaseSchemaTypeClient;
    private final SchemaAttributeClient databaseTableClient;
    private final SchemaAttributeClient databaseViewClient;
    private final SchemaAttributeClient databaseColumnClient;
    private final EndpointClient        endpointClient;
    private final ConnectionClient      connectionClient;
    private final ConnectorTypeClient   connectorTypeClient;
    private final AuditLog              auditLog;


    /**
     * This is the wrapper for the calls to open metadata.
     *
     * @param integrationContext context for this connector
     * @param auditLog logging destination
     */
    public Omas(IntegrationContext integrationContext,
                AuditLog             auditLog)
    {
        dataAssetClient          = integrationContext.getAssetClient(OpenMetadataType.DATA_ASSET.typeName);
        databaseClient           = integrationContext.getAssetClient(OpenMetadataType.DATABASE.typeName);
        databaseSchemaClient     = integrationContext.getAssetClient(OpenMetadataType.DEPLOYED_DATABASE_SCHEMA.typeName);
        databaseSchemaTypeClient = integrationContext.getSchemaTypeClient(OpenMetadataType.RELATIONAL_DB_SCHEMA_TYPE.typeName);
        databaseTableClient      = integrationContext.getSchemaAttributeClient(OpenMetadataType.RELATIONAL_TABLE.typeName);
        databaseViewClient       = integrationContext.getSchemaAttributeClient(OpenMetadataType.RELATIONAL_TABLE.typeName);
        databaseColumnClient     = integrationContext.getSchemaAttributeClient(OpenMetadataType.RELATIONAL_COLUMN.typeName);
        endpointClient           = integrationContext.getEndpointClient();
        connectorTypeClient      = integrationContext.getConnectorTypeClient();
        connectionClient         = integrationContext.getConnectionClient();


        this.auditLog = auditLog;
    }


    /**
     * Extract the qualified name from the supplied element.
     *
     * @param rootElement element to query
     * @return qualified name or null if this object is null, or not a referenceable
     */
    public String getQualifiedName(OpenMetadataRootElement rootElement)
    {
        return dataAssetClient.getQualifiedName(rootElement);
    }


    /**
     * Extract the display name from the supplied element.
     *
     * @param rootElement element to query
     * @return display name or null if this object is null, or not a referenceable
     */
    public String getDisplayName(OpenMetadataRootElement rootElement)
    {
        return dataAssetClient.getDisplayName(rootElement);
    }


    /**
     * Extract the additional properties from the supplied element.
     *
     * @param rootElement element to query
     * @return display name or null if this object is null, or not a referenceable
     */
    public Map<String, String> getAdditionalProperties(OpenMetadataRootElement rootElement)
    {
        return dataAssetClient.getAdditionalProperties(rootElement);
    }


    /**
     * Get schemas of database
     *
     * @param databaseGUID database guid
     *
     * @return schemas
     */
    public List<OpenMetadataRootElement> getSchemas(String databaseGUID)
    {
        return new OmasGetSchemas(databaseSchemaClient, auditLog).apply(databaseGUID);
    }

    /**
     * Get tables
     *
     * @param databaseAssetGUID database or schema guid
     *
     * @return tables
     */
    public List<OpenMetadataRootElement> getTables(String databaseAssetGUID)
    {
        return new OmasGetTables(dataAssetClient,
                                 databaseTableClient,
                                 auditLog).apply(databaseAssetGUID);
    }

    /**
     * Get views
     *
     * @param databaseAssetGUID database or schema guid
     *
     * @return tables
     */
    public List<OpenMetadataRootElement> getViews(String databaseAssetGUID)
    {
        return new OmasGetViews(dataAssetClient,
                                databaseViewClient,
                                auditLog).apply(databaseAssetGUID);
    }


    /**
     * Get columns of table
     *
     * @param databaseTableGUID table guid
     *
     * @return columns
     */
    public List<OpenMetadataRootElement> getColumns(String databaseTableGUID)
    {
        return new OmasGetColumns(databaseColumnClient, auditLog).apply(databaseTableGUID);
    }


    /**
     * Create endpoint
     *
     * @param newEndpointProperties properties
     *
     * @return guid
     */
    public Optional<String> createEndpoint(EndpointProperties newEndpointProperties)
    {
        return new OmasCreateEndpoint(endpointClient, auditLog).apply(newEndpointProperties);
    }


    /**
     * Create database
     *
     * @param newDatabaseProperties properties
     *
     * @return guid
     */
    public Optional<String> createDatabase(DatabaseProperties newDatabaseProperties)
    {
        return new OmasCreateDatabase(databaseClient, auditLog).apply(newDatabaseProperties);
    }

    /**
     * Create schema in database
     *
     * @param databaseGuid database guid
     * @param newSchemaProperties properties
     *
     * @return guid
     */
    public Optional<String> createSchema(String                           databaseGuid,
                                         DeployedDatabaseSchemaProperties newSchemaProperties)
    {
        return new OmasCreateSchema(databaseSchemaClient, auditLog).apply(databaseGuid, newSchemaProperties);
    }


    /**
     * Create table
     *
     * @param databaseSchemaGUID schema guid
     * @param newTableProperties properties
     *
     * @return guid
     */
    public Optional<String> createTable(OpenMetadataRootElement              anchorAsset,
                                        String                    databaseSchemaGUID,
                                        RelationalTableProperties newTableProperties)
    {
        return new OmasCreateTable(anchorAsset,
                                   dataAssetClient,
                                   databaseSchemaTypeClient,
                                   databaseTableClient,
                                   auditLog).apply(databaseSchemaGUID, newTableProperties);
    }


    /**
     * Create view
     *
     * @param databaseSchemaGUID parent guid
     * @param newViewProperties properties
     *
     * @return guid
     */
    public Optional<String> createView(OpenMetadataRootElement              anchorAsset,
                                       String                    databaseSchemaGUID,
                                       RelationalTableProperties newViewProperties,
                                       CalculatedValueProperties calculatedValueProperties)
    {
        return new OmasCreateView(anchorAsset,
                                  calculatedValueProperties,
                                  dataAssetClient,
                                  databaseSchemaTypeClient,
                                  databaseTableClient,
                                  auditLog).apply(databaseSchemaGUID, newViewProperties);
    }


    /**
     * Create column in table
     *
     * @param databaseTableGUID table guid
     * @param newColumnProperties properties
     *
     * @return guid
     */
    public Optional<String> createColumn(String                     databaseTableGUID,
                                         RelationalColumnProperties newColumnProperties)
    {
        return new OmasCreateColumn(databaseColumnClient,
                                    auditLog).apply(databaseTableGUID, newColumnProperties);
    }


    /**
     * Remove schema
     *
     * @param schemaElement schema
     */
    public void removeSchema(OpenMetadataRootElement schemaElement)
    {
        new OmasRemoveSchema(databaseSchemaClient, auditLog).accept(schemaElement);
    }

    /**
     * Remove table
     *
     * @param tableElement table
     */
    public void removeTable(OpenMetadataRootElement tableElement)
    {
        new OmasRemoveTable(databaseTableClient, auditLog).accept(tableElement);
    }


    /**
     * Remove view
     *
     * @param viewElement view
     */
    public void removeView(OpenMetadataRootElement viewElement)
    {
        new OmasRemoveView(databaseViewClient, auditLog).accept(viewElement);
    }


    /**
     * Remove column
     *
     * @param columnElement column
     */
    public void removeColumn(OpenMetadataRootElement columnElement)
    {
        new OmasRemoveColumn(databaseColumnClient, auditLog).accept(columnElement);
    }

    /**
     * Update database
     *
     * @param databaseGuid guid
     * @param databaseProperties properties
     */
    public void updateDatabase(String databaseGuid, DatabaseProperties databaseProperties)
    {
        new OmasUpdateDatabase(databaseClient, auditLog).accept(databaseGuid, databaseProperties);
    }

    /**
     * Update schema
     *
     * @param schemaGuid guid
     * @param schemaProperties properties
     */
    public void updateSchema(String schemaGuid, DeployedDatabaseSchemaProperties schemaProperties)
    {
        new OmasUpdateSchema(databaseSchemaClient, auditLog).accept(schemaGuid, schemaProperties);
    }

    /**
     * Update table
     *
     * @param tableGuid guid
     * @param tableProperties properties
     */
    public void updateTable(String tableGuid, RelationalTableProperties tableProperties)
    {
        new OmasUpdateTable(databaseTableClient, auditLog).accept(tableGuid, tableProperties);
    }


    /**
     * Update view
     *
     * @param viewGuid guid
     * @param viewProperties properties
     * @param calculatedValueProperties  for calculated value classification
     */
    public void updateView(String viewGuid,
                           RelationalTableProperties viewProperties,
                           CalculatedValueProperties calculatedValueProperties)
    {
        new OmasUpdateView(databaseViewClient, auditLog).accept(viewGuid, viewProperties);
    }


    /**
     * Update column
     *
     * @param columnGuid guid
     * @param columnProperties properties
     */
    public void updateColumn(String columnGuid, RelationalColumnProperties columnProperties)
    {
        new OmasUpdateColumn(databaseColumnClient, auditLog).accept(columnGuid, columnProperties);
    }


    /**
     * Set primary key
     *
     * @param columnGuid guid
     * @param primaryKeyProperties properties
     */
    public void setPrimaryKey(String columnGuid, PrimaryKeyProperties primaryKeyProperties)
    {
        new OmasSetPrimaryKey(databaseColumnClient, auditLog).accept(columnGuid, primaryKeyProperties);
    }


    /**
     * Remove primary key
     *
     * @param columnGuid guid
     */
    public void removePrimaryKey(String columnGuid)
    {
        new OmasRemovePrimaryKey(databaseColumnClient, auditLog).accept(columnGuid);
    }


    /**
     * Set foreign key
     *
     * @param primaryKeyColumnGuid guid
     * @param foreignKeyColumnGuid guid
     * @param foreignKeyProperties properties
     */
    public void setForeignKey(String               primaryKeyColumnGuid,
                              String               foreignKeyColumnGuid,
                              ForeignKeyProperties foreignKeyProperties)
    {
        new OmasSetForeignKey(databaseColumnClient, auditLog).accept(primaryKeyColumnGuid, foreignKeyColumnGuid, foreignKeyProperties);
    }


    /**
     * Remove foreign key
     *
     * @param primaryKeyColumnGuid guid
     * @param foreignKeyColumnGuid guid
     */
    public void removeForeignKey(String primaryKeyColumnGuid, String foreignKeyColumnGuid)
    {
        new OmasRemoveForeignKey(databaseColumnClient, auditLog).accept(primaryKeyColumnGuid, foreignKeyColumnGuid);
    }


    /**
     * Get databases
     *
     * @param databaseQualifiedName qualified name
     *
     * @return databases
     */
    public List<OpenMetadataRootElement> getDatabasesByName(String databaseQualifiedName)
    {
        return new OmasGetDatabasesByName(databaseClient, auditLog).apply(databaseQualifiedName);
    }


    /**
     * Get connector types by name
     *
     * @param connectorTypeQualifiedName qualified name
     *
     * @return connector types
     */
    public List<OpenMetadataRootElement> getConnectorTypesByName(String connectorTypeQualifiedName)
    {
        return new OmasGetConnectorTypesByName(connectorTypeClient, auditLog).apply(connectorTypeQualifiedName);
    }


    /**
     * Get connection by name
     *
     * @param connectionQualifiedName qualified name
     *
     * @return connections
     */
    public List<OpenMetadataRootElement> getConnectionsByName(String connectionQualifiedName)
    {
        return new OmasGetConnectionsByName(connectionClient, auditLog).apply(connectionQualifiedName);
    }


    /**
     * Find endpoints
     *
     * @param searchBy criteria
     *
     * @return endpoints
     */
    public List<OpenMetadataRootElement> findEndpoints(String searchBy)
    {
        return new OmasFindEndpoints(endpointClient, auditLog).apply(searchBy);
    }


    /**
     * Find columns
     *
     * @param searchBy criteria
     *
     * @return columns
     */
    public List<OpenMetadataRootElement> findDatabaseColumns(String searchBy)
    {
        return new OmasFindDatabaseColumns(databaseColumnClient, auditLog).apply(searchBy);
    }
}
