/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.dataengine.fvt;

import org.odpi.openmetadata.accessservices.dataengine.ConnectionAndEndpointSetupService;
import org.odpi.openmetadata.accessservices.dataengine.DataStoreAndRelationalTableSetupService;
import org.odpi.openmetadata.accessservices.dataengine.FindSetupService;
import org.odpi.openmetadata.accessservices.dataengine.LineageSetupService;
import org.odpi.openmetadata.accessservices.dataengine.PortSetupService;
import org.odpi.openmetadata.accessservices.dataengine.ProcessSetupService;
import org.odpi.openmetadata.accessservices.dataengine.EngineSetupService;
import org.odpi.openmetadata.accessservices.dataengine.TopicAndEventTypeSetupService;
import org.odpi.openmetadata.accessservices.dataengine.model.Attribute;
import org.odpi.openmetadata.accessservices.dataengine.model.DataFile;
import org.odpi.openmetadata.accessservices.dataengine.model.Database;
import org.odpi.openmetadata.accessservices.dataengine.model.DatabaseSchema;
import org.odpi.openmetadata.accessservices.dataengine.model.EventType;
import org.odpi.openmetadata.accessservices.dataengine.model.Port;
import org.odpi.openmetadata.accessservices.dataengine.model.Process;
import org.odpi.openmetadata.accessservices.dataengine.model.RelationalTable;
import org.odpi.openmetadata.accessservices.dataengine.model.Topic;
import org.odpi.openmetadata.http.HttpHelper;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * Base class for FVTs in data-engine-fvt module
 */
public class DataEngineFVT {

    protected static final String SOFTWARE_SERVER_CAPABILITY_TYPE_GUID = "fe30a033-8f86-4d17-8986-e6166fa24177";
    protected static final String DATABASE_TYPE_GUID = "0921c83f-b2db-4086-a52c-0d10e52ca078";
    protected static final String DEPLOYED_DATABASE_SCHEMA_TYPE_GUID = "eab811ec-556a-45f1-9091-bc7ac8face0f";
    protected static final String DATAFILE_TYPE_GUID = "10752b4a-4b5d-4519-9eae-fdd6d162122f";
    protected static final String RELATIONAL_TABLE_TYPE_GUID = "ce7e72b8-396a-4013-8688-f9d973067425";
    protected static final String PROCESS_TYPE_GUID = "d8f33bd7-afa9-4a11-a8c7-07dcec83c050";
    protected static final String PORT_IMPLEMENTATION_TYPE_GUID = "ADbbdF06-a6A3-4D5F-7fA3-DB4Cb0eDeC0E";
    protected static final String PORT_ALIAS_TYPE_GUID = "DFa5aEb1-bAb4-c25B-bDBD-B95Ce6fAB7F5";
    protected static final String OWNERSHIP_CLASSIFICATION_GUID = "8139a911-a4bd-432b-a9f4-f6d11c511abe";
    protected static final String PROCESS_PORT_RELATIONSHIP_GUID = "fB4E00CF-37e4-88CE-4a94-233BAdB84DA2";
    protected static final String PORT_DELEGATION_RELATIONSHIP_GUID = "98bB8BA1-dc6A-eb9D-32Cf-F837bEbCbb8E";
    protected static final String PORT_SCHEMA_RELATIONSHIP_GUID = "B216fA00-8281-F9CC-9911-Ae6377f2b457";
    protected static final String ATTRIBUTE_FOR_SCHEMA_RELATIONSHIP_GUID = "86b176a2-015c-44a6-8106-54d5d69ba661";
    protected static final String DATA_FLOW_RELATIONSHIP_GUID = "d2490c0c-06cc-458a-add2-33cf2f5dd724";
    protected static final String ASSET_SCHEMA_TYPE_RELATIONSHIP_GUID = "815b004d-73c6-4728-9dd9-536f4fe803cd";
    protected static final String DATA_CONTENT_FOR_DATA_SET_RELATIONSHIP_GUID = "b827683c-2924-4df3-a92d-7be1888e23c0";
    protected static final String NESTED_SCHEMA_ATTRIBUTE_RELATIONSHIP_GUID = "0ffb9d87-7074-45da-a9b0-ae0859611133";
    protected static final String CONNECTION_TYPE_GUID = "114e9f8f-5ff3-4c32-bd37-a7eb42712253";
    protected static final String ENDPOINT_TYPE_GUID = "dbc20663-d705-4ff0-8424-80c262c6b8e7";
    protected static final String FILE_FOLDER_TYPE_GUID = "229ed5cc-de31-45fc-beb4-9919fd247398";
    protected static final String TABULAR_SCHEMA_TYPE_TYPE_GUID = "248975ec-8019-4b8a-9caf-084c8b724233";
    protected static final String TOPIC_TYPE_GUID = "29100f49-338e-4361-b05d-7e4e8e818325";
    protected static final String EVENT_TYPE_TYPE_GUID = "8bc88aba-d7e4-4334-957f-cfe8e8eadc32";
    protected static final String EVENT_SCHEMA_ATTRIBUTE_TYPE_GUID = "5be4ee8f-4d0c-45cd-a411-22a468950342";
    protected static final String TABULAR_COLUMN_TYPE_GUID = "d81a0425-4e9b-4f31-bc1c-e18c3566da10";

    protected static final String DESCRIPTION = "description";
    protected static final String NAME = "name";
    protected static final String PATCH_LEVEL = "patchLevel";
    protected static final String QUALIFIED_NAME = "qualifiedName";
    protected static final String DISPLAY_NAME = "displayName";
    protected static final String SOURCE = "source";
    protected static final String FILE_TYPE = "fileType";
    protected static final String CAPABILITY_VERSION = "capabilityVersion";
    protected static final String CAPABILITY_TYPE = "capabilityType";
    protected static final String DEPLOYED_IMPLEMENTATION_TYPE = "deployedImplementationType";
    protected static final String DATABASE_VERSION = "databaseVersion";
    protected static final String INSTANCE = "instance";
    protected static final String IMPORTED_FROM = "importedFrom";
    protected static final String OWNER = "owner";
    protected static final String PORT_TYPE = "portType";
    protected static final String TOPIC_TYPE = "topicType";

    protected static final String DATA_ENGINE = "DataEngine";
    protected static final String ADMIN = "admin";

    public DataEngineFVT() {
        HttpHelper.noStrictSSL();
    }

    protected final LineageSetupService lineageSetupService = new LineageSetupService();
    protected final EngineSetupService engineSetupService = new EngineSetupService();
    protected final DataStoreAndRelationalTableSetupService dataStoreAndRelationalTableSetupService = new DataStoreAndRelationalTableSetupService();
    protected final ProcessSetupService processSetupService = new ProcessSetupService();
    protected final PortSetupService portSetupService = new PortSetupService();
    protected final ConnectionAndEndpointSetupService connectionAndEndpointSetupService = new ConnectionAndEndpointSetupService();
    protected final FindSetupService findSetupService = new FindSetupService();
    protected final TopicAndEventTypeSetupService topicAndEventTypeSetupService = new TopicAndEventTypeSetupService();

    protected EntityDetail assertProcess(Process process, List<EntityDetail> processes) {
        assertNotNull(processes);
        assertEquals(1, processes.size());

        EntityDetail processAsEntityDetail = processes.get(0);
        assertEquals(process.getQualifiedName(), processAsEntityDetail.getProperties().getPropertyValue(QUALIFIED_NAME).valueAsString());
        assertEquals(process.getDisplayName(), processAsEntityDetail.getProperties().getPropertyValue(DISPLAY_NAME).valueAsString());
        assertEquals(process.getName(), processAsEntityDetail.getProperties().getPropertyValue(NAME).valueAsString());
        assertEquals(process.getDescription(), processAsEntityDetail.getProperties().getPropertyValue(DESCRIPTION).valueAsString());
        assertEquals(process.getOwner(), getOwnership(processAsEntityDetail));

        return processAsEntityDetail;
    }

    private String getOwnership(EntityDetail entityDetail){
        return entityDetail.getClassifications().stream()
                .filter(c -> c.getType().getTypeDefGUID().equals(OWNERSHIP_CLASSIFICATION_GUID))
                .map(c -> c.getProperties().getPropertyValue(OWNER).valueAsString())
                .findFirst().orElse(null);
    }


    protected EntityDetail assertDatabase(Database database, List<EntityDetail> databases) {
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

    protected EntityDetail assertDatabaseSchema(DatabaseSchema databaseSchema, List<EntityDetail> databaseSchemas) {
        assertNotNull(databaseSchemas);
        assertEquals(1, databaseSchemas.size());

        EntityDetail databaseAsEntityDetail = databaseSchemas.get(0);
        assertEquals(databaseSchema.getDisplayName(), databaseAsEntityDetail.getProperties().getPropertyValue(NAME).valueAsString());
        assertEquals(databaseSchema.getDescription(), databaseAsEntityDetail.getProperties().getPropertyValue(DESCRIPTION).valueAsString());
        return databaseAsEntityDetail;
    }

    protected EntityDetail assertRelationalTable(RelationalTable relationalTable, List<EntityDetail> relationalTables) {
        assertNotNull(relationalTables);
        assertEquals(1, relationalTables.size());

        EntityDetail relationalTableAsEntityDetail = relationalTables.get(0);
        assertEquals(relationalTable.getDisplayName(),
                relationalTableAsEntityDetail.getProperties().getPropertyValue(DISPLAY_NAME).valueAsString());
        assertEquals(relationalTable.getDescription(),
                relationalTableAsEntityDetail.getProperties().getPropertyValue(DESCRIPTION).valueAsString());
        return relationalTableAsEntityDetail;
    }

    protected EntityDetail assertDataFile(DataFile dataFile, List<EntityDetail> dataFiles) {
        assertNotNull(dataFiles);
        assertEquals(1, dataFiles.size());

        EntityDetail dataFileAsEntityDetail = dataFiles.get(0);
        assertEquals(dataFile.getDisplayName(), dataFileAsEntityDetail.getProperties().getPropertyValue(NAME).valueAsString());
        assertEquals(dataFile.getQualifiedName(), dataFileAsEntityDetail.getProperties().getPropertyValue(QUALIFIED_NAME).valueAsString());
        assertEquals(dataFile.getDescription(), dataFileAsEntityDetail.getProperties().getPropertyValue(DESCRIPTION).valueAsString());
        assertEquals(dataFile.getFileType(), dataFileAsEntityDetail.getProperties().getPropertyValue(FILE_TYPE).valueAsString());
        return dataFileAsEntityDetail;
    }

    protected EntityDetail assertPort(Port port, List<EntityDetail> ports) {
        assertNotNull(ports);
        assertEquals(1, ports.size());

        EntityDetail portImplementationAsEntityDetail = ports.get(0);
        assertEquals(port.getQualifiedName(),
                portImplementationAsEntityDetail.getProperties().getPropertyValue(QUALIFIED_NAME).valueAsString());
        assertEquals(port.getDisplayName(),
                portImplementationAsEntityDetail.getProperties().getPropertyValue(DISPLAY_NAME).valueAsString());
        assertEquals(port.getPortType().getName(),
                portImplementationAsEntityDetail.getProperties().getPropertyValue(PORT_TYPE).valueAsString());
        return portImplementationAsEntityDetail;
    }

    protected EntityDetail assertTopic(Topic topic, List<EntityDetail> topics) {
        assertNotNull(topics);
        assertEquals(1, topics.size());

        EntityDetail topicAsEntityDetail = topics.get(0);
        assertEquals(topic.getDisplayName(), topicAsEntityDetail.getProperties().getPropertyValue(NAME).valueAsString());
        assertEquals(topic.getDescription(), topicAsEntityDetail.getProperties().getPropertyValue(DESCRIPTION).valueAsString());
        assertEquals(topic.getTopicType(), topicAsEntityDetail.getProperties().getPropertyValue(TOPIC_TYPE).valueAsString());

        return topicAsEntityDetail;
    }

    protected void assertEventType(EventType eventType, EntityDetail eventTypeAsEntityDetail) {
        assertNotNull(eventTypeAsEntityDetail);

        assertEquals(eventType.getDisplayName(), eventTypeAsEntityDetail.getProperties().getPropertyValue(DISPLAY_NAME).valueAsString());
        assertEquals(eventType.getDescription(), eventTypeAsEntityDetail.getProperties().getPropertyValue(DESCRIPTION).valueAsString());
    }

    protected void assertEventSchemaAttribute(Attribute attribute, EntityDetail attributeAsEntityDetail) {
        assertNotNull(attributeAsEntityDetail);

        assertEquals(attribute.getDisplayName(), attributeAsEntityDetail.getProperties().getPropertyValue(DISPLAY_NAME).valueAsString());
    }
}
