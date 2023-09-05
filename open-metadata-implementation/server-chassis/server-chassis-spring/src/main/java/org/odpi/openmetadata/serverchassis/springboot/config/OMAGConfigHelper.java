/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.serverchassis.springboot.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.io.Files;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.odpi.openmetadata.adapters.repositoryservices.ConnectorConfigurationFactory;
import org.odpi.openmetadata.adminservices.configuration.properties.OMAGServerConfig;
import org.odpi.openmetadata.repositoryservices.admin.OMRSConfigurationFactory;
import org.odpi.openmetadata.serverchassis.springboot.constants.SupportedConfigTypes;
import org.odpi.openmetadata.serverchassis.springboot.exception.OMAGServerActivationError;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.yaml.snakeyaml.Yaml;

import java.io.IOException;
import java.util.Objects;


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
    ConnectorConfigurationFactory connectorConfigurationFactory;
    OMRSConfigurationFactory omrsConfigurationFactory;

    @Autowired
    public OMAGConfigHelper(OMAGServerProperties properties,
                            @Qualifier("jsonObjectMapper") ObjectMapper jsonObjectMapper,
                            @Qualifier("yamlObjectMapper") ObjectMapper yamlObjectMapper,
                            ConnectorConfigurationFactory connectorConfigurationFactory,
                            OMRSConfigurationFactory omrsConfigurationFactory) {
        this.serverProperties = properties;
        this.jsonObjectMapper = jsonObjectMapper;
        this.yamlObjectMapper = yamlObjectMapper;
        this.connectorConfigurationFactory = connectorConfigurationFactory;
        this.omrsConfigurationFactory = omrsConfigurationFactory;
    }

    /**
     * Method implementing logic for deciding proper configuration source
     * and loading its content into configuration object
     * @throws OMAGServerActivationError
     */
    public void loadConfig() throws OMAGServerActivationError {

        try {
            if (isConfigurationFileProvided()) {

                Resource serverConfigFile = serverProperties.getServerConfigFile();

                log.info("Configuration {}", serverConfigFile);

                if (isJsonConfigurationFile(serverConfigFile)) {
                    omagServerConfig = jsonObjectMapper.reader().readValue(serverConfigFile.getInputStream(), OMAGServerConfig.class);
                } else if (isYamlConfigurationFile(serverConfigFile)) {
                    omagServerConfig = yamlObjectMapper.reader().readValue(serverConfigFile.getInputStream(), OMAGServerConfig.class);
                } else if (isYmlConfigurationFile(serverConfigFile)) {
                    Yaml yaml = new Yaml();
                    omagServerConfig = yaml.loadAs(serverConfigFile.getInputStream(), OMAGServerConfig.class);
                } else {
                    throw new OMAGServerActivationError("Configuration file is not supported");
                }

            } else if (isPropertiesConfiguration()) {

                log.info("[EXPERIMENTAL] Configuring server using omag. application properties");
                omagServerConfig = serverProperties.getServerConfig();
            }

        } catch (IOException e) {
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
        return SupportedConfigTypes.JSON.name().equalsIgnoreCase(Objects.requireNonNull(getFileExtension(serverConfigFile)));
    }
    private boolean isYamlConfigurationFile(Resource serverConfigFile) {
        return SupportedConfigTypes.YAML.name().equalsIgnoreCase(Objects.requireNonNull(getFileExtension(serverConfigFile)));
    }
    private boolean isYmlConfigurationFile(Resource serverConfigFile) {
        return SupportedConfigTypes.YML.name().equalsIgnoreCase(Objects.requireNonNull(getFileExtension(serverConfigFile)));
    }

    private boolean isPropertiesConfiguration() {
        return serverProperties.getServerConfig() != null;
    }

}
