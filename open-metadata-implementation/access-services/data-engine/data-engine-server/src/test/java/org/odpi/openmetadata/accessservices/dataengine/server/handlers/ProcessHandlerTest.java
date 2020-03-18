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
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.odpi.openmetadata.accessservices.dataengine.ffdc.DataEngineErrorCode;
import org.odpi.openmetadata.accessservices.dataengine.model.ParentProcess;
import org.odpi.openmetadata.accessservices.dataengine.model.Process;
import org.odpi.openmetadata.accessservices.dataengine.model.ProcessContainmentType;
import org.odpi.openmetadata.accessservices.dataengine.model.UpdateSemantic;
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
import org.odpi.openmetadata.metadatasecurity.server.OpenMetadataServerSecurityVerifier;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetailDifferences;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceStatus;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceType;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.TypeDef;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
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
import static org.testng.AssertJUnit.assertFalse;

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
    private static final String EXTERNAL_SOURCE_DE_QUALIFIED_NAME = "externalSourceDataEngineQualifiedName";
    private static final List<String> ZONES = Collections.singletonList("ZONE");
    private static final String PARENT_PROCESS_QUALIFIED_NAME = "parentQualifiedName";
    private static final String PARENT_GUID = "parentGUID";

    @Captor
    private ArgumentCaptor<Asset> originalProcessCaptor;

    @Mock
    private RepositoryHandler repositoryHandler;

    @Mock
    private OMRSRepositoryHelper repositoryHelper;

    @Mock
    private InvalidParameterHandler invalidParameterHandler;

    @Mock
    private AssetHandler assetHandler;

    @Mock
    private OpenMetadataServerSecurityVerifier securityVerifier;

    @InjectMocks
    private ProcessHandler processHandler;

    @Mock
    private DataEngineCommonHandler dataEngineCommonHandler;

    @BeforeEach
    void before() {
        processHandler.setSecurityVerifier(securityVerifier);
    }

    @Test
    void createProcess() throws UserNotAuthorizedException, PropertyServerException, InvalidParameterException {
        String methodName = "createProcess";

        mockTypeDef(ProcessPropertiesMapper.PROCESS_TYPE_NAME, ProcessPropertiesMapper.PROCESS_TYPE_GUID);

        when(dataEngineCommonHandler.createExternalEntity(USER, null, InstanceStatus.DRAFT,
                ProcessPropertiesMapper.PROCESS_TYPE_NAME, EXTERNAL_SOURCE_DE_QUALIFIED_NAME)).thenReturn(GUID);

        when(securityVerifier.initializeAssetZones(any(), any())).thenReturn(ZONES);
        String result = processHandler.createProcess(USER, getProcess(), EXTERNAL_SOURCE_DE_QUALIFIED_NAME);

        assertEquals(GUID, result);
        verify(invalidParameterHandler, times(1)).validateUserId(USER, methodName);
        verify(invalidParameterHandler, times(1)).validateName(QUALIFIED_NAME,
                ProcessPropertiesMapper.QUALIFIED_NAME_PROPERTY_NAME, methodName);

        verify(repositoryHandler, times(1)).classifyEntity(USER, GUID, AssetMapper.ASSET_ZONES_CLASSIFICATION_GUID,
                AssetMapper.ASSET_ZONES_CLASSIFICATION_NAME, null, "classifyAsset");
        verify(repositoryHandler, times(1)).classifyEntity(USER, GUID, AssetMapper.ASSET_OWNERSHIP_CLASSIFICATION_GUID,
                AssetMapper.ASSET_OWNERSHIP_CLASSIFICATION_NAME, null, "classifyAsset");
    }

    @Test
    void createProcess_throwsUserNotAuthorizedException() throws UserNotAuthorizedException, PropertyServerException,
                                                                 InvocationTargetException, NoSuchMethodException,
                                                                 InstantiationException,
                                                                 IllegalAccessException, InvalidParameterException {
        String methodName = "createProcess";

        UserNotAuthorizedException mockedException = mockException(UserNotAuthorizedException.class, methodName);
        when(dataEngineCommonHandler.createExternalEntity(USER, null, InstanceStatus.DRAFT,
                ProcessPropertiesMapper.PROCESS_TYPE_NAME, EXTERNAL_SOURCE_DE_QUALIFIED_NAME)).thenThrow(mockedException);

        UserNotAuthorizedException thrown = assertThrows(UserNotAuthorizedException.class, () ->
                processHandler.createProcess(USER, getProcess(), EXTERNAL_SOURCE_DE_QUALIFIED_NAME));

        assertTrue(thrown.getMessage().contains("OMAS-DATA-ENGINE-404-001 "));
    }

    @Test
    void updateProcess() throws UserNotAuthorizedException, PropertyServerException, InvalidParameterException {
        EntityDetail mockedOriginalProcessEntity = Mockito.mock(EntityDetail.class);
        when(mockedOriginalProcessEntity.getGUID()).thenReturn(PROCESS_GUID);

        EntityDetail mockedUpdatedProcessEntity = Mockito.mock(EntityDetail.class);
        when(dataEngineCommonHandler.buildEntityDetail(PROCESS_GUID, null)).thenReturn(mockedUpdatedProcessEntity);

        EntityDetailDifferences mockedDifferences = mock(EntityDetailDifferences.class);
        when(mockedDifferences.hasInstancePropertiesDifferences()).thenReturn(Boolean.TRUE);
        when(repositoryHelper.getEntityDetailDifferences(mockedOriginalProcessEntity, mockedUpdatedProcessEntity, true)).thenReturn(mockedDifferences);

        processHandler.updateProcess(USER, mockedOriginalProcessEntity, getProcess());

        verify(assetHandler, times(1)).reclassifyAsset(any(), originalProcessCaptor.capture(),
                any(), any(), any(), any());
        verify(dataEngineCommonHandler, times(1)).updateEntity(USER, PROCESS_GUID, null, ProcessPropertiesMapper.PROCESS_TYPE_NAME);
    }

    @Test
    void updateProcess_noChanges() throws UserNotAuthorizedException, PropertyServerException, InvalidParameterException {
        EntityDetail mockedOriginalProcessEntity = Mockito.mock(EntityDetail.class);
        when(mockedOriginalProcessEntity.getGUID()).thenReturn(PROCESS_GUID);

        EntityDetail mockedUpdatedProcessEntity = Mockito.mock(EntityDetail.class);
        when(dataEngineCommonHandler.buildEntityDetail(PROCESS_GUID, null)).thenReturn(mockedUpdatedProcessEntity);

        EntityDetailDifferences mockedDifferences = mock(EntityDetailDifferences.class);
        when(mockedDifferences.hasInstancePropertiesDifferences()).thenReturn(Boolean.FALSE);
        when(repositoryHelper.getEntityDetailDifferences(mockedOriginalProcessEntity, mockedUpdatedProcessEntity, true)).thenReturn(mockedDifferences);

        processHandler.updateProcess(USER, mockedOriginalProcessEntity, getProcess());

        verify(assetHandler, times(1)).reclassifyAsset(any(), originalProcessCaptor.capture(),
                any(), any(), any(), any());
        verify(dataEngineCommonHandler, times(0)).updateEntity(USER, PROCESS_GUID, null, ProcessPropertiesMapper.PROCESS_TYPE_NAME);
    }

    @Test
    void updateProcess_throwsUserNotAuthorizedException() throws UserNotAuthorizedException, PropertyServerException,
                                                                 InvocationTargetException, NoSuchMethodException,
                                                                 InstantiationException,
                                                                 IllegalAccessException {
        EntityDetail mockedOriginalProcessEntity = Mockito.mock(EntityDetail.class);
        when(mockedOriginalProcessEntity.getGUID()).thenReturn(PROCESS_GUID);

        EntityDetail mockedUpdatedProcessEntity = Mockito.mock(EntityDetail.class);
        when(dataEngineCommonHandler.buildEntityDetail(PROCESS_GUID, null)).thenReturn(mockedUpdatedProcessEntity);

        EntityDetailDifferences mockedDifferences = mock(EntityDetailDifferences.class);
        when(mockedDifferences.hasInstancePropertiesDifferences()).thenReturn(Boolean.TRUE);
        when(repositoryHelper.getEntityDetailDifferences(mockedOriginalProcessEntity, mockedUpdatedProcessEntity, true)).thenReturn(mockedDifferences);

        UserNotAuthorizedException mockedException = mockException(UserNotAuthorizedException.class, "updateProcess");
        doThrow(mockedException).when(dataEngineCommonHandler).updateEntity(USER, PROCESS_GUID, null, ProcessPropertiesMapper.PROCESS_TYPE_NAME);

        UserNotAuthorizedException thrown = assertThrows(UserNotAuthorizedException.class, () -> processHandler.updateProcess(USER,
                mockedOriginalProcessEntity, getProcess()));

        assertTrue(thrown.getMessage().contains("OMAS-DATA-ENGINE-404-001 "));
    }

    @Test
    void findProcess() throws UserNotAuthorizedException, PropertyServerException, InvalidParameterException {
        EntityDetail entityDetail = mock(EntityDetail.class);
        when(entityDetail.getGUID()).thenReturn(GUID);
        Optional<EntityDetail> optionalOfMockedEntity = Optional.of(entityDetail);
        when(dataEngineCommonHandler.findEntity(USER, QUALIFIED_NAME, ProcessPropertiesMapper.PROCESS_TYPE_NAME)).thenReturn(optionalOfMockedEntity);

        Optional<EntityDetail> result = processHandler.findProcessEntity(USER, QUALIFIED_NAME);

        assertEquals(GUID, result.get().getGUID());
    }

    @Test
    void findProcess_notExisting() throws UserNotAuthorizedException, PropertyServerException,
                                          InvalidParameterException {

        when(dataEngineCommonHandler.findEntity(USER, QUALIFIED_NAME, ProcessPropertiesMapper.PROCESS_TYPE_NAME)).thenReturn(Optional.empty());

        Optional<EntityDetail> result = processHandler.findProcessEntity(USER, QUALIFIED_NAME);

        assertFalse(result.isPresent());
    }

    @Test
    void addProcessPortRelationship() throws UserNotAuthorizedException, PropertyServerException,
                                             InvalidParameterException {
        processHandler.addProcessPortRelationship(USER, PROCESS_GUID, GUID, EXTERNAL_SOURCE_DE_QUALIFIED_NAME);

        verify(dataEngineCommonHandler, times(1)).createOrUpdateExternalRelationship(USER, PROCESS_GUID, GUID,
                ProcessPropertiesMapper.PROCESS_PORT_TYPE_NAME, ProcessPropertiesMapper.PROCESS_TYPE_NAME, EXTERNAL_SOURCE_DE_QUALIFIED_NAME, null);
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

        UserNotAuthorizedException mockedException = mockException(UserNotAuthorizedException.class, methodName);
        doThrow(mockedException).when(dataEngineCommonHandler).createOrUpdateExternalRelationship(USER, PROCESS_GUID, GUID,
                ProcessPropertiesMapper.PROCESS_PORT_TYPE_NAME, ProcessPropertiesMapper.PROCESS_TYPE_NAME, EXTERNAL_SOURCE_DE_QUALIFIED_NAME, null);

        UserNotAuthorizedException thrown = assertThrows(UserNotAuthorizedException.class, () ->
                processHandler.addProcessPortRelationship(USER, PROCESS_GUID, GUID, EXTERNAL_SOURCE_DE_QUALIFIED_NAME));

        assertTrue(thrown.getMessage().contains("OMAS-DATA-ENGINE-404-001 "));
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

    @Test
    void createOrUpdateProcessHierarchyRelationship() throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException {
        ParentProcess parentProcess = new ParentProcess();
        parentProcess.setProcessContainmentType(ProcessContainmentType.OWNED);
        parentProcess.setQualifiedName(PARENT_PROCESS_QUALIFIED_NAME);

        EntityDetail entityDetail = mock(EntityDetail.class);
        when(entityDetail.getGUID()).thenReturn(PARENT_GUID);
        Optional<EntityDetail> optionalOfMockedEntity = Optional.of(entityDetail);
        when(dataEngineCommonHandler.findEntity(USER, PARENT_PROCESS_QUALIFIED_NAME, ProcessPropertiesMapper.PROCESS_TYPE_NAME)).thenReturn(optionalOfMockedEntity);

        processHandler.createOrUpdateProcessHierarchyRelationship(USER, parentProcess, GUID, EXTERNAL_SOURCE_DE_QUALIFIED_NAME);

        verify(dataEngineCommonHandler, times(1)).createOrUpdateExternalRelationship(USER, PARENT_GUID, GUID,
                ProcessPropertiesMapper.PROCESS_HIERARCHY_TYPE_NAME, ProcessPropertiesMapper.PROCESS_TYPE_NAME, EXTERNAL_SOURCE_DE_QUALIFIED_NAME,
                null);
    }

    @Test
    void createOrUpdateProcessHierarchyRelationship_parentProcessNotFound() throws InvalidParameterException, PropertyServerException,
                                                                                             UserNotAuthorizedException {
        ParentProcess parentProcess = new ParentProcess();
        parentProcess.setProcessContainmentType(ProcessContainmentType.OWNED);
        parentProcess.setQualifiedName(PARENT_PROCESS_QUALIFIED_NAME);

        when(dataEngineCommonHandler.findEntity(USER, PARENT_PROCESS_QUALIFIED_NAME, ProcessPropertiesMapper.PROCESS_TYPE_NAME)).thenReturn(Optional.empty());

        processHandler.createOrUpdateProcessHierarchyRelationship(USER, parentProcess, GUID, EXTERNAL_SOURCE_DE_QUALIFIED_NAME);

        verify(dataEngineCommonHandler, times(1)).throwInvalidParameterException(DataEngineErrorCode.PROCESS_NOT_FOUND,
                "createOrUpdateProcessHierarchyRelationship", PARENT_PROCESS_QUALIFIED_NAME);
    }

    private void mockTypeDef(String typeName, String typeGUID) {
        TypeDef entityTypeDef = mock(TypeDef.class);
        when(repositoryHelper.getTypeDefByName(USER, typeName)).thenReturn(entityTypeDef);

        when(entityTypeDef.getName()).thenReturn(typeName);
        when(entityTypeDef.getGUID()).thenReturn(typeGUID);
    }

    private Process getProcess() {
        Process process = new Process();

        process.setQualifiedName(QUALIFIED_NAME);
        process.setName(NAME);
        process.setDisplayName(NAME);
        process.setDescription(DESCRIPTION);
        process.setLatestChange(LATEST_CHANGE);
        process.setFormula(FORMULA);
        process.setOwner(OWNER);
        process.setOwnerType(OwnerType.USER_ID);
        process.setUpdateSemantic(UpdateSemantic.REPLACE);

        return process;
    }
}