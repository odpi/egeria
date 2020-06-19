/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.restclients.spring;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.conn.ssl.DefaultHostnameVerifier;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
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
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.DefaultUriBuilderFactory;

import javax.net.ssl.*;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
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

    // These are distinct from SERVER (in the context of a socket) settings
    // As we may want to make distinction (for mutual ssl). here is only client
    private static final String ENV_SSL_VERIFY = "EGERIA_SSL_CLIENT_VERIFY";
    private static final String SYSPROP_SSL_VERIFY = "egeria.ssl.client.verify";
    private static boolean SSL_VERIFY=false;

    private static final String ENV_SSL_TRUSTSTORE = "EGERIA_SSL_CLIENT_TRUSTSTORE";
    private static final String SYSPROP_SSL_SERVER_TRUSTSTORE = "server.ssl.trust-store";
    private static final String SYSPROP_SSL_TRUSTSTORE = "egeria.ssl.client.truststore";
    private static final String SYSPROP_SSL_JVM_TRUSTSTORE = "javax.net.ssl.trustStore";
    private static String truststore=null; // use to check a received certificate

    private static final String ENV_SSL_TRUSTSTOREPASS = "EGERIA_SSL_CLIENT_TRUSTSTOREPASS";
    private static final String SYSPROP_SSL_SERVER_TRUSTSTOREPASS = "server.ssl.trust-store-password";
    private static final String SYSPROP_SSL_TRUSTSTOREPASS = "egeria.ssl.client.truststorepass";
    private static final String SYSPROP_SSL_JVM_TRUSTSTOREPASS = "javax.net.ssl.trustStorePassword";
    private static String truststorePassword=null; // auth for above

    private static final String ENV_SSL_KEYSTORE = "EGERIA_SSL_CLIENT_KEYSTORE";
    private static final String SYSPROP_SSL_SERVER_KEYSTORE = "server.ssl.key-store";
    private static final String SYSPROP_SSL_KEYSTORE = "egeria.ssl.client.keystore";
    private static final String SYSPROP_SSL_JVM_KEYSTORE = "javax.net.ssl.keyStore";
    private static String keystore=null; // use to check a received certificate

    private static final String ENV_SSL_KEYSTOREPASS = "EGERIA_SSL_CLIENT_KEYSTOREPASS";
    private static final String SYSPROP_SSL_SERVER_KEYSTOREPASS = "server.ssl.key-store-password";
    private static final String SYSPROP_SSL_KEYSTOREPASS = "egeria.ssl.client.keystorepass";
    private static final String SYSPROP_SSL_JVM_KEYSTOREPASS = "javax.net.ssl.keyStorePassword";
    private static String keystorePassword=null; // auth for above

    // Initialize configuration
    // Environment variables take precedence, then system properties, then our built-in defaults
    // used if not explicitly passed on other methods

    static{
        String sslVerifySetting;
        String truststoreSetting=null;
        String truststorePasswordSetting=null;
        String keystoreSetting=null;
        String keystorePasswordSetting=null;

        // Take care to default to strict security checks. Any non-empty value is used - env then property,
        // but we only switch off the check if set to (case insignificant) false - hence double -ve on compare
        sslVerifySetting = System.getenv(ENV_SSL_VERIFY);
        if (StringUtils.isNotBlank(sslVerifySetting))
        {
            log.debug("egeria-ssl: ssl verification set from environment " + ENV_SSL_VERIFY);
        }
        else
        {
             sslVerifySetting = System.getProperty(SYSPROP_SSL_VERIFY);
                    if (StringUtils.isNotBlank(sslVerifySetting))
                    {
                        log.debug("egeria-ssl: ssl verification set from system property " + SYSPROP_SSL_VERIFY);

                    }
                    else {
                        sslVerifySetting = "true"; // default
                        log.debug("egeria-ssl: ssl verification left to default");
                    }
        }

        SSL_VERIFY = !StringUtils.equalsIgnoreCase(sslVerifySetting, "false");
        log.debug("egeria-ssl: ssl verification SET to: " + SSL_VERIFY);


        // Decide which truststore to use - once we decide we'll use the corresponding password if found
        // We will use Environment -> Egeria Client Property -> JVM property -> server config (if found)
        // If none found we'll remain set at null
        // NOte that a null password is valid - so if the truststore is set we'll take the associated pass even if null
        truststoreSetting = System.getenv(ENV_SSL_TRUSTSTORE);
        if (StringUtils.isNotBlank(truststoreSetting)) {
            log.debug("egeria-ssl: truststore set from environment " + ENV_SSL_TRUSTSTORE);
            truststorePasswordSetting = System.getenv(ENV_SSL_TRUSTSTOREPASS);
        }
        else
        {
            truststoreSetting = System.getProperty(SYSPROP_SSL_TRUSTSTORE);
            if (StringUtils.isNotBlank(truststoreSetting)) {
                log.debug("egeria-ssl: truststore set from egeria system property " + SYSPROP_SSL_TRUSTSTORE);
                truststorePasswordSetting = System.getProperty(SYSPROP_SSL_TRUSTSTOREPASS);
            }
            else
            {
                truststoreSetting = System.getProperty(SYSPROP_SSL_JVM_TRUSTSTORE);
                if (StringUtils.isNotBlank(truststoreSetting)) {
                    log.debug("egeria-ssl: truststore set from jvm system property " + SYSPROP_SSL_JVM_TRUSTSTORE);
                    truststorePasswordSetting = System.getProperty(SYSPROP_SSL_JVM_TRUSTSTOREPASS);
                }
                else
                {
                    truststoreSetting = System.getProperty(SYSPROP_SSL_SERVER_TRUSTSTORE);
                    if (StringUtils.isNotBlank(truststoreSetting)) {
                        log.debug("egeria-ssl: truststore set from spring server environment " + SYSPROP_SSL_SERVER_TRUSTSTORE);
                        truststorePasswordSetting = System.getProperty(SYSPROP_SSL_SERVER_TRUSTSTOREPASS);
                    }
                }
            }
        }

        if (truststoreSetting != null)
        {
            truststore=truststoreSetting;
            truststorePassword=truststorePasswordSetting;
            log.debug("egeria-ssl: truststore SET to " + truststore);
        }
        else
            log.debug("egeria-ssl: truststore left at default");

        // Decide which truststore to use - once we decide we'll use the corresponding password if found
        // We will use Environment -> Egeria Client Property -> JVM property -> server config (if found)
        // If none found we'll remain set at null
        // NOte that a null password is valid - so if the truststore is set we'll take the associated pass even if null
        keystoreSetting = System.getenv(ENV_SSL_KEYSTORE);
        if (StringUtils.isNotBlank(keystoreSetting)) {
            log.debug("egeria-ssl: keystore set from environment " + ENV_SSL_KEYSTORE);
            keystorePasswordSetting = System.getenv(ENV_SSL_KEYSTOREPASS);
        }
        else
        {
            keystore = System.getProperty(SYSPROP_SSL_KEYSTORE);
            if (StringUtils.isNotBlank(keystore)) {
                log.debug("egeria-ssl: keystore set from egeria system property " + SYSPROP_SSL_KEYSTORE);
                keystorePasswordSetting = System.getProperty(SYSPROP_SSL_KEYSTOREPASS);
            }
            else
            {
                keystore = System.getProperty(SYSPROP_SSL_JVM_KEYSTORE);
                if (StringUtils.isNotBlank(keystore)) {
                    log.debug("egeria-ssl: keystore set from jvm system property " + SYSPROP_SSL_JVM_KEYSTORE);
                    keystorePasswordSetting = System.getProperty(SYSPROP_SSL_JVM_KEYSTOREPASS);
                }
                else
                {
                    keystore = System.getProperty(SYSPROP_SSL_SERVER_KEYSTORE);
                    if (StringUtils.isNotBlank(keystore)) {
                        log.debug("egeria-ssl: keystore set from spring server environment " + SYSPROP_SSL_SERVER_KEYSTORE);
                        keystorePasswordSetting = System.getProperty(SYSPROP_SSL_SERVER_KEYSTOREPASS);
                    }
                }
            }
        }

        if (keystoreSetting != null)
        {
            keystore=keystoreSetting;
            keystorePassword=keystorePasswordSetting;
            log.debug("egeria-ssl: keystore SET to " + truststore);
        }
        else
            log.debug("egeria-ssl: keystore left at default");

        // The truststore and password should now be available - null will just mean not certs loaded
    }

    /**
     * Default constructor
     */
    public SpringRESTClientConnector() throws NoSuchAlgorithmException, KeyStoreException, KeyManagementException, IOException, CertificateException, UnrecoverableKeyException {
        this(SSL_VERIFY);
    }

    /**
     * constructor with ssl option
     * @param ssl_verify boolean to indicate if SSL cert checking is done
     */
    public SpringRESTClientConnector(boolean ssl_verify) throws NoSuchAlgorithmException, KeyStoreException, KeyManagementException,
            IOException, CertificateException, UnrecoverableKeyException {
        super();

        /* TODO: Disable SSL cert verification -- for now */
        //TODO: Can throw NoSuchAlgorithmException : TLS v1.3 SSLContext not available
        // TLS v1.3 only possible in Java 11+
        SSLContext sc = SSLContext.getInstance("TLSv1.2");

        // TODO - this is where we inject key manager and trust manager if we want to use non-default settings
        // For now keystore must be in pkcs12 - default Java 9 & above, and for all non java
        if (ssl_verify) {
            InputStream tsf;
            InputStream ksf;

            // Truststore -- where we store CAs & certificates we trust. Used by client in 1way and mutual SSL
            KeyStore tStore = KeyStore.getInstance("PKCS12");
            TrustManagerFactory tmFactory = TrustManagerFactory.getInstance("X509");

            // Allow use of classpath - to find resource inside jar. Useful for out of the box config
            if (StringUtils.startsWithIgnoreCase(truststore, "classpath:")) {
                tsf=this.getClass().getClassLoader().getResourceAsStream(StringUtils.removeStart(truststore,"classpath:"));
            }
            else
                // otherwise absolute filename
            //TODO: If truststore is null - or file not found, we'll hit an exception here
                tsf = new FileInputStream(truststore);

            // Load the keystore, initialise the factory
            tStore.load(tsf,truststorePassword.toCharArray());
            tmFactory.init(tStore);
            tsf.close();

            KeyStore kStore=null;
            KeyManagerFactory kmf=null;
            if (StringUtils.isNotBlank(keystore)) {
                // Very similar for keystore - this is needed for mutual ssl
                // Default KeyManager will choose an appropriate cert to use
                //TODO: Implement own X509KeyManager if required to choose by alias - the default
                //implementation will chose the first appropriate one in alphanumeric order.
                kStore = KeyStore.getInstance("PKCS12");
                 kmf = KeyManagerFactory.getInstance("X509");

                if (StringUtils.startsWithIgnoreCase(keystore, "classpath:")) {
                    ksf = this.getClass().getClassLoader().getResourceAsStream(StringUtils.removeStart(keystore, "classpath:"));
                } else
                    ksf = new FileInputStream(keystore);
                kStore.load(ksf, keystorePassword.toCharArray());
                kmf.init(kStore, keystorePassword.toCharArray());
                ksf.close();
            }

            // Get a good seed for the crypt
            SecureRandom random = new SecureRandom();
            random.setSeed(System.currentTimeMillis());

            // Now have a new SSL context
            // Quick fix - if keystore not set let's skip that part
            if (StringUtils.isNotBlank(keystore))
            {
                sc.init(kmf.getKeyManagers(), tmFactory.getTrustManagers(), random);
            }
            else
                sc.init(null, tmFactory.getTrustManagers(), random);

        }

        else
            // no-ops when SSL verification is not used
            sc.init(null, INSECURE_MANAGER, null);


        // Get a custom httpclient - scope to Egeria rest API calls & do not impact other usages

        HttpClientBuilder httpBuilder = HttpClientBuilder.create();
        httpBuilder.useSystemProperties();
        httpBuilder.setSSLContext(sc);

        if (ssl_verify)
            httpBuilder.setSSLHostnameVerifier(new DefaultHostnameVerifier());
        else
            httpBuilder.setSSLHostnameVerifier(new NoopHostnameVerifier());

        CloseableHttpClient httpClient;
        httpClient = httpBuilder.build();

        HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory();
        requestFactory.setHttpClient(httpClient);

        restTemplate = new RestTemplate(requestFactory);

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
        restTemplate.setUriTemplateHandler(builderFactory);

        /* Ensure that the REST template always uses UTF-8 */
        List<HttpMessageConverter<?>> converters = restTemplate.getMessageConverters();
        converters.removeIf(httpMessageConverter -> httpMessageConverter instanceof StringHttpMessageConverter);
        converters.add(0, new StringHttpMessageConverter(StandardCharsets.UTF_8));

    }


    /**
     * Dummy TrustManager that is happy with any cert
     *
     */
    private static final TrustManager[] INSECURE_MANAGER = new TrustManager[]{new X509TrustManager() {
        public X509Certificate[] getAcceptedIssuers() {
            return null;
        }

        /**
         * check client is trusted - it ALWAYS is in this dummy implementation
         * (an exception would be caused if not)
         *
         * @param certs X509 certificates
         * @param authType authType
         */
        public void checkClientTrusted(X509Certificate[] certs, String authType) {
        }

        /**
         * check server is trusted - it ALWAYS is in this dummy implementation
         * (an exception would be caused if not)
         *
         * @param certs X509 certificates
         * @param authType authType
         */
        public void checkServerTrusted(X509Certificate[] certs, String authType) {
        }
    }
    };


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
    public  <T> T callPutRESTCall(String    methodName,
                                   Class<T>  returnClass,
                                   String    urlTemplate,
                                   Object    requestBody,
                                   Object... params) throws RESTServerException
    {
        try
        {
            log.debug("Calling " + methodName + " with URL template " + urlTemplate + " and parameters " + Arrays.toString(params) + ".");

            HttpEntity<?> request = new HttpEntity<>(requestBody);

            if (requestBody == null)
            {
                // continue with a null body, we may want to fail this request here in the future.
                log.warn("Poorly formed PUT call made by " + methodName);
            }
            if (basicAuthorizationHeader != null)
            {
                    request = new HttpEntity<>(requestBody, basicAuthorizationHeader);
            }

            ResponseEntity<T> responseEntity = restTemplate.exchange(urlTemplate, HttpMethod.PUT, request, returnClass, params);
            T responseObject = responseEntity.getBody();

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

            // requestBody may be null
            HttpEntity<?> request = new HttpEntity<>(requestBody) ;
            if (basicAuthorizationHeader != null) {
                request = new HttpEntity<>(requestBody, basicAuthorizationHeader);
            }
            ResponseEntity<T>  responseEntity = restTemplate.exchange(urlTemplate, HttpMethod.DELETE, request, returnClass, params);
            T  responseObject = responseEntity.getBody();
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
