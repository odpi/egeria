/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.commonservices.ffdc.rest;

import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;


/**
 * RESTClient is responsible for issuing calls to the OMAS REST APIs.
 */
public class FFDCRESTClient extends FFDCRESTClientBase
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
    public FFDCRESTClient(String    serverName,
                          String    serverPlatformURLRoot,
                          AuditLog  auditLog) throws InvalidParameterException
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
    public FFDCRESTClient(String serverName,
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
    public FFDCRESTClient(String   serverName,
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
    public FFDCRESTClient(String serverName,
                          String serverPlatformURLRoot,
                          String userId,
                          String password) throws InvalidParameterException
    {
        super(serverName, serverPlatformURLRoot, userId, password);
    }


    /**
     * Issue a GET REST call that returns a list of names.
     *
     * @param methodName  name of the method being called.
     * @param urlTemplate template of the URL for the REST API with place-holders for the parameters.
     * @param params      a list of parameters that are slotted into the url template.
     *
     * @return NameListResponse
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws UserNotAuthorizedException the user is not authorized to make this request.
     * @throws PropertyServerException something went wrong with the REST call stack.
     */
    public NameListResponse callNameListGetRESTCall(String    methodName,
                                                    String    urlTemplate,
                                                    Object... params) throws InvalidParameterException,
                                                                             UserNotAuthorizedException,
                                                                             PropertyServerException
    {
        NameListResponse restResult = this.callGetRESTCall(methodName, NameListResponse.class, urlTemplate, params);

        exceptionHandler.detectAndThrowStandardExceptions(methodName, restResult);

        return restResult;
    }


    /**
     * Issue a GET REST call that returns a list of names mapped to their descriptions.
     *
     * @param methodName  name of the method being called.
     * @param urlTemplate template of the URL for the REST API with place-holders for the parameters.
     * @param params      a list of parameters that are slotted into the url template.
     *
     * @return StringMapResponse
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws UserNotAuthorizedException the user is not authorized to make this request.
     * @throws PropertyServerException something went wrong with the REST call stack.
     */
    public StringMapResponse callStringMapGetRESTCall(String    methodName,
                                                      String    urlTemplate,
                                                      Object... params) throws InvalidParameterException,
                                                                               UserNotAuthorizedException,
                                                                               PropertyServerException
    {
        StringMapResponse restResult = this.callGetRESTCall(methodName, StringMapResponse.class, urlTemplate, params);

        exceptionHandler.detectAndThrowStandardExceptions(methodName, restResult);

        return restResult;
    }


    /**
     * Issue a GET REST call that returns a GUIDResponse object.
     *
     * @param methodName  name of the method being called.
     * @param urlTemplate template of the URL for the REST API with place-holders for the parameters.
     * @param params      a list of parameters that are slotted into the url template.
     *
     * @return GUIDResponse
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws UserNotAuthorizedException the user is not authorized to make this request.
     * @throws PropertyServerException something went wrong with the REST call stack.
     */
    public    GUIDResponse callGUIDGetRESTCall(String    methodName,
                                               String    urlTemplate,
                                               Object... params) throws InvalidParameterException,
                                                                        UserNotAuthorizedException,
                                                                        PropertyServerException
    {
        GUIDResponse restResult = this.callGetRESTCall(methodName, GUIDResponse.class, urlTemplate, params);

        exceptionHandler.detectAndThrowStandardExceptions(methodName, restResult);

        return restResult;
    }



    /**
     * Issue a POST REST call that returns a guid object.
     *
     * @param methodName  name of the method being called
     * @param urlTemplate  template of the URL for the REST API with place-holders for the parameters.
     * @param requestBody request body for the request.
     * @param params  a list of parameters that are slotted into the url template.
     *
     * @return GUIDResponse
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws UserNotAuthorizedException the user is not authorized to make this request.
     * @throws PropertyServerException something went wrong with the REST call stack.
     */
    public   GUIDResponse callGUIDPostRESTCall(String    methodName,
                                               String    urlTemplate,
                                               Object    requestBody,
                                               Object... params) throws InvalidParameterException,
                                                                        UserNotAuthorizedException,
                                                                        PropertyServerException
    {
        GUIDResponse restResult = this.callPostRESTCall(methodName,
                                                        GUIDResponse.class,
                                                        urlTemplate,
                                                        requestBody,
                                                        params);

        exceptionHandler.detectAndThrowStandardExceptions(methodName, restResult);

        return restResult;
    }


    /**
     * Issue a GET REST call that returns a list of GUIDs object.
     *
     * @param methodName  name of the method being called.
     * @param urlTemplate template of the URL for the REST API with place-holders for the parameters.
     * @param params      a list of parameters that are slotted into the url template.
     *
     * @return GUIDListResponse
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws UserNotAuthorizedException the user is not authorized to make this request.
     * @throws PropertyServerException something went wrong with the REST call stack.
     */
    public GUIDListResponse callGUIDListGetRESTCall(String    methodName,
                                                    String    urlTemplate,
                                                    Object... params) throws InvalidParameterException,
                                                                             UserNotAuthorizedException,
                                                                             PropertyServerException
    {
        GUIDListResponse restResult = this.callGetRESTCall(methodName, GUIDListResponse.class, urlTemplate, params);

        exceptionHandler.detectAndThrowStandardExceptions(methodName, restResult);

        return restResult;
    }


    /**
     * Issue a POST REST call that returns a list of GUIDs object.
     *
     * @param methodName  name of the method being called.
     * @param urlTemplate template of the URL for the REST API with place-holders for the parameters.
     * @param requestBody request body for the request.
     * @param params      a list of parameters that are slotted into the url template.
     *
     * @return GUIDListResponse
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws UserNotAuthorizedException the user is not authorized to make this request.
     * @throws PropertyServerException something went wrong with the REST call stack.
     */
    public GUIDListResponse callGUIDListPostRESTCall(String     methodName,
                                                     String     urlTemplate,
                                                     Object     requestBody,
                                                     Object...  params) throws InvalidParameterException,
                                                                               UserNotAuthorizedException,
                                                                               PropertyServerException
    {
        GUIDListResponse restResult = this.callPostRESTCall(methodName, GUIDListResponse.class, urlTemplate, requestBody, params);

        exceptionHandler.detectAndThrowStandardExceptions(methodName, restResult);

        return restResult;
    }


    /**
     * Issue a POST REST call that returns a VoidResponse object.  This is typically a create
     *
     * @param methodName  name of the method being called.
     * @param urlTemplate  template of the URL for the REST API with place-holders for the parameters.
     * @param params  a list of parameters that are slotted into the url template.
     *
     * @return VoidResponse
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws UserNotAuthorizedException the user is not authorized to make this request.
     * @throws PropertyServerException something went wrong with the REST call stack.
     */
    public VoidResponse callVoidGetRESTCall(String    methodName,
                                            String    urlTemplate,
                                            Object... params) throws InvalidParameterException,
                                                                      UserNotAuthorizedException,
                                                                      PropertyServerException
    {
        VoidResponse restResult =  this.callGetRESTCall(methodName,
                                                         VoidResponse.class,
                                                         urlTemplate,
                                                         params);

        exceptionHandler.detectAndThrowStandardExceptions(methodName, restResult);

        return restResult;
    }


    /**
     * Issue a POST REST call that returns a VoidResponse object.  This is typically a create
     *
     * @param methodName  name of the method being called.
     * @param urlTemplate  template of the URL for the REST API with place-holders for the parameters.
     * @param requestBody request body for the request.
     * @param params  a list of parameters that are slotted into the url template.
     *
     * @return VoidResponse
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws UserNotAuthorizedException the user is not authorized to make this request.
     * @throws PropertyServerException something went wrong with the REST call stack.
     */
    public VoidResponse callVoidPostRESTCall(String    methodName,
                                             String    urlTemplate,
                                             Object    requestBody,
                                             Object... params) throws InvalidParameterException,
                                                                      UserNotAuthorizedException,
                                                                      PropertyServerException
    {
        VoidResponse restResult =  this.callPostRESTCall(methodName,
                                                         VoidResponse.class,
                                                         urlTemplate,
                                                         requestBody,
                                                         params);

        exceptionHandler.detectAndThrowStandardExceptions(methodName, restResult);

        return restResult;
    }


    /**
     * Issue a GET REST call that returns a CountResponse object.
     *
     * @param methodName  name of the method being called.
     * @param urlTemplate template of the URL for the REST API with place-holders for the parameters.
     * @param params      a list of parameters that are slotted into the url template.
     *
     * @return CountResponse
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws UserNotAuthorizedException the user is not authorized to make this request.
     * @throws PropertyServerException something went wrong with the REST call stack.
     */
    public CountResponse callCountGetRESTCall(String    methodName,
                                              String    urlTemplate,
                                              Object... params) throws InvalidParameterException,
                                                                       UserNotAuthorizedException,
                                                                       PropertyServerException
    {
        CountResponse restResult =  this.callGetRESTCall(methodName, CountResponse.class, urlTemplate, params);

        exceptionHandler.detectAndThrowStandardExceptions(methodName, restResult);

        return restResult;
    }


    /**
     * Issue a GET REST call that returns a ConnectorReportResponse object.
     *
     * @param methodName  name of the method being called.
     * @param urlTemplate template of the URL for the REST API with place-holders for the parameters.
     * @param params      a list of parameters that are slotted into the url template.
     *
     * @return ConnectorReportResponse
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws UserNotAuthorizedException the user is not authorized to make this request.
     * @throws PropertyServerException something went wrong with the REST call stack.
     */
    public ConnectorReportResponse callOCFConnectorReportGetRESTCall(String    methodName,
                                                                     String    urlTemplate,
                                                                     Object... params) throws InvalidParameterException,
                                                                                          UserNotAuthorizedException,
                                                                                          PropertyServerException
    {
        ConnectorReportResponse restResult = this.callGetRESTCall(methodName,
                                                                  ConnectorReportResponse.class,
                                                                  urlTemplate,
                                                                  params);

        exceptionHandler.detectAndThrowStandardExceptions(methodName, restResult);

        return restResult;
    }
}
