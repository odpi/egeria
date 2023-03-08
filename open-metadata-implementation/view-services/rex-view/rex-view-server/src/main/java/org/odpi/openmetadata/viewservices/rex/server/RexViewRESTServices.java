/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project */
/* Copyright Contributors to the ODPi Egeria category. */
package org.odpi.openmetadata.viewservices.rex.server;


import org.odpi.openmetadata.commonservices.ffdc.RESTCallLogger;
import org.odpi.openmetadata.commonservices.ffdc.RESTCallToken;
import org.odpi.openmetadata.commonservices.ffdc.RESTExceptionHandler;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.viewservices.rex.api.ffdc.RexExceptionHandler;
import org.odpi.openmetadata.viewservices.rex.api.ffdc.RexViewErrorCode;
import org.odpi.openmetadata.viewservices.rex.api.ffdc.RexViewServiceException;
import org.odpi.openmetadata.viewservices.rex.api.properties.*;
import org.odpi.openmetadata.viewservices.rex.api.rest.*;
import org.odpi.openmetadata.viewservices.rex.handlers.RexViewHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import java.util.Date;
import java.util.List;
import java.util.Map;


/**
 * The RexViewRESTServices provides the org.odpi.openmetadata.viewservices.rex.services implementation of the Repository Explorer Open Metadata
 * View Service (OMVS). This interface provides view interfaces for enterprise architects.
 */

public class RexViewRESTServices {

    protected static final RexViewInstanceHandler instanceHandler       = new RexViewInstanceHandler();

    private static final RESTExceptionHandler     restExceptionHandler  = new RESTExceptionHandler();

    private static final RESTCallLogger           restCallLogger        = new RESTCallLogger(LoggerFactory.getLogger(RexViewRESTServices.class),
                                                                                       instanceHandler.getServiceName());

    private static final Logger             log                   = LoggerFactory.getLogger(RexViewRESTServices.class);

    private static String className = RexViewRESTServices.class.getName();

    /**
     * Default constructor
     */
    public RexViewRESTServices() {

    }


    /**
     * Retrieve platform origin
     *
     * @param serverName    name of the local view server.
     * @param userId        userId under which the request is performed
     * @return response     the list of resource endpoints configured for the view service
     *
     */
    public RexResourceEndpointListResponse getResourceEndpointList(String serverName, String userId)
    {

        final String methodName = "getResourceEndpointList";

        RexResourceEndpointListResponse response = new RexResourceEndpointListResponse();

        AuditLog auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            RexViewHandler handler = instanceHandler.getRexViewHandler(userId, serverName, methodName);

            Map<String, List<ResourceEndpoint>> lists = handler.getResourceEndpoints(userId, methodName);
            List<ResourceEndpoint> platformList = null;
            List<ResourceEndpoint> serverList = null;
            if (lists != null)
            {
                platformList = lists.get("platformList");
                serverList = lists.get("serverList");
            }
            response.setPlatformList(platformList);
            response.setServerList(serverList);

        }
        catch (InvalidParameterException error)
        {
            restExceptionHandler.captureInvalidParameterException(response, error);
        }
        catch (PropertyServerException error)
        {
            restExceptionHandler.capturePropertyServerException(response, error);
        }
        catch (UserNotAuthorizedException error)
        {
            restExceptionHandler.captureUserNotAuthorizedException(response, error);
        }
        catch (Exception exception)
        {
            restExceptionHandler.captureExceptions(response, exception, methodName, auditLog);
        }

        log.debug("Returning from method: " + methodName + " with response: " + response.toString());

        return response;

    }



    /**
     * Load types
     *
     * @param serverName    name of the local view server.
     * @param userId        userId under which the request is performed
     * @param requestBody   request body
     * @return response     the repository's type information or exception information
     */

    public TypeExplorerResponse getTypeExplorer(String serverName, String userId, RexTypesRequestBody requestBody)
    {

        final String methodName = "getTypeExplorer";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        TypeExplorerResponse response = new TypeExplorerResponse();

        if (requestBody != null)
        {

            AuditLog auditLog = null;
            RexViewHandler handler = null;

            try
            {
                auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

                handler = instanceHandler.getRexViewHandler(userId, serverName, methodName);

            }
            catch (InvalidParameterException error)
            {
                restExceptionHandler.captureInvalidParameterException(response, error);
            }
            catch (PropertyServerException error)
            {
                restExceptionHandler.capturePropertyServerException(response, error);
            }
            catch (UserNotAuthorizedException error)
            {
                restExceptionHandler.captureUserNotAuthorizedException(response, error);
            }
            catch (Exception exception) {
                restExceptionHandler.captureExceptions(response, exception, methodName, auditLog);
            }

            /*
             * Attempt to retrieve the type information
             */
            try
            {
                response.setTypeExplorer(handler.getTypeExplorer(userId,
                                                                 requestBody.getServerName(),
                                                                 requestBody.getPlatformName(),
                                                                 requestBody.getEnterpriseOption(),
                                                                 methodName));
            }
            catch (RexViewServiceException exception)
            {
                RexExceptionHandler.captureCheckedException( response, exception, exception.getClass().getName());
            }
            catch (Exception exception) {
                restExceptionHandler.captureExceptions(response, exception, methodName, auditLog);
            }
        }

        else
        {
            /*
             * Raise (and immediately capture) a RexViewServicesException
             */
            RexViewServiceException exception = new RexViewServiceException(RexViewErrorCode.VIEW_SERVICE_REQUEST_BODY_MISSING.getMessageDefinition(),
                                                                        this.getClass().getName(),
                                                                        methodName);

            RexExceptionHandler.captureCheckedException( response, exception, exception.getClass().getName());
        }


        log.debug("Returning from method: " + methodName + " with response: " + response.toString());

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }

    /**
     * Get entity by GUID
     *
     * @param serverName    name of the local view server.
     * @param userId        userId under which the request is performed
     * @param requestBody   request body
     * @return the created category.
     *
     * <ul>
     * <li> InvalidParameterException            one of the parameters is null or invalid.
     * </ul>
     */

    public RexEntityDetailResponse getEntity(String               serverName,
                                             String               userId,
                                             RexEntityRequestBody requestBody)
    {

        final String methodName = "getEntity";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        RexEntityDetailResponse response = new RexEntityDetailResponse();

        /*
         * If there is no requestBody then do not proceed - just raise an exception
         */
        if (requestBody != null)
        {

            AuditLog auditLog = null;
            RexViewHandler handler = null;

            try
            {
                auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

                handler = instanceHandler.getRexViewHandler(userId, serverName, methodName);

            }
            catch (InvalidParameterException exception)
            {
                restExceptionHandler.captureInvalidParameterException(response, exception);
            }
            catch (PropertyServerException exception)
            {
                restExceptionHandler.capturePropertyServerException(response, exception);
            }
            catch (UserNotAuthorizedException exception)
            {
                restExceptionHandler.captureUserNotAuthorizedException(response, exception);
            }
            catch (Exception exception)
            {
                restExceptionHandler.captureExceptions(response, exception, methodName, auditLog);
            }

            /*
             * Attempt to retrieve the entity
             */


            try {


                if (handler == null) {

                    throw new RexViewServiceException(RexViewErrorCode.HANDLER_INVALID.getMessageDefinition(methodName),
                                                      className,
                                                      methodName);
                } else {
                    long asOfTime = requestBody.getAsOfTime();
                    Date asOfTimeDate = null;
                    if (asOfTime != 0) {
                        asOfTimeDate = new Date(asOfTime);
                        if (asOfTimeDate.after(new Date())) {
                            // we do not support future dates
                            throw new RexViewServiceException(RexViewErrorCode.INVALID_AS_OF_DATETIME.getMessageDefinition(methodName),
                                                              className,
                                                              methodName);
                        }
                    }

                    response.setExpandedEntityDetail(handler.getEntity(userId,
                                                                       requestBody.getServerName(),
                                                                       requestBody.getPlatformName(),
                                                                       requestBody.getEnterpriseOption(),
                                                                       requestBody.getEntityGUID(),
                                                                       asOfTimeDate,
                                                                       methodName));
                }
            }
            catch (RexViewServiceException exception)
            {
                RexExceptionHandler.captureCheckedException(response, exception, exception.getClass().getName());
            }
            catch (Exception exception)
            {
                restExceptionHandler.captureExceptions(response, exception, methodName, auditLog);
            }

        }

        else
        {
            /*
             * Raise (and immediately capture) a RexViewServicesException
             */
            RexViewServiceException exception = new RexViewServiceException(RexViewErrorCode.VIEW_SERVICE_REQUEST_BODY_MISSING.getMessageDefinition(),
                                                                        this.getClass().getName(),
                                                                        methodName);

            RexExceptionHandler.captureCheckedException(response, exception, exception.getClass().getName());
        }


        log.debug("Returning from method: " + methodName + " with response: " + response.toString());

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }

    /**
     * Get relationship by GUID
     *
     * @param serverName    name of the local view server.
     * @param userId        userId under which the request is performed
     * @param requestBody   request body
     * @return the created category.
     *
     * <ul>
     * <li> InvalidParameterException            one of the parameters is null or invalid.
     * </ul>
     */

    public RexRelationshipResponse getRelationship(String                     serverName,
                                                   String                     userId,
                                                   RexRelationshipRequestBody requestBody)
    {

        final String methodName = "getRelationship";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        RexRelationshipResponse response = new RexRelationshipResponse();

        if (requestBody != null)
        {
            AuditLog auditLog = null;
            RexViewHandler handler = null;

            try
            {
                auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

                handler = instanceHandler.getRexViewHandler(userId, serverName, methodName);
            }
            catch (InvalidParameterException exception)
            {
                restExceptionHandler.captureInvalidParameterException(response, exception);
            }
            catch (PropertyServerException exception)
            {
                restExceptionHandler.capturePropertyServerException(response, exception);
            }
            catch (UserNotAuthorizedException exception)
            {
                restExceptionHandler.captureUserNotAuthorizedException(response, exception);
            }
            catch (Exception exception)
            {
                restExceptionHandler.captureExceptions(response, exception, methodName, auditLog);
            }


            /*
             * Attempt to retrieve the relationship
             */
            try
            {
                if (handler == null) {

                    throw new RexViewServiceException(RexViewErrorCode.HANDLER_INVALID.getMessageDefinition(methodName),
                                                      className,
                                                      methodName);
                } else {
                    long asOfTime = requestBody.getAsOfTime();
                    Date asOfTimeDate = null;
                    if (asOfTime != 0) {
                        asOfTimeDate = new Date(asOfTime);
                        if (asOfTimeDate.after(new Date())) {
                            // we do not support future dates
                            throw new RexViewServiceException(RexViewErrorCode.INVALID_AS_OF_DATETIME.getMessageDefinition(methodName),
                                                              className,
                                                              methodName);
                        }
                    }

                    response.setExpandedRelationship(handler.getRelationship(userId,
                                                                             requestBody.getServerName(),
                                                                             requestBody.getPlatformName(),
                                                                             requestBody.getEnterpriseOption(),
                                                                             requestBody.getRelationshipGUID(),
                                                                             asOfTimeDate,
                                                                             methodName));
                }
            }
            catch (RexViewServiceException exception)
            {
                RexExceptionHandler.captureCheckedException(response, exception, exception.getClass().getName());
            }
            catch (Exception exception)
            {
                restExceptionHandler.captureExceptions(response, exception, methodName, auditLog);
            }
        }
        else
        {
            /*
             * Raise (and immediately capture) a RexViewServicesException
             */
            RexViewServiceException exception = new RexViewServiceException(RexViewErrorCode.VIEW_SERVICE_REQUEST_BODY_MISSING.getMessageDefinition(),
                                                                        this.getClass().getName(),
                                                                        methodName);

            RexExceptionHandler.captureCheckedException( response, exception, exception.getClass().getName());
        }


        log.debug("Returning from method: " + methodName + " with response: " + response.toString());

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }


    /**
     * Find entities using searchText
     *
     * @param serverName    name of the local view server.
     * @param userId        userId under which the request is performed
     * @param requestBody   request body
     * @return a RexSearchResponse containing a map of entity digests that match the search parameters.
     *         The map has type Map of String to RexEntityDigest, where the key is the entity's GUID
     *
     * <ul>
     * <li> InvalidParameterException            one of the parameters is null or invalid.
     * </ul>
     */

    public RexSearchResponse findEntities(String         serverName,
                                          String         userId,
                                          RexSearchBody  requestBody) {

        final String methodName = "findEntities";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        RexSearchResponse response = new RexSearchResponse();

        if (requestBody != null)
        {

            AuditLog auditLog = null;
            RexViewHandler handler = null;

            try
            {
                auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

                handler = instanceHandler.getRexViewHandler(userId, serverName, methodName);
            }
            catch (InvalidParameterException exception)
            {
                restExceptionHandler.captureInvalidParameterException(response, exception);
            }
            catch (PropertyServerException exception)
            {
                restExceptionHandler.capturePropertyServerException(response, exception);
            }
            catch (UserNotAuthorizedException exception)
            {
                restExceptionHandler.captureUserNotAuthorizedException(response, exception);
            }
            catch (Exception exception)
            {
                restExceptionHandler.captureExceptions(response, exception, methodName, auditLog);
            }

            try
            {
                if (handler == null) {

                    throw new RexViewServiceException(RexViewErrorCode.HANDLER_INVALID.getMessageDefinition(methodName),
                                                      className,
                                                      methodName);
                } else {
                    long asOfTime = requestBody.getAsOfTime();
                    Date asOfTimeDate = null;
                    if (asOfTime != 0) {
                        asOfTimeDate = new Date(asOfTime);
                        if (asOfTimeDate.after(new Date())) {
                            // we do not support future dates
                            throw new RexViewServiceException(RexViewErrorCode.INVALID_AS_OF_DATETIME.getMessageDefinition(methodName),
                                                              className,
                                                              methodName);
                        }
                    }
                    response.setEntities(handler.findEntities(userId,
                                                              requestBody.getServerName(),
                                                              requestBody.getPlatformName(),
                                                              requestBody.getEnterpriseOption(),
                                                              requestBody.getSearchText(),
                                                              requestBody.getTypeName(),
                                                              requestBody.getClassificationNames(),
                                                              asOfTimeDate,
                                                              methodName));

                    response.setSearchCategory("Entity");
                    response.setSearchText(requestBody.getSearchText());
                    response.setServerName(requestBody.getServerName());

                }
            }
            catch (RexViewServiceException exception)
            {
                RexExceptionHandler.captureCheckedException(response, exception, exception.getClass().getName());
            }
            catch (Exception exception)
            {
                restExceptionHandler.captureExceptions(response, exception, methodName, auditLog);
            }
        }
        else
        {
            /*
             * Raise (and immediately capture) a RexViewServicesException
             */
            RexViewServiceException exception = new RexViewServiceException(RexViewErrorCode.VIEW_SERVICE_REQUEST_BODY_MISSING.getMessageDefinition(),
                                                                        this.getClass().getName(),
                                                                        methodName);

            RexExceptionHandler.captureCheckedException(response, exception, exception.getClass().getName());
        }


        log.debug("Returning from method: " + methodName + " with response: " + response.toString());

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }


    /**
     * Find relationships using searchText
     *
     * @param serverName    name of the local view server.
     * @param userId        userId under which the request is performed
     * @param requestBody   request body
     * @return a RexSearchResponse containing a map of relationship digests that match the search parameters.
     *         The map has type Map of String to RexRelationshipDigest, where the key is the relationship's GUID
     *
     * <ul>
     * <li> InvalidParameterException            one of the parameters is null or invalid.
     * </ul>
     */

    public RexSearchResponse findRelationships(String         serverName,
                                               String         userId,
                                               RexSearchBody  requestBody)
    {

        final String methodName = "findRelationships";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        RexSearchResponse response = new RexSearchResponse();

        if (requestBody != null)
        {

            AuditLog auditLog = null;
            RexViewHandler handler = null;

            try
            {
                auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

                handler = instanceHandler.getRexViewHandler(userId, serverName, methodName);
            }
            catch (InvalidParameterException exception)
            {
                restExceptionHandler.captureInvalidParameterException(response, exception);
            }
            catch (PropertyServerException exception)
            {
                restExceptionHandler.capturePropertyServerException(response, exception);
            }
            catch (UserNotAuthorizedException exception)
            {
                restExceptionHandler.captureUserNotAuthorizedException(response, exception);
            }
            catch (Exception exception)
            {
                restExceptionHandler.captureExceptions(response, exception, methodName, auditLog);
            }

            try
            {
                if (handler == null) {

                    throw new RexViewServiceException(RexViewErrorCode.HANDLER_INVALID.getMessageDefinition(methodName),
                                                      className,
                                                      methodName);
                } else {
                    long asOfTime = requestBody.getAsOfTime();
                    Date asOfTimeDate = null;
                    if (asOfTime != 0) {
                        asOfTimeDate = new Date(asOfTime);
                        if (asOfTimeDate.after(new Date())) {
                            // we do not support future dates
                            throw new RexViewServiceException(RexViewErrorCode.INVALID_AS_OF_DATETIME.getMessageDefinition(methodName),
                                                              className,
                                                              methodName);
                        }
                    }
                    Map<String, RexRelationshipAndEntitiesDigest> superDigests = handler.findRelationships(userId,
                                                                                                           requestBody.getServerName(),
                                                                                                           requestBody.getPlatformName(),
                                                                                                           requestBody.getEnterpriseOption(),
                                                                                                           requestBody.getSearchText(),
                                                                                                           requestBody.getTypeName(),
                                                                                                           asOfTimeDate,
                                                                                                           methodName);

                    response.setRelationships(superDigests);


                    response.setSearchCategory("Relationship");
                    response.setSearchText(requestBody.getSearchText());
                    response.setServerName(requestBody.getServerName());
                }
            }
            catch (RexViewServiceException exception)
            {
                RexExceptionHandler.captureCheckedException(response, exception, exception.getClass().getName());
            }
            catch (Exception exception)
            {
                restExceptionHandler.captureExceptions(response, exception, methodName, auditLog);
            }
        }
        else
        {
            /*
             * Raise (and immediately capture) a RexViewServicesException
             */
            RexViewServiceException exception = new RexViewServiceException(RexViewErrorCode.VIEW_SERVICE_REQUEST_BODY_MISSING.getMessageDefinition(),
                                                                        this.getClass().getName(),
                                                                        methodName);

            RexExceptionHandler.captureCheckedException(response, exception, exception.getClass().getName());
        }


        log.debug("Returning from method: " + methodName + " with response: " + response.toString());

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }


    /**
     *  This method retrieves the neighborhood around a starting entity for pre=traversal
     *  <p>
     *  When exploring an entity neighborhood we return an InstanceGraph which contains
     *  te entities and relationships that were traversed.
     *  <p>
     *  The method used is POST because the parameters supplied by the UI to the VS are conveyed in
     *  the request body.
     *
     *
     * @param serverName   name of the server running the view-service.
     * @param userId       user account under which to conduct operation.
     * @param requestBody  request body containing parameters to formulate repository request
     * @return response object containing the InstanceGraph for the traersal or exception information
     *
     * <ul>
     * <li> InvalidParameterException            one of the parameters is null or invalid.
     * </ul>
     */

    public RexPreTraversalResponse preTraversal(String                  serverName,
                                                String                  userId,
                                                RexTraversalRequestBody requestBody)
    {

        final String methodName = "preTraversal";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        RexPreTraversalResponse response = new RexPreTraversalResponse();

        if (requestBody != null)
        {

            AuditLog auditLog = null;
            RexViewHandler handler = null;

            try
            {
                auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

                handler = instanceHandler.getRexViewHandler(userId, serverName, methodName);
            }
            catch (InvalidParameterException exception)
            {
                restExceptionHandler.captureInvalidParameterException(response, exception);
            }
            catch (PropertyServerException exception)
            {
                restExceptionHandler.capturePropertyServerException(response, exception);
            }
            catch (UserNotAuthorizedException exception)
            {
                restExceptionHandler.captureUserNotAuthorizedException(response, exception);
            }
            catch (Exception exception)
            {
                restExceptionHandler.captureExceptions(response, exception, methodName, auditLog);
            }

            try
            {

                RexPreTraversal preTraversal = handler.preTraversal(userId,
                                                                    requestBody.getServerName(),
                                                                    requestBody.getPlatformName(),
                                                                    requestBody.getEnterpriseOption(),
                                                                    requestBody.getEntityGUID(),
                                                                    requestBody.getDepth(),
                                                                    requestBody.getAsOfTime(),
                                                                    methodName);

                if (preTraversal != null)
                {
                    response.setRexPreTraversal(preTraversal);
                }

            }
            catch (RexViewServiceException exception)
            {
                RexExceptionHandler.captureCheckedException(response, exception, exception.getClass().getName());
            }
            catch (Exception exception)
            {
                restExceptionHandler.captureExceptions(response, exception, methodName, auditLog);
            }
        }

        else
        {
            /*
             * Raise (and immediately capture) a RexViewServicesException
             */
            RexViewServiceException exception = new RexViewServiceException(RexViewErrorCode.VIEW_SERVICE_REQUEST_BODY_MISSING.getMessageDefinition(),
                                                                        this.getClass().getName(),
                                                                        methodName);

            RexExceptionHandler.captureCheckedException(response, exception, exception.getClass().getName());
        }

        log.debug("Returning from method: " + methodName + " with response: " + response.toString());

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }


    /**
     *  This method retrieves the neighborhood around a starting entity.
     *  <p>
     *  When exploring an entity neighborhood we return an InstanceGraph which contains
     *  te entities and relationships that were traversed.
     *  <p>
     *  The method used is POST because the parameters supplied by the UI to the VS are conveyed in
     *  the request body.
     *
     *
     * @param serverName   name of the server running the view-service.
     * @param userId       user account under which to conduct operation.
     * @param requestBody  request body containing parameters to formulate repository request
     * @return response object containing the InstanceGraph for the traersal or exception information
     *
     * <ul>
     * <li> InvalidParameterException            one of the parameters is null or invalid.
     * </ul>
     */

    public RexTraversalResponse traversal(String                  serverName,
                                             String                  userId,
                                             RexTraversalRequestBody requestBody)
    {

        final String methodName = "traversal";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        RexTraversalResponse response = new RexTraversalResponse();

        if (requestBody != null)
        {

            AuditLog auditLog = null;
            RexViewHandler handler = null;

            try
            {
                auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

                handler = instanceHandler.getRexViewHandler(userId, serverName, methodName);
            }
            catch (InvalidParameterException exception)
            {
                restExceptionHandler.captureInvalidParameterException(response, exception);
            }
            catch (PropertyServerException exception)
            {
                restExceptionHandler.capturePropertyServerException(response, exception);
            }
            catch (UserNotAuthorizedException exception)
            {
                restExceptionHandler.captureUserNotAuthorizedException(response, exception);
            }
            catch (Exception exception)
            {
                restExceptionHandler.captureExceptions(response, exception, methodName, auditLog);
            }

            try {

                RexTraversal traversal = handler.traversal(userId,
                                                           requestBody.getServerName(),
                                                           requestBody.getPlatformName(),
                                                           requestBody.getEnterpriseOption(),
                                                           requestBody.getEntityGUID(),
                                                           requestBody.getDepth(),
                                                           requestBody.getEntityTypeGUIDs(),
                                                           requestBody.getRelationshipTypeGUIDs(),
                                                           requestBody.getClassificationNames(),
                                                           requestBody.getAsOfTime(),
                                                           methodName);

                if (traversal != null)
                {
                    response.setRexTraversal(traversal);
                }

            }
            catch (RexViewServiceException exception)
            {
                RexExceptionHandler.captureCheckedException(response, exception, exception.getClass().getName());
            }
            catch (Exception exception)
            {
                restExceptionHandler.captureExceptions(response, exception, methodName, auditLog);
            }
        }
        else
        {
            /*
             * Raise (and immediately capture) a RexViewServicesException
             */
            RexViewServiceException exception = new RexViewServiceException(RexViewErrorCode.VIEW_SERVICE_REQUEST_BODY_MISSING.getMessageDefinition(),
                                                                        this.getClass().getName(),
                                                                        methodName);

            RexExceptionHandler.captureCheckedException(response, exception, exception.getClass().getName());
        }


        log.debug("Returning from method: " + methodName + " with response: " + response.toString());

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }




}
