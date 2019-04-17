/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.discoveryengine.client;

import org.odpi.openmetadata.accessservices.discoveryengine.ffdc.DiscoveryEngineErrorCode;
import org.odpi.openmetadata.accessservices.discoveryengine.rest.*;
import org.odpi.openmetadata.adapters.connectors.restclients.RESTClientConnector;
import org.odpi.openmetadata.adapters.connectors.restclients.RESTClientFactory;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.discovery.ffdc.InvalidParameterException;


/**
 * RESTClient is responsible for issuing calls to the OMAS REST APIs.
 */
class RESTClient
{
    private RESTClientConnector clientConnector;        /* Initialized in constructor */
    private String              serverName;             /* Initialized in constructor */
    private String              serverPlatformURLRoot;  /* Initialized in constructor */

    /**
     * Constructor for no authentication.
     *
     * @param serverName name of the OMAG Server to call
     * @param serverPlatformURLRoot URL root of the server platform where the OMAG Server is running.
     * @throws InvalidParameterException there is a problem creating the client-side components to issue any
     * REST API calls.
     */
    RESTClient(String serverName,
               String serverPlatformURLRoot) throws InvalidParameterException
    {
        final String  methodName = "RESTClient(no authentication)";

        RESTClientFactory factory = new RESTClientFactory(serverName, serverPlatformURLRoot);

        this.serverName = serverName;
        this.serverPlatformURLRoot = serverPlatformURLRoot;

        try
        {
            this.clientConnector = factory.getClientConnector();
        }
        catch (Throwable     error)
        {
            DiscoveryEngineErrorCode errorCode    = DiscoveryEngineErrorCode.NULL_LOCAL_SERVER_NAME;
            String                   errorMessage = errorCode.getErrorMessageId()
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
    RESTClient(String serverName,
               String serverPlatformURLRoot,
               String userId,
               String password) throws InvalidParameterException
    {
        final String  methodName = "RESTClient(userId and password)";

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
            DiscoveryEngineErrorCode errorCode    = DiscoveryEngineErrorCode.NULL_LOCAL_SERVER_NAME;
            String                   errorMessage = errorCode.getErrorMessageId()
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
     * Issue a GET REST call that returns a list of GUIDs object.
     *
     * @param methodName  name of the method being called.
     * @param urlTemplate template of the URL for the REST API call with place-holders for the parameters.
     * @param params      a list of parameters that are slotted into the url template.
     *
     * @return ConnectionResponse
     * @throws PropertyServerException something went wrong with the REST call stack.
     */
    GUIDListResponse callGUIDListGetRESTCall(String    methodName,
                                             String    urlTemplate,
                                             Object... params) throws PropertyServerException
    {
        try
        {
            return clientConnector.callGetRESTCall(methodName, GUIDListResponse.class, urlTemplate, params);
        }
        catch (Throwable     error)
        {
            DiscoveryEngineErrorCode errorCode    = DiscoveryEngineErrorCode.CLIENT_SIDE_REST_API_ERROR;
            String                   errorMessage = errorCode.getErrorMessageId()
                                                  + errorCode.getFormattedErrorMessage(error.getClass().getName(),
                                                                                       urlTemplate,
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
    GUIDResponse callGUIDGetRESTCall(String    methodName,
                                     String    urlTemplate,
                                     Object... params) throws PropertyServerException
    {
        try
        {
            return clientConnector.callGetRESTCall(methodName, GUIDResponse.class, urlTemplate, params);
        }
        catch (Throwable     error)
        {
            DiscoveryEngineErrorCode errorCode    = DiscoveryEngineErrorCode.CLIENT_SIDE_REST_API_ERROR;
            String                   errorMessage = errorCode.getErrorMessageId()
                                                  + errorCode.getFormattedErrorMessage(error.getClass().getName(),
                                                                                       urlTemplate,
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
    GUIDResponse callGUIDPostRESTCall(String    methodName,
                                      String    urlTemplate,
                                      Object    requestBody,
                                      Object... params) throws PropertyServerException
    {
        try
        {
            return clientConnector.callPostRESTCall(methodName,
                                                    GUIDResponse.class,
                                                    urlTemplate,
                                                    requestBody,
                                                    params);
        }
        catch (Throwable     error)
        {
            DiscoveryEngineErrorCode errorCode    = DiscoveryEngineErrorCode.CLIENT_SIDE_REST_API_ERROR;
            String                   errorMessage = errorCode.getErrorMessageId()
                                                  + errorCode.getFormattedErrorMessage(error.getClass().getName(),
                                                                                       urlTemplate,
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
    VoidResponse callVoidPostRESTCall(String    methodName,
                                      String    urlTemplate,
                                      Object    requestBody,
                                      Object... params) throws PropertyServerException
    {
        try
        {
            return clientConnector.callPostRESTCall(methodName,
                                                    VoidResponse.class,
                                                    urlTemplate,
                                                    requestBody,
                                                    params);
        }
        catch (Throwable     error)
        {
            DiscoveryEngineErrorCode errorCode    = DiscoveryEngineErrorCode.CLIENT_SIDE_REST_API_ERROR;
            String                   errorMessage = errorCode.getErrorMessageId()
                                                  + errorCode.getFormattedErrorMessage(error.getClass().getName(),
                                                                                       urlTemplate,
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


    /**
     * Issue a GET REST call that returns a DiscoveryEnginePropertiesResponse object.
     *
     * @param methodName  name of the method being called.
     * @param urlTemplate template of the URL for the REST API call with place-holders for the parameters.
     * @param params      a list of parameters that are slotted into the url template.
     *
     * @return DiscoveryEnginePropertiesResponse
     * @throws PropertyServerException something went wrong with the REST call stack.
     */
    DiscoveryEnginePropertiesResponse callDiscoveryEnginePropertiesGetRESTCall(String    methodName,
                                                                               String    urlTemplate,
                                                                               Object... params) throws PropertyServerException
    {
        try
        {
            return clientConnector.callGetRESTCall(methodName, DiscoveryEnginePropertiesResponse.class, urlTemplate, params);
        }
        catch (Throwable     error)
        {
            DiscoveryEngineErrorCode errorCode    = DiscoveryEngineErrorCode.CLIENT_SIDE_REST_API_ERROR;
            String                   errorMessage = errorCode.getErrorMessageId()
                                                  + errorCode.getFormattedErrorMessage(error.getClass().getName(),
                                                                                       urlTemplate,
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


    /**
     * Issue a GET REST call that returns a DiscoveryEngineListResponse object.
     *
     * @param methodName  name of the method being called.
     * @param urlTemplate template of the URL for the REST API call with place-holders for the parameters.
     * @param params      a list of parameters that are slotted into the url template.
     *
     * @return DiscoveryEngineListResponse
     * @throws PropertyServerException something went wrong with the REST call stack.
     */
    DiscoveryEngineListResponse callDiscoveryEngineListGetRESTCall(String    methodName,
                                                                   String    urlTemplate,
                                                                   Object... params) throws PropertyServerException
    {
        try
        {
            return clientConnector.callGetRESTCall(methodName, DiscoveryEngineListResponse.class, urlTemplate, params);
        }
        catch (Throwable     error)
        {
            DiscoveryEngineErrorCode errorCode    = DiscoveryEngineErrorCode.CLIENT_SIDE_REST_API_ERROR;
            String                   errorMessage = errorCode.getErrorMessageId()
                                                  + errorCode.getFormattedErrorMessage(error.getClass().getName(),
                                                                                       urlTemplate,
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


    /**
     * Issue a GET REST call that returns a DiscoveryServicePropertiesResponse object.
     *
     * @param methodName  name of the method being called.
     * @param urlTemplate template of the URL for the REST API call with place-holders for the parameters.
     * @param params      a list of parameters that are slotted into the url template.
     *
     * @return DiscoveryServicePropertiesResponse
     * @throws PropertyServerException something went wrong with the REST call stack.
     */
    DiscoveryServicePropertiesResponse callDiscoveryServicePropertiesGetRESTCall(String    methodName,
                                                                                 String    urlTemplate,
                                                                                 Object... params) throws PropertyServerException
    {
        try
        {
            return clientConnector.callGetRESTCall(methodName, DiscoveryServicePropertiesResponse.class, urlTemplate, params);
        }
        catch (Throwable     error)
        {
            DiscoveryEngineErrorCode errorCode    = DiscoveryEngineErrorCode.CLIENT_SIDE_REST_API_ERROR;
            String                   errorMessage = errorCode.getErrorMessageId()
                                                  + errorCode.getFormattedErrorMessage(error.getClass().getName(),
                                                                                       urlTemplate,
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


    /**
     * Issue a GET REST call that returns a DiscoveryEngineListResponse object.
     *
     * @param methodName  name of the method being called.
     * @param urlTemplate template of the URL for the REST API call with place-holders for the parameters.
     * @param params      a list of parameters that are slotted into the url template.
     *
     * @return DiscoveryEngineListResponse
     * @throws PropertyServerException something went wrong with the REST call stack.
     */
    DiscoveryServiceListResponse callDiscoveryServiceListGetRESTCall(String    methodName,
                                                                     String    urlTemplate,
                                                                     Object... params) throws PropertyServerException
    {
        try
        {
            return clientConnector.callGetRESTCall(methodName, DiscoveryServiceListResponse.class, urlTemplate, params);
        }
        catch (Throwable     error)
        {
            DiscoveryEngineErrorCode errorCode    = DiscoveryEngineErrorCode.CLIENT_SIDE_REST_API_ERROR;
            String                   errorMessage = errorCode.getErrorMessageId()
                                                  + errorCode.getFormattedErrorMessage(error.getClass().getName(),
                                                                                       urlTemplate,
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


    /**
     * Issue a GET REST call that returns a RegisteredDiscoveryServiceResponse object.
     *
     * @param methodName  name of the method being called.
     * @param urlTemplate template of the URL for the REST API call with place-holders for the parameters.
     * @param params      a list of parameters that are slotted into the url template.
     *
     * @return RegisteredDiscoveryServiceResponse
     * @throws PropertyServerException something went wrong with the REST call stack.
     */
    RegisteredDiscoveryServiceResponse callRegisteredDiscoveryServiceGetRESTCall(String    methodName,
                                                                                 String    urlTemplate,
                                                                                 Object... params) throws PropertyServerException
    {
        try
        {
            return clientConnector.callGetRESTCall(methodName, RegisteredDiscoveryServiceResponse.class, urlTemplate, params);
        }
        catch (Throwable     error)
        {
            DiscoveryEngineErrorCode errorCode    = DiscoveryEngineErrorCode.CLIENT_SIDE_REST_API_ERROR;
            String                   errorMessage = errorCode.getErrorMessageId()
                                                  + errorCode.getFormattedErrorMessage(error.getClass().getName(),
                                                                                       urlTemplate,
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

}
