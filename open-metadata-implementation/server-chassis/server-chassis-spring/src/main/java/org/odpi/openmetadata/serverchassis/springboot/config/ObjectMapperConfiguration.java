/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.serverchassis.springboot.config;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.yaml.YAMLGenerator;
import com.fasterxml.jackson.dataformat.yaml.YAMLMapper;
import lombok.extern.slf4j.Slf4j;

import org.odpi.openmetadata.serverchassis.springboot.constants.Extensions;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.util.StringUtils;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

/**
 * This class provides configurations for customizing
 * json bean Jackson object mapper singleton instance configuration
 * and
 * yaml bean Jackson object mapper singleton instance configuration
 */
@Slf4j
@Configuration
public class ObjectMapperConfiguration {
    public static final String START_LOG_SUFIX = "ObjectMapper configuration started.";

    private static ObjectMapper newObjectMapper(Jackson2ObjectMapperBuilder jackson2ObjectMapperBuilder) {

        return jackson2ObjectMapperBuilder
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

    /**
     * @param jackson2ObjectMapperBuilder a builder used to create ObjectMapper instances with a fluent API.
     * @return JsonMapper a JSON-format specific ObjectMapper implementation
     */
    @Bean
    @Qualifier("jsonObjectMapper")
    ObjectMapper objectMapper(Jackson2ObjectMapperBuilder jackson2ObjectMapperBuilder) {

        log.info("{}{}", StringUtils.capitalize(Extensions.JSON.name()), START_LOG_SUFIX);

        return newObjectMapper(jackson2ObjectMapperBuilder);
    }

    /**
     * @return YAMLMapper a convenience version of ObjectMapper which is configured with YAMLFactory
     */
    @Bean
    @Qualifier("yamlObjectMapper")
    public ObjectMapper yamlMapper() {

        log.info("{}{}", StringUtils.capitalize(Extensions.YAML.name()), START_LOG_SUFIX);

        return YAMLMapper.builder()
                .disable(YAMLGenerator.Feature.WRITE_DOC_START_MARKER)
                .disable(YAMLGenerator.Feature.ALLOW_LONG_KEYS)
                .serializationInclusion(JsonInclude.Include.NON_NULL)
                .configure(DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES, false)
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
                .enable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY)
                .enable(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES)
                .build();
    }
}