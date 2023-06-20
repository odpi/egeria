/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.integration.openapis.properties;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;
import java.util.Map;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * OpenAPIPathDescription describes the operations associated with a specific path.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class OpenAPIPathDescription
{
    private String                        summary     = null;
    private String                        description = null;
    private OpenAPIOperation              get         = null;
    private OpenAPIOperation              put         = null;
    private OpenAPIOperation              post        = null;
    private OpenAPIOperation              delete      = null;
    private OpenAPIOperation              options     = null;
    private OpenAPIOperation              head        = null;
    private OpenAPIOperation              patch       = null;
    private OpenAPIOperation              trace       = null;
    private List<OpenAPIServer>           servers     = null;
    private List<OpenAPIParameter>        parameters  = null;
    private String                        $ref        = null;
    private java.util.Map<String, Object> extensions  = null;

    public OpenAPIPathDescription()
    {
    }


    public OpenAPIOperation getGet()
    {
        return get;
    }


    public void setGet(OpenAPIOperation get)
    {
        this.get = get;
    }


    public OpenAPIOperation getPut()
    {
        return put;
    }


    public void setPut(OpenAPIOperation put)
    {
        this.put = put;
    }


    public OpenAPIOperation getPost()
    {
        return post;
    }


    public void setPost(OpenAPIOperation post)
    {
        this.post = post;
    }


    public OpenAPIOperation getDelete()
    {
        return delete;
    }


    public void setDelete(OpenAPIOperation delete)
    {
        this.delete = delete;
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


    public OpenAPIOperation getOptions()
    {
        return options;
    }


    public void setOptions(OpenAPIOperation options)
    {
        this.options = options;
    }


    public OpenAPIOperation getHead()
    {
        return head;
    }


    public void setHead(OpenAPIOperation head)
    {
        this.head = head;
    }


    public OpenAPIOperation getPatch()
    {
        return patch;
    }


    public void setPatch(OpenAPIOperation patch)
    {
        this.patch = patch;
    }


    public OpenAPIOperation getTrace()
    {
        return trace;
    }


    public void setTrace(OpenAPIOperation trace)
    {
        this.trace = trace;
    }


    public List<OpenAPIServer> getServers()
    {
        return servers;
    }


    public void setServers(List<OpenAPIServer> servers)
    {
        this.servers = servers;
    }


    public List<OpenAPIParameter> getParameters()
    {
        return parameters;
    }


    public void setParameters(List<OpenAPIParameter> parameters)
    {
        this.parameters = parameters;
    }


    public String get$ref()
    {
        return $ref;
    }


    public void set$ref(String $ref)
    {
        this.$ref = $ref;
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
        return "OpenAPIPathDescription{" +
                       "summary='" + summary + '\'' +
                       ", description='" + description + '\'' +
                       ", get=" + get +
                       ", put=" + put +
                       ", post=" + post +
                       ", delete=" + delete +
                       ", options=" + options +
                       ", head=" + head +
                       ", patch=" + patch +
                       ", trace=" + trace +
                       ", servers=" + servers +
                       ", parameters=" + parameters +
                       ", $ref='" + $ref + '\'' +
                       ", extensions=" + extensions +
                       '}';
    }
}
