/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.governanceengine.client.rest;


import org.odpi.openmetadata.accessservices.governanceengine.rest.*;
import org.odpi.openmetadata.commonservices.ocf.metadatamanagement.client.OCFRESTClient;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;


/**
 * GovernanceEngineRESTClient is responsible for issuing calls to the OMAS REST APIs.
 */
public class GovernanceEngineRESTClient extends OCFRESTClient
{
    /**
     * Constructor for no authentication with audit log.
     *
     * @param serverName name of the OMAG Server to call
     * @param serverPlatformURLRoot URL root of the server manager where the OMAG Server is running.
     * @param auditLog destination for log messages.
     *
     * @throws InvalidParameterException there is a problem creating the client-side components to issue any
     * REST API calls.
     */
    public GovernanceEngineRESTClient(String   serverName,
                                      String   serverPlatformURLRoot,
                                      AuditLog auditLog) throws InvalidParameterException
    {
        super(serverName, serverPlatformURLRoot, auditLog);
    }


    /**
     * Constructor for no authentication.
     *
     * @param serverName name of the OMAG Server to call
     * @param serverPlatformURLRoot URL root of the server manager where the OMAG Server is running.
     * @throws InvalidParameterException there is a problem creating the client-side components to issue any
     * REST API calls.
     */
    public GovernanceEngineRESTClient(String serverName,
                                      String serverPlatformURLRoot) throws InvalidParameterException
    {
        super(serverName, serverPlatformURLRoot);
    }


    /**
     * Constructor for simple userId and password authentication with audit log.
     *
     * @param serverName name of the OMAG Server to call
     * @param serverPlatformURLRoot URL root of the server manager where the OMAG Server is running.
     * @param userId user id for the HTTP request
     * @param password password for the HTTP request
     * @param auditLog destination for log messages.
     * @throws InvalidParameterException there is a problem creating the client-side components to issue any
     * REST API calls.
     */
    public GovernanceEngineRESTClient(String   serverName,
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
     * @param serverPlatformURLRoot URL root of the server manager where the OMAG Server is running.
     * @param userId user id for the HTTP request
     * @param password password for the HTTP request
     * @throws InvalidParameterException there is a problem creating the client-side components to issue any
     * REST API calls.
     */
    public GovernanceEngineRESTClient(String serverName,
                                      String serverPlatformURLRoot,
                                      String userId,
                                      String password) throws InvalidParameterException
    {
        super(serverName, serverPlatformURLRoot, userId, password);
    }



    /**
     * Issue a GET REST call that returns a GovernanceEngineElementResponse object.
     *
     * @param methodName  name of the method being called
     * @param urlTemplate template of the URL for the REST API call with place-holders for the parameters
     * @param params      a list of parameters that are slotted into the url template
     *
     * @return response object
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws UserNotAuthorizedException the user is not authorized to make this request.
     * @throws PropertyServerException the repository is not available or not working properly.
     */
    public GovernanceEngineElementResponse callGovernanceEngineGetRESTCall(String    methodName,
                                                                           String    urlTemplate,
                                                                           Object... params) throws InvalidParameterException,
                                                                                                    UserNotAuthorizedException,
                                                                                                    PropertyServerException
    {
        GovernanceEngineElementResponse restResult = this.callGetRESTCall(methodName, GovernanceEngineElementResponse.class, urlTemplate, params);

        exceptionHandler.detectAndThrowStandardExceptions(methodName, restResult);

        return restResult;
    }




    /**
     * Issue a POST REST call that returns a GovernanceEngineElementResponse object.
     *
     * @param methodName  name of the method being called
     * @param urlTemplate template of the URL for the REST API call with place-holders for the parameters
     * @param requestBody object that passes additional parameters
     * @param params      a list of parameters that are slotted into the url template
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
        GovernanceEngineElementResponse restResult = this.callPostRESTCall(methodName, GovernanceEngineElementResponse.class, urlTemplate, requestBody, params);

        exceptionHandler.detectAndThrowStandardExceptions(methodName, restResult);

        return restResult;
    }



    /**
     * Issue a GET REST call that returns a GovernanceEngineElementsResponse object.
     *
     * @param methodName  name of the method being called.
     * @param urlTemplate template of the URL for the REST API call with place-holders for the parameters.
     * @param params      a list of parameters that are slotted into the url template.
     *
     * @return response object
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws UserNotAuthorizedException the user is not authorized to make this request.
     * @throws PropertyServerException the repository is not available or not working properly.
     */
    public GovernanceEngineElementsResponse callGovernanceEnginesGetRESTCall(String    methodName,
                                                                             String    urlTemplate,
                                                                             Object... params) throws InvalidParameterException,
                                                                                                      UserNotAuthorizedException,
                                                                                                      PropertyServerException
    {
        GovernanceEngineElementsResponse restResult = this.callGetRESTCall(methodName, GovernanceEngineElementsResponse.class, urlTemplate, params);

        exceptionHandler.detectAndThrowStandardExceptions(methodName, restResult);

        return restResult;
    }


    /**
     * Issue a GET REST call that returns a GovernanceServiceElementResponse object.
     *
     * @param methodName  name of the method being called.
     * @param urlTemplate template of the URL for the REST API call with place-holders for the parameters.
     * @param params      a list of parameters that are slotted into the url template.
     *
     * @return response object
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws UserNotAuthorizedException the user is not authorized to make this request.
     * @throws PropertyServerException the repository is not available or not working properly.
     */
    public GovernanceServiceElementResponse callGovernanceServiceGetRESTCall(String    methodName,
                                                                             String    urlTemplate,
                                                                             Object... params) throws InvalidParameterException,
                                                                                                      UserNotAuthorizedException,
                                                                                                      PropertyServerException
    {
        GovernanceServiceElementResponse restResult = this.callGetRESTCall(methodName, GovernanceServiceElementResponse.class, urlTemplate, params);

        exceptionHandler.detectAndThrowStandardExceptions(methodName, restResult);

        return restResult;
    }


    /**
     * Issue a GET REST call that returns a GovernanceServiceElementsResponse object.
     *
     * @param methodName  name of the method being called.
     * @param urlTemplate template of the URL for the REST API call with place-holders for the parameters.
     * @param params      a list of parameters that are slotted into the url template.
     *
     * @return response object
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws UserNotAuthorizedException the user is not authorized to make this request.
     * @throws PropertyServerException the repository is not available or not working properly.
     */
    public GovernanceServiceElementsResponse callGovernanceServicesGetRESTCall(String    methodName,
                                                                               String    urlTemplate,
                                                                               Object... params) throws InvalidParameterException,
                                                                                                        UserNotAuthorizedException,
                                                                                                        PropertyServerException
    {
        GovernanceServiceElementsResponse restResult = this.callGetRESTCall(methodName, GovernanceServiceElementsResponse.class, urlTemplate, params);

        exceptionHandler.detectAndThrowStandardExceptions(methodName, restResult);

        return restResult;
    }



    /**
     * Issue a GET REST call that returns a RegisteredGovernanceServiceResponse object.
     *
     * @param methodName  name of the method being called.
     * @param urlTemplate template of the URL for the REST API call with place-holders for the parameters.
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
     * Issue a POST REST call that returns a ElementHeaderResponse object.
     *
     * @param methodName  name of the method being called.
     * @param urlTemplate template of the URL for the REST API call with place-holders for the parameters.
     * @param requestBody object that passes additional parameters
     * @param params      a list of parameters that are slotted into the url template.
     *
     * @return response object
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws UserNotAuthorizedException the user is not authorized to make this request.
     * @throws PropertyServerException the repository is not available or not working properly.
     */
    public ElementHeaderResponse callElementHeaderPostRESTCall(String    methodName,
                                                               String    urlTemplate,
                                                               Object    requestBody,
                                                               Object... params) throws InvalidParameterException,
                                                                                        UserNotAuthorizedException,
                                                                                        PropertyServerException
    {
        ElementHeaderResponse restResult = this.callPostRESTCall(methodName,
                                                                 ElementHeaderResponse.class,
                                                                 urlTemplate,
                                                                 requestBody,
                                                                 params);

        exceptionHandler.detectAndThrowStandardExceptions(methodName, restResult);

        return restResult;
    }


    /**
     * Issue a POST REST call that returns a ElementHeadersResponse object.
     *
     * @param methodName  name of the method being called.
     * @param urlTemplate template of the URL for the REST API call with place-holders for the parameters.
     * @param requestBody object that passes additional parameters
     * @param params      a list of parameters that are slotted into the url template.
     *
     * @return response object
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws UserNotAuthorizedException the user is not authorized to make this request.
     * @throws PropertyServerException the repository is not available or not working properly.
     */
    public ElementHeadersResponse callElementHeadersPostRESTCall(String    methodName,
                                                                 String    urlTemplate,
                                                                 Object    requestBody,
                                                                 Object... params) throws InvalidParameterException,
                                                                                          UserNotAuthorizedException,
                                                                                          PropertyServerException
    {
        ElementHeadersResponse restResult = this.callPostRESTCall(methodName,
                                                                  ElementHeadersResponse.class,
                                                                  urlTemplate,
                                                                  requestBody,
                                                                  params);

        exceptionHandler.detectAndThrowStandardExceptions(methodName, restResult);

        return restResult;
    }


    /**
     * Issue a GET REST call that returns a OpenMetadataElementsResponse object.
     *
     * @param methodName  name of the method being called.
     * @param urlTemplate template of the URL for the REST API call with place-holders for the parameters.
     * @param params      a list of parameters that are slotted into the url template.
     *
     * @return response object
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws UserNotAuthorizedException the user is not authorized to make this request.
     * @throws PropertyServerException the repository is not available or not working properly.
     */
    public OpenMetadataElementsResponse callOpenMetadataElementsGetRESTCall(String    methodName,
                                                                             String    urlTemplate,
                                                                             Object... params) throws InvalidParameterException,
                                                                                                      UserNotAuthorizedException,
                                                                                                      PropertyServerException
    {
        OpenMetadataElementsResponse restResult = this.callGetRESTCall(methodName, OpenMetadataElementsResponse.class, urlTemplate, params);

        exceptionHandler.detectAndThrowStandardExceptions(methodName, restResult);

        return restResult;
    }


    /**
     * Issue a POST REST call that returns a OpenMetadataElementsResponse object.
     *
     * @param methodName  name of the method being called.
     * @param urlTemplate template of the URL for the REST API call with place-holders for the parameters.
     * @param requestBody object that passes additional parameters
     * @param params      a list of parameters that are slotted into the url template.
     *
     * @return response object
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws UserNotAuthorizedException the user is not authorized to make this request.
     * @throws PropertyServerException the repository is not available or not working properly.
     */
    public OpenMetadataElementsResponse callOpenMetadataElementsPostRESTCall(String    methodName,
                                                                             String    urlTemplate,
                                                                             Object    requestBody,
                                                                             Object... params) throws InvalidParameterException,
                                                                                                      UserNotAuthorizedException,
                                                                                                      PropertyServerException
    {
        OpenMetadataElementsResponse restResult = this.callPostRESTCall(methodName,
                                                                        OpenMetadataElementsResponse.class,
                                                                        urlTemplate,
                                                                        requestBody,
                                                                        params);

        exceptionHandler.detectAndThrowStandardExceptions(methodName, restResult);

        return restResult;
    }


    /**
     * Issue a GET REST call that returns a OpenMetadataElementResponse object.
     *
     * @param methodName  name of the method being called.
     * @param urlTemplate template of the URL for the REST API call with place-holders for the parameters.
     * @param params      a list of parameters that are slotted into the url template.
     *
     * @return response object
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws UserNotAuthorizedException the user is not authorized to make this request.
     * @throws PropertyServerException the repository is not available or not working properly.
     */
    public OpenMetadataElementResponse callOpenMetadataElementGetRESTCall(String    methodName,
                                                                          String    urlTemplate,
                                                                          Object... params) throws InvalidParameterException,
                                                                                                   UserNotAuthorizedException,
                                                                                                   PropertyServerException
    {
        OpenMetadataElementResponse restResult = this.callGetRESTCall(methodName, OpenMetadataElementResponse.class, urlTemplate, params);

        exceptionHandler.detectAndThrowStandardExceptions(methodName, restResult);

        return restResult;
    }


    /**
     * Issue a POST REST call that returns a RelatedMetadataElementsResponse object.
     *
     * @param methodName  name of the method being called.
     * @param urlTemplate template of the URL for the REST API call with place-holders for the parameters.
     * @param requestBody object that passes additional parameters
     * @param params      a list of parameters that are slotted into the url template.
     *
     * @return response object
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws UserNotAuthorizedException the user is not authorized to make this request.
     * @throws PropertyServerException the repository is not available or not working properly.
     */
    public RelatedMetadataElementsResponse callRelatedMetadataElementsPostRESTCall(String    methodName,
                                                                                   String    urlTemplate,
                                                                                   Object    requestBody,
                                                                                   Object... params) throws InvalidParameterException,
                                                                                                            UserNotAuthorizedException,
                                                                                                            PropertyServerException
    {
        RelatedMetadataElementsResponse restResult = this.callPostRESTCall(methodName,
                                                                           RelatedMetadataElementsResponse.class,
                                                                           urlTemplate,
                                                                           requestBody,
                                                                           params);

        exceptionHandler.detectAndThrowStandardExceptions(methodName, restResult);

        return restResult;
    }



    /**
     * Issue a GET REST call that returns a RelatedMetadataElementsResponse object.
     *
     * @param methodName  name of the method being called.
     * @param urlTemplate template of the URL for the REST API call with place-holders for the parameters.
     * @param params      a list of parameters that are slotted into the url template.
     *
     * @return response object
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws UserNotAuthorizedException the user is not authorized to make this request.
     * @throws PropertyServerException the repository is not available or not working properly.
     */
    public RelatedMetadataElementsResponse callRelatedMetadataElementsGetRESTCall(String    methodName,
                                                                                  String    urlTemplate,
                                                                                  Object... params) throws InvalidParameterException,
                                                                                                           UserNotAuthorizedException,
                                                                                                           PropertyServerException
    {
        RelatedMetadataElementsResponse restResult = this.callGetRESTCall(methodName, RelatedMetadataElementsResponse.class, urlTemplate, params);

        exceptionHandler.detectAndThrowStandardExceptions(methodName, restResult);

        return restResult;
    }
}
