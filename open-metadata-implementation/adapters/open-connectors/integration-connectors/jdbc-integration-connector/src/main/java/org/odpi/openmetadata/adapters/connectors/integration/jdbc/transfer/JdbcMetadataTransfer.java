/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.integration.jdbc.transfer;

import org.apache.commons.lang3.StringUtils;
import org.odpi.openmetadata.accessservices.datamanager.metadataelements.DatabaseColumnElement;
import org.odpi.openmetadata.accessservices.datamanager.metadataelements.DatabaseElement;
import org.odpi.openmetadata.accessservices.datamanager.metadataelements.DatabaseSchemaElement;
import org.odpi.openmetadata.accessservices.datamanager.metadataelements.DatabaseTableElement;
import org.odpi.openmetadata.accessservices.datamanager.metadataelements.DatabaseViewElement;
import org.odpi.openmetadata.adapters.connectors.integration.jdbc.transfer.customization.TransferCustomizations;
import org.odpi.openmetadata.adapters.connectors.integration.jdbc.transfer.model.JdbcForeignKey;
import org.odpi.openmetadata.adapters.connectors.integration.jdbc.transfer.model.JdbcPrimaryKey;
import org.odpi.openmetadata.adapters.connectors.integration.jdbc.transfer.requests.Jdbc;
import org.odpi.openmetadata.adapters.connectors.integration.jdbc.transfer.requests.Omas;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.integrationservices.database.connector.DatabaseIntegratorContext;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.odpi.openmetadata.adapters.connectors.integration.jdbc.ffdc.JDBCIntegrationConnectorAuditCode.EXITING_ON_DATABASE_TRANSFER_FAIL;
import static org.odpi.openmetadata.adapters.connectors.integration.jdbc.ffdc.JDBCIntegrationConnectorAuditCode.PARTIAL_TRANSFER_COMPLETE_FOR_DB_OBJECTS;
import static org.odpi.openmetadata.adapters.connectors.integration.jdbc.ffdc.JDBCIntegrationConnectorAuditCode.TRANSFER_EXCEPTIONS_FOR_DB_OBJECT;

/**
 * Transfers metadata from jdbc in an exploratory way. What can be accessed will be transferred
 */
public class JdbcMetadataTransfer {

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
    private final String databaseManagerName;
    private final String address;
    private final String connectorTypeQualifiedName;
    private final String catalog;
    private final TransferCustomizations transferCustomizations;

    private final AuditLog auditLog;

    public JdbcMetadataTransfer(JdbcMetadata jdbcMetadata, DatabaseIntegratorContext databaseIntegratorContext, String address,
                                String connectorTypeQualifiedName, String catalog,  TransferCustomizations transferCustomizations,
                                AuditLog auditLog) {
        this.jdbc = new Jdbc(jdbcMetadata, auditLog);
        this.omas = new Omas(databaseIntegratorContext, auditLog);
        this.databaseManagerName = databaseIntegratorContext.getDatabaseManagerName();
        this.address = address;
        this.connectorTypeQualifiedName = connectorTypeQualifiedName;
        this.catalog = catalog;
        this.transferCustomizations = transferCustomizations;
        this.auditLog = auditLog;
    }

    /**
     * Triggers database, schema, table and column metadata transfer. Will do the best it can to transfer as much of the
     * metadata as possible. If available will also build the asset (database) connection structure
     */
    public void execute() {
        String methodName = "JdbcMetadataTransfer.execute";

        DatabaseElement database = new DatabaseTransfer(jdbc, databaseManagerName, address, catalog, omas, auditLog).execute();
        if (database == null) {
            auditLog.logMessage("Verifying database metadata transferred. None found. Stopping transfer",
                    EXITING_ON_DATABASE_TRANSFER_FAIL.getMessageDefinition(methodName));
            return;
        }

        createAssetConnection(database);

        transferTablesWithoutSchema(database);
        transferViewsWithoutSchema(database);
        transferColumnsOfTablesWithoutSchema(database);
        transferForeignKeysIgnoringSchemas(database);

        transferSchemas(database);
        List<DatabaseSchemaElement> schemas = omas.getSchemas(database.getElementHeader().getGUID());
        if(schemas.isEmpty()){
            return;
        }
        transferTables(database, schemas);
        transferViews(database, schemas);
        transferColumns(database, schemas);
        transferForeignKeys(database);
    }

    /**
     * Triggers the transfer of available tables that are not assigned to any schema, depending also on inclusions and
     * exclusions
     *
     * @param databaseElement database element
     */
    private void transferTablesWithoutSchema(DatabaseElement databaseElement) {
        long start = System.currentTimeMillis();

        String databaseQualifiedName = databaseElement.getDatabaseProperties().getQualifiedName();
        String databaseGuid = databaseElement.getElementHeader().getGUID();

        // already known tables by the omas, previously transferred
        List<DatabaseTableElement> omasTables = omas.getTables(databaseGuid);
        // a table update will always occur as long as the table is returned by jdbc
        List<DatabaseTableElement> omasTablesUpdated = jdbc.getTables(catalog,"").parallelStream()
                .filter(jdbcTable -> jdbcTable.getTableSchem() == null || jdbcTable.getTableSchem().length() < 1 )
                .filter(table -> transferCustomizations.shouldTransferTable(table.getTableName()))
                .map(new TableTransfer(omas, auditLog, omasTables, databaseQualifiedName, databaseGuid))
                .collect(Collectors.toList());

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
    private void transferViewsWithoutSchema(DatabaseElement databaseElement) {
        long start = System.currentTimeMillis();

        String databaseQualifiedName = databaseElement.getDatabaseProperties().getQualifiedName();
        String databaseGuid = databaseElement.getElementHeader().getGUID();

        // already known views by the omas, previously transferred
        List<DatabaseViewElement> omasViews = omas.getViews(databaseGuid);
        // a view update will always occur as long as the view is returned by jdbc
        List<DatabaseViewElement> omasViewsUpdated = jdbc.getViews(catalog,"").parallelStream()
                .filter(jdbcView -> jdbcView.getTableSchem() == null || jdbcView.getTableSchem().length() < 1 )
                .filter(view -> transferCustomizations.shouldTransferTable(view.getTableName()))
                .map(new ViewTransfer(omas, auditLog, omasViews, databaseQualifiedName, databaseGuid))
                .collect(Collectors.toList());

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
    private void transferColumnsOfTablesWithoutSchema(DatabaseElement databaseElement){
        long start = System.currentTimeMillis();

        String databaseGuid = databaseElement.getElementHeader().getGUID();

        omas.getTables(databaseGuid).parallelStream()
                .filter(table -> transferCustomizations.shouldTransferTable(table.getDatabaseTableProperties().getDisplayName()))
                .peek(table -> {
                    String schemaName = "";
                    String tableName = table.getDatabaseTableProperties().getDisplayName();
                    String tableGuid = table.getElementHeader().getGUID();

                    List<JdbcPrimaryKey> jdbcPrimaryKeys = jdbc.getPrimaryKeys(schemaName, tableName);
                    // already known columns by the omas, previously transferred
                    List<DatabaseColumnElement> omasColumns = omas.getColumns(tableGuid);
                    // a column update will always occur as long as the column is returned by jdbc
                    List<DatabaseColumnElement> omasUpdatedColumns = jdbc.getColumns(catalog, schemaName, tableName).parallelStream()
                            .filter(column -> transferCustomizations.shouldTransferColumn(column.getColumnName()))
                            .map(new ColumnTransfer(omas, auditLog, omasColumns, jdbcPrimaryKeys, table)).collect(Collectors.toList());

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
    private void transferForeignKeysIgnoringSchemas(DatabaseElement databaseElement){
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

    private void createAssetConnection(DatabaseElement databaseElement){
        CreateConnectionStructure createConnectionStructure = new CreateConnectionStructure(omas, jdbc,
                connectorTypeQualifiedName, auditLog);
        createConnectionStructure.accept(databaseElement);
    }

    /**
     * Triggers the transfer of all available schemas, depending also on inclusions and exclusions
     *
     * @param databaseElement database
     */
    private void transferSchemas(DatabaseElement databaseElement){
        long start = System.currentTimeMillis();

        String databaseQualifiedName = databaseElement.getDatabaseProperties().getQualifiedName();
        String databaseGuid = databaseElement.getElementHeader().getGUID();

        // already known schemas by the omas, previously transferred
        List<DatabaseSchemaElement> omasSchemas = omas.getSchemas(databaseGuid);
        // a schema update will always occur as long as the schema is returned by jdbc
        List<DatabaseSchemaElement> omasSchemasUpdated =
                jdbc.getSchemas(catalog).parallelStream()
                        .filter(schema -> transferCustomizations.shouldTransferSchema(schema.getTableSchem()))
                        .map(new SchemaTransfer(omas, auditLog, omasSchemas, databaseQualifiedName, databaseGuid))
                        .collect(Collectors.toList());

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
    private void transferTables(DatabaseElement databaseElement, List<DatabaseSchemaElement> schemas){
        long start = System.currentTimeMillis();

        schemas.parallelStream()
                .filter(schema -> transferCustomizations.shouldTransferSchema(schema.getDatabaseSchemaProperties().getName()))
                .peek(schema -> {
            String schemaDisplayName = schema.getDatabaseSchemaProperties().getName();
            String schemaGuid = schema.getElementHeader().getGUID();
            String schemaQualifiedName = schema.getDatabaseSchemaProperties().getQualifiedName();

            // already known tables by the omas, previously transferred
            List<DatabaseTableElement> omasTables = omas.getTables(schemaGuid);
            // a table update will always occur as long as the table is returned by jdbc
            List<DatabaseTableElement> omasTablesUpdated = jdbc.getTables(catalog, schemaDisplayName).parallelStream()
                    .filter(table -> transferCustomizations.shouldTransferTable(table.getTableName()))
                    .map(new TableTransfer(omas, auditLog, omasTables, schemaQualifiedName, schemaGuid))
                    .collect(Collectors.toList());

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
    private void transferViews(DatabaseElement databaseElement, List<DatabaseSchemaElement> schemas){
        long start = System.currentTimeMillis();

        schemas.parallelStream()
                .filter(schema -> transferCustomizations.shouldTransferSchema(schema.getDatabaseSchemaProperties().getName()))
                .peek(schema -> {
                    String schemaDisplayName = schema.getDatabaseSchemaProperties().getName();
                    String schemaGuid = schema.getElementHeader().getGUID();
                    String schemaQualifiedName = schema.getDatabaseSchemaProperties().getQualifiedName();

                    // already known views by the omas, previously transferred
                    List<DatabaseViewElement> omasViews = omas.getViews(schemaGuid);
                    // a view update will always occur as long as the view is returned by jdbc
                    List<DatabaseViewElement> omasViewsUpdated = jdbc.getViews(catalog, schemaDisplayName).parallelStream()
                            .filter(jdbcView -> transferCustomizations.shouldTransferTable(jdbcView.getTableName()))
                            .map(new ViewTransfer(omas, auditLog, omasViews, schemaQualifiedName, schemaGuid))
                            .collect(Collectors.toList());

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
    private void transferColumns(DatabaseElement databaseElement, List<DatabaseSchemaElement> schemas){
        long start = System.currentTimeMillis();

         schemas.parallelStream()
                 .filter(schema -> transferCustomizations.shouldTransferSchema(schema.getDatabaseSchemaProperties().getName()))
                 .flatMap(s -> omas.getTables(s.getElementHeader().getGUID()).parallelStream())
                 .filter(table -> transferCustomizations.shouldTransferTable(table.getDatabaseTableProperties().getDisplayName()))
                 .peek(table -> {
                     String schemaName = table.getDatabaseTableProperties().getAdditionalProperties().get(Jdbc.JDBC_SCHEMA_KEY);
                     String tableName = table.getDatabaseTableProperties().getDisplayName();
                     String tableGuid = table.getElementHeader().getGUID();

                     List<JdbcPrimaryKey> jdbcPrimaryKeys = jdbc.getPrimaryKeys(schemaName, tableName);
                     // already known columns by the omas, previously transferred
                     List<DatabaseColumnElement> omasColumns = omas.getColumns(tableGuid);
                     // a column update will always occur as long as the column is returned by jdbc
                     List<DatabaseColumnElement> omasUpdatedColumns = jdbc.getColumns(catalog, schemaName, tableName).parallelStream()
                             .filter(column -> transferCustomizations.shouldTransferColumn(column.getColumnName()))
                             .map(new ColumnTransfer(omas, auditLog, omasColumns, jdbcPrimaryKeys, table)).collect(Collectors.toList());

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
    private void transferForeignKeys(DatabaseElement databaseElement){
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
