/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.serverchassis.springboot.config;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.odpi.openmetadata.adminservices.configuration.properties.OMAGServerConfig;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.core.io.Resource;
import org.springframework.validation.annotation.Validated;

/**
 *  This class provides validation support for OMAG specific application properties.
 */
@ConfigurationProperties(prefix = "omag")
@Getter
@Setter
@Validated
public class OMAGServerProperties {

    /**
     * Configures the location of the OMAGServerConfig json document defined as Spring Resource.
     * This property is required and cannot be null.
     */
    @NotNull
    private Resource serverConfig;

    /**
     * Configures the username parameter used to activate the OMAG server instance using platform operational services.
     * @see org.odpi.openmetadata.platformservices.server.OMAGServerOperationalServices#activateWithSuppliedConfig(String, String, OMAGServerConfig)
     * Default value is set to 'system', can be overwritted
     */
    private String serverUser = "system";

}
