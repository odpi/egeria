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
import org.odpi.openmetadata.accessservices.dataengine.model.DeleteSemantic;
import org.odpi.openmetadata.accessservices.dataengine.model.OwnerType;
import org.odpi.openmetadata.accessservices.dataengine.model.RelationalColumn;
import org.odpi.openmetadata.accessservices.dataengine.model.RelationalTable;
import org.odpi.openmetadata.accessservices.dataengine.model.SchemaType;
import org.odpi.openmetadata.commonservices.ffdc.InvalidParameterHandler;
import org.odpi.openmetadata.commonservices.generichandlers.AssetHandler;
import org.odpi.openmetadata.commonservices.generichandlers.RelationalDataHandler;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.governanceaction.mapper.OpenMetadataProperty;
import org.odpi.openmetadata.frameworks.governanceaction.mapper.OpenMetadataType;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstancePropertyValue;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.FunctionNotSupportedException;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.WARN)
class DataEngineRelationalDataHandlerTest {
    private static final String DATABASE_SCHEMA_GUID = "databaseSchemaGUID";
    private static final String UPSERT_DATABASE_SCHEMA_METHOD = "upsertDatabaseSchema";
    private static final String UPSERT_RELATIONAL_TABLE_METHOD = "upsertRelationalTable";
    @Mock
    private OMRSRepositoryHelper repositoryHelper;
    @Mock
    private InvalidParameterHandler invalidParameterHandler;
    @Mock
    private DataEngineCommonHandler dataEngineCommonHandler;
    @Mock
    private DataEngineRegistrationHandler registrationHandler;
    @Mock
    private RelationalDataHandler<Database, DatabaseSchema, RelationalTable, RelationalTable, RelationalColumn,
            SchemaType> relationalDataHandler;
    @Mock
    private AssetHandler<DatabaseSchema> databaseSchemaAssetHandler;

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
    private static final String DATABASE_GUID = "database_guid";
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

    private void verifyInvalidParameterHandlerInvocations(String methodName) throws
                                                                             InvalidParameterException {
        //verify(invalidParameterHandler, times(1)).validateUserId(USER, methodName);
        //verify(invalidParameterHandler, times(1)).validateName(QUALIFIED_NAME, QUALIFIED_NAME_PROPERTY_NAME, methodName);
        //verify(invalidParameterHandler, times(1)).validateName(NAME, DISPLAY_NAME_PROPERTY_NAME, methodName);
    }

    @Test
    void upsertDatabase_create() throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException {
        String methodName = "upsertDatabase";
        Database database = getDatabase();
        DatabaseSchema databaseSchema = getDatabaseSchema();
        RelationalTable relationalTable = getRelationalTable();
        database.setDatabaseSchema(databaseSchema);
        database.setTables(Collections.singletonList(relationalTable));

        when(registrationHandler.getExternalDataEngine(USER, EXTERNAL_SOURCE_DE_NAME)).thenReturn(EXTERNAL_SOURCE_DE_GUID);

        when(relationalDataHandler.createDatabase(USER, EXTERNAL_SOURCE_DE_GUID, EXTERNAL_SOURCE_DE_NAME, database.getQualifiedName(),
                                                  database.getDisplayName(), null, database.getDescription(), database.getOwner(), database.getOwnerType().getOpenTypeOrdinal(),
                                                  database.getZoneMembership(), database.getOriginOrganizationGUID(), database.getOriginBusinessCapabilityGUID(),
                                                  database.getOtherOriginValues(), database.getPathName(), database.getCreateTime(), database.getModifiedTime(),
                                                  database.getEncodingType(), database.getEncodingLanguage(), database.getEncodingDescription(), database.getEncodingProperties(),
                                                  database.getDatabaseType(), database.getDatabaseVersion(), database.getDatabaseInstance(), database.getDatabaseImportedFrom(),
                                                  database.getAdditionalProperties(), OpenMetadataType.DATABASE_TYPE_NAME, null, null, null, null,
                                                  false, false, null, methodName))
                .thenReturn(DATABASE_GUID);

        EntityDetail entityDetail = mock(EntityDetail.class);
        when(entityDetail.getGUID()).thenReturn(DATABASE_SCHEMA_GUID);
        Optional<EntityDetail> optionalOfMockedEntity = Optional.of(entityDetail);
        when(dataEngineCommonHandler.findEntity(USER, DS_QUALIFIED_NAME, OpenMetadataType.DEPLOYED_DATABASE_SCHEMA_TYPE_NAME))
                .thenReturn(Optional.empty(), optionalOfMockedEntity);

        int ordinal = OwnerType.USER_ID.getOrdinal();
        when(relationalDataHandler.createDatabaseSchema(USER, EXTERNAL_SOURCE_DE_GUID, EXTERNAL_SOURCE_DE_NAME, DATABASE_GUID,
                databaseSchema.getQualifiedName(), databaseSchema.getDisplayName(), null, databaseSchema.getDescription(),
                databaseSchema.getOwner(), ordinal, databaseSchema.getZoneMembership(), databaseSchema.getOriginOrganizationGUID(),
                databaseSchema.getOriginBusinessCapabilityGUID(), databaseSchema.getOtherOriginValues(),
                databaseSchema.getAdditionalProperties(), OpenMetadataType.DEPLOYED_DATABASE_SCHEMA_TYPE_NAME, null,
                null, null, null, false, false,
                null, UPSERT_DATABASE_SCHEMA_METHOD)).thenReturn(SCHEMA_GUID);

        when(relationalDataHandler.createDatabaseTable(USER, EXTERNAL_SOURCE_DE_GUID, EXTERNAL_SOURCE_DE_NAME, DATABASE_SCHEMA_GUID,
                relationalTable.getQualifiedName(), relationalTable.getDisplayName(), relationalTable.getDescription(),
                relationalTable.getIsDeprecated(), relationalTable.getAliases(), relationalTable.getAdditionalProperties(),
                                                       OpenMetadataType.RELATIONAL_TABLE_TYPE_NAME, null, null, null, null,
                 false, false, null, UPSERT_RELATIONAL_TABLE_METHOD))
                .thenReturn(TABLE_GUID);
        String result = dataEngineRelationalDataHandler.upsertDatabase(USER, database, EXTERNAL_SOURCE_DE_NAME);

        assertEquals(DATABASE_GUID, result);
        verifyInvalidParameterHandlerInvocations(methodName);

        verify(dataEngineConnectionAndEndpointHandler, times(1)).upsertConnectionAndEndpoint(QUALIFIED_NAME,
                DATABASE_GUID, OpenMetadataType.DATABASE_TYPE_NAME, PROTOCOL, NETWORK_ADDRESS, EXTERNAL_SOURCE_DE_GUID, EXTERNAL_SOURCE_DE_NAME, USER);
        verify(relationalDataHandler, times(1)).createDatabaseSchema(USER, EXTERNAL_SOURCE_DE_GUID,
                EXTERNAL_SOURCE_DE_NAME, DATABASE_GUID, databaseSchema.getQualifiedName(), databaseSchema.getDisplayName(),
                null, databaseSchema.getDescription(), databaseSchema.getOwner(), ordinal, databaseSchema.getZoneMembership(),
                databaseSchema.getOriginOrganizationGUID(), databaseSchema.getOriginBusinessCapabilityGUID(),
                databaseSchema.getOtherOriginValues(), databaseSchema.getAdditionalProperties(), OpenMetadataType.DEPLOYED_DATABASE_SCHEMA_TYPE_NAME,
                null, null, null, null,
                false, false, null, UPSERT_DATABASE_SCHEMA_METHOD);
        verify(relationalDataHandler, times(1)).createDatabaseTable(USER, EXTERNAL_SOURCE_DE_GUID,
                EXTERNAL_SOURCE_DE_NAME, DATABASE_SCHEMA_GUID, relationalTable.getQualifiedName(), relationalTable.getDisplayName(),
                relationalTable.getDescription(), relationalTable.getIsDeprecated(), relationalTable.getAliases(),
                relationalTable.getAdditionalProperties(), OpenMetadataType.RELATIONAL_TABLE_TYPE_NAME, null,
                null, null, null, false, false, null, UPSERT_RELATIONAL_TABLE_METHOD);
    }

    @Test
    void upsertDatabase_update() throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException {
        String methodName = "upsertDatabase";
        Database database = getDatabase();
        database.setDatabaseSchema(getDatabaseSchema());

        when(registrationHandler.getExternalDataEngine(USER, EXTERNAL_SOURCE_DE_NAME)).thenReturn(EXTERNAL_SOURCE_DE_GUID);

        mockFindEntity(QUALIFIED_NAME, GUID, OpenMetadataType.DATABASE_TYPE_NAME);
        mockFindEntity(DS_QUALIFIED_NAME, DATABASE_GUID, OpenMetadataType.DEPLOYED_DATABASE_SCHEMA_TYPE_NAME);

        String result = dataEngineRelationalDataHandler.upsertDatabase(USER, database, EXTERNAL_SOURCE_DE_NAME);

        assertEquals(GUID, result);
        verifyInvalidParameterHandlerInvocations(methodName);
        verify(relationalDataHandler, times(1)).updateDatabase(USER, EXTERNAL_SOURCE_DE_GUID, EXTERNAL_SOURCE_DE_NAME, GUID,
                database.getQualifiedName(), database.getDisplayName(), database.getDescription(), database.getOwner(),
                database.getOwnerType().getOpenTypeOrdinal(), database.getZoneMembership(), database.getOriginOrganizationGUID(),
                database.getOriginBusinessCapabilityGUID(), database.getOtherOriginValues(), database.getCreateTime(), database.getModifiedTime(),
                database.getEncodingType(), database.getEncodingLanguage(), database.getEncodingDescription(), database.getEncodingProperties(),
                database.getDatabaseType(), database.getDatabaseVersion(), database.getDatabaseInstance(), database.getDatabaseImportedFrom(),
                database.getAdditionalProperties(), OpenMetadataType.DATABASE_TYPE_NAME, null, null, null, null,
                true, false, false, null, methodName);

        verify(dataEngineConnectionAndEndpointHandler, times(1)).upsertConnectionAndEndpoint(QUALIFIED_NAME,
                GUID, OpenMetadataType.DATABASE_TYPE_NAME, PROTOCOL, NETWORK_ADDRESS, EXTERNAL_SOURCE_DE_GUID, EXTERNAL_SOURCE_DE_NAME, USER);
    }

    @Test
    void upsertDatabaseSchema_create() throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException {
        String methodName = "upsertDatabaseSchema";
        DatabaseSchema databaseSchema = getDatabaseSchema();

        when(registrationHandler.getExternalDataEngine(USER, EXTERNAL_SOURCE_DE_NAME)).thenReturn(EXTERNAL_SOURCE_DE_GUID);

        int ordinal = OwnerType.USER_ID.getOrdinal();
        when(relationalDataHandler.createDatabaseSchema(USER, EXTERNAL_SOURCE_DE_GUID, EXTERNAL_SOURCE_DE_NAME, DATABASE_GUID,
                databaseSchema.getQualifiedName(), databaseSchema.getDisplayName(), null, databaseSchema.getDescription(),
                databaseSchema.getOwner(), ordinal, databaseSchema.getZoneMembership(), databaseSchema.getOriginOrganizationGUID(),
                databaseSchema.getOriginBusinessCapabilityGUID(), databaseSchema.getOtherOriginValues(),
                databaseSchema.getAdditionalProperties(), OpenMetadataType.DEPLOYED_DATABASE_SCHEMA_TYPE_NAME, null,
                null, null, null,
                false, false, null, methodName)).thenReturn(SCHEMA_GUID);

        String result = dataEngineRelationalDataHandler.upsertDatabaseSchema(USER, DATABASE_GUID, databaseSchema,
                EXTERNAL_SOURCE_DE_NAME);

        assertEquals(SCHEMA_GUID, result);
    }


    @Test
    void upsertDatabaseSchema_create_incomplete() throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException {
        String methodName = "upsertDatabaseSchema";
        DatabaseSchema databaseSchema = getDatabaseSchema();
        databaseSchema.setIncomplete(true);

        when(registrationHandler.getExternalDataEngine(USER, EXTERNAL_SOURCE_DE_NAME)).thenReturn(EXTERNAL_SOURCE_DE_GUID);

        int ordinal = OwnerType.USER_ID.getOrdinal();
        when(relationalDataHandler.createDatabaseSchema(USER, EXTERNAL_SOURCE_DE_GUID, EXTERNAL_SOURCE_DE_NAME, DATABASE_GUID,
                databaseSchema.getQualifiedName(), databaseSchema.getDisplayName(), null, databaseSchema.getDescription(),
                databaseSchema.getOwner(), ordinal, databaseSchema.getZoneMembership(), databaseSchema.getOriginOrganizationGUID(),
                databaseSchema.getOriginBusinessCapabilityGUID(), databaseSchema.getOtherOriginValues(),
                databaseSchema.getAdditionalProperties(), OpenMetadataType.DEPLOYED_DATABASE_SCHEMA_TYPE_NAME, null,
                null, null, null,
                false, false, null, methodName)).thenReturn(SCHEMA_GUID);

        doNothing().when(databaseSchemaAssetHandler).setClassificationInRepository(USER, EXTERNAL_SOURCE_DE_GUID, EXTERNAL_SOURCE_DE_NAME, SCHEMA_GUID,
                DATABASE_SCHEMA_GUID, OpenMetadataType.DEPLOYED_DATABASE_SCHEMA_TYPE_NAME, OpenMetadataType.INCOMPLETE_CLASSIFICATION_TYPE_GUID,
                                                                                   OpenMetadataType.INCOMPLETE_CLASSIFICATION_TYPE_NAME,
                null, null, false, false, true,null,
                 null, methodName);

        String result = dataEngineRelationalDataHandler.upsertDatabaseSchema(USER, DATABASE_GUID, databaseSchema,
                EXTERNAL_SOURCE_DE_NAME);

        verify(databaseSchemaAssetHandler, times(1)).setClassificationInRepository(USER, EXTERNAL_SOURCE_DE_GUID,
                EXTERNAL_SOURCE_DE_NAME, SCHEMA_GUID, DATABASE_SCHEMA_GUID, OpenMetadataType.DEPLOYED_DATABASE_SCHEMA_TYPE_NAME,
                                                                                   OpenMetadataType.INCOMPLETE_CLASSIFICATION_TYPE_GUID,
                                                                                   OpenMetadataType.INCOMPLETE_CLASSIFICATION_TYPE_NAME, null, true, false,
                false, null, methodName);
        assertEquals(SCHEMA_GUID, result);
    }

    @Test
    void upsertDatabaseSchema_update() throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException {
        String methodName = "upsertDatabaseSchema";
        DatabaseSchema databaseSchema = getDatabaseSchema();

        when(registrationHandler.getExternalDataEngine(USER, EXTERNAL_SOURCE_DE_NAME)).thenReturn(EXTERNAL_SOURCE_DE_GUID);
        mockFindEntity(DS_QUALIFIED_NAME, SCHEMA_GUID, OpenMetadataType.DEPLOYED_DATABASE_SCHEMA_TYPE_NAME);

        int ordinal = OwnerType.USER_ID.getOrdinal();
        doNothing().when(relationalDataHandler).updateDatabaseSchema(USER, EXTERNAL_SOURCE_DE_GUID, EXTERNAL_SOURCE_DE_NAME,
                SCHEMA_GUID, databaseSchema.getQualifiedName(), databaseSchema.getDisplayName(), null, databaseSchema.getDescription(),
                databaseSchema.getOwner(), ordinal, databaseSchema.getZoneMembership(), databaseSchema.getOriginOrganizationGUID(),
                databaseSchema.getOriginBusinessCapabilityGUID(), databaseSchema.getOtherOriginValues(),
                databaseSchema.getAdditionalProperties(), OpenMetadataType.DEPLOYED_DATABASE_SCHEMA_TYPE_NAME,
                null, null, null, null, true,
                false, false, null, methodName);

        String result = dataEngineRelationalDataHandler.upsertDatabaseSchema(USER, DATABASE_GUID, databaseSchema,
                EXTERNAL_SOURCE_DE_NAME);

        assertEquals(SCHEMA_GUID, result);
    }

    @Test
    void upsertRelationalTable_create() throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException {
        String methodName = "upsertRelationalTable";
        RelationalTable relationalTable = getRelationalTable();
        RelationalColumn column = getRelationalColumn();
        relationalTable.setColumns(Collections.singletonList(column));

        when(registrationHandler.getExternalDataEngine(USER, EXTERNAL_SOURCE_DE_NAME)).thenReturn(EXTERNAL_SOURCE_DE_GUID);

        mockFindEntity(QUALIFIED_NAME, DATABASE_GUID, OpenMetadataType.DEPLOYED_DATABASE_SCHEMA_TYPE_NAME);

        when(relationalDataHandler.createDatabaseTable(USER, EXTERNAL_SOURCE_DE_GUID, EXTERNAL_SOURCE_DE_NAME, DATABASE_GUID,
                relationalTable.getQualifiedName(), relationalTable.getDisplayName(), relationalTable.getDescription(),
                relationalTable.getIsDeprecated(), relationalTable.getAliases(), relationalTable.getAdditionalProperties(),
                                                       OpenMetadataType.RELATIONAL_TABLE_TYPE_NAME, null, null, null, null,
                false, false, null, methodName)).thenReturn(TABLE_GUID);

        String result = dataEngineRelationalDataHandler.upsertRelationalTable(USER, QUALIFIED_NAME, relationalTable,
                EXTERNAL_SOURCE_DE_NAME);

        assertEquals(TABLE_GUID, result);
        verifyInvalidParameterHandlerInvocations(methodName);
        verify(relationalDataHandler, times(1)).createDatabaseColumn(USER, EXTERNAL_SOURCE_DE_GUID, EXTERNAL_SOURCE_DE_NAME,
                TABLE_GUID, column.getQualifiedName(), column.getDisplayName(), column.getDescription(), column.getExternalTypeGUID(),
                column.getDataType(), column.getDefaultValue(), column.getFixedValue(), column.getValidValuesSetGUID(), column.getFormula(),
                column.getIsDeprecated(), column.getPosition(), column.getMinCardinality(), column.getMaxCardinality(),
                column.getAllowsDuplicateValues(), column.getOrderedValues(), column.getDefaultValueOverride(),
                column.getSortOrder().getOpenTypeOrdinal(), column.getMinimumLength(), column.getLength(), column.getPrecision(),
                column.getIsNullable(), column.getNativeClass(), column.getAliases(), column.getAdditionalProperties(),
                                                                     OpenMetadataType.RELATIONAL_COLUMN_TYPE_NAME, null, null, null,
                null, false, false, null,"upsertRelationalColumns");
    }

    @Test
    void upsertRelationalTable_create_incomplete() throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException {
        String methodName = "upsertRelationalTable";
        RelationalTable relationalTable = getRelationalTable();
        RelationalColumn column = getRelationalColumn();
        relationalTable.setColumns(Collections.singletonList(column));
        relationalTable.setIncomplete(true);

        when(registrationHandler.getExternalDataEngine(USER, EXTERNAL_SOURCE_DE_NAME)).thenReturn(EXTERNAL_SOURCE_DE_GUID);

        mockFindEntity(QUALIFIED_NAME, DATABASE_GUID, OpenMetadataType.DEPLOYED_DATABASE_SCHEMA_TYPE_NAME);

        when(relationalDataHandler.createDatabaseTable(USER, EXTERNAL_SOURCE_DE_GUID, EXTERNAL_SOURCE_DE_NAME, DATABASE_GUID,
                relationalTable.getQualifiedName(), relationalTable.getDisplayName(), relationalTable.getDescription(),
                relationalTable.getIsDeprecated(), relationalTable.getAliases(), relationalTable.getAdditionalProperties(),
                                                       OpenMetadataType.RELATIONAL_TABLE_TYPE_NAME, null, null, null, null, false,
                false, null, methodName)).thenReturn(TABLE_GUID);

        doNothing().when(databaseSchemaAssetHandler).setClassificationInRepository(USER, null, null,
                TABLE_GUID, OpenMetadataType.RELATIONAL_TABLE_TYPE_GUID, OpenMetadataType.RELATIONAL_TABLE_TYPE_NAME,
                                                                                   OpenMetadataType.INCOMPLETE_CLASSIFICATION_TYPE_GUID,
                                                                                   OpenMetadataType.INCOMPLETE_CLASSIFICATION_TYPE_NAME,
                null, null, false, false, true,null, null, methodName);

        String result = dataEngineRelationalDataHandler.upsertRelationalTable(USER, QUALIFIED_NAME, relationalTable,
                EXTERNAL_SOURCE_DE_NAME);

        assertEquals(TABLE_GUID, result);
        verifyInvalidParameterHandlerInvocations(methodName);
        verify(relationalDataHandler, times(1)).createDatabaseColumn(USER, EXTERNAL_SOURCE_DE_GUID, EXTERNAL_SOURCE_DE_NAME,
                TABLE_GUID, column.getQualifiedName(), column.getDisplayName(), column.getDescription(), column.getExternalTypeGUID(),
                column.getDataType(), column.getDefaultValue(), column.getFixedValue(), column.getValidValuesSetGUID(), column.getFormula(),
                column.getIsDeprecated(), column.getPosition(), column.getMinCardinality(), column.getMaxCardinality(),
                column.getAllowsDuplicateValues(), column.getOrderedValues(), column.getDefaultValueOverride(),
                column.getSortOrder().getOpenTypeOrdinal(), column.getMinimumLength(), column.getLength(), column.getPrecision(),
                column.getIsNullable(), column.getNativeClass(), column.getAliases(), column.getAdditionalProperties(),
                                                                     OpenMetadataType.RELATIONAL_COLUMN_TYPE_NAME, null, null, null, null,
                false, false,  null, "upsertRelationalColumns");

        verify(databaseSchemaAssetHandler, times(1)).setClassificationInRepository(USER,EXTERNAL_SOURCE_DE_GUID, EXTERNAL_SOURCE_DE_NAME,
                TABLE_GUID, OpenMetadataType.RELATIONAL_TABLE_TYPE_GUID, OpenMetadataType.RELATIONAL_TABLE_TYPE_NAME,
                                                                                   OpenMetadataType.INCOMPLETE_CLASSIFICATION_TYPE_GUID,
                                                                                   OpenMetadataType.INCOMPLETE_CLASSIFICATION_TYPE_NAME,
                null, true, false, false, null, methodName);
    }

    @Test
    void upsertRelationalTable_update() throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException {
        String methodName = "upsertRelationalTable";
        RelationalTable relationalTable = getRelationalTable();
        RelationalColumn column = getRelationalColumn();
        relationalTable.setColumns(Collections.singletonList(column));

        when(registrationHandler.getExternalDataEngine(USER, EXTERNAL_SOURCE_DE_NAME)).thenReturn(EXTERNAL_SOURCE_DE_GUID);

        mockFindEntity(QUALIFIED_NAME, TABLE_GUID, OpenMetadataType.RELATIONAL_TABLE_TYPE_NAME);
        mockFindEntity(COLUMN_QUALIFIED_NAME, COLUMN_GUID, OpenMetadataType.RELATIONAL_COLUMN_TYPE_NAME);

        String result = dataEngineRelationalDataHandler.upsertRelationalTable(USER, QUALIFIED_NAME, relationalTable, EXTERNAL_SOURCE_DE_NAME);

        assertEquals(TABLE_GUID, result);
        verifyInvalidParameterHandlerInvocations(methodName);
        verify(relationalDataHandler, times(1)).updateDatabaseTable(USER, EXTERNAL_SOURCE_DE_GUID, EXTERNAL_SOURCE_DE_NAME, TABLE_GUID,
                relationalTable.getQualifiedName(), relationalTable.getDisplayName(), relationalTable.getDescription(),
                relationalTable.getIsDeprecated(), relationalTable.getAliases(), relationalTable.getAdditionalProperties(),
                                                                    OpenMetadataType.RELATIONAL_TABLE_TYPE_NAME, null, null, null,
                null, true, false, false, null, methodName);
        verify(relationalDataHandler, times(1)).updateDatabaseColumn(USER, EXTERNAL_SOURCE_DE_GUID, EXTERNAL_SOURCE_DE_NAME,
                COLUMN_GUID, column.getQualifiedName(), column.getDisplayName(), column.getDescription(),
                column.getDataType(), column.getDefaultValue(), column.getFixedValue(), column.getFormula(), column.getIsDeprecated(),
                column.getPosition(), column.getMinCardinality(), column.getMaxCardinality(), column.getAllowsDuplicateValues(),
                column.getOrderedValues(), column.getDefaultValueOverride(), column.getSortOrder().getOpenTypeOrdinal(), column.getMinimumLength(),
                column.getLength(), column.getPrecision(), column.getIsNullable(), column.getNativeClass(), column.getAliases(),
                column.getAdditionalProperties(), OpenMetadataType.RELATIONAL_COLUMN_TYPE_NAME, null, null,null,
                null, true, false, false, null, "upsertRelationalColumns");
    }

    @Test
    void removeDatabase() throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException, FunctionNotSupportedException {

        final String methodName = "removeDatabase";

        EntityDetail mockedEntity = mockEntityDetail(GUID);
        when(dataEngineCommonHandler.getEntityDetails(USER, GUID, OpenMetadataType.DATABASE_TYPE_NAME))
                .thenReturn(Optional.of(mockedEntity));

        when(registrationHandler.getExternalDataEngine(USER, EXTERNAL_SOURCE_DE_NAME)).thenReturn(EXTERNAL_SOURCE_DE_GUID);

        dataEngineRelationalDataHandler.removeDatabase(USER, GUID, EXTERNAL_SOURCE_DE_NAME, DeleteSemantic.SOFT);

        verify(dataEngineCommonHandler, times(1)).validateDeleteSemantic(DeleteSemantic.SOFT, methodName);

        verify(relationalDataHandler, times(1)).removeDatabase(USER, EXTERNAL_SOURCE_DE_GUID, EXTERNAL_SOURCE_DE_NAME,
                GUID, QUALIFIED_NAME, false, false, null, methodName);
    }

    @Test
    void removeDatabase_throwsFunctionNotSupportedException() throws FunctionNotSupportedException {
        FunctionNotSupportedException mockedException = mock(FunctionNotSupportedException.class);
        doThrow(mockedException).when(dataEngineCommonHandler).validateDeleteSemantic(DeleteSemantic.HARD, "removeDatabase");

        assertThrows(FunctionNotSupportedException.class, () ->
                dataEngineRelationalDataHandler.removeDatabase(USER, GUID, EXTERNAL_SOURCE_DE_NAME, DeleteSemantic.HARD));
    }

    @Test
    void removeDatabaseSchema() throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException, FunctionNotSupportedException {

        final String methodName = "removeDatabaseSchema";

        EntityDetail mockedEntity = mockEntityDetail(SCHEMA_GUID);
        when(dataEngineCommonHandler.getEntityDetails(USER, SCHEMA_GUID, OpenMetadataType.DEPLOYED_DATABASE_SCHEMA_TYPE_NAME))
                .thenReturn(Optional.of(mockedEntity));

        when(registrationHandler.getExternalDataEngine(USER, EXTERNAL_SOURCE_DE_NAME)).thenReturn(EXTERNAL_SOURCE_DE_GUID);

        dataEngineRelationalDataHandler.removeDatabaseSchema(USER, SCHEMA_GUID, EXTERNAL_SOURCE_DE_NAME, DeleteSemantic.SOFT);

        verify(dataEngineCommonHandler, times(1)).validateDeleteSemantic(DeleteSemantic.SOFT, methodName);

        verify(relationalDataHandler, times(1)).removeDatabaseSchema(USER, EXTERNAL_SOURCE_DE_GUID,
                EXTERNAL_SOURCE_DE_NAME, SCHEMA_GUID, QUALIFIED_NAME, false, false, null, methodName);
    }

    @Test
    void removeRelationalTable() throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException, FunctionNotSupportedException {

        final String methodName = "removeRelationalTable";

        EntityDetail mockedEntity = mockEntityDetail(GUID);
        when(dataEngineCommonHandler.getEntityDetails(USER, GUID, OpenMetadataType.RELATIONAL_TABLE_TYPE_NAME))
                .thenReturn(Optional.of(mockedEntity));

        when(registrationHandler.getExternalDataEngine(USER, EXTERNAL_SOURCE_DE_NAME)).thenReturn(EXTERNAL_SOURCE_DE_GUID);

        dataEngineRelationalDataHandler.removeRelationalTable(USER, GUID, EXTERNAL_SOURCE_DE_NAME, DeleteSemantic.SOFT);

        verify(dataEngineCommonHandler, times(1)).validateDeleteSemantic(DeleteSemantic.SOFT, methodName);

        verify(relationalDataHandler, times(1)).removeDatabaseTable(USER,
                EXTERNAL_SOURCE_DE_GUID, EXTERNAL_SOURCE_DE_NAME, GUID, GUID, QUALIFIED_NAME,
                false, false, null, methodName);
    }

    private EntityDetail mockEntityDetail(String guid) {
        EntityDetail entityDetail = mock(EntityDetail.class);

        when(entityDetail.getGUID()).thenReturn(guid);
        InstanceProperties instanceProperties = mock(InstanceProperties.class);
        InstancePropertyValue propertyValue = mock(InstancePropertyValue.class);
        when(propertyValue.valueAsString()).thenReturn(QUALIFIED_NAME);
        when(instanceProperties.getPropertyValue(OpenMetadataProperty.QUALIFIED_NAME.name)).thenReturn(propertyValue);
        when(entityDetail.getProperties()).thenReturn(instanceProperties);

        return entityDetail;
    }

    private void mockFindEntity(String qualifiedName, String guid, String entityTypeName) throws UserNotAuthorizedException,
                                                                                                         PropertyServerException,
                                                                                                         InvalidParameterException {
        EntityDetail entityDetail = mock(EntityDetail.class);
        when(entityDetail.getGUID()).thenReturn(guid);
        Optional<EntityDetail> optionalOfMockedEntity = Optional.of(entityDetail);
        when(dataEngineCommonHandler.findEntity(USER, qualifiedName, entityTypeName)).thenReturn(optionalOfMockedEntity);
    }

    private DatabaseSchema getDatabaseSchema() {
        DatabaseSchema databaseSchema = new DatabaseSchema();
        databaseSchema.setQualifiedName(DS_QUALIFIED_NAME);
        databaseSchema.setDisplayName(DS_DISPLAY_NAME);
        databaseSchema.setDescription(DS_DESCRIPTION);
        databaseSchema.setOwnerType(OwnerType.USER_ID);
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
        relationalTable.setIsDeprecated(false);
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