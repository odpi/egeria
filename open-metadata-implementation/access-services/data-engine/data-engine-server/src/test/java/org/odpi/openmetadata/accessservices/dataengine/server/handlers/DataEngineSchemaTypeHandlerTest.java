/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.dataengine.server.handlers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.odpi.openmetadata.accessservices.dataengine.ffdc.DataEngineErrorCode;
import org.odpi.openmetadata.accessservices.dataengine.model.Attribute;
import org.odpi.openmetadata.accessservices.dataengine.model.DeleteSemantic;
import org.odpi.openmetadata.accessservices.dataengine.model.SchemaType;
import org.odpi.openmetadata.commonservices.ffdc.InvalidParameterHandler;
import org.odpi.openmetadata.commonservices.generichandlers.SchemaTypeBuilder;
import org.odpi.openmetadata.commonservices.generichandlers.SchemaTypeHandler;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetailDifferences;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceType;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.TypeDef;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;
import org.odpi.openmetadata.repositoryservices.ffdc.OMRSErrorCode;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.FunctionNotSupportedException;

import java.lang.reflect.InvocationTargetException;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.odpi.openmetadata.accessservices.dataengine.server.handlers.DataEngineSchemaTypeHandler.SCHEMA_TYPE_GUID_PARAMETER_NAME;
import static org.odpi.openmetadata.accessservices.dataengine.server.util.MockedExceptionUtil.mockException;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.WARN)
class DataEngineSchemaTypeHandlerTest {
    private static final String USER = "user";
    private static final String QUALIFIED_NAME = "qualifiedName";
    private static final String NAME = "name";
    private static final String VERSION = "version";
    private static final String GUID = "guid";
    private static final String AUTHOR = "author";
    private static final String USAGE = "usage";
    private static final String ENCODING_STANDARD = "encodingStandard";
    private static final String ATTRIBUTE_QUALIFIED_NAME = "attributeQualifiedName";
    private static final String ATTRIBUTE_DISPLAY_NAME = "attributeName";
    private static final String ATTRIBUTE_GUID = "attributeGuid";
    private static final String SOURCE_GUID = "sourceGuid";
    private static final String SOURCE_QUALIFIED_NAME = "sourceQualifiedName";
    private static final String SOURCE_TYPE = "sourceType";
    private static final String TARGET_TYPE = "targetType";
    private static final String TARGET_GUID = "targetGuid";
    private static final String TARGET_QUALIFIED_NAME = "targetQualifiedName";
    private static final String EXTERNAL_SOURCE_DE_GUID = "externalSourceDataEngineGuid";
    private static final String EXTERNAL_SOURCE_DE_QUALIFIED_NAME = "externalSourceDataEngineQualifiedName";

    @Mock
    private OMRSRepositoryHelper repositoryHelper;

    @Mock
    private InvalidParameterHandler invalidParameterHandler;

    @Mock
    private SchemaTypeHandler<SchemaType> schemaTypeHandler;

    @Mock
    private DataEngineRegistrationHandler dataEngineRegistrationHandler;

    @Mock
    private DataEngineCommonHandler dataEngineCommonHandler;

    @Mock
    private DataEngineSchemaAttributeHandler dataEngineSchemaAttributeHandler;

    @Spy
    @InjectMocks
    private DataEngineSchemaTypeHandler dataEngineSchemaTypeHandler;

    private final Attribute attribute = getAttribute();

    private final List<Attribute> attributeList = Collections.singletonList(attribute);

    @BeforeEach
    void setup() throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException {
        when(dataEngineRegistrationHandler.getExternalDataEngine(USER, EXTERNAL_SOURCE_DE_QUALIFIED_NAME)).thenReturn(EXTERNAL_SOURCE_DE_GUID);
    }

    @Test
    void createSchemaTypeWithSchemaAttribute() throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException {
        String methodName = "upsertSchemaType";

        when(dataEngineCommonHandler.findEntity(USER, QUALIFIED_NAME, OpenMetadataType.SCHEMA_TYPE_TYPE_NAME)).thenReturn(Optional.empty());

        SchemaType schemaType = getSchemaType();
        SchemaTypeBuilder schemaTypeBuilder = getSchemaTypeBuilder(schemaType);
        doReturn(schemaTypeBuilder).when(dataEngineSchemaTypeHandler).getSchemaTypeBuilder(schemaType);
        when(schemaTypeHandler.addSchemaType(USER, EXTERNAL_SOURCE_DE_GUID, EXTERNAL_SOURCE_DE_QUALIFIED_NAME,
                schemaTypeBuilder, null, null, false, false, null,
                methodName)).thenReturn(GUID);

        mockTypeDef(OpenMetadataType.TYPE_EMBEDDED_ATTRIBUTE_CLASSIFICATION_TYPE_NAME, OpenMetadataType.TYPE_TO_ATTRIBUTE_RELATIONSHIP_TYPE_GUID);

        String result = dataEngineSchemaTypeHandler.upsertSchemaType(USER, schemaType, null, EXTERNAL_SOURCE_DE_QUALIFIED_NAME);

        assertEquals(GUID, result);
        verify(invalidParameterHandler, times(1)).validateUserId(USER, methodName);
        verify(invalidParameterHandler, times(1)).validateName(QUALIFIED_NAME, OpenMetadataProperty.QUALIFIED_NAME.name, methodName);
        verify(invalidParameterHandler, times(1)).validateName(NAME, OpenMetadataProperty.DISPLAY_NAME.name, methodName);
        verify(dataEngineSchemaAttributeHandler, times(1)).upsertSchemaAttributes(USER, attributeList,
                EXTERNAL_SOURCE_DE_QUALIFIED_NAME, EXTERNAL_SOURCE_DE_GUID, GUID);
    }

    @Test
    void updateSchemaTypeWithSchemaAttribute() throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException {
        String methodName = "upsertSchemaType";

        when(dataEngineRegistrationHandler.getExternalDataEngine(USER, EXTERNAL_SOURCE_DE_QUALIFIED_NAME)).thenReturn(EXTERNAL_SOURCE_DE_GUID);

        SchemaType schemaType = getSchemaType();
        SchemaTypeBuilder schemaTypeBuilder = getSchemaTypeBuilder(schemaType);
        doReturn(schemaTypeBuilder).when(dataEngineSchemaTypeHandler).getSchemaTypeBuilder(schemaType);

        EntityDetail schemaTypeEntity = mockFindEntity(QUALIFIED_NAME, GUID, SOURCE_TYPE, OpenMetadataType.SCHEMA_TYPE_TYPE_NAME);

        EntityDetail schemaTypeUpdatedEntity = mock(EntityDetail.class);
        when(dataEngineCommonHandler.buildEntityDetail(GUID, null)).thenReturn(schemaTypeUpdatedEntity);
        EntityDetailDifferences schemaTypeDifferences = mock(EntityDetailDifferences.class);
        when((schemaTypeDifferences.hasInstancePropertiesDifferences())).thenReturn(Boolean.TRUE);
        when(repositoryHelper.getEntityDetailDifferences(schemaTypeEntity, schemaTypeUpdatedEntity, true))
                .thenReturn(schemaTypeDifferences);

        when(dataEngineRegistrationHandler.getExternalDataEngine(USER, EXTERNAL_SOURCE_DE_QUALIFIED_NAME))
                .thenReturn(EXTERNAL_SOURCE_DE_GUID);

        String result = dataEngineSchemaTypeHandler.upsertSchemaType(USER, schemaType, null, EXTERNAL_SOURCE_DE_QUALIFIED_NAME);

        assertEquals(GUID, result);
        verify(invalidParameterHandler, times(1)).validateUserId(USER, methodName);
        verify(invalidParameterHandler, times(1)).validateName(QUALIFIED_NAME,
                OpenMetadataProperty.QUALIFIED_NAME.name, methodName);
        verify(invalidParameterHandler, times(1)).validateName(NAME,
                OpenMetadataProperty.DISPLAY_NAME.name, methodName);
        verify(schemaTypeHandler, times(1)).updateSchemaType(USER, EXTERNAL_SOURCE_DE_GUID,
                EXTERNAL_SOURCE_DE_QUALIFIED_NAME, GUID, SCHEMA_TYPE_GUID_PARAMETER_NAME, schemaTypeBuilder, true,
                false, false, null, methodName);
        verify(dataEngineSchemaAttributeHandler, times(1)).upsertSchemaAttributes(USER, attributeList,
                EXTERNAL_SOURCE_DE_QUALIFIED_NAME, EXTERNAL_SOURCE_DE_GUID, GUID);
    }

    @Test
    void createSchemaType_throwsUserNotAuthorizedException() throws InvalidParameterException,
                                                                    PropertyServerException,
                                                                    UserNotAuthorizedException,
                                                                    InvocationTargetException,
                                                                    NoSuchMethodException,
                                                                    InstantiationException,
                                                                    IllegalAccessException {
        String methodName = "upsertSchemaType";

        SchemaType schemaType = getSchemaType();
        SchemaTypeBuilder schemaTypeBuilder = getSchemaTypeBuilder(schemaType);

        when(dataEngineRegistrationHandler.getExternalDataEngine(USER, EXTERNAL_SOURCE_DE_QUALIFIED_NAME)).thenReturn(EXTERNAL_SOURCE_DE_GUID);

        when(dataEngineCommonHandler.findEntity(USER, QUALIFIED_NAME, OpenMetadataType.SCHEMA_TYPE_TYPE_NAME)).thenReturn(Optional.empty());

        doReturn(schemaTypeBuilder).when(dataEngineSchemaTypeHandler).getSchemaTypeBuilder(schemaType);

        UserNotAuthorizedException mockedException = mockException(UserNotAuthorizedException.class, methodName);
        when(schemaTypeHandler.addSchemaType(USER, EXTERNAL_SOURCE_DE_GUID, EXTERNAL_SOURCE_DE_QUALIFIED_NAME, schemaTypeBuilder, null,
                null, false, false, null,
                methodName)).thenThrow(mockedException);
        UserNotAuthorizedException thrown = assertThrows(UserNotAuthorizedException.class, () ->
                dataEngineSchemaTypeHandler.upsertSchemaType(USER, getSchemaType(), null, EXTERNAL_SOURCE_DE_QUALIFIED_NAME));

        assertTrue(thrown.getMessage().contains("OMAS-DATA-ENGINE-404-001 "));
    }

    @Test
    void addDataFlowRelationship() throws UserNotAuthorizedException, PropertyServerException,
                                                InvalidParameterException {
        mockFindEntity(SOURCE_QUALIFIED_NAME, SOURCE_GUID, SOURCE_TYPE, OpenMetadataType.REFERENCEABLE.typeName);
        mockFindEntity(TARGET_QUALIFIED_NAME, TARGET_GUID, TARGET_TYPE, OpenMetadataType.REFERENCEABLE.typeName);

        dataEngineSchemaTypeHandler.addDataFlowRelationship(USER, SOURCE_QUALIFIED_NAME, TARGET_QUALIFIED_NAME,
                EXTERNAL_SOURCE_DE_QUALIFIED_NAME, null, null);

        verify(dataEngineCommonHandler, times(1)).upsertExternalRelationship(USER, SOURCE_GUID, TARGET_GUID,
                                                                             OpenMetadataType.DATA_FLOW_TYPE_NAME, SOURCE_TYPE, TARGET_TYPE, EXTERNAL_SOURCE_DE_QUALIFIED_NAME, null);
    }

    @Test
    void addDataFlowRelationship_throwsUserNotAuthorizedException() throws UserNotAuthorizedException,
                                                                                 PropertyServerException,
                                                                                 InvocationTargetException,
                                                                                 NoSuchMethodException,
                                                                                 InstantiationException,
                                                                                 IllegalAccessException,
                                                                                 InvalidParameterException {
        final String methodName = "addDataFlowRelationship";

        mockFindEntity(SOURCE_QUALIFIED_NAME, SOURCE_GUID, SOURCE_TYPE, OpenMetadataType.REFERENCEABLE.typeName);
        mockFindEntity(TARGET_QUALIFIED_NAME, TARGET_GUID, TARGET_TYPE, OpenMetadataType.REFERENCEABLE.typeName);

        UserNotAuthorizedException mockedException = mockException(UserNotAuthorizedException.class, methodName);
        doThrow(mockedException).when(dataEngineCommonHandler).upsertExternalRelationship(USER, SOURCE_GUID, TARGET_GUID,
                                                                                          OpenMetadataType.DATA_FLOW_TYPE_NAME,
                                                                                          SOURCE_TYPE,
                                                                                          TARGET_TYPE,
                                                                                          EXTERNAL_SOURCE_DE_QUALIFIED_NAME, null);

        UserNotAuthorizedException thrown = assertThrows(UserNotAuthorizedException.class, () ->
                dataEngineSchemaTypeHandler.addDataFlowRelationship(USER, SOURCE_QUALIFIED_NAME, TARGET_QUALIFIED_NAME,
                        EXTERNAL_SOURCE_DE_QUALIFIED_NAME, null, null));

        assertTrue(thrown.getMessage().contains("OMAS-DATA-ENGINE-404-001 "));
    }

    @Test
    void addDataFlowRelationship_throwsInvalidParameterException() throws UserNotAuthorizedException,
                                                                                PropertyServerException,
                                                                                InvalidParameterException {
        when(dataEngineCommonHandler.findEntity(USER, TARGET_QUALIFIED_NAME, OpenMetadataType.SCHEMA_ATTRIBUTE.typeName)).thenReturn(Optional.empty());

        dataEngineSchemaTypeHandler.addDataFlowRelationship(USER, SOURCE_QUALIFIED_NAME, TARGET_QUALIFIED_NAME,
                EXTERNAL_SOURCE_DE_QUALIFIED_NAME, null, null);

        verify(dataEngineCommonHandler, times(1)).throwInvalidParameterException(DataEngineErrorCode.REFERENCEABLE_NOT_FOUND,
                "addDataFlowRelationship", SOURCE_QUALIFIED_NAME);
    }

    @Test
    void removeSchemaType() throws UserNotAuthorizedException, PropertyServerException, InvalidParameterException, FunctionNotSupportedException {
        EntityDetail mockedEntity = mock(EntityDetail.class);
        when(mockedEntity.getGUID()).thenReturn(GUID);
        when(dataEngineCommonHandler.findEntity(USER, QUALIFIED_NAME, OpenMetadataType.SCHEMA_TYPE_TYPE_NAME)).thenReturn(Optional.of(mockedEntity));

        //mock getSchemaAttributesForSchemaType
        mockTypeDef(OpenMetadataType.TYPE_TO_ATTRIBUTE_RELATIONSHIP_TYPE_NAME,
                    OpenMetadataType.TYPE_TO_ATTRIBUTE_RELATIONSHIP_TYPE_GUID);
        EntityDetail entityDetail = mock(EntityDetail.class);
        when(entityDetail.getGUID()).thenReturn(ATTRIBUTE_GUID);
        Set<EntityDetail> entityDetails = new HashSet<>();
        entityDetails.add(entityDetail);
        when(dataEngineCommonHandler.getEntitiesForRelationship(USER, GUID, OpenMetadataType.TYPE_TO_ATTRIBUTE_RELATIONSHIP_TYPE_NAME,
                                                                OpenMetadataType.SCHEMA_ATTRIBUTE.typeName, OpenMetadataType.SCHEMA_TYPE_TYPE_NAME)).thenReturn(entityDetails);

        dataEngineSchemaTypeHandler.removeSchemaType(USER, GUID, EXTERNAL_SOURCE_DE_QUALIFIED_NAME, DeleteSemantic.SOFT);
        verify(dataEngineCommonHandler, times(1)).removeEntity(USER, GUID,
                                                               OpenMetadataType.TABULAR_SCHEMA_TYPE_TYPE_NAME, EXTERNAL_SOURCE_DE_QUALIFIED_NAME);
        verify(dataEngineCommonHandler, times(1)).removeEntity(USER, ATTRIBUTE_GUID,
                                                               OpenMetadataType.TABULAR_COLUMN_TYPE_NAME, EXTERNAL_SOURCE_DE_QUALIFIED_NAME);

    }

    @Test
    void removeSchemaType_throwsFunctionNotSupportedException() throws FunctionNotSupportedException {
       FunctionNotSupportedException mockedException = new FunctionNotSupportedException(
                OMRSErrorCode.METHOD_NOT_IMPLEMENTED.getMessageDefinition("removeSchemaType", this.getClass().getName(),
                        "server"), this.getClass().getName(), "removeSchemaType");
        doThrow(mockedException).when(dataEngineCommonHandler).validateDeleteSemantic(DeleteSemantic.HARD, "removeSchemaType");

        assertThrows(FunctionNotSupportedException.class, () ->
                dataEngineSchemaTypeHandler.removeSchemaType(USER, GUID, EXTERNAL_SOURCE_DE_QUALIFIED_NAME, DeleteSemantic.HARD));
    }

    private EntityDetail mockFindEntity(String qualifiedName, String guid, String entityType, String entityTypeName)
            throws UserNotAuthorizedException, PropertyServerException, InvalidParameterException {

        InstanceType type = mock(InstanceType.class);
        when(type.getTypeDefName()).thenReturn(entityType);
        EntityDetail entityDetail = mock(EntityDetail.class);
        when(entityDetail.getGUID()).thenReturn(guid);
        when(entityDetail.getType()).thenReturn(type);
        Optional<EntityDetail> optionalOfMockedEntity = Optional.of(entityDetail);
        when(dataEngineCommonHandler.findEntity(USER, qualifiedName, entityTypeName)).thenReturn(optionalOfMockedEntity);

        return entityDetail;
    }

    private void mockTypeDef(String typeName, String typeGUID) {
        TypeDef entityTypeDef = mock(TypeDef.class);
        when(repositoryHelper.getTypeDefByName(USER, typeName)).thenReturn(entityTypeDef);

        when(entityTypeDef.getName()).thenReturn(typeName);
        when(entityTypeDef.getGUID()).thenReturn(typeGUID);
    }

    private Attribute getAttribute() {
        Attribute attribute = new Attribute();
        attribute.setQualifiedName(ATTRIBUTE_QUALIFIED_NAME);
        attribute.setDisplayName(ATTRIBUTE_DISPLAY_NAME);
        attribute.setMinCardinality(1);
        attribute.setMaxCardinality(1);
        attribute.setPosition(1);

        return attribute;
    }

    private SchemaType getSchemaType() {
        SchemaType schemaType = new SchemaType();

        schemaType.setQualifiedName(QUALIFIED_NAME);
        schemaType.setDisplayName(NAME);
        schemaType.setAuthor(AUTHOR);
        schemaType.setUsage(USAGE);
        schemaType.setEncodingStandard(ENCODING_STANDARD);
        schemaType.setVersionNumber(VERSION);

        schemaType.setAttributeList(attributeList);

        return schemaType;
    }

    SchemaTypeBuilder getSchemaTypeBuilder(SchemaType schemaType) {
        return new SchemaTypeBuilder(schemaType.getQualifiedName(), schemaType.getDisplayName(), null,
                schemaType.getVersionNumber(), false, schemaType.getAuthor(), schemaType.getUsage(),
                schemaType.getEncodingStandard(), null, null,
                                     OpenMetadataType.TABULAR_SCHEMA_TYPE_TYPE_GUID,
                                     OpenMetadataType.TABULAR_SCHEMA_TYPE_TYPE_NAME,
                                     null, repositoryHelper, "serviceName", "serverName");
    }
}