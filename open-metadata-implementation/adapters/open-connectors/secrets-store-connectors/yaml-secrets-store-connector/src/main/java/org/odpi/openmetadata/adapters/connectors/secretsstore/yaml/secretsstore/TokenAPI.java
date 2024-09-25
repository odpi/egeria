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
    private String              url         = null;
    private Map<String, Object> requestBody = null;

    /**
     * Default constructor
     */
    public TokenAPI()
    {
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
                "url='" + url + '\'' +
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
        return Objects.equals(url, tokenAPI.url) && Objects.equals(requestBody, tokenAPI.requestBody);
    }


    /**
     * Hash of properties
     *
     * @return int
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(url, requestBody);
    }
}
