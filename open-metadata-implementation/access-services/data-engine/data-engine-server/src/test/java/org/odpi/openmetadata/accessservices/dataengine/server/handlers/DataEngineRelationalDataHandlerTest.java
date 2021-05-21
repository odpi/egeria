/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.dataengine.server.handlers;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.odpi.openmetadata.accessservices.dataengine.model.DataItemSortOrder;
import org.odpi.openmetadata.accessservices.dataengine.model.Database;
import org.odpi.openmetadata.accessservices.dataengine.model.DatabaseSchema;
import org.odpi.openmetadata.accessservices.dataengine.model.OwnerType;
import org.odpi.openmetadata.accessservices.dataengine.model.RelationalColumn;
import org.odpi.openmetadata.accessservices.dataengine.model.RelationalTable;
import org.odpi.openmetadata.accessservices.dataengine.model.SchemaType;
import org.odpi.openmetadata.commonservices.ffdc.InvalidParameterHandler;
import org.odpi.openmetadata.commonservices.generichandlers.RelationalDataHandler;
import org.odpi.openmetadata.commonservices.repositoryhandler.RepositoryHandler;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.TypeDef;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.odpi.openmetadata.commonservices.generichandlers.OpenMetadataAPIMapper.DATABASE_TYPE_NAME;
import static org.odpi.openmetadata.commonservices.generichandlers.OpenMetadataAPIMapper.DATA_CONTENT_FOR_DATA_SET_TYPE_GUID;
import static org.odpi.openmetadata.commonservices.generichandlers.OpenMetadataAPIMapper.DATA_CONTENT_FOR_DATA_SET_TYPE_NAME;
import static org.odpi.openmetadata.commonservices.generichandlers.OpenMetadataAPIMapper.DEPLOYED_DATABASE_SCHEMA_TYPE_NAME;
import static org.odpi.openmetadata.commonservices.generichandlers.OpenMetadataAPIMapper.DISPLAY_NAME_PROPERTY_NAME;
import static org.odpi.openmetadata.commonservices.generichandlers.OpenMetadataAPIMapper.QUALIFIED_NAME_PROPERTY_NAME;
import static org.odpi.openmetadata.commonservices.generichandlers.OpenMetadataAPIMapper.RELATIONAL_COLUMN_TYPE_NAME;
import static org.odpi.openmetadata.commonservices.generichandlers.OpenMetadataAPIMapper.RELATIONAL_TABLE_TYPE_NAME;
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.WARN)
class DataEngineRelationalDataHandlerTest {
    @Mock
    private RepositoryHandler repositoryHandler;
    @Mock
    private OMRSRepositoryHelper repositoryHelper;
    @Mock
    private InvalidParameterHandler invalidParameterHandler;
    @Mock
    private DataEngineCommonHandler dataEngineCommonHandler;
    @Mock
    private DataEngineRegistrationHandler registrationHandler;
    @Mock
    RelationalDataHandler<Database, DatabaseSchema, RelationalTable, RelationalTable, RelationalColumn,
            SchemaType> relationalDataHandler;
    @Mock
    private DataEngineConnectionAndEndpointHandler dataEngineConnectionAndEndpointHandler;

    @InjectMocks
    private DataEngineRelationalDataHandler dataEngineRelationalDataHandler;

    private static final String USER = "user";
    private static final String QUALIFIED_NAME = "qualifiedName";
    private static final String NAME = "name";
    private static final String DESCRIPTION = "desc";
    private static final String OWNER = "OWNER";
    private static final String DATABASE_TYPE = "DB2";
    private static final String DATABASE_VERSION = "10.05.0002";
    private static final String DATABASE_INSTANCE = "db2inst1";
    private static final String DATABASE_IMPORTED_FROM = "DB2Connector 9.1";
    private static final String GUID = "guid";
    private static final String EXTERNAL_SOURCE_DE_GUID = "externalSourceDataEngineGuid";
    private static final String EXTERNAL_SOURCE_DE_NAME = "externalSourceDataEngineQualifiedName";
    private static final List<String> ZONE_MEMBERSHIP = Collections.singletonList("default");
    private static final String DS_QUALIFIED_NAME = "DS_qualifiedName";
    private static final String DS_DISPLAY_NAME = "DS_displayName";
    private static final String DS_DESCRIPTION = "DS_desc";
    private static final String SCHEMA_GUID = "schema_guid";
    private static final String TABLE_GUID = "table_guid";
    private static final String COLUMN_QUALIFIED_NAME = "column_qualifiedName";
    private static final String COLUMN_NAME = "column_name";
    private static final String COLUMN_DESCRIPTION = "column_desc";
    private static final String COLUMN_DATA_TYPE = "String";
    private static final String COLUMN_FORMULA = "formula";
    private static final String COLUMN_GUID = "column_guid";
    private static final String PROTOCOL = "protocol";
    private static final String NETWORK_ADDRESS = "networkAddress";

    @Test
    void upsertDatabase_create_withDefaultSchema() throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException {
        String methodName = "upsertDatabase";
        Database database = getDatabase();

        when(registrationHandler.getExternalDataEngine(USER, EXTERNAL_SOURCE_DE_NAME)).thenReturn(EXTERNAL_SOURCE_DE_GUID);

        when(relationalDataHandler.createDatabase(USER, EXTERNAL_SOURCE_DE_GUID, EXTERNAL_SOURCE_DE_NAME, database.getQualifiedName(),
                database.getDisplayName(), database.getDescription(), database.getOwner(), database.getOwnerType().getOpenTypeOrdinal(),
                database.getZoneMembership(), database.getOriginOrganizationGUID(), database.getOriginBusinessCapabilityGUID(),
                database.getOtherOriginValues(), database.getPathName(), database.getCreateTime(), database.getModifiedTime(),
                database.getEncodingType(), database.getEncodingLanguage(), database.getEncodingDescription(), database.getEncodingProperties(),
                database.getDatabaseType(), database.getDatabaseVersion(), database.getDatabaseInstance(), database.getDatabaseImportedFrom(),
                database.getAdditionalProperties(), DATABASE_TYPE_NAME, null, null, methodName)).thenReturn(GUID);

        String result = dataEngineRelationalDataHandler.upsertDatabase(USER, database, EXTERNAL_SOURCE_DE_NAME);

        assertEquals(GUID, result);
        verifyInvalidParameterHandlerInvocations(methodName);
        String postfix = ":schema";
        verify(relationalDataHandler, times(1)).createDatabaseSchema(USER, EXTERNAL_SOURCE_DE_GUID, EXTERNAL_SOURCE_DE_NAME, GUID,
                database.getQualifiedName() + postfix, null, null, database.getOwner(),
                database.getOwnerType().getOpenTypeOrdinal(), database.getZoneMembership(), database.getOriginOrganizationGUID(),
                database.getOriginBusinessCapabilityGUID(), database.getOtherOriginValues(), null,
                DEPLOYED_DATABASE_SCHEMA_TYPE_NAME, null, null, "upsertDatabaseSchema");
        verify(dataEngineConnectionAndEndpointHandler, times(1)).upsertConnectionAndEndpoint(QUALIFIED_NAME,
                DATABASE_TYPE_NAME, PROTOCOL, NETWORK_ADDRESS, EXTERNAL_SOURCE_DE_GUID, EXTERNAL_SOURCE_DE_NAME, USER, "upsertDatabase");
    }

    private void verifyInvalidParameterHandlerInvocations(String methodName) throws
                                                                             org.odpi.openmetadata.commonservices.ffdc.exceptions.InvalidParameterException {
        verify(invalidParameterHandler, times(1)).validateUserId(USER, methodName);
        verify(invalidParameterHandler, times(1)).validateName(QUALIFIED_NAME, QUALIFIED_NAME_PROPERTY_NAME, methodName);
        verify(invalidParameterHandler, times(1)).validateName(NAME, DISPLAY_NAME_PROPERTY_NAME, methodName);
    }

    @Test
    void upsertDatabase_create_withSchema() throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException {
        String methodName = "upsertDatabase";
        Database database = getDatabase();
        database.setDatabaseSchema(getDatabaseSchema());

        when(registrationHandler.getExternalDataEngine(USER, EXTERNAL_SOURCE_DE_NAME)).thenReturn(EXTERNAL_SOURCE_DE_GUID);

        when(relationalDataHandler.createDatabase(USER, EXTERNAL_SOURCE_DE_GUID, EXTERNAL_SOURCE_DE_NAME, database.getQualifiedName(),
                database.getDisplayName(), database.getDescription(), database.getOwner(), database.getOwnerType().getOpenTypeOrdinal(),
                database.getZoneMembership(), database.getOriginOrganizationGUID(), database.getOriginBusinessCapabilityGUID(),
                database.getOtherOriginValues(), database.getPathName(), database.getCreateTime(), database.getModifiedTime(),
                database.getEncodingType(), database.getEncodingLanguage(), database.getEncodingDescription(), database.getEncodingProperties(),
                database.getDatabaseType(), database.getDatabaseVersion(), database.getDatabaseInstance(), database.getDatabaseImportedFrom(),
                database.getAdditionalProperties(), DATABASE_TYPE_NAME, null, null, methodName)).thenReturn(GUID);

        String result = dataEngineRelationalDataHandler.upsertDatabase(USER, database, EXTERNAL_SOURCE_DE_NAME);

        assertEquals(GUID, result);
        verifyInvalidParameterHandlerInvocations(methodName);
        verify(relationalDataHandler, times(1)).createDatabaseSchema(USER, EXTERNAL_SOURCE_DE_GUID, EXTERNAL_SOURCE_DE_NAME, GUID,
                database.getDatabaseSchema().getQualifiedName(), database.getDatabaseSchema().getDisplayName(),
                database.getDatabaseSchema().getDescription(), database.getOwner(), database.getOwnerType().getOpenTypeOrdinal(),
                database.getZoneMembership(), database.getOriginOrganizationGUID(), database.getOriginBusinessCapabilityGUID(),
                database.getOtherOriginValues(), null, DEPLOYED_DATABASE_SCHEMA_TYPE_NAME,
                null, null, "upsertDatabaseSchema");
        verify(dataEngineConnectionAndEndpointHandler, times(1)).upsertConnectionAndEndpoint(QUALIFIED_NAME,
                DATABASE_TYPE_NAME, PROTOCOL, NETWORK_ADDRESS, EXTERNAL_SOURCE_DE_GUID, EXTERNAL_SOURCE_DE_NAME, USER, "upsertDatabase");

    }

    @Test
    void upsertDatabase_update_withSchema() throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException {
        String methodName = "upsertDatabase";
        Database database = getDatabase();
        database.setDatabaseSchema(getDatabaseSchema());

        when(registrationHandler.getExternalDataEngine(USER, EXTERNAL_SOURCE_DE_NAME)).thenReturn(EXTERNAL_SOURCE_DE_GUID);

        mockFindEntity(QUALIFIED_NAME, GUID, DATABASE_TYPE_NAME);
        mockFindEntity(DS_QUALIFIED_NAME, SCHEMA_GUID, DEPLOYED_DATABASE_SCHEMA_TYPE_NAME);

        String result = dataEngineRelationalDataHandler.upsertDatabase(USER, database, EXTERNAL_SOURCE_DE_NAME);

        assertEquals(GUID, result);
        verifyInvalidParameterHandlerInvocations(methodName);
        verify(relationalDataHandler, times(1)).updateDatabase(USER, EXTERNAL_SOURCE_DE_GUID, EXTERNAL_SOURCE_DE_NAME, GUID,
                database.getQualifiedName(), database.getDisplayName(), database.getDescription(), database.getOwner(),
                database.getOwnerType().getOpenTypeOrdinal(), database.getZoneMembership(), database.getOriginOrganizationGUID(),
                database.getOriginBusinessCapabilityGUID(), database.getOtherOriginValues(), database.getCreateTime(), database.getModifiedTime(),
                database.getEncodingType(), database.getEncodingLanguage(), database.getEncodingDescription(), database.getEncodingProperties(),
                database.getDatabaseType(), database.getDatabaseVersion(), database.getDatabaseInstance(), database.getDatabaseImportedFrom(),
                database.getAdditionalProperties(), DATABASE_TYPE_NAME, null, null, methodName);

        verify(relationalDataHandler, times(1)).updateDatabaseSchema(USER, EXTERNAL_SOURCE_DE_GUID, EXTERNAL_SOURCE_DE_NAME,
                SCHEMA_GUID, database.getDatabaseSchema().getQualifiedName(), database.getDatabaseSchema().getDisplayName(),
                database.getDatabaseSchema().getDescription(), database.getOwner(), database.getOwnerType().getOpenTypeOrdinal(),
                database.getZoneMembership(), database.getOriginOrganizationGUID(), database.getOriginBusinessCapabilityGUID(),
                database.getOtherOriginValues(), null, DEPLOYED_DATABASE_SCHEMA_TYPE_NAME,
                null, null, "upsertDatabaseSchema");

        verify(dataEngineConnectionAndEndpointHandler, times(1)).upsertConnectionAndEndpoint(QUALIFIED_NAME,
                DATABASE_TYPE_NAME, PROTOCOL, NETWORK_ADDRESS, EXTERNAL_SOURCE_DE_GUID, EXTERNAL_SOURCE_DE_NAME, USER, "upsertDatabase");
    }

    @Test
    void upsertRelationalTable_create() throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException {
        String methodName = "upsertRelationalTable";
        RelationalTable relationalTable = getRelationalTable();
        RelationalColumn column = getRelationalColumn();
        relationalTable.setColumns(Collections.singletonList(column));

        when(registrationHandler.getExternalDataEngine(USER, EXTERNAL_SOURCE_DE_NAME)).thenReturn(EXTERNAL_SOURCE_DE_GUID);

        mockFindEntity(QUALIFIED_NAME, GUID, DATABASE_TYPE_NAME);
        mockGetDatabaseSchemaGUID();

        when(relationalDataHandler.createDatabaseTable(USER, EXTERNAL_SOURCE_DE_GUID, EXTERNAL_SOURCE_DE_NAME, SCHEMA_GUID,
                relationalTable.getQualifiedName(), relationalTable.getDisplayName(), relationalTable.getDescription(),
                relationalTable.isDeprecated(), relationalTable.getAliases(), relationalTable.getAdditionalProperties(),
                RELATIONAL_TABLE_TYPE_NAME, null, null, methodName)).thenReturn(TABLE_GUID);

        String result = dataEngineRelationalDataHandler.upsertRelationalTable(USER, QUALIFIED_NAME, relationalTable, EXTERNAL_SOURCE_DE_NAME);

        assertEquals(TABLE_GUID, result);
        verifyInvalidParameterHandlerInvocations(methodName);
        verify(relationalDataHandler, times(1)).createDatabaseColumn(USER, EXTERNAL_SOURCE_DE_GUID, EXTERNAL_SOURCE_DE_NAME,
                TABLE_GUID, column.getQualifiedName(), column.getDisplayName(), column.getDescription(), column.getExternalTypeGUID(),
                column.getDataType(), column.getDefaultValue(), column.getFixedValue(), column.getValidValuesSetGUID(), column.getFormula(),
                column.isDeprecated(), column.getPosition(), column.getMinCardinality(), column.getMaxCardinality(),
                column.getAllowsDuplicateValues(), column.getOrderedValues(), column.getDefaultValueOverride(),
                column.getSortOrder().getOpenTypeOrdinal(), column.getMinimumLength(), column.getLength(), column.getPrecision(),
                column.isNullable(), column.getNativeClass(), column.getAliases(), column.getAdditionalProperties(),
                RELATIONAL_COLUMN_TYPE_NAME, null, null, "upsertRelationalColumns");
    }

    @Test
    void upsertRelationalTable_update() throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException {
        String methodName = "upsertRelationalTable";
        RelationalTable relationalTable = getRelationalTable();
        RelationalColumn column = getRelationalColumn();
        relationalTable.setColumns(Collections.singletonList(column));

        when(registrationHandler.getExternalDataEngine(USER, EXTERNAL_SOURCE_DE_NAME)).thenReturn(EXTERNAL_SOURCE_DE_GUID);

        mockFindEntity(QUALIFIED_NAME, TABLE_GUID, RELATIONAL_TABLE_TYPE_NAME);
        mockFindEntity(COLUMN_QUALIFIED_NAME, COLUMN_GUID, RELATIONAL_COLUMN_TYPE_NAME);

        String result = dataEngineRelationalDataHandler.upsertRelationalTable(USER, QUALIFIED_NAME, relationalTable, EXTERNAL_SOURCE_DE_NAME);

        assertEquals(TABLE_GUID, result);
        verifyInvalidParameterHandlerInvocations(methodName);
        verify(relationalDataHandler, times(1)).updateDatabaseTable(USER, EXTERNAL_SOURCE_DE_GUID, EXTERNAL_SOURCE_DE_NAME, TABLE_GUID,
                relationalTable.getQualifiedName(), relationalTable.getDisplayName(), relationalTable.getDescription(),
                relationalTable.isDeprecated(), relationalTable.getAliases(), relationalTable.getAdditionalProperties(),
                RELATIONAL_TABLE_TYPE_NAME, null, null, methodName);
        verify(relationalDataHandler, times(1)).updateDatabaseColumn(USER, EXTERNAL_SOURCE_DE_GUID, EXTERNAL_SOURCE_DE_NAME,
                COLUMN_GUID, column.getQualifiedName(), column.getDisplayName(), column.getDescription(),
                column.getDataType(), column.getDefaultValue(), column.getFixedValue(), column.getFormula(), column.isDeprecated(),
                column.getPosition(), column.getMinCardinality(), column.getMaxCardinality(), column.getAllowsDuplicateValues(),
                column.getOrderedValues(), column.getDefaultValueOverride(), column.getSortOrder().getOpenTypeOrdinal(), column.getMinimumLength(),
                column.getLength(), column.getPrecision(), column.isNullable(), column.getNativeClass(), column.getAliases(),
                column.getAdditionalProperties(), RELATIONAL_COLUMN_TYPE_NAME, null, null,
                "upsertRelationalColumns");
    }

    private void mockGetDatabaseSchemaGUID() throws UserNotAuthorizedException, PropertyServerException {
        TypeDef relationshipTypeDef = mock(TypeDef.class);
        when(relationshipTypeDef.getName()).thenReturn(DATA_CONTENT_FOR_DATA_SET_TYPE_NAME);
        when(relationshipTypeDef.getGUID()).thenReturn(DATA_CONTENT_FOR_DATA_SET_TYPE_GUID);
        when(repositoryHelper.getTypeDefByName(USER, DATA_CONTENT_FOR_DATA_SET_TYPE_NAME)).thenReturn(relationshipTypeDef);
        EntityDetail mockedEntityDetail = mock(EntityDetail.class);
        when(mockedEntityDetail.getGUID()).thenReturn(SCHEMA_GUID);
        when(repositoryHandler.getEntityForRelationshipType(USER, GUID, DATABASE_TYPE_NAME, DATA_CONTENT_FOR_DATA_SET_TYPE_GUID,
                DATA_CONTENT_FOR_DATA_SET_TYPE_NAME, "findSchemaForDatabase")).thenReturn(mockedEntityDetail);
    }

    private EntityDetail mockFindEntity(String qualifiedName, String guid, String entityTypeName) throws UserNotAuthorizedException,
                                                                                                         PropertyServerException,
                                                                                                         InvalidParameterException {
        EntityDetail entityDetail = mock(EntityDetail.class);
        when(entityDetail.getGUID()).thenReturn(guid);
        Optional<EntityDetail> optionalOfMockedEntity = Optional.of(entityDetail);
        when(dataEngineCommonHandler.findEntity(USER, qualifiedName, entityTypeName)).thenReturn(optionalOfMockedEntity);

        return entityDetail;
    }

    private DatabaseSchema getDatabaseSchema() {
        DatabaseSchema databaseSchema = new DatabaseSchema();
        databaseSchema.setQualifiedName(DS_QUALIFIED_NAME);
        databaseSchema.setDisplayName(DS_DISPLAY_NAME);
        databaseSchema.setDescription(DS_DESCRIPTION);
        return databaseSchema;
    }

    private Database getDatabase() {
        Database database = new Database();
        database.setQualifiedName(QUALIFIED_NAME);
        database.setDisplayName(NAME);
        database.setDescription(DESCRIPTION);
        database.setOwner(OWNER);
        database.setOwnerType(OwnerType.USER_ID);
        database.setZoneMembership(ZONE_MEMBERSHIP);
        database.setDatabaseType(DATABASE_TYPE);
        database.setDatabaseVersion(DATABASE_VERSION);
        database.setDatabaseInstance(DATABASE_INSTANCE);
        database.setDatabaseImportedFrom(DATABASE_IMPORTED_FROM);
        database.setProtocol(PROTOCOL);
        database.setNetworkAddress(NETWORK_ADDRESS);

        return database;
    }

    private RelationalTable getRelationalTable() {
        RelationalTable relationalTable = new RelationalTable();
        relationalTable.setQualifiedName(QUALIFIED_NAME);
        relationalTable.setDisplayName(NAME);
        relationalTable.setDescription(DESCRIPTION);
        relationalTable.setDeprecated(false);
        relationalTable.setAliases(Collections.singletonList("alias"));

        return relationalTable;
    }

    private RelationalColumn getRelationalColumn() {
        RelationalColumn relationalColumn = new RelationalColumn();
        relationalColumn.setQualifiedName(COLUMN_QUALIFIED_NAME);
        relationalColumn.setDisplayName(COLUMN_NAME);
        relationalColumn.setDescription(COLUMN_DESCRIPTION);
        relationalColumn.setDataType(COLUMN_DATA_TYPE);
        relationalColumn.setFormula(COLUMN_FORMULA);
        relationalColumn.setSortOrder(DataItemSortOrder.ASCENDING);
        relationalColumn.setMaxCardinality(1);
        relationalColumn.setMinCardinality(0);
        relationalColumn.setLength(50);
        relationalColumn.setMinimumLength(0);

        return relationalColumn;
    }
}