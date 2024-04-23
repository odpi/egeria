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
import org.odpi.openmetadata.accessservices.dataengine.model.DeleteSemantic;
import org.odpi.openmetadata.accessservices.dataengine.model.FileFolder;
import org.odpi.openmetadata.accessservices.dataengine.model.Referenceable;
import org.odpi.openmetadata.accessservices.dataengine.server.mappers.CommonMapper;
import org.odpi.openmetadata.commonservices.ffdc.InvalidParameterHandler;
import org.odpi.openmetadata.commonservices.generichandlers.AssetHandler;
import org.odpi.openmetadata.commonservices.generichandlers.OpenMetadataAPIGenericHandler;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceStatus;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.FunctionNotSupportedException;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.WARN)
class DataEngineFolderHierarchyHandlerTest {

    private static final String USER = "user";
    private static final String METHOD = "method";
    private static final String PATH = "/test/file.txt";
    private static final String EXTERNAL_SOURCE_GUID = "externalSourceGuid";
    private static final String EXTERNAL_SOURCE_NAME = "externalSourceName";
    private static final String GUID_VALUE_1 = "1";
    private static final String GUID_VALUE_2 = "2";
    private static final String GUID_VALUE_3 = "3";

    @Mock
    private InvalidParameterHandler invalidParameterHandler;

    @Mock
    private OpenMetadataAPIGenericHandler<Referenceable> genericHandler;

    @Mock
    private DataEngineCommonHandler dataEngineCommonHandler;

    @Mock
    private AssetHandler<FileFolder> folderHandler;

    @InjectMocks
    private DataEngineFolderHierarchyHandler dataEngineFolderHierarchyHandler;

    @Test
    void upsertFolderHierarchy() throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException {
        mockDataEngineCommonHandler();
        mockFolderHandler();

        when(genericHandler.getAttachmentLinks(USER, GUID_VALUE_3, CommonMapper.GUID_PROPERTY_NAME, OpenMetadataType.DATA_FILE.typeName,
                                               null, null, null, null,
                                               2, false, false, 0,
                                               invalidParameterHandler.getMaxPagingSize(), null, METHOD)).thenReturn(Collections.emptyList());

        dataEngineFolderHierarchyHandler.upsertFolderHierarchy(GUID_VALUE_3, OpenMetadataType.DATA_FILE.typeName, PATH, EXTERNAL_SOURCE_GUID,
                EXTERNAL_SOURCE_NAME, USER, METHOD);

        verify(dataEngineCommonHandler, times(1)).findEntity(USER, EXTERNAL_SOURCE_NAME + "::/test",
                                                             OpenMetadataType.FILE_FOLDER.typeName);
        verify(folderHandler, times(1)).createAssetInRepository(USER, EXTERNAL_SOURCE_GUID,
                EXTERNAL_SOURCE_NAME, EXTERNAL_SOURCE_NAME + "::/test", "test", null, null,
                null, null, 0, null, null,
                null, null, OpenMetadataType.FILE_FOLDER.typeGUID, OpenMetadataType.FILE_FOLDER.typeName, null, null,
                null, InstanceStatus.ACTIVE, null, METHOD);
        verify(folderHandler, times(1)).createAssetInRepository(USER, EXTERNAL_SOURCE_GUID,
                EXTERNAL_SOURCE_NAME, EXTERNAL_SOURCE_NAME + "::/", "/", null,
                null, null, null, 0, null, null,
                null, null, OpenMetadataType.FILE_FOLDER.typeGUID, OpenMetadataType.FILE_FOLDER.typeName, null,
                null, null, InstanceStatus.ACTIVE, null, METHOD);
        // verify(dataEngineCommonHandler, times(1)).upsertExternalRelationship(USER, GUID_VALUE_1, GUID_VALUE_2,
        //        NESTED_FILE_TYPE_NAME, FILE_FOLDER.typeName, FILE_FOLDER.typeName, EXTERNAL_SOURCE_NAME,null);

        verify(dataEngineCommonHandler, times(1)).upsertExternalRelationship(USER, EXTERNAL_SOURCE_GUID,
                GUID_VALUE_2, OpenMetadataType.SERVER_ASSET_USE_RELATIONSHIP.typeName, OpenMetadataType.SOFTWARE_SERVER_CAPABILITY.typeName,
                                                                             OpenMetadataType.FILE_FOLDER.typeName, EXTERNAL_SOURCE_NAME,
                null);
    }

    @Test
    void removeFolder() throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException, FunctionNotSupportedException {


        final String methodName = "removeFolder";
        dataEngineFolderHierarchyHandler.removeFolder(USER, GUID_VALUE_1, DeleteSemantic.SOFT, EXTERNAL_SOURCE_NAME);

        verify(dataEngineCommonHandler, times(1)).validateDeleteSemantic(DeleteSemantic.SOFT, methodName);
        verify(invalidParameterHandler, times(1)).validateUserId(USER, methodName);
        verify(invalidParameterHandler, times(1)).validateGUID(GUID_VALUE_1, OpenMetadataProperty.GUID.name, methodName);
        verify(dataEngineCommonHandler, times(1)).removeEntity(USER, GUID_VALUE_1, OpenMetadataType.FILE_FOLDER.typeName, EXTERNAL_SOURCE_NAME);
    }

    @Test
    void removeFolder_throwsFunctionNotSupportedException() throws FunctionNotSupportedException {
        FunctionNotSupportedException mockedException = mock(FunctionNotSupportedException.class);
        doThrow(mockedException).when(dataEngineCommonHandler).validateDeleteSemantic(DeleteSemantic.HARD, "removeFolder");

        assertThrows(FunctionNotSupportedException.class, () ->
                dataEngineFolderHierarchyHandler.removeFolder(USER, GUID_VALUE_1, DeleteSemantic.HARD, EXTERNAL_SOURCE_NAME));
    }


    private void mockDataEngineCommonHandler()
            throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException {
        when(dataEngineCommonHandler.findEntity(USER, EXTERNAL_SOURCE_NAME + "::/test", OpenMetadataType.FILE_FOLDER.typeName))
                .thenReturn(Optional.empty());
        when(dataEngineCommonHandler.findEntity(USER, EXTERNAL_SOURCE_NAME + "::/", OpenMetadataType.FILE_FOLDER.typeName))
                .thenReturn(Optional.empty());
    }

    private void mockFolderHandler() throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException {
        when(folderHandler.createAssetInRepository(USER, EXTERNAL_SOURCE_GUID, EXTERNAL_SOURCE_NAME,
                EXTERNAL_SOURCE_NAME + "::/test", "test", null, null, null,
                null, 0, null, null, null,
                null, OpenMetadataType.FILE_FOLDER.typeGUID, OpenMetadataType.FILE_FOLDER.typeName, null, null,
                null, InstanceStatus.ACTIVE, null, METHOD)).
                thenReturn(GUID_VALUE_1);
        when(folderHandler.createAssetInRepository(USER, EXTERNAL_SOURCE_GUID, EXTERNAL_SOURCE_NAME,
                EXTERNAL_SOURCE_NAME + "::/", "/", null, null, null,
                null, 0, null, null, null,
                null, OpenMetadataType.FILE_FOLDER.typeGUID, OpenMetadataType.FILE_FOLDER.typeName, null, null,
                null, InstanceStatus.ACTIVE, null, METHOD)).
                thenReturn(GUID_VALUE_2);
    }

}
