/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.commonservices.ffdc.rest;


import org.odpi.openmetadata.adapters.connectors.restclients.RESTClientConnector;
import org.odpi.openmetadata.adapters.connectors.restclients.factory.RESTClientFactory;
import org.odpi.openmetadata.adapters.connectors.restclients.spring.SpringRESTClientConnector;
import org.odpi.openmetadata.commonservices.ffdc.OMAGCommonAuditCode;
import org.odpi.openmetadata.commonservices.ffdc.OMAGCommonErrorCode;
import org.odpi.openmetadata.commonservices.ffdc.RESTExceptionHandler;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.springframework.core.ParameterizedTypeReference;


/**
 * RESTClient is responsible for issuing calls to the OMAS REST APIs.
 */
public class FFDCRESTClientBase
{
    protected String               serverName;             /* Initialized in constructor */
    protected String               serverPlatformURLRoot;  /* Initialized in constructor */
    protected AuditLog             auditLog = null;        /* Initialized in constructor */

    protected final RESTExceptionHandler exceptionHandler = new RESTExceptionHandler();

    private final RESTClientConnector clientConnector;        /* Initialized in constructor */


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
    protected FFDCRESTClientBase(String    serverName,
                                 String    serverPlatformURLRoot,
                                 AuditLog  auditLog) throws InvalidParameterException
    {
        this(serverName, serverPlatformURLRoot);

        this.auditLog = auditLog;
    }


    /**
     * Constructor for no authentication.
     *
     * @param serverName name of the OMAG Server to call
     * @param serverPlatformURLRoot URL root of the server platform where the OMAG Server is running.
     * @throws InvalidParameterException there is a problem creating the client-side components to issue any
     * REST API calls.
     */
    protected FFDCRESTClientBase(String serverName,
                                 String serverPlatformURLRoot) throws InvalidParameterException
    {
        final String  methodName = "RESTClient(no authentication)";

        this.serverName = serverName;
        this.serverPlatformURLRoot = serverPlatformURLRoot;

        RESTClientFactory factory = new RESTClientFactory(serverName, serverPlatformURLRoot);

        try
        {
            this.clientConnector = factory.getClientConnector();
        }
        catch (Exception     error)
        {
            throw new InvalidParameterException(OMAGCommonErrorCode.NULL_LOCAL_SERVER_NAME.getMessageDefinition(serverName, error.getMessage()),
                                                this.getClass().getName(),
                                                methodName,
                                                error,
                                                "serverPlatformURLRoot or serverName");
        }
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
    protected FFDCRESTClientBase(String   serverName,
                                 String   serverPlatformURLRoot,
                                 String   userId,
                                 String   password,
                                 AuditLog auditLog) throws InvalidParameterException
    {
        this(serverName, serverPlatformURLRoot, userId, password);

        this.auditLog = auditLog;
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
    protected FFDCRESTClientBase(String serverName,
                                 String serverPlatformURLRoot,
                                 String userId,
                                 String password) throws InvalidParameterException
    {
        final String  methodName = "RESTClient(userId and password)";

        this.serverName = serverName;
        this.serverPlatformURLRoot = serverPlatformURLRoot;

        RESTClientFactory  factory = new RESTClientFactory(serverName,
                                                           serverPlatformURLRoot,
                                                           userId,
                                                           password);

        try
        {
            this.clientConnector = factory.getClientConnector();
        }
        catch (Exception     error)
        {
            throw new InvalidParameterException(OMAGCommonErrorCode.NULL_LOCAL_SERVER_NAME.getMessageDefinition(serverName, error.getMessage()),
                                                this.getClass().getName(),
                                                methodName,
                                                error,
                                                "serverPlatformURLRoot or serverName");
        }
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
     * @throws PropertyServerException something went wrong with the REST call stack.
     */
    protected <T> T callGetRESTCallNoParams(String    methodName,
                                            Class<T>  returnClass,
                                            String    urlTemplate) throws PropertyServerException
    {
        try
        {
            return clientConnector.callGetRESTCall(methodName, returnClass, urlTemplate);
        }
        catch (Exception error)
        {
            logRESTCallException(methodName, error);
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
     * @throws PropertyServerException something went wrong with the REST call stack.
     */
    protected  <T> T callGetRESTCall(String    methodName,
                                     Class<T>  returnClass,
                                     String    urlTemplate,
                                     Object... params) throws PropertyServerException
    {
        try
        {
            return clientConnector.callGetRESTCall(methodName, returnClass, urlTemplate, params);
        }
        catch (Exception error)
        {
            logRESTCallException(methodName, error);
        }

        return null;
    }

    /**
     * Issue a GET REST call that returns a response object. It's working only with {@link SpringRESTClientConnector}
     *
     * @param <T> return type
     * @param methodName  name of the method being called.
     * @param responseType class of the response object.
     * @param urlTemplate template of the URL for the REST API call with place-holders for the parameters.
     * @param params      a list of parameters that are slotted into the url template.
     *
     * @return response object
     * @throws PropertyServerException something went wrong with the REST call stack.
     */
    protected  <T> T callGetRESTCall(String                        methodName,
                                     ParameterizedTypeReference<T> responseType,
                                     String                        urlTemplate,
                                     Object...                     params) throws PropertyServerException
    {
        try
        {
            SpringRESTClientConnector clientConnector = (SpringRESTClientConnector) this.clientConnector;
            return clientConnector.callGetRESTCall(methodName, responseType, urlTemplate, params);
        }
        catch (Exception error)
        {
            logRESTCallException(methodName, error);
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
     * @throws PropertyServerException something went wrong with the REST call stack.
     */
    public <T> T callPostRESTCallNoParams(String    methodName,
                                          Class<T>  returnClass,
                                          String    urlTemplate,
                                          Object    requestBody) throws PropertyServerException
    {
        try
        {
            return clientConnector.callPostRESTCallNoParams(methodName, returnClass, urlTemplate, requestBody);
        }
        catch (Exception error)
        {
            logRESTCallException(methodName, error);
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
     * @throws PropertyServerException something went wrong with the REST call stack.
     */
    protected  <T> T callPostRESTCall(String    methodName,
                                      Class<T>  returnClass,
                                      String    urlTemplate,
                                      Object    requestBody,
                                      Object... params) throws PropertyServerException
    {
        try
        {
            return clientConnector.callPostRESTCall(methodName, returnClass, urlTemplate, requestBody, params);
        }
        catch (Exception error)
        {
            logRESTCallException(methodName, error);
        }

        return null;
    }


    /**
     * Issue a POST REST call that returns a response object.  This is typically a create, update, or find with
     * complex parameters. It's working only with {@link SpringRESTClientConnector}
     *
     * @param <T> return type
     * @param methodName  name of the method being called.
     * @param responseType class of the response for generic object.
     * @param urlTemplate  template of the URL for the REST API call with place-holders for the parameters.
     * @param requestBody request body for the request.
     * @param params  a list of parameters that are slotted into the url template.
     *
     * @return response object
     * @throws PropertyServerException something went wrong with the REST call stack.
     */
    protected  <T> T callPostRESTCall(String                        methodName,
                                      ParameterizedTypeReference<T> responseType,
                                      String                        urlTemplate,
                                      Object                        requestBody,
                                      Object...                     params) throws PropertyServerException
    {
        try
        {
            SpringRESTClientConnector clientConnector = (SpringRESTClientConnector) this.clientConnector;
            return clientConnector.callPostRESTCall(methodName, responseType, urlTemplate, requestBody, params);
        }
        catch (Exception error)
        {
            logRESTCallException(methodName, error);
        }

        return null;
    }

    /**
     * Issue a PUT REST call that returns a response object.  This is typically an update.
     *
     * @param <T> return type
     * @param methodName  name of the method being called.
     * @param returnClass class of the response object.
     * @param urlTemplate  template of the URL for the REST API call with place-holders for the parameters.
     * @param requestBody request body for the request.
     * @param params  a list of parameters that are slotted into the url template.
     *
     * @return response object
     * @throws PropertyServerException something went wrong with the REST call stack.
     */
    protected  <T> T callPutRESTCall(String    methodName,
                                     Class<T>  returnClass,
                                     String    urlTemplate,
                                     Object    requestBody,
                                     Object... params) throws PropertyServerException
    {
        try
        {
            return clientConnector.callPutRESTCall(methodName, returnClass, urlTemplate, requestBody, params);
        }
        catch (Exception error)
        {
            logRESTCallException(methodName, error);
        }

        return null;
    }

    /**
     * Issue a PUT REST call that returns a response object.  This is typically an update.
     * It's working only with {@link SpringRESTClientConnector}
     *
     * @param <T> return type
     * @param methodName  name of the method being called.
     * @param responseType class of the response for generic object.
     * @param urlTemplate  template of the URL for the REST API call with place-holders for the parameters.
     * @param requestBody request body for the request.
     * @param params  a list of parameters that are slotted into the url template.
     *
     * @return response object
     * @throws PropertyServerException something went wrong with the REST call stack.
     */
    protected  <T> T callPutRESTCall(String                        methodName,
                                     ParameterizedTypeReference<T> responseType,
                                     String                        urlTemplate,
                                     Object                        requestBody,
                                     Object...                     params) throws PropertyServerException
    {
        try
        {
            SpringRESTClientConnector clientConnector = (SpringRESTClientConnector) this.clientConnector;
            return clientConnector.callPutRESTCall(methodName, responseType, urlTemplate, requestBody, params);
        }
        catch (Exception error)
        {
            logRESTCallException(methodName, error);
        }

        return null;
    }


    /**
     * Issue a Delete REST call that returns a response object.
     *
     * @param <T> return type
     * @param methodName  name of the method being called.
     * @param returnClass class of the response object.
     * @param urlTemplate template of the URL for the REST API call with place-holders for the parameters.
     * @param requestBody body of request
     * @param params      a list of parameters that are slotted into the url template.
     *
     * @return response object
     * @throws PropertyServerException something went wrong with the REST call stack.
     */
    protected  <T> T callDeleteRESTCall(String    methodName,
                                        Class<T>  returnClass,
                                        String    urlTemplate,
                                        Object    requestBody,
                                        Object... params) throws PropertyServerException
    {
        try
        {
           return clientConnector.callDeleteRESTCall(methodName, returnClass, urlTemplate, requestBody, params);
        }
        catch (Exception error)
        {
            logRESTCallException(methodName, error);
        }

        return null;
    }


    /**
     * Issue a Delete REST call that returns a response object.
     *
     * @param <T> return type
     * @param methodName  name of the method being called.
     * @param responseType class of the response for generic object.
     * @param urlTemplate template of the URL for the REST API call with place-holders for the parameters.
     * @param params      a list of parameters that are slotted into the url template.
     *
     * @return response object
     * @throws PropertyServerException something went wrong with the REST call stack.
     */
    protected  <T> T callDeleteRESTCall(String                        methodName,
                                        ParameterizedTypeReference<T> responseType,
                                        String                        urlTemplate, Object... params) throws PropertyServerException
    {
        try
        {
            SpringRESTClientConnector clientConnector = (SpringRESTClientConnector) this.clientConnector;
            return clientConnector.callDeleteRESTCall(methodName, responseType, urlTemplate, null, params);
        }
        catch (Exception error)
        {
            logRESTCallException(methodName, error);
        }

        return null;
    }


    /**
     * Provide detailed logging for exceptions.
     *
     * @param methodName calling method
     * @param error resulting exception
     * @throws PropertyServerException wrapping exception
     */
    private void logRESTCallException(String    methodName,
                                      Exception error) throws PropertyServerException
    {
        if (auditLog != null)
        {
            auditLog.logException(methodName,
                                  OMAGCommonAuditCode.CLIENT_SIDE_REST_API_ERROR.getMessageDefinition(methodName,
                                                                                                      serverName,
                                                                                                      serverPlatformURLRoot,
                                                                                                      error.getMessage()),
                                  error);
        }

        throw new PropertyServerException(OMAGCommonErrorCode.CLIENT_SIDE_REST_API_ERROR.getMessageDefinition(methodName,
                                                                                                              serverName,
                                                                                                              serverPlatformURLRoot,
                                                                                                              error.getMessage()),
                                          this.getClass().getName(),
                                          methodName,
                                          error);
    }
}
