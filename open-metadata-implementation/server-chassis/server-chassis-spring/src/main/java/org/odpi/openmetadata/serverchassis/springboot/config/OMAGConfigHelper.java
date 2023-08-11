package org.odpi.openmetadata.serverchassis.springboot.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import org.odpi.openmetadata.adapters.repositoryservices.ConnectorConfigurationFactory;
import org.odpi.openmetadata.adminservices.configuration.properties.LocalRepositoryConfig;
import org.odpi.openmetadata.adminservices.configuration.properties.OMAGServerConfig;
import org.odpi.openmetadata.adminservices.configuration.properties.RepositoryServicesConfig;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.Connection;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.ConnectorType;
import org.odpi.openmetadata.repositoryservices.admin.OMRSConfigurationFactory;
import org.odpi.openmetadata.serverchassis.springboot.exception.OMAGServerActivationError;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;


@EnableConfigurationProperties(OMAGServerProperties.class)
@Configuration
public class OMAGConfigHelper {

    @Getter final OMAGServerProperties serverProperties;
    @Getter OMAGServerConfig omagServerConfig;
    private static final Logger LOG = LoggerFactory.getLogger(OMAGConfigHelper.class);
    private final ObjectMapper mapper;
    ConnectorConfigurationFactory connectorConfigurationFactory;
    OMRSConfigurationFactory omrsConfigurationFactory;
    @Autowired
    public OMAGConfigHelper(OMAGServerProperties properties, ObjectMapper objectMapper, ConnectorConfigurationFactory connectorConfigurationFactory, OMRSConfigurationFactory omrsConfigurationFactory) {
        this.serverProperties = properties;
        this.mapper = objectMapper;
        this.connectorConfigurationFactory = connectorConfigurationFactory;
        this.omrsConfigurationFactory = omrsConfigurationFactory;
    }


    public void loadConfig() throws OMAGServerActivationError {

        try {

            if (serverProperties.getJsonConfigFile() == null) {

                LOG.info("JSON configuration file not used. Configuring server using omag. application properties");

                omagServerConfig = new OMAGServerConfig();

                omagServerConfig.setLocalServerName(serverProperties.getServer().getName());
                omagServerConfig.setLocalServerUserId(serverProperties.getServer().getUser());
                omagServerConfig.setMaxPageSize(serverProperties.getServer().getMaxPageSize());

                if(serverProperties.getServer().getRepositoryServices() != null) {
                    RepositoryServicesConfig repositoryServicesConfig = omrsConfigurationFactory.getDefaultRepositoryServicesConfig();
                    if (serverProperties.getServer().getRepositoryServices().getAuditLogConnectors() != null) {

                        List<Connection> auditLogConnectors = serverProperties.getServer().getRepositoryServices().getAuditLogConnectors().stream().map(o -> {
                            Connection c = new Connection();
                            ConnectorType ct = new ConnectorType();
                            ct.setConnectorProviderClassName(o.getProviderClassName());
                            c.setConnectorType(ct);
                            //TODO: Fix map/list binding problem. The type of supportedSeverities in configurationProperties should be resolved to List instead of Map!!!
                            c.setConfigurationProperties(o.getConfigurationProperties());
                            return c;
                        }).collect(Collectors.toList());

                        repositoryServicesConfig.setAuditLogConnections(auditLogConnectors);
                    }
                    //TODO: Work in progress currently using default in-meme local repo.
                    LocalRepositoryConfig localRepositoryConfig = getLocalRepositoryConfig();
                    repositoryServicesConfig.setLocalRepositoryConfig(localRepositoryConfig);
                    omagServerConfig.setRepositoryServicesConfig(repositoryServicesConfig);
                }


            } else {

                LOG.info("Configuration {}", serverProperties.getJsonConfigFile());
                omagServerConfig = mapper.reader().readValue(serverProperties.getJsonConfigFile().getInputStream(), OMAGServerConfig.class);
                LOG.info("Configuration document for server: {} - loaded successfully", omagServerConfig.getLocalServerName());

            }


        } catch (IOException e) {
            LOG.info("Configuration document cannot be loaded from the resource provided - check application configuration");
            throw new OMAGServerActivationError(
                    String.format("Configuration document cannot be loaded from the resource provided - check application configuration"), e);
        }

    }

    private LocalRepositoryConfig getLocalRepositoryConfig() {
        LocalRepositoryConfig localRepositoryConfig = new LocalRepositoryConfig();
        localRepositoryConfig.setEventsToSendRule(getServerProperties().getServer().getRepositoryServices().getLocalRepository().getEventsToSend());
        localRepositoryConfig.setEventsToSaveRule(getServerProperties().getServer().getRepositoryServices().getLocalRepository().getEventsToSave());
        localRepositoryConfig.setMetadataCollectionId(getServerProperties().getServer().getRepositoryServices().getLocalRepository().getMetadataCollectionId());
        localRepositoryConfig.setLocalRepositoryLocalConnection(connectorConfigurationFactory.getInMemoryLocalRepositoryLocalConnection());
        return localRepositoryConfig;
    }

}
