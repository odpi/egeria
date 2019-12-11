/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.dataengine.server.handlers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.odpi.openmetadata.accessservices.dataengine.server.mappers.PortPropertiesMapper;
import org.odpi.openmetadata.accessservices.dataengine.server.mappers.ProcessPropertiesMapper;
import org.odpi.openmetadata.commonservices.ffdc.InvalidParameterHandler;
import org.odpi.openmetadata.commonservices.ocf.metadatamanagement.handlers.AssetHandler;
import org.odpi.openmetadata.commonservices.ocf.metadatamanagement.mappers.AssetMapper;
import org.odpi.openmetadata.commonservices.repositoryhandler.RepositoryHandler;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.Asset;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.OwnerType;
import org.odpi.openmetadata.metadatasecurity.properties.AssetAuditHeader;
import org.odpi.openmetadata.metadatasecurity.server.OpenMetadataServerSecurityVerifier;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceStatus;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceType;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Relationship;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.TypeDef;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.odpi.openmetadata.accessservices.dataengine.server.util.MockedExceptionUtil.mockException;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.WARN)
class ProcessHandlerTest {
    private static final String USER = "user";
    private static final String QUALIFIED_NAME = "qualifiedName";
    private static final String NAME = "name";
    private static final String DESCRIPTION = "desc";
    private static final String GUID = "guid";
    private static final String LATEST_CHANGE = "latestChange";
    private static final String FORMULA = "formula";
    private static final String OWNER = "OWNER";
    private static final String PROCESS_GUID = "processGuid";
    private static final String PORT_IMPL_GUID = "portImplGUID";
    private static final String PORT_ALIAS_GUID = "portAliasGUID";
    private static final String EXTERNAL_SOURCE_DE_GUID = "externalSourceDataEngineGuid";
    private static final String EXTERNAL_SOURCE_DE_QUALIFIED_NAME = "externalSourceDataEngineQualifiedName";

    @Captor
    private ArgumentCaptor<Asset> originalProcessCaptor;

    @Captor
    private ArgumentCaptor<Asset> updatedProcessCaptor;

    @Captor
    private ArgumentCaptor<AssetAuditHeader> assetHeaderCaptor;

    @Mock
    private RepositoryHandler repositoryHandler;

    @Mock
    private OMRSRepositoryHelper repositoryHelper;

    @Mock
    private InvalidParameterHandler invalidParameterHandler;

    @Mock
    private DataEngineRegistrationHandler dataEngineRegistrationHandler;

    @Mock
    private AssetHandler assetHandler;

    @Mock
    private OpenMetadataServerSecurityVerifier securityVerifier;

    @InjectMocks
    private ProcessHandler processHandler;

    @BeforeEach
    void before() {
        processHandler.setSecurityVerifier(securityVerifier);
    }

    @Test
    void createProcess() throws UserNotAuthorizedException, PropertyServerException, InvalidParameterException {
        String methodName = "createProcess";

        mockTypeDef(ProcessPropertiesMapper.PROCESS_TYPE_NAME, ProcessPropertiesMapper.PROCESS_TYPE_GUID);

        when(dataEngineRegistrationHandler.getExternalDataEngineByQualifiedName(USER,
                EXTERNAL_SOURCE_DE_QUALIFIED_NAME)).thenReturn(EXTERNAL_SOURCE_DE_GUID);

        Asset asset = mock(Asset.class);
        when(assetHandler.createEmptyAsset(ProcessPropertiesMapper.PROCESS_TYPE_NAME, methodName)).thenReturn(asset);

        when(repositoryHandler.createExternalEntity(USER, ProcessPropertiesMapper.PROCESS_TYPE_GUID,
                ProcessPropertiesMapper.PROCESS_TYPE_NAME, EXTERNAL_SOURCE_DE_GUID, EXTERNAL_SOURCE_DE_QUALIFIED_NAME,
                null, InstanceStatus.DRAFT, methodName)).thenReturn(GUID);

        String result = processHandler.createProcess(USER, QUALIFIED_NAME, NAME, DESCRIPTION, LATEST_CHANGE,
                null, NAME, FORMULA, OWNER, OwnerType.USER_ID, EXTERNAL_SOURCE_DE_QUALIFIED_NAME);

        assertEquals(GUID, result);
        verify(invalidParameterHandler, times(1)).validateUserId(USER, methodName);
        verify(invalidParameterHandler, times(1)).validateName(QUALIFIED_NAME,
                ProcessPropertiesMapper.QUALIFIED_NAME_PROPERTY_NAME, methodName);

        verify(repositoryHandler, times(1)).classifyEntity(USER, GUID, AssetMapper.ASSET_ZONES_CLASSIFICATION_GUID,
                AssetMapper.ASSET_ZONES_CLASSIFICATION_NAME, null, "addAssetClassifications");
        verify(repositoryHandler, times(1)).classifyEntity(USER, GUID, AssetMapper.ASSET_OWNERSHIP_CLASSIFICATION_GUID,
                AssetMapper.ASSET_OWNERSHIP_CLASSIFICATION_NAME, null, "addAssetClassifications");
    }

    @Test
    void createProcess_throwsUserNotAuthorizedException() throws UserNotAuthorizedException, PropertyServerException,
                                                                 InvocationTargetException, NoSuchMethodException,
                                                                 InstantiationException,
                                                                 IllegalAccessException, InvalidParameterException {
        String methodName = "createProcess";

        mockTypeDef(ProcessPropertiesMapper.PROCESS_TYPE_NAME, ProcessPropertiesMapper.PROCESS_TYPE_GUID);

        UserNotAuthorizedException mockedException = mockException(UserNotAuthorizedException.class, methodName);
        when(repositoryHandler.createExternalEntity(USER, ProcessPropertiesMapper.PROCESS_TYPE_GUID,
                ProcessPropertiesMapper.PROCESS_TYPE_NAME, EXTERNAL_SOURCE_DE_GUID, EXTERNAL_SOURCE_DE_QUALIFIED_NAME,
                null, InstanceStatus.DRAFT, methodName)).thenThrow(mockedException);

        when(dataEngineRegistrationHandler.getExternalDataEngineByQualifiedName(USER,
                EXTERNAL_SOURCE_DE_QUALIFIED_NAME)).thenReturn(EXTERNAL_SOURCE_DE_GUID);

        UserNotAuthorizedException thrown = assertThrows(UserNotAuthorizedException.class, () ->
                processHandler.createProcess(USER, QUALIFIED_NAME, NAME, DESCRIPTION, LATEST_CHANGE,
                        null, NAME, FORMULA, OWNER, OwnerType.USER_ID, EXTERNAL_SOURCE_DE_QUALIFIED_NAME));

        assertTrue(thrown.getMessage().contains("OMAS-DATA-ENGINE-404-001 "));
    }

    @Test
    void updateProcess() throws UserNotAuthorizedException, PropertyServerException, InvalidParameterException {
        String methodName = "updateProcess";

        EntityDetail entityDetail = mock(EntityDetail.class);
        when(repositoryHandler.getEntityByGUID(USER, PROCESS_GUID, ProcessPropertiesMapper.GUID_PROPERTY_NAME,
                ProcessPropertiesMapper.PROCESS_TYPE_NAME, methodName)).thenReturn(entityDetail);

        processHandler.updateProcess(USER, PROCESS_GUID, QUALIFIED_NAME, NAME, DESCRIPTION, LATEST_CHANGE,
                null, NAME, FORMULA, OWNER, OwnerType.USER_ID);

        verify(assetHandler, times(1)).updateAsset(any(), originalProcessCaptor.capture(),
                assetHeaderCaptor.capture(), updatedProcessCaptor.capture(), any(), any(), any(), any());
    }

    @Test
    void updateProcess_throwsUserNotAuthorizedException() throws UserNotAuthorizedException, PropertyServerException,
                                                                 InvocationTargetException, NoSuchMethodException,
                                                                 InstantiationException,
                                                                 IllegalAccessException, InvalidParameterException {
        String methodName = "updateProcess";

        EntityDetail entityDetail = mock(EntityDetail.class);
        when(repositoryHandler.getEntityByGUID(USER, PROCESS_GUID, ProcessPropertiesMapper.GUID_PROPERTY_NAME,
                ProcessPropertiesMapper.PROCESS_TYPE_NAME, methodName)).thenReturn(entityDetail);

        UserNotAuthorizedException mockedException = mockException(UserNotAuthorizedException.class, methodName);
        doThrow(mockedException).when(assetHandler).updateAsset(any(), originalProcessCaptor.capture(),
                assetHeaderCaptor.capture(), updatedProcessCaptor.capture(), any(), any(), any(), any());

        UserNotAuthorizedException thrown = assertThrows(UserNotAuthorizedException.class, () ->
                processHandler.updateProcess(USER, PROCESS_GUID, QUALIFIED_NAME, NAME, DESCRIPTION, LATEST_CHANGE,
                        null, NAME, FORMULA, OWNER, OwnerType.USER_ID));

        assertTrue(thrown.getMessage().contains("OMAS-DATA-ENGINE-404-001 "));
    }

    @Test
    void findProcess() throws UserNotAuthorizedException, PropertyServerException, InvalidParameterException {
        String methodName = "findProcess";

        mockTypeDef(ProcessPropertiesMapper.PROCESS_TYPE_NAME, ProcessPropertiesMapper.PROCESS_TYPE_GUID);
        when(repositoryHelper.getExactMatchRegex(QUALIFIED_NAME)).thenReturn(QUALIFIED_NAME);

        EntityDetail entityDetail = mock(EntityDetail.class);
        when(entityDetail.getGUID()).thenReturn(GUID);
        when(repositoryHandler.getUniqueEntityByName(USER, QUALIFIED_NAME,
                ProcessPropertiesMapper.QUALIFIED_NAME_PROPERTY_NAME, null,
                ProcessPropertiesMapper.PROCESS_TYPE_GUID, ProcessPropertiesMapper.PROCESS_TYPE_NAME, methodName)).
                thenReturn(entityDetail);

        String result = processHandler.findProcess(USER, QUALIFIED_NAME);

        assertTrue(result.equalsIgnoreCase(GUID));
        verify(invalidParameterHandler, times(1)).validateUserId(USER, methodName);
        verify(invalidParameterHandler, times(1)).validateName(QUALIFIED_NAME,
                PortPropertiesMapper.QUALIFIED_NAME_PROPERTY_NAME, methodName);
    }

    @Test
    void findProcess_notExisting() throws UserNotAuthorizedException, PropertyServerException,
                                          InvalidParameterException {
        String methodName = "findProcess";

        mockTypeDef(ProcessPropertiesMapper.PROCESS_TYPE_NAME, ProcessPropertiesMapper.PROCESS_TYPE_GUID);
        when(repositoryHelper.getExactMatchRegex(QUALIFIED_NAME)).thenReturn(QUALIFIED_NAME);

        when(repositoryHandler.getUniqueEntityByName(USER, QUALIFIED_NAME,
                ProcessPropertiesMapper.QUALIFIED_NAME_PROPERTY_NAME, null,
                ProcessPropertiesMapper.PROCESS_TYPE_GUID, ProcessPropertiesMapper.PROCESS_TYPE_NAME, methodName)).
                thenReturn(null);

        String result = processHandler.findProcess(USER, QUALIFIED_NAME);

        assertNull(result);
        verify(invalidParameterHandler, times(1)).validateUserId(USER, methodName);
        verify(invalidParameterHandler, times(1)).validateName(QUALIFIED_NAME,
                PortPropertiesMapper.QUALIFIED_NAME_PROPERTY_NAME, methodName);
    }

    @Test
    void addProcessPortRelationship() throws UserNotAuthorizedException, PropertyServerException,
                                             InvalidParameterException {
        String methodName = "addProcessPortRelationship";

        mockTypeDef(ProcessPropertiesMapper.PROCESS_PORT_TYPE_NAME, ProcessPropertiesMapper.PROCESS_PORT_TYPE_GUID);

        when(dataEngineRegistrationHandler.getExternalDataEngineByQualifiedName(USER,
                EXTERNAL_SOURCE_DE_QUALIFIED_NAME)).thenReturn(EXTERNAL_SOURCE_DE_GUID);

        processHandler.addProcessPortRelationship(USER, PROCESS_GUID, GUID, EXTERNAL_SOURCE_DE_QUALIFIED_NAME);

        verify(repositoryHandler, times(1)).createExternalRelationship(USER,
                ProcessPropertiesMapper.PROCESS_PORT_TYPE_GUID, EXTERNAL_SOURCE_DE_GUID,
                EXTERNAL_SOURCE_DE_QUALIFIED_NAME, PROCESS_GUID, GUID, null, methodName);
        verify(invalidParameterHandler, times(1)).validateUserId(USER, methodName);
        verify(invalidParameterHandler, times(1)).validateGUID(GUID,
                PortPropertiesMapper.GUID_PROPERTY_NAME, methodName);
        verify(invalidParameterHandler, times(1)).validateGUID(PROCESS_GUID,
                PortPropertiesMapper.GUID_PROPERTY_NAME, methodName);
    }

    @Test
    void addProcessPortRelationship_throwsUserNotAuthorizedException() throws UserNotAuthorizedException,
                                                                              PropertyServerException,
                                                                              InvocationTargetException,
                                                                              NoSuchMethodException,
                                                                              InstantiationException,
                                                                              IllegalAccessException,
                                                                              InvalidParameterException {
        String methodName = "addProcessPortRelationship";

        mockTypeDef(ProcessPropertiesMapper.PROCESS_PORT_TYPE_NAME, ProcessPropertiesMapper.PROCESS_PORT_TYPE_GUID);

        UserNotAuthorizedException mockedException = mockException(UserNotAuthorizedException.class, methodName);
        doThrow(mockedException).when(repositoryHandler).createExternalRelationship(USER,
                ProcessPropertiesMapper.PROCESS_PORT_TYPE_GUID, EXTERNAL_SOURCE_DE_GUID,
                EXTERNAL_SOURCE_DE_QUALIFIED_NAME, PROCESS_GUID, GUID, null, methodName);

        when(dataEngineRegistrationHandler.getExternalDataEngineByQualifiedName(USER,
                EXTERNAL_SOURCE_DE_QUALIFIED_NAME)).thenReturn(EXTERNAL_SOURCE_DE_GUID);

        UserNotAuthorizedException thrown = assertThrows(UserNotAuthorizedException.class, () ->
                processHandler.addProcessPortRelationship(USER, PROCESS_GUID, GUID, EXTERNAL_SOURCE_DE_QUALIFIED_NAME));

        assertTrue(thrown.getMessage().contains("OMAS-DATA-ENGINE-404-001 "));
    }

    @Test
    void addProcessPortRelationship_existingRelationship() throws UserNotAuthorizedException, PropertyServerException,
                                                                  InvalidParameterException {
        String methodName = "addProcessPortRelationship";

        mockTypeDef(ProcessPropertiesMapper.PROCESS_PORT_TYPE_NAME, ProcessPropertiesMapper.PROCESS_PORT_TYPE_GUID);

        Relationship relationship = mock(Relationship.class);
        when(repositoryHandler.getRelationshipBetweenEntities(USER, PROCESS_GUID,
                ProcessPropertiesMapper.PROCESS_TYPE_NAME, GUID, ProcessPropertiesMapper.PROCESS_PORT_TYPE_GUID,
                ProcessPropertiesMapper.PROCESS_PORT_TYPE_NAME, methodName)).thenReturn(relationship);

        when(dataEngineRegistrationHandler.getExternalDataEngineByQualifiedName(USER,
                EXTERNAL_SOURCE_DE_QUALIFIED_NAME)).thenReturn(EXTERNAL_SOURCE_DE_GUID);

        processHandler.addProcessPortRelationship(USER, PROCESS_GUID, GUID, EXTERNAL_SOURCE_DE_QUALIFIED_NAME);

        verify(repositoryHandler, times(0)).createExternalRelationship(USER,
                ProcessPropertiesMapper.PROCESS_PORT_TYPE_GUID, EXTERNAL_SOURCE_DE_GUID,
                EXTERNAL_SOURCE_DE_QUALIFIED_NAME, PROCESS_GUID, GUID, null, methodName);
        verify(invalidParameterHandler, times(1)).validateUserId(USER, methodName);
        verify(invalidParameterHandler, times(1)).validateGUID(GUID,
                PortPropertiesMapper.GUID_PROPERTY_NAME, methodName);
        verify(invalidParameterHandler, times(1)).validateGUID(PROCESS_GUID,
                PortPropertiesMapper.GUID_PROPERTY_NAME, methodName);
    }

    @Test
    void updateProcessStatus() throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException {
        final String methodName = "updateProcessStatus";

        mockTypeDef(ProcessPropertiesMapper.PROCESS_TYPE_NAME, ProcessPropertiesMapper.PROCESS_TYPE_GUID);

        processHandler.updateProcessStatus(USER, PROCESS_GUID, InstanceStatus.ACTIVE);

        verify(invalidParameterHandler, times(1)).validateUserId(USER, methodName);
        verify(invalidParameterHandler, times(1)).validateGUID(PROCESS_GUID,
                PortPropertiesMapper.GUID_PROPERTY_NAME, methodName);

        verify(repositoryHandler, times(1)).updateEntityStatus(USER, PROCESS_GUID,
                ProcessPropertiesMapper.PROCESS_TYPE_GUID, ProcessPropertiesMapper.PROCESS_TYPE_NAME,
                InstanceStatus.ACTIVE, methodName);
    }

    @Test
    void getPortsForProcess() throws UserNotAuthorizedException, PropertyServerException, InvalidParameterException {
        final String methodName = "getPortsForProcess";

        mockTypeDef(ProcessPropertiesMapper.PROCESS_PORT_TYPE_NAME, ProcessPropertiesMapper.PROCESS_PORT_TYPE_GUID);

        EntityDetail portImplementation = mock(EntityDetail.class);
        InstanceType mockedTypeImpl = mock(InstanceType.class);
        when(mockedTypeImpl.getTypeDefName()).thenReturn(PortPropertiesMapper.PORT_IMPLEMENTATION_TYPE_NAME);
        when(portImplementation.getType()).thenReturn(mockedTypeImpl);
        when(portImplementation.getGUID()).thenReturn(PORT_IMPL_GUID);

        EntityDetail portAlias = mock(EntityDetail.class);
        InstanceType mockedTypeAlias = mock(InstanceType.class);
        when(mockedTypeAlias.getTypeDefName()).thenReturn(PortPropertiesMapper.PORT_ALIAS_TYPE_NAME);
        when(portAlias.getType()).thenReturn(mockedTypeImpl);
        when(portAlias.getGUID()).thenReturn(PORT_ALIAS_GUID);

        List<EntityDetail> portEntityGUIDs = Arrays.asList(portAlias, portImplementation);
        when(repositoryHandler.getEntitiesForRelationshipType(USER, PROCESS_GUID,
                ProcessPropertiesMapper.PROCESS_TYPE_NAME, ProcessPropertiesMapper.PROCESS_PORT_TYPE_GUID,
                ProcessPropertiesMapper.PROCESS_PORT_TYPE_NAME, 0, 0, methodName)).thenReturn(portEntityGUIDs);


        Set<String> resultGUIDs = processHandler.getPortsForProcess(USER, PROCESS_GUID,
                PortPropertiesMapper.PORT_IMPLEMENTATION_TYPE_NAME);
        assertEquals(2, resultGUIDs.size());
        assertTrue(resultGUIDs.contains(PORT_IMPL_GUID));
        assertTrue(resultGUIDs.contains(PORT_ALIAS_GUID));
    }

    private void mockTypeDef(String typeName, String typeGUID) {
        TypeDef entityTypeDef = mock(TypeDef.class);
        when(repositoryHelper.getTypeDefByName(USER, typeName)).thenReturn(entityTypeDef);

        when(entityTypeDef.getName()).thenReturn(typeName);
        when(entityTypeDef.getGUID()).thenReturn(typeGUID);
    }
}