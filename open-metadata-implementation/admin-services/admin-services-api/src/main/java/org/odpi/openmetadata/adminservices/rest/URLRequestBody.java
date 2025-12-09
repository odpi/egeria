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

/**
 * URLRequestBody provides a container for transporting a URL string in a request body.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class URLRequestBody
{
    private String urlRoot = null;
    private String secretsStoreProvider   = "org.odpi.openmetadata.adapters.connectors.secretsstore.yaml.YAMLSecretsStoreProvider";
    private String secretsStoreLocation   = null;
    private String secretsStoreCollection = null;


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
            urlRoot                = template.getUrlRoot();
            secretsStoreProvider   = template.getSecretsStoreProvider();
            secretsStoreLocation   = template.getSecretsStoreLocation();
            secretsStoreCollection = template.getSecretsStoreCollection();
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
     * Return the class name of the secrets store provider.
     *
     * @return string
     */
    public String getSecretsStoreProvider()
    {
        return secretsStoreProvider;
    }


    /**
     * Set up the class name of the secrets store provider.
     *
     * @param secretsStoreProvider string
     */
    public void setSecretsStoreProvider(String secretsStoreProvider)
    {
        this.secretsStoreProvider = secretsStoreProvider;
    }


    /**
     * Return the location information for the secrets store - this is stored in the endpoint network address for the
     * secrets store connector.
     *
     * @return string
     */
    public String getSecretsStoreLocation()
    {
        return secretsStoreLocation;
    }


    /**
     * Set up the location information for the secrets store - this is stored in the endpoint network address for the
     * secrets store connector.
     *
     * @param secretsStoreLocation string
     */
    public void setSecretsStoreLocation(String secretsStoreLocation)
    {
        this.secretsStoreLocation = secretsStoreLocation;
    }


    /**
     * Return the secrets collection to use for the connection to the remote server.
     *
     * @return string
     */
    public String getSecretsStoreCollection()
    {
        return secretsStoreCollection;
    }


    /**
     * Set up the secrets collection to use for the connection to the remote server.
     *
     * @param secretsStoreCollection string
     */
    public void setSecretsStoreCollection(String secretsStoreCollection)
    {
        this.secretsStoreCollection = secretsStoreCollection;
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
                ", secretsStoreProvider='" + secretsStoreProvider + '\'' +
                ", secretsStoreLocation='" + secretsStoreLocation + '\'' +
                ", secretsStoreCollection='" + secretsStoreCollection + '\'' +
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
        if (this == objectToCompare) return true;
        if (objectToCompare == null || getClass() != objectToCompare.getClass()) return false;
        URLRequestBody that = (URLRequestBody) objectToCompare;
        return Objects.equals(urlRoot, that.urlRoot) && Objects.equals(secretsStoreProvider, that.secretsStoreProvider) && Objects.equals(secretsStoreLocation, that.secretsStoreLocation) && Objects.equals(secretsStoreCollection, that.secretsStoreCollection);
    }

    /**
     * Simple hash for the object
     *
     * @return int
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(urlRoot, secretsStoreProvider, secretsStoreLocation, secretsStoreCollection);
    }
}
