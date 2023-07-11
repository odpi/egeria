/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.server.config;

import org.springframework.boot.actuate.autoconfigure.health.ConditionalOnEnabledHealthIndicator;
import org.springframework.boot.actuate.availability.LivenessStateHealthIndicator;
import org.springframework.boot.actuate.availability.ReadinessStateHealthIndicator;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.availability.ApplicationAvailabilityAutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionMessage;
import org.springframework.boot.autoconfigure.condition.ConditionOutcome;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.SpringBootCondition;
import org.springframework.boot.availability.ApplicationAvailability;
import org.springframework.boot.cloud.CloudPlatform;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.core.type.AnnotatedTypeMetadata;

@Configuration(proxyBeanMethods = false)
@Conditional(AvailabilityProbesAutoConfiguration.ProbesCondition.class)
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

    static class ProbesCondition extends SpringBootCondition {
        // fixme adjust this
        private static final String ENABLED_PROPERTY = "management.health.probes.enabled";

        @Override
        public ConditionOutcome getMatchOutcome(ConditionContext context, AnnotatedTypeMetadata metadata) {
            Environment environment = context.getEnvironment();
            ConditionMessage.Builder message = ConditionMessage.forCondition("Health availability");
            String enabled = environment.getProperty(ENABLED_PROPERTY);
            if (enabled != null) {
                boolean match = !"false".equalsIgnoreCase(enabled);
                return new ConditionOutcome(match,
                        message.because("'" + ENABLED_PROPERTY + "' set to '" + enabled + "'"));
            }
            if (CloudPlatform.getActive(environment) == CloudPlatform.KUBERNETES) {
                return ConditionOutcome.match(message.because("running on Kubernetes"));
            }
            return ConditionOutcome.noMatch(message.because("not running on a kubernetes cloud platform"));
        }

    }

}

