/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.restclients.spring;

import org.apache.commons.codec.binary.Base64;
import org.odpi.openmetadata.adapters.connectors.restclients.RESTClientConnector;
import org.odpi.openmetadata.adapters.connectors.restclients.ffdc.RESTClientConnectorErrorCode;
import org.odpi.openmetadata.adapters.connectors.restclients.ffdc.exceptions.RESTServerException;
import org.odpi.openmetadata.frameworks.connectors.properties.ConnectionProperties;
import org.odpi.openmetadata.frameworks.connectors.properties.EndpointProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.Charset;


/**
 * RESTClient is responsible for issuing calls to the server's REST APIs.
 */
public class SpringRESTClientConnector extends RESTClientConnector
{
    private RestTemplate restTemplate             = new RestTemplate();
    private String       serverName               = null;
    private String       serverPlatformURLRoot    = null;
    private HttpHeaders  basicAuthorizationHeader = null;

    private static final Logger log = LoggerFactory.getLogger(SpringRESTClientConnector.class);

    /**
     * Default constructor
     */
    public SpringRESTClientConnector()
    {
        super();
    }


    /**
     * Initialize the connector.
     *
     * @param connectorInstanceId - unique id for the connector instance - useful for messages etc
     * @param connectionProperties - POJO for the configuration used to create the connector.
     */
    @Override
    public void initialize(String connectorInstanceId, ConnectionProperties connectionProperties)
    {
        super.initialize(connectorInstanceId, connectionProperties);

        EndpointProperties   endpoint             = connectionProperties.getEndpoint();

        if (endpoint != null)
        {
            this.serverPlatformURLRoot = endpoint.getAddress();
            this.serverName = endpoint.getDisplayName();
        }
        else
        {
            log.error("No endpoint for connector.");

            this.serverPlatformURLRoot = null;
            this.serverName = null;
        }

        String     userId = connectionProperties.getUserId();
        String     password = connectionProperties.getClearPassword();

        if ((userId != null) && (password != null))
        {
            log.debug("Using basic authentication to call server " + this.serverName + " on platform " + this.serverPlatformURLRoot + ".");

            basicAuthorizationHeader = this.createHeaders(userId, password);
        }
        else
        {
            log.debug("Using no authentication to call server " + this.serverName + " on platform " + this.serverPlatformURLRoot + ".");

        }
    }


    /**
     * Create the HTTP header for basic authorization.
     *
     * @param username userId of the caller
     * @param password password of the caller
     * @return HTTPHeaders object
     */
    HttpHeaders createHeaders(String username, String password)
    {
        String authorizationString = username + ":" + password;
        byte[] encodedAuthorizationString = Base64.encodeBase64(authorizationString.getBytes(Charset.forName("US-ASCII")) );
        String authHeader = "Basic " + new String( encodedAuthorizationString );

        HttpHeaders header = new HttpHeaders();

        header.set( "Authorization", authHeader );

        return header;
    }


    /**
     * Issue a GET REST call that returns a response object.
     *
     * @param methodName  name of the method being called.
     * @param returnClass class of the response object.
     * @param urlTemplate template of the URL for the REST API call with place-holders for the parameters.
     *
     * @return response object
     * @throws RESTServerException something went wrong with the REST call stack.
     */
    public  <T> T callGetRESTCallNoParams(String    methodName,
                                          Class<T>  returnClass,
                                          String    urlTemplate) throws RESTServerException
    {
        try
        {
            log.debug("Calling " + methodName + " with URL template " + urlTemplate + " and no parameters.");

            T responseObject;

            if (basicAuthorizationHeader == null)
            {
                responseObject = restTemplate.getForObject(urlTemplate, returnClass);
            }
            else
            {
                HttpEntity<?> request = new HttpEntity<>(basicAuthorizationHeader);

                ResponseEntity<T>  responseEntity = restTemplate.exchange(urlTemplate, HttpMethod.GET, request, returnClass);

                responseObject = responseEntity.getBody();
            }

            if (responseObject != null)
            {
                log.debug("Returning from " + methodName + " with response object " + responseObject.toString() + ".");
            }
            else
            {
                log.debug("Returning from " + methodName + " with no response object.");
            }

            return responseObject;
        }
        catch (Throwable error)
        {
            log.debug("Exception " + error.getClass().getName() + " with message " + error.getMessage() + " occurred during REST call for " + methodName + ".");

            RESTClientConnectorErrorCode errorCode = RESTClientConnectorErrorCode.CLIENT_SIDE_REST_API_ERROR;
            String errorMessage = errorCode.getErrorMessageId() + errorCode.getFormattedErrorMessage(error.getClass().getName(),
                                                                                                     methodName,
                                                                                                     serverPlatformURLRoot,
                                                                                                     error.getMessage());

            throw new RESTServerException(errorCode.getHTTPErrorCode(),
                                          this.getClass().getName(),
                                          methodName,
                                          errorMessage,
                                          errorCode.getSystemAction(),
                                          errorCode.getUserAction(),
                                          error);
        }
    }


    /**
     * Issue a GET REST call that returns a response object.
     *
     * @param methodName  name of the method being called.
     * @param returnClass class of the response object.
     * @param urlTemplate template of the URL for the REST API call with place-holders for the parameters.
     * @param params      a list of parameters that are slotted into the url template.
     *
     * @return response object
     * @throws RESTServerException something went wrong with the REST call stack.
     */
    public  <T> T callGetRESTCall(String    methodName,
                                  Class<T>  returnClass,
                                  String    urlTemplate,
                                  Object... params) throws RESTServerException
    {
        try
        {
            log.debug("Calling " + methodName + " with URL template " + urlTemplate + " and parameters " + params.toString() + ".");

            T  responseObject;

            if (basicAuthorizationHeader == null)
            {
                responseObject = restTemplate.getForObject(urlTemplate, returnClass, params);
            }
            else
            {
                HttpEntity<?> request = new HttpEntity<>(basicAuthorizationHeader);

                ResponseEntity<T>  responseEntity = restTemplate.exchange(urlTemplate, HttpMethod.GET, request, returnClass, params);

                responseObject = responseEntity.getBody();
            }

            if (responseObject != null)
            {
                log.debug("Returning from " + methodName + " with response object " + responseObject.toString() + ".");
            }
            else
            {
                log.debug("Returning from " + methodName + " with no response object.");
            }

            return responseObject;
        }
        catch (Throwable error)
        {
            log.debug("Exception " + error.getClass().getName() + " with message " + error.getMessage() + " occurred during REST call for " + methodName + ".");

            RESTClientConnectorErrorCode errorCode = RESTClientConnectorErrorCode.CLIENT_SIDE_REST_API_ERROR;
            String errorMessage = errorCode.getErrorMessageId() + errorCode.getFormattedErrorMessage(error.getClass().getName(),
                                                                                                     methodName,
                                                                                                     serverPlatformURLRoot,
                                                                                                     error.getMessage());

            throw new RESTServerException(errorCode.getHTTPErrorCode(),
                                          this.getClass().getName(),
                                          methodName,
                                          errorMessage,
                                          errorCode.getSystemAction(),
                                          errorCode.getUserAction(),
                                          error);
        }
    }


    /**
     * Issue a POST REST call that returns a response object.  This is typically a create, update, or find with
     * complex parameters.
     *
     * @param methodName  name of the method being called.
     * @param returnClass class of the response object.
     * @param urlTemplate  template of the URL for the REST API call with place-holders for the parameters.
     * @param requestBody request body for the request.
     *
     * @return Object
     * @throws RESTServerException something went wrong with the REST call stack.
     */
    public  <T> T callPostRESTCallNoParams(String    methodName,
                                           Class<T>  returnClass,
                                           String    urlTemplate,
                                           Object    requestBody) throws RESTServerException
    {
        try
        {
            log.debug("Calling " + methodName + " with URL template " + urlTemplate + " and no parameters.");

            T  responseObject;

            if (basicAuthorizationHeader == null)
            {
                responseObject = restTemplate.postForObject(urlTemplate, requestBody, returnClass);
            }
            else
            {
                HttpEntity<?> request;

                if (requestBody != null)
                {
                    request = new HttpEntity<>(requestBody, basicAuthorizationHeader);
                }
                else
                {
                    log.warn("Poorly formed POST call made by " + methodName);
                    request = new HttpEntity<>(basicAuthorizationHeader);
                }

                ResponseEntity<T>  responseEntity = restTemplate.exchange(urlTemplate, HttpMethod.POST, request, returnClass);

                responseObject = responseEntity.getBody();
            }

            if (responseObject != null)
            {
                log.debug("Returning from " + methodName + " with response object " + responseObject.toString() + ".");
            }
            else
            {
                log.debug("Returning from " + methodName + " with no response object.");
            }

            return responseObject;
        }
        catch (Throwable error)
        {
            log.debug("Exception " + error.getClass().getName() + " with message " + error.getMessage() + " occurred during REST call for " + methodName + ".");

            RESTClientConnectorErrorCode errorCode = RESTClientConnectorErrorCode.CLIENT_SIDE_REST_API_ERROR;
            String errorMessage = errorCode.getErrorMessageId() + errorCode.getFormattedErrorMessage(methodName,
                                                                                                     serverName,
                                                                                                     serverPlatformURLRoot,
                                                                                                     error.getMessage());

            throw new RESTServerException(errorCode.getHTTPErrorCode(),
                                          this.getClass().getName(),
                                          methodName,
                                          errorMessage,
                                          errorCode.getSystemAction(),
                                          errorCode.getUserAction(),
                                          error);
        }
    }



    /**
     * Issue a POST REST call that returns a response object.  This is typically a create, update, or find with
     * complex parameters.
     *
     * @param methodName  name of the method being called.
     * @param returnClass class of the response object.
     * @param urlTemplate  template of the URL for the REST API call with place-holders for the parameters.
     * @param requestBody request body for the request.
     * @param params  a list of parameters that are slotted into the url template.
     *
     * @return Object
     * @throws RESTServerException something went wrong with the REST call stack.
     */
    public  <T> T callPostRESTCall(String    methodName,
                                   Class<T>  returnClass,
                                   String    urlTemplate,
                                   Object    requestBody,
                                   Object... params) throws RESTServerException
    {
        try
        {
            log.debug("Calling " + methodName + " with URL template " + urlTemplate + " and parameters " + params.toString() + ".");

            T  responseObject;

            if (basicAuthorizationHeader == null)
            {
                responseObject = restTemplate.postForObject(urlTemplate, requestBody, returnClass, params);
            }
            else
            {
                HttpEntity<?> request;

                if (requestBody != null)
                {
                    request = new HttpEntity<>(requestBody, basicAuthorizationHeader);
                }
                else
                {
                    log.warn("Poorly formed POST call made by " + methodName);
                    request = new HttpEntity<>(basicAuthorizationHeader);
                }

                ResponseEntity<T>  responseEntity = restTemplate.exchange(urlTemplate, HttpMethod.POST, request, returnClass, params);

                responseObject = responseEntity.getBody();
            }

            if (responseObject != null)
            {
                log.debug("Returning from " + methodName + " with response object " + responseObject.toString() + ".");
            }
            else
            {
                log.debug("Returning from " + methodName + " with no response object.");
            }

            return responseObject;
        }
        catch (Throwable error)
        {
            log.debug("Exception " + error.getClass().getName() + " with message " + error.getMessage() + " occurred during REST call for " + methodName + ".");

            RESTClientConnectorErrorCode errorCode = RESTClientConnectorErrorCode.CLIENT_SIDE_REST_API_ERROR;
            String errorMessage = errorCode.getErrorMessageId() + errorCode.getFormattedErrorMessage(methodName,
                                                                                                     serverName,
                                                                                                     serverPlatformURLRoot,
                                                                                                     error.getMessage());

            throw new RESTServerException(errorCode.getHTTPErrorCode(),
                                              this.getClass().getName(),
                                              methodName,
                                              errorMessage,
                                              errorCode.getSystemAction(),
                                              errorCode.getUserAction(),
                                              error);
        }
    }
}
