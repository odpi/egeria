/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.server.config;

import org.springframework.boot.actuate.autoconfigure.health.ConditionalOnEnabledHealthIndicator;
import org.springframework.boot.actuate.availability.LivenessStateHealthIndicator;
import org.springframework.boot.actuate.availability.ReadinessStateHealthIndicator;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.availability.ApplicationAvailabilityAutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.availability.ApplicationAvailability;
import org.springframework.context.annotation.Bean;

import org.springframework.context.annotation.Configuration;


/**
 * This class provides configuration for Application Availability support components.
 */
@Configuration(proxyBeanMethods = false)
@AutoConfigureAfter(ApplicationAvailabilityAutoConfiguration.class)
public class AvailabilityProbesAutoConfiguration {

    @Bean
    @ConditionalOnEnabledHealthIndicator("livenessState")
    @ConditionalOnMissingBean
    public LivenessStateHealthIndicator livenessStateHealthIndicator(ApplicationAvailability applicationAvailability) {
        return new LivenessStateHealthIndicator(applicationAvailability);
    }

    @Bean
    @ConditionalOnEnabledHealthIndicator("readinessState")
    @ConditionalOnMissingBean
    public ReadinessStateHealthIndicator readinessStateHealthIndicator(
            ApplicationAvailability applicationAvailability) {
        return new ReadinessStateHealthIndicator(applicationAvailability);
    }

}

