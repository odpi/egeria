/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.serveroperations.client.rest;


import org.odpi.openmetadata.adminservices.rest.OMAGServerConfigResponse;
import org.odpi.openmetadata.commonservices.ffdc.rest.FFDCRESTClient;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.connectors.SecretsStoreConnector;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.serveroperations.rest.OMAGServerStatusResponse;
import org.odpi.openmetadata.serveroperations.rest.ServerServicesListResponse;
import org.odpi.openmetadata.serveroperations.rest.ServerStatusResponse;

import java.util.Map;


/**
 * AssetOwnerRESTClient is responsible for issuing calls to the OMAS REST APIs.
 */
public class ServerOperationsRESTClient extends FFDCRESTClient
{
    /**
     * Constructor for no authentication with audit log.
     *
     * @param platformName name of the OMAG Server to call
     * @param platformURLRoot URL root of the server manager where the OMAG Server is running.
     * @param localServerSecretsStoreProvider secrets store connector for bearer token
     * @param localServerSecretsStoreLocation secrets store location for bearer token
     * @param localServerSecretsStoreCollection secrets store collection for bearer token
     * @param auditLog destination for log messages.
     *
     * @throws InvalidParameterException a problem creating the client-side components to issue any
     * REST API calls.
     */
    public ServerOperationsRESTClient(String   platformName,
                                      String   platformURLRoot,
                                      String   localServerSecretsStoreProvider,
                                      String   localServerSecretsStoreLocation,
                                      String   localServerSecretsStoreCollection,
                                      AuditLog auditLog) throws InvalidParameterException
    {
        super(platformName, platformURLRoot, localServerSecretsStoreProvider, localServerSecretsStoreLocation, localServerSecretsStoreCollection, auditLog);
    }


    /**
     * Create a new client with bearer token from supplied secrets store.
     *
     * @param platformName name of the platform to connect to
     * @param platformRootURL the network address of the server running the OMAS REST services
     * @param secretsStoreConnectorMap connectors to secrets stores
     * @param auditLog destination for log messages.
     * @throws InvalidParameterException a problem creating the client-side components to issue any
     * REST API calls.
     */
    public ServerOperationsRESTClient(String                             platformName,
                                      String                             platformRootURL,
                                      Map<String, SecretsStoreConnector> secretsStoreConnectorMap,
                                      AuditLog                           auditLog) throws InvalidParameterException
    {
        super(platformName, platformRootURL, secretsStoreConnectorMap, auditLog);
    }

    /**
     * Issue a GET REST call that returns a ServerStatusResponse object.
     *
     * @param methodName  name of the method being called.
     * @param urlTemplate template of the URL for the REST API with place-holders for the parameters.
     * @param params      a list of parameters that are slotted into the url template.
     *
     * @return response object
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws UserNotAuthorizedException the user is not authorized to make this request.
     * @throws PropertyServerException the repository is not available or not working properly.
     */
    public ServerStatusResponse callServerStatusGetRESTCall(String methodName,
                                                            String urlTemplate,
                                                            Object... params) throws InvalidParameterException,
                                                                              UserNotAuthorizedException,
                                                                              PropertyServerException
    {
        ServerStatusResponse restResult = this.callGetRESTCall(methodName, ServerStatusResponse.class, urlTemplate, params);

        exceptionHandler.detectAndThrowStandardExceptions(methodName, restResult);

        return restResult;
    }


    /**
     * Issue a GET REST call that returns a ServerServicesListResponse object.
     *
     * @param methodName  name of the method being called.
     * @param urlTemplate template of the URL for the REST API with place-holders for the parameters.
     * @param params      a list of parameters that are slotted into the url template.
     *
     * @return response object
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws UserNotAuthorizedException the user is not authorized to make this request.
     * @throws PropertyServerException the repository is not available or not working properly.
     */
    public ServerServicesListResponse callServiceListGetRESTCall(String methodName,
                                                                 String urlTemplate,
                                                                 Object... params) throws InvalidParameterException,
                                                                                   UserNotAuthorizedException,
                                                                                   PropertyServerException
    {
        ServerServicesListResponse restResult = this.callGetRESTCall(methodName, ServerServicesListResponse.class, urlTemplate, params);

        exceptionHandler.detectAndThrowStandardExceptions(methodName, restResult);

        return restResult;
    }



    /**
     * Issue a GET REST call that returns a OMAGServerStatusResponse object.
     *
     * @param methodName  name of the method being called.
     * @param urlTemplate template of the URL for the REST API call with place-holders for the parameters.
     * @param params      a list of parameters that are slotted into the url template.
     *
     * @return OMAGServerStatusResponse
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws UserNotAuthorizedException the user is not authorized to make this request.
     * @throws PropertyServerException the repository is not available or not working properly.
     */
    public OMAGServerStatusResponse callOMAGServerStatusGetRESTCall(String methodName,
                                                                    String urlTemplate,
                                                                    Object... params) throws InvalidParameterException,
                                                                                      UserNotAuthorizedException,
                                                                                      PropertyServerException
    {
        OMAGServerStatusResponse restResult = this.callGetRESTCall(methodName, OMAGServerStatusResponse.class, urlTemplate, params);

        exceptionHandler.detectAndThrowStandardExceptions(methodName, restResult);

        return restResult;
    }


    /**
     * Issue a GET REST call that returns a OMAGServerConfigResponse object.
     *
     * @param methodName  name of the method being called.
     * @param urlTemplate template of the URL for the REST API call with place-holders for the parameters.
     * @param params      a list of parameters that are slotted into the url template.
     *
     * @return OMAGServerConfigResponse
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws UserNotAuthorizedException the user is not authorized to make this request.
     * @throws PropertyServerException something went wrong with the REST call stack.
     */
    public OMAGServerConfigResponse callOMAGServerConfigGetRESTCall(String methodName,
                                                                    String urlTemplate,
                                                                    Object... params) throws InvalidParameterException,
                                                                                      UserNotAuthorizedException,
                                                                                      PropertyServerException
    {
        OMAGServerConfigResponse restResult = this.callGetRESTCall(methodName, OMAGServerConfigResponse.class, urlTemplate, params);

        exceptionHandler.detectAndThrowStandardExceptions(methodName, restResult);

        return restResult;
    }
}
