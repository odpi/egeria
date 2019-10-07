/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.commonservices.ffdc.rest;


import org.odpi.openmetadata.adapters.connectors.restclients.RESTClientConnector;
import org.odpi.openmetadata.adapters.connectors.restclients.RESTClientFactory;
import org.odpi.openmetadata.commonservices.ffdc.OMAGCommonErrorCode;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;


/**
 * RESTClient is responsible for issuing calls to the OMAS REST APIs.
 */
public class FFDCRESTClient
{
    protected String              serverName;             /* Initialized in constructor */
    protected String              serverPlatformURLRoot;  /* Initialized in constructor */
    protected RESTClientConnector clientConnector;        /* Initialized in constructor */

    /**
     * Constructor for no authentication.
     *
     * @param serverName name of the OMAG Server to call
     * @param serverPlatformURLRoot URL root of the server platform where the OMAG Server is running.
     * @throws InvalidParameterException there is a problem creating the client-side components to issue any
     * REST API calls.
     */
    protected FFDCRESTClient(String serverName,
                             String serverPlatformURLRoot) throws InvalidParameterException
    {
        final String  methodName = "RESTClient(no authentication)";

        this.serverName = serverName;
        this.serverPlatformURLRoot = serverPlatformURLRoot;

        RESTClientFactory factory = new RESTClientFactory(serverName, serverPlatformURLRoot);

        try
        {
            this.clientConnector = factory.getClientConnector();
        }
        catch (Throwable     error)
        {
            OMAGCommonErrorCode errorCode = OMAGCommonErrorCode.NULL_LOCAL_SERVER_NAME;
            String              errorMessage = errorCode.getErrorMessageId()
                                             + errorCode.getFormattedErrorMessage(serverName, error.getMessage());

            throw new InvalidParameterException(errorCode.getHTTPErrorCode(),
                                                this.getClass().getName(),
                                                methodName,
                                                errorMessage,
                                                errorCode.getSystemAction(),
                                                errorCode.getUserAction(),
                                                error,
                                                "serverPlatformURLRoot or serverName");
        }
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
    protected FFDCRESTClient(String serverName,
                             String serverPlatformURLRoot,
                             String userId,
                             String password) throws InvalidParameterException
    {
        final String  methodName = "RESTClient(userId and password)";

        this.serverName = serverName;
        this.serverPlatformURLRoot = serverPlatformURLRoot;

        RESTClientFactory  factory = new RESTClientFactory(serverName,
                                                           serverPlatformURLRoot,
                                                           userId,
                                                           password);

        try
        {
            this.clientConnector = factory.getClientConnector();
        }
        catch (Throwable     error)
        {
            OMAGCommonErrorCode errorCode    = OMAGCommonErrorCode.NULL_LOCAL_SERVER_NAME;
            String              errorMessage = errorCode.getErrorMessageId()
                                             + errorCode.getFormattedErrorMessage(serverName, error.getMessage());

            throw new InvalidParameterException(errorCode.getHTTPErrorCode(),
                                                this.getClass().getName(),
                                                methodName,
                                                errorMessage,
                                                errorCode.getSystemAction(),
                                                errorCode.getUserAction(),
                                                error,
                                                "serverPlatformURLRoot or serverName");
        }
    }



    /**
     * Issue a GET REST call that returns a GUIDResponse object.
     *
     * @param methodName  name of the method being called.
     * @param urlTemplate template of the URL for the REST API call with place-holders for the parameters.
     * @param params      a list of parameters that are slotted into the url template.
     *
     * @return GUIDResponse
     * @throws PropertyServerException something went wrong with the REST call stack.
     */
    public    GUIDResponse callGUIDGetRESTCall(String    methodName,
                                               String    urlTemplate,
                                               Object... params) throws PropertyServerException
    {
        return this.callGetRESTCall(methodName, GUIDResponse.class, urlTemplate, params);
    }



    /**
     * Issue a POST REST call that returns a guid object.
     *
     * @param methodName  name of the method being called
     * @param urlTemplate  template of the URL for the REST API call with place-holders for the parameters.
     * @param requestBody request body for the request.
     * @param params  a list of parameters that are slotted into the url template.
     *
     * @return GUIDResponse
     * @throws PropertyServerException something went wrong with the REST call stack.
     */
    public   GUIDResponse callGUIDPostRESTCall(String    methodName,
                                               String    urlTemplate,
                                               Object    requestBody,
                                               Object... params) throws PropertyServerException
    {

        return this.callPostRESTCall(methodName,
                                     GUIDResponse.class,
                                     urlTemplate,
                                     requestBody,
                                     params);
    }


    /**
     * Issue a GET REST call that returns a list of GUIDs object.
     *
     * @param methodName  name of the method being called.
     * @param urlTemplate template of the URL for the REST API call with place-holders for the parameters.
     * @param params      a list of parameters that are slotted into the url template.
     *
     * @return ConnectionResponse
     * @throws PropertyServerException something went wrong with the REST call stack.
     */
    public GUIDListResponse callGUIDListGetRESTCall(String    methodName,
                                                    String    urlTemplate,
                                                    Object... params) throws PropertyServerException
    {
        return this.callGetRESTCall(methodName, GUIDListResponse.class, urlTemplate, params);
    }


    /**
     * Issue a POST REST call that returns a list of GUIDs object.
     *
     * @param methodName  name of the method being called.
     * @param urlTemplate template of the URL for the REST API call with place-holders for the parameters.
     * @param requestBody request body for the request.
     * @param params      a list of parameters that are slotted into the url template.
     *
     * @return GUIDListResponse
     * @throws PropertyServerException something went wrong with the REST call stack.
     */
    public GUIDListResponse callGUIDListPostRESTCall(String     methodName,
                                                     String     urlTemplate,
                                                     Object     requestBody,
                                                     Object...  params) throws PropertyServerException
    {
        return this.callPostRESTCall(methodName, GUIDListResponse.class, urlTemplate, requestBody, params);
    }


    /**
     * Issue a POST REST call that returns a VoidResponse object.  This is typically a create
     *
     * @param methodName  name of the method being called.
     * @param urlTemplate  template of the URL for the REST API call with place-holders for the parameters.
     * @param requestBody request body for the request.
     * @param params  a list of parameters that are slotted into the url template.
     *
     * @return VoidResponse
     * @throws PropertyServerException something went wrong with the REST call stack.
     */
    public VoidResponse callVoidPostRESTCall(String    methodName,
                                             String    urlTemplate,
                                             Object    requestBody,
                                             Object... params) throws PropertyServerException
    {
        return this.callPostRESTCall(methodName,
                                     VoidResponse.class,
                                     urlTemplate,
                                     requestBody,
                                     params);
    }


    /**
     * Issue a GET REST call that returns a CountResponse object.
     *
     * @param methodName  name of the method being called.
     * @param urlTemplate template of the URL for the REST API call with place-holders for the parameters.
     * @param params      a list of parameters that are slotted into the url template.
     *
     * @return CountResponse
     * @throws PropertyServerException something went wrong with the REST call stack.
     */
    public CountResponse callCountGetRESTCall(String    methodName,
                                              String    urlTemplate,
                                              Object... params) throws PropertyServerException
    {
        return this.callGetRESTCall(methodName, CountResponse.class, urlTemplate, params);
    }


    /**
     * Issue a GET REST call that returns a response object.
     *
     * @param <T> return type
     * @param methodName  name of the method being called.
     * @param returnClass class of the response object.
     * @param urlTemplate template of the URL for the REST API call with place-holders for the parameters.
     *
     * @return response object
     * @throws PropertyServerException something went wrong with the REST call stack.
     */
    protected <T> T callGetRESTCallNoParams(String    methodName,
                                            Class<T>  returnClass,
                                            String    urlTemplate) throws PropertyServerException
    {
        try
        {
            return clientConnector.callGetRESTCall(methodName, returnClass, urlTemplate);
        }
        catch (Throwable error)
        {
            logRESTCallException(methodName, error);
        }

        return null;
    }


    /**
     * Issue a GET REST call that returns a response object.
     *
     * @param <T> return type
     * @param methodName  name of the method being called.
     * @param returnClass class of the response object.
     * @param urlTemplate template of the URL for the REST API call with place-holders for the parameters.
     * @param params      a list of parameters that are slotted into the url template.
     *
     * @return response object
     * @throws PropertyServerException something went wrong with the REST call stack.
     */
    protected  <T> T callGetRESTCall(String    methodName,
                                     Class<T>  returnClass,
                                     String    urlTemplate,
                                     Object... params) throws PropertyServerException
    {
        try
        {
            return clientConnector.callGetRESTCall(methodName, returnClass, urlTemplate, params);
        }
        catch (Throwable error)
        {
            logRESTCallException(methodName, error);
        }

        return null;
    }


    /**
     * Issue a POST REST call that returns a response object.  This is typically a create, update, or find with
     * complex parameters.
     *
     * @param <T> return type
     * @param methodName  name of the method being called.
     * @param returnClass class of the response object.
     * @param urlTemplate  template of the URL for the REST API call with place-holders for the parameters.
     * @param requestBody request body for the request.
     *
     * @return response object
     * @throws PropertyServerException something went wrong with the REST call stack.
     */
    protected <T> T callPostRESTCallNoParams(String    methodName,
                                             Class<T>  returnClass,
                                             String    urlTemplate,
                                             Object    requestBody) throws PropertyServerException
    {
        try
        {
            return clientConnector.callPostRESTCallNoParams(methodName, returnClass, urlTemplate, requestBody);
        }
        catch (Throwable error)
        {
            logRESTCallException(methodName, error);
        }

        return null;
    }


    /**
     * Issue a POST REST call that returns a response object.  This is typically a create, update, or find with
     * complex parameters.
     *
     * @param <T> return type
     * @param methodName  name of the method being called.
     * @param returnClass class of the response object.
     * @param urlTemplate  template of the URL for the REST API call with place-holders for the parameters.
     * @param requestBody request body for the request.
     * @param params  a list of parameters that are slotted into the url template.
     *
     * @return response object
     * @throws PropertyServerException something went wrong with the REST call stack.
     */
    protected  <T> T callPostRESTCall(String    methodName,
                                      Class<T>  returnClass,
                                      String    urlTemplate,
                                      Object    requestBody,
                                      Object... params) throws PropertyServerException
    {
        try
        {
            return clientConnector.callPostRESTCall(methodName, returnClass, urlTemplate, requestBody, params);
        }
        catch (Throwable error)
        {
            logRESTCallException(methodName, error);
        }

        return null;
    }


    /**
     * Provide detailed logging for exceptions.
     *
     * @param methodName calling method
     * @param error resulting exception
     * @throws PropertyServerException wrapping exception
     */
    private void logRESTCallException(String    methodName,
                                      Throwable error) throws PropertyServerException
    {
        OMAGCommonErrorCode errorCode = OMAGCommonErrorCode.CLIENT_SIDE_REST_API_ERROR;
        String errorMessage = errorCode.getErrorMessageId() + errorCode.getFormattedErrorMessage(methodName,
                                                                                                 serverName,
                                                                                                 serverPlatformURLRoot,
                                                                                                 error.getMessage());

        throw new PropertyServerException(errorCode.getHTTPErrorCode(),
                                          this.getClass().getName(),
                                          methodName,
                                          errorMessage,
                                          errorCode.getSystemAction(),
                                          errorCode.getUserAction(),
                                          error);
    }
}
