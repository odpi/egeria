/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.serverchassis.springboot.config;

import org.odpi.openmetadata.serveroperations.server.OMAGServerOperationalServices;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;


/**
 * Provides configuration supporting OMAG related components required by the application.
 */
@Configuration
public class OMAGServicesConfiguration {

    /**
     * Provides singleton bean instance of OMAGServerOperationalServices
     *
     * @return OMAGServerOperationalServices instance
     * @see OMAGServerOperationalServices
     */
    @Primary
    @Bean(name = {"serverOperationalServices"})
    public OMAGServerOperationalServices serverOperationalServices(){
        return new OMAGServerOperationalServices();
    }
}
