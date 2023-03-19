/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.integration.openapis.properties;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * OpenAPIOperation describes a specific API operation.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class OpenAPIOperation implements Serializable
{
    private static final long    serialVersionUID = 1L;

    private List<String>                 tags         = null;
    private String                       summary      = null;
    private String                       description  = null;
    private OpenAPIExternalDocs          externalDocs = null;
    private String                       operationId  = null;
    private List<OpenAPIParameter>       parameters   = null;
    private OpenAPIRequestBody           requestBody  = null;
    private Map<String, OpenAPIResponse> responses    = null;
    private boolean                      deprecated   = false;
    private Map<String, List<String>>    security     = null;
    private List<OpenAPIServer>          servers      = null;
    private Map<String, Object>          extensions   = null;

    public OpenAPIOperation()
    {
    }


    public List<String> getTags()
    {
        return tags;
    }


    public void setTags(List<String> tags)
    {
        this.tags = tags;
    }


    public String getSummary()
    {
        return summary;
    }


    public void setSummary(String summary)
    {
        this.summary = summary;
    }


    public String getDescription()
    {
        return description;
    }


    public void setDescription(String description)
    {
        this.description = description;
    }


    public OpenAPIExternalDocs getExternalDocs()
    {
        return externalDocs;
    }


    public void setExternalDocs(OpenAPIExternalDocs externalDocs)
    {
        this.externalDocs = externalDocs;
    }


    public String getOperationId()
    {
        return operationId;
    }


    public void setOperationId(String operationId)
    {
        this.operationId = operationId;
    }


    public List<OpenAPIParameter> getParameters()
    {
        return parameters;
    }


    public void setParameters(List<OpenAPIParameter> parameters)
    {
        this.parameters = parameters;
    }


    public Map<String, OpenAPIResponse> getResponses()
    {
        return responses;
    }


    public OpenAPIRequestBody getRequestBody()
    {
        return requestBody;
    }


    public void setRequestBody(OpenAPIRequestBody requestBody)
    {
        this.requestBody = requestBody;
    }


    public void setResponses(Map<String, OpenAPIResponse> responses)
    {
        this.responses = responses;
    }


    public boolean isDeprecated()
    {
        return deprecated;
    }


    public void setDeprecated(boolean deprecated)
    {
        this.deprecated = deprecated;
    }


    public Map<String, List<String>> getSecurity()
    {
        return security;
    }


    public void setSecurity(Map<String, List<String>> security)
    {
        this.security = security;
    }


    public List<OpenAPIServer> getServers()
    {
        return servers;
    }


    public void setServers(List<OpenAPIServer> servers)
    {
        this.servers = servers;
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
        return "OpenAPIOperation{" +
                       "tags=" + tags +
                       ", summary='" + summary + '\'' +
                       ", description='" + description + '\'' +
                       ", externalDocs=" + externalDocs +
                       ", operationId='" + operationId + '\'' +
                       ", parameters=" + parameters +
                       ", requestBody=" + requestBody +
                       ", responses=" + responses +
                       ", deprecated=" + deprecated +
                       ", security=" + security +
                       ", servers=" + servers +
                       ", extensions=" + extensions +
                       '}';
    }
}
