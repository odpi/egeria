/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.server.config;


import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

/**
 * This class provides configuration bean for customizing Jackson object mapper singleton instance configuration.
 */
@Configuration
public class ObjectMapperConfiguration {
    public static final String PREFIX = "Object mapper configuration started.";
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    public static ObjectMapper newObjectMapper(Jackson2ObjectMapperBuilder builder) {

        return builder
                .serializationInclusion(NON_NULL)
                .failOnEmptyBeans(false)
                .failOnUnknownProperties(false)
                .featuresToEnable(
                        MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES,
                        DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY)
                .featuresToDisable(
                        SerializationFeature.WRITE_DATES_AS_TIMESTAMPS,
                        SerializationFeature.WRITE_SINGLE_ELEM_ARRAYS_UNWRAPPED)
                .build();
    }

    @Bean(name = {"objectMapper"})
    @Primary
    ObjectMapper objectMapper(Jackson2ObjectMapperBuilder builder) {

        log.debug(PREFIX);

        return newObjectMapper(builder);
    }
}

