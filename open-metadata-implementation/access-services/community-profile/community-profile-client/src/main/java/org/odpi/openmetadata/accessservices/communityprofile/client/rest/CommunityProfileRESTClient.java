/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.communityprofile.client.rest;

import org.odpi.openmetadata.accessservices.communityprofile.rest.*;
import org.odpi.openmetadata.commonservices.ffdc.rest.FFDCRESTClient;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;

/**
 * CommunityProfileRESTClient is responsible for issuing calls to the Community Profile OMAS REST APIs.
 */
public class CommunityProfileRESTClient extends FFDCRESTClient
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
    public CommunityProfileRESTClient(String    serverName,
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
    public CommunityProfileRESTClient(String serverName,
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
    public CommunityProfileRESTClient(String   serverName,
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
    public CommunityProfileRESTClient(String serverName,
                                      String serverPlatformURLRoot,
                                      String userId,
                                      String password) throws InvalidParameterException
    {
        super(serverName, serverPlatformURLRoot, userId, password);
    }


    /**
     * Issue a GET REST call that returns a MetadataSourceResponse object.
     *
     * @param methodName  name of the method being called.
     * @param urlTemplate  REST API call URL template with place-holders for the parameters.
     * @param params      a list of parameters that are slotted into the url template.
     *
     * @return MetadataSourceResponse
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws UserNotAuthorizedException the user is not authorized to make this request.
     * @throws PropertyServerException something went wrong with the REST call stack.
     */
    public MetadataSourceResponse callMetadataSourceGetRESTCall(String    methodName,
                                                                String    urlTemplate,
                                                                Object... params) throws InvalidParameterException,
                                                                                         UserNotAuthorizedException,
                                                                                         PropertyServerException
    {
        MetadataSourceResponse restResult = this.callGetRESTCall(methodName, MetadataSourceResponse.class, urlTemplate, params);

        exceptionHandler.detectAndThrowStandardExceptions(methodName, restResult);

        return restResult;
    }


    /**
     * Issue a GET REST call that returns a ActorProfileElement in a response object.
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
    public ActorProfileResponse callActorProfileGetRESTCall(String    methodName,
                                                            String    urlTemplate,
                                                            Object... params) throws InvalidParameterException,
                                                                                     UserNotAuthorizedException,
                                                                                     PropertyServerException
    {
        ActorProfileResponse restResult = this.callGetRESTCall(methodName, ActorProfileResponse.class, urlTemplate, params);

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
     * Issue a GET REST call that returns a list of ActorProfileElements in a response object.
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
    public ActorProfileListResponse callActorProfileListPostRESTCall(String    methodName,
                                                                     String    urlTemplate,
                                                                     Object    requestBody,
                                                                     Object... params)  throws InvalidParameterException,
                                                                                               UserNotAuthorizedException,
                                                                                               PropertyServerException
    {
        ActorProfileListResponse restResult = this.callPostRESTCall(methodName, ActorProfileListResponse.class, urlTemplate, requestBody, params);

        exceptionHandler.detectAndThrowStandardExceptions(methodName, restResult);

        return restResult;
    }


    /**
     * Issue a GET REST call that returns a CommunityElement in a response object.
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
    public CommunityResponse callCommunityGetRESTCall(String    methodName,
                                                      String    urlTemplate,
                                                      Object... params) throws InvalidParameterException,
                                                                               UserNotAuthorizedException,
                                                                               PropertyServerException
    {
        CommunityResponse restResult = this.callGetRESTCall(methodName, CommunityResponse.class, urlTemplate, params);

        exceptionHandler.detectAndThrowStandardExceptions(methodName, restResult);

        return restResult;
    }


    /**
     * Issue a GET REST call that returns a list of CommunityElements in a response object.
     *
     * @param methodName  name of the method being called
     * @param urlTemplate   REST API call URL template with place-holders for the parameters
     * @param params  a list of parameters that are slotted into the url template
     * @return response object
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws UserNotAuthorizedException the user is not authorized to make this request.
     * @throws PropertyServerException something went wrong with the REST call stack.
     */
    public CommunityListResponse callCommunityListGetRESTCall(String    methodName,
                                                              String    urlTemplate,
                                                              Object... params)  throws InvalidParameterException,
                                                                                        UserNotAuthorizedException,
                                                                                        PropertyServerException
    {
        CommunityListResponse restResult = this.callGetRESTCall(methodName, CommunityListResponse.class, urlTemplate, params);

        exceptionHandler.detectAndThrowStandardExceptions(methodName, restResult);

        return restResult;
    }


    /**
     * Issue a GET REST call that returns a list of CommunityElements in a response object.
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
    public CommunityListResponse callCommunityListPostRESTCall(String    methodName,
                                                               String    urlTemplate,
                                                               Object    requestBody,
                                                               Object... params)  throws InvalidParameterException,
                                                                                         UserNotAuthorizedException,
                                                                                         PropertyServerException
    {
        CommunityListResponse restResult = this.callPostRESTCall(methodName, CommunityListResponse.class, urlTemplate, requestBody, params);

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
     * Issue a POST REST call that returns a list of PersonRoleAppointees in a response object.
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
    public PersonRoleAppointeeListResponse callPersonRoleAppointeeListPostRESTCall(String    methodName,
                                                                                   String    urlTemplate,
                                                                                   Object    requestBody,
                                                                                   Object... params)  throws InvalidParameterException,
                                                                                                             UserNotAuthorizedException,
                                                                                                             PropertyServerException
    {
        PersonRoleAppointeeListResponse restResult = this.callPostRESTCall(methodName, PersonRoleAppointeeListResponse.class, urlTemplate, requestBody, params);

        exceptionHandler.detectAndThrowStandardExceptions(methodName, restResult);

        return restResult;
    }


    /**
     * Issue a GET REST call that returns a UserIdentityElement in a response object.
     *
     * @param methodName  name of the method being called.
     * @param urlTemplate  REST API call URL template with place-holders for the parameters.
     * @param params      a list of parameters that are slotted into the url template.
     *
     * @return UserIdentityResponse
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws UserNotAuthorizedException the user is not authorized to make this request.
     * @throws PropertyServerException something went wrong with the REST call stack.
     */
    public UserIdentityResponse callUserIdentityGetRESTCall(String    methodName,
                                                            String    urlTemplate,
                                                            Object... params) throws InvalidParameterException,
                                                                                     UserNotAuthorizedException,
                                                                                     PropertyServerException
    {
        UserIdentityResponse restResult = this.callGetRESTCall(methodName, UserIdentityResponse.class, urlTemplate, params);

        exceptionHandler.detectAndThrowStandardExceptions(methodName, restResult);

        return restResult;
    }


    /**
     * Issue a GET REST call that returns a list of UserIdentityElements in a response object.
     *
     * @param methodName  name of the method being called
     * @param urlTemplate   REST API call URL template with place-holders for the parameters
     * @param params  a list of parameters that are slotted into the url template
     * @return UserIdentityListResponse
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws UserNotAuthorizedException the user is not authorized to make this request.
     * @throws PropertyServerException something went wrong with the REST call stack.
     */
    public UserIdentityListResponse callUserIdentityListGetRESTCall(String    methodName,
                                                                    String    urlTemplate,
                                                                    Object... params)  throws InvalidParameterException,
                                                                                              UserNotAuthorizedException,
                                                                                              PropertyServerException
    {
        UserIdentityListResponse restResult = this.callGetRESTCall(methodName, UserIdentityListResponse.class, urlTemplate, params);

        exceptionHandler.detectAndThrowStandardExceptions(methodName, restResult);

        return restResult;
    }


    /**
     * Issue a POST REST call that returns a list of UserIdentityElements in a response object.
     *
     * @param methodName  name of the method being called
     * @param urlTemplate   REST API call URL template with place-holders for the parameters
     * @param requestBody request body for the request
     * @param params  a list of parameters that are slotted into the url template
     * @return UserIdentityListResponse
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws UserNotAuthorizedException the user is not authorized to make this request.
     * @throws PropertyServerException something went wrong with the REST call stack.
     */
    public UserIdentityListResponse callUserIdentityListPostRESTCall(String    methodName,
                                                                     String    urlTemplate,
                                                                     Object    requestBody,
                                                                     Object... params)  throws InvalidParameterException,
                                                                                              UserNotAuthorizedException,
                                                                                              PropertyServerException
    {
        UserIdentityListResponse restResult = this.callPostRESTCall(methodName, UserIdentityListResponse.class, urlTemplate, requestBody, params);

        exceptionHandler.detectAndThrowStandardExceptions(methodName, restResult);

        return restResult;
    }


    /**
     * Issue a GET REST call that returns a PersonalProfileUniverse in a response  object.
     *
     * @param methodName  name of the method being called.
     * @param urlTemplate  REST API call URL template with place-holders for the parameters.
     * @param params      a list of parameters that are slotted into the url template.
     *
     * @return PersonalProfileResponse
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws UserNotAuthorizedException the user is not authorized to make this request.
     * @throws PropertyServerException something went wrong with the REST call stack.
     */
    public PersonalProfileResponse callPersonalProfileGetRESTCall(String    methodName,
                                                                  String    urlTemplate,
                                                                  Object... params) throws InvalidParameterException,
                                                                                           UserNotAuthorizedException,
                                                                                           PropertyServerException
    {
        PersonalProfileResponse restResult = this.callGetRESTCall(methodName, PersonalProfileResponse.class, urlTemplate, params);

        exceptionHandler.detectAndThrowStandardExceptions(methodName, restResult);

        return restResult;
    }



    /**
     * Issue a POST REST call that returns a  PersonalProfileElement in a response object.
     *
     * @param methodName  name of the method being called
     * @param urlTemplate   REST API call URL template with place-holders for the parameters
     * @param requestBody request body for the request
     * @param params  a list of parameters that are slotted into the url template
     * @return PersonalProfileResponse
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws UserNotAuthorizedException the user is not authorized to make this request.
     * @throws PropertyServerException something went wrong with the REST call stack.
     */
    public PersonalProfileResponse callPersonalProfilePostRESTCall(String    methodName,
                                                                   String    urlTemplate,
                                                                   Object    requestBody,
                                                                   Object... params)  throws InvalidParameterException,
                                                                                             UserNotAuthorizedException,
                                                                                             PropertyServerException
    {
        PersonalProfileResponse restResult = this.callPostRESTCall(methodName, PersonalProfileResponse.class, urlTemplate, requestBody, params);

        exceptionHandler.detectAndThrowStandardExceptions(methodName, restResult);

        return restResult;
    }



    /**
     * Issue a GET REST call that returns a list of PersonalProfileElements in a response object.
     *
     * @param methodName  name of the method being called
     * @param urlTemplate   REST API call URL template with place-holders for the parameters
     * @param params  a list of parameters that are slotted into the url template
     * @return PersonalProfileListResponse
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws UserNotAuthorizedException the user is not authorized to make this request.
     * @throws PropertyServerException something went wrong with the REST call stack.
     */
    public PersonalProfileListResponse callPersonalProfileListGetRESTCall(String    methodName,
                                                                          String    urlTemplate,
                                                                          Object... params)  throws InvalidParameterException,
                                                                                                    UserNotAuthorizedException,
                                                                                                    PropertyServerException
    {
        PersonalProfileListResponse restResult = this.callGetRESTCall(methodName, PersonalProfileListResponse.class, urlTemplate, params);

        exceptionHandler.detectAndThrowStandardExceptions(methodName, restResult);

        return restResult;
    }



    /**
     * Issue a POST REST call that returns a list of PersonalProfileElements in a response object.
     *
     * @param methodName  name of the method being called
     * @param urlTemplate   REST API call URL template with place-holders for the parameters
     * @param requestBody request body for the request
     * @param params  a list of parameters that are slotted into the url template
     * @return PersonalProfileListResponse
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws UserNotAuthorizedException the user is not authorized to make this request.
     * @throws PropertyServerException something went wrong with the REST call stack.
     */
    public PersonalProfileListResponse callPersonalProfileListPostRESTCall(String    methodName,
                                                                           String    urlTemplate,
                                                                           Object    requestBody,
                                                                           Object... params)  throws InvalidParameterException,
                                                                                                     UserNotAuthorizedException,
                                                                                                     PropertyServerException
    {
        PersonalProfileListResponse restResult = this.callPostRESTCall(methodName, PersonalProfileListResponse.class, urlTemplate, requestBody, params);

        exceptionHandler.detectAndThrowStandardExceptions(methodName, restResult);

        return restResult;
    }



    /**
     * Issue a GET REST call that returns an AssetListResponse object.
     *
     * @param methodName  name of the method being called.
     * @param urlTemplate  REST API call URL template with place-holders for the parameters.
     * @param params      a list of parameters that are slotted into the url template.
     *
     * @return AssetListResponse
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws UserNotAuthorizedException the user is not authorized to make this request.
     * @throws PropertyServerException something went wrong with the REST call stack.
     */
    public AssetListResponse callAssetListGetRESTCall(String    methodName,
                                                      String    urlTemplate,
                                                      Object... params)  throws InvalidParameterException,
                                                                                UserNotAuthorizedException,
                                                                                PropertyServerException
    {
        AssetListResponse restResult = this.callGetRESTCall(methodName, AssetListResponse.class, urlTemplate, params);

        exceptionHandler.detectAndThrowStandardExceptions(methodName, restResult);

        return restResult;
    }



    /**
     * Issue a GET REST call that returns a SecurityGroupResponse object.
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
    public SecurityGroupResponse callSecurityGroupGetRESTCall(String    methodName,
                                                              String    urlTemplate,
                                                              Object... params) throws InvalidParameterException,
                                                                                       UserNotAuthorizedException,
                                                                                       PropertyServerException
    {
        SecurityGroupResponse restResult = this.callGetRESTCall(methodName, SecurityGroupResponse.class, urlTemplate, params);

        exceptionHandler.detectAndThrowStandardExceptions(methodName, restResult);

        return restResult;
    }


    /**
     * Issue a POST REST call that returns a SecurityGroupResponse object.
     *
     * @param methodName  name of the method being called.
     * @param urlTemplate  REST API call URL template with place-holders for the parameters.
     * @param requestBody object that passes additional parameters
     * @param params      a list of parameters that are slotted into the url template.
     *
     * @return response object
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws UserNotAuthorizedException the user is not authorized to make this request.
     * @throws PropertyServerException the repository is not available or not working properly.
     */
    public SecurityGroupResponse callSecurityGroupPostRESTCall(String    methodName,
                                                               String    urlTemplate,
                                                               Object    requestBody,
                                                               Object... params) throws InvalidParameterException,
                                                                                        UserNotAuthorizedException,
                                                                                        PropertyServerException
    {
        SecurityGroupResponse restResult = this.callPostRESTCall(methodName, SecurityGroupResponse.class, urlTemplate, requestBody, params);

        exceptionHandler.detectAndThrowStandardExceptions(methodName, restResult);

        return restResult;
    }


    /**
     * Issue a GET REST call that returns a SecurityGroupsResponse object.
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
    public SecurityGroupsResponse callSecurityGroupsGetRESTCall(String    methodName,
                                                                String    urlTemplate,
                                                                Object... params) throws InvalidParameterException,
                                                                                         UserNotAuthorizedException,
                                                                                         PropertyServerException
    {
        SecurityGroupsResponse restResult = this.callGetRESTCall(methodName, SecurityGroupsResponse.class, urlTemplate, params);

        exceptionHandler.detectAndThrowStandardExceptions(methodName, restResult);

        return restResult;
    }


    /**
     * Issue a POST REST call that returns a SecurityGroupsResponse object.
     *
     * @param methodName  name of the method being called.
     * @param urlTemplate  REST API call URL template with place-holders for the parameters.
     * @param requestBody object that passes additional parameters
     * @param params      a list of parameters that are slotted into the url template.
     *
     * @return response object
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws UserNotAuthorizedException the user is not authorized to make this request.
     * @throws PropertyServerException the repository is not available or not working properly.
     */
    public SecurityGroupsResponse callSecurityGroupsPostRESTCall(String    methodName,
                                                                 String    urlTemplate,
                                                                 Object    requestBody,
                                                                 Object... params) throws InvalidParameterException,
                                                                                          UserNotAuthorizedException,
                                                                                          PropertyServerException
    {
        SecurityGroupsResponse restResult = this.callPostRESTCall(methodName, SecurityGroupsResponse.class, urlTemplate, requestBody, params);

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


    /**
     * Issue a GET REST call that returns a LocationResponse object.
     *
     * @param methodName  name of the method being called.
     * @param urlTemplate  REST API call URL template with place-holders for the parameters.
     * @param params      a list of parameters that are slotted into the url template.
     *
     * @return LocationResponse
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws UserNotAuthorizedException the user is not authorized to make this request.
     * @throws PropertyServerException the repository is not available or not working properly.
     */
    public LocationResponse callLocationGetRESTCall(String    methodName,
                                                    String    urlTemplate,
                                                    Object... params) throws InvalidParameterException,
                                                                             UserNotAuthorizedException,
                                                                             PropertyServerException
    {
        LocationResponse restResult = this.callGetRESTCall(methodName, LocationResponse.class, urlTemplate, params);

        exceptionHandler.detectAndThrowStandardExceptions(methodName, restResult);

        return restResult;
    }


    /**
     * Issue a GET REST call that returns a LocationListResponse object.
     *
     * @param methodName  name of the method being called.
     * @param urlTemplate  REST API call URL template with place-holders for the parameters.
     * @param params      a list of parameters that are slotted into the url template.
     *
     * @return LocationListResponse
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws UserNotAuthorizedException the user is not authorized to make this request.
     * @throws PropertyServerException the repository is not available or not working properly.
     */
    public LocationListResponse callLocationsGetRESTCall(String    methodName,
                                                         String    urlTemplate,
                                                         Object... params) throws InvalidParameterException,
                                                                                  UserNotAuthorizedException,
                                                                                  PropertyServerException
    {
        LocationListResponse restResult = this.callGetRESTCall(methodName, LocationListResponse.class, urlTemplate, params);

        exceptionHandler.detectAndThrowStandardExceptions(methodName, restResult);

        return restResult;
    }


    /**
     * Issue a POST REST call that returns a LocationListResponse object.
     *
     * @param methodName  name of the method being called.
     * @param urlTemplate  REST API call URL template with place-holders for the parameters.
     * @param requestBody properties describing the valid value definition/set
     * @param params      a list of parameters that are slotted into the url template.
     *
     * @return LocationListResponse
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws UserNotAuthorizedException the user is not authorized to make this request.
     * @throws PropertyServerException the repository is not available or not working properly.
     */
    public LocationListResponse callLocationsPostRESTCall(String    methodName,
                                                          String    urlTemplate,
                                                          Object    requestBody,
                                                          Object... params) throws InvalidParameterException,
                                                                                UserNotAuthorizedException,
                                                                                PropertyServerException
    {
        LocationListResponse restResult = this.callPostRESTCall(methodName, LocationListResponse.class, urlTemplate, requestBody, params);

        exceptionHandler.detectAndThrowStandardExceptions(methodName, restResult);

        return restResult;
    }


    /**
     * Issue a GET REST call that returns a ValidValueElement in a response object.
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
    public ValidValueResponse callValidValueGetRESTCall(String    methodName,
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
     * Issue a GET REST call that returns a list of ValidValueElements in a response object.
     *
     * @param methodName  name of the method being called
     * @param urlTemplate   REST API call URL template with place-holders for the parameters
     * @param params  a list of parameters that are slotted into the url template
     * @return response object
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws UserNotAuthorizedException the user is not authorized to make this request.
     * @throws PropertyServerException something went wrong with the REST call stack.
     */
    public ValidValueListResponse callValidValueListGetRESTCall(String    methodName,
                                                                String    urlTemplate,
                                                                Object... params)  throws InvalidParameterException,
                                                                                          UserNotAuthorizedException,
                                                                                          PropertyServerException
    {
        ValidValueListResponse restResult = this.callGetRESTCall(methodName, ValidValueListResponse.class, urlTemplate, params);

        exceptionHandler.detectAndThrowStandardExceptions(methodName, restResult);

        return restResult;
    }


    /**
     * Issue a GET REST call that returns a list of ValidValueElements in a response object.
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
    public ValidValueListResponse callValidValueListPostRESTCall(String    methodName,
                                                                 String    urlTemplate,
                                                                 Object    requestBody,
                                                                 Object... params)  throws InvalidParameterException,
                                                                                           UserNotAuthorizedException,
                                                                                           PropertyServerException
    {
        ValidValueListResponse restResult = this.callPostRESTCall(methodName, ValidValueListResponse.class, urlTemplate, requestBody, params);

        exceptionHandler.detectAndThrowStandardExceptions(methodName, restResult);

        return restResult;
    }
}
