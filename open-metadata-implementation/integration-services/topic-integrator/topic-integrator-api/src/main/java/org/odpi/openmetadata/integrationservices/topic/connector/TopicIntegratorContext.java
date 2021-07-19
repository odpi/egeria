/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.integrationservices.topic.connector;

import org.odpi.openmetadata.accessservices.datamanager.api.DataManagerEventListener;
import org.odpi.openmetadata.accessservices.datamanager.client.EventBrokerClient;
import org.odpi.openmetadata.accessservices.datamanager.client.DataManagerEventClient;
import org.odpi.openmetadata.accessservices.datamanager.metadataelements.*;
import org.odpi.openmetadata.accessservices.datamanager.properties.*;
import org.odpi.openmetadata.frameworks.connectors.ffdc.*;

import java.util.List;


/**
 * TopicIntegratorContext is the context for cataloging topics from an event broker server.
 */
public class TopicIntegratorContext
{
    private EventBrokerClient      client;
    private DataManagerEventClient eventClient;
    private String                 userId;
    private String                 eventBrokerGUID;
    private String                 eventBrokerName;
    private boolean                eventBrokerIsHome = true;

    /**
     * Create a new client with no authentication embedded in the HTTP request.
     *
     * @param client client to map request to
     * @param eventClient client to register for events
     * @param userId integration daemon's userId
     * @param eventBrokerGUID unique identifier of the software server capability for the event broker
     * @param eventBrokerName unique name of the software server capability for the event broker
     */
    public TopicIntegratorContext(EventBrokerClient      client,
                                  DataManagerEventClient eventClient,
                                  String                 userId,
                                  String                 eventBrokerGUID,
                                  String                 eventBrokerName)
    {
        this.client           = client;
        this.eventClient      = eventClient;
        this.userId           = userId;
        this.eventBrokerGUID = eventBrokerGUID;
        this.eventBrokerName = eventBrokerName;
    }


    /* ========================================================
     * Set up whether topic/event metadata is owned by the event broker
     */


    /**
     * Set up the flag that controls the ownership of metadata created for this Event Broker. Default is true.
     *
     * @param eventBrokerIsHome should the topic metadata be marked as owned by the event broker so others can not update?
     */
    public void setEventBrokerIsHome(boolean eventBrokerIsHome)
    {
        this.eventBrokerIsHome = eventBrokerIsHome;
    }


    /* ========================================================
     * Register for inbound events from the Data Manager OMAS OutTopic
     */


    /**
     * Register a listener object that will be passed each of the events published by the Data Manager OMAS.
     *
     * @param listener listener object
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws ConnectionCheckedException there are errors in the configuration of the connection which is preventing
     *                                      the creation of a connector.
     * @throws ConnectorCheckedException there are errors in the initialization of the connector.
     * @throws PropertyServerException there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void registerListener(DataManagerEventListener listener) throws InvalidParameterException,
                                                                           ConnectionCheckedException,
                                                                           ConnectorCheckedException,
                                                                           PropertyServerException,
                                                                           UserNotAuthorizedException
    {
        eventClient.registerListener(userId, listener);
    }


    /* ========================================================
     * The topic is the top level asset on a topic server
     */


    /**
     * Create a new metadata element to represent a topic.
     *
     * @param topicProperties properties to store
     *
     * @return unique identifier of the new metadata element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public String createTopic(TopicProperties topicProperties) throws InvalidParameterException,
                                                                      UserNotAuthorizedException,
                                                                      PropertyServerException
    {
        return client.createTopic(userId, eventBrokerGUID, eventBrokerName, eventBrokerIsHome, topicProperties);
    }


    /**
     * Create a new metadata element to represent a topic using an existing metadata element as a template.
     *
     * @param templateGUID unique identifier of the metadata element to copy
     * @param templateProperties properties that override the template
     *
     * @return unique identifier of the new metadata element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public String createTopicFromTemplate(String             templateGUID,
                                          TemplateProperties templateProperties) throws InvalidParameterException,
                                                                                        UserNotAuthorizedException,
                                                                                        PropertyServerException
    {
        return client.createTopicFromTemplate(userId, eventBrokerGUID, eventBrokerName, eventBrokerIsHome, templateGUID, templateProperties);
    }


    /**
     * Update the metadata element representing a topic.
     *
     * @param topicGUID unique identifier of the metadata element to update
     * @param isMergeUpdate are unspecified properties unchanged (true) or removed?
     * @param topicProperties new properties for this element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void updateTopic(String          topicGUID,
                            boolean         isMergeUpdate,
                            TopicProperties topicProperties) throws InvalidParameterException,
                                                                    UserNotAuthorizedException,
                                                                    PropertyServerException
    {
        client.updateTopic(userId, eventBrokerGUID, eventBrokerName, topicGUID, isMergeUpdate, topicProperties);
    }


    /**
     * Update the zones for the topic asset so that it becomes visible to consumers.
     * (The zones are set to the list of zones in the publishedZones option configured for each
     * instance of the Data Manager OMAS).
     *
     * @param topicGUID unique identifier of the metadata element to publish
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void publishTopic(String topicGUID) throws InvalidParameterException,
                                                      UserNotAuthorizedException,
                                                      PropertyServerException
    {
        client.publishTopic(userId, topicGUID);
    }


    /**
     * Update the zones for the topic asset so that it is no longer visible to consumers.
     * (The zones are set to the list of zones in the defaultZones option configured for each
     * instance of the Data Manager OMAS.  This is the setting when the topic is first created).
     *
     * @param topicGUID unique identifier of the metadata element to withdraw
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void withdrawTopic(String topicGUID) throws InvalidParameterException,
                                                       UserNotAuthorizedException,
                                                       PropertyServerException
    {
        client.withdrawTopic(userId, topicGUID);
    }


    /**
     * Remove the metadata element representing a topic.
     *
     * @param topicGUID unique identifier of the metadata element to remove
     * @param qualifiedName unique name of the metadata element to remove
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void removeTopic(String topicGUID,
                            String qualifiedName) throws InvalidParameterException,
                                                         UserNotAuthorizedException,
                                                         PropertyServerException
    {
        client.removeTopic(userId, eventBrokerGUID, eventBrokerName, topicGUID, qualifiedName);
    }


    /**
     * Retrieve the list of topic metadata elements that contain the search string.
     * The search string is treated as a regular expression.
     *
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
    public List<TopicElement> findTopics(String searchString,
                                         int    startFrom,
                                         int    pageSize) throws InvalidParameterException,
                                                                 UserNotAuthorizedException,
                                                                 PropertyServerException
    {
        return client.findTopics(userId, searchString, startFrom, pageSize);
    }


    /**
     * Retrieve the list of topic metadata elements with a matching qualified or display name.
     * There are no wildcards supported on this request.
     *
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
    public List<TopicElement>   getTopicsByName(String name,
                                                int    startFrom,
                                                int    pageSize) throws InvalidParameterException,
                                                                        UserNotAuthorizedException,
                                                                        PropertyServerException
    {
        return client.getTopicsByName(userId, name, startFrom, pageSize);
    }


    /**
     * Retrieve the list of topics created by this caller.
     *
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return list of matching metadata elements
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<TopicElement>   getMyTopics(int    startFrom,
                                            int    pageSize) throws InvalidParameterException,
                                                                    UserNotAuthorizedException,
                                                                    PropertyServerException
    {
        return client.getTopicsForEventBroker(userId, eventBrokerGUID, eventBrokerName, startFrom, pageSize);
    }


    /**
     * Retrieve the topic metadata element with the supplied unique identifier.
     *
     * @param guid unique identifier of the requested metadata element
     *
     * @return matching metadata element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public TopicElement getTopicByGUID(String guid) throws InvalidParameterException,
                                                           UserNotAuthorizedException,
                                                           PropertyServerException
    {
        return client.getTopicByGUID(userId, guid);
    }


    /* ============================================================================
     * A topic may host one or more event types depending on its capability
     */

    /**
     * Create a new metadata element to represent a event type.
     *
     * @param topicGUID unique identifier of the topic where the event type is located
     * @param properties properties about the event type
     *
     * @return unique identifier of the new event type
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public String createEventType(String              topicGUID,
                                  EventTypeProperties properties) throws InvalidParameterException,
                                                                         UserNotAuthorizedException,
                                                                         PropertyServerException
    {
        return client.createEventType(userId, eventBrokerGUID, eventBrokerName, eventBrokerIsHome, topicGUID, properties);
    }



    /**
     * Create a new metadata element to represent a event type using an existing metadata element as a template.
     *
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
    public String createEventTypeFromTemplate(String             templateGUID,
                                              String             topicGUID,
                                              TemplateProperties templateProperties) throws InvalidParameterException,
                                                                                            UserNotAuthorizedException,
                                                                                            PropertyServerException
    {
        return client.createEventTypeFromTemplate(userId, eventBrokerGUID, eventBrokerName, eventBrokerIsHome, templateGUID, topicGUID, templateProperties);
    }


    /**
     * Update the metadata element representing a event type.
     *
     * @param eventTypeGUID unique identifier of the metadata element to update
     * @param isMergeUpdate are unspecified properties unchanged (true) or removed?
     * @param properties new properties for the metadata element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void updateEventType(String              eventTypeGUID,
                                boolean             isMergeUpdate,
                                EventTypeProperties properties) throws InvalidParameterException,
                                                                       UserNotAuthorizedException,
                                                                       PropertyServerException
    {
        client.updateEventType(userId, eventBrokerGUID, eventBrokerName, eventTypeGUID, isMergeUpdate, properties);
    }


    /**
     * Remove the metadata element representing a event type.
     *
     * @param eventTypeGUID unique identifier of the metadata element to remove
     * @param qualifiedName unique name of the metadata element to remove
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void removeEventType(String eventTypeGUID,
                                String qualifiedName) throws InvalidParameterException,
                                                             UserNotAuthorizedException,
                                                             PropertyServerException
    {
        client.removeEventType(userId, eventBrokerGUID, eventBrokerName, eventTypeGUID, qualifiedName);
    }


    /**
     * Retrieve the list of event type metadata elements that contain the search string.
     * The search string is treated as a regular expression.
     *
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
    public List<EventTypeElement>   findEventTypes(String searchString,
                                                   int    startFrom,
                                                   int    pageSize) throws InvalidParameterException,
                                                                           UserNotAuthorizedException,
                                                                           PropertyServerException
    {
        return client.findEventTypes(userId, searchString, startFrom, pageSize);
    }


    /**
     * Return the list of event types associated with a topic.
     *
     * @param eventSetGUID unique identifier of the topic to query
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return list of metadata elements describing the event types associated with the requested topic
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<EventTypeElement> getEventTypesForEventSet(String eventSetGUID,
                                                           int    startFrom,
                                                           int    pageSize) throws InvalidParameterException,
                                                                                   UserNotAuthorizedException,
                                                                                   PropertyServerException
    {
        return client.getEventTypesForEventSet(userId, eventSetGUID, startFrom, pageSize);
    }



    /**
     * Return the list of event types associated with a topic.
     *
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
    public List<EventTypeElement> getEventTypesForTopic(String topicGUID,
                                                        int    startFrom,
                                                        int    pageSize) throws InvalidParameterException,
                                                                                UserNotAuthorizedException,
                                                                                PropertyServerException
    {
        return client.getEventTypesForTopic(userId, topicGUID, startFrom, pageSize);
    }


    /**
     * Retrieve the list of event type metadata elements with a matching qualified or display name.
     * There are no wildcards supported on this request.
     *
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
    public List<EventTypeElement>   getEventTypesByName(String name,
                                                        int    startFrom,
                                                        int    pageSize) throws InvalidParameterException,
                                                                                UserNotAuthorizedException,
                                                                                PropertyServerException
    {
        return client.getEventTypesByName(userId, name, startFrom, pageSize);
    }


    /**
     * Retrieve the event type metadata element with the supplied unique identifier.
     *
     * @param guid unique identifier of the requested metadata element
     *
     * @return requested metadata element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public EventTypeElement getEventTypeByGUID(String guid) throws InvalidParameterException,
                                                                   UserNotAuthorizedException,
                                                                   PropertyServerException
    {
        return client.getEventTypeByGUID(userId, guid);
    }


    /* =====================================================================================================================
     * A schemaType is used to describe complex structures found in the schema of an event type
     */

    /**
     * Create a new metadata element to represent a primitive schema type such as a string, integer or character.
     *
     * @param schemaTypeProperties properties about the schema type to store
     *
     * @return unique identifier of the new schema type
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public String createPrimitiveSchemaType(PrimitiveSchemaTypeProperties schemaTypeProperties) throws InvalidParameterException,
                                                                                                       UserNotAuthorizedException,
                                                                                                       PropertyServerException
    {
        if (eventBrokerIsHome)
        {
            return client.createPrimitiveSchemaType(userId, eventBrokerGUID, eventBrokerName, schemaTypeProperties);
        }
        else
        {
            return client.createPrimitiveSchemaType(userId, null, null, schemaTypeProperties);
        }
    }


    /**
     * Create a new metadata element to represent a schema type that has a fixed value.
     *
     * @param schemaTypeProperties properties about the schema type to store
     *
     * @return unique identifier of the new schema type
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public String createLiteralSchemaType(LiteralSchemaTypeProperties schemaTypeProperties) throws InvalidParameterException,
                                                                                                   UserNotAuthorizedException,
                                                                                                   PropertyServerException
    {
        if (eventBrokerIsHome)
        {
            return client.createLiteralSchemaType(userId, eventBrokerGUID, eventBrokerName, schemaTypeProperties);
        }
        else
        {
            return client.createLiteralSchemaType(userId, null, null, schemaTypeProperties);
        }
    }


    /**
     * Create a new metadata element to represent a schema type that has a fixed set of values that are described by a valid value set.
     *
     * @param schemaTypeProperties properties about the schema type to store
     * @param validValuesSetGUID unique identifier of the valid values set to used
     *
     * @return unique identifier of the new schema type
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public String createEnumSchemaType(EnumSchemaTypeProperties schemaTypeProperties,
                                       String                   validValuesSetGUID) throws InvalidParameterException,
                                                                                           UserNotAuthorizedException,
                                                                                           PropertyServerException
    {
        if (eventBrokerIsHome)
        {
            return client.createEnumSchemaType(userId, eventBrokerGUID, eventBrokerName, schemaTypeProperties, validValuesSetGUID);
        }
        else
        {
            return client.createEnumSchemaType(userId, null, null, schemaTypeProperties, validValuesSetGUID);
        }
    }


    /**
     * Retrieve the list of valid value set metadata elements with a matching qualified or display name.
     * There are no wildcards supported on this request.
     *
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
    public List<ValidValueSetElement> getValidValueSetByName(String name,
                                                             int    startFrom,
                                                             int    pageSize) throws InvalidParameterException,
                                                                                     UserNotAuthorizedException,
                                                                                     PropertyServerException
    {
        return client.getValidValueSetByName(userId, name, startFrom, pageSize);
    }


    /**
     * Retrieve the list of valid value set metadata elements that contain the search string.
     * The search string is treated as a regular expression.
     *
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
    public List<ValidValueSetElement> findValidValueSet(String searchString,
                                                        int    startFrom,
                                                        int    pageSize) throws InvalidParameterException,
                                                                                UserNotAuthorizedException,
                                                                                PropertyServerException
    {
        return client.findValidValueSet(userId, searchString, startFrom, pageSize);
    }


    /**
     * Create a new metadata element to represent a schema type.
     *
     * @param schemaTypeProperties properties about the schema type to store
     *
     * @return unique identifier of the new schema type
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public String createStructSchemaType(StructSchemaTypeProperties schemaTypeProperties) throws InvalidParameterException,
                                                                                                 UserNotAuthorizedException,
                                                                                                 PropertyServerException
    {
        if (eventBrokerIsHome)
        {
            return client.createStructSchemaType(userId, eventBrokerGUID, eventBrokerName, schemaTypeProperties);
        }
        else
        {
            return client.createStructSchemaType(userId, null, null, schemaTypeProperties);
        }
    }


    /**
     * Create a new metadata element to represent a list of possible schema types that can be used for the attached schema attribute.
     *
     * @param schemaTypeProperties properties about the schema type to store
     *
     * @return unique identifier of the new schema type
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public String createSchemaTypeChoice(SchemaTypeChoiceProperties schemaTypeProperties,
                                         List<String>               schemaTypeOptionGUIDs) throws InvalidParameterException,
                                                                                                  UserNotAuthorizedException,
                                                                                                  PropertyServerException
    {
        if (eventBrokerIsHome)
        {
            return client.createSchemaTypeChoice(userId, eventBrokerGUID, eventBrokerName, schemaTypeProperties, schemaTypeOptionGUIDs);
        }
        else
        {
            return client.createSchemaTypeChoice(userId, null, null, schemaTypeProperties, schemaTypeOptionGUIDs);
        }
    }


    /**
     * Create a new metadata element to represent a schema type.
     *
     * @param schemaTypeProperties properties about the schema type to store
     * @param mapFromSchemaTypeGUID unique identifier of the the domain of the map
     * @param mapToSchemaTypeGUID unique identifier of the the range of the map
     *
     * @return unique identifier of the new schema type
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public String createMapSchemaType(MapSchemaTypeProperties schemaTypeProperties,
                                      String                  mapFromSchemaTypeGUID,
                                      String                  mapToSchemaTypeGUID) throws InvalidParameterException,
                                                                                          UserNotAuthorizedException,
                                                                                          PropertyServerException
    {
        if (eventBrokerIsHome)
        {
            return client.createMapSchemaType(userId, eventBrokerGUID, eventBrokerName, schemaTypeProperties, mapFromSchemaTypeGUID, mapToSchemaTypeGUID);
        }
        else
        {
            return client.createMapSchemaType(userId, null, null, schemaTypeProperties, mapFromSchemaTypeGUID, mapToSchemaTypeGUID);
        }
    }


    /**
     * Create a new metadata element to represent a schema type using an existing metadata element as a template.
     *
     * @param templateGUID unique identifier of the metadata element to copy
     * @param templateProperties properties that override the template
     *
     * @return unique identifier of the new schema type
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public String createSchemaTypeFromTemplate(String             templateGUID,
                                               TemplateProperties templateProperties) throws InvalidParameterException,
                                                                                             UserNotAuthorizedException,
                                                                                             PropertyServerException
    {
        if (eventBrokerIsHome)
        {
            return client.createSchemaTypeFromTemplate(userId, eventBrokerGUID, eventBrokerName, templateGUID, templateProperties);
        }
        else
        {
            return client.createSchemaTypeFromTemplate(userId, null, null, templateGUID, templateProperties);
        }
    }


    /**
     * Update the metadata element representing a schema type.  It is possible to use the subtype property classes or
     * set up specialized properties in extended properties.
     *
     * @param schemaTypeGUID unique identifier of the metadata element to update
     * @param isMergeUpdate should the new properties be merged with existing properties (true) or completely replace them (false)?
     * @param schemaTypeProperties new properties for the metadata element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void updateSchemaType(String               schemaTypeGUID,
                                 boolean              isMergeUpdate,
                                 SchemaTypeProperties schemaTypeProperties) throws InvalidParameterException,
                                                                                   UserNotAuthorizedException,
                                                                                   PropertyServerException
    {
        client.updateSchemaType(userId, eventBrokerGUID, eventBrokerName, schemaTypeGUID, isMergeUpdate, schemaTypeProperties);
    }


    /**
     * Remove the metadata element representing a schema type.
     *
     * @param schemaTypeGUID unique identifier of the metadata element to remove
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void removeSchemaType(String schemaTypeGUID) throws InvalidParameterException,
                                                               UserNotAuthorizedException,
                                                               PropertyServerException
    {
        client.removeSchemaType(userId, eventBrokerGUID, eventBrokerName, schemaTypeGUID);
    }


    /**
     * Retrieve the list of schema type metadata elements that contain the search string.
     * The search string is treated as a regular expression.
     *
     * @param searchString string to find in the properties
     * @param typeName optional type name for the schema type - used to restrict the search results
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return list of matching metadata elements
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<SchemaTypeElement> findSchemaType(String searchString,
                                                  String typeName,
                                                  int    startFrom,
                                                  int    pageSize) throws InvalidParameterException,
                                                                          UserNotAuthorizedException,
                                                                          PropertyServerException
    {
        return client.findSchemaType(userId, searchString, typeName, startFrom, pageSize);
    }


    /**
     * Return the schema type associated with a specific open metadata element (data asset, process or port).
     *
     * @param parentElementGUID unique identifier of the open metadata element that this schema type is connected to
     * @param parentElementTypeName unique type name of the open metadata element that this schema type is connected to
     *
     * @return metadata element describing the schema type associated with the requested parent element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public SchemaTypeElement getSchemaTypeForElement(String parentElementGUID,
                                                     String parentElementTypeName) throws InvalidParameterException,
                                                                                          UserNotAuthorizedException,
                                                                                          PropertyServerException
    {
        return client.getSchemaTypeForElement(userId, parentElementGUID, parentElementTypeName);
    }


    /**
     * Retrieve the list of schema type metadata elements with a matching qualified or display name.
     * There are no wildcards supported on this request.
     *
     * @param name name to search for
     * @param typeName optional type name for the schema type - used to restrict the search results
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return list of matching metadata elements
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<SchemaTypeElement>   getSchemaTypeByName(String name,
                                                         String typeName,
                                                         int    startFrom,
                                                         int    pageSize) throws InvalidParameterException,
                                                                                 UserNotAuthorizedException,
                                                                                 PropertyServerException
    {
        return client.getSchemaTypeByName(userId, name, typeName, startFrom, pageSize);
    }


    /**
     * Retrieve the schema type metadata element with the supplied unique identifier.
     *
     * @param schemaTypeGUID unique identifier of the requested metadata element
     *
     * @return requested metadata element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public SchemaTypeElement getSchemaTypeByGUID(String schemaTypeGUID) throws InvalidParameterException,
                                                                               UserNotAuthorizedException,
                                                                               PropertyServerException
    {
        return client.getSchemaTypeByGUID(userId, schemaTypeGUID);
    }


    /**
     * Retrieve the header of the metadata element connected to a schema type.
     *
     * @param schemaTypeGUID unique identifier of the requested metadata element
     *
     * @return header for parent element (data asset, process, port)
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public ElementHeader getSchemaTypeParent(String schemaTypeGUID) throws InvalidParameterException,
                                                                           UserNotAuthorizedException,
                                                                           PropertyServerException
    {
        return client.getSchemaTypeParent(userId, schemaTypeGUID);
    }


    /* ===============================================================================
     * A schemaType typically contains many schema attributes, linked with relationships.  These are called
     */

    /**
     * Create a new metadata element to represent a schema attribute.
     *
     * @param schemaElementGUID unique identifier of the schemaType or Schema Attribute where the schema attribute is nested underneath
     * @param schemaAttributeProperties properties for the schema attribute
     *
     * @return unique identifier of the new metadata element for the schema attribute
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public String createSchemaAttribute(String                    schemaElementGUID,
                                        SchemaAttributeProperties schemaAttributeProperties) throws InvalidParameterException,
                                                                                                    UserNotAuthorizedException,
                                                                                                    PropertyServerException
    {
        if (eventBrokerIsHome)
        {
            return client.createSchemaAttribute(userId, eventBrokerGUID, eventBrokerName, schemaElementGUID, schemaAttributeProperties);
        }
        else
        {
            return client.createSchemaAttribute(userId, null, null, schemaElementGUID, schemaAttributeProperties);
        }
    }


    /**
     * Create a new metadata element to represent a schema attribute using an existing metadata element as a template.
     *
     * @param schemaElementGUID unique identifier of the schemaType or Schema Attribute where the schema attribute is connected to
     * @param templateGUID unique identifier of the metadata element to copy
     * @param templateProperties properties that override the template
     *
     * @return unique identifier of the new metadata element for the schema attribute
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public String createSchemaAttributeFromTemplate(String             schemaElementGUID,
                                                    String             templateGUID,
                                                    TemplateProperties templateProperties) throws InvalidParameterException,
                                                                                                  UserNotAuthorizedException,
                                                                                                  PropertyServerException
    {
        if (eventBrokerIsHome)
        {
            return client.createSchemaAttributeFromTemplate(userId, eventBrokerGUID, eventBrokerName, schemaElementGUID, templateGUID, templateProperties);
        }
        else
        {
            return client.createSchemaAttributeFromTemplate(userId, null, null, schemaElementGUID, templateGUID, templateProperties);
        }
    }


    /**
     * Connect a schema type to a schema attribute.
     *
     * @param relationshipTypeName name of relationship to create
     * @param schemaAttributeGUID unique identifier of the schema attribute
     * @param schemaTypeGUID unique identifier of the schema type to connect
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void setupSchemaType(String relationshipTypeName,
                                String schemaAttributeGUID,
                                String schemaTypeGUID) throws InvalidParameterException,
                                                              UserNotAuthorizedException,
                                                              PropertyServerException
    {
        if (eventBrokerIsHome)
        {
            client.setupSchemaType(userId, eventBrokerGUID, eventBrokerName, relationshipTypeName, schemaAttributeGUID, schemaTypeGUID);
        }
        else
        {
            client.setupSchemaType(userId, null, null, relationshipTypeName, schemaAttributeGUID, schemaTypeGUID);
        }
    }


    /**
     * Remove the linked schema types from a schema attribute.
     *
     * @param schemaAttributeGUID unique identifier of the schema attribute
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void clearSchemaTypes(String schemaAttributeGUID) throws InvalidParameterException,
                                                                    UserNotAuthorizedException,
                                                                    PropertyServerException
    {
        client.clearSchemaTypes(userId, eventBrokerGUID, eventBrokerName, schemaAttributeGUID);
    }


    /**
     * Update the properties of the metadata element representing a schema attribute.
     *
     * @param schemaAttributeGUID unique identifier of the schema attribute to update
     * @param isMergeUpdate should the new properties be merged with existing properties (true) or completely replace them (false)?
     * @param schemaAttributeProperties new properties for the schema attribute
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void updateSchemaAttribute(String                    schemaAttributeGUID,
                                      boolean                   isMergeUpdate,
                                      SchemaAttributeProperties schemaAttributeProperties) throws InvalidParameterException,
                                                                                                  UserNotAuthorizedException,
                                                                                                  PropertyServerException
    {
        client.updateSchemaAttribute(userId, eventBrokerGUID, eventBrokerName, schemaAttributeGUID, isMergeUpdate, schemaAttributeProperties);
    }


    /**
     * Remove the metadata element representing a schema attribute.
     *
     * @param schemaAttributeGUID unique identifier of the metadata element to remove
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void removeSchemaAttribute(String schemaAttributeGUID) throws InvalidParameterException,
                                                                         UserNotAuthorizedException,
                                                                         PropertyServerException
    {
        client.removeSchemaAttribute(userId, eventBrokerGUID, eventBrokerName, schemaAttributeGUID);
    }


    /**
     * Retrieve the list of schema attribute metadata elements that contain the search string.
     * The search string is treated as a regular expression.
     *
     * @param searchString string to find in the properties
     * @param typeName optional type name for the schema type - used to restrict the search results
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return list of matching metadata elements
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<SchemaAttributeElement> findSchemaAttributes(String searchString,
                                                             String typeName,
                                                             int    startFrom,
                                                             int    pageSize) throws InvalidParameterException,
                                                                                     UserNotAuthorizedException,
                                                                                     PropertyServerException
    {
        return client.findSchemaAttributes(userId, searchString, typeName, startFrom, pageSize);
    }


    /**
     * Retrieve the list of schema attributes associated with a StructSchemaType or nested underneath a schema attribute.
     *
     * @param parentSchemaElementGUID unique identifier of the schemaType of interest
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return list of associated metadata elements
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<SchemaAttributeElement> getNestedAttributes(String parentSchemaElementGUID,
                                                            int    startFrom,
                                                            int    pageSize) throws InvalidParameterException,
                                                                                    UserNotAuthorizedException,
                                                                                    PropertyServerException
    {
        return client.getNestedAttributes(userId, parentSchemaElementGUID, startFrom, pageSize);
    }


    /**
     * Retrieve the list of schema attribute metadata elements with a matching qualified or display name.
     * There are no wildcards supported on this request.
     *
     * @param name name to search for
     * @param typeName optional type name for the schema type - used to restrict the search results
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return list of matching metadata elements
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<SchemaAttributeElement> getSchemaAttributesByName(String name,
                                                                  String typeName,
                                                                  int    startFrom,
                                                                  int    pageSize) throws InvalidParameterException,
                                                                                          UserNotAuthorizedException,
                                                                                          PropertyServerException
    {
        return client.getSchemaAttributesByName(userId, name, typeName, startFrom, pageSize);
    }


    /**
     * Retrieve the schema attribute metadata element with the supplied unique identifier.
     *
     * @param schemaAttributeGUID unique identifier of the requested metadata element
     *
     * @return matching metadata element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public SchemaAttributeElement getSchemaAttributeByGUID(String schemaAttributeGUID) throws InvalidParameterException,
                                                                                              UserNotAuthorizedException,
                                                                                              PropertyServerException
    {
        return client.getSchemaAttributeByGUID(userId, schemaAttributeGUID);
    }
}
