/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.projectmanagement.client.rest;

import org.odpi.openmetadata.accessservices.projectmanagement.rest.ActorProfileListResponse;
import org.odpi.openmetadata.accessservices.projectmanagement.rest.ElementStubsResponse;
import org.odpi.openmetadata.accessservices.projectmanagement.rest.PersonRoleListResponse;
import org.odpi.openmetadata.accessservices.projectmanagement.rest.PersonRoleResponse;
import org.odpi.openmetadata.accessservices.projectmanagement.rest.ProjectListResponse;
import org.odpi.openmetadata.accessservices.projectmanagement.rest.ProjectResponse;
import org.odpi.openmetadata.accessservices.projectmanagement.rest.RelatedElementListResponse;
import org.odpi.openmetadata.commonservices.ffdc.rest.FFDCRESTClient;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;

/**
 * ProjectManagementRESTClient is responsible for issuing calls to the Project Profile OMAS REST APIs.
 */
public class ProjectManagementRESTClient extends FFDCRESTClient
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
    public ProjectManagementRESTClient(String    serverName,
                                       String    serverPlatformURLRoot,
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
    public ProjectManagementRESTClient(String serverName,
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
    public ProjectManagementRESTClient(String   serverName,
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
    public ProjectManagementRESTClient(String serverName,
                                       String serverPlatformURLRoot,
                                       String userId,
                                       String password) throws InvalidParameterException
    {
        super(serverName, serverPlatformURLRoot, userId, password);
    }


    /**
     * Issue a GET REST call that returns a ProjectElement in a response object.
     *
     * @param methodName  name of the method being called.
     * @param urlTemplate  REST API call URL template with place-holders for the parameters.
     * @param params      a list of parameters that are slotted into the url template.
     *
     * @return response object
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws UserNotAuthorizedException the user is not authorized to make this request.
     * @throws PropertyServerException something went wrong with the REST call stack.
     */
    public ProjectResponse callProjectGetRESTCall(String    methodName,
                                                  String    urlTemplate,
                                                  Object... params) throws InvalidParameterException,
                                                                               UserNotAuthorizedException,
                                                                               PropertyServerException
    {
        ProjectResponse restResult = this.callGetRESTCall(methodName, ProjectResponse.class, urlTemplate, params);

        exceptionHandler.detectAndThrowStandardExceptions(methodName, restResult);

        return restResult;
    }


    /**
     * Issue a GET REST call that returns a list of ProjectElements in a response object.
     *
     * @param methodName  name of the method being called
     * @param urlTemplate   REST API call URL template with place-holders for the parameters
     * @param params  a list of parameters that are slotted into the url template
     * @return response object
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws UserNotAuthorizedException the user is not authorized to make this request.
     * @throws PropertyServerException something went wrong with the REST call stack.
     */
    public ProjectListResponse callProjectListGetRESTCall(String    methodName,
                                                          String    urlTemplate,
                                                          Object... params)  throws InvalidParameterException,
                                                                                        UserNotAuthorizedException,
                                                                                        PropertyServerException
    {
        ProjectListResponse restResult = this.callGetRESTCall(methodName, ProjectListResponse.class, urlTemplate, params);

        exceptionHandler.detectAndThrowStandardExceptions(methodName, restResult);

        return restResult;
    }


    /**
     * Issue a GET REST call that returns a list of ProjectElements in a response object.
     *
     * @param methodName  name of the method being called
     * @param urlTemplate   REST API call URL template with place-holders for the parameters
     * @param requestBody request body for the request
     * @param params  a list of parameters that are slotted into the url template
     * @return response object
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws UserNotAuthorizedException the user is not authorized to make this request.
     * @throws PropertyServerException something went wrong with the REST call stack.
     */
    public ProjectListResponse callProjectListPostRESTCall(String    methodName,
                                                               String    urlTemplate,
                                                               Object    requestBody,
                                                               Object... params)  throws InvalidParameterException,
                                                                                         UserNotAuthorizedException,
                                                                                         PropertyServerException
    {
        ProjectListResponse restResult = this.callPostRESTCall(methodName, ProjectListResponse.class, urlTemplate, requestBody, params);

        exceptionHandler.detectAndThrowStandardExceptions(methodName, restResult);

        return restResult;
    }


    /**
     * Issue a GET REST call that returns a PersonRoleElement in a response object.
     *
     * @param methodName  name of the method being called.
     * @param urlTemplate  REST API call URL template with place-holders for the parameters.
     * @param params      a list of parameters that are slotted into the url template.
     *
     * @return response object
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws UserNotAuthorizedException the user is not authorized to make this request.
     * @throws PropertyServerException something went wrong with the REST call stack.
     */
    public PersonRoleResponse callPersonRoleGetRESTCall(String    methodName,
                                                        String    urlTemplate,
                                                        Object... params) throws InvalidParameterException,
                                                                                 UserNotAuthorizedException,
                                                                                 PropertyServerException
    {
        PersonRoleResponse restResult = this.callGetRESTCall(methodName, PersonRoleResponse.class, urlTemplate, params);

        exceptionHandler.detectAndThrowStandardExceptions(methodName, restResult);

        return restResult;
    }


    /**
     * Issue a GET REST call that returns a list of PersonRoleElements in a response object.
     *
     * @param methodName  name of the method being called
     * @param urlTemplate   REST API call URL template with place-holders for the parameters
     * @param params  a list of parameters that are slotted into the url template
     * @return response object
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws UserNotAuthorizedException the user is not authorized to make this request.
     * @throws PropertyServerException something went wrong with the REST call stack.
     */
    public PersonRoleListResponse callPersonRoleListGetRESTCall(String    methodName,
                                                                String    urlTemplate,
                                                                Object... params)  throws InvalidParameterException,
                                                                                          UserNotAuthorizedException,
                                                                                          PropertyServerException
    {
        PersonRoleListResponse restResult = this.callGetRESTCall(methodName, PersonRoleListResponse.class, urlTemplate, params);

        exceptionHandler.detectAndThrowStandardExceptions(methodName, restResult);

        return restResult;
    }


    /**
     * Issue a POST REST call that returns a list of PersonRoleElements in a response object.
     *
     * @param methodName  name of the method being called
     * @param urlTemplate   REST API call URL template with place-holders for the parameters
     * @param requestBody request body for the request
     * @param params  a list of parameters that are slotted into the url template
     * @return response object
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws UserNotAuthorizedException the user is not authorized to make this request.
     * @throws PropertyServerException something went wrong with the REST call stack.
     */
    public PersonRoleListResponse callPersonRoleListPostRESTCall(String    methodName,
                                                                 String    urlTemplate,
                                                                 Object    requestBody,
                                                                 Object... params)  throws InvalidParameterException,
                                                                                           UserNotAuthorizedException,
                                                                                           PropertyServerException
    {
        PersonRoleListResponse restResult = this.callPostRESTCall(methodName, PersonRoleListResponse.class, urlTemplate, requestBody, params);

        exceptionHandler.detectAndThrowStandardExceptions(methodName, restResult);

        return restResult;
    }


    /**
     * Issue a GET REST call that returns a list of ActorProfileElements in a response object.
     *
     * @param methodName  name of the method being called
     * @param urlTemplate   REST API call URL template with place-holders for the parameters
     * @param params  a list of parameters that are slotted into the url template
     * @return response object
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws UserNotAuthorizedException the user is not authorized to make this request.
     * @throws PropertyServerException something went wrong with the REST call stack.
     */
    public ActorProfileListResponse callActorProfileListGetRESTCall(String    methodName,
                                                                    String    urlTemplate,
                                                                    Object... params)  throws InvalidParameterException,
                                                                                              UserNotAuthorizedException,
                                                                                              PropertyServerException
    {
        ActorProfileListResponse restResult = this.callGetRESTCall(methodName, ActorProfileListResponse.class, urlTemplate, params);

        exceptionHandler.detectAndThrowStandardExceptions(methodName, restResult);

        return restResult;
    }


    /**
     * Issue a GET REST call that returns a ElementStubsResponse object.
     *
     * @param methodName  name of the method being called.
     * @param urlTemplate  REST API call URL template with place-holders for the parameters.
     * @param params      a list of parameters that are slotted into the url template.
     *
     * @return response object
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws UserNotAuthorizedException the user is not authorized to make this request.
     * @throws PropertyServerException the repository is not available or not working properly.
     */
    public ElementStubsResponse callElementStubsGetRESTCall(String    methodName,
                                                            String    urlTemplate,
                                                            Object... params) throws InvalidParameterException,
                                                                                     UserNotAuthorizedException,
                                                                                     PropertyServerException
    {
        ElementStubsResponse restResult = this.callGetRESTCall(methodName, ElementStubsResponse.class, urlTemplate, params);

        exceptionHandler.detectAndThrowStandardExceptions(methodName, restResult);

        return restResult;
    }



    /**
     * Issue a GET REST call that returns a RelatedElementListResponse object.
     *
     * @param methodName  name of the method being called.
     * @param urlTemplate  REST API call URL template with place-holders for the parameters.
     * @param params      a list of parameters that are slotted into the url template.
     *
     * @return response object
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws UserNotAuthorizedException the user is not authorized to make this request.
     * @throws PropertyServerException the repository is not available or not working properly.
     */
    public RelatedElementListResponse callRelatedElementListGetRESTCall(String    methodName,
                                                                        String    urlTemplate,
                                                                        Object... params) throws InvalidParameterException,
                                                                                                 UserNotAuthorizedException,
                                                                                                 PropertyServerException
    {
        RelatedElementListResponse restResult = this.callGetRESTCall(methodName, RelatedElementListResponse.class, urlTemplate, params);

        exceptionHandler.detectAndThrowStandardExceptions(methodName, restResult);

        return restResult;
    }
}
