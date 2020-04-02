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
import org.odpi.openmetadata.accessservices.dataengine.ffdc.DataEngineErrorCode;
import org.odpi.openmetadata.accessservices.dataengine.model.Attribute;
import org.odpi.openmetadata.accessservices.dataengine.model.SchemaType;
import org.odpi.openmetadata.accessservices.dataengine.server.mappers.SchemaTypePropertiesMapper;
import org.odpi.openmetadata.commonservices.ffdc.InvalidParameterHandler;
import org.odpi.openmetadata.commonservices.ocf.metadatamanagement.handlers.SchemaTypeHandler;
import org.odpi.openmetadata.commonservices.ocf.metadatamanagement.mappers.SchemaElementMapper;
import org.odpi.openmetadata.commonservices.repositoryhandler.RepositoryHandler;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.ComplexSchemaType;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.SchemaAttribute;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetailDifferences;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.TypeDef;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;

import java.lang.reflect.InvocationTargetException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
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
    private static final String TARGET_GUID = "targetGuid";
    private static final String TARGET_QUALIFIED_NAME = "targetQualifiedName";
    private static final String EXTERNAL_SOURCE_DE_GUID = "externalSourceDataEngineGuid";
    private static final String EXTERNAL_SOURCE_DE_QUALIFIED_NAME = "externalSourceDataEngineQualifiedName";
    private static final String PROCESS_GUID = "processGUID";

    @Mock
    private RepositoryHandler repositoryHandler;

    @Mock
    private OMRSRepositoryHelper repositoryHelper;

    @Mock
    private InvalidParameterHandler invalidParameterHandler;

    @Mock
    private SchemaTypeHandler schemaTypeHandler;

    @Mock
    private DataEngineRegistrationHandler dataEngineRegistrationHandler;

    @Mock
    private DataEngineCommonHandler dataEngineCommonHandler;

    @InjectMocks
    private DataEngineSchemaTypeHandler dataEngineSchemaTypeHandler;

    private Attribute attribute = getAttribute();

    private List<Attribute> attributeList = Collections.singletonList(attribute);

    @Test
    void createSchemaTypeWithSchemaAttribute() throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException {
        String methodName = "createOrUpdateSchemaType";

        ComplexSchemaType schemaType = new ComplexSchemaType();
        SchemaAttribute schemaAttribute = new SchemaAttribute();

        when(schemaTypeHandler.getEmptyComplexSchemaType(SchemaElementMapper.TABULAR_SCHEMA_TYPE_TYPE_GUID,
                SchemaElementMapper.TABULAR_SCHEMA_TYPE_TYPE_NAME)).thenReturn(schemaType);

        when(schemaTypeHandler.getEmptyTabularColumn()).thenReturn(schemaAttribute);

        when(dataEngineCommonHandler.findEntity(USER, QUALIFIED_NAME, SchemaElementMapper.SCHEMA_TYPE_TYPE_NAME)).thenReturn(Optional.empty());

        when(dataEngineRegistrationHandler.getExternalDataEngineByQualifiedName(USER, EXTERNAL_SOURCE_DE_QUALIFIED_NAME)).thenReturn(EXTERNAL_SOURCE_DE_GUID);

        when(schemaTypeHandler.addExternalSchemaType(USER, schemaType, EXTERNAL_SOURCE_DE_GUID, EXTERNAL_SOURCE_DE_QUALIFIED_NAME)).thenReturn(GUID);

        when(dataEngineCommonHandler.findEntity(USER, ATTRIBUTE_QUALIFIED_NAME, SchemaElementMapper.SCHEMA_ATTRIBUTE_TYPE_NAME)).thenReturn(Optional.empty());

        when(schemaTypeHandler.addExternalSchemaAttribute(USER, schemaAttribute, EXTERNAL_SOURCE_DE_GUID, EXTERNAL_SOURCE_DE_QUALIFIED_NAME)).thenReturn(ATTRIBUTE_GUID);

        mockTypeDef(SchemaTypePropertiesMapper.TYPE_EMBEDDED_ATTRIBUTE_NAME, SchemaTypePropertiesMapper.TYPE_EMBEDDED_ATTRIBUTE_NAME);

        String result = dataEngineSchemaTypeHandler.createOrUpdateSchemaType(USER, getSchemaType(), EXTERNAL_SOURCE_DE_QUALIFIED_NAME);

        assertEquals(GUID, result);

        verify(invalidParameterHandler, times(1)).validateUserId(USER, methodName);
        verify(invalidParameterHandler, times(1)).validateName(QUALIFIED_NAME, SchemaTypePropertiesMapper.QUALIFIED_NAME_PROPERTY_NAME, methodName);
        verify(invalidParameterHandler, times(1)).validateName(NAME, SchemaTypePropertiesMapper.DISPLAY_NAME_PROPERTY_NAME, methodName);
        verify(repositoryHandler, times(1)).createExternalRelationship(USER, SchemaElementMapper.TYPE_TO_ATTRIBUTE_RELATIONSHIP_TYPE_GUID,
                EXTERNAL_SOURCE_DE_GUID, EXTERNAL_SOURCE_DE_QUALIFIED_NAME, GUID, ATTRIBUTE_GUID, null, "createSchemaAttribute");
        verify(repositoryHandler, times(1)).classifyEntity(USER, ATTRIBUTE_GUID, SchemaTypePropertiesMapper.TYPE_EMBEDDED_ATTRIBUTE_NAME,
                SchemaTypePropertiesMapper.TYPE_EMBEDDED_ATTRIBUTE_NAME, null, "createSchemaAttribute");
    }

    @Test
    void updateSchemaTypeWithSchemaAttribute() throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException {
        String methodName = "createOrUpdateSchemaType";

        ComplexSchemaType schemaType = new ComplexSchemaType();
        SchemaAttribute schemaAttribute = new SchemaAttribute();

        when(schemaTypeHandler.getEmptyComplexSchemaType(SchemaElementMapper.TABULAR_SCHEMA_TYPE_TYPE_GUID,
                SchemaElementMapper.TABULAR_SCHEMA_TYPE_TYPE_NAME)).thenReturn(schemaType);

        when(schemaTypeHandler.getEmptyTabularColumn()).thenReturn(schemaAttribute);

        EntityDetail schemaTypeEntity = mockFindEntity(QUALIFIED_NAME, GUID, SchemaElementMapper.SCHEMA_TYPE_TYPE_NAME);

        EntityDetail schemaTypeUpdatedEntity = mock(EntityDetail.class);
        when(dataEngineCommonHandler.buildEntityDetail(GUID, null)).thenReturn(schemaTypeUpdatedEntity);
        EntityDetailDifferences schemaTypeDifferences = mock(EntityDetailDifferences.class);
        when((schemaTypeDifferences.hasInstancePropertiesDifferences())).thenReturn(Boolean.TRUE);
        when(repositoryHelper.getEntityDetailDifferences(schemaTypeEntity, schemaTypeUpdatedEntity, true)).thenReturn(schemaTypeDifferences);

        EntityDetail schemaAttributeEntity = mockFindEntity(ATTRIBUTE_QUALIFIED_NAME, ATTRIBUTE_GUID, SchemaElementMapper.SCHEMA_ATTRIBUTE_TYPE_NAME);

        EntityDetail schemaAttributeUpdatedEntity = mock(EntityDetail.class);
        when(dataEngineCommonHandler.buildEntityDetail(ATTRIBUTE_GUID, null)).thenReturn(schemaAttributeUpdatedEntity);
        EntityDetailDifferences attributeDifferences = mock(EntityDetailDifferences.class);
        when((attributeDifferences.hasInstancePropertiesDifferences())).thenReturn(Boolean.TRUE);
        when(repositoryHelper.getEntityDetailDifferences(schemaAttributeEntity, schemaAttributeUpdatedEntity, true)).thenReturn(attributeDifferences);

        String result = dataEngineSchemaTypeHandler.createOrUpdateSchemaType(USER, getSchemaType(), EXTERNAL_SOURCE_DE_QUALIFIED_NAME);

        assertEquals(GUID, result);

        verify(invalidParameterHandler, times(1)).validateUserId(USER, methodName);
        verify(invalidParameterHandler, times(1)).validateName(QUALIFIED_NAME, SchemaTypePropertiesMapper.QUALIFIED_NAME_PROPERTY_NAME, methodName);
        verify(invalidParameterHandler, times(1)).validateName(NAME, SchemaTypePropertiesMapper.DISPLAY_NAME_PROPERTY_NAME, methodName);
        verify(schemaTypeHandler, times(1)).updateSchemaType(USER, GUID, schemaType);
        verify(schemaTypeHandler, times(1)).updateSchemaAttribute(USER, ATTRIBUTE_GUID, schemaAttribute);
    }

    @Test
    void createSchemaType_throwsUserNotAuthorizedException() throws InvalidParameterException,
                                                                    PropertyServerException,
                                                                    UserNotAuthorizedException,
                                                                    InvocationTargetException,
                                                                    NoSuchMethodException,
                                                                    InstantiationException,
                                                                    IllegalAccessException {
        String methodName = "createOrUpdateSchemaType";

        ComplexSchemaType schemaType = new ComplexSchemaType();
        SchemaAttribute schemaAttribute = new SchemaAttribute();

        when(schemaTypeHandler.getEmptyComplexSchemaType(SchemaElementMapper.TABULAR_SCHEMA_TYPE_TYPE_GUID,
                SchemaElementMapper.TABULAR_SCHEMA_TYPE_TYPE_NAME)).thenReturn(schemaType);
        when(schemaTypeHandler.getEmptyTabularColumn()).thenReturn(schemaAttribute);

        when(dataEngineRegistrationHandler.getExternalDataEngineByQualifiedName(USER,
                EXTERNAL_SOURCE_DE_QUALIFIED_NAME)).thenReturn(EXTERNAL_SOURCE_DE_GUID);

        UserNotAuthorizedException mockedException = mockException(UserNotAuthorizedException.class, methodName);
        when(schemaTypeHandler.addExternalSchemaType(USER, schemaType, EXTERNAL_SOURCE_DE_GUID, EXTERNAL_SOURCE_DE_QUALIFIED_NAME)).thenThrow(mockedException);
        UserNotAuthorizedException thrown = assertThrows(UserNotAuthorizedException.class, () ->
                dataEngineSchemaTypeHandler.createOrUpdateSchemaType(USER, getSchemaType(), EXTERNAL_SOURCE_DE_QUALIFIED_NAME));

        assertTrue(thrown.getMessage().contains("OMAS-DATA-ENGINE-404-001 "));
    }

    @Test
    void addLineageMappingRelationship() throws UserNotAuthorizedException, PropertyServerException,
                                                InvalidParameterException {
        mockFindEntity(SOURCE_QUALIFIED_NAME, SOURCE_GUID, SchemaElementMapper.SCHEMA_ATTRIBUTE_TYPE_NAME);
        mockFindEntity(TARGET_QUALIFIED_NAME, TARGET_GUID, SchemaElementMapper.SCHEMA_ATTRIBUTE_TYPE_NAME);

        dataEngineSchemaTypeHandler.addLineageMappingRelationship(USER, SOURCE_QUALIFIED_NAME, TARGET_QUALIFIED_NAME,
                EXTERNAL_SOURCE_DE_QUALIFIED_NAME);

        verify(dataEngineCommonHandler, times(1)).createOrUpdateExternalRelationship(USER, SOURCE_GUID, TARGET_GUID,
                SchemaTypePropertiesMapper.LINEAGE_MAPPINGS_TYPE_NAME, SchemaElementMapper.SCHEMA_ATTRIBUTE_TYPE_NAME,
                EXTERNAL_SOURCE_DE_QUALIFIED_NAME, null);
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

        mockFindEntity(SOURCE_QUALIFIED_NAME, SOURCE_GUID, SchemaElementMapper.SCHEMA_ATTRIBUTE_TYPE_NAME);
        mockFindEntity(TARGET_QUALIFIED_NAME, TARGET_GUID, SchemaElementMapper.SCHEMA_ATTRIBUTE_TYPE_NAME);

        UserNotAuthorizedException mockedException = mockException(UserNotAuthorizedException.class, methodName);
        doThrow(mockedException).when(dataEngineCommonHandler).createOrUpdateExternalRelationship(USER, SOURCE_GUID, TARGET_GUID,
                SchemaTypePropertiesMapper.LINEAGE_MAPPINGS_TYPE_NAME, SchemaElementMapper.SCHEMA_ATTRIBUTE_TYPE_NAME,
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
        when(dataEngineCommonHandler.findEntity(USER, TARGET_QUALIFIED_NAME, SchemaElementMapper.SCHEMA_ATTRIBUTE_TYPE_NAME)).thenReturn(Optional.empty());

        dataEngineSchemaTypeHandler.addLineageMappingRelationship(USER, SOURCE_QUALIFIED_NAME, TARGET_QUALIFIED_NAME,
                EXTERNAL_SOURCE_DE_QUALIFIED_NAME);

        verify(dataEngineCommonHandler, times(1)).throwInvalidParameterException(DataEngineErrorCode.SCHEMA_ATTRIBUTE_NOT_FOUND,
                "addLineageMappingRelationship", SOURCE_QUALIFIED_NAME);
    }

    @Test
    void removeSchemaType() throws UserNotAuthorizedException, PropertyServerException, InvalidParameterException {
        //mock getSchemaAttributesForSchemaType
        mockTypeDef(SchemaElementMapper.TYPE_TO_ATTRIBUTE_RELATIONSHIP_TYPE_NAME, SchemaElementMapper.TYPE_TO_ATTRIBUTE_RELATIONSHIP_TYPE_GUID);
        EntityDetail entityDetail = mock(EntityDetail.class);
        when(entityDetail.getGUID()).thenReturn(ATTRIBUTE_GUID);
        List<EntityDetail> entityDetails = Collections.singletonList(entityDetail);
        when(repositoryHandler.getEntitiesForRelationshipType(USER, GUID, SchemaElementMapper.SCHEMA_TYPE_TYPE_NAME,
                SchemaElementMapper.TYPE_TO_ATTRIBUTE_RELATIONSHIP_TYPE_GUID, SchemaElementMapper.TYPE_TO_ATTRIBUTE_RELATIONSHIP_TYPE_NAME, 0, 0,
                "getSchemaAttributesForSchemaType")).thenReturn(entityDetails);

        dataEngineSchemaTypeHandler.removeSchemaType(USER, GUID);

        verify(dataEngineCommonHandler, times(1)).removeEntity(USER, ATTRIBUTE_GUID, SchemaElementMapper.TABULAR_COLUMN_TYPE_NAME);

        verify(dataEngineCommonHandler, times(1)).removeEntity(USER, GUID, SchemaElementMapper.TABULAR_SCHEMA_TYPE_TYPE_NAME);
    }

    @Test
    void addAnchorGUID() throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException {
        mockFindEntity(ATTRIBUTE_QUALIFIED_NAME, GUID, SchemaElementMapper.SCHEMA_ATTRIBUTE_TYPE_NAME);

        SchemaAttribute schemaAttribute = new SchemaAttribute();
        when(schemaTypeHandler.getEmptyTabularColumn()).thenReturn(schemaAttribute);

        dataEngineSchemaTypeHandler.addAnchorGUID(USER, attribute, PROCESS_GUID);

        verify(schemaTypeHandler, times(1)).updateSchemaAttribute(USER, GUID, schemaAttribute);
    }

    @Test
    void addAnchorGUID_throwsInvalidParameterException() throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException {
        SchemaAttribute schemaAttribute = new SchemaAttribute();

        when(schemaTypeHandler.getEmptyTabularColumn()).thenReturn(schemaAttribute);

        when(dataEngineCommonHandler.findEntity(USER, ATTRIBUTE_QUALIFIED_NAME, SchemaElementMapper.SCHEMA_ATTRIBUTE_TYPE_NAME)).thenReturn(Optional.empty());

        dataEngineSchemaTypeHandler.addAnchorGUID(USER, attribute, PROCESS_GUID);

        verify(dataEngineCommonHandler, times(1)).throwInvalidParameterException(DataEngineErrorCode.SCHEMA_ATTRIBUTE_NOT_FOUND,
                "addAnchorGUID");
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
}