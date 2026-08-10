/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.integration.openapis.properties;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * OpenAPISecurityScheme describes one of the named security schemes declared in the components section of the
 * specification, and referenced by name from an operation's security requirements.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class OpenAPISecurityScheme
{
    private String type             = null;
    private String description      = null;
    private String name             = null;
    private String in               = null;
    private String scheme           = null;
    private String bearerFormat     = null;
    private String openIdConnectUrl = null;


    public OpenAPISecurityScheme()
    {
    }


    public String getType()
    {
        return type;
    }


    public void setType(String type)
    {
        this.type = type;
    }


    public String getDescription()
    {
        return description;
    }


    public void setDescription(String description)
    {
        this.description = description;
    }


    public String getName()
    {
        return name;
    }


    public void setName(String name)
    {
        this.name = name;
    }


    public String getIn()
    {
        return in;
    }


    public void setIn(String in)
    {
        this.in = in;
    }


    public String getScheme()
    {
        return scheme;
    }


    public void setScheme(String scheme)
    {
        this.scheme = scheme;
    }


    public String getBearerFormat()
    {
        return bearerFormat;
    }


    public void setBearerFormat(String bearerFormat)
    {
        this.bearerFormat = bearerFormat;
    }


    public String getOpenIdConnectUrl()
    {
        return openIdConnectUrl;
    }


    public void setOpenIdConnectUrl(String openIdConnectUrl)
    {
        this.openIdConnectUrl = openIdConnectUrl;
    }


    @Override
    public String toString()
    {
        return "OpenAPISecurityScheme{" +
                       "type='" + type + '\'' +
                       ", description='" + description + '\'' +
                       ", name='" + name + '\'' +
                       ", in='" + in + '\'' +
                       ", scheme='" + scheme + '\'' +
                       ", bearerFormat='" + bearerFormat + '\'' +
                       ", openIdConnectUrl='" + openIdConnectUrl + '\'' +
                       '}';
    }
}
