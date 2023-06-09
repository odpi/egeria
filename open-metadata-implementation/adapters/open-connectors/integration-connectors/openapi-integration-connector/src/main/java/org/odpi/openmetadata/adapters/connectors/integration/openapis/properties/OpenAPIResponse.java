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
 * OpenAPIResponse describes a response to an API operation.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class OpenAPIResponse
{
    private String                        description = null;
    private Map<String, OpenAPIMediaType> content     = null;
    private Map<String, Object>           extensions  = null;
    private String                        $ref        = null;


    public OpenAPIResponse()
    {
    }


    public String getDescription()
    {
        return description;
    }


    public void setDescription(String description)
    {
        this.description = description;
    }


    public Map<String, OpenAPIMediaType> getContent()
    {
        return content;
    }


    public void setContent(Map<String, OpenAPIMediaType> content)
    {
        this.content = content;
    }


    public Map<String, Object> getExtensions()
    {
        return extensions;
    }


    public void setExtensions(Map<String, Object> extensions)
    {
        this.extensions = extensions;
    }


    public String get$ref()
    {
        return $ref;
    }


    public void set$ref(String $ref)
    {
        this.$ref = $ref;
    }


    @Override
    public String toString()
    {
        return "OpenAPIResponse{" +
                       "description='" + description + '\'' +
                       ", content=" + content +
                       ", extensions=" + extensions +
                       ", $ref='" + $ref + '\'' +
                       '}';
    }
}
