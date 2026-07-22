/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.integration.jdbc;

import org.odpi.openmetadata.adapters.connectors.integration.jdbc.ffdc.JDBCIntegrationConnectorAuditCode;
import org.odpi.openmetadata.adapters.connectors.integration.jdbc.transfer.JdbcMetadata;
import org.odpi.openmetadata.adapters.connectors.integration.jdbc.transfer.customization.TransferCustomizations;
import org.odpi.openmetadata.adapters.connectors.integration.jdbc.transfer.model.JdbcColumn;
import org.odpi.openmetadata.adapters.connectors.integration.jdbc.transfer.model.JdbcForeignKey;
import org.odpi.openmetadata.adapters.connectors.integration.jdbc.transfer.model.JdbcPrimaryKey;
import org.odpi.openmetadata.adapters.connectors.integration.jdbc.transfer.model.JdbcSchema;
import org.odpi.openmetadata.adapters.connectors.integration.jdbc.transfer.model.JdbcTable;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.integration.context.CatalogTargetContext;
import org.odpi.openmetadata.frameworks.openmetadata.connectorcontext.AssetClient;
import org.odpi.openmetadata.frameworks.openmetadata.connectorcontext.SchemaAttributeClient;
import org.odpi.openmetadata.frameworks.openmetadata.connectorcontext.SchemaTypeClient;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.OpenMetadataRootElement;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.RelatedMetadataElementSummary;
import org.odpi.openmetadata.frameworks.openmetadata.properties.ClassificationProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.AssetProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.databases.DatabaseProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.databases.DeployedDatabaseSchemaProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.schema.AttributeForSchemaProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.schema.ForeignKeyProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.schema.NestedSchemaAttributeProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.schema.PrimaryKeyProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.schema.databases.RelationalColumnProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.schema.databases.RelationalDBSchemaTypeProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.schema.databases.RelationalTableProperties;
import org.odpi.openmetadata.frameworks.openmetadata.search.MakeAnchorOptions;
import org.odpi.openmetadata.frameworks.openmetadata.search.NewElementOptions;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * RelationalDatabaseCataloguer walks a single relational database via JDBC and synchronizes its schemas, tables,
 * views, columns, primary keys and foreign keys with open metadata.
 * <br><br>
 * Every element (schema/table/view/column) is created or updated by looking it up directly by its deterministic
 * qualified name at the point it is written, rather than by diffing against a list of children fetched once at the
 * start of the pass. This means a failed or stale read can never be mistaken for "this element does not exist yet",
 * which is what previously caused elements (in particular columns) to be recreated - and rejected as duplicates - on
 * every refresh.
 */
class RelationalDatabaseCataloguer
{
    private static final String JDBC_CATALOG_KEY    = "jdbc.catalog";
    private static final String JDBC_SCHEMA_KEY      = "jdbc.schema";
    private static final String JDBC_TABLE_KEY       = "jdbc.table";
    private static final String JDBC_COLUMN_KEY      = "jdbc.column";
    private static final String JDBC_TABLE_TYPE_KEY  = "jdbc.tableType";

    private static final String[] TABLE_TYPES = {"TABLE", "FOREIGN TABLE"};
    private static final String[] VIEW_TYPES  = {"VIEW", "MATERIALIZED VIEW"};

    private final JdbcMetadata           jdbcMetadata;
    private final TransferCustomizations transferCustomizations;
    private final String                 catalogName;
    private final String                 connectorName;
    private final AuditLog               auditLog;

    private final AssetClient           dataAssetClient;
    private final AssetClient           databaseSchemaClient;
    private final SchemaTypeClient      databaseSchemaTypeClient;
    private final SchemaAttributeClient databaseTableClient;
    private final SchemaAttributeClient databaseViewClient;
    private final SchemaAttributeClient databaseColumnClient;


    /**
     * Constructor
     *
     * @param integrationContext context for this connector, used to obtain the open metadata clients
     * @param jdbcMetadata JDBC metadata access for the database being catalogued
     * @param transferCustomizations include/exclude rules extracted from the connector's configuration properties
     * @param catalogName JDBC catalog name (may be null)
     * @param connectorName name of this integration connector
     * @param auditLog logging destination
     */
    RelationalDatabaseCataloguer(CatalogTargetContext    integrationContext,
                                 JdbcMetadata             jdbcMetadata,
                                 TransferCustomizations   transferCustomizations,
                                 String                   catalogName,
                                 String                   connectorName,
                                 AuditLog                 auditLog)
    {
        this.jdbcMetadata           = jdbcMetadata;
        this.transferCustomizations = transferCustomizations;
        this.catalogName            = catalogName;
        this.connectorName          = connectorName;
        this.auditLog               = auditLog;

        this.dataAssetClient         = integrationContext.getAssetClient(OpenMetadataType.DATA_ASSET.typeName);
        this.databaseSchemaClient    = integrationContext.getAssetClient(OpenMetadataType.DEPLOYED_DATABASE_SCHEMA.typeName);
        this.databaseSchemaTypeClient = integrationContext.getSchemaTypeClient(OpenMetadataType.RELATIONAL_DB_SCHEMA_TYPE.typeName);
        this.databaseTableClient     = integrationContext.getSchemaAttributeClient(OpenMetadataType.RELATIONAL_TABLE.typeName);
        this.databaseViewClient      = integrationContext.getSchemaAttributeClient(OpenMetadataType.RELATIONAL_TABLE.typeName);
        this.databaseColumnClient    = integrationContext.getSchemaAttributeClient(OpenMetadataType.RELATIONAL_COLUMN.typeName);
    }


    /**
     * Catalog everything below the database asset: tables/views/columns that sit directly under the database,
     * every schema, and the tables/views/columns nested under each schema, plus all foreign key relationships.
     *
     * @param databaseElement the database asset
     */
    void catalogDatabaseContents(OpenMetadataRootElement databaseElement) throws SQLException
    {
        String databaseGUID          = databaseElement.getElementHeader().getGUID();
        String databaseQualifiedName = dataAssetClient.getQualifiedName(databaseElement);

        catalogTablesAndViews(databaseElement, databaseQualifiedName, databaseGUID, null);
        catalogForeignKeysForTables(databaseElement, databaseQualifiedName, null);

        List<JdbcSchema>              jdbcSchemas = jdbcMetadata.getSchemas(catalogName, null);
        List<OpenMetadataRootElement> schemas     = catalogSchemas(jdbcSchemas, databaseQualifiedName, databaseGUID);

        for (OpenMetadataRootElement schema : schemas)
        {
            String schemaDisplayName = databaseSchemaClient.getDisplayName(schema);

            if (! transferCustomizations.shouldTransferSchema(schemaDisplayName))
            {
                continue;
            }

            String schemaGUID          = schema.getElementHeader().getGUID();
            String schemaQualifiedName = databaseSchemaClient.getQualifiedName(schema);

            catalogTablesAndViews(databaseElement, schemaQualifiedName, schemaGUID, schemaDisplayName);
            catalogForeignKeysForTables(databaseElement, databaseQualifiedName, schemaDisplayName);
        }
    }


    /* ==================================================================================================
     * Schemas
     * ================================================================================================== */

    /**
     * Create or update every schema returned by JDBC, delete any schema that is no longer present, and return the
     * resulting (current) list of schemas so the caller can process their contents.
     *
     * @param jdbcSchemas schemas as read from JDBC
     * @param databaseQualifiedName qualified name of the database
     * @param databaseGUID guid of the database
     * @return current list of schema assets
     */
    private List<OpenMetadataRootElement> catalogSchemas(List<JdbcSchema> jdbcSchemas,
                                                         String           databaseQualifiedName,
                                                         String           databaseGUID)
    {
        Set<String> processedQualifiedNames = new HashSet<>();

        for (JdbcSchema jdbcSchema : jdbcSchemas)
        {
            if (! transferCustomizations.shouldTransferSchema(jdbcSchema.getTableSchem()))
            {
                continue;
            }

            String qualifiedName = databaseQualifiedName + "::" + jdbcSchema.getTableSchem();
            processedQualifiedNames.add(qualifiedName);

            getOrCreateSchema(jdbcSchema, databaseGUID, qualifiedName);
        }

        return deleteStaleSchemasAndReturnRemaining(databaseGUID, processedQualifiedNames);
    }


    /**
     * Look up a schema by its deterministic qualified name and either update it (if found) or create it.
     */
    private void getOrCreateSchema(JdbcSchema jdbcSchema, String databaseGUID, String qualifiedName)
    {
        final String methodName = "getOrCreateSchema";

        DeployedDatabaseSchemaProperties schemaProperties = buildSchemaProperties(jdbcSchema, qualifiedName);

        try
        {
            OpenMetadataRootElement existingSchema = databaseSchemaClient.getAssetByUniqueName(qualifiedName,
                                                                                                OpenMetadataProperty.QUALIFIED_NAME.name,
                                                                                                databaseSchemaClient.getGetOptions());

            if (existingSchema != null)
            {
                databaseSchemaClient.updateAsset(existingSchema.getElementHeader().getGUID(),
                                                 databaseSchemaClient.getUpdateOptions(false),
                                                 schemaProperties);
            }
            else
            {
                NewElementOptions newElementOptions = new NewElementOptions(databaseSchemaClient.getMetadataSourceOptions());

                newElementOptions.setAnchorGUID(databaseGUID);
                newElementOptions.setIsOwnAnchor(false);
                newElementOptions.setParentGUID(databaseGUID);
                newElementOptions.setParentAtEnd1(false);
                newElementOptions.setParentRelationshipTypeName(OpenMetadataType.DATA_SET_CONTENT_RELATIONSHIP.typeName);

                databaseSchemaClient.createAsset(newElementOptions, null, schemaProperties, null);
            }

            auditLog.logMessage(methodName, JDBCIntegrationConnectorAuditCode.TRANSFER_COMPLETE_FOR_DB_OBJECT.getMessageDefinition("schema " + qualifiedName));
        }
        catch (InvalidParameterException | PropertyServerException | UserNotAuthorizedException e)
        {
            auditLog.logException(methodName,
                                  JDBCIntegrationConnectorAuditCode.EXCEPTION_WRITING_OMAS.getMessageDefinition(e.getClass().getName(), methodName, e.getMessage()),
                                  e);
        }
    }


    private DeployedDatabaseSchemaProperties buildSchemaProperties(JdbcSchema jdbcSchema, String qualifiedName)
    {
        Map<String, String> additionalProperties = new HashMap<>();
        additionalProperties.put(JDBC_CATALOG_KEY, jdbcSchema.getTableCatalog());
        additionalProperties.put(JDBC_SCHEMA_KEY, jdbcSchema.getTableSchem());

        DeployedDatabaseSchemaProperties properties = new DeployedDatabaseSchemaProperties();
        properties.setDisplayName(jdbcSchema.getTableSchem());
        properties.setQualifiedName(qualifiedName);
        properties.setAdditionalProperties(additionalProperties);

        return properties;
    }


    /**
     * Delete any existing schema whose qualified name was not among those just catalogued, and return the schemas
     * that remain (both updated and newly created).
     */
    private List<OpenMetadataRootElement> deleteStaleSchemasAndReturnRemaining(String databaseGUID, Set<String> currentQualifiedNames)
    {
        final String methodName = "deleteStaleSchemas";

        List<OpenMetadataRootElement> remaining = new ArrayList<>();

        try
        {
            List<OpenMetadataRootElement> existingSchemas = databaseSchemaClient.getSupportedDataSets(databaseGUID, databaseSchemaClient.getQueryOptions());

            if (existingSchemas != null)
            {
                for (OpenMetadataRootElement existingSchema : existingSchemas)
                {
                    String existingQualifiedName = databaseSchemaClient.getQualifiedName(existingSchema);

                    if (currentQualifiedNames.contains(existingQualifiedName))
                    {
                        remaining.add(existingSchema);
                    }
                    else
                    {
                        deleteSchema(existingSchema);
                    }
                }
            }
        }
        catch (InvalidParameterException | PropertyServerException | UserNotAuthorizedException e)
        {
            auditLog.logException(methodName,
                                  JDBCIntegrationConnectorAuditCode.EXCEPTION_READING_OMAS.getMessageDefinition(methodName, e.getMessage()),
                                  e);
        }

        return remaining;
    }


    private void deleteSchema(OpenMetadataRootElement schemaElement)
    {
        final String methodName          = "deleteSchema";
        String       schemaGUID          = schemaElement.getElementHeader().getGUID();
        String       schemaQualifiedName = databaseSchemaClient.getQualifiedName(schemaElement);

        try
        {
            databaseSchemaClient.deleteAsset(schemaGUID, databaseSchemaClient.getDeleteOptions(true));
        }
        catch (InvalidParameterException | UserNotAuthorizedException | PropertyServerException e)
        {
            auditLog.logMessage(methodName,
                                JDBCIntegrationConnectorAuditCode.EXCEPTION_WHEN_REMOVING_ELEMENT_IN_OMAS.getMessageDefinition(schemaGUID, schemaQualifiedName));
        }
    }


    /* ==================================================================================================
     * Tables and views
     * ================================================================================================== */

    /**
     * Create or update every table and view found directly under the given parent (a database or a schema), catalog
     * the columns of each table, and delete any table/view that is no longer present.
     *
     * @param anchorAsset the database asset (tables and views always anchor to the database, even when nested under a schema)
     * @param parentQualifiedName qualified name of the parent (database or schema)
     * @param parentGUID guid of the parent (database or schema)
     * @param jdbcSchemaName schema name to pass to JDBC, or null when cataloguing tables/views directly under the database
     */
    private void catalogTablesAndViews(OpenMetadataRootElement anchorAsset,
                                       String                   parentQualifiedName,
                                       String                   parentGUID,
                                       String                   jdbcSchemaName) throws SQLException
    {
        String schemaTypeGUID = resolveSchemaTypeGUID(parentGUID, anchorAsset);

        if (schemaTypeGUID == null)
        {
            return;
        }

        Set<String> processedQualifiedNames = new HashSet<>();

        for (JdbcTable jdbcTable : jdbcMetadata.getTables(catalogName, jdbcSchemaName, null, TABLE_TYPES))
        {
            if (! transferCustomizations.shouldTransferTable(jdbcTable.getTableName()))
            {
                continue;
            }

            String qualifiedName = parentQualifiedName + "::" + jdbcTable.getTableName();
            processedQualifiedNames.add(qualifiedName);

            String tableGUID = getOrCreateTable(jdbcTable, anchorAsset, schemaTypeGUID, qualifiedName);

            if (tableGUID != null)
            {
                catalogColumns(anchorAsset, tableGUID, qualifiedName, jdbcTable.getTableName(), jdbcSchemaName);
            }
        }

        for (JdbcTable jdbcView : jdbcMetadata.getTables(catalogName, jdbcSchemaName, null, VIEW_TYPES))
        {
            if (! transferCustomizations.shouldTransferTable(jdbcView.getTableName()))
            {
                continue;
            }

            String qualifiedName = parentQualifiedName + "::" + jdbcView.getTableName();
            processedQualifiedNames.add(qualifiedName);

            getOrCreateView(jdbcView, anchorAsset, schemaTypeGUID, qualifiedName);
        }

        deleteStaleTablesAndViews(schemaTypeGUID, processedQualifiedNames);
    }


    /**
     * Resolve the schema type that tables and views hang off for the given parent, creating it if the parent does
     * not have one yet.
     */
    private String resolveSchemaTypeGUID(String parentGUID, OpenMetadataRootElement anchorAsset)
    {
        final String methodName = "resolveSchemaTypeGUID";

        try
        {
            OpenMetadataRootElement parentAsset = dataAssetClient.getAssetByGUID(parentGUID, dataAssetClient.getGetOptions());

            if (parentAsset.getSchemaType() != null)
            {
                return parentAsset.getSchemaType().getRelatedElement().getElementHeader().getGUID();
            }

            if (! (parentAsset.getProperties() instanceof AssetProperties assetProperties))
            {
                return null;
            }

            NewElementOptions newElementOptions = new NewElementOptions(databaseSchemaTypeClient.getMetadataSourceOptions());

            newElementOptions.setAnchorGUID(anchorAsset.getElementHeader().getGUID());
            newElementOptions.setIsOwnAnchor(false);
            newElementOptions.setParentGUID(parentGUID);
            newElementOptions.setParentAtEnd1(true);
            newElementOptions.setParentRelationshipTypeName(OpenMetadataType.SCHEMA_RELATIONSHIP.typeName);

            RelationalDBSchemaTypeProperties schemaTypeProperties = new RelationalDBSchemaTypeProperties();
            schemaTypeProperties.setQualifiedName(assetProperties.getQualifiedName() + "_schemaType");
            schemaTypeProperties.setDisplayName("Schema Type for " + assetProperties.getDisplayName());

            return databaseSchemaTypeClient.createSchemaType(newElementOptions, null, schemaTypeProperties, null);
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
     * Look up a table by its deterministic qualified name and either update it (if found) or create it.
     *
     * @return the guid of the table, or null if it could not be resolved (in which case its columns are skipped for this refresh)
     */
    private String getOrCreateTable(JdbcTable jdbcTable, OpenMetadataRootElement anchorAsset, String schemaTypeGUID, String qualifiedName)
    {
        RelationalTableProperties tableProperties = buildTableProperties(jdbcTable, qualifiedName);

        ElementLookup lookup = findExistingSchemaAttribute(databaseTableClient, qualifiedName);

        if (lookup.abort)
        {
            return null;
        }

        if (lookup.element != null)
        {
            updateTable(lookup.element.getElementHeader().getGUID(), tableProperties);
            return lookup.element.getElementHeader().getGUID();
        }

        return createTable(anchorAsset, schemaTypeGUID, tableProperties);
    }


    private String createTable(OpenMetadataRootElement anchorAsset, String schemaTypeGUID, RelationalTableProperties tableProperties)
    {
        final String methodName = "createTable";

        try
        {
            NewElementOptions newElementOptions = new NewElementOptions(databaseTableClient.getMetadataSourceOptions());

            newElementOptions.setAnchorGUID(anchorAsset.getElementHeader().getGUID());
            newElementOptions.setIsOwnAnchor(false);
            newElementOptions.setParentGUID(schemaTypeGUID);
            newElementOptions.setParentAtEnd1(true);
            newElementOptions.setParentRelationshipTypeName(OpenMetadataType.ATTRIBUTE_FOR_SCHEMA_RELATIONSHIP.typeName);

            AttributeForSchemaProperties attributeForSchemaProperties = new AttributeForSchemaProperties();
            attributeForSchemaProperties.setMaxCardinality(1);
            attributeForSchemaProperties.setMinCardinality(1);

            String tableGUID = databaseTableClient.createSchemaAttribute(newElementOptions, null, tableProperties, attributeForSchemaProperties);
            auditLog.logMessage(methodName, JDBCIntegrationConnectorAuditCode.TRANSFER_COMPLETE_FOR_DB_OBJECT.getMessageDefinition("table " + tableProperties.getQualifiedName()));
            return tableGUID;
        }
        catch (InvalidParameterException | PropertyServerException | UserNotAuthorizedException e)
        {
            auditLog.logException(methodName,
                                  JDBCIntegrationConnectorAuditCode.EXCEPTION_WRITING_OMAS.getMessageDefinition(e.getClass().getName(), methodName, e.getMessage()),
                                  e);
            return null;
        }
    }


    private void updateTable(String tableGUID, RelationalTableProperties tableProperties)
    {
        final String methodName = "updateTable";

        try
        {
            databaseTableClient.updateSchemaAttribute(tableGUID, databaseTableClient.getUpdateOptions(false), tableProperties);
            auditLog.logMessage(methodName, JDBCIntegrationConnectorAuditCode.TRANSFER_COMPLETE_FOR_DB_OBJECT.getMessageDefinition("table " + tableProperties.getQualifiedName()));
        }
        catch (InvalidParameterException | UserNotAuthorizedException | PropertyServerException e)
        {
            auditLog.logException(methodName,
                                  JDBCIntegrationConnectorAuditCode.EXCEPTION_WRITING_OMAS.getMessageDefinition(e.getClass().getName(), methodName, e.getMessage()),
                                  e);
        }
    }


    /**
     * Look up a view by its deterministic qualified name and either update it (if found) or create it. Views do not
     * have their columns catalogued separately (matching the existing behaviour of this connector).
     */
    private void getOrCreateView(JdbcTable jdbcView, OpenMetadataRootElement anchorAsset, String schemaTypeGUID, String qualifiedName)
    {
        RelationalTableProperties viewProperties = buildTableProperties(jdbcView, qualifiedName);

        ElementLookup lookup = findExistingSchemaAttribute(databaseViewClient, qualifiedName);

        if (lookup.abort)
        {
            return;
        }

        if (lookup.element != null)
        {
            updateView(lookup.element.getElementHeader().getGUID(), viewProperties);
        }
        else
        {
            createView(anchorAsset, schemaTypeGUID, viewProperties);
        }
    }


    private void createView(OpenMetadataRootElement anchorAsset, String schemaTypeGUID, RelationalTableProperties viewProperties)
    {
        final String methodName = "createView";

        try
        {
            NewElementOptions newElementOptions = new NewElementOptions(databaseViewClient.getMetadataSourceOptions());

            newElementOptions.setAnchorGUID(anchorAsset.getElementHeader().getGUID());
            newElementOptions.setIsOwnAnchor(false);
            newElementOptions.setParentGUID(schemaTypeGUID);
            newElementOptions.setParentAtEnd1(true);
            newElementOptions.setParentRelationshipTypeName(OpenMetadataType.ATTRIBUTE_FOR_SCHEMA_RELATIONSHIP.typeName);

            AttributeForSchemaProperties attributeForSchemaProperties = new AttributeForSchemaProperties();
            attributeForSchemaProperties.setMaxCardinality(1);
            attributeForSchemaProperties.setMinCardinality(1);

            // The SQL query that distinguishes a view from a table is not currently captured, so the
            // CalculatedValue classification is added with no properties (this matches existing behaviour).
            Map<String, ClassificationProperties> initialClassifications = new HashMap<>();
            initialClassifications.put(OpenMetadataType.CALCULATED_VALUE_CLASSIFICATION.typeName, null);

            databaseViewClient.createSchemaAttribute(newElementOptions, initialClassifications, viewProperties, attributeForSchemaProperties);
            auditLog.logMessage(methodName, JDBCIntegrationConnectorAuditCode.TRANSFER_COMPLETE_FOR_DB_OBJECT.getMessageDefinition("view " + viewProperties.getQualifiedName()));
        }
        catch (InvalidParameterException | PropertyServerException | UserNotAuthorizedException e)
        {
            auditLog.logException(methodName,
                                  JDBCIntegrationConnectorAuditCode.EXCEPTION_WRITING_OMAS.getMessageDefinition(e.getClass().getName(), methodName, e.getMessage()),
                                  e);
        }
    }


    private void updateView(String viewGUID, RelationalTableProperties viewProperties)
    {
        final String methodName = "updateView";

        try
        {
            databaseViewClient.updateSchemaAttribute(viewGUID, databaseViewClient.getUpdateOptions(false), viewProperties);
            auditLog.logMessage(methodName, JDBCIntegrationConnectorAuditCode.TRANSFER_COMPLETE_FOR_DB_OBJECT.getMessageDefinition("view " + viewProperties.getQualifiedName()));
        }
        catch (InvalidParameterException | UserNotAuthorizedException | PropertyServerException e)
        {
            auditLog.logException(methodName,
                                  JDBCIntegrationConnectorAuditCode.EXCEPTION_WRITING_OMAS.getMessageDefinition(e.getClass().getName(), methodName, e.getMessage()),
                                  e);
        }
    }


    /**
     * Tables and views are properties-compatible in this model (a view is a table with an added CalculatedValue
     * classification), so they share the same property-building logic.
     */
    private RelationalTableProperties buildTableProperties(JdbcTable jdbcTable, String qualifiedName)
    {
        Map<String, String> additionalProperties = new HashMap<>();
        additionalProperties.put(JDBC_CATALOG_KEY, jdbcTable.getTableCat());
        additionalProperties.put(JDBC_SCHEMA_KEY, jdbcTable.getTableSchem());
        additionalProperties.put(JDBC_TABLE_KEY, jdbcTable.getTableName());
        additionalProperties.put(JDBC_TABLE_TYPE_KEY, jdbcTable.getTableType());

        RelationalTableProperties properties = new RelationalTableProperties();
        properties.setDisplayName(jdbcTable.getTableName());
        properties.setQualifiedName(qualifiedName);
        properties.setAdditionalProperties(additionalProperties);

        return properties;
    }


    private void deleteStaleTablesAndViews(String schemaTypeGUID, Set<String> currentQualifiedNames)
    {
        final String methodName = "deleteStaleTablesAndViews";

        try
        {
            List<OpenMetadataRootElement> existingElements = databaseTableClient.getAttributesForSchemaType(schemaTypeGUID, databaseTableClient.getQueryOptions());

            if (existingElements != null)
            {
                for (OpenMetadataRootElement existingElement : existingElements)
                {
                    String existingQualifiedName = databaseTableClient.getQualifiedName(existingElement);

                    if (! currentQualifiedNames.contains(existingQualifiedName))
                    {
                        deleteTableOrView(existingElement);
                    }
                }
            }
        }
        catch (InvalidParameterException | PropertyServerException | UserNotAuthorizedException e)
        {
            auditLog.logException(methodName,
                                  JDBCIntegrationConnectorAuditCode.EXCEPTION_READING_OMAS.getMessageDefinition(methodName, e.getMessage()),
                                  e);
        }
    }


    private void deleteTableOrView(OpenMetadataRootElement element)
    {
        final String methodName      = "deleteTableOrView";
        String       guid            = element.getElementHeader().getGUID();
        String       qualifiedName   = databaseTableClient.getQualifiedName(element);

        try
        {
            databaseTableClient.deleteSchemaAttribute(guid, databaseTableClient.getDeleteOptions(true));
        }
        catch (InvalidParameterException | UserNotAuthorizedException | PropertyServerException e)
        {
            auditLog.logMessage(methodName,
                                JDBCIntegrationConnectorAuditCode.EXCEPTION_WHEN_REMOVING_ELEMENT_IN_OMAS.getMessageDefinition(guid, qualifiedName));
        }
    }


    /* ==================================================================================================
     * Columns and primary keys
     * ================================================================================================== */

    /**
     * Create or update every column of a table, set/remove primary key classifications, and delete any column that
     * is no longer present.
     */
    private void catalogColumns(OpenMetadataRootElement anchorAsset,
                                String                   tableGUID,
                                String                   tableQualifiedName,
                                String                   tableName,
                                String                   jdbcSchemaName) throws SQLException
    {
        List<JdbcPrimaryKey> jdbcPrimaryKeys = jdbcMetadata.getPrimaryKeys(catalogName, jdbcSchemaName, tableName);

        Set<String> processedQualifiedNames = new HashSet<>();

        for (JdbcColumn jdbcColumn : jdbcMetadata.getColumns(catalogName, jdbcSchemaName, tableName, null))
        {
            if (! transferCustomizations.shouldTransferColumn(jdbcColumn.getColumnName()))
            {
                continue;
            }

            String qualifiedName = tableQualifiedName + "::" + jdbcColumn.getColumnName();
            processedQualifiedNames.add(qualifiedName);

            getOrCreateColumn(jdbcColumn, anchorAsset, tableGUID, qualifiedName, jdbcPrimaryKeys);
        }

        deleteStaleColumns(tableGUID, processedQualifiedNames);
    }


    /**
     * Look up a column by its deterministic qualified name and either update it (if found) or create it, then
     * synchronize its primary key classification.
     */
    private void getOrCreateColumn(JdbcColumn              jdbcColumn,
                                   OpenMetadataRootElement  anchorAsset,
                                   String                   tableGUID,
                                   String                   qualifiedName,
                                   List<JdbcPrimaryKey>     jdbcPrimaryKeys)
    {
        RelationalColumnProperties columnProperties = buildColumnProperties(jdbcColumn, qualifiedName);

        ElementLookup lookup = findExistingSchemaAttribute(databaseColumnClient, qualifiedName);

        if (lookup.abort)
        {
            return;
        }

        if (lookup.element != null)
        {
            String columnGUID = lookup.element.getElementHeader().getGUID();

            updateColumn(columnGUID, columnProperties);
            updateOrRemovePrimaryKey(jdbcPrimaryKeys, jdbcColumn, columnGUID, lookup.element.getElementHeader().getPrimaryKey() != null);
        }
        else
        {
            String columnGUID = createColumn(anchorAsset, tableGUID, columnProperties);

            if (columnGUID != null)
            {
                updateOrRemovePrimaryKey(jdbcPrimaryKeys, jdbcColumn, columnGUID, false);
            }
        }
    }


    private String createColumn(OpenMetadataRootElement anchorAsset, String tableGUID, RelationalColumnProperties columnProperties)
    {
        final String methodName = "createColumn";

        try
        {
            NewElementOptions newElementOptions = new NewElementOptions(databaseColumnClient.getMetadataSourceOptions());

            newElementOptions.setAnchorGUID(anchorAsset.getElementHeader().getGUID());
            newElementOptions.setIsOwnAnchor(false);
            newElementOptions.setParentGUID(tableGUID);
            newElementOptions.setParentAtEnd1(true);
            newElementOptions.setParentRelationshipTypeName(OpenMetadataType.NESTED_SCHEMA_ATTRIBUTE_RELATIONSHIP.typeName);

            NestedSchemaAttributeProperties nestedSchemaAttributeProperties = new NestedSchemaAttributeProperties();
            nestedSchemaAttributeProperties.setMaxCardinality(1);
            nestedSchemaAttributeProperties.setMinCardinality(1);

            String columnGUID = databaseColumnClient.createSchemaAttribute(newElementOptions, null, columnProperties, nestedSchemaAttributeProperties);
            auditLog.logMessage(methodName, JDBCIntegrationConnectorAuditCode.TRANSFER_COMPLETE_FOR_DB_OBJECT.getMessageDefinition("column " + columnProperties.getQualifiedName()));
            return columnGUID;
        }
        catch (InvalidParameterException | UserNotAuthorizedException | PropertyServerException e)
        {
            auditLog.logException(methodName,
                                  JDBCIntegrationConnectorAuditCode.EXCEPTION_WRITING_OMAS.getMessageDefinition(e.getClass().getName(), methodName, e.getMessage()),
                                  e);
            return null;
        }
    }


    private void updateColumn(String columnGUID, RelationalColumnProperties columnProperties)
    {
        final String methodName = "updateColumn";

        try
        {
            databaseColumnClient.updateSchemaAttribute(columnGUID, databaseColumnClient.getUpdateOptions(false), columnProperties);
            auditLog.logMessage(methodName, JDBCIntegrationConnectorAuditCode.TRANSFER_COMPLETE_FOR_DB_OBJECT.getMessageDefinition("column " + columnProperties.getQualifiedName()));
        }
        catch (InvalidParameterException | UserNotAuthorizedException | PropertyServerException e)
        {
            auditLog.logException(methodName,
                                  JDBCIntegrationConnectorAuditCode.EXCEPTION_WRITING_OMAS.getMessageDefinition(e.getClass().getName(), methodName, e.getMessage()),
                                  e);
        }
    }


    private RelationalColumnProperties buildColumnProperties(JdbcColumn jdbcColumn, String qualifiedName)
    {
        Map<String, String> additionalProperties = new HashMap<>();
        additionalProperties.put(JDBC_CATALOG_KEY, jdbcColumn.getTableCat());
        additionalProperties.put(JDBC_SCHEMA_KEY, jdbcColumn.getTableSchem());
        additionalProperties.put(JDBC_TABLE_KEY, jdbcColumn.getTableName());
        additionalProperties.put(JDBC_COLUMN_KEY, jdbcColumn.getColumnName());

        RelationalColumnProperties properties = new RelationalColumnProperties();
        properties.setDisplayName(jdbcColumn.getColumnName());
        properties.setQualifiedName(qualifiedName);
        properties.setAdditionalProperties(additionalProperties);

        return properties;
    }


    /**
     * Set the primary key classification on a column if JDBC reports one for it, or remove the classification if
     * JDBC no longer reports one but the column previously had it.
     */
    private void updateOrRemovePrimaryKey(List<JdbcPrimaryKey>  jdbcPrimaryKeys,
                                          JdbcColumn             jdbcColumn,
                                          String                 columnGUID,
                                          boolean                hasExistingPrimaryKeyClassification)
    {
        JdbcPrimaryKey matchingPrimaryKey = null;

        for (JdbcPrimaryKey candidate : jdbcPrimaryKeys)
        {
            boolean catalogMatches = (candidate.getTableCat() == null) ? (jdbcColumn.getTableCat() == null) : candidate.getTableCat().equals(jdbcColumn.getTableCat());
            boolean schemaMatches  = (candidate.getTableSchem() == null) ? (jdbcColumn.getTableSchem() == null) : candidate.getTableSchem().equals(jdbcColumn.getTableSchem());

            if (catalogMatches && schemaMatches &&
                        candidate.getTableName().equals(jdbcColumn.getTableName()) &&
                        candidate.getColumnName().equals(jdbcColumn.getColumnName()))
            {
                matchingPrimaryKey = candidate;
                break;
            }
        }

        if (matchingPrimaryKey == null)
        {
            if (hasExistingPrimaryKeyClassification)
            {
                removePrimaryKey(columnGUID);
            }
            return;
        }

        PrimaryKeyProperties primaryKeyProperties = new PrimaryKeyProperties();
        primaryKeyProperties.setDisplayName(matchingPrimaryKey.getPkName());

        setPrimaryKey(columnGUID, primaryKeyProperties);
    }


    private void setPrimaryKey(String columnGUID, PrimaryKeyProperties primaryKeyProperties)
    {
        final String methodName = "setPrimaryKey";

        try
        {
            databaseColumnClient.addPrimaryKeyClassification(columnGUID, primaryKeyProperties, databaseColumnClient.getMetadataSourceOptions());
        }
        catch (UserNotAuthorizedException | InvalidParameterException | PropertyServerException e)
        {
            auditLog.logException(methodName,
                                  JDBCIntegrationConnectorAuditCode.EXCEPTION_WRITING_OMAS.getMessageDefinition(e.getClass().getName(), methodName, e.getMessage()),
                                  e);
        }
    }


    private void removePrimaryKey(String columnGUID)
    {
        final String methodName = "removePrimaryKey";

        try
        {
            databaseColumnClient.removePrimaryKeyClassification(columnGUID, databaseColumnClient.getMetadataSourceOptions());
        }
        catch (UserNotAuthorizedException | InvalidParameterException | PropertyServerException e)
        {
            auditLog.logException(methodName,
                                  JDBCIntegrationConnectorAuditCode.EXCEPTION_WRITING_OMAS.getMessageDefinition(e.getClass().getName(), methodName, e.getMessage()),
                                  e);
        }
    }


    private void deleteStaleColumns(String tableGUID, Set<String> currentQualifiedNames)
    {
        final String methodName = "deleteStaleColumns";

        try
        {
            List<OpenMetadataRootElement> existingColumns = databaseColumnClient.getNestedSchemaAttributes(tableGUID, databaseColumnClient.getQueryOptions());

            if (existingColumns != null)
            {
                for (OpenMetadataRootElement existingColumn : existingColumns)
                {
                    String existingQualifiedName = databaseColumnClient.getQualifiedName(existingColumn);

                    if (! currentQualifiedNames.contains(existingQualifiedName))
                    {
                        deleteColumn(existingColumn);
                    }
                }
            }
        }
        catch (InvalidParameterException | PropertyServerException | UserNotAuthorizedException e)
        {
            auditLog.logException(methodName,
                                  JDBCIntegrationConnectorAuditCode.EXCEPTION_READING_OMAS.getMessageDefinition(methodName, e.getMessage()),
                                  e);
        }
    }


    private void deleteColumn(OpenMetadataRootElement element)
    {
        final String methodName    = "deleteColumn";
        String       guid          = element.getElementHeader().getGUID();
        String       qualifiedName = databaseColumnClient.getQualifiedName(element);

        try
        {
            databaseColumnClient.deleteSchemaAttribute(guid, databaseColumnClient.getDeleteOptions(false));
        }
        catch (InvalidParameterException | UserNotAuthorizedException | PropertyServerException e)
        {
            auditLog.logMessage(methodName,
                                JDBCIntegrationConnectorAuditCode.EXCEPTION_WHEN_REMOVING_ELEMENT_IN_OMAS.getMessageDefinition(guid, qualifiedName));
        }
    }


    /* ==================================================================================================
     * Foreign keys
     * ================================================================================================== */

    /**
     * Link foreign keys for every table found under the given schema (or directly under the database, when
     * jdbcSchemaName is null). Foreign keys are handled at this level - rather than while cataloguing each table -
     * because a foreign key relationship can span tables in different schemas.
     */
    private void catalogForeignKeysForTables(OpenMetadataRootElement databaseElement,
                                             String                   databaseQualifiedName,
                                             String                   jdbcSchemaName) throws SQLException
    {
        if ((jdbcSchemaName != null) && (! transferCustomizations.shouldTransferSchema(jdbcSchemaName)))
        {
            return;
        }

        Set<JdbcForeignKey> foreignKeys = new HashSet<>();

        for (JdbcTable jdbcTable : jdbcMetadata.getTables(catalogName, jdbcSchemaName, null, TABLE_TYPES))
        {
            if (! transferCustomizations.shouldTransferTable(jdbcTable.getTableName()))
            {
                continue;
            }

            foreignKeys.addAll(jdbcMetadata.getImportedKeys(catalogName, jdbcSchemaName, jdbcTable.getTableName()));
            foreignKeys.addAll(jdbcMetadata.getExportedKeys(catalogName, jdbcSchemaName, jdbcTable.getTableName()));
        }

        for (JdbcForeignKey foreignKey : foreignKeys)
        {
            setForeignKeyIfNeeded(foreignKey, databaseElement, databaseQualifiedName);
        }
    }


    private void setForeignKeyIfNeeded(JdbcForeignKey jdbcForeignKey, OpenMetadataRootElement databaseElement, String databaseQualifiedName)
    {
        final String methodName = "setForeignKeyIfNeeded";

        String pkColumnQualifiedName = databaseQualifiedName
                + (jdbcForeignKey.getPkTableSchem() == null ? "" : "::" + jdbcForeignKey.getPkTableSchem())
                + "::" + jdbcForeignKey.getPkTableName() + "::" + jdbcForeignKey.getPkColumnName();
        String fkColumnQualifiedName = databaseQualifiedName
                + (jdbcForeignKey.getFkTableSchem() == null ? "" : "::" + jdbcForeignKey.getFkTableSchem())
                + "::" + jdbcForeignKey.getFkTableName() + "::" + jdbcForeignKey.getFkColumnName();

        OpenMetadataRootElement pkColumn = findSingleColumn(pkColumnQualifiedName);
        OpenMetadataRootElement fkColumn = findSingleColumn(fkColumnQualifiedName);

        if ((pkColumn == null) || (fkColumn == null))
        {
            return;
        }

        String pkColumnGUID = pkColumn.getElementHeader().getGUID();
        String fkColumnGUID = fkColumn.getElementHeader().getGUID();

        if (fkColumn.getForeignKeys() != null)
        {
            for (RelatedMetadataElementSummary relationship : fkColumn.getForeignKeys())
            {
                if (relationship.getRelatedElement().getElementHeader().getGUID().equals(pkColumnGUID))
                {
                    return;
                }
            }
        }

        ForeignKeyProperties foreignKeyProperties = new ForeignKeyProperties();
        foreignKeyProperties.setDisplayName(jdbcForeignKey.getPkName() + " - " + jdbcForeignKey.getFkName());

        if (databaseElement.getProperties() instanceof DatabaseProperties databaseProperties)
        {
            foreignKeyProperties.setSource(databaseProperties.getImportedFrom());
        }

        try
        {
            databaseColumnClient.linkForeignKey(pkColumnGUID,
                                                fkColumnGUID,
                                                new MakeAnchorOptions(databaseColumnClient.getMetadataSourceOptions()),
                                                foreignKeyProperties);
            auditLog.logMessage(methodName,
                                JDBCIntegrationConnectorAuditCode.TRANSFER_COMPLETE_FOR_DB_OBJECT.getMessageDefinition("foreign key relationship from " + pkColumnGUID + " to " + fkColumnGUID));
        }
        catch (UserNotAuthorizedException | InvalidParameterException | PropertyServerException e)
        {
            auditLog.logException(methodName,
                                  JDBCIntegrationConnectorAuditCode.EXCEPTION_WRITING_OMAS.getMessageDefinition(e.getClass().getName(), methodName, e.getMessage()),
                                  e);
        }
    }


    private OpenMetadataRootElement findSingleColumn(String columnPathSuffix)
    {
        final String methodName = "findSingleColumn";

        try
        {
            List<OpenMetadataRootElement> columns = databaseColumnClient.findSchemaAttributes(columnPathSuffix, databaseColumnClient.getSearchOptions());

            if ((columns != null) && (columns.size() == 1))
            {
                return columns.get(0);
            }
        }
        catch (InvalidParameterException | UserNotAuthorizedException | PropertyServerException e)
        {
            auditLog.logException(methodName,
                                  JDBCIntegrationConnectorAuditCode.EXCEPTION_READING_OMAS.getMessageDefinition(methodName, e.getMessage()),
                                  e);
        }

        return null;
    }


    /* ==================================================================================================
     * Shared "does this element already exist" lookup
     * ================================================================================================== */

    /**
     * Look up a schema attribute (table, view, or column) by its exact qualified name.
     * <br><br>
     * This direct, per-element lookup - rather than diffing against a list of children fetched once at the start of
     * a pass - is what fixes the duplicate-qualifiedName problem: if the lookup fails, or is ambiguous, the element
     * is skipped for this refresh instead of being treated as absent and recreated.
     */
    private ElementLookup findExistingSchemaAttribute(SchemaAttributeClient client, String qualifiedName)
    {
        final String methodName = "findExistingSchemaAttribute";

        try
        {
            List<OpenMetadataRootElement> candidates = client.getSchemaAttributesByName(qualifiedName, client.getQueryOptions());

            OpenMetadataRootElement match      = null;
            int                     matchCount = 0;

            if (candidates != null)
            {
                for (OpenMetadataRootElement candidate : candidates)
                {
                    if (qualifiedName.equals(client.getQualifiedName(candidate)))
                    {
                        match = candidate;
                        matchCount++;
                    }
                }
            }

            if (matchCount > 1)
            {
                auditLog.logMessage(methodName,
                                    JDBCIntegrationConnectorAuditCode.MULTIPLE_ELEMENTS_FOUND.getMessageDefinition(connectorName,
                                                                                                                    Integer.toString(matchCount),
                                                                                                                    qualifiedName));
                return new ElementLookup(null, true);
            }

            return new ElementLookup(match, false);
        }
        catch (InvalidParameterException | PropertyServerException | UserNotAuthorizedException e)
        {
            auditLog.logException(methodName,
                                  JDBCIntegrationConnectorAuditCode.EXCEPTION_READING_OMAS.getMessageDefinition(methodName, e.getMessage()),
                                  e);
            return new ElementLookup(null, true);
        }
    }


    /**
     * Result of {@link #findExistingSchemaAttribute}: either the matching element (null if none exists yet and it
     * is safe to create one), or abort=true meaning the lookup failed or was ambiguous and this element should be
     * skipped entirely for this refresh (not created, not updated, not deleted).
     */
    private static final class ElementLookup
    {
        private final OpenMetadataRootElement element;
        private final boolean                 abort;

        private ElementLookup(OpenMetadataRootElement element, boolean abort)
        {
            this.element = element;
            this.abort   = abort;
        }
    }
}
