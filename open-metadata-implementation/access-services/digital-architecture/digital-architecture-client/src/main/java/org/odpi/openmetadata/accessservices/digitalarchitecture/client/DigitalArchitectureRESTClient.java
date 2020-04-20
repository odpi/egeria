/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.digitalarchitecture.client;

import org.odpi.openmetadata.accessservices.digitalarchitecture.rest.*;
import org.odpi.openmetadata.commonservices.ocf.metadatamanagement.client.OCFRESTClient;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;


/**
 * AssetOwnerRESTClient is responsible for issuing calls to the OMAS REST APIs.
 */
class DigitalArchitectureRESTClient extends OCFRESTClient
{
    /**
     * Constructor for no authentication with audit log.
     *
     * @param serverName name of the OMAG Server to call
     * @param serverPlatformURLRoot URL root of the server platform where the OMAG Server is running.
     * @param auditLog destination for log messages.
     *
     * @throws InvalidParameterException there is a problem creating the client-side components to issue any
     * REST API calls.
     */
    DigitalArchitectureRESTClient(String   serverName,
                                  String   serverPlatformURLRoot,
                                  AuditLog auditLog) throws InvalidParameterException
    {
        super(serverName, serverPlatformURLRoot, auditLog);
    }


    /**
     * Constructor for no authentication.
     *
     * @param serverName name of the OMAG Server to call
     * @param serverPlatformURLRoot URL root of the server platform where the OMAG Server is running.
     * @throws InvalidParameterException there is a problem creating the client-side components to issue any
     * REST API calls.
     */
    DigitalArchitectureRESTClient(String serverName,
                                  String serverPlatformURLRoot) throws InvalidParameterException
    {
        super(serverName, serverPlatformURLRoot);
    }


    /**
     * Constructor for simple userId and password authentication with audit log.
     *
     * @param serverName name of the OMAG Server to call
     * @param serverPlatformURLRoot URL root of the server platform where the OMAG Server is running.
     * @param userId user id for the HTTP request
     * @param password password for the HTTP request
     * @param auditLog destination for log messages.
     * @throws InvalidParameterException there is a problem creating the client-side components to issue any
     * REST API calls.
     */
    DigitalArchitectureRESTClient(String   serverName,
                                  String   serverPlatformURLRoot,
                                  String   userId,
                                  String   password,
                                  AuditLog auditLog) throws InvalidParameterException
    {
        super(serverName, serverPlatformURLRoot, userId, password, auditLog);
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
    DigitalArchitectureRESTClient(String serverName,
                                  String serverPlatformURLRoot,
                                  String userId,
                                  String password) throws InvalidParameterException
    {
        super(serverName, serverPlatformURLRoot, userId, password);
    }



    /**
     * Issue a GET REST call that returns a ValidValuesResponse object.
     *
     * @param methodName  name of the method being called.
     * @param urlTemplate template of the URL for the REST API call with place-holders for the parameters.
     * @param params      a list of parameters that are slotted into the url template.
     *
     * @return ValidValueResponse
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws UserNotAuthorizedException the user is not authorized to make this request.
     * @throws PropertyServerException the repository is not available or not working properly.
     */
    ValidValueResponse callValidValueGetRESTCall(String    methodName,
                                                 String    urlTemplate,
                                                 Object... params) throws InvalidParameterException,
                                                                          UserNotAuthorizedException,
                                                                          PropertyServerException
    {
        ValidValueResponse restResult = this.callGetRESTCall(methodName, ValidValueResponse.class, urlTemplate, params);

        exceptionHandler.detectAndThrowStandardExceptions(methodName, restResult);

        return restResult;
    }


    /**
     * Issue a GET REST call that returns a ValidValuesResponse object.
     *
     * @param methodName  name of the method being called.
     * @param urlTemplate template of the URL for the REST API call with place-holders for the parameters.
     * @param params      a list of parameters that are slotted into the url template.
     *
     * @return ValidValuesResponse
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws UserNotAuthorizedException the user is not authorized to make this request.
     * @throws PropertyServerException the repository is not available or not working properly.
     */
    ValidValuesResponse callValidValuesGetRESTCall(String    methodName,
                                                   String    urlTemplate,
                                                   Object... params) throws InvalidParameterException,
                                                                            UserNotAuthorizedException,
                                                                            PropertyServerException
    {
        ValidValuesResponse restResult = this.callGetRESTCall(methodName, ValidValuesResponse.class, urlTemplate, params);

        exceptionHandler.detectAndThrowStandardExceptions(methodName, restResult);

        return restResult;
    }


    /**
     * Issue a POST REST call that returns a ValidValuesResponse object.
     *
     * @param methodName  name of the method being called.
     * @param urlTemplate template of the URL for the REST API call with place-holders for the parameters.
     * @param params      a list of parameters that are slotted into the url template.
     *
     * @return ValidValuesResponse
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws UserNotAuthorizedException the user is not authorized to make this request.
     * @throws PropertyServerException the repository is not available or not working properly.
     */
    ValidValuesResponse callValidValuesPostRESTCall(String    methodName,
                                                    String    urlTemplate,
                                                    Object    requestBody,
                                                    Object... params) throws InvalidParameterException,
                                                                             UserNotAuthorizedException,
                                                                             PropertyServerException
    {
        ValidValuesResponse restResult = this.callPostRESTCall(methodName, ValidValuesResponse.class, urlTemplate, requestBody, params);

        exceptionHandler.detectAndThrowStandardExceptions(methodName, restResult);

        return restResult;
    }


    /**
     * Issue a GET REST call that returns a ValidValueAssignmentConsumersResponse object.
     *
     * @param methodName  name of the method being called.
     * @param urlTemplate template of the URL for the REST API call with place-holders for the parameters.
     * @param params      a list of parameters that are slotted into the url template.
     *
     * @return ValidValueAssignmentConsumersResponse
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws UserNotAuthorizedException the user is not authorized to make this request.
     * @throws PropertyServerException the repository is not available or not working properly.
     */
    ValidValueAssignmentConsumersResponse callValidValueAssignmentConsumersGetRESTCall(String    methodName,
                                                                                       String    urlTemplate,
                                                                                       Object... params) throws InvalidParameterException,
                                                                                                                UserNotAuthorizedException,
                                                                                                                PropertyServerException
    {
        ValidValueAssignmentConsumersResponse restResult = this.callGetRESTCall(methodName,
                                                                                ValidValueAssignmentConsumersResponse.class,
                                                                                urlTemplate,
                                                                                params);

        exceptionHandler.detectAndThrowStandardExceptions(methodName, restResult);

        return restResult;
    }


    /**
     * Issue a GET REST call that returns a ValidValueAssignmentDefinitionsResponse object.
     *
     * @param methodName  name of the method being called.
     * @param urlTemplate template of the URL for the REST API call with place-holders for the parameters.
     * @param params      a list of parameters that are slotted into the url template.
     *
     * @return ValidValueAssignmentConsumersResponse
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws UserNotAuthorizedException the user is not authorized to make this request.
     * @throws PropertyServerException the repository is not available or not working properly.
     */
    ValidValueAssignmentDefinitionsResponse callValidValueAssignmentDefinitionsGetRESTCall(String    methodName,
                                                                                           String    urlTemplate,
                                                                                           Object... params) throws InvalidParameterException,
                                                                                                                    UserNotAuthorizedException,
                                                                                                                    PropertyServerException
    {
        ValidValueAssignmentDefinitionsResponse restResult = this.callGetRESTCall(methodName,
                                                                                  ValidValueAssignmentDefinitionsResponse.class,
                                                                                  urlTemplate,
                                                                                  params);

        exceptionHandler.detectAndThrowStandardExceptions(methodName, restResult);

        return restResult;
    }


    /**
     * Issue a GET REST call that returns a ValidValuesImplAssetsResponse object.
     *
     * @param methodName  name of the method being called.
     * @param urlTemplate template of the URL for the REST API call with place-holders for the parameters.
     * @param params      a list of parameters that are slotted into the url template.
     *
     * @return ValidValuesImplAssetsResponse
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws UserNotAuthorizedException the user is not authorized to make this request.
     * @throws PropertyServerException something went wrong with the REST call stack.
     */
    public ValidValuesImplAssetsResponse callValidValuesImplAssetsGetRESTCall(String    methodName,
                                                                              String    urlTemplate,
                                                                              Object... params) throws InvalidParameterException,
                                                                                                       UserNotAuthorizedException,
                                                                                                       PropertyServerException
    {
        ValidValuesImplAssetsResponse restResult = this.callGetRESTCall(methodName, ValidValuesImplAssetsResponse.class, urlTemplate, params);

        exceptionHandler.detectAndThrowStandardExceptions(methodName, restResult);

        return restResult;
    }


    /**
     * Issue a GET REST call that returns a ValidValuesImplDefinitionsResponse object.
     *
     * @param methodName  name of the method being called.
     * @param urlTemplate template of the URL for the REST API call with place-holders for the parameters.
     * @param params      a list of parameters that are slotted into the url template.
     *
     * @return ValidValuesImplAssetsResponse
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws UserNotAuthorizedException the user is not authorized to make this request.
     * @throws PropertyServerException something went wrong with the REST call stack.
     */
    public ValidValuesImplDefinitionsResponse callValidValuesImplDefinitionsGetRESTCall(String    methodName,
                                                                                        String    urlTemplate,
                                                                                        Object... params) throws InvalidParameterException,
                                                                                                                 UserNotAuthorizedException,
                                                                                                                 PropertyServerException
    {
        ValidValuesImplDefinitionsResponse restResult = this.callGetRESTCall(methodName,
                                                                             ValidValuesImplDefinitionsResponse.class,
                                                                             urlTemplate,
                                                                             params);

        exceptionHandler.detectAndThrowStandardExceptions(methodName, restResult);

        return restResult;
    }


    /**
     * Issue a GET REST call that returns a ReferenceValueAssignmentDefinitionsResponse object.
     *
     * @param methodName  name of the method being called.
     * @param urlTemplate template of the URL for the REST API call with place-holders for the parameters.
     * @param params      a list of parameters that are slotted into the url template.
     *
     * @return ValidValuesImplAssetsResponse
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws UserNotAuthorizedException the user is not authorized to make this request.
     * @throws PropertyServerException something went wrong with the REST call stack.
     */
    public ReferenceValueAssignmentDefinitionsResponse callReferenceValueAssignmentDefinitionsGetRESTCall(String    methodName,
                                                                                                          String    urlTemplate,
                                                                                                          Object... params) throws InvalidParameterException,
                                                                                                                                   UserNotAuthorizedException,
                                                                                                                                   PropertyServerException
    {
        ReferenceValueAssignmentDefinitionsResponse restResult = this.callGetRESTCall(methodName,
                                                                                      ReferenceValueAssignmentDefinitionsResponse.class,
                                                                                      urlTemplate,
                                                                                      params);

        exceptionHandler.detectAndThrowStandardExceptions(methodName, restResult);

        return restResult;
    }


    /**
     * Issue a GET REST call that returns a ReferenceValueAssignmentItemsResponse object.
     *
     * @param methodName  name of the method being called.
     * @param urlTemplate template of the URL for the REST API call with place-holders for the parameters.
     * @param params      a list of parameters that are slotted into the url template.
     *
     * @return ValidValuesImplAssetsResponse
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws UserNotAuthorizedException the user is not authorized to make this request.
     * @throws PropertyServerException something went wrong with the REST call stack.
     */
    public ReferenceValueAssignmentItemsResponse callReferenceValueAssignmentItemsGetRESTCall(String    methodName,
                                                                                              String    urlTemplate,
                                                                                              Object... params) throws InvalidParameterException,
                                                                                                                       UserNotAuthorizedException,
                                                                                                                       PropertyServerException
    {
        ReferenceValueAssignmentItemsResponse restResult = this.callGetRESTCall(methodName,
                                                                                ReferenceValueAssignmentItemsResponse.class,
                                                                                urlTemplate,
                                                                                params);

        exceptionHandler.detectAndThrowStandardExceptions(methodName, restResult);

        return restResult;
    }


    /**
     * Issue a GET REST call that returns a ValidValueMappingsResponse object.
     *
     * @param methodName  name of the method being called.
     * @param urlTemplate template of the URL for the REST API call with place-holders for the parameters.
     * @param params      a list of parameters that are slotted into the url template.
     *
     * @return ValidValuesImplAssetsResponse
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws UserNotAuthorizedException the user is not authorized to make this request.
     * @throws PropertyServerException something went wrong with the REST call stack.
     */
    public ValidValueMappingsResponse callValidValueMappingsGetRESTCall(String    methodName,
                                                                        String    urlTemplate,
                                                                        Object... params) throws InvalidParameterException,
                                                                                                 UserNotAuthorizedException,
                                                                                                 PropertyServerException
    {
        ValidValueMappingsResponse restResult = this.callGetRESTCall(methodName,
                                                                     ValidValueMappingsResponse.class,
                                                                     urlTemplate,
                                                                     params);

        exceptionHandler.detectAndThrowStandardExceptions(methodName, restResult);

        return restResult;
    }


    /**
     * Issue a GET REST call that returns a ValidValueMappingsResponse object.
     *
     * @param methodName  name of the method being called.
     * @param urlTemplate template of the URL for the REST API call with place-holders for the parameters.
     * @param params      a list of parameters that are slotted into the url template.
     *
     * @return ValidValuesImplAssetsResponse
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws UserNotAuthorizedException the user is not authorized to make this request.
     * @throws PropertyServerException something went wrong with the REST call stack.
     */
    public ValidValuesMappingsResponse callValidValuesMappingsGetRESTCall(String    methodName,
                                                                          String    urlTemplate,
                                                                          Object... params) throws InvalidParameterException,
                                                                                                   UserNotAuthorizedException,
                                                                                                   PropertyServerException
    {
        ValidValuesMappingsResponse restResult = this.callGetRESTCall(methodName,
                                                                      ValidValuesMappingsResponse.class,
                                                                      urlTemplate,
                                                                      params);

        exceptionHandler.detectAndThrowStandardExceptions(methodName, restResult);

        return restResult;
    }
}
