/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.integration.openapis;


import org.odpi.openmetadata.adapters.connectors.integration.openapis.ffdc.OpenAPIIntegrationConnectorAuditCode;
import org.odpi.openmetadata.adapters.connectors.integration.openapis.ffdc.OpenAPIIntegrationConnectorErrorCode;
import org.odpi.openmetadata.adapters.connectors.restclients.RESTClientConnector;
import org.odpi.openmetadata.adapters.connectors.restclients.factory.RESTClientFactory;
import org.odpi.openmetadata.adapters.connectors.restclients.spring.SpringRESTClientConnector;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.connectors.SecretsStoreConnector;
import org.odpi.openmetadata.frameworks.connectors.controls.SecretsStorePurpose;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.PropertyServerException;
import org.springframework.core.ParameterizedTypeReference;

import java.util.Map;


/**
 * RESTClient is responsible for issuing calls to the OMAS REST APIs.
 */
public class RESTClient
{
    protected String   serverName;             /* Initialized in constructor */
    protected String   url;                    /* Initialized in constructor */
    protected AuditLog auditLog = null;        /* Initialized in constructor */

    
    private final RESTClientConnector clientConnector;    /* Initialized in constructor */


    /**
     * Constructor for no authentication with audit log.
     *
     * @param serverName name of the OMAG Server to call
     * @param url URL root of the server platform where the OMAG Server is running.
     * @param secretsStoreProvider secrets store connector
     * @param secretsStoreLocation secrets store location
     * @param secretsStoreCollection secrets store collection
     * @param secretsStorePurpose the purpose to select
     * @param auditLog destination for log messages.
     *
     * @throws InvalidParameterException a problem creating the client-side components to issue any
     * REST API calls.
     */
    protected RESTClient(String              serverName,
                         String              url,
                         String              secretsStoreProvider,
                         String              secretsStoreLocation,
                         String              secretsStoreCollection,
                         SecretsStorePurpose secretsStorePurpose,
                         AuditLog            auditLog) throws InvalidParameterException
    {
        final String  methodName = "RESTClient(no authentication)";

        this.serverName = serverName;
        this.url = url;
        this.auditLog = auditLog;

        try
        {
            RESTClientFactory factory = new RESTClientFactory(serverName,
                                                              url,
                                                              secretsStoreProvider,
                                                              secretsStoreLocation,
                                                              secretsStoreCollection,
                                                              secretsStorePurpose.getName(),
                                                              auditLog);

            this.clientConnector = factory.getClientConnector();
        }
        catch (Exception error)
        {
            throw new InvalidParameterException(OpenAPIIntegrationConnectorErrorCode.NULL_URL.getMessageDefinition(serverName, error.getMessage()),
                                                this.getClass().getName(),
                                                methodName,
                                                error,
                                                "url or serverName");
        }
    }


    /**
     * Constructor for simple userId and password authentication with audit log.
     *
     * @param serverName name of the OMAG Server to call
     * @param url URL root of the server platform where the OMAG Server is running.
     * @param secretsStoreConnectorMap connectors to secrets stores
     * @param auditLog destination for log messages.
     * @throws InvalidParameterException a problem creating the client-side components to issue any
     * REST API calls.
     */
    protected RESTClient(String                             serverName,
                         String                             url,
                         Map<String, SecretsStoreConnector> secretsStoreConnectorMap,
                         AuditLog                           auditLog) throws InvalidParameterException
    {
        final String  methodName = "RESTClient(userId and password)";

        this.serverName = serverName;
        this.url = url;
        this.auditLog = auditLog;

        try
        {
            RESTClientFactory  factory = new RESTClientFactory(serverName,
                                                               url,
                                                               secretsStoreConnectorMap,
                                                               auditLog);

            this.clientConnector = factory.getClientConnector();
        }
        catch (Exception  error)
        {
            throw new InvalidParameterException(OpenAPIIntegrationConnectorErrorCode.NULL_URL.getMessageDefinition(serverName, error.getMessage()),
                                                this.getClass().getName(),
                                                methodName,
                                                error,
                                                "url or serverName");
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
    protected <T> T callPostRESTCallNoParams(String    methodName,
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
     * @param params      a list of parameters that are slotted into the url template.
     *
     * @return response object
     * @throws PropertyServerException something went wrong with the REST call stack.
     */
    protected  <T> T callDeleteRESTCall(String    methodName,
                                        Class<T>  returnClass,
                                        String    urlTemplate,
                                        Object... params) throws PropertyServerException
    {
        try
        {
            return clientConnector.callDeleteRESTCall(methodName, returnClass, urlTemplate, null, params);
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
                                  OpenAPIIntegrationConnectorAuditCode.CLIENT_SIDE_REST_API_ERROR.getMessageDefinition(methodName,
                                                                                                                       serverName,
                                                                                                                       url,
                                                                                                                       error.getMessage()),
                                  error);
        }

        throw new PropertyServerException(OpenAPIIntegrationConnectorErrorCode.CLIENT_SIDE_REST_API_ERROR.getMessageDefinition(methodName,
                                                                                                              serverName,
                                                                                                                               url,
                                                                                                              error.getMessage()),
                                          this.getClass().getName(),
                                          methodName,
                                          error);
    }
}
