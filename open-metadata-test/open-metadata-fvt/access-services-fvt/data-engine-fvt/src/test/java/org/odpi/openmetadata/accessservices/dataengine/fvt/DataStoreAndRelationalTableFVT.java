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
import org.odpi.openmetadata.accessservices.dataengine.model.RelationalColumn;
import org.odpi.openmetadata.accessservices.dataengine.model.RelationalTable;
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
 * This class holds functional verification tests written with the help of the Junit framework. There are parametrized tests
 * covering the creation of an external data engine source and a whole job process containing stages.
 * Depending on the number of the series of parameters of each test method, the tests will run or not multiple times.
 * The parameters are computed in the method indicated in the @MethodSource annotation.
 */
public class DataStoreAndRelationalTableFVT extends DataEngineFVT {

    @ParameterizedTest
    @MethodSource("org.odpi.openmetadata.accessservices.dataengine.PlatformConnectionProvider#getConnectionDetails")
    public void upsertDatabase(String userId, DataEngineClient dataEngineClient, RepositoryService repositoryService)
            throws UserNotAuthorizedException, ConnectorCheckedException, PropertyServerException, InvalidParameterException,
            org.odpi.openmetadata.repositoryservices.ffdc.exception.UserNotAuthorizedException, FunctionNotSupportedException,
            org.odpi.openmetadata.repositoryservices.ffdc.exception.InvalidParameterException, RepositoryErrorException,
            PropertyErrorException, TypeErrorException, PagingErrorException, EntityNotKnownException {

        softwareServerCapabilitySetupServer.createExternalDataEngine(userId, dataEngineClient, null);
        Database database = dataStoreAndRelationalTableSetupService.upsertDatabase(userId, dataEngineClient, null);

        // assert Database
        List<EntityDetail> databases = repositoryService.findEntityByPropertyValue(DATABASE_TYPE_GUID, database.getQualifiedName());
        EntityDetail databaseAsEntityDetail = assertDatabase(database, databases);

        // assert Deployed Database Schema
        List<EntityDetail> schemas = repositoryService
                .getRelatedEntities(databaseAsEntityDetail.getGUID(), DATA_CONTENT_FOR_DATA_SET_RELATIONSHIP_GUID);
        assertNotNull(schemas);
        assertEquals(1, schemas.size());

        EntityDetail deployedDatabaseSchemaAsEntityDetail = schemas.get(0);
        assertEquals(database.getQualifiedName() + ":schema",
                deployedDatabaseSchemaAsEntityDetail.getProperties().getPropertyValue(QUALIFIED_NAME).valueAsString());
    }

    @ParameterizedTest
    @MethodSource("org.odpi.openmetadata.accessservices.dataengine.PlatformConnectionProvider#getConnectionDetails")
    public void deleteDatabase(String userId, DataEngineClient dataEngineClient, RepositoryService repositoryService)
            throws UserNotAuthorizedException, ConnectorCheckedException, PropertyServerException, InvalidParameterException,
            org.odpi.openmetadata.repositoryservices.ffdc.exception.UserNotAuthorizedException, FunctionNotSupportedException,
            org.odpi.openmetadata.repositoryservices.ffdc.exception.InvalidParameterException, RepositoryErrorException,
            PropertyErrorException, TypeErrorException, PagingErrorException, EntityNotKnownException {

        softwareServerCapabilitySetupServer.createExternalDataEngine(userId, dataEngineClient, null);
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
        return database;
    }

    @ParameterizedTest
    @MethodSource("org.odpi.openmetadata.accessservices.dataengine.PlatformConnectionProvider#getConnectionDetails")
    public void upsertRelationalTable(String userId, DataEngineClient dataEngineClient, RepositoryService repositoryService)
            throws UserNotAuthorizedException, ConnectorCheckedException, PropertyServerException, InvalidParameterException,
            org.odpi.openmetadata.repositoryservices.ffdc.exception.UserNotAuthorizedException, FunctionNotSupportedException,
            org.odpi.openmetadata.repositoryservices.ffdc.exception.InvalidParameterException, RepositoryErrorException,
            PropertyErrorException, TypeErrorException, PagingErrorException, EntityNotKnownException {

        softwareServerCapabilitySetupServer.createExternalDataEngine(userId, dataEngineClient, null);
        Database database = dataStoreAndRelationalTableSetupService.upsertDatabase(userId, dataEngineClient, null);
        RelationalTable relationalTable = dataStoreAndRelationalTableSetupService.upsertRelationalTable(userId, dataEngineClient, null);

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
        assertEquals("SchemaOf:" + database.getQualifiedName() + ":schema",
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
            PropertyErrorException, TypeErrorException, PagingErrorException, EntityNotKnownException {

        softwareServerCapabilitySetupServer.createExternalDataEngine(userId, dataEngineClient, null);
        dataStoreAndRelationalTableSetupService.upsertDatabase(userId, dataEngineClient, null);
        RelationalTable relationalTable = dataStoreAndRelationalTableSetupService
                .upsertRelationalTable(userId, dataEngineClient, getRelationalTableToDelete());

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

        softwareServerCapabilitySetupServer.createExternalDataEngine(userId, dataEngineClient, null);
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
    public void deleteDataFile(String userId, DataEngineClient dataEngineClient, RepositoryService repositoryService)
            throws UserNotAuthorizedException, ConnectorCheckedException, PropertyServerException, InvalidParameterException,
            org.odpi.openmetadata.repositoryservices.ffdc.exception.UserNotAuthorizedException, FunctionNotSupportedException,
            org.odpi.openmetadata.repositoryservices.ffdc.exception.InvalidParameterException, RepositoryErrorException,
            PropertyErrorException, TypeErrorException, PagingErrorException, EntityNotKnownException {

        softwareServerCapabilitySetupServer.createExternalDataEngine(userId, dataEngineClient, null);
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
    }

    private DataFile getDataFileToDelete(){
        DataFile dataFile = new DataFile();
        dataFile.setQualifiedName("to-delete-data-file-qualified-name");
        dataFile.setDisplayName("to-delete-data-file-display-name");
        dataFile.setDescription("to-delete-data-file-description");
        dataFile.setFileType("to-delete-data-file-type");
        dataFile.setProtocol("to-delete-data-file-protocol");
        dataFile.setNetworkAddress("to-delete-data-file-network-address");
        dataFile.setPathName("/to-delete-data-file-pathname");
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

}
