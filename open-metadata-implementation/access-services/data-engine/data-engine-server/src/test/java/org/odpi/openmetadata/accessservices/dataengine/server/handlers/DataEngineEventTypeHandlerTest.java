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
import org.odpi.openmetadata.accessservices.dataengine.model.DeleteSemantic;
import org.odpi.openmetadata.accessservices.dataengine.model.EventType;
import org.odpi.openmetadata.commonservices.ffdc.InvalidParameterHandler;
import org.odpi.openmetadata.commonservices.generichandlers.EventTypeHandler;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.FunctionNotSupportedException;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.internal.verification.VerificationModeFactory.times;
import static org.odpi.openmetadata.accessservices.dataengine.server.handlers.DataEngineEventTypeHandler.EVENT_TYPE_GUID_PARAMETER_NAME;
import static org.odpi.openmetadata.accessservices.dataengine.server.handlers.DataEngineTopicHandler.TOPIC_GUID_PARAMETER_NAME;
import static org.odpi.openmetadata.commonservices.generichandlers.OpenMetadataAPIMapper.EVENT_SCHEMA_ATTRIBUTE_TYPE_NAME;
import static org.odpi.openmetadata.commonservices.generichandlers.OpenMetadataAPIMapper.EVENT_TYPE_TYPE_NAME;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.WARN)
class DataEngineEventTypeHandlerTest {
    private static final String USER = "user";
    private static final String QUALIFIED_NAME = "qualifiedName";
    private static final String DISPLAY_NAME = "name";
    private static final String EXTERNAL_SOURCE_DE_GUID = "externalSourceDataEngineGuid";
    private static final String EXTERNAL_SOURCE_DE_QUALIFIED_NAME = "externalSourceDataEngineQualifiedName";
    private static final String GUID = "guid";
    private static final String EVENT_SCHEMA_QUALIFIED_NAME = "eventSchemaAttributeQName";
    private static final String EVENT_SCHEMA_ATTRIBUTE_NAME = "eventSchemaAttributeName";
    private static final String TOPIC_GUID = "topicGUID";

    @Mock
    private DataEngineCommonHandler dataEngineCommonHandler;

    @Mock
    private DataEngineRegistrationHandler dataEngineRegistrationHandler;

    @Mock
    private EventTypeHandler<EventType> eventTypeHandler;

    @Mock
    private InvalidParameterHandler invalidParameterHandler;

    @Mock
    private DataEngineSchemaAttributeHandler dataEngineSchemaAttributeHandler;

    @InjectMocks
    DataEngineEventTypeHandler dataEngineEventTypeHandler;

    @Test
    void upsertEventType_create() throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException {
        when(dataEngineRegistrationHandler.getExternalDataEngine(USER, EXTERNAL_SOURCE_DE_QUALIFIED_NAME)).thenReturn(EXTERNAL_SOURCE_DE_GUID);
        when(dataEngineCommonHandler.findEntity(USER, QUALIFIED_NAME, EVENT_TYPE_TYPE_NAME)).thenReturn(Optional.empty());

        when(eventTypeHandler.createEventType(USER, EXTERNAL_SOURCE_DE_GUID, EXTERNAL_SOURCE_DE_QUALIFIED_NAME,
                TOPIC_GUID, TOPIC_GUID_PARAMETER_NAME, QUALIFIED_NAME, DISPLAY_NAME, null, null, false,
                null, null, null, null, null, EVENT_TYPE_TYPE_NAME, null,
                null, null, false, false, null,
                "upsertEventType")).thenReturn(GUID);
        EventType eventType = getEventType();
        List<Attribute> attributeList = Collections.singletonList(getAttribute());
        eventType.setAttributeList(attributeList);

        dataEngineEventTypeHandler.upsertEventType(USER, eventType, TOPIC_GUID, EXTERNAL_SOURCE_DE_QUALIFIED_NAME);

        verify(eventTypeHandler, times(1)).createEventType(USER, EXTERNAL_SOURCE_DE_GUID, EXTERNAL_SOURCE_DE_QUALIFIED_NAME,
                TOPIC_GUID, TOPIC_GUID_PARAMETER_NAME, QUALIFIED_NAME, DISPLAY_NAME, null, null, false,
                null, null, null, null, null, EVENT_TYPE_TYPE_NAME, null,
                null, null, false, false, null,
                "upsertEventType");
        verify(dataEngineSchemaAttributeHandler, Mockito.times(1)).upsertSchemaAttributes(USER, attributeList,
                EXTERNAL_SOURCE_DE_QUALIFIED_NAME, EXTERNAL_SOURCE_DE_GUID, GUID);
    }

    @Test
    void upsertEventType_update() throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException {
        when(dataEngineRegistrationHandler.getExternalDataEngine(USER, EXTERNAL_SOURCE_DE_QUALIFIED_NAME)).thenReturn(EXTERNAL_SOURCE_DE_GUID);
        EntityDetail eventTypeDetails = mockEntityDetail(GUID);
        when(dataEngineCommonHandler.findEntity(USER, QUALIFIED_NAME, EVENT_TYPE_TYPE_NAME)).thenReturn(Optional.of(eventTypeDetails));

        EventType eventType = getEventType();
        List<Attribute> attributeList = Collections.singletonList(getAttribute());
        eventType.setAttributeList(attributeList);

        dataEngineEventTypeHandler.upsertEventType(USER, eventType, TOPIC_GUID, EXTERNAL_SOURCE_DE_QUALIFIED_NAME);

        verify(eventTypeHandler, times(1)).updateEventType(USER, EXTERNAL_SOURCE_DE_GUID, EXTERNAL_SOURCE_DE_QUALIFIED_NAME,
                GUID, EVENT_TYPE_GUID_PARAMETER_NAME, QUALIFIED_NAME, DISPLAY_NAME, null, null, false,
                null, null, null, null, null, EVENT_TYPE_TYPE_NAME, null,
                null, null, true,false, false, null,
                "upsertEventType");
        verify(dataEngineSchemaAttributeHandler, Mockito.times(1)).upsertSchemaAttributes(USER, attributeList,
                EXTERNAL_SOURCE_DE_QUALIFIED_NAME, EXTERNAL_SOURCE_DE_GUID, GUID);
    }

    @Test
    void removeEventType() throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException, FunctionNotSupportedException {
        when(dataEngineRegistrationHandler.getExternalDataEngine(USER, EXTERNAL_SOURCE_DE_QUALIFIED_NAME)).thenReturn(EXTERNAL_SOURCE_DE_GUID);

        dataEngineEventTypeHandler.removeEventType(USER, GUID, QUALIFIED_NAME, EXTERNAL_SOURCE_DE_QUALIFIED_NAME, DeleteSemantic.SOFT);

        verify(dataEngineCommonHandler, times(1)).validateDeleteSemantic(DeleteSemantic.SOFT, "removeEventType");
        verify(eventTypeHandler, times(1)).removeEventType(USER, EXTERNAL_SOURCE_DE_GUID, EXTERNAL_SOURCE_DE_QUALIFIED_NAME,
                GUID, EVENT_TYPE_GUID_PARAMETER_NAME, QUALIFIED_NAME, false, false, null, "removeEventType");
    }

    private EventType getEventType() {
        EventType eventType = new EventType();
        eventType.setQualifiedName(QUALIFIED_NAME);
        eventType.setDisplayName(DISPLAY_NAME);

        return eventType;
    }

    private Attribute getAttribute() {
        Attribute attribute = new Attribute();
        attribute.setTypeName(EVENT_SCHEMA_ATTRIBUTE_TYPE_NAME);
        attribute.setQualifiedName(EVENT_SCHEMA_QUALIFIED_NAME);
        attribute.setDisplayName(EVENT_SCHEMA_ATTRIBUTE_NAME);

        return attribute;
    }

    private EntityDetail mockEntityDetail(String guid) {
        EntityDetail entityDetail = mock(EntityDetail.class);
        when(entityDetail.getGUID()).thenReturn(guid);
        return entityDetail;
    }
}