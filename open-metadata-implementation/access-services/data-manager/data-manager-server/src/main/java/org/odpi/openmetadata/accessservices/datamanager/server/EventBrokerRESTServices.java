/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.datamanager.server;

import org.odpi.openmetadata.accessservices.datamanager.metadataelements.*;
import org.odpi.openmetadata.accessservices.datamanager.properties.*;
import org.odpi.openmetadata.accessservices.datamanager.rest.*;
import org.odpi.openmetadata.commonservices.ffdc.RESTCallLogger;
import org.odpi.openmetadata.commonservices.ffdc.RESTCallToken;
import org.odpi.openmetadata.commonservices.ffdc.RESTExceptionHandler;
import org.odpi.openmetadata.commonservices.ffdc.rest.GUIDResponse;
import org.odpi.openmetadata.commonservices.ffdc.rest.NullRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.VoidResponse;
import org.odpi.openmetadata.commonservices.generichandlers.*;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceStatus;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * EventBrokerRESTServices is the server-side implementation of the Data Manager OMAS's
 * support for topics.  It matches the EventBrokerClient.
 */
public class EventBrokerRESTServices
{
    private static DataManagerInstanceHandler instanceHandler = new DataManagerInstanceHandler();
    private static RESTCallLogger             restCallLogger  = new RESTCallLogger(LoggerFactory.getLogger(EventBrokerRESTServices.class),
                                                                                   instanceHandler.getServiceName());

    private RESTExceptionHandler     restExceptionHandler = new RESTExceptionHandler();

    /**
     * Default constructor
     */
    public EventBrokerRESTServices()
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
     * @param eventBrokerGUID unique identifier of software server capability representing the DBMS
     * @param eventBrokerName unique name of software server capability representing the DBMS
     * @param topicProperties properties to store
     *
     * @return unique identifier of the new metadata element or
     * InvalidParameterException  one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to issue this request or
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public GUIDResponse createTopic(String          serverName,
                                    String          userId,
                                    String          eventBrokerGUID,
                                    String          eventBrokerName,
                                    TopicProperties topicProperties)
    {
        final String methodName = "createTopic";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        GUIDResponse response = new GUIDResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            AssetHandler<TopicElement> handler = instanceHandler.getTopicHandler(userId, serverName, methodName);

            String typeName = OpenMetadataAPIMapper.TOPIC_TYPE_NAME;

            if (topicProperties.getTypeName() != null)
            {
                typeName = topicProperties.getTypeName();
            }

            Map<String, Object> extendedProperties = topicProperties.getExtendedProperties();

            if (topicProperties.getTopicType() != null)
            {
                if (extendedProperties == null)
                {
                    extendedProperties = new HashMap<>();
                }

                extendedProperties.put(OpenMetadataAPIMapper.TOPIC_TYPE_PROPERTY_NAME, topicProperties.getTopicType());
            }

            String topicGUID = handler.createAssetInRepository(userId,
                                                               eventBrokerGUID,
                                                               eventBrokerName,
                                                               topicProperties.getQualifiedName(),
                                                               topicProperties.getDisplayName(),
                                                               topicProperties.getDescription(),
                                                               topicProperties.getAdditionalProperties(),
                                                               typeName,
                                                               extendedProperties,
                                                               InstanceStatus.ACTIVE,
                                                               methodName);

            if (topicProperties.getVendorProperties() != null)
            {
                handler.setVendorProperties(userId,
                                            topicGUID,
                                            topicProperties.getVendorProperties(),
                                            methodName);
            }

            response.setGUID(topicGUID);
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }


    /**
     * Create a new metadata element to represent a topic using an existing metadata element as a template.
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param eventBrokerGUID unique identifier of software server capability representing the DBMS
     * @param eventBrokerName unique name of software server capability representing the DBMS
     * @param templateGUID unique identifier of the metadata element to copy
     * @param templateProperties properties that override the template
     *
     * @return unique identifier of the new metadata element or
     * InvalidParameterException  one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to issue this request or
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public GUIDResponse createTopicFromTemplate(String             serverName,
                                                String             userId,
                                                String             eventBrokerGUID,
                                                String             eventBrokerName,
                                                String             templateGUID,
                                                TemplateProperties templateProperties)
    {
        final String methodName                 = "createTopicFromTemplate";
        final String templateGUIDParameterName  = "templateGUID";
        final String qualifiedNameParameterName = "qualifiedName";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        GUIDResponse response = new GUIDResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            AssetHandler<TopicElement> handler = instanceHandler.getTopicHandler(userId, serverName, methodName);

            response.setGUID(handler.addAssetFromTemplate(userId,
                                                          eventBrokerGUID,
                                                          eventBrokerName,
                                                          templateGUID,
                                                          templateGUIDParameterName,
                                                          OpenMetadataAPIMapper.TOPIC_TYPE_GUID,
                                                          OpenMetadataAPIMapper.TOPIC_TYPE_NAME,
                                                          templateProperties.getQualifiedName(),
                                                          qualifiedNameParameterName,
                                                          templateProperties.getDisplayName(),
                                                          templateProperties.getDescription(),
                                                          templateProperties.getNetworkAddress(),
                                                          methodName));
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }


    /**
     * Update the metadata element representing a topic.
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param eventBrokerGUID unique identifier of software server capability representing the DBMS
     * @param eventBrokerName unique name of software server capability representing the DBMS
     * @param topicGUID unique identifier of the metadata element to update
     * @param isMergeUpdate should the new properties be merged with the existing properties of overlay them?
     * @param topicProperties new properties for this element
     *
     * @return void or
     * InvalidParameterException  one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to issue this request or
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public VoidResponse updateTopic(String          serverName,
                                    String          userId,
                                    String          eventBrokerGUID,
                                    String          eventBrokerName,
                                    String          topicGUID,
                                    boolean         isMergeUpdate,
                                    TopicProperties topicProperties)
    {
        final String methodName = "updateTopic";
        final String topicGUIDParameterName = "topicGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            AssetHandler<TopicElement> handler = instanceHandler.getTopicHandler(userId, serverName, methodName);

            String typeName = OpenMetadataAPIMapper.TOPIC_TYPE_NAME;

            if (topicProperties.getTypeName() != null)
            {
                typeName = topicProperties.getTypeName();
            }

            Map<String, Object> extendedProperties = topicProperties.getExtendedProperties();

            if (topicProperties.getTopicType() != null)
            {
                if (extendedProperties == null)
                {
                    extendedProperties = new HashMap<>();
                }

                extendedProperties.put(OpenMetadataAPIMapper.TOPIC_TYPE_PROPERTY_NAME, topicProperties.getTopicType());
            }

            handler.updateAsset(userId,
                                eventBrokerGUID,
                                eventBrokerName,
                                topicGUID,
                                topicGUIDParameterName,
                                topicProperties.getQualifiedName(),
                                topicProperties.getDisplayName(),
                                topicProperties.getDescription(),
                                topicProperties.getAdditionalProperties(),
                                typeName,
                                topicProperties.getExtendedProperties(),
                                isMergeUpdate,
                                methodName);

            if (topicProperties.getVendorProperties() == null)
            {
                if (! isMergeUpdate)
                {
                    // todo delete vendor properties
                }
            }
            else
            {
                // todo update vendor properties
            }
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
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
    @SuppressWarnings(value = "unused")
    public VoidResponse publishTopic(String          serverName,
                                     String          userId,
                                     String          topicGUID,
                                     NullRequestBody nullRequestBody)
    {
        final String methodName = "publishTopic";
        final String topicGUIDParameterName = "topicGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            AssetHandler<TopicElement> handler = instanceHandler.getTopicHandler(userId, serverName, methodName);

            handler.publishAsset(userId, topicGUID, topicGUIDParameterName, methodName);
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
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
    @SuppressWarnings(value = "unused")
    public VoidResponse withdrawTopic(String          serverName,
                                      String          userId,
                                      String          topicGUID,
                                      NullRequestBody nullRequestBody)
    {
        final String methodName = "withdrawTopic";
        final String topicGUIDParameterName = "topicGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            AssetHandler<TopicElement> handler = instanceHandler.getTopicHandler(userId, serverName, methodName);

            handler.withdrawAsset(userId, topicGUID, topicGUIDParameterName, methodName);
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }


    /**
     * Remove the metadata element representing a topic.
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param eventBrokerGUID unique identifier of software server capability representing the DBMS
     * @param eventBrokerName unique name of software server capability representing the DBMS
     * @param topicGUID unique identifier of the metadata element to remove
     * @param qualifiedName unique name of the metadata element to remove
     * @param nullRequestBody empty request body
     *
     * @return void or
     * InvalidParameterException  one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to issue this request or
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @SuppressWarnings(value = "unused")
    public VoidResponse removeTopic(String          serverName,
                                    String          userId,
                                    String          eventBrokerGUID,
                                    String          eventBrokerName,
                                    String          topicGUID,
                                    String          qualifiedName,
                                    NullRequestBody nullRequestBody)
    {
        final String methodName = "removeTopic";
        final String topicGUIDParameterName = "topicGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            AssetHandler<TopicElement> handler = instanceHandler.getTopicHandler(userId, serverName, methodName);

            handler.deleteBeanInRepository(userId,
                                           eventBrokerGUID,
                                           eventBrokerName,
                                           topicGUID,
                                           topicGUIDParameterName,
                                           OpenMetadataAPIMapper.TOPIC_TYPE_GUID,
                                           OpenMetadataAPIMapper.TOPIC_TYPE_NAME,
                                           OpenMetadataAPIMapper.QUALIFIED_NAME_PROPERTY_NAME,
                                           qualifiedName,
                                           methodName);
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
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
    public TopicsResponse findTopics(String serverName,
                                     String userId,
                                     String searchString,
                                     int    startFrom,
                                     int    pageSize)
    {
        final String methodName = "findTopics";
        final String searchStringParameterName = "searchString";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        TopicsResponse response = new TopicsResponse();
        AuditLog          auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            AssetHandler<TopicElement> handler = instanceHandler.getTopicHandler(userId, serverName, methodName);

            List<TopicElement> topicAssets = handler.findAssets(userId,
                                                                searchString,
                                                                searchStringParameterName,
                                                                startFrom,
                                                                pageSize,
                                                                methodName);

            response.setElementList(topicAssets);
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
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
    public TopicsResponse   getTopicsByName(String serverName,
                                            String userId,
                                            String name,
                                            int    startFrom,
                                            int    pageSize)
    {
        final String methodName = "getTopicsByName";
        final String nameParameterName = "name";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        TopicsResponse response = new TopicsResponse();
        AuditLog          auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            AssetHandler<TopicElement> handler = instanceHandler.getTopicHandler(userId, serverName, methodName);

            List<TopicElement> topicAssets = handler.getAssetsByName(userId,
                                                                     OpenMetadataAPIMapper.TOPIC_TYPE_GUID,
                                                                     OpenMetadataAPIMapper.TOPIC_TYPE_NAME,
                                                                     name,
                                                                     nameParameterName,
                                                                     startFrom,
                                                                     pageSize,
                                                                     methodName);

            response.setElementList(topicAssets);
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }


    /**
     * Retrieve the list of topics created by this caller.
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param eventBrokerGUID unique identifier of software server capability representing the DBMS
     * @param eventBrokerName unique name of software server capability representing the DBMS
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return list of matching metadata elements or
     * InvalidParameterException  one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to issue this request or
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @SuppressWarnings(value = "unused")
    public TopicsResponse   getTopicsForEventBroker(String serverName,
                                                    String userId,
                                                    String eventBrokerGUID,
                                                    String eventBrokerName,
                                                    int    startFrom,
                                                    int    pageSize)
    {
        final String methodName = "getTopicsForEventBroker";
        final String eventBrokerGUIDParameterName = "eventBrokerGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        TopicsResponse response = new TopicsResponse();
        AuditLog          auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            AssetHandler<TopicElement> handler = instanceHandler.getTopicHandler(userId, serverName, methodName);

            List<TopicElement> topicAssets = handler.getAttachedElements(userId,
                                                                         eventBrokerGUID,
                                                                         eventBrokerGUIDParameterName,
                                                                         OpenMetadataAPIMapper.EVENT_BROKER_TYPE_NAME,
                                                                         OpenMetadataAPIMapper.SERVER_ASSET_USE_TYPE_GUID,
                                                                         OpenMetadataAPIMapper.SERVER_ASSET_USE_TYPE_NAME,
                                                                         OpenMetadataAPIMapper.TOPIC_TYPE_NAME,
                                                                         startFrom,
                                                                         pageSize,
                                                                         methodName);

            response.setElementList(topicAssets);
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
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
    public TopicResponse getTopicByGUID(String serverName,
                                        String userId,
                                        String guid)
    {
        final String methodName = "getTopicByGUID";
        final String guidParameterName = "topicGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        TopicResponse response = new TopicResponse();
        AuditLog         auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            AssetHandler<TopicElement> handler = instanceHandler.getTopicHandler(userId, serverName, methodName);

            TopicElement topicAsset = handler.getBeanFromRepository(userId,
                                                                    guid,
                                                                    guidParameterName,
                                                                    OpenMetadataAPIMapper.TOPIC_TYPE_NAME,
                                                                    methodName);

            response.setElement(topicAsset);
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }


    /* ============================================================================
     * A topic may host one or more event types depending on its capability
     */

    /**
     * Create a new metadata element to represent a event type.
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param eventBrokerGUID unique identifier of software server capability representing the DBMS
     * @param eventBrokerName unique name of software server capability representing the DBMS
     * @param topicGUID unique identifier of the topic where the schema is located
     * @param eventTypeProperties properties about the event type
     *
     * @return unique identifier of the new event type or
     * InvalidParameterException  one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to issue this request or
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public GUIDResponse createEventType(String              serverName,
                                        String              userId,
                                        String              eventBrokerGUID,
                                        String              eventBrokerName,
                                        String              topicGUID,
                                        EventTypeProperties eventTypeProperties)
    {
        final String methodName = "createEventType";
        final String topicGUIDParameterName = "topicGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        GUIDResponse response = new GUIDResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            EventTypeHandler<EventTypeElement> handler = instanceHandler.getEventTypeHandler(userId, serverName, methodName);

            String eventTypeGUID = handler.createEventType(userId,
                                                             eventBrokerGUID,
                                                             eventBrokerName,
                                                             topicGUID,
                                                             topicGUIDParameterName,
                                                             eventTypeProperties.getQualifiedName(),
                                                             eventTypeProperties.getDisplayName(),
                                                             eventTypeProperties.getDescription(),
                                                             eventTypeProperties.getVersionNumber(),
                                                             eventTypeProperties.getIsDeprecated(),
                                                             eventTypeProperties.getAuthor(),
                                                             eventTypeProperties.getUsage(),
                                                             eventTypeProperties.getEncodingStandard(),
                                                             eventTypeProperties.getNamespace(),
                                                             eventTypeProperties.getAdditionalProperties(),
                                                             eventTypeProperties.getTypeName(),
                                                             eventTypeProperties.getExtendedProperties(),
                                                             methodName);

            if (eventTypeProperties.getVendorProperties() != null)
            {
                handler.setVendorProperties(userId,
                                            topicGUID,
                                            eventTypeProperties.getVendorProperties(),
                                            methodName);
            }

            response.setGUID(eventTypeGUID);
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }


    /**
     * Create a new metadata element to represent a event type using an existing metadata element as a template.
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param eventBrokerGUID unique identifier of software server capability representing the DBMS
     * @param eventBrokerName unique name of software server capability representing the DBMS
     * @param templateGUID unique identifier of the metadata element to copy
     * @param topicGUID unique identifier of the topic where the schema is located
     * @param templateProperties properties that override the template
     *
     * @return unique identifier of the new event type or
     * InvalidParameterException  one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to issue this request or
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public GUIDResponse createEventTypeFromTemplate(String             serverName,
                                                    String             userId,
                                                    String             eventBrokerGUID,
                                                    String             eventBrokerName,
                                                    String             templateGUID,
                                                    String             topicGUID,
                                                    TemplateProperties templateProperties)
    {
        final String methodName = "createEventTypeFromTemplate";
        final String topicGUIDParameterName = "topicGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        GUIDResponse response = new GUIDResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            EventTypeHandler<EventTypeElement> handler = instanceHandler.getEventTypeHandler(userId, serverName, methodName);

            response.setGUID(handler.createEventTypeFromTemplate(userId,
                                                                 eventBrokerGUID,
                                                                 eventBrokerName,
                                                                 topicGUID,
                                                                 topicGUIDParameterName,
                                                                 templateGUID,
                                                                 templateProperties.getQualifiedName(),
                                                                 templateProperties.getDisplayName(),
                                                                 templateProperties.getDescription(),
                                                                 methodName));
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }


    /**
     * Update the metadata element representing a event type.
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param eventBrokerGUID unique identifier of software server capability representing the DBMS
     * @param eventBrokerName unique name of software server capability representing the DBMS
     * @param eventTypeGUID unique identifier of the metadata element to update
     * @param isMergeUpdate are unspecified properties unchanged (true) or removed?
     * @param eventTypeProperties new properties for the metadata element
     *
     * @return void or
     * InvalidParameterException  one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to issue this request or
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public VoidResponse updateEventType(String              serverName,
                                        String              userId,
                                        String              eventBrokerGUID,
                                        String              eventBrokerName,
                                        String              eventTypeGUID,
                                        boolean             isMergeUpdate,
                                        EventTypeProperties eventTypeProperties)
    {
        final String methodName = "updateEventType";
        final String eventTypeGUIDParameterName = "eventTypeGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            EventTypeHandler<EventTypeElement> handler = instanceHandler.getEventTypeHandler(userId, serverName, methodName);

            handler.updateEventType(userId,
                                    eventBrokerGUID,
                                    eventBrokerName,
                                    eventTypeGUID,
                                    eventTypeGUIDParameterName,
                                    eventTypeProperties.getQualifiedName(),
                                    eventTypeProperties.getDisplayName(),
                                    eventTypeProperties.getDescription(),
                                    eventTypeProperties.getVersionNumber(),
                                    eventTypeProperties.getIsDeprecated(),
                                    eventTypeProperties.getAuthor(),
                                    eventTypeProperties.getUsage(),
                                    eventTypeProperties.getEncodingStandard(),
                                    eventTypeProperties.getNamespace(),
                                    eventTypeProperties.getAdditionalProperties(),
                                    eventTypeProperties.getTypeName(),
                                    eventTypeProperties.getExtendedProperties(),
                                    isMergeUpdate,
                                    methodName);

            if (eventTypeProperties.getVendorProperties() == null)
            {
                if (! isMergeUpdate)
                {
                    // todo delete vendor properties
                }
            }
            else
            {
                // todo update vendor properties
            }
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }


    /**
     * Remove the metadata element representing a event type.
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param eventBrokerGUID unique identifier of software server capability representing the DBMS
     * @param eventBrokerName unique name of software server capability representing the DBMS
     * @param eventTypeGUID unique identifier of the metadata element to remove
     * @param qualifiedName unique name of the metadata element to remove
     * @param nullRequestBody empty request body
     *
     * @return void or
     * InvalidParameterException  one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to issue this request or
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @SuppressWarnings(value = "unused")
    public VoidResponse removeEventType(String          serverName,
                                        String          userId,
                                        String          eventBrokerGUID,
                                        String          eventBrokerName,
                                        String          eventTypeGUID,
                                        String          qualifiedName,
                                        NullRequestBody nullRequestBody)
    {
        final String methodName = "removeEventType";
        final String eventTypeGUIDParameterName = "eventTypeGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            EventTypeHandler<EventTypeElement> handler = instanceHandler.getEventTypeHandler(userId, serverName, methodName);

            handler.removeEventType(userId,
                                    eventBrokerGUID,
                                    eventBrokerName,
                                    eventTypeGUID,
                                    eventTypeGUIDParameterName,
                                    qualifiedName,
                                    methodName);
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
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
    public EventTypesResponse findEventTypes(String serverName,
                                             String userId,
                                             String searchString,
                                             int    startFrom,
                                             int    pageSize)
    {
        final String methodName = "findEventTypes";
        final String searchStringParameterName = "searchString";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        EventTypesResponse response = new EventTypesResponse();
        AuditLog           auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            EventTypeHandler<EventTypeElement> handler = instanceHandler.getEventTypeHandler(userId, serverName, methodName);

            List<EventTypeElement> topicSchemaAssets = handler.findEventTypes(userId, searchString, searchStringParameterName, startFrom, pageSize, methodName);

            response.setElementList(topicSchemaAssets);
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }



    /**
     * Return the list of event types associated with a EventType Set.
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
    public EventTypesResponse getEventTypesForEventSet(String serverName,
                                                       String userId,
                                                       String topicGUID,
                                                       int    startFrom,
                                                       int    pageSize)
    {
        final String methodName = "getEventTypesForEventSet";
        final String topicGUIDParameterName = "topicGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        EventTypesResponse response = new EventTypesResponse();
        AuditLog           auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            EventTypeHandler<EventTypeElement> handler = instanceHandler.getEventTypeHandler(userId, serverName, methodName);

            List<EventTypeElement> topicSchemaAssets = handler.getEventTypesForEventSet(userId,
                                                                                        topicGUID,
                                                                                        topicGUIDParameterName,
                                                                                        startFrom,
                                                                                        pageSize,
                                                                                        methodName);

            response.setElementList(topicSchemaAssets);
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
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
    public EventTypesResponse getEventTypesForTopic(String serverName,
                                                    String userId,
                                                    String topicGUID,
                                                    int    startFrom,
                                                    int    pageSize)
    {
        final String methodName = "getEventTypesForTopic";
        final String topicGUIDParameterName = "topicGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        EventTypesResponse response = new EventTypesResponse();
        AuditLog           auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            EventTypeHandler<EventTypeElement> handler = instanceHandler.getEventTypeHandler(userId, serverName, methodName);

            List<EventTypeElement> topicSchemaAssets = handler.getEventTypesForTopic(userId,
                                                                                     topicGUID,
                                                                                     topicGUIDParameterName,
                                                                                     startFrom,
                                                                                     pageSize,
                                                                                     methodName);

            response.setElementList(topicSchemaAssets);
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
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
    public EventTypesResponse getEventTypesByName(String serverName,
                                                  String userId,
                                                  String name,
                                                  int    startFrom,
                                                  int    pageSize)
    {
        final String methodName = "getEventTypesByName";
        final String nameParameterName = "name";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        EventTypesResponse response = new EventTypesResponse();
        AuditLog           auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            EventTypeHandler<EventTypeElement> handler = instanceHandler.getEventTypeHandler(userId, serverName, methodName);

            List<EventTypeElement> topicSchemaAssets = handler.getEventTypesByName(userId, name, nameParameterName, startFrom, pageSize, methodName);

            response.setElementList(topicSchemaAssets);
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
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
    public EventTypeResponse getEventTypeByGUID(String serverName,
                                                String userId,
                                                String guid)
    {
        final String methodName = "getEventTypeByGUID";
        final String eventTypeGUIDParameterName = "eventTypeGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        EventTypeResponse response = new EventTypeResponse();
        AuditLog          auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            EventTypeHandler<EventTypeElement> handler = instanceHandler.getEventTypeHandler(userId, serverName, methodName);

            EventTypeElement topicSchemaAsset = handler.getEventTypeByGUID(userId, guid, eventTypeGUIDParameterName, methodName);

            response.setElement(topicSchemaAsset);
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }
}
