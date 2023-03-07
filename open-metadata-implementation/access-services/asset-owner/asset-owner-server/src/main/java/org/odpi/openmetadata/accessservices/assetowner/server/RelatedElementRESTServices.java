/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.assetowner.server;


import org.odpi.openmetadata.accessservices.assetowner.metadataelements.RelatedElement;
import org.odpi.openmetadata.accessservices.assetowner.properties.ResourceListProperties;
import org.odpi.openmetadata.accessservices.assetowner.properties.StakeholderProperties;
import org.odpi.openmetadata.accessservices.assetowner.rest.ExternalSourceRequestBody;
import org.odpi.openmetadata.accessservices.assetowner.rest.RelatedElementListResponse;
import org.odpi.openmetadata.accessservices.assetowner.rest.RelationshipRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.RESTCallLogger;
import org.odpi.openmetadata.commonservices.ffdc.RESTCallToken;
import org.odpi.openmetadata.commonservices.ffdc.RESTExceptionHandler;
import org.odpi.openmetadata.commonservices.ffdc.rest.VoidResponse;
import org.odpi.openmetadata.commonservices.generichandlers.OpenMetadataAPIMapper;
import org.odpi.openmetadata.commonservices.generichandlers.ReferenceableHandler;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.slf4j.LoggerFactory;

import java.util.Date;

/**
 * RelatedElementRESTServices support requests about common Referenceable relationships.
 * It is the server side for the RelatedElementsManagementInterface.
 */
public class RelatedElementRESTServices
{
    private static final AssetOwnerInstanceHandler  instanceHandler = new AssetOwnerInstanceHandler();

    private static final RESTExceptionHandler restExceptionHandler = new RESTExceptionHandler();
    private static final RESTCallLogger       restCallLogger       = new RESTCallLogger(LoggerFactory.getLogger(RelatedElementRESTServices.class),
                                                                                        instanceHandler.getServiceName());


    /**
     * Create a "MoreInformation" relationship between an element that is descriptive and one that is providing the detail.
     *
     * @param serverName name of the service to route the request to.
     * @param userId             calling user
     * @param elementGUID        unique identifier of the element that is descriptive
     * @param detailGUID         unique identifier of the element that provides the detail
     * @param requestBody relationship properties
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public VoidResponse setupMoreInformation(String                  serverName,
                                             String                  userId,
                                             String                  elementGUID,
                                             String                  detailGUID,
                                             RelationshipRequestBody requestBody)
    {
        final String methodName               = "setupMoreInformation";
        final String elementGUIDParameterName = "elementGUID";
        final String detailGUIDParameterName  = "detailGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            ReferenceableHandler<RelatedElement> handler = instanceHandler.getRelatedElementHandler(userId, serverName, methodName);

            if (requestBody != null)
            {
                if (requestBody.getProperties() != null)
                {
                    handler.addMoreInformation(userId,
                                               requestBody.getExternalSourceGUID(),
                                               requestBody.getExternalSourceName(),
                                               elementGUID,
                                               elementGUIDParameterName,
                                               detailGUID,
                                               detailGUIDParameterName,
                                               requestBody.getProperties().getEffectiveFrom(),
                                               requestBody.getProperties().getEffectiveTo(),
                                               false,
                                               false,
                                               new Date(),
                                               methodName);
                }
                else
                {
                    handler.addMoreInformation(userId,
                                               requestBody.getExternalSourceGUID(),
                                               requestBody.getExternalSourceName(),
                                               elementGUID,
                                               elementGUIDParameterName,
                                               detailGUID,
                                               detailGUIDParameterName,
                                               null,
                                               null,
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
     * Remove a "MoreInformation" relationship between two referenceables.
     *
     * @param serverName name of the service to route the request to.
     * @param userId             calling user
     * @param elementGUID        unique identifier of the element that is descriptive
     * @param detailGUID         unique identifier of the element that provides the detail
     * @param requestBody external source identifiers
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public VoidResponse clearMoreInformation(String                    serverName,
                                             String                    userId,
                                             String                    elementGUID,
                                             String                    detailGUID,
                                             ExternalSourceRequestBody requestBody)
    {
        final String methodName               = "clearMoreInformation";
        final String elementGUIDParameterName = "elementGUID";
        final String detailGUIDParameterName  = "detailGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            ReferenceableHandler<RelatedElement> handler = instanceHandler.getRelatedElementHandler(userId, serverName, methodName);

            if (requestBody != null)
            {
                handler.removeMoreInformation(userId,
                                              requestBody.getExternalSourceGUID(),
                                              requestBody.getExternalSourceName(),
                                              elementGUID,
                                              elementGUIDParameterName,
                                              detailGUID,
                                              detailGUIDParameterName,
                                              false,
                                              false,
                                              new Date(),
                                              methodName);
            }
            else
            {
                handler.removeMoreInformation(userId,
                                              null,
                                              null,
                                              elementGUID,
                                              elementGUIDParameterName,
                                              detailGUID,
                                              detailGUIDParameterName,
                                              false,
                                              false,
                                              new Date(),
                                              methodName);
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
     * Retrieve the detail elements linked via a "MoreInformation" relationship between two referenceables.
     *
     * @param serverName name of the service to route the request to.
     * @param userId             calling user
     * @param elementGUID        unique identifier of the element that is descriptive
     * @param startFrom  index of the list to start from (0 for start)
     * @param pageSize   maximum number of elements to return.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public RelatedElementListResponse getMoreInformation(String serverName,
                                                         String userId,
                                                         String elementGUID,
                                                         int    startFrom,
                                                         int    pageSize)
    {
        final String methodName        = "getMoreInformation";
        final String guidPropertyName  = "elementGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        RelatedElementListResponse response = new RelatedElementListResponse();
        AuditLog                   auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            ReferenceableHandler<RelatedElement> handler = instanceHandler.getRelatedElementHandler(userId, serverName, methodName);

            response.setElementList(handler.getMoreInformation(userId,
                                                               elementGUID,
                                                               guidPropertyName,
                                                               OpenMetadataAPIMapper.REFERENCEABLE_TYPE_NAME,
                                                               OpenMetadataAPIMapper.REFERENCEABLE_TYPE_NAME,
                                                               startFrom,
                                                               pageSize,
                                                               false,
                                                               false,
                                                               new Date(),
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
     * Retrieve the descriptive elements linked via a "MoreInformation" relationship between two referenceables.
     *
     * @param serverName name of the service to route the request to.
     * @param userId             calling user
     * @param detailGUID         unique identifier of the element that provides the detail
     * @param startFrom  index of the list to start from (0 for start)
     * @param pageSize   maximum number of elements to return.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public  RelatedElementListResponse getDescriptiveElements(String serverName,
                                                              String userId,
                                                              String detailGUID,
                                                              int    startFrom,
                                                              int    pageSize)
    {
        final String methodName        = "getDescriptiveElements";
        final String guidPropertyName  = "detailGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        RelatedElementListResponse response = new RelatedElementListResponse();
        AuditLog                   auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            ReferenceableHandler<RelatedElement> handler = instanceHandler.getRelatedElementHandler(userId, serverName, methodName);

            response.setElementList(handler.getDescriptiveElements(userId,
                                                                   detailGUID,
                                                                   guidPropertyName,
                                                                   OpenMetadataAPIMapper.REFERENCEABLE_TYPE_NAME,
                                                                   OpenMetadataAPIMapper.REFERENCEABLE_TYPE_NAME,
                                                                   startFrom,
                                                                   pageSize,
                                                                   false,
                                                                   false,
                                                                   new Date(),
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
     * Create a "Stakeholder" relationship between an element and its stakeholder.
     *
     * @param serverName name of the service to route the request to.
     * @param userId             calling user
     * @param elementGUID        unique identifier of the element
     * @param stakeholderGUID    unique identifier of the stakeholder
     * @param requestBody relationship properties
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public VoidResponse setupStakeholder(String                  serverName,
                                         String                  userId,
                                         String                  elementGUID,
                                         String                  stakeholderGUID,
                                         RelationshipRequestBody requestBody)
    {
        final String methodName                   = "setupStakeholder";
        final String elementGUIDParameterName     = "elementGUID";
        final String stakeholderGUIDParameterName = "stakeholderGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            ReferenceableHandler<RelatedElement> handler = instanceHandler.getRelatedElementHandler(userId, serverName, methodName);

            if (requestBody != null)
            {
                if (requestBody.getProperties() instanceof StakeholderProperties)
                {
                    StakeholderProperties properties = (StakeholderProperties) requestBody.getProperties();

                    handler.addStakeholder(userId,
                                           requestBody.getExternalSourceGUID(),
                                           requestBody.getExternalSourceName(),
                                           elementGUID,
                                           elementGUIDParameterName,
                                           stakeholderGUID,
                                           stakeholderGUIDParameterName,
                                           properties.getStakeholderRole(),
                                           properties.getEffectiveFrom(),
                                           properties.getEffectiveTo(),
                                           false,
                                           false,
                                           new Date(),
                                           methodName);
                }
                else if (requestBody.getProperties() == null)
                {
                    handler.addStakeholder(userId,
                                           requestBody.getExternalSourceGUID(),
                                           requestBody.getExternalSourceName(),
                                           elementGUID,
                                           elementGUIDParameterName,
                                           stakeholderGUID,
                                           stakeholderGUIDParameterName,
                                           null,
                                           null,
                                           null,
                                           false,
                                           false,
                                           new Date(),
                                           methodName);
                }
                else
                {
                    restExceptionHandler.handleInvalidPropertiesObject(StakeholderProperties.class.getName(), methodName);
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
     * Remove a "Stakeholder" relationship between two referenceables.
     *
     * @param serverName name of the service to route the request to.
     * @param userId             calling user
     * @param elementGUID        unique identifier of the element
     * @param stakeholderGUID    unique identifier of the stakeholder
     * @param requestBody external source identifiers
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public VoidResponse clearStakeholder(String                    serverName,
                                         String                    userId,
                                         String                    elementGUID,
                                         String                    stakeholderGUID,
                                         ExternalSourceRequestBody requestBody)
    {
        final String methodName                   = "clearStakeholder";
        final String elementGUIDParameterName     = "elementGUID";
        final String stakeholderGUIDParameterName = "stakeholderGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            ReferenceableHandler<RelatedElement> handler = instanceHandler.getRelatedElementHandler(userId, serverName, methodName);

            if (requestBody != null)
            {
                handler.removeStakeholder(userId,
                                          requestBody.getExternalSourceGUID(),
                                          requestBody.getExternalSourceName(),
                                          elementGUID,
                                          elementGUIDParameterName,
                                          stakeholderGUID,
                                          stakeholderGUIDParameterName,
                                          false,
                                          false,
                                          new Date(),
                                          methodName);
            }
            else
            {
                handler.removeStakeholder(userId,
                                          null,
                                          null,
                                          elementGUID,
                                          elementGUIDParameterName,
                                          stakeholderGUID,
                                          stakeholderGUIDParameterName,
                                          false,
                                          false,
                                          new Date(),
                                          methodName);
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
     * Retrieve the stakeholder elements linked via the "Stakeholder"  relationship between two referenceables.
     *
     * @param serverName name of the service to route the request to.
     * @param userId             calling user
     * @param elementGUID        unique identifier of the element
     * @param startFrom  index of the list to start from (0 for start)
     * @param pageSize   maximum number of elements to return.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public  RelatedElementListResponse getStakeholders(String serverName,
                                                       String userId,
                                                       String elementGUID,
                                                       int    startFrom,
                                                       int    pageSize)
    {
        final String methodName        = "getStakeholders";
        final String guidPropertyName  = "elementGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        RelatedElementListResponse response = new RelatedElementListResponse();
        AuditLog                   auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            ReferenceableHandler<RelatedElement> handler = instanceHandler.getRelatedElementHandler(userId, serverName, methodName);

            response.setElementList(handler.getStakeholders(userId,
                                                            elementGUID,
                                                            guidPropertyName,
                                                            OpenMetadataAPIMapper.REFERENCEABLE_TYPE_NAME,
                                                            OpenMetadataAPIMapper.REFERENCEABLE_TYPE_NAME,
                                                            startFrom,
                                                            pageSize,
                                                            false,
                                                            false,
                                                            new Date(),
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
     * Retrieve the elements commissioned by a stakeholder, linked via the "Stakeholder"  relationship between two referenceables.
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param stakeholderGUID unique identifier of the stakeholder
     * @param startFrom  index of the list to start from (0 for start)
     * @param pageSize   maximum number of elements to return.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public  RelatedElementListResponse getStakeholderCommissionedElements(String serverName,
                                                                          String userId,
                                                                          String stakeholderGUID,
                                                                          int    startFrom,
                                                                          int    pageSize)
    {
        final String methodName        = "getStakeholderCommissionedElements";
        final String guidPropertyName  = "stakeholderGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        RelatedElementListResponse response = new RelatedElementListResponse();
        AuditLog                   auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            ReferenceableHandler<RelatedElement> handler = instanceHandler.getRelatedElementHandler(userId, serverName, methodName);

            response.setElementList(handler.getCommissionedByStakeholder(userId,
                                                                         stakeholderGUID,
                                                                         guidPropertyName,
                                                                         OpenMetadataAPIMapper.REFERENCEABLE_TYPE_NAME,
                                                                         OpenMetadataAPIMapper.REFERENCEABLE_TYPE_NAME,
                                                                         startFrom,
                                                                         pageSize,
                                                                         false,
                                                                         false,
                                                                         new Date(),
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
     * Create a "ResourceList" relationship between a consuming element and an element that represents resources.
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param elementGUID unique identifier of the element
     * @param resourceGUID unique identifier of the resource
     * @param requestBody relationship properties
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public VoidResponse setupResource(String                  serverName,
                                      String                  userId,
                                      String                  elementGUID,
                                      String                  resourceGUID,
                                      RelationshipRequestBody requestBody)
    {
        final String methodName                = "setupResource";
        final String elementGUIDParameterName  = "elementGUID";
        final String resourceGUIDParameterName = "resourceGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            ReferenceableHandler<RelatedElement> handler = instanceHandler.getRelatedElementHandler(userId, serverName, methodName);

            if (requestBody != null)
            {
                if (requestBody.getProperties() instanceof ResourceListProperties)
                {
                    ResourceListProperties properties = (ResourceListProperties) requestBody.getProperties();

                    handler.saveResourceListMember(userId,
                                                   requestBody.getExternalSourceGUID(),
                                                   requestBody.getExternalSourceName(),
                                                   elementGUID,
                                                   elementGUIDParameterName,
                                                   resourceGUID,
                                                   resourceGUIDParameterName,
                                                   properties.getResourceUse(),
                                                   properties.getWatchResource(),
                                                   properties.getEffectiveFrom(),
                                                   properties.getEffectiveTo(),
                                                   false,
                                                   false,
                                                   new Date(),
                                                   methodName);
                }
                else if (requestBody.getProperties() == null)
                {
                    handler.saveResourceListMember(userId,
                                                   requestBody.getExternalSourceGUID(),
                                                   requestBody.getExternalSourceName(),
                                                   elementGUID,
                                                   elementGUIDParameterName,
                                                   resourceGUID,
                                                   resourceGUIDParameterName,
                                                   null,
                                                   false,
                                                   null,
                                                   null,
                                                   false,
                                                   false,
                                                   new Date(),
                                                   methodName);
                }
                else
                {
                    restExceptionHandler.handleInvalidPropertiesObject(ResourceListProperties.class.getName(), methodName);
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
     * Remove a "ResourceList" relationship between two referenceables.
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param elementGUID unique identifier of the element
     * @param resourceGUID unique identifier of the resource
     * @param requestBody external source identifiers
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public VoidResponse clearResource(String                    serverName,
                                      String                    userId,
                                      String                    elementGUID,
                                      String                    resourceGUID,
                                      ExternalSourceRequestBody requestBody)
    {
        final String methodName                = "clearResource";
        final String elementGUIDParameterName  = "elementGUID";
        final String resourceGUIDParameterName = "resourceGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            ReferenceableHandler<RelatedElement> handler = instanceHandler.getRelatedElementHandler(userId, serverName, methodName);

            if (requestBody != null)
            {
                handler.removeResourceListMember(userId,
                                                 requestBody.getExternalSourceGUID(),
                                                 requestBody.getExternalSourceName(),
                                                 elementGUID,
                                                 elementGUIDParameterName,
                                                 resourceGUID,
                                                 resourceGUIDParameterName,
                                                 false,
                                                 false,
                                                 new Date(),
                                                 methodName);
            }
            else
            {
                handler.removeResourceListMember(userId,
                                                 null,
                                                 null,
                                                 elementGUID,
                                                 elementGUIDParameterName,
                                                 resourceGUID,
                                                 resourceGUIDParameterName,
                                                 false,
                                                 false,
                                                 new Date(),
                                                 methodName);
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
     * Retrieve the list of resources assigned to an element via the "ResourceList" relationship between two referenceables.
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param elementGUID unique identifier of the element
     * @param startFrom  index of the list to start from (0 for start)
     * @param pageSize   maximum number of elements to return.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public  RelatedElementListResponse getResourceList(String serverName,
                                                       String userId,
                                                       String elementGUID,
                                                       int    startFrom,
                                                       int    pageSize)
    {
        final String methodName        = "getResourceList";
        final String guidPropertyName  = "elementGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        RelatedElementListResponse response = new RelatedElementListResponse();
        AuditLog                   auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            ReferenceableHandler<RelatedElement> handler = instanceHandler.getRelatedElementHandler(userId, serverName, methodName);

            response.setElementList(handler.getResourceList(userId,
                                                            elementGUID,
                                                            guidPropertyName,
                                                            OpenMetadataAPIMapper.REFERENCEABLE_TYPE_NAME,
                                                            startFrom,
                                                            pageSize,
                                                            false,
                                                            false,
                                                            new Date(),
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
     * Retrieve the list of elements assigned to a resource via the "ResourceList" relationship between two referenceables.
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param resourceGUID unique identifier of the resource
     * @param startFrom  index of the list to start from (0 for start)
     * @param pageSize   maximum number of elements to return.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public  RelatedElementListResponse getSupportedByResource(String serverName,
                                                              String userId,
                                                              String resourceGUID,
                                                              int    startFrom,
                                                              int    pageSize)
    {
        final String methodName        = "getSupportedByResource";
        final String guidPropertyName  = "resourceGUID";


        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        RelatedElementListResponse response = new RelatedElementListResponse();
        AuditLog                   auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            ReferenceableHandler<RelatedElement> handler = instanceHandler.getRelatedElementHandler(userId, serverName, methodName);

            response.setElementList(handler.getSupportedByResource(userId,
                                                                   resourceGUID,
                                                                   guidPropertyName,
                                                                   OpenMetadataAPIMapper.REFERENCEABLE_TYPE_NAME,
                                                                   startFrom,
                                                                   pageSize,
                                                                   false,
                                                                   false,
                                                                   new Date(),
                                                                   methodName));
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }
}
