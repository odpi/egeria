/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria category. */
package org.odpi.openmetadata.viewservices.rex.server;


import org.odpi.openmetadata.commonservices.ffdc.RESTCallLogger;
import org.odpi.openmetadata.commonservices.ffdc.RESTCallToken;
import org.odpi.openmetadata.commonservices.ffdc.RESTExceptionHandler;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.viewservices.rex.api.rest.RexEntityDetailResponse;
import org.odpi.openmetadata.viewservices.rex.api.rest.RexEntityRequestBody;
import org.odpi.openmetadata.viewservices.rex.api.rest.RexSearchBody;
import org.odpi.openmetadata.viewservices.rex.api.rest.RexSearchResponse;
import org.odpi.openmetadata.viewservices.rex.api.rest.RexTypesRequestBody;
import org.odpi.openmetadata.viewservices.rex.api.rest.TypeExplorerResponse;
import org.odpi.openmetadata.viewservices.rex.handlers.RexViewHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;


/**
 * The RexViewRESTServices provides the org.odpi.openmetadata.viewservices.rex.services implementation of the Repository Explorer Open Metadata
 * View Service (OMVS). This interface provides view interfaces for enterprise architects.
 */

public class RexViewRESTServices {

    protected static RexViewInstanceHandler instanceHandler       = new RexViewInstanceHandler();

    private static RESTExceptionHandler     restExceptionHandler  = new RESTExceptionHandler();

    private static RESTCallLogger           restCallLogger        = new RESTCallLogger(LoggerFactory.getLogger(RexViewRESTServices.class),
                                                                                       instanceHandler.getServiceName());

    private static final Logger             log                   = LoggerFactory.getLogger(RexViewRESTServices.class);


    /**
     * Default constructor
     */
    public RexViewRESTServices() {

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

    public TypeExplorerResponse getTypeExplorer(String serverName, String userId, RexTypesRequestBody requestBody)
    {

        final String methodName = "getTypeExplorer";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        TypeExplorerResponse response = new TypeExplorerResponse();

        AuditLog auditLog = null;

        try {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null) {
                RexViewHandler handler = instanceHandler.getRexViewHandler(userId, serverName, methodName);

                response.setTypeExplorer(handler.getTypeExplorer(userId,
                                                                 requestBody.getServerName(),
                                                                 requestBody.getServerURLRoot(),
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

    public RexEntityDetailResponse getEntity(String serverName, String userId, RexEntityRequestBody requestBody)
    {

        final String methodName = "getEntity";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        RexEntityDetailResponse response = new RexEntityDetailResponse();

        AuditLog auditLog = null;

        try {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null) {
                RexViewHandler handler = instanceHandler.getRexViewHandler(userId, serverName, methodName);

                response.setExpandedEntityDetail(handler.getEntity(userId,
                                                                   requestBody.getServerName(),
                                                                   requestBody.getServerURLRoot(),
                                                                   requestBody.getEnterpriseOption(),
                                                                   requestBody.getEntityGUID(),
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

        AuditLog auditLog = null;

        try {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null) {
                RexViewHandler handler = instanceHandler.getRexViewHandler(userId, serverName, methodName);

                response.setEntities(handler.findEntities(userId,
                                                          requestBody.getServerName(),
                                                          requestBody.getServerURLRoot(),
                                                          requestBody.getEnterpriseOption(),
                                                          requestBody.getSearchText(),
                                                          requestBody.getTypeName(),
                                                          methodName));

                response.setSearchCategory("Entity");
                response.setSearchText(requestBody.getSearchText());
                response.setServerName(requestBody.getServerName());
                // TODO - do you want to add the typeName to the response object??  - useful for history??


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
