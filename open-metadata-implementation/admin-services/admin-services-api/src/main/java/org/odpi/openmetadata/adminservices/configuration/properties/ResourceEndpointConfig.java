/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adminservices.configuration.properties;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serial;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * ResourceEndpointConfig defines the properties of a resource endpoint that an integration view service can connect to.
 *
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class ResourceEndpointConfig extends AdminServicesConfigHeader
{
    @Serial
    private static final long serialVersionUID = 1L;

    private String              resourceCategory       = null;  // should be set to { "Platform" | "Server" }
    private String              description            = null;  // a descriptive string that describes the endpoint - can be displayed to a UI user
    private String              platformRootURL        = null;  // the root URL of the platform - always "platform" whether the resource is a platform or server running on the platform
    private String              serverName             = null;  // always null for a platform, but for a server this MUST contain the real server name to use in URL construction
    private String              serverInstanceName     = null;  // always null for a platform, but for a server this MUST contain a unique name used as a handle for the server/platform combination
    private String              platformName           = null;  // always null for a server, but for a platform this MUST contain a unique name used as a handle for the platform

    /**
     * Default constructor
     */
    public ResourceEndpointConfig()
    {
        super();
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public ResourceEndpointConfig(ResourceEndpointConfig template)
    {
        super(template);

        if (template != null)
        {
            resourceCategory   = template.getResourceCategory();
            description        = template.getDescription();
            platformRootURL    = template.getPlatformRootURL();
            serverName         = template.getServerName();
            serverInstanceName = template.getServerInstanceName();
            platformName       = template.getPlatformName();

        }
    }


    /**
     * Return the category of resource - indicating whether the resource is a platform or server.
     *
     * @return resourceCategory
     */
    public String getResourceCategory()
    {
        return resourceCategory;
    }


    /**
     * Set the category of resource - indicating whether the resource is a platform or server.
     *
     * @param resourceCategory string value
     */
    public void setResourceCategory(String resourceCategory)
    {
        this.resourceCategory = resourceCategory;
    }


    /**
     * Return the description of resource.
     *
     * @return description
     */
    public String getDescription()
    {
        return description;
    }


    /**
     * Set the displayName of resource.
     *
     * @param description string value
     */
    public void setDescription(String description)
    {
        this.description = description;
    }


    /**
     * Return the platformRootURL of resource.
     *
     * @return platformRootURL
     */
    public String getPlatformRootURL()
    {
        return platformRootURL;
    }


    /**
     * Set the platformRootURL of resource.
     *
     * @param platformRootURL string value
     */
    public void setPlatformRootURL(String platformRootURL)
    {
        this.platformRootURL = platformRootURL;
    }


    /**
     * Return the serverName of resource.
     *
     * @return serverName
     */
    public String getServerName()
    {
        return serverName;
    }


    /**
     * Set the serverName of resource.
     *
     * @param serverName string value
     */
    public void setServerName(String serverName)
    {
        this.serverName = serverName;
    }

    /**
     * Return the serverInstanceName of resource.
     *
     * @return serverInstanceName
     */
    public String getServerInstanceName()
    {
        return serverInstanceName;
    }


    /**
     * Set the serverInstanceName of resource.
     *
     * @param serverInstanceName string value
     */
    public void setServerInstanceName(String serverInstanceName)
    {
        this.serverInstanceName = serverInstanceName;
    }


    /**
     * Return the platformName of resource.
     *
     * @return platformName
     */
    public String getPlatformName()
    {
        return platformName;
    }


    /**
     * Set the platformName of resource.
     *
     * @param platformName string value
     */
    public void setPlatformName(String platformName)
    {
        this.platformName = platformName;
    }


    /**
     * Standard toString method.
     *
     * @return JSON style description of variables.
     */
    @Override
    public String toString()
    {
        return "ResourceEndpointConfig{" +
                "resourceCategory='" + resourceCategory + '\'' +
                ", description='" + description + '\'' +
                ", platformRootURL=" + platformRootURL +
                ", serverName=" + serverName +
                ", serverInstanceName=" + serverInstanceName +
                ", platformName=" + platformName +
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
        if (objectToCompare == null || getClass() != objectToCompare.getClass())
        {
            return false;
        }
        ResourceEndpointConfig that = (ResourceEndpointConfig) objectToCompare;
        return Objects.equals(getResourceCategory(), that.getResourceCategory()) &&
                Objects.equals(getDescription(), that.getDescription()) &&
                Objects.equals(getPlatformRootURL(), that.getPlatformRootURL()) &&
                Objects.equals(getPlatformName(), that.getPlatformName()) &&
                Objects.equals(getServerInstanceName(), that.getServerInstanceName()) &&
                Objects.equals(getServerName(), that.getServerName());
    }


    /**
     * Return a hash code based on the values of this object.
     *
     * @return in hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(getResourceCategory(), getDescription(), getPlatformRootURL(), getServerName(), getServerInstanceName(), getPlatformName());
    }
}
