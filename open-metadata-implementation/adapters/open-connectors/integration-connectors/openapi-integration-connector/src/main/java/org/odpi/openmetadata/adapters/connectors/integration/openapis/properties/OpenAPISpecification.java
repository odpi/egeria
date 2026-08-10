/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.integration.openapis.properties;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;
import java.util.Map;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;


/**
 * OpenAPISpecification provides a java class for Jackson to use to unpack the
 * open API specification retrieved from the server.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class OpenAPISpecification
{
    private String                                     openapi      = null;
    private OpenAPIExternalDocs                        externalDocs = null;
    private OpenAPIInfo                                info         = null;
    private List<OpenAPIServer>                        servers      = null;
    private List<OpenAPITag>                           tags         = null;
    private Map<String, Map<String, OpenAPIOperation>> paths        = null;
    private OpenAPIComponents                          components   = null;
    private List<Map<String, List<String>>>            security     = null;

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

    public OpenAPIExternalDocs getExternalDocs()
    {
        return externalDocs;
    }

    public void setExternalDocs(OpenAPIExternalDocs externalDocs)
    {
        this.externalDocs = externalDocs;
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


    public Map<String, Map<String, OpenAPIOperation>> getPaths()
    {
        return paths;
    }

    public void setPaths(Map<String, Map<String, OpenAPIOperation>> paths)
    {
        this.paths = paths;
    }


    public OpenAPIComponents getComponents()
    {
        return components;
    }


    public void setComponents(OpenAPIComponents components)
    {
        this.components = components;
    }


    public List<Map<String, List<String>>> getSecurity()
    {
        return security;
    }


    public void setSecurity(List<Map<String, List<String>>> security)
    {
        this.security = security;
    }


    @Override
    public String toString()
    {
        return "OpenAPISpecification{" +
                "openapi='" + openapi + '\'' +
                ", externalDocs=" + externalDocs +
                ", info=" + info +
                ", servers=" + servers +
                ", tags=" + tags +
                ", paths=" + paths +
                ", components=" + components +
                ", security=" + security +
                '}';
    }

    @Override
    public boolean equals(Object objectToCompare)
    {
        if (this == objectToCompare) return true;
        if (objectToCompare == null || getClass() != objectToCompare.getClass()) return false;
        OpenAPISpecification that = (OpenAPISpecification) objectToCompare;
        return Objects.equals(openapi, that.openapi) && Objects.equals(externalDocs, that.externalDocs) && Objects.equals(info, that.info) && Objects.equals(servers, that.servers) && Objects.equals(tags, that.tags) && Objects.equals(paths, that.paths) && Objects.equals(components, that.components) && Objects.equals(security, that.security);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(openapi, externalDocs, info, servers, tags, paths, components, security);
    }
}
