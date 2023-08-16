/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.serverchassis.springboot.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.io.Files;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.odpi.openmetadata.adapters.repositoryservices.ConnectorConfigurationFactory;
import org.odpi.openmetadata.adminservices.configuration.properties.LocalRepositoryConfig;
import org.odpi.openmetadata.adminservices.configuration.properties.OMAGServerConfig;
import org.odpi.openmetadata.adminservices.configuration.properties.RepositoryServicesConfig;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.Connection;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.ConnectorType;
import org.odpi.openmetadata.repositoryservices.admin.OMRSConfigurationFactory;
import org.odpi.openmetadata.serverchassis.springboot.constants.Extensions;
import org.odpi.openmetadata.serverchassis.springboot.exception.OMAGServerActivationError;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;


@EnableConfigurationProperties(OMAGServerProperties.class)
@Configuration
@Slf4j
public class OMAGConfigHelper {
    public static final String WRONG_EXTENSION_MESSAGE = "Unallowed config file extension, " +
            "allowed config file extensions are: " + Extensions.stream();
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

                //TODO: This is POC implementation.
                // Better way to identify content type may be required.

                if (!isJsonConfigurationFile(serverConfigFile) && !isYamlConfigurationFile(serverConfigFile)) {
                    log.error(WRONG_EXTENSION_MESSAGE);
                    throw new OMAGServerActivationError(WRONG_EXTENSION_MESSAGE);
                }

                log.info("{} based configuration with server-config-file {}", getFileExtension(serverConfigFile), serverConfigFile);

                if (isJsonConfigurationFile(serverConfigFile)) {
                    omagServerConfig = jsonObjectMapper.reader()
                            .readValue(serverConfigFile.getInputStream(), OMAGServerConfig.class);

                } else if (isYamlConfigurationFile(serverConfigFile)) {
                    //TODO: This is POC implementation. We need better code to deal with yaml i.e. one option is to use Jackson and Yaml data format.
//                    Yaml yaml = new Yaml();
//                    omagServerConfig = yaml.loadAs(serverConfigFile.getInputStream(), OMAGServerConfig.class);

                    omagServerConfig = yamlObjectMapper.reader()
                            .readValue(serverConfigFile.getInputStream(), OMAGServerConfig.class);
                }

            } else if (isPropertiesConfiguration()) {

                log.info("Configuring server using omag. application properties [EXPERIMENTAL]");

                OMAGServerProperties.Server omagServer = serverProperties.getServer();

                omagServerConfig = new OMAGServerConfig();
                omagServerConfig.setLocalServerName(omagServer.getName());
                omagServerConfig.setLocalServerUserId(omagServer.getUser());
                omagServerConfig.setMaxPageSize(omagServer.getMaxPageSize());

                if (omagServer.getRepositoryServices() != null) {
                    RepositoryServicesConfig repositoryServicesConfig = omrsConfigurationFactory.getDefaultRepositoryServicesConfig();

                    if (omagServer.getRepositoryServices().getAuditLogConnectors() != null) {

                        repositoryServicesConfig.setAuditLogConnections(getAuditLogConnectors());
                    }
                    //TODO: Work in progress currently using default in-mem local repo.
                    LocalRepositoryConfig localRepositoryConfig = getLocalRepositoryConfig();
                    repositoryServicesConfig.setLocalRepositoryConfig(localRepositoryConfig);
                    omagServerConfig.setRepositoryServicesConfig(repositoryServicesConfig);
                }
            }

            log.info("Configuration document for server: {} - loaded successfully", omagServerConfig.getLocalServerName());

        } catch (IOException e) {
            log.info("Configuration document cannot be loaded from the resource provided - check application configuration");
            throw new OMAGServerActivationError(
                    String.format("Configuration document cannot be loaded from the resource provided - check application configuration"), e);
        }
    }

    private String getFileExtension(Resource serverConfigFile) {
        return Files.getFileExtension(serverConfigFile.getFilename());
    }

    private boolean isConfigurationFileProvided() {
        return serverProperties.getServerConfigFile() != null;
    }

    private boolean isJsonConfigurationFile(Resource serverConfigFile) {
        return Extensions.JSON.name().equalsIgnoreCase(Objects.requireNonNull(getFileExtension(serverConfigFile)));
    }

    private boolean isYamlConfigurationFile(Resource serverConfigFile) {
        return Extensions.YAML.name().equalsIgnoreCase(Objects.requireNonNull(getFileExtension(serverConfigFile)))
                || Extensions.YML.name().equalsIgnoreCase(Objects.requireNonNull(getFileExtension(serverConfigFile)));
    }

    private boolean isPropertiesConfiguration() {
        return serverProperties.getServer() != null;
    }

    private List<Connection> getAuditLogConnectors() {

        return serverProperties.getServer()
                .getRepositoryServices()
                .getAuditLogConnectors()
                .stream()
                .map(o -> getConnection(o))
                .collect(Collectors.toList());
    }

    private Connection getConnection(OMAGServerProperties.Connector o) {
        Connection c = new Connection();
        ConnectorType ct = new ConnectorType();
        ct.setConnectorProviderClassName(o.getProviderClassName());
        c.setConnectorType(ct);
        //TODO: Fix binding problem. The type of supportedSeverities in configurationProperties should be resolved to List instead of Map!!!
        c.setConfigurationProperties(o.getConfigurationProperties());
        return c;
    }

    private LocalRepositoryConfig getLocalRepositoryConfig() {
        LocalRepositoryConfig localRepositoryConfig = new LocalRepositoryConfig();

        OMAGServerProperties.LocalRepository localRepository =
                getServerProperties().getServer().getRepositoryServices().getLocalRepository();

        localRepositoryConfig.setEventsToSendRule(localRepository.getEventsToSend());
        localRepositoryConfig.setEventsToSaveRule(localRepository.getEventsToSave());
        localRepositoryConfig.setMetadataCollectionId(localRepository.getMetadataCollectionId());
        localRepositoryConfig.setLocalRepositoryLocalConnection(connectorConfigurationFactory.getInMemoryLocalRepositoryLocalConnection());
        return localRepositoryConfig;
    }

}
