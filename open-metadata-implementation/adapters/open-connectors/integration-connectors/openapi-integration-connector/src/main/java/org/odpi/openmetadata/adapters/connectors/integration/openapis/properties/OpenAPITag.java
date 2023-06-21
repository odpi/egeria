/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.integration.openapis.properties;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * OpenAPILicense describes the license associated with the API.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class OpenAPITag
{
    private String              name         = null;
    private String              description  = null;
    private OpenAPIExternalDocs externalDocs = null;


    public OpenAPITag()
    {
    }


    public String getName()
    {
        return name;
    }


    public void setName(String name)
    {
        this.name = name;
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


    @Override
    public String toString()
    {
        return "OpenAPITag{" +
                       "name='" + name + '\'' +
                       ", description='" + description + '\'' +
                       ", externalDocs=" + externalDocs +
                       '}';
    }
}
