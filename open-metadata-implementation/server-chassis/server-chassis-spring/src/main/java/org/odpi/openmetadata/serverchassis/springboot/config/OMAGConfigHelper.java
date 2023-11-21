/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.serverchassis.springboot.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.InvalidTypeIdException;
import com.google.common.io.Files;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.odpi.openmetadata.adminservices.configuration.properties.OMAGServerConfig;
import org.odpi.openmetadata.serverchassis.springboot.exception.OMAGServerActivationError;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.yaml.snakeyaml.Yaml;

import java.util.Objects;

/**
 *  This class provides support for loading OMAGServerConfig document from different configuration options/styles.
 *  It supports JSON and YAML file based OMAG server configuration;
 *  Additionally provides EXPERIMENTAL feature: configure OMAG server using native spring configuration properties.
 */
@EnableConfigurationProperties(OMAGServerProperties.class)
@Configuration
@Slf4j
public class OMAGConfigHelper {

    @Getter
    final OMAGServerProperties serverProperties;
    private final ObjectMapper jsonObjectMapper;
    private final ObjectMapper yamlObjectMapper;
    @Getter
    OMAGServerConfig omagServerConfig;
    static final String YAML = "yaml";
    static final String YML = "yml";
    static final String JSON = "json";

    /**
     *  Constructor that injects required beans such as omag application properties and jackson object mappers.
     *
     * @param properties application properties prefixed with 'omag.'
     * @param jsonObjectMapper pre-configured object mapper bean for json processing
     * @param yamlObjectMapper pre-configured object mapper bean for yaml processing
     */
    @Autowired
    public OMAGConfigHelper(OMAGServerProperties properties,
                            @Qualifier("jsonObjectMapper") ObjectMapper jsonObjectMapper,
                            @Qualifier("yamlObjectMapper") ObjectMapper yamlObjectMapper) {
        this.serverProperties = properties;
        this.jsonObjectMapper = jsonObjectMapper;
        this.yamlObjectMapper = yamlObjectMapper;
    }

    /**
     * Implements logic for deciding proper configuration source
     * and loading its content into OMAGServerConfig configuration object
     *
     * @throws OMAGServerActivationError
     */
    public void loadConfig() throws OMAGServerActivationError {

        try {
            if (isConfigurationFileProvided()) {

                Resource serverConfigFile = serverProperties.getServerConfigFile();

                log.info("Using configuration from {}", serverConfigFile);

                if (isJsonConfigurationFile(serverConfigFile)) {
                    // Read json based omag server configuration file
                    log.debug("JSON file detected, reading values using jsonObjectMapper...");
                    omagServerConfig = jsonObjectMapper.reader().readValue(serverConfigFile.getInputStream(), OMAGServerConfig.class);
                } else if (isYamlConfigurationFile(serverConfigFile)) {
                    try {
                        // Read yaml based omag server configuration file
                        log.debug("YAML file detected, reading values using yamlObjectMapper...");
                        omagServerConfig = yamlObjectMapper.reader().readValue(serverConfigFile.getInputStream(), OMAGServerConfig.class);
                    } catch (InvalidTypeIdException e) {
                        log.debug("yamlObjectMapper failed reading values, trying snakeYaml...");
                        Yaml yaml = new Yaml();
                        omagServerConfig = yaml.loadAs(serverConfigFile.getInputStream(), OMAGServerConfig.class);
                    }
                } else {
                    throw new OMAGServerActivationError("Configuration file is not supported");
                }

            } else if (isPropertiesConfiguration()) {
                log.info("[EXPERIMENTAL] Configuring server using omag. application properties");
                omagServerConfig = serverProperties.getServerConfig();
                //TODO: Continue investigation
                // At this point we should have property mapped OMAGServerConfig object.
                // However, due to internal spring yaml to java object marshaling logic, Connection beans get wrongly typed Map for configurationProperties:
                // Our code expects Map<String, List> but gets Map<String, LinkedHashMap> -- this leads to wrong connector configuration compromising the connector functionality.
                // Reference to the problematic code https://github.com/spring-projects/spring-framework/blob/main/spring-beans/src/main/java/org/springframework/beans/factory/config/YamlProcessor.java#L330C11-L330C52
                // The same mapping problem is described in spring-boot issue https://github.com/spring-projects/spring-boot/issues/6180
            }

        } catch (Exception e) {
            log.info("Configuration document cannot be loaded from the resource provided - check application configuration");
            throw new OMAGServerActivationError(
                    "Configuration document cannot be loaded from the resource provided - check application configuration", e);
        }
    }

    private String getFileExtension(Resource serverConfigFile) {
        return Files.getFileExtension(Objects.requireNonNull(serverConfigFile.getFilename()));
    }

    private boolean isConfigurationFileProvided() {
        return serverProperties.getServerConfigFile() != null;
    }

    private boolean isJsonConfigurationFile(Resource serverConfigFile) {
        return JSON.equalsIgnoreCase(Objects.requireNonNull(getFileExtension(serverConfigFile)));
    }
    private boolean isYamlConfigurationFile(Resource serverConfigFile) {
        String fileExtension = Objects.requireNonNull(getFileExtension(serverConfigFile));
        return YAML.equalsIgnoreCase(fileExtension) || YML.equalsIgnoreCase(fileExtension);
    }

    private boolean isPropertiesConfiguration() {
        return serverProperties.getServerConfig() != null;
    }

}
