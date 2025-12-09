/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.governanceservers.enginehostservices.client.rest;

import org.odpi.openmetadata.commonservices.ffdc.rest.FFDCRESTClient;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.connectors.SecretsStoreConnector;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.governanceservers.enginehostservices.rest.GovernanceEngineSummariesResponse;
import org.odpi.openmetadata.governanceservers.enginehostservices.rest.GovernanceEngineSummaryResponse;

import java.util.Map;

/**
 * EngineHostRESTClient is responsible for issuing the REST API calls
 */
public class EngineHostRESTClient extends FFDCRESTClient
{
    /**
     * Create a new client with bearer token authentication embedded in the HTTP request.
     *
     * @param serverName name of the OMAG Server to call
     * @param serverPlatformURLRoot URL root of the server platform where the OMAG Server is running.
     * @param localServerSecretsStoreProvider secrets store connector for bearer token
     * @param localServerSecretsStoreLocation secrets store location for bearer token
     * @param localServerSecretsStoreCollection secrets store collection for bearer token
     * @param auditLog destination for log messages.
     *
     * @throws InvalidParameterException there is a problem creating the client-side components to issue any
     * REST API calls.
     */
    public EngineHostRESTClient(String   serverName,
                                String   serverPlatformURLRoot,
                                String   localServerSecretsStoreProvider,
                                String   localServerSecretsStoreLocation,
                                String   localServerSecretsStoreCollection,
                                AuditLog auditLog) throws InvalidParameterException
    {
        super(serverName, serverPlatformURLRoot, localServerSecretsStoreProvider, localServerSecretsStoreLocation, localServerSecretsStoreCollection, auditLog);
    }


    /**
     * Create a new client with bearer token authentication embedded in the HTTP request.
     *
     * @param serverName name of the OMAG Server to call
     * @param serverPlatformURLRoot URL root of the server platform where the OMAG Server is running.
     * @param secretsStoreConnectorMap connectors to secrets stores
     * @param auditLog destination for log messages.
     *
     * @throws InvalidParameterException there is a problem creating the client-side components to issue any
     * REST API calls.
     */
    public EngineHostRESTClient(String                             serverName,
                                String                             serverPlatformURLRoot,
                                Map<String, SecretsStoreConnector> secretsStoreConnectorMap,
                                AuditLog                           auditLog) throws InvalidParameterException
    {
        super(serverName, serverPlatformURLRoot, secretsStoreConnectorMap, auditLog);
    }


    /**
     * Issue a GET REST call that returns a GovernanceEngineSummariesResponse object.
     *
     * @param methodName  name of the method being called.
     * @param urlTemplate template of the URL for the REST API call with place-holders for the parameters.
     * @param params      a list of parameters that are slotted into the url template.
     *
     * @return GovernanceEngineSummariesResponse
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws UserNotAuthorizedException the user is not authorized to make this request.
     * @throws PropertyServerException something went wrong with the REST call stack.
     */
    public GovernanceEngineSummariesResponse callGovernanceEngineSummariesGetRESTCall(String    methodName,
                                                                                      String    urlTemplate,
                                                                                      Object... params) throws InvalidParameterException,
                                                                                                               UserNotAuthorizedException,
                                                                                                               PropertyServerException
    {
        GovernanceEngineSummariesResponse restResult = this.callGetRESTCall(methodName,
                                                                            GovernanceEngineSummariesResponse.class,
                                                                            urlTemplate,
                                                                            params);

        exceptionHandler.detectAndThrowStandardExceptions(methodName, restResult);

        return restResult;
    }


    /**
     * Issue a GET REST call that returns a GovernanceEngineSummaryResponse object.
     *
     * @param methodName  name of the method being called.
     * @param urlTemplate template of the URL for the REST API call with place-holders for the parameters.
     * @param params      a list of parameters that are slotted into the url template.
     *
     * @return GovernanceEngineSummaryResponse
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws UserNotAuthorizedException the user is not authorized to make this request.
     * @throws PropertyServerException something went wrong with the REST call stack.
     */
    public GovernanceEngineSummaryResponse callGovernanceEngineSummaryGetRESTCall(String    methodName,
                                                                                  String    urlTemplate,
                                                                                  Object... params) throws InvalidParameterException,
                                                                                                           UserNotAuthorizedException,
                                                                                                           PropertyServerException
    {
        GovernanceEngineSummaryResponse restResult = this.callGetRESTCall(methodName,
                                                                          GovernanceEngineSummaryResponse.class,
                                                                          urlTemplate,
                                                                          params);

        exceptionHandler.detectAndThrowStandardExceptions(methodName, restResult);

        return restResult;
    }
}
