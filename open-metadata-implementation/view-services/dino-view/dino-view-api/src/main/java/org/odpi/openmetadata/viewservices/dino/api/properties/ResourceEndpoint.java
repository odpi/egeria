/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.viewservices.dino.api.properties;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.adminservices.configuration.properties.ResourceEndpointConfig;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
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


    public ResourceEndpoint(String resourceCategory, String platformName, String resourceDescription, String resourceRootURL, String serverName, String serverInstanceName) {
        this.resourceCategory    = resourceCategory;
        this.resourceDescription = resourceDescription;
        this.resourceRootURL     = resourceRootURL;
        this.serverName          = serverName;
        this.platformName        = platformName;
        this.serverInstanceName  = serverInstanceName;
    }


    /*
     * Config constructor - create a ResourceEndpoint from a ResourceEndpointConfig
     */
    public ResourceEndpoint(ResourceEndpointConfig cfg) {
        this.resourceCategory    = cfg.getResourceCategory();
        this.resourceDescription = cfg.getDescription();
        this.resourceRootURL     = cfg.getPlatformRootURL();
        this.serverName          = cfg.getServerName();
        this.platformName        = cfg.getPlatformName();
        this.serverInstanceName  = cfg.getServerInstanceName();
    }

    public String getResourceCategory() {
        return resourceCategory;
    }

    public void setResourceCategory(String resourceCategory) {
        this.resourceCategory = resourceCategory;
    }

    public String getResourceDescription() {
        return resourceDescription;
    }

    public void setResourceDescription(String resourceDescription) {
        this.resourceDescription = resourceDescription;
    }

    public String getResourceRootURL() {
        return resourceRootURL;
    }

    public void setResourceRootURL(String resourceRootURL) {
        this.resourceRootURL = resourceRootURL;
    }

    public String getServerName() {
        return serverName;
    }

    public void setServerName(String serverName) {
        this.serverName = serverName;
    }

    public String getPlatformName() {
        return platformName;
    }

    public void setPlatformName(String platformName) {
        this.platformName = platformName;
    }

    public String getServerInstanceName() {
        return serverInstanceName;
    }

    public void setServerInstanceName(String serverInstanceName) {
        this.serverInstanceName = serverInstanceName;
    }

}
