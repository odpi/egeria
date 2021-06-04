/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria category. */
package org.odpi.openmetadata.viewservices.tex.server;


import org.odpi.openmetadata.viewservices.tex.api.ffdc.TexExceptionHandler;
import org.odpi.openmetadata.viewservices.tex.api.ffdc.TexViewErrorCode;
import org.odpi.openmetadata.viewservices.tex.api.ffdc.TexViewServiceException;
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

    protected static TexViewInstanceHandler instanceHandler = new TexViewInstanceHandler();

    private static RESTExceptionHandler restExceptionHandler = new RESTExceptionHandler();

    private static RESTCallLogger restCallLogger = new RESTCallLogger(LoggerFactory.getLogger(TexViewRESTServices.class),
                                                                      instanceHandler.getServiceName());

    private static final Logger log = LoggerFactory.getLogger(TexViewRESTServices.class);


    /**
     * Default constructor
     */
    public TexViewRESTServices()
    {

    }


    /**
     * Retrieve platform origin
     *
     * @param serverName name of the local view server.
     * @param userId     userId under which the request is performed
     * @return response     the list of resource endpoints configured for the view service
     */
    public TexResourceEndpointListResponse getResourceEndpointList(String serverName, String userId)
    {

        final String methodName = "getResourceEndpointList";

        TexResourceEndpointListResponse response = new TexResourceEndpointListResponse();

        AuditLog auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            TexViewHandler handler = instanceHandler.getTexViewHandler(userId, serverName, methodName);

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
        catch (Exception exception)
        {
            restExceptionHandler.captureThrowable(response, exception, methodName, auditLog);
        }

        log.debug("Returning from method: " + methodName + " with response: " + response.toString());

        return response;

    }


    /**
     * Load types
     *
     * @param serverName  name of the local view server.
     * @param userId      userId under which the request is performed
     * @param requestBody request body
     * @return response     the repository's type information or exception information
     */

    public TypeExplorerResponse getTypeExplorer(String serverName, String userId, TexTypesRequestBody requestBody)
    {

        final String methodName = "getTypeExplorer";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        TypeExplorerResponse response = new TypeExplorerResponse();

        if (requestBody != null)
        {

            AuditLog auditLog = null;
            TexViewHandler handler = null;

            try
            {
                auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

                handler = instanceHandler.getTexViewHandler(userId, serverName, methodName);

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
            catch ( Exception exception)
            {
                restExceptionHandler.captureThrowable(response, exception, methodName, auditLog);
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
                                                                 requestBody.getDeprecationOption(),
                                                                 methodName));

            }
            catch (TexViewServiceException exception)
            {
                TexExceptionHandler.captureCheckedException(response, exception, exception.getClass().getName());
            }
            catch ( Exception exception)
            {
                restExceptionHandler.captureThrowable(response, exception, methodName, auditLog);
            }
        }
        else
        {
            /*
             * Raise (and immediately capture) a RexViewServicesException
             */
            TexViewServiceException exception = new TexViewServiceException(TexViewErrorCode.VIEW_SERVICE_REQUEST_BODY_MISSING.getMessageDefinition(),
                                                                        this.getClass().getName(),
                                                                        methodName);

            TexExceptionHandler.captureCheckedException(response, exception, exception.getClass().getName());
        }


        log.debug("Returning from method: " + methodName + " with response: " + response.toString());

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;

    }


}
