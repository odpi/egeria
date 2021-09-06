/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.dataengine.fvt;


import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.odpi.openmetadata.accessservices.dataengine.RepositoryService;
import org.odpi.openmetadata.accessservices.dataengine.client.DataEngineClient;
import org.odpi.openmetadata.accessservices.dataengine.model.DataFile;
import org.odpi.openmetadata.accessservices.dataengine.model.Database;
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

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

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

        softwareServerCapabilitySetupServer.createExternalDataEngine(userId, dataEngineClient);
        Database database = dataStoreAndRelationalTableSetupService.upsertDatabase(userId, dataEngineClient);

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
    public void upsertRelationalTable(String userId, DataEngineClient dataEngineClient, RepositoryService repositoryService)
            throws UserNotAuthorizedException, ConnectorCheckedException, PropertyServerException, InvalidParameterException,
            org.odpi.openmetadata.repositoryservices.ffdc.exception.UserNotAuthorizedException, FunctionNotSupportedException,
            org.odpi.openmetadata.repositoryservices.ffdc.exception.InvalidParameterException, RepositoryErrorException,
            PropertyErrorException, TypeErrorException, PagingErrorException, EntityNotKnownException {

        softwareServerCapabilitySetupServer.createExternalDataEngine(userId, dataEngineClient);
        Database database = dataStoreAndRelationalTableSetupService.upsertDatabase(userId, dataEngineClient);
        RelationalTable relationalTable = dataStoreAndRelationalTableSetupService.upsertRelationalTable(userId, dataEngineClient);

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
    public void upsertDataFile(String userId, DataEngineClient dataEngineClient, RepositoryService repositoryService)
            throws UserNotAuthorizedException, ConnectorCheckedException, PropertyServerException, InvalidParameterException,
            org.odpi.openmetadata.repositoryservices.ffdc.exception.UserNotAuthorizedException, FunctionNotSupportedException,
            org.odpi.openmetadata.repositoryservices.ffdc.exception.InvalidParameterException, RepositoryErrorException,
            PropertyErrorException, TypeErrorException, PagingErrorException, EntityNotKnownException {

        softwareServerCapabilitySetupServer.createExternalDataEngine(userId, dataEngineClient);
        DataFile dataFile = dataStoreAndRelationalTableSetupService.upsertDataFile(userId, dataEngineClient);

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

}
