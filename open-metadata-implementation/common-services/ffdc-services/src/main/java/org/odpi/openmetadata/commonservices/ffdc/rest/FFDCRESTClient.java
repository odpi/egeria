/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.commonservices.ffdc.rest;

import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.UserNotAuthorizedException;


/**
 * RESTClient is responsible for issuing calls to the OMAS REST APIs.
 */
public class FFDCRESTClient extends FFDCRESTClientBase
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
    public FFDCRESTClient(String    serverName,
                          String    serverPlatformURLRoot,
                          AuditLog  auditLog) throws InvalidParameterException
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
    public FFDCRESTClient(String serverName,
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
    public FFDCRESTClient(String   serverName,
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
    public FFDCRESTClient(String serverName,
                          String serverPlatformURLRoot,
                          String userId,
                          String password) throws InvalidParameterException
    {
        super(serverName, serverPlatformURLRoot, userId, password);
    }


    /**
     * Issue a GET REST call that returns a list of names.
     *
     * @param methodName  name of the method being called.
     * @param urlTemplate template of the URL for the REST API with place-holders for the parameters.
     * @param params      a list of parameters that are slotted into the url template.
     *
     * @return NameListResponse
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws UserNotAuthorizedException the user is not authorized to make this request.
     * @throws PropertyServerException something went wrong with the REST call stack.
     */
    public NameListResponse callNameListGetRESTCall(String    methodName,
                                                    String    urlTemplate,
                                                    Object... params) throws InvalidParameterException,
                                                                             UserNotAuthorizedException,
                                                                             PropertyServerException
    {
        NameListResponse restResult = this.callGetRESTCall(methodName, NameListResponse.class, urlTemplate, params);

        exceptionHandler.detectAndThrowStandardExceptions(methodName, restResult);

        return restResult;
    }


    /**
     * Issue a GET REST call that returns a list of names mapped to their descriptions.
     *
     * @param methodName  name of the method being called.
     * @param urlTemplate template of the URL for the REST API with place-holders for the parameters.
     * @param params      a list of parameters that are slotted into the url template.
     *
     * @return StringMapResponse
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws UserNotAuthorizedException the user is not authorized to make this request.
     * @throws PropertyServerException something went wrong with the REST call stack.
     */
    public StringMapResponse callStringMapGetRESTCall(String    methodName,
                                                      String    urlTemplate,
                                                      Object... params) throws InvalidParameterException,
                                                                               UserNotAuthorizedException,
                                                                               PropertyServerException
    {
        StringMapResponse restResult = this.callGetRESTCall(methodName, StringMapResponse.class, urlTemplate, params);

        exceptionHandler.detectAndThrowStandardExceptions(methodName, restResult);

        return restResult;
    }


    /**
     * Issue a GET REST call that returns a GUIDResponse object.
     *
     * @param methodName  name of the method being called.
     * @param urlTemplate template of the URL for the REST API with place-holders for the parameters.
     * @param params      a list of parameters that are slotted into the url template.
     *
     * @return GUIDResponse
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws UserNotAuthorizedException the user is not authorized to make this request.
     * @throws PropertyServerException something went wrong with the REST call stack.
     */
    public    GUIDResponse callGUIDGetRESTCall(String    methodName,
                                               String    urlTemplate,
                                               Object... params) throws InvalidParameterException,
                                                                        UserNotAuthorizedException,
                                                                        PropertyServerException
    {
        GUIDResponse restResult = this.callGetRESTCall(methodName, GUIDResponse.class, urlTemplate, params);

        exceptionHandler.detectAndThrowStandardExceptions(methodName, restResult);

        return restResult;
    }



    /**
     * Issue a POST REST call that returns a guid object.
     *
     * @param methodName  name of the method being called
     * @param urlTemplate  template of the URL for the REST API with place-holders for the parameters.
     * @param requestBody request body for the request.
     * @param params  a list of parameters that are slotted into the url template.
     *
     * @return GUIDResponse
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws UserNotAuthorizedException the user is not authorized to make this request.
     * @throws PropertyServerException something went wrong with the REST call stack.
     */
    public   GUIDResponse callGUIDPostRESTCall(String    methodName,
                                               String    urlTemplate,
                                               Object    requestBody,
                                               Object... params) throws InvalidParameterException,
                                                                        UserNotAuthorizedException,
                                                                        PropertyServerException
    {
        GUIDResponse restResult = this.callPostRESTCall(methodName,
                                                        GUIDResponse.class,
                                                        urlTemplate,
                                                        requestBody,
                                                        params);

        exceptionHandler.detectAndThrowStandardExceptions(methodName, restResult);

        return restResult;
    }


    /**
     * Issue a GET REST call that returns a list of GUIDs object.
     *
     * @param methodName  name of the method being called.
     * @param urlTemplate template of the URL for the REST API with place-holders for the parameters.
     * @param params      a list of parameters that are slotted into the url template.
     *
     * @return GUIDListResponse
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws UserNotAuthorizedException the user is not authorized to make this request.
     * @throws PropertyServerException something went wrong with the REST call stack.
     */
    public GUIDListResponse callGUIDListGetRESTCall(String    methodName,
                                                    String    urlTemplate,
                                                    Object... params) throws InvalidParameterException,
                                                                             UserNotAuthorizedException,
                                                                             PropertyServerException
    {
        GUIDListResponse restResult = this.callGetRESTCall(methodName, GUIDListResponse.class, urlTemplate, params);

        exceptionHandler.detectAndThrowStandardExceptions(methodName, restResult);

        return restResult;
    }


    /**
     * Issue a POST REST call that returns a list of GUIDs object.
     *
     * @param methodName  name of the method being called.
     * @param urlTemplate template of the URL for the REST API with place-holders for the parameters.
     * @param requestBody request body for the request.
     * @param params      a list of parameters that are slotted into the url template.
     *
     * @return GUIDListResponse
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws UserNotAuthorizedException the user is not authorized to make this request.
     * @throws PropertyServerException something went wrong with the REST call stack.
     */
    public GUIDListResponse callGUIDListPostRESTCall(String     methodName,
                                                     String     urlTemplate,
                                                     Object     requestBody,
                                                     Object...  params) throws InvalidParameterException,
                                                                               UserNotAuthorizedException,
                                                                               PropertyServerException
    {
        GUIDListResponse restResult = this.callPostRESTCall(methodName, GUIDListResponse.class, urlTemplate, requestBody, params);

        exceptionHandler.detectAndThrowStandardExceptions(methodName, restResult);

        return restResult;
    }


    /**
     * Issue a POST REST call that returns a VoidResponse object.  This is typically a create
     *
     * @param methodName  name of the method being called.
     * @param urlTemplate  template of the URL for the REST API with place-holders for the parameters.
     * @param params  a list of parameters that are slotted into the url template.
     *
     * @return VoidResponse
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws UserNotAuthorizedException the user is not authorized to make this request.
     * @throws PropertyServerException something went wrong with the REST call stack.
     */
    public VoidResponse callVoidGetRESTCall(String    methodName,
                                            String    urlTemplate,
                                            Object... params) throws InvalidParameterException,
                                                                      UserNotAuthorizedException,
                                                                      PropertyServerException
    {
        VoidResponse restResult =  this.callGetRESTCall(methodName,
                                                         VoidResponse.class,
                                                         urlTemplate,
                                                         params);

        exceptionHandler.detectAndThrowStandardExceptions(methodName, restResult);

        return restResult;
    }


    /**
     * Issue a POST REST call that returns a BooleanResponse object.
     *
     * @param methodName  name of the method being called.
     * @param urlTemplate  template of the URL for the REST API with place-holders for the parameters.
     * @param params  a list of parameters that are slotted into the url template.
     *
     * @return response object
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws UserNotAuthorizedException the user is not authorized to make this request.
     * @throws PropertyServerException something went wrong with the REST call stack.
     */
    public BooleanResponse callBooleanGetRESTCall(String    methodName,
                                                  String    urlTemplate,
                                                  Object... params) throws InvalidParameterException,
                                                                           UserNotAuthorizedException,
                                                                           PropertyServerException
    {
        BooleanResponse restResult =  this.callGetRESTCall(methodName,
                                                           BooleanResponse.class,
                                                           urlTemplate,
                                                           params);

        exceptionHandler.detectAndThrowStandardExceptions(methodName, restResult);

        return restResult;
    }


    /**
     * Issue a POST REST call that returns a VoidResponse object.  This is typically a create
     *
     * @param methodName  name of the method being called.
     * @param urlTemplate  template of the URL for the REST API with place-holders for the parameters.
     * @param requestBody request body for the request.
     * @param params  a list of parameters that are slotted into the url template.
     *
     * @return VoidResponse
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws UserNotAuthorizedException the user is not authorized to make this request.
     * @throws PropertyServerException something went wrong with the REST call stack.
     */
    public VoidResponse callVoidPostRESTCall(String    methodName,
                                             String    urlTemplate,
                                             Object    requestBody,
                                             Object... params) throws InvalidParameterException,
                                                                      UserNotAuthorizedException,
                                                                      PropertyServerException
    {
        VoidResponse restResult =  this.callPostRESTCall(methodName,
                                                         VoidResponse.class,
                                                         urlTemplate,
                                                         requestBody,
                                                         params);

        exceptionHandler.detectAndThrowStandardExceptions(methodName, restResult);

        return restResult;
    }


    /**
     * Issue a GET REST call that returns a CountResponse object.
     *
     * @param methodName  name of the method being called.
     * @param urlTemplate template of the URL for the REST API with place-holders for the parameters.
     * @param params      a list of parameters that are slotted into the url template.
     *
     * @return CountResponse
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws UserNotAuthorizedException the user is not authorized to make this request.
     * @throws PropertyServerException something went wrong with the REST call stack.
     */
    public CountResponse callCountGetRESTCall(String    methodName,
                                              String    urlTemplate,
                                              Object... params) throws InvalidParameterException,
                                                                       UserNotAuthorizedException,
                                                                       PropertyServerException
    {
        CountResponse restResult =  this.callGetRESTCall(methodName, CountResponse.class, urlTemplate, params);

        exceptionHandler.detectAndThrowStandardExceptions(methodName, restResult);

        return restResult;
    }




    /**
     * Issue a GET REST call that returns a ConnectionResponse object.
     *
     * @param methodName  name of the method being called.
     * @param urlTemplate template of the URL for the REST API call with place-holders for the parameters.
     * @param params      a list of parameters that are slotted into the url template.
     *
     * @return OCFConnectionResponse
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws UserNotAuthorizedException the user is not authorized to make this request.
     * @throws PropertyServerException the repository is not available or not working properly.
     */
    public ConnectionResponse callConnectionGetRESTCall(String    methodName,
                                                        String    urlTemplate,
                                                        Object... params) throws InvalidParameterException,
                                                                                 UserNotAuthorizedException,
                                                                                 PropertyServerException
    {
        ConnectionResponse restResult = this.callGetRESTCall(methodName, ConnectionResponse.class, urlTemplate, params);

        exceptionHandler.detectAndThrowStandardExceptions(methodName, restResult);

        return restResult;
    }


    /**
     * Issue a POST REST call that returns a ConnectionsResponse object.
     *
     * @param methodName  name of the method being called.
     * @param urlTemplate template of the URL for the REST API call with place-holders for the parameters.
     * @param params      a list of parameters that are slotted into the url template.
     *
     * @return ConnectionsResponse
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws UserNotAuthorizedException the user is not authorized to make this request.
     * @throws PropertyServerException the repository is not available or not working properly.
     */
    public ConnectionsResponse callConnectionsGetRESTCall(String    methodName,
                                                          String    urlTemplate,
                                                          Object... params) throws InvalidParameterException,
                                                                                   UserNotAuthorizedException,
                                                                                   PropertyServerException
    {
        ConnectionsResponse restResult = this.callGetRESTCall(methodName, ConnectionsResponse.class, urlTemplate, params);

        exceptionHandler.detectAndThrowStandardExceptions(methodName, restResult);

        return restResult;
    }



    /**
     * Issue a POST REST call that returns a ConnectionsResponse object.
     *
     * @param methodName  name of the method being called.
     * @param urlTemplate template of the URL for the REST API call with place-holders for the parameters.
     * @param requestBody properties describing the valid value definition/set
     * @param params      a list of parameters that are slotted into the url template.
     *
     * @return ConnectionsResponse
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws UserNotAuthorizedException the user is not authorized to make this request.
     * @throws PropertyServerException the repository is not available or not working properly.
     */
    public ConnectionsResponse callConnectionsPostRESTCall(String    methodName,
                                                           String    urlTemplate,
                                                           Object    requestBody,
                                                           Object... params) throws InvalidParameterException,
                                                                                    UserNotAuthorizedException,
                                                                                    PropertyServerException
    {
        ConnectionsResponse restResult = this.callPostRESTCall(methodName, ConnectionsResponse.class, urlTemplate, requestBody, params);

        exceptionHandler.detectAndThrowStandardExceptions(methodName, restResult);

        return restResult;
    }


    /**
     * Issue a GET REST call that returns a ConnectorReportResponse object.
     *
     * @param methodName  name of the method being called.
     * @param urlTemplate template of the URL for the REST API call with place-holders for the parameters.
     * @param params      a list of parameters that are slotted into the url template.
     *
     * @return ConnectorReportResponse
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws UserNotAuthorizedException the user is not authorized to make this request.
     * @throws PropertyServerException the repository is not available or not working properly.
     */
    public ConnectorTypeResponse callConnectorTypeGetRESTCall(String    methodName,
                                                              String    urlTemplate,
                                                              Object... params) throws InvalidParameterException,
                                                                                       UserNotAuthorizedException,
                                                                                       PropertyServerException
    {
        ConnectorTypeResponse restResult = this.callGetRESTCall(methodName, ConnectorTypeResponse.class, urlTemplate, params);

        exceptionHandler.detectAndThrowStandardExceptions(methodName, restResult);

        return restResult;
    }


    /**
     * Issue a POST REST call that returns a ConnectorTypesResponse object.
     *
     * @param methodName  name of the method being called.
     * @param urlTemplate template of the URL for the REST API call with place-holders for the parameters.
     * @param requestBody properties describing the valid value definition/set
     * @param params      a list of parameters that are slotted into the url template.
     *
     * @return ConnectorTypesResponse
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws UserNotAuthorizedException the user is not authorized to make this request.
     * @throws PropertyServerException the repository is not available or not working properly.
     */
    public ConnectorTypesResponse callConnectorTypesPostRESTCall(String    methodName,
                                                                 String    urlTemplate,
                                                                 Object    requestBody,
                                                                 Object... params) throws InvalidParameterException,
                                                                                          UserNotAuthorizedException,
                                                                                          PropertyServerException
    {
        ConnectorTypesResponse restResult = this.callPostRESTCall(methodName, ConnectorTypesResponse.class, urlTemplate, requestBody, params);

        exceptionHandler.detectAndThrowStandardExceptions(methodName, restResult);

        return restResult;
    }


    /**
     * Issue a GET REST call that returns a EndpointResponse object.
     *
     * @param methodName  name of the method being called.
     * @param urlTemplate template of the URL for the REST API call with place-holders for the parameters.
     * @param params      a list of parameters that are slotted into the url template.
     *
     * @return EndpointResponse
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws UserNotAuthorizedException the user is not authorized to make this request.
     * @throws PropertyServerException the repository is not available or not working properly.
     */
    public EndpointResponse callEndpointGetRESTCall(String    methodName,
                                                    String    urlTemplate,
                                                    Object... params) throws InvalidParameterException,
                                                                             UserNotAuthorizedException,
                                                                             PropertyServerException
    {
        EndpointResponse restResult = this.callGetRESTCall(methodName, EndpointResponse.class, urlTemplate, params);

        exceptionHandler.detectAndThrowStandardExceptions(methodName, restResult);

        return restResult;
    }


    /**
     * Issue a GET REST call that returns a EndpointsResponse object.
     *
     * @param methodName  name of the method being called.
     * @param urlTemplate template of the URL for the REST API call with place-holders for the parameters.
     * @param params      a list of parameters that are slotted into the url template.
     *
     * @return EndpointsResponse
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws UserNotAuthorizedException the user is not authorized to make this request.
     * @throws PropertyServerException the repository is not available or not working properly.
     */
    public EndpointsResponse callEndpointsGetRESTCall(String    methodName,
                                                     String    urlTemplate,
                                                     Object... params) throws InvalidParameterException,
                                                                             UserNotAuthorizedException,
                                                                             PropertyServerException
    {
        EndpointsResponse restResult = this.callGetRESTCall(methodName, EndpointsResponse.class, urlTemplate, params);

        exceptionHandler.detectAndThrowStandardExceptions(methodName, restResult);

        return restResult;
    }


    /**
     * Issue a POST REST call that returns a EndpointsResponse object.
     *
     * @param methodName  name of the method being called.
     * @param urlTemplate template of the URL for the REST API call with place-holders for the parameters.
     * @param requestBody properties describing the valid value definition/set
     * @param params      a list of parameters that are slotted into the url template.
     *
     * @return EndpointsResponse
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws UserNotAuthorizedException the user is not authorized to make this request.
     * @throws PropertyServerException the repository is not available or not working properly.
     */
    public EndpointsResponse callEndpointsPostRESTCall(String    methodName,
                                                       String    urlTemplate,
                                                       Object    requestBody,
                                                       Object... params) throws InvalidParameterException,
                                                                                UserNotAuthorizedException,
                                                                                PropertyServerException
    {
        EndpointsResponse restResult = this.callPostRESTCall(methodName, EndpointsResponse.class, urlTemplate, requestBody, params);

        exceptionHandler.detectAndThrowStandardExceptions(methodName, restResult);

        return restResult;
    }


    /**
     * Issue a GET REST call that returns a FileSystemResponse object.
     *
     * @param methodName  name of the method being called.
     * @param urlTemplate template of the URL for the REST API call with place-holders for the parameters.
     * @param params      a list of parameters that are slotted into the url template.
     *
     * @return FileSystemResponse
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws UserNotAuthorizedException the user is not authorized to make this request.
     * @throws PropertyServerException the repository is not available or not working properly.
     */
    public FileSystemResponse callFileSystemGetRESTCall(String    methodName,
                                                        String    urlTemplate,
                                                        Object... params) throws InvalidParameterException,
                                                                                 UserNotAuthorizedException,
                                                                                 PropertyServerException
    {
        FileSystemResponse restResult = this.callGetRESTCall(methodName, FileSystemResponse.class, urlTemplate, params);

        exceptionHandler.detectAndThrowStandardExceptions(methodName, restResult);

        return restResult;
    }


    /**
     * Issue a GET REST call that returns a FolderResponse object.
     *
     * @param methodName  name of the method being called.
     * @param urlTemplate template of the URL for the REST API call with place-holders for the parameters.
     * @param params      a list of parameters that are slotted into the url template.
     *
     * @return FileFolderResponse
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws UserNotAuthorizedException the user is not authorized to make this request.
     * @throws PropertyServerException the repository is not available or not working properly.
     */
    public FileFolderResponse callFolderGetRESTCall(String    methodName,
                                                    String    urlTemplate,
                                                    Object... params) throws InvalidParameterException,
                                                                             UserNotAuthorizedException,
                                                                             PropertyServerException
    {
        FileFolderResponse restResult = this.callGetRESTCall(methodName, FileFolderResponse.class, urlTemplate, params);

        exceptionHandler.detectAndThrowStandardExceptions(methodName, restResult);

        return restResult;
    }



    /**
     * Issue a GET REST call that returns a ProjectElement in a response object.
     *
     * @param methodName  name of the method being called.
     * @param urlTemplate REST API call URL template with place-holders for the parameters.
     * @param params      a list of parameters that are slotted into the url template.
     * @return response object
     * @throws InvalidParameterException  one of the parameters is invalid.
     * @throws UserNotAuthorizedException the user is not authorized to make this request.
     * @throws PropertyServerException    something went wrong with the REST call stack.
     */
    public ProjectResponse callProjectGetRESTCall(String methodName,
                                                  String urlTemplate,
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
     * @param urlTemplate REST API call URL template with place-holders for the parameters
     * @param params      a list of parameters that are slotted into the url template
     * @return response object
     * @throws InvalidParameterException  one of the parameters is invalid.
     * @throws UserNotAuthorizedException the user is not authorized to make this request.
     * @throws PropertyServerException    something went wrong with the REST call stack.
     */
    public ProjectsResponse callProjectsGetRESTCall(String methodName,
                                                    String urlTemplate,
                                                    Object... params) throws InvalidParameterException,
                                                                             UserNotAuthorizedException,
                                                                             PropertyServerException
    {
        ProjectsResponse restResult = this.callGetRESTCall(methodName, ProjectsResponse.class, urlTemplate, params);

        exceptionHandler.detectAndThrowStandardExceptions(methodName, restResult);

        return restResult;
    }


    /**
     * Issue a GET REST call that returns a list of ProjectElements in a response object.
     *
     * @param methodName  name of the method being called
     * @param urlTemplate REST API call URL template with place-holders for the parameters
     * @param requestBody request body for the request
     * @param params      a list of parameters that are slotted into the url template
     * @return response object
     * @throws InvalidParameterException  one of the parameters is invalid.
     * @throws UserNotAuthorizedException the user is not authorized to make this request.
     * @throws PropertyServerException    something went wrong with the REST call stack.
     */
    public ProjectsResponse callProjectsPostRESTCall(String methodName,
                                                     String urlTemplate,
                                                     Object requestBody,
                                                     Object... params) throws InvalidParameterException,
                                                                              UserNotAuthorizedException,
                                                                              PropertyServerException
    {
        ProjectsResponse restResult = this.callPostRESTCall(methodName, ProjectsResponse.class, urlTemplate, requestBody, params);

        exceptionHandler.detectAndThrowStandardExceptions(methodName, restResult);

        return restResult;
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
     * Issue a GET REST call that returns a CollectionElement in a response object.
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
    public CollectionResponse callCollectionGetRESTCall(String    methodName,
                                                        String    urlTemplate,
                                                        Object... params) throws InvalidParameterException,
                                                                                 UserNotAuthorizedException,
                                                                                 PropertyServerException
    {
        CollectionResponse restResult = this.callGetRESTCall(methodName, CollectionResponse.class, urlTemplate, params);

        exceptionHandler.detectAndThrowStandardExceptions(methodName, restResult);

        return restResult;
    }


    /**
     * Issue a GET REST call that returns a list of CollectionElements in a response object.
     *
     * @param methodName  name of the method being called
     * @param urlTemplate   REST API call URL template with place-holders for the parameters
     * @param params  a list of parameters that are slotted into the url template
     * @return response object
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws UserNotAuthorizedException the user is not authorized to make this request.
     * @throws PropertyServerException something went wrong with the REST call stack.
     */
    public CollectionsResponse callCollectionListGetRESTCall(String    methodName,
                                                             String    urlTemplate,
                                                             Object... params)  throws InvalidParameterException,
                                                                                       UserNotAuthorizedException,
                                                                                       PropertyServerException
    {
        CollectionsResponse restResult = this.callGetRESTCall(methodName, CollectionsResponse.class, urlTemplate, params);

        exceptionHandler.detectAndThrowStandardExceptions(methodName, restResult);

        return restResult;
    }


    /**
     * Issue a GET REST call that returns a list of CollectionMembers in a response object.
     *
     * @param methodName  name of the method being called
     * @param urlTemplate   REST API call URL template with place-holders for the parameters
     * @param params  a list of parameters that are slotted into the url template
     * @return response object
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws UserNotAuthorizedException the user is not authorized to make this request.
     * @throws PropertyServerException something went wrong with the REST call stack.
     */
    public CollectionMembersResponse callCollectionMemberListGetRESTCall(String    methodName,
                                                                         String    urlTemplate,
                                                                         Object... params)  throws InvalidParameterException,
                                                                                                   UserNotAuthorizedException,
                                                                                                   PropertyServerException
    {
        CollectionMembersResponse restResult = this.callGetRESTCall(methodName, CollectionMembersResponse.class, urlTemplate, params);

        exceptionHandler.detectAndThrowStandardExceptions(methodName, restResult);

        return restResult;
    }


    /**
     * Issue a GET REST call that returns a CollectionMember in a response object.
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
    public CollectionMemberResponse callCollectionMemberGetRESTCall(String    methodName,
                                                                    String    urlTemplate,
                                                                    Object... params) throws InvalidParameterException,
                                                                                             UserNotAuthorizedException,
                                                                                             PropertyServerException
    {
        CollectionMemberResponse restResult = this.callGetRESTCall(methodName, CollectionMemberResponse.class, urlTemplate, params);

        exceptionHandler.detectAndThrowStandardExceptions(methodName, restResult);

        return restResult;
    }

    /**
     * Issue a GET REST call that returns a list of CollectionElements in a response object.
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
    public CollectionsResponse callCollectionListPostRESTCall(String    methodName,
                                                              String    urlTemplate,
                                                              Object    requestBody,
                                                              Object... params)  throws InvalidParameterException,
                                                                                        UserNotAuthorizedException,
                                                                                        PropertyServerException
    {
        CollectionsResponse restResult = this.callPostRESTCall(methodName, CollectionsResponse.class, urlTemplate, requestBody, params);

        exceptionHandler.detectAndThrowStandardExceptions(methodName, restResult);

        return restResult;
    }

    /**
     * Issue a GET REST call that returns a DatabaseColumnResponse object.
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
    public DatabaseColumnResponse callDatabaseColumnGetRESTCall(String    methodName,
                                                                String    urlTemplate,
                                                                Object... params) throws InvalidParameterException,
                                                                                         UserNotAuthorizedException,
                                                                                         PropertyServerException
    {
        DatabaseColumnResponse restResult = this.callGetRESTCall(methodName, DatabaseColumnResponse.class, urlTemplate, params);

        exceptionHandler.detectAndThrowStandardExceptions(methodName, restResult);

        return restResult;
    }


    /**
     * Issue a GET REST call that returns a DatabaseColumnsResponse object.
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
    public DatabaseColumnsResponse callDatabaseColumnsGetRESTCall(String    methodName,
                                                                  String    urlTemplate,
                                                                  Object... params) throws InvalidParameterException,
                                                                                           UserNotAuthorizedException,
                                                                                           PropertyServerException
    {
        DatabaseColumnsResponse restResult = this.callGetRESTCall(methodName, DatabaseColumnsResponse.class, urlTemplate, params);

        exceptionHandler.detectAndThrowStandardExceptions(methodName, restResult);

        return restResult;
    }


    /**
     * Issue a POST REST call that returns a DatabaseColumnsResponse object.
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
    public DatabaseColumnsResponse callDatabaseColumnsPostRESTCall(String    methodName,
                                                                   String    urlTemplate,
                                                                   Object    requestBody,
                                                                   Object... params) throws InvalidParameterException,
                                                                                            UserNotAuthorizedException,
                                                                                            PropertyServerException
    {
        DatabaseColumnsResponse restResult = this.callPostRESTCall(methodName, DatabaseColumnsResponse.class, urlTemplate, requestBody, params);

        exceptionHandler.detectAndThrowStandardExceptions(methodName, restResult);

        return restResult;
    }


    /**
     * Issue a GET REST call that returns a DatabaseResponse object.
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
    public DatabaseResponse callDatabaseGetRESTCall(String    methodName,
                                                    String    urlTemplate,
                                                    Object... params) throws InvalidParameterException,
                                                                             UserNotAuthorizedException,
                                                                             PropertyServerException
    {
        DatabaseResponse restResult = this.callGetRESTCall(methodName, DatabaseResponse.class, urlTemplate, params);

        exceptionHandler.detectAndThrowStandardExceptions(methodName, restResult);

        return restResult;
    }


    /**
     * Issue a GET REST call that returns a DatabasesResponse object.
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
    public DatabasesResponse callDatabasesPostRESTCall(String    methodName,
                                                       String    urlTemplate,
                                                       Object    requestBody,
                                                       Object... params) throws InvalidParameterException,
                                                                                UserNotAuthorizedException,
                                                                                PropertyServerException
    {
        DatabasesResponse restResult = this.callPostRESTCall(methodName, DatabasesResponse.class, urlTemplate, requestBody, params);

        exceptionHandler.detectAndThrowStandardExceptions(methodName, restResult);

        return restResult;
    }


    /**
     * Issue a GET REST call that returns a DatabaseSchemaResponse object.
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
    public DatabaseSchemaResponse callDatabaseSchemaGetRESTCall(String    methodName,
                                                                String    urlTemplate,
                                                                Object... params) throws InvalidParameterException,
                                                                                         UserNotAuthorizedException,
                                                                                         PropertyServerException
    {
        DatabaseSchemaResponse restResult = this.callGetRESTCall(methodName, DatabaseSchemaResponse.class, urlTemplate, params);

        exceptionHandler.detectAndThrowStandardExceptions(methodName, restResult);

        return restResult;
    }


    /**
     * Issue a GET REST call that returns a DatabaseSchemasResponse object.
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
    public DatabaseSchemasResponse callDatabaseSchemasGetRESTCall(String    methodName,
                                                                  String    urlTemplate,
                                                                  Object... params) throws InvalidParameterException,
                                                                                           UserNotAuthorizedException,
                                                                                           PropertyServerException
    {
        DatabaseSchemasResponse restResult = this.callGetRESTCall(methodName, DatabaseSchemasResponse.class, urlTemplate, params);

        exceptionHandler.detectAndThrowStandardExceptions(methodName, restResult);

        return restResult;
    }


    /**
     * Issue a POST REST call that returns a DatabaseSchemasResponse object.
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
    public DatabaseSchemasResponse callDatabaseSchemasPostRESTCall(String    methodName,
                                                                   String    urlTemplate,
                                                                   Object    requestBody,
                                                                   Object... params) throws InvalidParameterException,
                                                                                            UserNotAuthorizedException,
                                                                                            PropertyServerException
    {
        DatabaseSchemasResponse restResult = this.callPostRESTCall(methodName, DatabaseSchemasResponse.class, urlTemplate, requestBody, params);

        exceptionHandler.detectAndThrowStandardExceptions(methodName, restResult);

        return restResult;
    }


    /**
     * Issue a GET REST call that returns a DatabaseTableResponse object.
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
    public DatabaseTableResponse callDatabaseTableGetRESTCall(String    methodName,
                                                              String    urlTemplate,
                                                              Object... params) throws InvalidParameterException,
                                                                                       UserNotAuthorizedException,
                                                                                       PropertyServerException
    {
        DatabaseTableResponse restResult = this.callGetRESTCall(methodName, DatabaseTableResponse.class, urlTemplate, params);

        exceptionHandler.detectAndThrowStandardExceptions(methodName, restResult);

        return restResult;
    }


    /**
     * Issue a GET REST call that returns a DatabaseTablesResponse object.
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
    public DatabaseTablesResponse callDatabaseTablesGetRESTCall(String    methodName,
                                                                String    urlTemplate,
                                                                Object... params) throws InvalidParameterException,
                                                                                         UserNotAuthorizedException,
                                                                                         PropertyServerException
    {
        DatabaseTablesResponse restResult = this.callGetRESTCall(methodName, DatabaseTablesResponse.class, urlTemplate, params);

        exceptionHandler.detectAndThrowStandardExceptions(methodName, restResult);

        return restResult;
    }


    /**
     * Issue a POST REST call that returns a DatabaseTablesResponse object.
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
    public DatabaseTablesResponse callDatabaseTablesPostRESTCall(String    methodName,
                                                                 String    urlTemplate,
                                                                 Object    requestBody,
                                                                 Object... params) throws InvalidParameterException,
                                                                                          UserNotAuthorizedException,
                                                                                          PropertyServerException
    {
        DatabaseTablesResponse restResult = this.callPostRESTCall(methodName, DatabaseTablesResponse.class, urlTemplate, requestBody, params);

        exceptionHandler.detectAndThrowStandardExceptions(methodName, restResult);

        return restResult;
    }


    /**
     * Issue a GET REST call that returns a DatabaseViewResponse object.
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
    public DatabaseViewResponse callDatabaseViewGetRESTCall(String    methodName,
                                                            String    urlTemplate,
                                                            Object... params) throws InvalidParameterException,
                                                                                     UserNotAuthorizedException,
                                                                                     PropertyServerException
    {
        DatabaseViewResponse restResult = this.callGetRESTCall(methodName, DatabaseViewResponse.class, urlTemplate, params);

        exceptionHandler.detectAndThrowStandardExceptions(methodName, restResult);

        return restResult;
    }


    /**
     * Issue a GET REST call that returns a DatabaseViewsResponse object.
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
    public DatabaseViewsResponse callDatabaseViewsGetRESTCall(String    methodName,
                                                              String    urlTemplate,
                                                              Object... params) throws InvalidParameterException,
                                                                                       UserNotAuthorizedException,
                                                                                       PropertyServerException
    {
        DatabaseViewsResponse restResult = this.callGetRESTCall(methodName, DatabaseViewsResponse.class, urlTemplate, params);

        exceptionHandler.detectAndThrowStandardExceptions(methodName, restResult);

        return restResult;
    }


    /**
     * Issue a POST REST call that returns a DatabaseViewsResponse object.
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
    public DatabaseViewsResponse callDatabaseViewsPostRESTCall(String    methodName,
                                                               String    urlTemplate,
                                                               Object    requestBody,
                                                               Object... params) throws InvalidParameterException,
                                                                                        UserNotAuthorizedException,
                                                                                        PropertyServerException
    {
        DatabaseViewsResponse restResult = this.callPostRESTCall(methodName, DatabaseViewsResponse.class, urlTemplate, requestBody, params);

        exceptionHandler.detectAndThrowStandardExceptions(methodName, restResult);

        return restResult;
    }


    /**
     * Issue a GET REST call that returns a DataContainerResponse object.
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
    public DataContainerResponse callDataContainerGetRESTCall(String    methodName,
                                                              String    urlTemplate,
                                                              Object... params) throws InvalidParameterException,
                                                                                       UserNotAuthorizedException,
                                                                                       PropertyServerException
    {
        DataContainerResponse restResult = this.callGetRESTCall(methodName,DataContainerResponse.class, urlTemplate, params);

        exceptionHandler.detectAndThrowStandardExceptions(methodName, restResult);

        return restResult;
    }


    /**
     * Issue a GET REST call that returns a DataContainersResponse object.
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
    public DataContainersResponse callDataContainersGetRESTCall(String    methodName,
                                                                String    urlTemplate,
                                                                Object... params) throws InvalidParameterException,
                                                                                         UserNotAuthorizedException,
                                                                                         PropertyServerException
    {
        DataContainersResponse restResult = this.callGetRESTCall(methodName, DataContainersResponse.class, urlTemplate, params);

        exceptionHandler.detectAndThrowStandardExceptions(methodName, restResult);

        return restResult;
    }



    /**
     * Issue a POST REST call that returns a DataContainersResponse object.
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
    public DataContainersResponse callDataContainersPostRESTCall(String    methodName,
                                                                 String    urlTemplate,
                                                                 Object    requestBody,
                                                                 Object... params) throws InvalidParameterException,
                                                                                          UserNotAuthorizedException,
                                                                                          PropertyServerException
    {
        DataContainersResponse restResult = this.callPostRESTCall(methodName, DataContainersResponse.class, urlTemplate, requestBody, params);

        exceptionHandler.detectAndThrowStandardExceptions(methodName, restResult);

        return restResult;
    }


    /**
     * Issue a GET REST call that returns a DataFileResponse object.
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
    public DataFileResponse callDataFileGetRESTCall(String    methodName,
                                                    String    urlTemplate,
                                                    Object... params) throws InvalidParameterException,
                                                                             UserNotAuthorizedException,
                                                                             PropertyServerException
    {
        DataFileResponse restResult = this.callGetRESTCall(methodName, DataFileResponse.class, urlTemplate, params);

        exceptionHandler.detectAndThrowStandardExceptions(methodName, restResult);

        return restResult;
    }


    /**
     * Issue a POST REST call that returns a DataFileResponse object.
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
    public DataFileResponse callDataFilePostRESTCall(String    methodName,
                                                     String    urlTemplate,
                                                     Object    requestBody,
                                                     Object... params) throws InvalidParameterException,
                                                                              UserNotAuthorizedException,
                                                                              PropertyServerException
    {
        DataFileResponse restResult = this.callPostRESTCall(methodName, DataFileResponse.class, urlTemplate, requestBody, params);

        exceptionHandler.detectAndThrowStandardExceptions(methodName, restResult);

        return restResult;
    }


    /**
     * Issue a GET REST call that returns a DataFilesResponse object.
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
    public DataFilesResponse callDataFilesGetRESTCall(String    methodName,
                                                      String    urlTemplate,
                                                      Object... params) throws InvalidParameterException,
                                                                               UserNotAuthorizedException,
                                                                               PropertyServerException
    {
        DataFilesResponse restResult = this.callGetRESTCall(methodName, DataFilesResponse.class, urlTemplate, params);

        exceptionHandler.detectAndThrowStandardExceptions(methodName, restResult);

        return restResult;
    }


    /**
     * Issue a POST REST call that returns a DataFilesResponse object.
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
    public DataFilesResponse callDataFilesPostRESTCall(String    methodName,
                                                       String    urlTemplate,
                                                       Object    requestBody,
                                                       Object... params) throws InvalidParameterException,
                                                                                UserNotAuthorizedException,
                                                                                PropertyServerException
    {
        DataFilesResponse restResult = this.callPostRESTCall(methodName, DataFilesResponse.class, urlTemplate, requestBody, params);

        exceptionHandler.detectAndThrowStandardExceptions(methodName, restResult);

        return restResult;
    }


    /**
     * Issue a GET REST call that returns a FileFolderResponse object.
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
    public FileFolderResponse callFileFolderGetRESTCall(String    methodName,
                                                        String    urlTemplate,
                                                        Object... params) throws InvalidParameterException,
                                                                                 UserNotAuthorizedException,
                                                                                 PropertyServerException
    {
        FileFolderResponse restResult = this.callGetRESTCall(methodName, FileFolderResponse.class, urlTemplate, params);

        exceptionHandler.detectAndThrowStandardExceptions(methodName, restResult);

        return restResult;
    }


    /**
     * Issue a POST REST call that returns a FileFolderResponse object.
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
    public FileFolderResponse callFileFolderPostRESTCall(String    methodName,
                                                         String    urlTemplate,
                                                         Object    requestBody,
                                                         Object... params) throws InvalidParameterException,
                                                                                  UserNotAuthorizedException,
                                                                                  PropertyServerException
    {
        FileFolderResponse restResult = this.callPostRESTCall(methodName, FileFolderResponse.class, urlTemplate, requestBody, params);

        exceptionHandler.detectAndThrowStandardExceptions(methodName, restResult);

        return restResult;
    }


    /**
     * Issue a GET REST call that returns a FileFoldersResponse object.
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
    public FileFoldersResponse callFileFoldersGetRESTCall(String    methodName,
                                                          String    urlTemplate,
                                                          Object... params) throws InvalidParameterException,
                                                                                   UserNotAuthorizedException,
                                                                                   PropertyServerException
    {
        FileFoldersResponse restResult = this.callGetRESTCall(methodName, FileFoldersResponse.class, urlTemplate, params);

        exceptionHandler.detectAndThrowStandardExceptions(methodName, restResult);

        return restResult;
    }


    /**
     * Issue a GET REST call that returns a FormResponse object.
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
    public FormResponse callFormGetRESTCall(String    methodName,
                                            String    urlTemplate,
                                            Object... params) throws InvalidParameterException,
                                                                     UserNotAuthorizedException,
                                                                     PropertyServerException
    {
        FormResponse restResult = this.callGetRESTCall(methodName, FormResponse.class, urlTemplate, params);

        exceptionHandler.detectAndThrowStandardExceptions(methodName, restResult);

        return restResult;
    }


    /**
     * Issue a GET REST call that returns a FormsResponse object.
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
    public FormsResponse callFormsGetRESTCall(String    methodName,
                                              String    urlTemplate,
                                              Object... params) throws InvalidParameterException,
                                                                       UserNotAuthorizedException,
                                                                       PropertyServerException
    {
        FormsResponse restResult = this.callGetRESTCall(methodName, FormsResponse.class, urlTemplate, params);

        exceptionHandler.detectAndThrowStandardExceptions(methodName, restResult);

        return restResult;
    }


    /**
     * Issue a POST REST call that returns a FormsResponse object.
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
    public FormsResponse callFormsPostRESTCall(String    methodName,
                                               String    urlTemplate,
                                               Object    requestBody,
                                               Object... params) throws InvalidParameterException,
                                                                        UserNotAuthorizedException,
                                                                        PropertyServerException
    {
        FormsResponse restResult = this.callPostRESTCall(methodName, FormsResponse.class, urlTemplate, requestBody, params);

        exceptionHandler.detectAndThrowStandardExceptions(methodName, restResult);

        return restResult;
    }


    /**
     * Issue a GET REST call that returns a QueryResponse object.
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
    public QueryResponse callQueryGetRESTCall(String    methodName,
                                              String    urlTemplate,
                                              Object... params) throws InvalidParameterException,
                                                                       UserNotAuthorizedException,
                                                                       PropertyServerException
    {
        QueryResponse restResult = this.callGetRESTCall(methodName, QueryResponse.class, urlTemplate, params);

        exceptionHandler.detectAndThrowStandardExceptions(methodName, restResult);

        return restResult;
    }


    /**
     * Issue a GET REST call that returns a QueriesResponse object.
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
    public QueriesResponse callQueriesGetRESTCall(String    methodName,
                                                  String    urlTemplate,
                                                  Object... params) throws InvalidParameterException,
                                                                           UserNotAuthorizedException,
                                                                           PropertyServerException
    {
        QueriesResponse restResult = this.callGetRESTCall(methodName, QueriesResponse.class, urlTemplate, params);

        exceptionHandler.detectAndThrowStandardExceptions(methodName, restResult);

        return restResult;
    }



    /**
     * Issue a POST REST call that returns a QueriesResponse object.
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
    public QueriesResponse callQueriesPostRESTCall(String    methodName,
                                                   String    urlTemplate,
                                                   Object    requestBody,
                                                   Object... params) throws InvalidParameterException,
                                                                            UserNotAuthorizedException,
                                                                            PropertyServerException
    {
        QueriesResponse restResult = this.callPostRESTCall(methodName, QueriesResponse.class, urlTemplate, requestBody, params);

        exceptionHandler.detectAndThrowStandardExceptions(methodName, restResult);

        return restResult;
    }



    /**
     * Issue a GET REST call that returns a ReportResponse object.
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
    public ReportResponse callReportGetRESTCall(String    methodName,
                                                String    urlTemplate,
                                                Object... params) throws InvalidParameterException,
                                                                         UserNotAuthorizedException,
                                                                         PropertyServerException
    {
        ReportResponse restResult = this.callGetRESTCall(methodName, ReportResponse.class, urlTemplate, params);

        exceptionHandler.detectAndThrowStandardExceptions(methodName, restResult);

        return restResult;
    }


    /**
     * Issue a GET REST call that returns a ReportsResponse object.
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
    public ReportsResponse callReportsGetRESTCall(String    methodName,
                                                  String    urlTemplate,
                                                  Object... params) throws InvalidParameterException,
                                                                           UserNotAuthorizedException,
                                                                           PropertyServerException
    {
        ReportsResponse restResult = this.callGetRESTCall(methodName, ReportsResponse.class, urlTemplate, params);

        exceptionHandler.detectAndThrowStandardExceptions(methodName, restResult);

        return restResult;
    }



    /**
     * Issue a POST REST call that returns a ReportsResponse object.
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
    public ReportsResponse callReportsPostRESTCall(String    methodName,
                                                   String    urlTemplate,
                                                   Object    requestBody,
                                                   Object... params) throws InvalidParameterException,
                                                                            UserNotAuthorizedException,
                                                                            PropertyServerException
    {
        ReportsResponse restResult = this.callPostRESTCall(methodName, ReportsResponse.class, urlTemplate, requestBody, params);

        exceptionHandler.detectAndThrowStandardExceptions(methodName, restResult);

        return restResult;
    }


    /**
     * Issue a GET REST call that returns a TopicResponse object.
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
    public TopicResponse callTopicGetRESTCall(String    methodName,
                                              String    urlTemplate,
                                              Object... params) throws InvalidParameterException,
                                                                       UserNotAuthorizedException,
                                                                       PropertyServerException
    {
        TopicResponse restResult = this.callGetRESTCall(methodName, TopicResponse.class, urlTemplate, params);

        exceptionHandler.detectAndThrowStandardExceptions(methodName, restResult);

        return restResult;
    }


    /**
     * Issue a GET REST call that returns a TopicsResponse object.
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
    public TopicsResponse callTopicsGetRESTCall(String    methodName,
                                                String    urlTemplate,
                                                Object... params) throws InvalidParameterException,
                                                                         UserNotAuthorizedException,
                                                                         PropertyServerException
    {
        TopicsResponse restResult = this.callGetRESTCall(methodName, TopicsResponse.class, urlTemplate, params);

        exceptionHandler.detectAndThrowStandardExceptions(methodName, restResult);

        return restResult;
    }


    /**
     * Issue a POST REST call that returns a TopicsResponse object.
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
    public TopicsResponse callTopicsPostRESTCall(String    methodName,
                                                 String    urlTemplate,
                                                 Object    requestBody,
                                                 Object... params) throws InvalidParameterException,
                                                                          UserNotAuthorizedException,
                                                                          PropertyServerException
    {
        TopicsResponse restResult = this.callPostRESTCall(methodName, TopicsResponse.class, urlTemplate, requestBody, params);

        exceptionHandler.detectAndThrowStandardExceptions(methodName, restResult);

        return restResult;
    }


    /**
     * Issue a GET REST call that returns a EventTypeResponse object.
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
    public EventTypeResponse callEventTypeGetRESTCall(String    methodName,
                                                      String    urlTemplate,
                                                      Object... params) throws InvalidParameterException,
                                                                               UserNotAuthorizedException,
                                                                               PropertyServerException
    {
        EventTypeResponse restResult = this.callGetRESTCall(methodName, EventTypeResponse.class, urlTemplate, params);

        exceptionHandler.detectAndThrowStandardExceptions(methodName, restResult);

        return restResult;
    }


    /**
     * Issue a GET REST call that returns a EventTypesResponse object.
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
    public EventTypesResponse callEventTypesGetRESTCall(String    methodName,
                                                        String    urlTemplate,
                                                        Object... params) throws InvalidParameterException,
                                                                                 UserNotAuthorizedException,
                                                                                 PropertyServerException
    {
        EventTypesResponse restResult = this.callGetRESTCall(methodName, EventTypesResponse.class, urlTemplate, params);

        exceptionHandler.detectAndThrowStandardExceptions(methodName, restResult);

        return restResult;
    }



    /**
     * Issue a POST REST call that returns a EventTypesResponse object.
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
    public EventTypesResponse callEventTypesPostRESTCall(String    methodName,
                                                         String    urlTemplate,
                                                         Object    requestBody,
                                                         Object... params) throws InvalidParameterException,
                                                                                  UserNotAuthorizedException,
                                                                                  PropertyServerException
    {
        EventTypesResponse restResult = this.callPostRESTCall(methodName, EventTypesResponse.class, urlTemplate, requestBody, params);

        exceptionHandler.detectAndThrowStandardExceptions(methodName, restResult);

        return restResult;
    }


    /**
     * Issue a GET REST call that returns a APIResponse object.
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
    public APIResponse callAPIGetRESTCall(String    methodName,
                                          String    urlTemplate,
                                          Object... params) throws InvalidParameterException,
                                                                   UserNotAuthorizedException,
                                                                   PropertyServerException
    {
        APIResponse restResult = this.callGetRESTCall(methodName, APIResponse.class, urlTemplate, params);

        exceptionHandler.detectAndThrowStandardExceptions(methodName, restResult);

        return restResult;
    }


    /**
     * Issue a GET REST call that returns a APIsResponse object.
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
    public APIsResponse callAPIsGetRESTCall(String    methodName,
                                            String    urlTemplate,
                                            Object... params) throws InvalidParameterException,
                                                                     UserNotAuthorizedException,
                                                                     PropertyServerException
    {
        APIsResponse restResult = this.callGetRESTCall(methodName, APIsResponse.class, urlTemplate, params);

        exceptionHandler.detectAndThrowStandardExceptions(methodName, restResult);

        return restResult;
    }



    /**
     * Issue a POST REST call that returns a APIsResponse object.
     *
     * @param methodName  name of the method being called.
     * @param urlTemplate template of the URL for the REST API with place-holders for the parameters.
     * @param requestBody request body for the request.
     * @param params      a list of parameters that are slotted into the url template.
     *
     * @return response object
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws UserNotAuthorizedException the user is not authorized to make this request.
     * @throws PropertyServerException the repository is not available or not working properly.
     */
    public APIsResponse callAPIsPostRESTCall(String    methodName,
                                             String    urlTemplate,
                                             Object    requestBody,
                                             Object... params) throws InvalidParameterException,
                                                                      UserNotAuthorizedException,
                                                                      PropertyServerException
    {
        APIsResponse restResult = this.callPostRESTCall(methodName, APIsResponse.class, urlTemplate, requestBody, params);

        exceptionHandler.detectAndThrowStandardExceptions(methodName, restResult);

        return restResult;
    }


    /**
     * Issue a GET REST call that returns a APIOperationResponse object.
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
    public APIOperationResponse callAPIOperationGetRESTCall(String    methodName,
                                                            String    urlTemplate,
                                                            Object... params) throws InvalidParameterException,
                                                                                     UserNotAuthorizedException,
                                                                                     PropertyServerException
    {
        APIOperationResponse restResult = this.callGetRESTCall(methodName, APIOperationResponse.class, urlTemplate, params);

        exceptionHandler.detectAndThrowStandardExceptions(methodName, restResult);

        return restResult;
    }


    /**
     * Issue a GET REST call that returns a APIOperationsResponse object.
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
    public APIOperationsResponse callAPIOperationsGetRESTCall(String    methodName,
                                                              String    urlTemplate,
                                                              Object... params) throws InvalidParameterException,
                                                                                       UserNotAuthorizedException,
                                                                                       PropertyServerException
    {
        APIOperationsResponse restResult = this.callGetRESTCall(methodName, APIOperationsResponse.class, urlTemplate, params);

        exceptionHandler.detectAndThrowStandardExceptions(methodName, restResult);

        return restResult;
    }



    /**
     * Issue a POST REST call that returns a APIOperationsResponse object.
     *
     * @param methodName  name of the method being called.
     * @param urlTemplate template of the URL for the REST API with place-holders for the parameters.
     * @param requestBody request body for the request.
     * @param params      a list of parameters that are slotted into the url template.
     *
     * @return response object
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws UserNotAuthorizedException the user is not authorized to make this request.
     * @throws PropertyServerException the repository is not available or not working properly.
     */
    public APIOperationsResponse callAPIOperationsPostRESTCall(String    methodName,
                                                               String    urlTemplate,
                                                               Object    requestBody,
                                                               Object... params) throws InvalidParameterException,
                                                                                        UserNotAuthorizedException,
                                                                                        PropertyServerException
    {
        APIOperationsResponse restResult = this.callPostRESTCall(methodName, APIOperationsResponse.class, urlTemplate, requestBody, params);

        exceptionHandler.detectAndThrowStandardExceptions(methodName, restResult);

        return restResult;
    }


    /**
     * Issue a GET REST call that returns a APIParametersResponse object.
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
    public APIParametersResponse callAPIParametersGetRESTCall(String    methodName,
                                                              String    urlTemplate,
                                                              Object... params) throws InvalidParameterException,
                                                                                       UserNotAuthorizedException,
                                                                                       PropertyServerException
    {
        APIParametersResponse restResult = this.callGetRESTCall(methodName, APIParametersResponse.class, urlTemplate, params);

        exceptionHandler.detectAndThrowStandardExceptions(methodName, restResult);

        return restResult;
    }


    /**
     * Issue a GET REST call that returns a APIParameterListsResponse object.
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
    public APIParameterListsResponse callAPIParameterListsGetRESTCall(String    methodName,
                                                                      String    urlTemplate,
                                                                      Object... params) throws InvalidParameterException,
                                                                                               UserNotAuthorizedException,
                                                                                               PropertyServerException
    {
        APIParameterListsResponse restResult = this.callGetRESTCall(methodName, APIParameterListsResponse.class, urlTemplate, params);

        exceptionHandler.detectAndThrowStandardExceptions(methodName, restResult);

        return restResult;
    }


    /**
     * Issue a GET REST call that returns a APIParameterListsResponse object.
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
    public APIParameterListResponse callAPIParameterListGetRESTCall(String    methodName,
                                                                    String    urlTemplate,
                                                                    Object... params) throws InvalidParameterException,
                                                                                             UserNotAuthorizedException,
                                                                                             PropertyServerException
    {
        APIParameterListResponse restResult = this.callGetRESTCall(methodName, APIParameterListResponse.class, urlTemplate, params);

        exceptionHandler.detectAndThrowStandardExceptions(methodName, restResult);

        return restResult;
    }





    /**
     * Issue a GET REST call that returns a APIParameterListsResponse object.
     *
     * @param methodName  name of the method being called.
     * @param urlTemplate template of the URL for the REST API with place-holders for the parameters.
     * @param requestBody request body for the request.
     * @param params      a list of parameters that are slotted into the url template.
     *
     * @return response object
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws UserNotAuthorizedException the user is not authorized to make this request.
     * @throws PropertyServerException the repository is not available or not working properly.
     */
    public APIParameterListsResponse callAPIParameterListsPostRESTCall(String    methodName,
                                                                       String    urlTemplate,
                                                                       Object    requestBody,
                                                                       Object... params) throws InvalidParameterException,
                                                                                                UserNotAuthorizedException,
                                                                                                PropertyServerException
    {
        APIParameterListsResponse restResult = this.callPostRESTCall(methodName, APIParameterListsResponse.class, urlTemplate, requestBody, params);

        exceptionHandler.detectAndThrowStandardExceptions(methodName, restResult);

        return restResult;
    }


    /**
     * Issue a GET REST call that returns a SchemaAttributeResponse object.
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
    public SchemaAttributeResponse callSchemaAttributeGetRESTCall(String    methodName,
                                                                  String    urlTemplate,
                                                                  Object... params) throws InvalidParameterException,
                                                                                           UserNotAuthorizedException,
                                                                                           PropertyServerException
    {
        SchemaAttributeResponse restResult = this.callGetRESTCall(methodName, SchemaAttributeResponse.class, urlTemplate, params);

        exceptionHandler.detectAndThrowStandardExceptions(methodName, restResult);

        return restResult;
    }


    /**
     * Issue a GET REST call that returns a SchemaAttributesResponse object.
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
    public SchemaAttributesResponse callSchemaAttributesGetRESTCall(String    methodName,
                                                                    String    urlTemplate,
                                                                    Object... params) throws InvalidParameterException,
                                                                                             UserNotAuthorizedException,
                                                                                             PropertyServerException
    {
        SchemaAttributesResponse restResult = this.callGetRESTCall(methodName, SchemaAttributesResponse.class, urlTemplate, params);

        exceptionHandler.detectAndThrowStandardExceptions(methodName, restResult);

        return restResult;
    }


    /**
     * Issue a GET REST call that returns a SchemaTypeResponse object.
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
    public SchemaTypeResponse callSchemaTypeGetRESTCall(String    methodName,
                                                        String    urlTemplate,
                                                        Object... params) throws InvalidParameterException,
                                                                                 UserNotAuthorizedException,
                                                                                 PropertyServerException
    {
        SchemaTypeResponse restResult = this.callGetRESTCall(methodName, SchemaTypeResponse.class, urlTemplate, params);

        exceptionHandler.detectAndThrowStandardExceptions(methodName, restResult);

        return restResult;
    }


    /**
     * Issue a POST REST call that returns a ValidValueSetsResponse object.
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
    public ValidValueSetsResponse callValidValueSetsPostRESTCall(String    methodName,
                                                                 String    urlTemplate,
                                                                 Object    requestBody,
                                                                 Object... params) throws InvalidParameterException,
                                                                                          UserNotAuthorizedException,
                                                                                          PropertyServerException
    {
        ValidValueSetsResponse restResult = this.callGetRESTCall(methodName, ValidValueSetsResponse.class, urlTemplate, requestBody, params);

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
     * @return ValidValueResponse
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws UserNotAuthorizedException the user is not authorized to make this request.
     * @throws PropertyServerException the repository is not available or not working properly.
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
    public ValidValuesResponse callValidValueListGetRESTCall(String    methodName,
                                                             String    urlTemplate,
                                                             Object... params)  throws InvalidParameterException,
                                                                                       UserNotAuthorizedException,
                                                                                       PropertyServerException
    {
        ValidValuesResponse restResult = this.callGetRESTCall(methodName, ValidValuesResponse.class, urlTemplate, params);

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
    public ValidValuesResponse callValidValueListPostRESTCall(String    methodName,
                                                              String    urlTemplate,
                                                              Object    requestBody,
                                                              Object... params)  throws InvalidParameterException,
                                                                                        UserNotAuthorizedException,
                                                                                        PropertyServerException
    {
        ValidValuesResponse restResult = this.callPostRESTCall(methodName, ValidValuesResponse.class, urlTemplate, requestBody, params);

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
    public ValidValuesResponse callValidValuesGetRESTCall(String    methodName,
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
     * @param requestBody additional parameters
     * @param params      a list of parameters that are slotted into the url template.
     *
     * @return ValidValuesResponse
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws UserNotAuthorizedException the user is not authorized to make this request.
     * @throws PropertyServerException the repository is not available or not working properly.
     */
    public ValidValuesResponse callValidValuesPostRESTCall(String    methodName,
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
    public ValidValueAssignmentConsumersResponse callValidValueAssignmentConsumersGetRESTCall(String    methodName,
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
    public ValidValueAssignmentDefinitionsResponse callValidValueAssignmentDefinitionsGetRESTCall(String    methodName,
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

    /**
     * Issue a GET REST call that returns an AssetElementResponse object.
     *
     * @param methodName  name of the method being called.
     * @param urlTemplate template of the URL for the REST API call with place-holders for the parameters.
     * @param params      a list of parameters that are slotted into the url template.
     *
     * @return AssetElementResponse
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws UserNotAuthorizedException the user is not authorized to make this request.
     * @throws PropertyServerException something went wrong with the REST call stack.
     */
    public AssetElementResponse callAssetElementGetRESTCall(String    methodName,
                                                            String    urlTemplate,
                                                            Object... params) throws InvalidParameterException,
                                                                                     UserNotAuthorizedException,
                                                                                     PropertyServerException
    {
        AssetElementResponse restResult = this.callGetRESTCall(methodName, AssetElementResponse.class, urlTemplate, params);

        exceptionHandler.detectAndThrowStandardExceptions(methodName, restResult);

        return restResult;
    }


    /**
     * Issue a GET REST call that returns an AssetElementsResponse object.
     *
     * @param methodName  name of the method being called.
     * @param urlTemplate template of the URL for the REST API call with place-holders for the parameters.
     * @param params      a list of parameters that are slotted into the url template.
     *
     * @return AssetElementsResponse
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws UserNotAuthorizedException the user is not authorized to make this request.
     * @throws PropertyServerException something went wrong with the REST call stack.
     */
    public AssetElementsResponse callAssetElementsGetRESTCall(String    methodName,
                                                              String    urlTemplate,
                                                              Object... params) throws InvalidParameterException,
                                                                                       UserNotAuthorizedException,
                                                                                       PropertyServerException
    {
        AssetElementsResponse restResult = this.callGetRESTCall(methodName, AssetElementsResponse.class, urlTemplate, params);

        exceptionHandler.detectAndThrowStandardExceptions(methodName, restResult);

        return restResult;
    }


    /**
     * Issue a POST REST call that returns an AssetElementsResponse object.
     *
     * @param methodName  name of the method being called.
     * @param requestBody request body for the REST call - contains most of the parameters
     * @param urlTemplate template of the URL for the REST API call with place-holders for the parameters.
     * @param params      a list of parameters that are slotted into the url template.
     *
     * @return AssetElementsResponse
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws UserNotAuthorizedException the user is not authorized to make this request.
     * @throws PropertyServerException something went wrong with the REST call stack.
     */
    public AssetElementsResponse callAssetElementsPostRESTCall(String    methodName,
                                                               String    urlTemplate,
                                                               Object    requestBody,
                                                               Object... params) throws InvalidParameterException,
                                                                                        UserNotAuthorizedException,
                                                                                        PropertyServerException
    {
        AssetElementsResponse restResult = this.callPostRESTCall(methodName, AssetElementsResponse.class, urlTemplate, requestBody, params);

        exceptionHandler.detectAndThrowStandardExceptions(methodName, restResult);

        return restResult;
    }




    /**
     * Issue a POST REST call that returns a list of AssetRelationshipsResponse in a response object.
     *
     * @param methodName  name of the method being called
     * @param urlTemplate  template of the URL for the REST API call with place-holders for the parameters
     * @param requestBody request body for the request
     * @param params  a list of parameters that are slotted into the url template
     * @return response objects
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws UserNotAuthorizedException the user is not authorized to make this request.
     * @throws PropertyServerException something went wrong with the REST call stack.
     */
    public AssetRelationshipsResponse callAssetRelationshipsPostRESTCall(String    methodName,
                                                                         String    urlTemplate,
                                                                         Object    requestBody,
                                                                         Object... params)  throws InvalidParameterException,
                                                                                                      UserNotAuthorizedException,
                                                                                                      PropertyServerException
    {
        AssetRelationshipsResponse restResult = this.callPostRESTCall(methodName, AssetRelationshipsResponse.class, urlTemplate, requestBody, params);

        exceptionHandler.detectAndThrowStandardExceptions(methodName, restResult);

        return restResult;
    }


    /**
     * Issue a POST REST call that returns a list of AssetRelationshipsResponse in a response object.
     *
     * @param methodName  name of the method being called
     * @param urlTemplate  template of the URL for the REST API call with place-holders for the parameters
     * @param requestBody request body for the request
     * @param params  a list of parameters that are slotted into the url template
     * @return response objects
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws UserNotAuthorizedException the user is not authorized to make this request.
     * @throws PropertyServerException something went wrong with the REST call stack.
     */
    public RelatedAssetsResponse callRelatedAssetsPostRESTCall(String    methodName,
                                                               String    urlTemplate,
                                                               Object    requestBody,
                                                               Object... params)  throws InvalidParameterException,
                                                                                         UserNotAuthorizedException,
                                                                                         PropertyServerException
    {
        RelatedAssetsResponse restResult = this.callPostRESTCall(methodName, RelatedAssetsResponse.class, urlTemplate, requestBody, params);

        exceptionHandler.detectAndThrowStandardExceptions(methodName, restResult);

        return restResult;
    }



    /**
     * Issue a GET REST call that returns a ServerAssetUseElement in a response object.
     *
     * @param methodName  name of the method being called.
     * @param urlTemplate template of the URL for the REST API call with place-holders for the parameters.
     * @param params      a list of parameters that are slotted into the url template.
     *
     * @return response object
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws UserNotAuthorizedException the user is not authorized to make this request.
     * @throws PropertyServerException something went wrong with the REST call stack.
     */
    public ServerAssetUseResponse callServerAssetUseGetRESTCall(String    methodName,
                                                                String    urlTemplate,
                                                                Object... params) throws InvalidParameterException,
                                                                                         UserNotAuthorizedException,
                                                                                         PropertyServerException
    {
        ServerAssetUseResponse restResult = this.callGetRESTCall(methodName, ServerAssetUseResponse.class, urlTemplate, params);

        exceptionHandler.detectAndThrowStandardExceptions(methodName, restResult);

        return restResult;
    }


    /**
     * Issue a GET REST call that returns a list of ServerAssetUseElements in a response object.
     *
     * @param methodName  name of the method being called
     * @param urlTemplate  template of the URL for the REST API call with place-holders for the parameters
     * @param params  a list of parameters that are slotted into the url template
     * @return response objects
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws UserNotAuthorizedException the user is not authorized to make this request.
     * @throws PropertyServerException something went wrong with the REST call stack.
     */
    public ServerAssetUsesResponse callServerAssetUseListGetRESTCall(String    methodName,
                                                                     String    urlTemplate,
                                                                     Object... params)  throws InvalidParameterException,
                                                                                               UserNotAuthorizedException,
                                                                                               PropertyServerException
    {
        ServerAssetUsesResponse restResult = this.callGetRESTCall(methodName, ServerAssetUsesResponse.class, urlTemplate, params);

        exceptionHandler.detectAndThrowStandardExceptions(methodName, restResult);

        return restResult;
    }


    /**
     * Issue a POST REST call that returns a list of ServerAssetUseElements in a response object.
     *
     * @param methodName  name of the method being called
     * @param urlTemplate  template of the URL for the REST API call with place-holders for the parameters
     * @param requestBody request body for the request
     * @param params  a list of parameters that are slotted into the url template
     * @return response objects
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws UserNotAuthorizedException the user is not authorized to make this request.
     * @throws PropertyServerException something went wrong with the REST call stack.
     */
    public ServerAssetUsesResponse callServerAssetUseListPostRESTCall(String    methodName,
                                                                      String    urlTemplate,
                                                                      Object    requestBody,
                                                                      Object... params)  throws InvalidParameterException,
                                                                                                UserNotAuthorizedException,
                                                                                                PropertyServerException
    {
        ServerAssetUsesResponse restResult = this.callPostRESTCall(methodName, ServerAssetUsesResponse.class, urlTemplate, requestBody, params);

        exceptionHandler.detectAndThrowStandardExceptions(methodName, restResult);

        return restResult;
    }


    /**
     * Issue a POST REST call that returns a list of SupportedCapabilityElements in a response object.
     *
     * @param methodName  name of the method being called
     * @param urlTemplate  template of the URL for the REST API call with place-holders for the parameters
     * @param requestBody request body for the request
     * @param params  a list of parameters that are slotted into the url template
     * @return response objects
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws UserNotAuthorizedException the user is not authorized to make this request.
     * @throws PropertyServerException something went wrong with the REST call stack.
     */
    public SupportedCapabilitiesResponse callSupportedCapabilityListPostRESTCall(String    methodName,
                                                                                 String    urlTemplate,
                                                                                 Object    requestBody,
                                                                                 Object... params)  throws InvalidParameterException,
                                                                                                           UserNotAuthorizedException,
                                                                                                           PropertyServerException
    {
        SupportedCapabilitiesResponse restResult = this.callPostRESTCall(methodName, SupportedCapabilitiesResponse.class, urlTemplate, requestBody, params);

        exceptionHandler.detectAndThrowStandardExceptions(methodName, restResult);

        return restResult;
    }



    /**
     * Issue a POST REST call that returns a DataFlowElementResponse object.
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
    public DataFlowElementResponse callDataFlowPostRESTCall(String    methodName,
                                                            String    urlTemplate,
                                                            Object    requestBody,
                                                            Object... params) throws InvalidParameterException,
                                                                                     UserNotAuthorizedException,
                                                                                     PropertyServerException
    {
        DataFlowElementResponse restResult = this.callPostRESTCall(methodName, DataFlowElementResponse.class, urlTemplate, requestBody, params);

        exceptionHandler.detectAndThrowStandardExceptions(methodName, restResult);

        return restResult;
    }


    /**
     * Issue a POST REST call that returns a DataFlowElementsResponse object.
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
    public DataFlowElementsResponse callDataFlowsPostRESTCall(String    methodName,
                                                              String    urlTemplate,
                                                              Object    requestBody,
                                                              Object... params) throws InvalidParameterException,
                                                                                       UserNotAuthorizedException,
                                                                                       PropertyServerException
    {
        DataFlowElementsResponse restResult = this.callPostRESTCall(methodName, DataFlowElementsResponse.class, urlTemplate, requestBody, params);

        exceptionHandler.detectAndThrowStandardExceptions(methodName, restResult);

        return restResult;
    }


    /**
     * Issue a POST REST call that returns a ControlFlowElementResponse object.
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
    public ControlFlowElementResponse callControlFlowPostRESTCall(String    methodName,
                                                                  String    urlTemplate,
                                                                  Object    requestBody,
                                                                  Object... params) throws InvalidParameterException,
                                                                                           UserNotAuthorizedException,
                                                                                           PropertyServerException
    {
        ControlFlowElementResponse restResult = this.callPostRESTCall(methodName, ControlFlowElementResponse.class, urlTemplate, requestBody, params);

        exceptionHandler.detectAndThrowStandardExceptions(methodName, restResult);

        return restResult;
    }


    /**
     * Issue a POST REST call that returns a ControlFlowElementsResponse object.
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
    public ControlFlowElementsResponse callControlFlowsPostRESTCall(String    methodName,
                                                                    String    urlTemplate,
                                                                    Object    requestBody,
                                                                    Object... params) throws InvalidParameterException,
                                                                                             UserNotAuthorizedException,
                                                                                             PropertyServerException
    {
        ControlFlowElementsResponse restResult = this.callPostRESTCall(methodName, ControlFlowElementsResponse.class, urlTemplate, requestBody, params);

        exceptionHandler.detectAndThrowStandardExceptions(methodName, restResult);

        return restResult;
    }


    /**
     * Issue a POST REST call that returns a ProcessCallElementResponse object.
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
    public ProcessCallElementResponse callProcessCallPostRESTCall(String    methodName,
                                                                  String    urlTemplate,
                                                                  Object    requestBody,
                                                                  Object... params) throws InvalidParameterException,
                                                                                           UserNotAuthorizedException,
                                                                                           PropertyServerException
    {
        ProcessCallElementResponse restResult = this.callPostRESTCall(methodName, ProcessCallElementResponse.class, urlTemplate, requestBody, params);

        exceptionHandler.detectAndThrowStandardExceptions(methodName, restResult);

        return restResult;
    }


    /**
     * Issue a POST REST call that returns a ProcessCallElementsResponse object.
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
    public ProcessCallElementsResponse callProcessCallsPostRESTCall(String    methodName,
                                                                    String    urlTemplate,
                                                                    Object    requestBody,
                                                                    Object... params) throws InvalidParameterException,
                                                                                             UserNotAuthorizedException,
                                                                                             PropertyServerException
    {
        ProcessCallElementsResponse restResult = this.callPostRESTCall(methodName, ProcessCallElementsResponse.class, urlTemplate, requestBody, params);

        exceptionHandler.detectAndThrowStandardExceptions(methodName, restResult);

        return restResult;
    }


    /**
     * Issue a POST REST call that returns a LineageMappingElementResponse object.
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
    public LineageMappingElementResponse callLineageMappingPostRESTCall(String    methodName,
                                                                        String    urlTemplate,
                                                                        Object    requestBody,
                                                                        Object... params) throws InvalidParameterException,
                                                                                                 UserNotAuthorizedException,
                                                                                                 PropertyServerException
    {
        LineageMappingElementResponse restResult = this.callPostRESTCall(methodName, LineageMappingElementResponse.class, urlTemplate, requestBody, params);

        exceptionHandler.detectAndThrowStandardExceptions(methodName, restResult);

        return restResult;
    }


    /**
     * Issue a POST REST call that returns a LineageMappingElementsResponse object.
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
    public LineageMappingElementsResponse callLineageMappingsPostRESTCall(String    methodName,
                                                                          String    urlTemplate,
                                                                          Object    requestBody,
                                                                          Object... params) throws InvalidParameterException,
                                                                                                   UserNotAuthorizedException,
                                                                                                   PropertyServerException
    {
        LineageMappingElementsResponse restResult = this.callPostRESTCall(methodName, LineageMappingElementsResponse.class, urlTemplate, requestBody, params);

        exceptionHandler.detectAndThrowStandardExceptions(methodName, restResult);

        return restResult;
    }


    /**
     * Issue a GET REST call that returns a DiscoveryAnalysisReportListResponse object.
     *
     * @param methodName  name of the method being called.
     * @param urlTemplate template of the URL for the REST API call with place-holders for the parameters.
     * @param params      a list of parameters that are slotted into the url template.
     *
     * @return DiscoveryAnalysisReportResponse
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws UserNotAuthorizedException the user is not authorized to make this request.
     * @throws PropertyServerException something went wrong with the REST call stack.
     */
    public SurveyReportsResponse callSurveyReportListGetRESTCall(String    methodName,
                                                                 String    urlTemplate,
                                                                 Object... params) throws InvalidParameterException,
                                                                                          UserNotAuthorizedException,
                                                                                          PropertyServerException
    {
        SurveyReportsResponse restResult = this.callGetRESTCall(methodName,
                                                                SurveyReportsResponse.class,
                                                                urlTemplate,
                                                                params);

        exceptionHandler.detectAndThrowStandardExceptions(methodName, restResult);

        return restResult;
    }


    /**
     * Issue a POST REST call that returns a DiscoveryAnalysisReportListResponse object.
     *
     * @param methodName  name of the method being called.
     * @param urlTemplate template of the URL for the REST API call with place-holders for the parameters.
     * @param requestBody request body for the REST call - contains most of the parameters
     * @param params      a list of parameters that are slotted into the url template.
     *
     * @return DiscoveryAnalysisReportResponse
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws UserNotAuthorizedException the user is not authorized to make this request.
     * @throws PropertyServerException something went wrong with the REST call stack.
     */
    public SurveyReportsResponse callDiscoveryAnalysisReportListPostRESTCall(String    methodName,
                                                                             String    urlTemplate,
                                                                             Object    requestBody,
                                                                             Object... params) throws InvalidParameterException,
                                                                                                      UserNotAuthorizedException,
                                                                                                      PropertyServerException
    {
        SurveyReportsResponse restResult = this.callPostRESTCall(methodName,
                                                                 SurveyReportsResponse.class,
                                                                 urlTemplate,
                                                                 requestBody,
                                                                 params);

        exceptionHandler.detectAndThrowStandardExceptions(methodName, restResult);

        return restResult;
    }


    /**
     * Issue a GET REST call that returns an AnnotationsResponse object.
     *
     * @param methodName  name of the method being called.
     * @param urlTemplate template of the URL for the REST API call with place-holders for the parameters.
     * @param params      a list of parameters that are slotted into the url template.
     *
     * @return AnnotationsResponse
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws UserNotAuthorizedException the user is not authorized to make this request.
     * @throws PropertyServerException something went wrong with the REST call stack.
     */
    public AnnotationsResponse callAnnotationListGetRESTCall(String    methodName,
                                                             String    urlTemplate,
                                                             Object... params) throws InvalidParameterException,
                                                                                      UserNotAuthorizedException,
                                                                                      PropertyServerException
    {
        AnnotationsResponse restResult = this.callGetRESTCall(methodName, AnnotationsResponse.class, urlTemplate, params);

        exceptionHandler.detectAndThrowStandardExceptions(methodName, restResult);

        return restResult;
    }


    /**
     * Issue a POST REST call that returns an AnnotationResponse object.
     *
     * @param methodName  name of the method being called.
     * @param urlTemplate template of the URL for the REST API call with place-holders for the parameters.
     * @param requestBody request body for the REST call - contains most of the parameters
     * @param params      a list of parameters that are slotted into the url template.
     *
     * @return AnnotationResponse
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws UserNotAuthorizedException the user is not authorized to make this request.
     * @throws PropertyServerException something went wrong with the REST call stack.
     */
    public AnnotationsResponse callAnnotationListPostRESTCall(String    methodName,
                                                              String    urlTemplate,
                                                              Object    requestBody,
                                                              Object... params) throws InvalidParameterException,
                                                                                       UserNotAuthorizedException,
                                                                                       PropertyServerException
    {
        AnnotationsResponse restResult = this.callPostRESTCall(methodName, AnnotationsResponse.class, urlTemplate, requestBody, params);

        exceptionHandler.detectAndThrowStandardExceptions(methodName, restResult);

        return restResult;
    }


    /**
     * Issue a GET REST call that returns an AnnotationResponse object.
     *
     * @param methodName  name of the method being called.
     * @param urlTemplate template of the URL for the REST API call with place-holders for the parameters.
     * @param params      a list of parameters that are slotted into the url template.
     *
     * @return AnnotationResponse
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws UserNotAuthorizedException the user is not authorized to make this request.
     * @throws PropertyServerException something went wrong with the REST call stack.
     */
    public AnnotationResponse callAnnotationGetRESTCall(String    methodName,
                                                        String    urlTemplate,
                                                        Object... params) throws InvalidParameterException,
                                                                                 UserNotAuthorizedException,
                                                                                 PropertyServerException
    {
        AnnotationResponse restResult = this.callGetRESTCall(methodName, AnnotationResponse.class, urlTemplate, params);

        exceptionHandler.detectAndThrowStandardExceptions(methodName, restResult);

        return restResult;
    }


    /**
     * Issue a POST REST call that returns an AnnotationResponse object.
     *
     * @param methodName  name of the method being called.
     * @param urlTemplate template of the URL for the REST API call with place-holders for the parameters.
     * @param requestBody request body for the REST call - contains most of the parameters
     * @param params      a list of parameters that are slotted into the url template.
     *
     * @return AnnotationResponse
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws UserNotAuthorizedException the user is not authorized to make this request.
     * @throws PropertyServerException something went wrong with the REST call stack.
     */
    public AnnotationResponse callAnnotationPostRESTCall(String    methodName,
                                                         String    urlTemplate,
                                                         Object    requestBody,
                                                         Object... params) throws InvalidParameterException,
                                                                                  UserNotAuthorizedException,
                                                                                  PropertyServerException
    {
        AnnotationResponse restResult = this.callPostRESTCall(methodName, AnnotationResponse.class, urlTemplate, requestBody, params);

        exceptionHandler.detectAndThrowStandardExceptions(methodName, restResult);

        return restResult;
    }


    /**
     * Issue a GET REST call that returns a CertificationTypeResponse object.
     *
     * @param methodName  name of the method being called
     * @param urlTemplate  template of the URL with place-holders for the parameters
     * @param params  a list of parameters that are slotted into the url template
     * @return CertificationTypeResponse
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws UserNotAuthorizedException the user is not authorized to make this request.
     * @throws PropertyServerException the repository is not available or not working properly.
     */
    public CertificationTypeResponse callCertificationTypeGetRESTCall(String    methodName,
                                                                      String    urlTemplate,
                                                                      Object... params) throws InvalidParameterException,
                                                                                               PropertyServerException,
                                                                                               UserNotAuthorizedException
    {
        CertificationTypeResponse restResult = this.callGetRESTCall(methodName, CertificationTypeResponse.class, urlTemplate, params);

        exceptionHandler.detectAndThrowStandardExceptions(methodName, restResult);

        return restResult;
    }


    /**
     * Issue a GET REST call that returns a list of CertificationTypeElement objects.
     *
     * @param methodName  name of the method being called
     * @param urlTemplate  template of the URL with place-holders for the parameters
     * @param params  a list of parameters that are slotted into the url template
     * @return CertificationTypesResponse
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws UserNotAuthorizedException the user is not authorized to make this request.
     * @throws PropertyServerException the repository is not available or not working properly.
     */
    public CertificationTypesResponse callCertificationTypeListGetRESTCall(String    methodName,
                                                                           String    urlTemplate,
                                                                           Object... params) throws InvalidParameterException,
                                                                                                    PropertyServerException,
                                                                                                    UserNotAuthorizedException
    {
        CertificationTypesResponse restResult = this.callGetRESTCall(methodName, CertificationTypesResponse.class, urlTemplate, params);

        exceptionHandler.detectAndThrowStandardExceptions(methodName, restResult);

        return restResult;
    }


    /**
     * Issue a POST REST call that returns a list of CertificationTypeElement objects.
     *
     * @param methodName  name of the method being called
     * @param urlTemplate  template of the URL with place-holders for the parameters
     * @param requestBody request body for the REST call - contains most of the parameters
     * @param params  a list of parameters that are slotted into the url template
     * @return CertificationTypesResponse
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws UserNotAuthorizedException the user is not authorized to make this request.
     * @throws PropertyServerException the repository is not available or not working properly.
     */
    public CertificationTypesResponse callCertificationTypeListPostRESTCall(String    methodName,
                                                                            String    urlTemplate,
                                                                            Object    requestBody,
                                                                            Object... params) throws InvalidParameterException,
                                                                                                     PropertyServerException,
                                                                                                     UserNotAuthorizedException
    {
        CertificationTypesResponse restResult = this.callPostRESTCall(methodName, CertificationTypesResponse.class, urlTemplate, requestBody, params);

        exceptionHandler.detectAndThrowStandardExceptions(methodName, restResult);

        return restResult;
    }


    /**
     * Issue a GET REST call that returns a ElementStubsResponse object.
     *
     * @param methodName  name of the method being called
     * @param urlTemplate  template of the URL with place-holders for the parameters
     * @param params  a list of parameters that are slotted into the url template
     * @return response object
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws UserNotAuthorizedException the user is not authorized to make this request.
     * @throws PropertyServerException the repository is not available or not working properly.
     */
    public ElementStubsResponse callElementStubsGetRESTCall(String    methodName,
                                                            String    urlTemplate,
                                                            Object... params) throws InvalidParameterException,
                                                                                     PropertyServerException,
                                                                                     UserNotAuthorizedException
    {
        ElementStubsResponse restResult = this.callGetRESTCall(methodName, ElementStubsResponse.class, urlTemplate, params);

        exceptionHandler.detectAndThrowStandardExceptions(methodName, restResult);

        return restResult;
    }




    /**
     * Issue a POST REST call that returns a ElementStubsResponse object.
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
    public ElementStubsResponse callElementStubsPostRESTCall(String    methodName,
                                                             String    urlTemplate,
                                                             Object    requestBody,
                                                             Object... params) throws InvalidParameterException,
                                                                                      UserNotAuthorizedException,
                                                                                      PropertyServerException
    {
        ElementStubsResponse restResult = this.callPostRESTCall(methodName, ElementStubsResponse.class, urlTemplate, requestBody, params);

        exceptionHandler.detectAndThrowStandardExceptions(methodName, restResult);

        return restResult;
    }


    /**
     * Issue a POST REST call that returns a MetadataElementSummariesResponse object.
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
    public MetadataElementSummariesResponse callMetadataElementSummariesPostRESTCall(String    methodName,
                                                                                     String    urlTemplate,
                                                                                     Object    requestBody,
                                                                                     Object... params) throws InvalidParameterException,
                                                                                                              UserNotAuthorizedException,
                                                                                                              PropertyServerException
    {
        MetadataElementSummariesResponse restResult = this.callPostRESTCall(methodName, MetadataElementSummariesResponse.class, urlTemplate, requestBody, params);

        exceptionHandler.detectAndThrowStandardExceptions(methodName, restResult);

        return restResult;
    }


    /**
     * Issue a GET REST call that returns a ExternalReferenceResponse object.
     *
     * @param methodName  name of the method being called
     * @param urlTemplate  template of the URL with place-holders for the parameters
     * @param params  a list of parameters that are slotted into the url template
     * @return ExternalReferenceResponse
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws UserNotAuthorizedException the user is not authorized to make this request.
     * @throws PropertyServerException the repository is not available or not working properly.
     */
    public ExternalReferenceResponse callExternalReferenceGetRESTCall(String    methodName,
                                                                      String    urlTemplate,
                                                                      Object... params) throws InvalidParameterException,
                                                                                               PropertyServerException,
                                                                                               UserNotAuthorizedException
    {
        ExternalReferenceResponse restResult = this.callGetRESTCall(methodName, ExternalReferenceResponse.class, urlTemplate, params);

        exceptionHandler.detectAndThrowStandardExceptions(methodName, restResult);

        return restResult;
    }


    /**
     * Issue a GET REST call that returns a list of ExternalReferenceElement objects.
     *
     * @param methodName  name of the method being called
     * @param urlTemplate  template of the URL with place-holders for the parameters
     * @param params  a list of parameters that are slotted into the url template
     * @return ExternalReferencesResponse
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws UserNotAuthorizedException the user is not authorized to make this request.
     * @throws PropertyServerException the repository is not available or not working properly.
     */
    public ExternalReferencesResponse callExternalReferencesGetRESTCall(String    methodName,
                                                                        String    urlTemplate,
                                                                        Object... params) throws InvalidParameterException,
                                                                                                    PropertyServerException,
                                                                                                    UserNotAuthorizedException
    {
        ExternalReferencesResponse restResult = this.callGetRESTCall(methodName, ExternalReferencesResponse.class, urlTemplate, params);

        exceptionHandler.detectAndThrowStandardExceptions(methodName, restResult);

        return restResult;
    }


    /**
     * Issue a POST REST call that returns a list of ExternalReferenceElement objects.
     *
     * @param methodName  name of the method being called
     * @param urlTemplate  template of the URL with place-holders for the parameters
     * @param requestBody request body for the REST call - contains most of the parameters
     * @param params  a list of parameters that are slotted into the url template
     * @return ExternalReferencesResponse
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws UserNotAuthorizedException the user is not authorized to make this request.
     * @throws PropertyServerException the repository is not available or not working properly.
     */
    public ExternalReferencesResponse callExternalReferencesPostRESTCall(String    methodName,
                                                                         String    urlTemplate,
                                                                         Object    requestBody,
                                                                         Object... params) throws InvalidParameterException,
                                                                                                     PropertyServerException,
                                                                                                     UserNotAuthorizedException
    {
        ExternalReferencesResponse restResult = this.callPostRESTCall(methodName, ExternalReferencesResponse.class, urlTemplate, requestBody, params);

        exceptionHandler.detectAndThrowStandardExceptions(methodName, restResult);

        return restResult;
    }


    /**
     * Issue a GET REST call that returns a RelatedElementsResponse object.
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
    public RelatedElementsResponse callRelatedElementsGetRESTCall(String    methodName,
                                                                  String    urlTemplate,
                                                                  Object... params) throws InvalidParameterException,
                                                                                              UserNotAuthorizedException,
                                                                                              PropertyServerException
    {
        RelatedElementsResponse restResult = this.callGetRESTCall(methodName, RelatedElementsResponse.class, urlTemplate, params);

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
    public RelatedElementsResponse callRelatedElementsPostRESTCall(String    methodName,
                                                                   String    urlTemplate,
                                                                   Object    requestBody,
                                                                   Object... params) throws InvalidParameterException,
                                                                                            UserNotAuthorizedException,
                                                                                            PropertyServerException
    {
        RelatedElementsResponse restResult = this.callPostRESTCall(methodName,
                                                                   RelatedElementsResponse.class,
                                                                   urlTemplate,
                                                                   requestBody,
                                                                   params);

        exceptionHandler.detectAndThrowStandardExceptions(methodName, restResult);

        return restResult;
    }



    /**
     * Issue a GET REST call that returns a LicenseTypeResponse object.
     *
     * @param methodName  name of the method being called
     * @param urlTemplate  template of the URL with place-holders for the parameters
     * @param params  a list of parameters that are slotted into the url template
     * @return LicenseTypeResponse
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws UserNotAuthorizedException the user is not authorized to make this request.
     * @throws PropertyServerException the repository is not available or not working properly.
     */
    public LicenseTypeResponse callLicenseTypeGetRESTCall(String    methodName,
                                                          String    urlTemplate,
                                                          Object... params) throws InvalidParameterException,
                                                                                   PropertyServerException,
                                                                                   UserNotAuthorizedException
    {
        LicenseTypeResponse restResult = this.callGetRESTCall(methodName, LicenseTypeResponse.class, urlTemplate, params);

        exceptionHandler.detectAndThrowStandardExceptions(methodName, restResult);

        return restResult;
    }


    /**
     * Issue a GET REST call that returns a list of LicenseTypeElement objects.
     *
     * @param methodName  name of the method being called
     * @param urlTemplate  template of the URL with place-holders for the parameters
     * @param params  a list of parameters that are slotted into the url template
     * @return LicenseTypesResponse
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws UserNotAuthorizedException the user is not authorized to make this request.
     * @throws PropertyServerException the repository is not available or not working properly.
     */
    public LicenseTypesResponse callLicenseTypesGetRESTCall(String    methodName,
                                                            String    urlTemplate,
                                                            Object... params) throws InvalidParameterException,
                                                                                     PropertyServerException,
                                                                                     UserNotAuthorizedException
    {
        LicenseTypesResponse restResult = this.callGetRESTCall(methodName, LicenseTypesResponse.class, urlTemplate, params);

        exceptionHandler.detectAndThrowStandardExceptions(methodName, restResult);

        return restResult;
    }



    /**
     * Issue a GET REST call that returns a list of LicenseElement objects.
     *
     * @param methodName  name of the method being called
     * @param urlTemplate  template of the URL with place-holders for the parameters
     * @param params  a list of parameters that are slotted into the url template
     * @return LicensesResponse
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws UserNotAuthorizedException the user is not authorized to make this request.
     * @throws PropertyServerException the repository is not available or not working properly.
     */
    public LicensesResponse callLicensesGetRESTCall(String    methodName,
                                                    String    urlTemplate,
                                                    Object... params) throws InvalidParameterException,
                                                                             PropertyServerException,
                                                                             UserNotAuthorizedException
    {
        LicensesResponse restResult = this.callGetRESTCall(methodName, LicensesResponse.class, urlTemplate, params);

        exceptionHandler.detectAndThrowStandardExceptions(methodName, restResult);

        return restResult;
    }


    /**
     * Issue a POST REST call that returns a list of LicenseTypeElement objects.
     *
     * @param methodName  name of the method being called
     * @param urlTemplate  template of the URL with place-holders for the parameters
     * @param requestBody request body for the REST call - contains most of the parameters
     * @param params  a list of parameters that are slotted into the url template
     * @return LicenseTypesResponse
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws UserNotAuthorizedException the user is not authorized to make this request.
     * @throws PropertyServerException the repository is not available or not working properly.
     */
    public LicenseTypesResponse callLicenseTypeListPostRESTCall(String    methodName,
                                                                String    urlTemplate,
                                                                Object    requestBody,
                                                                Object... params) throws InvalidParameterException,
                                                                                         PropertyServerException,
                                                                                         UserNotAuthorizedException
    {
        LicenseTypesResponse restResult = this.callPostRESTCall(methodName, LicenseTypesResponse.class, urlTemplate, requestBody, params);

        exceptionHandler.detectAndThrowStandardExceptions(methodName, restResult);

        return restResult;
    }



    /**
     * Issue a POST REST call that returns a SchemaAttributeResponse object.
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
    public SchemaAttributeResponse callSchemaAttributePostRESTCall(String    methodName,
                                                                   String    urlTemplate,
                                                                   Object    requestBody,
                                                                   Object... params) throws InvalidParameterException,
                                                                                            UserNotAuthorizedException,
                                                                                            PropertyServerException
    {
        SchemaAttributeResponse restResult = this.callPostRESTCall(methodName, SchemaAttributeResponse.class, urlTemplate, requestBody, params);

        exceptionHandler.detectAndThrowStandardExceptions(methodName, restResult);

        return restResult;
    }


    /**
     * Issue a POST REST call that returns a SchemaAttributesResponse object.
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
    public SchemaAttributesResponse callSchemaAttributesPostRESTCall(String    methodName,
                                                                     String    urlTemplate,
                                                                     Object    requestBody,
                                                                     Object... params) throws InvalidParameterException,
                                                                                              UserNotAuthorizedException,
                                                                                              PropertyServerException
    {
        SchemaAttributesResponse restResult = this.callPostRESTCall(methodName, SchemaAttributesResponse.class, urlTemplate, requestBody, params);

        exceptionHandler.detectAndThrowStandardExceptions(methodName, restResult);

        return restResult;
    }


    /**
     * Issue a POST REST call that returns a SchemaTypeElementResponse object.
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
    public SchemaTypeElementResponse callSchemaTypePostRESTCall(String    methodName,
                                                                String    urlTemplate,
                                                                Object    requestBody,
                                                                Object... params) throws InvalidParameterException,
                                                                                         UserNotAuthorizedException,
                                                                                         PropertyServerException
    {
        SchemaTypeElementResponse restResult = this.callPostRESTCall(methodName, SchemaTypeElementResponse.class, urlTemplate, requestBody, params);

        exceptionHandler.detectAndThrowStandardExceptions(methodName, restResult);

        return restResult;
    }


    /**
     * Issue a POST REST call that returns a SchemaTypeElementsResponse object.
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
    public SchemaTypeElementsResponse callSchemaTypesPostRESTCall(String    methodName,
                                                                  String    urlTemplate,
                                                                  Object    requestBody,
                                                                  Object... params) throws InvalidParameterException,
                                                                                           UserNotAuthorizedException,
                                                                                           PropertyServerException
    {
        SchemaTypeElementsResponse restResult = this.callPostRESTCall(methodName, SchemaTypeElementsResponse.class, urlTemplate, requestBody, params);

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
     * Issue a POST REST call that returns a RelationshipElementResponse object.
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
    public RelationshipElementResponse callRelationshipPostRESTCall(String    methodName,
                                                                    String    urlTemplate,
                                                                    Object    requestBody,
                                                                    Object... params) throws InvalidParameterException,
                                                                                             UserNotAuthorizedException,
                                                                                             PropertyServerException
    {
        RelationshipElementResponse restResult = this.callPostRESTCall(methodName, RelationshipElementResponse.class, urlTemplate, requestBody, params);

        exceptionHandler.detectAndThrowStandardExceptions(methodName, restResult);

        return restResult;
    }


    /**
     * Issue a POST REST call that returns a RelationshipElementsResponse object.
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
    public RelationshipElementsResponse callRelationshipsPostRESTCall(String    methodName,
                                                                      String    urlTemplate,
                                                                      Object    requestBody,
                                                                      Object... params) throws InvalidParameterException,
                                                                                               UserNotAuthorizedException,
                                                                                               PropertyServerException
    {
        RelationshipElementsResponse restResult = this.callPostRESTCall(methodName, RelationshipElementsResponse.class, urlTemplate, requestBody, params);

        exceptionHandler.detectAndThrowStandardExceptions(methodName, restResult);

        return restResult;
    }


    /**
     * Issue a GET REST call that returns a ElementStubResponse object.
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
    public ElementStubResponse callElementStubGetRESTCall(String    methodName,
                                                          String    urlTemplate,
                                                          Object... params) throws InvalidParameterException,
                                                                                   UserNotAuthorizedException,
                                                                                   PropertyServerException
    {
        ElementStubResponse restResult = this.callGetRESTCall(methodName, ElementStubResponse.class, urlTemplate, params);

        exceptionHandler.detectAndThrowStandardExceptions(methodName, restResult);

        return restResult;
    }


    /**
     * Issue a GET REST call that returns a ConnectorReportResponse object.
     *
     * @param methodName  name of the method being called.
     * @param urlTemplate template of the URL for the REST API with place-holders for the parameters.
     * @param params      a list of parameters that are slotted into the url template.
     *
     * @return ConnectorReportResponse
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws UserNotAuthorizedException the user is not authorized to make this request.
     * @throws PropertyServerException something went wrong with the REST call stack.
     */
    public ConnectorReportResponse callOCFConnectorReportGetRESTCall(String    methodName,
                                                                     String    urlTemplate,
                                                                     Object... params) throws InvalidParameterException,
                                                                                          UserNotAuthorizedException,
                                                                                          PropertyServerException
    {
        ConnectorReportResponse restResult = this.callGetRESTCall(methodName,
                                                                  ConnectorReportResponse.class,
                                                                  urlTemplate,
                                                                  params);

        exceptionHandler.detectAndThrowStandardExceptions(methodName, restResult);

        return restResult;
    }



    /**
     * Issue a GET REST call that returns a ITProfileElement in a response object.
     *
     * @param methodName  name of the method being called.
     * @param urlTemplate template of the URL for the REST API call with place-holders for the parameters.
     * @param params      a list of parameters that are slotted into the url template.
     *
     * @return response object
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws UserNotAuthorizedException the user is not authorized to make this request.
     * @throws PropertyServerException something went wrong with the REST call stack.
     */
    public ITProfileResponse callITProfileGetRESTCall(String    methodName,
                                                      String    urlTemplate,
                                                      Object... params) throws InvalidParameterException,
                                                                               UserNotAuthorizedException,
                                                                               PropertyServerException
    {
        ITProfileResponse restResult = this.callGetRESTCall(methodName, ITProfileResponse.class, urlTemplate, params);

        exceptionHandler.detectAndThrowStandardExceptions(methodName, restResult);

        return restResult;
    }


    /**
     * Issue a GET REST call that returns a list of ITProfileElements in a response object.
     *
     * @param methodName  name of the method being called
     * @param urlTemplate  template of the URL for the REST API call with place-holders for the parameters
     * @param params  a list of parameters that are slotted into the url template
     * @return response object
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws UserNotAuthorizedException the user is not authorized to make this request.
     * @throws PropertyServerException something went wrong with the REST call stack.
     */
    public ITProfilesResponse callITProfileListGetRESTCall(String    methodName,
                                                           String    urlTemplate,
                                                           Object... params)  throws InvalidParameterException,
                                                                                     UserNotAuthorizedException,
                                                                                     PropertyServerException
    {
        ITProfilesResponse restResult = this.callGetRESTCall(methodName, ITProfilesResponse.class, urlTemplate, params);

        exceptionHandler.detectAndThrowStandardExceptions(methodName, restResult);

        return restResult;
    }


    /**
     * Issue a GET REST call that returns a list of ITProfileElements in a response object.
     *
     * @param methodName  name of the method being called
     * @param urlTemplate  template of the URL for the REST API call with place-holders for the parameters
     * @param requestBody request body for the request
     * @param params  a list of parameters that are slotted into the url template
     * @return response object
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws UserNotAuthorizedException the user is not authorized to make this request.
     * @throws PropertyServerException something went wrong with the REST call stack.
     */
    public ITProfilesResponse callITProfileListPostRESTCall(String    methodName,
                                                            String    urlTemplate,
                                                            Object    requestBody,
                                                            Object... params)  throws InvalidParameterException,
                                                                                      UserNotAuthorizedException,
                                                                                      PropertyServerException
    {
        ITProfilesResponse restResult = this.callPostRESTCall(methodName, ITProfilesResponse.class, urlTemplate, requestBody, params);

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
     * Issue a POST REST call that returns a UserIdentityResponse object.
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
    public UserIdentityResponse callUserIdentityPostRESTCall(String    methodName,
                                                             String    urlTemplate,
                                                             Object    requestBody,
                                                             Object... params) throws InvalidParameterException,
                                                                                      UserNotAuthorizedException,
                                                                                      PropertyServerException
    {
        UserIdentityResponse restResult = this.callPostRESTCall(methodName, UserIdentityResponse.class, urlTemplate, requestBody, params);

        exceptionHandler.detectAndThrowStandardExceptions(methodName, restResult);

        return restResult;
    }


    /**
     * Issue a POST REST call that returns a UserIdentitiesResponse object.
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
    public UserIdentitiesResponse callUserIdentitiesPostRESTCall(String    methodName,
                                                                 String    urlTemplate,
                                                                 Object    requestBody,
                                                                 Object... params) throws InvalidParameterException,
                                                                                          UserNotAuthorizedException,
                                                                                          PropertyServerException
    {
        UserIdentitiesResponse restResult = this.callPostRESTCall(methodName, UserIdentitiesResponse.class, urlTemplate, requestBody, params);

        exceptionHandler.detectAndThrowStandardExceptions(methodName, restResult);

        return restResult;
    }



    /**
     * Issue a GET REST call that returns a list of UserIdentityElements in a response object.
     *
     * @param methodName  name of the method being called
     * @param urlTemplate  template of the URL for the REST API call with place-holders for the parameters
     * @param params  a list of parameters that are slotted into the url template
     * @return UserIdentitiesResponse
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws UserNotAuthorizedException the user is not authorized to make this request.
     * @throws PropertyServerException something went wrong with the REST call stack.
     */
    public UserIdentitiesResponse callUserIdentitiesGetRESTCall(String    methodName,
                                                                String    urlTemplate,
                                                                Object... params)  throws InvalidParameterException,
                                                                                          UserNotAuthorizedException,
                                                                                          PropertyServerException
    {
        UserIdentitiesResponse restResult = this.callGetRESTCall(methodName, UserIdentitiesResponse.class, urlTemplate, params);

        exceptionHandler.detectAndThrowStandardExceptions(methodName, restResult);

        return restResult;
    }


    /**
     * Issue a POST REST call that returns a list of UserIdentityElements in a response object.
     *
     * @param methodName  name of the method being called
     * @param urlTemplate  template of the URL for the REST API call with place-holders for the parameters
     * @param requestBody request body for the request
     * @param params  a list of parameters that are slotted into the url template
     * @return UserIdentitiesResponse
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws UserNotAuthorizedException the user is not authorized to make this request.
     * @throws PropertyServerException something went wrong with the REST call stack.
     */
    public UserIdentitiesResponse callUserIdentityListPostRESTCall(String    methodName,
                                                                     String    urlTemplate,
                                                                     Object    requestBody,
                                                                     Object... params)  throws InvalidParameterException,
                                                                                               UserNotAuthorizedException,
                                                                                               PropertyServerException
    {
        UserIdentitiesResponse restResult = this.callPostRESTCall(methodName, UserIdentitiesResponse.class, urlTemplate, requestBody, params);

        exceptionHandler.detectAndThrowStandardExceptions(methodName, restResult);

        return restResult;
    }




    /**
     * Issue a GET REST call that returns a SoftwareCapabilityElement in a response object.
     *
     * @param methodName  name of the method being called.
     * @param urlTemplate template of the URL for the REST API call with place-holders for the parameters.
     * @param params      a list of parameters that are slotted into the url template.
     *
     * @return response object
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws UserNotAuthorizedException the user is not authorized to make this request.
     * @throws PropertyServerException something went wrong with the REST call stack.
     */
    public SoftwareCapabilityResponse callSoftwareServerCapabilityGetRESTCall(String    methodName,
                                                                              String    urlTemplate,
                                                                              Object... params) throws InvalidParameterException,
                                                                                                       UserNotAuthorizedException,
                                                                                                       PropertyServerException
    {
        SoftwareCapabilityResponse restResult = this.callGetRESTCall(methodName, SoftwareCapabilityResponse.class, urlTemplate, params);

        exceptionHandler.detectAndThrowStandardExceptions(methodName, restResult);

        return restResult;
    }


    /**
     * Issue a GET REST call that returns a list of SoftwareServerCapabilityElements in a response object.
     *
     * @param methodName  name of the method being called
     * @param urlTemplate  template of the URL for the REST API call with place-holders for the parameters
     * @param params  a list of parameters that are slotted into the url template
     * @return response objects
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws UserNotAuthorizedException the user is not authorized to make this request.
     * @throws PropertyServerException something went wrong with the REST call stack.
     */
    public SoftwareCapabilitiesResponse callSoftwareServerCapabilitiesGetRESTCall(String    methodName,
                                                                                  String    urlTemplate,
                                                                                  Object... params)  throws InvalidParameterException,
                                                                                                            UserNotAuthorizedException,
                                                                                                            PropertyServerException
    {
        SoftwareCapabilitiesResponse restResult = this.callGetRESTCall(methodName, SoftwareCapabilitiesResponse.class, urlTemplate, params);

        exceptionHandler.detectAndThrowStandardExceptions(methodName, restResult);

        return restResult;
    }


    /**
     * Issue a POST REST call that returns a list of SoftwareServerCapabilityElements in a response object.
     *
     * @param methodName  name of the method being called
     * @param urlTemplate  template of the URL for the REST API call with place-holders for the parameters
     * @param requestBody request body for the request
     * @param params  a list of parameters that are slotted into the url template
     * @return response objects
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws UserNotAuthorizedException the user is not authorized to make this request.
     * @throws PropertyServerException something went wrong with the REST call stack.
     */
    public SoftwareCapabilitiesResponse callSoftwareServerCapabilitiesPostRESTCall(String    methodName,
                                                                                   String    urlTemplate,
                                                                                   Object    requestBody,
                                                                                   Object... params)  throws InvalidParameterException,
                                                                                                             UserNotAuthorizedException,
                                                                                                             PropertyServerException
    {
        SoftwareCapabilitiesResponse restResult = this.callPostRESTCall(methodName, SoftwareCapabilitiesResponse.class, urlTemplate, requestBody, params);

        exceptionHandler.detectAndThrowStandardExceptions(methodName, restResult);

        return restResult;
    }



    /**
     * Issue a GET REST call that returns a DigitalServiceElement in a response object.
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
    public DigitalServiceResponse callDigitalServiceGetRESTCall(String    methodName,
                                                                String    urlTemplate,
                                                                Object... params) throws InvalidParameterException,
                                                                                         UserNotAuthorizedException,
                                                                                         PropertyServerException
    {
        DigitalServiceResponse restResult = this.callGetRESTCall(methodName, DigitalServiceResponse.class, urlTemplate, params);

        exceptionHandler.detectAndThrowStandardExceptions(methodName, restResult);

        return restResult;
    }


    /**
     * Issue a GET REST call that returns a list of DigitalServiceElements in a response object.
     *
     * @param methodName  name of the method being called
     * @param urlTemplate   REST API call URL template with place-holders for the parameters
     * @param params  a list of parameters that are slotted into the url template
     * @return response object
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws UserNotAuthorizedException the user is not authorized to make this request.
     * @throws PropertyServerException something went wrong with the REST call stack.
     */
    public DigitalServicesResponse callDigitalServicesGetRESTCall(String    methodName,
                                                                  String    urlTemplate,
                                                                  Object... params)  throws InvalidParameterException,
                                                                                            UserNotAuthorizedException,
                                                                                            PropertyServerException
    {
        DigitalServicesResponse restResult = this.callGetRESTCall(methodName, DigitalServicesResponse.class, urlTemplate, params);

        exceptionHandler.detectAndThrowStandardExceptions(methodName, restResult);

        return restResult;
    }


    /**
     * Issue a GET REST call that returns a list of DigitalServiceElements in a response object.
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
    public DigitalServicesResponse callDigitalServicesPostRESTCall(String    methodName,
                                                                   String    urlTemplate,
                                                                   Object    requestBody,
                                                                   Object... params)  throws InvalidParameterException,
                                                                                             UserNotAuthorizedException,
                                                                                             PropertyServerException
    {
        DigitalServicesResponse restResult = this.callPostRESTCall(methodName, DigitalServicesResponse.class, urlTemplate, requestBody, params);

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
    public ActorProfileGraphResponse callActorProfileGetRESTCall(String    methodName,
                                                                 String    urlTemplate,
                                                                 Object... params) throws InvalidParameterException,
                                                                                          UserNotAuthorizedException,
                                                                                          PropertyServerException
    {
        ActorProfileGraphResponse restResult = this.callGetRESTCall(methodName, ActorProfileGraphResponse.class, urlTemplate, params);

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
    public ActorProfilesResponse callActorProfilesGetRESTCall(String    methodName,
                                                              String    urlTemplate,
                                                              Object... params)  throws InvalidParameterException,
                                                                                        UserNotAuthorizedException,
                                                                                        PropertyServerException
    {
        ActorProfilesResponse restResult = this.callGetRESTCall(methodName, ActorProfilesResponse.class, urlTemplate, params);

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
    public ActorProfilesResponse callActorProfilesPostRESTCall(String    methodName,
                                                               String    urlTemplate,
                                                               Object    requestBody,
                                                               Object... params)  throws InvalidParameterException,
                                                                                         UserNotAuthorizedException,
                                                                                         PropertyServerException
    {
        ActorProfilesResponse restResult = this.callPostRESTCall(methodName, ActorProfilesResponse.class, urlTemplate, requestBody, params);

        exceptionHandler.detectAndThrowStandardExceptions(methodName, restResult);

        return restResult;
    }



    /**
     * Issue a GET REST call that returns a ActorRoleElement in a response object.
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
    public PersonRolesResponse callPersonRolesGetRESTCall(String    methodName,
                                                          String    urlTemplate,
                                                          Object... params)  throws InvalidParameterException,
                                                                                    UserNotAuthorizedException,
                                                                                    PropertyServerException
    {
        PersonRolesResponse restResult = this.callGetRESTCall(methodName, PersonRolesResponse.class, urlTemplate, params);

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
    public PersonRolesResponse callPersonRolesPostRESTCall(String    methodName,
                                                           String    urlTemplate,
                                                           Object    requestBody,
                                                           Object... params)  throws InvalidParameterException,
                                                                                     UserNotAuthorizedException,
                                                                                     PropertyServerException
    {
        PersonRolesResponse restResult = this.callPostRESTCall(methodName, PersonRolesResponse.class, urlTemplate, requestBody, params);

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
    public AppointeesResponse callAppointeesPostRESTCall(String    methodName,
                                                         String    urlTemplate,
                                                         Object    requestBody,
                                                         Object... params)  throws InvalidParameterException,
                                                                                   UserNotAuthorizedException,
                                                                                   PropertyServerException
    {
        AppointeesResponse restResult = this.callPostRESTCall(methodName, AppointeesResponse.class, urlTemplate, requestBody, params);

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
    public CommunitiesResponse callCommunitiesGetRESTCall(String    methodName,
                                                          String    urlTemplate,
                                                          Object... params)  throws InvalidParameterException,
                                                                                    UserNotAuthorizedException,
                                                                                    PropertyServerException
    {
        CommunitiesResponse restResult = this.callGetRESTCall(methodName, CommunitiesResponse.class, urlTemplate, params);

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
    public CommunitiesResponse callCommunitiesPostRESTCall(String    methodName,
                                                           String    urlTemplate,
                                                           Object    requestBody,
                                                           Object... params)  throws InvalidParameterException,
                                                                                     UserNotAuthorizedException,
                                                                                     PropertyServerException
    {
        CommunitiesResponse restResult = this.callPostRESTCall(methodName, CommunitiesResponse.class, urlTemplate, requestBody, params);

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
     * Issue a GET REST call that returns a LocationsResponse object.
     *
     * @param methodName  name of the method being called.
     * @param urlTemplate  REST API call URL template with place-holders for the parameters.
     * @param params      a list of parameters that are slotted into the url template.
     *
     * @return LocationsResponse
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws UserNotAuthorizedException the user is not authorized to make this request.
     * @throws PropertyServerException the repository is not available or not working properly.
     */
    public LocationsResponse callLocationsGetRESTCall(String    methodName,
                                                      String    urlTemplate,
                                                      Object... params) throws InvalidParameterException,
                                                                               UserNotAuthorizedException,
                                                                               PropertyServerException
    {
        LocationsResponse restResult = this.callGetRESTCall(methodName, LocationsResponse.class, urlTemplate, params);

        exceptionHandler.detectAndThrowStandardExceptions(methodName, restResult);

        return restResult;
    }


    /**
     * Issue a POST REST call that returns a LocationsResponse object.
     *
     * @param methodName  name of the method being called.
     * @param urlTemplate  REST API call URL template with place-holders for the parameters.
     * @param requestBody properties describing the valid value definition/set
     * @param params      a list of parameters that are slotted into the url template.
     *
     * @return LocationsResponse
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws UserNotAuthorizedException the user is not authorized to make this request.
     * @throws PropertyServerException the repository is not available or not working properly.
     */
    public LocationsResponse callLocationsPostRESTCall(String    methodName,
                                                       String    urlTemplate,
                                                       Object    requestBody,
                                                       Object... params) throws InvalidParameterException,
                                                                                UserNotAuthorizedException,
                                                                                PropertyServerException
    {
        LocationsResponse restResult = this.callPostRESTCall(methodName, LocationsResponse.class, urlTemplate, requestBody, params);

        exceptionHandler.detectAndThrowStandardExceptions(methodName, restResult);

        return restResult;
    }




}
