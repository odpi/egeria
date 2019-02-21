/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.communityprofile.client;

import org.odpi.openmetadata.accessservices.communityprofile.ffdc.CommunityProfileErrorCode;
import org.odpi.openmetadata.accessservices.communityprofile.ffdc.exceptions.PropertyServerException;
import org.odpi.openmetadata.accessservices.communityprofile.rest.*;
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
     * Issue a GET REST call that returns a PersonalProfileResponse object.
     *
     * @param methodName  name of the method being called.
     * @param urlTemplate template of the URL for the REST API call with place-holders for the parameters.
     * @param params      a list of parameters that are slotted into the url template.
     *
     * @return PersonalProfileResponse
     * @throws PropertyServerException something went wrong with the REST call stack.
     */
    PersonalProfileResponse callPersonalProfileGetRESTCall(String    methodName,
                                                           String    urlTemplate,
                                                           Object... params) throws PropertyServerException
    {
        return (PersonalProfileResponse)this.callGetRESTCall(methodName, PersonalProfileResponse.class, urlTemplate, params);
    }


    /**
     * Issue a GET REST call that returns a list of PersonalProfile objects.
     *
     * @param methodName  name of the method being called
     * @param urlTemplate  template of the URL for the REST API call with place-holders for the parameters
     * @param params  a list of parameters that are slotted into the url template
     * @return PersonalProfileListResponse
     * @throws PropertyServerException something went wrong with the REST call stack.
     */
    PersonalProfileListResponse callPersonalProfileListGetRESTCall(String    methodName,
                                                                   String    urlTemplate,
                                                                   Object... params) throws PropertyServerException
    {
        return (PersonalProfileListResponse)this.callGetRESTCall(methodName, PersonalProfileListResponse.class, urlTemplate, params);
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
    CountResponse callCountGetRESTCall(String    methodName,
                                       String    urlTemplate,
                                       Object... params) throws PropertyServerException
    {
        return (CountResponse)this.callGetRESTCall(methodName, CountResponse.class, urlTemplate, params);
    }


    /**
     * Issue a GET REST call that returns a Connection object.
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
     * Issue a GET REST call that returns a AssetListResponse object.
     *
     * @param methodName  name of the method being called.
     * @param urlTemplate template of the URL for the REST API call with place-holders for the parameters.
     * @param params      a list of parameters that are slotted into the url template.
     *
     * @return AssetListResponse
     * @throws PropertyServerException something went wrong with the REST call stack.
     */
    AssetListResponse callAssetListGetRESTCall(String    methodName,
                                               String    urlTemplate,
                                               Object... params) throws PropertyServerException
    {
        return (AssetListResponse)this.callGetRESTCall(methodName, AssetListResponse.class, urlTemplate, params);
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
            CommunityProfileErrorCode errorCode = CommunityProfileErrorCode.CLIENT_SIDE_REST_API_ERROR;
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
     * Issue a POST REST call that returns a VoidResponse object.  This is typically a create
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
            CommunityProfileErrorCode errorCode = CommunityProfileErrorCode.CLIENT_SIDE_REST_API_ERROR;
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
