/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.dataengine.server.handlers;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.odpi.openmetadata.accessservices.dataengine.model.Attribute;
import org.odpi.openmetadata.accessservices.dataengine.server.mappers.ProcessPropertiesMapper;
import org.odpi.openmetadata.accessservices.dataengine.server.mappers.SchemaTypePropertiesMapper;
import org.odpi.openmetadata.commonservices.ffdc.InvalidParameterHandler;
import org.odpi.openmetadata.commonservices.ocf.metadatamanagement.handlers.SchemaTypeHandler;
import org.odpi.openmetadata.commonservices.ocf.metadatamanagement.mappers.SchemaElementMapper;
import org.odpi.openmetadata.commonservices.repositoryhandler.RepositoryHandler;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.ComplexSchemaType;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.PrimitiveSchemaType;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.SchemaAttribute;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.SchemaType;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.TypeDef;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;

import java.lang.reflect.InvocationTargetException;
import java.util.Collections;
import java.util.List;

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

    @InjectMocks
    private DataEngineSchemaTypeHandler dataEngineSchemaTypeHandler;

    @Test
    void createSchemaType() throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException {
        String methodName = "createOrUpdateSchemaType";

        ComplexSchemaType schemaType = new ComplexSchemaType();
        SchemaAttribute schemaAttribute = new SchemaAttribute();

        when(schemaTypeHandler.getEmptyComplexSchemaType(SchemaElementMapper.TABULAR_SCHEMA_TYPE_TYPE_GUID,
                SchemaElementMapper.TABULAR_SCHEMA_TYPE_TYPE_NAME)).thenReturn(schemaType);
        when(schemaTypeHandler.getEmptyTabularColumn()).thenReturn(schemaAttribute);

        when(schemaTypeHandler.saveExternalSchemaType(USER, schemaType, Collections.singletonList(schemaAttribute),
                EXTERNAL_SOURCE_DE_GUID, EXTERNAL_SOURCE_DE_QUALIFIED_NAME, methodName)).thenReturn(GUID);

        when(dataEngineRegistrationHandler.getExternalDataEngineByQualifiedName(USER,
                EXTERNAL_SOURCE_DE_QUALIFIED_NAME)).thenReturn(EXTERNAL_SOURCE_DE_GUID);

        mockFindSchemaAttribute(ATTRIBUTE_QUALIFIED_NAME, ATTRIBUTE_GUID);
        mockTypeDef(SchemaTypePropertiesMapper.TYPE_EMBEDDED_ATTRIBUTE_NAME,
                SchemaTypePropertiesMapper.TYPE_EMBEDDED_ATTRIBUTE_NAME);

        String result = dataEngineSchemaTypeHandler.createOrUpdateSchemaType(USER, QUALIFIED_NAME, NAME, AUTHOR,
                ENCODING_STANDARD, USAGE, VERSION, Collections.singletonList(new Attribute(ATTRIBUTE_QUALIFIED_NAME,
                        ATTRIBUTE_DISPLAY_NAME, "1", "1", null, null, 1, null, null, null)),
                EXTERNAL_SOURCE_DE_QUALIFIED_NAME);

        assertEquals(GUID, result);

        verify(invalidParameterHandler, times(1)).validateUserId(USER, methodName);
        verify(invalidParameterHandler, times(1)).validateName(QUALIFIED_NAME,
                SchemaTypePropertiesMapper.QUALIFIED_NAME_PROPERTY_NAME, methodName);
        verify(invalidParameterHandler, times(1)).validateName(NAME,
                SchemaTypePropertiesMapper.DISPLAY_NAME_PROPERTY_NAME, methodName);
        verify(repositoryHandler, times(1)).classifyEntity(USER, ATTRIBUTE_GUID,
                SchemaTypePropertiesMapper.TYPE_EMBEDDED_ATTRIBUTE_NAME,
                SchemaTypePropertiesMapper.TYPE_EMBEDDED_ATTRIBUTE_NAME, null,
                "addTypeEmbeddedAttributeClassifications");
    }

    @Test
    void createSchemaType_throwsUserNotAuthorizedException() throws InvalidParameterException, PropertyServerException,
                                                                    UserNotAuthorizedException,
                                                                    InvocationTargetException, NoSuchMethodException,
                                                                    InstantiationException, IllegalAccessException {
        String methodName = "createOrUpdateSchemaType";

        ComplexSchemaType schemaType = new ComplexSchemaType();
        SchemaAttribute schemaAttribute = new SchemaAttribute();

        when(schemaTypeHandler.getEmptyComplexSchemaType(SchemaElementMapper.TABULAR_SCHEMA_TYPE_TYPE_GUID,
                SchemaElementMapper.TABULAR_SCHEMA_TYPE_TYPE_NAME)).thenReturn(schemaType);
        when(schemaTypeHandler.getEmptyTabularColumn()).thenReturn(schemaAttribute);

        when(dataEngineRegistrationHandler.getExternalDataEngineByQualifiedName(USER,
                EXTERNAL_SOURCE_DE_QUALIFIED_NAME)).thenReturn(EXTERNAL_SOURCE_DE_GUID);

        UserNotAuthorizedException mockedException = mockException(UserNotAuthorizedException.class, methodName);
        when(schemaTypeHandler.saveExternalSchemaType(USER, schemaType, Collections.singletonList(schemaAttribute),
                EXTERNAL_SOURCE_DE_GUID, EXTERNAL_SOURCE_DE_QUALIFIED_NAME, methodName)).thenThrow(mockedException);

        UserNotAuthorizedException thrown = assertThrows(UserNotAuthorizedException.class, () ->
                dataEngineSchemaTypeHandler.createOrUpdateSchemaType(USER, QUALIFIED_NAME, NAME, AUTHOR,
                        ENCODING_STANDARD, USAGE, VERSION,
                        Collections.singletonList(new Attribute(ATTRIBUTE_QUALIFIED_NAME,
                                ATTRIBUTE_DISPLAY_NAME, "1", "1", null, null, 1, null, null, null)),
                        EXTERNAL_SOURCE_DE_QUALIFIED_NAME));

        assertTrue(thrown.getMessage().contains("OMAS-DATA-ENGINE-404-001 "));
    }

    @Test
    void addLineageMappingRelationship() throws UserNotAuthorizedException, PropertyServerException,
                                                InvalidParameterException {
        mockFindSchemaAttribute(SOURCE_QUALIFIED_NAME, SOURCE_GUID);
        mockFindSchemaAttribute(TARGET_QUALIFIED_NAME, TARGET_GUID);

        mockTypeDef(SchemaTypePropertiesMapper.LINEAGE_MAPPINGS_TYPE_NAME,
                SchemaTypePropertiesMapper.LINEAGE_MAPPINGS_TYPE_GUID);

        when(dataEngineRegistrationHandler.getExternalDataEngineByQualifiedName(USER,
                EXTERNAL_SOURCE_DE_QUALIFIED_NAME)).thenReturn(EXTERNAL_SOURCE_DE_GUID);

        dataEngineSchemaTypeHandler.addLineageMappingRelationship(USER, SOURCE_QUALIFIED_NAME, TARGET_QUALIFIED_NAME,
                EXTERNAL_SOURCE_DE_QUALIFIED_NAME);

        verify(repositoryHandler, times(1)).createExternalRelationship(USER,
                SchemaTypePropertiesMapper.LINEAGE_MAPPINGS_TYPE_GUID, EXTERNAL_SOURCE_DE_GUID,
                EXTERNAL_SOURCE_DE_QUALIFIED_NAME,
                SOURCE_GUID, TARGET_GUID, null, "addLineageMappingRelationship");
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

        mockFindSchemaAttribute(SOURCE_QUALIFIED_NAME, SOURCE_GUID);
        mockFindSchemaAttribute(TARGET_QUALIFIED_NAME, TARGET_GUID);

        mockTypeDef(SchemaTypePropertiesMapper.LINEAGE_MAPPINGS_TYPE_NAME,
                SchemaTypePropertiesMapper.LINEAGE_MAPPINGS_TYPE_GUID);

        when(dataEngineRegistrationHandler.getExternalDataEngineByQualifiedName(USER,
                EXTERNAL_SOURCE_DE_QUALIFIED_NAME)).thenReturn(EXTERNAL_SOURCE_DE_GUID);

        when(repositoryHandler.getRelationshipBetweenEntities(USER, SOURCE_GUID,
                SchemaElementMapper.SCHEMA_ATTRIBUTE_TYPE_NAME, TARGET_GUID,
                SchemaTypePropertiesMapper.LINEAGE_MAPPINGS_TYPE_GUID,
                SchemaTypePropertiesMapper.LINEAGE_MAPPINGS_TYPE_NAME, methodName)).thenReturn(null);

        UserNotAuthorizedException mockedException = mockException(UserNotAuthorizedException.class, methodName);
        doThrow(mockedException).when(repositoryHandler).createExternalRelationship(USER,
                SchemaTypePropertiesMapper.LINEAGE_MAPPINGS_TYPE_GUID, EXTERNAL_SOURCE_DE_GUID,
                EXTERNAL_SOURCE_DE_QUALIFIED_NAME,
                SOURCE_GUID, TARGET_GUID, null, methodName);

        UserNotAuthorizedException thrown = assertThrows(UserNotAuthorizedException.class, () ->
                dataEngineSchemaTypeHandler.addLineageMappingRelationship(USER, SOURCE_QUALIFIED_NAME,
                        TARGET_QUALIFIED_NAME, EXTERNAL_SOURCE_DE_QUALIFIED_NAME));

        assertTrue(thrown.getMessage().contains("OMAS-DATA-ENGINE-404-001 "));
    }


    @Test
    void removeSchemaType() throws UserNotAuthorizedException, PropertyServerException, InvalidParameterException {
        //mock getSchemaAttributesForSchemaType
        mockTypeDef(SchemaElementMapper.TYPE_TO_ATTRIBUTE_RELATIONSHIP_TYPE_NAME,
                SchemaElementMapper.TYPE_TO_ATTRIBUTE_RELATIONSHIP_TYPE_GUID);
        EntityDetail entityDetail = mock(EntityDetail.class);
        when(entityDetail.getGUID()).thenReturn(ATTRIBUTE_GUID);
        List<EntityDetail> entityDetails = Collections.singletonList(entityDetail);
        when(repositoryHandler.getEntitiesForRelationshipType(USER, GUID, SchemaElementMapper.SCHEMA_TYPE_TYPE_NAME,
                SchemaElementMapper.TYPE_TO_ATTRIBUTE_RELATIONSHIP_TYPE_GUID,
                SchemaElementMapper.TYPE_TO_ATTRIBUTE_RELATIONSHIP_TYPE_NAME, 0, 0,
                "getSchemaAttributesForSchemaType")).thenReturn(entityDetails);

        //mock type for removeTabularColumn & removeTabularSchemaType
        mockTypeDef(SchemaElementMapper.TABULAR_COLUMN_TYPE_NAME, SchemaElementMapper.TABULAR_COLUMN_TYPE_GUID);
        mockTypeDef(SchemaElementMapper.TABULAR_SCHEMA_TYPE_TYPE_NAME,
                SchemaElementMapper.TABULAR_SCHEMA_TYPE_TYPE_GUID);

        dataEngineSchemaTypeHandler.removeSchemaType(USER, GUID);

        verify(repositoryHandler, times(1)).removeEntity(USER, ATTRIBUTE_GUID,
                SchemaElementMapper.TABULAR_COLUMN_TYPE_GUID, SchemaElementMapper.TABULAR_COLUMN_TYPE_NAME,
                null, null, "removeTabularColumn");

        verify(repositoryHandler, times(1)).removeEntity(USER, GUID,
                SchemaElementMapper.TABULAR_SCHEMA_TYPE_TYPE_GUID, SchemaElementMapper.TABULAR_SCHEMA_TYPE_TYPE_NAME,
                null, null, "removeTabularSchemaType");

    }

    private void mockFindSchemaAttribute(String qualifiedName, String guid) throws UserNotAuthorizedException,
                                                                                   PropertyServerException {
        TypeDef mockedType = mock(TypeDef.class);
        when(mockedType.getName()).thenReturn(SchemaElementMapper.SCHEMA_ATTRIBUTE_TYPE_NAME);
        when(mockedType.getGUID()).thenReturn(SchemaElementMapper.SCHEMA_ATTRIBUTE_TYPE_GUID);
        when(repositoryHelper.getTypeDefByName(USER, SchemaElementMapper.SCHEMA_ATTRIBUTE_TYPE_NAME)).thenReturn(mockedType);

        when(repositoryHelper.getExactMatchRegex(qualifiedName)).thenReturn(qualifiedName);

        EntityDetail mockedEntity = Mockito.mock(EntityDetail.class);
        when(mockedEntity.getGUID()).thenReturn(guid);
        when(repositoryHandler.getUniqueEntityByName(USER, qualifiedName,
                SchemaTypePropertiesMapper.QUALIFIED_NAME_PROPERTY_NAME, null,
                SchemaElementMapper.SCHEMA_ATTRIBUTE_TYPE_GUID,
                SchemaElementMapper.SCHEMA_ATTRIBUTE_TYPE_NAME, "findSchemaAttribute")).thenReturn(mockedEntity);
    }

    private void mockTypeDef(String typeName, String typeGUID) {
        TypeDef entityTypeDef = mock(TypeDef.class);
        when(repositoryHelper.getTypeDefByName(USER, typeName)).thenReturn(entityTypeDef);

        when(entityTypeDef.getName()).thenReturn(typeName);
        when(entityTypeDef.getGUID()).thenReturn(typeGUID);
    }
}