/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.dataengine.fvt;


import org.odpi.openmetadata.accessservices.dataengine.DataStoreAndRelationalTableSetupService;
import org.odpi.openmetadata.accessservices.dataengine.LineageSetupService;
import org.odpi.openmetadata.accessservices.dataengine.ProcessSetupService;
import org.odpi.openmetadata.accessservices.dataengine.SoftwareServerCapabilitySetupService;
import org.odpi.openmetadata.accessservices.dataengine.model.DataFile;
import org.odpi.openmetadata.accessservices.dataengine.model.Database;
import org.odpi.openmetadata.accessservices.dataengine.model.Process;
import org.odpi.openmetadata.accessservices.dataengine.model.RelationalTable;
import org.odpi.openmetadata.http.HttpHelper;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * This class holds functional verification tests written with the help of the Junit framework. There are parametrized tests
 * covering the creation of an external data engine source and a whole job process containing stages.
 * Depending on the number of the series of parameters of each test method, the tests will run or not multiple times.
 * The parameters are computed in the method indicated in the @MethodSource annotation.
 */
public class DataEngineFVT {

    protected static final String SOFTWARE_SERVER_CAPABILITY_TYPE_GUID = "fe30a033-8f86-4d17-8986-e6166fa24177";
    protected static final String DATABASE_TYPE_GUID = "0921c83f-b2db-4086-a52c-0d10e52ca078";
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
    protected static final String LINEAGE_MAPPING_RELATIONSHIP_GUID = "a5991bB2-660D-A3a1-2955-fAcDA2d5F4Ff";
    protected static final String ASSET_SCHEMA_TYPE_RELATIONSHIP_GUID = "815b004d-73c6-4728-9dd9-536f4fe803cd";
    protected static final String DATA_CONTENT_FOR_DATA_SET_RELATIONSHIP_GUID = "b827683c-2924-4df3-a92d-7be1888e23c0";
    protected static final String NESTED_SCHEMA_ATTRIBUTE_RELATIONSHIP_GUID = "0ffb9d87-7074-45da-a9b0-ae0859611133";

    protected static final String DESCRIPTION = "description";
    protected static final String NAME = "name";
    protected static final String TYPE = "type";
    protected static final String VERSION = "capabilityVersion";
    protected static final String PATCH_LEVEL = "patchLevel";
    protected static final String QUALIFIED_NAME = "qualifiedName";
    protected static final String DISPLAY_NAME = "displayName";
    protected static final String SOURCE = "source";
    protected static final String FILE_TYPE = "fileType";
    protected static final String DEPLOYED_IMPLEMENTATION_TYPE = "deployedImplementationType";
    protected static final String DATABASE_VERSION = "databaseVersion";
    protected static final String INSTANCE = "instance";
    protected static final String IMPORTED_FROM = "importedFrom";
    protected static final String OWNER = "owner";
    protected static final String PORT_TYPE = "portType";

    protected static final String DATA_ENGINE = "DataEngine";
    protected static final String ADMIN = "admin";

    public DataEngineFVT() {
        HttpHelper.noStrictSSL();
    }

    protected final LineageSetupService lineageSetupService = new LineageSetupService();
    protected final SoftwareServerCapabilitySetupService softwareServerCapabilitySetupServer = new SoftwareServerCapabilitySetupService();
    protected final DataStoreAndRelationalTableSetupService dataStoreAndRelationalTableSetupService = new DataStoreAndRelationalTableSetupService();
    protected final ProcessSetupService processSetupService = new ProcessSetupService();

    protected void assertProcess(Process process, List<EntityDetail> processes) {
        assertNotNull(processes);
        assertEquals(1, processes.size());

        EntityDetail processAsEntityDetail = processes.get(0);
        assertEquals(process.getQualifiedName(), processAsEntityDetail.getProperties().getPropertyValue(QUALIFIED_NAME).valueAsString());
        assertEquals(process.getDisplayName(), processAsEntityDetail.getProperties().getPropertyValue(DISPLAY_NAME).valueAsString());
        assertEquals(process.getName(), processAsEntityDetail.getProperties().getPropertyValue(NAME).valueAsString());
        assertEquals(process.getDescription(), processAsEntityDetail.getProperties().getPropertyValue(DESCRIPTION).valueAsString());
        assertEquals(process.getOwner(), getOwnership(processAsEntityDetail));
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

}
