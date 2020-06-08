/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.subjectarea.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.odpi.openmetadata.accessservices.subjectarea.ffdc.SubjectAreaErrorCode;
import org.odpi.openmetadata.accessservices.subjectarea.ffdc.exceptions.InvalidParameterException;
import org.odpi.openmetadata.accessservices.subjectarea.ffdc.exceptions.MetadataServerUncontactableException;
import org.odpi.openmetadata.accessservices.subjectarea.responses.SubjectAreaOMASAPIResponse;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import javax.net.ssl.*;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;

import static com.google.json.JsonSanitizer.sanitize;

/**
 * Methods to issue rest calls for the SubjectAreaOMAS using the RestTemplate Spring API
 *
 * TODO there is no security that is added to these rest calls at this time.
 */
public class RestCaller {
    /**
     * Issue a GET REST call that returns a SubjectAreaOMASAPIResponse object.
     * @param className  name of the calling class
     * @param methodName   name of the calling method
     * @param url url
     * @return SubjectAreaOMASAPIResponse  subject area omas response
     * @throws MetadataServerUncontactableException - something went wrong with the REST call stack.
     */
    public static SubjectAreaOMASAPIResponse issueGet(String className,
                                                      String methodName,
                                                      String url) throws MetadataServerUncontactableException {
        return issueExchangeWithoutBody(className, methodName, HttpMethod.GET, url);
    }

    /**
     * Issue a POST REST call that returns a SubjectAreaOMASAPIResponse object.
     *
     * @param className  name of the calling class
     * @param methodName  name of the calling method
     * @param requestBody body of the rest request
     * @param url  the URL for the REST API call
     * @return SubjectAreaOMASAPIResponse    response
     * @throws MetadataServerUncontactableException - something went wrong with the REST call stack.
     */
    public static SubjectAreaOMASAPIResponse issuePost(String className,
                                                       String methodName,
                                                       String requestBody,
                                                       String url
                                                       )
                                             throws MetadataServerUncontactableException {
        return issueExchangeWithBody(className,methodName,HttpMethod.POST,requestBody,url);

    }
    /**
     * Issue a POST REST call that returns a SubjectAreaOMASAPIResponse object.
     *
     * @param className  name of the calling class
     * @param methodName  name of the calling method
     * @param url  the URL for the REST API call
     * @return SubjectAreaOMASAPIResponse    response
     * @throws MetadataServerUncontactableException - something went wrong with the REST call stack.
     */
    public static SubjectAreaOMASAPIResponse issuePostNoBody(String className,
                                                       String methodName,
                                                       String url
    )
            throws MetadataServerUncontactableException {
        return issueExchangeWithoutBody(className,methodName,HttpMethod.POST,url);

    }
    /**
     * Issue a PUT REST call that returns a SubjectAreaOMASAPIResponse object.
     *
     * @param className  name of the calling class
     * @param methodName  name of the calling method
     * @param requestBody body of the rest request
     * @param url  the URL for the REST API call
     * @return SubjectAreaOMASAPIResponse    response
     * @throws MetadataServerUncontactableException - something went wrong with the REST call stack.
     */
    public static SubjectAreaOMASAPIResponse issuePut(String className,
                                                       String methodName,
                                                       String requestBody,
                                                       String url
    )
            throws MetadataServerUncontactableException {
        return issueExchangeWithBody(className,methodName,HttpMethod.PUT,requestBody,url);

    }
    /**
     * Issue a DELETE REST call that returns a SubjectAreaOMASAPIResponse object.
     *
     * @param className name of the calling class
     * @param methodName  name of the calling method
     * @param url url for the server
     * @return SubjectAreaOMASAPIResponse    response
     * @throws MetadataServerUncontactableException - something went wrong with the REST call stack.
     */
    public static SubjectAreaOMASAPIResponse issueDelete(String className,
                                                      String methodName,
                                                      String url
    )
            throws MetadataServerUncontactableException {
        return issueExchangeWithoutBody(className,methodName,HttpMethod.DELETE,url);
    }

    /**
     * Issue a rest exchange call with a rest body.
     * @param className  name of the calling class
     * @param methodName  name of the calling method
     * @param httpMethod - http method
     * @param requestBody body of the rest request
     * @param url  the URL for the REST API call
     * @return SubjectAreaOMASAPIResponse    response
     * @throws MetadataServerUncontactableException - something went wrong with the REST call stack.
     */
    private static SubjectAreaOMASAPIResponse issueExchangeWithBody(String className,
                                                                    String methodName,
                                                                    HttpMethod httpMethod,
                                                                    String requestBody,
                                                                    String url
    )
            throws MetadataServerUncontactableException {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<?> entity = new HttpEntity<Object>(requestBody, headers);

        return issueExchange(className, methodName, httpMethod, url, entity);
    }
    /**
     * Issue a rest exchange call without a rest body.
     * @param className  name of the calling class
     * @param methodName  name of the calling method
     * @param httpMethod - http method
     * @param url  the URL for the REST API call
     * @return SubjectAreaOMASAPIResponse    response
     * @throws MetadataServerUncontactableException - something went wrong with the REST call stack.
     */
    private static SubjectAreaOMASAPIResponse issueExchangeWithoutBody(String className,
                                                                    String methodName,
                                                                    HttpMethod httpMethod,
                                                                    String url
    )
            throws MetadataServerUncontactableException {


        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<?> entity = new HttpEntity<Object>( headers);
        return issueExchange(className, methodName, httpMethod, url, entity);
    }

    private static SubjectAreaOMASAPIResponse issueExchange(String className, String methodName, HttpMethod httpMethod, String url, HttpEntity<?> entity) throws MetadataServerUncontactableException {
        SubjectAreaOMASAPIResponse restResponse = null;
        RestTemplate restTemplate = new RestTemplate();
        String resultBody =null;
        ResponseEntity<String> result =null;

        skipSSL();

        try {

            result = restTemplate.exchange(url, httpMethod, entity, String.class);
            resultBody = result.getBody();
        } catch (Throwable error) {
            SubjectAreaErrorCode errorCode = SubjectAreaErrorCode.CLIENT_SIDE_REST_API_ERROR;

            throw new MetadataServerUncontactableException(
                    errorCode.getMessageDefinition(),
                    className,
                    methodName);
        }
        ObjectMapper mapper = new ObjectMapper();
        try {
            resultBody= sanitize(resultBody);
            restResponse =  mapper.readValue(resultBody,SubjectAreaOMASAPIResponse.class);
        } catch (IOException ioException) {
            SubjectAreaErrorCode errorCode = SubjectAreaErrorCode.CLIENT_SIDE_API_REST_RESPONSE_ERROR;
//            String errorMessage = errorCode.getErrorMessageId() + errorCode.getFormattedErrorMessage(methodName,
//                    url,
//                    ioException.getMessage());

            throw new MetadataServerUncontactableException(
                    errorCode.getMessageDefinition(),
                    className,
                    methodName,
                    ioException);
        }
        return restResponse;
    }

    public static void throwJsonParseError (String className, String methodName,  JsonProcessingException error) throws InvalidParameterException {
        SubjectAreaErrorCode errorCode = SubjectAreaErrorCode.UNABLE_TO_PARSE_SUPPLIED_JSON;
        //
        throw new InvalidParameterException(errorCode.getMessageDefinition(),
                className,
                methodName,
                "json",
                "invalid");
    }

    /**
     * Dummy TrustManager that is happy with any cert
     *
     * @param hostname hostname
     * @param sslSession ssl ession
     * @return boolean result
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
         * @param authType authtype
         */
        public void checkClientTrusted(X509Certificate[] certs, String authType) {
        }

        /**
         * check server is trusted - it ALWAYS is in this dummy implementation
         * (an exception would be caused if not)
         *
         * @param certs X509 certificates
         * @param authType authtype
         */
        public void checkServerTrusted(X509Certificate[] certs, String authType) {
        }
    }
    };

    /**
     * Dummy HostnameVerifier that is happy with any host (for the SSL host checking)
     *
     * @param hostname hostname
     * @param sslSession ssl ession
     * @return boolean result
     */
    private static final HostnameVerifier bypassVerifier = new HostnameVerifier() {
        public boolean verify(String hostname, SSLSession sslSession) {
            return true;
        }
    };

    /* TODO: Temporary fix -- a) SHould use spring rest template connector b) needs to support certs */
    private static void skipSSL() {
        /* TODO: Disable SSL cert verification -- for now */
        try {
            HttpsURLConnection.setDefaultHostnameVerifier(bypassVerifier);
            SSLContext sc = SSLContext.getInstance("SSL");
            sc.init(null, INSECURE_MANAGER, null);
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
        } catch (NoSuchAlgorithmException e) {
        } catch (KeyManagementException e) {
        }
    }
}
