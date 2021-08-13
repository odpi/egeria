/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.assetlineage.handlers;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.odpi.openmetadata.accessservices.assetlineage.model.LineageEntity;
import org.odpi.openmetadata.frameworks.connectors.ffdc.OCFCheckedExceptionBase;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceType;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.odpi.openmetadata.accessservices.assetlineage.util.AssetLineageConstants.ASSET_SCHEMA_TYPE;
import static org.odpi.openmetadata.accessservices.assetlineage.util.AssetLineageConstants.ATTRIBUTE_FOR_SCHEMA;
import static org.odpi.openmetadata.accessservices.assetlineage.util.AssetLineageConstants.CONNECTION;
import static org.odpi.openmetadata.accessservices.assetlineage.util.AssetLineageConstants.CONNECTION_ENDPOINT;
import static org.odpi.openmetadata.accessservices.assetlineage.util.AssetLineageConstants.CONNECTION_TO_ASSET;
import static org.odpi.openmetadata.accessservices.assetlineage.util.AssetLineageConstants.DATA_CONTENT_FOR_DATA_SET;
import static org.odpi.openmetadata.accessservices.assetlineage.util.AssetLineageConstants.DATA_FILE;
import static org.odpi.openmetadata.accessservices.assetlineage.util.AssetLineageConstants.FILE_FOLDER;
import static org.odpi.openmetadata.accessservices.assetlineage.util.AssetLineageConstants.NESTED_FILE;
import static org.odpi.openmetadata.accessservices.assetlineage.util.AssetLineageConstants.NESTED_SCHEMA_ATTRIBUTE;
import static org.odpi.openmetadata.accessservices.assetlineage.util.AssetLineageConstants.RELATIONAL_COLUMN;
import static org.odpi.openmetadata.accessservices.assetlineage.util.AssetLineageConstants.RELATIONAL_TABLE;
import static org.odpi.openmetadata.accessservices.assetlineage.util.AssetLineageConstants.TABULAR_COLUMN;
import static org.odpi.openmetadata.accessservices.assetlineage.util.AssetLineageConstants.TABULAR_FILE_COLUMN;
import static org.odpi.openmetadata.accessservices.assetlineage.util.AssetLineageConstants.TABULAR_SCHEMA_TYPE;
import static org.odpi.openmetadata.commonservices.ocf.metadatamanagement.mappers.SchemaElementMapper.RELATIONAL_DB_SCHEMA_TYPE_TYPE_NAME;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.WARN)
class AssetContextHandlerTest {
    private static final String GUID = "guid";
    private static final String USER = "user";
    @Mock
    private HandlerHelper handlerHelper;
    @Mock
    private List<String> supportedZones;
    @InjectMocks
    private AssetContextHandler assetContextHandler;

    @Test
    void buildSchemaElementContext_tabularFileColumn() throws OCFCheckedExceptionBase {
        EntityDetail entityDetail = mockEntityDetail(TABULAR_FILE_COLUMN);
        EntityDetail schemaType = mockEntityDetail(TABULAR_SCHEMA_TYPE);
        when(handlerHelper.addContextForRelationships(eq(USER), eq(entityDetail), eq(ATTRIBUTE_FOR_SCHEMA), any())).thenReturn(schemaType);

        assetContextHandler.buildSchemaElementContext(USER, entityDetail);

        verify(handlerHelper, times(1)).validateAsset(entityDetail, "buildSchemaElementContext", supportedZones);
        verify(handlerHelper, times(1)).addContextForRelationships(eq(USER), eq(schemaType), eq(ASSET_SCHEMA_TYPE), any());
    }

    @Test
    void buildSchemaElementContext_relationalColumn() throws OCFCheckedExceptionBase {
        EntityDetail entityDetail = mockEntityDetail(RELATIONAL_COLUMN);

        assetContextHandler.buildSchemaElementContext(USER, entityDetail);

        verify(handlerHelper, times(1)).validateAsset(entityDetail, "buildSchemaElementContext", supportedZones);
        verify(handlerHelper, times(1)).addContextForRelationships(eq(USER), eq(entityDetail), eq(NESTED_SCHEMA_ATTRIBUTE), any());
    }

    @Test
    void buildAssetContext_relationalTable() throws OCFCheckedExceptionBase {
        LineageEntity lineageEntity = mockLineageEntity(RELATIONAL_TABLE);
        EntityDetail entityDetail = mockEntityDetail(RELATIONAL_TABLE);
        when(handlerHelper.getEntityDetails(USER, GUID, RELATIONAL_TABLE)).thenReturn(entityDetail);

        when(handlerHelper.isTable(USER, entityDetail)).thenReturn(true);

        EntityDetail schemaType = mockEntityDetail(RELATIONAL_DB_SCHEMA_TYPE_TYPE_NAME);
        when(handlerHelper.addContextForRelationships(eq(USER), eq(entityDetail), eq(ATTRIBUTE_FOR_SCHEMA), any())).thenReturn(schemaType);

        EntityDetail deployedDbSchema = mockEntityDetail("DeployedDatabaseSchema");
        when(handlerHelper.addContextForRelationships(eq(USER), eq(schemaType), eq(ASSET_SCHEMA_TYPE), any())).thenReturn(deployedDbSchema);

        EntityDetail database = mockEntityDetail(RELATIONAL_DB_SCHEMA_TYPE_TYPE_NAME);
        when(handlerHelper.addContextForRelationships(eq(USER), eq(deployedDbSchema), eq(DATA_CONTENT_FOR_DATA_SET), any())).thenReturn(database);

        EntityDetail connection = mockEntityDetail(CONNECTION);
        when(handlerHelper.addContextForRelationships(eq(USER), eq(database), eq(CONNECTION_TO_ASSET), any())).thenReturn(connection);

        assetContextHandler.buildAssetContext(USER, lineageEntity);
        verify(handlerHelper, times(1)).addContextForRelationships(eq(USER), eq(connection), eq(CONNECTION_ENDPOINT), any());
        verify(handlerHelper, times(1)).validateAsset(entityDetail, "buildAssetContext", supportedZones);
    }

    @Test
    void buildAssetContext_dataFile() throws OCFCheckedExceptionBase {
        LineageEntity lineageEntity = mockLineageEntity(DATA_FILE);
        EntityDetail entityDetail = mockEntityDetail(DATA_FILE);
        when(handlerHelper.getEntityDetails(USER, GUID, DATA_FILE)).thenReturn(entityDetail);

        when(handlerHelper.isDataStore(USER, entityDetail)).thenReturn(true);

        EntityDetail connection = mockEntityDetail(CONNECTION);
        when(handlerHelper.addContextForRelationships(eq(USER), eq(entityDetail), eq(CONNECTION_TO_ASSET), any())).thenReturn(connection);

        EntityDetail fileFolder = mockEntityDetail(FILE_FOLDER);
        when(handlerHelper.addContextForRelationships(eq(USER), eq(entityDetail), eq(NESTED_FILE), any())).thenReturn(fileFolder);

        assetContextHandler.buildAssetContext(USER, lineageEntity);
        verify(handlerHelper, times(1)).addContextForRelationships(eq(USER), eq(connection), eq(CONNECTION_ENDPOINT), any());
        verify(handlerHelper, times(1)).validateAsset(entityDetail, "buildAssetContext", supportedZones);
    }

    @Test
    void buildColumnContext() throws OCFCheckedExceptionBase {
        LineageEntity lineageEntity = mockLineageEntity(RELATIONAL_COLUMN);
        EntityDetail entityDetail = mockEntityDetail(RELATIONAL_COLUMN);
        when(handlerHelper.isTabularColumn(USER, RELATIONAL_COLUMN)).thenReturn(true);
        when(handlerHelper.getEntityDetails(USER, GUID, TABULAR_COLUMN)).thenReturn(entityDetail);

        assetContextHandler.buildColumnContext(USER, lineageEntity);
        verify(handlerHelper, times(1)).addContextForRelationships(eq(USER), eq(entityDetail), eq(NESTED_SCHEMA_ATTRIBUTE), any());
    }

    @Test
    void buildAssetEntityContext() throws OCFCheckedExceptionBase {
        LineageEntity lineageEntity = mockLineageEntity(RELATIONAL_TABLE);
        EntityDetail entityDetail = mockEntityDetail(RELATIONAL_TABLE);
        when(handlerHelper.getEntityDetails(USER, GUID, RELATIONAL_TABLE)).thenReturn(entityDetail);
        when(handlerHelper.isTableOrDataStore(USER, entityDetail)).thenReturn(true);

        when(handlerHelper.getLineageEntity(entityDetail)).thenReturn(lineageEntity);

        assetContextHandler.buildAssetEntityContext(USER, GUID, RELATIONAL_TABLE);
        verify(handlerHelper, times(1)).getLineageEntity(entityDetail);
    }

    private EntityDetail mockEntityDetail(String typeDefName) {
        EntityDetail entityDetail = mock(EntityDetail.class);
        when(entityDetail.getGUID()).thenReturn(GUID);
        InstanceType mockedType = mock(InstanceType.class);
        when(entityDetail.getType()).thenReturn(mockedType);
        when(mockedType.getTypeDefName()).thenReturn(typeDefName);

        return entityDetail;
    }

    private LineageEntity mockLineageEntity(String typeDefName) {
        LineageEntity lineageEntity = new LineageEntity();
        lineageEntity.setGuid(GUID);
        lineageEntity.setTypeDefName(typeDefName);
        return lineageEntity;
    }
}
