/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.dataengine;

import org.odpi.openmetadata.accessservices.dataengine.client.DataEngineClient;
import org.odpi.openmetadata.accessservices.dataengine.model.Attribute;
import org.odpi.openmetadata.accessservices.dataengine.model.EventType;
import org.odpi.openmetadata.accessservices.dataengine.model.Topic;
import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectorCheckedException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;

import java.util.Collections;

/**
 * Generates test data of type Topic and EventType, and triggers requests via client for aforementioned types
 */
public class TopicAndEventTypeSetupService {
    /**
     * Upsert a Topic using the dataEngineClient received
     *
     * @param userId           user id
     * @param dataEngineClient data engine client
     * @param topic            topic to upsert. If null, a default will be used
     *
     * @return Topic instance containing sent values
     */
    public Topic upsertTopic(String userId, DataEngineClient dataEngineClient, Topic topic) throws UserNotAuthorizedException,
                                                                                                   ConnectorCheckedException,
                                                                                                   PropertyServerException,
                                                                                                   InvalidParameterException {
        if (topic == null) {
            topic = getDefaultTopic();
        }
        dataEngineClient.upsertTopic(userId, topic);
        return topic;
    }

    /**
     * Delete a Topic using the dataEngineClient received
     *
     * @param userId           user id
     * @param dataEngineClient data engine client
     * @param qualifiedName    qualified name
     * @param guid             guid
     */
    public void deleteTopic(String userId, DataEngineClient dataEngineClient, String qualifiedName, String guid) throws UserNotAuthorizedException,
                                                                                                                        ConnectorCheckedException,
                                                                                                                        PropertyServerException,
                                                                                                                        InvalidParameterException {

        if (qualifiedName == null || guid == null) {
            throw new IllegalArgumentException("Unable to delete Topic. QualifiedName and Guid are both required. Missing at least one");
        }
        dataEngineClient.deleteTopic(userId, qualifiedName, guid);
    }

    /**
     * Upsert a DatabaseSchema using the dataEngineClient received
     *
     * @param userId                user id
     * @param dataEngineClient      data engine client
     * @param eventType        eventType to upsert. If null, a default will be used
     * @param topicQualifiedName the topic's qualified name
     *
     * @return EventType instance containing sent values
     */
    public EventType upsertEventType(String userId, DataEngineClient dataEngineClient, EventType eventType, String topicQualifiedName) throws
                                                                                                                                            UserNotAuthorizedException,
                                                                                                                                            ConnectorCheckedException,
                                                                                                                                            PropertyServerException,
                                                                                                                                            InvalidParameterException {
        if (eventType == null) {
            eventType = getDefaultEventType();
        }
        dataEngineClient.upsertEventType(userId, eventType, topicQualifiedName);
        return eventType;
    }

    /**
     * Delete an EventType using the dataEngineClient received
     *
     * @param userId           user id
     * @param dataEngineClient data engine client
     * @param qualifiedName    qualified name
     * @param guid             guid
     */
    public void deleteEventType(String userId, DataEngineClient dataEngineClient, String qualifiedName, String guid)
            throws UserNotAuthorizedException, ConnectorCheckedException, PropertyServerException, InvalidParameterException {

        if (qualifiedName == null || guid == null) {
            throw new IllegalArgumentException("Unable to delete DatabaseSchema. QualifiedName and Guid are both required. Missing at least one");
        }
        dataEngineClient.deleteEventType(userId, qualifiedName, guid);
    }

    private Topic getDefaultTopic() {
        Topic topic = new Topic();

        topic.setQualifiedName("default-topic-qualified-name");
        topic.setDisplayName("default-topic-display-name");
        topic.setDescription("default-topic-description");
        topic.setTopicType("default-topic-type");

        return topic;
    }

    private EventType getDefaultEventType() {
        EventType eventType = new EventType();

        eventType.setQualifiedName("default-event-type-qualified-name");
        eventType.setDisplayName("default-event-type-display-name");
        eventType.setDescription("default-event-type-description");
        eventType.setAttributeList(Collections.singletonList(getDefaultEventSchemaAttribute()));

        return eventType;
    }

    private Attribute getDefaultEventSchemaAttribute() {
        Attribute attribute = new Attribute();

        attribute.setQualifiedName("default-event-schema-attribute-qualified-name");
        attribute.setDisplayName("default-vent-schema-attribute-display-name");

        return attribute;
    }

    public Topic getTopicWithEventType() {
        Topic topic = new Topic();
        topic.setQualifiedName("topic-qualified-name");
        topic.setDisplayName("topic-display-name");
        topic.setDescription("topic-description");
        topic.setTopicType("topic-type");

        EventType eventType = new EventType();
        eventType.setDisplayName("event-type-display-name");
        eventType.setQualifiedName("event-type-qualified-name");
        eventType.setDescription("event-type-description");

        Attribute attribute = new Attribute();
        attribute.setDisplayName("attribute-display-name");
        attribute.setQualifiedName("attribute-qualified-name");

        eventType.setAttributeList(Collections.singletonList(attribute));
        topic.setEventTypes(Collections.singletonList(eventType));

        return topic;
    }

    public Topic getDeleteTopic() {
        Topic topic = new Topic();
        topic.setQualifiedName("topic-to-delete-qualified-name");
        topic.setDisplayName("topic-to-delete-display-name");
        topic.setDescription("topic-to-delete-description");
        topic.setTopicType("topic-type");

        return topic;
    }

    public EventType getDeleteEventType() {
        EventType eventType = new EventType();
        eventType.setDisplayName("event-type-to-delete-display-name");
        eventType.setQualifiedName("event-type--to-delete-qualified-name");
        eventType.setDescription("event-type--to-delete-description");

        Attribute attribute = new Attribute();
        attribute.setDisplayName("attribute--to-delete-display-name");
        attribute.setQualifiedName("attribute--to-delete-qualified-name");

        eventType.setAttributeList(Collections.singletonList(attribute));
        return eventType;
    }
}
