/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.assetconsumer.client;

import org.odpi.openmetadata.accessservices.assetconsumer.ffdc.AssetConsumerErrorCode;
import org.odpi.openmetadata.accessservices.assetconsumer.ffdc.exceptions.PropertyServerException;
import org.odpi.openmetadata.accessservices.assetconsumer.rest.*;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.web.client.RestTemplate;

/**
 * RESTClient is responsible for issuing calls to the Community Profile OMAS REST APIs.
 */
class RESTClient
{
    private RestTemplate    restTemplate;   /* Initialized in constructor */
    private String          serverName;     /* Initialized in constructor */
    private String          omasServerURL;  /* Initialized in constructor */


    /**
     * Constructor for no authentication.
     *
     * @param serverName name of server to connect to
     * @param omasServerURL URL root for this server
     */
    RESTClient(String serverName,
               String omasServerURL)
    {
        this.serverName = serverName;
        this.omasServerURL = omasServerURL;
        this.restTemplate = new RestTemplate();
    }


    /**
     * Constructor for simple userId and password authentication.
     *
     * @param serverName name of server to connect to
     * @param omasServerURL URL root for this server
     * @param userId user id for the HTTP request
     * @param password password for the HTTP request
     */
    RESTClient(String serverName, String omasServerURL, String userId, String password)
    {
        this.serverName = serverName;
        this.omasServerURL = omasServerURL;

        RestTemplateBuilder restTemplateBuilder = new RestTemplateBuilder();

        this.restTemplate = restTemplateBuilder.basicAuthentication(userId, password).build();
    }


    /**
     * Issue a GET REST call that returns a Connection object.
     *
     * @param methodName  name of the method being called.
     * @param urlTemplate template of the URL for the REST API call with place-holders for the parameters.
     * @param params      a list of parameters that are slotted into the url template.
     *
     * @return ConnectionResponse
     * @throws PropertyServerException something went wrong with the REST call stack.
     */
    ConnectionResponse callConnectionGetRESTCall(String    methodName,
                                                 String    urlTemplate,
                                                 Object... params) throws PropertyServerException
    {
        return (ConnectionResponse)this.callGetRESTCall(methodName, ConnectionResponse.class, urlTemplate, params);
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
        return (GUIDResponse)this.callGetRESTCall(methodName, GUIDResponse.class, urlTemplate, params);
    }


    /**
     * Issue a GET REST call that returns a MeaningResponse object.
     *
     * @param methodName  name of the method being called.
     * @param urlTemplate template of the URL for the REST API call with place-holders for the parameters.
     * @param params      a list of parameters that are slotted into the url template.
     *
     * @return MeaningResponse
     * @throws PropertyServerException something went wrong with the REST call stack.
     */
    MeaningResponse callMeaningGetRESTCall(String    methodName,
                                           String    urlTemplate,
                                           Object... params) throws PropertyServerException
    {
        return (MeaningResponse)this.callGetRESTCall(methodName, MeaningResponse.class, urlTemplate, params);
    }


    /**
     * Issue a GET REST call that returns a MeaningListResponse object.
     *
     * @param methodName  name of the method being called.
     * @param urlTemplate template of the URL for the REST API call with place-holders for the parameters.
     * @param params      a list of parameters that are slotted into the url template.
     *
     * @return MeaningListResponse
     * @throws PropertyServerException something went wrong with the REST call stack.
     */
    MeaningListResponse callMeaningListGetRESTCall(String    methodName,
                                                   String    urlTemplate,
                                                   Object... params) throws PropertyServerException
    {
        return (MeaningListResponse)this.callGetRESTCall(methodName, MeaningListResponse.class, urlTemplate, params);
    }


    /**
     * Issue a GET REST call that returns a TagResponse object.
     *
     * @param methodName  name of the method being called.
     * @param urlTemplate template of the URL for the REST API call with place-holders for the parameters.
     * @param params      a list of parameters that are slotted into the url template.
     *
     * @return TagResponse
     * @throws PropertyServerException something went wrong with the REST call stack.
     */
    TagResponse callTagGetRESTCall(String    methodName,
                                   String    urlTemplate,
                                   Object... params) throws PropertyServerException
    {
        return (TagResponse)this.callGetRESTCall(methodName, TagResponse.class, urlTemplate, params);
    }


    /**
     * Issue a GET REST call that returns a TagListResponse object.
     *
     * @param methodName  name of the method being called.
     * @param urlTemplate template of the URL for the REST API call with place-holders for the parameters.
     * @param params      a list of parameters that are slotted into the url template.
     *
     * @return TagListResponse
     * @throws PropertyServerException something went wrong with the REST call stack.
     */
    TagListResponse callTagListGetRESTCall(String    methodName,
                                           String    urlTemplate,
                                           Object... params) throws PropertyServerException
    {
        return (TagListResponse)this.callGetRESTCall(methodName, TagListResponse.class, urlTemplate, params);
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
        return (GUIDResponse)this.callPostRESTCall(methodName,
                                                   GUIDResponse.class,
                                                   urlTemplate,
                                                   requestBody,
                                                   params);
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
        return (VoidResponse)this.callPostRESTCall(methodName,
                                                   VoidResponse.class,
                                                   urlTemplate,
                                                   requestBody,
                                                   params);
    }


    /**
     * Issue a GET REST call that returns a response object.
     *
     * @param methodName  name of the method being called.
     * @param returnClass class of the response object.
     * @param urlTemplate template of the URL for the REST API call with place-holders for the parameters.
     * @param params      a list of parameters that are slotted into the url template.
     *
     * @return response object
     * @throws PropertyServerException something went wrong with the REST call stack.
     */
    private Object callGetRESTCall(String    methodName,
                                   Class     returnClass,
                                   String    urlTemplate,
                                   Object... params) throws PropertyServerException
    {
        try
        {
            return restTemplate.getForObject(urlTemplate, returnClass, params);
        }
        catch (Throwable error)
        {
            AssetConsumerErrorCode errorCode = AssetConsumerErrorCode.CLIENT_SIDE_REST_API_ERROR;
            String errorMessage = errorCode.getErrorMessageId() + errorCode.getFormattedErrorMessage(methodName,
                                                                                                     omasServerURL,
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
     * Issue a POST REST call.  This is typically a create or update.
     *
     * @param methodName  name of the method being called.
     * @param returnClass class of the response object.
     * @param urlTemplate  template of the URL for the REST API call with place-holders for the parameters.
     * @param requestBody request body for the request.
     * @param params  a list of parameters that are slotted into the url template.
     *
     * @return Object
     * @throws PropertyServerException something went wrong with the REST call stack.
     */
    private Object callPostRESTCall(String    methodName,
                                    Class     returnClass,
                                    String    urlTemplate,
                                    Object    requestBody,
                                    Object... params) throws PropertyServerException
    {
        try
        {
            return restTemplate.postForObject(urlTemplate, requestBody, returnClass, params);
        }
        catch (Throwable error)
        {
            AssetConsumerErrorCode errorCode = AssetConsumerErrorCode.CLIENT_SIDE_REST_API_ERROR;
            String errorMessage = errorCode.getErrorMessageId() + errorCode.getFormattedErrorMessage(methodName,
                                                                                                     serverName,
                                                                                                     omasServerURL,
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
