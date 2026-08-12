/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.restclients.jdk;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.odpi.openmetadata.adapters.connectors.restclients.RESTClientConnector;
import org.odpi.openmetadata.adapters.connectors.restclients.ffdc.RESTClientConnectorErrorCode;
import org.odpi.openmetadata.adapters.connectors.restclients.ffdc.exceptions.RESTServerException;
import org.odpi.openmetadata.frameworks.auditlog.MessageFormatter;
import org.odpi.openmetadata.frameworks.auditlog.messagesets.ExceptionMessageDefinition;
import org.odpi.openmetadata.frameworks.auditlog.requestid.RequestId;
import org.odpi.openmetadata.frameworks.auditlog.requestid.RequestIdService;
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

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * JDKRESTClientConnector is responsible for issuing calls to the server's REST APIs using the JDK's native
 * java.net.http.HttpClient rather than a third-party framework such as Spring.  It supports connection reuse
 * and HTTP/2 through java.net.http.HttpClient, and it calls Jackson directly to (de)serialize request and
 * response bodies, so that Jackson's own detailed error messages are surfaced unwrapped when a response body
 * cannot be parsed into the expected type.
 */
public class JDKRESTClientConnector extends RESTClientConnector
{
    private static final String UNRESERVED_CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789-_.~";

    private final HttpClient    httpClient;
    private final ObjectMapper  objectMapper = new ObjectMapper();
    private final MessageFormatter messageFormatter = new MessageFormatter();

    private String serverName             = null;
    private String serverPlatformURLRoot  = null;
    private String authorizationHeaderValue    = null;
    private Date   authorizationHeaderTimeout  = null;
    private long   refreshTimeInterval         = 0L;

    private static final Logger    log       = LoggerFactory.getLogger(JDKRESTClientConnector.class);
    private static final RequestId requestId = new RequestId();


    /**
     * Default constructor
     */
    public JDKRESTClientConnector()
    {
        super();

        objectMapper.findAndRegisterModules();

        httpClient = HttpClient.newBuilder()
                                .connectTimeout(Duration.ofSeconds(30))
                                .followRedirects(HttpClient.Redirect.NORMAL)
                                .build();
    }


    /**
     * Indicates that the connector is completely configured and can begin processing.
     *
     * @throws ConnectorCheckedException the connector detected a problem.
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
            this.serverName            = endpoint.getDisplayName();
        }
        else
        {
            log.error("No endpoint for connector.");

            this.serverPlatformURLRoot = null;
            this.serverName            = null;
        }

        refreshAuthorizationToken();
    }


    /**
     * Retrieve new values for the authorization header.
     *
     * @throws ConnectorCheckedException the connector detected a problem.
     */
    private void refreshAuthorizationToken() throws ConnectorCheckedException
    {
        String userId   = connectionBean.getUserId();
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
                            authorizationHeaderValue = "Bearer " + token;
                            break;
                        }
                    }
                    else if (SecretsStorePurpose.REST_BASIC_AUTHENTICATION.getName().equals(secretsStorePurpose))
                    {
                        userId   = secretsStoreConnector.getSecret(SecretsStoreCollectionProperty.USER_ID.getName());
                        password = secretsStoreConnector.getSecret(SecretsStoreCollectionProperty.CLEAR_PASSWORD.getName());

                        if ((userId != null) && (password != null))
                        {
                            authorizationHeaderValue = createBasicAuthorizationHeader(userId, password);
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
                                authorizationHeaderValue = createBasicAuthorizationHeader(userId, userAccount.getSecrets().get(SecretsStoreCollectionProperty.CLEAR_PASSWORD.getName()));
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

            authorizationHeaderValue = createBasicAuthorizationHeader(userId, password);
        }
        else
        {
            log.debug("Using no authentication to call server {} on platform {} .", this.serverName, this.serverPlatformURLRoot);
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
     * Create the value for the HTTP Authorization header for basic authentication.
     *
     * @param username userId of the caller
     * @param password password of the caller
     * @return header value
     */
    private String createBasicAuthorizationHeader(String username, String password)
    {
        String authorizationString = username + ":" + password;
        String encodedAuthorizationString = Base64.getEncoder().encodeToString(authorizationString.getBytes(StandardCharsets.UTF_8));

        return "Basic " + encodedAuthorizationString;
    }


    /**
     * Build the headers for a request: any thread-local headers set up by the caller, plus the authorization
     * header (refreshing it first if it has expired).
     *
     * @return map of header name to header value
     * @throws ConnectorCheckedException the connector detected a problem refreshing the authorization token.
     */
    private Map<String, String> getHttpHeaders() throws ConnectorCheckedException
    {
        Map<String, String> headers = new HashMap<>();

        Map<String, String> threadLocalHeaders = HTTPHeadersThreadLocal.getHeadersThreadLocal().get();

        if (threadLocalHeaders != null)
        {
            headers.putAll(threadLocalHeaders);
        }

        if ((authorizationHeaderTimeout != null) && (new Date().after(authorizationHeaderTimeout)))
        {
            refreshAuthorizationToken();
        }

        if (authorizationHeaderValue != null)
        {
            headers.put("Authorization", authorizationHeaderValue);
        }

        return headers;
    }


    /**
     * Substitute each positional {n} placeholder in the url template with the percent-encoded string form of the
     * corresponding parameter.  Only the substituted values are encoded - the literal structure of the template
     * (path separators, query separators, and so on) is left untouched.  Encoding uses RFC 3986 percent-encoding
     * of every character outside the unreserved set, rather than application/x-www-form-urlencoded conventions,
     * so that (for example) a literal '+' character in a value - such as one used in a regular expression passed
     * as searchCriteria - is preserved as a literal '+' once the server decodes the URL, rather than being
     * interpreted as a space.
     *
     * @param urlTemplate template of the URL with {0}, {1}, ... placeholders
     * @param params values to substitute into the placeholders, in order
     * @return the URL with all placeholders substituted
     */
    private String buildURL(String urlTemplate, Object... params)
    {
        if ((params == null) || (params.length == 0))
        {
            return urlTemplate;
        }

        String result = urlTemplate;

        for (int i = 0; i < params.length; i++)
        {
            String placeholder = "{" + i + "}";
            String value = (params[i] == null) ? "" : encodeValue(params[i].toString());

            result = result.replace(placeholder, value);
        }

        return result;
    }


    /**
     * Percent-encode a value for use in a URL, leaving only the RFC 3986 unreserved characters unescaped.
     *
     * @param value value to encode
     * @return encoded value
     */
    private String encodeValue(String value)
    {
        StringBuilder encoded = new StringBuilder();

        for (byte valueByte : value.getBytes(StandardCharsets.UTF_8))
        {
            char valueChar = (char) (valueByte & 0xFF);

            if (UNRESERVED_CHARACTERS.indexOf(valueChar) >= 0)
            {
                encoded.append(valueChar);
            }
            else
            {
                encoded.append('%').append(String.format("%02X", valueByte & 0xFF));
            }
        }

        return encoded.toString();
    }


    /**
     * Issue the HTTP request and return its response body as a string.  The caller is responsible for
     * deserializing the body into the expected type.
     *
     * @param methodName name of the method being called, for logging/error reporting
     * @param httpMethod HTTP method, eg GET, POST, PUT, DELETE, PATCH
     * @param urlTemplate template of the URL for the REST API call with place-holders for the parameters
     * @param requestBody request body for the request, or null if there is none
     * @param params a list of parameters that are slotted into the url template
     * @return response body, or null if the response had no body
     * @throws RESTServerException the server returned an unsuccessful HTTP status
     * @throws Exception any other problem sending the request or reading the response
     */
    private String sendAndGetBody(String    methodName,
                                  String    httpMethod,
                                  String    urlTemplate,
                                  Object    requestBody,
                                  Object... params) throws Exception
    {
        String finalURL = buildURL(urlTemplate, params);

        if (log.isDebugEnabled())
        {
            log.debug("Calling {} with URL {} using method {}.", methodName, finalURL, httpMethod);
        }

        if (requestBody instanceof RequestIdService requestIdService)
        {
            requestIdService.setRequestId(requestId.getRequestId());
        }

        Map<String, String> headers = getHttpHeaders();

        HttpRequest.BodyPublisher bodyPublisher = HttpRequest.BodyPublishers.noBody();
        String                    jsonBody      = null;

        if (requestBody != null)
        {
            jsonBody      = objectMapper.writeValueAsString(requestBody);
            bodyPublisher = HttpRequest.BodyPublishers.ofString(jsonBody, StandardCharsets.UTF_8);
        }

        HttpRequest.Builder requestBuilder = HttpRequest.newBuilder()
                                                         .uri(URI.create(finalURL))
                                                         .header("Accept", "application/json")
                                                         .method(httpMethod, bodyPublisher);

        if (jsonBody != null)
        {
            requestBuilder.header("Content-Type", "application/json");
        }

        for (Map.Entry<String, String> header : headers.entrySet())
        {
            if (header.getValue() != null)
            {
                requestBuilder.header(header.getKey(), header.getValue());
            }
        }

        HttpResponse<String> response = httpClient.send(requestBuilder.build(), HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));

        String responseBody = response.body();

        if ((response.statusCode() < 200) || (response.statusCode() >= 300))
        {
            RESTClientConnectorErrorCode errorCode = RESTClientConnectorErrorCode.UNSUCCESSFUL_HTTP_RESPONSE;
            ExceptionMessageDefinition   messageDefinition = errorCode.getMessageDefinition(methodName,
                                                                                             serverName,
                                                                                             serverPlatformURLRoot,
                                                                                             Integer.toString(response.statusCode()),
                                                                                             responseBody);
            String errorMessage = messageFormatter.getFormattedMessage(messageDefinition);

            throw new RESTServerException(messageDefinition.getHttpErrorCode(),
                                          this.getClass().getName(),
                                          methodName,
                                          errorMessage,
                                          messageDefinition.getSystemAction(),
                                          messageDefinition.getUserAction());
        }

        if (log.isDebugEnabled())
        {
            log.debug("Returning from {} with response body {}", methodName, responseBody);
        }

        return responseBody;
    }


    /**
     * Wrap an unexpected exception in the standard FFDC RESTServerException, unless it already is one.
     *
     * @param methodName name of the method being called
     * @param urlTemplate template of the URL that was being called
     * @param error the exception that was caught
     * @return exception ready to throw
     */
    private RESTServerException wrapException(String methodName, String urlTemplate, Exception error)
    {
        if (error instanceof RESTServerException restServerException)
        {
            return restServerException;
        }

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

        return new RESTServerException(messageDefinition.getHttpErrorCode(),
                                       this.getClass().getName(),
                                       methodName,
                                       errorMessage,
                                       messageDefinition.getSystemAction(),
                                       messageDefinition.getUserAction(),
                                       error);
    }


    /**
     * Deserialize a response body into the requested class.  Called directly against Jackson's ObjectMapper -
     * not wrapped by any intervening framework - so that if the body does not match returnClass, the resulting
     * exception carries Jackson's own detailed error message (the field/path/expected-type detail) unmodified.
     *
     * @param <T> class name
     * @param responseBody response body, or null
     * @param returnClass class to deserialize into
     * @return deserialized object, or null if there was no response body or no return class
     * @throws Exception Jackson deserialization problem
     */
    private <T> T deserialize(String responseBody, Class<T> returnClass) throws Exception
    {
        if ((responseBody == null) || responseBody.isEmpty() || (returnClass == null))
        {
            return null;
        }

        return objectMapper.readValue(responseBody, returnClass);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public <T> T callGetRESTCallNoParams(String    methodName,
                                         Class<T>  returnClass,
                                         String    urlTemplate) throws RESTServerException
    {
        try
        {
            return deserialize(sendAndGetBody(methodName, "GET", urlTemplate, null), returnClass);
        }
        catch (Exception error)
        {
            throw wrapException(methodName, urlTemplate, error);
        }
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public <T> T callGetRESTCall(String    methodName,
                                 Class<T>  returnClass,
                                 String    urlTemplate,
                                 Object... params) throws RESTServerException
    {
        try
        {
            return deserialize(sendAndGetBody(methodName, "GET", urlTemplate, null, params), returnClass);
        }
        catch (Exception error)
        {
            throw wrapException(methodName, urlTemplate, error);
        }
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public <T> T callPostRESTCallNoParams(String    methodName,
                                          Class<T>  returnClass,
                                          String    urlTemplate,
                                          Object    requestBody) throws RESTServerException
    {
        try
        {
            return deserialize(sendAndGetBody(methodName, "POST", urlTemplate, requestBody), returnClass);
        }
        catch (Exception error)
        {
            throw wrapException(methodName, urlTemplate, error);
        }
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public <T> T callPostRESTCall(String    methodName,
                                  Class<T>  returnClass,
                                  String    urlTemplate,
                                  Object    requestBody,
                                  Object... params) throws RESTServerException
    {
        try
        {
            return deserialize(sendAndGetBody(methodName, "POST", urlTemplate, requestBody, params), returnClass);
        }
        catch (Exception error)
        {
            throw wrapException(methodName, urlTemplate, error);
        }
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public <T> T callPutRESTCall(String    methodName,
                                 Class<T>  returnClass,
                                 String    urlTemplate,
                                 Object    requestBody,
                                 Object... params) throws RESTServerException
    {
        try
        {
            return deserialize(sendAndGetBody(methodName, "PUT", urlTemplate, requestBody, params), returnClass);
        }
        catch (Exception error)
        {
            throw wrapException(methodName, urlTemplate, error);
        }
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public <T> T callPutRESTCallNoParams(String    methodName,
                                         Class<T>  returnClass,
                                         String    urlTemplate,
                                         Object    requestBody) throws RESTServerException
    {
        try
        {
            return deserialize(sendAndGetBody(methodName, "PUT", urlTemplate, requestBody), returnClass);
        }
        catch (Exception error)
        {
            throw wrapException(methodName, urlTemplate, error);
        }
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public <T> T callDeleteRESTCallNoParams(String    methodName,
                                            Class<T>  returnClass,
                                            String    urlTemplate,
                                            Object    requestBody) throws RESTServerException
    {
        try
        {
            return deserialize(sendAndGetBody(methodName, "DELETE", urlTemplate, requestBody), returnClass);
        }
        catch (Exception error)
        {
            throw wrapException(methodName, urlTemplate, error);
        }
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public <T> T callDeleteRESTCall(String    methodName,
                                    Class<T>  returnClass,
                                    String    urlTemplate,
                                    Object    requestBody,
                                    Object... params) throws RESTServerException
    {
        try
        {
            return deserialize(sendAndGetBody(methodName, "DELETE", urlTemplate, requestBody, params), returnClass);
        }
        catch (Exception error)
        {
            throw wrapException(methodName, urlTemplate, error);
        }
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public <T> T callPatchRESTCall(String    methodName,
                                   Class<T>  returnClass,
                                   String    urlTemplate,
                                   Object    requestBody,
                                   Object... params) throws RESTServerException
    {
        try
        {
            return deserialize(sendAndGetBody(methodName, "PATCH", urlTemplate, requestBody, params), returnClass);
        }
        catch (Exception error)
        {
            throw wrapException(methodName, urlTemplate, error);
        }
    }
}
