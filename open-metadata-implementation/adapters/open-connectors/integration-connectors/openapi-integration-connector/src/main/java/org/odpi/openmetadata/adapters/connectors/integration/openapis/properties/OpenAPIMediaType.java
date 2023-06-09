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
 * OpenAPIMediaType describes the content of the request or response for an API.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class OpenAPIMediaType
{
    private OpenAPISchema                schema     = null;
    private Map<String, OpenAPIEncoding> encoding   = null;
    private Map<String, Object>          extensions = null;

    public OpenAPIMediaType()
    {
    }


    public OpenAPISchema getSchema()
    {
        return schema;
    }


    public void setSchema(OpenAPISchema schema)
    {
        this.schema = schema;
    }


    public Map<String, OpenAPIEncoding> getEncoding()
    {
        return encoding;
    }


    public void setEncoding(Map<String, OpenAPIEncoding> encoding)
    {
        this.encoding = encoding;
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
        return "OpenAPIMediaType{" +
                       "schema=" + schema +
                       ", encoding=" + encoding +
                       ", extensions=" + extensions +
                       '}';
    }
}
