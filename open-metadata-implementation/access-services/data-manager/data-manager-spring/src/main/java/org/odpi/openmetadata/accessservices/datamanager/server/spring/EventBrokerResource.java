/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.datamanager.server.spring;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.odpi.openmetadata.accessservices.datamanager.properties.*;
import org.odpi.openmetadata.accessservices.datamanager.rest.*;
import org.odpi.openmetadata.accessservices.datamanager.server.EventBrokerRESTServices;
import org.odpi.openmetadata.commonservices.ffdc.rest.GUIDResponse;
import org.odpi.openmetadata.commonservices.ffdc.rest.NullRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.VoidResponse;
import org.springframework.web.bind.annotation.*;


/**
 * EventBrokerResource is the server-side implementation of the Data Manager OMAS's
 * support for topics.  It matches the EventBrokerClient.
 */
@RestController
@RequestMapping("/servers/{serverName}/open-metadata/access-services/data-manager/users/{userId}")

@Tag(name="Data Manager OMAS",
     description="The Data Manager OMAS provides APIs for tools and applications wishing to manage metadata relating to data manager " +
                         "such as topic servers, content managers and file systems.",
     externalDocs=@ExternalDocumentation(description="Data Manager Open Metadata Access Service (OMAS)",
                                         url="https://egeria.odpi.org/open-metadata-implementation/access-services/data-manager/"))

public class EventBrokerResource
{
    private EventBrokerRESTServices restAPI = new EventBrokerRESTServices();

    /**
     * Default constructor
     */
    public EventBrokerResource()
    {
    }


    /* ========================================================
     * The topic is the top level asset on a topic server
     */


    /**
     * Create a new metadata element to represent a topic.
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param eventBrokerGUID unique identifier of software server capability representing the owning event broker
     * @param eventBrokerName unique name of software server capability representing the owning event broker
     * @param topicProperties properties to store
     *
     * @return unique identifier of the new metadata element or
     * InvalidParameterException  one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to issue this request or
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/event-brokers/{eventBrokerGUID}/{eventBrokerName}/topics")

    public GUIDResponse createTopic(@PathVariable String          serverName,
                                    @PathVariable String          userId,
                                    @PathVariable String          eventBrokerGUID,
                                    @PathVariable String          eventBrokerName,
                                    @RequestBody  TopicProperties topicProperties)
    {
        return restAPI.createTopic(serverName, userId, eventBrokerGUID, eventBrokerName, topicProperties);
    }


    /**
     * Create a new metadata element to represent a topic using an existing metadata element as a template.
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param eventBrokerGUID unique identifier of software server capability representing the owning event broker
     * @param eventBrokerName unique name of software server capability representing the owning event broker
     * @param templateGUID unique identifier of the metadata element to copy
     * @param templateProperties properties that override the template
     *
     * @return unique identifier of the new metadata element or
     * InvalidParameterException  one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to issue this request or
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/event-brokers/{eventBrokerGUID}/{eventBrokerName}/topics/from-template/{templateGUID}")

    public GUIDResponse createTopicFromTemplate(@PathVariable String             serverName,
                                                @PathVariable String             userId,
                                                @PathVariable String             eventBrokerGUID,
                                                @PathVariable String             eventBrokerName,
                                                @PathVariable String             templateGUID,
                                                @RequestBody  TemplateProperties templateProperties)
    {
        return restAPI.createTopicFromTemplate(serverName, userId, eventBrokerGUID, eventBrokerName, templateGUID, templateProperties);
    }


    /**
     * Update the metadata element representing a topic.
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param eventBrokerGUID unique identifier of software server capability representing the owning event broker
     * @param eventBrokerName unique name of software server capability representing the owning event broker
     * @param topicGUID unique identifier of the metadata element to update
     * @param isMergeUpdate are unspecified properties unchanged (true) or removed?
     * @param topicProperties new properties for this element
     *
     * @return void or
     * InvalidParameterException  one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to issue this request or
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/event-brokers/{eventBrokerGUID}/{eventBrokerName}/topics/{topicGUID}")

    public VoidResponse updateTopic(@PathVariable String          serverName,
                                    @PathVariable String          userId,
                                    @PathVariable String          eventBrokerGUID,
                                    @PathVariable String          eventBrokerName,
                                    @PathVariable String          topicGUID,
                                    @RequestParam boolean         isMergeUpdate,
                                    @RequestBody  TopicProperties topicProperties)
    {
        return restAPI.updateTopic(serverName, userId, eventBrokerGUID, eventBrokerName, topicGUID, isMergeUpdate, topicProperties);
    }


    /**
     * Update the zones for the topic asset so that it becomes visible to consumers.
     * (The zones are set to the list of zones in the publishedZones option configured for each
     * instance of the Data Manager OMAS).
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param topicGUID unique identifier of the metadata element to publish
     * @param nullRequestBody empty request body
     *
     * @return void or
     * InvalidParameterException  one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to issue this request or
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/topics/{topicGUID}/publish")

    public VoidResponse publishTopic(@PathVariable                  String          serverName,
                                     @PathVariable                  String          userId,
                                     @PathVariable                  String          topicGUID,
                                     @RequestBody(required = false) NullRequestBody nullRequestBody)
    {
        return restAPI.publishTopic(serverName, userId, topicGUID, nullRequestBody);
    }


    /**
     * Update the zones for the topic asset so that it is no longer visible to consumers.
     * (The zones are set to the list of zones in the defaultZones option configured for each
     * instance of the Data Manager OMAS.  This is the setting when the topic is first created).
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param topicGUID unique identifier of the metadata element to withdraw
     * @param nullRequestBody empty request body
     *
     * @return void or
     * InvalidParameterException  one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to issue this request or
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/topics/{topicGUID}/withdraw")

    public VoidResponse withdrawTopic(@PathVariable                  String          serverName,
                                      @PathVariable                  String          userId,
                                      @PathVariable                  String          topicGUID,
                                      @RequestBody(required = false) NullRequestBody nullRequestBody)
    {
        return restAPI.withdrawTopic(serverName, userId, topicGUID, nullRequestBody);
    }


    /**
     * Remove the metadata element representing a topic.
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param eventBrokerGUID unique identifier of software server capability representing the owning event broker
     * @param eventBrokerName unique name of software server capability representing the owning event broker
     * @param topicGUID unique identifier of the metadata element to remove
     * @param qualifiedName unique name of the metadata element to remove
     * @param nullRequestBody empty request body
     *
     * @return void or
     * InvalidParameterException  one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to issue this request or
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/event-brokers/{eventBrokerGUID}/{eventBrokerName}/topics/{topicGUID}/{qualifiedName}/delete")

    public VoidResponse removeTopic(@PathVariable                  String          serverName,
                                    @PathVariable                  String          userId,
                                    @PathVariable                  String          eventBrokerGUID,
                                    @PathVariable                  String          eventBrokerName,
                                    @PathVariable                  String          topicGUID,
                                    @PathVariable                  String          qualifiedName,
                                    @RequestBody(required = false) NullRequestBody nullRequestBody)
    {
        return restAPI.removeTopic(serverName, userId, eventBrokerGUID, eventBrokerName, topicGUID, qualifiedName, nullRequestBody);
    }


    /**
     * Retrieve the list of topic metadata elements that contain the search string.
     * The search string is treated as a regular expression.
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param searchString string to find in the properties
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return list of matching metadata elements or
     * InvalidParameterException  one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to issue this request or
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @GetMapping(path = "/topics/by-search-string/{searchString}")

    public TopicsResponse findTopics(@PathVariable String serverName,
                                     @PathVariable String userId,
                                     @PathVariable String searchString,
                                     @RequestParam int    startFrom,
                                     @RequestParam int    pageSize)
    {
        return restAPI.findTopics(serverName, userId, searchString, startFrom, pageSize);
    }


    /**
     * Retrieve the list of topic metadata elements with a matching qualified or display name.
     * There are no wildcards supported on this request.
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param name name to search for
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return list of matching metadata elements or
     * InvalidParameterException  one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to issue this request or
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @GetMapping(path = "/topics/by-name/{name}")

    public TopicsResponse   getTopicsByName(@PathVariable String serverName,
                                            @PathVariable String userId,
                                            @PathVariable String name,
                                            @RequestParam int    startFrom,
                                            @RequestParam int    pageSize)
    {
        return restAPI.getTopicsByName(serverName, userId, name, startFrom, pageSize);
    }


    /**
     * Retrieve the list of topics created by this topic manager.
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param eventBrokerGUID unique identifier of software server capability representing the owning event broker
     * @param eventBrokerName unique name of software server capability representing the owning event broker
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return list of matching metadata elements or
     * InvalidParameterException  one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to issue this request or
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @GetMapping(path = "/event-brokers/{eventBrokerGUID}/{eventBrokerName}/topics")

    public TopicsResponse   getTopicsForEventBroker(@PathVariable String serverName,
                                                    @PathVariable String userId,
                                                    @PathVariable String eventBrokerGUID,
                                                    @PathVariable String eventBrokerName,
                                                    @RequestParam int    startFrom,
                                                    @RequestParam int    pageSize)
    {
        return restAPI.getTopicsForEventBroker(serverName, userId, eventBrokerGUID, eventBrokerName, startFrom, pageSize);
    }


    /**
     * Retrieve the topic metadata element with the supplied unique identifier.
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param guid unique identifier of the requested metadata element
     *
     * @return matching metadata element or
     * InvalidParameterException  one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to issue this request or
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @GetMapping(path = "/topics/{guid}")

    public TopicResponse getTopicByGUID(@PathVariable String serverName,
                                        @PathVariable String userId,
                                        @PathVariable String guid)
    {
        return restAPI.getTopicByGUID(serverName, userId, guid);
    }


    /* ============================================================================
     * A topic may host one or more event types depending on its capability
     */

    /**
     * Create a new metadata element to represent a event type.
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param eventBrokerGUID unique identifier of software server capability representing the owning event broker
     * @param eventBrokerName unique name of software server capability representing the owning event broker
     * @param topicGUID unique identifier of the topic where the event type is located
     * @param eventTypeProperties properties about the event type
     *
     * @return unique identifier of the new event type or
     * InvalidParameterException  one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to issue this request or
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/event-brokers/{eventBrokerGUID}/{eventBrokerName}/topics/{topicGUID}/event-types")

    public GUIDResponse createEventType(@PathVariable String              serverName,
                                        @PathVariable String              userId,
                                        @PathVariable String              eventBrokerGUID,
                                        @PathVariable String              eventBrokerName,
                                        @PathVariable String              topicGUID,
                                        @RequestBody  EventTypeProperties eventTypeProperties)
    {
        return restAPI.createEventType(serverName, userId, eventBrokerGUID, eventBrokerName, topicGUID, eventTypeProperties);
    }


    /**
     * Create a new metadata element to represent a event type using an existing metadata element as a template.
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param eventBrokerGUID unique identifier of software server capability representing the owning event broker
     * @param eventBrokerName unique name of software server capability representing the owning event broker
     * @param templateGUID unique identifier of the metadata element to copy
     * @param topicGUID unique identifier of the topic where the event type is located
     * @param templateProperties properties that override the template
     *
     * @return unique identifier of the new event type or
     * InvalidParameterException  one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to issue this request or
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/event-brokers/{eventBrokerGUID}/{eventBrokerName}/topics/{topicGUID}/event-types/from-template/{templateGUID}")

    public GUIDResponse createEventTypeFromTemplate(@PathVariable String             serverName,
                                                    @PathVariable String             userId,
                                                    @PathVariable String             eventBrokerGUID,
                                                    @PathVariable String             eventBrokerName,
                                                    @PathVariable String             templateGUID,
                                                    @PathVariable String             topicGUID,
                                                    @RequestBody  TemplateProperties templateProperties)
    {
        return restAPI.createEventTypeFromTemplate(serverName, userId, eventBrokerGUID, eventBrokerName, templateGUID, topicGUID, templateProperties);
    }


    /**
     * Update the metadata element representing a event type.
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param eventBrokerGUID unique identifier of software server capability representing the owning event broker
     * @param eventBrokerName unique name of software server capability representing the owning event broker
     * @param eventTypeGUID unique identifier of the metadata element to update
     * @param isMergeUpdate are unspecified properties unchanged (true) or removed?
     * @param eventTypeProperties new properties for the metadata element
     *
     * @return void or
     * InvalidParameterException  one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to issue this request or
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/event-brokers/{eventBrokerGUID}/{eventBrokerName}/topics/event-types/{eventTypeGUID}")

    public VoidResponse updateEventType(@PathVariable String              serverName,
                                        @PathVariable String              userId,
                                        @PathVariable String              eventBrokerGUID,
                                        @PathVariable String              eventBrokerName,
                                        @PathVariable String              eventTypeGUID,
                                        @RequestParam boolean             isMergeUpdate,
                                        @RequestBody  EventTypeProperties eventTypeProperties)
    {
        return restAPI.updateEventType(serverName, userId, eventBrokerGUID, eventBrokerName, eventTypeGUID, isMergeUpdate, eventTypeProperties);
    }


    /**
     * Remove the metadata element representing a event type.
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param eventBrokerGUID unique identifier of software server capability representing the owning event broker
     * @param eventBrokerName unique name of software server capability representing the owning event broker
     * @param eventTypeGUID unique identifier of the metadata element to remove
     * @param qualifiedName unique name of the metadata element to remove
     * @param nullRequestBody empty request body
     *
     * @return void or
     * InvalidParameterException  one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to issue this request or
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/event-brokers/{eventBrokerGUID}/{eventBrokerName}/topics/event-types/{eventTypeGUID}/{qualifiedName}/delete")

    public VoidResponse removeEventType(@PathVariable                  String          serverName,
                                        @PathVariable                  String          userId,
                                        @PathVariable                  String          eventBrokerGUID,
                                        @PathVariable                  String          eventBrokerName,
                                        @PathVariable                  String          eventTypeGUID,
                                        @PathVariable                  String          qualifiedName,
                                        @RequestBody(required = false) NullRequestBody nullRequestBody)
    {
        return restAPI.removeEventType(serverName, userId, eventBrokerGUID, eventBrokerName, eventTypeGUID, qualifiedName, nullRequestBody);
    }


    /**
     * Retrieve the list of event type metadata elements that contain the search string.
     * The search string is treated as a regular expression.
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param searchString string to find in the properties
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return list of matching metadata elements or
     * InvalidParameterException  one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to issue this request or
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @GetMapping(path = "/topics/event-types/by-search-string/{searchString}")

    public EventTypesResponse findEventTypes(@PathVariable String serverName,
                                             @PathVariable String userId,
                                             @PathVariable String searchString,
                                             @RequestParam int    startFrom,
                                             @RequestParam int    pageSize)
    {
        return restAPI.findEventTypes(serverName, userId, searchString, startFrom, pageSize);
    }


    /**
     * Return the list of event types associated with a EventSet.
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param eventSetGUID unique identifier of the event set to query
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return list of metadata elements describing the schemas associated with the requested topic or
     * InvalidParameterException  one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to issue this request or
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @GetMapping(path = "/event-sets/{eventSetGUID}/event-types")

    public EventTypesResponse getEventTypesForEventSet(@PathVariable String serverName,
                                                       @PathVariable String userId,
                                                       @PathVariable String eventSetGUID,
                                                       @RequestParam int    startFrom,
                                                       @RequestParam int    pageSize)
    {
        return restAPI.getEventTypesForEventSet(serverName, userId, eventSetGUID, startFrom, pageSize);
    }


    /**
     * Return the list of schemas associated with a topic.
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param topicGUID unique identifier of the topic to query
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return list of metadata elements describing the schemas associated with the requested topic or
     * InvalidParameterException  one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to issue this request or
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @GetMapping(path = "/topics/{topicGUID}/event-types")

    public EventTypesResponse getEventTypesForTopic(@PathVariable String serverName,
                                                 @PathVariable String userId,
                                                 @PathVariable String topicGUID,
                                                 @RequestParam int    startFrom,
                                                 @RequestParam int    pageSize)
    {
        return restAPI.getEventTypesForTopic(serverName, userId, topicGUID, startFrom, pageSize);
    }


    /**
     * Retrieve the list of event type metadata elements with a matching qualified or display name.
     * There are no wildcards supported on this request.
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param name name to search for
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return list of matching metadata elements or
     * InvalidParameterException  one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to issue this request or
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @GetMapping(path = "/topics/event-types/by-name/{name}")

    public EventTypesResponse getEventTypesByName(@PathVariable String serverName,
                                                  @PathVariable String userId,
                                                  @PathVariable String name,
                                                  @RequestParam int    startFrom,
                                                  @RequestParam int    pageSize)
    {
        return restAPI.getEventTypesByName(serverName, userId, name, startFrom, pageSize);
    }


    /**
     * Retrieve the event type metadata element with the supplied unique identifier.
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param guid unique identifier of the requested metadata element
     *
     * @return requested metadata element or
     * InvalidParameterException  one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to issue this request or
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @GetMapping(path = "/topics/event-types/{guid}")

    public EventTypeResponse getEventTypeByGUID(@PathVariable String serverName,
                                                @PathVariable String userId,
                                                @PathVariable String guid)
    {
        return restAPI.getEventTypeByGUID(serverName, userId, guid);
    }
    
}
