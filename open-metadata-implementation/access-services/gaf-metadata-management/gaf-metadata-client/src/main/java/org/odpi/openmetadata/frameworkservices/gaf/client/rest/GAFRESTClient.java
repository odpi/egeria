/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworkservices.gaf.client.rest;


import org.odpi.openmetadata.commonservices.ffdc.rest.BooleanResponse;
import org.odpi.openmetadata.frameworkservices.gaf.rest.*;
import org.odpi.openmetadata.frameworkservices.ocf.metadatamanagement.client.OCFRESTClient;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.UserNotAuthorizedException;


/**
 * GAFRESTClient is responsible for issuing calls to the OMAS REST APIs.
 */
public class GAFRESTClient extends OCFRESTClient
{
    /**
     * Constructor for no authentication with audit log.
     *
     * @param serverName name of the OMAG Server to call
     * @param serverPlatformURLRoot URL root of the server manager where the OMAG Server is running.
     * @param auditLog destination for log messages.
     *
     * @throws InvalidParameterException a problem creating the client-side components to issue any
     * REST API calls.
     */
    public GAFRESTClient(String   serverName,
                         String   serverPlatformURLRoot,
                         String   localServerSecretsStoreProvider,
                         String   localServerSecretsStoreLocation,
                         String   localServerSecretsStoreCollection,
                         AuditLog auditLog) throws InvalidParameterException
    {
        super(serverName, serverPlatformURLRoot, localServerSecretsStoreProvider, localServerSecretsStoreLocation, localServerSecretsStoreCollection, auditLog);
    }


    /**
     * Issue a GET REST call that returns a BooleanResponse object.
     *
     * @param methodName  name of the method being called.
     * @param urlTemplate template of the URL for the REST API call, with place-holders for the parameters.
     * @param params      a list of parameters that are slotted into the url template.
     *
     * @return response object
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws UserNotAuthorizedException the user is not authorized to make this request.
     * @throws PropertyServerException the repository is not available or not working properly.
     */
    public BooleanResponse callBooleanGetRESTCall(String    methodName,
                                                  String    urlTemplate,
                                                  Object... params) throws InvalidParameterException,
                                                                           UserNotAuthorizedException,
                                                                           PropertyServerException
    {
        BooleanResponse restResult = this.callGetRESTCall(methodName, BooleanResponse.class, urlTemplate, params);

        exceptionHandler.detectAndThrowStandardExceptions(methodName, restResult);

        return restResult;
    }


    /**
     * Issue a GET REST call that returns a EngineActionElementResponse object.
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
    public EngineActionElementResponse callEngineActionGetRESTCall(String    methodName,
                                                                   String    urlTemplate,
                                                                   Object... params) throws InvalidParameterException,
                                                                                            UserNotAuthorizedException,
                                                                                            PropertyServerException
    {
        EngineActionElementResponse restResult = this.callGetRESTCall(methodName, EngineActionElementResponse.class, urlTemplate, params);

        exceptionHandler.detectAndThrowStandardExceptions(methodName, restResult);

        return restResult;
    }


    /**
     * Issue a GET REST call that returns a EngineActionElementsResponse object.
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
    public EngineActionElementsResponse callEngineActionsGetRESTCall(String    methodName,
                                                                     String    urlTemplate,
                                                                     Object... params) throws InvalidParameterException,
                                                                                              UserNotAuthorizedException,
                                                                                              PropertyServerException
    {
        EngineActionElementsResponse restResult = this.callGetRESTCall(methodName, EngineActionElementsResponse.class, urlTemplate, params);

        exceptionHandler.detectAndThrowStandardExceptions(methodName, restResult);

        return restResult;
    }


    /**
     * Issue a POST REST call that returns a EngineActionElementsResponse object.
     *
     * @param methodName  name of the method being called.
     * @param urlTemplate template of the URL for the REST API with place-holders for the parameters.
     * @param requestBody object that passes additional parameters
     * @param params      a list of parameters that are slotted into the url template.
     *
     * @return response object
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws UserNotAuthorizedException the user is not authorized to make this request.
     * @throws PropertyServerException the repository is not available or not working properly.
     */
    public EngineActionElementsResponse callEngineActionsPostRESTCall(String    methodName,
                                                                      String    urlTemplate,
                                                                      Object    requestBody,
                                                                      Object... params) throws InvalidParameterException,
                                                                                               UserNotAuthorizedException,
                                                                                               PropertyServerException
    {
        EngineActionElementsResponse restResult = this.callPostRESTCall(methodName,
                                                                        EngineActionElementsResponse.class,
                                                                        urlTemplate,
                                                                        requestBody,
                                                                        params);

        exceptionHandler.detectAndThrowStandardExceptions(methodName, restResult);

        return restResult;
    }


    /**
     * Issue a GET REST call that returns a GovernanceActionProcessGraphResponse object.
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
    public GovernanceActionProcessGraphResponse callGovernanceActionProcessGraphPostRESTCall(String    methodName,
                                                                                             String    urlTemplate,
                                                                                             Object    requestBody,
                                                                                             Object... params) throws InvalidParameterException,
                                                                                                                      UserNotAuthorizedException,
                                                                                                                      PropertyServerException
    {
        GovernanceActionProcessGraphResponse restResult = this.callPostRESTCall(methodName,
                                                                                GovernanceActionProcessGraphResponse.class,
                                                                                urlTemplate,
                                                                                requestBody,
                                                                                params);

        exceptionHandler.detectAndThrowStandardExceptions(methodName, restResult);

        return restResult;
    }


    /**
     * Issue a POST REST call that returns a GovernanceEngineElementResponse object.
     *
     * @param methodName  name of the method being called.
     * @param urlTemplate template of the URL for the REST API with place-holders for the parameters.
     * @param requestBody object that passes additional parameters
     * @param params      a list of parameters that are slotted into the url template.
     *
     * @return response object
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws UserNotAuthorizedException the user is not authorized to make this request.
     * @throws PropertyServerException the repository is not available or not working properly.
     */
    public GovernanceEngineElementResponse callGovernanceEnginePostRESTCall(String    methodName,
                                                                             String    urlTemplate,
                                                                             Object    requestBody,
                                                                             Object... params) throws InvalidParameterException,
                                                                                                      UserNotAuthorizedException,
                                                                                                      PropertyServerException
    {
        GovernanceEngineElementResponse restResult = this.callPostRESTCall(methodName,
                                                                            GovernanceEngineElementResponse.class,
                                                                            urlTemplate,
                                                                            requestBody,
                                                                            params);

        exceptionHandler.detectAndThrowStandardExceptions(methodName, restResult);

        return restResult;
    }



    /**
     * Issue a GET REST call that returns a RegisteredGovernanceServiceResponse object.
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
    public RegisteredGovernanceServiceResponse callRegisteredGovernanceServiceGetRESTCall(String    methodName,
                                                                                          String    urlTemplate,
                                                                                          Object... params) throws InvalidParameterException,
                                                                                                                   UserNotAuthorizedException,
                                                                                                                   PropertyServerException
    {
        RegisteredGovernanceServiceResponse restResult = this.callGetRESTCall(methodName, RegisteredGovernanceServiceResponse.class, urlTemplate, params);

        exceptionHandler.detectAndThrowStandardExceptions(methodName, restResult);

        return restResult;
    }


    /**
     * Issue a GET REST call that returns a RegisteredGovernanceServicesResponse object.
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
    public RegisteredGovernanceServicesResponse callRegisteredGovernanceServicesGetRESTCall(String    methodName,
                                                                                            String    urlTemplate,
                                                                                            Object... params) throws InvalidParameterException,
                                                                                                                     UserNotAuthorizedException,
                                                                                                                     PropertyServerException
    {
        RegisteredGovernanceServicesResponse restResult = this.callGetRESTCall(methodName, RegisteredGovernanceServicesResponse.class, urlTemplate, params);

        exceptionHandler.detectAndThrowStandardExceptions(methodName, restResult);

        return restResult;
    }


    /**
     * Issue a GET REST call that returns a IntegrationGroupElementResponse object.
     *
     * @param methodName  name of the method being called
     * @param urlTemplate template of the URL for the REST API with place-holders for the parameters
     * @param params      a list of parameters that are slotted into the url template
     *
     * @return response object
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws UserNotAuthorizedException the user is not authorized to make this request.
     * @throws PropertyServerException the repository is not available or not working properly.
     */
    public IntegrationGroupElementResponse callIntegrationGroupGetRESTCall(String    methodName,
                                                                           String    urlTemplate,
                                                                           Object... params) throws InvalidParameterException,
                                                                                                    UserNotAuthorizedException,
                                                                                                    PropertyServerException
    {
        IntegrationGroupElementResponse restResult = this.callGetRESTCall(methodName, IntegrationGroupElementResponse.class, urlTemplate, params);

        exceptionHandler.detectAndThrowStandardExceptions(methodName, restResult);

        return restResult;
    }


    /**
     * Issue a GET REST call that returns a RegisteredIntegrationConnectorResponse object.
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
    public RegisteredIntegrationConnectorResponse callRegisteredIntegrationConnectorGetRESTCall(String    methodName,
                                                                                                String    urlTemplate,
                                                                                                Object... params) throws InvalidParameterException,
                                                                                                                         UserNotAuthorizedException,
                                                                                                                         PropertyServerException
    {
        RegisteredIntegrationConnectorResponse restResult = this.callGetRESTCall(methodName, RegisteredIntegrationConnectorResponse.class, urlTemplate, params);

        exceptionHandler.detectAndThrowStandardExceptions(methodName, restResult);

        return restResult;
    }


    /**
     * Issue a GET REST call that returns a RegisteredIntegrationConnectorsResponse object.
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
    public RegisteredIntegrationConnectorsResponse callRegisteredIntegrationConnectorsGetRESTCall(String    methodName,
                                                                                                  String    urlTemplate,
                                                                                                  Object... params) throws InvalidParameterException,
                                                                                                                           UserNotAuthorizedException,
                                                                                                                           PropertyServerException
    {
        RegisteredIntegrationConnectorsResponse restResult = this.callGetRESTCall(methodName, RegisteredIntegrationConnectorsResponse.class, urlTemplate, params);

        exceptionHandler.detectAndThrowStandardExceptions(methodName, restResult);

        return restResult;
    }
}
