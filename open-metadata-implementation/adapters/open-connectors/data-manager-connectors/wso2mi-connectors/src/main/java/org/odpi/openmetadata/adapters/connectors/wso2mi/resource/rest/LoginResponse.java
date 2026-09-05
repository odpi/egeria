/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.wso2mi.resource.rest;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * LoginResponse maps the payload returned by the WSO2 Micro Integrator Management API's
 * {@code GET /management/login} resource:
 *
 * <pre>
 * { "AccessToken": "eyJhbGciOi..." }
 * </pre>
 *
 * The field name has varied in case across WSO2 versions, so both {@code AccessToken} and
 * {@code accessToken} are accepted.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class LoginResponse
{
    private String accessToken = null;


    /**
     * Default constructor.
     */
    public LoginResponse()
    {
    }


    /**
     * Return the bearer token to use on subsequent Management API calls.
     *
     * @return string token
     */
    @JsonAlias({"AccessToken", "accessToken"})
    public String getAccessToken()
    {
        return accessToken;
    }


    /**
     * Set up the bearer token.
     *
     * @param accessToken string token
     */
    public void setAccessToken(String accessToken)
    {
        this.accessToken = accessToken;
    }


    /**
     * JSON-style toString.  The token value is deliberately not included.
     *
     * @return string
     */
    @Override
    public String toString()
    {
        return "LoginResponse{accessToken=" + (accessToken == null ? "null" : "***") + '}';
    }
}
