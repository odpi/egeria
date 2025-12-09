/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adminservices.client.rest;

import org.odpi.openmetadata.adapters.connectors.restclients.RESTClientConnector;
import org.odpi.openmetadata.adapters.connectors.restclients.factory.RESTClientFactory;
import org.odpi.openmetadata.adminservices.ffdc.OMAGAdminErrorCode;
import org.odpi.openmetadata.adminservices.ffdc.exception.OMAGConfigurationErrorException;
import org.odpi.openmetadata.adminservices.rest.*;
import org.odpi.openmetadata.adminservices.rest.ConnectionResponse;
import org.odpi.openmetadata.commonservices.ffdc.rest.*;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.connectors.SecretsStoreConnector;
import org.odpi.openmetadata.frameworks.connectors.controls.SecretsStorePurpose;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.UserNotAuthorizedException;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;


/**
 * AssetOwnerRESTClient is responsible for issuing calls to the Admin Services REST APIs.
 */
public class AdminServicesRESTClient
{
    private final String                          serverPlatformURLRoot;  /* Initialized in constructor */
    private final AdminClientRESTExceptionHandler exceptionHandler = new AdminClientRESTExceptionHandler();

    private final RESTClientConnector clientConnector;        /* Initialized in constructor */


    /**
     * Constructor for simple userId and password authentication.
     *
     * @param serverName name of the OMAG Server to call
     * @param serverPlatformURLRoot URL root of the server platform where the OMAG Server is running.
     * @param secretsStoreProvider secrets store connector for bearer token
     * @param secretsStoreLocation secrets store location for bearer token
     * @param secretsStoreCollection secrets store collection for bearer token
     * @param auditLog destination for log messages.
     * @throws InvalidParameterException there is a problem creating the client-side components to issue any
     * REST API calls.
     */
    public AdminServicesRESTClient(String   serverName,
                                   String   serverPlatformURLRoot,
                                   String   secretsStoreProvider,
                                   String   secretsStoreLocation,
                                   String   secretsStoreCollection,
                                   AuditLog auditLog) throws InvalidParameterException
    {
        final String  methodName = "RESTClient(userId and password)";

        this.serverPlatformURLRoot = serverPlatformURLRoot;

        try
        {
            RESTClientFactory  factory = new RESTClientFactory(serverName,
                                                               serverPlatformURLRoot,
                                                               secretsStoreProvider,
                                                               secretsStoreLocation,
                                                               secretsStoreCollection,
                                                               SecretsStorePurpose.REST_BEARER_TOKEN.getName(),
                                                               auditLog);

            this.clientConnector = factory.getClientConnector();
        }
        catch (Exception error)
        {
            throw new InvalidParameterException(OMAGAdminErrorCode.NULL_LOCAL_SERVER_NAME.getMessageDefinition(serverName, error.getMessage()),
                                                this.getClass().getName(),
                                                methodName,
                                                error,
                                                "serverName");
        }
    }


    /**
     * Constructor for simple userId and password authentication.
     *
     * @param serverName name of the OMAG Server to call
     * @param serverPlatformURLRoot URL root of the server platform where the OMAG Server is running.
     * @param secretsStoreConnectorMap connectors to secrets stores
     * @param auditLog destination for log messages.
     * @throws InvalidParameterException there is a problem creating the client-side components to issue any
     * REST API calls.
     */
    public AdminServicesRESTClient(String                             serverName,
                                   String                             serverPlatformURLRoot,
                                   Map<String, SecretsStoreConnector> secretsStoreConnectorMap,
                                   AuditLog                           auditLog) throws InvalidParameterException
    {
        final String  methodName = "RESTClient(userId and password)";

        this.serverPlatformURLRoot = serverPlatformURLRoot;

        try
        {
            RESTClientFactory  factory = new RESTClientFactory(serverName,
                                                               serverPlatformURLRoot,
                                                               secretsStoreConnectorMap,
                                                               auditLog);

            this.clientConnector = factory.getClientConnector();
        }
        catch (Exception error)
        {
            throw new InvalidParameterException(OMAGAdminErrorCode.NULL_LOCAL_SERVER_NAME.getMessageDefinition(serverName, error.getMessage()),
                                                this.getClass().getName(),
                                                methodName,
                                                error,
                                                "serverName");
        }
    }


    /**
     * Issue a POST REST call that returns a VoidResponse object.  This is typically a create
     *
     * @param methodName  name of the method being called.
     * @param urlTemplate  template of the URL for the REST API call with place-holders for the parameters.
     * @param requestBody request body for the request.
     * @param params  a list of parameters that are slotted into the url template.
     *
     * @return VoidResponse
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws UserNotAuthorizedException the user is not authorized to make this request.
     * @throws OMAGConfigurationErrorException something went wrong with the REST call stack.
     */
    public VoidResponse callVoidPostRESTCall(String    methodName,
                                             String    urlTemplate,
                                             Object    requestBody,
                                             Object... params) throws InvalidParameterException,
                                                                      UserNotAuthorizedException,
                                                                      OMAGConfigurationErrorException
    {
        VoidResponse restResult =  this.callPostRESTCall(methodName,
                                                         VoidResponse.class,
                                                         urlTemplate,
                                                         requestBody,
                                                         params);

        exceptionHandler.detectAndThrowAdminExceptions(restResult);

        return restResult;
    }


    /**
     * Issue a DELETE REST call that returns a VoidResponse object.  This is typically a delete
     *
     * @param methodName  name of the method being called.
     * @param urlTemplate  template of the URL for the REST API call with place-holders for the parameters.
     * @param params  a list of parameters that are slotted into the url template.
     *
     * @return VoidResponse
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws UserNotAuthorizedException the user is not authorized to make this request.
     * @throws OMAGConfigurationErrorException something went wrong with the REST call stack.
     */
    public VoidResponse callVoidDeleteRESTCall(String    methodName,
                                               String    urlTemplate,
                                               Object... params) throws InvalidParameterException,
                                                                        UserNotAuthorizedException,
                                                                        OMAGConfigurationErrorException
    {
        VoidResponse restResult =  this.callDeleteRESTCall(methodName,
                                                           VoidResponse.class,
                                                           urlTemplate,
                                                           null,
                                                           params);

        exceptionHandler.detectAndThrowAdminExceptions(restResult);

        return restResult;
    }


    /**
     * Issue a POST REST call that returns a SuccessMessageResponse object.  This is typically a server start.
     *
     * @param methodName  name of the method being called.
     * @param urlTemplate  template of the URL for the REST API call with place-holders for the parameters.
     * @param requestBody request body for the request.
     * @param params  a list of parameters that are slotted into the url template.
     *
     * @return SuccessMessageResponse
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws UserNotAuthorizedException the user is not authorized to make this request.
     * @throws OMAGConfigurationErrorException something went wrong with the REST call stack.
     */
    public SuccessMessageResponse callSuccessMessagePostRESTCall(String    methodName,
                                                                 String    urlTemplate,
                                                                 Object    requestBody,
                                                                 Object... params) throws InvalidParameterException,
                                                                                          UserNotAuthorizedException,
                                                                                          OMAGConfigurationErrorException
    {
        SuccessMessageResponse restResult =  this.callPostRESTCall(methodName,
                                                                   SuccessMessageResponse.class,
                                                                   urlTemplate,
                                                                   requestBody,
                                                                   params);

        exceptionHandler.detectAndThrowAdminExceptions(restResult);

        return restResult;
    }


    /**
     * Issue a GET REST call that returns a StringResponse object.
     *
     * @param methodName  name of the method being called.
     * @param urlTemplate template of the URL for the REST API call with place-holders for the parameters.
     * @param params      a list of parameters that are slotted into the url template.
     *
     * @return StringResponse
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws UserNotAuthorizedException the user is not authorized to make this request.
     * @throws OMAGConfigurationErrorException something went wrong with the REST call stack.
     */
    public StringResponse callStringGetRESTCall(String    methodName,
                                                String    urlTemplate,
                                                Object... params) throws InvalidParameterException,
                                                                         UserNotAuthorizedException,
                                                                         OMAGConfigurationErrorException
    {
        StringResponse restResult = this.callGetRESTCall(methodName, StringResponse.class, urlTemplate, params);

        exceptionHandler.detectAndThrowAdminExceptions(restResult);

        return restResult;
    }

    /**
     * Issue a GET REST call that returns a BasicServerPropertiesResponse object.
     *
     * @param methodName  name of the method being called.
     * @param urlTemplate template of the URL for the REST API call with place-holders for the parameters.
     * @param params      a list of parameters that are slotted into the url template.
     *
     * @return BasicServerPropertiesResponse
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws UserNotAuthorizedException the user is not authorized to make this request.
     * @throws OMAGConfigurationErrorException something went wrong with the REST call stack.
     */
    public BasicServerPropertiesResponse callBasicServerPropertiesGetRESTCall(String    methodName,
                                                                              String    urlTemplate,
                                                                              Object... params) throws InvalidParameterException,
                                                                                                       UserNotAuthorizedException,
                                                                                                       OMAGConfigurationErrorException
    {
        BasicServerPropertiesResponse restResult = this.callGetRESTCall(methodName, BasicServerPropertiesResponse.class, urlTemplate, params);

        exceptionHandler.detectAndThrowAdminExceptions(restResult);

        return restResult;
    }


    /**
     * Issue a GET REST call that returns a DedicatedTopicListResponse object.
     *
     * @param methodName  name of the method being called.
     * @param urlTemplate template of the URL for the REST API call with place-holders for the parameters.
     * @param params      a list of parameters that are slotted into the url template.
     *
     * @return DedicatedTopicListResponse
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws UserNotAuthorizedException the user is not authorized to make this request.
     * @throws OMAGConfigurationErrorException something went wrong with the REST call stack.
     */
    public DedicatedTopicListResponse callDedicatedTopicListGetRESTCall(String    methodName,
                                                                        String    urlTemplate,
                                                                        Object... params) throws InvalidParameterException,
                                                                                                 UserNotAuthorizedException,
                                                                                                 OMAGConfigurationErrorException
    {
        DedicatedTopicListResponse restResult = this.callGetRESTCall(methodName, DedicatedTopicListResponse.class, urlTemplate, params);

        exceptionHandler.detectAndThrowAdminExceptions(restResult);

        return restResult;
    }


    /**
     * Issue a GET REST call that returns a GUIDResponse object.
     *
     * @param methodName  name of the method being called.
     * @param urlTemplate template of the URL for the REST API call with place-holders for the parameters.
     * @param params      a list of parameters that are slotted into the url template.
     *
     * @return GUIDResponse
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws UserNotAuthorizedException the user is not authorized to make this request.
     * @throws OMAGConfigurationErrorException something went wrong with the REST call stack.
     */
    public GUIDResponse callGUIDGetRESTCall(String    methodName,
                                            String    urlTemplate,
                                            Object... params) throws InvalidParameterException,
                                                                     UserNotAuthorizedException,
                                                                     OMAGConfigurationErrorException
    {
        GUIDResponse restResult = this.callGetRESTCall(methodName, GUIDResponse.class, urlTemplate, params);

        exceptionHandler.detectAndThrowAdminExceptions(restResult);

        return restResult;
    }


    /**
     * Issue a GET REST call that returns a StringMapResponse object.
     *
     * @param methodName  name of the method being called.
     * @param urlTemplate template of the URL for the REST API call with place-holders for the parameters.
     * @param params      a list of parameters that are slotted into the url template.
     *
     * @return StringMapResponse
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws UserNotAuthorizedException the user is not authorized to make this request.
     * @throws OMAGConfigurationErrorException something went wrong with the REST call stack.
     */
    public StringMapResponse callStringMapGetRESTCall(String    methodName,
                                                      String    urlTemplate,
                                                      Object... params) throws InvalidParameterException,
                                                                               UserNotAuthorizedException,
                                                                               OMAGConfigurationErrorException
    {
        StringMapResponse restResult = this.callGetRESTCall(methodName, StringMapResponse.class, urlTemplate, params);

        exceptionHandler.detectAndThrowAdminExceptions(restResult);

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
     * @throws OMAGConfigurationErrorException something went wrong with the REST call stack.
     */
    public OMAGServerConfigResponse callOMAGServerConfigGetRESTCall(String    methodName,
                                                                    String    urlTemplate,
                                                                    Object... params) throws InvalidParameterException,
                                                                                             UserNotAuthorizedException,
                                                                                             OMAGConfigurationErrorException
    {
        OMAGServerConfigResponse restResult = this.callGetRESTCall(methodName, OMAGServerConfigResponse.class, urlTemplate, params);

        exceptionHandler.detectAndThrowAdminExceptions(restResult);

        return restResult;
    }


    /**
     * Issue a GET REST call that returns a OMAGServerConfigsResponse object.
     *
     * @param methodName  name of the method being called.
     * @param urlTemplate template of the URL for the REST API call with place-holders for the parameters.
     * @param params      a list of parameters that are slotted into the url template.
     *
     * @return OMAGServerConfigResponse
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws UserNotAuthorizedException the user is not authorized to make this request.
     * @throws OMAGConfigurationErrorException something went wrong with the REST call stack.
     */
    public OMAGServerConfigsResponse callGetAllServerConfigurationsRESTCall(String    methodName,
                                                                            String    urlTemplate,
                                                                            Object... params) throws InvalidParameterException,
                                                                                                     UserNotAuthorizedException,
                                                                                                     OMAGConfigurationErrorException
    {
        OMAGServerConfigsResponse restResult = this.callGetRESTCall(methodName, OMAGServerConfigsResponse.class, urlTemplate, params);

        exceptionHandler.detectAndThrowAdminExceptions(restResult);

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
     * @throws OMAGConfigurationErrorException something went wrong with the REST call stack.
     */
    @Deprecated
    public OMAGServerStatusResponse callOMAGServerStatusGetRESTCall(String    methodName,
                                                                    String    urlTemplate,
                                                                    Object... params) throws InvalidParameterException,
                                                                                             UserNotAuthorizedException,
                                                                                             OMAGConfigurationErrorException
    {
        OMAGServerStatusResponse restResult = this.callGetRESTCall(methodName, OMAGServerStatusResponse.class, urlTemplate, params);

        exceptionHandler.detectAndThrowAdminExceptions(restResult);

        return restResult;
    }


    /**
     * Issue a GET REST call that returns a OCFConnectionResponse object.
     *
     * @param methodName  name of the method being called.
     * @param urlTemplate template of the URL for the REST API call with place-holders for the parameters.
     * @param params      a list of parameters that are slotted into the url template.
     *
     * @return OCFConnectionResponse
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws UserNotAuthorizedException the user is not authorized to make this request.
     * @throws OMAGConfigurationErrorException something went wrong with the REST call stack.
     */
    public ConnectionResponse callConnectionGetRESTCall(String    methodName,
                                                        String    urlTemplate,
                                                        Object... params) throws InvalidParameterException,
                                                                                 UserNotAuthorizedException,
                                                                                 OMAGConfigurationErrorException
    {
        ConnectionResponse restResult = this.callGetRESTCall(methodName, ConnectionResponse.class, urlTemplate, params);

        exceptionHandler.detectAndThrowAdminExceptions(restResult);

        return restResult;
    }


    /**
     * Issue a GET REST call that returns a ServerTypeClassificationResponse object.
     *
     * @param methodName  name of the method being called.
     * @param urlTemplate template of the URL for the REST API call with place-holders for the parameters.
     * @param params      a list of parameters that are slotted into the url template.
     *
     * @return ServerTypeClassificationResponse
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws UserNotAuthorizedException the user is not authorized to make this request.
     * @throws OMAGConfigurationErrorException something went wrong with the REST call stack.
     */
    public ServerTypeClassificationResponse callServerClassificationGetRESTCall(String    methodName,
                                                                                String    urlTemplate,
                                                                                Object... params) throws InvalidParameterException,
                                                                                                         UserNotAuthorizedException,
                                                                                                         OMAGConfigurationErrorException
    {
        ServerTypeClassificationResponse restResult = this.callGetRESTCall(methodName, ServerTypeClassificationResponse.class, urlTemplate, params);

        exceptionHandler.detectAndThrowAdminExceptions(restResult);

        return restResult;
    }


    /**
     * Issue a GET REST call that returns a RegisteredOMAGServicesResponse object.
     *
     * @param methodName  name of the method being called.
     * @param urlTemplate template of the URL for the REST API call with place-holders for the parameters.
     * @param params      a list of parameters that are slotted into the url template.
     *
     * @return RegisteredOMAGServicesResponse
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws UserNotAuthorizedException the user is not authorized to make this request.
     * @throws OMAGConfigurationErrorException something went wrong with the REST call stack.
     */
    public RegisteredOMAGServicesResponse callRegisteredOMAGServicesGetRESTCall(String    methodName,
                                                                                String    urlTemplate,
                                                                                Object... params) throws InvalidParameterException,
                                                                                                         UserNotAuthorizedException,
                                                                                                         OMAGConfigurationErrorException
    {
        RegisteredOMAGServicesResponse restResult = this.callGetRESTCall(methodName, RegisteredOMAGServicesResponse.class, urlTemplate, params);

        exceptionHandler.detectAndThrowAdminExceptions(restResult);

        return restResult;
    }


    /**
     * Issue a GET REST call that returns a AccessServicesResponse object.
     *
     * @param methodName  name of the method being called.
     * @param urlTemplate template of the URL for the REST API call with place-holders for the parameters.
     * @param params      a list of parameters that are slotted into the url template.
     *
     * @return AccessServicesResponse
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws UserNotAuthorizedException the user is not authorized to make this request.
     * @throws OMAGConfigurationErrorException something went wrong with the REST call stack.
     */
    public AccessServicesResponse callAccessServicesGetRESTCall(String    methodName,
                                                                String    urlTemplate,
                                                                Object... params) throws InvalidParameterException,
                                                                                         UserNotAuthorizedException,
                                                                                         OMAGConfigurationErrorException
    {
        AccessServicesResponse restResult = this.callGetRESTCall(methodName, AccessServicesResponse.class, urlTemplate, params);

        exceptionHandler.detectAndThrowAdminExceptions(restResult);

        return restResult;
    }


    /**
     * Issue a GET REST call that returns a EngineHostServicesResponse object.
     *
     * @param methodName  name of the method being called.
     * @param urlTemplate template of the URL for the REST API call with place-holders for the parameters.
     * @param params      a list of parameters that are slotted into the url template.
     *
     * @return EngineHostServicesResponse
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws UserNotAuthorizedException the user is not authorized to make this request.
     * @throws OMAGConfigurationErrorException something went wrong with the REST call stack.
     */
    public EngineHostServicesResponse callEngineHostServicesGetRESTCall(String    methodName,
                                                                        String    urlTemplate,
                                                                        Object... params) throws InvalidParameterException,
                                                                                                 UserNotAuthorizedException,
                                                                                                 OMAGConfigurationErrorException
    {
        EngineHostServicesResponse restResult = this.callGetRESTCall(methodName, EngineHostServicesResponse.class, urlTemplate, params);

        exceptionHandler.detectAndThrowAdminExceptions(restResult);

        return restResult;
    }


    /**
     * Issue a GET REST call that returns a IntegrationGroupsResponse object.
     *
     * @param methodName  name of the method being called.
     * @param urlTemplate template of the URL for the REST API call with place-holders for the parameters.
     * @param params      a list of parameters that are slotted into the url template.
     *
     * @return IntegrationGroupsResponse
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws UserNotAuthorizedException the user is not authorized to make this request.
     * @throws OMAGConfigurationErrorException something went wrong with the REST call stack.
     */
    public IntegrationGroupsResponse callIntegrationGroupsGetRESTCall(String    methodName,
                                                                      String    urlTemplate,
                                                                      Object... params) throws InvalidParameterException,
                                                                                               UserNotAuthorizedException,
                                                                                               OMAGConfigurationErrorException
    {
        IntegrationGroupsResponse restResult = this.callGetRESTCall(methodName, IntegrationGroupsResponse.class, urlTemplate, params);

        exceptionHandler.detectAndThrowAdminExceptions(restResult);

        return restResult;
    }


    /**
     * Issue a GET REST call that returns a ViewServicesResponse object.
     *
     * @param methodName  name of the method being called.
     * @param urlTemplate template of the URL for the REST API call with place-holders for the parameters.
     * @param params      a list of parameters that are slotted into the url template.
     *
     * @return ViewServicesResponse
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws UserNotAuthorizedException the user is not authorized to make this request.
     * @throws OMAGConfigurationErrorException something went wrong with the REST call stack.
     */
    public ViewServicesResponse callViewServicesGetRESTCall(String    methodName,
                                                            String    urlTemplate,
                                                            Object... params) throws InvalidParameterException,
                                                                                     UserNotAuthorizedException,
                                                                                     OMAGConfigurationErrorException
    {
        ViewServicesResponse restResult = this.callGetRESTCall(methodName, ViewServicesResponse.class, urlTemplate, params);

        exceptionHandler.detectAndThrowAdminExceptions(restResult);

        return restResult;
    }


    /**
     * Issue a GET REST call that returns a response object.
     *
     * @param <T> return type
     * @param methodName  name of the method being called.
     * @param returnClass class of the response object.
     * @param urlTemplate template of the URL for the REST API call with place-holders for the parameters.
     *
     * @return response object
     * @throws OMAGConfigurationErrorException something went wrong with the REST call stack.
     */
    protected <T> T callGetRESTCallNoParams(String    methodName,
                                            Class<T>  returnClass,
                                            String    urlTemplate) throws OMAGConfigurationErrorException
    {
        try
        {
            return clientConnector.callGetRESTCall(methodName, returnClass, urlTemplate);
        }
        catch (Exception error)
        {
            exceptionHandler.logRESTCallException(serverPlatformURLRoot, methodName, error);
        }

        return null;
    }


    /**
     * Issue a GET REST call that returns a response object.
     *
     * @param <T> return type
     * @param methodName  name of the method being called.
     * @param returnClass class of the response object.
     * @param urlTemplate template of the URL for the REST API call with place-holders for the parameters.
     * @param params      a list of parameters that are slotted into the url template.
     *
     * @return response object
     * @throws OMAGConfigurationErrorException something went wrong with the REST call stack.
     */
    protected  <T> T callGetRESTCall(String    methodName,
                                     Class<T>  returnClass,
                                     String    urlTemplate,
                                     Object... params) throws OMAGConfigurationErrorException
    {
        try
        {
            return clientConnector.callGetRESTCall(methodName, returnClass, urlTemplate, params);
        }
        catch (Exception error)
        {
            exceptionHandler.logRESTCallException(serverPlatformURLRoot, methodName, error);
        }

        return null;
    }


    /**
     * Issue a POST REST call that returns a response object.  This is typically a create, update, or find with
     * complex parameters.
     *
     * @param <T> return type
     * @param methodName  name of the method being called.
     * @param returnClass class of the response object.
     * @param urlTemplate  template of the URL for the REST API call with place-holders for the parameters.
     * @param requestBody request body for the request.
     *
     * @return response object
     * @throws OMAGConfigurationErrorException something went wrong with the REST call stack.
     */
    protected <T> T callPostRESTCallNoParams(String    methodName,
                                             Class<T>  returnClass,
                                             String    urlTemplate,
                                             Object    requestBody) throws OMAGConfigurationErrorException
    {
        try
        {
            return clientConnector.callPostRESTCallNoParams(methodName, returnClass, urlTemplate, requestBody);
        }
        catch (Exception error)
        {
            exceptionHandler.logRESTCallException(serverPlatformURLRoot, methodName, error);
        }

        return null;
    }


    /**
     * Issue a POST REST call that returns a response object.  This is typically a create, update, or find with
     * complex parameters.
     *
     * @param <T> return type
     * @param methodName  name of the method being called.
     * @param returnClass class of the response object.
     * @param urlTemplate  template of the URL for the REST API call with place-holders for the parameters.
     * @param requestBody request body for the request.
     * @param params  a list of parameters that are slotted into the url template.
     *
     * @return response object
     * @throws OMAGConfigurationErrorException something went wrong with the REST call stack.
     */
    protected  <T> T callPostRESTCall(String    methodName,
                                      Class<T>  returnClass,
                                      String    urlTemplate,
                                      Object    requestBody,
                                      Object... params) throws OMAGConfigurationErrorException
    {
        try
        {
            return clientConnector.callPostRESTCall(methodName, returnClass, urlTemplate, requestBody, params);
        }
        catch (Exception error)
        {
            exceptionHandler.logRESTCallException(serverPlatformURLRoot, methodName, error);
        }

        return null;
    }


    /**
     * Issue a DELETE REST call that returns a response object.
     *
     * @param <T> return type
     * @param methodName  name of the method being called.
     * @param returnClass class of the response object.
     * @param urlTemplate  template of the URL for the REST API call with place-holders for the parameters.
     * @param requestBody request body for the request.
     *
     * @return response object
     * @throws OMAGConfigurationErrorException something went wrong with the REST call stack.
     */
    protected <T> T callDeleteRESTCallNoParams(String    methodName,
                                               Class<T>  returnClass,
                                               String    urlTemplate,
                                               Object    requestBody) throws OMAGConfigurationErrorException
    {
        try
        {
            return clientConnector.callDeleteRESTCallNoParams(methodName, returnClass, urlTemplate, requestBody);
        }
        catch (Exception error)
        {
            exceptionHandler.logRESTCallException(serverPlatformURLRoot, methodName, error);
        }

        return null;
    }


    /**
     * Issue a DELETE REST call that returns a response object.
     *
     * @param <T> return type
     * @param methodName  name of the method being called.
     * @param returnClass class of the response object.
     * @param urlTemplate  template of the URL for the REST API call with place-holders for the parameters.
     * @param requestBody request body for the request.
     * @param params  a list of parameters that are slotted into the url template.
     *
     * @return response object
     * @throws OMAGConfigurationErrorException something went wrong with the REST call stack.
     */
    protected  <T> T callDeleteRESTCall(String    methodName,
                                        Class<T>  returnClass,
                                        String    urlTemplate,
                                        Object    requestBody,
                                        Object... params) throws OMAGConfigurationErrorException
    {
        try
        {
            return clientConnector.callDeleteRESTCall(methodName, returnClass, urlTemplate, requestBody, params);
        }
        catch (Exception error)
        {
            exceptionHandler.logRESTCallException(serverPlatformURLRoot, methodName, error);
        }

        return null;
    }
}
