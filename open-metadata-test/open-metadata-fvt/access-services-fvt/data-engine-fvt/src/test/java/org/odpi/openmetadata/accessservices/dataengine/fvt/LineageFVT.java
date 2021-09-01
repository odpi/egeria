/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.dataengine.fvt;


import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
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
import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectorCheckedException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
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
public class LineageFVT extends DataEngineFVT{

    @ParameterizedTest
    @MethodSource("org.odpi.openmetadata.accessservices.dataengine.PlatformConnectionProvider#getConnectionDetails")
    public void verifyLineageMappingsForAJobProcess(String userId, DataEngineClient dataEngineClient, RepositoryService repositoryService)
            throws InvalidParameterException, UserNotAuthorizedException, PropertyServerException, ConnectorCheckedException,
            org.odpi.openmetadata.repositoryservices.ffdc.exception.UserNotAuthorizedException,
            FunctionNotSupportedException, org.odpi.openmetadata.repositoryservices.ffdc.exception.InvalidParameterException,
            RepositoryErrorException, PropertyErrorException, TypeErrorException, PagingErrorException, EntityNotKnownException {

        softwareServerCapabilitySetupServer.createExternalDataEngine(userId, dataEngineClient);
        List<Process> processes = lineageSetupService.createJobProcessWithContent(userId, dataEngineClient);
        for(Process process : processes){
            validate(process, repositoryService);
        }

        Map<String, List<String>> columnLineages = lineageSetupService.getJobProcessLineageMappingsProxiesByCsvColumn();

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
    public void verifyHighLevelLineage(String userId, DataEngineClient dataEngineClient, RepositoryService repositoryService)
            throws UserNotAuthorizedException, ConnectorCheckedException, PropertyServerException, InvalidParameterException,
            org.odpi.openmetadata.repositoryservices.ffdc.exception.UserNotAuthorizedException, FunctionNotSupportedException,
            org.odpi.openmetadata.repositoryservices.ffdc.exception.InvalidParameterException, RepositoryErrorException,
            PropertyErrorException, TypeErrorException, PagingErrorException, EntityNotKnownException {

        softwareServerCapabilitySetupServer.createExternalDataEngine(userId, dataEngineClient);
        Database database = dataStoreAndRelationalTableSetupService.upsertDatabase(userId, dataEngineClient);
        List<EntityDetail> databases = repositoryService.findEntityByPropertyValue(DATABASE_TYPE_GUID, database.getQualifiedName());
        assertDatabase(database, databases);

        Process process = processSetupService.createOrUpdateSimpleProcess(userId, dataEngineClient, null);
        DataFile dataFile = dataStoreAndRelationalTableSetupService.upsertDataFile(userId, dataEngineClient);
        RelationalTable relationalTable = dataStoreAndRelationalTableSetupService.upsertRelationalTable(userId, dataEngineClient);

        List<LineageMapping> lineageMappings = new ArrayList<>();
        lineageMappings.add(lineageSetupService.createLineageMapping(dataFile.getQualifiedName(), process.getQualifiedName()));
        lineageMappings.add(lineageSetupService.createLineageMapping(process.getQualifiedName(), relationalTable.getQualifiedName()));
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

}
