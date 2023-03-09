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
 * OpenAPIEncoding describes the encoding of the request or response from the API.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class OpenAPIEncoding implements Serializable
{
    private static final long    serialVersionUID = 1L;

    private String contentType = null;


    public OpenAPIEncoding()
    {
    }


    public String getContentType()
    {
        return contentType;
    }


    public void setContentType(String contentType)
    {
        this.contentType = contentType;
    }


    @Override
    public String toString()
    {
        return "OpenAPIEncoding{" +
                       "contentType='" + contentType + '\'' +
                       '}';
    }
}
