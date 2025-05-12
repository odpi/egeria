/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.datamanager.client;

import org.odpi.openmetadata.accessservices.datamanager.api.EventBrokerInterface;
import org.odpi.openmetadata.accessservices.datamanager.client.rest.DataManagerRESTClient;
import org.odpi.openmetadata.accessservices.datamanager.properties.TemplateProperties;
import org.odpi.openmetadata.accessservices.datamanager.rest.TemplateRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.*;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.*;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.topics.*;
import org.odpi.openmetadata.frameworks.openmetadata.properties.schema.events.*;

import java.util.List;

/**
 * EventBrokerClient is the client for managing topics from an Event Manager.
 */
public class EventBrokerClient extends SchemaManagerClient implements EventBrokerInterface
{
    private static final String topicURLTemplatePrefix     = "/servers/{0}/open-metadata/access-services/data-manager/users/{1}/topics";
    private static final String defaultSchemaAttributeName = "EventSchemaAttribute";

    /**
     * Create a new client with no authentication embedded in the HTTP request.
     *
     * @param serverName name of the server to connect to
     * @param serverPlatformURLRoot the network address of the server running the OMAS REST services
     * @param auditLog logging destination
     * @throws InvalidParameterException there is a problem creating the client-side components to issue any
     * REST API calls.
     */
    public EventBrokerClient(String   serverName,
                             String   serverPlatformURLRoot,
                             AuditLog auditLog) throws InvalidParameterException
    {
        super(defaultSchemaAttributeName, serverName, serverPlatformURLRoot, auditLog);
    }


    /**
     * Create a new client with no authentication embedded in the HTTP request.
     *
     * @param serverName name of the server to connect to
     * @param serverPlatformURLRoot the network address of the server running the OMAS REST services
     * @throws InvalidParameterException there is a problem creating the client-side components to issue any
     * REST API calls.
     */
    public EventBrokerClient(String serverName,
                             String serverPlatformURLRoot) throws InvalidParameterException
    {
        super(defaultSchemaAttributeName, serverName, serverPlatformURLRoot);
    }


    /**
     * Create a new client that passes userId and password in each HTTP request.  This is the
     * userId/password of the calling server.  The end user's userId is sent on each request.
     *
     * @param serverName name of the server to connect to
     * @param serverPlatformURLRoot the network address of the server running the OMAS REST services
     * @param userId caller's userId embedded in all HTTP requests
     * @param password caller's userId embedded in all HTTP requests
     * @param auditLog logging destination
     *
     * @throws InvalidParameterException there is a problem creating the client-side components to issue any
     * REST API calls.
     */
    public EventBrokerClient(String   serverName,
                             String   serverPlatformURLRoot,
                             String   userId,
                             String   password,
                             AuditLog auditLog) throws InvalidParameterException
    {
        super(defaultSchemaAttributeName, serverName, serverPlatformURLRoot, userId, password, auditLog);
    }


    /**
     * Create a new client that is going to be used in an OMAG Server.
     *
     * @param serverName name of the server to connect to
     * @param serverPlatformURLRoot the network address of the server running the OMAS REST services
     * @param restClient client that issues the REST API calls
     * @param maxPageSize maximum number of results supported by this server
     * @throws InvalidParameterException there is a problem creating the client-side components to issue any
     * REST API calls.
     */
    public EventBrokerClient(String                serverName,
                             String                serverPlatformURLRoot,
                             DataManagerRESTClient restClient,
                             int                   maxPageSize) throws InvalidParameterException
    {
        super(defaultSchemaAttributeName, serverName, serverPlatformURLRoot, restClient, maxPageSize);
    }


    /**
     * Create a new client that passes userId and password in each HTTP request.  This is the
     * userId/password of the calling server.  The end user's userId is sent on each request.
     *
     * @param serverName name of the server to connect to
     * @param serverPlatformURLRoot the network address of the server running the OMAS REST services
     * @param userId caller's userId embedded in all HTTP requests
     * @param password caller's userId embedded in all HTTP requests
     * @throws InvalidParameterException there is a problem creating the client-side components to issue any
     * REST API calls.
     */
    public EventBrokerClient(String serverName,
                             String serverPlatformURLRoot,
                             String userId,
                             String password) throws InvalidParameterException
    {
        super(defaultSchemaAttributeName, serverName, serverPlatformURLRoot, userId, password);
    }


    /* ========================================================
     * The topic is the top level asset on an event manager server
     */


    /**
     * Create a new metadata element to represent a topic.
     *
     * @param userId calling user
     * @param eventBrokerGUID unique identifier of software server capability representing the event broker
     * @param eventBrokerName unique name of software server capability representing the event broker
     * @param eventBrokerIsHome should the topic be marked as owned by the event broker so others can not update?
     * @param topicProperties properties to store
     *
     * @return unique identifier of the new metadata element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public String createTopic(String          userId,
                              String          eventBrokerGUID,
                              String          eventBrokerName,
                              boolean         eventBrokerIsHome,
                              TopicProperties topicProperties) throws InvalidParameterException,
                                                                      UserNotAuthorizedException,
                                                                      PropertyServerException
    {
        final String methodName                  = "createTopic";
        final String propertiesParameterName     = "topicProperties";
        final String qualifiedNameParameterName  = "qualifiedName";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateObject(topicProperties, propertiesParameterName, methodName);
        invalidParameterHandler.validateName(topicProperties.getQualifiedName(), qualifiedNameParameterName, methodName);

        final String urlTemplate = serverPlatformURLRoot + topicURLTemplatePrefix + "?eventBrokerIsHome={2}";

        TopicRequestBody requestBody = new TopicRequestBody(topicProperties);

        requestBody.setExternalSourceGUID(eventBrokerGUID);
        requestBody.setExternalSourceName(eventBrokerName);

        GUIDResponse restResult = restClient.callGUIDPostRESTCall(methodName,
                                                                  urlTemplate,
                                                                  requestBody,
                                                                  serverName,
                                                                  userId,
                                                                  eventBrokerIsHome);

        return restResult.getGUID();
    }


    /**
     * Create a new metadata element to represent a topic using an existing metadata element as a template.
     *
     * @param userId calling user
     * @param eventBrokerGUID unique identifier of software server capability representing the event broker
     * @param eventBrokerName unique name of software server capability representing the event broker
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
    @Override
    public String createTopicFromTemplate(String             userId,
                                          String             eventBrokerGUID,
                                          String             eventBrokerName,
                                          boolean            eventBrokerIsHome,
                                          String             templateGUID,
                                          TemplateProperties templateProperties) throws InvalidParameterException,
                                                                                        UserNotAuthorizedException,
                                                                                        PropertyServerException
    {
        final String methodName                  = "createTopicFromTemplate";
        final String templateGUIDParameterName   = "templateGUID";
        final String propertiesParameterName     = "templateProperties";
        final String qualifiedNameParameterName  = "qualifiedName";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(templateGUID, templateGUIDParameterName, methodName);
        invalidParameterHandler.validateObject(templateProperties, propertiesParameterName, methodName);
        invalidParameterHandler.validateName(templateProperties.getQualifiedName(), qualifiedNameParameterName, methodName);

        final String urlTemplate = serverPlatformURLRoot + topicURLTemplatePrefix + "/from-template/{2}?eventBrokerIsHome={3}";

        TemplateRequestBody requestBody = new TemplateRequestBody(templateProperties);

        requestBody.setExternalSourceGUID(eventBrokerGUID);
        requestBody.setExternalSourceName(eventBrokerName);

        GUIDResponse restResult = restClient.callGUIDPostRESTCall(methodName,
                                                                  urlTemplate,
                                                                  requestBody,
                                                                  serverName,
                                                                  userId,
                                                                  templateGUID,
                                                                  eventBrokerIsHome);

        return restResult.getGUID();
    }


    /**
     * Update the metadata element representing a topic.
     *
     * @param userId calling user
     * @param eventBrokerGUID unique identifier of software server capability representing the event broker
     * @param eventBrokerName unique name of software server capability representing the event broker
     * @param topicGUID unique identifier of the metadata element to update
     * @param isMergeUpdate are unspecified properties unchanged (true) or removed?
     * @param topicProperties new properties for this element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public void updateTopic(String          userId,
                            String          eventBrokerGUID,
                            String          eventBrokerName,
                            String          topicGUID,
                            boolean         isMergeUpdate,
                            TopicProperties topicProperties) throws InvalidParameterException,
                                                                    UserNotAuthorizedException,
                                                                    PropertyServerException
    {
        final String methodName                  = "updateTopic";
        final String elementGUIDParameterName    = "topicGUID";
        final String propertiesParameterName     = "topicProperties";
        final String qualifiedNameParameterName  = "qualifiedName";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(topicGUID, elementGUIDParameterName, methodName);
        invalidParameterHandler.validateObject(topicProperties, propertiesParameterName, methodName);
        if (! isMergeUpdate)
        {
            invalidParameterHandler.validateName(topicProperties.getQualifiedName(), qualifiedNameParameterName, methodName);
        }

        final String urlTemplate = serverPlatformURLRoot + topicURLTemplatePrefix + "/{2}?isMergeUpdate={3}";

        TopicRequestBody requestBody = new TopicRequestBody(topicProperties);

        requestBody.setExternalSourceGUID(eventBrokerGUID);
        requestBody.setExternalSourceName(eventBrokerName);

        restClient.callVoidPostRESTCall(methodName,
                                        urlTemplate,
                                        requestBody,
                                        serverName,
                                        userId,
                                        topicGUID,
                                        isMergeUpdate);
    }


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
    @Override
    public void publishTopic(String userId,
                             String topicGUID) throws InvalidParameterException,
                                                      UserNotAuthorizedException,
                                                      PropertyServerException
    {
        final String methodName               = "publishTopic";
        final String elementGUIDParameterName = "topicGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(topicGUID, elementGUIDParameterName, methodName);

        final String urlTemplate = serverPlatformURLRoot + topicURLTemplatePrefix + "/{2}/publish";

        restClient.callVoidPostRESTCall(methodName,
                                        urlTemplate,
                                        nullRequestBody,
                                        serverName,
                                        userId,
                                        topicGUID);
    }


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
    @Override
    public void withdrawTopic(String userId,
                              String topicGUID) throws InvalidParameterException,
                                                       UserNotAuthorizedException,
                                                       PropertyServerException
    {
        final String methodName               = "withdrawTopic";
        final String elementGUIDParameterName = "topicGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(topicGUID, elementGUIDParameterName, methodName);

        final String urlTemplate = serverPlatformURLRoot + topicURLTemplatePrefix + "/{2}/withdraw";

        restClient.callVoidPostRESTCall(methodName,
                                        urlTemplate,
                                        nullRequestBody,
                                        serverName,
                                        userId,
                                        topicGUID);
    }


    /**
     * Remove the metadata element representing a topic.
     *
     * @param userId calling user
     * @param eventBrokerGUID unique identifier of software server capability representing the event broker
     * @param eventBrokerName unique name of software server capability representing the event broker
     * @param topicGUID unique identifier of the metadata element to remove
     * @param qualifiedName unique name of the metadata element to remove
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public void removeTopic(String userId,
                            String eventBrokerGUID,
                            String eventBrokerName,
                            String topicGUID,
                            String qualifiedName) throws InvalidParameterException,
                                                         UserNotAuthorizedException,
                                                         PropertyServerException
    {
        final String methodName = "removeTopic";
        final String elementGUIDParameterName    = "topicGUID";
        final String qualifiedNameParameterName  = "qualifiedName";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(topicGUID, elementGUIDParameterName, methodName);
        invalidParameterHandler.validateName(qualifiedName, qualifiedNameParameterName, methodName);

        final String urlTemplate = serverPlatformURLRoot + topicURLTemplatePrefix + "/{2}/{3}/delete";

        MetadataSourceRequestBody requestBody = new MetadataSourceRequestBody();

        requestBody.setExternalSourceGUID(eventBrokerGUID);
        requestBody.setExternalSourceName(eventBrokerName);

        restClient.callVoidPostRESTCall(methodName,
                                        urlTemplate,
                                        requestBody,
                                        serverName,
                                        userId,
                                        topicGUID,
                                        qualifiedName);
    }


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
    @Override
    public List<TopicElement> findTopics(String userId,
                                         String searchString,
                                         int    startFrom,
                                         int    pageSize) throws InvalidParameterException,
                                                                 UserNotAuthorizedException,
                                                                 PropertyServerException
    {
        final String methodName                = "findTopics";
        final String searchStringParameterName = "searchString";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateSearchString(searchString, searchStringParameterName, methodName);
        int validatedPageSize = invalidParameterHandler.validatePaging(startFrom, pageSize, methodName);

        final String urlTemplate = serverPlatformURLRoot + topicURLTemplatePrefix + "/by-search-string?startFrom={2}&pageSize={3}";

        SearchStringRequestBody requestBody = new SearchStringRequestBody();

        requestBody.setSearchString(searchString);
        requestBody.setSearchStringParameterName(searchStringParameterName);

        TopicsResponse restResult = restClient.callTopicsPostRESTCall(methodName,
                                                                      urlTemplate,
                                                                      requestBody,
                                                                      serverName,
                                                                      userId,
                                                                      startFrom,
                                                                      validatedPageSize);

        return restResult.getElements();
    }


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
    @Override
    public List<TopicElement>   getTopicsByName(String userId,
                                                String name,
                                                int    startFrom,
                                                int    pageSize) throws InvalidParameterException,
                                                                        UserNotAuthorizedException,
                                                                        PropertyServerException
    {
        final String methodName        = "getTopicsByName";
        final String nameParameterName = "name";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateName(name, nameParameterName, methodName);
        int validatedPageSize = invalidParameterHandler.validatePaging(startFrom, pageSize, methodName);

        final String urlTemplate = serverPlatformURLRoot + topicURLTemplatePrefix + "/by-name?startFrom={2}&pageSize={3}";

        NameRequestBody requestBody = new NameRequestBody();

        requestBody.setName(name);
        requestBody.setNamePropertyName(nameParameterName);

        TopicsResponse restResult = restClient.callTopicsPostRESTCall(methodName,
                                                                      urlTemplate,
                                                                      requestBody,
                                                                      serverName,
                                                                      userId,
                                                                      startFrom,
                                                                      validatedPageSize);

        return restResult.getElements();
    }


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
    @Override
    public List<TopicElement> getTopicsForEventBroker(String userId,
                                                      String eventBrokerGUID,
                                                      String eventBrokerName,
                                                      int    startFrom,
                                                      int    pageSize) throws InvalidParameterException,
                                                                                 UserNotAuthorizedException,
                                                                                 PropertyServerException
    {
        final String methodName = "getTopicsForEventBroker";
        final String eventBrokerGUIDParameterName = "eventBrokerGUID";
        final String eventBrokerNameParameterName = "eventBrokerName";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(eventBrokerGUID, eventBrokerGUIDParameterName, methodName);
        invalidParameterHandler.validateName(eventBrokerName, eventBrokerNameParameterName, methodName);

        int validatedPageSize = invalidParameterHandler.validatePaging(startFrom, pageSize, methodName);

        final String urlTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/access-services/data-manager/users/{1}/event-brokers/{2}/{3}/topics?startFrom={4}&pageSize={5}";

        TopicsResponse restResult = restClient.callTopicsGetRESTCall(methodName,
                                                                     urlTemplate,
                                                                     serverName,
                                                                     userId,
                                                                     eventBrokerGUID,
                                                                     eventBrokerName,
                                                                     startFrom,
                                                                     validatedPageSize);

        return restResult.getElements();
    }


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
    @Override
    public TopicElement getTopicByGUID(String userId,
                                       String guid) throws InvalidParameterException,
                                                           UserNotAuthorizedException,
                                                           PropertyServerException
    {
        final String methodName = "getTopicByGUID";
        final String guidParameterName = "guid";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(guid, guidParameterName, methodName);

        final String urlTemplate = serverPlatformURLRoot + topicURLTemplatePrefix + "/{2}";

        TopicResponse restResult = restClient.callTopicGetRESTCall(methodName,
                                                                   urlTemplate,
                                                                   serverName,
                                                                   userId,
                                                                   guid);

        return restResult.getElement();
    }


    /* ============================================================================
     * A topic may host one or more event types depending on its capability
     */

    /**
     * Create a new metadata element to represent a event type.
     *
     * @param userId calling user
     * @param eventBrokerGUID unique identifier of software server capability representing the event broker
     * @param eventBrokerName unique name of software server capability representing the event broker
     * @param topicGUID unique identifier of the topic where the event type is located
     * @param properties properties about the event type
     *
     * @return unique identifier of the new event type
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public String createEventType(String              userId,
                                  String              eventBrokerGUID,
                                  String              eventBrokerName,
                                  String              topicGUID,
                                  EventTypeProperties properties) throws InvalidParameterException,
                                                                         UserNotAuthorizedException,
                                                                         PropertyServerException
    {
        final String methodName                     = "createEventType";
        final String parentElementGUIDParameterName = "topicGUID";
        final String propertiesParameterName        = "properties";
        final String qualifiedNameParameterName     = "properties.qualifiedName";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(topicGUID, parentElementGUIDParameterName, methodName);
        invalidParameterHandler.validateObject(properties, propertiesParameterName, methodName);
        invalidParameterHandler.validateName(properties.getQualifiedName(), qualifiedNameParameterName, methodName);

        final String urlTemplate = serverPlatformURLRoot + topicURLTemplatePrefix + "/{2}/event-types";

        EventTypeRequestBody requestBody = new EventTypeRequestBody(properties);

        requestBody.setExternalSourceGUID(eventBrokerGUID);
        requestBody.setExternalSourceName(eventBrokerName);

        GUIDResponse restResult = restClient.callGUIDPostRESTCall(methodName,
                                                                  urlTemplate,
                                                                  requestBody,
                                                                  serverName,
                                                                  userId,
                                                                  topicGUID);

        return restResult.getGUID();
    }


    /**
     * Create a new metadata element to represent a event type using an existing metadata element as a template.
     *
     * @param userId calling user
     * @param eventBrokerGUID unique identifier of software server capability representing the event broker
     * @param eventBrokerName unique name of software server capability representing the event broker
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
    @Override
    public String createEventTypeFromTemplate(String             userId,
                                              String             eventBrokerGUID,
                                              String             eventBrokerName,
                                              String             templateGUID,
                                              String             topicGUID,
                                              TemplateProperties templateProperties) throws InvalidParameterException,
                                                                                            UserNotAuthorizedException,
                                                                                            PropertyServerException
    {
        final String methodName                     = "createEventTypeFromTemplate";
        final String templateGUIDParameterName      = "templateGUID";
        final String parentElementGUIDParameterName = "topicGUID";
        final String propertiesParameterName        = "templateProperties";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(templateGUID, templateGUIDParameterName, methodName);
        invalidParameterHandler.validateGUID(topicGUID, parentElementGUIDParameterName, methodName);
        invalidParameterHandler.validateObject(templateProperties, propertiesParameterName, methodName);

        final String urlTemplate = serverPlatformURLRoot + topicURLTemplatePrefix + "/{2}/event-types/from-template/{3}";

        TemplateRequestBody requestBody = new TemplateRequestBody(templateProperties);

        requestBody.setExternalSourceGUID(eventBrokerGUID);
        requestBody.setExternalSourceName(eventBrokerName);

        GUIDResponse restResult = restClient.callGUIDPostRESTCall(methodName,
                                                                  urlTemplate,
                                                                  requestBody,
                                                                  serverName,
                                                                  userId,
                                                                  topicGUID,
                                                                  templateGUID);

        return restResult.getGUID();
    }


    /**
     * Update the metadata element representing a event type.
     *
     * @param userId calling user
     * @param eventBrokerGUID unique identifier of software server capability representing the event broker
     * @param eventBrokerName unique name of software server capability representing the event broker
     * @param eventTypeGUID unique identifier of the metadata element to update
     * @param isMergeUpdate are unspecified properties unchanged (true) or removed?
     * @param properties new properties for the metadata element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public void updateEventType(String              userId,
                                String              eventBrokerGUID,
                                String              eventBrokerName,
                                String              eventTypeGUID,
                                boolean             isMergeUpdate,
                                EventTypeProperties properties) throws InvalidParameterException,
                                                                       UserNotAuthorizedException,
                                                                       PropertyServerException
    {
        final String methodName               = "updateEventType";
        final String elementGUIDParameterName = "eventTypeGUID";
        final String propertiesParameterName  = "topicProperties";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(eventTypeGUID, elementGUIDParameterName, methodName);
        invalidParameterHandler.validateObject(properties, propertiesParameterName, methodName);

        final String urlTemplate = serverPlatformURLRoot + topicURLTemplatePrefix + "/event-types/{2}?isMergeUpdate={3}";

        EventTypeRequestBody requestBody = new EventTypeRequestBody(properties);

        requestBody.setExternalSourceGUID(eventBrokerGUID);
        requestBody.setExternalSourceName(eventBrokerName);

        restClient.callVoidPostRESTCall(methodName,
                                        urlTemplate,
                                        requestBody,
                                        serverName,
                                        userId,
                                        eventTypeGUID,
                                        isMergeUpdate);
    }


    /**
     * Remove the metadata element representing a event type.
     *
     * @param userId calling user
     * @param eventBrokerGUID unique identifier of software server capability representing the event broker
     * @param eventBrokerName unique name of software server capability representing the event broker
     * @param eventTypeGUID unique identifier of the metadata element to remove
     * @param qualifiedName unique name of the metadata element to remove
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public void removeEventType(String userId,
                                String eventBrokerGUID,
                                String eventBrokerName,
                                String eventTypeGUID,
                                String qualifiedName) throws InvalidParameterException,
                                                             UserNotAuthorizedException,
                                                             PropertyServerException
    {
        final String methodName                  = "removeEventType";
        final String elementGUIDParameterName    = "eventTypeGUID";
        final String qualifiedNameParameterName  = "qualifiedName";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(eventTypeGUID, elementGUIDParameterName, methodName);
        invalidParameterHandler.validateName(qualifiedName, qualifiedNameParameterName, methodName);

        final String urlTemplate = serverPlatformURLRoot + topicURLTemplatePrefix + "/event-types/{2}/{3}/delete";

        MetadataSourceRequestBody requestBody = new MetadataSourceRequestBody();

        requestBody.setExternalSourceGUID(eventBrokerGUID);
        requestBody.setExternalSourceName(eventBrokerName);

        restClient.callVoidPostRESTCall(methodName,
                                        urlTemplate,
                                        requestBody,
                                        serverName,
                                        userId,
                                        eventTypeGUID,
                                        qualifiedName);
    }


    /**
     * Retrieve the list of event type metadata elements that contain the search string.
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
    @Override
    public List<EventTypeElement> findEventTypes(String userId,
                                                 String searchString,
                                                 int    startFrom,
                                                 int    pageSize) throws InvalidParameterException,
                                                                         UserNotAuthorizedException,
                                                                         PropertyServerException
    {
        final String methodName                = "findEventTypes";
        final String searchStringParameterName = "searchString";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateSearchString(searchString, searchStringParameterName, methodName);
        int validatedPageSize = invalidParameterHandler.validatePaging(startFrom, pageSize, methodName);

        final String urlTemplate = serverPlatformURLRoot + topicURLTemplatePrefix + "/event-types/by-search-string?startFrom={2}&pageSize={3}";

        SearchStringRequestBody requestBody = new SearchStringRequestBody();

        requestBody.setSearchString(searchString);
        requestBody.setSearchStringParameterName(searchStringParameterName);

        EventTypesResponse restResult = restClient.callEventTypesPostRESTCall(methodName,
                                                                              urlTemplate,
                                                                              requestBody,
                                                                              serverName,
                                                                              userId,
                                                                              startFrom,
                                                                              validatedPageSize);

        return restResult.getElements();
    }


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
    @Override
    public List<EventTypeElement> getEventTypesForEventSet(String userId,
                                                           String eventSetGUID,
                                                           int    startFrom,
                                                           int    pageSize) throws InvalidParameterException,
                                                                                   UserNotAuthorizedException,
                                                                                   PropertyServerException
    {
        final String methodName                     = "getEventTypesForTopic";
        final String parentElementGUIDParameterName = "topicGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(eventSetGUID, parentElementGUIDParameterName, methodName);
        int validatedPageSize = invalidParameterHandler.validatePaging(startFrom, pageSize, methodName);

        final String urlTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/access-services/data-manager/users/{1}/event-sets/{2}/event-types?startFrom={3}&pageSize={4}";

        EventTypesResponse restResult = restClient.callEventTypesGetRESTCall(methodName,
                                                                             urlTemplate,
                                                                             serverName,
                                                                             userId,
                                                                             eventSetGUID,
                                                                             startFrom,
                                                                             validatedPageSize);

        return restResult.getElements();
    }


    /**
     * Return the list of event-types associated with a topic.
     *
     * @param userId calling user
     * @param topicGUID unique identifier of the topic to query
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return list of metadata elements describing the event-types associated with the requested topic
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public List<EventTypeElement> getEventTypesForTopic(String userId,
                                                        String topicGUID,
                                                        int    startFrom,
                                                        int    pageSize) throws InvalidParameterException,
                                                                                UserNotAuthorizedException,
                                                                                PropertyServerException
    {
        final String methodName                     = "getEventTypesForTopic";
        final String parentElementGUIDParameterName = "topicGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(topicGUID, parentElementGUIDParameterName, methodName);
        int validatedPageSize = invalidParameterHandler.validatePaging(startFrom, pageSize, methodName);

        final String urlTemplate = serverPlatformURLRoot + topicURLTemplatePrefix + "/{2}/event-types?startFrom={3}&pageSize={4}";

        EventTypesResponse restResult = restClient.callEventTypesGetRESTCall(methodName,
                                                                             urlTemplate,
                                                                             serverName,
                                                                             userId,
                                                                             topicGUID,
                                                                             startFrom,
                                                                             validatedPageSize);

        return restResult.getElements();
    }


    /**
     * Retrieve the list of event type metadata elements with a matching qualified or display name.
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
    @Override
    public List<EventTypeElement>   getEventTypesByName(String userId,
                                                        String name,
                                                        int    startFrom,
                                                        int    pageSize) throws InvalidParameterException,
                                                                                UserNotAuthorizedException,
                                                                                PropertyServerException
    {
        final String methodName        = "getEventTypesByName";
        final String nameParameterName = "name";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateName(name, nameParameterName, methodName);
        int validatedPageSize = invalidParameterHandler.validatePaging(startFrom, pageSize, methodName);

        final String urlTemplate = serverPlatformURLRoot + topicURLTemplatePrefix + "/event-types/by-name?startFrom={2}&pageSize={3}";

        NameRequestBody requestBody = new NameRequestBody();

        requestBody.setName(name);
        requestBody.setNamePropertyName(nameParameterName);

        EventTypesResponse restResult = restClient.callEventTypesPostRESTCall(methodName,
                                                                              urlTemplate,
                                                                              requestBody,
                                                                              serverName,
                                                                              userId,
                                                                              startFrom,
                                                                              validatedPageSize);

        return restResult.getElements();
    }


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
    @Override
    public EventTypeElement getEventTypeByGUID(String userId,
                                               String guid) throws InvalidParameterException,
                                                                   UserNotAuthorizedException,
                                                                   PropertyServerException
    {
        final String methodName        = "getEventTypeByGUID";
        final String guidParameterName = "guid";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(guid, guidParameterName, methodName);

        final String urlTemplate = serverPlatformURLRoot + topicURLTemplatePrefix + "/event-types/{2}";

        EventTypeResponse restResult = restClient.callEventTypeGetRESTCall(methodName,
                                                                           urlTemplate,
                                                                           serverName,
                                                                           userId,
                                                                           guid);

        return restResult.getElement();
    }
}
