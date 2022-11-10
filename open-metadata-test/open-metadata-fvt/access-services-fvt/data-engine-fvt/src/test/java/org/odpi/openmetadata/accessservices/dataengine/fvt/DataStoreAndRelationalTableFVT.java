/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.dataengine.fvt;


import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.odpi.openmetadata.accessservices.dataengine.RepositoryService;
import org.odpi.openmetadata.accessservices.dataengine.client.DataEngineClient;
import org.odpi.openmetadata.accessservices.dataengine.model.Attribute;
import org.odpi.openmetadata.accessservices.dataengine.model.DataFile;
import org.odpi.openmetadata.accessservices.dataengine.model.Database;
import org.odpi.openmetadata.accessservices.dataengine.model.DatabaseSchema;
import org.odpi.openmetadata.accessservices.dataengine.model.RelationalColumn;
import org.odpi.openmetadata.accessservices.dataengine.model.RelationalTable;
import org.odpi.openmetadata.accessservices.dataengine.model.Engine;
import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectorCheckedException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.EntityNotKnownException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.FunctionNotSupportedException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.PagingErrorException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.PropertyErrorException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.RepositoryErrorException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.TypeErrorException;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

/**
 * Holds FVTs related to types Database, RelationalTable and DataFile
 */
public class DataStoreAndRelationalTableFVT extends DataEngineFVT {

    @ParameterizedTest
    @MethodSource("org.odpi.openmetadata.accessservices.dataengine.PlatformConnectionProvider#getConnectionDetails")
    public void upsertDatabase(String userId, DataEngineClient dataEngineClient, RepositoryService repositoryService)
            throws UserNotAuthorizedException, ConnectorCheckedException, PropertyServerException, InvalidParameterException,
            org.odpi.openmetadata.repositoryservices.ffdc.exception.UserNotAuthorizedException, FunctionNotSupportedException,
            org.odpi.openmetadata.repositoryservices.ffdc.exception.InvalidParameterException, RepositoryErrorException,
            PropertyErrorException, TypeErrorException, PagingErrorException, EntityNotKnownException {

        engineSetupService.createExternalDataEngine(userId, dataEngineClient, null);
        Database database = dataStoreAndRelationalTableSetupService.upsertDatabase(userId, dataEngineClient, null);

        // assert Database
        List<EntityDetail> databases = repositoryService.findEntityByPropertyValue(DATABASE_TYPE_GUID, database.getQualifiedName());
        assertDatabase(database, databases);
    }

    @ParameterizedTest
    @MethodSource("org.odpi.openmetadata.accessservices.dataengine.PlatformConnectionProvider#getConnectionDetails")
    public void deleteDatabase(String userId, DataEngineClient dataEngineClient, RepositoryService repositoryService)
            throws UserNotAuthorizedException, ConnectorCheckedException, PropertyServerException, InvalidParameterException,
            org.odpi.openmetadata.repositoryservices.ffdc.exception.UserNotAuthorizedException, FunctionNotSupportedException,
            org.odpi.openmetadata.repositoryservices.ffdc.exception.InvalidParameterException, RepositoryErrorException,
            PropertyErrorException, TypeErrorException, PagingErrorException {

        engineSetupService.createExternalDataEngine(userId, dataEngineClient, null);
        Database database = dataStoreAndRelationalTableSetupService.upsertDatabase(userId, dataEngineClient, getDatabaseToDelete());

        // assert Database
        List<EntityDetail> databases = repositoryService.findEntityByPropertyValue(DATABASE_TYPE_GUID, database.getQualifiedName());
        EntityDetail databaseAsEntityDetail = assertDatabase(database, databases);

        // delete Database
        dataStoreAndRelationalTableSetupService.deleteDatabase(userId, dataEngineClient, database.getQualifiedName(),
                databaseAsEntityDetail.getGUID());
        List<EntityDetail> databasesAfterDelete = repositoryService
                .findEntityByPropertyValue(DATABASE_TYPE_GUID, database.getQualifiedName());
        assertNull(databasesAfterDelete);
    }

    private Database getDatabaseToDelete(){
        Database database = new Database();
        database.setQualifiedName("to-delete-database-qualified-name");
        database.setDisplayName("to-delete-database-display-name");
        database.setDescription("to-delete-database-description");
        database.setDatabaseType("to-delete-database-type");
        database.setDatabaseVersion("to-delete-database-version");
        database.setDatabaseInstance("to-delete-database-instance");
        database.setDatabaseImportedFrom("to-delete-database-imported-from");
        database.setNetworkAddress("to-delete-database-network-address");
        return database;
    }

    @ParameterizedTest
    @MethodSource("org.odpi.openmetadata.accessservices.dataengine.PlatformConnectionProvider#getConnectionDetails")
    public void upsertDatabaseSchema(String userId, DataEngineClient dataEngineClient, RepositoryService repositoryService)
            throws UserNotAuthorizedException, ConnectorCheckedException, PropertyServerException, InvalidParameterException,
            org.odpi.openmetadata.repositoryservices.ffdc.exception.UserNotAuthorizedException, FunctionNotSupportedException,
            org.odpi.openmetadata.repositoryservices.ffdc.exception.InvalidParameterException, RepositoryErrorException,
            PropertyErrorException, TypeErrorException, PagingErrorException, EntityNotKnownException {

        engineSetupService.createExternalDataEngine(userId, dataEngineClient, null);
        Database database = dataStoreAndRelationalTableSetupService.upsertDatabase(userId, dataEngineClient, null);
        DatabaseSchema databaseSchema = dataStoreAndRelationalTableSetupService.upsertDatabaseSchema(userId,
                dataEngineClient, null, database.getQualifiedName(), false);

        // assert DatabaseSchema
        List<EntityDetail> databaseSchemas = repositoryService.findEntityByPropertyValue(DEPLOYED_DATABASE_SCHEMA_TYPE_GUID,
                databaseSchema.getQualifiedName());
        assertDatabaseSchema(databaseSchema, databaseSchemas);
    }

    @ParameterizedTest
    @MethodSource("org.odpi.openmetadata.accessservices.dataengine.PlatformConnectionProvider#getConnectionDetails")
    public void deleteDatabaseSchema(String userId, DataEngineClient dataEngineClient, RepositoryService repositoryService)
            throws UserNotAuthorizedException, ConnectorCheckedException, PropertyServerException, InvalidParameterException,
            org.odpi.openmetadata.repositoryservices.ffdc.exception.UserNotAuthorizedException, FunctionNotSupportedException,
            org.odpi.openmetadata.repositoryservices.ffdc.exception.InvalidParameterException, RepositoryErrorException,
            PropertyErrorException, TypeErrorException, PagingErrorException {

        engineSetupService.createExternalDataEngine(userId, dataEngineClient, null);
        Database database = dataStoreAndRelationalTableSetupService.upsertDatabase(userId, dataEngineClient, getDatabaseToDelete());
        DatabaseSchema databaseSchema = dataStoreAndRelationalTableSetupService.upsertDatabaseSchema(userId,
                dataEngineClient, getDatabaseSchemaToDelete(), database.getQualifiedName(), false);

        // assert DatabaseSchema
        List<EntityDetail> databaseSchemas = repositoryService.findEntityByPropertyValue(DEPLOYED_DATABASE_SCHEMA_TYPE_GUID,
                databaseSchema.getQualifiedName());
        EntityDetail databaseSchemaAsEntityDetail = assertDatabaseSchema(databaseSchema, databaseSchemas);

        // delete DatabaseSchema
        dataStoreAndRelationalTableSetupService.deleteDatabaseSchema(userId, dataEngineClient, databaseSchema.getQualifiedName(),
                databaseSchemaAsEntityDetail.getGUID());
        List<EntityDetail> databaseSchemasAfterDelete = repositoryService
                .findEntityByPropertyValue(DEPLOYED_DATABASE_SCHEMA_TYPE_GUID, databaseSchema.getQualifiedName());
        assertNull(databaseSchemasAfterDelete);
    }

    private DatabaseSchema getDatabaseSchemaToDelete() {
        DatabaseSchema databaseSchema = new DatabaseSchema();
        databaseSchema.setQualifiedName("to-delete-database-schema-qualified-name");
        databaseSchema.setDisplayName("to-delete-database-schema-display-name");
        databaseSchema.setDescription("to-delete-database-schema-description");
        return databaseSchema;
    }

    @ParameterizedTest
    @MethodSource("org.odpi.openmetadata.accessservices.dataengine.PlatformConnectionProvider#getConnectionDetails")
    public void upsertRelationalTable(String userId, DataEngineClient dataEngineClient, RepositoryService repositoryService)
            throws UserNotAuthorizedException, ConnectorCheckedException, PropertyServerException, InvalidParameterException,
            org.odpi.openmetadata.repositoryservices.ffdc.exception.UserNotAuthorizedException, FunctionNotSupportedException,
            org.odpi.openmetadata.repositoryservices.ffdc.exception.InvalidParameterException, RepositoryErrorException,
            PropertyErrorException, TypeErrorException, PagingErrorException, EntityNotKnownException {

        engineSetupService.createExternalDataEngine(userId, dataEngineClient, null);
        Database database = dataStoreAndRelationalTableSetupService.upsertDatabase(userId, dataEngineClient, null);
        DatabaseSchema databaseSchema = dataStoreAndRelationalTableSetupService.upsertDatabaseSchema(userId, dataEngineClient,
                null, database.getQualifiedName(), false);
        RelationalTable relationalTable = dataStoreAndRelationalTableSetupService.upsertRelationalTable(userId, dataEngineClient,
                null, databaseSchema.getQualifiedName(), false);

        // assert Relational Table
        List<EntityDetail> relationalTables = repositoryService
                .findEntityByPropertyValue(RELATIONAL_TABLE_TYPE_GUID, relationalTable.getQualifiedName());
        EntityDetail relationalTableAsEntityDetail = assertRelationalTable(relationalTable, relationalTables);

        // assert Relational DB Schema Type
        List<EntityDetail> relationalSchemas = repositoryService
                .getRelatedEntities(relationalTableAsEntityDetail.getGUID(), ATTRIBUTE_FOR_SCHEMA_RELATIONSHIP_GUID);
        assertNotNull(relationalSchemas);
        assertEquals(1, relationalSchemas.size());

        EntityDetail relationalDbSchemaAsEntityDetail = relationalSchemas.get(0);
        assertEquals("SchemaOf:" + databaseSchema.getQualifiedName(),
                relationalDbSchemaAsEntityDetail.getProperties().getPropertyValue(QUALIFIED_NAME).valueAsString());

        // assert Relational Columns
        List<EntityDetail> relationalColumns = repositoryService
                .getRelatedEntities(relationalTableAsEntityDetail.getGUID(), NESTED_SCHEMA_ATTRIBUTE_RELATIONSHIP_GUID);
        assertNotNull(relationalColumns);
        assertEquals(1, relationalColumns.size());

        EntityDetail relationalColumnAsEntityDetail = relationalColumns.get(0);
        assertEquals(relationalTable.getColumns().get(0).getDisplayName(),
                relationalColumnAsEntityDetail.getProperties().getPropertyValue(DISPLAY_NAME).valueAsString());
        assertEquals(relationalTable.getColumns().get(0).getDescription(),
                relationalColumnAsEntityDetail.getProperties().getPropertyValue(DESCRIPTION).valueAsString());
    }

    @ParameterizedTest
    @MethodSource("org.odpi.openmetadata.accessservices.dataengine.PlatformConnectionProvider#getConnectionDetails")
    public void deleteRelationalTable(String userId, DataEngineClient dataEngineClient, RepositoryService repositoryService)
            throws UserNotAuthorizedException, ConnectorCheckedException, PropertyServerException, InvalidParameterException,
            org.odpi.openmetadata.repositoryservices.ffdc.exception.UserNotAuthorizedException, FunctionNotSupportedException,
            org.odpi.openmetadata.repositoryservices.ffdc.exception.InvalidParameterException, RepositoryErrorException,
            PropertyErrorException, TypeErrorException, PagingErrorException {

        engineSetupService.createExternalDataEngine(userId, dataEngineClient, null);
        Database database = dataStoreAndRelationalTableSetupService.upsertDatabase(userId, dataEngineClient, null);
        DatabaseSchema databaseSchema = dataStoreAndRelationalTableSetupService.upsertDatabaseSchema(userId, dataEngineClient,
                null, database.getQualifiedName(), false);
        RelationalTable relationalTable = dataStoreAndRelationalTableSetupService.upsertRelationalTable(userId,
                dataEngineClient, getRelationalTableToDelete(), databaseSchema.getQualifiedName(), false);

        // assert Relational Table
        List<EntityDetail> relationalTables = repositoryService
                .findEntityByPropertyValue(RELATIONAL_TABLE_TYPE_GUID, relationalTable.getQualifiedName());
        EntityDetail relationalTableAsEntityDetail = assertRelationalTable(relationalTable, relationalTables);

        // delete Relational Table
        dataStoreAndRelationalTableSetupService.deleteRelationalTable(userId, dataEngineClient, relationalTable.getQualifiedName(),
                relationalTableAsEntityDetail.getGUID());
        List<EntityDetail> relationalTablesToDelete = repositoryService
                .findEntityByPropertyValue(RELATIONAL_TABLE_TYPE_GUID, relationalTable.getQualifiedName());
        assertNull(relationalTablesToDelete);
    }

    private RelationalTable getRelationalTableToDelete(){
        RelationalTable relationalTable = new RelationalTable();
        relationalTable.setQualifiedName("to-delete-to-delete-relational-table-qualified-name");
        relationalTable.setDisplayName("to-delete-relational-table-display-name");
        relationalTable.setDescription("to-delete-relational-table-description");
        relationalTable.setType("to-delete-relational-table-type");
        relationalTable.setColumns(buildToDeleteRelationalColumns());
        return relationalTable;
    }

    private List<RelationalColumn> buildToDeleteRelationalColumns(){
        List<RelationalColumn> columns = new ArrayList<>();

        RelationalColumn column = new RelationalColumn();
        column.setQualifiedName("to-delete-relational-column-qualified-name");
        column.setDisplayName("to-delete-relational-column-display-name");
        column.setDescription("to-delete-relational-column-description");
        column.setDataType("to-delete-relational-column-data-type");
        columns.add(column);

        return columns;
    }

    @ParameterizedTest
    @MethodSource("org.odpi.openmetadata.accessservices.dataengine.PlatformConnectionProvider#getConnectionDetails")
    public void upsertDataFile(String userId, DataEngineClient dataEngineClient, RepositoryService repositoryService)
            throws UserNotAuthorizedException, ConnectorCheckedException, PropertyServerException, InvalidParameterException,
            org.odpi.openmetadata.repositoryservices.ffdc.exception.UserNotAuthorizedException, FunctionNotSupportedException,
            org.odpi.openmetadata.repositoryservices.ffdc.exception.InvalidParameterException, RepositoryErrorException,
            PropertyErrorException, TypeErrorException, PagingErrorException, EntityNotKnownException {

        engineSetupService.createExternalDataEngine(userId, dataEngineClient, null);
        DataFile dataFile = dataStoreAndRelationalTableSetupService.upsertDataFile(userId, dataEngineClient, null);

        // assert Data File
        List<EntityDetail> dataFiles = repositoryService
                .findEntityByPropertyValue(DATAFILE_TYPE_GUID, dataFile.getQualifiedName());
        EntityDetail dataFileAsEntityDetail = assertDataFile(dataFile, dataFiles);

        // assert Tabular Schema Type
        List<EntityDetail> tabularSchemas = repositoryService
                .getRelatedEntities(dataFileAsEntityDetail.getGUID(), ASSET_SCHEMA_TYPE_RELATIONSHIP_GUID);
        assertNotNull(tabularSchemas);
        assertEquals(1, tabularSchemas.size());

        EntityDetail tabularSchemaTypeAsEntityDetail = tabularSchemas.get(0);
        assertEquals("Schema", tabularSchemaTypeAsEntityDetail.getProperties().getPropertyValue(DISPLAY_NAME).valueAsString());
        assertEquals(dataFileAsEntityDetail.getProperties().getPropertyValue(QUALIFIED_NAME).valueAsString() + "::schema",
                tabularSchemaTypeAsEntityDetail.getProperties().getPropertyValue(QUALIFIED_NAME).valueAsString());

        // assert Tabular Column
        List<EntityDetail> tabularColumns = repositoryService
                .getRelatedEntities(tabularSchemaTypeAsEntityDetail.getGUID(), ATTRIBUTE_FOR_SCHEMA_RELATIONSHIP_GUID);
        assertNotNull(tabularColumns);
        assertEquals(1, tabularColumns.size());

        EntityDetail tabularColumnAsEntityDetail = tabularColumns.get(0);
        assertEquals(dataFile.getColumns().get(0).getDisplayName(),
                tabularColumnAsEntityDetail.getProperties().getPropertyValue(DISPLAY_NAME).valueAsString());
        assertEquals(dataFile.getColumns().get(0).getDescription(),
                tabularColumnAsEntityDetail.getProperties().getPropertyValue(DESCRIPTION).valueAsString());
    }

    @ParameterizedTest
    @MethodSource("org.odpi.openmetadata.accessservices.dataengine.PlatformConnectionProvider#getConnectionDetails")
    public void deleteDataFileAndFileFolder(String userId, DataEngineClient dataEngineClient, RepositoryService repositoryService)
            throws UserNotAuthorizedException, ConnectorCheckedException, PropertyServerException, InvalidParameterException,
            org.odpi.openmetadata.repositoryservices.ffdc.exception.UserNotAuthorizedException, FunctionNotSupportedException,
            org.odpi.openmetadata.repositoryservices.ffdc.exception.InvalidParameterException, RepositoryErrorException,
            PropertyErrorException, TypeErrorException, PagingErrorException {

        Engine engine = engineSetupService
                .createExternalDataEngine(userId, dataEngineClient, null);
        DataFile dataFile = dataStoreAndRelationalTableSetupService
                .upsertDataFile(userId, dataEngineClient, getDataFileToDelete());

        // assert Data File
        List<EntityDetail> dataFiles = repositoryService
                .findEntityByPropertyValue(DATAFILE_TYPE_GUID, dataFile.getQualifiedName());
        EntityDetail dataFileAsEntityDetail = assertDataFile(dataFile, dataFiles);

        // delete Data File
        dataStoreAndRelationalTableSetupService.deleteDataFile(userId, dataEngineClient, dataFile.getQualifiedName(),
                dataFileAsEntityDetail.getGUID());
        List<EntityDetail> dataFilesToDelete = repositoryService
                .findEntityByPropertyValue(DATAFILE_TYPE_GUID, dataFile.getQualifiedName());
        assertNull(dataFilesToDelete);

        // delete FileFolder
        String fileFolderQualifiedName = engine.getQualifiedName() + "::/to-delete-data-file-pathname";
        List<EntityDetail> fileFolders = repositoryService.findEntityByPropertyValue(FILE_FOLDER_TYPE_GUID, fileFolderQualifiedName);
        assertNotNull(fileFolders);
        assertEquals(1, fileFolders.size());
        EntityDetail fileFolderAsEntityDetail = fileFolders.get(0);

        dataStoreAndRelationalTableSetupService.deleteFolder(userId, dataEngineClient, fileFolderQualifiedName,
                fileFolderAsEntityDetail.getGUID());
        List<EntityDetail> fileFoldersToDelete = repositoryService
                .findEntityByPropertyValue(FILE_FOLDER_TYPE_GUID, fileFolderQualifiedName);
        assertNull(fileFoldersToDelete);
    }

    private DataFile getDataFileToDelete(){
        DataFile dataFile = new DataFile();
        dataFile.setQualifiedName("to-delete-data-file-qualified-name");
        dataFile.setDisplayName("to-delete-data-file-display-name");
        dataFile.setDescription("to-delete-data-file-description");
        dataFile.setFileType("to-delete-data-file-type");
        dataFile.setProtocol("to-delete-data-file-protocol");
        dataFile.setNetworkAddress("to-delete-data-file-network-address");
        dataFile.setPathName("/to-delete-data-file-pathname/to-delete-data-file-display-name");
        dataFile.setColumns(buildTabularColumns());
        return dataFile;
    }

    private List<Attribute> buildTabularColumns(){
        List<Attribute> columns = new ArrayList<>();

        Attribute column = new Attribute();
        column.setQualifiedName("to-delete-column-qualified-name");
        column.setDisplayName("to-delete-column-display-name");
        column.setDescription("to-delete-column-description");
        columns.add(column);

        return columns;
    }

    @ParameterizedTest
    @MethodSource("org.odpi.openmetadata.accessservices.dataengine.PlatformConnectionProvider#getConnectionDetails")
    public void deleteSchemaType(String userId, DataEngineClient dataEngineClient, RepositoryService repositoryService)
            throws UserNotAuthorizedException, ConnectorCheckedException, PropertyServerException, InvalidParameterException,
            org.odpi.openmetadata.repositoryservices.ffdc.exception.UserNotAuthorizedException, FunctionNotSupportedException,
            org.odpi.openmetadata.repositoryservices.ffdc.exception.InvalidParameterException, RepositoryErrorException,
            PropertyErrorException, TypeErrorException, PagingErrorException {

        engineSetupService.createExternalDataEngine(userId, dataEngineClient, null);
        DataFile dataFile = dataStoreAndRelationalTableSetupService
                .upsertDataFile(userId, dataEngineClient, getDataFileToDelete());

        // delete Tabular Schema Type
        String tabularSchemaTypeQualifiedName = dataFile.getQualifiedName() + "::schema";
        List<EntityDetail> tabularSchemaTypes = repositoryService
                .findEntityByPropertyValue(TABULAR_SCHEMA_TYPE_TYPE_GUID, tabularSchemaTypeQualifiedName);
        assertNotNull(tabularSchemaTypes);
        assertEquals(1, tabularSchemaTypes.size());
        EntityDetail tabularSchemaTypeAsEntityDetail = tabularSchemaTypes.get(0);

        dataStoreAndRelationalTableSetupService.deleteSchemaType(userId, dataEngineClient, tabularSchemaTypeQualifiedName,
                tabularSchemaTypeAsEntityDetail.getGUID());
        List<EntityDetail> tabularSchemaTypeToDelete = repositoryService
                .findEntityByPropertyValue(TABULAR_SCHEMA_TYPE_TYPE_GUID, tabularSchemaTypeQualifiedName);
        assertNull(tabularSchemaTypeToDelete);
    }

}
