/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.integration.jdbc.transfer;

import org.apache.commons.lang3.StringUtils;
import org.odpi.openmetadata.adapters.connectors.integration.jdbc.transfer.customization.TransferCustomizations;
import org.odpi.openmetadata.adapters.connectors.integration.jdbc.transfer.model.JdbcForeignKey;
import org.odpi.openmetadata.adapters.connectors.integration.jdbc.transfer.model.JdbcPrimaryKey;
import org.odpi.openmetadata.adapters.connectors.integration.jdbc.transfer.requests.Jdbc;
import org.odpi.openmetadata.adapters.connectors.integration.jdbc.transfer.requests.Omas;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.integration.context.IntegrationContext;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.OpenMetadataRootElement;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.AssetProperties;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.odpi.openmetadata.adapters.connectors.integration.jdbc.ffdc.JDBCIntegrationConnectorAuditCode.*;

/**
 * Transfers metadata from jdbc in an exploratory way. What can be accessed will be transferred
 */
public class JdbcMetadataTransfer
{
    private static final String TABLES_WITH_NO_SCHEMA = "tables with no schema";
    private static final String VIEWS_WITH_NO_SCHEMA = "views with no schema";
    private static final String COLUMNS_OF_TABLES_WITH_NO_SCHEMA = "columns of tables with no schema";
    private static final String SKIPPING = "Skipping ";
    private static final String TRANSFERRING = "Transferring ";
    private static final String SCHEMAS = "schemas";
    private static final String TABLES = "tables";
    private static final String VIEWS = "views";
    private static final String COLUMNS = "columns";
    private final Jdbc jdbc;
    private final Omas omas;
    private final String          databaseManagerName;
    private       OpenMetadataRootElement databaseElement;
    private final String          address;
    private final String catalog;
    private final TransferCustomizations transferCustomizations;

    private final AuditLog auditLog;

    public JdbcMetadataTransfer(JdbcMetadata              jdbcMetadata,
                                IntegrationContext        integrationContext,
                                OpenMetadataRootElement   databaseElement,
                                String                    endpointJDBCConnectionURL,
                                String                    catalogName,
                                TransferCustomizations    transferCustomizations,
                                AuditLog                  auditLog)
    {
        this.jdbc = new Jdbc(jdbcMetadata, auditLog);
        this.omas = new Omas(integrationContext, auditLog);
        this.databaseManagerName = integrationContext.getMetadataSourceQualifiedName();
        this.databaseElement     = databaseElement;
        this.address             = endpointJDBCConnectionURL;
        this.catalog = catalogName;
        this.transferCustomizations = transferCustomizations;
        this.auditLog = auditLog;
    }

    /**
     * Triggers database, schema, table and column metadata transfer. Will do the best it can to transfer as much of the
     * metadata as possible.
     */
    public void execute()
    {
        String methodName = "JdbcMetadataTransfer.execute";

        databaseElement = new DatabaseTransfer(jdbc, databaseManagerName, this.databaseElement, address, catalog, omas, auditLog).execute();
        if (databaseElement == null)
        {
            auditLog.logMessage("Verifying database metadata transferred. None found. Stopping transfer",
                    EXITING_ON_DATABASE_TRANSFER_FAIL.getMessageDefinition(methodName));
            return;
        }

        transferTablesWithoutSchema(databaseElement);
        transferViewsWithoutSchema(databaseElement);
        transferColumnsOfTablesWithoutSchema(databaseElement);
        transferForeignKeysIgnoringSchemas(databaseElement);

        transferSchemas(databaseElement);
        List<OpenMetadataRootElement> schemas = omas.getSchemas(databaseElement.getElementHeader().getGUID());
        if (schemas.isEmpty())
        {
            return;
        }
        transferTables(databaseElement, schemas);
        transferViews(databaseElement, schemas);
        transferColumns(databaseElement, schemas);
        transferForeignKeys(databaseElement);
    }

    /**
     * Triggers the transfer of available tables that are not assigned to any schema, depending also on inclusions and
     * exclusions
     *
     * @param databaseElement database element
     */
    private void transferTablesWithoutSchema(OpenMetadataRootElement databaseElement)
    {
        long start = System.currentTimeMillis();

        String databaseQualifiedName = omas.getQualifiedName(databaseElement);
        String databaseGuid = databaseElement.getElementHeader().getGUID();

        // already known tables by the omas, previously transferred
        List<OpenMetadataRootElement> omasTables = omas.getTables(databaseGuid);
        // a table update will always occur as long as the table is returned by jdbc
        List<OpenMetadataRootElement> omasTablesUpdated = jdbc.getTables(catalog,"").parallelStream()
                .filter(jdbcTable -> jdbcTable.getTableSchem() == null || jdbcTable.getTableSchem().isEmpty())
                .filter(table -> transferCustomizations.shouldTransferTable(table.getTableName()))
                .map(new TableTransfer(omas, databaseElement, auditLog, omasTables, databaseQualifiedName, databaseGuid))
                .toList();

        // will remove all updated tables, and what remains are the ones deleted in jdbc
        omasTables.removeAll(omasTablesUpdated);
        // remove from omas the tables deleted in jdbc
        omasTables.forEach(omas::removeTable);

        String excludedTables = transferCustomizations.getExcludedTables();
        if(StringUtils.isNotEmpty(excludedTables)) {
            auditLog.logMessage(SKIPPING + TABLES_WITH_NO_SCHEMA,
                    TRANSFER_EXCEPTIONS_FOR_DB_OBJECT.getMessageDefinition(TABLES_WITH_NO_SCHEMA, excludedTables));
        }
        long end = System.currentTimeMillis();
        auditLog.logMessage(TRANSFERRING + TABLES_WITH_NO_SCHEMA,
                PARTIAL_TRANSFER_COMPLETE_FOR_DB_OBJECTS.getMessageDefinition(TABLES_WITH_NO_SCHEMA, "" + (end - start)/1000));
    }

    /**
     * Triggers the transfer of available views that are not assigned to any schema, depending also on inclusions and
     * exclusions
     *
     * @param databaseElement database element
     */
    private void transferViewsWithoutSchema(OpenMetadataRootElement databaseElement)
    {
        long start = System.currentTimeMillis();

        String databaseQualifiedName = null;

        if (databaseElement.getProperties() instanceof AssetProperties assetProperties)
        {
            databaseQualifiedName = assetProperties.getQualifiedName();
        }

        String databaseGUID = databaseElement.getElementHeader().getGUID();

        // already known views by the omas, previously transferred
        List<OpenMetadataRootElement> omasViews = omas.getViews(databaseGUID);

        // a view update will always occur as long as the view is returned by jdbc
        List<OpenMetadataRootElement> omasViewsUpdated = jdbc.getViews(catalog,"").parallelStream()
                .filter(jdbcView -> jdbcView.getTableSchem() == null || jdbcView.getTableSchem().isEmpty())
                .filter(view -> transferCustomizations.shouldTransferTable(view.getTableName()))
                .map(new ViewTransfer(omas, databaseElement, auditLog, omasViews, databaseQualifiedName, databaseGUID))
                .toList();

        // will remove all updated views, and what remains are the ones deleted in jdbc
        omasViews.removeAll(omasViewsUpdated);
        // remove from omas the tables deleted in jdbc
        omasViews.forEach(omas::removeView);

        String excludedViews = transferCustomizations.getExcludedViews();
        if(StringUtils.isNotEmpty(excludedViews)) {
            auditLog.logMessage(SKIPPING + VIEWS_WITH_NO_SCHEMA,
                    TRANSFER_EXCEPTIONS_FOR_DB_OBJECT.getMessageDefinition(VIEWS_WITH_NO_SCHEMA, excludedViews));
        }
        long end = System.currentTimeMillis();
        auditLog.logMessage(TRANSFERRING + VIEWS_WITH_NO_SCHEMA,
                PARTIAL_TRANSFER_COMPLETE_FOR_DB_OBJECTS.getMessageDefinition(VIEWS_WITH_NO_SCHEMA, "" + (end - start)/1000));
    }

    /**
     * Triggers the transfer of columns from tables without schema, depending also on inclusions and exclusions
     *
     * @param databaseElement database element
     */
    private void transferColumnsOfTablesWithoutSchema(OpenMetadataRootElement databaseElement)
    {
        long start = System.currentTimeMillis();

        String databaseTableDisplayName = null;

        if (databaseElement.getProperties() instanceof AssetProperties assetProperties)
        {
            databaseTableDisplayName = assetProperties.getDisplayName();
        }

        String databaseGuid = databaseElement.getElementHeader().getGUID();

        omas.getTables(databaseGuid).parallelStream()
                .filter(table -> transferCustomizations.shouldTransferTable(omas.getDisplayName(table)))
                .peek(table -> {
                    String schemaName = "";
                    String tableName = omas.getDisplayName(table);
                    String tableGuid = table.getElementHeader().getGUID();

                    List<JdbcPrimaryKey> jdbcPrimaryKeys = jdbc.getPrimaryKeys(schemaName, tableName);
                    // already known columns by the omas, previously transferred
                    List<OpenMetadataRootElement> omasColumns = omas.getColumns(tableGuid);
                    // a column update will always occur as long as the column is returned by jdbc
                    List<OpenMetadataRootElement> omasUpdatedColumns = jdbc.getColumns(catalog, schemaName, tableName).parallelStream()
                            .filter(column -> transferCustomizations.shouldTransferColumn(column.getColumnName()))
                            .map(new ColumnTransfer(omas, databaseElement, auditLog, omasColumns, jdbcPrimaryKeys, table)).toList();

                    // will remove all updated column, and what remains are the ones deleted in jdbc
                    omasColumns.removeAll(omasUpdatedColumns);
                    // remove from omas the columns deleted in jdbc
                    omasColumns.forEach(omas::removeColumn);
                }).collect(Collectors.toList());

        String excludedColumns = transferCustomizations.getExcludedColumns();
        if(StringUtils.isNotEmpty(excludedColumns)) {
            auditLog.logMessage(SKIPPING + COLUMNS_OF_TABLES_WITH_NO_SCHEMA,
                    TRANSFER_EXCEPTIONS_FOR_DB_OBJECT.getMessageDefinition(COLUMNS_OF_TABLES_WITH_NO_SCHEMA, excludedColumns));
        }
        long end = System.currentTimeMillis();
        auditLog.logMessage(TRANSFERRING + COLUMNS_OF_TABLES_WITH_NO_SCHEMA,
                PARTIAL_TRANSFER_COMPLETE_FOR_DB_OBJECTS.getMessageDefinition(COLUMNS_OF_TABLES_WITH_NO_SCHEMA, "" + (end - start)/1000));
    }

    /**
     * Triggers the transfer of all foreign keys between columns of tables without schemas, depending also on inclusions
     * and exclusions
     *
     * @param databaseElement database element
     */
    private void transferForeignKeysIgnoringSchemas(OpenMetadataRootElement databaseElement)
    {
        long start = System.currentTimeMillis();

        Set<JdbcForeignKey> foreignKeys = Stream.concat(
                        jdbc.getTables(catalog, "").stream()
                                .filter(table -> transferCustomizations.shouldTransferTable(table.getTableName()))
                                .flatMap( t -> jdbc.getImportedKeys(catalog, "", t.getTableName()).stream() ),
                        jdbc.getTables(catalog, "").stream()
                                .filter(table -> transferCustomizations.shouldTransferTable(table.getTableName()))
                                .flatMap( t -> jdbc.getExportedKeys(catalog, "", t.getTableName()).stream() ))
                .collect(Collectors.toSet());

        foreignKeys.forEach(new ForeignKeyTransfer(omas, auditLog, databaseElement));

        long end = System.currentTimeMillis();
        auditLog.logMessage("Foreign key transfer complete",
                PARTIAL_TRANSFER_COMPLETE_FOR_DB_OBJECTS
                        .getMessageDefinition("foreign keys between columns of tables without schemas", "" + (end - start)/1000));
    }


    /**
     * Triggers the transfer of all available schemas, depending also on inclusions and exclusions
     *
     * @param databaseElement database
     */
    private void transferSchemas(OpenMetadataRootElement databaseElement)
    {
        long start = System.currentTimeMillis();

        String databaseQualifiedName = omas.getQualifiedName(databaseElement);
        String databaseGuid = databaseElement.getElementHeader().getGUID();

        // already known schemas by the omas, previously transferred
        List<OpenMetadataRootElement> omasSchemas = omas.getSchemas(databaseGuid);
        // a schema update will always occur as long as the schema is returned by jdbc
        List<OpenMetadataRootElement> omasSchemasUpdated =
                jdbc.getSchemas(catalog).parallelStream()
                        .filter(schema -> transferCustomizations.shouldTransferSchema(schema.getTableSchem()))
                        .map(new SchemaTransfer(omas, auditLog, omasSchemas, databaseQualifiedName, databaseGuid))
                        .toList();

        // will remove all updated schemas, and what remains are the ones deleted in jdbc
        omasSchemas.removeAll(omasSchemasUpdated);
        // remove from omas the schemas deleted in jdbc
        omasSchemas.forEach(omas::removeSchema);

        String excludedSchemas = transferCustomizations.getExcludedSchemas();
        if(StringUtils.isNotEmpty(excludedSchemas)) {
            auditLog.logMessage(SKIPPING + SCHEMAS,
                    TRANSFER_EXCEPTIONS_FOR_DB_OBJECT.getMessageDefinition(SCHEMAS, excludedSchemas));
        }
        long end = System.currentTimeMillis();
        auditLog.logMessage("Schema transfer complete",
                PARTIAL_TRANSFER_COMPLETE_FOR_DB_OBJECTS.getMessageDefinition(SCHEMAS, "" + (end - start)/1000));
    }

    /**
     * Triggers the transfer of all available tables, depending also on inclusions and exclusions
     *
     * @param databaseElement database element
     * @param schemas schemas
     */
    private void transferTables(OpenMetadataRootElement databaseElement, List<OpenMetadataRootElement> schemas){
        long start = System.currentTimeMillis();

        schemas.parallelStream()
                .filter(schema -> transferCustomizations.shouldTransferSchema(omas.getDisplayName(schema)))
                .peek(schema -> {
            String schemaDisplayName = omas.getDisplayName(schema);
            String schemaGuid = schema.getElementHeader().getGUID();
            String schemaQualifiedName = omas.getQualifiedName(schema);

            // already known tables by the omas, previously transferred
            List<OpenMetadataRootElement> omasTables = omas.getTables(schemaGuid);
            // a table update will always occur as long as the table is returned by jdbc
            List<OpenMetadataRootElement> omasTablesUpdated = jdbc.getTables(catalog, schemaDisplayName).parallelStream()
                    .filter(table -> transferCustomizations.shouldTransferTable(table.getTableName()))
                    .map(new TableTransfer(omas, databaseElement, auditLog, omasTables, schemaQualifiedName, schemaGuid))
                    .toList();

            // will remove all updated tables, and what remains are the ones deleted in jdbc
            omasTables.removeAll(omasTablesUpdated);
            // remove from omas the tables deleted in jdbc
            omasTables.forEach(omas::removeTable);
        }).collect(Collectors.toList());

        String excludedTables = transferCustomizations.getExcludedTables();
        if(StringUtils.isNotEmpty(excludedTables)) {
            auditLog.logMessage(SKIPPING + TABLES,
                    TRANSFER_EXCEPTIONS_FOR_DB_OBJECT.getMessageDefinition(TABLES, excludedTables));
        }
        long end = System.currentTimeMillis();
        auditLog.logMessage("Table transfer complete",
                PARTIAL_TRANSFER_COMPLETE_FOR_DB_OBJECTS.getMessageDefinition(TABLES, "" + (end - start)/1000));
    }

    /**
     * Triggers the transfer of all available views, depending also on inclusions and exclusions
     *
     * @param databaseElement database element
     * @param schemas schemas
     */
    private void transferViews(OpenMetadataRootElement databaseElement, List<OpenMetadataRootElement> schemas)
    {
        long start = System.currentTimeMillis();

        schemas.parallelStream()
                .filter(schema -> transferCustomizations.shouldTransferSchema(omas.getDisplayName(schema)))
                .peek(schema -> {
                    String schemaDisplayName = omas.getDisplayName(schema);
                    String schemaGuid = schema.getElementHeader().getGUID();
                    String schemaQualifiedName = omas.getQualifiedName(schema);

                    // already known views by the omas, previously transferred
                    List<OpenMetadataRootElement> omasViews = omas.getViews(schemaGuid);
                    // a view update will always occur as long as the view is returned by jdbc
                    List<OpenMetadataRootElement> omasViewsUpdated = jdbc.getViews(catalog, schemaDisplayName).parallelStream()
                            .filter(jdbcView -> transferCustomizations.shouldTransferTable(jdbcView.getTableName()))
                            .map(new ViewTransfer(omas, databaseElement, auditLog, omasViews, schemaQualifiedName, schemaGuid))
                            .toList();

                    // will remove all updated tables, and what remains are the ones deleted in jdbc
                    omasViews.removeAll(omasViewsUpdated);
                    // remove from omas the tables deleted in jdbc
                    omasViews.forEach(omas::removeView);
                }).collect(Collectors.toList());

        String excludedViews = transferCustomizations.getExcludedViews();
        if(StringUtils.isNotEmpty(excludedViews)) {
            auditLog.logMessage(SKIPPING + VIEWS,
                    TRANSFER_EXCEPTIONS_FOR_DB_OBJECT.getMessageDefinition(TABLES, excludedViews));
        }
        long end = System.currentTimeMillis();
        auditLog.logMessage("View transfer complete",
                PARTIAL_TRANSFER_COMPLETE_FOR_DB_OBJECTS.getMessageDefinition(VIEWS, "" + (end - start)/1000));
    }

    /**
     * Triggers the transfer of all available columns, depending also on inclusions and exclusions
     *
     * @param databaseElement database element
     * @param schemas schemas
     */
    private void transferColumns(OpenMetadataRootElement databaseElement, List<OpenMetadataRootElement> schemas)
    {
        long start = System.currentTimeMillis();

         schemas.parallelStream()
                 .filter(schema -> transferCustomizations.shouldTransferSchema(omas.getDisplayName(schema)))
                 .flatMap(s -> omas.getTables(s.getElementHeader().getGUID()).parallelStream())
                 .filter(table -> transferCustomizations.shouldTransferTable(omas.getDisplayName(table)))
                 .peek(table -> {
                     String schemaName = omas.getAdditionalProperties(table).get(Jdbc.JDBC_SCHEMA_KEY);
                     String tableName = omas.getDisplayName(table);
                     String tableGuid = table.getElementHeader().getGUID();

                     List<JdbcPrimaryKey> jdbcPrimaryKeys = jdbc.getPrimaryKeys(schemaName, tableName);
                     // already known columns by the omas, previously transferred
                     List<OpenMetadataRootElement> omasColumns = omas.getColumns(tableGuid);
                     // a column update will always occur as long as the column is returned by jdbc
                     List<OpenMetadataRootElement> omasUpdatedColumns = jdbc.getColumns(catalog, schemaName, tableName).parallelStream()
                             .filter(column -> transferCustomizations.shouldTransferColumn(column.getColumnName()))
                             .map(new ColumnTransfer(omas, databaseElement, auditLog, omasColumns, jdbcPrimaryKeys, table)).collect(Collectors.toList());

                     // will remove all updated column, and what remains are the ones deleted in jdbc
                     omasColumns.removeAll(omasUpdatedColumns);
                     // remove from omas the columns deleted in jdbc
                     omasColumns.forEach(omas::removeColumn);
                }).collect(Collectors.toList());

        String excludedColumns = transferCustomizations.getExcludedColumns();
        if(StringUtils.isNotEmpty(excludedColumns)) {
            auditLog.logMessage(SKIPPING + COLUMNS,
                    TRANSFER_EXCEPTIONS_FOR_DB_OBJECT.getMessageDefinition(COLUMNS, excludedColumns));
        }
        long end = System.currentTimeMillis();
        auditLog.logMessage("Column transfer complete",
                PARTIAL_TRANSFER_COMPLETE_FOR_DB_OBJECTS.getMessageDefinition(COLUMNS, "" + (end - start)/1000));
    }

    /**
     * Triggers the transfer of all foreign keys, depending also on inclusions and exclusions. The reason for doing this
     * at database level is that a foreign key relationship can exist between columns located in tables in different schemas
     *
     * @param databaseElement database element
     */
    private void transferForeignKeys(OpenMetadataRootElement databaseElement)
    {
        long start = System.currentTimeMillis();

        // all foreign keys as returned by calling getExportedKeys and getImportedKeys on jdbc
        Set<JdbcForeignKey> foreignKeys = Stream.concat(
                jdbc.getSchemas(catalog).stream().filter(schema -> transferCustomizations.shouldTransferSchema(schema.getTableSchem()))
                        .flatMap(s -> jdbc.getTables(catalog, s.getTableSchem()).stream()
                                .filter(table -> transferCustomizations.shouldTransferTable(table.getTableName())))
                        .flatMap(t -> jdbc.getImportedKeys(catalog, t.getTableSchem(), t.getTableName()).stream()),
                jdbc.getSchemas(catalog).stream().filter(schema -> transferCustomizations.shouldTransferSchema(schema.getTableSchem()))
                        .flatMap(s -> jdbc.getTables(catalog, s.getTableSchem()).stream()
                                .filter(table -> transferCustomizations.shouldTransferTable(table.getTableName())))
                        .flatMap(t -> jdbc.getExportedKeys(catalog, t.getTableSchem(), t.getTableName()).stream())
        ).collect(Collectors.toSet());

        foreignKeys.forEach(new ForeignKeyTransfer(omas, auditLog, databaseElement));

        long end = System.currentTimeMillis();
        auditLog.logMessage("Foreign key transfer complete",
                PARTIAL_TRANSFER_COMPLETE_FOR_DB_OBJECTS.getMessageDefinition("foreign keys", "" + (end - start)/1000));
    }

}
