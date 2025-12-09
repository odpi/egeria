/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adminservices.configuration.properties;

import java.util.Objects;

/**
 * ServerPropertiesRequestBody is the request body to add the basic server properties to an OMAG Server's configuration document.
 */
public class BasicServerProperties extends AdminServicesConfigHeader
{
    /*
     * Default values used when the server configuration does not provide a value.
     */
    public  static final String  defaultLocalServerType                   = null;
    private static final String  defaultLocalOrganizationName             = null;
    private static final String  defaultLocalServerURL                    = "~{egeriaEndpoint}~";
    private static final String  defaultLocalServerUserId                 = "OMAGServer";
    public  static final int     defaultMaxPageSize                       = 1000;

    private String localServerDescription = null;
    private String localServerType        = null;
    private String organizationName       = defaultLocalOrganizationName;
    private String localServerURL         = defaultLocalServerURL;
    private String localServerUserId      = defaultLocalServerUserId;
    private String secretsStoreProvider   = "org.odpi.openmetadata.adapters.connectors.secretsstore.yaml.YAMLSecretsStoreProvider";
    private String secretsStoreLocation   = null;
    private String secretsStoreCollection = null;
    private int    maxPageSize            = defaultMaxPageSize;


    /**
     * Default constructor.
     */
    public BasicServerProperties()
    {
        super();
    }


    /**
     * Copy/clone constructor.
     *
     * @param template object to copy
     */
    public BasicServerProperties(BasicServerProperties template)
    {
        super(template);

        if (template != null)
        {
            localServerDescription          = template.getLocalServerDescription();
            localServerType                 = template.getLocalServerType();
            organizationName                = template.getOrganizationName();
            localServerURL                  = template.getLocalServerURL();
            localServerUserId               = template.getLocalServerUserId();
            secretsStoreProvider            = template.getSecretsStoreProvider();
            secretsStoreLocation            = template.getSecretsStoreLocation();
            secretsStoreCollection          = template.getSecretsStoreCollection();
            maxPageSize                     = template.getMaxPageSize();
        }
    }


    /**
     * Return the description of the local server.
     *
     * @return String server description
     */
    public String getLocalServerDescription()
    {
        return localServerDescription;
    }


    /**
     * Set up the description of the local server.
     *
     * @param localServerDescription String local server description
     */
    public void setLocalServerDescription(String localServerDescription)
    {
        this.localServerDescription = localServerDescription;
    }


    /**
     * Return the name of the organization that is running the server.
     *
     * @return String organization name
     */
    public String getOrganizationName()
    {
        return organizationName;
    }


    /**
     * Set up the name of the organization that is running the server.
     *
     * @param organizationName String organization name
     */
    public void setOrganizationName(String organizationName)
    {
        this.organizationName = organizationName;
    }


    /**
     * Return the base URL for calling the local server.
     *
     * @return String URL
     */
    public String getLocalServerURL()
    {
        return localServerURL;
    }


    /**
     * Set up the root URL for this server that is used to construct full URL paths to calls for
     * this server's REST interfaces.  Typically, this is the URL root of the OMAG Server Platform
     * Where the server is deployed to.  However, it may be a DNS name - particularly if the server is
     * deployed to multiple platforms for high availability (HA).
     * The default value is <a href="https://localhost:9443">"https://localhost:9443"</a>.
     *
     * @param localServerURL String URL
     */
    public void setLocalServerURL(String localServerURL)
    {
        this.localServerURL = localServerURL;
    }


    /**
     * Return the userId that the local server should use when processing events and there is no external user
     * driving the operation.
     *
     * @return user id
     */
    public String getLocalServerUserId()
    {
        return localServerUserId;
    }


    /**
     * Set up the userId that the local server should use when processing events and there is no external user
     * driving the operation.
     *
     * @param localServerUserId string user id
     */
    public void setLocalServerUserId(String localServerUserId)
    {
        this.localServerUserId = localServerUserId;
    }


    /**
     * Return the descriptive name for the server type.
     *
     * @return String server type
     */
    public String getLocalServerType()
    {
        return localServerType;
    }


    /**
     * Set up the descriptive name for the server type.
     *
     * @param localServerType String server type
     */
    public void setLocalServerType(String localServerType)
    {
        this.localServerType = localServerType;
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
     * Return the maximum page size supported by this server.
     *
     * @return int number of elements
     */
    public int getMaxPageSize()
    {
        return maxPageSize;
    }


    /**
     * Set up the maximum page size supported by this server.
     *
     * @param maxPageSize int number of elements
     */
    public void setMaxPageSize(int maxPageSize)
    {
        this.maxPageSize = maxPageSize;
    }


    /**
     * Standard toString method.
     *
     * @return JSON style description of variables.
     */
    @Override
    public String toString()
    {
        return "BasicServerProperties{" +
                "localServerDescription='" + localServerDescription + '\'' +
                ", localServerType='" + localServerType + '\'' +
                ", organizationName='" + organizationName + '\'' +
                ", localServerURL='" + localServerURL + '\'' +
                ", localServerUserId='" + localServerUserId + '\'' +
                ", secretsStoreProvider='" + secretsStoreProvider + '\'' +
                ", secretsStoreLocation='" + secretsStoreLocation + '\'' +
                ", secretsStoreCollection='" + secretsStoreCollection + '\'' +
                ", maxPageSize=" + maxPageSize +
                "} " + super.toString();
    }



    /**
     * Validate that an object is equal depending on their stored values.
     *
     * @param objectToCompare object
     * @return boolean result
     */
    @Override
    public boolean equals(Object objectToCompare)
    {
        if (this == objectToCompare)
        {
            return true;
        }
        if (! (objectToCompare instanceof BasicServerProperties that))
        {
            return false;
        }
        return maxPageSize == that.maxPageSize &&
                Objects.equals(localServerDescription, that.localServerDescription) &&
                Objects.equals(organizationName, that.organizationName) &&
                Objects.equals(localServerURL, that.localServerURL) &&
                Objects.equals(localServerUserId, that.localServerUserId) &&
                Objects.equals(secretsStoreProvider, that.secretsStoreProvider) &&
                Objects.equals(secretsStoreLocation, that.secretsStoreLocation) &&
                Objects.equals(secretsStoreCollection, that.secretsStoreCollection);
    }


    /**
     * Return a hash code based on the values of this object.
     *
     * @return in hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(localServerDescription, localServerType, organizationName, localServerURL,
                            localServerUserId, secretsStoreProvider, secretsStoreLocation, secretsStoreCollection,
                            maxPageSize);
    }
}
