/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.assetconsumer.client;

import org.odpi.openmetadata.accessservices.assetconsumer.rest.GlossaryTermListResponse;
import org.odpi.openmetadata.accessservices.assetconsumer.rest.GlossaryTermResponse;
import org.odpi.openmetadata.commonservices.ocf.metadatamanagement.client.OCFRESTClient;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;


/**
 * AssetConsumerRESTClient is responsible for issuing calls to the OMAG REST APIs.
 */
class AssetConsumerRESTClient extends OCFRESTClient
{
    /**
     * Constructor for no authentication.
     *
     * @param serverName name of the OMAG Server to call
     * @param serverPlatformURLRoot URL root of the server platform where the OMAG Server is running.
     * @throws InvalidParameterException there is a problem creating the client-side components to issue any
     * REST API calls.
     */
    AssetConsumerRESTClient(String serverName,
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
    AssetConsumerRESTClient(String serverName,
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
     * @throws PropertyServerException something went wrong with the REST call stack.
     */
    GlossaryTermResponse callGlossaryTermGetRESTCall(String    methodName,
                                                     String    urlTemplate,
                                                     Object... params) throws PropertyServerException
    {
        return this.callGetRESTCall(methodName, GlossaryTermResponse.class, urlTemplate, params);
    }


    /**
     * Issue a GET REST call that returns a GlossaryTermListResponse object.
     *
     * @param methodName  name of the method being called.
     * @param urlTemplate template of the URL for the REST API call with place-holders for the parameters.
     * @param searchString request body describing the value to search for
     * @param params      a list of parameters that are slotted into the url template.
     *
     * @return GlossaryTermListResponse
     * @throws PropertyServerException something went wrong with the REST call stack.
     */
    GlossaryTermListResponse callGlossaryTermListPostRESTCall(String    methodName,
                                                              String    urlTemplate,
                                                              String    searchString,
                                                              Object... params) throws PropertyServerException
    {
        return this.callPostRESTCall(methodName, GlossaryTermListResponse.class, urlTemplate, searchString, params);
    }
}
