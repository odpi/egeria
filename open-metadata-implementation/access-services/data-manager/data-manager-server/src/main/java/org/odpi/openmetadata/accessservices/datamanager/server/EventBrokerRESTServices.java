/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.datamanager.server;

import org.odpi.openmetadata.accessservices.datamanager.metadataelements.*;
import org.odpi.openmetadata.accessservices.datamanager.properties.EventTypeProperties;
import org.odpi.openmetadata.accessservices.datamanager.properties.TopicProperties;
import org.odpi.openmetadata.accessservices.datamanager.rest.*;
import org.odpi.openmetadata.commonservices.ffdc.RESTCallLogger;
import org.odpi.openmetadata.commonservices.ffdc.RESTCallToken;
import org.odpi.openmetadata.commonservices.ffdc.RESTExceptionHandler;
import org.odpi.openmetadata.commonservices.ffdc.rest.GUIDResponse;
import org.odpi.openmetadata.commonservices.ffdc.rest.NameRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.NullRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.SearchStringRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.VoidResponse;
import org.odpi.openmetadata.commonservices.generichandlers.AssetHandler;
import org.odpi.openmetadata.commonservices.generichandlers.EventTypeHandler;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.governanceaction.mapper.OpenMetadataProperty;
import org.odpi.openmetadata.frameworks.governanceaction.mapper.OpenMetadataType;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceStatus;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * EventBrokerRESTServices is the server-side implementation of the Data Manager OMAS's
 * support for topics.  It matches the EventBrokerClient.
 */
public class EventBrokerRESTServices
{
    private static final DataManagerInstanceHandler instanceHandler = new DataManagerInstanceHandler();
    private static final RESTCallLogger             restCallLogger  = new RESTCallLogger(LoggerFactory.getLogger(EventBrokerRESTServices.class),
                                                                                         instanceHandler.getServiceName());

    private static final RESTExceptionHandler     restExceptionHandler = new RESTExceptionHandler();

    /**
     * Default constructor
     */
    public EventBrokerRESTServices()
    {
    }


    /* ========================================================
     * The topic is the top level asset on an event broker
     */

    /**
     * Create a new metadata element to represent a topic.
     *
     * @param serverName name of the server to route the request to.
     * @param userId calling user
     * @param eventBrokerIsHome should the topic be marked as owned by the event broker so others can not update?
     * @param requestBody properties to store
     *
     * @return unique identifier of the new metadata element or
     * InvalidParameterException  one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to issue this request or
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public GUIDResponse createTopic(String           serverName,
                                    String           userId,
                                    boolean          eventBrokerIsHome,
                                    TopicRequestBody requestBody)
    {
        final String methodName                   = "createTopic";
        final String eventBrokerGUIDParameterName = "eventBrokerGUID";
        final String topicGUIDParameterName       = "topicGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        GUIDResponse response = new GUIDResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            AssetHandler<TopicElement> handler = instanceHandler.getTopicHandler(userId, serverName, methodName);

            if (requestBody != null)
            {
                String typeName = OpenMetadataType.TOPIC_TYPE_NAME;

                if (requestBody.getTypeName() != null)
                {
                    typeName = requestBody.getTypeName();
                }

                Map<String, Object> extendedProperties = requestBody.getExtendedProperties();

                if (requestBody.getTopicType() != null)
                {
                    if (extendedProperties == null)
                    {
                        extendedProperties = new HashMap<>();
                    }

                    extendedProperties.put(OpenMetadataType.TOPIC_TYPE_PROPERTY_NAME, requestBody.getTopicType());
                }

                String topicGUID;

                if (eventBrokerIsHome)
                {
                    topicGUID = handler.createAssetInRepository(userId,
                                                                requestBody.getExternalSourceGUID(),
                                                                requestBody.getExternalSourceName(),
                                                                requestBody.getQualifiedName(),
                                                                requestBody.getName(),
                                                                requestBody.getVersionIdentifier(),
                                                                requestBody.getDescription(),
                                                                requestBody.getAdditionalProperties(),
                                                                typeName,
                                                                extendedProperties,
                                                                InstanceStatus.ACTIVE,
                                                                requestBody.getEffectiveFrom(),
                                                                requestBody.getEffectiveTo(),
                                                                new Date(),
                                                                methodName);

                    if ((topicGUID != null) && (requestBody.getExternalSourceGUID() != null))
                    {
                        handler.linkElementToElement(userId,
                                                     requestBody.getExternalSourceGUID(),
                                                     requestBody.getExternalSourceName(),
                                                     requestBody.getExternalSourceGUID(),
                                                     eventBrokerGUIDParameterName,
                                                     OpenMetadataType.SOFTWARE_CAPABILITY.typeName,
                                                     topicGUID,
                                                     topicGUIDParameterName,
                                                     OpenMetadataType.TOPIC_TYPE_NAME,
                                                     false,
                                                     false,
                                                     OpenMetadataType.SERVER_ASSET_USE_RELATIONSHIP.typeGUID,
                                                     OpenMetadataType.SERVER_ASSET_USE_RELATIONSHIP.typeName,
                                                     (InstanceProperties) null,
                                                     null,
                                                     null,
                                                     new Date(),
                                                     methodName);
                    }
                }
                else
                {
                    topicGUID = handler.createAssetInRepository(userId,
                                                                null,
                                                                null,
                                                                requestBody.getQualifiedName(),
                                                                requestBody.getName(),
                                                                requestBody.getVersionIdentifier(),
                                                                requestBody.getDescription(),
                                                                requestBody.getAdditionalProperties(),
                                                                typeName,
                                                                extendedProperties,
                                                                InstanceStatus.ACTIVE,
                                                                requestBody.getEffectiveFrom(),
                                                                requestBody.getEffectiveTo(),
                                                                new Date(),
                                                                methodName);

                    if ((topicGUID != null) && (requestBody.getExternalSourceGUID() != null))
                    {
                        handler.linkElementToElement(userId,
                                                     null,
                                                     null,
                                                     requestBody.getExternalSourceGUID(),
                                                     eventBrokerGUIDParameterName,
                                                     OpenMetadataType.SOFTWARE_CAPABILITY.typeName,
                                                     topicGUID,
                                                     topicGUIDParameterName,
                                                     OpenMetadataType.TOPIC_TYPE_NAME,
                                                     false,
                                                     false,
                                                     OpenMetadataType.SERVER_ASSET_USE_RELATIONSHIP.typeGUID,
                                                     OpenMetadataType.SERVER_ASSET_USE_RELATIONSHIP.typeName,
                                                     (InstanceProperties) null,
                                                     null,
                                                     null,
                                                     new Date(),
                                                     methodName);
                    }
                }

                if (topicGUID != null)
                {
                    handler.setVendorProperties(userId,
                                                topicGUID,
                                                requestBody.getVendorProperties(),
                                                false,
                                                false,
                                                new Date(),
                                                methodName);
                }

                response.setGUID(topicGUID);
            }
            else
            {
                restExceptionHandler.handleNoRequestBody(userId, methodName, serverName);
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
     * Create a new metadata element to represent a topic using an existing metadata element as a template.
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param templateGUID unique identifier of the metadata element to copy
     * @param eventBrokerIsHome should the topic be marked as owned by the event broker so others can not update?
     * @param requestBody properties that override the template
     *
     * @return unique identifier of the new metadata element or
     * InvalidParameterException  one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to issue this request or
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public GUIDResponse createTopicFromTemplate(String              serverName,
                                                String              userId,
                                                String              templateGUID,
                                                boolean             eventBrokerIsHome,
                                                TemplateRequestBody requestBody)
    {
        final String methodName                   = "createTopicFromTemplate";
        final String eventBrokerGUIDParameterName = "eventBrokerGUID";
        final String topicGUIDParameterName       = "topicGUID";
        final String templateGUIDParameterName    = "templateGUID";
        final String qualifiedNameParameterName   = "qualifiedName";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        GUIDResponse response = new GUIDResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            AssetHandler<TopicElement> handler = instanceHandler.getTopicHandler(userId, serverName, methodName);

            if (requestBody != null)
            {
                String topicGUID = handler.addAssetFromTemplate(userId,
                                                                handler.getExternalSourceID(eventBrokerIsHome, requestBody.getExternalSourceGUID()),
                                                                handler.getExternalSourceID(eventBrokerIsHome, requestBody.getExternalSourceName()),
                                                                templateGUID,
                                                                templateGUIDParameterName,
                                                                OpenMetadataType.TOPIC_TYPE_GUID,
                                                                OpenMetadataType.TOPIC_TYPE_NAME,
                                                                requestBody.getQualifiedName(),
                                                                qualifiedNameParameterName,
                                                                requestBody.getDisplayName(),
                                                                requestBody.getVersionIdentifier(),
                                                                requestBody.getDescription(),
                                                                null,
                                                                requestBody.getNetworkAddress(),
                                                                false,
                                                                false,
                                                                new Date(),
                                                                methodName);

                if ((topicGUID != null) && (requestBody.getExternalSourceGUID() != null))
                {
                    handler.linkElementToElement(userId,
                                                 handler.getExternalSourceID(eventBrokerIsHome, requestBody.getExternalSourceGUID()),
                                                 handler.getExternalSourceID(eventBrokerIsHome, requestBody.getExternalSourceName()),
                                                 requestBody.getExternalSourceGUID(),
                                                 eventBrokerGUIDParameterName,
                                                 OpenMetadataType.SOFTWARE_CAPABILITY.typeName,
                                                 topicGUID,
                                                 topicGUIDParameterName,
                                                 OpenMetadataType.TOPIC_TYPE_NAME,
                                                 false,
                                                 false,
                                                 OpenMetadataType.SERVER_ASSET_USE_RELATIONSHIP.typeGUID,
                                                 OpenMetadataType.SERVER_ASSET_USE_RELATIONSHIP.typeName,
                                                 (InstanceProperties) null,
                                                 null,
                                                 null,
                                                 new Date(),
                                                 methodName);
                }


                response.setGUID(topicGUID);
            }
            else
            {
                restExceptionHandler.handleNoRequestBody(userId, methodName, serverName);
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
     * Update the metadata element representing a topic.
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param topicGUID unique identifier of the metadata element to update
     * @param isMergeUpdate should the new properties be merged with the existing properties of overlay them?
     * @param requestBody new properties for this element
     *
     * @return void or
     * InvalidParameterException  one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to issue this request or
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public VoidResponse updateTopic(String           serverName,
                                    String           userId,
                                    String           topicGUID,
                                    boolean          isMergeUpdate,
                                    TopicRequestBody requestBody)
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

            if (requestBody != null)
            {
                String typeName = OpenMetadataType.TOPIC_TYPE_NAME;

                if (requestBody.getTypeName() != null)
                {
                    typeName = requestBody.getTypeName();
                }

                Map<String, Object> extendedProperties = requestBody.getExtendedProperties();

                if (requestBody.getTopicType() != null)
                {
                    if (extendedProperties == null)
                    {
                        extendedProperties = new HashMap<>();
                    }

                    extendedProperties.put(OpenMetadataType.TOPIC_TYPE_PROPERTY_NAME, requestBody.getTopicType());
                }

                handler.updateAsset(userId,
                                    requestBody.getExternalSourceGUID(),
                                    requestBody.getExternalSourceName(),
                                    topicGUID,
                                    topicGUIDParameterName,
                                    requestBody.getQualifiedName(),
                                    requestBody.getName(),
                                    requestBody.getVersionIdentifier(),
                                    requestBody.getDescription(),
                                    requestBody.getAdditionalProperties(),
                                    typeName,
                                    extendedProperties,
                                    requestBody.getEffectiveFrom(),
                                    requestBody.getEffectiveTo(),
                                    isMergeUpdate,
                                    false,
                                    false,
                                    new Date(),
                                    methodName);

                if ((!isMergeUpdate) || (requestBody.getVendorProperties() != null))
                {
                    handler.setVendorProperties(userId,
                                                topicGUID,
                                                requestBody.getVendorProperties(),
                                                false,
                                                false,
                                                new Date(),
                                                methodName);
                }
            }
            else
            {
                restExceptionHandler.handleNoRequestBody(userId, methodName, serverName);
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

            handler.publishAsset(userId, topicGUID, topicGUIDParameterName, false, false, new Date(), methodName);
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

            handler.withdrawAsset(userId, topicGUID, topicGUIDParameterName, false, false, new Date(), methodName);
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
     * @param topicGUID unique identifier of the metadata element to remove
     * @param qualifiedName unique name of the metadata element to remove
     * @param requestBody external source identifiers
     *
     * @return void or
     * InvalidParameterException  one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to issue this request or
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public VoidResponse removeTopic(String                    serverName,
                                    String                    userId,
                                    String                    topicGUID,
                                    String                    qualifiedName,
                                    MetadataSourceRequestBody requestBody)
    {
        final String methodName = "removeTopic";
        final String topicGUIDParameterName = "topicGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            if (requestBody != null)
            {
                auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

                AssetHandler<TopicElement> handler = instanceHandler.getTopicHandler(userId, serverName, methodName);

                handler.deleteBeanInRepository(userId,
                                               requestBody.getExternalSourceGUID(),
                                               requestBody.getExternalSourceName(),
                                               topicGUID,
                                               topicGUIDParameterName,
                                               OpenMetadataType.TOPIC_TYPE_GUID,
                                               OpenMetadataType.TOPIC_TYPE_NAME,
                                               OpenMetadataProperty.QUALIFIED_NAME.name,
                                               qualifiedName,
                                               false,
                                               false,
                                               new Date(),
                                               methodName);
            }
            else
            {
                restExceptionHandler.handleNoRequestBody(userId, methodName, serverName);
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
     * Retrieve the list of topic metadata elements that contain the search string.
     * The search string is treated as a regular expression.
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param requestBody string to find in the properties
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return list of matching metadata elements or
     * InvalidParameterException  one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to issue this request or
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public TopicsResponse findTopics(String                  serverName,
                                     String                  userId,
                                     SearchStringRequestBody requestBody,
                                     int                     startFrom,
                                     int                     pageSize)
    {
        final String methodName = "findTopics";
        final String searchStringParameterName = "searchString";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        TopicsResponse response = new TopicsResponse();
        AuditLog          auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
                AssetHandler<TopicElement> handler = instanceHandler.getTopicHandler(userId, serverName, methodName);

                List<TopicElement> topicAssets = handler.findAssets(userId,
                                                                    OpenMetadataType.TOPIC_TYPE_GUID,
                                                                    OpenMetadataType.TOPIC_TYPE_NAME,
                                                                    requestBody.getSearchString(),
                                                                    searchStringParameterName,
                                                                    startFrom,
                                                                    pageSize,
                                                                    false,
                                                                    false,
                                                                    new Date(),
                                                                    methodName);

                response.setElementList(setUpVendorProperties(userId, topicAssets, handler, methodName));
            }
            else
            {
                restExceptionHandler.handleNoRequestBody(userId, methodName, serverName);
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
     * Retrieve the list of topic metadata elements with a matching qualified or display name.
     * There are no wildcards supported on this request.
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param requestBody name to search for
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return list of matching metadata elements or
     * InvalidParameterException  one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to issue this request or
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public TopicsResponse   getTopicsByName(String          serverName,
                                            String          userId,
                                            NameRequestBody requestBody,
                                            int             startFrom,
                                            int             pageSize)
    {
        final String methodName = "getTopicsByName";
        final String nameParameterName = "name";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        TopicsResponse response = new TopicsResponse();
        AuditLog          auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
                AssetHandler<TopicElement> handler = instanceHandler.getTopicHandler(userId, serverName, methodName);

                List<TopicElement> topicAssets = handler.getAssetsByName(userId,
                                                                         OpenMetadataType.TOPIC_TYPE_GUID,
                                                                         OpenMetadataType.TOPIC_TYPE_NAME,
                                                                         requestBody.getName(),
                                                                         nameParameterName,
                                                                         startFrom,
                                                                         pageSize,
                                                                         false,
                                                                         false,
                                                                         new Date(),
                                                                         methodName);

                response.setElementList(setUpVendorProperties(userId, topicAssets, handler, methodName));
            }
            else
            {
                restExceptionHandler.handleNoRequestBody(userId, methodName, serverName);
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
     * Retrieve the list of topics created by this caller.
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param eventBrokerGUID unique identifier of software server capability representing the event broker
     * @param eventBrokerName unique name of software server capability representing the event broker
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
                                                                         OpenMetadataType.SOFTWARE_CAPABILITY.typeName,
                                                                         OpenMetadataType.SERVER_ASSET_USE_RELATIONSHIP.typeGUID,
                                                                         OpenMetadataType.SERVER_ASSET_USE_RELATIONSHIP.typeName,
                                                                         OpenMetadataType.TOPIC_TYPE_NAME,
                                                                         null,
                                                                         null,
                                                                         0,
                                                                         false,
                                                                         false,
                                                                         startFrom,
                                                                         pageSize,
                                                                         new Date(),
                                                                         methodName);

            response.setElementList(setUpVendorProperties(userId, topicAssets, handler, methodName));
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
                                                                    OpenMetadataType.TOPIC_TYPE_NAME,
                                                                    false,
                                                                    false,
                                                                    new Date(),
                                                                    methodName);

            response.setElement(setUpVendorProperties(userId, topicAsset, handler, methodName));
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
     * @param topicGUID unique identifier of the topic where the schema is located
     * @param requestBody properties about the event type
     *
     * @return unique identifier of the new event type or
     * InvalidParameterException  one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to issue this request or
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public GUIDResponse createEventType(String               serverName,
                                        String               userId,
                                        String               topicGUID,
                                        EventTypeRequestBody requestBody)
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

            if (requestBody != null)
            {
                String eventTypeGUID = handler.createEventType(userId,
                                                               requestBody.getExternalSourceGUID(),
                                                               requestBody.getExternalSourceName(),
                                                               topicGUID,
                                                               topicGUIDParameterName,
                                                               requestBody.getQualifiedName(),
                                                               requestBody.getDisplayName(),
                                                               requestBody.getDescription(),
                                                               requestBody.getVersionNumber(),
                                                               requestBody.getIsDeprecated(),
                                                               requestBody.getAuthor(),
                                                               requestBody.getUsage(),
                                                               requestBody.getEncodingStandard(),
                                                               requestBody.getNamespace(),
                                                               requestBody.getAdditionalProperties(),
                                                               requestBody.getTypeName(),
                                                               requestBody.getExtendedProperties(),
                                                               null,
                                                               null,
                                                               false,
                                                               false,
                                                               new Date(),
                                                               methodName);

                handler.setVendorProperties(userId,
                                            eventTypeGUID,
                                            requestBody.getVendorProperties(),
                                            false,
                                            false,
                                            new Date(),
                                            methodName);

                response.setGUID(eventTypeGUID);
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
     * Create a new metadata element to represent an event type using an existing metadata element as a template.
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param templateGUID unique identifier of the metadata element to copy
     * @param topicGUID unique identifier of the topic where the schema is located
     * @param requestBody properties that override the template
     *
     * @return unique identifier of the new event type or
     * InvalidParameterException  one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to issue this request or
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public GUIDResponse createEventTypeFromTemplate(String              serverName,
                                                    String              userId,
                                                    String              templateGUID,
                                                    String              topicGUID,
                                                    TemplateRequestBody requestBody)
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

            if (requestBody != null)
            {
                response.setGUID(handler.createEventTypeFromTemplate(userId,
                                                                     requestBody.getExternalSourceGUID(),
                                                                     requestBody.getExternalSourceName(),
                                                                     topicGUID,
                                                                     topicGUIDParameterName,
                                                                     templateGUID,
                                                                     requestBody.getQualifiedName(),
                                                                     requestBody.getDisplayName(),
                                                                     requestBody.getDescription(),
                                                                     null,
                                                                     null,
                                                                     false,
                                                                     false,
                                                                     new Date(),
                                                                     methodName));
            }
            else
            {
                restExceptionHandler.handleNoRequestBody(userId, methodName, serverName);
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
     * Update the metadata element representing an event type.
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param eventTypeGUID unique identifier of the metadata element to update
     * @param isMergeUpdate are unspecified properties unchanged (true) or removed?
     * @param requestBody new properties for the metadata element
     *
     * @return void or
     * InvalidParameterException  one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to issue this request or
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public VoidResponse updateEventType(String               serverName,
                                        String               userId,
                                        String               eventTypeGUID,
                                        boolean              isMergeUpdate,
                                        EventTypeRequestBody requestBody)
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

            if (requestBody != null)
            {
                handler.updateEventType(userId,
                                        requestBody.getExternalSourceGUID(),
                                        requestBody.getExternalSourceName(),
                                        eventTypeGUID,
                                        eventTypeGUIDParameterName,
                                        requestBody.getQualifiedName(),
                                        requestBody.getDisplayName(),
                                        requestBody.getDescription(),
                                        requestBody.getVersionNumber(),
                                        requestBody.getIsDeprecated(),
                                        requestBody.getAuthor(),
                                        requestBody.getUsage(),
                                        requestBody.getEncodingStandard(),
                                        requestBody.getNamespace(),
                                        requestBody.getAdditionalProperties(),
                                        requestBody.getTypeName(),
                                        requestBody.getExtendedProperties(),
                                        null,
                                        null,
                                        isMergeUpdate,
                                        false,
                                        false,
                                        new Date(),
                                        methodName);

                if ((!isMergeUpdate) || (requestBody.getVendorProperties() != null))
                {
                    handler.setVendorProperties(userId,
                                                eventTypeGUID,
                                                requestBody.getVendorProperties(),
                                                false,
                                                false,
                                                new Date(),
                                                methodName);
                }
            }
            else
            {
                restExceptionHandler.handleNoRequestBody(userId, methodName, serverName);
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
     * Remove the metadata element representing an event type.
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param eventTypeGUID unique identifier of the metadata element to remove
     * @param qualifiedName unique name of the metadata element to remove
     * @param requestBody external source identifiers
     *
     * @return void or
     * InvalidParameterException  one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to issue this request or
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public VoidResponse removeEventType(String                    serverName,
                                        String                    userId,
                                        String                    eventTypeGUID,
                                        String                    qualifiedName,
                                        MetadataSourceRequestBody requestBody)
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

            if (requestBody != null)
            {
                handler.removeEventType(userId,
                                        requestBody.getExternalSourceGUID(),
                                        requestBody.getExternalSourceName(),
                                        eventTypeGUID,
                                        eventTypeGUIDParameterName,
                                        qualifiedName,
                                        false,
                                        false,
                                        new Date(),
                                        methodName);
            }
            else
            {
                restExceptionHandler.handleNoRequestBody(userId, methodName, serverName);
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
     * Retrieve the list of event type metadata elements that contain the search string.
     * The search string is treated as a regular expression.
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param requestBody string to find in the properties
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return list of matching metadata elements or
     * InvalidParameterException  one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to issue this request or
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public EventTypesResponse findEventTypes(String                  serverName,
                                             String                  userId,
                                             SearchStringRequestBody requestBody,
                                             int                     startFrom,
                                             int                     pageSize)
    {
        final String methodName = "findEventTypes";
        final String searchStringParameterName = "searchString";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        EventTypesResponse response = new EventTypesResponse();
        AuditLog           auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
                EventTypeHandler<EventTypeElement> handler = instanceHandler.getEventTypeHandler(userId, serverName, methodName);

                List<EventTypeElement> eventTypes = handler.findEventTypes(userId,
                                                                           requestBody.getSearchString(),
                                                                           searchStringParameterName,
                                                                           startFrom,
                                                                           pageSize,
                                                                           false,
                                                                           false,
                                                                           new Date(),
                                                                           methodName);

                setUpVendorProperties(userId, eventTypes, handler, methodName);
                response.setElementList(eventTypes);
            }
            else
            {
                restExceptionHandler.handleNoRequestBody(userId, methodName, serverName);
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

            List<EventTypeElement> eventTypes = handler.getEventTypesForEventSet(userId,
                                                                                 topicGUID,
                                                                                 topicGUIDParameterName,
                                                                                 startFrom,
                                                                                 pageSize,
                                                                                 false,
                                                                                 false,
                                                                                 new Date(),
                                                                                 methodName);

            setUpVendorProperties(userId, eventTypes, handler, methodName);
            response.setElementList(eventTypes);
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

            List<EventTypeElement> eventTypes = handler.getEventTypesForTopic(userId,
                                                                              topicGUID,
                                                                              topicGUIDParameterName,
                                                                              startFrom,
                                                                              pageSize,
                                                                              false,
                                                                              false,
                                                                              new Date(),
                                                                              methodName);

            setUpVendorProperties(userId, eventTypes, handler, methodName);
            response.setElementList(eventTypes);
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
     * @param requestBody name to search for
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return list of matching metadata elements or
     * InvalidParameterException  one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to issue this request or
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public EventTypesResponse getEventTypesByName(String          serverName,
                                                  String          userId,
                                                  NameRequestBody requestBody,
                                                  int             startFrom,
                                                  int             pageSize)
    {
        final String methodName = "getEventTypesByName";
        final String nameParameterName = "name";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        EventTypesResponse response = new EventTypesResponse();
        AuditLog           auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
                EventTypeHandler<EventTypeElement> handler = instanceHandler.getEventTypeHandler(userId, serverName, methodName);

                List<EventTypeElement> eventTypes = handler.getEventTypesByName(userId,
                                                                                requestBody.getName(),
                                                                                nameParameterName,
                                                                                startFrom,
                                                                                pageSize,
                                                                                false,
                                                                                false,
                                                                                new Date(),
                                                                                methodName);

                setUpVendorProperties(userId, eventTypes, handler, methodName);
                response.setElementList(eventTypes);
            }
            else
            {
                restExceptionHandler.handleNoRequestBody(userId, methodName, serverName);
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

            EventTypeElement eventType = handler.getEventTypeByGUID(userId,
                                                                    guid,
                                                                    eventTypeGUIDParameterName,
                                                                    false,
                                                                    false,
                                                                    new Date(),
                                                                    methodName);

            setUpVendorProperties(userId, eventType, handler, methodName);

            response.setElement(eventType);
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }


    /**
     * Set up the vendor properties in the retrieved elements.
     *
     * @param userId calling user
     * @param retrievedResults results from the repositories
     * @param handler handler used to retrieve the vendor properties
     * @param methodName calling method
     *
     * @return updated results
     *
     * @throws InvalidParameterException one of the parameters is null or invalid
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    private List<TopicElement> setUpVendorProperties(String                     userId,
                                                     List<TopicElement>         retrievedResults,
                                                     AssetHandler<TopicElement> handler,
                                                     String                     methodName) throws InvalidParameterException,
                                                                                                   UserNotAuthorizedException,
                                                                                                   PropertyServerException
    {
        if (retrievedResults != null)
        {
            for (TopicElement element : retrievedResults)
            {
                if (element != null)
                {
                    setUpVendorProperties(userId, element, handler, methodName);
                }
            }
        }

        return retrievedResults;
    }


    /**
     * Set up the vendor properties in the retrieved element.
     *
     * @param userId calling user
     * @param element results from the repositories
     * @param handler handler used to retrieve the vendor properties
     * @param methodName calling method
     *
     * @return updated results
     *
     * @throws InvalidParameterException one of the parameters is null or invalid
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    private TopicElement setUpVendorProperties(String                     userId,
                                               TopicElement               element,
                                               AssetHandler<TopicElement> handler,
                                               String                     methodName) throws InvalidParameterException,
                                                                                             UserNotAuthorizedException,
                                                                                             PropertyServerException
    {
        final String elementGUIDParameterName = "element.getElementHeader().getGUID()";

        if (element != null)
        {
            TopicProperties properties = element.getProperties();

            properties.setVendorProperties(handler.getVendorProperties(userId,
                                                                       element.getElementHeader().getGUID(),
                                                                       elementGUIDParameterName,
                                                                       false,
                                                                       false,
                                                                       new Date(),
                                                                       methodName));
        }

        return element;
    }


    /**
     * Set up the vendor properties in the retrieved elements.
     *
     * @param userId calling user
     * @param retrievedResults results from the repositories
     * @param handler handler used to retrieve the vendor properties
     * @param methodName calling method
     *
     * @throws InvalidParameterException one of the parameters is null or invalid
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    private void setUpVendorProperties(String                              userId,
                                       List<EventTypeElement>             retrievedResults,
                                       EventTypeHandler<EventTypeElement> handler,
                                       String                             methodName) throws InvalidParameterException,
                                                                                                               UserNotAuthorizedException,
                                                                                                               PropertyServerException
    {
        if (retrievedResults != null)
        {
            for (EventTypeElement element : retrievedResults)
            {
                if (element != null)
                {
                    setUpVendorProperties(userId, element, handler, methodName);
                }
            }
        }
    }


    /**
     * Set up the vendor properties in the retrieved element.
     *
     * @param userId calling user
     * @param element results from the repositories
     * @param handler handler used to retrieve the vendor properties
     * @param methodName calling method
     *
     * @throws InvalidParameterException one of the parameters is null or invalid
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    private void setUpVendorProperties(String                             userId,
                                       EventTypeElement                   element,
                                       EventTypeHandler<EventTypeElement> handler,
                                       String                             methodName) throws InvalidParameterException,
                                                                                                         UserNotAuthorizedException,
                                                                                                         PropertyServerException
    {
        final String elementGUIDParameterName = "element.getElementHeader().getGUID()";

        if (element != null)
        {
            EventTypeProperties properties = element.getProperties();

            properties.setVendorProperties(handler.getVendorProperties(userId,
                                                                       element.getElementHeader().getGUID(),
                                                                       elementGUIDParameterName,
                                                                       false,
                                                                       false,
                                                                       new Date(),
                                                                       methodName));
        }
    }
}
