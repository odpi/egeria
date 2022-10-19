/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.dataengine.fvt;


import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.odpi.openmetadata.accessservices.dataengine.RepositoryService;
import org.odpi.openmetadata.accessservices.dataengine.client.DataEngineClient;
import org.odpi.openmetadata.accessservices.dataengine.model.Attribute;
import org.odpi.openmetadata.accessservices.dataengine.model.EventType;
import org.odpi.openmetadata.accessservices.dataengine.model.Topic;
import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectorCheckedException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.EntityNotKnownException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.FunctionNotSupportedException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.PagingErrorException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.PropertyErrorException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.RepositoryErrorException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.TypeErrorException;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNull;

/**
 * Holds FVTs related to types Database, RelationalTable and DataFile
 */
public class TopicAndEventTypeFVT extends DataEngineFVT {

    @ParameterizedTest
    @MethodSource("org.odpi.openmetadata.accessservices.dataengine.PlatformConnectionProvider#getConnectionDetails")
    public void upsertTopicWithEventType(String userId, DataEngineClient dataEngineClient, RepositoryService repositoryService)
            throws UserNotAuthorizedException, ConnectorCheckedException, PropertyServerException, InvalidParameterException,
                   org.odpi.openmetadata.repositoryservices.ffdc.exception.UserNotAuthorizedException, FunctionNotSupportedException,
                   org.odpi.openmetadata.repositoryservices.ffdc.exception.InvalidParameterException, RepositoryErrorException,
                   PropertyErrorException, TypeErrorException, PagingErrorException, EntityNotKnownException, InterruptedException {

        engineSetupService.createExternalDataEngine(userId, dataEngineClient, null);
        Topic topic = topicAndEventTypeSetupService.upsertTopic(userId, dataEngineClient, topicAndEventTypeSetupService.getTopicWithEventType());

        // assert Topic
        List<EntityDetail> topics = repositoryService.findEntityByPropertyValue(TOPIC_TYPE_GUID, topic.getQualifiedName());
        EntityDetail topicAsEntityDetail = assertTopic(topic, topics);

        EventType eventType = topic.getEventTypes().get(0);
        // assert EventType
        EntityDetail eventTypeAsEntityDetail = repositoryService.findEntityByQualifiedName(eventType.getQualifiedName(), EVENT_TYPE_TYPE_GUID);
        assertEventType(eventType, eventTypeAsEntityDetail);

        // assert EventSchemaAttributes
        Attribute attribute = eventType.getAttributeList().get(0);
        EntityDetail attributeAsEntityDetail = repositoryService.findEntityByQualifiedName(attribute.getQualifiedName(),
                EVENT_SCHEMA_ATTRIBUTE_TYPE_GUID);
        assertEventSchemaAttribute(attribute, attributeAsEntityDetail);
    }

    @ParameterizedTest
    @MethodSource("org.odpi.openmetadata.accessservices.dataengine.PlatformConnectionProvider#getConnectionDetails")
    public void deleteTopic(String userId, DataEngineClient dataEngineClient, RepositoryService repositoryService)
            throws UserNotAuthorizedException, ConnectorCheckedException, PropertyServerException, InvalidParameterException,
                   org.odpi.openmetadata.repositoryservices.ffdc.exception.UserNotAuthorizedException, FunctionNotSupportedException,
                   org.odpi.openmetadata.repositoryservices.ffdc.exception.InvalidParameterException, RepositoryErrorException,
                   PropertyErrorException, TypeErrorException, PagingErrorException, EntityNotKnownException, InterruptedException {

        engineSetupService.createExternalDataEngine(userId, dataEngineClient, null);
        Topic topic = topicAndEventTypeSetupService.upsertTopic(userId, dataEngineClient, topicAndEventTypeSetupService.getDeleteTopic());

        // assert Topic
        List<EntityDetail> topics = repositoryService.findEntityByPropertyValue(TOPIC_TYPE_GUID, topic.getQualifiedName());
        EntityDetail topicAsEntityDetail = assertTopic(topic, topics);

        // delete Topic
        topicAndEventTypeSetupService.deleteTopic(userId, dataEngineClient, topic.getQualifiedName(), topicAsEntityDetail.getGUID());
        EntityDetail topicAfterDelete = repositoryService.findEntityByQualifiedName(topic.getQualifiedName(), TOPIC_TYPE_GUID);
        assertNull(topicAfterDelete);
    }

    @ParameterizedTest
    @MethodSource("org.odpi.openmetadata.accessservices.dataengine.PlatformConnectionProvider#getConnectionDetails")
    public void upserEventType(String userId, DataEngineClient dataEngineClient, RepositoryService repositoryService)
            throws UserNotAuthorizedException, ConnectorCheckedException, PropertyServerException, InvalidParameterException,
                   org.odpi.openmetadata.repositoryservices.ffdc.exception.UserNotAuthorizedException, FunctionNotSupportedException,
                   org.odpi.openmetadata.repositoryservices.ffdc.exception.InvalidParameterException, RepositoryErrorException,
                   PropertyErrorException, TypeErrorException, PagingErrorException, EntityNotKnownException, InterruptedException {

        engineSetupService.createExternalDataEngine(userId, dataEngineClient, null);
        Topic topic = topicAndEventTypeSetupService.upsertTopic(userId, dataEngineClient, null);
        EventType eventType = topicAndEventTypeSetupService.upsertEventType(userId, dataEngineClient, null, topic.getQualifiedName());

        // assert EventType
        EntityDetail eventTypeAsEntityDetail = repositoryService.findEntityByQualifiedName(eventType.getQualifiedName(), EVENT_TYPE_TYPE_GUID);
        assertEventType(eventType, eventTypeAsEntityDetail);

        // assert EventSchemaAttributes
        Attribute attribute = eventType.getAttributeList().get(0);
        EntityDetail attributeAsEntityDetail = repositoryService.findEntityByQualifiedName(attribute.getQualifiedName(),
                EVENT_SCHEMA_ATTRIBUTE_TYPE_GUID);
        assertEventSchemaAttribute(attribute, attributeAsEntityDetail);
    }

    @ParameterizedTest
    @MethodSource("org.odpi.openmetadata.accessservices.dataengine.PlatformConnectionProvider#getConnectionDetails")
    public void upsertAndDeleteEventType(String userId, DataEngineClient dataEngineClient, RepositoryService repositoryService)
            throws UserNotAuthorizedException, ConnectorCheckedException, PropertyServerException, InvalidParameterException,
                   org.odpi.openmetadata.repositoryservices.ffdc.exception.UserNotAuthorizedException, FunctionNotSupportedException,
                   org.odpi.openmetadata.repositoryservices.ffdc.exception.InvalidParameterException, RepositoryErrorException,
                   PropertyErrorException, TypeErrorException, PagingErrorException, EntityNotKnownException, InterruptedException {

        engineSetupService.createExternalDataEngine(userId, dataEngineClient, null);
        Topic topic = topicAndEventTypeSetupService.upsertTopic(userId, dataEngineClient, null);
        EventType eventType = topicAndEventTypeSetupService.upsertEventType(userId, dataEngineClient,
                topicAndEventTypeSetupService.getDeleteEventType(), topic.getQualifiedName());

        // assert EventType
        EntityDetail eventTypeAsEntityDetail = repositoryService.findEntityByQualifiedName(eventType.getQualifiedName(), EVENT_TYPE_TYPE_GUID);
        assertEventType(eventType, eventTypeAsEntityDetail);

        // assert EventSchemaAttributes
        Attribute attribute = eventType.getAttributeList().get(0);
        EntityDetail attributeAsEntityDetail = repositoryService.findEntityByQualifiedName(attribute.getQualifiedName(),
                EVENT_SCHEMA_ATTRIBUTE_TYPE_GUID);
        assertEventSchemaAttribute(attribute, attributeAsEntityDetail);

        // delete EventType
        topicAndEventTypeSetupService.deleteEventType(userId, dataEngineClient, eventType.getQualifiedName(), eventTypeAsEntityDetail.getGUID());
        // wait for the linked elements to be deleted
        Thread.sleep(3000);

        EntityDetail eventTypeAfterDelete = repositoryService.findEntityByQualifiedName(eventType.getQualifiedName(), EVENT_TYPE_TYPE_GUID);
        assertNull(eventTypeAfterDelete);
        EntityDetail eventSchemaAttributeAfterDelete = repositoryService.findEntityByQualifiedName(attribute.getQualifiedName(),
                EVENT_SCHEMA_ATTRIBUTE_TYPE_GUID);
        assertNull(eventSchemaAttributeAfterDelete);
    }
}
