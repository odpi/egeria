/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.dataengine.server.handlers;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.odpi.openmetadata.accessservices.dataengine.ffdc.DataEngineErrorCode;
import org.odpi.openmetadata.accessservices.dataengine.model.DeleteSemantic;
import org.odpi.openmetadata.accessservices.dataengine.model.OwnerType;
import org.odpi.openmetadata.accessservices.dataengine.model.ParentProcess;
import org.odpi.openmetadata.accessservices.dataengine.model.Process;
import org.odpi.openmetadata.accessservices.dataengine.model.ProcessContainmentType;
import org.odpi.openmetadata.accessservices.dataengine.model.UpdateSemantic;
import org.odpi.openmetadata.accessservices.dataengine.server.builders.ProcessPropertiesBuilder;
import org.odpi.openmetadata.accessservices.dataengine.server.mappers.CommonMapper;
import org.odpi.openmetadata.commonservices.ffdc.InvalidParameterHandler;
import org.odpi.openmetadata.commonservices.generichandlers.AssetHandler;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.metadatasecurity.properties.Asset;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Classification;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetailDifferences;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceStatus;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceType;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.TypeDef;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;
import org.odpi.openmetadata.repositoryservices.ffdc.OMRSErrorCode;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.FunctionNotSupportedException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.TypeErrorException;

import java.lang.reflect.InvocationTargetException;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.odpi.openmetadata.accessservices.dataengine.server.util.MockedExceptionUtil.mockException;
import static org.odpi.openmetadata.commonservices.generichandlers.OpenMetadataAPIMapper.DISPLAY_NAME_PROPERTY_NAME;
import static org.odpi.openmetadata.commonservices.generichandlers.OpenMetadataAPIMapper.FORMULA_PROPERTY_NAME;
import static org.odpi.openmetadata.commonservices.generichandlers.OpenMetadataAPIMapper.PORT_IMPLEMENTATION_TYPE_NAME;
import static org.odpi.openmetadata.commonservices.generichandlers.OpenMetadataAPIMapper.PROCESS_HIERARCHY_TYPE_NAME;
import static org.odpi.openmetadata.commonservices.generichandlers.OpenMetadataAPIMapper.PROCESS_PORT_TYPE_GUID;
import static org.odpi.openmetadata.commonservices.generichandlers.OpenMetadataAPIMapper.PROCESS_PORT_TYPE_NAME;
import static org.odpi.openmetadata.commonservices.generichandlers.OpenMetadataAPIMapper.PROCESS_TYPE_GUID;
import static org.odpi.openmetadata.commonservices.generichandlers.OpenMetadataAPIMapper.PROCESS_TYPE_NAME;
import static org.odpi.openmetadata.commonservices.generichandlers.OpenMetadataAPIMapper.QUALIFIED_NAME_PROPERTY_NAME;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.WARN)
class DataEngineProcessHandlerTest {
    private static final String USER = "user";
    private static final String QUALIFIED_NAME = "qualifiedName";
    private static final String NAME = "name";
    private static final String DESCRIPTION = "desc";
    private static final String GUID = "guid";
    private static final String FORMULA = "formula";
    private static final String OWNER = "OWNER";
    private static final String PROCESS_GUID = "processGuid";
    private static final String PORT_IMPL_GUID = "portImplGUID";
    private static final String EXTERNAL_SOURCE_DE_GUID = "externalSourceDataEngineGuid";
    private static final String EXTERNAL_SOURCE_DE_QUALIFIED_NAME = "externalSourceDataEngineQualifiedName";
    private static final String PARENT_PROCESS_QUALIFIED_NAME = "parentQualifiedName";
    private static final String PARENT_GUID = "parentGUID";
    private static Map<String, Object> extendedProperties = createExtendedPropertiesMap();

    private static Map<String, Object> createExtendedPropertiesMap() {
        extendedProperties = new HashMap<>();
        extendedProperties.put(FORMULA_PROPERTY_NAME, FORMULA);
        extendedProperties.put(DISPLAY_NAME_PROPERTY_NAME, NAME);

        return extendedProperties;
    }

    @Mock
    private OMRSRepositoryHelper repositoryHelper;

    @Mock
    private InvalidParameterHandler invalidParameterHandler;

    @Mock
    private AssetHandler<Asset> assetHandler;

    @Mock
    private DataEngineCommonHandler dataEngineCommonHandler;

    @Mock
    private DataEngineRegistrationHandler registrationHandler;

    @Spy
    @InjectMocks
    private DataEngineProcessHandler processHandler;

    @Test
    void createProcess() throws UserNotAuthorizedException, PropertyServerException, InvalidParameterException {
        String methodName = "createProcess";

        Process process = getProcess();

        when(registrationHandler.getExternalDataEngine(USER, EXTERNAL_SOURCE_DE_QUALIFIED_NAME)).thenReturn(EXTERNAL_SOURCE_DE_GUID);

        when(assetHandler.createAssetInRepository(USER, EXTERNAL_SOURCE_DE_GUID, EXTERNAL_SOURCE_DE_QUALIFIED_NAME,
                process.getQualifiedName(), process.getName(), null, process.getDescription(), process.getZoneMembership(), process.getOwner(),
                process.getOwnerType().getOpenTypeOrdinal(), process.getOriginOrganizationGUID(),
                process.getOriginBusinessCapabilityGUID(), process.getOtherOriginValues(), process.getAdditionalProperties(),
                PROCESS_TYPE_GUID, PROCESS_TYPE_NAME, extendedProperties, null, null, InstanceStatus.DRAFT, null, methodName)).thenReturn(GUID);

        String result = processHandler.createProcess(USER, process, EXTERNAL_SOURCE_DE_QUALIFIED_NAME);

        assertEquals(GUID, result);
        verify(invalidParameterHandler, times(1)).validateUserId(USER, methodName);
        verify(invalidParameterHandler, times(1)).validateName(QUALIFIED_NAME, QUALIFIED_NAME_PROPERTY_NAME, methodName);
    }

    @Test
    void createProcess_throwsUserNotAuthorizedException() throws UserNotAuthorizedException, PropertyServerException,
                                                                 InvocationTargetException, NoSuchMethodException,
                                                                 InstantiationException,
                                                                 IllegalAccessException, InvalidParameterException {
        String methodName = "createProcess";
        Process process = getProcess();

        when(registrationHandler.getExternalDataEngine(USER, EXTERNAL_SOURCE_DE_QUALIFIED_NAME))
                .thenReturn(EXTERNAL_SOURCE_DE_GUID);

        UserNotAuthorizedException mockedException = mockException(UserNotAuthorizedException.class, methodName);
        doThrow(mockedException).when(assetHandler).createAssetInRepository(USER, EXTERNAL_SOURCE_DE_GUID, EXTERNAL_SOURCE_DE_QUALIFIED_NAME,
                process.getQualifiedName(), process.getName(), null, process.getDescription(), process.getZoneMembership(), process.getOwner(),
                process.getOwnerType().getOpenTypeOrdinal(), process.getOriginOrganizationGUID(),
                process.getOriginBusinessCapabilityGUID(), process.getOtherOriginValues(), process.getAdditionalProperties(),
                PROCESS_TYPE_GUID, PROCESS_TYPE_NAME, extendedProperties, null, null, InstanceStatus.DRAFT, null, methodName);

        UserNotAuthorizedException thrown = assertThrows(UserNotAuthorizedException.class, () ->
                processHandler.createProcess(USER, process, EXTERNAL_SOURCE_DE_QUALIFIED_NAME));

        assertTrue(thrown.getMessage().contains("OMAS-DATA-ENGINE-404-001 "));
    }

    @Test
    void updateProcess() throws UserNotAuthorizedException, PropertyServerException, InvalidParameterException {
        String methodName = "updateProcess";
        EntityDetail mockedOriginalProcessEntity = Mockito.mock(EntityDetail.class);
        ProcessPropertiesBuilder mockedBuilder = Mockito.mock(ProcessPropertiesBuilder.class);
        when(mockedOriginalProcessEntity.getGUID()).thenReturn(PROCESS_GUID);
        Process process = getProcess();
        doReturn(mockedBuilder).when(processHandler).getProcessPropertiesBuilder(process);
        EntityDetail mockedUpdatedProcessEntity = Mockito.mock(EntityDetail.class);
        when(dataEngineCommonHandler.buildEntityDetail(PROCESS_GUID, null)).thenReturn(mockedUpdatedProcessEntity);

        EntityDetailDifferences mockedDifferences = mock(EntityDetailDifferences.class);
        when(mockedDifferences.hasInstancePropertiesDifferences()).thenReturn(Boolean.TRUE);
        when(repositoryHelper.getEntityDetailDifferences(mockedOriginalProcessEntity, mockedUpdatedProcessEntity, true))
                .thenReturn(mockedDifferences);

        when(registrationHandler.getExternalDataEngine(USER, EXTERNAL_SOURCE_DE_QUALIFIED_NAME))
                .thenReturn(EXTERNAL_SOURCE_DE_GUID);

        processHandler.updateProcess(USER, mockedOriginalProcessEntity, process, EXTERNAL_SOURCE_DE_QUALIFIED_NAME);

        verify(assetHandler, times(1)).updateAsset(USER, EXTERNAL_SOURCE_DE_GUID, EXTERNAL_SOURCE_DE_QUALIFIED_NAME,
                                                   PROCESS_GUID, "processGUID", process.getQualifiedName(), process.getName(), null, process.getDescription(),
                                                   process.getAdditionalProperties(), PROCESS_TYPE_GUID, PROCESS_TYPE_NAME, extendedProperties,
                                                   null, null, true, false, false, null, methodName);

    }

    @Test
    void updateProcess_noChanges() throws UserNotAuthorizedException, PropertyServerException, InvalidParameterException, TypeErrorException {
        EntityDetail mockedOriginalProcessEntity = Mockito.mock(EntityDetail.class);
        when(mockedOriginalProcessEntity.getGUID()).thenReturn(PROCESS_GUID);
        Process process = getProcess();

        EntityDetail mockedUpdatedProcessEntity = Mockito.mock(EntityDetail.class);
        when(dataEngineCommonHandler.buildEntityDetail(PROCESS_GUID, null)).thenReturn(mockedUpdatedProcessEntity);

        EntityDetailDifferences mockedDifferences = mock(EntityDetailDifferences.class);
        when(mockedDifferences.hasInstancePropertiesDifferences()).thenReturn(Boolean.FALSE);
        when(repositoryHelper.getEntityDetailDifferences(mockedOriginalProcessEntity, mockedUpdatedProcessEntity, true))
                .thenReturn(mockedDifferences);

        Classification classification = new Classification();
        classification.setName("classificationName");
        when(repositoryHelper.getNewClassification(any(), any(), any(), any(), any(), any(), any(), any(), any(), any()))
                .thenReturn(classification);

        processHandler.updateProcess(USER, mockedOriginalProcessEntity, process, EXTERNAL_SOURCE_DE_QUALIFIED_NAME);

        verify(assetHandler, times(0)).updateAsset(USER, EXTERNAL_SOURCE_DE_GUID, EXTERNAL_SOURCE_DE_QUALIFIED_NAME,
                PROCESS_GUID, CommonMapper.GUID_PROPERTY_NAME, process.getQualifiedName(), process.getName(), null, process.getDescription(),
                process.getAdditionalProperties(), PROCESS_TYPE_GUID, PROCESS_TYPE_NAME, extendedProperties,
                null,null,  true,false, false, null, "updateProcess");
    }

    @Test
    void updateProcess_throwsUserNotAuthorizedException() throws UserNotAuthorizedException, PropertyServerException, InvocationTargetException,
                                                                 NoSuchMethodException, InstantiationException, IllegalAccessException,
                                                                 InvalidParameterException {
        String methodName = "updateProcess";
        Process process = getProcess();

        EntityDetail mockedOriginalProcessEntity = Mockito.mock(EntityDetail.class);
        when(mockedOriginalProcessEntity.getGUID()).thenReturn(PROCESS_GUID);

        ProcessPropertiesBuilder mockedBuilder = Mockito.mock(ProcessPropertiesBuilder.class);
        doReturn(mockedBuilder).when(processHandler).getProcessPropertiesBuilder(process);

        InstanceProperties updatedProcessProperties = new InstanceProperties();
        when(mockedBuilder.getInstanceProperties(methodName)).thenReturn(updatedProcessProperties);

        EntityDetail mockedUpdatedProcessEntity = Mockito.mock(EntityDetail.class);
        when(dataEngineCommonHandler.buildEntityDetail(PROCESS_GUID, updatedProcessProperties)).thenReturn(mockedUpdatedProcessEntity);

        EntityDetailDifferences mockedDifferences = mock(EntityDetailDifferences.class);
        when(mockedDifferences.hasInstancePropertiesDifferences()).thenReturn(Boolean.TRUE);
        when(repositoryHelper.getEntityDetailDifferences(mockedOriginalProcessEntity, mockedUpdatedProcessEntity, true))
                .thenReturn(mockedDifferences);

        when(registrationHandler.getExternalDataEngine(USER, EXTERNAL_SOURCE_DE_QUALIFIED_NAME))
                .thenReturn(EXTERNAL_SOURCE_DE_GUID);

        UserNotAuthorizedException mockedException = mockException(UserNotAuthorizedException.class, methodName);

        doThrow(mockedException).when(assetHandler).updateAsset(USER, EXTERNAL_SOURCE_DE_GUID, EXTERNAL_SOURCE_DE_QUALIFIED_NAME,
                PROCESS_GUID, "processGUID", process.getQualifiedName(), process.getName(), null, process.getDescription(),
                process.getAdditionalProperties(), PROCESS_TYPE_GUID, PROCESS_TYPE_NAME, extendedProperties, null, null,
                true,false, false, null, methodName);

        UserNotAuthorizedException thrown = assertThrows(UserNotAuthorizedException.class, () -> processHandler.updateProcess(USER,
                mockedOriginalProcessEntity, process, EXTERNAL_SOURCE_DE_QUALIFIED_NAME));

        assertTrue(thrown.getMessage().contains("OMAS-DATA-ENGINE-404-001 "));
    }

    @Test
    void findProcess() throws UserNotAuthorizedException, PropertyServerException, InvalidParameterException {
        EntityDetail entityDetail = mock(EntityDetail.class);
        when(entityDetail.getGUID()).thenReturn(GUID);
        Optional<EntityDetail> optionalOfMockedEntity = Optional.of(entityDetail);
        when(dataEngineCommonHandler.findEntity(USER, QUALIFIED_NAME, PROCESS_TYPE_NAME))
                .thenReturn(optionalOfMockedEntity);

        Optional<EntityDetail> result = processHandler.findProcessEntity(USER, QUALIFIED_NAME);

        assertTrue(result.isPresent());
        assertEquals(GUID, result.get().getGUID());
    }

    @Test
    void findProcess_notExisting() throws UserNotAuthorizedException, PropertyServerException,
                                          InvalidParameterException {

        when(dataEngineCommonHandler.findEntity(USER, QUALIFIED_NAME, PROCESS_TYPE_NAME)).thenReturn(Optional.empty());

        Optional<EntityDetail> result = processHandler.findProcessEntity(USER, QUALIFIED_NAME);

        assertFalse(result.isPresent());
    }

    @Test
    void updateProcessStatus() throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException {
        final String methodName = "updateProcessStatus";

        mockTypeDef(PROCESS_TYPE_NAME, PROCESS_TYPE_GUID);
        when(registrationHandler.getExternalDataEngine(USER, EXTERNAL_SOURCE_DE_QUALIFIED_NAME))
                .thenReturn(EXTERNAL_SOURCE_DE_GUID);

        processHandler.updateProcessStatus(USER, PROCESS_GUID, InstanceStatus.ACTIVE, EXTERNAL_SOURCE_DE_QUALIFIED_NAME);

        verify(invalidParameterHandler, times(1)).validateUserId(USER, methodName);
        verify(invalidParameterHandler, times(1)).validateGUID(PROCESS_GUID, "guid", methodName);

        verify(assetHandler, times(1)).updateBeanStatusInRepository(USER, EXTERNAL_SOURCE_DE_GUID,
                EXTERNAL_SOURCE_DE_QUALIFIED_NAME, PROCESS_GUID, "processGUID", PROCESS_TYPE_GUID, PROCESS_TYPE_NAME,
                false, false, InstanceStatus.ACTIVE,
                "processStatus", null, methodName);
    }

    @Test
    void getPortsForProcess() throws UserNotAuthorizedException, PropertyServerException, InvalidParameterException {

        mockTypeDef(PROCESS_PORT_TYPE_NAME, PROCESS_PORT_TYPE_GUID);

        EntityDetail portImplementation = mock(EntityDetail.class);
        InstanceType mockedTypeImpl = mock(InstanceType.class);
        when(mockedTypeImpl.getTypeDefName()).thenReturn(PORT_IMPLEMENTATION_TYPE_NAME);
        when(portImplementation.getType()).thenReturn(mockedTypeImpl);
        when(portImplementation.getGUID()).thenReturn(PORT_IMPL_GUID);

        Set<EntityDetail> portEntityGUIDs = new HashSet<>(List.of(portImplementation));
        when(dataEngineCommonHandler.getEntitiesForRelationship(USER, PROCESS_GUID, PROCESS_PORT_TYPE_NAME,
                PORT_IMPLEMENTATION_TYPE_NAME, PROCESS_TYPE_NAME)).thenReturn(portEntityGUIDs);
        Set<EntityDetail> result = processHandler.getPortsForProcess(USER, PROCESS_GUID, PORT_IMPLEMENTATION_TYPE_NAME);
        assertEquals(2, result.size());
        assertTrue(result.contains(portImplementation));
    }

    @Test
    void upsertProcessHierarchyRelationship() throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException {
        ParentProcess parentProcess = new ParentProcess();
        parentProcess.setProcessContainmentType(ProcessContainmentType.OWNED);
        parentProcess.setQualifiedName(PARENT_PROCESS_QUALIFIED_NAME);

        EntityDetail entityDetail = mock(EntityDetail.class);
        when(entityDetail.getGUID()).thenReturn(PARENT_GUID);
        Optional<EntityDetail> optionalOfMockedEntity = Optional.of(entityDetail);
        when(dataEngineCommonHandler.findEntity(USER, PARENT_PROCESS_QUALIFIED_NAME, PROCESS_TYPE_NAME)).thenReturn(optionalOfMockedEntity);

        processHandler.upsertProcessHierarchyRelationship(USER, parentProcess, GUID, EXTERNAL_SOURCE_DE_QUALIFIED_NAME);

        verify(dataEngineCommonHandler, times(1)).upsertExternalRelationship(USER, PARENT_GUID, GUID,
                PROCESS_HIERARCHY_TYPE_NAME, PROCESS_TYPE_NAME, PROCESS_TYPE_NAME, EXTERNAL_SOURCE_DE_QUALIFIED_NAME,
                null);
    }

    @Test
    void upsertProcessHierarchyRelationship_parentProcessNotFound() throws InvalidParameterException, PropertyServerException,
                                                                           UserNotAuthorizedException {
        ParentProcess parentProcess = new ParentProcess();
        parentProcess.setProcessContainmentType(ProcessContainmentType.OWNED);
        parentProcess.setQualifiedName(PARENT_PROCESS_QUALIFIED_NAME);

        when(dataEngineCommonHandler.findEntity(USER, PARENT_PROCESS_QUALIFIED_NAME, PROCESS_TYPE_NAME)).thenReturn(Optional.empty());

        processHandler.upsertProcessHierarchyRelationship(USER, parentProcess, GUID, EXTERNAL_SOURCE_DE_QUALIFIED_NAME);

        String methodName = "upsertProcessHierarchyRelationship";
        verify(dataEngineCommonHandler, times(1)).throwInvalidParameterException(DataEngineErrorCode.PROCESS_NOT_FOUND,
                methodName, PARENT_PROCESS_QUALIFIED_NAME);
    }

    @Test
    void removeProcess() throws UserNotAuthorizedException, PropertyServerException, InvalidParameterException, FunctionNotSupportedException {
        when(registrationHandler.getExternalDataEngine(USER, EXTERNAL_SOURCE_DE_QUALIFIED_NAME)).thenReturn(EXTERNAL_SOURCE_DE_GUID);

        processHandler.removeProcess(USER, PROCESS_GUID, EXTERNAL_SOURCE_DE_QUALIFIED_NAME, DeleteSemantic.SOFT);

        verify(assetHandler, times(1)).deleteBeanInRepository(USER, EXTERNAL_SOURCE_DE_GUID, EXTERNAL_SOURCE_DE_QUALIFIED_NAME,
                PROCESS_GUID, "processGUID", PROCESS_TYPE_GUID, PROCESS_TYPE_NAME, null, null, false, false, null, "removeProcess");
    }


    @Test
    void removeProcess_throwsFunctionNotSupportedException() throws FunctionNotSupportedException {
        FunctionNotSupportedException mockedException = new FunctionNotSupportedException(
                OMRSErrorCode.METHOD_NOT_IMPLEMENTED.getMessageDefinition("removeProcess", this.getClass().getName(),
                        "server"), this.getClass().getName(), "removeProcess");
        doThrow(mockedException).when(dataEngineCommonHandler).validateDeleteSemantic(DeleteSemantic.HARD, "removeProcess");

        assertThrows(FunctionNotSupportedException.class, () ->
                processHandler.removeProcess(USER, PROCESS_GUID, EXTERNAL_SOURCE_DE_QUALIFIED_NAME, DeleteSemantic.HARD));
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
        process.setFormula(FORMULA);
        process.setOwner(OWNER);
        process.setOwnerType(OwnerType.USER_ID);
        process.setUpdateSemantic(UpdateSemantic.REPLACE);
        process.setZoneMembership(Collections.singletonList("default"));

        return process;
    }
}