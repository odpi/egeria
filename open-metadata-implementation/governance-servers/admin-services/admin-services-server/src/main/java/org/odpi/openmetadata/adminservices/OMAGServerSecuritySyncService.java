/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adminservices;

import org.odpi.openmetadata.adapters.repositoryservices.ConnectorConfigurationFactory;
import org.odpi.openmetadata.adminservices.configuration.properties.EventBusConfig;
import org.odpi.openmetadata.adminservices.configuration.properties.OMAGServerConfig;
import org.odpi.openmetadata.adminservices.configuration.properties.SecuritySyncConfig;
import org.odpi.openmetadata.adminservices.ffdc.exception.OMAGInvalidParameterException;
import org.odpi.openmetadata.adminservices.rest.VoidResponse;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class OMAGServerSecuritySyncService {

    private OMAGServerAdminStoreServices configStore = new OMAGServerAdminStoreServices();
    private OMAGServerErrorHandler errorHandler = new OMAGServerErrorHandler();

    private static final String defaultOutTopicName = "OutTopic";
    private static final String defaultInTopicName = "open-metadata.access-services.GovernanceEngine.outTopic";

    private static final String outputTopic = "open-metadata.security-sync.";
    private static final String defaultOutTopic = ".outTopic";

    public VoidResponse setSecuritySyncConfig(String userId, String serverName, SecuritySyncConfig securitySyncConfig) {
        String methodName = "setSecuritySyncConfig";
        VoidResponse response = new VoidResponse();

        try {
            OMAGServerConfig serverConfig = configStore.getServerConfig(serverName, methodName);

            List<String> configAuditTrail = serverConfig.getAuditTrail();

            if (configAuditTrail == null) {
                configAuditTrail = new ArrayList<>();
            }

            if (securitySyncConfig == null) {
                configAuditTrail.add(new Date().toString() + " " + userId + " removed configuration for security sync services.");
            } else {
                configAuditTrail.add(new Date().toString() + " " + userId + " updated configuration for security sync services.");
            }

            serverConfig.setAuditTrail(configAuditTrail);
            ConnectorConfigurationFactory connectorConfigurationFactory = new ConnectorConfigurationFactory();

            EventBusConfig eventBusConfig = serverConfig.getEventBusConfig();
            if(securitySyncConfig != null && securitySyncConfig.getSecuritySyncInTopicName() != null) {
                securitySyncConfig.setSecuritySyncInTopic(
                        connectorConfigurationFactory.getDefaultEventBusConnection(
                                defaultInTopicName,
                                eventBusConfig.getConnectorProvider(),
                                eventBusConfig.getTopicURLRoot() + ".server." + serverName,
                                securitySyncConfig.getSecuritySyncInTopicName(),
                                UUID.randomUUID().toString(),
                                eventBusConfig.getConfigurationProperties()));
            }

            if(securitySyncConfig != null && securitySyncConfig.getSecurityServerType() != null) {
                securitySyncConfig.setSecuritySyncOutTopic(
                        connectorConfigurationFactory.getDefaultEventBusConnection(defaultOutTopicName,
                                eventBusConfig.getConnectorProvider(),
                                eventBusConfig.getTopicURLRoot() + ".server." + serverName,
                                getOutputTopicName(securitySyncConfig.getSecurityServerType()),
                                serverConfig.getLocalServerId(),
                                eventBusConfig.getConfigurationProperties()));
            }

            serverConfig.setSecuritySyncConfig(securitySyncConfig);

            configStore.saveServerConfig(serverName, methodName, serverConfig);
        } catch (OMAGInvalidParameterException e) {
            errorHandler.captureInvalidParameterException(response, e);
        }
        return response;
    }

    private String getOutputTopicName(String securityServerType) {
        return outputTopic + securityServerType + defaultOutTopic;
    }

    public VoidResponse enableSecuritySyncService(String userId, String serverName) {

        final String methodName = "enableSecuritySyncService";
        VoidResponse response = new VoidResponse();

        try {
            OMAGServerConfig serverConfig = configStore.getServerConfig(serverName, methodName);
            SecuritySyncConfig securitySyncConfig = serverConfig.getSecuritySyncConfig();
            this.setSecuritySyncConfig(userId, serverName, securitySyncConfig);
        } catch (OMAGInvalidParameterException e) {
            errorHandler.captureInvalidParameterException(response, e);
        }
        return response;
    }

}
