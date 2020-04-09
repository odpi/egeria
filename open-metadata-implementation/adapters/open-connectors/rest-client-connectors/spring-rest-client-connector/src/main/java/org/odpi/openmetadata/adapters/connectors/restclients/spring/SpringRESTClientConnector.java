/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.restclients.spring;

import org.codehaus.plexus.util.Base64;
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
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.DefaultUriBuilderFactory;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;


/**
 * RESTClient is responsible for issuing calls to the server's REST APIs.
 */
public class SpringRESTClientConnector extends RESTClientConnector
{
    private RestTemplate restTemplate;
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

        /*
         * Rather than creating a RestTemplate directly, the RestTemplateBuilder is used so that the
         * uriTemplateHandler can be specified. The URI encoding is set to VALUES_ONLY so that the
         * '+' character, which is used in queryParameters conveying searchCriteria, which can be a
         * regex, is encoded as '+' and not converted to a space character.
         * Prior to this change a regex containing a '+' character would be split into two space
         * separated words. For example, the regex "name_0+7" (which would match name_07, name_007,
         * name_0007, etc) would be sent to the server as "name_0 7".
         */
        DefaultUriBuilderFactory builderFactory = new DefaultUriBuilderFactory();
        builderFactory.setEncodingMode(DefaultUriBuilderFactory.EncodingMode.VALUES_ONLY);
        restTemplate = new RestTemplate();
        restTemplate.setUriTemplateHandler(builderFactory);

        /* Ensure that the REST template always uses UTF-8 */
        List<HttpMessageConverter<?>> converters = restTemplate.getMessageConverters();
        converters.removeIf(httpMessageConverter -> httpMessageConverter instanceof StringHttpMessageConverter);
        converters.add(0, new StringHttpMessageConverter(StandardCharsets.UTF_8));

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
    private HttpHeaders createHeaders(String username, String password)
    {
        String authorizationString = username + ":" + password;
        byte[] encodedAuthorizationString = Base64.encodeBase64(authorizationString.getBytes(StandardCharsets.US_ASCII));
        String authHeader = "Basic " + new String( encodedAuthorizationString );

        HttpHeaders header = new HttpHeaders();

        header.set( "Authorization", authHeader );

        return header;
    }


    /**
     * Issue a GET REST call that returns a response object.
     *
     * @param <T> type of the return object
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
                                                                                                     urlTemplate,
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
     * Issue a GET REST call that returns a response object.
     *
     * @param <T> type of the return object
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
            log.debug("Calling " + methodName + " with URL template " + urlTemplate + " and parameters " + Arrays.toString(params) + ".");

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
                                                                                                     urlTemplate,
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
     * @param <T> type of the return object
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
            String errorMessage = errorCode.getErrorMessageId() + errorCode.getFormattedErrorMessage(error.getClass().getName(),
                                                                                                     methodName,
                                                                                                     urlTemplate,
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
     * @param <T> type of the return object
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
            log.debug("Calling " + methodName + " with URL template " + urlTemplate + " and parameters " + Arrays.toString(params) + ".");

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
            String errorMessage = errorCode.getErrorMessageId() + errorCode.getFormattedErrorMessage(error.getClass().getName(),
                                                                                                     methodName,
                                                                                                     urlTemplate,
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
     * Issue a DELETE REST call that returns a response object.
     *
     * @param <T> type of the return object
     * @param methodName  name of the method being called.
     * @param returnClass class of the response object.
     * @param urlTemplate  template of the URL for the REST API call with place-holders for the parameters.
     * @param requestBody request body for the request.
     *
     * @return Object
     * @throws RESTServerException something went wrong with the REST call stack.
     */
    public  <T> T callDeleteRESTCallNoParams(String    methodName,
                                             Class<T>  returnClass,
                                             String    urlTemplate,
                                             Object    requestBody) throws RESTServerException
    {
        try
        {
            log.debug("Calling " + methodName + " with URL template " + urlTemplate + " and no parameters.");

            T  responseObject = null;

            if (basicAuthorizationHeader == null)
            {
                restTemplate.delete(urlTemplate);
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

                ResponseEntity<T>  responseEntity = restTemplate.exchange(urlTemplate, HttpMethod.DELETE, request, returnClass);

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
            log.error("Exception " + error.getClass().getName() + " with message " + error.getMessage() + " occurred during REST call for " + methodName + ".");

            RESTClientConnectorErrorCode errorCode = RESTClientConnectorErrorCode.CLIENT_SIDE_REST_API_ERROR;
            String errorMessage = errorCode.getErrorMessageId() + errorCode.getFormattedErrorMessage(error.getClass().getName(),
                                                                                                     methodName,
                                                                                                     urlTemplate,
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
     * Issue a DELETE REST call that returns a response object.
     *
     * @param <T> type of the return object
     * @param methodName  name of the method being called.
     * @param returnClass class of the response object.
     * @param urlTemplate  template of the URL for the REST API call with place-holders for the parameters.
     * @param requestBody request body for the request.
     * @param params  a list of parameters that are slotted into the url template.
     *
     * @return Object
     * @throws RESTServerException something went wrong with the REST call stack.
     */
    public  <T> T callDeleteRESTCall(String    methodName,
                                     Class<T>  returnClass,
                                     String    urlTemplate,
                                     Object    requestBody,
                                     Object... params) throws RESTServerException
    {
        try
        {
            log.debug("Calling " + methodName + " with URL template " + urlTemplate + " and parameters " + Arrays.toString(params) + ".");

            T  responseObject = null;

            if (basicAuthorizationHeader == null)
            {
                restTemplate.delete(urlTemplate, params);
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
                    request = new HttpEntity<>(basicAuthorizationHeader);
                }

                ResponseEntity<T>  responseEntity = restTemplate.exchange(urlTemplate, HttpMethod.DELETE, request, returnClass, params);

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
            log.error("Exception " + error.getClass().getName() + " with message " + error.getMessage() + " occurred during REST call for " + methodName + ".");

            RESTClientConnectorErrorCode errorCode = RESTClientConnectorErrorCode.CLIENT_SIDE_REST_API_ERROR;
            String errorMessage = errorCode.getErrorMessageId() + errorCode.getFormattedErrorMessage(error.getClass().getName(),
                                                                                                     methodName,
                                                                                                     urlTemplate,
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
