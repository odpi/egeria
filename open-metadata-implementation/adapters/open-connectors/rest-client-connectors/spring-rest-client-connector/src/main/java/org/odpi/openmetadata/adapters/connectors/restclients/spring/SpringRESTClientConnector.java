/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.restclients.spring;

import org.codehaus.plexus.util.Base64;
import org.odpi.openmetadata.adapters.connectors.restclients.RESTClientConnector;
import org.odpi.openmetadata.adapters.connectors.restclients.ffdc.RESTClientConnectorErrorCode;
import org.odpi.openmetadata.adapters.connectors.restclients.ffdc.exceptions.RESTServerException;
import org.odpi.openmetadata.frameworks.auditlog.MessageFormatter;
import org.odpi.openmetadata.frameworks.auditlog.messagesets.ExceptionMessageDefinition;
import org.odpi.openmetadata.frameworks.connectors.SecretsStoreConnector;
import org.odpi.openmetadata.frameworks.connectors.controls.SecretsStoreCollectionProperty;
import org.odpi.openmetadata.frameworks.connectors.controls.SecretsStorePurpose;
import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectorCheckedException;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.Endpoint;
import org.odpi.openmetadata.frameworks.connectors.properties.users.UserAccount;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.tokenmanager.http.HTTPHeadersThreadLocal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.DefaultUriBuilderFactory;

import java.nio.charset.StandardCharsets;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;


/**
 * SpringRESTClientConnector is responsible for issuing calls to the server's REST APIs using Spring.
 * It is supported through a connector because there are often changes in this integration, and it saves
 * maintenance work if all Egeria clients use this connector.
 */
public class SpringRESTClientConnector extends RESTClientConnector
{
    private final RestTemplate restTemplate;

    private String      serverName                 = null;
    private String      serverPlatformURLRoot      = null;
    private HttpHeaders authorizationHeader        = null;
    private Date        authorizationHeaderTimeout = null;
    private long        refreshTimeInterval         = 0L;
    private final MessageFormatter messageFormatter = new MessageFormatter();

    private static final Logger log = LoggerFactory.getLogger(SpringRESTClientConnector.class);


    /**
     * This constructor is work in progress as part of the upgrade of Egeria to use security.
     *
     * @throws NoSuchAlgorithmException new exception added as part of the security work - no description provided yet
     * @throws KeyManagementException new exception added as part of the security work - no description provided yet
     */
    public SpringRESTClientConnector() throws NoSuchAlgorithmException, KeyManagementException
    {
        super();

        /*
         * Rather than creating a RestTemplate directly, the RestTemplateBuilder is used so that the
         * uriTemplateHandler can be specified. The URI encoding is set to VALUES_ONLY so that the
         * '+' character, which is used in queryParameters conveying searchCriteria, which can be a
         * regex, is encoded as '+' and not converted to a space character.
         * Prior to this change a regex containing a '+' character would be split into two space
         * separated words. For example, the regex "name_0+7" (which would match name_07, name_007,
         * name_0007, ...) would be sent to the server as "name_0 7".
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
     * Indicates that the connector is completely configured and can begin processing.
     *
     * @throws ConnectorCheckedException there is a problem within the connector.
     * @throws UserNotAuthorizedException the connector was disconnected before/during start
     */
    @Override
    public void start() throws ConnectorCheckedException, UserNotAuthorizedException
    {
        super.start();

        Endpoint endpoint = connectionBean.getEndpoint();

        if (endpoint != null)
        {
            this.serverPlatformURLRoot = endpoint.getNetworkAddress();
            this.serverName = endpoint.getDisplayName();
        }
        else
        {
            log.error("No endpoint for connector.");

            this.serverPlatformURLRoot = null;
            this.serverName = null;
        }

        refreshAuthorizationToken();
    }


    /**
     * Retrieve new values for the authorization header.
     *
     * @throws ConnectorCheckedException there is a problem within the connector.
     */
    private void refreshAuthorizationToken() throws ConnectorCheckedException
    {
        String userId = connectionBean.getUserId();
        String password = connectionBean.getClearPassword();

        if ((secretsStoreConnectorMap != null) && (! secretsStoreConnectorMap.isEmpty()))
        {
            log.debug("Using secrets connector to call server {} on platform {} .", this.serverName, this.serverPlatformURLRoot);

            for (String secretsStorePurpose : secretsStoreConnectorMap.keySet())
            {
                SecretsStoreConnector secretsStoreConnector = secretsStoreConnectorMap.get(secretsStorePurpose);

                if (secretsStoreConnector != null)
                {
                    refreshTimeInterval = secretsStoreConnector.getRefreshTimeInterval();

                    if (SecretsStorePurpose.REST_BEARER_TOKEN.getName().equals(secretsStorePurpose))
                    {
                        String token = secretsStoreConnector.getSecret(SecretsStoreCollectionProperty.TOKEN.getName());

                        if (token != null)
                        {
                            authorizationHeader = this.createAuthorizationHeaders(token);
                            break;
                        }
                    }
                    else if (SecretsStorePurpose.REST_BASIC_AUTHENTICATION.getName().equals(secretsStorePurpose))
                    {
                        userId = secretsStoreConnector.getSecret(SecretsStoreCollectionProperty.USER_ID.getName());
                        password = secretsStoreConnector.getSecret(SecretsStoreCollectionProperty.CLEAR_PASSWORD.getName());

                        if ((userId != null) && (password != null))
                        {
                            authorizationHeader = this.createAuthorizationHeaders(userId, password);
                            break;
                        }
                    }
                    else if (SecretsStorePurpose.USER_DIRECTORY.getName().equals(secretsStorePurpose))
                    {
                        if (userId != null)
                        {
                            UserAccount userAccount = secretsStoreConnector.getUser(userId);

                            if ((userAccount != null) && (userAccount.getSecrets() != null) && (userAccount.getSecrets().get(SecretsStoreCollectionProperty.CLEAR_PASSWORD.getName()) != null))
                            {
                                authorizationHeader = this.createAuthorizationHeaders(userId, userAccount.getSecrets().get(SecretsStoreCollectionProperty.CLEAR_PASSWORD.getName()));
                                break;
                            }
                        }
                    }
                }
            }
        }
        else if ((userId != null) && (password != null))
        {
            log.debug("Using basic authentication to call server {} on platform {} .", this.serverName, this.serverPlatformURLRoot);

            authorizationHeader = this.createAuthorizationHeaders(userId, password);
        }
        else
        {
            log.debug("Using no authentication to call server {} on platform {} .", this.serverName, this.serverPlatformURLRoot );
        }

        if (refreshTimeInterval != 0L)
        {
            long newRefreshTime = new Date().getTime() + (refreshTimeInterval * 60 * 1000);
            authorizationHeaderTimeout = new Date(newRefreshTime);
        }
        else
        {
            authorizationHeaderTimeout = null;
        }
    }


    /**
     * Create the HTTP header for basic authorization.
     *
     * @param username userId of the caller
     * @param password password of the caller
     * @return HTTPHeaders object
     */
    private HttpHeaders createAuthorizationHeaders(String username, String password)
    {
        String authorizationString = username + ":" + password;
        byte[] encodedAuthorizationString = Base64.encodeBase64(authorizationString.getBytes(StandardCharsets.US_ASCII));
        String authHeader = "Basic " + new String( encodedAuthorizationString );

        HttpHeaders header = new HttpHeaders();

        header.set( "Authorization", authHeader );

        return header;
    }


    /**
     * Create the HTTP header for token authorization.
     *
     * @param token bearer token
     * @return HTTPHeaders object
     */
    private HttpHeaders createAuthorizationHeaders(String token)
    {
        String authHeader = "Bearer " + token;

        HttpHeaders header = new HttpHeaders();

        header.set( "Authorization", authHeader );

        return header;
    }


    /**
     * Creates the http headers for each request. It checks if there are headers saved in the thread local or
     * any authorisation headers and adds them to the list.
     *
     * @return http headers
     * @throws ConnectorCheckedException there is a problem within the connector.
     */
    private HttpHeaders getHttpHeaders() throws ConnectorCheckedException
    {
        HttpHeaders headers = new HttpHeaders();

        Map<String, String> threadLocalHeaders = HTTPHeadersThreadLocal.getHeadersThreadLocal().get();

        if (threadLocalHeaders != null)
        {
            for (Map.Entry<String, String> entry : threadLocalHeaders.entrySet())
            {
                headers.set(entry.getKey(), entry.getValue());
            }
        }

        if ((authorizationHeaderTimeout != null) && (new Date().after(authorizationHeaderTimeout)))
        {
            refreshAuthorizationToken();
        }

        if (authorizationHeader != null)
        {
            headers.addAll(authorizationHeader);
        }

        return headers;
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
    @Override
    public  <T> T callGetRESTCallNoParams(String    methodName,
                                          Class<T>  returnClass,
                                          String    urlTemplate) throws RESTServerException
    {
        try
        {
            log.debug("Calling {} with URL template {} and no parameters.",  methodName, urlTemplate);

            T responseObject;

            HttpHeaders headers = getHttpHeaders();

            if (headers.isEmpty())
            {
                responseObject = restTemplate.getForObject(urlTemplate, returnClass);
            }
            else
            {
                HttpEntity<?> request = new HttpEntity<>(headers);

                ResponseEntity<T> responseEntity = restTemplate.exchange(urlTemplate, HttpMethod.GET, request, returnClass);

                responseObject = responseEntity.getBody();
            }


            if (responseObject != null)
            {
                log.debug("Returning from {} with response object {}", methodName, responseObject);
            }
            else
            {
                log.debug("Returning from {} with no response object.", methodName);
            }

            return responseObject;
        }
        catch (Exception error)
        {
            log.debug("Exception {} with message {} occurred during REST call for {}.",
                      error.getClass().getName(),
                      error.getMessage(),
                      methodName);

            RESTClientConnectorErrorCode errorCode = RESTClientConnectorErrorCode.CLIENT_SIDE_REST_API_ERROR;
            ExceptionMessageDefinition   messageDefinition = errorCode.getMessageDefinition(error.getClass().getName(),
                                                                                            methodName,
                                                                                            urlTemplate,
                                                                                            serverName,
                                                                                            serverPlatformURLRoot,
                                                                                            error.getMessage());
            String errorMessage = messageFormatter.getFormattedMessage(messageDefinition);

            throw new RESTServerException(messageDefinition.getHttpErrorCode(),
                                          this.getClass().getName(),
                                          methodName,
                                          errorMessage,
                                          messageDefinition.getSystemAction(),
                                          messageDefinition.getUserAction(),
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
    @Override
    public  <T> T callGetRESTCall(String    methodName,
                                  Class<T>  returnClass,
                                  String    urlTemplate,
                                  Object... params) throws RESTServerException
    {
        try
        {
            if (log.isDebugEnabled())
            {
                //avoid calling Arrays.toString if not debug level
                log.debug("Calling {} with URL template {} and parameters {}.",
                          methodName,
                          urlTemplate,
                          Arrays.toString(params));
            }

            T  responseObject;

            HttpHeaders headers = getHttpHeaders();

            if (headers.isEmpty())
            {
                responseObject = restTemplate.getForObject(urlTemplate, returnClass, params);
            }
            else
            {
                HttpEntity<?> request = new HttpEntity<>(headers);

                ResponseEntity<T> responseEntity = restTemplate.exchange(urlTemplate, HttpMethod.GET, request, returnClass, params);

                responseObject = responseEntity.getBody();
            }


            if (responseObject != null)
            {
                log.debug("Returning from {} with response object {}", methodName, responseObject);
            }
            else
            {
                log.debug("Returning from {} with no response object.", methodName);
            }


            return responseObject;
        }
        catch (Exception error)
        {
            log.debug("Exception {} with message {} occurred during REST call for {}.",
                      error.getClass().getName(),
                      error.getMessage(),
                      methodName);

            RESTClientConnectorErrorCode errorCode = RESTClientConnectorErrorCode.CLIENT_SIDE_REST_API_ERROR;
            ExceptionMessageDefinition messageDefinition = errorCode.getMessageDefinition(error.getClass().getName(),
                                                                                          methodName,
                                                                                          urlTemplate,
                                                                                          serverName,
                                                                                          serverPlatformURLRoot,
                                                                                          error.getMessage());
            String errorMessage = messageFormatter.getFormattedMessage(messageDefinition);

            throw new RESTServerException(messageDefinition.getHttpErrorCode(),
                                          this.getClass().getName(),
                                          methodName,
                                          errorMessage,
                                          messageDefinition.getSystemAction(),
                                          messageDefinition.getUserAction(),
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
    @Override
    public  <T> T callPostRESTCallNoParams(String    methodName,
                                           Class<T>  returnClass,
                                           String    urlTemplate,
                                           Object    requestBody) throws RESTServerException
    {
        try
        {
            log.debug("Calling {} with URL template {} and no parameters.",  methodName, urlTemplate);

            T  responseObject;

            HttpHeaders headers = getHttpHeaders();

            if (headers.isEmpty())
            {
                responseObject = restTemplate.postForObject(urlTemplate, requestBody, returnClass);
            }
            else
            {
                HttpEntity<?> request;

                if (requestBody != null)
                {
                    request = new HttpEntity<>(requestBody, headers);
                }
                else
                {
                    log.warn("Poorly formed POST call made by {}.", methodName);
                    request = new HttpEntity<>(headers);
                }

                ResponseEntity<T>  responseEntity = restTemplate.exchange(urlTemplate, HttpMethod.POST, request, returnClass);

                responseObject = responseEntity.getBody();
            }


            if (responseObject != null)
            {
                log.debug("Returning from {} with response object {}", methodName, responseObject);
            }
            else
            {
                log.debug("Returning from {} with no response object.", methodName);
            }



            return responseObject;
        }
        catch (Exception error)
        {
            log.debug("Exception {} with message {} occurred during REST call for {}.",
                      error.getClass().getName(),
                      error.getMessage(),
                      methodName);

            RESTClientConnectorErrorCode errorCode = RESTClientConnectorErrorCode.CLIENT_SIDE_REST_API_ERROR;
            ExceptionMessageDefinition   messageDefinition = errorCode.getMessageDefinition(error.getClass().getName(),
                                                                                            methodName,
                                                                                            urlTemplate,
                                                                                            serverName,
                                                                                            serverPlatformURLRoot,
                                                                                            error.getMessage());
            String errorMessage = messageFormatter.getFormattedMessage(messageDefinition);

            throw new RESTServerException(messageDefinition.getHttpErrorCode(),
                                          this.getClass().getName(),
                                          methodName,
                                          errorMessage,
                                          messageDefinition.getSystemAction(),
                                          messageDefinition.getUserAction(),
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
    @Override
    public  <T> T callPostRESTCall(String    methodName,
                                   Class<T>  returnClass,
                                   String    urlTemplate,
                                   Object    requestBody,
                                   Object... params) throws RESTServerException
    {
        try
        {
            if (log.isDebugEnabled())
            {
                //avoid calling Arrays.toString if not debug level
                log.debug("Calling {} with URL template {} and parameters {}.",
                          methodName,
                          urlTemplate,
                          Arrays.toString(params));
            }
            T  responseObject;

            HttpHeaders headers = getHttpHeaders();

            if (headers.isEmpty())
            {
                responseObject = restTemplate.postForObject(urlTemplate, requestBody, returnClass, params);
            }
            else
            {
                HttpEntity<?> request;

                if (requestBody != null)
                {
                    request = new HttpEntity<>(requestBody, headers);
                }
                else
                {
                    log.warn("Poorly formed POST call made by {}.", methodName);
                    request = new HttpEntity<>(headers);
                }

                ResponseEntity<T>  responseEntity = restTemplate.exchange(urlTemplate, HttpMethod.POST, request, returnClass, params);

                responseObject = responseEntity.getBody();
            }


            if (responseObject != null)
            {
                log.debug("Returning from {} with response object {}", methodName, responseObject);
            }
            else
            {
                log.debug("Returning from {} with no response object.", methodName);
            }

            return responseObject;
        }
        catch (Exception error)
        {
            log.debug("Exception {} with message {} occurred during REST call for {}.",
                      error.getClass().getName(),
                      error.getMessage(),
                      methodName);

            RESTClientConnectorErrorCode errorCode = RESTClientConnectorErrorCode.CLIENT_SIDE_REST_API_ERROR;
            ExceptionMessageDefinition   messageDefinition = errorCode.getMessageDefinition(error.getClass().getName(),
                                                                                            methodName,
                                                                                            urlTemplate,
                                                                                            serverName,
                                                                                            serverPlatformURLRoot,
                                                                                            error.getMessage());
            String errorMessage = messageFormatter.getFormattedMessage(messageDefinition);

            throw new RESTServerException(messageDefinition.getHttpErrorCode(),
                                          this.getClass().getName(),
                                          methodName,
                                          errorMessage,
                                          messageDefinition.getSystemAction(),
                                          messageDefinition.getUserAction(),
                                          error);
        }
    }


    /**
     * Issue a PUT REST call that returns a response object. This is typically an update.
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
    @Override
    public  <T> T callPutRESTCall(String    methodName,
                                  Class<T>  returnClass,
                                  String    urlTemplate,
                                  Object    requestBody,
                                  Object... params) throws RESTServerException
    {
        try
        {
            if (log.isDebugEnabled())
            {
                log.debug("Calling {} with URL template {} and parameters {}.",
                          methodName,
                          urlTemplate,
                          Arrays.toString(params));
            }

            HttpEntity<?> request;
            HttpHeaders httpHeaders = getHttpHeaders();

            if (httpHeaders == null)
            {
                request = new HttpEntity<>(requestBody);
            }
            else
            {
                request = new HttpEntity<>(requestBody, httpHeaders);
            }

            ResponseEntity<T> responseEntity = restTemplate.exchange(urlTemplate, HttpMethod.PUT, request, returnClass, params);
            T responseObject = responseEntity.getBody();


            if (responseObject != null)
            {
                log.debug("Returning from {} with response object {}", methodName, responseObject);
            }
            else
            {
                log.debug("Returning from {} with no response object.", methodName);
            }

            return responseObject;
        }
        catch (Exception error)
        {
            log.debug("Exception {} with message {} occurred during REST call for {}.",
                      error.getClass().getName(),
                      error.getMessage(),
                      methodName);

            RESTClientConnectorErrorCode errorCode = RESTClientConnectorErrorCode.CLIENT_SIDE_REST_API_ERROR;
            ExceptionMessageDefinition   messageDefinition = errorCode.getMessageDefinition(error.getClass().getName(),
                                                                                            methodName,
                                                                                            urlTemplate,
                                                                                            serverName,
                                                                                            serverPlatformURLRoot,
                                                                                            error.getMessage());
            String errorMessage = messageFormatter.getFormattedMessage(messageDefinition);

            throw new RESTServerException(messageDefinition.getHttpErrorCode(),
                                          this.getClass().getName(),
                                          methodName,
                                          errorMessage,
                                          messageDefinition.getSystemAction(),
                                          messageDefinition.getUserAction(),
                                          error);
        }
    }


    /**
     * Issue a PUT REST call that returns a response object. This is typically an update.
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
    @Override
    public  <T> T callPutRESTCallNoParams(String    methodName,
                                          Class<T>  returnClass,
                                          String    urlTemplate,
                                          Object    requestBody) throws RESTServerException
    {
        try
        {
            HttpEntity<?> request;
            HttpHeaders httpHeaders = getHttpHeaders();

            if (httpHeaders == null)
            {
                request = new HttpEntity<>(requestBody);
            }
            else
            {
                request = new HttpEntity<>(requestBody, httpHeaders);
            }

            ResponseEntity<T> responseEntity = restTemplate.exchange(urlTemplate, HttpMethod.PUT, request, returnClass);
            T responseObject = responseEntity.getBody();


            if (responseObject != null)
            {
                log.debug("Returning from {} with response object {}", methodName, responseObject);
            }
            else
            {
                log.debug("Returning from {} with no response object.", methodName);
            }

            return responseObject;
        }
        catch (Exception error)
        {
            log.debug("Exception {} with message {} occurred during REST call for {}.",
                      error.getClass().getName(),
                      error.getMessage(),
                      methodName);

            RESTClientConnectorErrorCode errorCode = RESTClientConnectorErrorCode.CLIENT_SIDE_REST_API_ERROR;
            ExceptionMessageDefinition   messageDefinition = errorCode.getMessageDefinition(error.getClass().getName(),
                                                                                            methodName,
                                                                                            urlTemplate,
                                                                                            serverName,
                                                                                            serverPlatformURLRoot,
                                                                                            error.getMessage());
            String errorMessage = messageFormatter.getFormattedMessage(messageDefinition);

            throw new RESTServerException(messageDefinition.getHttpErrorCode(),
                                          this.getClass().getName(),
                                          methodName,
                                          errorMessage,
                                          messageDefinition.getSystemAction(),
                                          messageDefinition.getUserAction(),
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
    @Override
    public  <T> T callDeleteRESTCallNoParams(String    methodName,
                                             Class<T>  returnClass,
                                             String    urlTemplate,
                                             Object    requestBody) throws RESTServerException
    {
        try
        {
            log.debug("Calling {} with URL template {} and no parameters.",  methodName, urlTemplate);

            T  responseObject = null;

            HttpHeaders headers = getHttpHeaders();

            if (headers.isEmpty())
            {
                restTemplate.delete(urlTemplate);
            }
            else
            {
                HttpEntity<?> request;

                if (requestBody != null)
                {
                    request = new HttpEntity<>(requestBody, headers);
                }
                else
                {
                    log.warn("Poorly formed POST call made by {}.", methodName);
                    request = new HttpEntity<>(headers);
                }

                ResponseEntity<T>  responseEntity = restTemplate.exchange(urlTemplate, HttpMethod.DELETE, request, returnClass);

                responseObject = responseEntity.getBody();
            }


            if (responseObject != null)
            {
                log.debug("Returning from {} with response object {}", methodName, responseObject);
            }
            else
            {
                log.debug("Returning from {} with no response object.", methodName);
            }

            return responseObject;
        }
        catch (Exception error)
        {
            log.debug("Exception {} with message {} occurred during REST call for {}.",
                      error.getClass().getName(),
                      error.getMessage(),
                      methodName);

            RESTClientConnectorErrorCode errorCode = RESTClientConnectorErrorCode.CLIENT_SIDE_REST_API_ERROR;
            ExceptionMessageDefinition   messageDefinition = errorCode.getMessageDefinition(error.getClass().getName(),
                                                                                            methodName,
                                                                                            urlTemplate,
                                                                                            serverName,
                                                                                            serverPlatformURLRoot,
                                                                                            error.getMessage());
            String errorMessage = messageFormatter.getFormattedMessage(messageDefinition);

            throw new RESTServerException(messageDefinition.getHttpErrorCode(),
                                          this.getClass().getName(),
                                          methodName,
                                          errorMessage,
                                          messageDefinition.getSystemAction(),
                                          messageDefinition.getUserAction(),
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
    @Override
    public  <T> T callDeleteRESTCall(String    methodName,
                                     Class<T>  returnClass,
                                     String    urlTemplate,
                                     Object    requestBody,
                                     Object... params) throws RESTServerException
    {
        try
        {
            if (log.isDebugEnabled())
            {
                //avoid calling Arrays.toString if not debug level
                log.debug("Calling {} with URL template {} and parameters {}.",
                          methodName,
                          urlTemplate,
                          Arrays.toString(params));
            }

            HttpEntity<?> request;
            HttpHeaders httpHeaders = getHttpHeaders();

            if (httpHeaders == null)
            {
                request = new HttpEntity<>(requestBody);
            }
            else
            {
                request = new HttpEntity<>(requestBody, httpHeaders);
            }

            ResponseEntity<T>  responseEntity = restTemplate.exchange(urlTemplate, HttpMethod.DELETE, request, returnClass, params);
            T  responseObject = responseEntity.getBody();

            if (responseObject != null)
            {
                log.debug("Returning from {} with response object {}", methodName, responseObject);
            }
            else
            {
                log.debug("Returning from {} with no response object.", methodName);
            }

            return responseObject;
        }
        catch (Exception error)
        {
            log.error("Exception " + error.getClass().getName() + " with message " + error.getMessage() + " occurred during REST call for " + methodName + ".");

            RESTClientConnectorErrorCode errorCode = RESTClientConnectorErrorCode.CLIENT_SIDE_REST_API_ERROR;
            ExceptionMessageDefinition   messageDefinition = errorCode.getMessageDefinition(error.getClass().getName(),
                                                                                            methodName,
                                                                                            urlTemplate,
                                                                                            serverName,
                                                                                            serverPlatformURLRoot,
                                                                                            error.getMessage());
            String errorMessage = messageFormatter.getFormattedMessage(messageDefinition);

            throw new RESTServerException(messageDefinition.getHttpErrorCode(),
                                          this.getClass().getName(),
                                          methodName,
                                          errorMessage,
                                          messageDefinition.getSystemAction(),
                                          messageDefinition.getUserAction(),
                                          error);
        }
    }


    /**
     * Issue a PATCH REST call that returns a response object.
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
    public  <T> T callPatchRESTCall(String    methodName,
                                    Class<T>  returnClass,
                                    String    urlTemplate,
                                    Object    requestBody,
                                    Object... params) throws RESTServerException
    {
        try
        {
            if (log.isDebugEnabled())
            {
                //avoid calling Arrays.toString if not debug level
                log.debug("Calling {} with URL template {} and parameters {}.",
                          methodName,
                          urlTemplate,
                          Arrays.toString(params));
            }

            HttpEntity<?> request;
            HttpHeaders httpHeaders = getHttpHeaders();

            if (httpHeaders == null)
            {
                request = new HttpEntity<>(requestBody);
            }
            else
            {
                request = new HttpEntity<>(requestBody, httpHeaders);
            }

            ResponseEntity<T>  responseEntity = restTemplate.exchange(urlTemplate, HttpMethod.PATCH, request, returnClass, params);
            T  responseObject = responseEntity.getBody();

            if (responseObject != null)
            {
                log.debug("Returning from {} with response object {}", methodName, responseObject);
            }
            else
            {
                log.debug("Returning from {} with no response object.", methodName);
            }

            return responseObject;
        }
        catch (Exception error)
        {
            log.error("Exception " + error.getClass().getName() + " with message " + error.getMessage() + " occurred during REST call for " + methodName + ".");

            RESTClientConnectorErrorCode errorCode = RESTClientConnectorErrorCode.CLIENT_SIDE_REST_API_ERROR;
            ExceptionMessageDefinition   messageDefinition = errorCode.getMessageDefinition(error.getClass().getName(),
                                                                                            methodName,
                                                                                            urlTemplate,
                                                                                            serverName,
                                                                                            serverPlatformURLRoot,
                                                                                            error.getMessage());
            String errorMessage = messageFormatter.getFormattedMessage(messageDefinition);

            throw new RESTServerException(messageDefinition.getHttpErrorCode(),
                                          this.getClass().getName(),
                                          methodName,
                                          errorMessage,
                                          messageDefinition.getSystemAction(),
                                          messageDefinition.getUserAction(),
                                          error);
        }
    }


    /**
     * Issue a POST REST call that returns a response object.  This is typically a create, update, or find with
     * complex parameters.
     *
     * @param <T> type of the return object
     * @param methodName  name of the method being called.
     * @param responseType class of the response for generic object.
     * @param urlTemplate  template of the URL for the REST API call with place-holders for the parameters.
     * @param requestBody request body for the request.
     * @param params  a list of parameters that are slotted into the url template.
     *
     * @return Object
     * @throws RESTServerException something went wrong with the REST call stack.
     */
    public <T> T callPostRESTCall(String methodName,
                                  ParameterizedTypeReference<T> responseType,
                                  String urlTemplate,
                                  Object requestBody,
                                  Object... params) throws RESTServerException
    {
        try
        {
            if (log.isDebugEnabled())
            {
                //avoid calling Arrays.toString if not debug level
                log.debug("Calling {} with URL template {} and parameters {}.",
                          methodName,
                          urlTemplate,
                          Arrays.toString(params)
                );
            }

            T responseObject;

            HttpEntity<?> request;

            HttpHeaders headers = getHttpHeaders();

            if (headers.isEmpty())
            {
                request = new HttpEntity<>(requestBody);
            }
            else
            {
                if (requestBody != null)
                {
                    request = new HttpEntity<>(requestBody, headers);
                }
                else
                {
                    log.warn("Poorly formed POST call made by {}.", methodName);
                    request = new HttpEntity<>(headers);
                }
            }
            ResponseEntity<T> responseEntity = restTemplate.exchange(urlTemplate, HttpMethod.POST, request, responseType, params);

            responseObject = responseEntity.getBody();


            if (responseObject != null)
            {
                log.debug("Returning from {} with response object {}", methodName, responseObject);
            }
            else
            {
                log.debug("Returning from {} with no response object.", methodName);
            }

            return responseObject;
        }
        catch (Exception error)
        {
            log.debug("Exception {} with message {} occurred during REST call for {}.",
                      error.getClass().getName(),
                      error.getMessage(),
                      methodName);

            RESTClientConnectorErrorCode errorCode = RESTClientConnectorErrorCode.CLIENT_SIDE_REST_API_ERROR;
            ExceptionMessageDefinition   messageDefinition = errorCode.getMessageDefinition(error.getClass().getName(),
                                                                                            methodName,
                                                                                            urlTemplate,
                                                                                            serverName,
                                                                                            serverPlatformURLRoot,
                                                                                            error.getMessage());
            String errorMessage = messageFormatter.getFormattedMessage(messageDefinition);

            throw new RESTServerException(messageDefinition.getHttpErrorCode(),
                                          this.getClass().getName(),
                                          methodName,
                                          errorMessage,
                                          messageDefinition.getSystemAction(),
                                          messageDefinition.getUserAction(),
                                          error);
        }
    }


    /**
     * Issue a GET REST call that returns a response object.
     *
     * @param <T> type of the return object
     * @param methodName  name of the method being called.
     * @param responseType class of the response for generic object.
     * @param urlTemplate template of the URL for the REST API call with place-holders for the parameters.
     * @param params      a list of parameters that are slotted into the url template.
     *
     * @return response object
     * @throws RESTServerException something went wrong with the REST call stack.
     */
    public <T> T callGetRESTCall(String methodName,
                                 ParameterizedTypeReference<T> responseType,
                                 String urlTemplate,
                                 Object... params) throws RESTServerException
    {
        try
        {
            if(log.isDebugEnabled())
            {
                //avoid calling Arrays.toString if not debug level
                log.debug("Calling {} with URL template {} and parameters {}.",
                          methodName,
                          urlTemplate,
                          Arrays.toString(params)
                );
            }

            T responseObject;

            HttpEntity<?> request;

            HttpHeaders headers = getHttpHeaders();

            if (headers.isEmpty())
            {
                request = HttpEntity.EMPTY;
            }
            else
            {
                request = new HttpEntity<>(headers);
            }

            ResponseEntity<T> responseEntity = restTemplate.exchange(urlTemplate, HttpMethod.GET, request, responseType, params);

            responseObject = responseEntity.getBody();

            if (responseObject != null)
            {
                log.debug("Returning from {} with response object {}", methodName, responseObject);
            }
            else
            {
                log.debug("Returning from {} with no response object.", methodName);
            }

            return responseObject;
        }
        catch (Exception error)
        {
            log.debug("Exception {} with message {} occurred during REST call for {}.",
                      error.getClass().getName(),
                      error.getMessage(),
                      methodName);

            RESTClientConnectorErrorCode errorCode = RESTClientConnectorErrorCode.CLIENT_SIDE_REST_API_ERROR;
            ExceptionMessageDefinition   messageDefinition = errorCode.getMessageDefinition(error.getClass().getName(),
                                                                                            methodName,
                                                                                            urlTemplate,
                                                                                            serverName,
                                                                                            serverPlatformURLRoot,
                                                                                            error.getMessage());
            String errorMessage = messageFormatter.getFormattedMessage(messageDefinition);

            throw new RESTServerException(messageDefinition.getHttpErrorCode(),
                                          this.getClass().getName(),
                                          methodName,
                                          errorMessage,
                                          messageDefinition.getSystemAction(),
                                          messageDefinition.getUserAction(),
                                          error);
        }
    }


    /**
     * Issue a DELETE REST call that returns a response object.
     *
     * @param <T> type of the return object
     * @param methodName  name of the method being called.
     * @param responseType class of the response for generic object.
     * @param urlTemplate  template of the URL for the REST API call with place-holders for the parameters.
     * @param requestBody request body for the request.
     * @param params  a list of parameters that are slotted into the url template.
     *
     * @return Object
     * @throws RESTServerException something went wrong with the REST call stack.
     */
    public  <T> T callDeleteRESTCall(String    methodName,
                                     ParameterizedTypeReference<T> responseType,
                                     String    urlTemplate,
                                     Object    requestBody,
                                     Object... params) throws RESTServerException
    {
        try
        {
            if (log.isDebugEnabled())
            {
                //avoid calling Arrays.toString if not debug level
                log.debug("Calling {} with URL template {} and parameters {}.",
                          methodName,
                          urlTemplate,
                          Arrays.toString(params));
            }

            HttpEntity<?> request;
            HttpHeaders httpHeaders = getHttpHeaders();

            if (httpHeaders == null)
            {
                request = new HttpEntity<>(requestBody);
            }
            else
            {
                request = new HttpEntity<>(requestBody, httpHeaders);
            }

            ResponseEntity<T> responseEntity = restTemplate.exchange(urlTemplate, HttpMethod.DELETE, request, responseType, params);
            T responseObject = responseEntity.getBody();

            if (responseObject != null)
            {
                log.debug("Returning from {} with response object {}", methodName, responseObject);
            }
            else
            {
                log.debug("Returning from {} with no response object.", methodName);
            }

            return responseObject;
        }
        catch (Exception error)
        {
            log.error("Exception " + error.getClass().getName() + " with message " + error.getMessage() + " occurred during REST call for " + methodName + ".");

            RESTClientConnectorErrorCode errorCode = RESTClientConnectorErrorCode.CLIENT_SIDE_REST_API_ERROR;
            ExceptionMessageDefinition   messageDefinition = errorCode.getMessageDefinition(error.getClass().getName(),
                                                                                            methodName,
                                                                                            urlTemplate,
                                                                                            serverName,
                                                                                            serverPlatformURLRoot,
                                                                                            error.getMessage());
            String errorMessage = messageFormatter.getFormattedMessage(messageDefinition);

            throw new RESTServerException(messageDefinition.getHttpErrorCode(),
                                          this.getClass().getName(),
                                          methodName,
                                          errorMessage,
                                          messageDefinition.getSystemAction(),
                                          messageDefinition.getUserAction(),
                                          error);
        }
    }


    /**
     * Issue a PUT REST call that returns a response object. This is typically an update.
     *
     * @param <T> type of the return object
     * @param methodName  name of the method being called.
     * @param responseType class of the response for generic object.
     * @param urlTemplate  template of the URL for the REST API call with place-holders for the parameters.
     * @param requestBody request body for the request.
     * @param params  a list of parameters that are slotted into the url template.
     *
     * @return Object
     * @throws RESTServerException something went wrong with the REST call stack.
     */
    public  <T> T callPutRESTCall(String    methodName,
                                  ParameterizedTypeReference<T> responseType,
                                  String    urlTemplate,
                                  Object    requestBody,
                                  Object... params) throws RESTServerException
    {
        try
        {
            if(log.isDebugEnabled())
            {
                //avoid calling Arrays.toString if not debug level
                log.debug("Calling {} with URL template {} and parameters {}.",
                          methodName,
                          urlTemplate,
                          Arrays.toString(params)
                );
            }

            HttpEntity<?> request;
            HttpHeaders httpHeaders = getHttpHeaders();

            if (httpHeaders == null)
            {
                request = new HttpEntity<>(requestBody);
            }
            else
            {
                request = new HttpEntity<>(requestBody, httpHeaders);
            }

            ResponseEntity<T> responseEntity = restTemplate.exchange(urlTemplate, HttpMethod.PUT, request, responseType, params);
            T responseObject = responseEntity.getBody();


            if (responseObject != null)
            {
                log.debug("Returning from {} with response object {}", methodName, responseObject);
            }
            else
            {
                log.debug("Returning from {} with no response object.", methodName);
            }

            return responseObject;
        }
        catch (Exception error)
        {
            log.debug("Exception {} with message {} occurred during REST call for {}.",
                      error.getClass().getName(),
                      error.getMessage(),
                      methodName);

            RESTClientConnectorErrorCode errorCode = RESTClientConnectorErrorCode.CLIENT_SIDE_REST_API_ERROR;
            ExceptionMessageDefinition   messageDefinition = errorCode.getMessageDefinition(error.getClass().getName(),
                                                                                            methodName,
                                                                                            urlTemplate,
                                                                                            serverName,
                                                                                            serverPlatformURLRoot,
                                                                                            error.getMessage());
            String errorMessage = messageFormatter.getFormattedMessage(messageDefinition);

            throw new RESTServerException(messageDefinition.getHttpErrorCode(),
                                          this.getClass().getName(),
                                          methodName,
                                          errorMessage,
                                          messageDefinition.getSystemAction(),
                                          messageDefinition.getUserAction(),
                                          error);
        }
    }
}
