/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adapters.connectors.secretsstore.yaml.secretsstore;


import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Map;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * Describes a REST API to acquire a token
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class TokenAPI
{
    private String              httpRequestType = "GET";
    private String              url             = null;
    private String              contentType     = "application/json";
    private Map<String, Object> requestBody     = null;

    /**
     * Default constructor
     */
    public TokenAPI()
    {
    }


    /**
     * Return the HTTP request type for the Token API: GET, POST etc
     *
     * @return string
     */
    public String getHttpRequestType()
    {
        return httpRequestType;
    }


    /**
     * Set up the HTTP request type for the Token API: GET, POST etc
     *
     * @param httpRequestType string
     */
    public void setHttpRequestType(String httpRequestType)
    {
        this.httpRequestType = httpRequestType;
    }


    /**
     * Return the content type for the API.
     *
     * @return content type
     */
    public String getContentType()
    {
        return contentType;
    }


    /**
     * Set up the content type for the API - default is "application/json"
     *
     * @param contentType MIME content type
     */
    public void setContentType(String contentType)
    {
        this.contentType = contentType;
    }


    /**
     * Return the URL to use when issuing a REST call to get a token.
     *
     * @return string URL
     */
    public String getURL()
    {
        return url;
    }


    /**
     * Set up the URL to use when issuing a REST call to get a token.
     *
     * @param url string URL
     */
    public void setURL(String url)
    {
        this.url = url;
    }


    /**
     * Return the request body properties to use when issuing a REST call to get a token.
     *
     * @return JSON Structure as a map
     */
    public Map<String, Object> getRequestBody()
    {
        return requestBody;
    }


    /**
     * Set up the request body properties to use when issuing a REST call to get a token.
     *
     * @param requestBody JSON Structure as a map
     */
    public void setRequestBody(Map<String, Object> requestBody)
    {
        this.requestBody = requestBody;
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "TokenAPI{" +
                "httpRequestType='" + httpRequestType + '\'' +
                ", url='" + url + '\'' +
                ", contentType='" + contentType + '\'' +
                ", requestBody=" + requestBody +
                '}';
    }

    /**
     * Compare the values of the supplied object with those stored in the current object.
     *
     * @param objectToCompare supplied object
     * @return boolean result of comparison
     */
    @Override
    public boolean equals(Object objectToCompare)
    {
        if (this == objectToCompare) return true;
        if (objectToCompare == null || getClass() != objectToCompare.getClass()) return false;
        TokenAPI tokenAPI = (TokenAPI) objectToCompare;
        return Objects.equals(httpRequestType, tokenAPI.httpRequestType) &&
                Objects.equals(url, tokenAPI.url) &&
                Objects.equals(contentType, tokenAPI.contentType) &&
                Objects.equals(requestBody, tokenAPI.requestBody);
    }


    /**
     * Hash of properties
     *
     * @return int
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(httpRequestType, url, contentType, requestBody);
    }
}
