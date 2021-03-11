/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria category. */
package org.odpi.openmetadata.viewservices.dino.server;


import org.odpi.openmetadata.adminservices.rest.ServerTypeClassificationSummary;
import org.odpi.openmetadata.commonservices.ffdc.RESTCallLogger;
import org.odpi.openmetadata.commonservices.ffdc.RESTCallToken;
import org.odpi.openmetadata.commonservices.ffdc.RESTExceptionHandler;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.viewservices.dino.api.ffdc.DinoExceptionHandler;
import org.odpi.openmetadata.viewservices.dino.api.ffdc.DinoViewErrorCode;
import org.odpi.openmetadata.viewservices.dino.api.ffdc.DinoViewServiceException;
import org.odpi.openmetadata.viewservices.dino.api.properties.RequestSummary;
import org.odpi.openmetadata.viewservices.dino.api.properties.ResourceEndpoint;
import org.odpi.openmetadata.viewservices.dino.api.rest.*;
import org.odpi.openmetadata.viewservices.dino.handlers.DinoViewHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;


/**
 * The DinoViewRESTServices provides the implementation of the Dino Open Metadata View Service (OMVS).
 * This interface provides view interfaces for infrastructure and ops users.
 */

public class DinoViewRESTServices {

    protected static DinoViewInstanceHandler instanceHandler       = new DinoViewInstanceHandler();

    private static RESTExceptionHandler     restExceptionHandler  = new RESTExceptionHandler();

    private static RESTCallLogger           restCallLogger        = new RESTCallLogger(LoggerFactory.getLogger(DinoViewRESTServices.class),
                                                                                       instanceHandler.getServiceName());

    private static final Logger             log                   = LoggerFactory.getLogger(DinoViewRESTServices.class);


    /**
     * Default constructor
     */
    public DinoViewRESTServices() {

    }

    /**
     * Retrieve platform origin
     *
     * @param serverName    name of the local view server.
     * @param userId        userId under which the request is performed
     * @return response     the list of resource endpoints configured for the view service
     *
     */
    public DinoResourceEndpointListResponse getResourceEndpointList(String serverName, String userId)
    {

        final String methodName = "getResourceEndpointList";

        DinoResourceEndpointListResponse response = new DinoResourceEndpointListResponse();

        AuditLog auditLog = null;
        DinoViewHandler handler;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            handler = instanceHandler.getDinoViewHandler(userId, serverName, methodName);

            if (handler == null)
            {
                throw new DinoViewServiceException(DinoViewErrorCode.COULD_NOT_CREATE_HANDLER.getMessageDefinition(methodName),
                                                   this.getClass().getName(),
                                                   methodName);

            }

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
        catch (DinoViewServiceException exception)
        {
            DinoExceptionHandler.captureCheckedException(response, exception, exception.getClass().getName());
        }
        catch (Exception exception)
        {
            restExceptionHandler.captureThrowable(response, exception, methodName, auditLog);
        }


        log.debug("Returning from method: " + methodName + " with response: " + response.toString());

        return response;
    }

    /**
     * Retrieve platform overview - this is an overview of multiple aspects of the platform for when it becomes the focus.
     *
     * @param serverName    name of the local view server.
     * @param userId        userId under which the request is performed
     * @param requestBody   body containing platformName of the platform to be queried.
     * @return response     the platform overview.
     *
     */
    public DinoPlatformOverviewResponse platformGetOverview(String serverName, String userId, DinoPlatformRequestBody requestBody) {

        final String methodName = "platformGetOverview";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        DinoPlatformOverviewResponse response = new DinoPlatformOverviewResponse();

        if (requestBody != null)
        {

            // The serverName parameter to the RequestSummary is the target server not the server running the VS
            RequestSummary request = new RequestSummary(requestBody.getPlatformName(), null, null, methodName);
            response.setRequestSummary(request);

            AuditLog auditLog = null;
            DinoViewHandler handler;


            try
            {
                auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

                handler = instanceHandler.getDinoViewHandler(userId, serverName, methodName);

                if (handler == null)
                {
                    throw new DinoViewServiceException(DinoViewErrorCode.COULD_NOT_CREATE_HANDLER.getMessageDefinition(methodName),
                                                       this.getClass().getName(),
                                                       methodName);

                }

                response.setPlatformOverview(handler.platformGetOverview(userId,
                                                                         requestBody.getPlatformName(),
                                                                         methodName));
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
            catch (DinoViewServiceException exception)
            {
                DinoExceptionHandler.captureCheckedException(response, exception, exception.getClass().getName());
            }
            catch (Exception exception)
            {
                restExceptionHandler.captureThrowable(response, exception, methodName, auditLog);
            }

        }
        else
        {
            /*
             * Raise (and immediately capture) a RexViewServicesException
             */
            DinoViewServiceException exception = new DinoViewServiceException(DinoViewErrorCode.VIEW_SERVICE_REQUEST_BODY_MISSING.getMessageDefinition(),
                                                                          this.getClass().getName(),
                                                                          methodName);

            DinoExceptionHandler.captureCheckedException( response, exception, exception.getClass().getName());
        }

        log.debug("Returning from method: " + methodName + " with response: " + response.toString());

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }



    /**
     * Retrieve platform origin
     *
     * @param serverName    name of the local view server.
     * @param userId        userId under which the request is performed
     * @param requestBody   body containing platformName of the platform to be queried.
     * @return response     the origin string for the platform.
     *
     */
    public DinoStringResponse platformGetOrigin(String serverName, String userId, DinoPlatformRequestBody requestBody)
    {

        final String methodName = "platformGetOrigin";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        DinoStringResponse response = new DinoStringResponse();

        if (requestBody != null)
        {

            // The serverName parameter to the RequestSummary is the target server not the server running the VS
            RequestSummary request = new RequestSummary(requestBody.getPlatformName(), null, null, methodName);
            response.setRequestSummary(request);


            AuditLog auditLog = null;
            DinoViewHandler handler;

            try
            {
                auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

                handler = instanceHandler.getDinoViewHandler(userId, serverName, methodName);

                if (handler == null)
                {
                    throw new DinoViewServiceException(DinoViewErrorCode.COULD_NOT_CREATE_HANDLER.getMessageDefinition(methodName),
                                                       this.getClass().getName(),
                                                       methodName);

                }

                response.setString(handler.platformGetOrigin(userId,
                                                             requestBody.getPlatformName(),
                                                             methodName));
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
            catch (DinoViewServiceException exception)
            {
                DinoExceptionHandler.captureCheckedException(response, exception, exception.getClass().getName());
            }
            catch (Exception exception)
            {
                restExceptionHandler.captureThrowable(response, exception, methodName, auditLog);
            }

        }
        else
        {
            /*
             * Raise (and immediately capture) a RexViewServicesException
             */
            DinoViewServiceException exception = new DinoViewServiceException(DinoViewErrorCode.VIEW_SERVICE_REQUEST_BODY_MISSING.getMessageDefinition(),
                                                                          this.getClass().getName(),
                                                                          methodName);

            DinoExceptionHandler.captureCheckedException( response, exception, exception.getClass().getName());
        }

        log.debug("Returning from method: " + methodName + " with response: " + response.toString());

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }



    /**
     * Retrieve active servers for platform
     *
     * @param serverName    name of the local view server.
     * @param userId        userId under which the request is performed
     * @param requestBody   body containing platformName and platformRootURL of the platform to be queried.
     * @return response     the list of servers that are active on the platform
     *
     */
    public DinoServerListResponse platformGetActiveServers(String serverName, String userId, DinoPlatformRequestBody requestBody) {

        final String methodName = "platformGetActiveServers";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        DinoServerListResponse response = new DinoServerListResponse();

        if (requestBody != null)
        {

            // The serverName parameter to the RequestSummary is the target server not the server running the VS
            RequestSummary request = new RequestSummary(requestBody.getPlatformName(), null, null, methodName);
            response.setRequestSummary(request);


            AuditLog auditLog = null;
            DinoViewHandler handler;

            try
            {
                auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

                handler = instanceHandler.getDinoViewHandler(userId, serverName, methodName);

                if (handler == null)
                {
                    throw new DinoViewServiceException(DinoViewErrorCode.COULD_NOT_CREATE_HANDLER.getMessageDefinition(methodName),
                                                       this.getClass().getName(),
                                                       methodName);

                }

                response.setServerList(handler.platformGetActiveServerList(userId,
                                                                           requestBody.getPlatformName(),
                                                                           methodName));
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
            catch (DinoViewServiceException exception)
            {
                DinoExceptionHandler.captureCheckedException(response, exception, exception.getClass().getName());
            }
            catch (Exception exception)
            {
                restExceptionHandler.captureThrowable(response, exception, methodName, auditLog);
            }


        }
        else
        {
            /*
             * Raise (and immediately capture) a RexViewServicesException
             */
            DinoViewServiceException exception = new DinoViewServiceException(DinoViewErrorCode.VIEW_SERVICE_REQUEST_BODY_MISSING.getMessageDefinition(),
                                                                          this.getClass().getName(),
                                                                          methodName);

            DinoExceptionHandler.captureCheckedException( response, exception, exception.getClass().getName());
        }

        log.debug("Returning from method: " + methodName + " with response: " + response.toString());

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }


    /**
     * Retrieve known servers for platform
     *
     * @param serverName    name of the local view server.
     * @param userId        userId under which the request is performed
     * @param requestBody   body containing platformName and platformRootURL of the platform to be queried.
     * @return response     the list of servers that are active on the platform
     *
     */
    public DinoServerListResponse platformGetKnownServers(String serverName, String userId, DinoPlatformRequestBody requestBody) {

        final String methodName = "platformGetKnownServers";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        DinoServerListResponse response = new DinoServerListResponse();

        if (requestBody != null)
        {

            // The serverName parameter to the RequestSummary is the target server not the server running the VS
            RequestSummary request = new RequestSummary(requestBody.getPlatformName(), null, null, methodName);
            response.setRequestSummary(request);


            AuditLog auditLog = null;
            DinoViewHandler handler;

            try
            {
                auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

                handler = instanceHandler.getDinoViewHandler(userId, serverName, methodName);

                if (handler == null)
                {
                    throw new DinoViewServiceException(DinoViewErrorCode.COULD_NOT_CREATE_HANDLER.getMessageDefinition(methodName),
                                                       this.getClass().getName(),
                                                       methodName);

                }

                response.setServerList(handler.platformGetKnownServerList(userId,
                                                                          requestBody.getPlatformName(),
                                                                          methodName));

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
            catch (DinoViewServiceException exception)
            {
                DinoExceptionHandler.captureCheckedException(response, exception, exception.getClass().getName());
            }
            catch (Exception exception)
            {
                restExceptionHandler.captureThrowable(response, exception, methodName, auditLog);
            }

        }
        else
        {
            /*
             * Raise (and immediately capture) a RexViewServicesException
             */
            DinoViewServiceException exception = new DinoViewServiceException(DinoViewErrorCode.VIEW_SERVICE_REQUEST_BODY_MISSING.getMessageDefinition(),
                                                                          this.getClass().getName(),
                                                                          methodName);

            DinoExceptionHandler.captureCheckedException( response, exception, exception.getClass().getName());
        }

        log.debug("Returning from method: " + methodName + " with response: " + response.toString());

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }


    /**
     * Retrieve access services for platform
     *
     * @param serverName    name of the local view server.
     * @param userId        userId under which the request is performed
     * @param requestBody   body containing platformName and platformRootURL of the platform to be queried.
     * @return response     the list of services registered to the platform
     *
     */
    public DinoServiceListResponse platformGetAccessServices(String serverName, String userId, DinoPlatformRequestBody requestBody) {

        final String methodName = "platformGetAccessServices";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        DinoServiceListResponse response = new DinoServiceListResponse();

        if (requestBody != null)
        {

            // The serverName parameter to the RequestSummary is the target server not the server running the VS
            RequestSummary request = new RequestSummary(requestBody.getPlatformName(), null, null, methodName);
            response.setRequestSummary(request);


            AuditLog auditLog = null;
            DinoViewHandler handler;

            try
            {
                auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

                handler = instanceHandler.getDinoViewHandler(userId, serverName, methodName);

                if (handler == null)
                {
                    throw new DinoViewServiceException(DinoViewErrorCode.COULD_NOT_CREATE_HANDLER.getMessageDefinition(methodName),
                                                       this.getClass().getName(),
                                                       methodName);

                }

                response.setServiceList(handler.platformGetAccessServiceList(userId,
                                                                             requestBody.getPlatformName(),
                                                                             methodName));
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
            catch (DinoViewServiceException exception)
            {
                DinoExceptionHandler.captureCheckedException(response, exception, exception.getClass().getName());
            }
            catch (Exception exception)
            {
                restExceptionHandler.captureThrowable(response, exception, methodName, auditLog);
            }

        }
        else
        {
            /*
             * Raise (and immediately capture) a RexViewServicesException
             */
            DinoViewServiceException exception = new DinoViewServiceException(DinoViewErrorCode.VIEW_SERVICE_REQUEST_BODY_MISSING.getMessageDefinition(),
                                                                          this.getClass().getName(),
                                                                          methodName);

            DinoExceptionHandler.captureCheckedException( response, exception, exception.getClass().getName());
        }

        log.debug("Returning from method: " + methodName + " with response: " + response.toString());

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }

    /**
     * Retrieve view services for platform
     *
     * @param serverName    name of the local view server.
     * @param userId        userId under which the request is performed
     * @param requestBody   body containing platformName and platformRootURL of the platform to be queried.
     * @return response     the list of services registered to the platform
     *
     */
    public DinoServiceListResponse platformGetViewServices(String serverName, String userId, DinoPlatformRequestBody requestBody) {

        final String methodName = "platformGetViewServices";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        DinoServiceListResponse response = new DinoServiceListResponse();

        if (requestBody != null)
        {

            // The serverName parameter to the RequestSummary is the target server not the server running the VS
            RequestSummary request = new RequestSummary(requestBody.getPlatformName(), null, null, methodName);
            response.setRequestSummary(request);


            AuditLog auditLog = null;
            DinoViewHandler handler;


            try
            {
                auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);


                handler = instanceHandler.getDinoViewHandler(userId, serverName, methodName);

                if (handler == null)
                {
                    throw new DinoViewServiceException(DinoViewErrorCode.COULD_NOT_CREATE_HANDLER.getMessageDefinition(methodName),
                                                       this.getClass().getName(),
                                                       methodName);

                }

                response.setServiceList(handler.platformGetViewServiceList(userId,
                                                                           requestBody.getPlatformName(),
                                                                           methodName));
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
            catch (DinoViewServiceException exception)
            {
                DinoExceptionHandler.captureCheckedException(response, exception, exception.getClass().getName());
            }
            catch (Exception exception)
            {
                restExceptionHandler.captureThrowable(response, exception, methodName, auditLog);
            }

        }
        else
        {
            /*
             * Raise (and immediately capture) a RexViewServicesException
             */
            DinoViewServiceException exception = new DinoViewServiceException(DinoViewErrorCode.VIEW_SERVICE_REQUEST_BODY_MISSING.getMessageDefinition(),
                                                                          this.getClass().getName(),
                                                                          methodName);

            DinoExceptionHandler.captureCheckedException( response, exception, exception.getClass().getName());
        }

        log.debug("Returning from method: " + methodName + " with response: " + response.toString());

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }

    /**
     * Retrieve governance services for platform
     *
     * @param serverName    name of the local view server.
     * @param userId        userId under which the request is performed
     * @param requestBody   body containing platformName and platformRootURL of the platform to be queried.
     * @return response     the list of services registered to the platform
     *
     */
    public DinoServiceListResponse platformGetGovernanceServices(String serverName, String userId, DinoPlatformRequestBody requestBody) {

        final String methodName = "platformGetGovernanceServices";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        DinoServiceListResponse response = new DinoServiceListResponse();

        if (requestBody != null)
        {

            // The serverName parameter to the RequestSummary is the target server not the server running the VS
            RequestSummary request = new RequestSummary(requestBody.getPlatformName(), null, null, methodName);
            response.setRequestSummary(request);


            AuditLog auditLog = null;
            DinoViewHandler handler;

            try
            {
                auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

                handler = instanceHandler.getDinoViewHandler(userId, serverName, methodName);

                if (handler == null)
                {
                    throw new DinoViewServiceException(DinoViewErrorCode.COULD_NOT_CREATE_HANDLER.getMessageDefinition(methodName),
                                                       this.getClass().getName(),
                                                       methodName);

                }

                response.setServiceList(handler.platformGetGovernanceServiceList(userId,
                                                                                 requestBody.getPlatformName(),
                                                                                 methodName));

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
            catch (DinoViewServiceException exception)
            {
                DinoExceptionHandler.captureCheckedException(response, exception, exception.getClass().getName());
            }
            catch (Exception exception)
            {
                restExceptionHandler.captureThrowable(response, exception, methodName, auditLog);
            }


        }
        else
        {
            /*
             * Raise (and immediately capture) a RexViewServicesException
             */
            DinoViewServiceException exception = new DinoViewServiceException(DinoViewErrorCode.VIEW_SERVICE_REQUEST_BODY_MISSING.getMessageDefinition(),
                                                                          this.getClass().getName(),
                                                                          methodName);

            DinoExceptionHandler.captureCheckedException( response, exception, exception.getClass().getName());
        }



        log.debug("Returning from method: " + methodName + " with response: " + response.toString());

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }

    /**
     * Retrieve common services for platform
     *
     * @param serverName    name of the local view server.
     * @param userId        userId under which the request is performed
     * @param requestBody   body containing platformName and platformRootURL of the platform to be queried.
     * @return response     the list of services registered to the platform
     *
     */
    public DinoServiceListResponse platformGetCommonServices(String serverName, String userId, DinoPlatformRequestBody requestBody) {

        final String methodName = "platformGetCommonServices";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        DinoServiceListResponse response = new DinoServiceListResponse();

        if (requestBody != null)
        {

            // The serverName parameter to the RequestSummary is the target server not the server running the VS
            RequestSummary request = new RequestSummary(requestBody.getPlatformName(), null, null, methodName);
            response.setRequestSummary(request);


            AuditLog auditLog = null;
            DinoViewHandler handler;

            try
            {
                auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

                handler = instanceHandler.getDinoViewHandler(userId, serverName, methodName);

                if (handler == null)
                {
                    throw new DinoViewServiceException(DinoViewErrorCode.COULD_NOT_CREATE_HANDLER.getMessageDefinition(methodName),
                                                       this.getClass().getName(),
                                                       methodName);

                }

                response.setServiceList(handler.platformGetCommonServiceList(userId,
                                                                             requestBody.getPlatformName(),
                                                                             methodName));
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
            catch (DinoViewServiceException exception)
            {
                DinoExceptionHandler.captureCheckedException(response, exception, exception.getClass().getName());
            }
            catch (Exception exception)
            {
                restExceptionHandler.captureThrowable(response, exception, methodName, auditLog);
            }

        }
        else
        {
            /*
             * Raise (and immediately capture) a RexViewServicesException
             */
            DinoViewServiceException exception = new DinoViewServiceException(DinoViewErrorCode.VIEW_SERVICE_REQUEST_BODY_MISSING.getMessageDefinition(),
                                                                          this.getClass().getName(),
                                                                          methodName);

            DinoExceptionHandler.captureCheckedException( response, exception, exception.getClass().getName());
        }

        log.debug("Returning from method: " + methodName + " with response: " + response.toString());

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }


    /**
     * Retrieve server overview - this is an overview of multiple aspects of the server for when it becomes the focus.
     *
     * @param serverName    name of the local view server.
     * @param userId        userId under which the request is performed
     * @param requestBody   body containing serverName of the server to be queried.
     * @return response     the server overview.
     *
     */

    public DinoServerOverviewResponse serverGetOverview(String serverName, String userId, DinoServerRequestBody requestBody) {

        final String methodName = "serverGetOverview";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        DinoServerOverviewResponse response = new DinoServerOverviewResponse();

        if (requestBody != null)
        {

            // The serverName parameter to the RequestSummary is the target server not the server running the VS
            RequestSummary request = new RequestSummary(requestBody.getPlatformName(), requestBody.getServerName(), null, methodName);
            response.setRequestSummary(request);


            AuditLog auditLog = null;
            DinoViewHandler handler;

            try
            {
                auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

                handler = instanceHandler.getDinoViewHandler(userId, serverName, methodName);

                if (handler == null)
                {
                    throw new DinoViewServiceException(DinoViewErrorCode.COULD_NOT_CREATE_HANDLER.getMessageDefinition(methodName),
                                                       this.getClass().getName(),
                                                       methodName);

                }

                response.setServerOverview(handler.serverGetOverview(userId,
                                                                     requestBody.getServerName(),
                                                                     requestBody.getPlatformName(),
                                                                     requestBody.getServerInstanceName(),
                                                                     requestBody.getDescription(),
                                                                     methodName));
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
            catch (DinoViewServiceException exception)
            {
                DinoExceptionHandler.captureCheckedException(response, exception, exception.getClass().getName());
            }
            catch (Exception exception)
            {
                restExceptionHandler.captureThrowable(response, exception, methodName, auditLog);
            }

        }
        else
        {
            /*
             * Raise (and immediately capture) a RexViewServicesException
             */
            DinoViewServiceException exception = new DinoViewServiceException(DinoViewErrorCode.VIEW_SERVICE_REQUEST_BODY_MISSING.getMessageDefinition(),
                                                                          this.getClass().getName(),
                                                                          methodName);

            DinoExceptionHandler.captureCheckedException( response, exception, exception.getClass().getName());
        }

        log.debug("Returning from method: " + methodName + " with response: " + response.toString());

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }







    /**
     * Retrieve server origin
     *
     * @param serverName    name of the local view server.
     * @param userId        userId under which the request is performed
     * @param requestBody   body containing serverName of the server to be queried.
     * @return response     the origin string for the server.
     *
     */
    public DinoStringResponse serverGetOrigin(String serverName, String userId, DinoServerRequestBody requestBody) {

        final String methodName = "serverGetOrigin";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        DinoStringResponse response = new DinoStringResponse();

        if (requestBody != null)
        {

            // The serverName parameter to the RequestSummary is the target server not the server running the VS
            RequestSummary request = new RequestSummary(requestBody.getPlatformName(), requestBody.getServerName(), null, methodName);
            response.setRequestSummary(request);

            AuditLog auditLog = null;
            DinoViewHandler handler;

            try
            {
                auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

                handler = instanceHandler.getDinoViewHandler(userId, serverName, methodName);

                if (handler == null)
                {
                    throw new DinoViewServiceException(DinoViewErrorCode.COULD_NOT_CREATE_HANDLER.getMessageDefinition(methodName),
                                                       this.getClass().getName(),
                                                       methodName);

                }

                response.setString(handler.serverGetOrigin(userId,
                                                           requestBody.getServerName(),
                                                           requestBody.getPlatformName(),
                                                           methodName));
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
            catch (DinoViewServiceException exception)
            {
                DinoExceptionHandler.captureCheckedException(response, exception, exception.getClass().getName());
            }
            catch (Exception exception)
            {
                restExceptionHandler.captureThrowable(response, exception, methodName, auditLog);
            }
        }
        else
        {
            /*
             * Raise (and immediately capture) a RexViewServicesException
             */
            DinoViewServiceException exception = new DinoViewServiceException(DinoViewErrorCode.VIEW_SERVICE_REQUEST_BODY_MISSING.getMessageDefinition(),
                                                                          this.getClass().getName(),
                                                                          methodName);

            DinoExceptionHandler.captureCheckedException( response, exception, exception.getClass().getName());
        }

        log.debug("Returning from method: " + methodName + " with response: " + response.toString());

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }


    /**
     * Retrieve server type classification
     *
     * @param serverName    name of the local view server.
     * @param userId        userId under which the request is performed
     * @param requestBody   body containing serverName of the server to be queried.
     * @return response     the server type, as a string response.
     *
     */
    public DinoServerTypeResponse serverGetTypeClassification(String serverName, String userId, DinoServerRequestBody requestBody) {

        final String methodName = "serverGetTypeClassification";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        DinoServerTypeResponse response = new DinoServerTypeResponse();

        if (requestBody != null)
        {

            // The serverName parameter to the RequestSummary is the target server not the server running the VS
            RequestSummary request = new RequestSummary(requestBody.getPlatformName(), requestBody.getServerName(), null, methodName);
            response.setRequestSummary(request);


            AuditLog auditLog = null;
            DinoViewHandler handler;

            try
            {
                auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

                handler = instanceHandler.getDinoViewHandler(userId, serverName, methodName);

                if (handler == null)
                {
                    throw new DinoViewServiceException(DinoViewErrorCode.COULD_NOT_CREATE_HANDLER.getMessageDefinition(methodName),
                                                       this.getClass().getName(),
                                                       methodName);

                }

                ServerTypeClassificationSummary summary = handler.serverGetTypeClassification(userId,
                                                                                              requestBody.getServerName(),
                                                                                              requestBody.getPlatformName(),
                                                                                              methodName);
                response.setServerTypeName(summary.getServerTypeName());
                response.setServerTypeDescription(summary.getServerTypeDescription());

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
            catch (DinoViewServiceException exception)
            {
                DinoExceptionHandler.captureCheckedException(response, exception, exception.getClass().getName());
            }
            catch (Exception exception)
            {
                restExceptionHandler.captureThrowable(response, exception, methodName, auditLog);
            }

        }
        else
        {
            /*
             * Raise (and immediately capture) a RexViewServicesException
             */
            DinoViewServiceException exception = new DinoViewServiceException(DinoViewErrorCode.VIEW_SERVICE_REQUEST_BODY_MISSING.getMessageDefinition(),
                                                                          this.getClass().getName(),
                                                                          methodName);

            DinoExceptionHandler.captureCheckedException( response, exception, exception.getClass().getName());
        }

        log.debug("Returning from method: " + methodName + " with response: " + response.toString());

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }


    /**
     * Retrieve server's stored configuration
     *
     * @param serverName    name of the local view server.
     * @param userId        userId under which the request is performed
     * @param requestBody   body containing serverName of the server to be queried.
     * @return response     the configuration for the server.
     *
     */
    public DinoServerConfigResponse serverGetStoredConfiguration(String serverName, String userId, DinoServerRequestBody requestBody) {

        final String methodName = "serverGetConfiguration";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        DinoServerConfigResponse response = new DinoServerConfigResponse();

        if (requestBody != null)
        {

            // The serverName parameter to the RequestSummary is the target server not the server running the VS
            RequestSummary request = new RequestSummary(requestBody.getPlatformName(), requestBody.getServerName(), null, methodName);
            response.setRequestSummary(request);

            AuditLog auditLog = null;
            DinoViewHandler handler;

            try
            {
                auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

                handler = instanceHandler.getDinoViewHandler(userId, serverName, methodName);

                if (handler == null)
                {
                    throw new DinoViewServiceException(DinoViewErrorCode.COULD_NOT_CREATE_HANDLER.getMessageDefinition(methodName),
                                                       this.getClass().getName(),
                                                       methodName);

                }

                response.setServerConfig(handler.serverGetStoredConfiguration(userId,
                                                                              requestBody.getServerName(),
                                                                              requestBody.getPlatformName(),
                                                                              methodName));
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
            catch (DinoViewServiceException exception)
            {
                DinoExceptionHandler.captureCheckedException(response, exception, exception.getClass().getName());
            }
            catch (Exception exception)
            {
                restExceptionHandler.captureThrowable(response, exception, methodName, auditLog);
            }

        }
        else
        {
            /*
             * Raise (and immediately capture) a RexViewServicesException
             */
            DinoViewServiceException exception = new DinoViewServiceException(DinoViewErrorCode.VIEW_SERVICE_REQUEST_BODY_MISSING.getMessageDefinition(),
                                                                          this.getClass().getName(),
                                                                          methodName);

            DinoExceptionHandler.captureCheckedException( response, exception, exception.getClass().getName());
        }

        log.debug("Returning from method: " + methodName + " with response: " + response.toString());

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }

    /**
     * Retrieve server's running instance configuration
     *
     * @param serverName    name of the local view server.
     * @param userId        userId under which the request is performed
     * @param requestBody   body containing serverName of the server to be queried.
     * @return response     the configuration for the server.
     *
     */
    public DinoServerConfigResponse serverGetInstanceConfiguration(String serverName, String userId, DinoServerRequestBody requestBody) {

        final String methodName = "serverGetInstanceConfiguration";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        DinoServerConfigResponse response = new DinoServerConfigResponse();

        if (requestBody != null)
        {

            // The serverName parameter to the RequestSummary is the target server not the server running the VS
            RequestSummary request = new RequestSummary(requestBody.getPlatformName(), requestBody.getServerName(), null, methodName);
            response.setRequestSummary(request);


            AuditLog auditLog = null;
            DinoViewHandler handler;

            try
            {
                auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

                handler = instanceHandler.getDinoViewHandler(userId, serverName, methodName);

                if (handler == null)
                {
                    throw new DinoViewServiceException(DinoViewErrorCode.COULD_NOT_CREATE_HANDLER.getMessageDefinition(methodName),
                                                       this.getClass().getName(),
                                                       methodName);

                }

                response.setServerConfig(handler.serverGetInstanceConfiguration(userId,
                                                                                requestBody.getServerName(),
                                                                                requestBody.getPlatformName(),
                                                                                methodName));
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
            catch (DinoViewServiceException exception)
            {
                DinoExceptionHandler.captureCheckedException(response, exception, exception.getClass().getName());
            }
            catch (Exception exception)
            {
                restExceptionHandler.captureThrowable(response, exception, methodName, auditLog);
            }

        }
        else
        {
            /*
             * Raise (and immediately capture) a RexViewServicesException
             */
            DinoViewServiceException exception = new DinoViewServiceException(DinoViewErrorCode.VIEW_SERVICE_REQUEST_BODY_MISSING.getMessageDefinition(),
                                                                          this.getClass().getName(),
                                                                          methodName);

            DinoExceptionHandler.captureCheckedException( response, exception, exception.getClass().getName());
        }

        log.debug("Returning from method: " + methodName + " with response: " + response.toString());

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }


    /**
     * Retrieve server's running instance configuration
     *
     * @param serverName    name of the local view server.
     * @param userId        userId under which the request is performed
     * @param requestBody   body containing serverName of the server to be queried.
     * @return response     the configuration for the server.
     *
     */
    public DinoServerDoubleConfigResponse serverGetStoredAndActiveConfiguration(String serverName, String userId, DinoServerRequestBody requestBody) {

        final String methodName = "serverGetInstanceConfiguration";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        DinoServerDoubleConfigResponse response = new DinoServerDoubleConfigResponse();

        if (requestBody != null)
        {

            // The serverName parameter to the RequestSummary is the target server not the server running the VS
            RequestSummary request = new RequestSummary(requestBody.getPlatformName(), requestBody.getServerName(), null, methodName);
            response.setRequestSummary(request);


            AuditLog auditLog = null;
            DinoViewHandler handler;

            try
            {
                auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

                handler = instanceHandler.getDinoViewHandler(userId, serverName, methodName);

                if (handler == null)
                {
                    throw new DinoViewServiceException(DinoViewErrorCode.COULD_NOT_CREATE_HANDLER.getMessageDefinition(methodName),
                                                       this.getClass().getName(),
                                                       methodName);

                }

                response.setStoredConfig(handler.serverGetStoredConfiguration(userId,
                                                                              requestBody.getServerName(),
                                                                              requestBody.getPlatformName(),
                                                                              methodName));

                /*
                 * It is possible to get a null return (from serverGetInstanceConfiguration) if the server
                 * is not running. The handler detects this condition and returns null rather than
                 * throwing an exception.
                 */

                response.setActiveConfig(handler.serverGetInstanceConfiguration(userId,
                                                                                requestBody.getServerName(),
                                                                                requestBody.getPlatformName(),
                                                                                methodName));

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
            catch (DinoViewServiceException exception)
            {
                /*
                 * If the method managed to retrieve stored configuration and it is just the 'instance'
                 * (i.e. active) configuration that is missing, then ignore the exception and treat it
                 * the same as if the user had requested the stored configuration only.
                 * On the other hand, if we have failed to even get the stored configuration then it's
                 * game over.
                 */
                if (response.getStoredConfig() == null)
                {
                    DinoExceptionHandler.captureCheckedException(response, exception, exception.getClass().getName());
                }
                else {
                    /* proceed to the normal method exit with logging and return.... */
                }
            }
            catch (Exception exception)
            {
                restExceptionHandler.captureThrowable(response, exception, methodName, auditLog);
            }

        }
        else
        {
            /*
             * Raise (and immediately capture) a RexViewServicesException
             */
            DinoViewServiceException exception = new DinoViewServiceException(DinoViewErrorCode.VIEW_SERVICE_REQUEST_BODY_MISSING.getMessageDefinition(),
                                                                          this.getClass().getName(),
                                                                          methodName);

            DinoExceptionHandler.captureCheckedException( response, exception, exception.getClass().getName());
        }

        log.debug("Returning from method: " + methodName + " with response: " + response.toString());

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }


    /**
     * Retrieve server's audit log
     *
     * @param serverName    name of the local view server.
     * @param userId        userId under which the request is performed
     * @param requestBody   body containing serverName of the server to be queried.
     * @return response     the configuration for the server.
     *
     */
    public DinoServerAuditLogResponse serverGetAuditLog(String serverName, String userId, DinoServerRequestBody requestBody) {

        final String methodName = "serverGetAuditLog";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        DinoServerAuditLogResponse response = new DinoServerAuditLogResponse();

        if (requestBody != null)
        {

            // The serverName parameter to the RequestSummary is the target server not the server running the VS
            RequestSummary request = new RequestSummary(requestBody.getPlatformName(), requestBody.getServerName(), null, methodName);
            response.setRequestSummary(request);


            AuditLog auditLog = null;
            DinoViewHandler handler;

            try
            {
                auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

                handler = instanceHandler.getDinoViewHandler(userId, serverName, methodName);

                if (handler == null)
                {
                    throw new DinoViewServiceException(DinoViewErrorCode.COULD_NOT_CREATE_HANDLER.getMessageDefinition(methodName),
                                                       this.getClass().getName(),
                                                       methodName);

                }

                response.setAuditLog(handler.serverGetAuditLog(userId,
                                                               requestBody.getServerName(),
                                                               requestBody.getPlatformName(),
                                                               methodName));

            }
            catch (PropertyServerException exception)
            {
                restExceptionHandler.capturePropertyServerException(response, exception);
            }
            catch (InvalidParameterException exception)
            {
                restExceptionHandler.captureInvalidParameterException(response, exception);
            }
            catch (UserNotAuthorizedException exception)
            {
                restExceptionHandler.captureUserNotAuthorizedException(response, exception);
            }
            catch (DinoViewServiceException exception)
            {
                DinoExceptionHandler.captureCheckedException(response, exception, exception.getClass().getName());
            }
            catch (Exception exception)
            {
                restExceptionHandler.captureThrowable(response, exception, methodName, auditLog);
            }

        }
        else
        {
            /*
             * Raise (and immediately capture) a RexViewServicesException
             */
            DinoViewServiceException exception = new DinoViewServiceException(DinoViewErrorCode.VIEW_SERVICE_REQUEST_BODY_MISSING.getMessageDefinition(),
                                                                          this.getClass().getName(),
                                                                          methodName);

            DinoExceptionHandler.captureCheckedException( response, exception, exception.getClass().getName());
        }

        log.debug("Returning from method: " + methodName + " with response: " + response.toString());

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }



    /**
     * Retrieve list of integration services for a specified server
     *
     * @param serverName    name of the local view server.
     * @param userId        userId under which the request is performed
     * @param requestBody   body containing serverName of the server and serviceName of the service to be queried.
     * @return response     the configuration for the server.
     *
     */
    public DinoServiceListResponse serverGetIntegrationServices(String serverName, String userId, DinoServiceRequestBody requestBody) {

        final String methodName = "serverGetIntegrationServices";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        DinoServiceListResponse response = new DinoServiceListResponse();

        if (requestBody != null)
        {

            // The serverName parameter to the RequestSummary is the target server not the server running the VS
            RequestSummary request = new RequestSummary(requestBody.getPlatformName(), requestBody.getServerName(), null, methodName);
            response.setRequestSummary(request);


            AuditLog auditLog = null;
            DinoViewHandler handler;

            try
            {
                auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

                handler = instanceHandler.getDinoViewHandler(userId, serverName, methodName);

                if (handler == null)
                {
                    throw new DinoViewServiceException(DinoViewErrorCode.COULD_NOT_CREATE_HANDLER.getMessageDefinition(methodName),
                                                       this.getClass().getName(),
                                                       methodName);

                }

                response.setServiceList(handler.serverGetIntegrationServices(userId,
                                                                           requestBody.getServerName(),
                                                                           requestBody.getPlatformName(),
                                                                           methodName));

            }
            catch (PropertyServerException exception)
            {
                restExceptionHandler.capturePropertyServerException(response, exception);
            }
            catch (InvalidParameterException exception)
            {
                restExceptionHandler.captureInvalidParameterException(response, exception);
            }
            catch (UserNotAuthorizedException exception)
            {
                restExceptionHandler.captureUserNotAuthorizedException(response, exception);
            }
            catch (DinoViewServiceException exception)
            {
                DinoExceptionHandler.captureCheckedException(response, exception, exception.getClass().getName());
            }
            catch (Exception exception)
            {
                restExceptionHandler.captureThrowable(response, exception, methodName, auditLog);
            }

        }
        else
        {
            /*
             * Raise (and immediately capture) a RexViewServicesException
             */
            DinoViewServiceException exception = new DinoViewServiceException(DinoViewErrorCode.VIEW_SERVICE_REQUEST_BODY_MISSING.getMessageDefinition(),
                                                                          this.getClass().getName(),
                                                                          methodName);

            DinoExceptionHandler.captureCheckedException( response, exception, exception.getClass().getName());
        }

        log.debug("Returning from method: " + methodName + " with response: " + response.toString());

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }

    /**
     * Retrieve list of engine services for a specified server
     *
     * @param serverName    name of the local view server.
     * @param userId        userId under which the request is performed
     * @param requestBody   body containing serverName of the server and serviceName of the service to be queried.
     * @return response     the configuration for the server.
     *
     */
    public DinoServiceListResponse serverGetEngineServices(String serverName, String userId, DinoServiceRequestBody requestBody) {

        final String methodName = "serverGetEngineServices";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        DinoServiceListResponse response = new DinoServiceListResponse();

        if (requestBody != null)
        {

            // The serverName parameter to the RequestSummary is the target server not the server running the VS
            RequestSummary request = new RequestSummary(requestBody.getPlatformName(), requestBody.getServerName(), null, methodName);
            response.setRequestSummary(request);


            AuditLog auditLog = null;
            DinoViewHandler handler;

            try
            {
                auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

                handler = instanceHandler.getDinoViewHandler(userId, serverName, methodName);

                if (handler == null)
                {
                    throw new DinoViewServiceException(DinoViewErrorCode.COULD_NOT_CREATE_HANDLER.getMessageDefinition(methodName),
                                                       this.getClass().getName(),
                                                       methodName);

                }

                response.setServiceList(handler.serverGetEngineServices(userId,
                                                                        requestBody.getServerName(),
                                                                        requestBody.getPlatformName(),
                                                                        methodName));

            }
            catch (PropertyServerException exception)
            {
                restExceptionHandler.capturePropertyServerException(response, exception);
            }
            catch (InvalidParameterException exception)
            {
                restExceptionHandler.captureInvalidParameterException(response, exception);
            }
            catch (UserNotAuthorizedException exception)
            {
                restExceptionHandler.captureUserNotAuthorizedException(response, exception);
            }
            catch (DinoViewServiceException exception)
            {
                DinoExceptionHandler.captureCheckedException(response, exception, exception.getClass().getName());
            }
            catch (Exception exception)
            {
                restExceptionHandler.captureThrowable(response, exception, methodName, auditLog);
            }

        }
        else
        {
            /*
             * Raise (and immediately capture) a RexViewServicesException
             */
            DinoViewServiceException exception = new DinoViewServiceException(DinoViewErrorCode.VIEW_SERVICE_REQUEST_BODY_MISSING.getMessageDefinition(),
                                                                          this.getClass().getName(),
                                                                          methodName);

            DinoExceptionHandler.captureCheckedException( response, exception, exception.getClass().getName());
        }

        log.debug("Returning from method: " + methodName + " with response: " + response.toString());

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }




    /**
     * Retrieve list of access services for a specified server
     *
     * @param serverName    name of the local view server.
     * @param userId        userId under which the request is performed
     * @param requestBody   body containing serverName of the server and serviceName of the service to be queried.
     * @return response     the configuration for the server.
     *
     */
    public DinoServiceListResponse serverGetAccessServices(String serverName, String userId, DinoServiceRequestBody requestBody) {

        final String methodName = "serverGetAccessServices";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        DinoServiceListResponse response = new DinoServiceListResponse();

        if (requestBody != null)
        {

            // The serverName parameter to the RequestSummary is the target server not the server running the VS
            RequestSummary request = new RequestSummary(requestBody.getPlatformName(), requestBody.getServerName(), null, methodName);
            response.setRequestSummary(request);


            AuditLog auditLog = null;
            DinoViewHandler handler;

            try
            {
                auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

                handler = instanceHandler.getDinoViewHandler(userId, serverName, methodName);

                if (handler == null)
                {
                    throw new DinoViewServiceException(DinoViewErrorCode.COULD_NOT_CREATE_HANDLER.getMessageDefinition(methodName),
                                                       this.getClass().getName(),
                                                       methodName);

                }

                response.setServiceList(handler.serverGetAccessServices(userId,
                                                                        requestBody.getServerName(),
                                                                        requestBody.getPlatformName(),
                                                                        methodName));

            }
            catch (PropertyServerException exception)
            {
                restExceptionHandler.capturePropertyServerException(response, exception);
            }
            catch (InvalidParameterException exception)
            {
                restExceptionHandler.captureInvalidParameterException(response, exception);
            }
            catch (UserNotAuthorizedException exception)
            {
                restExceptionHandler.captureUserNotAuthorizedException(response, exception);
            }
            catch (DinoViewServiceException exception)
            {
                DinoExceptionHandler.captureCheckedException(response, exception, exception.getClass().getName());
            }
            catch (Exception exception)
            {
                restExceptionHandler.captureThrowable(response, exception, methodName, auditLog);
            }

        }
        else
        {
            /*
             * Raise (and immediately capture) a RexViewServicesException
             */
            DinoViewServiceException exception = new DinoViewServiceException(DinoViewErrorCode.VIEW_SERVICE_REQUEST_BODY_MISSING.getMessageDefinition(),
                                                                          this.getClass().getName(),
                                                                          methodName);

            DinoExceptionHandler.captureCheckedException( response, exception, exception.getClass().getName());
        }

        log.debug("Returning from method: " + methodName + " with response: " + response.toString());

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }



    /**
     * Retrieve list of view services for a specified server
     *
     * @param serverName    name of the local view server.
     * @param userId        userId under which the request is performed
     * @param requestBody   body containing serverName of the server and serviceName of the service to be queried.
     * @return response     the configuration for the server.
     *
     */
    public DinoServiceListResponse serverGetViewServices(String serverName, String userId, DinoServiceRequestBody requestBody) {

        final String methodName = "serverGetViewServices";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        DinoServiceListResponse response = new DinoServiceListResponse();

        if (requestBody != null)
        {

            // The serverName parameter to the RequestSummary is the target server not the server running the VS
            RequestSummary request = new RequestSummary(requestBody.getPlatformName(), requestBody.getServerName(), null, methodName);
            response.setRequestSummary(request);


            AuditLog auditLog = null;
            DinoViewHandler handler;

            try
            {
                auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

                handler = instanceHandler.getDinoViewHandler(userId, serverName, methodName);

                if (handler == null)
                {
                    throw new DinoViewServiceException(DinoViewErrorCode.COULD_NOT_CREATE_HANDLER.getMessageDefinition(methodName),
                                                       this.getClass().getName(),
                                                       methodName);

                }

                response.setServiceList(handler.serverGetViewServices(userId,
                                                                      requestBody.getServerName(),
                                                                      requestBody.getPlatformName(),
                                                                      methodName));

            }
            catch (PropertyServerException exception)
            {
                restExceptionHandler.capturePropertyServerException(response, exception);
            }
            catch (InvalidParameterException exception)
            {
                restExceptionHandler.captureInvalidParameterException(response, exception);
            }
            catch (UserNotAuthorizedException exception)
            {
                restExceptionHandler.captureUserNotAuthorizedException(response, exception);
            }
            catch (DinoViewServiceException exception)
            {
                DinoExceptionHandler.captureCheckedException(response, exception, exception.getClass().getName());
            }
            catch (Exception exception)
            {
                restExceptionHandler.captureThrowable(response, exception, methodName, auditLog);
            }

        }
        else
        {
            /*
             * Raise (and immediately capture) a RexViewServicesException
             */
            DinoViewServiceException exception = new DinoViewServiceException(DinoViewErrorCode.VIEW_SERVICE_REQUEST_BODY_MISSING.getMessageDefinition(),
                                                                          this.getClass().getName(),
                                                                          methodName);

            DinoExceptionHandler.captureCheckedException( response, exception, exception.getClass().getName());
        }

        log.debug("Returning from method: " + methodName + " with response: " + response.toString());

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }





    /**
     * Retrieve service details for a specified integration service
     *
     * @param serverName    name of the local view server.
     * @param userId        userId under which the request is performed
     * @param requestBody   body containing serverName of the server and serviceName of the service to be queried.
     * @return response     the configuration for the server.
     *
     */
    public DinoServiceDetailsResponse serverGetIntegrationServiceDetails(String serverName, String userId, DinoServiceRequestBody requestBody) {

        final String methodName = "serverGetIntegrationServiceDetails";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        DinoServiceDetailsResponse response = new DinoServiceDetailsResponse();

        if (requestBody != null)
        {

            // The serverName parameter to the RequestSummary is the target server not the server running the VS
            RequestSummary request = new RequestSummary(requestBody.getPlatformName(),
                                                        requestBody.getServerName(),
                                                        null,
                                                        methodName);
            response.setRequestSummary(request);


            AuditLog auditLog = null;
            DinoViewHandler handler;

            try
            {
                auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

                handler = instanceHandler.getDinoViewHandler(userId, serverName, methodName);

                if (handler == null)
                {
                    throw new DinoViewServiceException(DinoViewErrorCode.COULD_NOT_CREATE_HANDLER.getMessageDefinition(methodName),
                                                       this.getClass().getName(),
                                                       methodName);

                }

                response.setServiceDetails(handler.serverGetIntegrationServiceDetails(userId,
                                                                                      requestBody.getServerName(),
                                                                                      requestBody.getPlatformName(),
                                                                                      requestBody.getServiceURLMarker(),
                                                                                      methodName));

            }
            catch (PropertyServerException exception)
            {
                restExceptionHandler.capturePropertyServerException(response, exception);
            }
            catch (InvalidParameterException exception)
            {
                restExceptionHandler.captureInvalidParameterException(response, exception);
            }
            catch (UserNotAuthorizedException exception)
            {
                restExceptionHandler.captureUserNotAuthorizedException(response, exception);
            }
            catch (DinoViewServiceException exception)
            {
                DinoExceptionHandler.captureCheckedException(response, exception, exception.getClass().getName());
            }
            catch (Exception exception)
            {
                restExceptionHandler.captureThrowable(response, exception, methodName, auditLog);
            }

        }
        else
        {
            /*
             * Raise (and immediately capture) a RexViewServicesException
             */
            DinoViewServiceException exception = new DinoViewServiceException(DinoViewErrorCode.VIEW_SERVICE_REQUEST_BODY_MISSING.getMessageDefinition(),
                                                                          this.getClass().getName(),
                                                                          methodName);

            DinoExceptionHandler.captureCheckedException( response, exception, exception.getClass().getName());
        }

        log.debug("Returning from method: " + methodName + " with response: " + response.toString());

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }

    /**
     * Retrieve service details for a specified engine service
     *
     * @param serverName    name of the local view server.
     * @param userId        userId under which the request is performed
     * @param requestBody   body containing serverName of the server and serviceName of the service to be queried.
     * @return response     the configuration for the server.
     *
     */
    public DinoServiceDetailsResponse serverGetEngineServiceDetails(String serverName, String userId, DinoServiceRequestBody requestBody) {

        final String methodName = "serverGetEngineServiceDetails";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        DinoServiceDetailsResponse response = new DinoServiceDetailsResponse();

        if (requestBody != null)
        {

            // The serverName parameter to the RequestSummary is the target server not the server running the VS
            RequestSummary request = new RequestSummary(requestBody.getPlatformName(),
                                                        requestBody.getServerName(),
                                                        null,
                                                        methodName);
            response.setRequestSummary(request);


            AuditLog auditLog = null;
            DinoViewHandler handler;

            try
            {
                auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

                handler = instanceHandler.getDinoViewHandler(userId, serverName, methodName);

                if (handler == null)
                {
                    throw new DinoViewServiceException(DinoViewErrorCode.COULD_NOT_CREATE_HANDLER.getMessageDefinition(methodName),
                                                       this.getClass().getName(),
                                                       methodName);

                }

                response.setServiceDetails(handler.serverGetEngineServiceDetails(userId,
                                                                                 requestBody.getServerName(),
                                                                                 requestBody.getPlatformName(),
                                                                                 requestBody.getServiceURLMarker(),
                                                                                 methodName));

            }
            catch (PropertyServerException exception)
            {
                restExceptionHandler.capturePropertyServerException(response, exception);
            }
            catch (InvalidParameterException exception)
            {
                restExceptionHandler.captureInvalidParameterException(response, exception);
            }
            catch (UserNotAuthorizedException exception)
            {
                restExceptionHandler.captureUserNotAuthorizedException(response, exception);
            }
            catch (DinoViewServiceException exception)
            {
                DinoExceptionHandler.captureCheckedException(response, exception, exception.getClass().getName());
            }
            catch (Exception exception)
            {
                restExceptionHandler.captureThrowable(response, exception, methodName, auditLog);
            }

        }
        else
        {
            /*
             * Raise (and immediately capture) a RexViewServicesException
             */
            DinoViewServiceException exception = new DinoViewServiceException(DinoViewErrorCode.VIEW_SERVICE_REQUEST_BODY_MISSING.getMessageDefinition(),
                                                                          this.getClass().getName(),
                                                                          methodName);

            DinoExceptionHandler.captureCheckedException( response, exception, exception.getClass().getName());
        }

        log.debug("Returning from method: " + methodName + " with response: " + response.toString());

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }

    /**
     * Retrieve service details for a specified access service
     *
     * @param serverName    name of the local view server.
     * @param userId        userId under which the request is performed
     * @param requestBody   body containing serverName of the server and serviceName of the service to be queried.
     * @return response     the configuration for the server.
     *
     */
    public DinoServiceDetailsResponse serverGetAccessServiceDetails(String serverName, String userId, DinoServiceRequestBody requestBody) {

        final String methodName = "serverGetAccessServiceDetails";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        DinoServiceDetailsResponse response = new DinoServiceDetailsResponse();

        if (requestBody != null)
        {

            // The serverName parameter to the RequestSummary is the target server not the server running the VS
            RequestSummary request = new RequestSummary(requestBody.getPlatformName(),
                                                        requestBody.getServerName(),
                                                        null,
                                                        methodName);
            response.setRequestSummary(request);


            AuditLog auditLog = null;
            DinoViewHandler handler;

            try
            {
                auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

                handler = instanceHandler.getDinoViewHandler(userId, serverName, methodName);

                if (handler == null)
                {
                    throw new DinoViewServiceException(DinoViewErrorCode.COULD_NOT_CREATE_HANDLER.getMessageDefinition(methodName),
                                                       this.getClass().getName(),
                                                       methodName);

                }

                response.setServiceDetails(handler.serverGetAccessServiceDetails(userId,
                                                                                 requestBody.getServerName(),
                                                                                 requestBody.getPlatformName(),
                                                                                 requestBody.getServiceFullName(),
                                                                                 requestBody.getServiceURLMarker(),
                                                                                 methodName));

            }
            catch (PropertyServerException exception)
            {
                restExceptionHandler.capturePropertyServerException(response, exception);
            }
            catch (InvalidParameterException exception)
            {
                restExceptionHandler.captureInvalidParameterException(response, exception);
            }
            catch (UserNotAuthorizedException exception)
            {
                restExceptionHandler.captureUserNotAuthorizedException(response, exception);
            }
            catch (DinoViewServiceException exception)
            {
                DinoExceptionHandler.captureCheckedException(response, exception, exception.getClass().getName());
            }
            catch (Exception exception)
            {
                restExceptionHandler.captureThrowable(response, exception, methodName, auditLog);
            }

        }
        else
        {
            /*
             * Raise (and immediately capture) a RexViewServicesException
             */
            DinoViewServiceException exception = new DinoViewServiceException(DinoViewErrorCode.VIEW_SERVICE_REQUEST_BODY_MISSING.getMessageDefinition(),
                                                                          this.getClass().getName(),
                                                                          methodName);

            DinoExceptionHandler.captureCheckedException( response, exception, exception.getClass().getName());
        }

        log.debug("Returning from method: " + methodName + " with response: " + response.toString());

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }


    /**
     * Retrieve service details for a specified view service
     *
     * @param serverName    name of the local view server.
     * @param userId        userId under which the request is performed
     * @param requestBody   body containing serverName of the server and serviceName of the service to be queried.
     * @return response     the configuration for the server.
     *
     */
    public DinoServiceDetailsResponse serverGetViewServiceDetails(String serverName, String userId, DinoServiceRequestBody requestBody) {

        final String methodName = "serverGetViewServiceDetails";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        DinoServiceDetailsResponse response = new DinoServiceDetailsResponse();

        if (requestBody != null)
        {

            // The serverName parameter to the RequestSummary is the target server not the server running the VS
            RequestSummary request = new RequestSummary(requestBody.getPlatformName(),
                                                        requestBody.getServerName(),
                                                        null,
                                                        methodName);
            response.setRequestSummary(request);


            AuditLog auditLog = null;
            DinoViewHandler handler;

            try
            {
                auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

                handler = instanceHandler.getDinoViewHandler(userId, serverName, methodName);

                if (handler == null)
                {
                    throw new DinoViewServiceException(DinoViewErrorCode.COULD_NOT_CREATE_HANDLER.getMessageDefinition(methodName),
                                                       this.getClass().getName(),
                                                       methodName);

                }

                response.setServiceDetails(handler.serverGetViewServiceDetails(userId,
                                                                                 requestBody.getServerName(),
                                                                                 requestBody.getPlatformName(),
                                                                                 requestBody.getServiceURLMarker(),
                                                                                 methodName));

            }
            catch (PropertyServerException exception)
            {
                restExceptionHandler.capturePropertyServerException(response, exception);
            }
            catch (InvalidParameterException exception)
            {
                restExceptionHandler.captureInvalidParameterException(response, exception);
            }
            catch (UserNotAuthorizedException exception)
            {
                restExceptionHandler.captureUserNotAuthorizedException(response, exception);
            }
            catch (DinoViewServiceException exception)
            {
                DinoExceptionHandler.captureCheckedException(response, exception, exception.getClass().getName());
            }
            catch (Exception exception)
            {
                restExceptionHandler.captureThrowable(response, exception, methodName, auditLog);
            }

        }
        else
        {
            /*
             * Raise (and immediately capture) a RexViewServicesException
             */
            DinoViewServiceException exception = new DinoViewServiceException(DinoViewErrorCode.VIEW_SERVICE_REQUEST_BODY_MISSING.getMessageDefinition(),
                                                                          this.getClass().getName(),
                                                                          methodName);

            DinoExceptionHandler.captureCheckedException( response, exception, exception.getClass().getName());
        }

        log.debug("Returning from method: " + methodName + " with response: " + response.toString());

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }



    /**
     * Retrieve engine details for a specified engine
     *
     * @param serverName    name of the local view server.
     * @param userId        userId under which the request is performed
     * @param requestBody   body containing serverName of the server and serviceName of the service to be queried.
     * @return response     the configuration for the server.
     *
     */
    public DinoEngineDetailsResponse serverGetEngineDetails(String                serverName,
                                                            String                userId,
                                                            DinoEngineRequestBody requestBody) {

        final String methodName = "serverGetEngineDetails";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        DinoEngineDetailsResponse response = new DinoEngineDetailsResponse();

        if (requestBody != null)
        {

            // The serverName parameter to the RequestSummary is the target server not the server running the VS
            RequestSummary request = new RequestSummary(requestBody.getPlatformName(),
                                                        requestBody.getServerName(),
                                                        requestBody.getRequestContextCorrelator(),  // OMES instance GUID
                                                        methodName);
            response.setRequestSummary(request);


            AuditLog auditLog = null;
            DinoViewHandler handler;

            try
            {
                auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

                handler = instanceHandler.getDinoViewHandler(userId, serverName, methodName);

                if (handler == null)
                {
                    throw new DinoViewServiceException(DinoViewErrorCode.COULD_NOT_CREATE_HANDLER.getMessageDefinition(methodName),
                                                       this.getClass().getName(),
                                                       methodName);

                }

                response.setEngineDetails(handler.serverGetEngineDetails(userId,
                                                                         requestBody.getServerName(),
                                                                         requestBody.getPlatformName(),
                                                                         requestBody.getEngineQualifiedName(),
                                                                         methodName));

            }
            catch (PropertyServerException exception)
            {
                restExceptionHandler.capturePropertyServerException(response, exception);
            }
            catch (InvalidParameterException exception)
            {
                restExceptionHandler.captureInvalidParameterException(response, exception);
            }
            catch (UserNotAuthorizedException exception)
            {
                restExceptionHandler.captureUserNotAuthorizedException(response, exception);
            }
            catch (DinoViewServiceException exception)
            {
                DinoExceptionHandler.captureCheckedException(response, exception, exception.getClass().getName());
            }
            catch (Exception exception)
            {
                restExceptionHandler.captureThrowable(response, exception, methodName, auditLog);
            }

        }
        else
        {
            /*
             * Raise (and immediately capture) a RexViewServicesException
             */
            DinoViewServiceException exception = new DinoViewServiceException(DinoViewErrorCode.VIEW_SERVICE_REQUEST_BODY_MISSING.getMessageDefinition(),
                                                                          this.getClass().getName(),
                                                                          methodName);

            DinoExceptionHandler.captureCheckedException( response, exception, exception.getClass().getName());
        }

        log.debug("Returning from method: " + methodName + " with response: " + response.toString());

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }


}
