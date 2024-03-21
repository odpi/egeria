/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adminservices.properties;

import org.odpi.openmetadata.adminservices.configuration.properties.OMAGServerConfig;
import org.odpi.openmetadata.adminservices.rest.AdminServicesAPIResponse;

import java.util.Objects;

/**
 * ServerPropertiesRequestBody is the request body to add the basic server properties to an OMAG Server's configuration document.
 */
public class BasicServerProperties
{
    private String localServerDescription = null;
    private String organizationName       = null;
    private String localServerURL         = null;
    private String localServerUserId      = null;
    private String localServerPassword    = null;
    private int    maxPageSize            = OMAGServerConfig.defaultMaxPageSize;


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
        if (template != null)
        {
            localServerDescription          = template.getLocalServerDescription();
            organizationName                = template.getOrganizationName();
            localServerURL                  = template.getLocalServerURL();
            localServerUserId               = template.getLocalServerUserId();
            localServerPassword             = template.getLocalServerPassword();
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
     * Return the password that the local server should use on outbound REST calls (this is the password for
     * the localServerUserId).  Using userId's and passwords for server authentication is not suitable
     * for production environments.
     *
     * @return password in clear
     */
    public String getLocalServerPassword()
    {
        return localServerPassword;
    }


    /**
     * Set up the password that the local server should use on outbound REST calls (this is the password for
     * the localServerUserId).  Using userId's and passwords for server authentication is not suitable
     * for production environments.
     *
     * @param localServerPassword password in clear
     */
    public void setLocalServerPassword(String localServerPassword)
    {
        this.localServerPassword = localServerPassword;
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
        return "ServerPropertiesRequestBody{" +
                       "localServerDescription='" + localServerDescription + '\'' +
                       ", organizationName='" + organizationName + '\'' +
                       ", localServerURL='" + localServerURL + '\'' +
                       ", localServerUserId='" + localServerUserId + '\'' +
                       ", localServerPassword='" + localServerPassword + '\'' +
                       ", maxPageSize=" + maxPageSize +
                       '}';
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
                       Objects.equals(localServerPassword, that.localServerPassword);
    }


    /**
     * Return a hash code based on the values of this object.
     *
     * @return in hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(localServerDescription, organizationName, localServerURL, localServerUserId, localServerPassword, maxPageSize);
    }
}
