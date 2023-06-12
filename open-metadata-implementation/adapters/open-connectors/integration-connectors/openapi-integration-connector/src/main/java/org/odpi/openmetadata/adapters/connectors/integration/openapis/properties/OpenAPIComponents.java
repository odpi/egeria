/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.integration.openapis.properties;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Map;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * OpenAPIComponents describes the schemas referenced in the API.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class OpenAPIComponents
{
    private Map<String, OpenAPISchema>      schemas       = null;
    private Map<String, OpenAPIResponse>    responses     = null;
    private Map<String, OpenAPIParameter>   parameters    = null;
    private Map<String, OpenAPIRequestBody> requestBodies = null;
    private Map<String, Object>             extensions    = null;


    public OpenAPIComponents()
    {
    }


    public Map<String, OpenAPISchema> getSchemas()
    {
        return schemas;
    }


    public void setSchemas(Map<String, OpenAPISchema> schemas)
    {
        this.schemas = schemas;
    }


    public Map<String, OpenAPIResponse> getResponses()
    {
        return responses;
    }


    public void setResponses(
            Map<String, OpenAPIResponse> responses)
    {
        this.responses = responses;
    }


    public Map<String, OpenAPIParameter> getParameters()
    {
        return parameters;
    }


    public void setParameters(
            Map<String, OpenAPIParameter> parameters)
    {
        this.parameters = parameters;
    }


    public Map<String, OpenAPIRequestBody> getRequestBodies()
    {
        return requestBodies;
    }


    public void setRequestBodies(
            Map<String, OpenAPIRequestBody> requestBodies)
    {
        this.requestBodies = requestBodies;
    }


    public Map<String, Object> getExtensions()
    {
        return extensions;
    }


    public void setExtensions(Map<String, Object> extensions)
    {
        this.extensions = extensions;
    }


    @Override
    public String toString()
    {
        return "OpenAPIComponents{" +
                       "schemas=" + schemas +
                       ", responses=" + responses +
                       ", parameters=" + parameters +
                       ", requestBodies=" + requestBodies +
                       ", extensions=" + extensions +
                       '}';
    }
}
