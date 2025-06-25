/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project */
/* Copyright Contributors to the ODPi Egeria category. */
package org.odpi.openmetadata.viewservices.collectionmanager.server;

import org.odpi.openmetadata.accessservices.digitalservice.client.CollectionsClient;
import org.odpi.openmetadata.commonservices.ffdc.RESTCallLogger;
import org.odpi.openmetadata.commonservices.ffdc.RESTCallToken;
import org.odpi.openmetadata.commonservices.ffdc.RESTExceptionHandler;
import org.odpi.openmetadata.commonservices.ffdc.rest.*;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.openmetadata.enums.ElementStatus;
import org.odpi.openmetadata.frameworks.openmetadata.enums.SequencingOrder;
import org.odpi.openmetadata.frameworks.openmetadata.properties.collections.CollectionMembershipProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.collections.CollectionProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.digitalbusiness.*;
import org.odpi.openmetadata.frameworks.openmetadata.properties.resources.ResourceListProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.solutions.SolutionBlueprintCompositionProperties;
import org.odpi.openmetadata.frameworks.openmetadata.search.TemplateFilter;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;
import org.odpi.openmetadata.frameworkservices.omf.rest.AnyTimeRequestBody;
import org.odpi.openmetadata.tokencontroller.TokenController;
import org.slf4j.LoggerFactory;

import java.util.Date;


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
                response.setElements(handler.getAttachedCollections(userId,
                                                                    parentGUID,
                                                                    requestBody.getFilter(),
                                                                    requestBody.getTemplateFilter(),
                                                                    requestBody.getLimitResultsByStatus(),
                                                                    requestBody.getAsOfTime(),
                                                                    requestBody.getSequencingOrder(),
                                                                    requestBody.getSequencingProperty(),
                                                                    startFrom,
                                                                    pageSize,
                                                                    requestBody.getForLineage(),
                                                                    requestBody.getForDuplicateProcessing(),
                                                                    requestBody.getEffectiveTime()));
            }
            else
            {
                response.setElements(handler.getAttachedCollections(userId,
                                                                    parentGUID,
                                                                    null,
                                                                    TemplateFilter.ALL,
                                                                    null,
                                                                    null,
                                                                    SequencingOrder.CREATION_DATE_RECENT,
                                                                    null,
                                                                    startFrom,
                                                                    pageSize,
                                                                    false,
                                                                    false,
                                                                    new Date()));
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
     * Returns the list of collections matching the search string - this is coded as a regular expression.
     *
     * @param serverName name of the service to route the request to
     * @param classificationName option name of a collection classification
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
                                               String            classificationName,
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
                                                             classificationName,
                                                             instanceHandler.getSearchString(requestBody.getFilter(), startsWith, endsWith, ignoreCase),
                                                             requestBody.getTemplateFilter(),
                                                             requestBody.getLimitResultsByStatus(),
                                                             requestBody.getAsOfTime(),
                                                             requestBody.getSequencingOrder(),
                                                             requestBody.getSequencingProperty(),
                                                             startFrom,
                                                             pageSize,
                                                             requestBody.getForLineage(),
                                                             requestBody.getForDuplicateProcessing(),
                                                             requestBody.getEffectiveTime()));
            }
            else
            {
                response.setElements(handler.findCollections(userId,
                                                             classificationName,
                                                             instanceHandler.getSearchString(null, startsWith, endsWith, ignoreCase),
                                                             TemplateFilter.ALL,
                                                             null,
                                                             null,
                                                             SequencingOrder.CREATION_DATE_RECENT,
                                                             null,
                                                             startFrom,
                                                             pageSize,
                                                             false,
                                                             false,
                                                             new Date()));
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
     * @param requestBody      name of the collections to return - match is full text match in qualifiedName or name
     * @param classificationName option name of a collection classification
     * @param startFrom index of the list to start from (0 for start)
     * @param pageSize  maximum number of elements to return
     *
     * @return a list of collections
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public CollectionsResponse getCollectionsByName(String            serverName,
                                                    String            classificationName,
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
                                                              classificationName,
                                                              requestBody.getFilter(),
                                                              requestBody.getTemplateFilter(),
                                                              requestBody.getLimitResultsByStatus(),
                                                              requestBody.getAsOfTime(),
                                                              requestBody.getSequencingOrder(),
                                                              requestBody.getSequencingProperty(),
                                                              startFrom,
                                                              pageSize,
                                                              requestBody.getForLineage(),
                                                              requestBody.getForDuplicateProcessing(),
                                                              requestBody.getEffectiveTime()));
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
     * @param classificationName option name of a collection classification
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
                                                    String            classificationName,
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
                                                                  classificationName,
                                                                  requestBody.getFilter(),
                                                                  requestBody.getTemplateFilter(),
                                                                  requestBody.getLimitResultsByStatus(),
                                                                  requestBody.getAsOfTime(),
                                                                  requestBody.getSequencingOrder(),
                                                                  requestBody.getSequencingProperty(),
                                                                  startFrom,
                                                                  pageSize,
                                                                  requestBody.getForLineage(),
                                                                  requestBody.getForDuplicateProcessing(),
                                                                  requestBody.getEffectiveTime()));
            }
            else
            {
                response.setElements(handler.getCollectionsByType(userId,
                                                                  classificationName,
                                                                  null,
                                                                  TemplateFilter.ALL,
                                                                  null,
                                                                  null,
                                                                  SequencingOrder.PROPERTY_ASCENDING,
                                                                  OpenMetadataProperty.QUALIFIED_NAME.name,
                                                                  startFrom,
                                                                  pageSize,
                                                                  false,
                                                                  false,
                                                                  new Date()));
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
     * @param collectionGUID unique identifier of the required collection
     * @param requestBody time values for the query
     *
     * @return collection properties
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public CollectionResponse getCollection(String             serverName,
                                            String             collectionGUID,
                                            AnyTimeRequestBody requestBody)
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

            if (requestBody != null)
            {
                response.setElement(handler.getCollection(userId,
                                                          collectionGUID,
                                                          requestBody.getAsOfTime(),
                                                          requestBody.getForLineage(),
                                                          requestBody.getForDuplicateProcessing(),
                                                          requestBody.getEffectiveTime()));
            }
            else
            {
                response.setElement(handler.getCollection(userId, collectionGUID, null, false, false, new Date()));
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
    public GUIDResponse createCollection(String                serverName,
                                         String                optionalClassificationName,
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
                CollectionsClient handler = instanceHandler.getCollectionsClient(userId, serverName, methodName);

                ElementStatus initialStatus = ElementStatus.ACTIVE;

                if (requestBody instanceof NewDigitalProductRequestBody digitalProductRequestBody)
                {
                    initialStatus = handler.getElementStatus(digitalProductRequestBody.getInitialStatus());
                }
                else if (requestBody instanceof NewAgreementRequestBody agreementRequestBody)
                {
                    initialStatus = handler.getElementStatus(agreementRequestBody.getInitialStatus());
                }

                if (requestBody.getProperties() instanceof CollectionProperties collectionProperties)
                {
                    response.setGUID(handler.createCollection(userId,
                                                              requestBody.getAnchorGUID(),
                                                              requestBody.getIsOwnAnchor(),
                                                              requestBody.getAnchorScopeGUID(),
                                                              optionalClassificationName,
                                                              collectionProperties,
                                                              initialStatus,
                                                              requestBody.getParentGUID(),
                                                              requestBody.getParentRelationshipTypeName(),
                                                              requestBody.getParentRelationshipProperties(),
                                                              requestBody.getParentAtEnd1(),
                                                              requestBody.getEffectiveTime()));
                }
                else if (requestBody.getProperties() == null)
                {
                    response.setGUID(handler.createCollection(userId,
                                                              requestBody.getAnchorGUID(),
                                                              requestBody.getIsOwnAnchor(),
                                                              requestBody.getAnchorScopeGUID(),
                                                              optionalClassificationName,
                                                              null,
                                                              initialStatus,
                                                              requestBody.getParentGUID(),
                                                              requestBody.getParentRelationshipTypeName(),
                                                              requestBody.getParentRelationshipProperties(),
                                                              requestBody.getParentAtEnd1(),
                                                              requestBody.getEffectiveTime()));
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
                                                                      requestBody.getAnchorScopeGUID(),
                                                                      null,
                                                                      null,
                                                                      requestBody.getTemplateGUID(),
                                                                      requestBody.getReplacementProperties(),
                                                                      requestBody.getPlaceholderPropertyValues(),
                                                                      requestBody.getParentGUID(),
                                                                      requestBody.getParentRelationshipTypeName(),
                                                                      requestBody.getParentRelationshipProperties(),
                                                                      requestBody.getParentAtEnd1(),
                                                                      requestBody.getEffectiveTime()));
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
    public VoidResponse   updateCollection(String                   serverName,
                                           String                   collectionGUID,
                                           boolean                  replaceAllProperties,
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
                CollectionsClient handler = instanceHandler.getCollectionsClient(userId, serverName, methodName);

                if (requestBody.getProperties() instanceof CollectionProperties properties)
                {
                    handler.updateCollection(userId,
                                             collectionGUID,
                                             replaceAllProperties,
                                             properties,
                                             requestBody.getEffectiveTime());
                }
                else if (requestBody.getProperties() == null)
                {
                    handler.updateCollection(userId,
                                             collectionGUID,
                                             replaceAllProperties,
                                             null,
                                             requestBody.getEffectiveTime());
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
     * Update the status of a digital product.
     *
     * @param serverName         name of called server.
     * @param digitalProductGUID unique identifier of the digital product (returned from create)
     * @param requestBody     properties for the new element.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is invalid.
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public VoidResponse updateDigitalProductStatus(String                          serverName,
                                                   String                          digitalProductGUID,
                                                   DigitalProductStatusRequestBody requestBody)
    {
        final String methodName = "updateDigitalProductStatus";

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

                handler.updateCollectionStatus(userId,
                                               requestBody.getExternalSourceGUID(),
                                               requestBody.getExternalSourceName(),
                                               digitalProductGUID,
                                               handler.getElementStatus(requestBody.getStatus()),
                                               requestBody.getForLineage(),
                                               requestBody.getForDuplicateProcessing(),
                                               requestBody.getEffectiveTime());
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
     * Update the status of an agreement.
     *
     * @param serverName         name of called server.
     * @param agreementGUID unique identifier of the agreement (returned from create)
     * @param requestBody     properties for the new element.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is invalid.
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public VoidResponse updateAgreementStatus(String                     serverName,
                                              String                     agreementGUID,
                                              AgreementStatusRequestBody requestBody)
    {
        final String methodName = "updateAgreementStatus";

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

                handler.updateCollectionStatus(userId,
                                               requestBody.getExternalSourceGUID(),
                                               requestBody.getExternalSourceName(),
                                               agreementGUID,
                                               handler.getElementStatus(requestBody.getStatus()),
                                               requestBody.getForLineage(),
                                               requestBody.getForDuplicateProcessing(),
                                               requestBody.getEffectiveTime());
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
    public VoidResponse attachCollection(String                  serverName,
                                         String                  collectionGUID,
                                         String                  parentGUID,
                                         boolean                 makeAnchor,
                                         RelationshipRequestBody requestBody)
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

            CollectionsClient handler = instanceHandler.getCollectionsClient(userId, serverName, methodName);

            if (requestBody != null)
            {
                if (requestBody.getProperties() instanceof ResourceListProperties properties)
                {
                    handler.attachCollection(userId,
                                             requestBody.getExternalSourceGUID(),
                                             requestBody.getExternalSourceName(),
                                             collectionGUID,
                                             parentGUID,
                                             properties,
                                             makeAnchor,
                                             requestBody.getForLineage(),
                                             requestBody.getForDuplicateProcessing(),
                                             requestBody.getEffectiveTime());
                }
                else if (requestBody.getProperties() == null)
                {
                    handler.attachCollection(userId,
                                             requestBody.getExternalSourceGUID(),
                                             requestBody.getExternalSourceName(),
                                             collectionGUID,
                                             parentGUID,
                                             null,
                                             makeAnchor,
                                             requestBody.getForLineage(),
                                             requestBody.getForDuplicateProcessing(),
                                             requestBody.getEffectiveTime());
                }
                else
                {
                    restExceptionHandler.handleInvalidPropertiesObject(ResourceListProperties.class.getName(), methodName);
                }
            }
            else
            {
                handler.attachCollection(userId,
                                         null,
                                         null,
                                         collectionGUID,
                                         parentGUID,
                                         null,
                                         makeAnchor,
                                         false,
                                         false,
                                         new Date());
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
     * @param collectionGUID unique identifier of the collection.
     * @param parentGUID     unique identifier of referenceable object that the collection should be attached to
     * @param requestBody null request body
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public VoidResponse detachCollection(String                    serverName,
                                         String                    collectionGUID,
                                         String                    parentGUID,
                                         MetadataSourceRequestBody requestBody)
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

            if (requestBody != null)
            {
                handler.detachCollection(userId,
                                         requestBody.getExternalSourceGUID(),
                                         requestBody.getExternalSourceName(),
                                         collectionGUID,
                                         parentGUID,
                                         requestBody.getForLineage(),
                                         requestBody.getForDuplicateProcessing(),
                                         requestBody.getEffectiveTime());
            }
            else
            {
                handler.detachCollection(userId,
                                         null,
                                         null,
                                         collectionGUID,
                                         parentGUID,
                                         false,
                                         false,
                                         new Date());
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
     * Link two dependent products.
     *
     * @param serverName         name of called server
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
                                                     String                  consumerDigitalProductGUID,
                                                     String                  consumedDigitalProductGUID,
                                                     RelationshipRequestBody requestBody)
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
            CollectionsClient handler = instanceHandler.getCollectionsClient(userId, serverName, methodName);

            if (requestBody != null)
            {
                if (requestBody.getProperties() instanceof DigitalProductDependencyProperties properties)
                {
                    handler.linkDigitalProductDependency(userId,
                                                         requestBody.getExternalSourceGUID(),
                                                         requestBody.getExternalSourceName(),
                                                         consumerDigitalProductGUID,
                                                         consumedDigitalProductGUID,
                                                         properties,
                                                         requestBody.getForLineage(),
                                                         requestBody.getForDuplicateProcessing(),
                                                         requestBody.getEffectiveTime());
                }
                else if (requestBody.getProperties() == null)
                {
                    handler.linkDigitalProductDependency(userId,
                                                         requestBody.getExternalSourceGUID(),
                                                         requestBody.getExternalSourceName(),
                                                         consumerDigitalProductGUID,
                                                         consumedDigitalProductGUID,
                                                         null,
                                                         requestBody.getForLineage(),
                                                         requestBody.getForDuplicateProcessing(),
                                                         requestBody.getEffectiveTime());
                }
                else
                {
                    restExceptionHandler.handleInvalidPropertiesObject(SolutionBlueprintCompositionProperties.class.getName(), methodName);
                }
            }
            else
            {
                handler.linkDigitalProductDependency(userId,
                                                     null,
                                                     null,
                                                     consumerDigitalProductGUID,
                                                     consumedDigitalProductGUID,
                                                     null,
                                                     false,
                                                     false,
                                                     new Date());
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
     * @param consumerDigitalProductGUID    unique identifier of the digital product that has the dependency.
     * @param consumedDigitalProductGUID    unique identifier of the digital product that it is using.
     * @param requestBody  description of the relationship.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public VoidResponse detachDigitalProductDependency(String                    serverName,
                                                       String                    consumerDigitalProductGUID,
                                                       String                    consumedDigitalProductGUID,
                                                       MetadataSourceRequestBody requestBody)
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

            CollectionsClient handler = instanceHandler.getCollectionsClient(userId, serverName, methodName);

            if (requestBody != null)
            {
                handler.detachDigitalProductDependency(userId,
                                                       requestBody.getExternalSourceGUID(),
                                                       requestBody.getExternalSourceName(),
                                                       consumerDigitalProductGUID,
                                                       consumedDigitalProductGUID,
                                                       requestBody.getForLineage(),
                                                       requestBody.getForDuplicateProcessing(),
                                                       requestBody.getEffectiveTime());
            }
            else
            {
                handler.detachDigitalProductDependency(userId,
                                                       null,
                                                       null,
                                                       consumerDigitalProductGUID,
                                                       consumedDigitalProductGUID,
                                                       false,
                                                       false,
                                                       new Date());
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
     * Attach a subscriber to a subscription.
     *
     * @param serverName         name of called server
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
                                       String                  digitalSubscriberGUID,
                                       String                  digitalSubscriptionGUID,
                                       RelationshipRequestBody requestBody)
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
            CollectionsClient handler = instanceHandler.getCollectionsClient(userId, serverName, methodName);

            if (requestBody != null)
            {
                if (requestBody.getProperties() instanceof DigitalSubscriberProperties properties)
                {
                    handler.linkSubscriber(userId,
                                           requestBody.getExternalSourceGUID(),
                                           requestBody.getExternalSourceName(),
                                           digitalSubscriberGUID,
                                           digitalSubscriptionGUID,
                                           properties,
                                           requestBody.getForLineage(),
                                           requestBody.getForDuplicateProcessing(),
                                           requestBody.getEffectiveTime());
                }
                else if (requestBody.getProperties() == null)
                {
                    handler.linkSubscriber(userId,
                                           requestBody.getExternalSourceGUID(),
                                           requestBody.getExternalSourceName(),
                                           digitalSubscriberGUID,
                                           digitalSubscriptionGUID,
                                           null,
                                           requestBody.getForLineage(),
                                           requestBody.getForDuplicateProcessing(),
                                           requestBody.getEffectiveTime());
                }
                else
                {
                    restExceptionHandler.handleInvalidPropertiesObject(SolutionBlueprintCompositionProperties.class.getName(), methodName);
                }
            }
            else
            {
                handler.linkSubscriber(userId,
                                       null,
                                       null,
                                       digitalSubscriberGUID,
                                       digitalSubscriptionGUID,
                                       null,
                                       false,
                                       false,
                                       new Date());
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
     * @param digitalSubscriberGUID  unique identifier of the subscriber (referenceable)
     * @param digitalSubscriptionGUID unique identifier of the  digital subscription agreement
     * @param requestBody  description of the relationship.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public VoidResponse detachSubscriber(String                    serverName,
                                         String                    digitalSubscriberGUID,
                                         String                    digitalSubscriptionGUID,
                                         MetadataSourceRequestBody requestBody)
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

            CollectionsClient handler = instanceHandler.getCollectionsClient(userId, serverName, methodName);

            if (requestBody != null)
            {
                handler.detachSubscriber(userId,
                                         requestBody.getExternalSourceGUID(),
                                         requestBody.getExternalSourceName(),
                                         digitalSubscriberGUID,
                                         digitalSubscriptionGUID,
                                         requestBody.getForLineage(),
                                         requestBody.getForDuplicateProcessing(),
                                         requestBody.getEffectiveTime());
            }
            else
            {
                handler.detachSubscriber(userId,
                                         null,
                                         null,
                                         digitalSubscriberGUID,
                                         digitalSubscriptionGUID,
                                         false,
                                         false,
                                         new Date());
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
     * Attach a product manager to a digital product.
     *
     * @param serverName         name of called server
     * @param digitalProductGUID  unique identifier of the digital product
     * @param digitalProductManagerGUID      unique identifier of the product manager role
     * @param requestBody  description of the relationship.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public VoidResponse linkProductManager(String                  serverName,
                                           String                  digitalProductGUID,
                                           String                  digitalProductManagerGUID,
                                           RelationshipRequestBody requestBody)
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
            CollectionsClient handler = instanceHandler.getCollectionsClient(userId, serverName, methodName);

            if (requestBody != null)
            {
                if (requestBody.getProperties() instanceof DigitalProductManagementProperties properties)
                {
                    handler.linkProductManager(userId,
                                               requestBody.getExternalSourceGUID(),
                                               requestBody.getExternalSourceName(),
                                               digitalProductGUID,
                                               digitalProductManagerGUID,
                                               properties,
                                               requestBody.getForLineage(),
                                               requestBody.getForDuplicateProcessing(),
                                               requestBody.getEffectiveTime());
                }
                else if (requestBody.getProperties() == null)
                {
                    handler.linkProductManager(userId,
                                               requestBody.getExternalSourceGUID(),
                                               requestBody.getExternalSourceName(),
                                               digitalProductGUID,
                                               digitalProductManagerGUID,
                                               null,
                                               requestBody.getForLineage(),
                                               requestBody.getForDuplicateProcessing(),
                                               requestBody.getEffectiveTime());
                }
                else
                {
                    restExceptionHandler.handleInvalidPropertiesObject(SolutionBlueprintCompositionProperties.class.getName(), methodName);
                }
            }
            else
            {
                handler.linkProductManager(userId,
                                           null,
                                           null,
                                           digitalProductGUID,
                                           digitalProductManagerGUID,
                                           null,
                                           false,
                                           false,
                                           new Date());
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
     * @param digitalProductGUID  unique identifier of the digital product
     * @param digitalProductManagerGUID      unique identifier of the product manager role
     * @param requestBody  description of the relationship.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public VoidResponse detachProductManager(String                    serverName,
                                             String                    digitalProductGUID,
                                             String                    digitalProductManagerGUID,
                                             MetadataSourceRequestBody requestBody)
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

            CollectionsClient handler = instanceHandler.getCollectionsClient(userId, serverName, methodName);

            if (requestBody != null)
            {
                handler.detachProductManager(userId,
                                             requestBody.getExternalSourceGUID(),
                                             requestBody.getExternalSourceName(),
                                             digitalProductGUID,
                                             digitalProductManagerGUID,
                                             requestBody.getForLineage(),
                                             requestBody.getForDuplicateProcessing(),
                                             requestBody.getEffectiveTime());
            }
            else
            {
                handler.detachProductManager(userId,
                                             null,
                                             null,
                                             digitalProductGUID,
                                             digitalProductManagerGUID,
                                             false,
                                             false,
                                             new Date());
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
     * Attach an actor to an agreement.
     *
     * @param serverName         name of called server
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
                                           String                  agreementGUID,
                                           String                  actorGUID,
                                           RelationshipRequestBody requestBody)
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
            CollectionsClient handler = instanceHandler.getCollectionsClient(userId, serverName, methodName);

            if (requestBody != null)
            {
                if (requestBody.getProperties() instanceof AgreementActorProperties properties)
                {
                    response.setGUID(handler.linkAgreementActor(userId,
                                                                requestBody.getExternalSourceGUID(),
                                                                requestBody.getExternalSourceName(),
                                                                agreementGUID,
                                                                actorGUID,
                                                                properties,
                                                                requestBody.getForLineage(),
                                                                requestBody.getForDuplicateProcessing(),
                                                                requestBody.getEffectiveTime()));
                }
                else if (requestBody.getProperties() == null)
                {
                    response.setGUID(handler.linkAgreementActor(userId,
                                                                requestBody.getExternalSourceGUID(),
                                                                requestBody.getExternalSourceName(),
                                                                agreementGUID,
                                                                actorGUID,
                                                                null,
                                                                requestBody.getForLineage(),
                                                                requestBody.getForDuplicateProcessing(),
                                                                requestBody.getEffectiveTime()));
                }
                else
                {
                    restExceptionHandler.handleInvalidPropertiesObject(SolutionBlueprintCompositionProperties.class.getName(), methodName);
                }
            }
            else
            {
                response.setGUID(handler.linkAgreementActor(userId,
                                                            null,
                                                            null,
                                                            agreementGUID,
                                                            actorGUID,
                                                            null,
                                                            false,
                                                            false,
                                                            new Date()));
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
     * @param agreementActorRelationshipGUID  unique identifier of the element being described
     * @param requestBody  description of the relationship.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public VoidResponse detachAgreementActor(String                    serverName,
                                             String                    agreementActorRelationshipGUID,
                                             MetadataSourceRequestBody requestBody)
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

            CollectionsClient handler = instanceHandler.getCollectionsClient(userId, serverName, methodName);

            if (requestBody != null)
            {
                handler.detachAgreementActor(userId,
                                             requestBody.getExternalSourceGUID(),
                                             requestBody.getExternalSourceName(),
                                             agreementActorRelationshipGUID,
                                             requestBody.getForLineage(),
                                             requestBody.getForDuplicateProcessing(),
                                             requestBody.getEffectiveTime());
            }
            else
            {
                handler.detachAgreementActor(userId,
                                             null,
                                             null,
                                             agreementActorRelationshipGUID,
                                             false,
                                             false,
                                             new Date());
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
     * Attach an agreement to an element involved in its definition.
     *
     * @param serverName         name of called server
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
                                          String                  agreementGUID,
                                          String                  agreementItemGUID,
                                          RelationshipRequestBody requestBody)
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
            CollectionsClient handler = instanceHandler.getCollectionsClient(userId, serverName, methodName);

            if (requestBody != null)
            {
                if (requestBody.getProperties() instanceof AgreementItemProperties properties)
                {
                    handler.linkAgreementItem(userId,
                                              requestBody.getExternalSourceGUID(),
                                              requestBody.getExternalSourceName(),
                                              agreementGUID,
                                              agreementItemGUID,
                                              properties,
                                              requestBody.getForLineage(),
                                              requestBody.getForDuplicateProcessing(),
                                              requestBody.getEffectiveTime());
                }
                else if (requestBody.getProperties() == null)
                {
                    handler.linkAgreementItem(userId,
                                              requestBody.getExternalSourceGUID(),
                                              requestBody.getExternalSourceName(),
                                              agreementGUID,
                                              agreementItemGUID,
                                              null,
                                              requestBody.getForLineage(),
                                              requestBody.getForDuplicateProcessing(),
                                              requestBody.getEffectiveTime());
                }
                else
                {
                    restExceptionHandler.handleInvalidPropertiesObject(SolutionBlueprintCompositionProperties.class.getName(), methodName);
                }
            }
            else
            {
                handler.linkAgreementItem(userId,
                                          null,
                                          null,
                                          agreementGUID,
                                          agreementItemGUID,
                                          null,
                                          false,
                                          false,
                                          new Date());
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
     * @param agreementGUID  unique identifier of the agreement
     * @param agreementItemGUID      unique identifier of the agreement item
     * @param requestBody  description of the relationship.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public VoidResponse detachAgreementItem(String                    serverName,
                                            String                    agreementGUID,
                                            String                    agreementItemGUID,
                                            MetadataSourceRequestBody requestBody)
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

            CollectionsClient handler = instanceHandler.getCollectionsClient(userId, serverName, methodName);

            if (requestBody != null)
            {
                handler.detachAgreementItem(userId,
                                            requestBody.getExternalSourceGUID(),
                                            requestBody.getExternalSourceName(),
                                            agreementGUID,
                                            agreementItemGUID,
                                            requestBody.getForLineage(),
                                            requestBody.getForDuplicateProcessing(),
                                            requestBody.getEffectiveTime());
            }
            else
            {
                handler.detachAgreementItem(userId,
                                            null,
                                            null,
                                            agreementGUID,
                                            agreementItemGUID,
                                            false,
                                            false,
                                            new Date());
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
     * Attach an agreement to an external reference element that describes the location of the contract documents.
     *
     * @param serverName         name of called server
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
                                     String                  agreementGUID,
                                     String                  externalReferenceGUID,
                                     RelationshipRequestBody requestBody)
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
            CollectionsClient handler = instanceHandler.getCollectionsClient(userId, serverName, methodName);

            if (requestBody != null)
            {
                if (requestBody.getProperties() instanceof ContractLinkProperties properties)
                {
                    handler.linkContract(userId,
                                         requestBody.getExternalSourceGUID(),
                                         requestBody.getExternalSourceName(),
                                         agreementGUID,
                                         externalReferenceGUID,
                                         properties,
                                         requestBody.getForLineage(),
                                         requestBody.getForDuplicateProcessing(),
                                         requestBody.getEffectiveTime());
                }
                else if (requestBody.getProperties() == null)
                {
                    handler.linkContract(userId,
                                         requestBody.getExternalSourceGUID(),
                                         requestBody.getExternalSourceName(),
                                         agreementGUID,
                                         externalReferenceGUID,
                                         null,
                                         requestBody.getForLineage(),
                                         requestBody.getForDuplicateProcessing(),
                                         requestBody.getEffectiveTime());
                }
                else
                {
                    restExceptionHandler.handleInvalidPropertiesObject(SolutionBlueprintCompositionProperties.class.getName(), methodName);
                }
            }
            else
            {
                handler.linkContract(userId,
                                     null,
                                     null,
                                     agreementGUID,
                                     externalReferenceGUID,
                                     null,
                                     false,
                                     false,
                                     new Date());
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
     * @param agreementGUID  unique identifier of the agreement
     * @param externalReferenceGUID      unique identifier of the external reference describing the location of the contract
     * @param requestBody  description of the relationship.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public VoidResponse detachContract(String                    serverName,
                                       String                    agreementGUID,
                                       String                    externalReferenceGUID,
                                       MetadataSourceRequestBody requestBody)
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

            CollectionsClient handler = instanceHandler.getCollectionsClient(userId, serverName, methodName);

            if (requestBody != null)
            {
                handler.detachContract(userId,
                                       requestBody.getExternalSourceGUID(),
                                       requestBody.getExternalSourceName(),
                                       agreementGUID,
                                       externalReferenceGUID,
                                       requestBody.getForLineage(),
                                       requestBody.getForDuplicateProcessing(),
                                       requestBody.getEffectiveTime());
            }
            else
            {
                handler.detachContract(userId,
                                       null,
                                       null,
                                       agreementGUID,
                                       externalReferenceGUID,
                                       false,
                                       false,
                                       new Date());
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
     * Delete a collection.  It is detached from all parent elements.  If members are anchored to the collection
     * then they are also deleted.
     *
     * @param serverName         name of called server.
     * @param collectionGUID unique identifier of the collection
     * @param cascadedDelete should nested collections be deleted? If false, the delete fails if there are nested
     *                       collections.  If true, nested collections are delete - but not member elements
     *                       unless they are anchored to the collection
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
                                         boolean         cascadedDelete,
                                         MetadataSourceRequestBody requestBody)
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

            handler.deleteCollection(userId, collectionGUID, cascadedDelete);
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
     * @param collectionGUID unique identifier of the collection.
     * @param startFrom      index of the list to start from (0 for start)
     * @param pageSize       maximum number of elements to return.
     *
     * @return list of asset details
     *  InvalidParameterException  one of the parameters is invalid.
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public CollectionMembersResponse getCollectionMembers(String             serverName,
                                                          String             collectionGUID,
                                                          int                startFrom,
                                                          int                pageSize,
                                                          ResultsRequestBody requestBody)
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

            if (requestBody != null)
            {
                response.setElements(handler.getCollectionMembers(userId,
                                                                  collectionGUID,
                                                                  requestBody.getLimitResultsByStatus(),
                                                                  requestBody.getAsOfTime(),
                                                                  requestBody.getSequencingProperty(),
                                                                  requestBody.getSequencingOrder(),
                                                                  requestBody.getForLineage(),
                                                                  requestBody.getForDuplicateProcessing(),
                                                                  requestBody.getEffectiveTime(),
                                                                  startFrom,
                                                                  pageSize));
            }
            else
            {
                response.setElements(handler.getCollectionMembers(userId,
                                                                  collectionGUID,
                                                                  null,
                                                                  null,
                                                                  null,
                                                                  SequencingOrder.CREATION_DATE_RECENT,
                                                                  false,
                                                                  false,
                                                                  new Date(),
                                                                  startFrom,
                                                                  pageSize));
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
     * Return a graph of elements that are the nested members of a collection along
     * with elements immediately connected to the starting collection.  The result
     * includes a mermaid graph of the returned elements.
     *
     * @param serverName         name of called server.
     * @param collectionGUID unique identifier of the collection.
     * @param startFrom      index of the list to start from (0 for start)
     * @param pageSize       maximum number of elements to return.
     *
     * @return list of collection details
     *  InvalidParameterException  one of the parameters is invalid.
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public CollectionGraphResponse getCollectionGraph(String             serverName,
                                                      String             collectionGUID,
                                                      int                startFrom,
                                                      int                pageSize,
                                                      ResultsRequestBody requestBody)
    {
        final String methodName = "getCollectionGraph";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        CollectionGraphResponse response = new CollectionGraphResponse();
        AuditLog                auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            CollectionsClient handler = instanceHandler.getCollectionsClient(userId, serverName, methodName);

            if (requestBody != null)
            {
                response.setGraph(handler.getCollectionGraph(userId,
                                                             collectionGUID,
                                                             requestBody.getLimitResultsByStatus(),
                                                             requestBody.getAsOfTime(),
                                                             requestBody.getSequencingProperty(),
                                                             requestBody.getSequencingOrder(),
                                                             requestBody.getForLineage(),
                                                             requestBody.getForDuplicateProcessing(),
                                                             requestBody.getEffectiveTime(),
                                                             startFrom,
                                                             pageSize));
            }
            else
            {
                response.setGraph(handler.getCollectionGraph(userId,
                                                             collectionGUID,
                                                             null,
                                                             null,
                                                             null,
                                                             SequencingOrder.CREATION_DATE_RECENT,
                                                             false,
                                                             false,
                                                             new Date(),
                                                             startFrom,
                                                             pageSize));
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
    public VoidResponse addToCollection(String                  serverName,
                                        String                  collectionGUID,
                                        String                  elementGUID,
                                        RelationshipRequestBody requestBody)
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

            CollectionsClient handler = instanceHandler.getCollectionsClient(userId, serverName, methodName);

            if (requestBody != null)
            {
                if (requestBody.getProperties() instanceof CollectionMembershipProperties properties)
                {
                    handler.addToCollection(userId, collectionGUID, properties, elementGUID, null);
                }
                else if (requestBody.getProperties() instanceof CollectionMembershipProperties properties)
                {
                    handler.addToCollection(userId, collectionGUID, properties, elementGUID, null);
                }
                else
                {
                    restExceptionHandler.handleInvalidPropertiesObject(CollectionMembershipProperties.class.getName(), methodName);
                }
            }
            else
            {
                handler.addToCollection(userId, collectionGUID, null, elementGUID, null);
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
    public VoidResponse updateCollectionMembership(String                  serverName,
                                                   String                  collectionGUID,
                                                   String                  elementGUID,
                                                   boolean                 replaceAllProperties,
                                                   RelationshipRequestBody requestBody)
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

            CollectionsClient handler = instanceHandler.getCollectionsClient(userId, serverName, methodName);

            if (requestBody != null)
            {
                if (requestBody.getProperties() instanceof CollectionMembershipProperties properties)
                {
                    handler.updateCollectionMembership(userId, collectionGUID, replaceAllProperties, properties, elementGUID, requestBody.getEffectiveTime());
                }
                else if (requestBody.getProperties() == null)
                {
                    handler.updateCollectionMembership(userId, collectionGUID, replaceAllProperties, null, elementGUID, requestBody.getEffectiveTime());
                }
                else
                {
                    restExceptionHandler.handleInvalidPropertiesObject(CollectionMembershipProperties.class.getName(), methodName);
                }
            }
            else
            {
                handler.updateCollectionMembership(userId, collectionGUID, replaceAllProperties, null, elementGUID, new Date());
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
    public VoidResponse removeFromCollection(String                    serverName,
                                             String                    collectionGUID,
                                             String                    elementGUID,
                                             MetadataSourceRequestBody requestBody)
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

            handler.removeFromCollection(userId, collectionGUID, elementGUID, null);
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }
}
