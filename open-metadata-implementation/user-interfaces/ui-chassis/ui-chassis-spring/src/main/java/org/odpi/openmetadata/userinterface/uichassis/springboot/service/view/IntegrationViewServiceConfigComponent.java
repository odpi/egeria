/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.userinterface.uichassis.springboot.service.view;

import org.odpi.openmetadata.adminservices.configuration.properties.IntegrationViewServiceConfig;
import org.odpi.openmetadata.adminservices.configuration.properties.ResourceEndpointConfig;
import org.odpi.openmetadata.adminservices.configuration.registration.ServiceOperationalStatus;
import org.odpi.openmetadata.adminservices.configuration.registration.ViewServiceAdmin;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class IntegrationViewServiceConfigComponent extends IntegrationViewServiceConfig implements InitializingBean {

    @Value("${view-service.remote.server.name}")
    private String remoteServerName;

    @Value("${view-service.remote.server.instance.name}")
    private String remoteServerInstanceName;

    @Value("${view-service.remote.platform.url}")
    private String remotePlatformURL;

    @Value("${view-service.server.name}")
    private String omagServerName;

    @Value("${view-service.service.name}")
    private String omagServiceName;

    @Value("${view-service.service.full.name}")
    private String omagServiceFullName;

    @Value("${view-service.url-marker}")
    private String urlMarker;


    static final String PLATFORM_NAME = "";
    static final String PLATFORM_RESOURCE_CATEGORY = "Platform";
    static final String SERVER_RESOURCE_CATEGORY = "Server";


    private ResourceEndpointConfig getPlatformResourceEndpointConfig(){
        ResourceEndpointConfig resourceEndpointConfig = new ResourceEndpointConfig();
        resourceEndpointConfig.setServerName(remoteServerName);
        resourceEndpointConfig.setPlatformRootURL(remotePlatformURL);
        resourceEndpointConfig.setResourceCategory(PLATFORM_RESOURCE_CATEGORY);
        resourceEndpointConfig.setPlatformName(PLATFORM_NAME);
        return resourceEndpointConfig;
    }

    private ResourceEndpointConfig getServerResourceEndpointConfig() {
        ResourceEndpointConfig resourceEndpointConfig = new ResourceEndpointConfig();
        resourceEndpointConfig.setServerInstanceName(remoteServerInstanceName);
        resourceEndpointConfig.setServerName(remoteServerName);
        resourceEndpointConfig.setPlatformRootURL(remotePlatformURL);
        resourceEndpointConfig.setResourceCategory(SERVER_RESOURCE_CATEGORY);
        resourceEndpointConfig.setPlatformName(PLATFORM_NAME);
        return resourceEndpointConfig;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        setViewServiceOperationalStatus(ServiceOperationalStatus.ENABLED);
        setViewServiceAdminClass(ViewServiceAdmin.class.getName());
        setOMAGServerPlatformRootURL( remotePlatformURL );
        setOMAGServerName( omagServerName );
        setViewServiceName(omagServiceName);
        setViewServiceFullName( omagServiceFullName );
        setViewServiceURLMarker( urlMarker );

        setResourceEndpoints(
                List.of(getPlatformResourceEndpointConfig(), getServerResourceEndpointConfig())
        );

    }
}
