/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.serverchassis.springboot.config;

import lombok.Getter;
import lombok.Setter;
import org.odpi.openmetadata.adminservices.configuration.properties.OpenMetadataExchangeRule;
import org.odpi.openmetadata.adminservices.configuration.registration.ServerTypeClassification;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.core.io.Resource;
import org.springframework.validation.annotation.Validated;
import java.util.List;
import java.util.Map;

/**
 *  This class provides validation support for OMAG specific application properties.
 */
@ConfigurationProperties(prefix = "omag", ignoreUnknownFields=false)
@Getter
@Setter
@Validated
public class OMAGServerProperties {

    /**
     * Configures the location of the OMAGServerConfig json document defined as org.springframework.core.io.Resource
     * This property is required and cannot be null.
     */
//    @NotNull
    private Resource serverConfigFile;

    /**
     * Server bean represents simplified OMAG server configuration model used for YAML based application configuration.
     */
    private Server server;

    @Getter
    @Setter
    public static class Server {
        private String name;
        private ServerTypeClassification type;
        private String user;
        private int maxPageSize;
        private RepositoryServices repositoryServices;
    }

    @Getter
    @Setter
    public static class RepositoryServices {
        private List<Connector> auditLogConnectors;
        private LocalRepository localRepository;
    }

    @Getter
    @Setter
    public static class Connector {
        private String providerClassName;
        private Map<String, Object> configurationProperties;
        private Endpoint endpoint;
    }

    @Getter
    @Setter
    public static class LocalRepository {
        String metadataCollectionId;
        Connector localConnection;
        Connector remoteConnection;
        OpenMetadataExchangeRule eventsToSave;
        OpenMetadataExchangeRule eventsToSend;
    }

    @Getter
    @Setter
    public static class Endpoint {
        private String address;
    }

}
