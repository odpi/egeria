/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.assetconsumer.client.rest;

import org.odpi.openmetadata.accessservices.assetconsumer.rest.GlossaryTermListResponse;
import org.odpi.openmetadata.accessservices.assetconsumer.rest.GlossaryTermResponse;
import org.odpi.openmetadata.accessservices.assetconsumer.rest.TagResponse;
import org.odpi.openmetadata.accessservices.assetconsumer.rest.TagsResponse;
import org.odpi.openmetadata.commonservices.ocf.metadatamanagement.client.OCFRESTClient;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;


/**
 * AssetConsumerRESTClient is responsible for issuing calls to the OMAG REST APIs.
 */
public class AssetConsumerRESTClient extends OCFRESTClient
{
    /**
     * Constructor for no authentication with audit log.
     *
     * @param serverName name of the OMAG Server to call
     * @param serverPlatformURLRoot URL root of the server platform where the OMAG Server is running.
     * @param auditLog destination for log messages.
     *
     * @throws InvalidParameterException there is a problem creating the client-side components to issue any
     * REST API calls.
     */
    public AssetConsumerRESTClient(String    serverName,
                                   String    serverPlatformURLRoot,
                                   AuditLog auditLog) throws InvalidParameterException
    {
        super(serverName, serverPlatformURLRoot, auditLog);
    }


    /**
     * Constructor for no authentication.
     *
     * @param serverName name of the OMAG Server to call
     * @param serverPlatformURLRoot URL root of the server platform where the OMAG Server is running.
     * @throws InvalidParameterException there is a problem creating the client-side components to issue any
     * REST API calls.
     */
    public AssetConsumerRESTClient(String serverName,
                                   String serverPlatformURLRoot) throws InvalidParameterException
    {
        super(serverName, serverPlatformURLRoot);
    }


    /**
     * Constructor for simple userId and password authentication with audit log.
     *
     * @param serverName name of the OMAG Server to call
     * @param serverPlatformURLRoot URL root of the server platform where the OMAG Server is running.
     * @param userId user id for the HTTP request
     * @param password password for the HTTP request
     * @param auditLog destination for log messages.
     * @throws InvalidParameterException there is a problem creating the client-side components to issue any
     * REST API calls.
     */
    public AssetConsumerRESTClient(String   serverName,
                                   String   serverPlatformURLRoot,
                                   String   userId,
                                   String   password,
                                   AuditLog auditLog) throws InvalidParameterException
    {
        super(serverName, serverPlatformURLRoot, userId, password, auditLog);
    }


    /**
     * Constructor for simple userId and password authentication.
     *
     * @param serverName name of the OMAG Server to call
     * @param serverPlatformURLRoot URL root of the server platform where the OMAG Server is running.
     * @param userId user id for the HTTP request
     * @param password password for the HTTP request
     * @throws InvalidParameterException there is a problem creating the client-side components to issue any
     * REST API calls.
     */
    public AssetConsumerRESTClient(String serverName,
                                   String serverPlatformURLRoot,
                                   String userId,
                                   String password) throws InvalidParameterException
    {
        super(serverName, serverPlatformURLRoot, userId, password);
    }




    /**
     * Issue a GET REST call that returns a GlossaryTermResponse object.
     *
     * @param methodName  name of the method being called.
     * @param urlTemplate template of the URL for the REST API call with place-holders for the parameters.
     * @param params      a list of parameters that are slotted into the url template.
     *
     * @return GlossaryTermResponse
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws UserNotAuthorizedException the user is not authorized to make this request.
     * @throws PropertyServerException the repository is not available or not working properly.
     */
    public GlossaryTermResponse callGlossaryTermGetRESTCall(String    methodName,
                                                            String    urlTemplate,
                                                            Object... params) throws InvalidParameterException,
                                                                                     UserNotAuthorizedException,
                                                                                     PropertyServerException
    {
        GlossaryTermResponse restResult = this.callGetRESTCall(methodName, GlossaryTermResponse.class, urlTemplate, params);

        exceptionHandler.detectAndThrowStandardExceptions(methodName, restResult);

        return restResult;
    }


    /**
     * Issue a GET REST call that returns a GlossaryTermListResponse object.
     *
     * @param methodName  name of the method being called.
     * @param urlTemplate template of the URL for the REST API call with place-holders for the parameters.
     * @param requestBody request body describing the value to search for
     * @param params      a list of parameters that are slotted into the url template.
     *
     * @return GlossaryTermListResponse
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws UserNotAuthorizedException the user is not authorized to make this request.
     * @throws PropertyServerException the repository is not available or not working properly.
     */
    public GlossaryTermListResponse callGlossaryTermListPostRESTCall(String    methodName,
                                                                     String    urlTemplate,
                                                                     Object    requestBody,
                                                                     Object... params) throws InvalidParameterException,
                                                                                              UserNotAuthorizedException,
                                                                                              PropertyServerException
    {
        GlossaryTermListResponse restResult = this.callPostRESTCall(methodName, GlossaryTermListResponse.class, urlTemplate, requestBody, params);

        exceptionHandler.detectAndThrowStandardExceptions(methodName, restResult);

        return restResult;
    }


    /**
     * Issue a GET REST call that returns a TagResponse object.
     *
     * @param methodName  name of the method being called.
     * @param urlTemplate template of the URL for the REST API call with place-holders for the parameters.
     * @param params      a list of parameters that are slotted into the url template.
     *
     * @return TagResponse
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws UserNotAuthorizedException the user is not authorized to make this request.
     * @throws PropertyServerException something went wrong with the REST call stack.
     */
    public TagResponse callInformalTagGetRESTCall(String    methodName,
                                                  String    urlTemplate,
                                                  Object... params) throws InvalidParameterException,
                                                                           UserNotAuthorizedException,
                                                                           PropertyServerException
    {
        TagResponse restResult = this.callGetRESTCall(methodName, TagResponse.class, urlTemplate, params);

        exceptionHandler.detectAndThrowStandardExceptions(methodName, restResult);

        return restResult;
    }


    /**
     * Issue a GET REST call that returns a TagsResponse object.
     *
     * @param methodName  name of the method being called.
     * @param urlTemplate template of the URL for the REST API call with place-holders for the parameters.
     * @param params      a list of parameters that are slotted into the url template.
     *
     * @return TagsResponse
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws UserNotAuthorizedException the user is not authorized to make this request.
     * @throws PropertyServerException something went wrong with the REST call stack.
     */
    public TagsResponse callInformalTagListGetRESTCall(String    methodName,
                                                       String    urlTemplate,
                                                       Object... params) throws InvalidParameterException,
                                                                                UserNotAuthorizedException,
                                                                                PropertyServerException
    {
        TagsResponse restResult = this.callGetRESTCall(methodName, TagsResponse.class, urlTemplate, params);

        exceptionHandler.detectAndThrowStandardExceptions(methodName, restResult);

        return restResult;
    }



    /**
     * Issue a GET REST call that returns a TagsResponse object.
     *
     * @param methodName  name of the method being called.
     * @param urlTemplate template of the URL for the REST API call with place-holders for the parameters.
     * @param requestBody request body for the REST API call
     * @param params      a list of parameters that are slotted into the url template.
     *
     * @return TagsResponse
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws UserNotAuthorizedException the user is not authorized to make this request.
     * @throws PropertyServerException something went wrong with the REST call stack.
     */
    public TagsResponse callInformalTagListPostRESTCall(String    methodName,
                                                        String    urlTemplate,
                                                        Object    requestBody,
                                                        Object... params) throws InvalidParameterException,
                                                                                 UserNotAuthorizedException,
                                                                                 PropertyServerException
    {
        TagsResponse restResult =  this.callPostRESTCall(methodName, TagsResponse.class, urlTemplate, requestBody, params);

        exceptionHandler.detectAndThrowStandardExceptions(methodName, restResult);

        return restResult;
    }
}
