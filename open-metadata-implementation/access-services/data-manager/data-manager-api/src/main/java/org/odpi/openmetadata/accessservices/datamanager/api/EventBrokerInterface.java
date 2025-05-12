/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.datamanager.api;


import org.odpi.openmetadata.accessservices.datamanager.properties.*;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.*;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.topics.TopicProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.schema.events.EventTypeProperties;

import java.util.List;

/**
 * EventBrokerInterface defines the client side interface for the Data Manager OMAS that is
 * relevant for topic assets that provide event-based services.   It provides the ability to
 * define and maintain the metadata about a topic and the schemas (event payloads) it contains.
 */
public interface EventBrokerInterface
{

    /*
     * The topic is the top level asset in an event manager
     */

    /**
     * Create a new metadata element to represent a topic.
     *
     * @param userId calling user
     * @param eventBrokerGUID unique identifier of software server capability representing the caller
     * @param eventBrokerName unique name of software server capability representing the caller
     * @param eventBrokerIsHome should the topic be marked as owned by the event broker so others can not update?
     * @param topicProperties properties to store
     *
     * @return unique identifier of the new metadata element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    String createTopic(String          userId,
                       String          eventBrokerGUID,
                       String          eventBrokerName,
                       boolean         eventBrokerIsHome,
                       TopicProperties topicProperties) throws InvalidParameterException,
                                                               UserNotAuthorizedException,
                                                               PropertyServerException;


    /**
     * Create a new metadata element to represent a topic using an existing metadata element as a template.
     *
     * @param userId calling user
     * @param eventBrokerGUID unique identifier of software server capability representing the caller
     * @param eventBrokerName unique name of software server capability representing the caller
     * @param eventBrokerIsHome should the topic be marked as owned by the event broker so others can not update?
     * @param templateGUID unique identifier of the metadata element to copy
     * @param templateProperties properties that override the template
     *
     * @return unique identifier of the new metadata element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    String createTopicFromTemplate(String             userId,
                                   String             eventBrokerGUID,
                                   String             eventBrokerName,
                                   boolean            eventBrokerIsHome,
                                   String             templateGUID,
                                   TemplateProperties templateProperties) throws InvalidParameterException,
                                                                                 UserNotAuthorizedException,
                                                                                 PropertyServerException;


    /**
     * Update the metadata element representing a topic.
     *
     * @param userId calling user
     * @param eventBrokerGUID unique identifier of software server capability representing the caller
     * @param eventBrokerName unique name of software server capability representing the caller
     * @param topicGUID unique identifier of the metadata element to update
     * @param isMergeUpdate are unspecified properties unchanged (true) or removed?
     * @param topicProperties new properties for this element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    void updateTopic(String          userId,
                     String          eventBrokerGUID,
                     String          eventBrokerName,
                     String          topicGUID,
                     boolean         isMergeUpdate,
                     TopicProperties topicProperties) throws InvalidParameterException,
                                                             UserNotAuthorizedException,
                                                             PropertyServerException;


    /**
     * Update the zones for the topic asset so that it becomes visible to consumers.
     * (The zones are set to the list of zones in the publishedZones option configured for each
     * instance of the Data Manager OMAS).
     *
     * @param userId calling user
     * @param topicGUID unique identifier of the metadata element to publish
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    void publishTopic(String userId,
                      String topicGUID) throws InvalidParameterException,
                                               UserNotAuthorizedException,
                                               PropertyServerException;


    /**
     * Update the zones for the topic asset so that it is no longer visible to consumers.
     * (The zones are set to the list of zones in the defaultZones option configured for each
     * instance of the Data Manager OMAS.  This is the setting when the topic is first created).
     *
     * @param userId calling user
     * @param topicGUID unique identifier of the metadata element to withdraw
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    void withdrawTopic(String userId,
                       String topicGUID) throws InvalidParameterException,
                                                UserNotAuthorizedException,
                                                PropertyServerException;


    /**
     * Remove the metadata element representing a topic.
     *
     * @param userId calling user
     * @param eventBrokerGUID unique identifier of software server capability representing the caller
     * @param eventBrokerName unique name of software server capability representing the caller
     * @param topicGUID unique identifier of the metadata element to remove
     * @param qualifiedName unique name of the metadata element to remove
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    void removeTopic(String userId,
                     String eventBrokerGUID,
                     String eventBrokerName,
                     String topicGUID,
                     String qualifiedName) throws InvalidParameterException,
                                                  UserNotAuthorizedException,
                                                  PropertyServerException;


    /**
     * Retrieve the list of topic metadata elements that contain the search string.
     * The search string is treated as a regular expression.
     *
     * @param userId calling user
     * @param searchString string to find in the properties
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return list of matching metadata elements
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    List<TopicElement> findTopics(String userId,
                                  String searchString,
                                  int    startFrom,
                                  int    pageSize) throws InvalidParameterException,
                                                          UserNotAuthorizedException,
                                                          PropertyServerException;


    /**
     * Retrieve the list of topic metadata elements with a matching qualified or display name.
     * There are no wildcards supported on this request.
     *
     * @param userId calling user
     * @param name name to search for
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return list of matching metadata elements
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    List<TopicElement> getTopicsByName(String userId,
                                       String name,
                                       int    startFrom,
                                       int    pageSize) throws InvalidParameterException,
                                                               UserNotAuthorizedException,
                                                               PropertyServerException;


    /**
     * Retrieve the list of topics created by this caller.
     *
     * @param userId calling user
     * @param eventBrokerGUID unique identifier of software server capability representing the topic manager (event broker)
     * @param eventBrokerName unique name of software server capability representing the topic manager (event broker)
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return list of matching metadata elements
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    List<TopicElement> getTopicsForEventBroker(String userId,
                                               String eventBrokerGUID,
                                               String eventBrokerName,
                                               int    startFrom,
                                               int    pageSize) throws InvalidParameterException,
                                                                        UserNotAuthorizedException,
                                                                        PropertyServerException;


    /**
     * Retrieve the topic metadata element with the supplied unique identifier.
     *
     * @param userId calling user
     * @param guid unique identifier of the requested metadata element
     *
     * @return matching metadata element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    TopicElement getTopicByGUID(String userId,
                                String guid) throws InvalidParameterException,
                                                    UserNotAuthorizedException,
                                                    PropertyServerException;


    /*
     * A topic may support one or more types of event depending on its capability
     */

    /**
     * Create a new metadata element to represent an event type.  This describes the structure of an event supported by
     * the topic. The structure of this event type is added using SchemaAttributes.   These SchemaAttributes can have
     * a simple type or a nested structure.
     *
     * @param userId calling user
     * @param eventBrokerGUID unique identifier of software server capability representing the caller
     * @param eventBrokerName unique name of software server capability representing the caller
     * @param topicGUID unique identifier of a topic
     * @param properties properties about the topic schema
     *
     * @return unique identifier of the new topic schema
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    String createEventType(String              userId,
                           String              eventBrokerGUID,
                           String              eventBrokerName,
                           String              topicGUID,
                           EventTypeProperties properties) throws InvalidParameterException,
                                                                  UserNotAuthorizedException,
                                                                  PropertyServerException;


    /**
     * Create a new metadata element to represent a an event type using an existing event type as a template.
     *
     * @param userId calling user
     * @param eventBrokerGUID unique identifier of software server capability representing the caller
     * @param eventBrokerName unique name of software server capability representing the caller
     * @param templateGUID unique identifier of the metadata element to copy
     * @param topicGUID unique identifier of the topic where the event type is located
     * @param templateProperties properties that override the template
     *
     * @return unique identifier of the new event type
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    String createEventTypeFromTemplate(String             userId,
                                       String             eventBrokerGUID,
                                       String             eventBrokerName,
                                       String             templateGUID,
                                       String             topicGUID,
                                       TemplateProperties templateProperties) throws InvalidParameterException,
                                                                                     UserNotAuthorizedException,
                                                                                     PropertyServerException;


    /**
     * Update the metadata element representing an event type.
     *
     * @param userId calling user
     * @param eventBrokerGUID unique identifier of software server capability representing the caller
     * @param eventBrokerName unique name of software server capability representing the caller
     * @param eventTypeGUID unique identifier of the metadata element to update
     * @param isMergeUpdate are unspecified properties unchanged (true) or removed?
     * @param properties new properties for the metadata element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    void updateEventType(String              userId,
                         String              eventBrokerGUID,
                         String              eventBrokerName,
                         String              eventTypeGUID,
                         boolean             isMergeUpdate,
                         EventTypeProperties properties) throws InvalidParameterException,
                                                                UserNotAuthorizedException,
                                                                PropertyServerException;


    /**
     * Remove an event type.
     *
     * @param userId calling user
     * @param eventBrokerGUID unique identifier of software server capability representing the caller
     * @param eventBrokerName unique name of software server capability representing the caller
     * @param eventTypeGUID unique identifier of the metadata element to remove
     * @param qualifiedName unique name of the metadata element to remove
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    void removeEventType(String userId,
                         String eventBrokerGUID,
                         String eventBrokerName,
                         String eventTypeGUID,
                         String qualifiedName) throws InvalidParameterException,
                                                      UserNotAuthorizedException,
                                                      PropertyServerException;


    /**
     * Retrieve the list of event types metadata elements that contain the search string.
     * The search string is treated as a regular expression.
     *
     * @param userId calling user
     * @param searchString string to find in the properties
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return list of matching metadata elements
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    List<EventTypeElement> findEventTypes(String userId,
                                          String searchString,
                                          int    startFrom,
                                          int    pageSize) throws InvalidParameterException,
                                                                  UserNotAuthorizedException,
                                                                  PropertyServerException;


    /**
     * Return the list of event types associated with an EventSet.  This is a collection of EventType definitions.
     * These event types can be used as a template for adding the event types to a topic.
     *
     * @param userId calling user
     * @param eventSetGUID unique identifier of the topic to query
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return list of metadata elements describing the event types associated with the requested EventSet
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    List<EventTypeElement> getEventTypesForEventSet(String userId,
                                                    String eventSetGUID,
                                                    int    startFrom,
                                                    int    pageSize) throws InvalidParameterException,
                                                                            UserNotAuthorizedException,
                                                                            PropertyServerException;

    /**
     * Return the list of event types associated with a topic.
     *
     * @param userId calling user
     * @param topicGUID unique identifier of the topic to query
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return list of metadata elements describing the event types associated with the requested topic
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    List<EventTypeElement> getEventTypesForTopic(String userId,
                                                 String topicGUID,
                                                 int    startFrom,
                                                 int    pageSize) throws InvalidParameterException,
                                                                         UserNotAuthorizedException,
                                                                         PropertyServerException;


    /**
     * Retrieve the list of event types metadata elements with a matching qualified or display name.
     * There are no wildcards supported on this request.
     *
     * @param userId calling user
     * @param name name to search for
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return list of matching metadata elements
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    List<EventTypeElement> getEventTypesByName(String userId,
                                               String name,
                                               int    startFrom,
                                               int    pageSize) throws InvalidParameterException,
                                                                       UserNotAuthorizedException,
                                                                       PropertyServerException;


    /**
     * Retrieve the event type metadata element with the supplied unique identifier.
     *
     * @param userId calling user
     * @param guid unique identifier of the requested metadata element
     *
     * @return requested metadata element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    EventTypeElement getEventTypeByGUID(String userId,
                                        String guid) throws InvalidParameterException,
                                                            UserNotAuthorizedException,
                                                            PropertyServerException;
}
