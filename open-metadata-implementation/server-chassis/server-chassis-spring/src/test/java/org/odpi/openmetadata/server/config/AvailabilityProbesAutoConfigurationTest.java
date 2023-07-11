package org.odpi.openmetadata.server.config;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.boot.autoconfigure.availability.ApplicationAvailabilityAutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionOutcome;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.annotation.MergedAnnotations;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.type.AnnotatedTypeMetadata;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.*;

@ContextConfiguration(classes = {
        AvailabilityProbesAutoConfiguration.class,
        AvailabilityProbesAutoConfiguration.ProbesCondition.class,
        ApplicationAvailabilityAutoConfiguration.class})
@ExtendWith(SpringExtension.class)
class AvailabilityProbesAutoConfigurationTest {

    //fixme I am not complete!

    @Autowired
    private final Environment env;
    @Autowired
    AvailabilityProbesAutoConfiguration.ProbesCondition probesCondition;
    ConditionContext context;
    AnnotatedTypeMetadata metadata;

    @Autowired
    AvailabilityProbesAutoConfigurationTest(Environment env) {
        this.env = env;
    }

    @BeforeEach
    void setUp() {
        context = new ConditionContext() {
            @Override
            public BeanDefinitionRegistry getRegistry() {
                return null;
            }

            @Override
            public ConfigurableListableBeanFactory getBeanFactory() {
                return null;
            }

            @Override
            public Environment getEnvironment() {
                return env;
            }

            @Override
            public ResourceLoader getResourceLoader() {
                return null;
            }

            @Override
            public ClassLoader getClassLoader() {
                return null;
            }
        };
        metadata = new AnnotatedTypeMetadata() {
            @Override
            public MergedAnnotations getAnnotations() {
                return null;
            }
        };
    }

    @Test
    void livenessStateHealthIndicator() {

        //fixme I am not complete!
        ConditionOutcome matchOutcome = probesCondition.getMatchOutcome(context, metadata);

        assertNotNull(matchOutcome);

        assertInstanceOf(ConditionOutcome.class, matchOutcome);

        assertEquals("Health availability not running on a kubernetes cloud platform",
                matchOutcome.getMessage());
    }
}