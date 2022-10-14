/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.userinterface.uichassis.springboot.service.view;

import org.odpi.openmetadata.adminservices.configuration.properties.IntegrationViewServiceConfig;
import org.odpi.openmetadata.adminservices.configuration.properties.ResourceEndpointConfig;
import org.odpi.openmetadata.adminservices.configuration.registration.ServiceOperationalStatus;
import org.odpi.openmetadata.adminservices.configuration.registration.ViewServiceAdmin;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class IntegrationViewServiceConfigComponent extends IntegrationViewServiceConfig implements InitializingBean {

    @Value("${viewServiceFullName}")
    private String omagServiceFullName;

    @Value("${viewServiceUrlMarker}")
    private String urlMarker;

    @Autowired
    ResourceEndpointConfigComponent resourceEndpointConfigComponent;

    @Override
    public void afterPropertiesSet() throws Exception {
        setViewServiceOperationalStatus(ServiceOperationalStatus.ENABLED);
        setViewServiceAdminClass(ViewServiceAdmin.class.getName());

        setViewServiceFullName( omagServiceFullName );
        setViewServiceURLMarker( urlMarker );

        setResourceEndpoints(
                resourceEndpointConfigComponent.getResourceEndpointConfigs()
        );

    }
}
