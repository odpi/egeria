/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.viewservices.dino.api.properties;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.adminservices.configuration.properties.ResourceEndpointConfig;
import org.odpi.openmetadata.commonservices.ffdc.rest.RegisteredOMAGService;

import java.util.List;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class PlatformOverview {

    private String platformName;
    private String platformRootURL;    // e.g. "https://localhost:9443"
    private String description;
    private String platformOrigin;

    private List<RegisteredOMAGService> accessServices;
    private List<RegisteredOMAGService> commonServices;
    private List<RegisteredOMAGService> governanceServices;
    private List<RegisteredOMAGService> viewServices;
    private List<RegisteredOMAGService> engineServices;
    private List<RegisteredOMAGService> integrationServices;

    /**
     * Default Constructor sets the properties to nulls
     */
    public PlatformOverview()
    {
        /*
         * Nothing to do.
         */
    }


    public PlatformOverview(String platformName, String description, String platformRootURL, String platformOrigin) {

        this.platformName        = platformName;
        this.description         = description;
        this.platformRootURL     = platformRootURL;
        this.platformOrigin      = platformOrigin;
    }


    /*
     * Config constructor - create a skeletal PlatformOverview from a ResourceEndpointConfig
     */
    public PlatformOverview(ResourceEndpointConfig cfg) {

        this.platformName         = cfg.getPlatformName();
        this.description         = cfg.getDescription();
        this.platformRootURL     = cfg.getPlatformRootURL();
    }


    public String getPlatformName() {
        return platformName;
    }

    public void setPlatformName(String platformName) {
        this.platformName = platformName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPlatformRootURL() {
        return platformRootURL;
    }

    public void setPlatformRootURL(String platformRootURL) {
        this.platformRootURL = platformRootURL;
    }

    public String getPlatformOrigin() {
        return platformOrigin;
    }

    public void setPlatformOrigin(String platformOrigin) {
        this.platformOrigin = platformOrigin;
    }

    public List<RegisteredOMAGService> getAccessServices() {
        return accessServices;
    }

    public void setAccessServices(List<RegisteredOMAGService> accessServices) {
        this.accessServices = accessServices;
    }

    public List<RegisteredOMAGService> getCommonServices() {
        return commonServices;
    }

    public void setCommonServices(List<RegisteredOMAGService> commonServices) {
        this.commonServices = commonServices;
    }

    public List<RegisteredOMAGService> getGovernanceServices() {
        return governanceServices;
    }

    public void setGovernanceServices(List<RegisteredOMAGService> governanceServices) { this.governanceServices = governanceServices; }

    public List<RegisteredOMAGService> getViewServices() {
        return viewServices;
    }

    public void setViewServices(List<RegisteredOMAGService> viewServices) {
        this.viewServices = viewServices;
    }

    public List<RegisteredOMAGService> getEngineServices() {
        return engineServices;
    }

    public void setEngineServices(List<RegisteredOMAGService> engineServices) {
        this.engineServices = engineServices;
    }

    public List<RegisteredOMAGService> getIntegrationServices() {
        return integrationServices;
    }

    public void setIntegrationServices(List<RegisteredOMAGService> integrationServices) {
        this.integrationServices = integrationServices;
    }
}
