/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria category. */
package org.odpi.openmetadata.viewservices.tex.server;


import org.odpi.openmetadata.viewservices.tex.api.properties.ResourceEndpoint;
import org.odpi.openmetadata.commonservices.ffdc.RESTCallLogger;
import org.odpi.openmetadata.commonservices.ffdc.RESTCallToken;
import org.odpi.openmetadata.commonservices.ffdc.RESTExceptionHandler;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.viewservices.tex.api.rest.TexResourceEndpointListResponse;
import org.odpi.openmetadata.viewservices.tex.api.rest.TexTypesRequestBody;
import org.odpi.openmetadata.viewservices.tex.api.rest.TypeExplorerResponse;
import org.odpi.openmetadata.viewservices.tex.handlers.TexViewHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;


/**
 * The TexViewRESTServices provides the org.odpi.openmetadata.viewservices.tex.services implementation of the Type Explorer Open Metadata
 * View Service (OMVS). This interface provides view interfaces for enterprise architects.
 */

public class TexViewRESTServices {

    protected static TexViewInstanceHandler instanceHandler       = new TexViewInstanceHandler();

    private static RESTExceptionHandler     restExceptionHandler  = new RESTExceptionHandler();

    private static RESTCallLogger           restCallLogger        = new RESTCallLogger(LoggerFactory.getLogger(TexViewRESTServices.class),
                                                                                       instanceHandler.getServiceName());

    private static final Logger             log                   = LoggerFactory.getLogger(TexViewRESTServices.class);


    /**
     * Default constructor
     */
    public TexViewRESTServices() {

    }


    /**
     * Retrieve platform origin
     *
     * @param serverName    name of the local view server.
     * @param userId        userId under which the request is performed
     * @return response     the list of resource endpoints configured for the view service
     *
     */
    public TexResourceEndpointListResponse getResourceEndpointList(String serverName, String userId)
    {

        final String methodName = "getResourceEndpointList";

        TexResourceEndpointListResponse response = new TexResourceEndpointListResponse();

        AuditLog auditLog = null;

        try {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            TexViewHandler handler = instanceHandler.getTexViewHandler(userId, serverName, methodName);

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
     * Load types
     *
     * @param serverName    name of the local view server.
     * @param userId        userId under which the request is performed
     * @param requestBody   request body
     * @return response     the repository's type information or exception information
     *
     * <ul>
     * <li> InvalidParameterException            one of the parameters is null or invalid.
     * </ul>
     */

    public TypeExplorerResponse getTypeExplorer(String serverName, String userId, TexTypesRequestBody requestBody)
    {

        final String methodName = "getTypeExplorer";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        TypeExplorerResponse response = new TypeExplorerResponse();

        // The serverName parameter to the RequestSummary is the target server not the server running the VS
        //RequestSummary request = new RequestSummary(requestBody.getPlatformName(), null, methodName);
        //response.setRequestSummary(request);

        AuditLog auditLog = null;

        try {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null) {
                TexViewHandler handler = instanceHandler.getTexViewHandler(userId, serverName, methodName);

                response.setTypeExplorer(handler.getTypeExplorer(userId,
                                                                 requestBody.getServerName(),
                                                                 requestBody.getPlatformName(),
                                                                 requestBody.getEnterpriseOption(),
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




}
