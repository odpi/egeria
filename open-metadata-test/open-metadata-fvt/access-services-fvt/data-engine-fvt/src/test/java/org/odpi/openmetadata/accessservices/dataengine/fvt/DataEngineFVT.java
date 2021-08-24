/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.dataengine.fvt;


import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.odpi.openmetadata.accessservices.dataengine.DataStoreAndRelationalTableSetupService;
import org.odpi.openmetadata.accessservices.dataengine.ProcessSetupService;
import org.odpi.openmetadata.accessservices.dataengine.RepositoryService;
import org.odpi.openmetadata.accessservices.dataengine.client.DataEngineClient;
import org.odpi.openmetadata.accessservices.dataengine.model.DataFile;
import org.odpi.openmetadata.accessservices.dataengine.model.Database;
import org.odpi.openmetadata.accessservices.dataengine.model.LineageMapping;
import org.odpi.openmetadata.accessservices.dataengine.model.Port;
import org.odpi.openmetadata.accessservices.dataengine.model.PortAlias;
import org.odpi.openmetadata.accessservices.dataengine.model.PortImplementation;
import org.odpi.openmetadata.accessservices.dataengine.model.Process;
import org.odpi.openmetadata.accessservices.dataengine.model.RelationalTable;
import org.odpi.openmetadata.accessservices.dataengine.model.SchemaType;
import org.odpi.openmetadata.accessservices.dataengine.model.SoftwareServerCapability;
import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectorCheckedException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.http.HttpHelper;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Relationship;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.EntityNotKnownException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.FunctionNotSupportedException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.PagingErrorException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.PropertyErrorException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.RepositoryErrorException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.TypeErrorException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.fail;

/**
 * This class holds functional verification tests written with the help of the Junit framework. There are parametrized tests
 * covering the creation of an external data engine source and a whole job process containing stages.
 * Depending on the number of the series of parameters of each test method, the tests will run or not multiple times.
 * The parameters are computed in the method indicated in the @MethodSource annotation.
 */
public class DataEngineFVT {

    private static final String SOFTWARE_SERVER_CAPABILITY_TYPE_GUID = "fe30a033-8f86-4d17-8986-e6166fa24177";
    private static final String DATABASE_TYPE_GUID = "0921c83f-b2db-4086-a52c-0d10e52ca078";
    private static final String DATAFILE_TYPE_GUID = "10752b4a-4b5d-4519-9eae-fdd6d162122f";
    private static final String RELATIONAL_TABLE_TYPE_GUID = "ce7e72b8-396a-4013-8688-f9d973067425";
    private static final String PROCESS_TYPE_GUID = "d8f33bd7-afa9-4a11-a8c7-07dcec83c050";
    private static final String PORT_IMPLEMENTATION_TYPE_GUID = "ADbbdF06-a6A3-4D5F-7fA3-DB4Cb0eDeC0E";
    private static final String PORT_ALIAS_TYPE_GUID = "DFa5aEb1-bAb4-c25B-bDBD-B95Ce6fAB7F5";
    private static final String OWNERSHIP_CLASSIFICATION_GUID = "8139a911-a4bd-432b-a9f4-f6d11c511abe";
    private static final String PROCESS_PORT_RELATIONSHIP_GUID = "fB4E00CF-37e4-88CE-4a94-233BAdB84DA2";
    private static final String PORT_DELEGATION_RELATIONSHIP_GUID = "98bB8BA1-dc6A-eb9D-32Cf-F837bEbCbb8E";
    private static final String PORT_SCHEMA_RELATIONSHIP_GUID = "B216fA00-8281-F9CC-9911-Ae6377f2b457";
    private static final String ATTRIBUTE_FOR_SCHEMA_RELATIONSHIP_GUID = "86b176a2-015c-44a6-8106-54d5d69ba661";
    private static final String LINEAGE_MAPPING_RELATIONSHIP_GUID = "a5991bB2-660D-A3a1-2955-fAcDA2d5F4Ff";
    private static final String ASSET_SCHEMA_TYPE_RELATIONSHIP_GUID = "815b004d-73c6-4728-9dd9-536f4fe803cd";
    private static final String DATA_CONTENT_FOR_DATA_SET_RELATIONSHIP_GUID = "b827683c-2924-4df3-a92d-7be1888e23c0";
    private static final String NESTED_SCHEMA_ATTRIBUTE_RELATIONSHIP_GUID = "0ffb9d87-7074-45da-a9b0-ae0859611133";

    private static final String DESCRIPTION = "description";
    private static final String NAME = "name";
    private static final String TYPE = "type";
    private static final String VERSION = "version";
    private static final String PATCH_LEVEL = "patchLevel";
    private static final String QUALIFIED_NAME = "qualifiedName";
    private static final String DISPLAY_NAME = "displayName";
    private static final String SOURCE = "source";
    private static final String FILE_TYPE = "fileType";
    private static final String DEPLOYED_IMPLEMENTATION_TYPE = "deployedImplementationType";
    private static final String DATABASE_VERSION = "databaseVersion";
    private static final String INSTANCE = "instance";
    private static final String IMPORTED_FROM = "importedFrom";
    private static final String OWNER = "owner";
    private static final String PORT_TYPE = "portType";

    private static final String DATA_ENGINE = "DataEngine";
    private static final String ADMIN = "admin";

    public DataEngineFVT() {
        HttpHelper.noStrictSSL();
    }

    private final ProcessSetupService processSetupService = new ProcessSetupService();
    private final DataStoreAndRelationalTableSetupService dataStoreAndRelationalTableSetupService = new DataStoreAndRelationalTableSetupService();

    @ParameterizedTest
    @MethodSource("org.odpi.openmetadata.accessservices.dataengine.PlatformConnectionProvider#getConnectionDetails")
    public void registerExternalTool(String userId, DataEngineClient dataEngineClient, RepositoryService repositoryService)
            throws InvalidParameterException, UserNotAuthorizedException, PropertyServerException, ConnectorCheckedException,
            org.odpi.openmetadata.repositoryservices.ffdc.exception.UserNotAuthorizedException, FunctionNotSupportedException,
            org.odpi.openmetadata.repositoryservices.ffdc.exception.InvalidParameterException, RepositoryErrorException,
            PropertyErrorException, TypeErrorException, PagingErrorException {

        SoftwareServerCapability softwareServerCapability = processSetupService.createExternalDataEngine(userId, dataEngineClient);

        List<EntityDetail> entityDetails = repositoryService.findEntityByPropertyValue(SOFTWARE_SERVER_CAPABILITY_TYPE_GUID,
                softwareServerCapability.getQualifiedName());
        if (entityDetails == null || entityDetails.isEmpty()) {
            fail();
        }

        assertEquals(1, entityDetails.size());
        EntityDetail entity = entityDetails.get(0);
        assertEquals(softwareServerCapability.getDescription(), entity.getProperties().getPropertyValue(DESCRIPTION).valueAsString());
        assertEquals(softwareServerCapability.getName(), entity.getProperties().getPropertyValue(NAME).valueAsString());
        assertEquals(softwareServerCapability.getEngineType(), entity.getProperties().getPropertyValue(TYPE).valueAsString());
        assertEquals(softwareServerCapability.getEngineVersion(), entity.getProperties().getPropertyValue(VERSION).valueAsString());
        assertEquals(softwareServerCapability.getPatchLevel(), entity.getProperties().getPropertyValue(PATCH_LEVEL).valueAsString());
        assertEquals(softwareServerCapability.getQualifiedName(), entity.getProperties().getPropertyValue(QUALIFIED_NAME).valueAsString());
        assertEquals(softwareServerCapability.getSource(), entity.getProperties().getPropertyValue(SOURCE).valueAsString());

    }

    @ParameterizedTest
    @MethodSource("org.odpi.openmetadata.accessservices.dataengine.PlatformConnectionProvider#getConnectionDetails")
    public void verifyLineageMappingsForAJobProcess(String userId, DataEngineClient dataEngineClient, RepositoryService repositoryService)
            throws InvalidParameterException, UserNotAuthorizedException, PropertyServerException, ConnectorCheckedException,
            org.odpi.openmetadata.repositoryservices.ffdc.exception.UserNotAuthorizedException,
            FunctionNotSupportedException, org.odpi.openmetadata.repositoryservices.ffdc.exception.InvalidParameterException,
            RepositoryErrorException, PropertyErrorException, TypeErrorException, PagingErrorException, EntityNotKnownException {

        processSetupService.createExternalDataEngine(userId, dataEngineClient);
        List<Process> processes = processSetupService.createJobProcessWithContent(userId, dataEngineClient);
        for(Process process : processes){
            validate(process, repositoryService);
        }

        Map<String, List<String>> columnLineages = processSetupService.getJobProcessLineageMappingsProxiesByCsvColumn();

        for (String columnName : columnLineages.keySet()) {
            List<String> attributes = columnLineages.get(columnName);
            String previousAttribute = null;
            for (int i = 0; i < attributes.size() - 1; i++) {
                String currentAttribute = attributes.get(i);
                EntityDetail entityDetail = repositoryService.findEntityByQualifiedName(currentAttribute);
                validateCurrentAttribute(currentAttribute, entityDetail);

                List<Relationship> relationships = repositoryService.findLineageMappingRelationshipsByGUID(entityDetail.getGUID());
                List<String> lineageMappingOtherProxyQualifiedName =
                        repositoryService.getLineageMappingsProxiesQualifiedNames(relationships, currentAttribute);
                List<String> expectedLineageMappings = new ArrayList<>();
                if (previousAttribute != null) {
                    expectedLineageMappings.add(previousAttribute);
                }
                expectedLineageMappings.add(attributes.get(i + 1));
                Collections.sort(expectedLineageMappings);
                Collections.sort(lineageMappingOtherProxyQualifiedName);
                assertEquals(expectedLineageMappings, lineageMappingOtherProxyQualifiedName);
                previousAttribute = currentAttribute;
            }
        }
    }

    private void validate(Process process, RepositoryService repositoryService)
            throws org.odpi.openmetadata.repositoryservices.ffdc.exception.UserNotAuthorizedException,
            FunctionNotSupportedException, org.odpi.openmetadata.repositoryservices.ffdc.exception.InvalidParameterException,
            RepositoryErrorException, PropertyErrorException, TypeErrorException, PagingErrorException, EntityNotKnownException {

        List<EntityDetail> processes = repositoryService.findEntityByPropertyValue(PROCESS_TYPE_GUID, process.getQualifiedName());
        assertNotNull(processes);
        assertEquals(1, processes.size());

        EntityDetail processAsEntityDetail = processes.get(0);
        assertEquals(process.getQualifiedName(), processAsEntityDetail.getProperties().getPropertyValue(QUALIFIED_NAME).valueAsString());
        assertEquals(process.getDisplayName(), processAsEntityDetail.getProperties().getPropertyValue(DISPLAY_NAME).valueAsString());

        if(process.getPortAliases() != null && !process.getPortAliases().isEmpty()){
            for(PortAlias portAlias : process.getPortAliases()){
                validatePortFields(PORT_ALIAS_TYPE_GUID, portAlias, repositoryService);
            }
        }

        if(process.getPortImplementations() != null && !process.getPortImplementations().isEmpty()){
            for(PortImplementation portImplementation : process.getPortImplementations()){
                validatePortFields(PORT_IMPLEMENTATION_TYPE_GUID, portImplementation, repositoryService);
            }
        }
        validateProcessStructure(process, processAsEntityDetail, repositoryService);
    }

    private void validateProcessStructure(Process process, EntityDetail processAsEntityDetail, RepositoryService repositoryService)
            throws org.odpi.openmetadata.repositoryservices.ffdc.exception.UserNotAuthorizedException,
            FunctionNotSupportedException, org.odpi.openmetadata.repositoryservices.ffdc.exception.InvalidParameterException,
            RepositoryErrorException, PropertyErrorException, TypeErrorException, PagingErrorException, EntityNotKnownException{

        if((process.getPortAliases() == null || process.getPortAliases().isEmpty()) &&
                (process.getPortImplementations() == null || process.getPortImplementations().isEmpty())){
            fail("Inconsistent process. No PortAlias(es) nor PortImplementation(s) defined");
        }
        boolean stageProcess = process.getPortImplementations() != null && !process.getPortImplementations().isEmpty();

        // case of stage Process, neighbours are already PortImplementations
        List<EntityDetail> neighboursViaProcessPortRelationships =
                repositoryService.getRelatedEntities(processAsEntityDetail.getGUID(), PROCESS_PORT_RELATIONSHIP_GUID);
        assertNotNull(neighboursViaProcessPortRelationships);
        List<EntityDetail> portImplementations = new ArrayList<>(neighboursViaProcessPortRelationships);

        if(!stageProcess){
            // case of main Process, neighbours are PortAlias; we need to traverse one more relationship of type PortDelegation
            // to reach PortImplementations
            portImplementations.clear();
            for(EntityDetail entityDetail : neighboursViaProcessPortRelationships){
                List<EntityDetail> neighboursViaPortDelegationRelationships =
                        repositoryService.getRelatedEntities(entityDetail.getGUID(), PORT_DELEGATION_RELATIONSHIP_GUID);
                assertNotNull(neighboursViaPortDelegationRelationships);
                assertEquals(1, neighboursViaPortDelegationRelationships.size());

                portImplementations.addAll(neighboursViaPortDelegationRelationships);
            }
        }

        assertNotNull(portImplementations);
        assertEquals(2, portImplementations.size());
        for(EntityDetail portImplementation : portImplementations){
            List<EntityDetail> tabularSchemaTypes =
                    repositoryService.getRelatedEntities(portImplementation.getGUID(), PORT_SCHEMA_RELATIONSHIP_GUID);
            assertNotNull(tabularSchemaTypes);
            assertEquals(1, tabularSchemaTypes.size());

            List<EntityDetail> columns =
                    repositoryService.getRelatedEntities(tabularSchemaTypes.get(0).getGUID(), ATTRIBUTE_FOR_SCHEMA_RELATIONSHIP_GUID);
            assertNotNull(columns);
            assertEquals(4, columns.size());
        }
    }

    private void validatePortFields(String portTypeGuid, Port port, RepositoryService repositoryService)
            throws org.odpi.openmetadata.repositoryservices.ffdc.exception.UserNotAuthorizedException,
            FunctionNotSupportedException, org.odpi.openmetadata.repositoryservices.ffdc.exception.InvalidParameterException,
            RepositoryErrorException, PropertyErrorException, TypeErrorException, PagingErrorException, EntityNotKnownException {

        List<EntityDetail> ports = repositoryService.findEntityByPropertyValue(portTypeGuid, port.getQualifiedName());
        assertNotNull(ports);
        assertEquals(1, ports.size());

        EntityDetail portAsEntityDetail = ports.get(0);
        assertEquals(port.getQualifiedName(), portAsEntityDetail.getProperties().getPropertyValue(QUALIFIED_NAME).valueAsString());
        assertEquals(port.getDisplayName(), portAsEntityDetail.getProperties().getPropertyValue(DISPLAY_NAME).valueAsString());
        assertEquals(port.getPortType().getName(), portAsEntityDetail.getProperties().getPropertyValue(PORT_TYPE).valueAsString());

        // assert Schema for PortImplementation only
        if(PORT_IMPLEMENTATION_TYPE_GUID.equals(portTypeGuid)) {
            List<EntityDetail> schemas = repositoryService.getRelatedEntities(portAsEntityDetail.getGUID(), PORT_SCHEMA_RELATIONSHIP_GUID);
            assertNotNull(schemas);
            assertEquals(1, schemas.size());

            EntityDetail schemaAsEntityDetail = schemas.get(0);
            SchemaType schemaType = ((PortImplementation) port).getSchemaType();
            assertEquals(schemaType.getQualifiedName(), schemaAsEntityDetail.getProperties().getPropertyValue(QUALIFIED_NAME).valueAsString());
            assertEquals(schemaType.getDisplayName(), schemaAsEntityDetail.getProperties().getPropertyValue(DISPLAY_NAME).valueAsString());
        }
    }

    private void validateCurrentAttribute(String currentAttribute, EntityDetail entityDetail) {
        assertEquals(currentAttribute, entityDetail.getProperties().getPropertyValue(QUALIFIED_NAME).valueAsString());
        assertEquals(currentAttribute + "_displayName", entityDetail.getProperties().getPropertyValue(DISPLAY_NAME).valueAsString());
        assertEquals(DATA_ENGINE, entityDetail.getMetadataCollectionName());
        assertEquals(ADMIN, entityDetail.getCreatedBy());
    }

    @ParameterizedTest
    @MethodSource("org.odpi.openmetadata.accessservices.dataengine.PlatformConnectionProvider#getConnectionDetails")
    public void verifyProcessAndUpdate(String userId, DataEngineClient dataEngineClient, RepositoryService repositoryService)
            throws UserNotAuthorizedException, ConnectorCheckedException, PropertyServerException, InvalidParameterException,
            org.odpi.openmetadata.repositoryservices.ffdc.exception.UserNotAuthorizedException, FunctionNotSupportedException,
            org.odpi.openmetadata.repositoryservices.ffdc.exception.InvalidParameterException, RepositoryErrorException,
            PropertyErrorException, TypeErrorException, PagingErrorException {

        Process process = processSetupService.createOrUpdateSimpleProcess(userId, dataEngineClient, null);

        List<EntityDetail> processes = repositoryService.findEntityByPropertyValue(PROCESS_TYPE_GUID, process.getQualifiedName());
        assertProcess(process, processes);

        //update process
        String newSuffix = "-new";
        process.setDisplayName(process.getDisplayName() + newSuffix);
        process.setName(process.getName() + newSuffix);
        process.setDescription(process.getDescription() + newSuffix);

        Process updatedProcess = processSetupService.createOrUpdateSimpleProcess(userId, dataEngineClient, process);

        List<EntityDetail> updatedProcesses =
                repositoryService.findEntityByPropertyValue(PROCESS_TYPE_GUID, updatedProcess.getQualifiedName());
        assertProcess(updatedProcess, updatedProcesses);
    }

    @ParameterizedTest
    @MethodSource("org.odpi.openmetadata.accessservices.dataengine.PlatformConnectionProvider#getConnectionDetails")
    public void verifyHighLevelLineage(String userId, DataEngineClient dataEngineClient, RepositoryService repositoryService)
            throws UserNotAuthorizedException, ConnectorCheckedException, PropertyServerException, InvalidParameterException,
            org.odpi.openmetadata.repositoryservices.ffdc.exception.UserNotAuthorizedException, FunctionNotSupportedException,
            org.odpi.openmetadata.repositoryservices.ffdc.exception.InvalidParameterException, RepositoryErrorException,
            PropertyErrorException, TypeErrorException, PagingErrorException, EntityNotKnownException {

        processSetupService.createExternalDataEngine(userId, dataEngineClient);
        Database database = dataStoreAndRelationalTableSetupService.upsertDatabase(userId, dataEngineClient);
        List<EntityDetail> databases = repositoryService.findEntityByPropertyValue(DATABASE_TYPE_GUID, database.getQualifiedName());
        assertDatabase(database, databases);

        Process process = processSetupService.createOrUpdateSimpleProcess(userId, dataEngineClient, null);
        DataFile dataFile = dataStoreAndRelationalTableSetupService.upsertDataFile(userId, dataEngineClient);
        RelationalTable relationalTable = dataStoreAndRelationalTableSetupService.upsertRelationalTable(userId, dataEngineClient);

        List<LineageMapping> lineageMappings = new ArrayList<>();
        lineageMappings.add(processSetupService.createLineageMapping(dataFile.getQualifiedName(), process.getQualifiedName()));
        lineageMappings.add(processSetupService.createLineageMapping(process.getQualifiedName(), relationalTable.getQualifiedName()));
        dataEngineClient.addLineageMappings(userId, lineageMappings);

        List<EntityDetail> targetDataFiles = repositoryService.findEntityByPropertyValue(DATAFILE_TYPE_GUID, dataFile.getQualifiedName());
        assertDataFile(dataFile, targetDataFiles);

        List<EntityDetail> targetProcesses = repositoryService.getRelatedEntities(targetDataFiles.get(0).getGUID(), LINEAGE_MAPPING_RELATIONSHIP_GUID);
        assertProcess(process, targetProcesses);

        List<EntityDetail> targetRelationalTable = repositoryService.findEntityByPropertyValue(RELATIONAL_TABLE_TYPE_GUID, relationalTable.getQualifiedName());
        assertRelationalTable(relationalTable, targetRelationalTable);

        targetProcesses = repositoryService.getRelatedEntities(targetRelationalTable.get(0).getGUID(), LINEAGE_MAPPING_RELATIONSHIP_GUID);
        assertProcess(process, targetProcesses);
    }

    @ParameterizedTest
    @MethodSource("org.odpi.openmetadata.accessservices.dataengine.PlatformConnectionProvider#getConnectionDetails")
    public void upsertDatabase(String userId, DataEngineClient dataEngineClient, RepositoryService repositoryService)
            throws UserNotAuthorizedException, ConnectorCheckedException, PropertyServerException, InvalidParameterException,
            org.odpi.openmetadata.repositoryservices.ffdc.exception.UserNotAuthorizedException, FunctionNotSupportedException,
            org.odpi.openmetadata.repositoryservices.ffdc.exception.InvalidParameterException, RepositoryErrorException,
            PropertyErrorException, TypeErrorException, PagingErrorException, EntityNotKnownException {

        processSetupService.createExternalDataEngine(userId, dataEngineClient);
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

        processSetupService.createExternalDataEngine(userId, dataEngineClient);
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

        processSetupService.createExternalDataEngine(userId, dataEngineClient);
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

    private void assertProcess(Process process, List<EntityDetail> processes) {
        assertNotNull(processes);
        assertEquals(1, processes.size());

        EntityDetail processAsEntityDetail = processes.get(0);
        assertEquals(process.getQualifiedName(), processAsEntityDetail.getProperties().getPropertyValue(QUALIFIED_NAME).valueAsString());
        assertEquals(process.getDisplayName(), processAsEntityDetail.getProperties().getPropertyValue(DISPLAY_NAME).valueAsString());
        assertEquals(process.getName(), processAsEntityDetail.getProperties().getPropertyValue(NAME).valueAsString());
        assertEquals(process.getDescription(), processAsEntityDetail.getProperties().getPropertyValue(DESCRIPTION).valueAsString());
        assertEquals(process.getOwner(), getOwnership(processAsEntityDetail));
    }

    private EntityDetail assertDatabase(Database database, List<EntityDetail> databases) {
        assertNotNull(databases);
        assertEquals(1, databases.size());

        EntityDetail databaseAsEntityDetail = databases.get(0);
        assertEquals(database.getDisplayName(), databaseAsEntityDetail.getProperties().getPropertyValue(NAME).valueAsString());
        assertEquals(database.getDescription(), databaseAsEntityDetail.getProperties().getPropertyValue(DESCRIPTION).valueAsString());
        assertEquals(database.getDatabaseType(), databaseAsEntityDetail.getProperties().getPropertyValue(DEPLOYED_IMPLEMENTATION_TYPE).valueAsString());
        assertEquals(database.getDatabaseVersion(), databaseAsEntityDetail.getProperties().getPropertyValue(DATABASE_VERSION).valueAsString());
        assertEquals(database.getDatabaseInstance(), databaseAsEntityDetail.getProperties().getPropertyValue(INSTANCE).valueAsString());
        assertEquals(database.getDatabaseImportedFrom(), databaseAsEntityDetail.getProperties().getPropertyValue(IMPORTED_FROM).valueAsString());
        return databaseAsEntityDetail;
    }

    private EntityDetail assertRelationalTable(RelationalTable relationalTable, List<EntityDetail> relationalTables) {
        assertNotNull(relationalTables);
        assertEquals(1, relationalTables.size());

        EntityDetail relationalTableAsEntityDetail = relationalTables.get(0);
        assertEquals(relationalTable.getDisplayName(),
                relationalTableAsEntityDetail.getProperties().getPropertyValue(DISPLAY_NAME).valueAsString());
        assertEquals(relationalTable.getDescription(),
                relationalTableAsEntityDetail.getProperties().getPropertyValue(DESCRIPTION).valueAsString());
        return relationalTableAsEntityDetail;
    }

    private EntityDetail assertDataFile(DataFile dataFile, List<EntityDetail> dataFiles) {
        assertNotNull(dataFiles);
        assertEquals(1, dataFiles.size());

        EntityDetail dataFileAsEntityDetail = dataFiles.get(0);
        assertEquals(dataFile.getDisplayName(), dataFileAsEntityDetail.getProperties().getPropertyValue(NAME).valueAsString());
        assertEquals(dataFile.getQualifiedName(), dataFileAsEntityDetail.getProperties().getPropertyValue(QUALIFIED_NAME).valueAsString());
        assertEquals(dataFile.getDescription(), dataFileAsEntityDetail.getProperties().getPropertyValue(DESCRIPTION).valueAsString());
        assertEquals(dataFile.getFileType(), dataFileAsEntityDetail.getProperties().getPropertyValue(FILE_TYPE).valueAsString());
        return dataFileAsEntityDetail;
    }

    private String getOwnership(EntityDetail entityDetail){
        return entityDetail.getClassifications().stream()
                .filter(c -> c.getType().getTypeDefGUID().equals(OWNERSHIP_CLASSIFICATION_GUID))
                .map(c -> c.getProperties().getPropertyValue(OWNER).valueAsString())
                .findFirst().orElse(null);
    }

}
