/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project */
/* Copyright Contributors to the ODPi Egeria category. */
package org.odpi.openmetadata.viewservices.collectionmanager.server;

import org.odpi.openmetadata.accessservices.digitalservice.client.CollectionsClient;
import org.odpi.openmetadata.commonservices.ffdc.RESTCallLogger;
import org.odpi.openmetadata.commonservices.ffdc.RESTCallToken;
import org.odpi.openmetadata.commonservices.ffdc.RESTExceptionHandler;
import org.odpi.openmetadata.commonservices.ffdc.rest.*;
import org.odpi.openmetadata.commonservices.ffdc.rest.CollectionResponse;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.openmetadata.properties.collections.CollectionMembershipProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.collections.CollectionProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.digitalbusiness.DigitalProductProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.resources.ResourceListProperties;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;
import org.odpi.openmetadata.tokencontroller.TokenController;
import org.odpi.openmetadata.viewservices.collectionmanager.rest.*;
import org.odpi.openmetadata.viewservices.collectionmanager.rest.TemplateRequestBody;
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
     * @param parentGUID     unique identifier of referenceable object (typically a personal profile, project or
     *                       community) that the collections hang off of
     * @param startFrom      index of the list to start from (0 for start)
     * @param pageSize       maximum number of elements to return
     * @param requestBody filter response by collection type - if null, any value will do
     *
     * @return a list of collections
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public CollectionsResponse getAttachedCollections(String            serverName,
                                                      String            parentGUID,
                                                      int               startFrom,
                                                      int               pageSize,
                                                      FilterRequestBody requestBody)
    {
        final String methodName = "getAttachedCollections";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        CollectionsResponse response = new CollectionsResponse();
        AuditLog            auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            CollectionsClient handler = instanceHandler.getCollectionsClient(userId, serverName, methodName);

            if (requestBody != null)
            {
                response.setElements(handler.getAttachedCollections(userId, parentGUID, requestBody.getFilter(), startFrom, pageSize));
            }
            else
            {
                response.setElements(handler.getAttachedCollections(userId, parentGUID, null, startFrom, pageSize));
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
     * Returns the list of collections with a particular classification.
     *
     * @param serverName         name of called server
     * @param requestBody        name of the classification - if null, all collections are returned
     * @param startFrom          index of the list to start from (0 for start)
     * @param pageSize           maximum number of elements to return
     *
     * @return a list of collections
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public CollectionsResponse getClassifiedCollections(String            serverName,
                                                        int               startFrom,
                                                        int               pageSize,
                                                        FilterRequestBody requestBody)
    {
        final String methodName = "getClassifiedCollections";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        CollectionsResponse response = new CollectionsResponse();
        AuditLog            auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            CollectionsClient handler = instanceHandler.getCollectionsClient(userId, serverName, methodName);

            if (requestBody != null)
            {
                response.setElements(handler.getClassifiedCollections(userId, requestBody.getFilter(), startFrom, pageSize));
            }
            else
            {
                response.setElements(handler.getClassifiedCollections(userId, null, startFrom, pageSize));
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
     * Returns the list of collections matching the search string - this is coded as a regular expression.
     *
     * @param serverName name of the service to route the request to
     * @param startsWith does the value start with the supplied string?
     * @param endsWith does the value end with the supplied string?
     * @param ignoreCase should the search ignore case?
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param requestBody string to find in the properties
     *
     * @return a list of collections
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public CollectionsResponse findCollections(String            serverName,
                                               boolean           startsWith,
                                               boolean           endsWith,
                                               boolean           ignoreCase,
                                               int               startFrom,
                                               int               pageSize,
                                               FilterRequestBody requestBody)
    {
        final String methodName = "findCollections";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        CollectionsResponse response = new CollectionsResponse();
        AuditLog            auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            CollectionsClient handler = instanceHandler.getCollectionsClient(userId, serverName, methodName);

            if (requestBody != null)
            {
                response.setElements(handler.findCollections(userId,
                                                             instanceHandler.getSearchString(requestBody.getFilter(), startsWith, endsWith, ignoreCase),
                                                             startFrom,
                                                             pageSize,
                                                             requestBody.getEffectiveTime()));
            }
            else
            {
                response.setElements(handler.findCollections(userId,
                                                             instanceHandler.getSearchString(null, startsWith, endsWith, ignoreCase),
                                                             startFrom,
                                                             pageSize,
                                                             null));
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
     * Returns the list of collections with a particular name.
     *
     * @param serverName    name of called server
     * @param requestBody      name of the collections to return - match is full text match in qualifiedName or name
     * @param startFrom index of the list to start from (0 for start)
     * @param pageSize  maximum number of elements to return
     *
     * @return a list of collections
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public CollectionsResponse getCollectionsByName(String            serverName,
                                                    int               startFrom,
                                                    int               pageSize,
                                                    FilterRequestBody requestBody)
    {
        final String methodName = "getCollectionsByName";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        CollectionsResponse response = new CollectionsResponse();
        AuditLog               auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            CollectionsClient handler = instanceHandler.getCollectionsClient(userId, serverName, methodName);

            response.setElements(handler.getCollectionsByName(userId,
                                                              requestBody.getFilter(),
                                                              startFrom,
                                                              pageSize,
                                                              requestBody.getEffectiveTime()));
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /**
     * Returns the list of collections with a particular collectionType.  This is an optional text field in the collection element.
     *
     * @param serverName         name of called server
     * @param requestBody the collection type value to match on.  If it is null, all collections with a null collectionType are returned
     * @param startFrom      index of the list to start from (0 for start)
     * @param pageSize       maximum number of elements to return
     *
     * @return a list of collections
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public CollectionsResponse getCollectionsByType(String            serverName,
                                                    int               startFrom,
                                                    int               pageSize,
                                                    FilterRequestBody requestBody)
    {
        final String methodName = "getCollectionsByType";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        CollectionsResponse response = new CollectionsResponse();
        AuditLog            auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            CollectionsClient handler = instanceHandler.getCollectionsClient(userId, serverName, methodName);

            if (requestBody != null)
            {
                response.setElements(handler.getCollectionsByType(userId,
                                                                  requestBody.getFilter(),
                                                                  startFrom,
                                                                  pageSize));
            }
            else
            {
                response.setElements(handler.getCollectionsByType(userId,
                                                                  null,
                                                                  startFrom,
                                                                  pageSize));
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
     * Return the properties of a specific collection.
     *
     * @param serverName         name of called server
     * @param collectionGUID unique identifier of the required collection
     *
     * @return collection properties
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public CollectionResponse getCollection(String serverName,
                                            String collectionGUID)
    {
        final String methodName = "getCollection";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        CollectionResponse response = new CollectionResponse();
        AuditLog           auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            CollectionsClient handler = instanceHandler.getCollectionsClient(userId, serverName, methodName);

            response.setElement(handler.getCollection(userId, collectionGUID));
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /**
     * Create a new generic collection.
     *
     * @param serverName                 name of called server.
     * @param optionalClassificationName name of collection classification
     * @param requestBody             properties for the collection.
     *
     * @return unique identifier of the newly created Collection
     *  InvalidParameterException  one of the parameters is invalid.
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public GUIDResponse createCollection(String                   serverName,
                                         String                   optionalClassificationName,
                                         NewCollectionRequestBody requestBody)
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
                CollectionsClient handler = instanceHandler.getCollectionsClient(userId, serverName, methodName);

                response.setGUID(handler.createCollection(userId,
                                                          requestBody.getAnchorGUID(),
                                                          requestBody.getIsOwnAnchor(),
                                                          optionalClassificationName,
                                                          requestBody.getCollectionProperties(),
                                                          requestBody.getParentGUID(),
                                                          requestBody.getParentRelationshipTypeName(),
                                                          requestBody.getParentRelationshipProperties(),
                                                          requestBody.getParentAtEnd1()));
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
     * Create a new collection with the RootCollection classification.  Used to identify the top of a
     * collection hierarchy.
     *
     * @param serverName                 name of called server.
     * @param requestBody             properties for the collection.
     *
     * @return unique identifier of the newly created Collection
     *  InvalidParameterException  one of the parameters is invalid.
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public GUIDResponse createRootCollection(String                   serverName,
                                             NewCollectionRequestBody requestBody)
    {
        final String methodName = "createRootCollection";

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
                CollectionsClient handler = instanceHandler.getCollectionsClient(userId, serverName, methodName);

                response.setGUID(handler.createCollection(userId,
                                                          requestBody.getAnchorGUID(),
                                                          requestBody.getIsOwnAnchor(),
                                                          OpenMetadataType.ROOT_COLLECTION.typeName,
                                                          requestBody.getCollectionProperties(),
                                                          requestBody.getParentGUID(),
                                                          requestBody.getParentRelationshipTypeName(),
                                                          requestBody.getParentRelationshipProperties(),
                                                          requestBody.getParentAtEnd1()));
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
     * Create a new collection with the DataSpecCollection classification.  Used to identify the top of a
     * collection hierarchy.
     *
     * @param serverName                 name of called server.
     * @param requestBody             properties for the collection.
     *
     * @return unique identifier of the newly created Collection
     *  InvalidParameterException  one of the parameters is invalid.
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public GUIDResponse createDataSpecCollection(String                   serverName,
                                                 NewCollectionRequestBody requestBody)
    {
        final String methodName = "createDataSpecCollection";

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
                CollectionsClient handler = instanceHandler.getCollectionsClient(userId, serverName, methodName);

                response.setGUID(handler.createCollection(userId,
                                                          requestBody.getAnchorGUID(),
                                                          requestBody.getIsOwnAnchor(),
                                                          OpenMetadataType.DATA_SPEC_COLLECTION.typeName,
                                                          requestBody.getCollectionProperties(),
                                                          requestBody.getParentGUID(),
                                                          requestBody.getParentRelationshipTypeName(),
                                                          requestBody.getParentRelationshipProperties(),
                                                          requestBody.getParentAtEnd1()));
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
     * Create a new collection with the Folder classification.  This is used to identify the organizing collections
     * in a collection hierarchy.
     *
     * @param serverName                 name of called server.
     * @param requestBody             properties for the collection.
     *
     * @return unique identifier of the newly created Collection
     *  InvalidParameterException  one of the parameters is invalid.
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public GUIDResponse createFolderCollection(String                   serverName,
                                               NewCollectionRequestBody requestBody)
    {
        final String methodName = "createFolderCollection";

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
                CollectionsClient handler = instanceHandler.getCollectionsClient(userId, serverName, methodName);

                response.setGUID(handler.createCollection(userId,
                                                          requestBody.getAnchorGUID(),
                                                          requestBody.getIsOwnAnchor(),
                                                          OpenMetadataType.FOLDER.typeName,
                                                          requestBody.getCollectionProperties(),
                                                          requestBody.getParentGUID(),
                                                          requestBody.getParentRelationshipTypeName(),
                                                          requestBody.getParentRelationshipProperties(),
                                                          requestBody.getParentAtEnd1()));
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
     * Create a new metadata element to represent a collection using an existing metadata element as a template.
     * The template defines additional classifications and relationships that should be added to the new collection.
     *
     * @param serverName             calling user
     * @param requestBody properties that override the template
     *
     * @return unique identifier of the new metadata element
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public GUIDResponse createCollectionFromTemplate(String              serverName,
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
                CollectionsClient handler = instanceHandler.getCollectionsClient(userId, serverName, methodName);

                response.setGUID(handler.createCollectionFromTemplate(userId,
                                                                      requestBody.getAnchorGUID(),
                                                                      requestBody.getIsOwnAnchor(),
                                                                      null,
                                                                      null,
                                                                      requestBody.getTemplateGUID(),
                                                                      requestBody.getReplacementProperties(),
                                                                      requestBody.getPlaceholderPropertyValues(),
                                                                      requestBody.getParentGUID(),
                                                                      requestBody.getParentRelationshipTypeName(),
                                                                      requestBody.getParentRelationshipProperties(),
                                                                      requestBody.getParentAtEnd1()));
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
     * Create a new collection that represents a digital product.
     *
     * @param serverName   name of called server.
     * @param requestBody properties for the collection and attached DigitalProduct classification

     * @return unique identifier of the newly created Collection
     *  InvalidParameterException  one of the parameters is invalid.
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public GUIDResponse createDigitalProduct(String                       serverName,
                                             NewDigitalProductRequestBody requestBody)
    {
        final String methodName = "createDigitalProduct";

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
                CollectionsClient handler = instanceHandler.getCollectionsClient(userId, serverName, methodName);

                response.setGUID(handler.createDigitalProduct(userId,
                                                              requestBody.getAnchorGUID(),
                                                              requestBody.getIsOwnAnchor(),
                                                              requestBody.getCollectionProperties(),
                                                              requestBody.getDigitalProductProperties(),
                                                              requestBody.getParentGUID(),
                                                              requestBody.getParentRelationshipTypeName(),
                                                              requestBody.getParentRelationshipProperties(),
                                                              requestBody.getParentAtEnd1()));
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
     * Update the properties of a collection.
     *
     * @param serverName         name of called server.
     * @param collectionGUID unique identifier of the collection (returned from create)
     * @param replaceAllProperties flag to indicate whether to completely replace the existing properties with the new properties, or just update
     *                          the individual properties specified on the request.
     * @param requestBody     properties for the collection.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is invalid.
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public VoidResponse   updateCollection(String               serverName,
                                           String               collectionGUID,
                                           boolean              replaceAllProperties,
                                           CollectionProperties requestBody)
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
                CollectionsClient handler = instanceHandler.getCollectionsClient(userId, serverName, methodName);

                handler.updateCollection(userId,
                                         collectionGUID,
                                         replaceAllProperties,
                                         requestBody);
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
     * Update the properties of the DigitalProduct classification attached to a collection.
     *
     * @param serverName         name of called server.
     * @param collectionGUID unique identifier of the collection (returned from create)
     * @param replaceAllProperties flag to indicate whether to completely replace the existing properties with the new properties, or just update
     *                          the individual properties specified on the request.
     * @param requestBody     properties for the DigitalProduct classification.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is invalid.
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public VoidResponse   updateDigitalProduct(String                   serverName,
                                               String                   collectionGUID,
                                               boolean                  replaceAllProperties,
                                               DigitalProductProperties requestBody)
    {
        final String methodName = "updateDigitalProduct";

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
                CollectionsClient handler = instanceHandler.getCollectionsClient(userId, serverName, methodName);

                handler.updateDigitalProduct(userId,
                                             collectionGUID,
                                             replaceAllProperties,
                                             requestBody);
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
     * Connect an existing collection to an element using the ResourceList relationship (0019).
     *
     * @param serverName         name of called server
     * @param collectionGUID unique identifier of the collection
     * @param parentGUID     unique identifier of referenceable object that the collection should be attached to
     * @param requestBody  description of how the collection will be used.
     * @param makeAnchor     like the lifecycle of the collection to that of the parent so that if the parent is deleted, so is the collection
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public VoidResponse attachCollection(String                 serverName,
                                         String                 collectionGUID,
                                         String                 parentGUID,
                                         boolean                makeAnchor,
                                         ResourceListProperties requestBody)
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

            if (requestBody != null)
            {
                CollectionsClient handler = instanceHandler.getCollectionsClient(userId, serverName, methodName);

                handler.attachCollection(userId,
                                         collectionGUID,
                                         parentGUID,
                                         requestBody,
                                         makeAnchor);
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
     * Detach an existing collection from an element.  If the collection is anchored to the element, it is deleted.
     *
     * @param serverName         name of called server.
     * @param collectionGUID unique identifier of the collection.
     * @param parentGUID     unique identifier of referenceable object that the collection should be attached to
     * @param requestBody null request body
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @SuppressWarnings(value = "unused")
    public VoidResponse detachCollection(String          serverName,
                                         String          collectionGUID,
                                         String          parentGUID,
                                         NullRequestBody requestBody)
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

            CollectionsClient handler = instanceHandler.getCollectionsClient(userId, serverName, methodName);

            handler.detachCollection(userId, collectionGUID, parentGUID);
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /**
     * Delete a collection.  It is detected from all parent elements.  If members are anchored to the collection
     * then they are also deleted.
     *
     * @param serverName         name of called server.
     * @param collectionGUID unique identifier of the collection.
     * @param requestBody null request body
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @SuppressWarnings(value = "unused")
    public VoidResponse deleteCollection(String          serverName,
                                         String          collectionGUID,
                                         NullRequestBody requestBody)
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

            CollectionsClient handler = instanceHandler.getCollectionsClient(userId, serverName, methodName);

            handler.deleteCollection(userId, collectionGUID);
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /**
     * Return a list of elements that are a member of a collection.
     *
     * @param serverName         name of called server.
     * @param collectionGUID unique identifier of the collection.
     * @param startFrom      index of the list to start from (0 for start)
     * @param pageSize       maximum number of elements to return.
     *
     * @return list of asset details
     *  InvalidParameterException  one of the parameters is invalid.
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public CollectionMembersResponse getCollectionMembers(String serverName,
                                                          String collectionGUID,
                                                          int    startFrom,
                                                          int    pageSize)
    {
        final String methodName = "getCollectionMembers";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        CollectionMembersResponse response = new CollectionMembersResponse();
        AuditLog                  auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            CollectionsClient handler = instanceHandler.getCollectionsClient(userId, serverName, methodName);

            response.setElements(handler.getCollectionMembers(userId, collectionGUID, startFrom, pageSize));
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }



    /**
     * Add an element to a collection.
     *
     * @param serverName               name of called server.
     * @param collectionGUID       unique identifier of the collection.
     * @param requestBody properties describing the membership characteristics.
     * @param elementGUID          unique identifier of the element.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is invalid.
     *  PropertyServerException    there is a problem updating information in the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public VoidResponse addToCollection(String                         serverName,
                                        String                         collectionGUID,
                                        String                         elementGUID,
                                        CollectionMembershipProperties requestBody)
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

            if (requestBody != null)
            {
                CollectionsClient handler = instanceHandler.getCollectionsClient(userId, serverName, methodName);

                handler.addToCollection(userId, collectionGUID, requestBody, elementGUID);
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
     * Update an element's membership to a collection.
     *
     * @param serverName               name of called server.
     * @param collectionGUID       unique identifier of the collection.
     * @param replaceAllProperties   flag to indicate whether to completely replace the existing properties with the new properties, or just update
     *                               the individual properties specified on the request.
     * @param requestBody properties describing the membership characteristics.
     * @param elementGUID          unique identifier of the element.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is invalid.
     *  PropertyServerException    there is a problem updating information in the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public VoidResponse updateCollectionMembership(String                         serverName,
                                                   String                         collectionGUID,
                                                   String                         elementGUID,
                                                   boolean                        replaceAllProperties,
                                                   CollectionMembershipProperties requestBody)
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

            if (requestBody != null)
            {
                CollectionsClient handler = instanceHandler.getCollectionsClient(userId, serverName, methodName);

                handler.updateCollectionMembership(userId, collectionGUID, replaceAllProperties, requestBody, elementGUID);
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
     * Remove an element from a collection.
     *
     * @param serverName         name of called server.
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
    public VoidResponse removeFromCollection(String          serverName,
                                             String          collectionGUID,
                                             String          elementGUID,
                                             NullRequestBody requestBody)
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

            CollectionsClient handler = instanceHandler.getCollectionsClient(userId, serverName, methodName);

            handler.removeFromCollection(userId, collectionGUID, elementGUID);
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }
}
