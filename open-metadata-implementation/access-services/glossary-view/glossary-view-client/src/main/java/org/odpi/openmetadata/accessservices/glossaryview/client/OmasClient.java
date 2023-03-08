/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.glossaryview.client;

import org.odpi.openmetadata.accessservices.glossaryview.exception.GlossaryViewOmasException;
import org.odpi.openmetadata.accessservices.glossaryview.rest.GlossaryViewEntityDetailResponse;
import org.odpi.openmetadata.commonservices.ffdc.InvalidParameterHandler;
import org.odpi.openmetadata.commonservices.ffdc.exceptions.InvalidParameterException;
import org.odpi.openmetadata.commonservices.ffdc.rest.FFDCRESTClient;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;

/**
 * Responsible to provide some functionality to requests, like paging and error handling
 * Actual request is handled higher in the hierarchy
 */
class OmasClient extends FFDCRESTClient {

    private final InvalidParameterHandler invalidParameterHandler = new InvalidParameterHandler();
    private final GlossaryViewClientExceptionHandler glossaryViewClientExceptionHandler = new GlossaryViewClientExceptionHandler();

    protected OmasClient(String serverName, String serverPlatformRootURL) throws
            org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException{
        super(serverName, serverPlatformRootURL);
    }

    protected OmasClient(String serverName, String serverPlatformRootURL, String userId, String password) throws
            org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException{
        super(serverName, serverPlatformRootURL, userId, password);
    }

    /*
     * Meant to send a request to Glossary View Omas that returns a single entity in the response
     */
    protected GlossaryViewEntityDetailResponse getSingleEntityResponse(String methodName, String path, String serverName,
                                                                     String userId, String entityGUID)
            throws PropertyServerException, InvalidParameterException, GlossaryViewOmasException {

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(entityGUID, "entityGUID", methodName);

        GlossaryViewEntityDetailResponse response = callGetRESTCall(methodName, GlossaryViewEntityDetailResponse.class,
                serverPlatformURLRoot + path, serverName, userId, entityGUID);

        glossaryViewClientExceptionHandler.detectAndThrowGlossaryViewOmasException(methodName, response);

        return response;
    }

    /*
     * Meant to send a request to Glossary View Omas that returns multiple related entities in the response. Supports pagination
     */
    protected GlossaryViewEntityDetailResponse getMultipleRelatedEntitiesPagedResponse(String methodName, String path, String serverName,
                                                                                     String userId, String entityGUID, Integer from, Integer size)
            throws PropertyServerException, InvalidParameterException, GlossaryViewOmasException{

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(entityGUID, "entityGUID", methodName);

        GlossaryViewEntityDetailResponse response = callGetRESTCall(methodName, GlossaryViewEntityDetailResponse.class,
                serverPlatformURLRoot + path, serverName, userId, entityGUID, from, size);

        glossaryViewClientExceptionHandler.detectAndThrowGlossaryViewOmasException(methodName, response);

        return response;
    }

    /*
     * Meant to send a request to Glossary View Omas that returns multiple entities in the response. Supports pagination
     */
    protected GlossaryViewEntityDetailResponse getMultipleEntitiesPagedResponse(String methodName, String path, String serverName,
                                                                              String userId, Integer from, Integer size)
            throws PropertyServerException, InvalidParameterException, GlossaryViewOmasException{

        invalidParameterHandler.validateUserId(userId, methodName);

        GlossaryViewEntityDetailResponse response = callGetRESTCall(methodName, GlossaryViewEntityDetailResponse.class,
                serverPlatformURLRoot + path, serverName, userId, from, size);

        glossaryViewClientExceptionHandler.detectAndThrowGlossaryViewOmasException(methodName, response);

        return response;
    }

}
