/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.assetconsumer.client;

import org.odpi.openmetadata.accessservices.assetconsumer.rest.*;
import org.odpi.openmetadata.commonservices.ffdc.rest.FFDCRESTClient;
import org.odpi.openmetadata.commonservices.ocf.metadatamanagement.rest.AssetResponse;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;


/**
 * RESTClient is responsible for issuing calls to the Community Profile OMAS REST APIs.
 */
class RESTClient extends FFDCRESTClient
{
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
        super(serverName, serverPlatformURLRoot);
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
        super(serverName, serverPlatformURLRoot, userId, password);
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
        return this.callGetRESTCall(methodName, ConnectionResponse.class, urlTemplate, params);
    }


    /**
     * Issue a GET REST call that returns a AssetResponse object.
     *
     * @param methodName  name of the method being called.
     * @param urlTemplate template of the URL for the REST API call with place-holders for the parameters.
     * @param params      a list of parameters that are slotted into the url template.
     *
     * @return AssetListResponse
     * @throws PropertyServerException something went wrong with the REST call stack.
     */
    AssetResponse callAssetGetRESTCall(String    methodName,
                                       String    urlTemplate,
                                       Object... params) throws PropertyServerException
    {
        return this.callGetRESTCall(methodName, AssetResponse.class, urlTemplate, params);
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
        return this.callGetRESTCall(methodName, AssetListResponse.class, urlTemplate, params);
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
        return this.callGetRESTCall(methodName, MeaningResponse.class, urlTemplate, params);
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
        return this.callGetRESTCall(methodName, MeaningListResponse.class, urlTemplate, params);
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
        return this.callGetRESTCall(methodName, TagResponse.class, urlTemplate, params);
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
        return this.callGetRESTCall(methodName, TagListResponse.class, urlTemplate, params);
    }
}
