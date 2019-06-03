/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.assetowner.client;

import org.odpi.openmetadata.accessservices.assetowner.rest.ZoneResponse;
import org.odpi.openmetadata.commonservices.odf.metadatamanagement.client.ODFRESTClient;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;


/**
 * AssetOwnerRESTClient is responsible for issuing calls to the OMAS REST APIs.
 */
class AssetOwnerRESTClient extends ODFRESTClient
{

    /**
     * Constructor for no authentication.
     *
     * @param serverName name of the OMAG Server to call
     * @param serverPlatformURLRoot URL root of the server platform where the OMAG Server is running.
     * @throws InvalidParameterException there is a problem creating the client-side components to issue any
     * REST API calls.
     */
    AssetOwnerRESTClient(String serverName,
                         String serverPlatformURLRoot) throws InvalidParameterException
    {
        super (serverName, serverPlatformURLRoot);
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
    AssetOwnerRESTClient(String serverName,
                         String serverPlatformURLRoot,
                         String userId,
                         String password) throws InvalidParameterException
    {
        super(serverName, serverPlatformURLRoot, userId, password);
    }


    /**
     * Issue a GET REST call that returns a DiscoveryAnalysisReportResponse object.
     *
     * @param methodName  name of the method being called.
     * @param urlTemplate template of the URL for the REST API call with place-holders for the parameters.
     * @param params      a list of parameters that are slotted into the url template.
     *
     * @return DiscoveryAnalysisReportResponse
     * @throws PropertyServerException something went wrong with the REST call stack.
     */
    public ZoneResponse callZoneGetRESTCall(String    methodName,
                                            String    urlTemplate,
                                            Object... params) throws PropertyServerException
    {
        return this.callGetRESTCall(methodName, ZoneResponse.class, urlTemplate, params);
    }


    /**
     * Issue a POST REST call that returns a DiscoveryAnalysisReportResponse object.
     *
     * @param methodName  name of the method being called.
     * @param urlTemplate template of the URL for the REST API call with place-holders for the parameters.
     * @param requestBody request body for the REST call - contains most of the parameters
     * @param params      a list of parameters that are slotted into the url template.
     *
     * @return DiscoveryAnalysisReportResponse
     * @throws PropertyServerException something went wrong with the REST call stack.
     */
    public ZoneResponse callZonePostRESTCall(String    methodName,
                                             String    urlTemplate,
                                             Object    requestBody,
                                             Object... params) throws PropertyServerException
    {
        return this.callPostRESTCall(methodName, ZoneResponse.class, urlTemplate, requestBody, params);
    }
}
