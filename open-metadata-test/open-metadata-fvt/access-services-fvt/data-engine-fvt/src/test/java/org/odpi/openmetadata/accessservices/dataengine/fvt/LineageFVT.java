/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.dataengine.fvt;


import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.odpi.openmetadata.accessservices.dataengine.RepositoryService;
import org.odpi.openmetadata.accessservices.dataengine.client.DataEngineClient;
import org.odpi.openmetadata.accessservices.dataengine.model.DataFile;
import org.odpi.openmetadata.accessservices.dataengine.model.DataFlow;
import org.odpi.openmetadata.accessservices.dataengine.model.Database;
import org.odpi.openmetadata.accessservices.dataengine.model.DatabaseSchema;
import org.odpi.openmetadata.accessservices.dataengine.model.Port;
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
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * Holds FVTs related to feature lineage
 */
public class LineageFVT extends DataEngineFVT {

    @ParameterizedTest
    @MethodSource("org.odpi.openmetadata.accessservices.dataengine.PlatformConnectionProvider#getConnectionDetails")
    public void verifyDataFlowsForAJobProcess(String userId, DataEngineClient dataEngineClient, RepositoryService repositoryService)
            throws InvalidParameterException, UserNotAuthorizedException, PropertyServerException, ConnectorCheckedException,
                   org.odpi.openmetadata.repositoryservices.ffdc.exception.UserNotAuthorizedException,
                   FunctionNotSupportedException, org.odpi.openmetadata.repositoryservices.ffdc.exception.InvalidParameterException,
                   RepositoryErrorException, PropertyErrorException, TypeErrorException, PagingErrorException, EntityNotKnownException {

        engineSetupService.createExternalDataEngine(userId, dataEngineClient, null);
        List<Process> processes = lineageSetupService.createJobProcessWithContent(userId, dataEngineClient);

        for (Process process : processes) {
            validate(process, repositoryService);
        }

        Map<String, List<String>> columnLineages = lineageSetupService.getJobProcessDataFlowsProxiesByCsvColumn();

        for (String columnName : columnLineages.keySet()) {
            List<String> attributes = columnLineages.get(columnName);
            String previousAttribute = null;
            for (int i = 0; i < attributes.size() - 1; i++) {
                String currentAttribute = attributes.get(i);
                EntityDetail entityDetail = repositoryService.findEntityByQualifiedName(currentAttribute, TABULAR_COLUMN_TYPE_GUID);
                validateCurrentAttribute(currentAttribute, entityDetail);

                List<Relationship> relationships = repositoryService.findDataFlowRelationshipsByGUID(entityDetail.getGUID());
                List<String> dataFlowOtherProxyQualifiedName =
                        repositoryService.getDataFlowsProxiesQualifiedNames(relationships, currentAttribute);
                List<String> expectedDataFlows = new ArrayList<>();
                if (previousAttribute != null) {
                    expectedDataFlows.add(previousAttribute);
                }
                expectedDataFlows.add(attributes.get(i + 1));
                Collections.sort(expectedDataFlows);
                Collections.sort(dataFlowOtherProxyQualifiedName);
                assertEquals(expectedDataFlows, dataFlowOtherProxyQualifiedName);
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

        if (process.getPortImplementations() != null && !process.getPortImplementations().isEmpty()) {
            for (PortImplementation portImplementation : process.getPortImplementations()) {
                validatePortFields(portImplementation, repositoryService);
            }
        }

        validateProcessStructure(process, processAsEntityDetail, repositoryService);
    }

    private void validateProcessStructure(Process process, EntityDetail processAsEntityDetail, RepositoryService repositoryService)
            throws org.odpi.openmetadata.repositoryservices.ffdc.exception.UserNotAuthorizedException,
                   FunctionNotSupportedException, org.odpi.openmetadata.repositoryservices.ffdc.exception.InvalidParameterException,
                   RepositoryErrorException, PropertyErrorException, TypeErrorException, PagingErrorException, EntityNotKnownException {
        boolean stageProcess = process.getPortImplementations() != null && !process.getPortImplementations().isEmpty();

        List<EntityDetail> neighboursViaProcessPortRelationships;
        Set<EntityDetail> portImplementations = new HashSet<>();
        if (stageProcess) {
            // case of stage Process, neighbours are already PortImplementations
            neighboursViaProcessPortRelationships =
                    repositoryService.getRelatedEntities(processAsEntityDetail.getGUID(), PROCESS_PORT_RELATIONSHIP_GUID);
            portImplementations = new HashSet<>(neighboursViaProcessPortRelationships);
            assertNotNull(portImplementations);
            assertEquals(2, portImplementations.size());
        } else {
            // case of main Process, neighbours are stage processes; we need to traverse one more relationship of type ProcessHierarchy
            // to reach PortImplementations
            List<EntityDetail> neighboursViaProcessHierarchiesRelationships =
                    repositoryService.getRelatedEntities(processAsEntityDetail.getGUID(), PROCESS_HIERARCHY_RELATIONSHIP_GUID);
            for (EntityDetail entityDetail : neighboursViaProcessHierarchiesRelationships) {
                neighboursViaProcessPortRelationships =
                        repositoryService.getRelatedEntities(entityDetail.getGUID(), PROCESS_PORT_RELATIONSHIP_GUID);
                assertNotNull(neighboursViaProcessPortRelationships);
                assertEquals(2, neighboursViaProcessPortRelationships.size());

                portImplementations.addAll(neighboursViaProcessPortRelationships);
            }
            assertNotNull(portImplementations);
            assertEquals(6, portImplementations.size());
        }


        for (EntityDetail portImplementation : portImplementations) {
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

    private void validatePortFields(Port port, RepositoryService repositoryService)
            throws org.odpi.openmetadata.repositoryservices.ffdc.exception.UserNotAuthorizedException,
                   FunctionNotSupportedException, org.odpi.openmetadata.repositoryservices.ffdc.exception.InvalidParameterException,
                   RepositoryErrorException, PropertyErrorException, TypeErrorException, PagingErrorException, EntityNotKnownException {

        List<EntityDetail> ports = repositoryService.findEntityByPropertyValue(PORT_IMPLEMENTATION_TYPE_GUID, port.getQualifiedName());
        assertNotNull(ports);
        assertEquals(1, ports.size());

        EntityDetail portAsEntityDetail = ports.get(0);
        assertEquals(port.getQualifiedName(), portAsEntityDetail.getProperties().getPropertyValue(QUALIFIED_NAME).valueAsString());
        assertEquals(port.getDisplayName(), portAsEntityDetail.getProperties().getPropertyValue(DISPLAY_NAME).valueAsString());
        assertEquals(port.getPortType().getName(), portAsEntityDetail.getProperties().getPropertyValue(PORT_TYPE).valueAsString());

        List<EntityDetail> schemas = repositoryService.getRelatedEntities(portAsEntityDetail.getGUID(), PORT_SCHEMA_RELATIONSHIP_GUID);
        assertNotNull(schemas);
        assertEquals(1, schemas.size());

        EntityDetail schemaAsEntityDetail = schemas.get(0);
        SchemaType schemaType = ((PortImplementation) port).getSchemaType();
        assertEquals(schemaType.getQualifiedName(), schemaAsEntityDetail.getProperties().getPropertyValue(QUALIFIED_NAME).valueAsString());
        assertEquals(schemaType.getDisplayName(), schemaAsEntityDetail.getProperties().getPropertyValue(DISPLAY_NAME).valueAsString());

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

        engineSetupService.createExternalDataEngine(userId, dataEngineClient, null);
        Database database = dataStoreAndRelationalTableSetupService.upsertDatabase(userId, dataEngineClient, null);
        List<EntityDetail> databases = repositoryService.findEntityByPropertyValue(DATABASE_TYPE_GUID, database.getQualifiedName());
        assertDatabase(database, databases);

        Process process = processSetupService.createOrUpdateSimpleProcess(userId, dataEngineClient, null);
        DataFile dataFile = dataStoreAndRelationalTableSetupService.upsertDataFile(userId, dataEngineClient, null);

        DatabaseSchema databaseSchema = dataStoreAndRelationalTableSetupService.upsertDatabaseSchema(userId, dataEngineClient,
                null, database.getQualifiedName(), false);
        RelationalTable relationalTable = dataStoreAndRelationalTableSetupService.upsertRelationalTable(userId, dataEngineClient,
                null, databaseSchema.getQualifiedName(), false);

        List<DataFlow> dataFlows = new ArrayList<>();
        dataFlows.add(lineageSetupService.createDataFlow(dataFile.getQualifiedName(), process.getQualifiedName()));
        dataFlows.add(lineageSetupService.createDataFlow(process.getQualifiedName(), relationalTable.getQualifiedName()));
        dataEngineClient.addDataFlows(userId, dataFlows);

        List<EntityDetail> targetDataFiles = repositoryService.findEntityByPropertyValue(DATAFILE_TYPE_GUID, dataFile.getQualifiedName());
        assertDataFile(dataFile, targetDataFiles);

        List<EntityDetail> targetProcesses = repositoryService.getRelatedEntities(targetDataFiles.get(0).getGUID(), DATA_FLOW_RELATIONSHIP_GUID);
        assertProcess(process, targetProcesses);

        List<EntityDetail> targetRelationalTable =
                repositoryService.findEntityByPropertyValue(RELATIONAL_TABLE_TYPE_GUID, relationalTable.getQualifiedName());
        assertRelationalTable(relationalTable, targetRelationalTable);

        targetProcesses = repositoryService.getRelatedEntities(targetRelationalTable.get(0).getGUID(), DATA_FLOW_RELATIONSHIP_GUID);
        assertProcess(process, targetProcesses);
    }

}
