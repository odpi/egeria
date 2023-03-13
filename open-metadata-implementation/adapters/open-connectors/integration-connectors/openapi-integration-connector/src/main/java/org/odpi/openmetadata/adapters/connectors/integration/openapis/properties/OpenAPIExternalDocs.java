/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.integration.openapis.properties;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * OpenAPIExternalDocs holds the links to external documentation.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class OpenAPIExternalDocs implements Serializable
{
    private static final long    serialVersionUID = 1L;

    private String description = null;
    private String url  = null;


    public OpenAPIExternalDocs()
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


    public String getUrl()
    {
        return url;
    }


    public void setUrl(String url)
    {
        this.url = url;
    }


    @Override
    public String toString()
    {
        return "OpenAPIExternalDocs{" +
                       "description='" + description + '\'' +
                       ", url='" + url + '\'' +
                       '}';
    }
}
