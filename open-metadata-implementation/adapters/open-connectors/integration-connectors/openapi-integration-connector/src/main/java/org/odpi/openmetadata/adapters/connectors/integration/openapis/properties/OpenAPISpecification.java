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
 * OpenAPISpecification provides a java class for Jackson to use to unpack the
 * open API specification retrieved from the server.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class OpenAPISpecification implements Serializable
{
    private static final long   serialVersionUID = 1L;

    private String                              openapi = null;
    private OpenAPIInfo                         info = null;
    private List<OpenAPIServer>                 servers = null;
    private List<OpenAPITag>                    tags = null;
    private Map<String, OpenAPIPathDescription> paths = null;

    public OpenAPISpecification()
    {
    }


    public String getOpenapi()
    {
        return openapi;
    }


    public void setOpenapi(String openapi)
    {
        this.openapi = openapi;
    }


    public OpenAPIInfo getInfo()
    {
        return info;
    }


    public void setInfo(OpenAPIInfo info)
    {
        this.info = info;
    }


    public List<OpenAPIServer> getServers()
    {
        return servers;
    }


    public void setServers(List<OpenAPIServer> servers)
    {
        this.servers = servers;
    }


    public List<OpenAPITag> getTags()
    {
        return tags;
    }


    public void setTags(List<OpenAPITag> tags)
    {
        this.tags = tags;
    }


    public Map<String, OpenAPIPathDescription> getPaths()
    {
        return paths;
    }


    public void setPaths(Map<String, OpenAPIPathDescription> paths)
    {
        this.paths = paths;
    }


    @Override
    public String toString()
    {
        return "OpenAPISpecification{" +
                       "openapi='" + openapi + '\'' +
                       ", info=" + info +
                       ", servers=" + servers +
                       ", tags=" + tags +
                       ", paths=" + paths +
                       '}';
    }
}
