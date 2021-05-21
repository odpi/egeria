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
import org.odpi.openmetadata.commonservices.generichandlers.SchemaAttributeBuilder;
import org.odpi.openmetadata.commonservices.generichandlers.SchemaAttributeHandler;
import org.odpi.openmetadata.commonservices.generichandlers.SchemaTypeBuilder;
import org.odpi.openmetadata.commonservices.generichandlers.SchemaTypeHandler;
import org.odpi.openmetadata.commonservices.repositoryhandler.RepositoryHandler;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.SchemaAttribute;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Classification;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.ClassificationOrigin;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetailDifferences;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProvenanceType;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.TypeDef;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.FunctionNotSupportedException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.TypeErrorException;

import java.lang.reflect.InvocationTargetException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

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
import static org.odpi.openmetadata.commonservices.generichandlers.OpenMetadataAPIMapper.DISPLAY_NAME_PROPERTY_NAME;
import static org.odpi.openmetadata.commonservices.generichandlers.OpenMetadataAPIMapper.LINEAGE_MAPPING_TYPE_NAME;
import static org.odpi.openmetadata.commonservices.generichandlers.OpenMetadataAPIMapper.QUALIFIED_NAME_PROPERTY_NAME;
import static org.odpi.openmetadata.commonservices.generichandlers.OpenMetadataAPIMapper.SCHEMA_ATTRIBUTE_TYPE_NAME;
import static org.odpi.openmetadata.commonservices.generichandlers.OpenMetadataAPIMapper.SCHEMA_TYPE_TYPE_NAME;
import static org.odpi.openmetadata.commonservices.generichandlers.OpenMetadataAPIMapper.TABULAR_COLUMN_TYPE_GUID;
import static org.odpi.openmetadata.commonservices.generichandlers.OpenMetadataAPIMapper.TABULAR_COLUMN_TYPE_NAME;
import static org.odpi.openmetadata.commonservices.generichandlers.OpenMetadataAPIMapper.TABULAR_SCHEMA_TYPE_TYPE_GUID;
import static org.odpi.openmetadata.commonservices.generichandlers.OpenMetadataAPIMapper.TABULAR_SCHEMA_TYPE_TYPE_NAME;
import static org.odpi.openmetadata.commonservices.generichandlers.OpenMetadataAPIMapper.TYPE_EMBEDDED_ATTRIBUTE_CLASSIFICATION_TYPE_NAME;
import static org.odpi.openmetadata.commonservices.generichandlers.OpenMetadataAPIMapper.TYPE_TO_ATTRIBUTE_RELATIONSHIP_TYPE_GUID;
import static org.odpi.openmetadata.commonservices.generichandlers.OpenMetadataAPIMapper.TYPE_TO_ATTRIBUTE_RELATIONSHIP_TYPE_NAME;

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
    private static final String TARGET_GUID = "targetGuid";
    private static final String TARGET_QUALIFIED_NAME = "targetQualifiedName";
    private static final String EXTERNAL_SOURCE_DE_GUID = "externalSourceDataEngineGuid";
    private static final String EXTERNAL_SOURCE_DE_QUALIFIED_NAME = "externalSourceDataEngineQualifiedName";

    @Mock
    private RepositoryHandler repositoryHandler;

    @Mock
    private OMRSRepositoryHelper repositoryHelper;

    @Mock
    private InvalidParameterHandler invalidParameterHandler;

    @Mock
    private SchemaTypeHandler<SchemaType> schemaTypeHandler;

    @Mock
    private SchemaAttributeHandler<SchemaAttribute, SchemaType> schemaAttributeHandler;

    @Mock
    private DataEngineRegistrationHandler dataEngineRegistrationHandler;

    @Mock
    private DataEngineCommonHandler dataEngineCommonHandler;

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
    void createSchemaTypeWithSchemaAttribute() throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException,
                                                      TypeErrorException {
        String methodName = "upsertSchemaType";

        when(dataEngineCommonHandler.findEntity(USER, QUALIFIED_NAME, SCHEMA_TYPE_TYPE_NAME))
                .thenReturn(Optional.empty());

        SchemaType schemaType = getSchemaType();
        SchemaTypeBuilder schemaTypeBuilder = getSchemaTypeBuilder(schemaType);
        doReturn(schemaTypeBuilder).when(dataEngineSchemaTypeHandler).getSchemaTypeBuilder(schemaType);
        when(schemaTypeHandler.addSchemaType(USER, EXTERNAL_SOURCE_DE_GUID, EXTERNAL_SOURCE_DE_QUALIFIED_NAME,
                schemaTypeBuilder, methodName)).thenReturn(GUID);

        SchemaAttributeBuilder schemaAttributeBuilder = getSchemaAttributePropertiesBuilder(attribute);
        doReturn(schemaAttributeBuilder).when(dataEngineSchemaTypeHandler).getSchemaAttributeBuilder(attribute);
        when(dataEngineCommonHandler.findEntity(USER, ATTRIBUTE_QUALIFIED_NAME, SCHEMA_ATTRIBUTE_TYPE_NAME)).thenReturn(Optional.empty());
        mockTypeDef(TYPE_EMBEDDED_ATTRIBUTE_CLASSIFICATION_TYPE_NAME, TYPE_TO_ATTRIBUTE_RELATIONSHIP_TYPE_GUID);


        final String schemaTypeGUIDParameterName = "schemaTypeGUID";
        final String qualifiedNameParameterName = "schemaAttribute.getQualifiedName()";
        when(schemaAttributeHandler.createNestedSchemaAttribute(USER, EXTERNAL_SOURCE_DE_GUID, EXTERNAL_SOURCE_DE_QUALIFIED_NAME,
                GUID, schemaTypeGUIDParameterName, TABULAR_SCHEMA_TYPE_TYPE_NAME, TYPE_TO_ATTRIBUTE_RELATIONSHIP_TYPE_GUID,
                TYPE_TO_ATTRIBUTE_RELATIONSHIP_TYPE_NAME, attribute.getQualifiedName(), qualifiedNameParameterName, schemaAttributeBuilder,
                "createSchemaAttribute"))
                .thenReturn(ATTRIBUTE_GUID);

        Classification classification = new Classification();
        classification.setName("classificationName");
        when(repositoryHelper.getNewClassification("serviceName", null, null,
                InstanceProvenanceType.LOCAL_COHORT, USER, TYPE_EMBEDDED_ATTRIBUTE_CLASSIFICATION_TYPE_NAME, TABULAR_COLUMN_TYPE_NAME,
                ClassificationOrigin.ASSIGNED, null,
                schemaTypeBuilder.getTypeEmbeddedInstanceProperties(TYPE_EMBEDDED_ATTRIBUTE_CLASSIFICATION_TYPE_NAME)))
                .thenReturn(classification);

        String result = dataEngineSchemaTypeHandler.upsertSchemaType(USER, schemaType, EXTERNAL_SOURCE_DE_QUALIFIED_NAME);

        assertEquals(GUID, result);
        verify(invalidParameterHandler, times(1)).validateUserId(USER, methodName);
        verify(invalidParameterHandler, times(1)).validateName(QUALIFIED_NAME, QUALIFIED_NAME_PROPERTY_NAME, methodName);
        verify(invalidParameterHandler, times(1)).validateName(NAME, DISPLAY_NAME_PROPERTY_NAME, methodName);
        verify(schemaAttributeHandler, times(1)).createNestedSchemaAttribute(USER, EXTERNAL_SOURCE_DE_GUID, EXTERNAL_SOURCE_DE_QUALIFIED_NAME,
                GUID, schemaTypeGUIDParameterName, TABULAR_SCHEMA_TYPE_TYPE_NAME,
                TYPE_TO_ATTRIBUTE_RELATIONSHIP_TYPE_GUID, TYPE_TO_ATTRIBUTE_RELATIONSHIP_TYPE_NAME,
                attribute.getQualifiedName(), qualifiedNameParameterName, schemaAttributeBuilder, "createSchemaAttribute");
    }

    @Test
    void updateSchemaTypeWithSchemaAttribute() throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException,
                                                      TypeErrorException {
        String methodName = "upsertSchemaType";

        when(dataEngineRegistrationHandler.getExternalDataEngine(USER, EXTERNAL_SOURCE_DE_QUALIFIED_NAME))
                .thenReturn(EXTERNAL_SOURCE_DE_GUID);

        SchemaType schemaType = getSchemaType();
        SchemaTypeBuilder schemaTypeBuilder = getSchemaTypeBuilder(schemaType);
        doReturn(schemaTypeBuilder).when(dataEngineSchemaTypeHandler).getSchemaTypeBuilder(schemaType);

        EntityDetail schemaTypeEntity = mockFindEntity(QUALIFIED_NAME, GUID, SCHEMA_TYPE_TYPE_NAME);

        EntityDetail schemaTypeUpdatedEntity = mock(EntityDetail.class);
        when(dataEngineCommonHandler.buildEntityDetail(GUID, null)).thenReturn(schemaTypeUpdatedEntity);
        EntityDetailDifferences schemaTypeDifferences = mock(EntityDetailDifferences.class);
        when((schemaTypeDifferences.hasInstancePropertiesDifferences())).thenReturn(Boolean.TRUE);
        when(repositoryHelper.getEntityDetailDifferences(schemaTypeEntity, schemaTypeUpdatedEntity, true))
                .thenReturn(schemaTypeDifferences);

        SchemaAttributeBuilder schemaAttributeBuilder = getSchemaAttributePropertiesBuilder(attribute);
        doReturn(schemaAttributeBuilder).when(dataEngineSchemaTypeHandler).getSchemaAttributeBuilder(attribute);
        when(dataEngineCommonHandler.findEntity(USER, ATTRIBUTE_QUALIFIED_NAME, SCHEMA_ATTRIBUTE_TYPE_NAME))
                .thenReturn(Optional.empty());
        mockTypeDef(TYPE_EMBEDDED_ATTRIBUTE_CLASSIFICATION_TYPE_NAME, TYPE_TO_ATTRIBUTE_RELATIONSHIP_TYPE_GUID);

        final String schemaTypeGUIDParameterName = "schemaTypeGUID";
        final String qualifiedNameParameterName = "schemaAttribute.getQualifiedName()";
        when(schemaAttributeHandler.createNestedSchemaAttribute(USER, EXTERNAL_SOURCE_DE_GUID, EXTERNAL_SOURCE_DE_QUALIFIED_NAME,
                GUID, schemaTypeGUIDParameterName, TABULAR_SCHEMA_TYPE_TYPE_NAME,
                TYPE_TO_ATTRIBUTE_RELATIONSHIP_TYPE_GUID, TYPE_TO_ATTRIBUTE_RELATIONSHIP_TYPE_NAME,
                attribute.getQualifiedName(), qualifiedNameParameterName, schemaAttributeBuilder, "createSchemaAttribute"))
                .thenReturn(ATTRIBUTE_GUID);

        Classification classification = new Classification();
        classification.setName("classificationName");
        when(repositoryHelper.getNewClassification("serviceName", null, null,
                InstanceProvenanceType.LOCAL_COHORT, USER, TYPE_EMBEDDED_ATTRIBUTE_CLASSIFICATION_TYPE_NAME,
                TABULAR_COLUMN_TYPE_NAME, ClassificationOrigin.ASSIGNED, null,
                schemaTypeBuilder.getTypeEmbeddedInstanceProperties(TYPE_EMBEDDED_ATTRIBUTE_CLASSIFICATION_TYPE_NAME)))
                .thenReturn(classification);

        EntityDetail schemaAttributeEntity = mockFindEntity(ATTRIBUTE_QUALIFIED_NAME, ATTRIBUTE_GUID, SCHEMA_ATTRIBUTE_TYPE_NAME);

        EntityDetail schemaAttributeUpdatedEntity = mock(EntityDetail.class);
        when(dataEngineCommonHandler.buildEntityDetail(ATTRIBUTE_GUID, null)).thenReturn(schemaAttributeUpdatedEntity);
        EntityDetailDifferences attributeDifferences = mock(EntityDetailDifferences.class);
        when((attributeDifferences.hasInstancePropertiesDifferences())).thenReturn(Boolean.TRUE);
        when(repositoryHelper.getEntityDetailDifferences(schemaAttributeEntity, schemaAttributeUpdatedEntity, true))
                .thenReturn(attributeDifferences);
        when(dataEngineRegistrationHandler.getExternalDataEngine(USER, EXTERNAL_SOURCE_DE_QUALIFIED_NAME))
                .thenReturn(EXTERNAL_SOURCE_DE_GUID);

        doReturn(schemaAttributeBuilder).when(dataEngineSchemaTypeHandler).getSchemaAttributeBuilder(attribute);

        String result = dataEngineSchemaTypeHandler.upsertSchemaType(USER, schemaType, EXTERNAL_SOURCE_DE_QUALIFIED_NAME);

        assertEquals(GUID, result);
        verify(invalidParameterHandler, times(1)).validateUserId(USER, methodName);
        verify(invalidParameterHandler, times(1)).validateName(QUALIFIED_NAME,
                QUALIFIED_NAME_PROPERTY_NAME, methodName);
        verify(invalidParameterHandler, times(1)).validateName(NAME,
                DISPLAY_NAME_PROPERTY_NAME, methodName);
        verify(schemaTypeHandler, times(1)).updateSchemaType(USER, EXTERNAL_SOURCE_DE_GUID,
                EXTERNAL_SOURCE_DE_QUALIFIED_NAME, GUID, SCHEMA_TYPE_GUID_PARAMETER_NAME, schemaTypeBuilder);
        verify(schemaAttributeHandler, times(1)).updateSchemaAttribute(USER,
                EXTERNAL_SOURCE_DE_GUID, EXTERNAL_SOURCE_DE_QUALIFIED_NAME, ATTRIBUTE_GUID,
                getSchemaAttributePropertiesBuilder(attribute).getInstanceProperties(methodName));
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

        when(dataEngineRegistrationHandler.getExternalDataEngine(USER,
                EXTERNAL_SOURCE_DE_QUALIFIED_NAME)).thenReturn(EXTERNAL_SOURCE_DE_GUID);

        when(dataEngineCommonHandler.findEntity(USER, QUALIFIED_NAME, SCHEMA_TYPE_TYPE_NAME)).thenReturn(Optional.empty());

        doReturn(schemaTypeBuilder).when(dataEngineSchemaTypeHandler).getSchemaTypeBuilder(schemaType);

        UserNotAuthorizedException mockedException = mockException(UserNotAuthorizedException.class, methodName);
        when(schemaTypeHandler.addSchemaType(USER, EXTERNAL_SOURCE_DE_GUID, EXTERNAL_SOURCE_DE_QUALIFIED_NAME, schemaTypeBuilder,
                methodName)).thenThrow(mockedException);
        UserNotAuthorizedException thrown = assertThrows(UserNotAuthorizedException.class, () ->
                dataEngineSchemaTypeHandler.upsertSchemaType(USER, getSchemaType(), EXTERNAL_SOURCE_DE_QUALIFIED_NAME));

        assertTrue(thrown.getMessage().contains("OMAS-DATA-ENGINE-404-001 "));
    }

    @Test
    void addLineageMappingRelationship() throws UserNotAuthorizedException, PropertyServerException,
                                                InvalidParameterException {
        mockFindEntity(SOURCE_QUALIFIED_NAME, SOURCE_GUID, SCHEMA_ATTRIBUTE_TYPE_NAME);
        mockFindEntity(TARGET_QUALIFIED_NAME, TARGET_GUID, SCHEMA_ATTRIBUTE_TYPE_NAME);

        dataEngineSchemaTypeHandler.addLineageMappingRelationship(USER, SOURCE_QUALIFIED_NAME, TARGET_QUALIFIED_NAME,
                EXTERNAL_SOURCE_DE_QUALIFIED_NAME);

        verify(dataEngineCommonHandler, times(1)).upsertExternalRelationship(USER, SOURCE_GUID, TARGET_GUID,
                LINEAGE_MAPPING_TYPE_NAME, SCHEMA_ATTRIBUTE_TYPE_NAME, EXTERNAL_SOURCE_DE_QUALIFIED_NAME, null);
    }

    @Test
    void addLineageMappingRelationship_throwsUserNotAuthorizedException() throws UserNotAuthorizedException,
                                                                                 PropertyServerException,
                                                                                 InvocationTargetException,
                                                                                 NoSuchMethodException,
                                                                                 InstantiationException,
                                                                                 IllegalAccessException,
                                                                                 InvalidParameterException {
        final String methodName = "addLineageMappingRelationship";

        mockFindEntity(SOURCE_QUALIFIED_NAME, SOURCE_GUID, SCHEMA_ATTRIBUTE_TYPE_NAME);
        mockFindEntity(TARGET_QUALIFIED_NAME, TARGET_GUID, SCHEMA_ATTRIBUTE_TYPE_NAME);

        UserNotAuthorizedException mockedException = mockException(UserNotAuthorizedException.class, methodName);
        doThrow(mockedException).when(dataEngineCommonHandler).upsertExternalRelationship(USER, SOURCE_GUID, TARGET_GUID,
                LINEAGE_MAPPING_TYPE_NAME, SCHEMA_ATTRIBUTE_TYPE_NAME,
                EXTERNAL_SOURCE_DE_QUALIFIED_NAME, null);

        UserNotAuthorizedException thrown = assertThrows(UserNotAuthorizedException.class, () ->
                dataEngineSchemaTypeHandler.addLineageMappingRelationship(USER, SOURCE_QUALIFIED_NAME, TARGET_QUALIFIED_NAME,
                        EXTERNAL_SOURCE_DE_QUALIFIED_NAME));

        assertTrue(thrown.getMessage().contains("OMAS-DATA-ENGINE-404-001 "));
    }

    @Test
    void addLineageMappingRelationship_throwsInvalidParameterException() throws UserNotAuthorizedException,
                                                                                PropertyServerException,
                                                                                InvalidParameterException {
        when(dataEngineCommonHandler.findEntity(USER, TARGET_QUALIFIED_NAME, SCHEMA_ATTRIBUTE_TYPE_NAME)).thenReturn(Optional.empty());

        dataEngineSchemaTypeHandler.addLineageMappingRelationship(USER, SOURCE_QUALIFIED_NAME, TARGET_QUALIFIED_NAME,
                EXTERNAL_SOURCE_DE_QUALIFIED_NAME);

        verify(dataEngineCommonHandler, times(1)).throwInvalidParameterException(DataEngineErrorCode.SCHEMA_ATTRIBUTE_NOT_FOUND,
                "addLineageMappingRelationship", SOURCE_QUALIFIED_NAME);
    }

    @Test
    void removeSchemaType() throws UserNotAuthorizedException, PropertyServerException, InvalidParameterException, FunctionNotSupportedException {
        EntityDetail mockedEntity = mock(EntityDetail.class);
        when(mockedEntity.getGUID()).thenReturn(GUID);
        when(dataEngineCommonHandler.findEntity(USER, QUALIFIED_NAME, SCHEMA_TYPE_TYPE_NAME)).thenReturn(Optional.of(mockedEntity));

        //mock getSchemaAttributesForSchemaType
        mockTypeDef(TYPE_TO_ATTRIBUTE_RELATIONSHIP_TYPE_NAME,
                TYPE_TO_ATTRIBUTE_RELATIONSHIP_TYPE_GUID);
        EntityDetail entityDetail = mock(EntityDetail.class);
        when(entityDetail.getGUID()).thenReturn(ATTRIBUTE_GUID);
        List<EntityDetail> entityDetails = Collections.singletonList(entityDetail);
        when(repositoryHandler.getEntitiesForRelationshipType(USER, GUID, SCHEMA_TYPE_TYPE_NAME,
                TYPE_TO_ATTRIBUTE_RELATIONSHIP_TYPE_GUID,
                TYPE_TO_ATTRIBUTE_RELATIONSHIP_TYPE_NAME, 0, 0,
                "getSchemaAttributesForSchemaType")).thenReturn(entityDetails);

        dataEngineSchemaTypeHandler.removeSchemaType(USER, GUID, EXTERNAL_SOURCE_DE_QUALIFIED_NAME, DeleteSemantic.HARD);

        verify(dataEngineCommonHandler, times(1)).removeEntity(USER, ATTRIBUTE_GUID,
                TABULAR_COLUMN_TYPE_NAME, EXTERNAL_SOURCE_DE_QUALIFIED_NAME);

        verify(dataEngineCommonHandler, times(1)).removeEntity(USER, GUID,
                TABULAR_SCHEMA_TYPE_TYPE_NAME, EXTERNAL_SOURCE_DE_QUALIFIED_NAME);
    }

    @Test
    void removeSchemaType_throwsFunctionNotSupportedException() {
        FunctionNotSupportedException thrown = assertThrows(FunctionNotSupportedException.class, () ->
                dataEngineSchemaTypeHandler.removeSchemaType(USER, GUID, EXTERNAL_SOURCE_DE_QUALIFIED_NAME, DeleteSemantic.SOFT));

        assertTrue(thrown.getMessage().contains("OMRS-METADATA-COLLECTION-501-001"));
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

    private SchemaAttributeBuilder getSchemaAttributePropertiesBuilder(Attribute attribute) {
        SchemaAttributeBuilder schemaAttributeBuilder = new SchemaAttributeBuilder(attribute.getQualifiedName(),
                attribute.getDisplayName(), null, attribute.getPosition(), attribute.getMinCardinality(), attribute.getMaxCardinality(),
                false, attribute.getDefaultValueOverride(), attribute.getAllowsDuplicateValues(), attribute.getOrderedValues(),
                0, 0, 0, 0, false, null, null, null,
                TABULAR_COLUMN_TYPE_GUID, TABULAR_COLUMN_TYPE_NAME, null, repositoryHelper, "serviceName", "serverName");
        Classification classification = new Classification();
        classification.setName("classification");
        schemaAttributeBuilder.setClassification(classification);
        return schemaAttributeBuilder;
    }

    SchemaTypeBuilder getSchemaTypeBuilder(SchemaType schemaType) {
        return new SchemaTypeBuilder(schemaType.getQualifiedName(), schemaType.getDisplayName(), null,
                schemaType.getVersionNumber(), false, schemaType.getAuthor(), schemaType.getUsage(),
                schemaType.getEncodingStandard(), null, null,
                TABULAR_SCHEMA_TYPE_TYPE_GUID, TABULAR_SCHEMA_TYPE_TYPE_NAME, null, repositoryHelper, "serviceName", "serverName");
    }
}