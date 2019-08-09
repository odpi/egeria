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
import org.odpi.openmetadata.accessservices.dataengine.ffdc.NoSchemaAttributeException;
import org.odpi.openmetadata.accessservices.dataengine.model.Attribute;
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
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;

import java.lang.reflect.InvocationTargetException;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doThrow;
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
    private static final String ATTRIBUTE_SCHEMA_TYPE_GUID = "attributeSchemaGuid";
    private static final String ATTRIBUTE_GUID = "attributeGuid";
    private static final String SOURCE_GUID = "sourceGuid";
    private static final String SOURCE_QUALIFIED_NAME = "sourceQualifiedName";
    private static final String TARGET_GUID = "targetGuid";
    private static final String TARGET_QUALIFIED_NAME = "targetQualifiedName";

    @Mock
    private RepositoryHandler repositoryHandler;

    @Mock
    private OMRSRepositoryHelper repositoryHelper;

    @Mock
    private InvalidParameterHandler invalidParameterHandler;

    @Mock
    private SchemaTypeHandler schemaTypeHandler;

    @InjectMocks
    private DataEngineSchemaTypeHandler dataEngineSchemaTypeHandler;

    @Test
    void createSchemaType() throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException {
        String methodName = "createSchemaType";

        ComplexSchemaType schemaType = new ComplexSchemaType();
        SchemaAttribute schemaAttribute = new SchemaAttribute();
        PrimitiveSchemaType attributeSchemaType = new PrimitiveSchemaType();

        when(schemaTypeHandler.getEmptyComplexSchemaType(SchemaElementMapper.TABULAR_SCHEMA_TYPE_TYPE_GUID,
                SchemaElementMapper.TABULAR_SCHEMA_TYPE_TYPE_NAME)).thenReturn(schemaType);
        when(schemaTypeHandler.getEmptySchemaAttribute()).thenReturn(schemaAttribute);
        when(schemaTypeHandler.getEmptyPrimitiveSchemaType(SchemaElementMapper.TABULAR_COLUMN_TYPE_TYPE_GUID,
                SchemaElementMapper.TABULAR_COLUMN_TYPE_TYPE_NAME)).thenReturn(attributeSchemaType);

        when(schemaTypeHandler.saveSchemaType(USER, schemaType, Collections.singletonList(schemaAttribute),
                methodName)).thenReturn(GUID);
        when(schemaTypeHandler.saveSchemaType(USER, attributeSchemaType, null,
                "saveAttributeSchemaTypes")).thenReturn(ATTRIBUTE_SCHEMA_TYPE_GUID);

        mockFindSchemaAttribute(ATTRIBUTE_QUALIFIED_NAME, ATTRIBUTE_GUID);

        String result = dataEngineSchemaTypeHandler.createSchemaType(USER, QUALIFIED_NAME, NAME, AUTHOR,
                ENCODING_STANDARD, USAGE, VERSION, Collections.singletonList(new Attribute(ATTRIBUTE_QUALIFIED_NAME,
                        ATTRIBUTE_DISPLAY_NAME, null, 1, null, null, null)));

        assertEquals(GUID, result);

        verify(invalidParameterHandler, times(1)).validateUserId(USER, methodName);
        verify(invalidParameterHandler, times(1)).validateName(QUALIFIED_NAME,
                SchemaTypePropertiesMapper.QUALIFIED_NAME_PROPERTY_NAME, methodName);
        verify(invalidParameterHandler, times(1)).validateName(NAME,
                SchemaTypePropertiesMapper.DISPLAY_NAME_PROPERTY_NAME, methodName);

        verify(repositoryHandler, times(1)).createRelationship(USER,
                SchemaElementMapper.ATTRIBUTE_TO_TYPE_RELATIONSHIP_TYPE_GUID, ATTRIBUTE_GUID,
                ATTRIBUTE_SCHEMA_TYPE_GUID, null, "saveAttributeSchemaTypes");
    }

    @Test
    void createSchemaType_throwsUserNotAuthorizedException() throws InvalidParameterException, PropertyServerException,
                                                                    UserNotAuthorizedException,
                                                                    InvocationTargetException, NoSuchMethodException,
                                                                    InstantiationException, IllegalAccessException {
        String methodName = "createSchemaType";

        ComplexSchemaType schemaType = new ComplexSchemaType();
        SchemaAttribute schemaAttribute = new SchemaAttribute();
        PrimitiveSchemaType attributeSchemaType = new PrimitiveSchemaType();

        when(schemaTypeHandler.getEmptyComplexSchemaType(SchemaElementMapper.TABULAR_SCHEMA_TYPE_TYPE_GUID,
                SchemaElementMapper.TABULAR_SCHEMA_TYPE_TYPE_NAME)).thenReturn(schemaType);
        when(schemaTypeHandler.getEmptySchemaAttribute()).thenReturn(schemaAttribute);
        when(schemaTypeHandler.getEmptyPrimitiveSchemaType(SchemaElementMapper.TABULAR_COLUMN_TYPE_TYPE_GUID,
                SchemaElementMapper.TABULAR_COLUMN_TYPE_TYPE_NAME)).thenReturn(attributeSchemaType);

        UserNotAuthorizedException mockedException = mockException(UserNotAuthorizedException.class, methodName);
        when(schemaTypeHandler.saveSchemaType(USER, schemaType, Collections.singletonList(schemaAttribute),
                methodName)).thenThrow(mockedException);

        UserNotAuthorizedException thrown = assertThrows(UserNotAuthorizedException.class, () ->
                dataEngineSchemaTypeHandler.createSchemaType(USER, QUALIFIED_NAME, NAME, AUTHOR, ENCODING_STANDARD,
                        USAGE, VERSION, Collections.singletonList(new Attribute(ATTRIBUTE_QUALIFIED_NAME,
                                ATTRIBUTE_DISPLAY_NAME, null, 1, null, null, null))));

        assertTrue(thrown.getMessage().contains("OMAS-DATA-ENGINE-404-001 "));
    }

    @Test
    void addLineageMappingRelationship() throws UserNotAuthorizedException, PropertyServerException,
                                                InvalidParameterException, NoSchemaAttributeException {
        final String methodName = "addLineageMappingRelationship";

        mockFindSchemaAttribute(SOURCE_QUALIFIED_NAME, SOURCE_GUID);
        mockFindSchemaAttribute(TARGET_QUALIFIED_NAME, TARGET_GUID);

        SchemaType sourceSchemaType = Mockito.mock(SchemaType.class);
        when(sourceSchemaType.getGUID()).thenReturn(SOURCE_GUID);
        when(schemaTypeHandler.getSchemaTypeForAttribute(USER, SOURCE_GUID, methodName)).thenReturn(sourceSchemaType);

        SchemaType targetSchemaType = Mockito.mock(SchemaType.class);
        when(targetSchemaType.getGUID()).thenReturn(TARGET_GUID);
        when(schemaTypeHandler.getSchemaTypeForAttribute(USER, TARGET_GUID, methodName)).thenReturn(targetSchemaType);

        dataEngineSchemaTypeHandler.addLineageMappingRelationship(USER, SOURCE_QUALIFIED_NAME, TARGET_QUALIFIED_NAME);

        verify(repositoryHandler, times(1)).createRelationship(USER,
                SchemaTypePropertiesMapper.LINEAGE_MAPPINGS_TYPE_GUID, SOURCE_GUID, TARGET_GUID, null,
                "addLineageMappingRelationship");
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

        SchemaType sourceSchemaType = Mockito.mock(SchemaType.class);
        when(sourceSchemaType.getGUID()).thenReturn(SOURCE_GUID);
        when(schemaTypeHandler.getSchemaTypeForAttribute(USER, SOURCE_GUID, methodName)).thenReturn(sourceSchemaType);

        SchemaType targetSchemaType = Mockito.mock(SchemaType.class);
        when(targetSchemaType.getGUID()).thenReturn(TARGET_GUID);
        when(schemaTypeHandler.getSchemaTypeForAttribute(USER, TARGET_GUID, methodName)).thenReturn(targetSchemaType);

        UserNotAuthorizedException mockedException = mockException(UserNotAuthorizedException.class, methodName);
        doThrow(mockedException).when(repositoryHandler).createRelationship(USER,
                SchemaTypePropertiesMapper.LINEAGE_MAPPINGS_TYPE_GUID, SOURCE_GUID, TARGET_GUID, null,
                methodName);

        UserNotAuthorizedException thrown = assertThrows(UserNotAuthorizedException.class, () ->
                dataEngineSchemaTypeHandler.addLineageMappingRelationship(USER, SOURCE_QUALIFIED_NAME,
                        TARGET_QUALIFIED_NAME));

        assertTrue(thrown.getMessage().contains("OMAS-DATA-ENGINE-404-001 "));
    }

    @Test
    void addLineageMappingRelationship_throwsNoSchemaAttributeException() throws UserNotAuthorizedException,
                                                                                 PropertyServerException,
                                                                                 InvalidParameterException {
        final String methodName = "addLineageMappingRelationship";

        mockFindSchemaAttribute(SOURCE_QUALIFIED_NAME, SOURCE_GUID);

        SchemaType sourceSchemaType = Mockito.mock(SchemaType.class);
        when(sourceSchemaType.getGUID()).thenReturn(SOURCE_GUID);
        when(schemaTypeHandler.getSchemaTypeForAttribute(USER, SOURCE_GUID, methodName)).thenReturn(sourceSchemaType);

        NoSchemaAttributeException thrown = assertThrows(NoSchemaAttributeException.class, () ->
                dataEngineSchemaTypeHandler.addLineageMappingRelationship(USER, SOURCE_QUALIFIED_NAME,
                        TARGET_QUALIFIED_NAME));

        assertTrue(thrown.getMessage().contains("OMAS-DATA-ENGINE-404-002 "));
    }

    private void mockFindSchemaAttribute(String qualifiedName, String guid) throws UserNotAuthorizedException,
                                                                                   PropertyServerException {
        EntityDetail mockedEntity = Mockito.mock(EntityDetail.class);
        when(mockedEntity.getGUID()).thenReturn(guid);
        when(repositoryHandler.getUniqueEntityByName(USER, qualifiedName,
                SchemaTypePropertiesMapper.QUALIFIED_NAME_PROPERTY_NAME, null,
                SchemaElementMapper.SCHEMA_ATTRIBUTE_TYPE_GUID,
                SchemaElementMapper.SCHEMA_ATTRIBUTE_TYPE_NAME, "findSchemaAttribute")).thenReturn(mockedEntity);
    }
}