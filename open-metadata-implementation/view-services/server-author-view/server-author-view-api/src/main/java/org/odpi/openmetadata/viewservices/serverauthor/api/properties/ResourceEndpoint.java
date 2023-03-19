/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.viewservices.serverauthor.api.properties;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.adminservices.configuration.properties.ResourceEndpointConfig;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
/**
 * Resource endpoint defines a resource for the Server author.
 */
public class ResourceEndpoint {

    private static final long    serialVersionUID = 1L;


    private String resourceCategory;
    private String resourceDescription;
    private String resourceRootURL;    // e.g. "https://localhost:9443"
    private String serverName;
    private String platformName;
    private String serverInstanceName;

    /**
     * Default Constructor sets the properties to nulls
     */
    public ResourceEndpoint()
    {
        /*
         * Nothing to do.
         */
    }

    /**
     * Constructor
     * @param resourceCategory resource category
     * @param platformName platform name
     * @param resourceDescription resource description
     * @param resourceRootURL resource root URL
     * @param serverName server anme
     * @param serverInstanceName server instance name
     */
    public ResourceEndpoint(String resourceCategory, String platformName, String resourceDescription, String resourceRootURL, String serverName, String serverInstanceName) {
        this.resourceCategory    = resourceCategory;
        this.resourceDescription = resourceDescription;
        this.resourceRootURL     = resourceRootURL;
        this.serverName          = serverName;
        this.platformName        = platformName;
        this.serverInstanceName  = serverInstanceName;
    }


    /**
     * Config constructor - create a ResourceEndpoint from a ResourceEndpointConfig
     * @param cfg resource end point configuration
     */
    public ResourceEndpoint(ResourceEndpointConfig cfg) {
        this.resourceCategory    = cfg.getResourceCategory();
        this.resourceDescription = cfg.getDescription();
        this.resourceRootURL     = cfg.getPlatformRootURL();
        this.serverName          = cfg.getServerName();
        this.platformName        = cfg.getPlatformName();
        this.serverInstanceName  = cfg.getServerInstanceName();
    }

    /**
     * get the resource category
     * @return the resource caegory
     */
    public String getResourceCategory() {
        return resourceCategory;
    }

    /**
     * Set the resource category
     * @param resourceCategory the resource category
     */
    public void setResourceCategory(String resourceCategory) {
        this.resourceCategory = resourceCategory;
    }

    /**
     * Get the resource description
     * @return resource description
     */
    public String getResourceDescription() {
        return resourceDescription;
    }

    /**
     * Set the resource description
     * @param resourceDescription resource description
     */
    public void setResourceDescription(String resourceDescription) {
        this.resourceDescription = resourceDescription;
    }

    /**
     * get the resource root URL
     * @return resource root URL
     */
    public String getResourceRootURL() {
        return resourceRootURL;
    }

    /**
     * set the resource root URL
     * @param resourceRootURL resource root URL
     */
    public void setResourceRootURL(String resourceRootURL) {
        this.resourceRootURL = resourceRootURL;
    }

    /**
     * get the server anme
     * @return server name
     */


    public String getServerName() {
        return serverName;
    }

    /**
     * Set the server name
     * @param serverName server name
     */
    public void setServerName(String serverName) {
        this.serverName = serverName;
    }

    /**
     * Get the platform name
     * @return platform name
     */
    public String getPlatformName() {
        return platformName;
    }

    /**
     * Set a meaningful name for the platform
     * @param platformName set platform name
     */
    public void setPlatformName(String platformName) {
        this.platformName = platformName;
    }

    /**
     * Get server instance name
     * @return server instance name
     */
    public String getServerInstanceName() {
        return serverInstanceName;
    }

    /**
     * set server instance name
     * @param serverInstanceName name of server instance
     */
    public void setServerInstanceName(String serverInstanceName) {
        this.serverInstanceName = serverInstanceName;
    }
    @Override
    public String toString() {
        return toString(new StringBuilder()).toString();
    }
    /**
     * toString builder
     * @param sb StringBuffer to build details in
     * @return updated StringBuffer
     */
    public StringBuilder toString(StringBuilder sb) {
        if (sb == null) {
            sb = new StringBuilder();
        }
        sb.append("resourceEndPoint=");
        sb.append("{");
        sb.append("resourceCategory=").append(this.resourceCategory).append(",");
        sb.append("resourceDescription=").append(this.resourceDescription).append(",");
        sb.append("resourceRootURL=").append(this.resourceRootURL).append(",");
        sb.append("serverName=").append(this.serverName).append(",");
        sb.append("platformName=").append(this.platformName).append(",");
        sb.append("serverInstanceName=").append(this.serverInstanceName).append(",");
        sb.append('}');
        return sb;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        ResourceEndpoint resourceEndPoint = (ResourceEndpoint) o;

        return Objects.equals(resourceCategory, resourceEndPoint.resourceCategory) &&
                Objects.equals(resourceDescription, resourceEndPoint.resourceDescription) &&
                Objects.equals(resourceRootURL, resourceEndPoint.resourceRootURL) &&
                Objects.equals(serverName, resourceEndPoint.serverName) &&
                Objects.equals(platformName, resourceEndPoint.platformName) &&
                Objects.equals(serverName, resourceEndPoint.resourceRootURL) &&
                Objects.equals(serverInstanceName, resourceEndPoint.serverInstanceName)
                ;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(),resourceCategory, resourceDescription, resourceRootURL, platformName, serverName, serverInstanceName );
    }
}
