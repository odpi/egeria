/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria category. */
package org.odpi.openmetadata.viewservices.dino.server;


import org.odpi.openmetadata.adminservices.rest.ServerTypeClassificationSummary;
import org.odpi.openmetadata.commonservices.ffdc.RESTCallLogger;
import org.odpi.openmetadata.commonservices.ffdc.RESTCallToken;
import org.odpi.openmetadata.commonservices.ffdc.RESTExceptionHandler;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.OCFCheckedExceptionBase;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.viewservices.dino.api.properties.RequestSummary;
import org.odpi.openmetadata.viewservices.dino.api.properties.ResourceEndpoint;
import org.odpi.openmetadata.viewservices.dino.api.rest.DinoPlatformOverviewResponse;
import org.odpi.openmetadata.viewservices.dino.api.rest.DinoPlatformRequestBody;
import org.odpi.openmetadata.viewservices.dino.api.rest.DinoResourceEndpointListResponse;
import org.odpi.openmetadata.viewservices.dino.api.rest.DinoServerAuditLogResponse;
import org.odpi.openmetadata.viewservices.dino.api.rest.DinoServerConfigResponse;
import org.odpi.openmetadata.viewservices.dino.api.rest.DinoServerDoubleConfigResponse;
import org.odpi.openmetadata.viewservices.dino.api.rest.DinoServerListResponse;
import org.odpi.openmetadata.viewservices.dino.api.rest.DinoServerOverviewResponse;
import org.odpi.openmetadata.viewservices.dino.api.rest.DinoServerRequestBody;
import org.odpi.openmetadata.viewservices.dino.api.rest.DinoServerTypeResponse;
import org.odpi.openmetadata.viewservices.dino.api.rest.DinoServiceListResponse;
import org.odpi.openmetadata.viewservices.dino.api.rest.DinoStringResponse;
import org.odpi.openmetadata.viewservices.dino.handlers.DinoViewHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;


/**
 * The DinoViewRESTServices provides the org.odpi.openmetadata.viewservices.tex.services implementation of the Type Explorer Open Metadata
 * View Service (OMVS). This interface provides view interfaces for enterprise architects.
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

        try {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            DinoViewHandler handler = instanceHandler.getDinoViewHandler(userId, serverName, methodName);

            Map<String, List<ResourceEndpoint>> lists =  handler.getResourceEndpoints(userId, methodName);
            response.setPlatformList(lists.get("platformList"));
            response.setServerList(lists.get("serverList"));

        } catch (InvalidParameterException error) {
            restExceptionHandler.captureInvalidParameterException(response, error);
        } catch (PropertyServerException error) {
            restExceptionHandler.capturePropertyServerException(response, error);
        } catch (UserNotAuthorizedException error) {
            restExceptionHandler.captureUserNotAuthorizedException(response, error);
        } catch (Throwable error) {
            restExceptionHandler.captureThrowable(response, error, methodName, auditLog);
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

        // The serverName parameter to the RequestSummary is the target server not the server running the VS
        RequestSummary request = new RequestSummary(requestBody.getPlatformName(), null, methodName);
        response.setRequestSummary(request);

        AuditLog auditLog = null;

        try {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null) {
                DinoViewHandler handler = instanceHandler.getDinoViewHandler(userId, serverName, methodName);

                response.setPlatformOverview(handler.platformGetOverview(userId,
                                                             requestBody.getPlatformName(),
                                                             methodName));
            }
        } catch (InvalidParameterException error) {
            restExceptionHandler.captureInvalidParameterException(response, error);
        } catch (PropertyServerException error) {
            restExceptionHandler.capturePropertyServerException(response, error);
        } catch (UserNotAuthorizedException error) {
            restExceptionHandler.captureUserNotAuthorizedException(response, error);
        } catch (Throwable error) {
            restExceptionHandler.captureThrowable(response, error, methodName, auditLog);
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
    public DinoStringResponse platformGetOrigin(String serverName, String userId, DinoPlatformRequestBody requestBody) {

        final String methodName = "platformGetOrigin";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        DinoStringResponse response = new DinoStringResponse();

        // The serverName parameter to the RequestSummary is the target server not the server running the VS
        RequestSummary request = new RequestSummary(requestBody.getPlatformName(), null, methodName);
        response.setRequestSummary(request);

        AuditLog auditLog = null;

        try {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null) {
                DinoViewHandler handler = instanceHandler.getDinoViewHandler(userId, serverName, methodName);

                response.setString(handler.platformGetOrigin(userId,
                                                             requestBody.getPlatformName(),
                                                             methodName));
            }
        } catch (InvalidParameterException error) {
            restExceptionHandler.captureInvalidParameterException(response, error);
        } catch (PropertyServerException error) {
            restExceptionHandler.capturePropertyServerException(response, error);
        } catch (UserNotAuthorizedException error) {
            restExceptionHandler.captureUserNotAuthorizedException(response, error);
        } catch (Throwable error) {
            restExceptionHandler.captureThrowable(response, error, methodName, auditLog);
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

        // The serverName parameter to the RequestSummary is the target server not the server running the VS
        RequestSummary request = new RequestSummary(requestBody.getPlatformName(), null, methodName);
        response.setRequestSummary(request);

        AuditLog auditLog = null;

        try {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null) {
                DinoViewHandler handler = instanceHandler.getDinoViewHandler(userId, serverName, methodName);

                response.setServerList(handler.platformGetActiveServerList(userId,
                                                                           requestBody.getPlatformName(),
                                                                           methodName));
            }
        } catch (InvalidParameterException error) {
            restExceptionHandler.captureInvalidParameterException(response, error);
        } catch (PropertyServerException error) {
            restExceptionHandler.capturePropertyServerException(response, error);
        } catch (UserNotAuthorizedException error) {
            restExceptionHandler.captureUserNotAuthorizedException(response, error);
        } catch (Throwable error) {
            restExceptionHandler.captureThrowable(response, error, methodName, auditLog);
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

        // The serverName parameter to the RequestSummary is the target server not the server running the VS
        RequestSummary request = new RequestSummary(requestBody.getPlatformName(), null, methodName);
        response.setRequestSummary(request);

        AuditLog auditLog = null;

        try {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null) {
                DinoViewHandler handler = instanceHandler.getDinoViewHandler(userId, serverName, methodName);

                response.setServerList(handler.platformGetKnownServerList(userId,
                                                                           requestBody.getPlatformName(),
                                                                           methodName));
            }
        } catch (InvalidParameterException error) {
            restExceptionHandler.captureInvalidParameterException(response, error);
        } catch (PropertyServerException error) {
            restExceptionHandler.capturePropertyServerException(response, error);
        } catch (UserNotAuthorizedException error) {
            restExceptionHandler.captureUserNotAuthorizedException(response, error);
        } catch (Throwable error) {
            restExceptionHandler.captureThrowable(response, error, methodName, auditLog);
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

        // The serverName parameter to the RequestSummary is the target server not the server running the VS
        RequestSummary request = new RequestSummary(requestBody.getPlatformName(), null, methodName);
        response.setRequestSummary(request);

        AuditLog auditLog = null;

        try {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null) {
                DinoViewHandler handler = instanceHandler.getDinoViewHandler(userId, serverName, methodName);

                response.setServiceList(handler.platformGetAccessServiceList(userId,
                                                                          requestBody.getPlatformName(),
                                                                          methodName));
            }
        } catch (InvalidParameterException error) {
            restExceptionHandler.captureInvalidParameterException(response, error);
        } catch (PropertyServerException error) {
            restExceptionHandler.capturePropertyServerException(response, error);
        } catch (UserNotAuthorizedException error) {
            restExceptionHandler.captureUserNotAuthorizedException(response, error);
        } catch (Throwable error) {
            restExceptionHandler.captureThrowable(response, error, methodName, auditLog);
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

        // The serverName parameter to the RequestSummary is the target server not the server running the VS
        RequestSummary request = new RequestSummary(requestBody.getPlatformName(), null, methodName);
        response.setRequestSummary(request);

        AuditLog auditLog = null;

        try {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null) {
                DinoViewHandler handler = instanceHandler.getDinoViewHandler(userId, serverName, methodName);

                response.setServiceList(handler.platformGetViewServiceList(userId,
                                                                             requestBody.getPlatformName(),
                                                                             methodName));
            }
        } catch (InvalidParameterException error) {
            restExceptionHandler.captureInvalidParameterException(response, error);
        } catch (PropertyServerException error) {
            restExceptionHandler.capturePropertyServerException(response, error);
        } catch (UserNotAuthorizedException error) {
            restExceptionHandler.captureUserNotAuthorizedException(response, error);
        } catch (Throwable error) {
            restExceptionHandler.captureThrowable(response, error, methodName, auditLog);
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

        // The serverName parameter to the RequestSummary is the target server not the server running the VS
        RequestSummary request = new RequestSummary(requestBody.getPlatformName(), null, methodName);
        response.setRequestSummary(request);

        AuditLog auditLog = null;

        try {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null) {
                DinoViewHandler handler = instanceHandler.getDinoViewHandler(userId, serverName, methodName);

                response.setServiceList(handler.platformGetGovernanceServiceList(userId,
                                                                             requestBody.getPlatformName(),
                                                                             methodName));
            }
        } catch (InvalidParameterException error) {
            restExceptionHandler.captureInvalidParameterException(response, error);
        } catch (PropertyServerException error) {
            restExceptionHandler.capturePropertyServerException(response, error);
        } catch (UserNotAuthorizedException error) {
            restExceptionHandler.captureUserNotAuthorizedException(response, error);
        } catch (Throwable error) {
            restExceptionHandler.captureThrowable(response, error, methodName, auditLog);
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

        // The serverName parameter to the RequestSummary is the target server not the server running the VS
        RequestSummary request = new RequestSummary(requestBody.getPlatformName(), null, methodName);
        response.setRequestSummary(request);

        AuditLog auditLog = null;

        try {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null) {
                DinoViewHandler handler = instanceHandler.getDinoViewHandler(userId, serverName, methodName);

                response.setServiceList(handler.platformGetCommonServiceList(userId,
                                                                             requestBody.getPlatformName(),
                                                                             methodName));
            }
        } catch (InvalidParameterException error) {
            restExceptionHandler.captureInvalidParameterException(response, error);
        } catch (PropertyServerException error) {
            restExceptionHandler.capturePropertyServerException(response, error);
        } catch (UserNotAuthorizedException error) {
            restExceptionHandler.captureUserNotAuthorizedException(response, error);
        } catch (Throwable error) {
            restExceptionHandler.captureThrowable(response, error, methodName, auditLog);
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

        // The serverName parameter to the RequestSummary is the target server not the server running the VS
        RequestSummary request = new RequestSummary(requestBody.getPlatformName(), requestBody.getServerName(), methodName);
        response.setRequestSummary(request);

        AuditLog auditLog = null;

        try {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null) {
                DinoViewHandler handler = instanceHandler.getDinoViewHandler(userId, serverName, methodName);

                response.setServerOverview(handler.serverGetOverview(userId,
                                                                         requestBody.getServerName(),
                                                                         requestBody.getPlatformName(),
                                                                         requestBody.getServerInstanceName(),
                                                                         requestBody.getDescription(),
                                                                         methodName));


            }
        } catch (InvalidParameterException error) {
            restExceptionHandler.captureInvalidParameterException(response, error);
        } catch (PropertyServerException error) {
            restExceptionHandler.capturePropertyServerException(response, error);
        } catch (UserNotAuthorizedException error) {
            restExceptionHandler.captureUserNotAuthorizedException(response, error);
        } catch (Throwable error) {
            restExceptionHandler.captureThrowable(response, error, methodName, auditLog);
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

        // The serverName parameter to the RequestSummary is the target server not the server running the VS
        RequestSummary request = new RequestSummary(requestBody.getPlatformName(), requestBody.getServerName(), methodName);
        response.setRequestSummary(request);

        AuditLog auditLog = null;

        try {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null) {
                DinoViewHandler handler = instanceHandler.getDinoViewHandler(userId, serverName, methodName);

                response.setString(handler.serverGetOrigin(userId,
                                                           requestBody.getServerName(),
                                                           requestBody.getPlatformName(),
                                                           methodName));
            }
        } catch (InvalidParameterException error) {
            restExceptionHandler.captureInvalidParameterException(response, error);
        } catch (PropertyServerException error) {
            restExceptionHandler.capturePropertyServerException(response, error);
        } catch (UserNotAuthorizedException error) {
            restExceptionHandler.captureUserNotAuthorizedException(response, error);
        } catch (Throwable error) {
            restExceptionHandler.captureThrowable(response, error, methodName, auditLog);
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

        // The serverName parameter to the RequestSummary is the target server not the server running the VS
        RequestSummary request = new RequestSummary(requestBody.getPlatformName(), requestBody.getServerName(), methodName);
        response.setRequestSummary(request);

        AuditLog auditLog = null;

        try {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null) {
                DinoViewHandler handler = instanceHandler.getDinoViewHandler(userId, serverName, methodName);

                ServerTypeClassificationSummary summary = handler.serverGetTypeClassification(userId,
                                                                                              requestBody.getServerName(),
                                                                                              requestBody.getPlatformName(),
                                                                                              methodName);
                response.setServerTypeName(summary.getServerTypeName());
                response.setServerTypeDescription(summary.getServerTypeDescription());

            }
        } catch (InvalidParameterException error) {
            restExceptionHandler.captureInvalidParameterException(response, error);
        } catch (PropertyServerException error) {
            restExceptionHandler.capturePropertyServerException(response, error);
        } catch (UserNotAuthorizedException error) {
            restExceptionHandler.captureUserNotAuthorizedException(response, error);
        } catch (Throwable error) {
            restExceptionHandler.captureThrowable(response, error, methodName, auditLog);
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
     * @return response     the configuration for the server.  TODO - currently using String - should be a configuration response object...
     *
     */
    public DinoServerConfigResponse serverGetStoredConfiguration(String serverName, String userId, DinoServerRequestBody requestBody) {

        final String methodName = "serverGetConfiguration";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        DinoServerConfigResponse response = new DinoServerConfigResponse();

        // The serverName parameter to the RequestSummary is the target server not the server running the VS
        RequestSummary request = new RequestSummary(requestBody.getPlatformName(), requestBody.getServerName(), methodName);
        response.setRequestSummary(request);

        AuditLog auditLog = null;

        try {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null) {
                DinoViewHandler handler = instanceHandler.getDinoViewHandler(userId, serverName, methodName);

                response.setServerConfig(handler.serverGetStoredConfiguration(userId,
                                                                        requestBody.getServerName(),
                                                                        requestBody.getPlatformName(),
                                                                        methodName));
            }
        } catch (InvalidParameterException error) {
            restExceptionHandler.captureInvalidParameterException(response, error);
        } catch (PropertyServerException error) {
            restExceptionHandler.capturePropertyServerException(response, error);
        } catch (UserNotAuthorizedException error) {
            restExceptionHandler.captureUserNotAuthorizedException(response, error);
        } catch (Throwable error) {
            restExceptionHandler.captureThrowable(response, error, methodName, auditLog);
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
     * @return response     the configuration for the server.  TODO - currently using String - should be a configuration response object...
     *
     */
    public DinoServerConfigResponse serverGetInstanceConfiguration(String serverName, String userId, DinoServerRequestBody requestBody) {

        final String methodName = "serverGetInstanceConfiguration";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        DinoServerConfigResponse response = new DinoServerConfigResponse();

        // The serverName parameter to the RequestSummary is the target server not the server running the VS
        RequestSummary request = new RequestSummary(requestBody.getPlatformName(), requestBody.getServerName(), methodName);
        response.setRequestSummary(request);

        AuditLog auditLog = null;

        try {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null) {
                DinoViewHandler handler = instanceHandler.getDinoViewHandler(userId, serverName, methodName);

                response.setServerConfig(handler.serverGetInstanceConfiguration(userId,
                                                                        requestBody.getServerName(),
                                                                        requestBody.getPlatformName(),
                                                                        methodName));
            }
        } catch (InvalidParameterException error) {
            restExceptionHandler.captureInvalidParameterException(response, error);
        } catch (PropertyServerException error) {
            restExceptionHandler.capturePropertyServerException(response, error);
        } catch (UserNotAuthorizedException error) {
            restExceptionHandler.captureUserNotAuthorizedException(response, error);
        } catch (Throwable error) {
            restExceptionHandler.captureThrowable(response, error, methodName, auditLog);
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
     * @return response     the configuration for the server.  TODO - currently using String - should be a configuration response object...
     *
     */
    public DinoServerDoubleConfigResponse serverGetStoredAndActiveConfiguration(String serverName, String userId, DinoServerRequestBody requestBody) {

        final String methodName = "serverGetInstanceConfiguration";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        DinoServerDoubleConfigResponse response = new DinoServerDoubleConfigResponse();

        // The serverName parameter to the RequestSummary is the target server not the server running the VS
        RequestSummary request = new RequestSummary(requestBody.getPlatformName(), requestBody.getServerName(), methodName);
        response.setRequestSummary(request);

        AuditLog auditLog = null;

        try {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null) {
                DinoViewHandler handler = instanceHandler.getDinoViewHandler(userId, serverName, methodName);

                response.setStoredConfig(handler.serverGetStoredConfiguration(userId,
                                                                                requestBody.getServerName(),
                                                                                requestBody.getPlatformName(),
                                                                                methodName));

                response.setActiveConfig(handler.serverGetInstanceConfiguration(userId,
                                                                              requestBody.getServerName(),
                                                                              requestBody.getPlatformName(),
                                                                              methodName));


            }
        } catch (PropertyServerException error) {
            /*
             * You may well get this exception if the server is not running - and we asked it for its instance configuration
             * In this case you will get an error exception in which the 'cause' has a reportedErrorMessageId of OMAG-MULTI-TENANT-404-001.
             * In this specific case only, tolerate the error and pass back a null in the response for activeConfig. For any other error codes
             * report the exception.
             */
            boolean tolerate = false;
            if (error.getCause() != null && error.getCause() instanceof OCFCheckedExceptionBase) {
                OCFCheckedExceptionBase cause = (OCFCheckedExceptionBase)(error.getCause());
                if (cause.getReportedErrorMessageId().equals("OMAG-MULTI-TENANT-404-001")) {
                    tolerate = true;
                    response.setActiveConfig(null);
                }
            }
            if (!tolerate)
              restExceptionHandler.capturePropertyServerException(response, error);

        } catch (InvalidParameterException error) {
            restExceptionHandler.captureInvalidParameterException(response, error);
        }
         catch (UserNotAuthorizedException error) {
            restExceptionHandler.captureUserNotAuthorizedException(response, error);
        } catch (Throwable error) {
            restExceptionHandler.captureThrowable(response, error, methodName, auditLog);
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
     * @return response     the configuration for the server.  TODO - currently using String - should be a configuration response object...
     *
     */
    public DinoServerAuditLogResponse serverGetAuditLog(String serverName, String userId, DinoServerRequestBody requestBody) {

        final String methodName = "serverGetAuditLog";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        DinoServerAuditLogResponse response = new DinoServerAuditLogResponse();

        // The serverName parameter to the RequestSummary is the target server not the server running the VS
        RequestSummary request = new RequestSummary(requestBody.getPlatformName(), requestBody.getServerName(), methodName);
        response.setRequestSummary(request);

        AuditLog auditLog = null;

        try {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null) {
                DinoViewHandler handler = instanceHandler.getDinoViewHandler(userId, serverName, methodName);

                response.setAuditLog(handler.serverGetAuditLog(userId,
                                                               requestBody.getServerName(),
                                                               requestBody.getPlatformName(),
                                                               methodName));

            }
        } catch (PropertyServerException error) {
            restExceptionHandler.capturePropertyServerException(response, error);
        } catch (InvalidParameterException error) {
            restExceptionHandler.captureInvalidParameterException(response, error);
        }
        catch (UserNotAuthorizedException error) {
            restExceptionHandler.captureUserNotAuthorizedException(response, error);
        } catch (Throwable error) {
            restExceptionHandler.captureThrowable(response, error, methodName, auditLog);
        }

        log.debug("Returning from method: " + methodName + " with response: " + response.toString());

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }




}
