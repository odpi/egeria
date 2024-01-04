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
import org.odpi.openmetadata.accessservices.dataengine.model.Attribute;
import org.odpi.openmetadata.accessservices.dataengine.model.SchemaType;
import org.odpi.openmetadata.commonservices.generichandlers.SchemaAttributeHandler;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.governanceaction.mapper.OpenMetadataProperty;
import org.odpi.openmetadata.frameworks.governanceaction.mapper.OpenMetadataType;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetailDifferences;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;

import java.util.Collections;
import java.util.Optional;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.internal.verification.VerificationModeFactory.times;
import static org.odpi.openmetadata.accessservices.dataengine.server.handlers.DataEngineSchemaAttributeHandler.SCHEMA_TYPE_GUID_PARAMETER_NAME;


@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.WARN)
class DataEngineSchemaAttributeHandlerTest {
    private static final String USER = "user";
    private static final String GUID = "guid";
    private static final String ATTRIBUTE_QUALIFIED_NAME = "attributeQualifiedName";
    private static final String ATTRIBUTE_DISPLAY_NAME = "attributeName";
    private static final String ATTRIBUTE_GUID = "attributeGuid";
    private static final String EXTERNAL_SOURCE_DE_GUID = "externalSourceDataEngineGuid";
    private static final String EXTERNAL_SOURCE_DE_QUALIFIED_NAME = "externalSourceDataEngineQualifiedName";

    @Mock
    private DataEngineCommonHandler dataEngineCommonHandler;

    @Mock
    private DataEngineRegistrationHandler dataEngineRegistrationHandler;

    @Mock
    private SchemaAttributeHandler<Attribute, SchemaType> schemaAttributeHandler;

    @Mock
    private OMRSRepositoryHelper repositoryHelper;

    @InjectMocks
    private DataEngineSchemaAttributeHandler dataEngineSchemaAttributeHandler;

    @Test
    void upsertSchemaAttributes_create() throws PropertyServerException, InvalidParameterException, UserNotAuthorizedException {
        when(dataEngineCommonHandler.findEntity(USER, ATTRIBUTE_QUALIFIED_NAME, OpenMetadataType.SCHEMA_ATTRIBUTE_TYPE_NAME)).thenReturn(Optional.empty());
        when(dataEngineRegistrationHandler.getExternalDataEngine(USER, EXTERNAL_SOURCE_DE_QUALIFIED_NAME)).thenReturn(EXTERNAL_SOURCE_DE_GUID);

        Attribute attribute = getAttribute();
        when(dataEngineCommonHandler.getSortOrder(attribute)).thenReturn(99);

        dataEngineSchemaAttributeHandler.upsertSchemaAttributes(USER, Collections.singletonList(attribute),
                EXTERNAL_SOURCE_DE_QUALIFIED_NAME, EXTERNAL_SOURCE_DE_GUID, GUID);

        verify(schemaAttributeHandler, times(1)).createNestedSchemaAttribute(USER, EXTERNAL_SOURCE_DE_GUID,
                                                                             EXTERNAL_SOURCE_DE_QUALIFIED_NAME, GUID, SCHEMA_TYPE_GUID_PARAMETER_NAME, ATTRIBUTE_QUALIFIED_NAME, OpenMetadataProperty.QUALIFIED_NAME.name,
                                                                             ATTRIBUTE_DISPLAY_NAME, null, null, null, null, null,
                                                                             null, null, false, 0, 0, 0, false,
                                                                             false, null, 99, 0, 0, 0, false, null,
                                                                             null, null, OpenMetadataType.TABULAR_COLUMN_TYPE_NAME, null, null, null, false,
                                                                             false, null, "createSchemaAttribute");
    }

    @Test
    void upsertSchemaAttributes_update() throws PropertyServerException, InvalidParameterException, UserNotAuthorizedException {
        final String methodName = "updateSchemaAttribute";
        EntityDetail schemaAttributeEntity = mock(EntityDetail.class);
        when(schemaAttributeEntity.getGUID()).thenReturn(ATTRIBUTE_GUID);
        when(dataEngineCommonHandler.findEntity(USER, ATTRIBUTE_QUALIFIED_NAME, OpenMetadataType.SCHEMA_ATTRIBUTE_TYPE_NAME)).thenReturn(Optional.of(schemaAttributeEntity));
        when(dataEngineRegistrationHandler.getExternalDataEngine(USER, EXTERNAL_SOURCE_DE_QUALIFIED_NAME)).thenReturn(EXTERNAL_SOURCE_DE_GUID);

        Attribute attribute = getAttribute();
        when(dataEngineCommonHandler.getSortOrder(attribute)).thenReturn(99);

        EntityDetail schemaAttributeUpdatedEntity = mock(EntityDetail.class);
        when(dataEngineCommonHandler.buildEntityDetail(ATTRIBUTE_GUID, null)).thenReturn(schemaAttributeUpdatedEntity);
        EntityDetailDifferences attributeDifferences = mock(EntityDetailDifferences.class);
        when((attributeDifferences.hasInstancePropertiesDifferences())).thenReturn(Boolean.TRUE);
        when(repositoryHelper.getEntityDetailDifferences(schemaAttributeEntity, schemaAttributeUpdatedEntity, true))
                .thenReturn(attributeDifferences);

        dataEngineSchemaAttributeHandler.upsertSchemaAttributes(USER, Collections.singletonList(attribute),
                EXTERNAL_SOURCE_DE_QUALIFIED_NAME, EXTERNAL_SOURCE_DE_GUID, GUID);

        verify(schemaAttributeHandler, times(1)).updateSchemaAttribute(USER, EXTERNAL_SOURCE_DE_GUID,
                EXTERNAL_SOURCE_DE_QUALIFIED_NAME, ATTRIBUTE_GUID, null, false, false, null, methodName);
    }

    private Attribute getAttribute() {
        Attribute attribute = new Attribute();
        attribute.setQualifiedName(ATTRIBUTE_QUALIFIED_NAME);
        attribute.setDisplayName(ATTRIBUTE_DISPLAY_NAME);

        return attribute;
    }

}