/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adminservices.rest;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
/**
 * URLRequestBody provides a container for transporting a URL string in a request body.
 */
public class URLRequestBody
{
    private String urlRoot = null;


    /**
     * Default constructor
     */
    public URLRequestBody()
    {
    }


    /**
     * Copy/clone constructor
     *
     * @param template source
     */
    public URLRequestBody(URLRequestBody template)
    {
        if (template != null)
        {
            urlRoot = template.getUrlRoot();
        }
    }


    /**
     * Return the URL root (typically host name and port).
     *
     * @return string
     */
    public String getUrlRoot()
    {
        return urlRoot;
    }


    /**
     * Set up the URL root (typically host name and port).
     *
     * @param urlRoot string
     */
    public void setUrlRoot(String urlRoot)
    {
        this.urlRoot = urlRoot;
    }


    /**
     * JSON-style toString
     *
     * @return description of the object values
     */
    @Override
    public String toString()
    {
        return "URLRequestBody{" +
                "urlRoot='" + urlRoot + '\'' +
                '}';
    }


    /**
     * Compare objects
     *
     * @param objectToCompare object
     * @return boolean
     */
    @Override
    public boolean equals(Object objectToCompare)
    {
        if (this == objectToCompare)
        {
            return true;
        }
        if (objectToCompare == null || getClass() != objectToCompare.getClass())
        {
            return false;
        }
        URLRequestBody that = (URLRequestBody) objectToCompare;
        return Objects.equals(getUrlRoot(), that.getUrlRoot());
    }


    /**
     * Simple hash for the object
     *
     * @return int
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(getUrlRoot());
    }
}
