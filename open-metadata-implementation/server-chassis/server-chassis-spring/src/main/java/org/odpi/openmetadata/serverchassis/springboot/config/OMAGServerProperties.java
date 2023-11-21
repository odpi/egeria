/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.serverchassis.springboot.config;

import lombok.Getter;
import lombok.Setter;
import org.odpi.openmetadata.adminservices.configuration.properties.OMAGServerConfig;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.core.io.Resource;
import org.springframework.validation.annotation.Validated;

/**
 *  Provides validation support for OMAG specific application properties.
 */
@ConfigurationProperties(prefix = "omag", ignoreUnknownFields=false)
@Getter
@Setter
@Validated
public class OMAGServerProperties {

    /**
     * Configures the location of the OMAGServerConfig json document defined as org.springframework.core.io.Resource
     * This property is required and cannot be null.
     */
//    @NotNull
    private Resource serverConfigFile;
    /**
     * Application property that maps to OMAGServerConfig document directly.
     * USED ONLY TO EXPERIMENT DUE TO UNDERLYING SPRING YAML CONFIGURATION PROCESSING BEHAVIOUR THAT COMPROMISES THE CURRENT FUNCTIONALITY.
     */
    private OMAGServerConfig serverConfig;

}
