/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.userinterface.uichassis.springboot.service.view;

import org.odpi.openmetadata.adminservices.configuration.properties.ResourceEndpointConfig;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@ConfigurationProperties(prefix = "view-services-resource-endpoints")
public class ResourceEndpointConfigComponent {

    private List<ResourceEndpointConfig> resourceEndpointConfigs;

    public void setResourceEndpointConfigs(List<ResourceEndpointConfig> resourceEndpointConfigs) {
        this.resourceEndpointConfigs = resourceEndpointConfigs;
    }

    public List<ResourceEndpointConfig> getResourceEndpointConfigs() {
        return resourceEndpointConfigs;
    }
}
