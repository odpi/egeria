/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project */
package org.odpi.openmetadata.viewservices.collectionmanager.server;

import org.odpi.openmetadata.commonservices.ffdc.RESTCallLogger;
import org.odpi.openmetadata.commonservices.ffdc.RESTCallToken;
import org.odpi.openmetadata.commonservices.ffdc.RESTExceptionHandler;
import org.odpi.openmetadata.commonservices.ffdc.rest.*;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.openmetadata.handlers.CollectionHandler;
import org.odpi.openmetadata.frameworks.openmetadata.properties.actors.AssignmentScopeProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.collections.CollectionMembershipProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.collections.CollectionProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.digitalbusiness.*;
import org.odpi.openmetadata.frameworks.openmetadata.properties.resources.ResourceListProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.solutions.SolutionBlueprintCompositionProperties;
import org.odpi.openmetadata.frameworks.openmetadata.search.MakeAnchorOptions;
import org.odpi.openmetadata.frameworks.openmetadata.search.MetadataSourceOptions;
import org.odpi.openmetadata.frameworks.openmetadata.search.UpdateOptions;
import org.odpi.openmetadata.commonservices.ffdc.rest.GetRequestBody;
import org.odpi.openmetadata.tokencontroller.TokenController;
import org.slf4j.LoggerFactory;


/**
 * The CollectionManagerRESTServices provides the implementation of the Collection Manager Open Metadata View Service (OMVS).
 */

public class CollectionManagerRESTServices extends TokenController
{
    private static final CollectionManagerInstanceHandler instanceHandler = new CollectionManagerInstanceHandler();

    private static final RESTExceptionHandler restExceptionHandler = new RESTExceptionHandler();

    private static final RESTCallLogger restCallLogger = new RESTCallLogger(LoggerFactory.getLogger(CollectionManagerRESTServices.class),
                                                                            instanceHandler.getServiceName());


    /**
     * Default constructor
     */
    public CollectionManagerRESTServices()
    {
    }

    /* =====================================================================================================================
     * CollectionsInterface methods
     */

    /**
     * Returns the list of collections that are linked off of the supplied element.
     *
     * @param serverName     name of called server
     * @param urlMarker  view service URL marker
     * @param parentGUID     unique identifier of referenceable object (typically a personal profile, project or
     *                       community) that the collections hang off of
     * @param requestBody filter response by collection type - if null, any value will do
     *
     * @return a list of collections
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public OpenMetadataRootElementsResponse getAttachedCollections(String             serverName,
                                                                   String             urlMarker,
                                                                   String             parentGUID,
                                                                   ResultsRequestBody requestBody)
    {
        final String methodName = "getAttachedCollections";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        OpenMetadataRootElementsResponse response = new OpenMetadataRootElementsResponse();
        AuditLog                         auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            CollectionHandler handler = instanceHandler.getCollectionHandler(userId, serverName, urlMarker, methodName);

            response.setElements(handler.getAttachedCollections(userId, parentGUID, requestBody));
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /**
     * Returns the list of collections matching the search string - this is coded as a regular expression.
     *
     * @param serverName name of the service to route the request to
     * @param urlMarker  view service URL marker
     * @param requestBody string to find in the properties
     *
     * @return a list of collections
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public OpenMetadataRootElementsResponse findCollections(String                  serverName,
                                                            String                  urlMarker,
                                                            SearchStringRequestBody requestBody)
    {
        final String methodName = "findCollections";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        OpenMetadataRootElementsResponse response = new OpenMetadataRootElementsResponse();
        AuditLog                         auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            CollectionHandler handler = instanceHandler.getCollectionHandler(userId, serverName, urlMarker, methodName);

            if (requestBody != null)
            {
                response.setElements(handler.findCollections(userId,
                                                             requestBody.getSearchString(),
                                                             requestBody));
            }
            else
            {
                response.setElements(handler.findCollections(userId,
                                                             null,
                                                             null));
            }
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /**
     * Returns the list of collections with a particular name.
     *
     * @param serverName    name of called server
     * @param urlMarker  view service URL marker
     * @param requestBody      name of the collections to return - match is full text match in qualifiedName or name

     *
     * @return a list of collections
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public OpenMetadataRootElementsResponse getCollectionsByName(String            serverName,
                                                                 String            urlMarker,
                                                                 FilterRequestBody requestBody)
    {
        final String methodName = "getCollectionsByName";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        OpenMetadataRootElementsResponse response = new OpenMetadataRootElementsResponse();
        AuditLog                         auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            CollectionHandler handler = instanceHandler.getCollectionHandler(userId, serverName, urlMarker, methodName);

            if (requestBody != null)
            {
                response.setElements(handler.getCollectionsByName(userId, requestBody.getFilter(), requestBody));
            }
            else
            {
                restExceptionHandler.handleNoRequestBody(userId, methodName, serverName);
            }
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /**
     * Returns the list of collections with a particular collectionType.  This is an optional text field in the collection element.
     *
     * @param serverName         name of called server
     * @param urlMarker  view service URL marker
     * @param requestBody the collection type value to match on.  If it is null, all collections with a null collectionType are returned
     *
     * @return a list of collections
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public OpenMetadataRootElementsResponse getCollectionsByCategory(String            serverName,
                                                                     String            urlMarker,
                                                                     FilterRequestBody requestBody)
    {
        final String methodName = "getCollectionsByCategory";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        OpenMetadataRootElementsResponse response = new OpenMetadataRootElementsResponse();
        AuditLog                         auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            CollectionHandler handler = instanceHandler.getCollectionHandler(userId, serverName, urlMarker, methodName);

            if (requestBody != null)
            {
                response.setElements(handler.getCollectionsByCategory(userId,
                                                                      requestBody.getFilter(),
                                                                      requestBody));
            }
            else
            {
                response.setElements(handler.getCollectionsByCategory(userId,
                                                                      null,
                                                                      null));
            }
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /**
     * Return the properties of a specific collection.
     *
     * @param serverName         name of called server
     * @param urlMarker  view service URL marker
     * @param collectionGUID unique identifier of the required collection
     * @param requestBody time values for the query
     *
     * @return collection properties
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public OpenMetadataRootElementResponse getCollectionByGUID(String             serverName,
                                                               String             urlMarker,
                                                               String             collectionGUID,
                                                               GetRequestBody requestBody)
    {
        final String methodName = "getCollectionByGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        OpenMetadataRootElementResponse response = new OpenMetadataRootElementResponse();
        AuditLog                        auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            CollectionHandler handler = instanceHandler.getCollectionHandler(userId, serverName, urlMarker, methodName);

            response.setElement(handler.getCollectionByGUID(userId, collectionGUID, requestBody));
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /**
     * Create a new generic collection.
     *
     * @param serverName                 name of called server.
     * @param urlMarker  view service URL marker
     * @param requestBody             properties for the collection.
     *
     * @return unique identifier of the newly created Collection
     *  InvalidParameterException  one of the parameters is invalid.
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public GUIDResponse createCollection(String                serverName,
                                         String                urlMarker,
                                         NewElementRequestBody requestBody)
    {
        final String methodName = "createCollection";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        GUIDResponse response = new GUIDResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
                CollectionHandler handler = instanceHandler.getCollectionHandler(userId, serverName, urlMarker, methodName);

                if (requestBody.getProperties() instanceof CollectionProperties collectionProperties)
                {
                    response.setGUID(handler.createCollection(userId,
                                                              requestBody,
                                                              requestBody.getInitialClassifications(),
                                                              collectionProperties,
                                                              requestBody.getParentRelationshipProperties()));
                }
                else
                {
                    restExceptionHandler.handleInvalidPropertiesObject(CollectionProperties.class.getName(), methodName);
                }
            }
            else
            {
                restExceptionHandler.handleNoRequestBody(userId, methodName, serverName);
            }
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /**
     * Create a new metadata element to represent a collection using an existing metadata element as a template.
     * The template defines additional classifications and relationships that should be added to the new collection.
     *
     * @param serverName             calling user
     * @param urlMarker  view service URL marker
     * @param requestBody properties that override the template
     *
     * @return unique identifier of the new metadata element
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public GUIDResponse createCollectionFromTemplate(String              serverName,
                                                     String              urlMarker,
                                                     TemplateRequestBody requestBody)
    {
        final String methodName = "createCollectionFromTemplate";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        GUIDResponse response = new GUIDResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
                CollectionHandler handler = instanceHandler.getCollectionHandler(userId, serverName, urlMarker, methodName);

                response.setGUID(handler.createCollectionFromTemplate(userId,
                                                                      requestBody,
                                                                      requestBody.getTemplateGUID(),
                                                                      requestBody.getReplacementProperties(),
                                                                      requestBody.getPlaceholderPropertyValues(),
                                                                      requestBody.getParentRelationshipProperties()));
            }
            else
            {
                restExceptionHandler.handleNoRequestBody(userId, methodName, serverName);
            }
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /**
     * Update the properties of a collection.
     *
     * @param serverName         name of called server.
     * @param urlMarker  view service URL marker
     * @param collectionGUID unique identifier of the collection (returned from create)
     * @param requestBody     properties for the collection.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is invalid.
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public VoidResponse   updateCollection(String                   serverName,
                                           String                   urlMarker,
                                           String                   collectionGUID,
                                           UpdateElementRequestBody requestBody)
    {
        final String methodName = "updateCollection";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
                CollectionHandler handler = instanceHandler.getCollectionHandler(userId, serverName, urlMarker, methodName);

                if (requestBody.getProperties() instanceof CollectionProperties properties)
                {
                    handler.updateCollection(userId, collectionGUID, requestBody, properties);
                }
                else
                {
                    restExceptionHandler.handleInvalidPropertiesObject(CollectionProperties.class.getName(), methodName);
                }
            }
            else
            {
                restExceptionHandler.handleNoRequestBody(userId, methodName, serverName);
            }
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }



    /**
     * Connect an existing collection to an element using the ResourceList relationship (0019).
     *
     * @param serverName         name of called server
     * @param urlMarker  view service URL marker
     * @param collectionGUID unique identifier of the collection
     * @param parentGUID     unique identifier of referenceable object that the collection should be attached to
     * @param requestBody  description of how the collection will be used.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public VoidResponse attachCollection(String                  serverName,
                                         String                  urlMarker,
                                         String                  collectionGUID,
                                         String                  parentGUID,
                                         NewRelationshipRequestBody requestBody)
    {
        final String methodName = "attachCollection";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            CollectionHandler handler = instanceHandler.getCollectionHandler(userId, serverName, urlMarker, methodName);

            if (requestBody != null)
            {
                if (requestBody.getProperties() instanceof ResourceListProperties properties)
                {
                    handler.attachCollection(userId,
                                             collectionGUID,
                                             parentGUID,
                                             requestBody,
                                             properties);
                }
                else if (requestBody.getProperties() == null)
                {
                    handler.attachCollection(userId,
                                             collectionGUID,
                                             parentGUID,
                                             requestBody,
                                             null);
                }
                else
                {
                    restExceptionHandler.handleInvalidPropertiesObject(ResourceListProperties.class.getName(), methodName);
                }
            }
            else
            {
                handler.attachCollection(userId,
                                         collectionGUID,
                                         parentGUID,
                                         new MakeAnchorOptions(),
                                         null);
            }
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /**
     * Detach an existing collection from an element.  If the collection is anchored to the element, it is deleted.
     *
     * @param serverName         name of called server.
     * @param urlMarker  view service URL marker
     * @param collectionGUID unique identifier of the collection.
     * @param parentGUID     unique identifier of referenceable object that the collection should be attached to
     * @param requestBody null request body
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public VoidResponse detachCollection(String                   serverName,
                                         String                   urlMarker,
                                         String                   collectionGUID,
                                         String                   parentGUID,
                                         DeleteRequestBody requestBody)
    {
        final String methodName = "detachCollection";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            CollectionHandler handler = instanceHandler.getCollectionHandler(userId, serverName, urlMarker, methodName);

            handler.detachCollection(userId, collectionGUID, parentGUID, requestBody);
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }



    /**
     * Link two dependent products.
     *
     * @param serverName         name of called server
     * @param urlMarker  view service URL marker
     * @param consumerDigitalProductGUID    unique identifier of the digital product that has the dependency.
     * @param consumedDigitalProductGUID    unique identifier of the digital product that it is using.
     * @param requestBody  description of the relationship.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public VoidResponse linkDigitalProductDependency(String                  serverName,
                                                     String                  urlMarker,
                                                     String                  consumerDigitalProductGUID,
                                                     String                  consumedDigitalProductGUID,
                                                     NewRelationshipRequestBody requestBody)
    {
        final String methodName = "linkDigitalProductDependency";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            CollectionHandler handler = instanceHandler.getCollectionHandler(userId, serverName, urlMarker, methodName);

            if (requestBody != null)
            {
                if (requestBody.getProperties() instanceof DigitalProductDependencyProperties properties)
                {
                    handler.linkDigitalProductDependency(userId,
                                                         consumerDigitalProductGUID,
                                                         consumedDigitalProductGUID,
                                                         requestBody,
                                                         properties);
                }
                else if (requestBody.getProperties() == null)
                {
                    handler.linkDigitalProductDependency(userId,
                                                         consumerDigitalProductGUID,
                                                         consumedDigitalProductGUID,
                                                         requestBody,
                                                         null);
                }
                else
                {
                    restExceptionHandler.handleInvalidPropertiesObject(SolutionBlueprintCompositionProperties.class.getName(), methodName);
                }
            }
            else
            {
                handler.linkDigitalProductDependency(userId,
                                                     consumerDigitalProductGUID,
                                                     consumedDigitalProductGUID,
                                                     new MetadataSourceOptions(),
                                                     null);
            }
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /**
     * Unlink dependent products.
     *
     * @param serverName         name of called server
     * @param urlMarker  view service URL marker
     * @param consumerDigitalProductGUID    unique identifier of the digital product that has the dependency.
     * @param consumedDigitalProductGUID    unique identifier of the digital product that it is using.
     * @param requestBody  description of the relationship.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public VoidResponse detachDigitalProductDependency(String                   serverName,
                                                       String                   urlMarker,
                                                       String                   consumerDigitalProductGUID,
                                                       String                   consumedDigitalProductGUID,
                                                       DeleteRequestBody requestBody)
    {
        final String methodName = "detachDigitalProductDependency";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            CollectionHandler handler = instanceHandler.getCollectionHandler(userId, serverName, urlMarker, methodName);

            handler.detachDigitalProductDependency(userId,
                                                   consumerDigitalProductGUID,
                                                   consumedDigitalProductGUID,
                                                   requestBody);
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /**
     * Attach a subscriber to a subscription.
     *
     * @param serverName         name of called server
     * @param urlMarker  view service URL marker
     * @param digitalSubscriberGUID  unique identifier of the subscriber (referenceable)
     * @param digitalSubscriptionGUID unique identifier of the  digital subscription agreement
     * @param requestBody  description of the relationship.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public VoidResponse linkSubscriber(String                  serverName,
                                       String                  urlMarker,
                                       String                  digitalSubscriberGUID,
                                       String                  digitalSubscriptionGUID,
                                       NewRelationshipRequestBody requestBody)
    {
        final String methodName = "linkSubscriber";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            CollectionHandler handler = instanceHandler.getCollectionHandler(userId, serverName, urlMarker, methodName);

            if (requestBody != null)
            {
                if (requestBody.getProperties() instanceof DigitalSubscriberProperties properties)
                {
                    handler.linkSubscriber(userId,
                                           digitalSubscriberGUID,
                                           digitalSubscriptionGUID,
                                           requestBody,
                                           properties);
                }
                else if (requestBody.getProperties() == null)
                {
                    handler.linkSubscriber(userId,
                                           digitalSubscriberGUID,
                                           digitalSubscriptionGUID,
                                           requestBody,
                                           null);
                }
                else
                {
                    restExceptionHandler.handleInvalidPropertiesObject(SolutionBlueprintCompositionProperties.class.getName(), methodName);
                }
            }
            else
            {
                handler.linkSubscriber(userId,
                                       digitalSubscriberGUID,
                                       digitalSubscriptionGUID,
                                       new MetadataSourceOptions(),
                                       null);
            }
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /**
     * Detach a subscriber from a subscription.
     *
     * @param serverName         name of called server
     * @param urlMarker  view service URL marker
     * @param digitalSubscriberGUID  unique identifier of the subscriber (referenceable)
     * @param digitalSubscriptionGUID unique identifier of the  digital subscription agreement
     * @param requestBody  description of the relationship.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public VoidResponse detachSubscriber(String                   serverName,
                                         String                   urlMarker,
                                         String                   digitalSubscriberGUID,
                                         String                   digitalSubscriptionGUID,
                                         DeleteRequestBody requestBody)
    {
        final String methodName = "detachSubscriber";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            CollectionHandler handler = instanceHandler.getCollectionHandler(userId, serverName, urlMarker, methodName);

            handler.detachSubscriber(userId,
                                     digitalSubscriberGUID,
                                     digitalSubscriptionGUID,
                                     requestBody);
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /**
     * Attach a product manager to a digital product.
     *
     * @param serverName         name of called server
     * @param urlMarker  view service URL marker
     * @param digitalProductGUID  unique identifier of the digital product
     * @param digitalProductManagerRoleGUID      unique identifier of the product manager role
     * @param requestBody  description of the relationship.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public VoidResponse linkProductManager(String                     serverName,
                                           String                     urlMarker,
                                           String                     digitalProductGUID,
                                           String                     digitalProductManagerRoleGUID,
                                           NewRelationshipRequestBody requestBody)
    {
        final String methodName = "linkProductManager";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            CollectionHandler handler = instanceHandler.getCollectionHandler(userId, serverName, urlMarker, methodName);

            if (requestBody != null)
            {
                if (requestBody.getProperties() instanceof AssignmentScopeProperties properties)
                {
                    handler.linkProductManager(userId,
                                               digitalProductGUID,
                                               digitalProductManagerRoleGUID,
                                               requestBody,
                                               properties);
                }
                else if (requestBody.getProperties() == null)
                {
                    handler.linkProductManager(userId,
                                               digitalProductGUID,
                                               digitalProductManagerRoleGUID,
                                               requestBody,
                                               null);
                }
                else
                {
                    restExceptionHandler.handleInvalidPropertiesObject(SolutionBlueprintCompositionProperties.class.getName(), methodName);
                }
            }
            else
            {
                handler.linkProductManager(userId,
                                           digitalProductGUID,
                                           digitalProductManagerRoleGUID,
                                           requestBody,
                                           null);
            }
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /**
     * Detach a product manager from a digital product.
     *
     * @param serverName         name of called server
     * @param urlMarker  view service URL marker
     * @param digitalProductGUID  unique identifier of the digital product
     * @param digitalProductManagerRoleGUID      unique identifier of the product manager role
     * @param requestBody  description of the relationship.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public VoidResponse detachProductManager(String            serverName,
                                             String            urlMarker,
                                             String            digitalProductGUID,
                                             String            digitalProductManagerRoleGUID,
                                             DeleteRequestBody requestBody)
    {
        final String methodName = "detachProductManager";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            CollectionHandler handler = instanceHandler.getCollectionHandler(userId, serverName, urlMarker, methodName);

            handler.detachProductManager(userId,
                                         digitalProductGUID,
                                         digitalProductManagerRoleGUID,
                                         requestBody);
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }



    /**
     * Attach an actor to an agreement.
     *
     * @param serverName         name of called server
     * @param urlMarker  view service URL marker
     * @param agreementGUID  unique identifier of the agreement
     * @param actorGUID      unique identifier of the actor
     * @param requestBody  description of the relationship.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public GUIDResponse linkAgreementActor(String                  serverName,
                                           String                  urlMarker,
                                           String                  agreementGUID,
                                           String                  actorGUID,
                                           NewRelationshipRequestBody requestBody)
    {
        final String methodName = "linkAgreementActor";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        GUIDResponse response = new GUIDResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            CollectionHandler handler = instanceHandler.getCollectionHandler(userId, serverName, urlMarker, methodName);

            if (requestBody != null)
            {
                if (requestBody.getProperties() instanceof AgreementActorProperties properties)
                {
                    response.setGUID(handler.linkAgreementActor(userId,
                                                                agreementGUID,
                                                                actorGUID,
                                                                requestBody,
                                                                properties));
                }
                else if (requestBody.getProperties() == null)
                {
                    response.setGUID(handler.linkAgreementActor(userId,
                                                                agreementGUID,
                                                                actorGUID,
                                                                requestBody,
                                                                null));
                }
                else
                {
                    restExceptionHandler.handleInvalidPropertiesObject(SolutionBlueprintCompositionProperties.class.getName(), methodName);
                }
            }
            else
            {
                response.setGUID(handler.linkAgreementActor(userId,
                                                            agreementGUID,
                                                            actorGUID,
                                                            new MetadataSourceOptions(),
                                                            null));
            }
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /**
     * Detach an actor from an agreement.
     *
     * @param serverName         name of called server
     * @param urlMarker  view service URL marker
     * @param agreementActorRelationshipGUID  unique identifier of the element being described
     * @param requestBody  description of the relationship.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public VoidResponse detachAgreementActor(String                   serverName,
                                             String                   urlMarker,
                                             String                   agreementActorRelationshipGUID,
                                             DeleteRequestBody requestBody)
    {
        final String methodName = "detachAgreementActor";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            CollectionHandler handler = instanceHandler.getCollectionHandler(userId, serverName, urlMarker, methodName);

            handler.detachAgreementActor(userId, agreementActorRelationshipGUID, requestBody);
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /**
     * Attach an agreement to an element involved in its definition.
     *
     * @param serverName         name of called server
     * @param urlMarker  view service URL marker
     * @param agreementGUID  unique identifier of the agreement
     * @param agreementItemGUID      unique identifier of the agreement item
     * @param requestBody  description of the relationship.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public VoidResponse linkAgreementItem(String                  serverName,
                                          String                  urlMarker,
                                          String                  agreementGUID,
                                          String                  agreementItemGUID,
                                          NewRelationshipRequestBody requestBody)
    {
        final String methodName = "linkAgreementItem";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            CollectionHandler handler = instanceHandler.getCollectionHandler(userId, serverName, urlMarker, methodName);

            if (requestBody != null)
            {
                if (requestBody.getProperties() instanceof AgreementItemProperties properties)
                {
                    handler.linkAgreementItem(userId,
                                              agreementGUID,
                                              agreementItemGUID,
                                              requestBody,
                                              properties);
                }
                else if (requestBody.getProperties() == null)
                {
                    handler.linkAgreementItem(userId,
                                              agreementGUID,
                                              agreementItemGUID,
                                              requestBody,
                                              null);
                }
                else
                {
                    restExceptionHandler.handleInvalidPropertiesObject(SolutionBlueprintCompositionProperties.class.getName(), methodName);
                }
            }
            else
            {
                handler.linkAgreementItem(userId,
                                          agreementGUID,
                                          agreementItemGUID,
                                          new MetadataSourceOptions(),
                                          null);
            }
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /**
     * Detach an agreement from an element involved in its definition.
     *
     * @param serverName         name of called server
     * @param urlMarker  view service URL marker
     * @param agreementGUID  unique identifier of the agreement
     * @param agreementItemGUID      unique identifier of the agreement item
     * @param requestBody  description of the relationship.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public VoidResponse detachAgreementItem(String                   serverName,
                                            String                   urlMarker,
                                            String                   agreementGUID,
                                            String                   agreementItemGUID,
                                            DeleteRequestBody requestBody)
    {
        final String methodName = "detachAgreementItem";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            CollectionHandler handler = instanceHandler.getCollectionHandler(userId, serverName, urlMarker, methodName);

            handler.detachAgreementItem(userId, agreementGUID, agreementItemGUID, requestBody);
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /**
     * Attach an agreement to an external reference element that describes the location of the contract documents.
     *
     * @param serverName         name of called server
     * @param urlMarker  view service URL marker
     * @param agreementGUID  unique identifier of the agreement
     * @param externalReferenceGUID      unique identifier of the external reference describing the location of the contract
     * @param requestBody  description of the relationship.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public VoidResponse linkContract(String                  serverName,
                                     String                  urlMarker,
                                     String                  agreementGUID,
                                     String                  externalReferenceGUID,
                                     NewRelationshipRequestBody requestBody)
    {
        final String methodName = "linkContract";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            CollectionHandler handler = instanceHandler.getCollectionHandler(userId, serverName, urlMarker, methodName);

            if (requestBody != null)
            {
                if (requestBody.getProperties() instanceof ContractLinkProperties properties)
                {
                    handler.linkContract(userId,
                                         agreementGUID,
                                         externalReferenceGUID,
                                         requestBody,
                                         properties);
                }
                else if (requestBody.getProperties() == null)
                {
                    handler.linkContract(userId,
                                         agreementGUID,
                                         externalReferenceGUID,
                                         requestBody,
                                         null);
                }
                else
                {
                    restExceptionHandler.handleInvalidPropertiesObject(SolutionBlueprintCompositionProperties.class.getName(), methodName);
                }
            }
            else
            {
                handler.linkContract(userId,
                                     agreementGUID,
                                     externalReferenceGUID,
                                     new MetadataSourceOptions(),
                                     null);
            }
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /**
     * Detach an agreement from an external reference describing the location of the contract documents.
     *
     * @param serverName         name of called server
     * @param urlMarker  view service URL marker
     * @param agreementGUID  unique identifier of the agreement
     * @param externalReferenceGUID      unique identifier of the external reference describing the location of the contract
     * @param requestBody  description of the relationship.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public VoidResponse detachContract(String                   serverName,
                                       String                   urlMarker,
                                       String                   agreementGUID,
                                       String                   externalReferenceGUID,
                                       DeleteRequestBody requestBody)
    {
        final String methodName = "detachContract";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            CollectionHandler handler = instanceHandler.getCollectionHandler(userId, serverName, urlMarker, methodName);

            handler.detachContract(userId, agreementGUID, externalReferenceGUID, requestBody);
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /**
     * Delete a collection.  It is detached from all parent elements.  If members are anchored to the collection
     * then they are also deleted.
     *
     * @param serverName         name of called server.
     * @param urlMarker  view service URL marker
     * @param collectionGUID unique identifier of the collection
     * @param requestBody delete request body
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @SuppressWarnings(value = "unused")
    public VoidResponse deleteCollection(String                    serverName,
                                         String                    urlMarker,
                                         String                    collectionGUID,
                                         DeleteRequestBody requestBody)
    {
        final String methodName = "deleteCollection";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            CollectionHandler handler = instanceHandler.getCollectionHandler(userId, serverName, urlMarker, methodName);

            handler.deleteCollection(userId, collectionGUID, requestBody);
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /**
     * Return a list of elements that are a member of a collection.
     *
     * @param serverName         name of called server.
     * @param urlMarker  view service URL marker
     * @param collectionGUID unique identifier of the collection.
     *
     * @return list of asset details
     *  InvalidParameterException  one of the parameters is invalid.
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public OpenMetadataRootElementsResponse getCollectionMembers(String             serverName,
                                                                 String             urlMarker,
                                                                 String             collectionGUID,
                                                                 ResultsRequestBody requestBody)
    {
        final String methodName = "getCollectionMembers";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        OpenMetadataRootElementsResponse response = new OpenMetadataRootElementsResponse();
        AuditLog                         auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            CollectionHandler handler = instanceHandler.getCollectionHandler(userId, serverName, urlMarker, methodName);

            response.setElements(handler.getCollectionMembers(userId, collectionGUID, requestBody));
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /**
     * Return a graph of elements that are the nested members of a collection along
     * with elements immediately connected to the starting collection.  The result
     * includes a mermaid graph of the returned elements.
     *
     * @param serverName         name of called server.
     * @param urlMarker  view service URL marker
     * @param collectionGUID unique identifier of the collection.
     *
     * @return list of collection details
     *  InvalidParameterException  one of the parameters is invalid.
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public OpenMetadataRootElementResponse getCollectionHierarchy(String             serverName,
                                                                  String             urlMarker,
                                                                  String             collectionGUID,
                                                                  ResultsRequestBody requestBody)
    {
        final String methodName = "getCollectionHierarchy";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        OpenMetadataRootElementResponse response = new OpenMetadataRootElementResponse();
        AuditLog                        auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            CollectionHandler handler = instanceHandler.getCollectionHandler(userId, serverName, urlMarker, methodName);

            response.setElement(handler.getCollectionHierarchy(userId, collectionGUID, requestBody));
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /**
     * Add an element to a collection.
     *
     * @param serverName               name of called server.
     * @param urlMarker  view service URL marker
     * @param collectionGUID       unique identifier of the collection.
     * @param requestBody properties describing the membership characteristics.
     * @param elementGUID          unique identifier of the element.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is invalid.
     *  PropertyServerException    there is a problem updating information in the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public VoidResponse addToCollection(String                  serverName,
                                        String                  urlMarker,
                                        String                  collectionGUID,
                                        String                  elementGUID,
                                        NewRelationshipRequestBody requestBody)
    {
        final String methodName = "addToCollection";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            CollectionHandler handler = instanceHandler.getCollectionHandler(userId, serverName, urlMarker, methodName);

            if (requestBody != null)
            {
                if (requestBody.getProperties() instanceof CollectionMembershipProperties properties)
                {
                    handler.addToCollection(userId,
                                            collectionGUID,
                                            elementGUID,
                                            requestBody,
                                            properties);
                }
                else if (requestBody.getProperties() == null)
                {
                    handler.addToCollection(userId,
                                            collectionGUID,
                                            elementGUID,
                                            requestBody,
                                            null);
                }
                else
                {
                    restExceptionHandler.handleInvalidPropertiesObject(CollectionMembershipProperties.class.getName(), methodName);
                }
            }
            else
            {
                handler.addToCollection(userId,
                                        collectionGUID,
                                        elementGUID,
                                        new MetadataSourceOptions(),
                                        null);
            }
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /**
     * Update an element's membership to a collection.
     *
     * @param serverName               name of called server.
     * @param urlMarker  view service URL marker
     * @param collectionGUID       unique identifier of the collection.
     * @param elementGUID          unique identifier of the element.
     * @param requestBody properties describing the membership characteristics.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is invalid.
     *  PropertyServerException    there is a problem updating information in the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public VoidResponse updateCollectionMembership(String                        serverName,
                                                   String                        urlMarker,
                                                   String                        collectionGUID,
                                                   String                        elementGUID,
                                                   UpdateRelationshipRequestBody requestBody)
    {
        final String methodName = "updateCollectionMembership";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            CollectionHandler handler = instanceHandler.getCollectionHandler(userId, serverName, urlMarker, methodName);

            if (requestBody != null)
            {
                if (requestBody.getProperties() instanceof CollectionMembershipProperties properties)
                {
                    handler.updateCollectionMembership(userId,
                                                       collectionGUID,
                                                       elementGUID,
                                                       requestBody,
                                                       properties);
                }
                else if (requestBody.getProperties() == null)
                {
                    handler.updateCollectionMembership(userId,
                                                       collectionGUID,
                                                       elementGUID,
                                                       requestBody,
                                                       null);
                }
                else
                {
                    restExceptionHandler.handleInvalidPropertiesObject(CollectionMembershipProperties.class.getName(), methodName);
                }
            }
            else
            {
                handler.updateCollectionMembership(userId,
                                                   collectionGUID,
                                                   elementGUID,
                                                   new UpdateOptions(),
                                                   null);
            }
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }



    /**
     * Remove an element from a collection.
     *
     * @param serverName         name of called server.
     * @param urlMarker  view service URL marker
     * @param collectionGUID unique identifier of the collection.
     * @param elementGUID    unique identifier of the element.
     * @param requestBody  null request body
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is invalid.
     *  PropertyServerException    there is a problem updating information in the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @SuppressWarnings(value = "unused")
    public VoidResponse removeFromCollection(String                   serverName,
                                             String                   urlMarker,
                                             String                   collectionGUID,
                                             String                   elementGUID,
                                             DeleteRequestBody requestBody)
    {
        final String methodName = "removeFromCollection";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            CollectionHandler handler = instanceHandler.getCollectionHandler(userId, serverName, urlMarker, methodName);

            handler.removeFromCollection(userId,
                                         collectionGUID,
                                         elementGUID,
                                         requestBody);
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }
}
